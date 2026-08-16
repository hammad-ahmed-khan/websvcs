package org.logicinfo.hybriscancellation.serviceImpl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.logicinfo.hybriscancellation.model.HybrisCancellationLineModel;
import org.logicinfo.hybriscancellation.model.HybrisCancellationMainModel;
import org.logicinfo.hybriscancellation.model.HybrisCancellationRefundModel;
import org.logicinfo.hybriscancellation.model.HybrisCancellationResLineModel;
import org.logicinfo.hybriscancellation.model.HybrisCancellationResModel;
import org.logicinfo.hybriscancellation.service.HybrisCancellationService;
import org.logicinfo.hybriscancellation.utill.UtillConstantsCode;
import org.logicinfo.hybriscancellation.utill.UtillConstantsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

@Transactional
@Repository("hybrisCancellationService")
public class HybrisCancellationServImpl implements HybrisCancellationService {
	private static final Logger _LOGGER = LogManager.getLogger(HybrisCancellationServImpl.class.getName());

	HybrisCancellationServImpl() {
		System.out.println("default constructor for hybrisCancellationService ");
	}

	@Value("${cancellation.api.url}")
	private String cancellationApiURL;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public HybrisCancellationResModel insertHybrisHeadTable(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Inside insert Hybris Head table  block");
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {
			_LOGGER.info("Inserting the request in Hybris Head table " + " for headseqno: " + request.getHeadSeqId() + "and " + "cancellation id " + request.getCancellationId() + "and order no: "
					+ request.getOrderCode());
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.insert_Hybris_Head);
			psmt.setLong(++i, request.getHeadSeqId());
			psmt.setString(++i, request.getOrderCode());
			psmt.setString(++i, request.getCancellationId());

			// if cancellation then generate oms cancellation id sequence
			if (request.getTransactionType().equals("CancelAndRefund")) {
				long cancelId = request.getOmsCancelId();

				// if oms cancellation id generated successfully
				if (cancelId > 0) {
					psmt.setLong(++i, cancelId);
				}

				// error while generate oms cancel id
				else {
					res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
					res.setStatus(UtillConstantsCode.failedMsg);
					res.setMessage(UtillConstantsCode.oms_Cancel_Id_Error); 
					return res;
				}

			}
			// if type is not a cancel then insert null to oms cancellation id
			else {
				psmt.setString(++i, null);
			}
			psmt.setString(++i, request.getTransactionType());
			psmt.setString(++i, request.getInitiatedFromStoreId());// request
																	// cancel id
			psmt.setString(++i, null); // status
			psmt.setString(++i, null); // READY_FOR_REFUND
			psmt.setString(++i, null); // MULE_REFUNDED
			psmt.setString(++i, null); // PUBLISH_TO_HYBRIS

			psmt.executeUpdate();
			_LOGGER.info("Inserted successfully in Hybris Head table " + " for headseqno: " + request.getHeadSeqId() + "and " + "cancellation id " + request.getCancellationId() + "and order no: "
					+ request.getOrderCode());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.hybris_head_error_msg);
			_LOGGER.info("Inserted Failed in Hybris Head table " + " for headseqno: " + request.getHeadSeqId() + "and " + "cancellation id " + request.getCancellationId() + "and order no: "
					+ request.getOrderCode());
		}

		finally {
			if (conn != null) {
				conn.close();
			}

			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel insertHybrisItemTable(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Inside insert Hybris Item Table block");
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			for (int j = 0; j < request.getLines().size(); j++) {
				// generate hybris item table sequence id
				long itemSeqId = generateHybrisItemSequenceId();
				// if item table SeqId is generated success
				if (itemSeqId > 0) {
					psmt = conn.prepareStatement(UtillConstantsQuery.insert_Hybris_Item);
					psmt.setLong(++i, request.getHeadSeqId());
					psmt.setLong(++i, itemSeqId);
					psmt.setLong(++i, request.getLines().get(j).getLineNo());
					psmt.setString(++i, request.getLines().get(j).getSku());
					psmt.setLong(++i, request.getLines().get(j).getQty());

					psmt.executeUpdate();
					i = 0;
				}
				// error while generate item table seq no
				else {
					res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
					res.setStatus(UtillConstantsCode.failedMsg);
					res.setMessage(UtillConstantsCode.sequence_Item_Error_msg);
					return res;
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			_LOGGER.error("Error while inserting hybris item table", e);
			res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.hybris_item_error_msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}

			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel insertHybrisTenderTable(HybrisCancellationMainModel request) throws SQLException {
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		ArrayList<Long> seq = new ArrayList<Long>();
		;
		int i = 0;
		int result = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();

			psmt = conn.prepareStatement(UtillConstantsQuery.insert_Hybris_Tender);

			// generate hybris item table sequence id
			// Inserting multiple tender values
			for (int j = 0; j < request.getRefunds().size(); j++) {
				psmt.setLong(++i, request.getHeadSeqId());
				long tenderSeqId = generateHybrisTenderSequenceId();
				// if item table SeqId is generated success
				if (tenderSeqId > 0) {

					request.setTenderSeqId(tenderSeqId);
					seq.add(tenderSeqId);

					psmt.setLong(++i, tenderSeqId);
					psmt.setString(++i, request.getRefunds().get(j).getPaymentType());
					psmt.setString(++i, request.getRefunds().get(j).getType());
					psmt.setString(++i, request.getRefunds().get(j).getSubtype());
					psmt.setDouble(++i, request.getRefunds().get(j).getDecimalAdjustment());
					psmt.setDouble(++i, request.getRefunds().get(j).getRefundAmount());
					psmt.setString(++i, request.getRefunds().get(j).getRefundable());
					psmt.setString(++i, request.getRefunds().get(j).getSadadBankId());
					psmt.setString(++i, request.getRefunds().get(j).getPayfortFortId());
					psmt.setString(++i, request.getRefunds().get(j).getTasheelWalletCivilId());
					psmt.setString(++i, request.getRefunds().get(j).getTasheelwalletRefNumber());
					psmt.setString(++i, request.getRefunds().get(j).getTasheelCardNo());
					psmt.setString(++i, request.getRefunds().get(j).getPayfortMerchantReference());

					result = psmt.executeUpdate();
					i = 0;
				}
				if (result > 0) {
					res.setCode(UtillConstantsCode.succesCode);
					res.setStatus(UtillConstantsCode.successMsg);

				}

				// error while generate item table seq no
				else {
					res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
					res.setStatus(UtillConstantsCode.failedMsg);
					res.setMessage(UtillConstantsCode.sequence_Tender_Error_msg);
					return res;
				}

			}
			request.setTenderSeqIdList(seq);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			_LOGGER.error("Error while inserting hybris item table", e);
			res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.hybris_tender_error_msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}

			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public void updateHybrisHeadTableErrorMessage(HybrisCancellationMainModel request, HybrisCancellationResModel res) throws SQLException {
		Connection conn = null;
		PreparedStatement psmt = null;

		int i = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();

			psmt = conn.prepareStatement(UtillConstantsQuery.update_Hybris_Head_Error_Message);
			psmt.setString(++i, res.getMessage());
			psmt.setLong(++i, request.getHeadSeqId());
			psmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		finally {
			if (conn != null) {
				conn.close();
			}

			if (psmt != null) {
				psmt.close();
			}
		}

	}

	public HybrisCancellationResModel updateHybrisHeadTableStatus(HybrisCancellationMainModel request, String status) throws SQLException {
		_LOGGER.info("Inside update Hybris Head Table Status Block");
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();

			psmt = conn.prepareStatement(status == "S" ? UtillConstantsQuery.update_Hybris_Head_Status : UtillConstantsQuery.update_Hybris_Head_Failed_Status);
			psmt.setString(++i, request.getCancellationId());
			psmt.setLong(++i, request.getHeadSeqId());

			psmt.executeUpdate();
			res.setCode(UtillConstantsCode.succesCode);

			if (res.getCode().equals(UtillConstantsCode.succesCode)) {

				// Updating OMS cancellation ID

				res = updateHybrisHeadTableOmsCancelID(request, status);

			}

		} catch (Exception e) {
			_LOGGER.error("Error while updating Hybris Head Table Status Block", e);
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.hybris_head_update_error_msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}

			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel updateHybrisHeadTableOmsCancelID(HybrisCancellationMainModel request, String status) throws SQLException {
		_LOGGER.info("Inside update Hybris Head Table ons cancellation id  Block");
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();

			psmt = conn.prepareStatement(UtillConstantsQuery.update_Hybris_Head_Oms_Cancellation_Id);
			psmt.setString(++i, request.getOrderCode());
			psmt.setLong(++i, request.getOmsCancelId());
			psmt.setLong(++i, request.getHeadSeqId());

			psmt.executeUpdate();
			res.setCode(UtillConstantsCode.succesCode);

		} catch (Exception e) {
			_LOGGER.error("Error while updating Hybris Head Table Status Block", e);
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.hybris_head_Oms_Cancel_Id_update_error_msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}

			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel checkvalidation(HybrisCancellationMainModel request, HybrisCancellationResModel res) throws SQLException {
		_LOGGER.info("Checking Common validation");
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		int i = 0;
		try {

			// befor check cutomer order number validation
			if (res.getCode().equals(UtillConstantsCode.invaildInput)) {
				return res;
			}
			_LOGGER.info("Checking the request data already process" + " in OMS and Hybris tables for headseqno: " + request.getHeadSeqId() + "and " + "cancellation id " + request.getCancellationId()
					+ "and order no: " + request.getOrderCode());

			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.Check_Refund_And_cancel_Byid_Query);
			psmt.setString(++i, request.getCancellationId());
			psmt.setString(++i, request.getTransactionType());
			psmt.setString(++i, request.getOrderCode());
			rs = psmt.executeQuery();
			if (rs.next()) {
				if (null == rs.getString(1)) {
					res.setCode(UtillConstantsCode.noDataCode);
					_LOGGER.info("No data found in Hybris custom " + "table for headseqid: " + request.getHeadSeqId() + "and order no :" + request.getOrderCode());
				}

				else if (rs.getString(1).equals(UtillConstantsCode.hybrisHeadStatus)) {

					if (rs.getString(2).equals("CancelAndRefund") || rs.getString(2).equals("CancelOnly")) {
						res.setCode(UtillConstantsCode.succesCode);
						res.setStatus(UtillConstantsCode.successMsg);
						res.setMessage(UtillConstantsCode.cancesuccessMsg);
					} else {
						res.setCode(UtillConstantsCode.succesCode);
						res.setStatus(UtillConstantsCode.successMsg);
						res.setMessage(UtillConstantsCode.refundsuccessMsg);
					}

					_LOGGER.info("Data found in Hybris custom " + "table for headseqid: " + request.getHeadSeqId() + "and" + " order no :" + request.getOrderCode() + "and order no: "
							+ request.getCancellationId() + "and status of" + (rs.getString(2) + "" + "" + UtillConstantsCode.successMsg));
				}

				else {

					if (rs.getString(2).equals("CancelAndRefund") || rs.getString(2).equals("CancelOnly")) {
						res = getLineItemresponsinvalidationlevel(request);
					} else {
						res.setCode(UtillConstantsCode.refundFailedCode);
						res.setStatus(UtillConstantsCode.failedMsg);
						res.setMessage(UtillConstantsCode.refundfailedMsg);
					}

					_LOGGER.info("Data found in Hybris custom " + "table for headseqid: " + request.getHeadSeqId() + "and" + " order no :" + request.getOrderCode() + "and order no: "
							+ request.getCancellationId() + "and status of" + (rs.getString(2) + "" + "" + UtillConstantsCode.failedMsg));
				}

			} else {
				res.setCode(UtillConstantsCode.noDataCode);
				res.setCode(UtillConstantsCode.noDataCode);
				_LOGGER.info("No data found in Hybris custom " + "table for headseqid: " + request.getHeadSeqId() + "and order no :" + request.getOrderCode());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(e.getMessage());
			_LOGGER.error("Error while checking common validation " + " for headseqid: " + request.getHeadSeqId() + "and order no :" + request.getOrderCode() + "error messsage of", e);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			_LOGGER.info("Closed connection for checking validation block");
		}

		return res;
	}

	public HybrisCancellationResModel checkCustomerOrder(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Checking Cutomer order no valid or not");
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;

		try {
			_LOGGER.info("Cutomer order no" + request.getOrderCode());

			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.Check_Oms_head_custOrd_no_Query);
			psmt.setString(++i, request.getOrderCode());

			rs = psmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(request.getOrderCode())) {
					res.setCode(UtillConstantsCode.succesCode);
					_LOGGER.info("Cutomer order number present in OMS");

					if (rs.getString(2).equals(UtillConstantsCode.success)) {
						res.setCode(UtillConstantsCode.succesCode);
						res.setStatus(UtillConstantsCode.successMsg);

						if (rs.getString(3).equals(UtillConstantsCode.success)) {
							res = updateHybrisHeadTableStatus(request, UtillConstantsCode.failure);
							res.setCode(UtillConstantsCode.refundFailedCode);
							res.setStatus(UtillConstantsCode.failure);
							res.setMessage(UtillConstantsCode.refundfailedMsg);
						}

					} else {
						// if sucess we need to update head table status and mule
						// table status
						res = insertDetailInMuleTableProcedure(request);
						if (res.getCode().equals(UtillConstantsCode.muleTableFailedCode)) {
							return res;
						}
						res = updateHybrisHeadTableStatus(request, UtillConstantsCode.success);
						// if error acuured while update status in hybris head table
						if (res.getCode().equals(UtillConstantsCode.failedCode)) {
							return res;
						}

						// if success
						else {

							res.setCode(UtillConstantsCode.succesCode);
							res.setStatus(UtillConstantsCode.success);
							res.setMessage(UtillConstantsCode.refundsuccessMsg);

							// need to insert data in mule table

						}
					}
				} else {
					res.setCode(UtillConstantsCode.failedCode);
					res.setStatus(UtillConstantsCode.failedMsg);
					res.setMessage(UtillConstantsCode.Payment_Error);
				}

			} else {
				res.setCode(UtillConstantsCode.invaildInput);
				res.setStatus(UtillConstantsCode.failedMsg);
				res.setMessage(UtillConstantsCode.custOrdError);
				_LOGGER.info("Customer order not present in OMS - Failed Code " + UtillConstantsCode.failedCode);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.invaildInput);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(e.getMessage());
			_LOGGER.error("Cutomer order no error", e);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			_LOGGER.info("Closed connection for customer order no validation block");
		}

		return res;

	}

	public HybrisCancellationResModel checkCustomerOrderPaymentStatus(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Checking Cutomer order no valid or not");
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {
			_LOGGER.info("Cutomer order no" + request.getOrderCode());

			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.Check_Oms_head_custOrd_Payment_Query);
			psmt.setString(++i, request.getOrderCode());

			rs = psmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(UtillConstantsCode.success)) {
					res.setCode(UtillConstantsCode.succesCode);
					res.setStatus(UtillConstantsCode.successMsg);
				}

				else {
					res.setCode(UtillConstantsCode.failedCode);
					res.setStatus(UtillConstantsCode.failedMsg);
					res.setMessage(UtillConstantsCode.Payment_Error);
				}

			}

			else {
				res.setCode(UtillConstantsCode.invaildInput);
				res.setStatus(UtillConstantsCode.failedMsg);
				res.setMessage(UtillConstantsCode.custOrdFailedError);
				_LOGGER.info("Cutomer order not present in OMS " + "Failed Code " + UtillConstantsCode.failedCode);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.invaildInput);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(e.getMessage());
			_LOGGER.error("Cutomer order no error", e);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			_LOGGER.info("Closed connection for customer order no validation block");
		}

		return res;

	}

	public HybrisCancellationResModel insertDetailInMuleTableProcedure(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Inside insert Detail In Mule Table Procedure Block");
		Connection conn = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		String RessultOut1 = "";
		String RessultOut2 = "";
		try {

			conn = jdbcTemplate.getDataSource().getConnection();
			String packageCallStmt = "{call XX_REFUND_REQUEST_PKG.create_xrr_refund_request(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			OracleCallableStatement psmt = (OracleCallableStatement) conn.prepareCall(packageCallStmt);

			for (int j = 0; j < request.getRefunds().size(); j++) {
				psmt.setLong(++i, request.getHeadSeqId());
				psmt.setLong(++i, request.getTenderSeqIdList().get(j));
				psmt.setString(++i, request.getRefunds().get(j).getPaymentType());
				psmt.setString(++i, request.getRefunds().get(j).getType());
				psmt.setString(++i, request.getRefunds().get(j).getSubtype());
				psmt.setDouble(++i, request.getRefunds().get(j).getDecimalAdjustment());
				psmt.setDouble(++i, request.getRefunds().get(j).getRefundAmount());
				psmt.setString(++i, request.getCountry());
				psmt.setString(++i, request.getCurrency());
				psmt.setString(++i, request.getRefunds().get(j).getRefundable());
				psmt.setString(++i, request.getRefunds().get(j).getSadadBankId());
				psmt.setString(++i, null);
				psmt.setString(++i, request.getRefunds().get(j).getSadadSPTN());
				psmt.setString(++i, request.getRefunds().get(j).getPayfortFortId());
				psmt.setString(++i, request.getRefunds().get(j).getPayfortMerchantReference());
				psmt.setString(++i, request.getRefunds().get(j).getTasheelWalletCivilId());
				psmt.setString(++i, request.getRefunds().get(j).getTasheelwalletRefNumber());

				if (request.getRefunds().get(j).getTasheelCardNo() != null) {
					psmt.setString(++i, request.getRefunds().get(j).getTasheelCardNo());
				} else {
					psmt.setString(++i, request.getRefunds().get(j).getBrandCode());
				}
				_LOGGER.info("BrandCode -" + request.getRefunds().get(j).getBrandCode());
				_LOGGER.info("Action -" + request.getRefunds().get(j).getAction());

				psmt.setString(++i, null);
				psmt.setString(++i, request.getRefunds().get(j).getAction());
				psmt.setString(++i, request.getRefunds().get(j).getTenderRefundId());
				psmt.setString(++i, null);
				psmt.setString(++i, null);
				psmt.setString(++i, request.getOrderCode());
				psmt.registerOutParameter(++i, OracleTypes.NUMBER);
				psmt.registerOutParameter(++i, OracleTypes.VARCHAR);
				psmt.registerOutParameter(++i, OracleTypes.VARCHAR);
				psmt.setString(++i, request.getMarketPlaceOrder());


				psmt.executeUpdate();
				i = 0;
				RessultOut1 = psmt.getString(26);
				RessultOut2 = psmt.getString(27);
				_LOGGER.info("Mule Package Response result1 :" + RessultOut1);
				_LOGGER.info("Mule Package Response result2 :" + RessultOut2);
				if (RessultOut1.equals("F")) {

					res = updateHybrisHeadTableStatus(request, "F");
					if (res.getCode().equals(UtillConstantsCode.failedCode)) {
						return res;
					}
					_LOGGER.info("Error While inserting mule table" + RessultOut2);
					res.setCode(UtillConstantsCode.muleTableFailedCode);
					res.setStatus(UtillConstantsCode.failedMsg);
					res.setMessage(UtillConstantsCode.mule_table_error_msg + "" + RessultOut2);
				} else {
					res.setCode(UtillConstantsCode.succesCode);
				}
			}

		} catch (Exception e) {
			_LOGGER.error("Error While inserting mule table", e);
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.muleTableFailedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.mule_table_error_msg + " :" + e);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
		}
		return res;
	}

	public HybrisCancellationResModel insertDetailInMuleTable(HybrisCancellationMainModel request) throws SQLException {

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {

			long refundId = generateRefundId();
			long sadadRefundId = generateSadadRefundId();
			if (refundId <= 0 || sadadRefundId <= 0) {
				res.setCode(UtillConstantsCode.failedCode);
				res.setStatus(UtillConstantsCode.failedMsg);
				res.setMessage(UtillConstantsCode.sequence_Mule_Error_msg);
				return res;
			}

			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.insert_Mule_Table);
			psmt.setLong(++i, refundId);
			psmt.setLong(++i, request.getHeadSeqId());
			psmt.setLong(++i, request.getTenderSeqId());
			psmt.setString(++i, request.getRefunds().get(0).getPaymentType());
			psmt.setString(++i, request.getRefunds().get(0).getType());
			psmt.setString(++i, request.getRefunds().get(0).getSubtype());
			psmt.setDouble(++i, request.getRefunds().get(0).getDecimalAdjustment());
			System.out.println("dec " + request.getRefunds().get(0).getDecimalAdjustment());
			psmt.setDouble(++i, request.getRefunds().get(0).getRefundAmount());
			psmt.setString(++i, request.getCountry());
			psmt.setString(++i, request.getCurrency());
			psmt.setString(++i, request.getRefunds().get(0).getRefundable());
			psmt.setString(++i, request.getRefunds().get(0).getSadadBankId());
			psmt.setString(++i, request.getRefunds().get(0).getSadadSPTN());
			psmt.setLong(++i, sadadRefundId);
			psmt.setString(++i, request.getRefunds().get(0).getPayfortFortId());
			psmt.setString(++i, request.getRefunds().get(0).getPayfortMerchantReference());
			psmt.setString(++i, request.getRefunds().get(0).getTasheelWalletCivilId());
			psmt.setString(++i, request.getRefunds().get(0).getTasheelwalletRefNumber());
			psmt.setString(++i, request.getRefunds().get(0).getTasheelCardNo());

			psmt.executeUpdate();
			res.setCode(UtillConstantsCode.succesCode);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.mule_table_error_msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel processingRefund(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Inside processing Refund Block");
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.Check_Oms_head_Status_Query);
			psmt.setString(++i, request.getOrderCode());
			rs = psmt.executeQuery();

			if (rs.next()) {
				if (rs.getString(1).equals(UtillConstantsCode.success)) {
					res = updateHybrisHeadTableStatus(request, UtillConstantsCode.failure);
					res.setCode(UtillConstantsCode.refundFailedCode);
					res.setStatus(UtillConstantsCode.failure);
					res.setMessage(UtillConstantsCode.refundfailedMsg);
				}
			} else {
				// if sucess we need to update head table status and mule
				// table status
				res = insertDetailInMuleTableProcedure(request);
				if (res.getCode().equals(UtillConstantsCode.muleTableFailedCode)) {
					return res;
				}
				res = updateHybrisHeadTableStatus(request, UtillConstantsCode.success);
				// if error acuured while update status in hybris head table
				if (res.getCode().equals(UtillConstantsCode.failedCode)) {
					return res;
				}
				// if success
				else {
					res.setCode(UtillConstantsCode.succesCode);
					res.setStatus(UtillConstantsCode.success);
					res.setMessage(UtillConstantsCode.refundsuccessMsg);
					// need to insert data in mule table
				}
			}
		} catch (Exception e) {
			_LOGGER.error("Error while processing refund processing Block", e);
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(e.getMessage());
		}
		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}
		return res;
	}

	public String generateJsonInputForCancellation(HybrisCancellationMainModel request, long cancelId) throws SQLException, DatatypeConfigurationException {
		_LOGGER.info("Inside generate Json Input For Cancellation Block");
		String comments = "HybrisCancellation";

		Date date = new Date();
		SimpleDateFormat DateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+HH:mm");
		String requestDate = DateTime.format(date);
		SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
		String cancelDate = dates.format(date);

		StringBuilder stringbuilder = new StringBuilder();
		StringBuilder stringbuildernew = new StringBuilder();
		StringBuilder stringbuilderReasonCode = new StringBuilder();
		try {
			for (HybrisCancellationLineModel req : request.getLines()) {

				stringbuilder.append("{" + "\"line_no\": " + req.getLineNo() + "," + "\"item\": " + "\"" + req.getSku() + "\"" + "," + "\"cancel_qty_suom\": " + req.getQty() + ","
						+ "\"item_comments\": " + "\"Item cancellation by Hybris\"" + "}" + ",");
			}

			stringbuilder.deleteCharAt(stringbuilder.length() - 1);

			double refundAmt = 0;
			for (HybrisCancellationRefundModel refund : request.getRefunds()) {
				refundAmt += refund.getRefundAmount();
			}
			Integer reasCode = request.getReasonCode();
			if (reasCode != null) {
				stringbuilderReasonCode.append("\"reason_code\":" + request.getReasonCode() + ",");
			}

			stringbuildernew.append(

					"{" + "\"entity_id\": " + "\"eXtra\" ," + "\"application_id\": " + "\"E-COMMERCE\" ," + " \"comments\": " + "\"" + comments + "\"" + " ," + "    \"cancellation_requestor_id\":"
							+ request.getInitiatedFromStoreId() + " ," + "    \"request_datetimestamp\":" + "\"" + requestDate + "\"" + " ," + "    \"cust_order_no\": " + "\"" + request.getOrderCode()
							+ "\"" + " ," + "    \"sub_cust_order_no\": " + 1 + "," + "    \"cancellation_id\":" + cancelId + " ," + "\"refund_preference\": " + "\"CLEARING\" ,"
							+ "\"refund_amount\": " + refundAmt + " ," + "\"cancellation_date\":" + "\"" + cancelDate + "\"" + "," + stringbuilderReasonCode + "\"reason\":" + "\""
							+ request.getReason() + "\"" + "," + "\"cancellation_items\":" + "[" + stringbuilder + "]"

							+ "}"

							+ "");

			_LOGGER.info("Input Json For COCancellation " + stringbuildernew);
			return stringbuildernew.toString();
		} catch (Exception e) {
			_LOGGER.error("Error while generating input json for CoCancellation ", e);
			return "F";
		}
	}

	public HybrisCancellationResModel processingCencellation(HybrisCancellationMainModel request, HybrisCancellationResModel res) throws SQLException {
		_LOGGER.info("Inside processing Cancellation Block");

		if (res.getStatus().equals(UtillConstantsCode.failedMsg)) {
			return res;
		}

		try {
			String reason = request.getReason();
			if (reason == null) {
				res.setCode(UtillConstantsCode.failedCode);
				res.setMessage(UtillConstantsCode.reason_Error);
				res.setStatus(UtillConstantsCode.failure);
				return res;
			}
			long cancelId = request.getOmsCancelId();

			if (cancelId <= 0) {
				res.setCode(UtillConstantsCode.failedCode);
				res.setStatus(UtillConstantsCode.failedMsg);
				res.setMessage(UtillConstantsCode.oms_Cancel_Id_Error);
				return res;
			}
			String input = generateJsonInputForCancellation(request, cancelId);
			if (input == "F") {
				res.setCode(UtillConstantsCode.failedCode);
				res.setMessage(UtillConstantsCode.json_Generate_Error);
				res.setStatus(UtillConstantsCode.failure);
				return res;
			}

			String responseString = "";
			String outputString = "";

//			if (request.getReason().equals("CancelAndRefund")) {
			final URL url = new URL(cancellationApiURL);
			final URLConnection connection = url.openConnection();
			final HttpURLConnection httpConn = (HttpURLConnection) connection;
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();

			byte[] buffer = new byte[input.length()];
			buffer = input.getBytes();
			bout.write(buffer);
			final byte[] b = bout.toByteArray();
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
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
			System.out.println(outputString);
			_LOGGER.info("json output from CoCancellation Web service" + outputString);

			res = responseToJson(request, outputString);

//			}
		} catch (Exception e) {
			_LOGGER.error("Error While calling CoCancellation Web Service", e);
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setMessage(UtillConstantsCode.CoCancellation_Service_Call_Error);
			res.setStatus(UtillConstantsCode.failure);
		}

		return res;
	}

	public HybrisCancellationResModel responseToJson(HybrisCancellationMainModel request, String res) throws SQLException {
		_LOGGER.info("Inside Response To Json block");
		List<HybrisCancellationResLineModel> lineList = new ArrayList<HybrisCancellationResLineModel>();
		HybrisCancellationResModel response = new HybrisCancellationResModel();
		JSONArray jsonObj = null;
		JSONArray JSONArray = null;
		org.json.JSONObject data = null;

		try {
			data = new JSONObject(res);

			String errorMsg = data.get("message_status").toString();

			String response_message = data.get("response_message").toString();

			jsonObj = data.getJSONArray("customerOrderCancelResponseItems");

			for (int i = 0; i < jsonObj.length(); i++) {
				HybrisCancellationResLineModel line = new HybrisCancellationResLineModel();

				line.setCode(jsonObj.getJSONObject(i).get("item").toString());
				line.setLineNo(jsonObj.getJSONObject(i).get("line_no").toString());
				line.setMessage(jsonObj.getJSONObject(i).get("message_desc").toString());
				lineList.add(line);

			}

			if (errorMsg.equals("F") && response_message.equals("FAILED")) {
				response.setCode(UtillConstantsCode.failedCode);
				response.setMessage(UtillConstantsCode.failedMsg);
				response.setStatus(UtillConstantsCode.canelfailedMsg);
				response.setLineRes(lineList);

			} else if (errorMsg.equals("E")) {
				response.setCode(UtillConstantsCode.failedCode);
				response.setMessage(UtillConstantsCode.failedMsg);
				response.setStatus(UtillConstantsCode.canel_system_error_Msg);
				response.setLineRes(lineList);
				return response;
			}

			if (errorMsg.equals("F") && response_message.equals("FAILED")) {

				// need to update cancellation status o failed
				response = updateHybrisHeadTableStatus(request, "F");
				if (response.getCode().equals(UtillConstantsCode.failedCode)) {
					return response;
				}
				response.setCode(UtillConstantsCode.failedCode);
				response.setMessage(UtillConstantsCode.failedMsg);
				response.setStatus(UtillConstantsCode.canelfailedMsg);
				response.setLineRes(lineList);
				response = updateHybrisItemTableErrorMsg(request, response);
				response.setCode(UtillConstantsCode.failedCode);
				response.setMessage(UtillConstantsCode.failedMsg);
				response.setStatus(UtillConstantsCode.canelfailedMsg);
				response.setLineRes(lineList);
				return response;
			} else {
				// need to update cancellation status sucesss and insert into
				// mule

				response = insertDetailInMuleTableProcedure(request);
				if (response.getCode().equals(UtillConstantsCode.muleTableFailedCode)) {
					return response;
				}

				response = updateHybrisHeadTableStatus(request, "S");

				if (response.getCode().equals(UtillConstantsCode.failedCode)) {
					return response;
				}

				response.setCode(UtillConstantsCode.succesCode);
				response.setMessage(UtillConstantsCode.cancesuccessMsg);
				response.setStatus(UtillConstantsCode.successMsg);
				response.setLineRes(lineList);

				return response;
			}

		} catch (Exception e) {
			_LOGGER.error("Error While generating Hybris json response output/Final Output error", e);
			response = updateHybrisHeadTableStatus(request, "F");
			if (response.getCode().equals(UtillConstantsCode.failedCode)) {
				return response;
			}
			System.out.println(e.getMessage());
			if (e.getMessage().contains("is not a JSONObject")) {
				JSONArray = data.getJSONArray("customerOrderCancelResponseItems");

				for (int i = 0; i < JSONArray.length(); i++) {
					HybrisCancellationResLineModel line = new HybrisCancellationResLineModel();
					line.setCode(JSONArray.getJSONObject(i).get("item").toString());
					line.setLineNo(JSONArray.getJSONObject(i).get("line_no").toString());
					line.setMessage(JSONArray.getJSONObject(i).get("message_desc").toString());
					lineList.add(line);

				}

				response.setLineRes(lineList);
				response = updateHybrisItemTableErrorMsg(request, response);
				response = getLineItemresponse(request);

				return response;

			} else {
				response.setCode(UtillConstantsCode.failedCode);
				response.setMessage(UtillConstantsCode.failedMsg);
				response.setStatus(UtillConstantsCode.convert_to_Json_Res);
			}

		}
		return response;

	}

	public HybrisCancellationResModel getLineItemresponse(HybrisCancellationMainModel request) throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		List<HybrisCancellationResLineModel> lineList = new ArrayList<HybrisCancellationResLineModel>();
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {

			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.get_Lineitem_Res_Item_table);
			psmt.setLong(++i, request.getHeadSeqId());
			rs = psmt.executeQuery();
			while (rs.next()) {
				HybrisCancellationResLineModel line = new HybrisCancellationResLineModel();
				line.setCode(rs.getString(1));
				line.setLineNo(rs.getString(2));
				if (rs.getNString(3).equals(null) || rs.getNString(3).isEmpty()) {
					line.setMessage(UtillConstantsCode.Common_Item_Error);
				} else {
					line.setMessage(rs.getNString(3));
				}

				lineList.add(line);
			}

			res.setCode(UtillConstantsCode.failedCode);
			res.setMessage(UtillConstantsCode.canelfailedMsg);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setLineRes(lineList);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.line_level_Res_Error_Msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel getLineItemresponsinvalidationlevel(HybrisCancellationMainModel request) throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		List<HybrisCancellationResLineModel> lineList = new ArrayList<HybrisCancellationResLineModel>();
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {

			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.get_Lineitem_Res_Item_table_Validation_level);
			psmt.setString(++i, request.getCancellationId());
			psmt.setString(++i, request.getOrderCode());
			psmt.setString(++i, UtillConstantsCode.failure);
			rs = psmt.executeQuery();
			while (rs.next()) {
				HybrisCancellationResLineModel line = new HybrisCancellationResLineModel();
				line.setCode(rs.getString(1));
				line.setLineNo(rs.getString(2));
				if (null == rs.getNString(3) || rs.getNString(3).isEmpty()) {
					line.setMessage(UtillConstantsCode.Common_Item_Error);
				} else {
					line.setMessage(rs.getNString(3));
				}

				lineList.add(line);
			}

			res.setCode(UtillConstantsCode.failedCode);
			res.setMessage(UtillConstantsCode.canelfailedMsg);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setLineRes(lineList);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.line_level_Res_Error_Msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel updateHybrisItemTableErrorMsg(HybrisCancellationMainModel request, HybrisCancellationResModel response) throws SQLException {

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		int i = 0;
		try {

			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.update_Hybris_Item_Table);
			for (int j = 0; j < response.getLineRes().size(); j++) {
				psmt.setString(++i, response.getLineRes().get(j).getMessage());
				psmt.setLong(++i, request.getHeadSeqId());
				psmt.setString(++i, response.getLineRes().get(j).getLineNo());
				psmt.setString(++i, response.getLineRes().get(j).getCode());
				psmt.executeUpdate();
				i = 0;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.failedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.hybris_item_Update_error_msg);
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return res;

	}

	public HybrisCancellationResModel checkRefundValue(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Inside check Refund Value block");
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		HybrisCancellationResModel res = new HybrisCancellationResModel();
		double requestTenderAmt = 0;
		int i = 0;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.get_tender_amt);
			psmt.setString(++i, request.getOrderCode());
			rs = psmt.executeQuery();

			if (rs.next()) {

				for (int j = 0; j < request.getRefunds().size(); j++) {
					requestTenderAmt = requestTenderAmt + request.getRefunds().get(j).getRefundAmount();
				}
				if (rs.getDouble(1) >= requestTenderAmt) {
					res.setCode(UtillConstantsCode.tenderSuccessCode);

				} else {
					res.setCode(UtillConstantsCode.tenderfailedCode);
					res.setStatus(UtillConstantsCode.failure);
					res.setMessage(UtillConstantsCode.invalidTenderAmt);
				}

			} else {
				res.setCode(UtillConstantsCode.tenderfailedCode);
				res.setStatus(UtillConstantsCode.failedMsg);
				res.setMessage(UtillConstantsCode.emptyTendervalue);
			}

		} catch (Exception e) {
			_LOGGER.error("Error while checking refund value in tender table ", e);
			System.out.println(e.getMessage());
			res.setCode(UtillConstantsCode.tenderfailedCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(e.getMessage());
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return res;
	}

	public HybrisCancellationResModel insertRequestInHybrisTables(HybrisCancellationMainModel request) throws SQLException {
		_LOGGER.info("Inside insert Request In Hybris Tables block");
		HybrisCancellationResModel res = new HybrisCancellationResModel();

		// generating sequence id for hybris custom table

		long seq = generateSequenceId();
		request.setHeadSeqId(seq);

		// if sequence generate successfully
		if (seq > 0) {

			res = insertHybrisHeadTable(request);
		}
		// error while generating sequence
		else {
			res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
			res.setStatus(UtillConstantsCode.failedMsg);
			res.setMessage(UtillConstantsCode.sequence_Error_msg);
			return res;
		}
		// if error accured in head table insertion
		if (res.getCode() == UtillConstantsCode.OmsCustomTablePresistErrorCode) {

			return res;
		}
		// insert hybris item table insertion
		else {
			// if sequence generate successfully
			if (seq > 0) {

				res = insertHybrisItemTable(request);
			}
			// error while generating sequence
			else {
				res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
				res.setStatus(UtillConstantsCode.failedMsg);
				res.setMessage(UtillConstantsCode.sequence_Error_msg);
				return res;
			}
			// if error accured in item table insertion
			if (res.getCode() == UtillConstantsCode.OmsCustomTablePresistErrorCode) {
				return res;
			}
			// insert hybris tender table insertion
			else {
				res = insertHybrisTenderTable(request);

				// if error accured in tender table insertion
				if (res.getCode() == UtillConstantsCode.OmsCustomTablePresistErrorCode) {
					return res;
				}
				// custom table insertion done successfully
				else {
					return res;
				}

			}

		}

	}

	public long generateSequenceId() throws SQLException {
		_LOGGER.info("Inside generating Head sequence block");

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		long seq = 0;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.generate_Head_seq_Id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				seq = rs.getLong(1) == 0 ? 1 : rs.getLong(1) + 1;
				_LOGGER.info("Head sequence id" + seq);
			}

		} catch (Exception e) {
			_LOGGER.error("Error while generating Head sequence id ", e);
			return seq;
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			_LOGGER.info("Closed connection for head seq generation block ");
		}

		return seq;
	}

	public long generateHybrisTenderSequenceId() throws SQLException {
		_LOGGER.info("Inside generate Hybris Tender Sequence Id Block");

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		long seq = 0;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.generate_tender_seq_Id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				seq = rs.getLong(1) == 0 ? 1 : rs.getLong(1) + 1;
			}

		} catch (Exception e) {
			_LOGGER.error("Error while generatig Hybris Tender Sequence Id", e);
			return seq;
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return seq;
	}

	public long generateHybrisItemSequenceId() throws SQLException {
		_LOGGER.info("Inside generate Hybris Item Sequence Id");
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		long seq = 0;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.generate_item_seq_Id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				// first time table is empty need to set (1) as squence id
				seq = rs.getLong(1) == 0 ? 1 : rs.getLong(1) + 1;
			}

		} catch (Exception e) {
			_LOGGER.error("Error While generating Hybris Item Sequence Id", e);
			return seq;
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return seq;
	}

	public long generateRefundId() throws SQLException {

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		long seq = 0;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.generate_Refund_Id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				seq = rs.getLong(1);
			}

		} catch (Exception e) {
			return seq;
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return seq;
	}

	public long generateSadadRefundId() throws SQLException {

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		long seq = 0;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.generate_Sadad_Refund_Id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				seq = rs.getLong(1);
			}

		} catch (Exception e) {
			return seq;
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
		}

		return seq;
	}

	public long generateOmsCancelId() throws SQLException {
		_LOGGER.info("Inside generate OMS Cancel Id  block");
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		long seq = 0;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			psmt = conn.prepareStatement(UtillConstantsQuery.generate_Oms_Cancel__Id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				seq = rs.getLong(1);
				_LOGGER.info("OMS Cancel Id" + seq);
			}

		} catch (Exception e) {
			_LOGGER.error("Error while generating OMS cancel id", e);
			return seq;
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			_LOGGER.info("Closed Connection for generating OMS cancel id block ");
		}

		return seq;
	}

	public void insertRequest(HybrisCancellationMainModel request) throws Exception {
		_LOGGER.info("Inserting request into XX_REF_REQ_LOG table");
		Connection conn = null;
		PreparedStatement psmt = null;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			String sql = "INSERT INTO XX_REF_REQ_LOG (ORDERNO, REQUEST, CREATEDATETIME) VALUES (?, ?, sysdate)";
			psmt = conn.prepareStatement(sql);

			// Set ORDERNO
			psmt.setString(1, request.getOrderCode());

			// Convert request object to JSON string
			ObjectMapper mapper = new ObjectMapper();
			String requestJson = mapper.writeValueAsString(request);

			// Set CLOB
			psmt.setClob(2, new StringReader(requestJson));

			int rowsInserted = psmt.executeUpdate();
			_LOGGER.info("Rows inserted into XX_REF_REQ_LOG: " + rowsInserted);

		} catch (Exception e) {
			_LOGGER.error("Failed to insert into XX_REF_REQ_LOG", e);
			throw e;
		} finally {
			if (psmt != null) {
				psmt.close();
			}
			if (conn != null) {
				conn.close();
			}
			_LOGGER.info("Closed connection after inserting into XX_REF_REQ_LOG");
		}
	}

	public void insertResponse(HybrisCancellationResModel res, String orderNo) throws Exception {
		_LOGGER.info("Updating XX_REF_REQ_LOG with response for orderNo: " + orderNo);
		Connection conn = null;
		PreparedStatement psmt = null;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			String sql = "UPDATE XX_REF_REQ_LOG SET RESPONSE = ?, LASTUPDATEDATETIME = sysdate WHERE ORDERNO = ?";
			psmt = conn.prepareStatement(sql);

			// Convert response object to JSON
			ObjectMapper mapper = new ObjectMapper();
			String responseJson = mapper.writeValueAsString(res);

			// Set CLOB
			psmt.setClob(1, new StringReader(responseJson));

			// Set ORDERNO
			psmt.setString(2, orderNo);

			int rowsUpdated = psmt.executeUpdate();
			_LOGGER.info("Rows updated in XX_REF_REQ_LOG: " + rowsUpdated);

		} catch (Exception e) {
			_LOGGER.error("Failed to update XX_REF_REQ_LOG", e);
			throw e;
		} finally {
			if (psmt != null) {
				psmt.close();

			}
			if (conn != null) {
				conn.close();
			}
			_LOGGER.info("Closed connection after updating XX_REF_REQ_LOG");
		}
	}

	public void getSeqIdDetails(HybrisCancellationMainModel request) throws SQLException {
		long headSeq = generateSequenceId();
		request.setHeadSeqId(headSeq);

		ArrayList<Long> seq = new ArrayList<>();

		for (int j = 0; j < request.getRefunds().size(); j++) {
			long tenderSeqId = generateHybrisTenderSequenceId();
			if (tenderSeqId > 0) {
				request.setTenderSeqId(tenderSeqId); // last one will remain in request
				seq.add(tenderSeqId);
			} else {
				throw new SQLException("Failed to generate tender sequence ID for refund index: " + j);
			}
		}

		request.setTenderSeqIdList(seq);
	}

}
