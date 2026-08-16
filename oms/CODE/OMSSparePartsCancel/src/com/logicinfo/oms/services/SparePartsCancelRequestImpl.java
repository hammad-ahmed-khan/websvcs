package com.logicinfo.oms.services;

import com.logicinfo.oms.beans.SparePartsCancelBean;
import com.logicinfo.oms.ejb.OmsSparePartCancelHdr;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.model.ObjectFactory;
import com.logicinfo.oms.model.SparePartsCancelResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso( { ObjectFactory.class })
@WebService(name = "SparePartsCancelRequest", serviceName = "SparePartsCancelRequestService", targetNamespace = "SparePartsCancelRequestService", portName = "SparePartsCancelRequestPort", wsdlLocation = "/WEB-INF/wsdl/SparePartsCancelRequestService.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class SparePartsCancelRequestImpl {
    private final static Logger log = Logger.getLogger(SparePartsCancelRequestImpl.class.getName());
    public SparePartsCancelRequestImpl() {
    }

    @WebResult(name = "SparePartsCancelResponse", partName = "SparePartsCancelResponse", targetNamespace = "http://com.logicinfo.oms/services/")
    @WebMethod(operationName = "SparePartsCancelRequestOperation", action = "mod:SparePartsCancelRequest/SparePartsCancelRequestOperation")
    public SparePartsCancelResponse sparePartsCancelRequestOperation(@WebParam(name = "SparePartsCancelRequest", partName = "SparePartsCancelRequest", targetNamespace = "http://com.logicinfo.oms/services/")
        com.logicinfo.oms.model.SparePartsCancelRequest sparePartsCancelRequest) {

        log.info("***** Cancel Spare Parts Request WS Invoked *****");

        SparePartsCancelBean theBean = new SparePartsCancelBean();
        log.info("  --> Received Spare Parts Cancellation Request for OMS Service Request Id :"+ sparePartsCancelRequest.getOMSServiceId()
                 + " \n Sequence Id "+ sparePartsCancelRequest.getSequenceId() + " \n Cancellation Id : "+ sparePartsCancelRequest.getCancellationId()
                 + " \n for Quantity " + sparePartsCancelRequest.getQuantity());

        try {
            //Step 1: Perform basic validations. If failed, then raise illegal argument exception.
            log.info("  --> Performing basic validation.");
            OmsSparePartHeader sparePartRequest = theBean.performBasicValidation(sparePartsCancelRequest);
            //Step 2: Insert a new record into OMS_SPARE_PART_CANCEL_HDR table, based on the data from the input to the database.
            OmsSparePartCancelHdr sparePartCancelHdr = theBean.createSparePartsCancellationHeaderEntry(sparePartsCancelRequest);
            log.info("  --> Created Spare Parts Cancellation Header Entry.");
            //Step 4: Identify the location from where the spare part will be fulfilled and process it.
            theBean.processSparePartsCancellationRequest(sparePartsCancelRequest,sparePartRequest);
            //Step 5: Set the status of the Cancellation Request as Closed in the Cancel Header table.
            theBean.closeCancellationRequest(sparePartCancelHdr);
            log.info("***** Cancel Spare Parts Cancellation WS Execution Completed *****");
            return theBean.getServiceResponse(sparePartsCancelRequest, null);
        } catch (IllegalArgumentException iae) {
            log.error(iae.getMessage(), iae);
            return theBean.getServiceResponse(sparePartsCancelRequest, iae.getMessage());
        } catch (SOAPException se) {
            log.error(se.getMessage(), se);     
            return theBean.getServiceResponse(sparePartsCancelRequest, se.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return theBean.getServiceResponse(sparePartsCancelRequest, "Unknown Error while processing spare part cancellation request.");
        }
        
    }
}
