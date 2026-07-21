package com.JJIN.domain.onboarding.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 여행 취향 대분류.
 */
@Getter
@RequiredArgsConstructor
public enum TravelCategory {

	FOOD("먹거리"),
	EXPERIENCE("체험"),
	NATURE("자연"),
	HISTORY("역사"),
	CULTURE("문화"),
	SHOPPING("쇼핑"),
	FESTIVAL("축제"),
	LEISURE("레저"),
	;

	private final String displayName;
}
