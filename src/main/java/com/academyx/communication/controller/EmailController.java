package com.academyx.communication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.academyx.communication.model.EmailRequest;
import com.academyx.communication.service.EmailService;

import jakarta.mail.MessagingException;



@RestController
public class EmailController {

	 @Autowired
	  private EmailService emailService;
	
	 @PostMapping("/send-email")
	  public void sendEmail(@RequestBody EmailRequest request) throws MessagingException {
	    emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody().getGreeting(),
	            request.getBody().getMain(), request.getBody().getFooter());
	  }
}
