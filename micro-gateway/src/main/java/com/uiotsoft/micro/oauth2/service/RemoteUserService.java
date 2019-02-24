package com.uiotsoft.micro.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uiotsoft.micro.oauth2.domain.OauthRepository;
import com.uiotsoft.micro.oauth2.domain.User;
import com.uiotsoft.micro.oauth2.domain.WdcyUserDetails;

@Service
public class RemoteUserService implements UserDetailsService {

	@Autowired
	private OauthRepository oauthRepository;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = oauthRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(
					"Not found any user for username[" + username + "]");
		}
		return new WdcyUserDetails(user.username(),user.password());
	}

}
