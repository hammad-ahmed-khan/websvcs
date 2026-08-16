package com.oracle.retail.oms.integration.services.customerorderservice.v1;

/***********************************************************************
/* Name:  ORPOS CreateCustomerOrder webservice
/* Description:  ORPOS CreateCustomerOrder webservice
/* Company:  Logic Information Systems
/* Modification History:
/* Date         Name                 Version   Modification Description
/* 24/02/2016   Sakshi Dubey           1.0     Added DiscntLineColDesc in Response Processing
/* 25/02/2016   Renuga Rajendran       1.0     ORPOS Customer Order for recreating the order when the status is failed
/* 26/02/2016   Renuga Rajendran               fixed Completed amount for RMA - Refund
/* 28/02/2016   Renuga Rajendran               fixed returned quantity for RMA Scenario
/* 06/03/2016   Renuga Rajendran               fixed CheckTender in Query WS
/* 09/03/2016   Renuga Rajendran               fixed Purchase Order Tender in Create,Query and Pick and cancel WS
/* 13/03/2016   Renuga Rajendran               fixed WH Check scenario for completed quantity in Query WS
/* 15/03/2016   Sakshi Dubey                   fixed PO and BackOrder Functionality
/* 15/03/2016   Renuga Rajendran               fixed RMA_Restocking fee in Query WS

/**********************************************************************/
import com.logicinfo.oms.beans.CustOrdCreateBean;
import com.logicinfo.oms.beans.PersistRequestResponse;
import com.logicinfo.oms.beans.PickCustOrdItemBean;
import com.logicinfo.oms.beans.QueryCustOrdBean;
import com.logicinfo.oms.beans.ReturnCustOrdBean;
import com.oracle.retail.integration.base.bo.custordercoldesc.v1.CustOrderColDesc;
import com.oracle.retail.integration.base.bo.custordercolref.v1.CustOrderColRef;
import com.oracle.retail.integration.base.bo.custordercrivo.v1.CustOrderCriVo;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.OrderStatus;
import com.oracle.retail.integration.base.bo.custorderpicvo.v1.CustOrderPicVo;
import com.oracle.retail.integration.base.bo.custorderref.v1.CustOrderRef;
import com.oracle.retail.integration.base.bo.custorderrtncolvo.v1.CustOrderRtnColVo;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.nothing.v1.Nothing;
import com.oracle.retail.integration.base.bo.pickupcustomerorderitemdetailsref.v1.PickupCustomerOrderItemDetailsRef;
import com.oracle.retail.integration.base.bo.receiptdesc.v1.ReceiptDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import java.math.BigDecimal;
import java.util.Date;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.apache.log4j.Logger;

@XmlSeeAlso({ com.oracle.retail.integration.base.bo.contactdesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.customerdesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custordcancelitemdetails.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custordcancelitemdetailscol.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custorddelcoldesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custorddeldesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custordercoldesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custordercolref.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custordercrivo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custorderdesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custorderpicvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custorderref.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custorderrtncolvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custorderrtnvo.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custordfulcoldesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custordfuldesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custorditmcoldesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custorditmdesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custorditmpkcolvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custorditmpkvo.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.custorditmrtcolvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.custorditmrtvo.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.discntlinecoldesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.discntlinedesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.discntlinepkcolvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.discntlinepkvo.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.discntlinertcolvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.discntlinertvo.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.emaildesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.geoaddrdesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.invocationsuccess.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.localedesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.nothing.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.paymentcoldesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.paymentdesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.phonedesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.pickupcustomerorderitemdetailsref.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.prcovdlinedesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.promolinedesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.receiptdesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.taxlinecoldesc.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.taxlinedesc.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.taxlinepkcolvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.taxlinepkvo.v1.ObjectFactory.class,
		com.oracle.retail.integration.base.bo.taxlinertcolvo.v1.ObjectFactory.class, com.oracle.retail.integration.base.bo.taxlinertvo.v1.ObjectFactory.class,
		com.oracle.retail.integration.services.exception.v1.ObjectFactory.class, ObjectFactory.class })
@WebService(name = "CustomerOrderPortType", serviceName = "CustomerOrderService", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", portName = "CustomerOrderPort", wsdlLocation = "/WEB-INF/wsdl/CustomerOrderService.wsdl")
public class CustomerOrderPortTypeImpl {
	public CustomerOrderPortTypeImpl() {
	}

	private final static Logger log = Logger.getLogger(com.oracle.retail.oms.integration.services.customerorderservice.v1.CustomerOrderPortTypeImpl.class.getName());

	@RequestWrapper(localName = "requestNewCustomerOrderId", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.RequestNewCustomerOrderId")
	@ResponseWrapper(localName = "requestNewCustomerOrderIdResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.RequestNewCustomerOrderIdResponse")
	@WebMethod
	@WebResult(name = "CustOrderRef", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1")
	public CustOrderRef requestNewCustomerOrderId(@WebParam(name = "Nothing", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/Nothing/v1") Nothing nothing)
			throws IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
			ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException,
			IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException,
			IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
			ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException {
		GenerateNewOrderId generateNewOrderId = new GenerateNewOrderId();
		long customerOrderId = generateNewOrderId.generateCustomerOrderId();
		CustOrderRef custOrderRef = new CustOrderRef();
		custOrderRef.setOrderId(String.valueOf(customerOrderId));
		return custOrderRef;
	}

	@RequestWrapper(localName = "cancelNewCustomerOrderId", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.CancelNewCustomerOrderId")
	@ResponseWrapper(localName = "cancelNewCustomerOrderIdResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.CancelNewCustomerOrderIdResponse")
	@WebMethod
	public void cancelNewCustomerOrderId(
			@WebParam(name = "CustOrderRef", mode = WebParam.Mode.INOUT, targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1") Holder<CustOrderRef> custOrderRef)
			throws IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException,
			IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException,
			IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
			ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException,
			IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException {
	}

	@RequestWrapper(localName = "createCustomerOrder", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.CreateCustomerOrder")
	@ResponseWrapper(localName = "createCustomerOrderResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.CreateCustomerOrderResponse")
	@WebMethod
	public void createCustomerOrder(
			@WebParam(name = "CustOrderDesc", mode = WebParam.Mode.INOUT, targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderDesc/v1") Holder<CustOrderDesc> custOrderDesc)
			throws SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		/*
		 * CustOrderDesc LocaleDesc CustomerDesc Phones Emails
		 *
		 */
		CustOrderDesc custOrderDesc1 = null;
		CustOrdCreateBean custOrdCreateBean = null;
		BigDecimal omsOrposCustOrderId = null;
		Date d1 = null;
		try {
			custOrderDesc1 = custOrderDesc.value;
			custOrdCreateBean = new CustOrdCreateBean();
			d1 = new Date();
			log.info("Time starts for Processing the POS order creation " + custOrderDesc1.getCustomerOrderId());
			log.info("Start time " + d1);
			log.info("Program begins calling the checkCustomerOrderStatus");
			String status = custOrdCreateBean.checkCustomerOrderStatus(custOrderDesc1);
			log.info("status returned from checkCustomerOrderStatus method is " + status);
			if (status.equals("F")) {
				log.info("inside status if condition proceeding for persisting");
				omsOrposCustOrderId = custOrdCreateBean.saveCreatCustOrd(custOrderDesc1);
				String orderStatus = custOrdCreateBean.checkOrderStatus(omsOrposCustOrderId, custOrderDesc1);
				log.info("In response orderStatus " + orderStatus);
				if (orderStatus.equals("CANCELED")) {
					log.info("inside cancelled");
					custOrderDesc1.setOrderStatus(OrderStatus.CANCELED);
					log.info("setted order status to CANCELED");
					custOrdCreateBean.callRollBackMethod(custOrderDesc1.getCustomerOrderId());
					custOrdCreateBean.reduceBOSrcQty();
					log.info("setted order status to CANCELED");
					log.info("Program exited after setting the order status to CANCELED");
				} else if (orderStatus.equals("FILLED")) {
					custOrdCreateBean.persistRTLogTable(custOrderDesc1);
					log.info("creating record in reconcillation batch");
					custOrdCreateBean.reconcilationCreateOrder(custOrderDesc1);
					log.info("Successfully persisted record for reconcillation");
					log.info("inside Filled");
					custOrderDesc1.setOrderDesc("NEW");
					custOrderDesc1.setOrderStatus(OrderStatus.FILLED);
					log.info("setted order status to FILLED");
					log.info("Program exited after setting the order status to FILLED");
					if (custOrderDesc1.getBookingDate() != null && custOrderDesc1.getBookingWindow() != null) {
						custOrdCreateBean.bookSlotForOrder(custOrderDesc1);
					}
				}
			} else if (status.equals("S")) {
				log.info("record already exist for " + custOrderDesc1.getCustomerOrderId() + "with status S");
				custOrderDesc1.setOrderStatus(OrderStatus.CANCELED);
			}
		} catch (Exception e) {
			log.error("Exception ", e);
			custOrdCreateBean.callRollBackMethod(custOrderDesc1.getCustomerOrderId());
			String orderStatus = custOrdCreateBean.changeStatus(custOrderDesc1);
			custOrdCreateBean.reduceBOSrcQty();
			custOrderDesc1.setOrderStatus(OrderStatus.CANCELED);
			log.error("setted order status to CANCELED");
		}
		PersistRequestResponse persistRequestResponse = new PersistRequestResponse();
		persistRequestResponse.persistRequestAndResponse(omsOrposCustOrderId, custOrderDesc.value, custOrderDesc1);
		Date d2 = new Date();
		log.info("End Time " + d2);
		log.info(" Difference between start and end time " + (d2.getTime() - d1.getTime()) / 1000 + " seconds");
		log.info("Processing  ends for POS order " + custOrderDesc1.getCustomerOrderId());
	}

	@RequestWrapper(localName = "queryCustomerOrder", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.QueryCustomerOrder")
	@ResponseWrapper(localName = "queryCustomerOrderResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.QueryCustomerOrderResponse")
	@WebMethod
	@WebResult(name = "CustOrderColDesc", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderColDesc/v1")
	public CustOrderColDesc queryCustomerOrder(@WebParam(name = "CustOrderCriVo", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1") CustOrderCriVo custOrderCriVo)
			throws IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
			ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
			ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException,
			IllegalArgumentWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException,
			IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException {
		CustOrderColDesc custOrderColDesc;
		QueryCustOrdBean queryCustOrdBean = new QueryCustOrdBean();
		custOrderColDesc = queryCustOrdBean.queryCustomerOrder(custOrderCriVo);
		return custOrderColDesc;
	}

	@RequestWrapper(localName = "pickupCustomerOrderItems", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.PickupCustomerOrderItems")
	@ResponseWrapper(localName = "pickupCustomerOrderItemsResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.PickupCustomerOrderItemsResponse")
	@WebMethod
	@WebResult(name = "PickupCustomerOrderItemDetailsRef", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/PickupCustomerOrderItemDetailsRef/v1")
	public PickupCustomerOrderItemDetailsRef pickupCustomerOrderItems(
			@WebParam(name = "CustOrderPicVo", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderPicVo/v1") CustOrderPicVo custOrderPicVo)
			throws SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException,
			SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, SOAPException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException, Exception {
		PickupCustomerOrderItemDetailsRef pickupCustomerOrderItemDetailsRef = null;
		PickCustOrdItemBean pickCustOrdItemBean = new PickCustOrdItemBean();
		pickCustOrdItemBean.getCustomerOrderandTransactionNo(custOrderPicVo);
		// Persists the data into the audit tables
		int collectionSize = pickCustOrdItemBean.saveCustOrderPicVo(custOrderPicVo);
		// Checking whether the Item coming in is for PickUp or Cancellation
		pickupCustomerOrderItemDetailsRef = pickCustOrdItemBean.findPickUpOrCancel(custOrderPicVo);
		try {
			pickCustOrdItemBean.reconcilePickCancel(pickupCustomerOrderItemDetailsRef, custOrderPicVo);
		} catch (Exception e) {
			log.info("inside catch block for persisting into reconcilation batch table" + e.getMessage());
		}
		return pickupCustomerOrderItemDetailsRef;
	}

	@RequestWrapper(localName = "returnCustomerOrderItems", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.ReturnCustomerOrderItems")
	@ResponseWrapper(localName = "returnCustomerOrderItemsResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.ReturnCustomerOrderItemsResponse")
	@WebMethod
	@WebResult(name = "CustOrderColRef", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderColRef/v1")
	public CustOrderColRef returnCustomerOrderItems(
			@WebParam(name = "CustOrderRtnColVo", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderRtnColVo/v1") CustOrderRtnColVo custOrderRtnColVo)
        			throws IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, 
					IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
					ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException,
					IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException,
					IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException,
					IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
					ValidationWSFaultException, IllegalArgumentWSFaultException, SOAPException {
		ReturnCustOrdBean returnCustOrdBean = new ReturnCustOrdBean();
		// returnCustOrdBean.getCustomerOrderNoandTransactionNo(custOrderRtnColVo.getCustOrderRtnVo().get(0).getCustomerOrderId());
		CustOrderColRef custOrderColRef = returnCustOrdBean.createResponse(custOrderRtnColVo);
		log.info("custOrderColRef.getStatus()" + custOrderColRef.getStatus());
		if (custOrderColRef.getStatus().equals("S")) {
			try {
				returnCustOrdBean.reconcilationReturnOrder(custOrderRtnColVo);
			} catch (Exception e) {
				log.info("inside catch block for persisting into reconcilation batch table" + e.getMessage());
			}
		}
		return custOrderColRef;
	}

	@RequestWrapper(localName = "updateReceipt", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.UpdateReceipt")
	@ResponseWrapper(localName = "updateReceiptResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.UpdateReceiptResponse")
	@WebMethod
	@WebResult(name = "InvocationSuccess", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1")
	public InvocationSuccess updateReceipt(@WebParam(name = "ReceiptDesc", targetNamespace = "http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1") ReceiptDesc receiptDesc)
			throws IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException,
			IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException,
			IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException,
			ValidationWSFaultException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException,
			IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, IllegalArgumentWSFaultException {
		return null;
	}

	@RequestWrapper(localName = "ping", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.Ping")
	@ResponseWrapper(localName = "pingResponse", targetNamespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", className = "com.oracle.retail.oms.integration.services.customerorderservice.v1.PingResponse")
	@WebMethod
	public String ping(@WebParam(name = "arg0") String arg0) {
		return null;
	}
}
