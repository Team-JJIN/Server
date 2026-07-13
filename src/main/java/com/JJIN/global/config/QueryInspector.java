package com.JJIN.global.config;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.JJIN.global.util.ApiQueryCounter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QueryInspector implements StatementInspector {

	private final ApiQueryCounter apiQueryCounter;

	@Override
	public String inspect(String sql) {
		if (RequestContextHolder.getRequestAttributes() != null) {
			apiQueryCounter.increaseCount();
		}
		return sql;
	}
}
