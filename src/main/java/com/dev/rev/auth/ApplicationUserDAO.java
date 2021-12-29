package com.dev.rev.auth;

import java.util.Optional;

public interface ApplicationUserDAO {

	Optional<ApplicationUser> selectApplicationUserByUsername(String username);
		
}
