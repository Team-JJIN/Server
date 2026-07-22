package com.JJIN.domain.onboarding.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JJIN.domain.member.dto.response.AuthTokenResponse;
import com.JJIN.domain.member.entity.Member;
import com.JJIN.domain.member.exception.MemberErrorCode;
import com.JJIN.domain.member.repository.MemberRepository;
import com.JJIN.domain.member.service.AuthService;
import com.JJIN.domain.onboarding.dto.request.CategoryPreferenceRequest;
import com.JJIN.domain.onboarding.dto.request.OnboardingCompleteRequest;
import com.JJIN.domain.onboarding.dto.response.OnboardingCompleteResponse;
import com.JJIN.domain.onboarding.entity.TravelProfile;
import com.JJIN.domain.onboarding.entity.enums.TravelSubcategory;
import com.JJIN.domain.onboarding.exception.OnboardingErrorCode;
import com.JJIN.domain.onboarding.repository.TravelProfileRepository;
import com.JJIN.domain.onboarding.validator.OnboardingRequestValidator;
import com.JJIN.global.exception.JjinException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

	private final MemberRepository memberRepository;
	private final TravelProfileRepository travelProfileRepository;
	private final OnboardingRequestValidator onboardingRequestValidator;
	private final AuthService authService;

	/**
	 * 온보딩에서 수집한 여행 기본 정보를 저장하고, 회원 역할을 ONBOARDING에서 MEMBER로 변경한다.
	 * 저장과 역할 변경은 하나의 트랜잭션으로 처리하며, 토큰 발급 이후에는 DB 작업을 두지 않는다.
	 */
	@Transactional
	public OnboardingCompleteResponse complete(
		final Long memberId,
		final OnboardingCompleteRequest request
	) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new JjinException(MemberErrorCode.MEMBER_NOT_FOUND));

		if (travelProfileRepository.existsByMemberId(memberId)) {
			throw new JjinException(OnboardingErrorCode.ONBOARDING_ALREADY_COMPLETED);
		}

		onboardingRequestValidator.validate(request);

		TravelProfile profile = saveProfile(member, request);

		// 역할 변경 및 토큰 재발급 (마지막 단계). 이후 실패 가능한 DB 작업을 두지 않는다.
		AuthTokenResponse tokens = authService.changeRoleToMember(memberId);

		log.info("온보딩 완료: memberId={}, onboardingId={}", memberId, profile.getId());

		return OnboardingCompleteResponse.of(
			profile.getId(),
			tokens.accessToken(),
			tokens.refreshToken(),
			tokens.role()
		);
	}

	/**
	 * existsByMemberId 검사와 저장 사이의 동시 요청은 막을 수 없으므로,
	 * member_id 유니크 제약 위반을 중복 온보딩 예외로 바꿔준다.
	 */
	private TravelProfile saveProfile(
		final Member member,
		final OnboardingCompleteRequest request
	) {
		try {
			return travelProfileRepository.save(toProfile(member, request));
		} catch (DataIntegrityViolationException e) {
			throw new JjinException(OnboardingErrorCode.ONBOARDING_ALREADY_COMPLETED);
		}
	}

	private TravelProfile toProfile(
		final Member member,
		final OnboardingCompleteRequest request
	) {
		TravelProfile profile = TravelProfile.create(
			member,
			request.region(),
			request.isRegionUndecided(),
			request.startDate(),
			request.endDate(),
			request.activityStartTime(),
			request.activityEndTime(),
			request.transportMode(),
			request.experienceLevel()
		);

		for (CategoryPreferenceRequest preference : request.preferences()) {
			for (TravelSubcategory subcategory : preference.subcategories()) {
				profile.addPreference(preference.category(), subcategory);
			}
		}
		return profile;
	}
}
