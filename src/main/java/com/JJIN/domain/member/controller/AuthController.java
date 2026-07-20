package com.JJIN.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JJIN.domain.member.dto.request.EmailSignupRequest;
import com.JJIN.domain.member.dto.request.GoogleLoginRequest;
import com.JJIN.domain.member.dto.request.LoginRequest;
import com.JJIN.domain.member.dto.request.ReissueRequest;
import com.JJIN.domain.member.dto.request.SendEmailCodeRequest;
import com.JJIN.domain.member.dto.request.VerifyEmailCodeRequest;
import com.JJIN.domain.member.dto.response.AuthTokenResponse;
import com.JJIN.domain.member.dto.response.ReissueResponse;
import com.JJIN.domain.member.exception.MemberSuccessCode;
import com.JJIN.domain.member.service.AuthService;
import com.JJIN.domain.member.service.EmailVerificationService;
import com.JJIN.global.auth.annotation.CurrentMember;
import com.JJIN.global.auth.dto.CurrentAuth;
import com.JJIN.global.auth.jwt.exception.TokenErrorCode;
import com.JJIN.global.exception.JjinException;
import com.JJIN.global.response.dto.SuccessResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final EmailVerificationService emailVerificationService;

	@PostMapping("/login/google")
	public ResponseEntity<SuccessResponse<AuthTokenResponse>> googleLogin(
		@Valid @RequestBody GoogleLoginRequest request
	) {
		AuthTokenResponse response = authService.loginWithGoogle(request);
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS, response));
	}

	/** 이메일 인증번호 발송 (재전송 포함) */
	@PostMapping("/email/code")
	public ResponseEntity<SuccessResponse<Void>> sendEmailCode(
		@Valid @RequestBody SendEmailCodeRequest request
	) {
		emailVerificationService.sendCode(request.email());
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.EMAIL_CODE_SENT));
	}

	/** 이메일 인증번호 검증 */
	@PostMapping("/email/verify")
	public ResponseEntity<SuccessResponse<Void>> verifyEmailCode(
		@Valid @RequestBody VerifyEmailCodeRequest request
	) {
		emailVerificationService.verify(request.email(), request.code());
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.EMAIL_VERIFIED));
	}

	/** 인증된 이메일로 회원가입 */
	@PostMapping("/signup")
	public ResponseEntity<SuccessResponse<AuthTokenResponse>> signup(
		@Valid @RequestBody EmailSignupRequest request
	) {
		AuthTokenResponse response = authService.signup(request);
		return ResponseEntity.status(MemberSuccessCode.SIGNUP_SUCCESS.getHttpStatus())
			.body(SuccessResponse.of(MemberSuccessCode.SIGNUP_SUCCESS, response));
	}

	/** 이메일/비밀번호 로그인 */
	@PostMapping("/login")
	public ResponseEntity<SuccessResponse<AuthTokenResponse>> login(
		@Valid @RequestBody LoginRequest request
	) {
		AuthTokenResponse response = authService.login(request);
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.LOGIN_SUCCESS, response));
	}

	@PatchMapping("/role")
	public ResponseEntity<SuccessResponse<AuthTokenResponse>> changeRoleToMember(
		@CurrentMember CurrentAuth currentAuth
	) {
		if (currentAuth == null) {
			throw new JjinException(TokenErrorCode.INVALID_AUTHORIZATION_HEADER);
		}
		AuthTokenResponse response = authService.changeRoleToMember(currentAuth.memberId());
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.ROLE_CHANGE_SUCCESS, response));
	}

	@PostMapping("/reissue")
	public ResponseEntity<SuccessResponse<ReissueResponse>> reissue(
		@Valid @RequestBody ReissueRequest request
	) {
		ReissueResponse response = authService.reissue(request.refreshToken());
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.REISSUE_SUCCESS, response));
	}

	@PostMapping("/logout")
	public ResponseEntity<SuccessResponse<Void>> logout(@CurrentMember CurrentAuth currentAuth) {
		if (currentAuth == null) {
			throw new JjinException(TokenErrorCode.INVALID_AUTHORIZATION_HEADER);
		}
		authService.logout(currentAuth.memberId());
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.LOGOUT_SUCCESS));
	}
}
