package com.logicinfo.oms.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
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

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsErrorCodes;

public class OMSUtil {
	private final static Logger log = Logger.getLogger(OMSUtil.class.getName());
	private static final OMSUtil instance = new OMSUtil();

	private static Map<String, DataSource> datasourceMap = new HashMap<String, DataSource>(3);

	public static OMSUtil getInstance() {
		return instance;
	}

	private OMSUtil() {
		initialize();
	}

	private Properties props = new Properties();

	private void initialize() {
		try {
			InputStream fis = getClass().getClassLoader().getResourceAsStream("app.properties");
			props.load(fis);
		} catch (IOException e) {
			log.error("Error while loading the property file app.properties ", e);
		}
	}

	public Properties getPropertyResource() throws SOAPException {

		return props;
	}

	public SOAPFault newSoapFault(String soapEM) throws SOAPException {
		SOAPFactory fac = SOAPFactory.newInstance("SOAP 1.2 Protocol");
		SOAPFault sf = fac.createFault(soapEM, new QName("http://www.w3.org/2003/05/soap-envelope", "Receiver", "X"));

		return sf;
	}

	public static Context getInitialContext() throws SOAPException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		String serverUrl = OMSUtil.getInstance().getPropertyResource().getProperty("ServerUrl");
		String port = OMSUtil.getInstance().getPropertyResource().getProperty("port");
		String serverAddress = "http://" + serverUrl + ":" + port;
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		// env.put(Context.PROVIDER_URL, "http://localhost:7101");
		// env.put(Context.PROVIDER_URL, "http://licrpap14-extra.logicindia.com:8014");
		env.put(Context.PROVIDER_URL, serverAddress);
		// env.put(Context.SECURITY_PRINCIPAL, "weblogic");
		// env.put(Context.SECURITY_CREDENTIALS, "welcome1");
		/*
		 * env.put(Context.PROVIDER_URL, "http://licrpap14-extra.logicindia.com:7020");
		 * env.put(Context.SECURITY_PRINCIPAL, "developer");
		 * env.put(Context.SECURITY_CREDENTIALS, "developer123");
		 */
		// env.put("java.naming.provider.url", "http://localhost:7001");
		try {
			return new InitialContext(env);

		} catch (NamingException e) {
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
			// throw e;
		}
	}

	public static OMSUtilSessionEJB doLookup() throws SOAPException {
		final Context context;
		OMSUtilSessionEJB utilSessionEJB = null;
		try {
			context = OMSUtil.getInitialContext();
			utilSessionEJB = (OMSUtilSessionEJB) context.lookup("OMSUtil-OMSUtil-OMSUtilSessionEJB#com.logicinfo.oms.ejb.OMSUtilSessionEJB");
		} catch (NamingException e) {
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
		}

		return utilSessionEJB;
	}

	public static Connection createDBConnection(String dataSource) throws Exception {
		Connection connection = null;
		try {
			log.info("inside createDBConnection method");
			Context initContext = new InitialContext();
			log.info("created object for  InitialContext ,dataSource: " + dataSource);

			DataSource ds = (DataSource) initContext.lookup(dataSource);
			log.info("after passing the  dataSource " + dataSource + " to lookup method ");

			connection = ds.getConnection();
			log.info("After getting the connection");
		} catch (Exception e) {
			throw new Exception("Unable to connect to database");
		}
		return connection;
	}

	public static Connection getDBConnection(String dataSource) throws Exception {
		Connection connection = null;
		Context initContext = null;
		try {
			log.info("created object for  InitialContext ,dataSource: " + dataSource);

			DataSource ds = datasourceMap.get(dataSource);
			if (ds == null) {
				log.info("inside createDBConnection method");
				initContext = new InitialContext();
				ds = (DataSource) initContext.lookup(dataSource);
				log.info("after passing the  dataSource " + dataSource + " to lookup method ");
				datasourceMap.put(dataSource, ds);
				try {
					initContext.close();
				} catch (Exception e) {
					// EAT Exception
				}
			}
			connection = ds.getConnection();
			log.info("After getting the connection");
		} catch (Exception e) {
			throw new Exception("Unable to connect to database");
		}
		return connection;
	}

	public static void closeDBConnection(Connection theConn, CallableStatement theStmt) throws SQLException {
		try {
			if (!theStmt.isClosed()) {
				theStmt.close();
			}
		} catch (Exception e) {
		}
		try {
			if (!theConn.isClosed()) {
				theConn.close();
			}
		} catch (Exception e) {
		}
	}

	public static void closeDBConnection(Connection theConn, PreparedStatement theStmt, ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (Exception e) {
		}
		try {
			if (theStmt != null && !theStmt.isClosed())
				theStmt.close();
		} catch (Exception e) {
		}
		try {
			if (theConn != null && !theConn.isClosed())
				theConn.close();
		} catch (Exception e) {
		}
	}

	public static boolean isResultSetEmpty(ResultSet rs) throws SQLException {
		return (!rs.isBeforeFirst() && rs.getRow() == 0);
	}

	public static OmsErrorCodes parseErrorString(String errorString) {
		OmsErrorCodes omsErrorObj = new OmsErrorCodes();

		// If the Error is thrown is a business error then its format would be
		// ERROR_CODE|ERROR_LANGUAGE|ERROR_DESCRIPTION and will be be processed as
		// below.
		String[] errorComponents = errorString.split("\\|");

		if (!errorString.contains("|") || errorComponents.length != 3) {
			omsErrorObj.setOmsErrorCode(OMSConstants.ERR_UNKNOWN_CODE);
			omsErrorObj.setLangCode("1");
			omsErrorObj.setOmsErrLangDesc(errorString);
		} else if (errorComponents.length == 3) {
			omsErrorObj.setOmsErrorCode(errorComponents[0]);
			omsErrorObj.setLangCode(errorComponents[1]);
			omsErrorObj.setOmsErrLangDesc(errorComponents[2]);
		}
		return omsErrorObj;
	}

}
