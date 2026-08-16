package com.logicinfo.oms.services;


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

import com.logicinfo.oms.beans.SparePartsRequestBean;
import com.logicinfo.oms.model.ObjectFactory;
import com.logicinfo.oms.model.SparePartsResponse;
import com.logicinfo.oms.util.BusinessException;
import com.sun.xml.ws.developer.SchemaValidation;


@SchemaValidation
@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso( { ObjectFactory.class })
@WebService(name = "SparePartsRequest", serviceName = "SparePartsRequestService", targetNamespace = "SparePartsRequestService", portName = "SparePartsRequestPort", wsdlLocation = "/WEB-INF/wsdl/SparePartsRequestService.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class SparePartsRequestImpl {
    private final static Logger log = Logger.getLogger(SparePartsRequestImpl.class.getName());

    public SparePartsRequestImpl() {
    }

    @WebResult(name = "SparePartsResponse", partName = "SparePartsResponse", targetNamespace = "http://com.logicinfo.oms/services/")
    @WebMethod(operationName = "SparePartsRequestOperation", action = "mod:SparePartsRequest/SparePartsRequestOperation")
    public SparePartsResponse sparePartsRequestOperation(@WebParam(name = "SparePartsRequest", partName = "SparePartsRequest", targetNamespace = "http://com.logicinfo.oms/services/")
        com.logicinfo.oms.model.SparePartsRequest input) throws SOAPException {
        log.info("***** Update Spare Parts Request WS Invoked *****");
        
        //log.info("The log file is :"+ ((org.apache.log4j.FileAppender)org.apache.log4j.Logger.getRootLogger().getAppender("FA") ).getFile());

        SparePartsRequestBean theBean = new SparePartsRequestBean();
        
        log.info("  --> Spare Parts request received for Request Id "+ input.getServiceRequestId()
                 + " Sequence Id "+ input.getSequenceId() + " for Item "+ input.getItemId()+ " from store id "+ input.getStoreId()+ " for quantity "+ input.getQuantity());
        try 
        {
    		//Step 1: Perform basic validations. If failed, then raise illegal argument exception.
    		log.info("  --> Performing basic validation.");
        	theBean.performBasicValidation(input);
        	//Step 2: Insert a new record into OMS_SPARE_PART_HEADER table, based on the data from the input to the database.
        	theBean.createSparePartsHeaderEntry(input);

            log.info("  --> Created Spare Parts Header Entry.");
            //Step 3: Identify the location from where the spare part will be fulfilled and process it.
            theBean.processSparePartsFulfillment(input);
            log.info("***** Update Spare Parts Request WS Execution Successfully Completed *****");
            return theBean.getServiceResponse(input, null, null);
        } 
        catch (IllegalArgumentException iae)
        {
            log.error("Error while processing the request", iae);
            return theBean.getServiceResponse(input, iae.getMessage(), null);
        }
        catch (SOAPException se) 
        {
            log.error("Error while processing the request", se);
            return theBean.getServiceResponse(input, se.getMessage(), null);
        } catch (BusinessException bae) {
        	log.error("Error while processing the request", bae);
        	return theBean.getServiceResponse(input, bae.getMessage(), bae.getCode());
        }
        catch (Exception e) 
        {
            log.error("Error while processing the request", e);
            return theBean.getServiceResponse(input, "  --> Unknown Error while creating spare part request.", null);
        }
    }
}
