package com.xorshop.admin.setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xorshop.admin.currency.CurrencyRepository;
import com.xorshop.admin.helper.SettingHelper;
import com.xorshop.admin.user.AccountController;
import com.xorshop.admin.util.GeneralSettingBag;
import com.xorshop.common.constants.Constants;
import com.xorshop.common.entity.Currency;
import com.xorshop.common.entity.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SettingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	private SettingService service;
	
	private CurrencyRepository currencyRepo;
	
	@Autowired
	public SettingController(SettingService service, CurrencyRepository currencyRepo) {
		this.service = service;
		this.currencyRepo = currencyRepo;
	}
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		
		LOGGER.info("SettingController | listAll is called");
		
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		
		LOGGER.info("SettingController | listAll | listSettings : " + listSettings.toString());
		LOGGER.info("SettingController | listAll | listCurrencies : " + listCurrencies.toString());

		model.addAttribute("listCurrencies", listCurrencies);
		model.addAttribute("activelink","18");
		for (Setting setting : listSettings) {
			LOGGER.info("SettingController | listAll | setting.getKey() : " + setting.getKey() + " | setting.getValue() : " + setting.getValue());
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		
		model.addAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
		
		LOGGER.info("SettingController | listAll | S3_BASE_URI : " + Constants.S3_BASE_URI);

		return "settings/settings";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		
		LOGGER.info("SettingController | saveGeneralSettings is called");
		
		GeneralSettingBag settingBag = service.getGeneralSettings();
		
		LOGGER.info("SettingController | saveGeneralSettings | settingBag : " + settingBag.toString());


		SettingHelper.saveCurrencySymbol(request, settingBag,currencyRepo);

		SettingHelper.updateSettingValuesFromForm(request, settingBag.list(),service);
		ra.addFlashAttribute("message", "Les réglages généraux ont été sauvegardés.");
		ra.addAttribute("TabOpned","overview-tab");
		return "redirect:/settings";
	}
	@PostMapping("/settings/save_logos")
	public String saveLogosSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			@RequestParam("logo_dashboard") MultipartFile LD,
			@RequestParam("logo_footer") MultipartFile LF,
			@RequestParam("logo_login_site") MultipartFile LGS,
			@RequestParam("logo_login_dashboard") MultipartFile LLD,
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		
		LOGGER.info("SettingController | saveLogosSettings is called");
		
		GeneralSettingBag settingBag = service.getGeneralSettings();
		
		
		LOGGER.info("SettingController | saveLogosSettings | settingBag : " + settingBag.toString());

		SettingHelper.saveSiteLogo(multipartFile, settingBag,"SITE_LOGO");
		SettingHelper.saveSiteLogo(LD, settingBag,"DASHBOARD_LOGO");
		SettingHelper.saveSiteLogo(LGS, settingBag,"SITE_LOGIN_LOGO");
		SettingHelper.saveSiteLogo(LLD, settingBag,"DASHBOARD_LOGIN_LOGO");
		SettingHelper.saveSiteLogo(LF, settingBag,"SITE_FOOTER_LOGO");
		SettingHelper.updateSettingValuesFromForm(request, settingBag.list(),service);
		ra.addFlashAttribute("TabOpned","logo-tab");
		ra.addFlashAttribute("message", "Les logos ont été sauvegardés.");
		return "redirect:/settings";
	}

	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSetttings(HttpServletRequest request, RedirectAttributes ra) {
		
		LOGGER.info("SettingController | saveMailServerSetttings is called");
		
		List<Setting> mailServerSettings = service.getMailServerSettings();
		
		LOGGER.info("SettingController | saveMailServerSetttings | mailServerSettings : " + mailServerSettings.toString());
		
		SettingHelper.updateSettingValuesFromForm(request, mailServerSettings,service);
		ra.addFlashAttribute("TabOpned","mailServer-tab");
		ra.addFlashAttribute("message", "Les paramètres du serveur de messagerie ont été enregistrés");

		return "redirect:/settings";
	}

	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes ra) {
		
		LOGGER.info("SettingController | saveMailTemplateSetttings is called");
		
		List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
		
		LOGGER.info("SettingController | saveMailTemplateSetttings | mailTemplateSettings : " + mailTemplateSettings.toString());
		
		SettingHelper.updateSettingValuesFromForm(request, mailTemplateSettings,service);
		ra.addFlashAttribute("TabOpned","mailTemplates-tab");
		ra.addFlashAttribute("message", "Les paramètres du modèle de courrier ont été enregistrés");

		return "redirect:/settings";
	}
	
	@PostMapping("/settings/save_payment")
	public String savePaymentSetttings(HttpServletRequest request, RedirectAttributes ra) {
		
		LOGGER.info("SettingController | savePaymentSetttings is called");
		
		List<Setting> paymentSettings = service.getPaymentSettings();
		
		LOGGER.info("SettingController | savePaymentSetttings | paymentSettings : " + paymentSettings.toString());
		
		SettingHelper.updateSettingValuesFromForm(request, paymentSettings,service);
		ra.addFlashAttribute("TabOpned","payment-tab");
		ra.addFlashAttribute("message", "Les paramètres de paiement ont été sauvegardés.");
		return "redirect:/settings";

	}
	@PostMapping("/settings/save_recaptcha")
	public String savereCAPTCHASettings(
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		
        LOGGER.info("SettingController | savereCAPTCHASettings is called");
		
        List<Setting> mailServerSettings = service.getMailServerSettings();
		
		LOGGER.info("SettingController | savereCAPTCHASettings | mailTemplateSettings : " + mailServerSettings.toString());
		
		SettingHelper.updateSettingValuesFromForm(request, mailServerSettings,service);
		ra.addFlashAttribute("TabOpned","reCAPTCHA-tab");
		ra.addFlashAttribute("message", "Utilisez cette clé de site dans le code HTML de votre site destiné aux utilisateurs.");

		return "redirect:/settings";
	}
}
