package com.usermanagement.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	
	private JavaMailSender mailSender;
	
//	tell msg sent or not
    public boolean sendEmail(String to, String subject, String body) {

    	boolean isMailSent = false;
    	try {
    		MimeMessage mimeMessage = mailSender.createMimeMessage();
    		
    		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    		
    		helper.setText(to);
    		helper.setSubject(subject);
//      helper.setText(body); // for only plain text
    		helper.setText(body, true); // for HTML tags & hyperLink
    		
    		mailSender.send(mimeMessage);
//     30:00
    		isMailSent = true;
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return isMailSent;
    }	
}

