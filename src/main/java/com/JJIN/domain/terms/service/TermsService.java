package com.JJIN.domain.terms.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JJIN.domain.member.dto.request.TermsAgreementRequest;
import com.JJIN.domain.member.exception.MemberErrorCode;
import com.JJIN.domain.terms.dto.response.TermsResponse;
import com.JJIN.domain.terms.entity.Terms;
import com.JJIN.domain.terms.entity.TermsAgreement;
import com.JJIN.domain.terms.entity.enums.TermsType;
import com.JJIN.domain.terms.repository.TermsAgreementRepository;
import com.JJIN.domain.terms.repository.TermsRepository;
import com.JJIN.global.exception.JjinException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsService {

	private final TermsRepository termsRepository;
	private final TermsAgreementRepository termsAgreementRepository;

	@Transactional(readOnly = true)
	public List<TermsResponse> getAllTerms() {
		return termsRepository.findAll().stream()
			.map(TermsResponse::from)
			.toList();
	}

	@Transactional
	public void saveAgreements(final Long memberId, final List<TermsAgreementRequest> agreements) {
		Map<TermsType, Boolean> agreedByType = new EnumMap<>(TermsType.class);
		for (TermsAgreementRequest agreement : agreements) {
			agreedByType.put(agreement.type(), agreement.agreed());
		}

		List<Terms> allTerms = termsRepository.findAll();
		for (Terms terms : allTerms) {
			boolean agreed = agreedByType.getOrDefault(terms.getType(), false);
			if (terms.isRequired() && !agreed) {
				throw new JjinException(MemberErrorCode.REQUIRED_TERMS_NOT_AGREED);
			}
		}

		List<TermsAgreement> toSave = allTerms.stream()
			.map(terms -> TermsAgreement.of(
				memberId,
				terms.getId(),
				agreedByType.getOrDefault(terms.getType(), false)
			))
			.toList();
		termsAgreementRepository.saveAll(toSave);
	}
}
