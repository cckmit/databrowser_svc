package com.csf.databrowser.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class   PageHelperConfig {

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties p = new Properties();
        p.setProperty("helperDialect", "mysql");
        p.setProperty("reasonable", "true");
        pageInterceptor.setProperties(p);
        return pageInterceptor;
    }
}
