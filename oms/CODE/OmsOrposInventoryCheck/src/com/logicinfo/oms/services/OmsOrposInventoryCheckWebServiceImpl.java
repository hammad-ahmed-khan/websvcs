package com.logicinfo.oms.services;

import com.logicinfo.oms.beans.InventoryCheckBean;
import com.logicinfo.oms.model.InventoryCheck;
import com.logicinfo.oms.model.InventoryCheckResponse;

import com.logicinfo.oms.model.ObjectFactory;

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
@WebService(name = "OmsOrposInventoryCheckWebService", serviceName = "OmsOrposInventoryCheckWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "OmsOrposInventoryCheckWebService", wsdlLocation = "/WEB-INF/wsdl/OmsOrposInventoryCheckWebservice.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class OmsOrposInventoryCheckWebServiceImpl {
    public OmsOrposInventoryCheckWebServiceImpl() {
    }
    private final static Logger log = Logger.getLogger(OmsOrposInventoryCheckWebServiceImpl.class.getName());

    @WebResult(name = "inventoryCheckResponse", partName = "inventoryCheckResponse", targetNamespace = "http://com.logicinfo.oms/model/")
    @WebMethod
    public InventoryCheckResponse checkInventory(@WebParam(name = "inventoryCheck", partName = "inventoryCheck", targetNamespace = "http://com.logicinfo.oms/model/")
        InventoryCheck inventoryCheck) throws SOAPException {
        log.info("checkInventory started");
        InventoryCheckBean inventoryCheckBean=new InventoryCheckBean();
      
       return inventoryCheckBean.checkInventory(inventoryCheck);
        
    }
}
