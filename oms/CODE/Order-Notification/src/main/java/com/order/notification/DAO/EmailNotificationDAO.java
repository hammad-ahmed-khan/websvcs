package com.order.notification.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.PropertiesReader;
import com.order.notification.mail.bean.Email;
import com.order.notification.mail.bean.EmailInfo;
import com.order.notification.mail.bean.Recipients;
import com.order.notification.mail.bean.To;

public class EmailNotificationDAO {
	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);

	Connection sim14Conn;
	Connection omsConn;
	PreparedStatement getOrderNoPeSTMT = null;
	PreparedStatement getReminderOrderNoPeSTMT = null;
	PreparedStatement getStoreManagerDetailsPeSTMT = null;
	PreparedStatement getSystemParameterPeSTMT = null;
	PreparedStatement updateEmailFlagPeSTMT = null;
	PreparedStatement getOrderStatusPeSTMT = null;
	PreparedStatement updateOrderPickStatusPeSTMT = null;
	PreparedStatement updateEmailStatusFlagPreStmt = null;
	PreparedStatement getCutomerDataPeSTMT = null;
	PreparedStatement getOrderTypePeSTMT = null;

	String getOrderNo = "select CUST_ORDER_NO,STORE_NO from CFS_SMS_EMAIL_STATUS_INFO where ORDER_PICK_STATUS = '0' and EMAIL_STATUS_CODE ='0' and EMAIL_PROCESS_IND='N' and trunc(CREATE_DATETIME)=trunc(sysdate) and STORE_NO NOT IN (select store_id from oms_cfs_store_noEmail)";
	String getReminderOrderNo = "select CUST_ORDER_NO,STORE_NO from CFS_SMS_EMAIL_STATUS_INFO where ORDER_PICK_STATUS != '1' and EMAIL_STATUS_CODE ='1' and EMAIL_PROCESS_IND='N' and trunc(CREATE_DATETIME)=trunc(sysdate) and STORE_NO NOT IN (select store_id from oms_cfs_store_noEmail)";
	String getOrderStatus = "select STATUS from sim14.FUL_ORD where CUST_ORDER_ID = ?";
	String updateEmailFlag = "update OMSDEV.CFS_SMS_EMAIL_STATUS_INFO set EMAIL_STATUS_CODE = ? where CUST_ORDER_NO =?";
	// Charan - QA
	// String getStoreManagerDetails = "select EMAILD from RMS14.XXK_STORE_CONTACT_V2 where EMAILD IS NOT NULL and STOREID = ? and EMAILD NOT IN (select EMAILID from extradev.restricted_mail)";
	// Charan - PROD
	String getStoreManagerDetails = "select EMAILD from extradev.xxk_store_contact_v where EMAILD IS NOT NULL and STOREID = ? and EMAILD NOT IN (select EMAILID from restricted_mail)";
	String getFromEmail = "SELECT PARAMETER_VALUE, PARAMETER_NAME FROM OMS_CFS_SYSTEM_PARAMETERS where PARAMETER_ID =?";
	String updateOrderPickStatus = "update OMSDEV.CFS_SMS_EMAIL_STATUS_INFO set ORDER_PICK_STATUS = ? where CUST_ORDER_NO =?";
	String updateStatusFlag = "UPDATE OMSDEV.CFS_SMS_EMAIL_STATUS_INFO SET EMAIL_PROCESS_IND='Y' WHERE CUST_ORDER_NO =?";
	String orderType = "select DELIVERY_TYPE from OMS_CUST_ORD_HEAD where ORD_PAYMENT_STATUS ='S' AND STATUS='S' AND CUST_ORDER_NO =?";
	String getCutomerData = "select H.CUST_ORDER_NO ,H.DELIVERY_TYPE ,A.BILL_FIRST_NAME ,A.DELIVER_PHONE_NO ,"
			+ " H.CREATE_DATETIME ,I.EXPECTED_DELIVERY_DATE , I.ITEM , M.ITEM_DESC, I.QTY_ORDERED_SUOM, I.COMMENTS from"
			+ " OMS_CUST_ORD_HEAD H , oms_cust_ord_item I , oms_cust_ord_address A, item_master M"
			+ " where M.ITEM = I.ITEM " + "and H.OMS_CUST_ORD_NO = I.OMS_CUST_ORD_NO " + "and H.OMS_CUST_ORD_NO = A.OMS_CUST_ORD_NO "
			+ "and H.ORD_PAYMENT_STATUS ='S' " + "and H.STATUS='S' " + "and H.CUST_ORDER_NO = ?";

	public EmailNotificationDAO(Connection sim14Conn, Connection omsConn) throws SQLException {
		this.sim14Conn = sim14Conn;
		this.getOrderNoPeSTMT = omsConn.prepareStatement(getOrderNo);
		this.getReminderOrderNoPeSTMT = omsConn.prepareStatement(getReminderOrderNo);
		this.getStoreManagerDetailsPeSTMT = omsConn.prepareStatement(getStoreManagerDetails);
		this.getSystemParameterPeSTMT = omsConn.prepareStatement(getFromEmail);
		this.updateEmailFlagPeSTMT = omsConn.prepareStatement(updateEmailFlag);
		this.getOrderStatusPeSTMT = sim14Conn.prepareStatement(getOrderStatus);
		this.updateOrderPickStatusPeSTMT = omsConn.prepareStatement(updateOrderPickStatus);
		// Added by me
		this.getOrderTypePeSTMT = omsConn.prepareStatement(orderType);
		this.updateEmailStatusFlagPreStmt = omsConn.prepareStatement(updateStatusFlag);
		this.getCutomerDataPeSTMT = omsConn.prepareStatement(getCutomerData);

	}

	public List<EmailInfo> getOrderEmailInfo() throws SQLException {
		LOGGER.info("***getOrderEmailInfo start executing ***");
		String fromEmail = null;
		String fromName = null;
		String subject = null;
		String body = null;
		List<EmailInfo> storManagerInfoList = new ArrayList<EmailInfo>();
		try {

			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.FROM_EMAIL));
			ResultSet fromEmailReST = getSystemParameterPeSTMT.executeQuery();
			LOGGER.info("***getOrderEmailInfo start executing ***1");
			
			if (fromEmailReST.next()) {
				fromEmail = fromEmailReST.getString("PARAMETER_VALUE");
				fromName = fromEmailReST.getString("PARAMETER_NAME");
				LOGGER.info("***getOrderEmailInfo start executing ***2");
			} else {
				throw new Exception("Email is not available in DB");
			}
			
			LOGGER.info("***getOrderEmailInfo start executing ***3");
			fromEmailReST.close();
			
			/*
			 * getOrderTypePeSTMT.setString(1, orderNumber);
			 * getSystemParameterPeSTMT.setString(1,
			 * PropertiesReader.getProperty(Constant.ORDER_EMAIL_TEMPLATE)); ResultSet
			 * orderTemplateReST = getSystemParameterPeSTMT.executeQuery();
			 * 
			 * if (orderTemplateReST.next()) {
			 * LOGGER.info("***getOrderEmailInfo start executing ***4"); body =
			 * orderTemplateReST.getString("PARAMETER_VALUE"); subject =
			 * orderTemplateReST.getString("PARAMETER_NAME"); } else { throw new
			 * Exception("Template is not available in DB"); }
			 * 
			 * LOGGER.info("***getOrderEmailInfo start executing ***5");
			 * orderTemplateReST.close();
			 */
			ResultSet orderNoReST = getOrderNoPeSTMT.executeQuery();
			
			while (orderNoReST.next()) {
				LOGGER.info("***getOrderEmailInfo start executing ***6");
				String orderNumber = orderNoReST.getString("CUST_ORDER_NO");
				System.out.println("inside order #:"+orderNumber);
				//Added to fix SFS and CFS appropriate email body
				getOrderTypePeSTMT.setString(1, orderNumber);
				ResultSet orderTypeReST = getOrderTypePeSTMT.executeQuery();
				if(orderTypeReST.next()) {
					System.out.println("Before Delv Type check::"+orderTypeReST.getString("DELIVERY_TYPE"));
					if(orderTypeReST.getString("DELIVERY_TYPE").equals("S")) {
						System.out.println("SFS:"+orderNumber);
						getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.ORDER_EMAIL_TEMPLATES));
					}						
					else {
						System.out.println("CFS:"+orderNumber);
						getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.ORDER_EMAIL_TEMPLATE));
					}
				}
				orderTypeReST.close();
				ResultSet orderTemplateReST = getSystemParameterPeSTMT.executeQuery();

				if (orderTemplateReST.next()) {
					body = orderTemplateReST.getString("PARAMETER_VALUE");
					subject = orderTemplateReST.getString("PARAMETER_NAME");
					LOGGER.info("***getOrderEmailInfo start executing ***4:"+body+":::"+subject);
				} else {
					throw new Exception("Template is not available in DB");
				}
				
				LOGGER.info("***getOrderEmailInfo start executing ***5");
				orderTemplateReST.close();
				
				
				
				
				
				EmailInfo emailInfo = new EmailInfo();
				Email email = new Email();
				email.setFrom(formatMessage(fromEmail, new String[] { orderNumber }));
				email.setFromName(fromName);
				email.setSubject(formatMessage(subject, new String[] { orderNumber }));
				email.setText(formatMessage(body, new String[] { orderNumber }));
				String text = email.getText();
				String table = getCustomerData(text,orderNumber);

				email.setText(text + table);

				getStoreManagerDetailsPeSTMT.setInt(1, orderNoReST.getInt("STORE_NO"));

				ResultSet rs = getStoreManagerDetailsPeSTMT.executeQuery();
				List<To> toEmailList = new ArrayList<To>();
				
				while (rs.next()) {
					To to = new To();
					to.setEmail(rs.getString("EMAILD"));
					System.out.println("EmailIDDD:"+to.getEmail());
					toEmailList.add(to);
					
				}
				Recipients recipients = new Recipients();
				recipients.setTo(toEmailList);
				email.setRecipients(recipients);
				emailInfo.setOrderNumber(orderNumber);
				emailInfo.setEmail(email);
				storManagerInfoList.add(emailInfo);
			}
			orderNoReST.close();

		} catch (SQLException e) {
			LOGGER.error("Error while fetching the StoreManager Details" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}
		return storManagerInfoList;
	}

	public List<EmailInfo> getOrderReminderEmailInfo() throws SQLException {
		String fromEmail = null;
		String fromName = null;
		String subject = null;
		String body = null;
		List<EmailInfo> storManagerInfoList = new ArrayList<EmailInfo>();
		try {
			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.FROM_EMAIL));
			ResultSet fromEmailReST = getSystemParameterPeSTMT.executeQuery();
			if (fromEmailReST.next()) {
				fromEmail = fromEmailReST.getString("PARAMETER_VALUE");
				fromName = fromEmailReST.getString("PARAMETER_NAME");
			} else {
				throw new Exception("Email is not available in DB");
			}
			fromEmailReST.close();
			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.ORDER_EMAIL_TEMPLATE));
			ResultSet orderTemplateReST = getSystemParameterPeSTMT.executeQuery();
			if (orderTemplateReST.next()) {
				body = orderTemplateReST.getString("PARAMETER_VALUE");
				subject = orderTemplateReST.getString("PARAMETER_NAME");
			} else {
				throw new Exception("Template is not available in DB");
			}
			orderTemplateReST.close();
			ResultSet reminderOrderNoReST = getReminderOrderNoPeSTMT.executeQuery();
			while (reminderOrderNoReST.next()) {
				String orderNumber = reminderOrderNoReST.getString("CUST_ORDER_NO");
				getOrderStatusPeSTMT.setString(1, orderNumber);
				ResultSet orderStatusReST = getOrderStatusPeSTMT.executeQuery();

				if (orderStatusReST.next()) {
					String status = orderStatusReST.getString("STATUS");
					if ("0".equals(status)) {
						EmailInfo emailInfo = new EmailInfo();
						Email email = new Email();
						email.setFrom(fromEmail);
						email.setFromName(fromName);
						email.setSubject(subject);
						email.setText(formatMessage(body, new String[] { orderNumber }));
						String text = email.getText();
						String table = getCustomerData(text,orderNumber);
						email.setText(text + table);
						getStoreManagerDetailsPeSTMT.setInt(1, reminderOrderNoReST.getInt("STORE_NO"));
						ResultSet rs = getStoreManagerDetailsPeSTMT.executeQuery();
						List<To> toEmailList = new ArrayList<To>();
						while (rs.next()) {
							To to = new To();
							to.setEmail(rs.getString("EMAILD"));
							toEmailList.add(to);

						}
						Recipients recipients = new Recipients();
						recipients.setTo(toEmailList);
						email.setRecipients(recipients);
						emailInfo.setOrderNumber(orderNumber);
						emailInfo.setEmail(email);
						storManagerInfoList.add(emailInfo);
					} else if ("1".equals(status)) {
						updateOrderPickStatusPeSTMT.setString(1, "1");
						updateOrderPickStatusPeSTMT.setString(2, orderNumber);
						updateOrderPickStatusPeSTMT.executeQuery();
					}
				}
			}

		} catch (SQLException e) {
			LOGGER.error("Error while fetching the StoreManager Details" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}
		return storManagerInfoList;
	}

	private String formatMessage(String message, String[] messageValues) {
		String valueToken;
		if (message.contains("%s")) {
			int c = 0;
			for (int i = 0; i < messageValues.length; i++) {
				valueToken = "%s" + (++c);
				message = message.replaceFirst(valueToken, messageValues[i]);

			}
		}

		return message;
	}

	public String getCustomerData(String msg,String orderNo) throws SQLException {
		String tabstart = "<br> <br> <table border='1' style='width:100%'>"
				+ " <tr> <th>Order #</th>  <th>Order Type</th>  <th>Customer Name</th> "
				+ " <th>Mobile #</th>  <th>Order Date</th>  <th>Customer promise date</th>"
				+ " <th>SKU</th> <th>Desc</th> <th>Order QTY</th> <th>Comment</th>" + "</tr>";
		String tabmid = "";
		String Tabend = "</table>";
		String mainTable = "";
		ResultSet rs = null;
		try {
			getCutomerDataPeSTMT.setString(1, orderNo);

			rs = getCutomerDataPeSTMT.executeQuery();
			while (rs.next()) {
				String CUST_ORDER_NO = rs.getString(1);
				String DELIVERY_TYPE = rs.getString(2);
				String BILL_FIRST_NAME = rs.getString(3);
				String DELIVER_PHONE_NO = rs.getString(4);
				String CREATE_DATETIME = rs.getString(5);
				String EXPECTED_DELIVERY_DATE = rs.getString(6);
				String ITEM = rs.getString(7);
				String ITEM_DESC = rs.getString(8);
				String QTY_ORDERED_SUOM = rs.getString(9);
				String COMMENTS = rs.getString(10);
				
				if (DELIVERY_TYPE.equals("S"))
					DELIVERY_TYPE = "SFS";
				else if (DELIVERY_TYPE.equals("C"))
					DELIVERY_TYPE = "CFS";

				tabmid = tabmid + "<tr> <td>" + CUST_ORDER_NO + "</td> <td>" + DELIVERY_TYPE + "</td>" + "<td>"
						+ BILL_FIRST_NAME + "</td>  <td>" + DELIVER_PHONE_NO + "</td> " + "<td>" + CREATE_DATETIME
						+ "</td>  <td>" + EXPECTED_DELIVERY_DATE + "</td>" + "<td>" + ITEM + "</td> <td>" + ITEM_DESC + "</td> <td>" 
						+ QTY_ORDERED_SUOM  + "</td> <td>" + COMMENTS + "</td> </tr>";
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}finally {
			if(rs!=null)
				rs.close();
		}

		mainTable = tabstart + tabmid + Tabend;
		return mainTable;

	}

	public void updateEmailFlag(String orderNumber, String status) {
		try {
			updateEmailFlagPeSTMT.setString(1, status);
			updateEmailFlagPeSTMT.setString(2, orderNumber);
			updateEmailFlagPeSTMT.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error while updating the ORDER_PICK_STATUS in OMS" + e);
			e.printStackTrace();
			LOGGER.error("Error while updating the ORDER_PICK_STATUS in OMS" + e);
		}

	}

	// Added by Bijay

	public void updateEmailStatus(String orderNumber) {

		try {
			updateEmailStatusFlagPreStmt.setString(1, orderNumber);
			updateEmailStatusFlagPreStmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error while updating the STATUS in OMS" + e);
			e.printStackTrace();

		}
	}

}
