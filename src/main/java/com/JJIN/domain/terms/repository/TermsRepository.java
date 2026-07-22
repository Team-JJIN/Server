package com.JJIN.domain.terms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JJIN.domain.terms.entity.Terms;
import com.JJIN.domain.terms.entity.enums.TermsType;

public interface TermsRepository extends JpaRepository<Terms, Long> {

	Optional<Terms> findByType(TermsType type);

	boolean existsByType(TermsType type);
}
