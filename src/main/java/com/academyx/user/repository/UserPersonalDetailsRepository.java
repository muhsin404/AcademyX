package com.academyx.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academyx.user.model.UserPersonalDetails;

public interface UserPersonalDetailsRepository extends JpaRepository<UserPersonalDetails, Long>{

}
