package com.dev.rev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${SPRING_KEY}")
	String senha;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Configuração basic auth  ( base 64 )
		http
			.authorizeRequests()
			// Autorizar sem precisar de permissão
			.antMatchers("/", "index", "/css/*", "/js/*")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
	}

	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		// Definindo um usuário instanciado na memória do banco de dados com seu login, senha e role
		UserDetails annaSmithUser = User.builder()
			.username(senha)
			.password(passwordEncoder.encode(senha))
			.roles("STUDENT") // ROLE_STUDENT
			.build();

		
		return new InMemoryUserDetailsManager(annaSmithUser);
		
	}
	
	

	
	
}
