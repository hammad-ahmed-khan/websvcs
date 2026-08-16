package com.extra.einvoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class EInvoiceApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EInvoiceApplication.class, args);
	}
}
