package com.capstone.autointro.domain.introduction.repository;

import com.capstone.autointro.domain.introduction.entity.AiGenerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiGenerationLogRepository extends JpaRepository<AiGenerationLog, Long> {

    List<AiGenerationLog> findAllByUserRequestIdOrderByCreatedAtDesc(Long userRequestId);
}
