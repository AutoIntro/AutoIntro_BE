package com.capstone.autointro.domain.term.repository;

import com.capstone.autointro.domain.term.entity.TermAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermAgreementRepository extends JpaRepository<TermAgreement, Long> {

    List<TermAgreement> findAllByUserId(Long userId);

    boolean existsByUserIdAndTermId(Long userId, Long termId);
}
