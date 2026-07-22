package com.JJIN.domain.onboarding.dto.response;

import com.JJIN.domain.member.entity.enums.Role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "온보딩 완료 응답")
public record OnboardingCompleteResponse(

	@Schema(description = "저장된 온보딩 프로필 ID", example = "1")
	Long onboardingId,

	@Schema(description = "MEMBER 역할이 반영된 새 액세스 토큰", example = "new-access-token")
	String accessToken,

	@Schema(description = "새 리프레쉬 토큰", example = "new-refresh-token")
	String refreshToken,

	@Schema(description = "변경된 회원 역할", example = "MEMBER")
	Role role
) {

	public static OnboardingCompleteResponse of(
		final Long onboardingId,
		final String accessToken,
		final String refreshToken,
		final Role role
	) {
		return new OnboardingCompleteResponse(onboardingId, accessToken, refreshToken, role);
	}
}
