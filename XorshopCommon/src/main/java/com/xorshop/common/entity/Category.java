package com.xorshop.common.entity;

import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xorshop.common.constants.Constants;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column( length = 128, nullable = false,unique=true)
	private String name;
	
	@Column( length = 64, nullable = false,unique=true)
	private String alias;
	
	@Column( length =128, nullable = false)
	private String image;
	
	@ManyToOne
	@JoinColumn(name="parent_id")
	private Category parent;
	
	@OneToMany( mappedBy="parent")
	private Set <Category>children=new HashSet<>();
	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;
	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	private boolean enabled;
	private boolean delete_status;
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
   

	public boolean isEnabled() {
		return enabled;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDelete_status() {
		return delete_status;
	}

	public void setDelete_status(boolean delete_status) {
		this.delete_status = delete_status;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Category() {
		
		
	}
	public Category(String name) {
	
		this.name = name;
		this.alias=name;
		this.image="default.png";
	}
	public Category(String name,Category parent) {
		this(name);
		this.parent=parent;
		
	}
	public Category(int id ) {
		// TODO Auto-generated constructor stub
		this.id=id;
	}



	
	public String getNameParent() {
		if(parent!=null)
		
			return parent.getName();
		else
			return null;

	}
	
	public int getIdParent() {
		if(parent!=null)
		
			return parent.getId();
		else
			return 0;

	}
	public static Category copyIdAndName(Category c) {
		Category copyCat=new Category();
		copyCat.setId(c.getId());
		copyCat.setName(c.getName());
		return copyCat;
	}
	public static Category copyIdAndName(int id,String name) {
		Category copyCat=new Category();
		copyCat.setId(id);
		copyCat.setName(name);
		return copyCat;
	}
	public static Category copyFull(Category c) {
		Category copyCat=new Category();
		copyCat.setId(c.getId());
		copyCat.setName(c.getName());
		copyCat.setAlias(c.getAlias());
		copyCat.setImage(c.getImage());
		copyCat.setEnabled(c.isEnabled());
		copyCat.setDelete_status(c.isDelete_status());
		copyCat.setHasChildren(c.getChildren().size()>0);
		
		return copyCat;
	}
	public static Category copyFull(Category c,String name) {
		Category copyCat=copyFull(c) ;		
		copyCat.setName(name);		
		return copyCat;
	}
	@Transient
	public String getCategoryImagePath() {
		if(image ==null || image.equals("default.png")) return "/assets/media/images/image-thumbnail.png";
		else 
			return Constants.S3_BASE_URI +"/category-images/"+this.id+"/"+this.image;
	}
	
	@Transient
	private boolean hasChildren;
	
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren (boolean hasCHildren) {
		this.hasChildren=hasCHildren;
	}
	
	
	
	
}
