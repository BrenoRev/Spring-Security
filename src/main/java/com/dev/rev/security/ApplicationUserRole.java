package com.dev.rev.security;

import java.util.Set;

import com.google.common.collect.Sets;

import static com.dev.rev.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {

	// Definindo as permissões de cada role
	
	STUDENT(Sets.newHashSet()),
	ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
	ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));
	
	private final Set<ApplicationUserPermission> permissions;

	ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}

	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}
	 
}
