package com.logicinfo.oms.service;

import com.logicinfo.oms.model.ObjectFactory;
import com.logicinfo.oms.model.RMADeleteRequest;
import com.logicinfo.oms.model.RMADeleteResponse;

import com.logicinfo.oms.model.RMADeletetionBean;

import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.ValidationWSFaultException;

import com.sun.xml.ws.developer.SchemaValidation;

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
@XmlSeeAlso( { ObjectFactory.class })
@WebService(name = "RMADeletionWebService", serviceName = "OMSRMADeletionWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "OMSRMADeletionWebService", wsdlLocation = "/WEB-INF/wsdl/OMSRMADeleteWebservice.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class RMADeletionWebServiceImpl {
    public RMADeletionWebServiceImpl() {
    }
    private final static Logger log = Logger.getLogger(RMADeletionWebServiceImpl.class.getName());

    @WebResult(name = "DeleteRMAResponse", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
    @WebMethod
    public RMADeleteResponse deleteRMA(@WebParam(name = "DeleteRMA", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/")
        RMADeleteRequest input) throws SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
                                       ValidationWSFaultException {
     log.info("***deleteRMA started***");
        RMADeletetionBean rmaDeletionBean=new RMADeletetionBean();
        try
        {
            rmaDeletionBean.validate(input);
        rmaDeletionBean.persistData(input);
        rmaDeletionBean.callRWMSWebservice(input);
        }catch(SOAPException e) {
            log.error("SOAP Exception "+e.getMessage());
            log.info("***deleteRMA ended***");
            return rmaDeletionBean.createResponse(input,e.getMessage());
        }catch(Exception e) {
            log.error(" Exception "+e.getMessage());
            log.info("***deleteRMA ended***");
            return rmaDeletionBean.createResponse(input,e.getMessage());
        }
        log.info("***deleteRMA ended***");
        return rmaDeletionBean.createResponse(input,"");
    }   
}
