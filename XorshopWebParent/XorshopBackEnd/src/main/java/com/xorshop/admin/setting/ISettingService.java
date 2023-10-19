package com.xorshop.admin.setting;

import java.util.List;

import com.xorshop.admin.util.GeneralSettingBag;
import com.xorshop.common.entity.setting.Setting;
import com.xorshop.common.entity.setting.Setting;

public interface ISettingService {

	public List<Setting> listAllSettings();
	
	public GeneralSettingBag getGeneralSettings();
	
	public void saveAll(Iterable<Setting> settings);
	
	public List<Setting> getMailServerSettings();
	
	public List<Setting> getMailTemplateSettings();

	public List<Setting> getCurrencySettings();

	public List<Setting> getPaymentSettings();
}
