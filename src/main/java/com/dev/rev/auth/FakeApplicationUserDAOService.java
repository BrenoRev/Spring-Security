package com.dev.rev.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.dev.rev.jwt.JwtConfig;
import com.dev.rev.security.ApplicationUserRole;
import com.google.common.collect.Lists;

@Repository
public class FakeApplicationUserDAOService implements ApplicationUserDAO {

	private final JwtConfig jwtConfig;
	private final PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public FakeApplicationUserDAOService(JwtConfig jwtConfig, PasswordEncoder passwordEncoder) {
		super();
		this.jwtConfig = jwtConfig;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
		return getApplicationUsers().stream().filter(x -> username.equals(x.getUsername()))
				.findFirst();
	}
	
	private List<ApplicationUser> getApplicationUsers(){
		List<ApplicationUser> applicationUsers = Lists.newArrayList(
				
				// USER
				new ApplicationUser(
						ApplicationUserRole.STUDENT.getGrantedAuthorities(), 
						jwtConfig.getUser(),
						passwordEncoder.encode(jwtConfig.getUser()), 
						true,
						true,
						true,
						true
						),
				// ADMIN
				new ApplicationUser(
						ApplicationUserRole.ADMIN.getGrantedAuthorities(), 
						jwtConfig.getAdmin(),
						passwordEncoder.encode(jwtConfig.getAdmin()), 
						true,
						true,
						true,
						true
						),
				// TRAINEE
				new ApplicationUser(
						ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities(), 
						jwtConfig.getTrainee(),
						passwordEncoder.encode(jwtConfig.getTrainee()), 
						true,
						true,
						true,
						true
						)
				);
		return applicationUsers;
	}

}
