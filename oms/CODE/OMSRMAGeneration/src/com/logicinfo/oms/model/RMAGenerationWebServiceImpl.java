package com.logicinfo.oms.model;
/***********************************************************************
/* Name:  OMS RMA Generation webservice                                                     
/* Description:  OMS RMA Generation webservice                                                                  
/* Company:  Logic Information Systems                                                                  
/* Modification History:                                              
/* Date         Name               Version   Modification Description             
/* 11/12/2014   Sakshi Dubey       1.0  
/* 27/04/2015   Sakshi Dubey       1.1       Removed entity beans and session ejb to OMSUtil project and created client
                                             for 
/**********************************************************************/


import java.math.BigDecimal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.ValidationWSFaultException;
import com.sun.xml.ws.developer.SchemaValidation;

@SchemaValidation
@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ ObjectFactory.class })
@WebService(name = "RMAGenerationWebService", serviceName = "OMSRMAGenerationWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "OMSRMAGenerationWebService", wsdlLocation = "/WEB-INF/wsdl/OMSRMAGenerationWebservice.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
//@Transactional
public class RMAGenerationWebServiceImpl {
	public RMAGenerationWebServiceImpl() {
	}

	String rma = "";
	private final static Logger log = Logger.getLogger(RMAGenerationWebServiceImpl.class.getName());

	@WebResult(name = "GenerateNewRMAResponse", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
	@WebMethod
	public CustomerOrderRMAResponse generateNewRMA(@WebParam(name = "GenerateNewRMA", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/") CustomerOrderRMA input)
			throws SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		String serviceStatus = "";
		log.info("****RMA generation processing starts***");
		RMAGenerationBean rmsGenerationBean = new RMAGenerationBean();
		try {
			String requestStoreId = rmsGenerationBean.validate(input);
			boolean b = rmsGenerationBean.checkDuplicate(input);
			if (b == true) {
				rmsGenerationBean.saveRMA(input);
				rmsGenerationBean.saveRMAItems(input);
				rmsGenerationBean.callRWMSWebService(input, requestStoreId);
			}
		} catch (SOAPException e) {
			log.error("SOAP exception error ", e);
			serviceStatus = "VALIDATE_ERROR";
			return rmsGenerationBean.createResponse(input, serviceStatus, e.getMessage());
		} catch (Exception e) {
			log.error("Unknown error ", e);
			serviceStatus = "EXT_SYS_ERROR";
			return rmsGenerationBean.createResponse(input, serviceStatus, e.getMessage());
		}
		serviceStatus = "SUCCESS";
		return rmsGenerationBean.createResponse(input, serviceStatus, "SUCCESS");
	}
}
