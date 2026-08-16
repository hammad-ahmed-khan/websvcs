package com.extra.oms.sim.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.RequestWrapper;

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

import com.extra.oms.service.client.IOracleSIMClient;
import com.oracle.retail.integration.base.bo.fodcremodvo.v1.FodCreModVo;
import com.oracle.retail.integration.base.bo.fodmodvo.v1.FodModVo;
import com.oracle.retail.integration.base.bo.fodref.v1.FodRef;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.CreateFulfillmentOrderDelivery;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.DispatchFulfillmentOrderDelivery;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.FulfillmentOrderDeliveryPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.UpdateFulfillmentOrderDelivery;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer.Default;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;

@Configuration
@ComponentScan(basePackages = { "com.extra.oms.sim" })
@EnableWebMvc
@EnableTransactionManagement
@PropertySource(value = "classpath:application.properties")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	private static final JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource getDataSource(@Value("${db.jndi.sim}") String jndiName) {
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

	@Bean
	public RequestInterceptor createRequestInterceptor() {
		return new RequestInterceptor() {
			
			@Override
			public void apply(RequestTemplate template) {
				template.header("SOAPAction", " ");
				template.header("Content-Type", "text/xml;charset=UTF-8");
			}
		};
	}

	@Bean("oracleSIMClient")
	public IOracleSIMClient getOracleSIMClient(@Autowired RequestInterceptor interceptor, @Value("${sim.oralce.base.api.url}") String url) throws NoSuchMethodException, SecurityException {
		
		Map<String, QName> qNameMap = new HashMap<>();
		RequestWrapper wrapper = FulfillmentOrderDeliveryPortType.class.getMethod("dispatchFulfillmentOrderDelivery", FodRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(DispatchFulfillmentOrderDelivery.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = FulfillmentOrderDeliveryPortType.class.getMethod("createFulfillmentOrderDelivery", FodCreModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(CreateFulfillmentOrderDelivery.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = FulfillmentOrderDeliveryPortType.class.getMethod("updateFulfillmentOrderDelivery", FodModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(UpdateFulfillmentOrderDelivery.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
		
		return Feign.builder().retryer(new Default()).encoder(new SOAPEncoder(jaxbFactory, qNameMap)).decoder(new SOAPDecoder(jaxbFactory)).requestInterceptor(interceptor).target(IOracleSIMClient.class, url);
	}
}
