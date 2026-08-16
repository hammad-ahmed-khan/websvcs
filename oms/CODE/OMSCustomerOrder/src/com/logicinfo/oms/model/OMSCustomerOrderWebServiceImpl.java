package com.logicinfo.oms.model;

/***********************************************************************
/* Name: OMSCustomerOrder
/* Description:  OMS Customer Order webservice
/* Company:  Logic Information Systems
/* Modification History:
/* Date         Name               Version   Modification Description
/* 16/02/2015   Sakshi Dubey       1.0
*  18/02/2015   Sakshi Dubey       1.2       Updated xsd for backorder,coded tender split part and code cleanup .
*  19/02/2015   Sakshi Dubey       1.3       Updated code for batch order processing of back orders.
*  26/02/2015   Sakshi Dubey       1.4       Created client for calling SIEBEL web service.
*  02/03/2015   Sakshi Dubey       1.5       Updated few method names.
*  23/03/2015   Sakshi Dubey       1.6       Updated xsd-added new fields,write code for spare part processing.
*  22/04/2015   Sakshi Dubey       1.7       Updated log4j.properties file for log file creation and mofified code for checking SOH from SIM in case of store.
*  1/12/2015    Sakshi Dubey       1.8        fixed for multiple item PO and changed XSD to make customer_id length to 14.
*  23/12/2015   Sakshi Dubey       1.9        Updated the processWebserviceResponse method and assigned source_loc_type and calling package for shipment classification.
*  27/12/2015   Sakshi Dubey       1.10        Updated the XSD to put line_no at item level instead of fulfilment level.
*   19/1/2016   Sakshi Dubey       1.11       Updated back order code
                                              Updated tender split ,changed datatype to float
                                             Updated to include exception hadling if SIM or RMS WS is down
                                              Update sourceLocIdentify class for back order to go through current inventory
                                              Updated the sourcelocIdentify as in case of WH not creating diffrent fulfilment order no
*   21/2/2016   Sakshi Dubey       1.11        Updated the ResponseProcessing for service_status='EXT_SYS_ERROR' when error description not found
*   07/3/2016   Sakshi Dubey       1.12        Updated response processing for backorder and reserve indicator R, commented the if condition
*  11/3/2016   Sakshi Dubey       1.13        Write method for auto approval of transfers
*  03/3/2016   Sakshi Dubey       1.14        In auto approval of transfer added wait timing
*  03/3/2016   Sakshi Dubey       1.15        Added return qty in oms_customer_order_item  , so initialize wd to zero.
*  20/3/2016   Sakshi Dubey       1.15        persisted combination id in oms_cust_ord_reserve table.
*   28/3/2016  Sakshi Dubey              While calling RMS package if table is locked then sending TABLE_LOCKED error code.
*   03/4/2016  Sakshi Dubey              In case of reservation unit_retaila and retail_curr not getting passed
*   23/4/2016  Sakshi Dubey              Updated response processing for back order as gettin null pointer exception for BAckorder in case of create ind C
*   24/4/2016  Sakshi Dubey              Restricting the Sibel call based on flag
*   04/6/2016  Sakshi Dubey              Max dly day will be 0 if null in matirx in back order logic
/**********************************************************************/

import com.logicinfo.oms.beans.ItemUnavailabilityStatus;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

import com.sun.xml.ws.developer.SchemaValidation;

import java.sql.Timestamp;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@SchemaValidation
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ ObjectFactory.class })
@WebService(name = "OMSCustomerOrderWebService", serviceName = "OMSCustomerOrderWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "OMSCustomerOrderWebService", wsdlLocation = "/WEB-INF/wsdl/OMSCustomerOrderWebService.wsdl")
public class OMSCustomerOrderWebServiceImpl {

	private final static Logger log = Logger.getLogger(OMSCustomerOrderWebServiceImpl.class.getName());

	public OMSCustomerOrderWebServiceImpl() {
	}

	@WebResult(name = "processNewOrderResp", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
	@WebMethod
	public CustomerOrderResponse processNewOrder(@WebParam(name = "processNewOrderReq", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/") CustomerOrder input)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException {
		log.info("-------------------------------------------------");
		log.info("***Customer order processing starts for customer order no " + input.getCustomerOrderNo() + "***");
		Date date1 = new Date();
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + " Start Time " + date1);
		OMSCustomerOrderBean omsCustomerOrderBean = new OMSCustomerOrderBean();
		ItemUnavailabilityStatus ItemUnavailabilityStatus = null;
		String serviceStatus = "";
		CustomerOrderResponse custresponse = null;
		log.info("********INPUT " + input.toString());
		log.info("********JUST INPUT " + input.toString());
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		try {
			serviceStatus = "VALIDATE_ERROR";
			Date date2 = new Date();
			omsCustomerOrderBean.validateInput(input);
			Date date3 = new Date();
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Time consumed in validation is " + (date3.getTime() - date2.getTime()) + "milli seconds");
			// Step 2:Persist data in oms tables
			Date date4 = new Date();
			omsCustomerOrderBean.persistData(input);
			Date date5 = new Date();
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Time consumed in persisting in base tables is " + (date5.getTime() - date4.getTime()) + "milli seconds");
			serviceStatus = "INV_UNAVILABLE";
			Date date6 = new Date();

			ItemUnavailabilityStatus = omsCustomerOrderBean.findSourceLocation(input);
			if (null != ItemUnavailabilityStatus) {
				if ("FAIL".equals(ItemUnavailabilityStatus.getStatus())) {
					log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "status=FAIL");
					custresponse = omsCustomerOrderBean.generateResponse(input, serviceStatus, "UNAVL_INV", ItemUnavailabilityStatus);
					// WEB Logging of failed orders
					if (input.getCustomerOrderNo().contains("WEB") && session.getOmsSystemParametersFindIndValue("FAILED_ORDERS_REQ_RESP", "FAILED_ORDERS").equals("Y")) {
						log.info("**********If loop is true for persisting WEB failed orders**********");
						updateRequestResponseDetails(input, custresponse);
					}
					// NOON Logging of failed orders
					else if (input.getCustomerOrderNo().contains("NSA") && session.getOmsSystemParametersFindIndValue("FAILED_ORDERS_REQ_RESP", "NOON_FAILED_ORDERS").equals("Y")) {
						log.info("**********If loop is true for persisting NOON failed orders**********");
						updateRequestResponseDetails(input, custresponse);
					}
					return custresponse;
				}
			}

			Date date7 = new Date();
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Time consumed in finding the best locations" + (date7.getTime() - date6.getTime()) + "milli seconds");
			serviceStatus = "EXT_SYS_ERROR";
			Date date8 = new Date();
			omsCustomerOrderBean.checkCreateOrReserveOrder(input);
			Date date9 = new Date();
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Time consumed in calling RMS/SIM" + (date9.getTime() - date8.getTime()) + "milli seconds");
			// Step 6: If TenderType ='CREDIT' then split the tender
			omsCustomerOrderBean.persistRTLog(input);
			omsCustomerOrderBean.splitTender(input);
			// Step 7:Send notification to SIEBEL
			Date date10 = new Date();
			omsCustomerOrderBean.notifySiebel(input);
			Date date11 = new Date();
			//step 8: Booking slot
			if("ODDSMALL".equals(input.getDeliveryModeType()) && "S".equals(input.getDeliveryType())){
				omsCustomerOrderBean.bookingODDSlot(input);
			}
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Time consumed in calling RMS/SIM=" + (date11.getTime() - date10.getTime()) + "milli seconds");
		} catch (Exception e) {

			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Exception occured" + e);
			log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Calling Rollback in impl class");

			try {
				if (!serviceStatus.equals("VALIDATE_ERROR")) {
					oMSUtilCommons.rollback(input.getCustomerOrderNo());
				}
			} catch (Exception g) {
				log.warn("Error while roll back the order in base web service", g);
			}
			if (e instanceof SOAPFaultException) {
				throw (SOAPFaultException)e;
			}
			String errorCode = e instanceof BusinessException ? ((BusinessException)e).getCode() : e.getMessage();
			custresponse = omsCustomerOrderBean.generateResponse(input, serviceStatus, errorCode, null);
			// WEB Logging of failed orders
			if (input.getCustomerOrderNo().contains("WEB") && session.getOmsSystemParametersFindIndValue("FAILED_ORDERS_REQ_RESP", "FAILED_ORDERS").equals("Y")) {
				log.info("**********If loop is true for persisting WEB failed orders**********");
				updateRequestResponseDetails(input, custresponse);
			}
			// NOON Logging of failed orders
			else if (input.getCustomerOrderNo().contains("NSA") && session.getOmsSystemParametersFindIndValue("FAILED_ORDERS_REQ_RESP", "NOON_FAILED_ORDERS").equals("Y")) {
				log.info("**********If loop is true for persisting NOON failed orders**********");
				updateRequestResponseDetails(input, custresponse);
			}

			return custresponse;
		}
		serviceStatus = "SUCCESS";
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "***Customer order processing ends,will send response***");
		// Step 8: Return response to extenal system
		Date date12 = new Date();
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Web service took " + (date12.getTime() - date1.getTime()) + "milli seconds");
		return omsCustomerOrderBean.generateResponse(input, serviceStatus, "", null);
	}

	@WebResult(name = "processBackOrderResp", partName = "backOrderResponse", targetNamespace = "http://com.logicinfo.oms/model/")
	@WebMethod
	public BackOrderResponse processBackOrder(
			@WebParam(name = "processBackOrderReq", partName = "backOrderRequest", targetNamespace = "http://com.logicinfo.oms/model/") BackOrderRequest backOrderRequest)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException {
		BackOrder backOrder = new BackOrder();
		BackOrderResponse backOrderResponse = new BackOrderResponse();
		Date date1 = new Date();
		int proceedOrders = 0;
		try {
			String batchProcessingIndicator = backOrder.getBatchProcessingIndicator();
			if (batchProcessingIndicator != null) {
				throw new Exception("Another back order batch is in Progress..");
			}
			int batchId = 0;
			try {
				// backOrder.getBackOrderBatchSequence();
				batchId = backOrder.insertBackOrderBachProgress();
				log.info("Started Processing BackOrder batch call" + new Timestamp(new Date().getTime()));
				proceedOrders = backOrder.findBackOrder();
				log.info("Number of proceesed backorder " + proceedOrders);
				backOrder.updateBackOrderBatchStatus("COMPLETED", batchId);
			} catch (Exception e) {
				backOrderResponse.setMessage("FAILED");
				backOrder.updateBackOrderBatchStatus("FAILED", batchId);
			}
		} catch (Exception e) {
			backOrderResponse.setMessage("Another back order batch is in Progress..");
			return backOrderResponse;
		}
		Date date2 = new Date();
		String resp = "End time of Processing BackOrder batch call" + new Timestamp(new Date().getTime());
		log.info(resp);
		log.info("Difference in calling the start time " + date1.getTime() + "and end time" + date2.getTime() + "of over all backOrder batch call in WS" + (date2.getTime() - date1.getTime())
				+ "milli seconds");
		backOrderResponse.setMessage(resp);
		log.info("Before returning the response time is " + new Timestamp(new Date().getTime()));
		return backOrderResponse;

	}

	private String jaxbObjectToXMLReq(CustomerOrder customerOrder) {
		String xmlContent = null;
		try {
			// Create JAXB Context
			JAXBContext jaxbContext = JAXBContext.newInstance(CustomerOrder.class);

			// Create Marshaller
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// //Required formatting??
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Print XML String to Console
			StringWriter sw = new StringWriter();

			// Write XML to StringWriter
			jaxbMarshaller.marshal(customerOrder, sw);

			// Verify XML Content
			xmlContent = sw.toString();
			// System.out.println( xmlContent );

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xmlContent;
	}

	private String jaxbObjectToXMLResp(CustomerOrderResponse customerOrderResponse) {
		String xmlContent = null;
		try {
			// Create JAXB Context
			JAXBContext jaxbContext = JAXBContext.newInstance(CustomerOrderResponse.class);

			// Create Marshaller
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// //Required formatting??
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Print XML String to Console
			StringWriter sw = new StringWriter();

			// Write XML to StringWriter
			jaxbMarshaller.marshal(customerOrderResponse, sw);

			// Verify XML Content
			xmlContent = sw.toString();
			// System.out.println( xmlContent );

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xmlContent;
	}

	private void updateRequestResponseDetails(CustomerOrder input, CustomerOrderResponse customerOrderResponse) {
		log.info("FAILED ORDERS Calling updateRequestResponseDetails method");
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PreparedStatement prepStatementItems = null;
		ResultSet rs = null;
		int candidateId = 0;
		Long l = new Long(10);
		int i = l.intValue();
		String requestInput = jaxbObjectToXMLReq(input);
		String responseOutput = jaxbObjectToXMLResp(customerOrderResponse);
		String check = "RequestResponse";
		byte b[] = check.getBytes();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			log.warn(e.toString());
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}
		XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

		try {
			log.info("connecting to OMS Schema");
			log.info("OMSConstants.DS_OMS_STRING " + OMSConstants.DS_OMS_STRING);
			connection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("connection of Connection : " + connection);

			String insert = "INSERT INTO oms_order_create_response_head(cust_order_no,oms_cust_ord_no,order_message_status,order_message_code,order_message_desc,xml_request,xml_response) VALUES (?, ?, ?, ?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(insert);
			prepStatement.setString(1, customerOrderResponse.getCustomerOrderNo());
			prepStatement.setLong(2, customerOrderResponse.getOmsCustomerOrderNo());
			prepStatement.setString(3, customerOrderResponse.getMessageStatus());
			prepStatement.setString(4, customerOrderResponse.getMessageCode());
			prepStatement.setString(5, customerOrderResponse.getMessageDesc());
			prepStatement.setString(6, requestInput);
			prepStatement.setString(7, responseOutput);

			if (customerOrderResponse.getCustomerOrderResponseItems() != null) {
				for (CustomerOrderResponseItems items : customerOrderResponse.getCustomerOrderResponseItems()) {
					String insertItems = "INSERT INTO oms_order_create_response_item(oms_cust_ord_no,line_no,item,item_status,item_status_message) VALUES (?, ?, ?, ?, ?)";
					prepStatementItems = connection.prepareStatement(insertItems);
					prepStatementItems.setLong(1, customerOrderResponse.getOmsCustomerOrderNo());
					prepStatementItems.setLong(2, items.getLineNo());
					prepStatementItems.setString(3, items.getItem());
					prepStatementItems.setString(4, items.getStatus());
					prepStatementItems.setString(5, items.getStatusMessage());
				}
			}
			int rowAffected = prepStatement.executeUpdate();
			int rowAffectedItems = prepStatementItems.executeUpdate();
			if (rowAffected == 1) {
				// get candidate id
				rs = prepStatement.getGeneratedKeys();
				if (rs.next())
					candidateId = rs.getInt(1);
			}
			if (rowAffectedItems == 1) {
				// get candidate id
				rs = prepStatementItems.getGeneratedKeys();
				if (rs.next())
					candidateId = rs.getInt(1);
			}
			log.info("FAILED ORDERS Insert Query " + customerOrderResponse.getMessageStatus() + "," + customerOrderResponse.getMessageCode() + "," + customerOrderResponse.getMessageDesc());
		} catch (Exception e) {
			log.info("Exception e " + e.getMessage());
		} finally {
			try {
				// OMSUtil.closeDBConnection(connection, prepStatement, rs);
				prepStatement.close();
				prepStatementItems.close();
				rs.close();
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				// throw new SOAPException(e.getMessage());
			}
		}

	} // End of method
}
