package com.xorshop.admin.user;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xorshop.admin.pagin.PagingAndSortingHelper;
import com.xorshop.admin.role.RoleRepository;
import com.xorshop.common.entity.Role;
import com.xorshop.common.entity.User;
import com.xorshop.common.exception.UserNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService implements IUserService{
	public static final int USER_PER_PAGE=5;
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public List<User> listAll(String keyword) {
		Sort lastnameSorting =  Sort.by("lastname").ascending();
		List<User> userList = new ArrayList<>();		
		if(!keyword.equals("null") ) {
			List<User>users=userRepo.findAll(keyword,lastnameSorting);
			for (User user : users) {
				userList.add(user);
			}		
		}
		else {

			List<User>users=userRepo.findAll(lastnameSorting);
			for (User user : users) {
				userList.add(user);
			}	
		}
		
		return userList;
	}
	
	@Override
	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}
	@Override
	public User save(User user) {
	boolean isUpdatingUser = (user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findByUserId(user.getId());
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
			
		} else {
			encodePassword(user);
		}
		
		return userRepo.save(user);
	}
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	@Override
	public boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepo.getUserByEmail(email);
		
		if (userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if (isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}
	@Override
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findByUserId(id);
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("l'utilisateur avec l'identifiant " + id + " est introuvable");
		}
	}
	@Override
	public void delete(Integer id) throws UserNotFoundException {
		User u = get(id);
		if (u == null || u.isDelete_status()) {
			throw new UserNotFoundException("l'utilisateur avec l'identifiant " + id + " est introuvable");
		} else {

			u.setDelete_status(true);
			userRepo.save(u);
		}
		
	}
	@Override
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
		
	}
	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, USER_PER_PAGE, userRepo);
		
	}
	@Override
	public User getByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepo.getUserByEmail(email);
	}
	@Override
	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findByUserId(userInForm.getId());

		if (!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}

		if (userInForm.getPhoto() != null) {
			userInDB.setPhoto(userInForm.getPhoto());
		}

		userInDB.setFirstname(userInForm.getFirstname());
		userInDB.setLastname(userInForm.getLastname());

		return userRepo.save(userInDB);
	}
	

	

}
