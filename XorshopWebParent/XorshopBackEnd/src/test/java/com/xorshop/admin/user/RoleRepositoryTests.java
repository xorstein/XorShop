package com.xorshop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xorshop.admin.role.RoleRepository;
import com.xorshop.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repo;
	/*@Test
	public void testCreateFirstRole() {
		Role radmin= new Role("Admin","gérer l'ensemble du site");
		Role savedrole=repo.save(radmin);
		assertThat(savedrole.getId()).isGreaterThanOrEqualTo(0);
	}
	@Test
	public void testCreateRestRoles() {
		Role Vendeur= new Role("Vendeur","gérer les prix des produits, les clients, les expéditions, les commandes et les rapports de vente");
		Role Editeur= new Role("Editeur","gérer les catégories, les marques, les produits, les articles et les menus");
		Role Livreur= new Role("Livreur","visualiser les produits, les commandes et mettre à jour l'état des commandes");
		Role Assistant= new Role("Assistant","gérer les questions et les revues");
		repo.saveAll(List.of(Vendeur,Editeur,Livreur,Assistant));
		
	}*/
}
