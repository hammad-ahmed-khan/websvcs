package com.logicinfo.oms.model;

/***********************************************************************
/* Name:  PaymentConfirmation webservice
/* Description:  OMS Customer Order Cancellation webservice
/* Company:  Logic Information Systems
/* Modification History:
/* Date         Name             Version   Modification Description
/* 5/1/2015   Sakshi Dubey       1.0
/* 7/11/2015   Sakshi Dubey                 updated of one of the oms system parameter name .
/* 30/11/2015   Sakshi Dubey                 Updated the payment_datetime datatype from date to dataTime in XSD.
/* 13/12/2015   Sakshi Dubey                 Fixed the deliver and bill address required error in case of transfer scenario
/* 18/12/2015   Sakshi Dubey                 while unreserving the qunatities passed source_loc instead of fulfill_loc.
/* 31/12/2015   Sakshi Dubey                 Put the validation to restirct payment confirmation of already paid or rejected orders.
/* 10/1/2016  Renuga Rajendran              Added timings in the logs
/* 18/1/2016  Sakshi Dubey              Updated the XSD to move line_no at item level and updated the response processing.
/* 07/3/2016  Sakshi Dubey              Commented the response processing for back order as 2 fulfillment object were goin into response
/* 17/3/2016  Sakshi Dubey              Added validation to restrict payment confirmation of failed orders
/* 28/3/2016  Sakshi Dubey              While calling RMS package if table is locked then sending TABLE_LOCKED error code.
/* 22/4/2016  Sakshi Dubey              Persisted combination id in oms_co_fulfill detail table.
/*  24/4/2016  Sakshi Dubey              Restricting the siebel call based on flag
/**********************************************************************/

import com.logicinfo.oms.ejb.OmsTempCoFo;

import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

import com.sun.xml.ws.developer.SchemaValidation;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

@SchemaValidation
@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ ObjectFactory.class })
@WebService(name = "PaymentConfirmationWebService", serviceName = "PaymentConfirmationWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "PaymentConfirmationWebService", wsdlLocation = "/WEB-INF/wsdl/PaymentConfirmationWebservice.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class PaymentConfirmationWebServiceImpl {
	public PaymentConfirmationWebServiceImpl() {
	}

	public final static Logger log = Logger.getLogger(com.logicinfo.oms.model.PaymentConfirmationWebServiceImpl.class.getName());

	@WebResult(name = "PaymentConfResponse", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
	@WebMethod
	public CoPaymentConfResponse processPaymentConf(@WebParam(name = "PaymentConf", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/") CoPaymentConf input)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException, Exception {
		log.info("Processing Payment Confirmation starts for " + input.getCustomerOrderNo());
		ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = new ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		OMSCustomerOrderBean omsCustomerOrderBean = new OMSCustomerOrderBean();
		CoPaymentConfResponse response = null;
		Boolean flag = false;
		BigDecimal omsCustOrdNo = null;
		boolean isPaymentProgress = false;

		Date date1 = new Date();
		log.info("Start Time " + date1);
		// String
		// payment_status=ValidatePayment.getTenderStatus(input.getCustomerOrderNo());
		// log.info("payment_status for customer order "+input.getCustomerOrderNo()+"is
		// "+payment_status);
		// if(payment_status.equalsIgnoreCase("PAYMNT_ALDRY_CONFD")||payment_status.equalsIgnoreCase("PAYMNT_INPROGRESS"))
		// {
		// return ValidatePayment.returnTenderStatusResponse(input,payment_status);
		// }
		try {
			log.info(" Getting the Customer Order No");
			omsCustOrdNo = omsCustomerOrderBean.getOmsCustOrdNo(input);

			isPaymentProgress = PersistPaymentRequest.checkPaymentProgress(input.getCustomerOrderNo(), omsCustOrdNo);
			if (isPaymentProgress) {
				return PersistPaymentRequest.returnTenderStatusResponse(input, "PaymentInProgress");
			} else {
				PersistPaymentRequest.persistInPaymentAuditTable(input.getCustomerOrderNo(), omsCustOrdNo);
			}
			// Step 1: Validate the input xml
			log.info(" Validate the input");
			omsCustomerOrderBean.validate(input);
			// Step 2: Persist in oms table
			log.info("persist Records into OmsCustOrdReserve Table");
			omsCustomerOrderBean.persistOmsCustOrdReserve(input);
			// added code for bug 2603
			log.info("<----------------Persist in OmsPaymentSync Table--------------->");
			omsCustomerOrderBean.persistOmsPaymentSync(omsCustOrdNo);
			// Step 3: Release the reserve quantity
			log.info("------unreserveQuantities---------->");
			omsCustomerOrderBean.unreserveQuantities(input);
			// Step 4:Call RMS/SIM webservices
			log.info("Processing the payment confirmation");
			fulfillDetailMap = omsCustomerOrderBean.processPaymentConfirmation(input);
			log.info("Calling SiebelOrderFeedInput");
			omsCustomerOrderBean.callSiebelOrderFeed(input);
		} catch (SOAPException e) {
			log.error("SOAP Exception occured " , e);
			log.info("Checking Payment status in OmsCustOrdHead Table");
			flag = omsCustomerOrderBean.checkOmsCustOrdHeadPaymentStatus();
			if (flag == Boolean.FALSE) {
				log.info("calling RollBack unReservation method");
				log.info("Before calling rollback for Timeout Exception : " + input.getCustomerOrderNo());
				omsCustomerOrderBean.rollbackForTimeout(omsCustOrdNo, input.getCustomerOrderNo());
				log.info("After calling rollback for Timeout Exception");
				// log.info("omsCustOrdNo "+omsCustOrdNo+"Deleting the record from paymentAudit
				// table in rollback");
				// PersistPaymentRequest.deletePaymentAuditTable(input.getCustomerOrderNo(),omsCustOrdNo);
				// log.info("omsCustOrdNo "+omsCustOrdNo+"Deleted the record from paymentAudit
				// table after rollback");
			}
			return omsCustomerOrderBean.createResponse(input, fulfillDetailMap, e.getMessage());
		} catch (Exception e) {
			log.error(" Exception occured" + e.getMessage());
			log.info("Checking Payment status in OmsCustOrdHead Table");
			flag = omsCustomerOrderBean.checkOmsCustOrdHeadPaymentStatus();
			log.info("Payment Status Result is" + flag);
			if (flag == Boolean.FALSE) {
				log.info("calling RollBack unReservation method");
				log.info("Before calling rollback for Timeout Exception : " + input.getCustomerOrderNo());
				omsCustomerOrderBean.rollbackForTimeout(omsCustOrdNo, input.getCustomerOrderNo());
				log.info("After calling rollback for Timeout Exception");
				// log.info("omsCustOrdNo "+omsCustOrdNo+"Deleting the record from paymentAudit
				// table in rollback");
				// PersistPaymentRequest.deletePaymentAuditTable(input.getCustomerOrderNo(),omsCustOrdNo);
				// log.info("omsCustOrdNo "+omsCustOrdNo+"Deleted the record from paymentAudit
				// table after rollback");
			}
			return omsCustomerOrderBean.createResponse(input, fulfillDetailMap, e.getMessage());
		}
		// Step 5:Send the response
		log.info("Processing ends");

		// Renuga
		log.info("inside if block for DC to DC");
		DCtoDCTransfer dctodc = new DCtoDCTransfer();
		log.info("object created for dc to dc for omsCustOrdNo " + omsCustOrdNo);
		dctodc.updateTsfNoandFulFillOrdNo(omsCustOrdNo, input.getCustomerOrderNo());
		response = omsCustomerOrderBean.createResponse(input, fulfillDetailMap, "S");
		log.info("response " + response.getMessageStatus() + " for omsCustOrdNo " + omsCustOrdNo + " customerOrderNo " + input.getCustomerOrderNo());

		Date date2 = new Date();
		log.info("End Time " + date2);
		log.info("omsCustOrdNo " + omsCustOrdNo + "Deleting the record from paymentAudit table");
		PersistPaymentRequest.deletePaymentAuditTable(input.getCustomerOrderNo(), omsCustOrdNo);
		log.info("omsCustOrdNo " + omsCustOrdNo + "Deleted the record from paymentAudit table");
		log.info(omsCustOrdNo + " Difference between start and end time " + (date2.getTime() - date1.getTime()) / 1000 + " seconds");
		log.info(omsCustOrdNo + "Processing Payment Confirmation ends for" + input.getCustomerOrderNo());

		return response;

	}

}
