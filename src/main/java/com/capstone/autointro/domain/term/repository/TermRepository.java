package com.capstone.autointro.domain.term.repository;

import com.capstone.autointro.domain.term.entity.Term;
import com.capstone.autointro.domain.term.enums.TermType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {

    List<Term> findAllByType(TermType type);
}
