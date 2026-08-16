package com.logicinfo.oms.model;
import com.logicinfo.oms.util.OMSUtil;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.Date;
import org.apache.log4j.Logger;


import javax.xml.soap.SOAPException;
public class PersistPaymentRequest
{
  public final static Logger log=Logger.getLogger(com.logicinfo.oms.model.PersistPaymentRequest.class.getName());

  public PersistPaymentRequest()
  {
   
  }
  public static boolean checkPaymentProgress(String customerOrderNo,BigDecimal omsCustOrdNo)
  {
    Connection conn=null;
    PreparedStatement pstmt=null;
    ResultSet rs = null;
    String selectQuery = "select *  from  Oms_PaymentConf_Audit where customerOrderNo =? and OMS_CUST_ORD_NO =?";
    try
    {
        conn=OMSUtil.createDBConnection("jdbc/oms");
        pstmt=conn.prepareStatement(selectQuery);
        pstmt.setString(1,customerOrderNo);
        pstmt.setBigDecimal(2,omsCustOrdNo);
        rs = pstmt.executeQuery();
        if(rs.next())
        {
          log.info("customerOrderNo "+customerOrderNo + "for omsCustOrdNo "+omsCustOrdNo +"record exist");
          return true;
        }
    }
    catch(Exception e)
    {
      log.info(omsCustOrdNo +" Exception while retriving the record into PaymentConfAudittable "+e.getMessage());
    }
    finally
    {
      OMSUtil.closeDBConnection(conn,pstmt,rs);
    }
    return false;
    
  }
  public static void deletePaymentAuditTable(String customerOrderNo,BigDecimal omsCustOrdNo)
  {
    Connection conn=null;
    PreparedStatement pstmt=null;
    String deleteQuery ="delete from Oms_PaymentConf_Audit where customerOrderNo =? and OMS_CUST_ORD_NO =?";
    try
    {
        conn=OMSUtil.createDBConnection("jdbc/oms");
        pstmt=conn.prepareStatement(deleteQuery);
        pstmt.setString(1,customerOrderNo);
        pstmt.setBigDecimal(2,omsCustOrdNo);
        pstmt.executeUpdate();
    }
    catch(Exception e)
    {
      log.info(omsCustOrdNo +" Excepton while deleting the record into PaymentConfAudittable "+e.getMessage());
    }
    finally
    {
      OMSUtil.closeDBConnection(conn,pstmt,null);
    }
  }
  public static void persistInPaymentAuditTable(String customerOrderNo, BigDecimal omsCustOrdNo)
  {
    Connection conn=null;
    PreparedStatement pstmt=null;
    String insertQuery="insert into Oms_PaymentConf_Audit  (customerOrderNo,OMS_CUST_ORD_NO,CREATE_DATETIME) values(?,?,?)";
    try
    {
        conn=OMSUtil.createDBConnection("jdbc/oms");
        pstmt=conn.prepareStatement(insertQuery);
        pstmt.setString(1,customerOrderNo);
        pstmt.setBigDecimal(2,omsCustOrdNo);
        pstmt.setTimestamp(3,new Timestamp(new Date().getTime()));
        pstmt.executeUpdate();
        log.info(omsCustOrdNo +"persisted into the PaymentConfAudittable table ");
    }
    catch(Exception e)
    {
      log.info(omsCustOrdNo +" Excepton while inserting the record into PaymentConfAudittable "+e.getMessage());
    }
    finally
    {
      OMSUtil.closeDBConnection(conn,pstmt,null);
    }
  }
  
  public static CoPaymentConfResponse returnTenderStatusResponse(CoPaymentConf input,String status) throws SOAPException
  {
    CoPaymentConfResponse response=new CoPaymentConfResponse();
    response.setApplicationId(input.getApplicationId());
    response.setCustomerOrderNo(input.getCustomerOrderNo());
    response.setEntityId(input.getEntityId());
    response.setComments(input.getComments());
    response.setMessageStatus("F"); 
    response.setRequestDatetimestamp(input.getRequestDatetimestamp());
    response.setMessageCode(status);
    response.setMessageDesc("Payment already inprogress");
    
    return response;
  }
}
