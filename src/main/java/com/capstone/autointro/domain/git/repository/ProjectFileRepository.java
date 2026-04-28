package com.capstone.autointro.domain.git.repository;

import com.capstone.autointro.domain.git.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {

    List<ProjectFile> findAllByProjectId(Long projectId);

    void deleteAllByProjectId(Long projectId);
}
