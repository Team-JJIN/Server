package com.JJIN.domain.terms.dto.response;

import com.JJIN.domain.terms.entity.Terms;
import com.JJIN.domain.terms.entity.enums.TermsType;

public record TermsResponse(
	Long id,
	TermsType type,
	String title,
	boolean required
) {

	public static TermsResponse from(final Terms terms) {
		return new TermsResponse(terms.getId(), terms.getType(), terms.getTitle(), terms.isRequired());
	}
}
