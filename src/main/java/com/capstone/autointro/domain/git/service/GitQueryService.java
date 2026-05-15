package com.capstone.autointro.domain.git.service;

import com.capstone.autointro.domain.git.dto.GitResponse;

public interface GitQueryService {

    GitResponse.ProjectListInfo getProjectList(Long userId);

    GitResponse.GithubRepoListInfo getGithubRepoList(Long userId);
}
