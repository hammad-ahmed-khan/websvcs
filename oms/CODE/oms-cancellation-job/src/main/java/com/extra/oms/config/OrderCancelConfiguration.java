package com.extra.oms.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.extra.oms.service.OMSCancellationAPI;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * OrderCancelConfiguration.java
 * aibrahim
 * 2024
 */
@Configuration
@ComponentScan(basePackages = "com.extra.oms")
@PropertySource(value = "classpath:application.properties")
public class OrderCancelConfiguration {

    @Bean(name = "dataSource", destroyMethod = "")
    public DataSource getDataSource(@Value("${db.driver}") String dbDriver, @Value("${db.url}") String dbURL, @Value("${db.username}") String dbUserName, @Value("${db.password}") String dbPassword) {
        DriverManagerDataSource bds = new DriverManagerDataSource();
        bds.setDriverClassName(dbDriver);
        bds.setUrl(dbURL);
        bds.setUsername(dbUserName);
        bds.setPassword(dbPassword);
        return bds;
    }

    @Bean("jndiTemplate")
    public NamedParameterJdbcTemplate getJdbcTemplate(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        JdbcTemplate template = ((JdbcTemplate) jdbcTemplate.getJdbcOperations());
        template.setFetchSize(1000);
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManager txManager(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("cancellationAPI")
    public OMSCancellationAPI cancellationAPIInstance(@Value("${oms.cancal.api.url}") String url) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OMSCancellationAPI.class, url);
    }
}
