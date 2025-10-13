package com.LSM.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LSM.home.entity.SiteUser;

import java.util.Optional;


public interface UserRepository extends JpaRepository<SiteUser, Long> {
	public Optional<SiteUser> findByUsername(String username);
	
}
