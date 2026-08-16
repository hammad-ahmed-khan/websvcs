/**
 * 
 */
package com.extra.imei.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.extra.imei.bean.IMEIValidateReq;
import com.extra.imei.bean.IMEIValidationRes;
import com.extra.imei.dao.IMEIDao;

/**
 * @author aibrahim
 *
 */
@Service
public class IMEIService {

	private static final Logger LOG = Logger.getLogger(IMEIService.class);

	@Autowired
	private IMEIDao imeiDao;

	public IMEIValidationRes validateIMEI(IMEIValidateReq validateReq) {
		LOG.info("Validating IMEI request");
		IMEIValidationRes response = new IMEIValidationRes();
		if (StringUtils.isEmpty(validateReq.getItem())) {
			response.setMessage("Item is null or empty.");
			LOG.error("Item number null or empty");
		} else if (StringUtils.isEmpty(validateReq.getImei())) {
			response.setMessage("IMEI is null or empty.");
			LOG.error("IMEI number null or empty");
		} else {
			LOG.info("Validating Item number '" + validateReq.getItem() + "' and IMEI '" + validateReq.getImei() + "'.");
			response.setStatus(imeiDao.validateIMEI(validateReq));
			LOG.info("Response received for Item number '" + validateReq.getItem() + "' IMEI '" + validateReq.getImei() + "' is " + response.isStatus());
			if (response.isStatus()) {
				response.setMessage("Valid Item and IMEI.");
			} else {
				response.setMessage("Invalid Item and IMEI number");
			}
		}
		return response;
	}
}
