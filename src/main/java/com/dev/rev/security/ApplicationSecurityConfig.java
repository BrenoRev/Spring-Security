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
	
	@Value("${SPRING_USER}")
	String user;
	
	@Value("${SPRING_ADMIN}")
	String admin;
	
	@Value("${SPRING_TRAINEE}")
	String trainee;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Configuração basic auth  ( base 64 )
		http
			.csrf().disable() // Desativa o csrf
			.authorizeRequests()
			// Autorizar sem precisar de permissão
			.antMatchers("/", "index", "/css/*", "/js/*").permitAll()
			// Só permitir acessar se tiver a role
			.antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
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
			.username(user)
			.password(passwordEncoder.encode(user))
			.roles(ApplicationUserRole.STUDENT.name()) // ROLE_STUDENT
			.build();
		
		// Definindo um usuário como administrador
		UserDetails brenoUser = User.builder()
			.username(admin)
			.password(passwordEncoder.encode(admin))
			// Colocando as permissões de admin no usuario
			.roles(ApplicationUserRole.ADMIN.name()) // ROLE_ADMIN
			.build();
		
		UserDetails tomUser = User.builder()
				.username(trainee)
				.password(passwordEncoder.encode(trainee))
				// Colocando as permissões de admin trainee no trainee
				.roles(ApplicationUserRole.ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
				.build();
		
		return new InMemoryUserDetailsManager(annaSmithUser, brenoUser, tomUser);
		
	}
	
	

	
	
}
