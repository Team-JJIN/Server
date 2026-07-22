package com.JJIN.domain.onboarding.controller.docs;

import org.springframework.http.ResponseEntity;

import com.JJIN.domain.onboarding.dto.request.OnboardingCompleteRequest;
import com.JJIN.domain.onboarding.dto.response.OnboardingCompleteResponse;
import com.JJIN.global.auth.dto.CurrentAuth;
import com.JJIN.global.response.dto.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 온보딩 API의 Swagger 문서 정의.
 * 컨트롤러 본문에서 문서화 애노테이션을 분리하기 위한 인터페이스이며,
 * 요청 매핑과 파라미터 바인딩 애노테이션은 구현 컨트롤러에 둔다.
 */
@Tag(name = "Onboarding", description = "온보딩 여행 기본 정보 API")
public interface OnboardingControllerDocs {

	@Operation(
		summary = "온보딩 여행 기본 정보 저장",
		description = """
			온보딩 S1~S4에서 수집한 여행 기본 정보를 마지막 '시작하기' 단계에서 한 번에 저장한다.
			저장에 성공하면 회원 역할이 ONBOARDING에서 MEMBER로 변경되고, MEMBER 역할이 반영된 토큰이 재발급된다.

			요청 필드
			- region: 여행 지역. SEOUL, BUSAN, INCHEON, JEJU, JEONJU, GYEONGJU, GANGNEUNG, SOKCHO, DAEGU, GWANGJU, YEOSU, CHUNCHEON
			- regionUndecided: 지역 미정 여부. true면 region은 반드시 null, false면 region 필수
			- startDate / endDate: 여행 기간. startDate는 Asia/Seoul 기준 오늘 이전 불가, endDate는 startDate 이상
			- activityStartTime / activityEndTime: 하루 활동 시간(HH:mm). 시작 < 종료
			- transportMode: 주 이동 수단 1개. WALKING, PUBLIC_TRANSIT, CAR
			- preferences: 취향 대분류 2~4개(중복 불가). 각 대분류마다 그 대분류에 속한 중분류를 1개 이상(중복 불가) 선택
			  - category: FOOD, EXPERIENCE, NATURE, HISTORY, CULTURE, SHOPPING, FESTIVAL, LEISURE
			  - subcategories: FOOD(KOREAN_FOOD, CAFE_TEAHOUSE, PUB, LIKE_ALL_FOOD),
			    EXPERIENCE(TRADITIONAL_EXPERIENCE, TEMPLE_STAY, UNIQUE_EXPERIENCE),
			    NATURE(MOUNTAIN_FOREST, SEA_BEACH, LAKE_RIVER, ISLAND, PARK),
			    HISTORY(PALACE, HISTORIC_SITE, MUSEUM, TRADITIONAL_VILLAGE),
			    CULTURE(GALLERY, PERFORMANCE_MUSICAL, STREET_ART, TEMPLE),
			    SHOPPING(TRADITIONAL_MARKET, LOCAL_SHOP, DUTY_FREE, VINTAGE),
			    FESTIVAL(FESTIVAL_EVENT, PERFORMANCE_EVENT, FIREWORKS, NIGHT_MARKET),
			    LEISURE(SURFING, SKIING, HIKING, CYCLING, WATER_SPORTS)
			  - LIKE_ALL_FOOD는 다른 FOOD 중분류와 함께 선택할 수 없다.
			- experienceLevel: LIGHT, NORMAL, DEEP
			""",
		security = @SecurityRequirement(name = "BearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "여행 기본 정보 설정 완료",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(value = """
					{
					  "status": 201,
					  "message": "여행 기본 정보 설정을 완료했습니다.",
					  "data": {
					    "onboardingId": 1,
					    "accessToken": "new-access-token",
					    "refreshToken": "new-refresh-token",
					    "role": "MEMBER"
					  }
					}
					""")
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "요청 값 또는 도메인 규칙 위반 (지역 선택, 여행 날짜, 활동 시간, 취향 선택)",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(value = """
					{
					  "status": 400,
					  "message": "취향 대분류는 중복 없이 2~4개를 선택해야 합니다."
					}
					""")
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "인증 정보 누락 또는 유효하지 않은 액세스 토큰",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(value = """
					{
					  "status": 401,
					  "message": "유효하지 않은 authorization 헤더입니다"
					}
					""")
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "회원을 찾을 수 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(value = """
					{
					  "status": 404,
					  "message": "회원을 찾을 수 없습니다."
					}
					""")
			)
		),
		@ApiResponse(
			responseCode = "409",
			description = "이미 온보딩을 완료한 회원",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(value = """
					{
					  "status": 409,
					  "message": "이미 온보딩을 완료한 회원입니다."
					}
					""")
			)
		)
	})
	ResponseEntity<SuccessResponse<OnboardingCompleteResponse>> completeOnboarding(
		CurrentAuth currentAuth,
		OnboardingCompleteRequest request
	);
}
