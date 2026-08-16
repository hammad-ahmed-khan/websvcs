package com.logicinfo.oms.service;

import com.logicinfo.oms.model.OMSRMAModificationBean;
import com.logicinfo.oms.model.ObjectFactory;
import com.logicinfo.oms.model.RMAModifyRequest;
import com.logicinfo.oms.model.RMAModifyResponse;

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
@WebService(name = "RMAModifyWebService", serviceName = "OMSRMAModifyWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "OMSRMAModifyWebService", wsdlLocation = "/WEB-INF/wsdl/OMSRMAModifyWebservice.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class RMAModifyWebServiceImpl 
{
    public RMAModifyWebServiceImpl() 
    {
    }
    private final static Logger log = Logger.getLogger(RMAModifyWebServiceImpl.class.getName());

    @WebResult(name = "ModifyRMAResponse", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
    @WebMethod
    public RMAModifyResponse modifyRMA(@WebParam(name = "ModifyRMA", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/")
        RMAModifyRequest input) throws SOAPException 
    {
        log.info("***RMA modification started ***");
       OMSRMAModificationBean omsRmaModificationBean=new OMSRMAModificationBean();
        try 
        {
            omsRmaModificationBean.validate(input);
            omsRmaModificationBean.persistOmsRmaMod(input);
            omsRmaModificationBean.callRWMSWebservice(input);
        }
        catch(SOAPException e) 
        {
            return omsRmaModificationBean.createResponse(input, e.getMessage());
        }
        catch(Exception e) 
        {
            return omsRmaModificationBean.createResponse(input, e.getMessage());
        }
        return omsRmaModificationBean.createResponse(input, "");
    }
}

