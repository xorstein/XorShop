package com.xorshop.admin.config;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xorshop.admin.pagin.PagingAndSortingArgumentResolver;

@Configuration
public class MvcConfig implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		//WebMvcConfigurer.super.addResourceHandlers(registry);
        String dirName="users-photo";
        //String dirName1="clients-photo";
		Path userPhotoDir=Paths.get(dirName);
		
		String userPhotosPath=userPhotoDir.toFile().getAbsolutePath();
		//System.out.println("hamza ");
		//System.out.println("userPhotosPath "+userPhotosPath+"");
		
//Path userPhotoDir1=Paths.get(dirName1);
		
		//String userPhotosPath1=userPhotoDir1.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/"+dirName+"/**").
		addResourceLocations("file:/"+userPhotosPath+"/");
		//registry.addResourceHandler("/"+dirName1+"/**").
		//addResourceLocations("file:/"+userPhotosPath1+"/");
		
		String dirImage="../category-images";
		Path categoryImageDir=Paths.get(dirImage);	
		String categoryImagePath=categoryImageDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/category-images/**").
		addResourceLocations("file:/"+categoryImagePath+"/");
		
		String dirLogoBrand="../brand-images";
		Path categoryLogoBrandDir=Paths.get(dirLogoBrand);	
		String categoryLogoBrandPath=categoryLogoBrandDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/brand-images/**").
		addResourceLocations("file:/"+categoryLogoBrandPath+"/");
		
		String dirImageProduct="../product-images";
		Path ImageParentProductDir=Paths.get(dirImageProduct);	
		String ImageParentProducdPath=ImageParentProductDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/product-images/**").
		addResourceLocations("file:/"+ImageParentProducdPath+"/");
		
		String dirLogo="../site-logo";
		Path LogoDirPath=Paths.get(dirLogo);	
		String LogoPath=LogoDirPath.toFile().getAbsolutePath();
		registry.addResourceHandler("/site-logo/**").
		addResourceLocations("file:/"+LogoPath+"/");
		
		
	}
	public void  exposeDirectory(String pathPattern,ResourceHandlerRegistry registry ) {
		Path path=Paths.get(pathPattern);
		String absolutePath=path.toFile().getAbsolutePath();
		
		String logicalPath=pathPattern.replace("../","/**");
		
		registry.addResourceHandler(logicalPath)
		   .addResourceLocations("file:/"+absolutePath+"/");
		
		
	}*/

}
