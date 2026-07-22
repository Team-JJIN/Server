  package com.JJIN.domain.member.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.JJIN.domain.member.exception.MemberErrorCode;
import com.JJIN.global.auth.email.EmailSender;
import com.JJIN.global.auth.email.EmailVerificationCodeStore;
import com.JJIN.global.exception.JjinException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

	private final EmailVerificationCodeStore codeStore;
	private final EmailSender emailSender;

	private final SecureRandom random = new SecureRandom();

	@Value("${auth.email.code-length}")
	private int codeLength;

	@Value("${auth.email.code-ttl-seconds}")
	private long codeTtlSeconds;

	@Value("${auth.email.verified-ttl-seconds}")
	private long verifiedTtlSeconds;

	/**
	 * 인증코드를 생성해 Redis에 저장하고 메일로 발송한다.
	 * 재전송 요청도 이 메서드를 호출하며, 기존 코드는 덮어써지고 TTL이 초기화된다.
	 */
	public void sendCode(final String email) {
		String code = generateCode();
		codeStore.saveCode(email, code, codeTtlSeconds);
		emailSender.sendVerificationCode(email, code);
		log.info("인증코드 발송 완료: email={}", email);
	}

	/**
	 * 입력 코드가 저장된 코드와 일치하면 인증 처리한다.
	 * 성공 시 코드는 삭제하고, 회원가입까지 사용할 인증 완료 마커를 저장한다.
	 */
	public void verify(final String email, final String inputCode) {
		String storedCode = codeStore.getCode(email)
			.orElseThrow(() -> new JjinException(MemberErrorCode.VERIFICATION_CODE_NOT_FOUND));

		if (!storedCode.equals(inputCode)) {
			throw new JjinException(MemberErrorCode.VERIFICATION_CODE_MISMATCH);
		}

		codeStore.deleteCode(email);
		codeStore.markVerified(email, verifiedTtlSeconds);
		log.info("이메일 인증 완료: email={}", email);
	}

	private String generateCode() {
		int bound = (int) Math.pow(10, codeLength);
		int number = random.nextInt(bound);
		return String.format("%0" + codeLength + "d", number);
	}
}
