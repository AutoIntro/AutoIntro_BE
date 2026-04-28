package com.capstone.autointro.domain.introduction.repository;

import com.capstone.autointro.domain.introduction.entity.AiIntroduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiIntroductionRepository extends JpaRepository<AiIntroduction, Long> {

    List<AiIntroduction> findAllByUserRequestId(Long userRequestId);

    Optional<AiIntroduction> findTopByUserRequestIdOrderByCreatedAtDesc(Long userRequestId);
}
