package com.xorshop.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodeTest {
	@Test
	public void  TestEncodePassword() {
		BCryptPasswordEncoder passwordencoder =new BCryptPasswordEncoder();
		String mypassword="dzundza";
		String mypasswordhash=passwordencoder.encode(mypassword);
		System.out.println(mypasswordhash);
		boolean matches=passwordencoder.matches("dzundza",mypasswordhash);
		System.out.println(matches);
		
	}

}
