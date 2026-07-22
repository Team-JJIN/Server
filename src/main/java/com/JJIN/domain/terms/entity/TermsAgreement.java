package com.JJIN.domain.terms.entity;

import com.JJIN.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원이 어떤 약관에 동의(혹은 미동의)했는지 기록한다.
 * 기존 프로젝트 컨벤션에 따라 연관관계 대신 FK를 Long으로 보관한다.
 */
@Entity
@Table(name = "terms_agreement")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TermsAgreement extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Long termsId;

	@Column(nullable = false)
	private boolean agreed;

	public static TermsAgreement of(final Long memberId, final Long termsId, final boolean agreed) {
		return TermsAgreement.builder()
			.memberId(memberId)
			.termsId(termsId)
			.agreed(agreed)
			.build();
	}
}
