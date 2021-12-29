package com.dev.rev.security;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dev.rev.auth.ApplicationUserService;

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
	
	@Value("${KEY_REMEMBER-ME}")
	String key;
	
	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserService applicationUserService;
	
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
	}
	
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
			.formLogin()
				.loginPage("/login").permitAll()
				.defaultSuccessUrl("/courses", true)
				.usernameParameter("username")
				.passwordParameter("password")
			.and()
			.rememberMe()
				.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) // Default: 2 semanas
				.key(key)
				.rememberMeParameter("remember-me")
			.and()
			.logout()
				.logoutUrl("/logout")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID","remember-me")
				.logoutSuccessUrl("/login");

	}
		@Bean
		public DaoAuthenticationProvider daoAuthenticationProvider() {
			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
			provider.setPasswordEncoder(passwordEncoder);
			provider.setUserDetailsService(applicationUserService);
			return provider;
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(daoAuthenticationProvider());
		}

	}
	
	

	
	

