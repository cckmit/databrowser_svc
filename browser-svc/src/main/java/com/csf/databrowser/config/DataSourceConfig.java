package com.csf.databrowser.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "dataScope")
    @ConfigurationProperties(prefix = "spring.datasource.datascope")
    public DataSource jdCompanyDs() {
        return DataSourceBuilder.create().build();
    }

}
