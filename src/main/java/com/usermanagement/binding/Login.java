package com.usermanagement.binding;

public class Login {
	
//	4. login page / REQUEST DTO
//	for page 5. emailId validate we use to send data from header beacuse its small data
	
	private String email;
	
	private String password; //password is sensitive data we use req dto 

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
