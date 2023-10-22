package com.xorshop.common.entity;

import java.beans.Transient;
import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.xorshop.common.constants.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "username", length = 255, nullable = false)
	private String username;
	@Column(name = "password", length = 255, nullable = false)
	private String password;
	@Column(name = "firstname", length = 255, nullable = false)
	private String firstname;
	@Column(name = "lastname", length = 255, nullable = false)
	private String lastname;
	@Column(length = 255, nullable = false, unique = true)
	private String emailid;
	
	@Column(name = "ltlog")
	private java.sql.Timestamp ltlog;

	private boolean delete_status;
	private boolean enabled;

	@Column(length = 64)
	private String photo;



	public User() {

		// TODO Auto-generated constructor stub
	}

	public User(String username,  String firstname, String lastname, String emailid,String password) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.emailid = emailid;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public java.sql.Timestamp getLtlog() {
		return ltlog;
	}

	public void setLtlog(java.sql.Timestamp ltlog) {
		this.ltlog = ltlog;
	}

	public boolean isDelete_status() {
		return delete_status;
	}

	public void setDelete_status(boolean delete_status) {
		this.delete_status = delete_status;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	@ManyToMany(fetch =FetchType.EAGER)
	@JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", emailid=" + emailid + ", roles=" + roles + "]";
	}
	@Transient
	public String getPhotosImagePath() {
		if(id==null || photo ==null) return "/assets/media/avatars/avatar10.jpg";
		else 
			 return Constants.S3_BASE_URI+ "/users-photo/"+this.id+"/"+this.photo;
	}
	@Transient
	public String getFullName() {
		
			return this.lastname+" "+this.firstname;
	}
	@Transient
	public String getPseudo() {
		
			return this.username;
	}


	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();

		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName)) {
				return true;
			}
		}

		return false;
	}

	
	

}

