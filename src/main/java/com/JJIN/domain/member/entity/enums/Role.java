package com.JJIN.domain.member.entity.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	MEMBER("ROLE_MEMBER"),
	ADMIN("ROLE_ADMIN"),
	;

	private final String roleName;

	public static Role fromRoleName(String roleName) {
		for (Role role : values()) {
			if (role.roleName.equals(roleName)) {
				return role;
			}
		}
		return null;
	}

	public GrantedAuthority toGrantedAuthority() {
		return new SimpleGrantedAuthority(roleName);
	}

}
