package com.extra.einvoicing.config;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gazt.einvoicing.hashing.generation.service.HashingGenerationService;
import com.gazt.einvoicing.hashing.generation.service.impl.HashingGenerationServiceImpl;
import com.zatca.sdk.service.flow.ApiValidationProcessorImpl;
import com.zatca.sdk.service.flow.ValidationProcessor;

/**
 * @author aibrahim
 *
 */
@Configuration
public class ApplicationConfiguration {

	@Bean("maxProcessCount")
	public Integer getProcessCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	@Bean
	public HashingGenerationService getHashingGenerationService() {
		return new HashingGenerationServiceImpl();
	}

	@Bean
	public ValidationProcessor getValidationProcessor() {
		return new ApiValidationProcessorImpl();
	}

	@Bean
	public ThreadPoolExecutor getInvoiceExecutor( ) {
		return new ThreadPoolExecutor(5, 5, 0, TimeUnit.HOURS, new LinkedBlockingQueue<>());
	}

	@Bean(name = "asyncTaskExecutor")
    public TaskExecutor taskExecutor () {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
        return executor;
    }
}
