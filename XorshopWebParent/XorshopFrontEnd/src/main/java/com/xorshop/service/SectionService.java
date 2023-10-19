package com.xorshop.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xorshop.common.entity.section.Section;
import com.xorshop.repository.SectionRepository;

@Service
@Transactional
public class SectionService {
	
	@Autowired 
	private SectionRepository repo;

	public List<Section> listEnabledSections() {
		return repo.findAllByEnabledOrderBySectionOrderAsc(true);
	}
	
	
}
