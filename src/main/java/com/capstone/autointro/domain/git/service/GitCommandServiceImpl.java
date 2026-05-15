package com.capstone.autointro.domain.git.service;

import com.capstone.autointro.common.exception.GeneralException;
import com.capstone.autointro.common.github.GithubApiClient;
import com.capstone.autointro.common.openai.OpenAiSummaryClient;
import com.capstone.autointro.common.status.error.ErrorStatus;
import com.capstone.autointro.domain.auth.entity.Provider;
import com.capstone.autointro.domain.auth.repository.ProviderRepository;
import com.capstone.autointro.domain.git.dto.GitRequest;
import com.capstone.autointro.domain.git.dto.GitResponse;
import com.capstone.autointro.domain.git.entity.CommitLog;
import com.capstone.autointro.domain.git.entity.Project;
import com.capstone.autointro.domain.git.entity.ProjectAnalysis;
import com.capstone.autointro.domain.git.repository.CommitLogRepository;
import com.capstone.autointro.domain.git.repository.ProjectAnalysisRepository;
import com.capstone.autointro.domain.git.repository.ProjectRepository;
import com.capstone.autointro.domain.user.entity.User;
import com.capstone.autointro.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GitCommandServiceImpl implements GitCommandService {

    private final ProjectRepository projectRepository;
    private final ProjectAnalysisRepository projectAnalysisRepository;
    private final CommitLogRepository commitLogRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final GithubApiClient githubApiClient;
    private final OpenAiSummaryClient openAiSummaryClient;

    @Override
    public GitResponse.ProjectInfo saveProject(GitRequest.Save request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Provider provider = getGithubProvider(userId);
        String accessToken = getAccessToken(provider);

        validateGithubOwnership(provider.getProviderUsername(), request.fullRepoName());

        String repoName = extractRepoName(request.fullRepoName());

        // 1. GitHub에서 레포 정보, 커밋, README, 소스 파일 가져오기
        GHRepository repo = githubApiClient.getRepository(accessToken, request.fullRepoName());
        List<GHCommit> commits = githubApiClient.getCommits(accessToken, request.fullRepoName());
        String readme = githubApiClient.getReadme(accessToken, request.fullRepoName());
        String sourceFiles = githubApiClient.getSourceFiles(accessToken, request.fullRepoName());
        String commitMessages = buildCommitMessages(commits);
        String fixDiffs = githubApiClient.getFixCommitDiffs(commits);

        // 2. OpenAI 분석
        OpenAiSummaryClient.SummaryResult summary = openAiSummaryClient.summarizeProject(
                repoName, request.mainLang(), readme, sourceFiles, commitMessages, fixDiffs, ""
        );

        // 3. Project 저장
        Project project = projectRepository.save(
                Project.builder()
                        .repoName(repoName)
                        .repoUrl(repo.getHtmlUrl().toString())
                        .mainLang(request.mainLang())
                        .user(user)
                        .build()
        );

        // 4. CommitLog 저장
        saveCommitLogs(commits, project);

        // 5. ProjectAnalysis 저장
        projectAnalysisRepository.save(
                ProjectAnalysis.builder()
                        .description(summary.description())
                        .techStack(summary.techStack())
                        .projectEffect(summary.projectEffect())
                        .troubleshooting(summary.troubleshooting())
                        .project(project)
                        .build()
        );

        return GitResponse.ProjectInfo.from(project);
    }

    @Override
    public GitResponse.ProjectInfo updateProject(Long gitId, GitRequest.Update request, Long userId) {
        Project project = projectRepository.findByIdAndUserId(gitId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_NOT_FOUND));

        project.update(request.repoName(), request.repoUrl(), request.mainLang());

        return GitResponse.ProjectInfo.from(project);
    }

    @Override
    public GitResponse.ProjectAnalysisInfo reorganizeProject(Long gitId, GitRequest.Reorganize request, Long userId) {
        Project project = projectRepository.findByIdAndUserId(gitId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_NOT_FOUND));

        Provider provider = getGithubProvider(userId);
        String accessToken = getAccessToken(provider);

        String fullRepoName = provider.getProviderUsername() + "/" + project.getRepoName();

        // GitHub에서 최신 데이터 재조회
        List<GHCommit> commits = githubApiClient.getCommits(accessToken, fullRepoName);
        String readme = githubApiClient.getReadme(accessToken, fullRepoName);
        String sourceFiles = githubApiClient.getSourceFiles(accessToken, fullRepoName);
        String commitMessages = buildCommitMessages(commits);
        String fixDiffs = githubApiClient.getFixCommitDiffs(commits);
        String additionalRequest = (request != null && request.additionalRequest() != null)
                ? request.additionalRequest() : "";

        // AI 재분석
        OpenAiSummaryClient.SummaryResult summary = openAiSummaryClient.summarizeProject(
                project.getRepoName(), project.getMainLang(), readme, sourceFiles, commitMessages, fixDiffs, additionalRequest
        );

        // ProjectAnalysis 업데이트
        ProjectAnalysis analysis = projectAnalysisRepository.findByProjectId(gitId)
                .orElseGet(() -> projectAnalysisRepository.save(
                        ProjectAnalysis.builder().project(project).build()
                ));
        projectAnalysisRepository.updateFields(
                analysis.getId(),
                summary.description(),
                summary.techStack(),
                summary.projectEffect(),
                summary.troubleshooting()
        );

        // CommitLog 재저장
        commitLogRepository.deleteAllByProjectId(gitId);
        saveCommitLogs(commits, project);

        // 업데이트된 analysis 재조회 후 반환
        ProjectAnalysis updatedAnalysis = projectAnalysisRepository.findByProjectId(gitId)
                .orElse(analysis);
        return GitResponse.ProjectAnalysisInfo.from(project, updatedAnalysis);
    }

    // ── private helpers ──────────────────────────────────────────

    private Provider getGithubProvider(Long userId) {
        return providerRepository.findByUserIdAndProviderName(userId, "github")
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    private String getAccessToken(Provider provider) {
        if (provider.getGithubAccessToken() == null) {
            throw new GeneralException(ErrorStatus.GITHUB_ACCESS_TOKEN_NOT_FOUND);
        }
        return provider.getGithubAccessToken();
    }

    private void validateGithubOwnership(String loginUsername, String fullRepoName) {
        String repoOwner = fullRepoName.split("/")[0];
        if (!loginUsername.equalsIgnoreCase(repoOwner)) {
            throw new GeneralException(ErrorStatus.GITHUB_ACCOUNT_MISMATCH);
        }
    }

    private String extractRepoName(String fullRepoName) {
        String[] parts = fullRepoName.split("/");
        if (parts.length != 2) {
            throw new GeneralException(ErrorStatus.BAD_REQUEST);
        }
        return parts[1];
    }

    private String buildCommitMessages(List<GHCommit> commits) {
        return commits.stream()
                .map(commit -> {
                    try {
                        return "- " + commit.getCommitShortInfo().getMessage();
                    } catch (IOException e) {
                        return "";
                    }
                })
                .filter(msg -> !msg.isBlank())
                .collect(Collectors.joining("\n"));
    }

    private void saveCommitLogs(List<GHCommit> commits, Project project) {
        List<CommitLog> commitLogs = commits.stream()
                .map(commit -> {
                    try {
                        return CommitLog.builder()
                                .message(commit.getCommitShortInfo().getMessage())
                                .commitDate(commit.getCommitDate().toInstant()
                                        .atZone(java.time.ZoneId.systemDefault())
                                        .toLocalDateTime())
                                .project(project)
                                .build();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(log -> log != null)
                .toList();

        commitLogRepository.saveAll(commitLogs);
    }
}
