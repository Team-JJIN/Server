package com.JJIN.global.auth.dto;

import com.JJIN.domain.member.entity.enums.Role;

public record CurrentAuth(Long memberId, Role role) {
}
