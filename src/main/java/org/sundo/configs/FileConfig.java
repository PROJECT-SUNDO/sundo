package org.sundo.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {
	
	@Value("${file.upload.path}")
	private String filePath;
	
	@Value("${file.upload.url}")
	private String fileUrl;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler(fileUrl + "**")
				.addResourceLocations("file:///" + filePath);
		
	}
	
}
