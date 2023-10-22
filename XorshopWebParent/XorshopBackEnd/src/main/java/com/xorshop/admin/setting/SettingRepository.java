package com.xorshop.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.xorshop.common.entity.setting.Setting;
import com.xorshop.common.entity.setting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String>,PagingAndSortingRepository<Setting, String> {
 
	public List<Setting> findByCategory(SettingCategory category);
}
