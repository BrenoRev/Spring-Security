package com.dev.rev.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.dev.rev.security.ApplicationUserRole;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository("fake")
public class FakeApplicationUserDAOService implements ApplicationUserDAO {

	@Value("${SPRING_USER}")
	String user;
	
	@Value("${SPRING_ADMIN}")
	String admin;
	
	@Value("${SPRING_TRAINEE}")
	String trainee;
	
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
		return getApplicationUsers().stream().filter((x) -> username.equals(x.getUsername()))
				.findFirst();
	}
	
	private List<ApplicationUser> getApplicationUsers(){
		List<ApplicationUser> applicationUsers = Lists.newArrayList(
				
				// USER
				new ApplicationUser(
						ApplicationUserRole.STUDENT.getGrantedAuthorities(), 
						passwordEncoder.encode(user), 
						user,
						true,
						true,
						true,
						true
						),
				// ADMIN
				new ApplicationUser(
						ApplicationUserRole.ADMIN.getGrantedAuthorities(), 
						passwordEncoder.encode(admin), 
						admin,
						true,
						true,
						true,
						true
						),
				// TRAINEE
				new ApplicationUser(
						ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities(), 
						passwordEncoder.encode(trainee), 
						trainee,
						true,
						true,
						true,
						true
						)
				);
		return applicationUsers;
	}

}
