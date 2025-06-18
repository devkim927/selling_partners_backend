package com.sellingPartners.backEnd.service;

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
}
