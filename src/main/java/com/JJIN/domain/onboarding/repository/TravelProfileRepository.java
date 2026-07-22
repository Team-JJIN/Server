package com.JJIN.domain.onboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JJIN.domain.onboarding.entity.TravelProfile;

public interface TravelProfileRepository extends JpaRepository<TravelProfile, Long> {

	boolean existsByMemberId(Long memberId);

	Optional<TravelProfile> findByMemberId(Long memberId);
}
