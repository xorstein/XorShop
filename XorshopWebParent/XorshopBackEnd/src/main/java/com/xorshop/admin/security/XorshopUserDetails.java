package com.xorshop.admin.security;

import java.util.Collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.xorshop.common.entity.Role;
import com.xorshop.common.entity.User;

public class XorshopUserDetails implements UserDetails {
	private User user;

	public XorshopUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set <Role> roles=user.getRoles();
		List <SimpleGrantedAuthority> authorities  =new ArrayList();
		
		
		for(Role role:roles) {
			authorities.add( new SimpleGrantedAuthority(role.getName()));
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmailid();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}
	
	public String getFullname() {
		return user.getLastname()+" "+user.getFirstname();
	}
	public String getPseudo() {
		return user.getUsername();
	}
	public String getPhoto() {
		return user.getPhotosImagePath();
	}
	public String getRoles() {
		return user.getRoles().toString();
	}
	public void setFirstname(String firstname) {
		this.user.setFirstname(firstname);
	}
	public void setLastname(String lastname) {
		this.user.setLastname(lastname);
	}
	public void setPseudo(String pseudo) {
		this.user.setUsername(pseudo);
	}
	public void setPhoto(String photo) {
		this.user.setPhoto(photo);
	}
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
	@Override
	public String toString() {
		return "XorshopUserDetails [user=" + user + "]";
	}
	
	public User getUser() {
		return user;
	}

}
