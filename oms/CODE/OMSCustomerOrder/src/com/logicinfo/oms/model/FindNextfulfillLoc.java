package com.logicinfo.oms.model;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.utils.ProjectUtils;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Logger;
public class FindNextfulfillLoc
{
public FindNextfulfillLoc()
{
  super();
}
private final static Logger log=Logger.getLogger(FindNextfulfillLoc.class.getName());
public BigDecimal processFulfillmentMatrixGetCombID(BigDecimal reqId,String itemType,String custCity,
                                                    String modeOfDelv, String deliveryZone, String marketPlaceInd, String applicationId, String shipToStore) throws EntityAlreadyExistsWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                              SOAPException
{
  log.info("***Start processFulfillmentMatrixGetCombID***");
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  BigDecimal combinationID=null;
  //Fetching combinationId
  try
  {
    log.info("Finding combination for "+"reqId"+reqId+"itemType"+itemType+"custCity"+custCity+"modeOfDelv"+modeOfDelv);
    combinationID=session.getOmsFulfillMatrixExtHeadFindCombination(reqId,itemType,custCity,modeOfDelv, deliveryZone, marketPlaceInd, applicationId, shipToStore);
  }
  catch(Exception e)
  {
    log.error("Combination ID unavailable for the given record ");
    OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
    //   omsCustomerOrderBean.rollback();
    try
    {
      combinationID=session.getOmsFulfillMatrixExtHeadFindCombination(reqId,itemType,"ALL",modeOfDelv,deliveryZone, marketPlaceInd, applicationId, shipToStore);
    }
    catch(Exception f)
    {
      //   throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Combination ID unavailable for the given record"));
      String errString=OMSUtilCommons.formErrorDescription("UNAVL_COMB_ID","1",new String[]{ });
      log.error(errString);
      throw new SOAPException("UNAVL_COMB_ID");
    }
  }
  ProjectUtils.setCombinationID(combinationID); 
  return combinationID;
}
public BigDecimal processFulfillmentMatrixGetCombIDWoCity(BigDecimal reqId,String itemType,
                                                          String modeOfDelv, String DeliveryZone,String marketPlaceInd, String applicationId, String shipToStore) throws EntityAlreadyExistsWSFaultException,
                                                                                    com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                                    com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                                    com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                                    com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                                    com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                                    com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                    com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                    com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                                    com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                                    SOAPException
{
  log.info("***Start processFulfillmentMatrixGetCombIDWoCity***");
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  BigDecimal combinationID=null;
  try
  {
    log.info("reqId"+reqId+"itemType"+itemType+"modeOfDelv"+modeOfDelv);
    combinationID=session.getOmsFulfillMatrixExtHeadFindCombinationWoCity(reqId,itemType,modeOfDelv, DeliveryZone, marketPlaceInd, applicationId,shipToStore);
    log.info(combinationID);
  }
  catch(Exception e)
  {
    e.printStackTrace();
    OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
    // omsCustomerOrderBean.rollback();
    //Combination ID unavailable for the given record
    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("LOC_MTRX_MISSING"));
  }
  ProjectUtils.setCombinationID(combinationID);
  return combinationID;
}
public OmsFulfillMatrixExtDetail processFulfillmentMatrix(BigDecimal combinationID,
                                                          int priority) throws EntityAlreadyExistsWSFaultException,
                                                                               com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                               com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                               com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                               com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                               com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                               com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                               com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                               com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                               com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                               SOAPException
{
  log.info("***Start processFulfillmentMatrixDetail***");
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  OmsFulfillMatrixExtDetail detailFindpriority=null;
  try
  {
    while(detailFindpriority==null)
    {
      log.info("Finding detail for combinationID:"+combinationID+"priority"+priority);
      detailFindpriority=session.getOmsFulfillMatrixExtDetailFindDetail(combinationID,new BigDecimal(priority));
      log.info(detailFindpriority.getLocation());
    }
  }
  catch(Exception e)
  {
    //Next location unavailable for the given record,rollbacking
    log.error("");
    e.printStackTrace();
    //rollbacking orders
    OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
    // omsCustomerOrderBean.rollback();
    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV")); //Unable to locate sufficient inventory
  }
  return detailFindpriority;
}
public String findShipmentClassification(String item,BigDecimal store,
                                         String shippingClassification) throws SOAPException
{
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  Connection con=null;
  CallableStatement pstmt=null;
  int result=0;
  String shipClassification="";
  try
  {
    con=OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
    BigDecimal channelId=session.getStoreFindChannelId(store);
    pstmt=con.prepareCall("{?=call OMS_SHIP_CLASSIFICATION(?,?,?)}");
    pstmt.registerOutParameter(1,Types.VARCHAR);
    pstmt.setString(2,item);
    pstmt.setInt(3,channelId.intValue());
    pstmt.setString(4,shippingClassification);
    pstmt.executeUpdate();
    shipClassification=pstmt.getString(1);
  }
  catch(Exception e)
  {
    e.printStackTrace();
  }
  finally
  {
    try
    {
      pstmt.close();
      con.close();
    }
    catch(SQLException e)
    {
      e.printStackTrace();
    }
  }
  return shipClassification;
}
public void checkFulfilment(CustomerOrderItems custOrdItems,
                            OmsFulfillMatrixExtDetail matrixDetail) throws EntityAlreadyExistsWSFaultException,
                                                                           com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                           com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                           com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                           com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                           com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                           com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                           com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                           com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                           com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                           SOAPException
{
  log.info("***Start processFulfillmentMatrixDetail***");
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  OmsFulfillMatrixExtDetail detailFindpriority=null;
  Boolean flag=false;
  try
  {
    while(detailFindpriority==null)
    {
      log.info("Finding detail for combinationID:"+matrixDetail.getCombinationId()+"priority"+
               matrixDetail.getPriority());
      detailFindpriority=
          session.getOmsFulfillMatrixExtDetailFindDetail(matrixDetail.getCombinationId(),matrixDetail.getPriority());
      log.info("matrixDetail Combination ID ********"+matrixDetail.getCombinationId());
      log.info("matrixDetail Priority ********"+matrixDetail.getPriority());
      log.info("matrixDetail Fulfil Location Type ********"+matrixDetail.getDeliveryFromLocType());
      log.info("matrixDetail Fulfil Location ********"+matrixDetail.getDeliveryFromLoc());
      log.info("matrixDetail Source Loc Type ********"+matrixDetail.getLocationType());
      log.info("matrixDetail Source Loc ********"+matrixDetail.getLocation());
      log.info("custOrdItems Source Loc Type ********"+custOrdItems.getItemSourceLocType());
      log.info("custOrdItems Source Loc ********"+custOrdItems.getItemSourceLoc());
      log.info("custOrdItems Fulfil Location Type ********"+custOrdItems.getItemFulfillLocType());
      log.info("custOrdItems Fulfil Location ********"+custOrdItems.getItemFulfillLoc());
      log.info("Ordered Qty **********: "+custOrdItems.getOrderQtySuom());
      if(matrixDetail.getLocationType().equals(custOrdItems.getItemSourceLocType())&&
         matrixDetail.getLocation().equals(new BigDecimal(custOrdItems.getItemSourceLoc()))&&
         matrixDetail.getDeliveryFromLocType().equals(custOrdItems.getItemFulfillLocType())&&
         matrixDetail.getDeliveryFromLoc().equals(new BigDecimal(custOrdItems.getItemFulfillLoc())))
      {
        log.info("All Good");
      }
      else
      {
        // throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_COMB_ID"));
        String errString=OMSUtilCommons.formErrorDescription("UNAVL_COMB_ID","1",new String[]{ });
        log.error(errString);
        throw new SOAPException("UNAVL_COMB_ID");
      }
      log.info(detailFindpriority.getLocation());
    }
  }
  catch(Exception e)
  {
    //Next location unavailable for the given record,rollbacking
    log.error("");
    e.printStackTrace();
    //rollbacking orders
    OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
    // omsCustomerOrderBean.rollback();
    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_COMB_ID")); //Unable to locate sufficient inventory
  }
}
//processFulfillmentMatrixForCarrera
public OmsFulfillMatrixExtDetail processFulfillmentMatrixForCarrera(BigDecimal combinationID,
                                                                    int priority) throws EntityAlreadyExistsWSFaultException,
                                                                                         com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                                         com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                                         com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                                         com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                                         com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                                         com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                         com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                         com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                                         com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                                         SOAPException
{
  log.info("***Start processFulfillmentMatrixDetail***");
  OMSUtilSessionEJB session=OMSUtil.doLookup();
  OmsFulfillMatrixExtDetail detailFindpriority=null;
  try
  {
    detailFindpriority=session.getOmsFulfillMatrixExtDetailFindDetail(combinationID,new BigDecimal(priority));
    log.info(detailFindpriority.getLocation());
  }
  catch(Exception e)
  {
    //Next location unavailable for the given record,rollbacking
    log.error("");
    e.printStackTrace();
    //rollbacking orders
    OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
    // omsCustomerOrderBean.rollback();
    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV")); //Unable to locate sufficient inventory
  }
  return detailFindpriority;
}
} //End of class
