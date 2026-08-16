package com.extra.oms.spareParts.config;

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

import com.extra.oms.service.client.IOracleInvAdjClient;
import com.extra.oms.service.client.IOracleSIMClient;
import com.oracle.retail.integration.base.bo.postrncoldesc.v1.PosTrnColDesc;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.ststsfrcvmodvo.v1.StsTsfRcvModVo;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;
import com.oracle.retail.integration.base.bo.ststsfreqmodvo.v1.StsTsfReqModVo;
import com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1.StsTsfShpModVo;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ApproveTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.DispatchTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ReadTransferDetail;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ReceiveTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.RequestTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SaveInProgressTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SaveInReceivingTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SaveTransferRequest;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferPortType;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SubmitTransfer;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer.Default;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.POSTransactionPortType;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.ProcessPOSTransactions;

@Configuration
@ComponentScan(basePackages = { "com.extra.oms.spareParts" })
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

	@Bean("sparePartsProcessor")
	public ThreadPoolExecutor createBackOrderProcessor() {
		return new ThreadPoolExecutor(5, 20, 1L, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(20000));
	}

	@Bean("oracleSIMClient")
	public IOracleSIMClient getOracleSIMClient(@Autowired RequestInterceptor interceptor, @Value("${sim.oralce.base.api.url}") String url) throws NoSuchMethodException, SecurityException {

		Map<String, QName> qNameMap = new HashMap<>();
		RequestWrapper wrapper = StoreToStoreTransferPortType.class.getMethod("approveTransfer", StsTsfRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(ApproveTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("saveTransferRequest", StsTsfReqModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(SaveTransferRequest.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("submitTransfer", StsTsfRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(SubmitTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("saveInReceivingTransfer", StsTsfRcvModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(SaveInReceivingTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("receiveTransfer", StsTsfRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(ReceiveTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("requestTransfer", StsTsfRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(RequestTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("readTransferDetail", StsTsfRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(ReadTransferDetail.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("saveInProgressTransfer", StsTsfShpModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(SaveInProgressTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = StoreToStoreTransferPortType.class.getMethod("dispatchTransfer", StsTsfRef.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(DispatchTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = POSTransactionPortType.class.getMethod("processPOSTransactions", PosTrnColDesc.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(ProcessPOSTransactions.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		wrapper = InventoryAdjustmentPortType.class.getMethod("saveAndConfirmInventoryAdjustment", StrAdjModVo.class).getAnnotation(RequestWrapper.class);
		qNameMap.put(SaveAndConfirmInventoryAdjustment.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

		return Feign.builder().retryer(new Default()).encoder(new SOAPEncoder(jaxbFactory, qNameMap)).decoder(new SOAPDecoder(jaxbFactory)).requestInterceptor(interceptor)
				.target(IOracleSIMClient.class, url);
	}

}
