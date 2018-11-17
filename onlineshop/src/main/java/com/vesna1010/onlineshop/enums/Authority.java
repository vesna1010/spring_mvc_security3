package com.vesna1010.onlineshop.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

	USER, ADMIN;

	@Override
	public String getAuthority() {
		return this.name();
	}

}
