package com.JJIN.global.auth.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.JJIN.global.auth.exception.OAuthErrorCode;
import com.JJIN.global.auth.oauth.dto.GoogleTokenResponse;
import com.JJIN.global.auth.oauth.dto.GoogleUserInfo;
import com.JJIN.global.exception.JjinException;

import lombok.extern.slf4j.Slf4j;

/**
 * 구글 OAuth 인가 코드(authorization code)를 받아
 * 액세스 토큰 교환 → 사용자 정보 조회까지 수행하는 클라이언트.
 */
@Slf4j
@Component
public class GoogleOAuthClient {

	private static final String GRANT_TYPE = "authorization_code";

	private final RestClient restClient = RestClient.create();

	@Value("${oauth.google.client-id}")
	private String clientId;

	@Value("${oauth.google.client-secret}")
	private String clientSecret;

	@Value("${oauth.google.redirect-uri}")
	private String redirectUri;

	@Value("${oauth.google.token-uri}")
	private String tokenUri;

	@Value("${oauth.google.userinfo-uri}")
	private String userInfoUri;

	public GoogleUserInfo getUserInfo(final String authorizationCode) {
		GoogleTokenResponse tokenResponse = requestToken(authorizationCode);
		return requestUserInfo(tokenResponse.accessToken());
	}

	private GoogleTokenResponse requestToken(final String authorizationCode) {
		return restClient.post()
			.uri(tokenUri)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(buildTokenRequestBody(authorizationCode))
			.retrieve()
			.onStatus(HttpStatusCode::isError, (request, response) -> {
				log.error("Google token 요청 실패: status={}", response.getStatusCode());
				throw new JjinException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
			})
			.body(GoogleTokenResponse.class);
	}

	private GoogleUserInfo requestUserInfo(final String accessToken) {
		return restClient.get()
			.uri(userInfoUri)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.onStatus(HttpStatusCode::isError, (request, response) -> {
				log.error("Google userinfo 요청 실패: status={}", response.getStatusCode());
				throw new JjinException(OAuthErrorCode.GET_INFO_ERROR);
			})
			.body(GoogleUserInfo.class);
	}

	private MultiValueMap<String, String> buildTokenRequestBody(final String authorizationCode) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", authorizationCode);
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("redirect_uri", redirectUri);
		body.add("grant_type", GRANT_TYPE);
		return body;
	}
}
