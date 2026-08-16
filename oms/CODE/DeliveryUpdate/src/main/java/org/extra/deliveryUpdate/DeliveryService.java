package org.extra.deliveryUpdate;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;

public class DeliveryService
{

  private static final Logger logger = LogManager.getLogger(DeliveryService.class.getName());

  public Connection getDBForOMSConnection()
  {
    Context ctx = null;
    Connection conn = null;
    try
    {
      ctx = new InitialContext();

      DataSource ds = (DataSource) ctx.lookup("jdbc/oms");

      conn = ds.getConnection();
    }
    catch (Exception e)
    {
      logger.info("Exception while creating a connection for OMS " + e.getMessage());
    }

    return conn;
  }

  public DeliveryResponse updateDeliveryService(DeliveryRequest request)
  {

    logger.info("Inside updateDeliveryService method for omsCustOrdNo " + request.getOrderNo());
    DeliveryResponse response = new DeliveryResponse();
//    BigDecimal omsCustOrdNo = BigDecimal.ZERO;
    int omsCustOrdNo = 0;
    if (request.getOmsCustOrdNo() == 0)
    {
      omsCustOrdNo = getOmsCustOrdNoFromOmsCustOrdHead(request.getOrderNo());
      logger.info("omsCustOrdNo form head table for " + request.getOrderNo() + "is " + omsCustOrdNo);
    }
    else
    {
      omsCustOrdNo = request.getOmsCustOrdNo();
    }
    Connection conn = getDBForOMSConnection();
    CallableStatement cstmt = null;

    if (conn == null)
    {
      response.setStatus("Failure");
      response.setMessage("Unable to connect to DB");
      return response;
    }
    try
    {
      String packageCallStmt = "{?= call XX_DLV_ADDRESS_UPDATE.VALIDATE_UPDATE_REQUEST(?, ?, ?, ?, ?, ?, ?, ?, ?) }";

      logger.info("Passing the package to preparecall  for omscustordno " + omsCustOrdNo);
      cstmt =  conn.prepareCall(packageCallStmt);
      
      cstmt.registerOutParameter(1, Types.VARCHAR);
      logger.info("setting parameter 1");
      
      cstmt.registerOutParameter(9, Types.VARCHAR);
      logger.info("setting parameter 1");
     
      logger.info("Setting the orderNo " + request.getOrderNo() + " for omsCustOrdNo " + omsCustOrdNo);
      cstmt.setString(2, request.getOrderNo());

      logger.info("Setting the omsCustOrdNo " + request.getOmsCustOrdNo());
      cstmt.setInt(3, omsCustOrdNo);

      logger.info("Setting the classification " + request.getClassification() + " for omsCustOrdNo " + omsCustOrdNo);
      cstmt.setString(4, request.getClassification());

      if (request.getFirstName() != null)
      {
        logger.info("Setting the FirstName " + request.getFirstName() + " for omsCustOrdNo " + omsCustOrdNo);
        cstmt.setString(5, request.getFirstName());
      }

      if (request.getLastName() != null)
      {
        logger.info("Setting the LastName " + request.getLastName() + " for omsCustOrdNo " + omsCustOrdNo);
        cstmt.setString(6, request.getLastName());
      }

      if (request.getAddress() != null)
      {
        logger.info("Setting the Address " + request.getAddress() + " for omsCustOrdNo " + omsCustOrdNo);
        cstmt.setString(7, request.getAddress());
      }

      if (request.getMobileNo() != null)
      {
        logger.info("Setting the MobileNo " + request.getMobileNo() + " for omsCustOrdNo " + omsCustOrdNo);
        cstmt.setString(8, request.getMobileNo());
      }else {
    	  cstmt.setNull(8, Types.VARCHAR);
      }
      cstmt.registerOutParameter(10, Types.VARCHAR);
      logger.info("setting parameter 10");

      logger.info("Calling executeUpdate for omsCustOrdNo " + omsCustOrdNo);
      
      cstmt.execute();
      
      logger.info("No. of rows updated for omsCustOrdNo " + omsCustOrdNo + "is ");

      String responseStatus =  cstmt.getString(1);
      String responseMsg =  cstmt.getString(10);
      logger.info("responseMsg from package for omsCustOrdNo " + omsCustOrdNo + " and the result is" + responseMsg);

      response.setStatus(responseStatus);
      if (response != null)
      {
        response.setMessage(responseMsg);
      }
      
    }
    catch (SQLException e)
    {
      logger.info("SQL exception occured while calling the package for omsCustOrdNo " + omsCustOrdNo + " is " + e.getMessage());
      response.setStatus("Failure");
      response.setMessage(e.getMessage());
    }

    finally
    {
      closeDBConnection(conn, cstmt);
    }
    return response;
  }

  private int getOmsCustOrdNoFromOmsCustOrdHead(String orderNo)
  {
    int omsCustOrdNo = 0;
    
    return omsCustOrdNo;
  }

  private void closeDBConnection(Connection conn, CallableStatement cstmt)
  {
    try
    {
      if (conn != null)
      {
        conn.close();
      }
      if (cstmt != null)
      {
        cstmt.close();
      }

    }
    catch (Exception e)
    {
      logger.info("Exception occured while closing the connection " + e.getMessage());

    }

  }

}
