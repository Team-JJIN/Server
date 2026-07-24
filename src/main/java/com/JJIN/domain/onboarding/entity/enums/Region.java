package com.JJIN.domain.onboarding.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 온보딩에서 선택 가능한 인기 여행 도시.
 * TourAPI KorService2 연동에 필요한 lDongRegnCd/lDongSignguCd는 확정된 값이 없어 아직 매핑하지 않는다.
 * TODO: 지역 추천 연동 시 법정동 코드(lDongRegnCd/lDongSignguCd) 매핑을 정의한다.
 */
@Getter
@RequiredArgsConstructor
public enum Region {

	SEOUL("서울"),
	BUSAN("부산"),
	INCHEON("인천"),
	JEJU("제주"),
	JEONJU("전주"),
	GYEONGJU("경주"),
	GANGNEUNG("강릉"),
	SOKCHO("속초"),
	DAEGU("대구"),
	GWANGJU("광주"),
	YEOSU("여수"),
	CHUNCHEON("춘천"),
	;

	private final String displayName;
}
