package com.extra.finance.config;

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

/**
 * @author aibrahim
 *
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.extra.finance")
@PropertySource(value = "classpath:application.properties")
public class AppConfig {

	@Bean(name = "sshClient")
	public SSHClient getSSHClient(@Value("${ssh.host:51.144.176.165}") String hostname, @Value("${ssh.port:6060}") Integer port, @Value("${ssh.username:proccotasheel}") String username, @Value("${ssh.password:Pr0((0+@$hee!0903}") String password) throws Exception {
		SSHClient ssh = new SSHClient();
		ssh.addHostKeyVerifier(new HostKeyVerifier() {
			
			@Override
			public boolean verify(String hostname, int port, PublicKey key) {
				return true;
			}
			
			@Override
			public List<String> findExistingAlgorithms(String hostname, int port) {
				return Collections.emptyList();
			}
		});
        ssh.loadKnownHosts();
        ssh.connect(hostname, port);
        ssh.authPassword(username, password);
		return ssh;
	}

	@Bean(name = "sftpClient")
	@Scope("prototype")
	public SFTPClient getSFTPClient(@Autowired SSHClient sshClient) throws Exception {
		return sshClient.newSFTPClient();
	}

	@Bean(name = "dataSource", destroyMethod = "")
	public DataSource getDataSource(@Value("${db.url}") String url, @Value("${db.username}") String userName, @Value("${db.password}") String password) {
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
