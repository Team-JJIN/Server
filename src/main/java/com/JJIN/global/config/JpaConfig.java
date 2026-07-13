package com.JJIN.global.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaConfig {

	private final QueryInspector queryInspector;

	@Bean
	public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
		return properties -> properties.put(AvailableSettings.STATEMENT_INSPECTOR, queryInspector);
	}
}
