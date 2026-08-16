package com.logicinfo.oms.beans;


import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class FindNextFulfillLoc {
    public FindNextFulfillLoc() {
        super();
    }
    private final static Logger log = Logger.getLogger(FindNextFulfillLoc.class.getName());
    public OmsFulfillMatrixExtDetail processFulfillmentMatrix(BigDecimal combinationID,
                                                              int priority) throws 
            SOAPException{
        log.info("***Start processFulfillmentMatrixDetail***");
        OMSUtilSessionEJB session =OMSUtil.doLookup();
        OmsFulfillMatrixExtDetail detailFindpriority = null;

        try {

            while (detailFindpriority == null) {
                log.info("Finding detail for combinationID:" + combinationID +"priority"+priority);
                
                detailFindpriority =
                        session.getOmsFulfillMatrixExtDetailFindDetail(combinationID, new BigDecimal(priority));
                log.info(detailFindpriority.getLocation());
            }
        } catch (Exception e) {
            //Next location unavailable for the given record,rollbacking
            log.error("No record found in oms_co_fulfill_detail table for combination id="+combinationID+"and priority"+priority);
           
            //rollbacking orders
           // OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();----check
           // omsCustomerOrderBean.rollback();
            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));//Unable to locate sufficient inventory
        }


        return detailFindpriority;
    }
    
    
    public long findSupplier(String item, String directSupplierInd) throws SOAPException{
        log.info("***Start findSupplier***");
        int supplier =0;
        Connection conn = null;           
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
       
        String query = "select supplier from item_supplier where item=? and direct_ship_ind=? and primary_supp_ind='Y'"; 
        
            try{
      
        
                log.info("connecting to DAS Schema");      
           conn = OMSUtil.createDBConnection(OMSConstants.DS_DAS_STRING);
            preparedStatement = conn.prepareStatement(query);
           
            log.info("Finding supplier for item="+item);

            log.info(Integer.parseInt(item));
            preparedStatement.setString(1,(item));
          
            preparedStatement.setString(2,directSupplierInd);
            
            rs = preparedStatement.executeQuery();
                                        log.info("before rs");
              
                while (rs.next()) {
                        
                        supplier = rs.getInt("supplier");
                        log.info("supplier : " + supplier);
                }
                log.info("After rs.next");
            }
        catch (SQLException e) {
            log.info(e.getMessage());
        }
        catch (Exception e) {
            log.info(e.getMessage());
        }
        finally{
            try {
                    rs.close();
                    preparedStatement.close();
                    conn.close();
                  } catch (SQLException e) {
                    e.printStackTrace();
                  }
                
          
        }
          return supplier;  
    }
    
    
    public String findItemStatus(String item, BigDecimal loc) throws SOAPException
    {
        log.info("***Start findItemStatus***");
        String status="";
        Connection conn = null;           
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
       //Check SOH in DAS schema
       log.info("item "+item);
       log.info("loc "+loc);
        String query = "select status from item_loc where item=? and loc=?"; 
        
            try
            {
                 
                log.info("item "+item);
                log.info("loc "+loc);
                log.info("OMSConstants.DS_DAS_STRING "+OMSConstants.DS_DAS_STRING);
                log.info("connecting to DAS Schema");
                conn = OMSUtil.createDBConnection(OMSConstants.DS_DAS_STRING);
                log.info("conn "+conn);
            preparedStatement = conn.prepareStatement(query);
           
            log.info("before parsing");
          
            //log.info(Integer.parseInt(item));
            preparedStatement.setString(1, item);
           // preparedStatement.setLong(1,Long.parseLong(item));
          
           
            preparedStatement.setInt(2,loc.intValue());
            
            rs = preparedStatement.executeQuery();
                                        log.info("before rs");
            
            while (rs.next())
            {
                    log.info("before avail");
                    status = rs.getString("status");
                    log.info("status : " + status);
            }
            }
        catch (SQLException e) {
            log.info(e.getMessage());
        }
        catch (Exception e) {
            log.info(e.getMessage());
        }
        finally{
            try {
                    rs.close();
                    preparedStatement.close();
                    conn.close();
                  } catch (SQLException e) {
                    e.printStackTrace();
                  }
                
          
        }
          return status;  
    }
    
    
    public CustFutureInvPosition findFutInvDateAndQty(String item, Long location, BigDecimal alloctedInventory,BigDecimal pendingQty,String locType,int channelId) throws SQLException {
       
        log.info("inside findFutInvDateAndQty for location "+location);
         CustFutureInvPosition custFutureInvPosition=new CustFutureInvPosition();
         BigDecimal SOH = new BigDecimal(0);
         Date futureDate=new Date();
         Connection conn = null;
         PreparedStatement preparedStatement = null;
         ResultSet rs = null;
        String query="";
        if(locType.equals("WH"))
        {
         query="select * from V_CUST_FUTURE_INV_POSITION where item=? and location=? and channel_id=? order by expected_date ";
        }else {
            query="select * from V_CUST_FUTURE_INV_POSITION where item=? and location=? order by expected_date ";
        }
        log.info(query);
         try {
             int targetQty=alloctedInventory.intValue()+pendingQty.intValue();
             log.info("targetQty="+targetQty+"alloctedInventory="+alloctedInventory+"pendingQty"+pendingQty);
             conn = OMSUtil.createDBConnection(OMSConstants.DS_DAS_STRING);
             preparedStatement = conn.prepareStatement(query);
             preparedStatement.setLong(1,Long.parseLong(item));
             preparedStatement.setLong(2, location);
             if(channelId>0) {
                 preparedStatement.setInt(3, channelId);
             }
             rs = preparedStatement.executeQuery();
             custFutureInvPosition.setExpectedQty(new BigDecimal(0));
             custFutureInvPosition.setExpectedDate(null);
           int expectedQty=0;
             while (rs.next()  ) 
             {
                 expectedQty=expectedQty+rs.getBigDecimal("EXPECTED_QTY").intValue();
                
                  //   custFutureInvPosition.setExpectedQty(rs.getBigDecimal("EXPECTED_QTY"));
                  
                     custFutureInvPosition.setExpectedDate(rs.getDate("EXPECTED_DATE"));  
             }
            log.info("expectedQty="+expectedQty);
             if(expectedQty<=alloctedInventory.intValue()) 
             {
                 custFutureInvPosition.setExpectedQty(new BigDecimal(0));   
             }else {
                 custFutureInvPosition.setExpectedQty(new BigDecimal(expectedQty).subtract(alloctedInventory));
             }
             
         } catch (Exception e) {
            e.getMessage();
        
         } finally {
             try {
                 OMSUtil.closeDBConnection(conn, preparedStatement, rs);
             } catch (Exception e) {
               e.getMessage();
             }
         }
         return custFutureInvPosition;
    }
}
