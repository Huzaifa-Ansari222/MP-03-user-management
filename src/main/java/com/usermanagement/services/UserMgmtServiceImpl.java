package com.usermanagement.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.usermanagement.binding.ActivateAccount;
import com.usermanagement.binding.Login;
import com.usermanagement.binding.User;
import com.usermanagement.entity.UserMaster;
import com.usermanagement.repo.UserMasterRepo;
import com.usermanagement.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	 private final UserMasterRepo userMasterRepo;
	 private EmailUtils emailUtils;

	 public UserMgmtServiceImpl (UserMasterRepo userMasterRepo){
		this.userMasterRepo = userMasterRepo;
	}
	
//	 user registration
	@Override
	public boolean saveUser(User user) {
		
//		#map dto to entity
		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity); //user dto to Entity
		
		String password = generateRandomPwd();
		
//		#set password before saving user data
		entity.setPassword(password);
		
//		#make account in-active
		entity.setAccStatus("In-Active");		
		
		UserMaster save = userMasterRepo.save(entity);
		
		String to = user.getEmail();
		String subject = "Registration Success";
		String body = readRegEmailBody(entity.getFullName(), entity.getPassword()); //pass fullname, tempPass

		emailUtils.sendEmail(to, subject, body);
		System.out.println(password); //
		return save.getUserId() != null; // saved user have id / if have id means data saved in db return true / else false

	}



//	make account active 
	@Override
	public boolean activateUserAcc(ActivateAccount activateAcc) {

//		mapping dto to enity only email and password on db table
		UserMaster entity = new UserMaster();
		entity.setEmail(activateAcc.getEmail());
		entity.setPassword(activateAcc.getTempPwd()); //to validate temp pwd
		
//		used to search DB using email and temp password values from this entity
//		'of' holds email + tempPwd as search criteria
		Example<UserMaster> of = Example.of(entity); // email + tempPwd

//		now searching in db with 'of' store email+tempPwd value
		List<UserMaster> findAll = userMasterRepo.findAll(of); //only 1 record come from db
		
//		if no value found in 'of' = invalid user 
		if(findAll.isEmpty() ) {
	        return false;		
	    }
		else {
//			.get(0) only 1 record come from findAll
			UserMaster userMaster = findAll.get(0);  // This is an update, not an insert.
			
//			for that particular record we set new password and status
			userMaster.setPassword(activateAcc.getNewPwd());
			userMaster.setAccStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}
	}

//	get all user data
	@Override
	public List<User> getAllUsers() {
 
		List<UserMaster> findAll = userMasterRepo.findAll();
		
		List<User> users = new ArrayList<>();
		
//		convert list of all UserMaster Entity to User dto response
		for(UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity , user);
			
			users.add(user); // adding all user to List
		}
		
		return users;
	}

//	edit user data
	@Override
	public User getUserById(Integer userId) {
//		1.validate user id exists
	    Optional<UserMaster> findById = userMasterRepo.findById(userId);

	    if (findById.isPresent()) {
	    		User user = new User(); //dto object
	        UserMaster userMaster = findById.get();   // returns actual UserMaster obj if id present
	        BeanUtils.copyProperties(userMaster, user); // entity to dto mapping
	        return user;
	    } 
		return null;
	}

//	delete user data
	@Override
	public boolean deleteUserById(Integer userId) {

		try {
			userMasterRepo.deleteById(userId);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

//	change user account status
	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
	    
		Optional<UserMaster> findById = userMasterRepo.findById(userId);

	    if (findById.isPresent()) {
	        UserMaster userMaster = findById.get();   // returns UserMaster object of same user where we get by findById
	        userMaster.setAccStatus(accStatus);
	        return true;
	    } 
		return false;
	}
	
//	login / String because if fail we want to see reason
	@Override
	public String login(Login login) {
		
		UserMaster entity = new UserMaster();
		
		entity.setEmail(login.getEmail());
		entity.setPassword(login.getPassword());
		
//		we can pass email and password both combine with ExampleOf / dynamic query
		Example<UserMaster> of = Example.of(entity);
		
//		with 'of' dynamic query find in db
		List<UserMaster> findAll = userMasterRepo.findAll(of);
		
		if(findAll.isEmpty()) {
			return "Invalid Credentials";
		} else {
			UserMaster userMaster = findAll.get(0); // get that data from db as row
			if (userMaster.getAccStatus().equals("Active")) {
				return "SUCCESS";
			}else {
				return "Account not activated";
			}
		}

	}

//	forgot pass send password to mail by email
	@Override
	public String forgotPwd(String email) {
		
		UserMaster entity = userMasterRepo.findByEmail(email);

		if (entity == null) {
		    return "Invalid Email";
		}

		// TODO: Send pwd to email

		return null;
		
//		Optional<UserMaster> byEmail = userMasterRepo.findByEmail(email);
//		
//		if(byEmail.isPresent()) {
//			UserMaster user = byEmail.get(); //optional give 1 obj data of UserMaster .we map raw db data to entity and use it
//			
//			String password = user.getPassword();
//			
//			sendPwdToMail(email,password);
//			
//			return true;
//		}
//		return false;
	}
	

	private String generateRandomPwd() {

//	    create a string of uppercase and lowercase characters and numbers
	    String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
	    String numbers = "0123456789";

//	    combine all strings
	    String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

//	    create random string builder
	    StringBuilder sb = new StringBuilder(); //sb is object not string

//	    create an object of Random class
	    Random random = new Random();

//	    specify length of random string
	    int length = 6;

	    for(int i = 0; i < length; i++) {

//	    generate random index number
	      int index = random.nextInt(alphaNumeric.length()); // pick any random length 0 to 61 

//	      get character specified by index
//	      from the string
	      char randomChar = alphaNumeric.charAt(index); //pick a index of char / index = 28 → 'c'

//	      append the character to string builder
	      sb.append(randomChar); //sb = "c"
	      
//	      before convert to string sb value
//	      sb = {'c','Z','3','P','q','9'}
	    }

	    return sb.toString(); // cZ3Pq9
	}
	
//	mail body content
	private String readRegEmailBody(String fullname, String tempPwd) {
		String fileName = "REG-EMAIL-BODY.txt";
		String url = "";
		String mailBody = null;
		
		try {
		    FileReader fr = new FileReader(fileName);
		    BufferedReader br = new BufferedReader(fr);
		    
//		    builder is better to use instead of buffer
		    StringBuffer buffer = new StringBuffer();
		    
//		   add all line to buffer 
		    String line = br.readLine();
		    while (line != null) {
		    		buffer.append(line);
		    		line = br.readLine();
			}
		    
		    br.close();
		    
		    mailBody = buffer.toString();
		    mailBody = mailBody.replace("{FULLNAME}", fullname);
		    mailBody = mailBody.replace("{TEMP-PWD}", tempPwd);
		    mailBody = mailBody.replace("{URL}", url);
		    
		} catch(Exception e) {
		    e.printStackTrace();
		}
		return mailBody;
	}

}
