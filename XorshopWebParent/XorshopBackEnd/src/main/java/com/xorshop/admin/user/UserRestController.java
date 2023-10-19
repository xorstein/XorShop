package com.xorshop.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserRestController {
	@Autowired
	private UserService service;
	@PostMapping("/users/check_email")
	public String CheckDuplicateEmail(@Param("emailid")String emailid,@Param("id")Integer id) {
		return service.isEmailUnique(id,emailid) ?"ok" :"Duplicated";
		
	}

}
