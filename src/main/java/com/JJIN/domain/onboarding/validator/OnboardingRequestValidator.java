package com.JJIN.domain.onboarding.validator;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.JJIN.domain.onboarding.dto.request.CategoryPreferenceRequest;
import com.JJIN.domain.onboarding.dto.request.OnboardingCompleteRequest;
import com.JJIN.domain.onboarding.entity.enums.TravelCategory;
import com.JJIN.domain.onboarding.entity.enums.TravelSubcategory;
import com.JJIN.domain.onboarding.exception.OnboardingErrorCode;
import com.JJIN.global.exception.JjinException;

import lombok.RequiredArgsConstructor;

/**
 * 온보딩 요청의 교차 필드 검증을 담당한다.
 * Bean Validation으로 표현할 수 없는 도메인 규칙만 여기서 다룬다.
 */
@Component
@RequiredArgsConstructor
public class OnboardingRequestValidator {

	public static final ZoneId TRAVEL_ZONE = ZoneId.of("Asia/Seoul");

	private static final int MIN_CATEGORY_COUNT = 2;
	private static final int MAX_CATEGORY_COUNT = 4;

	private final Clock clock;

	public void validate(final OnboardingCompleteRequest request) {
		validateRegion(request);
		validateTravelDate(request);
		validateActivityTime(request);
		validatePreferences(request);
	}

	/**
	 * 지역 미정 여부와 지역 값의 조합을 검증한다.
	 */
	private void validateRegion(final OnboardingCompleteRequest request) {
		if (request.regionUndecided() == null) {
			throw new JjinException(OnboardingErrorCode.INVALID_REGION_SELECTION);
		}
		boolean undecided = request.isRegionUndecided();
		boolean hasRegion = request.region() != null;

		if (undecided == hasRegion) { // 미정인데 지역이 있거나, 미정이 아닌데 지역이 없는 경우
			throw new JjinException(OnboardingErrorCode.INVALID_REGION_SELECTION);
		}
	}

	/**
	 * 여행 시작일/종료일을 Asia/Seoul 기준으로 검증한다.
	 */
	private void validateTravelDate(final OnboardingCompleteRequest request) {
		LocalDate startDate = request.startDate();
		LocalDate endDate = request.endDate();

		if (startDate == null || endDate == null) {
			throw new JjinException(OnboardingErrorCode.INVALID_TRAVEL_DATE);
		}
		if (startDate.isBefore(today())) {
			throw new JjinException(OnboardingErrorCode.INVALID_TRAVEL_DATE);
		}
		if (endDate.isBefore(startDate)) {
			throw new JjinException(OnboardingErrorCode.INVALID_TRAVEL_DATE);
		}
	}

	private void validateActivityTime(final OnboardingCompleteRequest request) {
		if (request.activityStartTime() == null || request.activityEndTime() == null) {
			throw new JjinException(OnboardingErrorCode.INVALID_ACTIVITY_TIME);
		}
		if (!request.activityStartTime().isBefore(request.activityEndTime())) {
			throw new JjinException(OnboardingErrorCode.INVALID_ACTIVITY_TIME);
		}
	}

	/**
	 * 취향 대분류 개수/중복, 중분류 존재·중복·소속 관계를 검증한다.
	 */
	private void validatePreferences(final OnboardingCompleteRequest request) {
		List<CategoryPreferenceRequest> preferences = request.preferences();

		if (preferences == null
			|| preferences.size() < MIN_CATEGORY_COUNT
			|| preferences.size() > MAX_CATEGORY_COUNT) {
			throw new JjinException(OnboardingErrorCode.INVALID_CATEGORY_COUNT);
		}

		Set<TravelCategory> categories = new HashSet<>();
		Set<TravelSubcategory> seenSubcategories = new HashSet<>();

		for (CategoryPreferenceRequest preference : preferences) {
			TravelCategory category = preference.category();
			if (category == null) {
				throw new JjinException(OnboardingErrorCode.INVALID_CATEGORY_COUNT);
			}
			if (!categories.add(category)) { // 대분류 중복
				throw new JjinException(OnboardingErrorCode.INVALID_CATEGORY_COUNT);
			}

			List<TravelSubcategory> subcategories = preference.subcategories();
			if (subcategories == null || subcategories.isEmpty()) {
				throw new JjinException(OnboardingErrorCode.MISSING_SUBCATEGORY);
			}

			List<TravelSubcategory> ownSubcategories = new ArrayList<>();
			for (TravelSubcategory subcategory : subcategories) {
				if (subcategory == null || !subcategory.belongsTo(category)) {
					throw new JjinException(OnboardingErrorCode.INVALID_SUBCATEGORY);
				}
				if (!seenSubcategories.add(subcategory)) { // 중분류 중복
					throw new JjinException(OnboardingErrorCode.INVALID_SUBCATEGORY);
				}
				ownSubcategories.add(subcategory);
			}

			validateAllFoodSelection(category, ownSubcategories);
		}
	}

	/**
	 * LIKE_ALL_FOOD는 단독으로만 선택할 수 있다.
	 */
	private void validateAllFoodSelection(
		final TravelCategory category,
		final List<TravelSubcategory> subcategories
	) {
		if (category != TravelCategory.FOOD) {
			return;
		}
		if (subcategories.contains(TravelSubcategory.LIKE_ALL_FOOD) && subcategories.size() > 1) {
			throw new JjinException(OnboardingErrorCode.INVALID_ALL_FOOD_SELECTION);
		}
	}

	private LocalDate today() {
		return LocalDate.now(clock.withZone(TRAVEL_ZONE));
	}
}
