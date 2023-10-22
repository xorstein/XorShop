package com.xorshop.admin.setting;



import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xorshop.common.entity.setting.Setting;
import com.xorshop.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class testCreateGeneralSettings {
	@Autowired 
	SettingRepository repo;
	@Test
	public void testCreateGeneralSettings() {
		//Setting siteName = new Setting("SITE_NAME", "Xorshop", SettingCategory.GENERAL);

		Setting DASHBOARD_LOGO = new Setting("DASHBOARD_LOGO", "Xorshop.png", SettingCategory.GENERAL);
		Setting SITE_FOOTER_LOGO = new Setting("SITE_FOOTER_LOGO", "Xorshop.png", SettingCategory.GENERAL);
		Setting SITE_LOGIN_LOGO = new Setting("SITE_LOGIN_LOGO", "Xorshop.png", SettingCategory.GENERAL);
		Setting SITE_DASHBOARD_LOGO = new Setting("SITE_DASHBOARD_LOGO", "Xorshop.png", SettingCategory.GENERAL);
		//Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2023 Xorstein.", SettingCategory.GENERAL);

		repo.saveAll(List.of(DASHBOARD_LOGO, SITE_FOOTER_LOGO, SITE_LOGIN_LOGO,SITE_DASHBOARD_LOGO));

		//Iterable<Setting> iterable = repo.findAll();

	
	}
	/*@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

		repo.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType, 
				decimalDigits, thousandsPointType));

	}*/
	/*@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);

		settings.forEach(System.out::println);
	}*/

}
