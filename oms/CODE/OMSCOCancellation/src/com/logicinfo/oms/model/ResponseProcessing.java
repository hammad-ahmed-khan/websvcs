package com.logicinfo.oms.model;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.util.OMSUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Logger;
public class ResponseProcessing
{
public ResponseProcessing()
{
  super();
}
private final static Logger log=
  Logger.getLogger(com.logicinfo.oms.model.OMSCOCancellationWebServiceImpl.class.getName());
public CustomerOrderCancellationResponse generateErrorResponse(CustomerOrderCancellation input,String serviceStatus,
                                                               List<ErrorListResponse> errorCode,
                                                               BigDecimal omsCustOrdNo) throws SOAPException
{
  log.info("inside generateErrorResponse in Respone Processing");
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  CustomerOrderCancellationResponse response=new CustomerOrderCancellationResponse();
  GregorianCalendar gregorianCalendar=new GregorianCalendar();
  DatatypeFactory datatypeFactory=null;
  try
  {
    datatypeFactory=DatatypeFactory.newInstance();
  }
  catch(DatatypeConfigurationException e)
  {
    log.warn(e.toString());
    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
  }
  XMLGregorianCalendar now=datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
  response.setCancellationId(input.getCancellationId());
  response.setResponseDatetimestamp(now);
  log.info("serviceStatus"+serviceStatus);
  List<CustomerOrderCancelResponseItems> responseItemLists=response.getCustomerOrderCancelResponseItems();
  CustomerOrderCancelResponseItems customerOrderResponseItems=null;
  if(serviceStatus.equals("FAILED"))
  {
    response.setResponseMessage("FAILED");
    for(ErrorListResponse list:errorCode)
    {
      log.info("inside ErrorListResponse loop");
      customerOrderResponseItems=new CustomerOrderCancelResponseItems();
      customerOrderResponseItems.setLineNo(list.getLineNo());
      customerOrderResponseItems.setItem(list.getItem());
      customerOrderResponseItems.setCancelQtySuom(list.getCancelQtySuom());
      customerOrderResponseItems.setMessageCode(list.getMessageCode());
      customerOrderResponseItems.setMessageDesc(list.getMessageDesc());
      responseItemLists.add(customerOrderResponseItems);
    } //end of CustomerOrderCancellationItems loop
    response.setMessageStatus("F");
  }
  return response;
}
public CustomerOrderCancellationResponse generateResponse(CustomerOrderCancellation input,String serviceStatus,
                                                          String errMessage,
                                                          BigDecimal omsCustOrdNo) throws SOAPException
{
  log.info("inside generateResponse in Respone Processing");
  log.info("Error code"+errMessage);
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  CustomerOrderCancellationResponse response=new CustomerOrderCancellationResponse();
  GregorianCalendar gregorianCalendar=new GregorianCalendar();
  DatatypeFactory datatypeFactory=null;
  try
  {
    datatypeFactory=DatatypeFactory.newInstance();
  }
  catch(DatatypeConfigurationException e)
  {
    log.warn(e.toString());
    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
  }
  XMLGregorianCalendar now=datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
  response.setCancellationId(input.getCancellationId());
  response.setResponseDatetimestamp(now);
  log.info("serviceStatus"+serviceStatus);
  if(serviceStatus.equals("VALIDATE_ERROR"))
  {
    response.setResponseMessage("FAILED");
    if(null!=errMessage)
    {
      log.error("errMessage is"+errMessage);
      OmsErrorCodes theErrorObj=OMSUtil.parseErrorString(errMessage);
      List<CustomerOrderCancelResponseItems> responseItemLists=response.getCustomerOrderCancelResponseItems();
      for(CustomerOrderCancellationItems item:input.getCancellationItems())
      {
        CustomerOrderCancelResponseItems customerOrderResponseItems=new CustomerOrderCancelResponseItems();
        log.info("theErrorObj.getOmsErrorCode()"+theErrorObj.getOmsErrorCode());
        log.info("theErrorObj.getOmsErrLangDesc()"+theErrorObj.getOmsErrLangDesc());
        customerOrderResponseItems.setMessageCode(theErrorObj.getOmsErrorCode());
        customerOrderResponseItems.setMessageDesc(theErrorObj.getOmsErrLangDesc());
        customerOrderResponseItems.setLineNo(item.getLineNo());
        customerOrderResponseItems.setItem(item.getItem());
        customerOrderResponseItems.setCancelQtySuom(item.getCancelQtySuom());
        responseItemLists.add(customerOrderResponseItems);
      }
    }
    response.setMessageStatus("F");
  }
  else if(serviceStatus.equals("EXT_SYS_ERROR"))
  {
    log.info("errMessage="+errMessage);
    response.setResponseMessage("FAILED");
    response.setMessageStatus("E");
    if(null!=errMessage)
    {
      OmsErrorCodes theErrorObj=OMSUtil.parseErrorString(errMessage);
      log.info("theErrorObj.getOmsErrorCode()"+theErrorObj.getOmsErrorCode());
      log.info("theErrorObj.getOmsErrLangDesc()"+theErrorObj.getOmsErrLangDesc());
    }
  }
  else if(serviceStatus.equals("SUCCESS"))
  {
    response.setResponseMessage("SUCCESS");
    response.setMessageStatus("S");
    List<CustomerOrderCancelResponseItems> responseItemLists=response.getCustomerOrderCancelResponseItems();
    for(CustomerOrderCancellationItems item:input.getCancellationItems())
    {
      CustomerOrderCancelResponseItems customerOrderResponseItems=new CustomerOrderCancelResponseItems();
      customerOrderResponseItems.setMessageCode("SUCCESS");
      customerOrderResponseItems.setMessageDesc("Order cancelled successfully.");
      customerOrderResponseItems.setLineNo(item.getLineNo());
      customerOrderResponseItems.setItem(item.getItem());
      customerOrderResponseItems.setCancelQtySuom(item.getCancelQtySuom());
      responseItemLists.add(customerOrderResponseItems);
    }
  }
  return response;
}
}
