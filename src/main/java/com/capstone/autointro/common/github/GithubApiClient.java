package com.capstone.autointro.common.github;

import com.capstone.autointro.common.exception.GeneralException;
import com.capstone.autointro.common.status.error.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class GithubApiClient {

    // 분석할 소스 파일 확장자
    private static final Set<String> SOURCE_EXTENSIONS = Set.of(
            ".java", ".kt", ".py", ".js", ".ts", ".go", ".rs",
            ".cpp", ".c", ".cs", ".rb", ".php", ".swift", ".xml",
            ".yml", ".yaml", ".gradle", ".properties"
    );

    // 제외할 디렉토리
    private static final Set<String> SKIP_DIRS = Set.of(
            "node_modules", ".git", "build", "target", "dist",
            "out", ".gradle", ".idea", "__pycache__", "vendor"
    );

    private static final int MAX_FILE_SIZE = 30_000;   // 파일 하나당 최대 30KB
    private static final int MAX_TOTAL_SIZE = 150_000; // 전체 파일 합계 최대 150KB
    private static final int MAX_FILES = 20;           // 최대 파일 수

    // fix/refactor 커밋 diff 관련 상수
    private static final int MAX_DIFF_COMMITS = 5;     // 최대 커밋 수
    private static final int MAX_PATCH_LENGTH = 500;   // 커밋당 patch 최대 500자
    private static final Set<String> FIX_PREFIXES = Set.of(
            "fix", "refactor", "bugfix", "hotfix", "bug", "수정", "버그", "개선"
    );

    private GitHub connect(String accessToken) {
        try {
            return new GitHubBuilder().withOAuthToken(accessToken).build();
        } catch (IOException e) {
            log.error("GitHub 연결 실패", e);
            throw new GeneralException(ErrorStatus.GITHUB_API_ERROR);
        }
    }

    /**
     * 해당 계정의 레포지토리 목록 조회
     */
    public List<GHRepository> getRepositories(String accessToken) {
        try {
            GitHub github = connect(accessToken);
            return github.getMyself().listRepositories().toList();
        } catch (IOException e) {
            log.error("레포지토리 목록 조회 실패", e);
            throw new GeneralException(ErrorStatus.GITHUB_API_ERROR);
        }
    }

    /**
     * 특정 레포지토리 조회 (owner/repoName 형식)
     */
    public GHRepository getRepository(String accessToken, String fullRepoName) {
        try {
            GitHub github = connect(accessToken);
            return github.getRepository(fullRepoName);
        } catch (IOException e) {
            log.error("레포지토리 조회 실패 - {}", fullRepoName, e);
            throw new GeneralException(ErrorStatus.PROJECT_NOT_FOUND);
        }
    }

    /**
     * 레포지토리의 커밋 목록 조회 (최근 50개)
     */
    public List<GHCommit> getCommits(String accessToken, String fullRepoName) {
        try {
            GHRepository repository = getRepository(accessToken, fullRepoName);
            return repository.listCommits().withPageSize(50).iterator().nextPage();
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            log.error("커밋 목록 조회 실패 - {}", fullRepoName, e);
            throw new GeneralException(ErrorStatus.GITHUB_API_ERROR);
        }
    }

    /**
     * README 내용 조회
     */
    public String getReadme(String accessToken, String fullRepoName) {
        try {
            GHRepository repository = getRepository(accessToken, fullRepoName);
            GHContent readme = repository.getReadme();
            if (readme == null) return "";
            String content;
            try (java.io.InputStream is = readme.read()) {
                content = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            }
            return content.length() > 10_000 ? content.substring(0, 10_000) : content;
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            log.warn("README 조회 실패 - {}", fullRepoName, e);
            return "";
        }
    }

    /**
     * 레포지토리 소스 파일 내용 수집 (확장자 필터 + 크기 제한)
     */
    public String getSourceFiles(String accessToken, String fullRepoName) {
        try {
            GHRepository repository = getRepository(accessToken, fullRepoName);
            StringBuilder sb = new StringBuilder();
            collectFiles(repository, "", sb, new int[]{0}, new int[]{0});
            return sb.toString();
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            log.warn("소스 파일 조회 실패 - {}", fullRepoName, e);
            return "";
        }
    }

    private void collectFiles(GHRepository repo, String path,
                              StringBuilder sb, int[] fileCount, int[] totalSize) throws IOException {
        if (fileCount[0] >= MAX_FILES || totalSize[0] >= MAX_TOTAL_SIZE) return;

        List<GHContent> contents = repo.getDirectoryContent(path.isEmpty() ? "/" : path);

        for (GHContent content : contents) {
            if (fileCount[0] >= MAX_FILES || totalSize[0] >= MAX_TOTAL_SIZE) break;

            if (content.isDirectory()) {
                if (!SKIP_DIRS.contains(content.getName())) {
                    collectFiles(repo, content.getPath(), sb, fileCount, totalSize);
                }
            } else if (content.isFile() && isSourceFile(content.getName())) {
                try {
                    long size = content.getSize();
                    if (size > MAX_FILE_SIZE) continue;

                    String fileContent;
                    try (java.io.InputStream is = content.read()) {
                        fileContent = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                    }

                    sb.append("\n=== ").append(content.getPath()).append(" ===\n");
                    sb.append(fileContent).append("\n");

                    fileCount[0]++;
                    totalSize[0] += fileContent.length();
                } catch (Exception e) {
                    log.warn("파일 읽기 실패 - {}", content.getPath());
                }
            }
        }
    }

    /**
     * fix/refactor 커밋의 실제 코드 변경사항(diff) 조회 (최대 5개)
     */
    public String getFixCommitDiffs(List<GHCommit> commits) {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (GHCommit commit : commits) {
            if (count >= MAX_DIFF_COMMITS) break;
            try {
                String message = commit.getCommitShortInfo().getMessage();
                String lowerMsg = message.toLowerCase();
                boolean isFixOrRefactor = FIX_PREFIXES.stream()
                        .anyMatch(lowerMsg::contains);
                if (!isFixOrRefactor) continue;

                sb.append("\n### 커밋: ").append(message).append("\n");

                List<GHCommit.File> files = commit.listFiles().toList();
                for (GHCommit.File file : files) {
                    String patch = file.getPatch();
                    if (patch == null || patch.isBlank()) continue;
                    if (!isSourceFile(file.getFileName())) continue;

                    String truncatedPatch = patch.length() > MAX_PATCH_LENGTH
                            ? patch.substring(0, MAX_PATCH_LENGTH) + "\n...(생략)"
                            : patch;

                    sb.append("파일: ").append(file.getFileName()).append("\n");
                    sb.append(truncatedPatch).append("\n");
                }
                count++;
            } catch (Exception e) {
                log.warn("커밋 diff 조회 실패 - {}", commit.getSHA1());
            }
        }

        return sb.toString();
    }

    private boolean isSourceFile(String fileName) {
        return SOURCE_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }
}
