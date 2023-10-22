package com.xorshop.controller;
import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.exception.CustomerNotFoundException;
import com.xorshop.service.CustomerService;
import com.xorshop.service.SettingService;
import com.xorshop.util.CustomerForgetPasswordUtil;
import com.xorshop.util.CustomerRegisterUtil;
@Controller
public class ForgotPasswordController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordController.class);
	
	private CustomerService customerService;
	
	private SettingService settingService;
	
	@Autowired
	public ForgotPasswordController(CustomerService customerService, SettingService settingService) {
		super();
		this.customerService = customerService;
		this.settingService = settingService;
	}

	@GetMapping("/forgot_password")
	public String showRequestForm(Model model) {
		
		LOGGER.info("ForgotPasswordController | showRequestForm is called");
		model.addAttribute("pageTitle", "Mot de passe oublié");
		return "customer/forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		
		LOGGER.info("ForgotPasswordController | processRequestForm is called");
		
		String email = request.getParameter("email");
		model.addAttribute("pageTitle", "Mot de passe oublié");
		LOGGER.info("ForgotPasswordController | processRequestForm | email : " + email);
		
		try {
			String token = customerService.updateResetPasswordToken(email);
			
			LOGGER.info("ForgotPasswordController | processRequestForm | token : " + token);
			
			String link = CustomerRegisterUtil.getSiteURL(request) + "/reset_password?token=" + token;
			
			LOGGER.info("ForgotPasswordController | processRequestForm | link : " + link);
			
			CustomerForgetPasswordUtil.sendEmail(link, email, settingService);

			model.addAttribute("message", "Nous avons envoyé un lien de réinitialisation du mot de passe à votre adresse e-mail."
					);
		} catch (CustomerNotFoundException e) {
			
			LOGGER.info("ForgotPasswordController | processRequestForm | error : " + e.getMessage());
			
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			
			LOGGER.info("ForgotPasswordController | processRequestForm | error : " + "Could not send email");
			
			model.addAttribute("error", "Impossible d'envoyer l'e-mail");
		}

		return "customer/forgot_password_form";
	}


	@GetMapping("/reset_password")
	public String showResetForm(@RequestParam("token") String token, Model model) {
		
		LOGGER.info("ForgotPasswordController | showResetForm is called");
		model.addAttribute("pageTitle", "Réinitialiser le mot de passe");
		Customer customer = customerService.getByResetPasswordToken(token);
		if(customer!=null)
		LOGGER.info("ForgotPasswordController | showResetForm | customer : " + customer.toString());
		
		if (customer != null) {
			
			LOGGER.info("ForgotPasswordController | showResetForm | token : " + token);
			
			model.addAttribute("token", token);
		} else {
			
			model.addAttribute("pageTitle", "Jeton invalide");
			model.addAttribute("message", "Jeton invalide");
			
			return "customer/forgot_password_form";
			
	
		}

		return "customer/reset_password_form";
	}

	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		
		LOGGER.info("ForgotPasswordController | processResetForm is called");
		model.addAttribute("pageTitle", "Réinitialiser le mot de passe");
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		
		LOGGER.info("ForgotPasswordController | processResetForm | token : " + token);
		LOGGER.info("ForgotPasswordController | processResetForm | password : " + password);

		try {
			customerService.updatePassword(token, password);

			model.addAttribute("pageTitle", "Réinitialiser le mot de passe");
			model.addAttribute("title", "Réinitialiser votre mot de passe");
			model.addAttribute("message", "Vous avez changé votre mot de passe avec succès.");
			
			LOGGER.info("ForgotPasswordController | processResetForm | pageTitle : " + "Reset Your Password");
			LOGGER.info("ForgotPasswordController | processResetForm | title : " + "Reset Your Password");
			LOGGER.info("ForgotPasswordController | processResetForm | message : " + "You have successfully changed your password.");
			

		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", e.getMessage());
			
			LOGGER.info("ForgotPasswordController | processResetForm | pageTitle : " + "Invalid Token");
			LOGGER.info("ForgotPasswordController | processResetForm | message : " + e.getMessage());
			
		}	

		return "customer/forgot_password_form";	
	}
}
