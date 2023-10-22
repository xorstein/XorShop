package com.xorshop.admin.util;

import java.util.List;

import com.xorshop.common.entity.setting.Setting;
import com.xorshop.common.entity.setting.SettingBag;

public class GeneralSettingBag extends SettingBag {

	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}

	public void updateSiteLogo(String name,String value) {
		super.update(name, value);
	}
}
