package com.xorshop.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.xorshop.common.entity.Customer;
import com.xorshop.common.entity.EmailSettingBag;
import com.xorshop.contact.Sender;
import com.xorshop.service.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

public class ContactUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContactUtil.class);
	public static void sendEmail(HttpServletRequest request, Sender sender, SettingService settingService)
	        throws UnsupportedEncodingException, MessagingException {
	    LOGGER.info("ContactUtil | sendEmail is called");

	    EmailSettingBag emailSettings = settingService.getEmailSettings();

	    LOGGER.info("ContactUtil | sendEmail | emailSettings : " + emailSettings.toString());

	    JavaMailSenderImpl mailSender = prepareMailSender(emailSettings);

	    LOGGER.info("ContactUtil | sendEmail | mailSender : " + mailSender.toString());

	    String fromAddress = sender.getEmail();
	    String subject = "Nouveau message via le formulaire de contact "+sender.getFullName();
	    String content = sender.getMessage();

	    LOGGER.info("ContactUtil | sendEmail | fromAddress : " + fromAddress);
	    LOGGER.info("ContactUtil | sendEmail | subject : " + subject);
	    LOGGER.info("ContactUtil | sendEmail | content : " + content);

	    MimeMessage message = mailSender.createMimeMessage();

	    LOGGER.info("ContactUtil | sendEmail | MimeMessage message : " + message.toString());

	    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

	    LOGGER.info("ContactUtil | sendEmail | MimeMessageHelper helper : " + helper.toString());
	

	    helper.setFrom(fromAddress, sender.getFullName() + " <" + fromAddress + ">");
	    helper.setTo(emailSettings.getFromAddress());
	    helper.setSubject(subject);

	    LOGGER.info("ContactUtil | sendEmail | MimeMessageHelper helper : " + helper.toString());

	   

	    LOGGER.info("ContactUtil | sendEmail | content : " + content);

	    String siteURL = getSiteURL(request);

	    LOGGER.info("ContactUtil | sendEmail | siteURL : " + siteURL);

	    content = content.replace("[[URL]]", siteURL);

	    LOGGER.info("ContactUtil | sendEmail | content : " + content);

	    helper.setText(content, true);

	    LOGGER.info("ContactUtil | sendEmail | MimeMessageHelper helper : " + helper.toString());

	    mailSender.send(message);

	    LOGGER.info("ContactUtil | sendEmail | to Address : " + fromAddress);
	    LOGGER.info("ContactUtil | sendEmail | Site URL : " + siteURL);
	}

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		
		LOGGER.info("ContactUtil | prepareMailSender is called");
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		mailSender.setDefaultEncoding("utf-8");

		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

		mailSender.setJavaMailProperties(mailProperties);
		
		LOGGER.info("ContactUtil | prepareMailSender | mailSender : " + mailSender.toString());

		return mailSender;
	}
public static String getSiteURL(HttpServletRequest request) {
		
		LOGGER.info("ContactUtil | getSiteURL is called");
		
		String siteURL = request.getRequestURL().toString();
		
		LOGGER.info("ContactUtil | getSiteURL | siteURL :" + siteURL);
		LOGGER.info("ContactUtil | getSiteURL | request.getServletPath() :" + request.getServletPath());
		LOGGER.info("ContactUtil | getSiteURL | siteURL.replace(request.getServletPath(), \"\") :" 
		+ siteURL.replace(request.getServletPath(), ""));
		
		return siteURL.replace(request.getServletPath(), "");
	}

}
