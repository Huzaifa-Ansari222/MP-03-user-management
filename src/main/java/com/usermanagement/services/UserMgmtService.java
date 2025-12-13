package com.usermanagement.services;

import java.util.List;

import com.usermanagement.binding.ActivateAccount;
import com.usermanagement.binding.Login;
import com.usermanagement.binding.User;
import com.usermanagement.entity.UserMaster;

public interface UserMgmtService {
	
//	registration
	public boolean saveUser (User user);

//	make account active 
	public boolean activateUserAcc(ActivateAccount activateAcc);
	
//	get all user data
	public List<User> getAllUsers();
	
//	edit user data
	public User getUserById(Integer userId);
	
//	delete user data
	public boolean deleteUserById(Integer userId);
	
//	change user account status
	public boolean changeAccountStatus(Integer userId,String accStatus);

//	login / String because if fail we want to see reason
	public String login(Login login);
	
//	check valid email for forget password
	public String forgotPwd(String email);

//	generate password
//	public String generateRandomPassword();
	
//	email sender for password
//	public boolean emailSender(String from, String to,String body);
	



}
