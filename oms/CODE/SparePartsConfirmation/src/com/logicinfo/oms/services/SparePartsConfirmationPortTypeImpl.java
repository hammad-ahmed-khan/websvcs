package com.logicinfo.oms.services;

import com.logicinfo.oms.beans.SparePartsConfirmationBean;
import com.logicinfo.oms.model.ObjectFactory;
import com.logicinfo.oms.model.SparePartsConfirmation;
import com.logicinfo.oms.model.SparePartsConfirmationResponse;

import com.sun.xml.ws.developer.SchemaValidation;

import java.math.BigDecimal;

import java.util.HashMap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Action;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import java.util.*;

@SchemaValidation
@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso( { ObjectFactory.class })
@WebService(name = "SparePartsConfirmation", serviceName = "SparePartsConfirmationService", targetNamespace = "http://com.logicinfo.oms/services/", portName = "SparePartsConfirmationPort", wsdlLocation = "/WEB-INF/wsdl/SparePartsConfirmationService.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class SparePartsConfirmationPortTypeImpl {
    private final static Logger log = Logger.getLogger(SparePartsConfirmationPortTypeImpl.class.getName());
    // Added By Santosh Suman
    HashMap<String, String> reasonCodeIdMap = null;
    
    public SparePartsConfirmationPortTypeImpl() {
    }

    @Action(input = "SparePartsConfirmationOperation", output = "http://com.logicinfo.oms/services/SparePartsConfirmation/SparePartsConfirmationOperationResponse")
    @WebResult(name = "SparePartsConfirmationResponse", partName = "SparePartsConfirmationResponse", targetNamespace = "http://com.logicinfo.oms/services/")
    @WebMethod(operationName = "SparePartsConfirmationOperation", action = "SparePartsConfirmationOperation")
    public SparePartsConfirmationResponse sparePartsConfirmationOperation(@WebParam(name = "SparePartsConfirmation", partName = "SparePartsConfirmation", targetNamespace = "http://com.logicinfo.oms/services/")
        SparePartsConfirmation sparePartsConfirmation) {
        log.info("***** Spare Parts Confirmation WS Invoked *****");
        SparePartsConfirmationBean theBean = new SparePartsConfirmationBean();
        SparePartsConfirmationResponse theCompletedResp;
        try {
            //Step 0: Perform basic validations. If failed, then raise illegal argument exception.
            log.info("  --> Performing basic validation.");
            theBean.performBasicValidation(sparePartsConfirmation);

            // Added By Santosh Suman
           log.info("  --> Validating reason code.");
           reasonCodeIdMap = theBean.validateGetReasonCodeId(sparePartsConfirmation);
           /* 
           for (Map.Entry<String, String> entry : reasonCodeMap.entrySet()) {
               log.info("  --> Validating reason code.... "+ entry.getKey() +" "+ entry.getValue());
           } */
            

                //Step 1: Insert a new record into OMS_CUST_UPD_DELIV_INFO based on the data from the input to the database.
            log.info("  --> Creating OMS SPARE PART CONFIRM HEADER record.");
            theBean.createSparePartsConfirmationHeader(sparePartsConfirmation);
            
            log.info("  --> Creating the OMS SPARE PART CONFIRM DETAIL record." );
            // Updated By Santosh Suman
            theCompletedResp = theBean.processAndCreateSparePartsConfirmationDetails(sparePartsConfirmation, reasonCodeIdMap);

         } catch (IllegalArgumentException iae) {
            return theBean.getServiceResponse( sparePartsConfirmation,iae.getMessage());
        } catch (SOAPException se) {
            return theBean.getServiceResponse( sparePartsConfirmation,se.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return theBean.getServiceResponse(sparePartsConfirmation,e.getMessage());
        }
        //Step 3: Send the OMS Delivery Id back to the WS Client.
        log.info("***** Spare Parts Confirmation Execution Completed *****");
        return theCompletedResp;
    }
}
