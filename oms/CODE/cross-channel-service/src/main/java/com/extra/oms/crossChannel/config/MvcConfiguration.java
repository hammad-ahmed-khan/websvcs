package com.extra.oms.crossChannel.config;

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
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = { "com.extra.oms.crossChannel" })
@EnableWebMvc
@EnableTransactionManagement
@PropertySource(value = "classpath:application.properties")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource getDataSource(@Value("${db.jndi.oms}") String jndiName) {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		return dataSourceLookup.getDataSource(jndiName);
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
}
