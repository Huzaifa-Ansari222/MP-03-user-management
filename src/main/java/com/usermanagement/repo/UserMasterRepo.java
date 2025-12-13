package com.usermanagement.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usermanagement.binding.User;
import com.usermanagement.entity.UserMaster;

@Repository
public interface UserMasterRepo extends JpaRepository<UserMaster, Integer> {

	
	UserMaster findByEmail(String currentUser);

	UserMaster save(User user);

	Optional<User> findPwdByEmail(String email);

}
