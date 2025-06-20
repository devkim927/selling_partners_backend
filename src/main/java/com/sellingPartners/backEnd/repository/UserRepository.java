package com.sellingPartners.backEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sellingPartners.backEnd.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	UserEntity findByUsername(String username);
}
