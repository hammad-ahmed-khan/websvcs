package com.extra.oms.discount.config;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = {"com.extra.oms.discount"})
@EnableWebMvc
@PropertySource(value = "classpath:application.properties")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource getDataSource(@Value("${rms.datasource.url}") String url, @Value("${rms.datasource.username}") String userName, @Value("${rms.datasource.password}") String password) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, userName, password);
		return dataSource;
	}

	@Bean("jndiTemplate")
	public NamedParameterJdbcTemplate getJdbcTemplate(@Autowired @Qualifier("dataSource") DataSource dataSource) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		JdbcTemplate template = ((JdbcTemplate) jdbcTemplate.getJdbcOperations());
		template.setFetchSize(100);
		template.setMaxRows(1001);
		return jdbcTemplate;
	}
}
