package com.capstone.autointro.domain.resume.repository;

import com.capstone.autointro.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findAllByUserId(Long userId);

    Optional<Resume> findByIdAndUserId(Long id, Long userId);
}
