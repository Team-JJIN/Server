package com.JJIN.domain.member.dto.response;

import com.JJIN.domain.member.entity.enums.Role;

public record AuthTokenResponse(
	String accessToken,
	String refreshToken,
	Role role
) {

	public static AuthTokenResponse of(
		final String accessToken,
		final String refreshToken,
		final Role role
	) {
		return new AuthTokenResponse(accessToken, refreshToken, role);
	}
}