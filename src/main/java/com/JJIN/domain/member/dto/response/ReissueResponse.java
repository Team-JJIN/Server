package com.JJIN.domain.member.dto.response;

public record ReissueResponse(
	String accessToken
) {

	public static ReissueResponse of(final String accessToken) {
		return new ReissueResponse(accessToken);
	}
}
