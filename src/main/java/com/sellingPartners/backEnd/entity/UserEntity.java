package com.sellingPartners.backEnd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long id;
	
	private String username;
	private String email;
	private String password;
	private String name;
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public UserEntity(String username,String password, Role role,String phoneNumber,String email,String name) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.name = name;
	}
	
}
