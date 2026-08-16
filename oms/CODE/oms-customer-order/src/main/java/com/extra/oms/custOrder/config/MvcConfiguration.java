package com.extra.oms.custOrder.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

import com.extra.oms.service.client.ICarreraClient;
import com.extra.oms.service.client.IOracleRMSClient;
import com.extra.oms.service.client.IOracleSIMClient;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvModVo;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CreateFulfilOrdColDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderPortType;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.CreateInvBackOrdColDesc;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.InventoryBackOrderPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ApproveTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SavePendingTransferRequest;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferPortType;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer.Default;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;

@Configuration
@ComponentScan(basePackages = { "com.extra.oms.custOrder" })
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

	@Bean("backOrderProcessor")
	public ThreadPoolExecutor createBackOrderProcessor() {
		return new ThreadPoolExecutor(5, 20, 1L, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(20000));
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

	@Bean("oracleRMSClient")
	public IOracleRMSClient getOracleRMSClient(@Autowired RequestInterceptor interceptor, @Value("${rms.oralce.base.api.url}") String url) throws NoSuchMethodException, SecurityException {
		Map<String, QName> qNameMap = new HashMap<>();
		RequestWrapper wrapper = FulfillOrderPortType.class.getMethod("createFulfilOrdColDesc", FulfilOrdColDesc.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(CreateFulfilOrdColDesc.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = InventoryBackOrderPortType.class.getMethod("createInvBackOrdColDesc", InvBackOrdColDesc.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(CreateInvBackOrdColDesc.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = FulfillOrderPortType.class.getMethod("cancelFulfilOrdColRef", FulfilOrdColRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(CancelFulfilOrdColRef.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
	
		return Feign.builder().retryer(new Default()).encoder(new SOAPEncoder(jaxbFactory, qNameMap)).decoder(new SOAPDecoder(jaxbFactory)).requestInterceptor(interceptor).target(IOracleRMSClient.class, url);
	}

	@Bean("oracleSIMClient")
	public IOracleSIMClient getOracleSIMClient(@Autowired RequestInterceptor interceptor, @Value("${sim.oralce.base.api.url}") String url) throws NoSuchMethodException, SecurityException {
		
		Map<String, QName> qNameMap = new HashMap<>();
		RequestWrapper wrapper = StoreFulfillmentOrderPortType.class.getMethod("createFulfillmentOrderDetail", FulfilOrdColDesc.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(CreateFulfillmentOrderDetail.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = InventoryAdjustmentPortType.class.getMethod("saveAndConfirmInventoryAdjustment", StrAdjModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(SaveAndConfirmInventoryAdjustment.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("savePendingTransferRequest", StsTsfApvModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(SavePendingTransferRequest.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("approveTransfer", StsTsfRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(ApproveTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
		
		return Feign.builder().retryer(new Default()).encoder(new SOAPEncoder(jaxbFactory, qNameMap)).decoder(new SOAPDecoder(jaxbFactory)).requestInterceptor(interceptor).target(IOracleSIMClient.class, url);
	}

	@Bean("carreraClient")
	public ICarreraClient getCarreraClient(@Value("${rsb.api.url}") String url) {
		return Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder()).target(ICarreraClient.class, url);
	}
}
