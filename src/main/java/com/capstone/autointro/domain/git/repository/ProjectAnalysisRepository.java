package com.capstone.autointro.domain.git.repository;

import com.capstone.autointro.domain.git.entity.ProjectAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectAnalysisRepository extends JpaRepository<ProjectAnalysis, Long> {

    Optional<ProjectAnalysis> findByProjectId(Long projectId);

    @Modifying
    @Query("UPDATE ProjectAnalysis pa SET pa.description = :description, pa.techStack = :techStack, pa.projectEffect = :projectEffect, pa.troubleshooting = :troubleshooting WHERE pa.id = :id")
    void updateFields(
            @Param("id") Long id,
            @Param("description") String description,
            @Param("techStack") String techStack,
            @Param("projectEffect") String projectEffect,
            @Param("troubleshooting") String troubleshooting
    );
}
