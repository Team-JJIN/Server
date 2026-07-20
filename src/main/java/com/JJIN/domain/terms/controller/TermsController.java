package com.JJIN.domain.terms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JJIN.domain.member.exception.MemberSuccessCode;
import com.JJIN.domain.terms.dto.response.TermsResponse;
import com.JJIN.domain.terms.service.TermsService;
import com.JJIN.global.response.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermsController {

	private final TermsService termsService;

	@GetMapping
	public ResponseEntity<SuccessResponse<List<TermsResponse>>> getTerms() {
		List<TermsResponse> terms = termsService.getAllTerms();
		return ResponseEntity.ok(SuccessResponse.of(MemberSuccessCode.TERMS_FETCH_SUCCESS, terms));
	}
}
