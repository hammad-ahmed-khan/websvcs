package com.extra.imei.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = "com.extra.imei")
@EnableWebMvc
@PropertySource(value = "classpath:application.properties")
public class MVCConfiguration extends WebMvcConfigurerAdapter {

	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource dataSource(@Value("${datasource.url}") String url, @Value("${datasource.username}") String username, @Value("${datasource.password}") String password) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
	    return dataSource;
	}

	@Bean(name = "jdbcTemplate")
	public NamedParameterJdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
