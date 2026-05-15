package com.capstone.autointro.domain.git.service;

import com.capstone.autointro.common.exception.GeneralException;
import com.capstone.autointro.common.github.GithubApiClient;
import com.capstone.autointro.common.status.error.ErrorStatus;
import com.capstone.autointro.domain.auth.entity.Provider;
import com.capstone.autointro.domain.auth.repository.ProviderRepository;
import com.capstone.autointro.domain.git.dto.GitResponse;
import com.capstone.autointro.domain.git.entity.Project;
import com.capstone.autointro.domain.git.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GitQueryServiceImpl implements GitQueryService {

    private final ProjectRepository projectRepository;
    private final ProviderRepository providerRepository;
    private final GithubApiClient githubApiClient;

    @Override
    public GitResponse.ProjectListInfo getProjectList(Long userId) {
        List<Project> projects = projectRepository.findAllByUserId(userId);
        return GitResponse.ProjectListInfo.from(projects);
    }

    @Override
    public GitResponse.GithubRepoListInfo getGithubRepoList(Long userId) {
        Provider provider = providerRepository.findByUserIdAndProviderName(userId, "github")
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (provider.getGithubAccessToken() == null) {
            throw new GeneralException(ErrorStatus.GITHUB_ACCESS_TOKEN_NOT_FOUND);
        }

        List<GHRepository> repos = githubApiClient.getRepositories(provider.getGithubAccessToken());
        return GitResponse.GithubRepoListInfo.from(repos);
    }
}
