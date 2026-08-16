package com.extra.einvoicing.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.extra.einvoicing.config.FeignMessageAuthConfig;
import com.extra.einvoicing.model.MessageInfo;
import com.extra.einvoicing.model.MessageResponse;

/**
 * @author aibrahim
 *
 */
@FeignClient(name = "emailService", url = "${email.config.url}", configuration = { FeignMessageAuthConfig.class })
public interface IEmailService {

	@PostMapping(path = "/email")
	public MessageResponse sendEmail(@RequestBody MessageInfo message);
}
