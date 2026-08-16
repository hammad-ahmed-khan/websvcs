package com.order.notification.DAO;

import java.io.IOException;
import java.io.InputStream;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.db.util.AwbEmailUtil;
import com.db.util.AwbInfoUtil;
import com.db.util.AwbSmsUtil;
import com.db.util.Constant;
import com.db.util.GetDBConnection;
import com.db.util.Items;
import com.db.util.PropertiesReader;
import com.order.notification.mail.bean.Email;
import com.order.notification.mail.bean.EmailInfo;
import com.order.notification.mail.bean.Metadata;
import com.order.notification.mail.bean.Recipients;
import com.order.notification.mail.bean.To;

public class EmailNotificationDAO {
	private static Logger LOGGER = Logger.getLogger(GetDBConnection.class);

	Connection omsConn;
	PreparedStatement getHeaderInfo = null;
	PreparedStatement getHeaderReminderInfo = null;
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
	AwbEmailUtil awbemailutil = null;
	AwbSmsUtil awbsmsutil = null;

	public EmailNotificationDAO(Connection omsConn) throws SQLException {
		this.getHeaderInfo = omsConn.prepareStatement(AwbInfoUtil.hearder_info_email);
		this.getSystemParameterPeSTMT = omsConn.prepareStatement(AwbInfoUtil.getFromEmail);
		this.updateEmailStatus = omsConn.prepareStatement(AwbInfoUtil.updateEmailFlag);
		this.getItemList = omsConn.prepareStatement(AwbInfoUtil.ItemList_email);
		this.getItemName = omsConn.prepareStatement(AwbInfoUtil.itemName_email);
		this.getLanguageCodeCustOrderNo = omsConn.prepareStatement(AwbInfoUtil.getcustOrderNoLanguageCode);
		this.getMultipleshipment = omsConn.prepareCall(AwbInfoUtil.getMultipleShipmentcheck);
		this.getCourierName = omsConn.prepareStatement(AwbInfoUtil.getCourierName);
		this.getcustomername = omsConn.prepareStatement(AwbInfoUtil.customer_name_email);
		this.getomsCurdNo = omsConn.prepareStatement(AwbInfoUtil.omsCurd_email);
		this.getHeaderReminderInfo = omsConn.prepareStatement(AwbInfoUtil.getReminderHeader_info_email);
		this.awbemailutil = new AwbEmailUtil();
		this.awbsmsutil = new AwbSmsUtil();
		
	}

	public List<EmailInfo> getOrderEmailInfo() throws SQLException {
		
		LOGGER.info("*******************getOrderEmailInfo method is executing*************************************");

		String fromEmail = null;
		String fromName = null;
		String htmlString = null;
		List<Items> items = null;
		
		InputStream in = null;

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
				LOGGER.info("AWB_id Number is" + Awb_id);
				String CourierName = resultSetHeader.getString("COURIER_NAME");

                String oms_curd_no = awbemailutil.getOmsCurdNo(getomsCurdNo, orderNumber);
				String customerName = awbemailutil.getCustomerName(getcustomername, oms_curd_no);

				 Integer languagecode = awbsmsutil.getcustomerOrderLanguageCode(getLanguageCodeCustOrderNo,
						orderNumber);
				LOGGER.info("langauage code is" + languagecode);

				String checkMultipleShipmentIndicator = awbsmsutil.getMultipleShipmentCheck(getMultipleshipment,
						orderNumber);
				LOGGER.info("Multiple Shipment is" + checkMultipleShipmentIndicator);
				

				String courier_url = awbsmsutil.getCourierUrl(getCourierName, CourierName, trackingID);

				
				/** Template Attachement **/
				if (languagecode.equals(AwbEmailUtil.englanguagecode) && checkMultipleShipmentIndicator.equals(AwbEmailUtil.engMultipleShiplment)) {
					LOGGER.info("Multiple shipment for the EnglishTemplateSplitted");
					//Charan
					in = this.getClass().getClassLoader().getResourceAsStream("resources/EmailTemplateSplitted_en.html");
					//in = this.getClass().getClassLoader().getResourceAsStream("EmailTemplateSplitted_en.html");

				} else if (languagecode.equals(AwbEmailUtil.englanguagecode)) {
					LOGGER.info("single shipment for the EnglishTemplate");
					in = this.getClass().getClassLoader().getResourceAsStream("resources/EmailTemplate_en.html");
					//in = this.getClass().getClassLoader().getResourceAsStream("EmailTemplate_en.html");

				} else if (languagecode.equals(AwbEmailUtil.arblanguagecode) && checkMultipleShipmentIndicator.equals(AwbEmailUtil.arbMultipleShiplment)) {
					LOGGER.info("Multiple shipment for the ArabicTemplate");
					in = this.getClass().getClassLoader().getResourceAsStream("resources/EmailTemplateSplitted_ar.html");
					//in = this.getClass().getClassLoader().getResourceAsStream("EmailTemplateSplitted_ar.html");

				} else if (languagecode.equals(AwbEmailUtil.arblanguagecode)) {
					LOGGER.info("Single shipment for the ArabicTemplate");
					in = this.getClass().getClassLoader().getResourceAsStream("resources/EmailTemplate_ar.html");
					//in = this.getClass().getClassLoader().getResourceAsStream("EmailTemplate_ar.html");
				}
				try {
					htmlString = IOUtils.toString(in, "UTF-8");
					LOGGER.info("Template selected");
				} catch (IOException e) {
					LOGGER.error("Exception in the template" + e);
				}
				
				
				StringBuilder builder = new StringBuilder();
				//Charan
				//Integer totalPrice = 0;
				Double totalPrice =0.0;
				Integer totalItems = 0;

				if (languagecode.equals(AwbEmailUtil.arblanguagecode)) {

					items = awbemailutil.getItemsArabic(getItemList, getItemName, Awb_id);
				} else {

					items = awbemailutil.getEnglishItems(getItemList, getItemName, Awb_id);
				}
				/** langauage code for checking itemlist **/

				if (languagecode.equals(AwbEmailUtil.englanguagecode)) {
					LOGGER.info("Iterating the English items list");
					for (Items item : items) {
						LOGGER.info("Iterating the English item list"+item.getPrice());
						LOGGER.info("Iterating the English item list"+Double.valueOf(item.getPrice()));
						totalPrice = totalPrice + Double.valueOf(item.getPrice());
						//Charan
						//totalPrice = totalPrice + Integer.parseInt(item.getPrice());
						LOGGER.info("Iterating the English totalPrice list"+totalPrice);
						totalItems = totalItems + Integer.parseInt(item.getQty());
						builder.append(
								"<tr>" + "<td style=\"text-align: left; font-size: 14px; padding: 25px 0 15px 0; width: 75%\">"
										+ item.getItemName() + "<p style=\"color:#9B9B9B;margin:3px 0 0 0\">"
										+ item.getQty() +"Item(s)"+"</p>" + " </td>"
										+ "<td style=\"text-align: right; font-size: 14px;font-weight: bold;vertical-align: top; padding: 25px 0 15px 0; width: 25%;\">"
										+ item.getPrice() + "SR" + "</td>"

										+ "</tr>" + "<br/>");

					}
					  


				} else {
					LOGGER.info("Iterating the Arabic items list");
					for (Items item : items) {
						totalPrice = totalPrice + Integer.parseInt(item.getPrice());
						totalItems = totalItems + Integer.parseInt(item.getQty());

						builder.append(
								"<tr>" + "<td style=\"text-align: right; font-size: 14px; padding: 25px 0 15px 0; width: 75%\">"
										+ item.getItemName() + "<p style=\"color:#9B9B9B;margin:3px 0 0 0;\">" + "عدد"
										+ "(" + item.getQty() + " منتج)" + "</p>" + "</td>"
										+ "<td style=\"text-align: left; font-size: 14px;font-weight: bold;vertical-align: top; padding: 25px 0 15px 0; width: 25%;\">"
										+ item.getPrice() + " رس" + "</td>" + "</tr>");

					}

				}
				//Charan
				//String Price = Integer.toString(totalPrice);
				String Price = Double.toString(totalPrice);
				String quanty = Integer.toString(totalItems);
			

				LOGGER.info("Replacing the data to the HTML Template");
				/** Replacement of the Html Attribute **/

				htmlString = htmlString.replace("$OrderId", orderNumber);
				htmlString = htmlString.replace("$CourierName", CourierName);
				htmlString = htmlString.replace("$AWBNo", trackingID);
				htmlString = htmlString.replace("$URL", courier_url);
				htmlString = htmlString.replace("$Customername", customerName);
				htmlString = htmlString.replace("$buf", builder);

				htmlString = htmlString.replace("$qnty", quanty);
				htmlString = htmlString.replace("$price", Price);
				
				EmailInfo emailInfo = new EmailInfo();
				emailInfo.setOrderNumber(Awb_id);
				Email email = new Email();
				Metadata metadata = new Metadata();
				email.setFrom(fromEmail);
				email.setFromName(fromName);
				if(languagecode.equals(AwbEmailUtil.englanguagecode)){
				email.setSubject("Your Order Items for #"+orderNumber+" has Been Shipped");
				}
				else{
					email.setSubject("شحن الطلب رقم "+orderNumber);
				}

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
		LOGGER.info("*******************getOrderEmailInfo method completed **************************");
		return emailInfoList;

	}

	
	
	
	
	public List<EmailInfo> getReminderEmailInfo() throws SQLException {
		// This Logger is identityfication of the normalEmail
		LOGGER.info("*******************getReminderEmailInfo method is executing*************************************");

		String fromEmail = null;
		String fromName = null;
		String htmlString = null;
		List<Items> items = null;
		
		InputStream template=null;

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

			ResultSet resultSetHeader = getHeaderReminderInfo.executeQuery();

			while (resultSetHeader.next()) {
				LOGGER.info("******Iterating the Reminder Header info Table is starting********");

				String orderNumber = resultSetHeader.getString("CUST_ORDER_NO");
				String trackingID = resultSetHeader.getString("TRACKING_ID");
				Integer Awb_id = resultSetHeader.getInt("AWB_ID");
				LOGGER.info("AWB_id is****" + Awb_id);
				String CourierName = resultSetHeader.getString("COURIER_NAME");

				String oms_curd_no = awbemailutil.getOmsCurdNo(getomsCurdNo, orderNumber);

				String customerName = awbemailutil.getCustomerName(getcustomername, oms_curd_no);

				
				// This for checking either english or Arabic

				Integer languagecode = awbsmsutil.getcustomerOrderLanguageCode(getLanguageCodeCustOrderNo,
						orderNumber);
				LOGGER.info("langauage code" + languagecode);

				String checkMultipleShipmentIndicator = awbsmsutil.getMultipleShipmentCheck(getMultipleshipment,
						orderNumber);
				LOGGER.info("checkMultipleShipmentIndicator" + checkMultipleShipmentIndicator);
				// gettting the url

				String courier_url = awbsmsutil.getCourierUrl(getCourierName, CourierName, trackingID);

				/** Template Attachement **/
				if (languagecode.equals(AwbEmailUtil.englanguagecode) && checkMultipleShipmentIndicator.equals(AwbEmailUtil.engMultipleShiplment)) {
					LOGGER.info("Multiple shipment for the Reminder EnglishTemplateSplitted");
					template = this.getClass().getClassLoader()
							.getResourceAsStream("resources/EmailTemplateSplitted_en.html");

				} else if (languagecode.equals(AwbEmailUtil.englanguagecode)) {
					LOGGER.info("single shipment for the Reminder EnglishTemplate");
					template = this.getClass().getClassLoader().getResourceAsStream("resources/EmailTemplate_en.html");

				} else if (languagecode.equals(AwbEmailUtil.arblanguagecode) && checkMultipleShipmentIndicator.equals(AwbEmailUtil.arbMultipleShiplment)) {
					LOGGER.info("Multiple shipment for the Reminder ArabicTemplate");
					template = this.getClass().getClassLoader().getResourceAsStream("resources/EmailTemplateSplitted_ar.html");

				} else if (languagecode.equals(AwbEmailUtil.arblanguagecode)) {
					LOGGER.info("Single shipment for the Reminder ArabicTemplate");
					template = this.getClass().getClassLoader().getResourceAsStream("resources/EmailTemplate_ar.html");
				}
				
				try {
					htmlString = IOUtils.toString(template, "UTF-8");//Conversion of the selected template to string
					LOGGER.info("Conversion of the template to string");
				} catch (IOException e) {
					LOGGER.error("Exception in the template" + e);
				}
				
				Integer totalReminderPrice = 0;
				Integer totalReminderItems = 0;
				StringBuilder builder = new StringBuilder();

				if (languagecode.equals(AwbEmailUtil.arblanguagecode)) {
                     items = awbemailutil.getItemsArabic(getItemList, getItemName, Awb_id);
				} else {

					items = awbemailutil.getEnglishItems(getItemList, getItemName, Awb_id);
				}
			
				
				/** langauage code for checking itemlist **/
				if (languagecode.equals(AwbEmailUtil.englanguagecode)) {
					LOGGER.info("Iterating the English items list");
					for (Items item : items) {
						totalReminderPrice = totalReminderPrice + Integer.parseInt(item.getPrice());
						totalReminderItems = totalReminderItems + Integer.parseInt(item.getQty());
						builder.append(
								"<tr>" + "<td style=\"text-align: left; font-size: 14px; padding: 25px 0 15px 0; width: 75%\">"
										+ item.getItemName() + "<p style=\"color:#9B9B9B;margin:3px 0 0 0\">"
										+ item.getQty()+"Item(s)"+"</p>" + " </td>"
										+ "<td style=\"text-align: right; font-size: 14px;font-weight: bold;vertical-align: top; padding: 25px 0 15px 0; width: 25%;\">"
										+ item.getPrice() + "SR" + "</td>"

										+ "</tr>" + "<br/>");

					}
				} else {
					LOGGER.info("Iterating the Arabic items list");
					for (Items item : items) {
						totalReminderPrice = totalReminderPrice + Integer.parseInt(item.getPrice());
						totalReminderItems = totalReminderItems + Integer.parseInt(item.getQty());
                    builder.append(
								"<tr>" + "<td style=\"text-align: right; font-size: 14px; padding: 25px 0 15px 0; width: 75%\">"
										+ item.getItemName() + "<p style=\"color:#9B9B9B;margin:3px 0 0 0;\">" + "عدد"
										+ "(" + item.getQty() + " منتج)" + "</p>" + "</td>"
										+ "<td style=\"text-align: left; font-size: 14px;font-weight: bold;vertical-align: top; padding: 25px 0 15px 0; width: 25%;\">"
										+ item.getPrice() + " رس" + "</td>" + "</tr>");

					}

				}

				String TotatlReminderPrice = Integer.toString(totalReminderPrice);
				String TotalReminderquanty = Integer.toString(totalReminderItems);
				
				

				LOGGER.info("Replacing the data to the HTML Template");
				/** Replacement of the Html Attribute **/

				htmlString = htmlString.replace("$OrderId", orderNumber);
				htmlString = htmlString.replace("$CourierName", CourierName);
				htmlString = htmlString.replace("$AWBNo", trackingID);
				htmlString = htmlString.replace("$URL", courier_url);
				htmlString = htmlString.replace("$Customername", customerName);
				htmlString = htmlString.replace("$buf", builder);
                htmlString = htmlString.replace("$qnty", TotalReminderquanty);
				htmlString = htmlString.replace("$price", TotatlReminderPrice);
				
				
				
				EmailInfo emailInfo = new EmailInfo();
				emailInfo.setOrderNumber(Awb_id);
				Email email = new Email();
				Metadata metadata = new Metadata();
				email.setFrom(fromEmail);
				email.setFromName(fromName);

				if(languagecode.equals(AwbEmailUtil.englanguagecode)){
					email.setSubject("Your Order Items for #"+orderNumber+"Has Been Shipped");
					}
					else{
						email.setSubject("شحن الطلب رقم "+orderNumber);
					}

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
				emailInfo.setEmail(email);
				emailInfoList.add(emailInfo);
                LOGGER.info("******Iterating the Reminder Header info Table is Completed********");

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
		LOGGER.info("Update the email Status is executing");
		try {
			updateEmailStatus.setString(1, status);
			updateEmailStatus.setInt(2, orderNumber);
			updateEmailStatus.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Error while updating the email status in OMS" + e);
			e.printStackTrace();
			LOGGER.error("Error while updating the email status in OMS" + e);
		}
		LOGGER.info("Update the email Status is completed");

	}

}

/*
 * byte[] bytearray = fullName.getBytes(); try {
 * LOGGER.info("Arabic name translation"); fullName = new String(bytearray,
 * "UTF-8");
 * 
 * System.out.println("Arabic Name"+fullName); } catch
 * (UnsupportedEncodingException e) { LOGGER.error("Exception in the ArabicName"
 * + e);
 * 
 * }
 */
