package com.sellingPartners.backEnd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sellingPartners.backEnd.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	Optional<UserEntity> findByUsername(String username);
}
