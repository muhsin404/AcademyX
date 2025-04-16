package com.academyx.registration.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.academyx.registration.model.UserCredentials;

public interface RegistrationRepositoryForUserCredentials extends JpaRepository<UserCredentials, Long>{
	
	@Query("SELECT COUNT(u) > 0 FROM UserCredentials u WHERE u.email = :email")
	boolean findEmailExistOrNot(@Param("email") String email);


}
