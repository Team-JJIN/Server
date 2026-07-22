package com.JJIN.domain.member.dto.request;

import com.JJIN.domain.terms.entity.enums.TermsType;

import jakarta.validation.constraints.NotNull;

public record TermsAgreementRequest(
	@NotNull(message = "약관 유형은 필수입니다.")
	TermsType type,

	boolean agreed
) {
}
