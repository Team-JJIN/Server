package com.JJIN.domain.onboarding.exception;

import org.springframework.http.HttpStatus;

import com.JJIN.global.response.base.BaseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OnboardingSuccessCode implements BaseCode {

	/*
	201 CREATED
	 */
	ONBOARDING_COMPLETE_SUCCESS(HttpStatus.CREATED, "여행 기본 정보 설정을 완료했습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
