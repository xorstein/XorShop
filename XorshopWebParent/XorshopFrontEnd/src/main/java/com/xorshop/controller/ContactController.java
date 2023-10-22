package com.xorshop.controller;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.xorshop.common.entity.Customer;
import com.xorshop.contact.Sender;
import com.xorshop.service.SettingService;
import com.xorshop.util.ContactUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContactController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	@Autowired 
	private SettingService settingService;
	
	@PostMapping("/send_email")
	public String ContactUsForm(Sender sender,Model model,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		LOGGER.info("ContactController | ContactUsForm is called");
		
		
		
		LOGGER.info("ContactController | ContactUsForm | Sender : " + sender);
		
		ContactUtil.sendEmail(request, sender, settingService);

		model.addAttribute("pageTitle", "Contacez-nous");

		return "contact/contact_success";
	}

}
