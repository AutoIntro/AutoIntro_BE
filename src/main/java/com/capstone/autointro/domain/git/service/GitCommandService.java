package com.capstone.autointro.domain.git.service;

import com.capstone.autointro.domain.git.dto.GitRequest;
import com.capstone.autointro.domain.git.dto.GitResponse;

public interface GitCommandService {

    GitResponse.ProjectInfo saveProject(GitRequest.Save request, Long userId);

    GitResponse.ProjectInfo updateProject(Long gitId, GitRequest.Update request, Long userId);

    GitResponse.ProjectAnalysisInfo reorganizeProject(Long gitId, GitRequest.Reorganize request, Long userId);
}
