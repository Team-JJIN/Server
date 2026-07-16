package com.JJIN.domain.member.dto.response;

public record AuthTokenResponse(
	String accessToken,
	String refreshToken,
	boolean isNewMember
) {

	public static AuthTokenResponse of(
		final String accessToken,
		final String refreshToken,
		final boolean isNewMember
	) {
		return new AuthTokenResponse(accessToken, refreshToken, isNewMember);
	}
}