package com.capstone.autointro.domain.git.repository;

import com.capstone.autointro.domain.git.entity.ProjectAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectAnalysisRepository extends JpaRepository<ProjectAnalysis, Long> {

    Optional<ProjectAnalysis> findByProjectId(Long projectId);
}
