package com.oracle.retail.oms.integration.services.customerorderservice.v1;


import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.soap.SOAPException;

public class GenerateNewOrderId {
    private final  static Logger log =Logger.getLogger(GenerateNewOrderId.class.getName());
    public GenerateNewOrderId() {
        super();
    }
    private  Connection createConnection() throws SOAPException {
         //System.out.println("***Start createConnection***");
         Connection connection = null;
         Properties props = new Properties();
         try
         {      
             Context initContext= OMSUtil.getInstance().getInitialContext();
             DataSource ds = (DataSource)initContext.lookup("jdbc/oms");
             connection = ds.getConnection();
         
         } 
         catch (Exception e)
         {
             System.out.println("unable to connected to db" + e +"retrying");
            
            //   log.error("unable to connect to database"+e);
            try {
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
                env.put(Context.PROVIDER_URL, "http://exvm-tstadfapp01.extrastores.com:7511");
              // env.put(Context.PROVIDER_URL, "http://licrpap14-extra.logicindia.com:8014");
                Context initContext = new InitialContext(env);
                DataSource ds = (DataSource)initContext.lookup("jdbc/oms");
                connection = ds.getConnection();
                
               
            } catch (Exception f) {
                System.out.println("unable to connected to db" + e );
            }
        }
         return  connection;         
     }
    
    public long generateCustomerOrderId() throws SOAPException {
        System.out.println("***Start generateCustomerOrderId***");
        long custOrderId =0;
        Connection conn = null;           
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
       //Check SOH in DAS schema
        String query = "select oms_cust_id_seq.nextval from dual"; 
        
            try{

        conn = createConnection();
                Statement stmt = conn.createStatement();
           
          rs = stmt.executeQuery(query);
        
            while (rs.next()) {                  
                    custOrderId = rs.getLong(1);
                    System.out.println("custOrderId : " + custOrderId);
            }
            }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally{
            try {
                    rs.close();
                   
                    conn.close();
                  } catch (SQLException e) {
                    e.printStackTrace();
                  }
                
          
        }
          return custOrderId;  
    }
}
