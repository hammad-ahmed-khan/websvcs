package com.logicinfo.transfer.receive.dao;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.logicinfo.transfer.receive.model.Items;
import com.logicinfo.transfer.receive.model.Response;
import com.logicinfo.transfer.receive.model.TsfReceiveRequest;
import com.logicinfo.transfer.receive.model.TsfReceiveResponse;

@Repository
public class TransferReceiveDaoImpl implements TransferReceiveDao {

	private static final Logger log = Logger.getLogger(TransferReceiveDaoImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private ConcurrentHashMap<String, String> requestMap = new ConcurrentHashMap<String, String>();

	public Response tsfReceive(TsfReceiveRequest wr) throws Exception {
		Response res = new Response();
		try {
			String itm = "";
			String recieptNo = wr.getReceiptNumber();
			String ponum = wr.getPoNumber();
			int destId = wr.getDestId();
			log.info("Items list size " + wr.getItems().size());
			String bol = getBol(ponum, wr.getItems());
			log.info("BOL Number:::" + bol);
			if (bol.equalsIgnoreCase("INVALID PO NUMBER")) {
				res.setRes("BOL NUMBER NOT ASSIGNED YET FOR THIS PO NO" + "" + "" + ponum);
				return res;
			}
			log.info("Inside Else");
			itm = null;
			itm = getItem(wr.getItems(), ponum);
			if (itm != null) {
				log.info("INVALID ITEM NUMBER1");
				res.setRes(itm);
				return res;
			}
			if (itm == null) {
				log.info("INSIDE ITEM NOT NULL " + wr.getItems().size());
				Response data = callwebserv(bol, ponum, recieptNo, destId, wr.getItems());
				res.setDoc(data.getDoc());
				res.setRes(data.getRes());
				res.setItem(data.getItem());
				return res;
			}
		} catch (Exception e) {
			log.info("Exception in tsfReceive " + e.getMessage());
		}
		return res;
	}

	private Response callwebserv(String bol, String ponum, String recieptNo, int destId, List<Items> item) {
		Connection connt = null;
		Statement stmtWs = null;
		ResultSet rsWs = null;
		String responseString = "";
		String outputString = "";
		com.logicinfo.transfer.receive.model.Response res = new Response();
		if (requestMap.put(recieptNo, recieptNo) != null) {
			String message = "Receipt number '" + recieptNo + "' is already is in progress. Please try after some time.";
			log.warn(message);
			res.setRes(message);
			return res;
		}
		log.info("Obtained map lock for po " + recieptNo);
		try {
			log.info("INSIDE WEBSERVICE CALL-BEFORE");
			// final String wsURL =
			// "http://192.168.41.193:7615/ReceivingPublishingBean/ReceivingPublishingService?WSDL";
			String wsURL = readPropertiesFile();
			log.info("wsURL " + wsURL);

			final Date receiptDate = new Date();
			final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			final Calendar cal = Calendar.getInstance();
			final String dateTime = formatter.format(cal.getTime());
			dateTime.concat("+00:00");
			log.info("Calendar:" + receiptDate + ":" + dateTime);
			String ItemId = null;
			String input = "";
			String carton = "";
			int ordQty = 0;
			int rcvQty = 0;
			int expQty = 0;
			int remQty = 0;
			int avlQty = 0;
			connt = jdbcTemplate.getDataSource().getConnection();
			stmtWs = connt.createStatement();
			log.info("Created the connection");
			for (int itmLst = 0; itmLst < item.size(); ++itmLst) {
				ItemId = item.get(itmLst).getItemId();
				ordQty = item.get(itmLst).getOrderQty();
				log.info("Inside FOR - itmLst:" + itmLst + "ITEMID:::" + ItemId + ":" + ordQty);

				int remaining_qty = 0;

				String tsfQuery = "select tsf_no, (NVL(ship_qty,0) - NVL(RECEIVED_QTY,0)) remaining_qty from tsfdetail where tsf_no ='" + ponum + "' and ITEM = '" + ItemId + "'";

				log.info("tsfQuery " + tsfQuery);

				rsWs = stmtWs.executeQuery(tsfQuery);

				if (rsWs.next()) {
					remaining_qty = rsWs.getInt("remaining_qty");
					log.info("remaining_qty " + remaining_qty);
				}

				String cartonQuery = "select CARTON, NVL(QTY_RECEIVED,0) QTY_RECEIVED, NVL(QTY_EXPECTED,0) QTY_EXPECTED  from shipsku where distro_no = '" + ponum + "' and ITEM = '" + ItemId + "'";
				try {
					rsWs.close();
				} catch (Exception e) {
					// Ignore Exception
				}
				rsWs = stmtWs.executeQuery(cartonQuery);
				if (!rsWs.isBeforeFirst()) {
					log.info("Inside RsWs error");
					res.setRes("ERROR WHILE GETTING SHIPPMENT DETAILS");
					return res;
				}
				avlQty = ordQty; // 7 orderQty
				while (rsWs.next()) {
					log.info("Inside RsWS while avlQty:" + avlQty);
					if (avlQty > 0) {
						carton = rsWs.getString("CARTON");
						rcvQty = rsWs.getInt("QTY_RECEIVED"); // 5 already received
						expQty = rsWs.getInt("QTY_EXPECTED"); // 10
						remQty = expQty - rcvQty;
						if (remaining_qty <= 0) {
							Items eItem = new Items();
							eItem.setItemId(ItemId);
							eItem.setOrderQty(remaining_qty);
							res.setRes("expected_qty_received_already");
							res.setItem(eItem);
							return res;
						}

						else if (ordQty > remaining_qty) {
							avlQty -= remaining_qty;
							Items eItem = new Items();
							eItem.setItemId(ItemId);
							eItem.setOrderQty(remaining_qty);
							log.info("REQUESTED QUANTITY IS MORE THAN THE PENDING QUANTITY" + "(" + remaining_qty + ")" + "TO BE RECEIVED FOR THIS ITEM " + "" + "" + ItemId);
							res.setRes("expected_qty_received_already");
							res.setItem(eItem);
							return res;

						}
						/*
						 * else { if(remaining_qty<remQty) { remQty = avlQty; avlQty = 0; } }
						 */
						else {
							if (avlQty <= remQty) {
								remQty = avlQty;
								avlQty = 0;
							} else {
								avlQty -= remQty;
							}
						}

						log.info("Inside RSWS: Carton" + carton + ":rcv:" + rcvQty + ":exp:" + expQty + ":rem:" + remQty);
						input = String.valueOf(input) + "<v12:ReceiptDtl>" + "<v12:item_id>" + ItemId + "</v12:item_id>" + "<v12:unit_qty>" + remQty + "</v12:unit_qty>"
								+ "<v12:receipt_xactn_type>R</v12:receipt_xactn_type>" + "<!-- Optional: -->" + "<v12:receipt_date>" + dateTime + "</v12:receipt_date>" + "<v12:receipt_nbr>"
								+ recieptNo + "</v12:receipt_nbr>" + "<v12:container_id>" + carton + "</v12:container_id>" + "</v12:ReceiptDtl>";
					}
				}
			}
			final URL url = new URL(wsURL);
			final URLConnection connection = url.openConnection();
			final HttpURLConnection httpConn = (HttpURLConnection) connection;
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/integration/bus/gateway/services/BusinessObjectId/v1\" xmlns:v11=\"http://www.oracle.com/retail/igs/integration/services/ReceivingPublishingService/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1\"><soapenv:Header></soapenv:Header><soapenv:Body><v11:publishReceiptCreateUsingReceiptDesc><!-- Optional: --><v12:ReceiptDesc><!-- 1 or more repetitions: --><v12:Receipt><v12:dc_dest_id>"
					+ destId + "</v12:dc_dest_id>" + "<v12:po_nbr>" + ponum + "</v12:po_nbr>" + "<v12:document_type>T</v12:document_type>" + "<v12:asn_nbr>" + bol + "</v12:asn_nbr>"
					+ "<!-- Zero or more repetitions: -->" + input + "</v12:Receipt>" + "</v12:ReceiptDesc>" + "</v11:publishReceiptCreateUsingReceiptDesc>" + "</soapenv:Body>"
					+ "</soapenv:Envelope>";
			log.info("INSIDE WEBSERVICE CALL-AFTER:" + xmlInput);
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			final byte[] b = bout.toByteArray();
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			final OutputStream out = httpConn.getOutputStream();
			out.write(b);
			out.close();
			final InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
			final BufferedReader in = new BufferedReader(isr);
			while ((responseString = in.readLine()) != null) {
				outputString = String.valueOf(outputString) + responseString;
			}
			if (outputString != null) {
				log.info("OUTPUT:" + outputString);
				final Document db = parseXmlFile(outputString);
				if (outputString.contains("successfully published") || outputString.contains("successfully")) {
					res.setDoc(db);
					res.setRes("success");
					return res;
				}
				res.setRes("ERROR WHILE EXECUTING THE WEB SERVICE");
				return res;
			}
		} catch (Exception e) {
			log.error("Exception in callwebserv ", e);
		} finally {
			try {
				if (rsWs != null && !rsWs.isClosed()) {
					rsWs.close();
				}
			} catch (Exception e) {
				log.warn("Error while closing the resultset");
			}
			try {
				if (stmtWs != null && !stmtWs.isClosed()) {
					stmtWs.close();
				}
			} catch (Exception e) {
				log.warn("Error while closing the statement");
			}
			try {
				if (connt != null && !connt.isClosed()) {
					connt.close();
				}
			} catch (Exception e) {
				log.warn("Error while closing the connection");
			}
			requestMap.remove(recieptNo);
			log.info("Released map lock for the po " + recieptNo);
		}
		return null;
	}

	private static Document parseXmlFile(final String in) {
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final InputSource is = new InputSource(new StringReader(in));
			return db.parse(is);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e2) {
			throw new RuntimeException(e2);
		} catch (IOException e3) {
			throw new RuntimeException(e3);
		}
	}

	public String getItem(final List<Items> item, final String poNum) throws SQLException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs1 = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.createStatement();
			log.info("Inside getItem");
			String itemId = "";
			int it = 0;
			while (it < item.size()) {
				itemId = item.get(it).getItemId();
				String sql1 = "select item from tsfdetail where item IN (" + itemId + ") and tsf_no IN (" + poNum + ")";
				log.info("sql1 " + sql1);
				rs1 = stmt.executeQuery(sql1);
				if (rs1.next()) {
					final String itm1 = rs1.getString("ITEM");
					log.info("Item:" + itm1 + ":" + itemId);
					if (!itm1.equalsIgnoreCase(itemId)) {
						log.info("INVALID ITEM NUMBER");
						return "INVALID ITEM NUMBER" + "" + "" + itemId;
					}

					return null;
				} else {
					++it;
					return "INVALID ITEM NUMBER " + "" + "" + itemId;
				}
			}
		} catch (Exception e) {
			log.info("Exception in getItem " + e.getMessage());
			return null;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return null;
	}

	public String getBol(final String distr, final List<Items> item) throws SQLException {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.createStatement();
			final String sql = "select bol_no from shipment where shipment in (select shipment from shipsku where distro_no=" + distr + " and rownum = 1)";
			rs = stmt.executeQuery(sql);
			if (!rs.isBeforeFirst() || rs == null) {
				return "INVALID PO NUMBER";
			}
			if (rs.next()) {
				String bol = rs.getString("bol_no");
				log.info("INSIDE BOL_NO:" + bol);
				return bol;
			}
		} catch (Exception e) {
			log.info("Exception in getBol " + e.getMessage());
			return null;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		if (stmt != null) {
			stmt.close();
		}
		if (rs != null) {
			rs.close();
		}
		if (connection != null) {
			connection.close();
		}
		return null;
	}

	public String checktransferNoExist(String transferNo) {

		String tsfNo = "Transfer not exist";
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "select * from tsfdetail where tsf_no in(" + transferNo + ")";

		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				tsfNo = "Transfer exist";
			}
			return tsfNo;
		} catch (Exception e) {
			log.info("Exception occured while establishing connecting for checktransferNoExist " + e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				log.info("Exception while closing the connection checktransferNoExist " + e.getMessage());
			}
		}
		return tsfNo;

	}

	@Override
	public ArrayList<Items> checkInvalidItems(TsfReceiveRequest request) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Items> invalidItems = new ArrayList<Items>();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.createStatement();
			for (int i = 0; i < request.getItems().size(); i++) {
				String reqItemId = request.getItems().get(i).getItemId();
				String sql = "select item from tsfdetail where item IN (" + reqItemId + ") and tsf_no IN (" + request.getPoNumber() + ")";
				log.info("Query for item check " + sql);
				rs = stmt.executeQuery(sql);
				if (!rs.next()) {
					Items item = new Items();
					item.setItemId(reqItemId);
					item.setOrderQty(request.getItems().get(i).getOrderQty());
					invalidItems.add(item);

				}

			}
		} catch (Exception e) {
			log.info("Exception in checkInvalidItems " + e.getMessage());
		} finally {

			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				log.info("Exception in closing the connection in checkInvalidItems " + e.getMessage());
			}
		}
		return invalidItems;

	}

	@Override
	public void lockReceiveRequest(TsfReceiveRequest request) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.prepareStatement("INSERT INTO XX_TRANSFER_LOCK_INFO(REQUEST_TYPE, TSF_NO, SOURCE_ID, DESTINATION_ID) VALUES(?, ?, NULL, ? )");
			stmt.setString(1, "TransferReceive");
			stmt.setLong(2, Long.parseLong(request.getPoNumber()));
			stmt.setInt(3, request.getDestId());
			stmt.executeUpdate();
		} catch (Exception e) {
			log.error("Exception while inserting into lock table ", e);
			throw e;
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
			}
		}
		log.info("Lock created successfully for the po -> " + request.getPoNumber());
	}

	public String readPropertiesFile() {
		Properties props = new Properties();
		String webservice_url = "";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
		try {
			if (inputStream != null) {

				props.load(inputStream);
				webservice_url = props.getProperty("webservice_url");
			}
		} catch (Exception e) {
			log.info("Exception in loading the property file " + e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.info("Exception in closing the inputstream " + e.getMessage());
			}
		}
		return webservice_url;
	}

	@Override
	public ArrayList<Items> checkInvalidQty(TsfReceiveRequest request) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Items> invalidQty = new ArrayList<Items>();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.createStatement();
			for (int i = 0; i < request.getItems().size(); i++) {
				String reqItemId = request.getItems().get(i).getItemId();
				String sql = "select SHIP_QTY  from tsfdetail  where item IN (" + reqItemId + ") and tsf_no IN (" + request.getPoNumber() + ")";
				log.info("Query for qty check " + sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					int shipQty = rs.getInt("SHIP_QTY");
					if (shipQty < request.getItems().get(i).getOrderQty() || request.getItems().get(i).getOrderQty() < 0) {
						Items item = new Items();
						item.setItemId(reqItemId);
						item.setOrderQty(request.getItems().get(i).getOrderQty());
						invalidQty.add(item);
					}

				}

			}
		} catch (Exception e) {
			log.info("Exception in checkInvalidQty " + e.getMessage());
		} finally {

			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				log.info("Exception in closing the connection in checkInvalidItems " + e.getMessage());
			}
		}
		return invalidQty;
	}

	@Override
	public ArrayList<Items> checkReceivedQty(TsfReceiveRequest request) {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Items> invalidQty = new ArrayList<Items>();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.createStatement();
			for (int i = 0; i < request.getItems().size(); i++) {
				String reqItemId = request.getItems().get(i).getItemId();
				String sql = "select (nvl(SHIP_QTY,0)- nvl(RECEIVED_QTY,0)) availQty from tsfdetail  where item IN (" + reqItemId + ") and tsf_no IN (" + request.getPoNumber() + ")";
				log.info("Query for qty check " + sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					int availQty = rs.getInt("availQty");
					if (availQty == 0) {
						Items item = new Items();
						item.setItemId(reqItemId);
						item.setOrderQty(availQty);
						invalidQty.add(item);
					}

				}

			}
		} catch (Exception e) {
			log.info("Exception in checkReceivedQty " + e.getMessage());
		} finally {

			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				log.info("Exception in closing the connection in checkInvalidItems " + e.getMessage());
			}
		}
		return invalidQty;
	}

	public int saveRequestandResponse(TsfReceiveRequest transferReceiveRequest, TsfReceiveResponse transferReceiveResponse) {
		log.info("Inside TsfReceiveController for saveRequestandResponse method");
		Connection conn = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			String requestQuery = "insert into OMS_NOON_TRANSFER_SERVICES (service_id,service_type,request,response,CREATE_DATETIME) values(noon_transfer_service_id.nextval,?,?,?,SYSTIMESTAMP)";
			pstmt = conn.prepareStatement(requestQuery);
			Gson gson = new Gson();
			String gsonRequestString = gson.toJson(transferReceiveRequest);
			String gsonResponeString = gson.toJson(transferReceiveResponse);
			Reader requestReader = new StringReader(gsonRequestString);
			Reader responseReader = new StringReader(gsonResponeString);
			pstmt.setString(1, "TransferReceive");
			pstmt.setClob(2, requestReader);
			pstmt.setClob(3, responseReader);
			i = pstmt.executeUpdate();

		} catch (SQLException e) {
			log.error("Exception in persisting the record into OMS_NOON_TRANSFER_SERVICES for the saveRequestandResponse " + transferReceiveRequest.getReceiptNumber(), e);
		}
		return i;
	}

	@Override
	public void removeReceiveRequestLock(TsfReceiveRequest request) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			log.info("Removing entry in lock table, po number -> " + request.getPoNumber());
			connection = jdbcTemplate.getDataSource().getConnection();
			stmt = connection.prepareStatement("DELETE FROM XX_TRANSFER_LOCK_INFO WHERE REQUEST_TYPE = ? AND TSF_NO = ? ");
			stmt.setString(1, "TransferReceive");
			stmt.setLong(2, Long.parseLong(request.getPoNumber()));
			stmt.executeUpdate();
		} catch (Exception e) {
			log.warn("Exception while removing in lock table, po number -> " + request.getPoNumber(), e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
			}
		}
		log.info("Lock released successfully for the po -> " + request.getPoNumber());
	}
}
