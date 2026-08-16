package com.extra.homemaintenance.config;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.extra.homemaintenance.service.HomeMaintenanceAPI;

import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * HomeMaintenanceSyncConfiguration.java
 * aibrahim
 * 2024
 */
@Configuration
@ComponentScan(basePackages = "com.extra.homemaintenance")
@PropertySource(value = "classpath:application.properties")
public class HomeMaintenanceSyncConfiguration {

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

    @Bean("homemaintenanceAPI")
    public HomeMaintenanceAPI homemaintenanceAPIInstance(@Value("${homemaintenance.url}") String url, @Value("${homemaintenance.username}") String mUsername, @Value("${homemaintenance.password}") String mPassword) {
        return Feign.builder()
                .client(new Client.Default(getSSLSocketFactory(), getHostnameVerifier()))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(mUsername, mPassword))
                .target(HomeMaintenanceAPI.class, url);
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static TrustManager[] getTrustManager() {
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
