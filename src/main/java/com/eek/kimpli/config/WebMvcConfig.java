package com.eek.kimpli.config;

import com.eek.kimpli.user.config.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


     @Autowired
    private UserInterceptor userInterceptor;



    @Value("${external.upload.path}")
    private String externalUploadPath;

    @Value("${external.upload.momentPath}")
    private String externalUploadMomentPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + externalUploadPath);

         registry.addResourceHandler("/uploads/moment/**")
                .addResourceLocations("file:" + externalUploadMomentPath);
    }


    //common.html에 회원 프로필사진

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor);
    }

}