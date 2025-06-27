package com.sellingPartners.backEnd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sellingPartners.backEnd.dto.LoginRequest;
import com.sellingPartners.backEnd.dto.SignUpRequest;
import com.sellingPartners.backEnd.entity.UserEntity;
import com.sellingPartners.backEnd.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	
	private final UserService userService;
	
	private final AuthenticationManager authenticationManager;
	
	@PostMapping("/signup")
	public ResponseEntity<UserEntity> signup(@RequestBody SignUpRequest signUp) {
		UserEntity newUser = userService.createUser(
				signUp.getUsername(),
				signUp.getPassword(),
				signUp.getRole(),
				signUp.getEmail(),
				signUp.getPhoneNumber(),
				signUp.getName()
		);
		return ResponseEntity.ok(newUser);
	}
	
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok("로그인 성공");
    }
}
