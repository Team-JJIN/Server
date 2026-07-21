package com.JJIN.domain.onboarding.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 여행 취향 중분류. 각 값은 자신이 속한 대분류({@link TravelCategory})를 가진다.
 */
@Getter
@RequiredArgsConstructor
public enum TravelSubcategory {

	/* FOOD */
	KOREAN_FOOD(TravelCategory.FOOD, "한식"),
	CAFE_TEAHOUSE(TravelCategory.FOOD, "카페/찻집"),
	PUB(TravelCategory.FOOD, "술집"),
	LIKE_ALL_FOOD(TravelCategory.FOOD, "다 좋아요"),

	/* EXPERIENCE */
	TRADITIONAL_EXPERIENCE(TravelCategory.EXPERIENCE, "전통 체험"),
	TEMPLE_STAY(TravelCategory.EXPERIENCE, "템플스테이"),
	UNIQUE_EXPERIENCE(TravelCategory.EXPERIENCE, "이색 체험"),

	/* NATURE */
	MOUNTAIN_FOREST(TravelCategory.NATURE, "산/숲"),
	SEA_BEACH(TravelCategory.NATURE, "바다/해변"),
	LAKE_RIVER(TravelCategory.NATURE, "호수/강"),
	ISLAND(TravelCategory.NATURE, "섬"),
	PARK(TravelCategory.NATURE, "공원"),

	/* HISTORY */
	PALACE(TravelCategory.HISTORY, "궁궐"),
	HISTORIC_SITE(TravelCategory.HISTORY, "유적지"),
	MUSEUM(TravelCategory.HISTORY, "박물관"),
	TRADITIONAL_VILLAGE(TravelCategory.HISTORY, "전통 마을"),

	/* CULTURE */
	GALLERY(TravelCategory.CULTURE, "미술관"),
	PERFORMANCE_MUSICAL(TravelCategory.CULTURE, "공연/뮤지컬"),
	STREET_ART(TravelCategory.CULTURE, "거리 예술"),
	TEMPLE(TravelCategory.CULTURE, "사찰"),

	/* SHOPPING */
	TRADITIONAL_MARKET(TravelCategory.SHOPPING, "전통 시장"),
	LOCAL_SHOP(TravelCategory.SHOPPING, "로컬 상점"),
	DUTY_FREE(TravelCategory.SHOPPING, "면세점"),
	VINTAGE(TravelCategory.SHOPPING, "빈티지"),

	/* FESTIVAL */
	FESTIVAL_EVENT(TravelCategory.FESTIVAL, "축제"),
	PERFORMANCE_EVENT(TravelCategory.FESTIVAL, "공연 행사"),
	FIREWORKS(TravelCategory.FESTIVAL, "불꽃 축제"),
	NIGHT_MARKET(TravelCategory.FESTIVAL, "야시장"),

	/* LEISURE */
	SURFING(TravelCategory.LEISURE, "서핑"),
	SKIING(TravelCategory.LEISURE, "스키"),
	HIKING(TravelCategory.LEISURE, "등산"),
	CYCLING(TravelCategory.LEISURE, "자전거"),
	WATER_SPORTS(TravelCategory.LEISURE, "수상 스포츠"),
	;

	private final TravelCategory category;
	private final String displayName;

	public boolean belongsTo(final TravelCategory category) {
		return this.category == category;
	}
}
