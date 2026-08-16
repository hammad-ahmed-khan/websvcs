package com.extra.restservice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.extra.restservice")
@PropertySource(value = { "classpath:application.properties" })
public class ExtraAppConfig {
	
	@Autowired
    private Environment env;
 
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        return dataSource;
    }
 
    @Bean
    public NamedParameterJdbcTemplate namedjdbcTemplate(DataSource dataSource) {
    	NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        //jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
    	JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
    	return jdbcTemplate;
    }

}
