package com.JJIN.global.auth.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.JJIN.domain.member.exception.MemberErrorCode;
import com.JJIN.global.exception.JjinException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	public void sendVerificationCode(final String to, final String code) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject("[JJIN] 이메일 인증 코드를 입력해주세요.");
			message.setText("인증 코드는 [" + code + "] 입니다.\n5분 이내에 입력해 주세요.");
			mailSender.send(message);
		} catch (MailException e) {
			log.error("인증 메일 발송 실패: to={}", to, e);
			throw new JjinException(MemberErrorCode.MAIL_SEND_FAILED);
		}
	}
}
