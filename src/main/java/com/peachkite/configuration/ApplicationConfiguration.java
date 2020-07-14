package com.peachkite.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAsync
@EnableScheduling
@PropertySources({
    @PropertySource("classpath:application.properties"),
    @PropertySource("classpath:peachkite.properties")
})
public class ApplicationConfiguration extends WebMvcConfigurerAdapter{
	
	@Value("${resource.directory}")
	protected String resourceDirectory;

    @Value("${company.files.upload.location}")
    protected String companyFilesUploadLocation;

    @Value("${company.files.url.prefix}")
    protected String companyFilesUrlPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(resourceDirectory);
        registry.addResourceHandler(companyFilesUrlPrefix+"**")
                .addResourceLocations("file:"+companyFilesUploadLocation);

    }
	
}
