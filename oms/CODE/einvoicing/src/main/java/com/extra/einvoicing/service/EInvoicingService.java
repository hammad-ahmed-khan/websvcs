package com.extra.einvoicing.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * @author aibrahim
 *
 */
@Service
public class EInvoicingService {

	private static Logger LOG = LoggerFactory.getLogger(EInvoicingService.class);

	@Autowired
	private B2BInvoicingService b2bInvoicingService;

	@Autowired
	private B2CXmlGenerationService b2cXmlGenerationService;
	
	@Autowired
	private B2CReportingService b2cReportingService;

	@Scheduled(cron = "${schedule.ebs.cron}")
	public void generateEBSInvoice() throws InterruptedException {
		LOG.info("Starting the EBS invoice process...");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("1. B2B EBS Process");
		b2bInvoicingService.processEBSInvoice();
		stopWatch.stop();
		LOG.warn(stopWatch.prettyPrint());
		LOG.info("EBS invoice clearance process completed succesfully");
	}

	@Scheduled(cron = "${schedule.canret.cron}")
	public void generateB2CXml() {
		LOG.info("Start processing B2C cancellation and return invoices");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("3. B2C OMS Process");
		b2cXmlGenerationService.generateB2CXml();
		stopWatch.stop();
		LOG.warn(stopWatch.prettyPrint());
		LOG.info("Processing B2C cancellation and return invoices completed");
	}

	@Scheduled(cron = "${schedule.pos.cron}")
	public void generateB2BPOSInvoice() {
		LOG.info("Start processing B2B POS invoices");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("2. B2B POS Process");
		b2bInvoicingService.processPOSInvoice();
		stopWatch.stop();
		LOG.warn(stopWatch.prettyPrint());
		LOG.info("Processing B2B POS invoices completed");
	}

	@Scheduled(cron = "${schedule.report.cron}")
	public void reportB2CInvoice() {
		LOG.info("Start reporting B2C invoices");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("4. B2C XML Reporting");
		b2cReportingService.reportInvoice();
		stopWatch.stop();
		LOG.warn(stopWatch.prettyPrint());
		LOG.info("Reporting B2C invoices completed");
	}

	@Scheduled(cron = "${schedule.siebel.cron}")
	public void generateSiebelInvoice() {
		LOG.info("Start processing B2C Siebel invoices");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("5. B2C Siebel Process");
		b2cXmlGenerationService.processSiebelInvoice();
		stopWatch.stop();
		LOG.warn(stopWatch.prettyPrint());
		LOG.info("Processing B2C Siebel invoices completed");
	}
	
	@Scheduled(cron = "${schedule.email.cron}")
	public void resendEmail() {
		LOG.info("Start sending email");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("6. EBS and POS Email Process");
		b2bInvoicingService.processResendEmail();
		stopWatch.stop();
		LOG.warn(stopWatch.prettyPrint());
		LOG.info("Email sent succesfully");
	}
	
}
