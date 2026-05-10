package com.capstone.autointro.domain.git.dto;

import com.capstone.autointro.domain.git.entity.Project;
import com.capstone.autointro.domain.git.entity.ProjectAnalysis;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.List;

public class GitResponse {

    public record ProjectInfo(
            Long projectId,
            String repoName,
            String repoUrl,
            String mainLang
    ) {
        public static ProjectInfo from(Project project) {
            return new ProjectInfo(
                    project.getId(),
                    project.getRepoName(),
                    project.getRepoUrl(),
                    project.getMainLang()
            );
        }
    }

    public record ProjectListInfo(
            List<ProjectInfo> projects,
            int totalCount
    ) {
        public static ProjectListInfo from(List<Project> projects) {
            List<ProjectInfo> projectInfos = projects.stream()
                    .map(ProjectInfo::from)
                    .toList();
            return new ProjectListInfo(projectInfos, projectInfos.size());
        }
    }

    public record GithubRepoInfo(
            String fullRepoName,
            String repoName,
            String repoUrl,
            String mainLang,
            String description,
            boolean isPrivate
    ) {
        public static GithubRepoInfo from(GHRepository repo) {
            return new GithubRepoInfo(
                    repo.getFullName(),
                    repo.getName(),
                    repo.getHtmlUrl().toString(),
                    repo.getLanguage() != null ? repo.getLanguage() : "Unknown",
                    repo.getDescription(),
                    repo.isPrivate()
            );
        }
    }

    public record GithubRepoListInfo(
            List<GithubRepoInfo> repos,
            int totalCount
    ) {
        public static GithubRepoListInfo from(List<GHRepository> repos) {
            List<GithubRepoInfo> repoInfos = repos.stream()
                    .map(GithubRepoInfo::from)
                    .toList();
            return new GithubRepoListInfo(repoInfos, repoInfos.size());
        }
    }

    public record ProjectAnalysisInfo(
            Long projectId,
            String repoName,
            String description,
            String techStack,
            String projectEffect,
            String troubleshooting
    ) {
        public static ProjectAnalysisInfo from(Project project, ProjectAnalysis analysis) {
            return new ProjectAnalysisInfo(
                    project.getId(),
                    project.getRepoName(),
                    analysis.getDescription(),
                    analysis.getTechStack(),
                    analysis.getProjectEffect(),
                    analysis.getTroubleshooting()
            );
        }
    }
}
