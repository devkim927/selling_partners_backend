package com.sellingPartners.backEnd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sellingPartners.backEnd.dto.SignUp;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<UserEntity> signup(@RequestBody SignUp signUp) {
		UserEntity newUser = userService.createUser(
				signUp.getUsername(),
				signUp.getPassword(),
				signUp.getRole(),
				signUp.getEmail(),
				signUp.getPhoneNumber()
		);
		return ResponseEntity.ok(newUser);
	}
}
