package com.xorshop.admin.brand;



import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;



@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	@Autowired
	private BrandRepository rp;
	@Autowired
	private TestEntityManager entitymanger;
	@Test
	public void CreatBrand1() {
		System.err.println("dzundza");
		List<Brand> iterableProducts = rp.findAll();
		System.err.println("taille "+iterableProducts.size());
		//iterableProducts.forEach(System.out::println);
	}

}
