package com.extra.finance.service;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import net.schmizz.sshj.sftp.SFTPClient;

/**
 * @author aibrahim
 *
 */
@Service
public class FTPService {

	private static final Logger LOG = Logger.getLogger(FTPService.class);

	@Value("${ssh.remote-path}")
	private String remotePath;

	@Autowired
	ApplicationContext applicationContext;

	public void validateContractForOrders(List<String> ordNos) {
		LOG.info("Checking the contract files are available");
		try (SFTPClient sftpClient = applicationContext.getBean(SFTPClient.class)) {
			
			Iterator<String> ordIterator = ordNos.listIterator();
			while(ordIterator.hasNext()) {
				String ordNo = ordIterator.next();
				try {
					sftpClient.lstat(remotePath + "/" + "contract-" + ordNo + ".pdf");
				} catch (Exception e) {
					LOG.warn("File not availbale for the order number " + ordNo);
					ordIterator.remove();
				}
			}
			
		} catch (Exception e) {
			LOG.error("Error while reading the file names in remote location", e);
		}
	}
}
