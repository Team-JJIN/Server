package com.JJIN.global.exception;

import com.JJIN.global.response.base.BaseCode;

import lombok.Getter;

@Getter
public class JjinException extends RuntimeException {
	private final BaseCode baseCode;

	public JjinException(BaseCode baseCode) {
		super(baseCode.getMessage());
		this.baseCode = baseCode;
	}
}
