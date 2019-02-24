package com.uiotsoft.micro.oauth2.config;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

//@Component
public class RemoteServiceAuthenticationProvider implements AuthenticationProvider,
		InitializingBean {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
		Object credentials = authentication.getCredentials();
		String password = credentials == null ? null : credentials.toString();
		UserDetails user = userDetailsService.loadUserByUsername(username);
		if(!username.equals(user.getPassword())){
			throw new BadCredentialsException("认证失败");
		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
		return new UsernamePasswordAuthenticationToken(username,
				password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}
	
	

}
