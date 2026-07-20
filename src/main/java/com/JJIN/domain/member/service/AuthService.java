package com.JJIN.domain.member.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JJIN.domain.member.dto.request.GoogleLoginRequest;
import com.JJIN.domain.member.dto.response.AuthTokenResponse;
import com.JJIN.domain.member.dto.response.ReissueResponse;
import com.JJIN.domain.member.entity.Member;
import com.JJIN.domain.member.entity.enums.Role;
import com.JJIN.domain.member.exception.MemberErrorCode;
import com.JJIN.domain.member.repository.MemberRepository;
import com.JJIN.global.auth.jwt.JwtTokenProvider;
import com.JJIN.global.auth.oauth.GoogleOAuthClient;
import com.JJIN.global.auth.oauth.dto.GoogleUserInfo;
import com.JJIN.global.auth.security.MemberAuthentication;
import com.JJIN.global.exception.JjinException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final GoogleOAuthClient googleOAuthClient;
	private final MemberRepository memberRepository;
	private final MemberTokenService memberTokenService;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 구글 소셜 로그인. 인가 코드로 구글 사용자 정보를 조회한 뒤,
	 * 기존 회원이면 로그인, 신규 사용자면 회원가입 후 토큰을 발급한다.
	 */
	@Transactional
	public AuthTokenResponse loginWithGoogle(final GoogleLoginRequest request) {
		GoogleUserInfo userInfo = googleOAuthClient.getUserInfo(request.code());

		Member member = memberRepository.findBySocialId(userInfo.sub())
			.orElseGet(() -> registerNewMember(userInfo));

		return issueTokens(member);
	}

	/**
	 * 유저의 role을 member로 업데이트하고 액세스/리프레쉬 토큰을 재발급한다.
	 */
	@Transactional
	public AuthTokenResponse changeRoleToMember(final Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new JjinException(MemberErrorCode.MEMBER_NOT_FOUND));

		member.changeRole(Role.MEMBER);

		return issueTokens(member);
	}

	/**
	 * 리프레쉬 토큰으로 액세스 토큰을 재발급한다.
	 */
	public ReissueResponse reissue(final String refreshToken) {
		String accessToken = memberTokenService.reissueAccessToken(refreshToken);
		return ReissueResponse.of(accessToken);
	}

	/**
	 * 저장된 리프레쉬 토큰을 삭제하여 로그아웃 처리한다.
	 */
	public void logout(final Long memberId) {
		memberTokenService.deleteRefreshToken(memberId);
	}

	private AuthTokenResponse issueTokens(final Member member) {
		Authentication authentication = toAuthentication(member);
		String accessToken = jwtTokenProvider.issueAccessToken(authentication);
		String refreshToken = jwtTokenProvider.issueRefreshToken(authentication);
		memberTokenService.saveRefreshToken(member.getId(), refreshToken);

		return AuthTokenResponse.of(accessToken, refreshToken, member.getRole());
	}

	private Member registerNewMember(final GoogleUserInfo userInfo) {
		log.info("신규 구글 회원 가입: socialId={}, email={}", userInfo.sub(), userInfo.email());
		return memberRepository.save(
			Member.createSocialMember(userInfo.email(), userInfo.sub(), Role.ONBOARDING)
		);
	}

	private Authentication toAuthentication(final Member member) {
		return new MemberAuthentication(
			member.getId(),
			null,
			List.of(member.getRole().toGrantedAuthority())
		);
	}
}
