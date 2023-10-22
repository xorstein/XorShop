package com.xorshop.service.impl;

import java.util.List;

import com.xorshop.common.entity.EmailSettingBag;
import com.xorshop.common.entity.setting.Setting;
import com.xorshop.setting.CurrencySettingBag;
import com.xorshop.setting.PaymentSettingBag;

public interface ISettingService {
	public List<Setting> getGeneralSettings();
	public EmailSettingBag getEmailSettings();
	public CurrencySettingBag getCurrencySettings();
	public String getCurrencyCode();
	public PaymentSettingBag getPaymentSettings();

}
