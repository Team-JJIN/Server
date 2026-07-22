package com.JJIN.domain.onboarding.exception;

import org.springframework.http.HttpStatus;

import com.JJIN.global.response.base.BaseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OnboardingErrorCode implements BaseCode {

	/*
	400 BAD REQUEST
	 */
	INVALID_REGION_SELECTION(HttpStatus.BAD_REQUEST, "지역 선택과 지역 미정 여부가 올바르지 않습니다."),
	INVALID_TRAVEL_DATE(HttpStatus.BAD_REQUEST, "여행 날짜가 올바르지 않습니다."),
	INVALID_ACTIVITY_TIME(HttpStatus.BAD_REQUEST, "활동 시간이 올바르지 않습니다."),
	INVALID_CATEGORY_COUNT(HttpStatus.BAD_REQUEST, "취향 대분류는 중복 없이 2~4개를 선택해야 합니다."),
	MISSING_SUBCATEGORY(HttpStatus.BAD_REQUEST, "선택한 대분류에는 중분류가 최소 1개 있어야 합니다."),
	INVALID_SUBCATEGORY(HttpStatus.BAD_REQUEST, "중분류 선택이 올바르지 않습니다."),
	INVALID_ALL_FOOD_SELECTION(HttpStatus.BAD_REQUEST, "'다 좋아요'는 다른 먹거리 중분류와 함께 선택할 수 없습니다."),

	/*
	409 CONFLICT
	 */
	ONBOARDING_ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 온보딩을 완료한 회원입니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
