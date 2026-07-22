package com.JJIN.domain.onboarding.dto.request;

import java.util.List;

import com.JJIN.domain.onboarding.entity.enums.TravelCategory;
import com.JJIN.domain.onboarding.entity.enums.TravelSubcategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "취향 대분류 1개와 그 하위 중분류 선택")
public record CategoryPreferenceRequest(

	@Schema(
		description = "취향 대분류",
		example = "FOOD",
		allowableValues = {"FOOD", "EXPERIENCE", "NATURE", "HISTORY", "CULTURE", "SHOPPING", "FESTIVAL", "LEISURE"}
	)
	@NotNull(message = "취향 대분류는 필수입니다.")
	TravelCategory category,

	@Schema(
		description = "해당 대분류에 속한 중분류 목록 (최소 1개, 중복 불가)",
		example = "[\"KOREAN_FOOD\", \"CAFE_TEAHOUSE\"]"
	)
	@NotEmpty(message = "중분류는 최소 1개 이상 선택해야 합니다.")
	List<@NotNull(message = "중분류 값이 올바르지 않습니다.") TravelSubcategory> subcategories
) {
}
