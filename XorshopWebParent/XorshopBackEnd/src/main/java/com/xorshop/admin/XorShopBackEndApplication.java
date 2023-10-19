package com.xorshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.xorshop.common.entity","com.xorshop.admin.user"})
public class XorShopBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(XorShopBackEndApplication.class, args);
	}

}
