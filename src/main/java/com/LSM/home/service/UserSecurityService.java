package com.LSM.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.LSM.home.entity.SiteUser;
import com.LSM.home.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	//spring security
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		SiteUser siteUser = userRepository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
		return org.springframework.security.core.userdetails.User
				.withUsername(siteUser.getUsername())
				.password(siteUser.getPassword())
				.authorities("USER")
				.build();
	}
	
	
	
	
}
