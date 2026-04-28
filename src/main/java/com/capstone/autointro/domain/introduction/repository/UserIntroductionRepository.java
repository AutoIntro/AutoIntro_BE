package com.capstone.autointro.domain.introduction.repository;

import com.capstone.autointro.domain.introduction.entity.UserIntroduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserIntroductionRepository extends JpaRepository<UserIntroduction, Long> {

    List<UserIntroduction> findAllByUserRequestUserIdOrderByCreatedAtDesc(Long userId);

    Optional<UserIntroduction> findByIdAndUserRequestUserId(Long id, Long userId);
}
