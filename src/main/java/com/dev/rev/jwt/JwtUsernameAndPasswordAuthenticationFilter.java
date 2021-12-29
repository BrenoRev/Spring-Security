package com.dev.rev.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			UsernameAndPasswordAuthenticationRequest authenticationRequest =
					new ObjectMapper()
					.readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
			
			Authentication authentication = 
			new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
													authenticationRequest.getPassword());
			
			Authentication authenticate = authenticationManager.authenticate(authentication);
			return authenticate;
			
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
			
		
	}
	
	

}
