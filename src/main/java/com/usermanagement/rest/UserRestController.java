package com.usermanagement.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagement.binding.ActivateAccount;
import com.usermanagement.binding.Login;
import com.usermanagement.binding.User;
import com.usermanagement.services.UserMgmtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
//@RequestMapping("/users")
public class UserRestController {
	private final UserMgmtService userMgmtService;

	public UserRestController(UserMgmtService userMgmtService) {
		this.userMgmtService = userMgmtService;
	}
//	registration
//	public boolean saveUser (User user);
	@PostMapping("/user")
	public ResponseEntity<String> userReg(@RequestBody User user){
		boolean isSavedUser = userMgmtService.saveUser(user);
		if(isSavedUser) {
			return new ResponseEntity<>("Registration sucess", HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	make account active 
//	public boolean activateUserAcc(ActivateAccount activateAcc);
	@PostMapping("/activate")
	public ResponseEntity<String> activateUserAcc(@RequestBody ActivateAccount activateUser){
		boolean activateUserAcc = userMgmtService.activateUserAcc(activateUser);
		if(activateUserAcc) {
			return new ResponseEntity<>("Account Activated", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Invalid temporary PWD", HttpStatus.BAD_REQUEST);
		}		

	}
	
	
//	get all user data
//	public List<User> getAllUsers();
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = userMgmtService.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}
	
	
//	edit user data
//	public User getUserById(Integer userId);
	@PutMapping("/user/{id}")
	public ResponseEntity<User> updateUseData(@PathVariable Integer id) {
		User userById = userMgmtService.getUserById(id);
		return new ResponseEntity<>(userById,HttpStatus.OK);
	}
	
//	delete user data
//	public boolean deleteUserById(Integer userId);
	@DeleteMapping("/user/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer id){
		boolean isDeleted = userMgmtService.deleteUserById(id);
		if(isDeleted) {
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);

		}		
		
	}
	
//	change user account status
//	public boolean changeAccountStatus(Integer userId,String accStatus);
	@PostMapping("/status/{id}/{status}")
	public ResponseEntity<String> chngeAccountStatus(
	        @PathVariable Integer id,
	        @RequestParam String status) {
		boolean changeAccountStatus = userMgmtService.changeAccountStatus(id, status);
		if(changeAccountStatus) {
			return new ResponseEntity<>("Status Changed", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Failed to changed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

//	login / String because if fail we want to see reason
//	public String login(Login login);
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {
		String status = userMgmtService.login(login);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	
//	check valid email for forget password
//	public String forgotPwd(String email);
	@PostMapping("/forgetPwd/{email}")
	public ResponseEntity<String> forgetPwd(@PathVariable String email) {
		String status = userMgmtService.forgotPwd(email);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
		
}
