package com.dev.rev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
			
			/* Pode ser substituido por @PreAuthorize() nos mappings dos end-points
			 * É necessário colocar essa anotação na classe de configuração autorizando os pre authorizes
			   @EnableGlobalMethodSecurity(prePostEnabled = true)
			 
			// Só libera o delete, post e put para quem tiver as autorização dentro de suas respectivas roles
			
			.antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
			.antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
			.antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
			
			// Libera o método get para quem tiver a role admin ou admin trainee
			 
			.antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())
			
			*/
			
			.anyRequest()
			.authenticated()
			.and()
			// Form auth
			.formLogin();

	}

	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		// Definindo um usuário instanciado na memória do banco de dados com seu login, senha e role
		UserDetails annaSmithUser = User.builder()
			.username(user)
			.password(passwordEncoder.encode(user))
			//.roles(ApplicationUserRole.STUDENT.name()) // ROLE_STUDENT
			.authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
			.build();
		
		// Definindo um usuário como administrador
		UserDetails brenoUser = User.builder()
			.username(admin)
			.password(passwordEncoder.encode(admin))
			// Colocando as permissões de admin no usuario
			//.roles(ApplicationUserRole.ADMIN.name()) // ROLE_ADMIN
			.authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
			.build();
		
		UserDetails tomUser = User.builder()
				.username(trainee)
				.password(passwordEncoder.encode(trainee))
				// Colocando as permissões de admin trainee no trainee
				//.roles(ApplicationUserRole.ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
				.authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
				.build();
		
		return new InMemoryUserDetailsManager(annaSmithUser, brenoUser, tomUser);
		
	}
	
	

	
	
}
