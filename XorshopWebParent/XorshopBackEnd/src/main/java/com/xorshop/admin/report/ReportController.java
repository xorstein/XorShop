package com.xorshop.admin.report;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.xorshop.admin.setting.SettingService;
import com.xorshop.admin.util.ReportUtil;

@Controller
public class ReportController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired 
	private SettingService settingService;
	
	
	@GetMapping("/reports")
	public String viewSalesReportHome(HttpServletRequest request,Model model) {
		
		LOGGER.info("ReportController | viewSalesReportHome is called");
		ReportUtil.loadCurrencySetting(request,settingService);
		model.addAttribute("activelink","10");
		model.addAttribute("pageTitle", "Rapports");
		return "reports/reports";
	}
	
	
}
