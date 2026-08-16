package com.extra.oms.core.config;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.RequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.extra.oms.common.AuthFilter;
import com.extra.oms.service.client.ICarreraClient;
import com.extra.oms.service.client.IMuleAddressClient;
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
import feign.Retryer;
import feign.Retryer.Default;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxb.JAXBContextFactory;
import feign.okhttp.OkHttpClient;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;

@Configuration
@ComponentScan(basePackages = {"com.extra.oms.core", "com.extra.oms.common.security", "com.extra.oms.einvoice"})
@EnableWebMvc
@PropertySource(value = "classpath:application.properties")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	private static final JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource getDataSource(@Value("${db.jndi.oms}") String jndiName, @Autowired ApplicationContext appContext) {
		AuthFilter.setJWTService(appContext);
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		return dataSourceLookup.getDataSource(jndiName);
	}

	@Bean("jndiTemplate")
	public NamedParameterJdbcTemplate getJdbcTemplate(@Autowired @Qualifier("dataSource") DataSource dataSource) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		JdbcTemplate template = ((JdbcTemplate) jdbcTemplate.getJdbcOperations());
		template.setFetchSize(100);
		template.setMaxRows(1001);
		return jdbcTemplate;
	}

	@Bean("rmsJndiTemplate")
	public NamedParameterJdbcTemplate getRmsJdbcTemplate(@Value("${rms.datasource.jdbc-url}") String jdbcURL, @Value("${rms.datasource.username}") String userName, @Value("${rms.datasource.password}") String password) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(new DriverManagerDataSource(jdbcURL, userName, password));
		JdbcTemplate template = ((JdbcTemplate) jdbcTemplate.getJdbcOperations());
		template.setFetchSize(100);
		template.setMaxRows(1001);
		return jdbcTemplate;
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

		return Feign.builder().retryer(new Default()).encoder(new SOAPEncoder(jaxbFactory, qNameMap)).decoder(new  SOAPDecoder(jaxbFactory)).requestInterceptor(interceptor).target(IOracleRMSClient.class, url);
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

	@Bean("carreraTransfer")
	public ICarreraClient getCarreraTransferProxy(@Value("${carrera.api.url}") String carreraURL) {
		return Feign.builder().encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder()).retryer(new Retryer.Default()).target(ICarreraClient.class, carreraURL);
	}

	@Bean()
	public IMuleAddressClient getMuleAddressClient(@Value("${mule.addr.api.url}") String addrURL, @Value("${mule.addr.api.username}") String username, @Value("${mule.addr.api.password}") String password) {
		return Feign.builder().encoder(new JacksonEncoder()).client(new OkHttpClient(new okhttp3.OkHttpClient.Builder().sslSocketFactory(getSSLSocketFactory(), getTrustManager()).build()) )
				.decoder(new JacksonDecoder()).requestInterceptor(new BasicAuthRequestInterceptor(username, password)).retryer(new Retryer.Default()).target(IMuleAddressClient.class, addrURL);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    return multipartResolver;
	}

	private static SSLSocketFactory getSSLSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, getTrustManagers(), new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static TrustManager[] getTrustManagers() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		} };
		return trustAllCerts;
	}
	
	private static X509TrustManager getTrustManager() {
		X509TrustManager trustAllCert = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		};
		return trustAllCert;
	}

	public static HostnameVerifier getHostnameVerifier() {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		};
		return hostnameVerifier;
	}
}
