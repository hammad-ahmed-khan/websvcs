package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OmsCustOrdHead;
/***********************************************************************
/* Name:  OMSCOCancellation webservice
/* Description:  OMS Customer Order Cancellation webservice
/* Company:  Logic Information Systems
/* Modification History:
/* Date         Name             Version   Modification Description
/* 5/1/2015   Sakshi Dubey       1.0
/* 18/12/2015   Sakshi Dubey                  On cancelltion of reserve order changing the ord_payment_status='R' (Rejected) in oms_cust_ord_head table and updating the close date time.
/* 23/12/2015   Sakshi Dubey                  On cancelltion of reserve order changing the payment_status_ind='R' (Rejected) in oms_cust_ord_tender table
/* 31/12/2015   Sakshi Dubey                  auto cancellation of shipping charge item
/* 3/01/2016   Sakshi Dubey                  Called SIM webservice for unreserving
/* 10/01/2016  Renuga Rajendran              Changed WSDL for item level message code and desc
/* 23/03/2016  Sakshi Dubey             in case of cancellation of reservation in oms_cust_ord_item ,cancelled_qty is not updating
/* 20/04/2016  Sakshi Dubey             Coded for back order cancelllation, and cancelllation of transfer
/* 21/04/2016  Sakshi Dubey             shifted the call to saveCustOrdLog method instead of impl class as even if failed in checking open delivery record getting inserted
/*04/05/2016  Sakshi Dubey             commneting auto cancelling of shipping charge item
/**********************************************************************/
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.sun.xml.ws.developer.SchemaValidation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.log4j.Logger;

@SchemaValidation
@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ ObjectFactory.class })
@WebService(name = "OMSCOCancellationWebService", serviceName = "OMSCOCancellationWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "OMSCOCancellationWebService", wsdlLocation = "/WEB-INF/wsdl/OMSCOCancellationWebService.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class OMSCOCancellationWebServiceImpl {
	public OMSCOCancellationWebServiceImpl() {
	}

	CustomerOrderCancellationResponse response;
	Properties props;
	private final static Logger log = Logger.getLogger(com.logicinfo.oms.model.OMSCOCancellationWebServiceImpl.class.getName());
	@Resource
	private WebServiceContext wsContext;

	@WebResult(name = "cancelOrderResponse", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
	@WebMethod
	public CustomerOrderCancellationResponse processCancellationOrder(
			@WebParam(name = "cancelOrder", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/") CustomerOrderCancellation input)
			throws SOAPException, EntityAlreadyExistsWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException {
		String serviceStatus = "";
		List<ErrorListResponse> errorCode = new ArrayList<ErrorListResponse>();
		COCancellationBean coCancellationBean = new COCancellationBean();
		try {
			log.info("------------------Cancellation start------------------");
			// Step 1:Validate the input xml
			coCancellationBean.validateCancelReqId(input);
			coCancellationBean.validateInput(input);
			errorCode = coCancellationBean.validateQty(input);
			log.info("errorCode size " + errorCode.size());
			if (errorCode.size() > 0) {
				serviceStatus = "FAILED";
				return coCancellationBean.generateErrorResponseList(input, serviceStatus, errorCode);
			}
			errorCode = coCancellationBean.checkLinkedItem(input);
			log.info("errorCode size " + errorCode.size());
			if (errorCode.size() > 0) {
				serviceStatus = "FAILED";
				return coCancellationBean.generateErrorResponseList(input, serviceStatus, errorCode);
			}
			// Step 2:Persist data in oms tables
			log.info("calling cancellationforShippingCharge method");
			OmsCustOrdHead omsCustOrdHead = coCancellationBean.cancellationforShippingCharge(input);
			coCancellationBean.saveCOCancellationDetails(input);
			coCancellationBean.checkItemExistence(input);
			// coCancellationBean.saveCustOrdLog(input);
			coCancellationBean.checkCreateOrReseveCancellation(input);
			// coCancellationBean.checkOpenDelivery(input);
			// coCancellationBean.saveCOCancellationItems(input);
			// coCancellationBean.processCancellation(input);
			coCancellationBean.checkItemStatus(input);
			if (!"HybrisCancellation".equals(input.getComments())) {
				log.info("calling from form");
				if ("S".equals(omsCustOrdHead.getOrdPaymentStatus()) && "S".equals(omsCustOrdHead.getStatus()) && input.getRefundPreference().equals("CLEARING") && BigDecimal.valueOf(19008).compareTo(omsCustOrdHead.getOrderRequestorId()) != 0 && input.getCancellationRequestorId().startsWith("1")) {				
					coCancellationBean.saveEInvoicingReq(input);
				}
				coCancellationBean.notifySiebel(input);
			}
			log.info("calling hybris website");
			// coCancellationBean.updateConfirmQty(input);
			log.info("------------------Cancellation ends-------------------");
		} catch (SOAPException se) {
			log.error("SOAPException" + se + "--" + se.getMessage());
			serviceStatus = "VALIDATE_ERROR";
			return coCancellationBean.generateResponse(input, serviceStatus, se.getMessage());
		} catch (Exception e) {
			log.error("Exception" + e);
			serviceStatus = "EXT_SYS_ERROR";
			log.error("e.getMessage()" + e.getMessage());
			return coCancellationBean.generateResponse(input, serviceStatus, e.getMessage());
		}
		serviceStatus = "SUCCESS";
		log.info("Cancellation ends------------------");
		return coCancellationBean.generateResponse(input, serviceStatus, "");
	}
}
