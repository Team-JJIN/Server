package com.JJIN.global.auth.email;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * 이메일 인증코드와 인증 완료 여부를 Redis에 저장한다.
 * - 코드 키:     email:code:{email}     (TTL: 인증코드 유효시간)
 * - 인증완료 키: email:verified:{email} (TTL: 인증 후 회원가입까지 유효시간)
 */
@Component
@RequiredArgsConstructor
public class EmailVerificationCodeStore {

	private static final String CODE_KEY_PREFIX = "email:code:";
	private static final String VERIFIED_KEY_PREFIX = "email:verified:";

	private final StringRedisTemplate redisTemplate;

	public void saveCode(final String email, final String code, final long ttlSeconds) {
		redisTemplate.opsForValue().set(codeKey(email), code, Duration.ofSeconds(ttlSeconds));
	}

	public Optional<String> getCode(final String email) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(codeKey(email)));
	}

	public void deleteCode(final String email) {
		redisTemplate.delete(codeKey(email));
	}

	public void markVerified(final String email, final long ttlSeconds) {
		redisTemplate.opsForValue().set(verifiedKey(email), "true", Duration.ofSeconds(ttlSeconds));
	}

	public boolean isVerified(final String email) {
		return Boolean.parseBoolean(redisTemplate.opsForValue().get(verifiedKey(email)));
	}

	public void deleteVerified(final String email) {
		redisTemplate.delete(verifiedKey(email));
	}

	private String codeKey(final String email) {
		return CODE_KEY_PREFIX + email;
	}

	private String verifiedKey(final String email) {
		return VERIFIED_KEY_PREFIX + email;
	}
}
