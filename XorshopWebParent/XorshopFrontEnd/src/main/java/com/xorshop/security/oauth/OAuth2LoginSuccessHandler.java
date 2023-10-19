package com.xorshop.security.oauth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.xorshop.common.entity.AuthenticationType;
import com.xorshop.common.entity.Customer;
import com.xorshop.service.CustomerService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);
	
	@Autowired 
	private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();

		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		String countryCode = request.getLocale().getCountry();
		String clientName = oauth2User.getClientName();
		
		LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  name : " + name);
		LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  email : " + email);
		LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  countryCode : " + countryCode);
		LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  clientName : " + clientName);
		
		AuthenticationType authenticationType = getAuthenticationType(clientName);

		Customer customer = customerService.getCustomerByEmail(email);
		if (customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
		} else {
			oauth2User.setFullName(customer.getFullName());
			customerService.updateAuthenticationType(customer, authenticationType);
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	private AuthenticationType getAuthenticationType(String clientName) {
		if (clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		} else if(clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		}
		else {
        return AuthenticationType.DATABASE;
		}
	}

}
