package com.cxl.cloud.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns=new String[]{"/login.shtml","/user/login","logout"};
        registry.addInterceptor(new SysInterceptor()).addPathPatterns("/**").excludePathPatterns(patterns);
    }
}
