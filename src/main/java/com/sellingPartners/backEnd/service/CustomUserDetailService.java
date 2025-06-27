package com.sellingPartners.backEnd.service;

import java.util.Optional;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("MemberDetailService")
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    log.info("loadUserByUsername: {}", username);

	    UserEntity user = userRepository.findByUsername(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

	    return new org.springframework.security.core.userdetails.User(
	        user.getUsername(),
	        user.getPassword(),
	        AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().name())
	    );
	}
}
