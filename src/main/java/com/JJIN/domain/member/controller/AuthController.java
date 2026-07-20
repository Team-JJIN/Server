package com.JJIN.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JJIN.domain.member.dto.request.GoogleLoginRequest;
import com.JJIN.domain.member.dto.request.ReissueRequest;
import com.JJIN.domain.member.dto.response.AuthTokenResponse;
import com.JJIN.domain.member.dto.response.ReissueResponse;
import com.JJIN.domain.member.exception.MemberSuccessCode;
import com.JJIN.domain.member.service.AuthService;
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

	@PostMapping("/login/google")
	public ResponseEntity<SuccessResponse<AuthTokenResponse>> googleLogin(
		@Valid @RequestBody GoogleLoginRequest request
	) {
		AuthTokenResponse response = authService.loginWithGoogle(request);
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
