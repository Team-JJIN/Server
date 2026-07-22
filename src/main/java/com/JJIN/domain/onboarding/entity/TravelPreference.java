package com.JJIN.domain.onboarding.entity;

import com.JJIN.domain.onboarding.entity.enums.TravelCategory;
import com.JJIN.domain.onboarding.entity.enums.TravelSubcategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원이 선택한 여행 취향 한 건(대분류 + 중분류).
 */
@Entity
@Getter
@Table(
	name = "travel_preference",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_travel_preference_profile_category_subcategory",
		columnNames = {"profile_id", "category", "subcategory"}
	)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPreference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profile_id", nullable = false)
	private TravelProfile profile;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private TravelCategory category;

	@Enumerated(EnumType.STRING)
	@Column(name = "subcategory", nullable = false)
	private TravelSubcategory subcategory;

	private TravelPreference(
		final TravelProfile profile,
		final TravelCategory category,
		final TravelSubcategory subcategory
	) {
		this.profile = profile;
		this.category = category;
		this.subcategory = subcategory;
	}

	static TravelPreference create(
		final TravelProfile profile,
		final TravelCategory category,
		final TravelSubcategory subcategory
	) {
		return new TravelPreference(profile, category, subcategory);
	}
}
