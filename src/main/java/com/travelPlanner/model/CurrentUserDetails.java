package com.travelPlanner.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.travelPlanner.persist.entity.User;

/**
 * A Model class that represents that data of the current user, and it implements Spring's {@link UserDetails}
 */
public class CurrentUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final static String ROLE_PREFIX = "ROLE_";
	private User user;
	private List<GrantedAuthority> authorities;

	public CurrentUserDetails(User user) {
		this.user = user;
		this.authorities = new ArrayList<>();

		if (user != null) authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().name()));
	}

	public User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
