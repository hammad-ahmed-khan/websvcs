package org.datacontract.schemas._2004._07.extra_services_shipping.repo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;



public class OMSUtilDBConnection {

     private static final OMSUtilDBConnection instance = new OMSUtilDBConnection();

     public static OMSUtilDBConnection getInstance() {

         return instance;
     }

     private OMSUtilDBConnection() {

     }
     Properties props = new Properties();

     public Properties LoadErrorMessages() throws SOAPException {

         try {
             InputStream fis = getClass().getClassLoader().getResourceAsStream("app.properties");
             props.load(fis);
         } catch (FileNotFoundException e) {
             throw new SOAPFaultException(newSoapFault(e.toString()));
         } catch (IOException e) {
             throw new SOAPFaultException(newSoapFault(e.toString()));
         }
         return props;
     }


     public SOAPFault newSoapFault(String soapEM) throws SOAPException {
         SOAPFactory fac = SOAPFactory.newInstance("SOAP 1.2 Protocol");
         SOAPFault sf = fac.createFault(soapEM, new QName("http://www.w3.org/2003/05/soap-envelope", "Receiver", "X"));

         return sf;
     }

     public static Context getInitialContext() throws SOAPException {
         Hashtable env = new Hashtable();
         String serverUrl = OMSUtilDBConnection.getInstance().LoadErrorMessages().getProperty("ServerUrl");
         String port = OMSUtilDBConnection.getInstance().LoadErrorMessages().getProperty("port");
         String serverAddress = "http://" + serverUrl + ":" + port;
         env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
         env.put(Context.PROVIDER_URL, serverAddress);
         try {
             return new InitialContext(env);

         } catch (NamingException e) {
             throw new SOAPFaultException(OMSUtilDBConnection.getInstance().newSoapFault(e.toString()));
             //  throw e;
         }
     }

     

     public static Connection createDBConnection(String dataSource) throws Exception {
         Connection connection = null;
         try 
         {
             Context initContext = new InitialContext();
             DataSource ds = (DataSource)initContext.lookup(dataSource);
             connection = ds.getConnection();
         } 
         catch (Exception e)
         {
             throw new Exception("Unable to connect to database");
         }
         return connection;
     }

     public static void closeDBConnection(Connection theConn, CallableStatement theStmt) throws SQLException {
         if (!theStmt.isClosed()) {
             theStmt.close();
         }
         if (!theConn.isClosed()) {
             theConn.close();
         }
     }

     public static void closeDBConnection(Connection theConn, PreparedStatement theStmt, ResultSet rs) {
         try {
             if (rs != null && !rs.isClosed())
                 rs.close();
             if (theStmt != null && !theStmt.isClosed())
                 theStmt.close();
             if (theConn != null && !theConn.isClosed())
                 theConn.close();
         } catch (Exception e) {
         }
     }
     
     public static boolean isResultSetEmpty(ResultSet rs) throws SQLException {
         return (!rs.isBeforeFirst() && rs.getRow() == 0);
     }
     
    
}
