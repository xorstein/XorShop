package com.xorshop.util;
import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.xorshop.common.entity.EmailSettingBag;
import com.xorshop.service.SettingService;

public class CustomerForgetPasswordUtil {
private static final Logger LOGGER = LoggerFactory.getLogger(CustomerForgetPasswordUtil.class);

public static void sendEmail(String link, String email, SettingService settingService) 
		throws UnsupportedEncodingException, MessagingException {
	
	LOGGER.info("ForgotPasswordController | sendEmail is called");
	
	
	EmailSettingBag emailSettings = settingService.getEmailSettings();
	
	LOGGER.info("CustomerForgetPasswordUtil | sendEmail | emailSettings : " + emailSettings.toString());
	
	JavaMailSenderImpl mailSender = CustomerRegisterUtil.prepareMailSender(emailSettings);
	
	LOGGER.info("CustomerForgetPasswordUtil | sendEmail | mailSender : " + mailSender.toString());

	String toAddress = email;
	String subject = "Voici le lien pour réinitialiser votre mot de passe";

	String content = "<p>Bonjour,</p>"
			+ "<p>Vous avez demandé à réinitialiser votre mot de passe.</p>"
			+ "Cliquez sur le lien ci-dessous pour changer votre mot de passe :</p>"
			+ "<p><a href=\"" + link + "\">Changer mon mot de passe</a></p>"
			+ "<br>"
			+ "<p>Ignorez cet e-mail si vous vous souvenez de votre mot de passe "
			+ "ou si vous n'avez pas effectué la demande.</p>";

	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
	
	helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
	helper.setTo(toAddress);
	helper.setSubject(subject);		

	helper.setText(content, true);
	
	LOGGER.info("CustomerForgetPasswordUtil | sendEmail | helper : " + helper.toString());
	LOGGER.info("CustomerForgetPasswordUtil | sendEmail | message : " + message.toString());
	
	mailSender.send(message);
}
}
