package com.dev.rev.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.net.HttpHeaders;

import lombok.Data;

@ConfigurationProperties(prefix = "application.jwt")
@Data
@Configuration
public class JwtConfig {

	private String secretKey;
	private String tokenPrefix;
	private String tokenExpirationAfterDays;
	
	@Value("${SPRING_USER}")
	String user;
	
	@Value("${SPRING_ADMIN}")
	String admin;
	
	@Value("${SPRING_TRAINEE}")
	String trainee;
	
	@Value("${KEY_REMEMBER-ME}")
	String key;
	
	public String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}

	
}
