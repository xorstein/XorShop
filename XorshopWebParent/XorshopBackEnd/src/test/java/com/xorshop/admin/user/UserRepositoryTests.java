package com.xorshop.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Rollback;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.xorshop.common.entity.Role;
import com.xorshop.common.entity.User;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository rp;
	@Autowired
	private TestEntityManager entitymanger;
	@Test
	public void testCreateUserWithTwoRole() {
	Role roleAdmin=entitymanger.find(Role.class, 1);
	User user1= new User("mgnfco","Hamza","BOULKAMH","mgnfco@live.com","password3");
	//User user1= new User("teemo","Darius","DEMACIA","teemo@live.com","password3");
	//Role role2= entitymanger.find(Role.class, 2);
	// role3= entitymanger.find(Role.class, 3);
	//Role role4= entitymanger.find(Role.class, 4);
	//Role role5= entitymanger.find(Role.class, 6);
	//Role comptable= new Role(4);
	//Role role1=  entitymanger.find(Role.class, 2);;
	//Role role2= entitymanger.find(Role.class, 5);;
	//Role roleAdmin=entitymanger.find(Role.class, 1);
	//user1.addRole(roleAdmin);
	//user1.addRole(comptable);
	 //user1.addRole(role2);
	//user1.addRole(role3);
	 //user1.addRole(role4);
	user1.addRole(roleAdmin);

	
	User saveduser=rp.save(user1);
	
	assertThat(saveduser.getId()).isGreaterThan(0);
}
	   
}
