package com.xorshop.setting;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.xorshop.common.constants.Constants;
import com.xorshop.common.entity.menu.Menu;
import com.xorshop.common.entity.setting.Setting;
import com.xorshop.service.MenuService;
import com.xorshop.service.SettingService;

@Component
public class SettingFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingFilter.class);
	
	private SettingService service; 
	
	private MenuService menuService;
	@Autowired 
	public SettingFilter(SettingService service, MenuService menuService) {
		super();
		this.service = service;
		this.menuService = menuService;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
         LOGGER.info("SettingFilter | doFilter is called");
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String url = servletRequest.getRequestURL().toString();
		LOGGER.info("SettingFilter | doFilter | url : " + url);

		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") ||
				url.endsWith(".jpg")) {
			LOGGER.info("SettingFilter | doFilter | .css , .js , .png , . jpg | url : " + url);
			
			chain.doFilter(request, response);
			return;
		}
		
		loadGeneralSettings(request);
		loadMenuSettings(request);

		chain.doFilter(request, response);

	}
    private void loadMenuSettings(ServletRequest request) {
		
		LOGGER.info("SettingFilter | loadMenuSettings is called");
		
		List<Menu> headerMenuItems = menuService.getHeaderMenuItems();
		LOGGER.info("SettingFilter | loadMenuSettings | headerMenuItems size : " + headerMenuItems.size());
		request.setAttribute("headerMenuItems", headerMenuItems);

		List<Menu> footerMenuItems = menuService.getFooterMenuItems();
		LOGGER.info("SettingFilter | loadMenuSettings | footerMenuItems size : " + footerMenuItems.size());
		request.setAttribute("footerMenuItems", footerMenuItems);	
		
		List<Menu> headerMenuTopItems = menuService.getHeaderMenuTopItems();
		LOGGER.info("SettingFilter | loadMenuSettings | headerMenuTopItems size : " + headerMenuTopItems.size());
		request.setAttribute("headerMenuTopItems", headerMenuTopItems);
	}
	
	private void loadGeneralSettings(ServletRequest request) {
		
		LOGGER.info("SettingFilter | loadGeneralSettings is called");
		
		List<Setting> generalSettings = service.getGeneralSettings();

		generalSettings.forEach(setting -> {
			LOGGER.info("SettingFilter | loadGeneralSettings | generalSettings : " + generalSettings);
			request.setAttribute(setting.getKey(), setting.getValue());
		});

		request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);

	}

}
