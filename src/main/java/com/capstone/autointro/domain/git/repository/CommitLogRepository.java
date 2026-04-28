package com.capstone.autointro.domain.git.repository;

import com.capstone.autointro.domain.git.entity.CommitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitLogRepository extends JpaRepository<CommitLog, Long> {

    List<CommitLog> findAllByProjectIdOrderByCommitDateDesc(Long projectId);

    void deleteAllByProjectId(Long projectId);
}
