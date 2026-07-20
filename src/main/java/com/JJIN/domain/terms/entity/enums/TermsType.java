package com.JJIN.domain.terms.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TermsType {

	SERVICE("이용약관 및 개인정보 처리방침", true),
	MARKETING("마케팅 정보 수신 동의(선택)", false),
	;

	private final String title;
	private final boolean required;
}
