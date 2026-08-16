package com.logicinfo.oms.beans;


import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;


public class OMSUtilJdbc 
{
    public OMSUtilJdbc() 
    {
        super();
    }
    private final static Logger log = Logger.getLogger(OMSUtilJdbc.class.getName());

    public BigDecimal getselectedandDistroQty(String item,BigDecimal tsfNo) throws Exception 
    {
        log.info("***Start getselectedandDistroQty ***");
        BigDecimal  totalSelectedDistroQty = BigDecimal.ZERO;
        BigDecimal  totalDistroQty = BigDecimal.ZERO;
        BigDecimal  totalShipQty = BigDecimal.ZERO;
        Connection conn = null;
        BigDecimal selectedQty=BigDecimal.ZERO;
        BigDecimal distroQty=BigDecimal.ZERO;
        BigDecimal shipQty=BigDecimal.ZERO;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        log.info("item "+item);
        log.info("tsfNo "+tsfNo);
        String query = "select distro_Qty,selected_Qty,ship_Qty  from tsfdetail where item= ? and tsf_no =?";
        log.info("query "+query);
        try 
        {
            conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,item);
            log.info("setted item in preparedStatement");
            preparedStatement.setBigDecimal(2,tsfNo);
            log.info("setted tsfNo in preparedStatement");
            rs = preparedStatement.executeQuery();
            log.info("after getting rs");
            while (rs.next()) 
            {
                log.info("inside while");
                distroQty=rs.getBigDecimal("distro_Qty");
                selectedQty = rs.getBigDecimal("selected_Qty");
                shipQty = rs.getBigDecimal("ship_Qty");
                log.info("selectedQty "+selectedQty);
                log.info("distroQty "+distroQty);
                log.info("shipQty "+shipQty);
                if(selectedQty==null)
                {
                   selectedQty=BigDecimal.ZERO;    
                }
                if(distroQty==null)
                {
                   distroQty=BigDecimal.ZERO;    
                }
                if(shipQty==null)
                {
                   shipQty=BigDecimal.ZERO;    
                }
                
                log.info("+++++++++++++before setting value for totalSelectedDistroQty"+totalSelectedDistroQty);
                
                log.info("+++++++++++++before setting value for totalSelectedDistroQty with shipQty"+totalSelectedDistroQty);
                totalDistroQty=selectedQty.add(distroQty);
                log.info("+++++++++++++after setting value for totalSelectedDistroQty with shipQty"+totalDistroQty);
                totalShipQty=shipQty.add(distroQty);
                log.info("+++++++++++++before setting value for totalSelectedDistroQty with distroQty"+totalShipQty);
                totalSelectedDistroQty=totalShipQty.add(totalDistroQty);
                log.info("+++++++++++++after setting value for totalSelectedDistroQty with distroQty"+totalSelectedDistroQty);
                
                log.info("totalSelecetedDistroQty "+totalSelectedDistroQty);
                
            }
            
        }    
        catch (Exception e) 
        {
                    log.error(e.getMessage());
                    throw new SOAPException(e.getMessage());
        }
        finally 
        {
             try
             {
                   OMSUtil.closeDBConnection(conn, preparedStatement, rs);
             } 
             catch (Exception e)
             {
                 log.error(e.getMessage());
                 throw new SOAPException(e.getMessage());
             }
        }  

        
       return totalSelectedDistroQty;
    }
    
    public void persistintoOmsOrposMasterAudit(String customerOrderId,
                                               BigDecimal omsCustOrdNo,
                                               String transactionNo,
                                               BigDecimal lineNo,
                                               BigDecimal pickupQuantity,
                                               BigDecimal cancelQuantity,
                                               BigDecimal returnQuantity,
                                               BigDecimal cancelReqId,
                                               BigDecimal rmaReqId,
                                               String eventId,
                                               String status)
                                               throws SOAPException
    {
        log.info("***insode persistintoOmsOrposMasterAudit ***");
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        log.info("customerOrderId "+customerOrderId);
        log.info("omsCustOrdNo "+omsCustOrdNo);
        log.info("transactionNo "+transactionNo);
        log.info("lineNo "+lineNo);
        log.info("pickupQuantity "+pickupQuantity);
        log.info("cancelQuantity "+cancelQuantity);
        log.info("returnQuantity "+returnQuantity);
        log.info("cancelReqId "+cancelReqId);
        log.info("rmaReqId "+rmaReqId);
        log.info("eventId "+eventId);
        log.info("status "+status);
        Timestamp t1=new Timestamp(new java.util.Date().getTime());
        log.info("t1 "+t1);
        try 
        {
          String query="INSERT INTO oms_orpos_master_audit " + 
                       " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                      
          log.info("query "+query); 
          conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
          preparedStatement = conn.prepareStatement(query);
          log.info("inserting into table");
            preparedStatement.setString(1,customerOrderId);
            log.info("setted customerOrderId");
            preparedStatement.setBigDecimal(2,omsCustOrdNo);
            log.info("setted omsCustOrdNo");
            preparedStatement.setString(3,transactionNo);
            log.info("setted transactionNo");
            preparedStatement.setBigDecimal(4,lineNo);
            log.info("setted lineNo");
            preparedStatement.setBigDecimal(5,pickupQuantity);
            log.info("setted pickupQuantity");
            preparedStatement.setBigDecimal(6,cancelQuantity);
            log.info("setted cancelQuantity");
            preparedStatement.setBigDecimal(7,returnQuantity);
            log.info("setted returnQuantity");
            preparedStatement.setBigDecimal(8,cancelReqId);
            log.info("setted cancelReqId");
            preparedStatement.setBigDecimal(9,rmaReqId);
            log.info("setted rmaReqId");
            preparedStatement.setString(10,eventId);
            log.info("setted eventId");
            preparedStatement.setString(11,status);
            log.info("setted status");
            preparedStatement.setTimestamp(12,new Timestamp(new java.util.Date().getTime()));
            log.info("setted Timestamp");
            preparedStatement.setTimestamp(13,null);
            log.info("inserting into oms_orpos_master_audit ");
          preparedStatement.executeUpdate();
          log.info("inserted into oms_orpos_master_audit");
            
        }
        catch (Exception e) 
        {
                    log.error(e.getMessage());
                    throw new SOAPException(e.getMessage());
        }
        finally 
        {
             try
             {
                   OMSUtil.closeDBConnection(conn, preparedStatement, rs);
             } 
             catch (Exception e)
             {
                 log.error(e.getMessage());
                 throw new SOAPException(e.getMessage());
             }
        }  
        
    }

}

