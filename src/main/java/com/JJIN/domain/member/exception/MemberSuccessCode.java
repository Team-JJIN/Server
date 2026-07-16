package com.JJIN.domain.member.exception;

import org.springframework.http.HttpStatus;

import com.JJIN.global.response.base.BaseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements BaseCode {

	/*
	200 OK
	 */
	LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
	REISSUE_SUCCESS(HttpStatus.OK, "액세스 토큰 재발급에 성공했습니다."),
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다."),

	/*
	201 CREATED
	 */
	SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입에 성공했습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
