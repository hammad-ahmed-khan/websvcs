package com.extra.apple.pricing.config;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.extra.apple.common.interceptor.HeaderInterceptor;
import com.extra.apple.pricing.service.AppleAPIService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import feign.Feign;
import feign.Logger.ErrorLogger;
import feign.Logger.Level;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;

@Configuration
@ComponentScan(basePackages = "com.extra.apple.pricing")
@EnableWebMvc
@EnableAsync
@PropertySource(value = "classpath:application.properties")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

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
		return jdbcTemplate;
	}
	
	@Bean("headerInterceptor")
	public HeaderInterceptor getInterceptor(@Value("${apple.pricing.secret}") String apiSecretKey, @Value("${apple.pricing.clientid}") String apiClientId) throws UnsupportedEncodingException {
		return new HeaderInterceptor(apiSecretKey, apiClientId);
	}

	@Bean("appleAPIService")
	public AppleAPIService getAppleAPIService(@Autowired @Qualifier("headerInterceptor") HeaderInterceptor interceptor, @Value("${apple.pricing.url.v2}") String url) {
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM/dd/yyyy HH:mm").create();
		return Feign.builder().requestInterceptor(interceptor).client(new OkHttpClient(new okhttp3.OkHttpClient.Builder().sslSocketFactory(getSSLSocketFactory(), getTrustManager()).build()) )
				.encoder(new GsonEncoder(gson)).decoder(new GsonDecoder(gson)).logger(new ErrorLogger()).logLevel(Level.FULL).target(AppleAPIService.class, url);
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
