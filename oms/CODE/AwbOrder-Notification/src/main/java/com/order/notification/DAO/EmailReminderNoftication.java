package com.order.notification.DAO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Request;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.db.util.AwbInfoUtil;
import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.Items;
import com.db.util.PropertiesReader;
import com.order.notification.mail.bean.Email;
import com.order.notification.mail.bean.EmailInfo;
import com.order.notification.mail.bean.Metadata;
import com.order.notification.mail.bean.Recipients;
import com.order.notification.mail.bean.To;

public class EmailReminderNoftication {
	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);

	Connection omsConn;
	PreparedStatement getHeaderInfo = null;
	PreparedStatement getSystemParameterPeSTMT = null;
	PreparedStatement updateEmailFlagPeSTMT = null;
	PreparedStatement updateEmailStatus = null;
	PreparedStatement getItemList = null;
	PreparedStatement getItemName = null;
	PreparedStatement getLanguageCodeCustOrderNo = null;
	CallableStatement getMultipleshipment = null;
	PreparedStatement getCourierName = null;
	PreparedStatement getcustomername = null;
	PreparedStatement getomsCurdNo = null;

	public EmailReminderNoftication(Connection omsConn) throws SQLException {
		this.getHeaderInfo = omsConn.prepareStatement(AwbInfoUtil.getReminderHeader_info_email);
		this.getSystemParameterPeSTMT = omsConn.prepareStatement(AwbInfoUtil.getFromEmail);
		this.updateEmailStatus = omsConn.prepareStatement(AwbInfoUtil.updateEmailFlag);
		this.getItemList = omsConn.prepareStatement(AwbInfoUtil.ItemList_email);
		this.getItemName = omsConn.prepareStatement(AwbInfoUtil.itemName_email);
		this.getLanguageCodeCustOrderNo = omsConn.prepareStatement(AwbInfoUtil.getcustOrderNoLanguageCode);
		this.getMultipleshipment = omsConn.prepareCall(AwbInfoUtil.getMultipleShipmentcheck);
		this.getCourierName = omsConn.prepareStatement(AwbInfoUtil.getCourierName);
		this.getcustomername = omsConn.prepareStatement(AwbInfoUtil.customer_name_email);
		this.getomsCurdNo = omsConn.prepareStatement(AwbInfoUtil.omsCurd_email);

	}

	public List<EmailInfo> getReminderEmailInfo() throws SQLException {

		LOGGER.info("*******************getReminderEmailInfo method is executing*************************************");

		String fromEmail = null;
		String fromName = null;

		List<EmailInfo> emailInfoList = new ArrayList<EmailInfo>();
		try {

			LOGGER.info("Preparing the email infolist");
			getSystemParameterPeSTMT.setString(1, PropertiesReader.getProperty(Constant.FROM_EMAIL));
			ResultSet fromEmailReST = getSystemParameterPeSTMT.executeQuery();
			if (fromEmailReST.next()) {
				fromEmail = fromEmailReST.getString("PARAMETER_VALUE");

				fromName = fromEmailReST.getString("PARAMETER_NAME");
			} else {
				throw new Exception("Email is not available in DB");
			}
			fromEmailReST.close();

			ResultSet resultSetHeader = getHeaderInfo.executeQuery();

			while (resultSetHeader.next()) {
				LOGGER.info("******Iterating the Header info Table is starting********");

				String orderNumber = resultSetHeader.getString("CUST_ORDER_NO");
				String trackingID = resultSetHeader.getString("TRACKING_ID");
				Integer Awb_id = resultSetHeader.getInt("AWB_ID");
				LOGGER.info("AWB_id is****" + Awb_id);
				String CourierName = resultSetHeader.getString("COURIER_NAME");

				String oms_curd_no = getOmsCurdNo(orderNumber);
				String customerName = getCustomerName(oms_curd_no);
				
				EmailInfo emailInfo = new EmailInfo();
				emailInfo.setOrderNumber(Awb_id);
				Email email = new Email();
				Metadata metadata = new Metadata();
				email.setFrom(fromEmail);
				email.setFromName(fromName);

				email.setSubject("Extra");

				// This for checking either english or Arabic
				Integer languagecode = getcustomerOrderLanguageCode(orderNumber);
				System.out.println("languagecode" + languagecode);
				String checkMultipleShipmentIndicator = getMultipleShipmentCheck(orderNumber);
				System.out.println("checkMultipleShipmentIndicator" + checkMultipleShipmentIndicator);

				/** Template Attachement **/

				File htmlTemplateFile = null;

				if (languagecode == 1 && checkMultipleShipmentIndicator.equals("N")) {
					LOGGER.info("Multiple shipment for the EnglishTemplateSplitted");
					htmlTemplateFile = new File("src/main/resources/EmailTemplateSplitted_en.html");

				} else if (languagecode == 1) {
					LOGGER.info("single shipment for the EnglishTemplate");
					htmlTemplateFile = new File("src/main/resources/EmailTemplate_en.html");
				} else if (languagecode == 13 && checkMultipleShipmentIndicator.equals("Y")) {
					LOGGER.info("Multiple shipment for the ArabicTemplate");
					htmlTemplateFile = new File("src/main/resources/EmailTemplateSplitted_ar.html");

				} else if (languagecode == 13) {
					LOGGER.info("Single shipment for the ArabicTemplate");
					htmlTemplateFile = new File("src/main/resources/EmailArabic.html");
				}

				// gettting the url
				String courier_url = getCourierUrl(CourierName, trackingID);

				String htmlString = null;
				try {
					htmlString = FileUtils.readFileToString(htmlTemplateFile);
				} catch (IOException e) {
					LOGGER.error("Exception in the template" + e);
				}

				Integer totalPrice = 0;
				Integer totalItems = 0;

				htmlString = htmlString.replace("$OrderId", orderNumber);
				htmlString = htmlString.replace("$CourierName", CourierName);
				htmlString = htmlString.replace("$AWBNo", trackingID);
				htmlString = htmlString.replace("$URL", courier_url);
				htmlString = htmlString.replace("$Customername", customerName);
				StringBuilder buf = new StringBuilder();
				List<Items> items = null;
				if (languagecode == 13) {

					items = getItemsArabic(Awb_id);
				} else {

					items = getEnglishItems(Awb_id);
				}
				/** langauage code for checking itemlist **/

				if (languagecode == 1) {
					LOGGER.info("Iterating the English items list");
					for (Items item : items) {
						totalPrice = totalPrice + Integer.parseInt(item.getPrice());
						totalItems = totalItems + Integer.parseInt(item.getQty());
						buf.append(
								"<tr>" + "<td style=\"text-align: left; font-size: 14px; padding: 25px 0 15px 0; width: 75%\">"
										+ item.getItemName() + "<p style=\"color:#9B9B9B;margin:3px 0 0 0\">"
										+ item.getQty() + "</p>" + " </td>"
										+ "<td style=\"text-align: right; font-size: 14px;font-weight: bold;vertical-align: top; padding: 25px 0 15px 0; width: 25%;\">"
										+ item.getPrice() + "SR" + "</td>"

										+ "</tr>" + "<br/>");

					}
				} else {
					LOGGER.info("Iterating the Arabic items list");
					for (Items item : items) {
						totalPrice = totalPrice + Integer.parseInt(item.getPrice());
						totalItems = totalItems + Integer.parseInt(item.getQty());

						buf.append(
								"<tr>" + "<td style=\"text-align: right; font-size: 14px; padding: 25px 0 15px 0; width: 75%\">"
										+ item.getItemName() + "<p style=\"color:#9B9B9B;margin:3px 0 0 0;\">" + "عدد"
										+ "(" + item.getQty() + " منتج)" + "</p>" + "</td>"
										+ "<td style=\"text-align: left; font-size: 14px;font-weight: bold;vertical-align: top; padding: 25px 0 15px 0; width: 25%;\">"
										+ item.getPrice() + " رس" + "</td>" + "</tr>");

					}

				}

				LOGGER.info("Replacing the data to the HTML Template");
				/** Replacement of the Html Attribute **/

				String Price = Integer.toString(totalPrice);
				String quanty = Integer.toString(totalItems);

				htmlString = htmlString.replace("$buf", buf);

				htmlString = htmlString.replace("$qnty", quanty);
				htmlString = htmlString.replace("$price", Price);
				email.setText(htmlString);
				metadata.setCampaignType("TRANSACTIONAL");
				email.setMetadata(metadata);
				List<To> toEmailList = new ArrayList<To>();
				To to = new To();
				to.setEmail(resultSetHeader.getString("EMAIL_ID"));
				toEmailList.add(to);
				Recipients recipients = new Recipients();
				recipients.setTo(toEmailList);
				email.setRecipients(recipients);
				// emailInfo.setOrderNumber(orderNumber);
				emailInfo.setEmail(email);
				emailInfoList.add(emailInfo);

				LOGGER.info("******Iterating the Header info Table is Completed********");

			}
			resultSetHeader.close();

		} catch (SQLException e) {
			LOGGER.error("Error while fetching the Header Details" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + e);
		}
		LOGGER.info("*******************getReminderEmailInfo method completed **************************");
		return emailInfoList;

	}

	/// updating the email flags
	public void updateEmailFlag(Integer orderNumber, String status) {
		LOGGER.info("Update the eamil Status is executing");
		try {
			updateEmailStatus.setString(1, status);
			updateEmailStatus.setInt(2, orderNumber);
			updateEmailStatus.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error while updating the email status in OMS" + e);
			e.printStackTrace();
			LOGGER.error("Error while updating the email status in OMS" + e);
		}
		LOGGER.info("Update the eamil Status is completed");

	}

	// getting items for the english
	private List<Items> getEnglishItems(Integer awb_id) {
		LOGGER.info("Preparing the English items list is excuting");
		List<Items> items = new ArrayList<Items>();
		try {
			getItemList.setInt(1, awb_id);
			ResultSet itemresultset = getItemList.executeQuery();
			while (itemresultset.next()) {
				Items item = new Items();
			

				getItemName.setString(1, itemresultset.getString("ITEM_ID"));
				ResultSet resultSet = getItemName.executeQuery();

				String itemName = null;
				while (resultSet.next()) {

					itemName = resultSet.getString(25);
					

				}
				item.setItemName(itemName);
				item.setPrice(itemresultset.getString("UNIT_RETAIL"));
				item.setQty(itemresultset.getString("QTY"));
				items.add(item);

			}
		} catch (SQLException e) {

			LOGGER.info("Exception getEnglishItems" + e);
		}
		LOGGER.info("Preparing the English item list is completed");
		return items;

	}

	// getting items for the arabic
	private List<Items> getItemsArabic(Integer awb_id) {
		LOGGER.info("Preparing the Arabic items list is executing");
		List<Items> items = new ArrayList<Items>();
		try {
			getItemList.setInt(1, awb_id);
			ResultSet itemresultset = getItemList.executeQuery();
			while (itemresultset.next()) {
				Items item = new Items();

				getItemName.setString(1, itemresultset.getString("ITEM_ID"));

				ResultSet resultSet = getItemName.executeQuery();

				String itemName = null;
				while (resultSet.next()) {
					LOGGER.info("Get the ItemName");
					itemName = resultSet.getString(24);

				}

				byte[] bytearray = itemName.getBytes();
				try {
					LOGGER.info("Arabic Conversion");
					itemName = new String(bytearray, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("arabicBodyMsg is null" + e);

				}
                item.setItemName(itemName);
				item.setPrice(itemresultset.getString("UNIT_RETAIL"));
				item.setQty(itemresultset.getString("QTY"));
				items.add(item);

			}
		} catch (SQLException e) {
			LOGGER.info("Exception getItemsArabic" + e);
		}
		LOGGER.info("Preparing the Arabic item list is completed");
		return items;

	}

	// getting the language code
	private Integer getcustomerOrderLanguageCode(String custorderNo) {
		LOGGER.info("getcustomerOrderLanguageCode method is executing");
		ResultSet orderLanguageCodeResultSet = null;
		Integer laguagecode = 1;
		try {
			getLanguageCodeCustOrderNo.setString(1, custorderNo);
			orderLanguageCodeResultSet = getLanguageCodeCustOrderNo.executeQuery();
			while (orderLanguageCodeResultSet.next()) {
				laguagecode = orderLanguageCodeResultSet.getInt("CUSTOMER_LANG");

			}
		} catch (SQLException e) {
			LOGGER.error("Error while getcustomerOrderlanguage code" + e);

		}
		LOGGER.info("getcustomerOrderLanguageCode method is completed");
		return laguagecode;
	}

	private String getMultipleShipmentCheck(String orderNumber) {
		LOGGER.info("getMultipleShipmentCheck method is executing");
		String indicator = null;
		try {
			this.getMultipleshipment.setString(2, orderNumber);
			this.getMultipleshipment.registerOutParameter(1, Types.VARCHAR);
			this.getMultipleshipment.execute();
			indicator = this.getMultipleshipment.getString(1);
			LOGGER.info(" end of getMultipleShipmentCheck method");
		} catch (SQLException e) {
			LOGGER.error("Exception in the multipleshipment ");
		}
		LOGGER.info("getMultipleShipmentCheck method is completed");
		return indicator;
	}

	private String getCourierUrl(String courieName, String trackingNumber) {
		LOGGER.info("get CourierUrl method is executing");
		System.out.println(courieName+trackingNumber);
		String CourierUrl = null;

		try {
			getCourierName.setString(1, courieName.toUpperCase());
			getCourierName.setString(2, courieName.toUpperCase());
			ResultSet result = getCourierName.executeQuery();
			while (result.next()) {
				LOGGER.info("getting the courier url from table");
				CourierUrl = result.getString("COURIER_URL");

				if (CourierUrl.contains("$$")) {
					LOGGER.info("Courierurl with $$ and @@");
					int indexOf = CourierUrl.indexOf("@@");
					CourierUrl = CourierUrl.substring(0, indexOf - 1) + trackingNumber
							+ CourierUrl.substring(indexOf + 3);

					int IndexOfAnd = CourierUrl.indexOf("$$");
					CourierUrl = CourierUrl.substring(0, IndexOfAnd) + "&" + CourierUrl.substring(IndexOfAnd + 2);

				} else {
					LOGGER.info("Courierurl with only @@");
					int indexOf = CourierUrl.indexOf("@@");
					CourierUrl = CourierUrl.substring(0, indexOf - 1) + trackingNumber
							+ CourierUrl.substring(indexOf + 3);

				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Exception in the Courier URl" + e);
		}
		LOGGER.info("get CourierUrl method is completed"+CourierUrl);
		return CourierUrl;

	}

	private String getOmsCurdNo(String omscurd) {

		LOGGER.info("Oms customer order number method is excuting");
		String omscurdno = null;
		try {
			getomsCurdNo.setString(1, omscurd);
			ResultSet result = getomsCurdNo.executeQuery();
			while (result.next()) {
				omscurdno = result.getString("OMS_CUST_ORD_NO");
			}
		} catch (SQLException e) {
			LOGGER.error("Exception in the omsCurd No" + e);
		}
		LOGGER.info("Oms customer order number is completed");
		return omscurdno;

	}

	private String getCustomerName(String omsCurdNo) {
		String customername = null;
		LOGGER.info("get customerName method is executing");

		try {
			getcustomername.setString(1, omsCurdNo);
			ResultSet resultset = getcustomername.executeQuery();

			while (resultset.next()) {
				customername = resultset.getString("DELIVER_FIRST_NAME");
				customername = customername + "" + resultset.getString("DELIVER_LAST_NAME");
				
				boolean isarabic=isArabic(customername);
				if(isarabic==true){
					byte[] bytearray=customername.getBytes();
					try {
						LOGGER.info("Arabic name transalation");
						customername = new String(bytearray, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						LOGGER.error("Exception in the ArabicName" + e);

					}
				}
				
				
				
			}
			if (customername == null) {
				LOGGER.info("Customer Name is Null");
				customername="";
			}

		} catch (SQLException e) {
			LOGGER.error("EXception in the Customer Name" + e);
		}
		LOGGER.info("get customerName method is completed");
		return customername;
	}
	
	
	public boolean isArabic(String text){
	    String textWithoutSpace = text.trim().replaceAll(" ",""); //to ignore whitepace
	    for (int i = 0; i < textWithoutSpace.length();) {
	        int c = textWithoutSpace.codePointAt(i);//get the unicode for the each characater
	        
	      //range of arabic chars/symbols is from 0x0600 to 0x06ff
	        //the arabic letter 'لا' is special case having the range from 0xFE70 to 0xFEFF
	        if (c >= 0x0600 && c <=0x06FF || (c >= 0xFE70 && c<=0xFEFF)) 
	            i += Character.charCount(c);   
	        else                
	            return false;

	    } 
	    return true;
	  }
	

}
