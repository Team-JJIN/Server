package com.JJIN.domain.terms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JJIN.domain.terms.entity.TermsAgreement;

public interface TermsAgreementRepository extends JpaRepository<TermsAgreement, Long> {
}
