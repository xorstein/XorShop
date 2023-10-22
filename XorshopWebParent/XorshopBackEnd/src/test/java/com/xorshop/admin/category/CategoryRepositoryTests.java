package com.xorshop.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.xorshop.admin.category.CategoryRepository;
import com.xorshop.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
	@Autowired
	private CategoryRepository repcategory;
	@Autowired
	private TestEntityManager entitymanger;
	/*@Test
	public void Creat() {
		Category parent=new Category(7);
		
		
		Category category= new Category ("Samsung",parent);
		Category category2= new Category ("Iphone",parent);
		//category2= new Category ("Laptops",parent);
		//rp.saveAll(List.of(category,category2));
		//Category category3= new Category ("Computer Components",parent);
		//Category savedCat=rp.save(category);
		rp.saveAll(List.of(category,category2));
		/*Category parent=new Category(1);
		category= new Category ("Desktops",parent);
		category2= new Category ("Laptops",parent);
		rp.saveAll(List.of(category,category2));*/
	    //assertThat(savedCat.getId()).isGreaterThan(0);
	//}*/
	
	/*private static void printChildren(Category parent, int subLevel) {
	    int newSubLevel=subLevel+1;
	    Set<Category> children = parent.getChildren();
	    for (Category SubCategory :children) {
	        for (int i = 0; i < newSubLevel; i++) {
	        	System.out.print("--");
	        }
	                 
	        System.out.println(SubCategory.getName());
	         
	        printChildren(SubCategory, newSubLevel );
	    }
	}*/
	/*@Test
	public void testPrintHierrarchicalCategories() {
		Iterable <Category> categories = rp.findAll();
		 
	    for (Category category:categories) {
	    	if(category.getParent()==null) {
	    		System.out.println(category.getName());
	    		
	    		Set<Category> children = category.getChildren();
	    		 for (Category Subcategory:children) {
	    				System.out.println("--"+Subcategory.getName());
	    				printChildren(Subcategory, 1);
	    		}
	    	}
	    }	 	   
		
	}
	/*@Test
	public void PrintCategories() {
		Category c= rp.findById(2).get();
		System.out.println(c.getName());
		Set<Category> children = c.getChildren();
		 for (Category Subcategory:children) {
				System.out.println(Subcategory.getName());
				
		}
		 assertThat(children.size()).isGreaterThan(0);
	}*/
	public List<Category> ListCategoriesUsedInForm() {
		List <Category> CategoriesUsedInForm= new ArrayList();
		List<Category> categgoriesInDb = (List<Category>) repcategory.findRootCategories(Sort.by("name").ascending());
		for (Category cat : categgoriesInDb) {
			if (cat.getParent() == null) {
				CategoriesUsedInForm.add(Category.copyIdAndName(cat));
				Set<Category> children =sortSubCategories( cat.getChildren());
				for (Category sub : children) {
					if(!sub.isDelete_status()) {
						String name = "  " + sub.getName();
						CategoriesUsedInForm.add(Category.copyIdAndName(sub.getId(), name));
						listSubCategoriesusedInForm( CategoriesUsedInForm,sub, 1);
					}
					
				}
			}

		}
		return CategoriesUsedInForm;
	}
	private void listSubCategoriesusedInForm(List<Category> CategoriesUsedInForm, Category parent, int sublevel) {
		int newsublevel = sublevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());
		
		for (Category category : children) {
			if(!category.isDelete_status()) {
				String name="";
				for (int i = 0; i < newsublevel; i++) {
					name+="  ";
				}
				name+=category.getName();
				CategoriesUsedInForm.add(Category.copyIdAndName(category.getId(), name));
				listSubCategoriesusedInForm(CategoriesUsedInForm,category,newsublevel);
			}
			
		}
	}
	private Set<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children,"asc");
	}
	 private SortedSet<Category> sortSubCategories(Set<Category> children,String sortDir){
			
			SortedSet<Category> sortedChildren= new TreeSet<>(new Comparator<Category>() {
		        @Override
		        public int compare(Category s1, Category s2) {
		        	if(sortDir.equals("asc")) {
		        		return s1.getName().compareTo(s2.getName());
		        	}
		        	else {
		        		return s2.getName().compareTo(s1.getName());
		        	}
		            
		        }
		    });
			sortedChildren.addAll(children);
			return sortedChildren;
			
			
		}
	 @Test
		public void testPrintHierrarchicalCategories() {
			List <Category> categories = ListCategoriesUsedInForm();
			 
		    for (Category category:categories) {
		    	System.out.println(category.getName());
		    }	 	   
			
		}
	

}
