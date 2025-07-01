package com.sellingPartners.backEnd.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sellingPartners.backEnd.entity.Role;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserEntity createUser(String username,String password,Role role, String phoneNumber,String email,String name) {
		String encodePw = passwordEncoder.encode(password);
		UserEntity user = new UserEntity(username, encodePw, role, phoneNumber, email, name);
		
		return userRepository.save(user);
	}
	
	public UserEntity getCurrentUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	        throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
	    }

	    String username = authentication.getName(); 

	    return userRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
	}
	
	// 로그인/비로그인만 판별 없으면 null 반환. - comet
	public UserEntity isAuthenticated() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	    	return null;
	    }

	    String username = authentication.getName(); 

	    return userRepository.findByUsername(username).orElse(null);
	    
	}			
}
