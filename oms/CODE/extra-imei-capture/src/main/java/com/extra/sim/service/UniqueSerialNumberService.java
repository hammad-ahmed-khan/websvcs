package com.extra.sim.service;

import java.util.List;

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
public interface UniqueSerialNumberService {
	public SuccessResponse saveIMEI(List<UniqueSerialNumber> request) throws Exception;

	public SuccessResponse cancelIMEI(List<UniqueSerialNumber> request) throws Exception;

	public SuccessResponse lookupIMEI(ExtraFulfillmentOrderLineItem request) throws Exception;

	public SuccessResponse updateIndicatorIMEI(ExtraIMEIFulfillmentOrderDelivery request) throws Exception;

	public SuccessResponse lookupUINenabled(LookupUIN request) throws Exception;

	public SuccessResponse lookupIMEIQty(UniqueSerialNumber request) throws Exception;

	public SuccessResponse cancelDelivery(IMEICancelDelivery request) throws Exception;

	public SuccessResponse deleteQuantityReverted(ExtraFulfillmentOrderLineItem request) throws Exception;

	public SuccessResponse ping() throws Exception;

	public EnteredSerialNumbers retrieveIMEI(UniqueSerialNumber request)throws Exception;

	public SuccessResponse checkIfBOLexists(List<UniqueSerialNumber> request) throws Exception;


}
