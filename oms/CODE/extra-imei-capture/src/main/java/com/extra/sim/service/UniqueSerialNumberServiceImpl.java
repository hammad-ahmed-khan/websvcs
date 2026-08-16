package com.extra.sim.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.extra.sim.dao.UniqueSerialNumberDao;
import com.extra.sim.model.EnteredSerialNumbers;
import com.extra.sim.model.ExtraFulfillmentOrderLineItem;
import com.extra.sim.model.ExtraIMEIFulfillmentOrderDelivery;
import com.extra.sim.model.IMEICancelDelivery;
import com.extra.sim.model.LookupUIN;
import com.extra.sim.model.SuccessResponse;
import com.extra.sim.model.UniqueSerialNumber;

/**
 * @author Madhuchandra
 */
@Service
@Transactional
public class UniqueSerialNumberServiceImpl implements UniqueSerialNumberService {

	@Autowired
	private UniqueSerialNumberDao dao;

	private final static Logger log = Logger.getLogger(UniqueSerialNumberServiceImpl.class.getName());

	@Override
	public SuccessResponse saveIMEI(List<UniqueSerialNumber> request) throws Exception {
		log.info("inside service");
		return dao.saveIMEI(request);
	}

	@Override
	public SuccessResponse cancelIMEI(List<UniqueSerialNumber> request) throws Exception {
		return dao.cancelIMEI(request);
	}

	@Override
	public SuccessResponse lookupIMEI(ExtraFulfillmentOrderLineItem request) throws Exception {
		return dao.lookupIMEI(request);
	}

	@Override
	public SuccessResponse updateIndicatorIMEI(ExtraIMEIFulfillmentOrderDelivery request) throws Exception {
		return dao.updateIndicatorIMEI(request);
	}

	@Override
	public SuccessResponse lookupUINenabled(LookupUIN request) throws Exception {
		return dao.lookupUINEnabled(request);
	}

	@Override
	public SuccessResponse lookupIMEIQty(UniqueSerialNumber request) throws Exception {
		return dao.lookupIMEIQty(request);
	}

	@Override
	public SuccessResponse cancelDelivery(IMEICancelDelivery request) throws Exception {
		return dao.cancelDelivery(request);
	}

	@Override
	public SuccessResponse deleteQuantityReverted(ExtraFulfillmentOrderLineItem request) throws Exception {
		return dao.deleteQuantityReverted(request);
	}

	@Override
	public SuccessResponse ping() throws Exception {
		return dao.ping();

	}

	@Override
	public EnteredSerialNumbers retrieveIMEI(UniqueSerialNumber request) throws Exception {
		return dao.retrieveIMEI(request);
	}

	@Override
	public SuccessResponse checkIfBOLexists(List<UniqueSerialNumber> request) throws Exception {
		return dao.checkIfBOLexists(request);
	}

}
