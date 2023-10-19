package com.xorshop.util;

import java.util.List;

import com.xorshop.common.entity.section.Section;
import com.xorshop.common.entity.section.SectionType;

public class SectionUtil {

	public static boolean hasAllCategoriesSection(List<Section> listSections) {
		// TODO Auto-generated method stub
		for (Section section : listSections) {
			if (section.getType().equals(SectionType.ALL_CATEGORIES)) {
				return true;
			}
		}

		return false;
	}

}
