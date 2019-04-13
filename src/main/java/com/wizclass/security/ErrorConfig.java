package com.wizclass.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ErrorConfig implements WebMvcConfigurer {
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error403").setViewName("error/403");
		registry.addViewController("/error404").setViewName("error/404");
		registry.addViewController("/error405").setViewName("error/405");
	}

}
