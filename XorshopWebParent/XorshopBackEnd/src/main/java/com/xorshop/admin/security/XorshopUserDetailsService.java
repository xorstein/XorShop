package com.xorshop.admin.security;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.xorshop.admin.role.RoleRepository;
import com.xorshop.admin.user.UserRepository;
import com.xorshop.common.entity.Role;
import com.xorshop.common.entity.User;

public class XorshopUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	

	@Override
	public UserDetails loadUserByUsername(String emailid) throws UsernameNotFoundException {
		Sort IdSorting =  Sort.by("id").ascending();
		List<User>users=userRepo.findAll(IdSorting);	
		if(users.size()==0) {
			User userSaved=SaveIT();
			emailid=userSaved.getEmailid();
		}
		User user =userRepo.getUserByEmail(emailid);

		if (user!=null) {
			return new XorshopUserDetails(user);
		}
		throw new  UsernameNotFoundException("Impossible de trouver l'utilisateur avec e-mail: "+emailid);
	}

	private User SaveIT() {
		// TODO Auto-generated method stub
	
		User u= new User("mgnfco","Hamza","BOULKAMH","mgnfco@live.com","$2a$10$e1TQTaSZ.NxDBpw4Ip1JIOuxALXt.M65EzxLzZNCXlz6y90rxDydO");
		u.setDelete_status(false);;
		u.setEnabled(true);
		Role role=roleRepo.findById(1).get();
		u.addRole(role);
		return userRepo.save(u);
		
		
	}
}
