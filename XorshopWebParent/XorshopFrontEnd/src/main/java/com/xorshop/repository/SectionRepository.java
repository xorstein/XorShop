package com.xorshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xorshop.common.entity.section.Section;

public interface SectionRepository extends JpaRepository<Section, Integer>{
	// list sections by enabled status and sorted by order in ascending order
	public List<Section> findAllByEnabledOrderBySectionOrderAsc(boolean enabled);
}
