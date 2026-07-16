package com.JJIN.global.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfo(
	@JsonProperty("sub") String sub,
	@JsonProperty("email") String email,
	@JsonProperty("email_verified") Boolean emailVerified,
	@JsonProperty("name") String name,
	@JsonProperty("picture") String picture
) {
}
