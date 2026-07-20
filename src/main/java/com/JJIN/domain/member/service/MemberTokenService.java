package com.JJIN.domain.member.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.JJIN.domain.member.entity.enums.Role;
import com.JJIN.global.auth.jwt.JwtTokenProvider;
import com.JJIN.global.auth.jwt.JwtValidationType;
import com.JJIN.global.auth.jwt.TokenRepository;
import com.JJIN.global.auth.jwt.exception.TokenErrorCode;
import com.JJIN.global.auth.redis.Token;
import com.JJIN.global.auth.security.MemberAuthentication;
import com.JJIN.global.exception.JjinException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberTokenService {

	private final TokenRepository tokenRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public void saveRefreshToken(final Long memberId, final String refreshToken) {
		log.info("Saving refresh token for memberId: {}", memberId);
		tokenRepository.save(Token.of(memberId, refreshToken));
		log.info("Successfully saved refresh token for memberId: {}", memberId);
	}

	public Long findIdByRefreshToken(final String refreshToken) {
		log.info("Searching for memberId using refresh token: {}", refreshToken);
		Token token = tokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> {
				log.error("Refresh token not found in Redis: {}", refreshToken);
				return new JjinException(TokenErrorCode.REFRESH_TOKEN_NOT_FOUND);
			});
		log.info("Found memberId: {} for refresh token", token.getId());
		return token.getId();
	}

	public String reissueAccessToken(final String refreshToken) {
		validateRefreshToken(refreshToken);

		Long memberIdFromToken = jwtTokenProvider.getMemberIdFromJwt(refreshToken);
		Long storedMemberId = findIdByRefreshToken(refreshToken);
		if (!memberIdFromToken.equals(storedMemberId)) {
			throw new JjinException(TokenErrorCode.REFRESH_TOKEN_MEMBER_ID_MISMATCH_ERROR);
		}

		Role role = jwtTokenProvider.getRoleFromJwt(refreshToken);
		Authentication authentication = new MemberAuthentication(
			memberIdFromToken,
			null,
			List.of(role.toGrantedAuthority())
		);
		return jwtTokenProvider.issueAccessToken(authentication);
	}

	private void validateRefreshToken(final String refreshToken) {
		JwtValidationType type = jwtTokenProvider.validateToken(refreshToken);
		switch (type) {
			case VALID_JWT -> {
			}
			case EXPIRED_JWT_TOKEN -> throw new JjinException(TokenErrorCode.REFRESH_TOKEN_EXPIRED_ERROR);
			case INVALID_JWT_SIGNATURE -> throw new JjinException(TokenErrorCode.REFRESH_TOKEN_SIGNATURE_ERROR);
			case UNSUPPORTED_JWT_TOKEN -> throw new JjinException(TokenErrorCode.UNSUPPORTED_REFRESH_TOKEN_ERROR);
			case EMPTY_JWT -> throw new JjinException(TokenErrorCode.REFRESH_TOKEN_EMPTY_ERROR);
			default -> throw new JjinException(TokenErrorCode.INVALID_REFRESH_TOKEN_ERROR);
		}
	}

	@Transactional
	public void deleteRefreshToken(final Long memberId) {
		log.info("Deleting refresh token for memberId: {}", memberId);
		Token token = tokenRepository.findById(memberId)
			.orElseThrow(() -> {
				log.error("No refresh token found in Redis for memberId: {}", memberId);
				return new JjinException(TokenErrorCode.REFRESH_TOKEN_NOT_FOUND);
			});
		tokenRepository.delete(token);
		log.info("Successfully deleted refresh token for memberId: {}", memberId);
	}
}
