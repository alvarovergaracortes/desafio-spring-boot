package cl.previred.common.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cl.previred.user.infrastructure.persistence.entity.UserEntity;

public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private final UserEntity userEntity;
	

	public UserDetailsImpl(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}
	
	@Override
	public String getPassword() {
		return userEntity.getPassword();
	}
	
	@Override
	public String getUsername() {
		return userEntity.getUsername();
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


	public Long getId() {
		return userEntity.getId();
	}

}