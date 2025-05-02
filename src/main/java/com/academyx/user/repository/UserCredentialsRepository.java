package com.academyx.user.repository;

//import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.academyx.user.model.UserCredentials;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long>{
	
	@Query(value = "Select COUNT(u) > 0 from UserCredentials u where u.email = :email")
	boolean findEmailExistOrNot(@Param("email") String email);

	@Query("SELECT u FROM UserCredentials u WHERE u.email = :email")
	UserCredentials findUserByEmail(@Param("email")String email);

	@Query("SELECT u FROM UserCredentials u WHERE u.userToken = :userToken")
	UserCredentials verifyUser(@Param("userToken") String userToken);

	@Query("SELECT u FROM UserCredentials u WHERE u.userId = :userId AND u.status = 1")
	UserCredentials getActiveUserById(@Param("userId") Long userId);

}
