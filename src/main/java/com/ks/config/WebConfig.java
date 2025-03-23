package com.ks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Marks this class as a configuration class for Spring
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Configure resource handlers for serving static files

//---------------------------------------------------------------------------------------------------------------------------

		registry.addResourceHandler("/uploads/category_images/**")
				.addResourceLocations("file:./uploads/category_images/");
//---------------------------------------------------------------------------------------------------------------------------

		registry.addResourceHandler("/uploads/product_images/**")
				.addResourceLocations("file:./uploads/product_images/");
//---------------------------------------------------------------------------------------------------------------------------

		registry.addResourceHandler("/uploads/profile_images/**")
				.addResourceLocations("file:./uploads/profile_images/");
	}
}