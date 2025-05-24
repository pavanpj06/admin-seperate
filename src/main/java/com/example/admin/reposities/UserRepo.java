package com.example.admin.reposities;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.admin.entities.UserEntity;

import org.springframework.transaction.annotation.Transactional;


public interface UserRepo extends JpaRepository<UserEntity, Long>{

	
	
	@Modifying
	@Transactional
	@Query("UPDATE UserEntity u SET u.accountSw = :status WHERE u.id = :userId")
	public Integer updateTheAccount(@Param("userId") Integer userId, @Param("status") String status);


	
	
	public Optional<UserEntity> findByEmail(String email);
	
	
	
	
	public UserEntity findByEmailAndPassword(String email, String password);
	
	
	
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM user_creation_entity u WHERE u.id = :userId", nativeQuery = true)
	public Long deleteAccount(@Param("userId") Long userId);


	
}
