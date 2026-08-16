package com.logicinfo.oms.services;

import java.util.Arrays;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

import com.logicinfo.oms.model.CustOrdFulDesc;
import com.logicinfo.oms.model.CustOrdFulDescResponse;
import com.logicinfo.oms.model.InventoryCheck;
import com.logicinfo.oms.model.InventoryCheckResponse;
import com.logicinfo.oms.model.ObjectFactory;
import com.logicinfo.oms.util.OmsSysParameterUtil;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;


@javax.jws.soap.SOAPBinding(parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ ObjectFactory.class })
@WebService(name = "OmsOrposInventoryCheckWebService", serviceName = "OmsOrposInventoryCheckWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "OmsOrposInventoryCheckWebService", wsdlLocation = "/WEB-INF/wsdl/OmsOrposInventoryCheckWebservice.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class OmsOrposInventoryCheckWebServiceImpl {

	private InventoryCheckService checkService = null;

	public OmsOrposInventoryCheckWebServiceImpl() {
		checkService = new InventoryCheckService();
	}

	private final static Logger log = Logger.getLogger(OmsOrposInventoryCheckWebServiceImpl.class.getName());

	@WebResult(name = "inventoryCheckResponse", partName = "inventoryCheckResponse", targetNamespace = "http://com.logicinfo.oms/model/")
	@WebMethod
	public InventoryCheckResponse checkInventory(
			@WebParam(name = "inventoryCheck", partName = "inventoryCheck", targetNamespace = "http://com.logicinfo.oms/model/") InventoryCheck inventoryCheck)
			throws SOAPException {
		log.info("checkInventory started");
		try {
			return checkService.getStockAvailability(inventoryCheck); 
		} catch (Exception e) {
			log.error("Error while processing the orpos inventory check", e);
			InventoryCheckResponse errResp = new InventoryCheckResponse();
			CustOrdFulDescResponse fullOrdResp = new CustOrdFulDescResponse();
			List<CustOrdFulDesc> fulDescs = inventoryCheck.getCustOrdFulDesc();
			if (fulDescs != null && !fulDescs.isEmpty()) {
				fullOrdResp.setDeliveryType(fulDescs.get(0).getDeliveryType());
				fullOrdResp.setShipCity(fulDescs.get(0).getShipCity());
			}
			fullOrdResp.setErrorMessage("OMS_ORPOS_ERROR_102");
			errResp.getCustOrdFulDescResponse().add(fullOrdResp);
			return errResp;
		}
	}

	@WebMethod
	@WebResult(name = "InvocationSuccess", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1")
	public InvocationSuccess refreshParams() {
		OmsSysParameterUtil.reload(Arrays.asList("WH_SMALL_POS_THRESHOLD", "WH_BIG_POS_THRESHOLD", "OMS_SYSTEM_OPTION", "ST_BIG_POS_THRESHOLD", "ST_SMALL_POS_THRESHOLD"));
		InvocationSuccess resp = new InvocationSuccess();
		resp.setSuccessMessage("OK");
		return resp;
	}
}
