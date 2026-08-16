package com.extra.jood.dao;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extra.jood.bean.CappingCheckResult;
import com.extra.jood.bean.CbHistoryRequest;
import com.extra.jood.bean.CbHistoryResponse;
import com.extra.jood.bean.Lines;
import com.extra.jood.bean.MemberShipCbInfo;
import com.extra.jood.bean.MembershipCbResponse;
import com.extra.jood.bean.MembershipInfo;
import com.extra.jood.bean.MembershipResponse;
import com.extra.jood.bean.OrderDetail;
import com.extra.jood.bean.ResponseDetail;
import com.extra.jood.bean.ResponseInfo;
import com.extra.jood.bean.Stage;
import com.extra.jood.bean.Transaction;
import com.extra.jood.bean.TransactionDetail;
import com.extra.jood.bean.TransactionDetails;
import com.extra.jood.bean.TransactionInfo;

import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.OracleTypes;

/**
 * @author aibrahim
 *
 */
@Repository
public class JOODDAO {

	private static final Logger LOG = Logger.getLogger(JOODDAO.class);

	@Autowired
	private NamedParameterJdbcOperations jdbcTemplate;

	public Map<String, Object> saveTransactionDetail(final TransactionInfo transaction) throws Exception {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(19);

		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.TIMESTAMP));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.ARRAY));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));

		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("joodTranId", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("joodTranSeqNo", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("errorMessage", Types.VARCHAR));
		final Object[][] objArr = new Object[transaction.getTransactionLineItems().size()][10];
		for (int i = 0; i < transaction.getTransactionLineItems().size(); i++) {
			TransactionDetail detail = transaction.getTransactionLineItems().get(i);
			objArr[i][0] = detail.getLineNumber();
			objArr[i][1] = detail.getItem();
			objArr[i][2] = detail.getQty();
			objArr[i][3] = detail.getUnitRetailPrice();
			objArr[i][4] = detail.getUnitTotalRetailDiscount();
			objArr[i][5] = detail.getUnitTotalJoodDiscount();
			objArr[i][6] = detail.getUnitSellingPrice();
			objArr[i][7] = detail.getUnitRewardsCB();
			objArr[i][8] = detail.getUnitRedeemedCB();
			objArr[i][9] = detail.getRedeemptionEligible();
		}
		Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ CALL XX_JOOD_MEM_TRANSACTION.XX_JOOD_TRANSACTION_PROCESS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
				stmt.setLong(1, transaction.getActiveMembershipID());
				stmt.setString(2, transaction.getChannel());
				stmt.setString(3, transaction.getTransactionType());
				stmt.setTimestamp(4, new Timestamp(transaction.getDate().getTime()));

				stmt.setString(5, transaction.getOrderNumber());
				stmt.setString(6, transaction.getTransactionNumber());
				stmt.setString(7, transaction.getOrigTranscationNumber());
				stmt.setBigDecimal(8, transaction.getTotalRetailPrice());
				stmt.setBigDecimal(9, transaction.getTotalRetailDiscount());
				stmt.setBigDecimal(10, transaction.getTotalJoodDiscount());
				stmt.setBigDecimal(11, transaction.getTotalSellingPrice());
				stmt.setBigDecimal(12, transaction.getTotalRewardsCB());
				stmt.setBigDecimal(13, transaction.getTotalRedeemedCB());

				stmt.setArray(14, ((oracle.jdbc.OracleConnection) con).createOracleArray("XX_JOOD_TRANDTL_TBL", objArr));
				stmt.setString(15, transaction.getIsFirstPurchaseAvail());
				stmt.setLong(16, transaction.getMembershipProgram());

				stmt.registerOutParameter(17, Types.VARCHAR);
				stmt.registerOutParameter(18, Types.VARCHAR);
				stmt.registerOutParameter(19, Types.VARCHAR);
				stmt.registerOutParameter(20, Types.NUMERIC);
				stmt.registerOutParameter(21, Types.VARCHAR);
				return stmt;
			}
		}, declaredParameters);
		Object result = resultMap.get("status");
		if (!"Success".equalsIgnoreCase(result != null ? result.toString() : null)) {
			LOG.error("Message recevied from package for order number " + transaction.getOrderNumber() + ": " + resultMap.get("message") + " : " + resultMap.get("errorMessage") + " : "
					+ resultMap.get("joodTranSeqNo"));
			throw new Exception(resultMap.get("message").toString());
		}
		return resultMap;
	}

	public MembershipResponse checkEligibility(MembershipInfo membershipInfo) throws Exception {
		MembershipResponse response = new MembershipResponse();
		if ("Y".equals(membershipInfo.getTransaction().getCashbackCheck())) {
			response = validateCashBackEligibility(membershipInfo);
		} else {
			response = validateEligibility(membershipInfo);
		}
		return response;
	}

	@Transactional
	public MembershipResponse validateEligibility(final MembershipInfo membershipInfo) throws Exception {
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(35);

		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.TIMESTAMP));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.ARRAY));

		declaredParameters.add(new SqlOutParameter("memStatus", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("headerStatus", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("headerType", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("headerId", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("cappingAmt", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("consumedAmt", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("availableAmt", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("requestedAmt", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("rejectedAmt", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("totalCashBack", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("usedCashBack", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("remaimgingCashBack", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("results", OracleTypes.ARRAY));
		declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("respCode", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("errorMessage", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("firstPurchAvail", Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("joodProgram", Types.NUMERIC));
		final Object[][] objArr = new Object[membershipInfo.getTransaction().getTransactionLineItems().size()]["ORPOS".equals(membershipInfo.getTransaction().getChannel()) ? 10 : 9];
		for (int i = 0; i < membershipInfo.getTransaction().getTransactionLineItems().size(); i++) {
			int j = 0;
			TransactionDetail detail = membershipInfo.getTransaction().getTransactionLineItems().get(i);
			objArr[i][j++] = membershipInfo.getActiveMembershipID();
			objArr[i][j++] = membershipInfo.getMembershipTypeID();
			objArr[i][j++] = "FALSE";
			objArr[i][j++] = detail.getItem();
			if ("ORPOS".equals(membershipInfo.getTransaction().getChannel())) {
				objArr[i][j++] = BigDecimal.valueOf(detail.getLineNumber());
			}
			objArr[i][j++] = BigDecimal.valueOf(detail.getQty());
			objArr[i][j++] = new BigDecimal(detail.getUnitRetailPrice());
			objArr[i][j++] = new BigDecimal(detail.getUnitTotalRetailDiscount());
			objArr[i][j++] = new BigDecimal(detail.getUnitTotalJoodDiscount());
			objArr[i][j++] = new BigDecimal(detail.getUnitSellingPrice());
		}
		Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ call " + ("ORPOS".equals(membershipInfo.getTransaction().getChannel()) ? "XX_JOOD_POS_ELIGIBILITY_CHECK" : "XX_JOOD_ELIGIBILITY_CHECK")
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
				stmt.setLong(1, membershipInfo.getActiveMembershipID());
				stmt.setString(2, String.valueOf(membershipInfo.isRenewUpgradeDetails()).toUpperCase());
				stmt.setLong(3, membershipInfo.getMembershipTypeID());
				stmt.setString(4, membershipInfo.getTransaction().getTransactionNumber());
				stmt.setString(5, membershipInfo.getTransaction().getOrderNumber());
				stmt.setString(6, membershipInfo.getTransaction().getChannel());
				stmt.setTimestamp(7, new Timestamp(membershipInfo.getTransaction().getDate().getTime()));

				stmt.setLong(8, membershipInfo.getTransaction().getLocation());
				stmt.setString(9, membershipInfo.getTransaction().getCountry());
				stmt.setString(10, membershipInfo.getTransaction().getCustomer().getId());
				stmt.setString(11, membershipInfo.getTransaction().getCustomer().getName());
				stmt.setString(12, membershipInfo.getTransaction().getCustomer().getMobile());
				stmt.setString(13, membershipInfo.getTransaction().getCustomer().getEmail());
				stmt.setBigDecimal(14, membershipInfo.getTransaction().getTotalRetailPrice());
				stmt.setBigDecimal(15, membershipInfo.getTransaction().getTotalRetailDiscount());
				stmt.setBigDecimal(16, membershipInfo.getTransaction().getTotalJoodDiscount());
				stmt.setBigDecimal(17, membershipInfo.getTransaction().getTotalSellingPrice());

				String objTypeName = "";
				if ("ORPOS".equals(membershipInfo.getTransaction().getChannel())) {
					objTypeName = "XX_JOOD_POS_ELIG_DETAIL_TBL";
				} else {
					objTypeName = "XX_JOOD_ELIG_DETAIL_TBL";
				}

				stmt.setArray(18, ((oracle.jdbc.OracleConnection) con).createOracleArray(objTypeName, objArr));

				stmt.registerOutParameter(19, Types.VARCHAR);
				stmt.registerOutParameter(20, Types.VARCHAR);
				stmt.registerOutParameter(21, Types.VARCHAR);
				stmt.registerOutParameter(22, Types.VARCHAR);
				stmt.registerOutParameter(23, Types.NUMERIC);
				stmt.registerOutParameter(24, Types.NUMERIC);
				stmt.registerOutParameter(25, Types.NUMERIC);
				stmt.registerOutParameter(26, Types.NUMERIC);
				stmt.registerOutParameter(27, Types.NUMERIC);
				stmt.registerOutParameter(28, Types.NUMERIC);
				stmt.registerOutParameter(29, Types.NUMERIC);
				stmt.registerOutParameter(30, Types.NUMERIC);
				stmt.registerOutParameter(31, OracleTypes.ARRAY, "ORPOS".equals(membershipInfo.getTransaction().getChannel()) ? "XX_JOOD_POS_ELIG_RESULT_TBL" : "XX_JOOD_ELIG_RESULT_TBL");
				stmt.registerOutParameter(32, Types.VARCHAR);
				stmt.registerOutParameter(33, Types.VARCHAR);
				stmt.registerOutParameter(34, Types.VARCHAR);
				stmt.registerOutParameter(35, Types.VARCHAR);
				stmt.registerOutParameter(36, Types.VARCHAR);
				stmt.registerOutParameter(37, Types.NUMERIC);

				return stmt;
			}
		}, declaredParameters);
		Object result = resultMap.get("status");
		LOG.info("Message recevied from package for order number " + membershipInfo.getTransaction().getOrderNumber() + ": Status " + result + ": " + resultMap.get("message") + " : "
				+ resultMap.get("respCode"));
		MembershipResponse response = new MembershipResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(resultMap.get("status").toString().charAt(0));
		responseInfo.setMessage(resultMap.get("message").toString());
		responseInfo.setResCode(resultMap.get("respCode").toString());
		response.setResponseHeader(responseInfo);

		response.setResponseDetails(new LinkedList<ResponseDetail>());
		OracleArray stageArr = (OracleArray) resultMap.get("results");
		ResponseDetail detail = new ResponseDetail();
		response.getResponseDetails().add(detail);
		detail.setStages(new LinkedList<Stage>());
		Stage stage = new Stage();
		stage.setStage("active");
		stage.setIsFirstPurchaseAvail(resultMap.get("firstPurchAvail").toString());
		long memberShipProgram = ((BigDecimal) resultMap.get("joodProgram")).intValue();
		LOG.info("memberShipProgram :" + memberShipProgram);
		stage.setMembershipProgram(memberShipProgram);
		stage.setTotalCbAvail((BigDecimal) resultMap.get("remaimgingCashBack"));
		stage.setTotalCbEarned((BigDecimal) resultMap.get("totalCashBack"));
		stage.setTotalCbRedeemed((BigDecimal) resultMap.get("usedCashBack"));
		stage.setCappingCheckResults(new LinkedList<CappingCheckResult>());
		detail.getStages().add(stage);
		for (Object obj : (Object[]) stageArr.getArray()) {
			OracleStruct struct = (OracleStruct) obj;
			CappingCheckResult checkResult = new CappingCheckResult();
			Object[] stageRS = struct.getAttributes();
			int j = 1;
			if ("ORPOS".equals(membershipInfo.getTransaction().getChannel())) {
				checkResult.setLineNumber(stageRS[++j] != null ? Long.parseLong(stageRS[j].toString()) : null);
			}
			checkResult.setLevelType(stageRS[++j].toString());
			checkResult.setLevelDescription(stageRS[++j].toString());
			checkResult.setLevelIdentifier(stageRS[++j].toString());
			checkResult.setRequested(stageRS[++j] != null ? new BigDecimal(stageRS[j].toString()) : null);
			checkResult.setCapping(new BigDecimal(stageRS[++j].toString()));
			checkResult.setConsumed(stageRS[++j] != null ? new BigDecimal(stageRS[j].toString()) : null);
			checkResult.setAvailable(stageRS[++j] != null ? new BigDecimal(stageRS[j].toString()) : null);
			checkResult.setRejected(stageRS[++j] != null ? new BigDecimal(stageRS[j].toString()) : null);
			checkResult.setStatus(stageRS[++j] != null ? stageRS[j].toString() : null);
			stage.getCappingCheckResults().add(checkResult);
		}
		return response;
	}

	@Transactional
	private MembershipResponse validateCashBackEligibility(final MembershipInfo membershipInfo) throws Exception {
		List<SqlParameter> declaredParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // V_MEMBERSHIP_ID
				new SqlParameter(Types.VARCHAR), // V_MOBILE_NO
				new SqlParameter(Types.ARRAY), // V_ITEM_DETAILS

				new SqlOutParameter("memStatus", Types.VARCHAR), new SqlOutParameter("headerStatus", Types.VARCHAR), new SqlOutParameter("totalCashBack", Types.NUMERIC),
				new SqlOutParameter("usedCashBack", Types.NUMERIC), new SqlOutParameter("cbAvailable", Types.VARCHAR), new SqlOutParameter("results", OracleTypes.ARRAY),
				new SqlOutParameter("status", Types.VARCHAR), new SqlOutParameter("respCode", Types.VARCHAR), new SqlOutParameter("message", Types.VARCHAR),
				new SqlOutParameter("errorMessage", Types.VARCHAR));

		final List<TransactionDetail> details = membershipInfo.getTransaction().getTransactionLineItems();
		final Object[][] objArr = new Object[details.size()][4];

		for (int i = 0; i < details.size(); i++) {
			TransactionDetail detail = details.get(i);
			objArr[i][0] = membershipInfo.getActiveMembershipID();
			objArr[i][1] = membershipInfo.getMembershipTypeID();
			objArr[i][2] = detail.getItem();
			objArr[i][3] = BigDecimal.valueOf(detail.getQty());
		}

		Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ call XX_JOOD_CB_ELIG_CHECK(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

				OracleConnection oracleCon = con.unwrap(oracle.jdbc.OracleConnection.class);
				stmt.setLong(1, membershipInfo.getActiveMembershipID());
				stmt.setString(2, membershipInfo.getTransaction().getCustomer().getMobile());
				stmt.setArray(3, oracleCon.createOracleArray("XX_JOOD_CB_ELIG_DET_TBL", objArr));

				stmt.registerOutParameter(4, Types.VARCHAR);
				stmt.registerOutParameter(5, Types.VARCHAR);
				stmt.registerOutParameter(6, Types.NUMERIC);
				stmt.registerOutParameter(7, Types.NUMERIC);
				stmt.registerOutParameter(8, Types.VARCHAR);
				stmt.registerOutParameter(9, OracleTypes.ARRAY, "XX_JOOD_CB_RESULT_DET_TBL");
				stmt.registerOutParameter(10, Types.VARCHAR);
				stmt.registerOutParameter(11, Types.VARCHAR);
				stmt.registerOutParameter(12, Types.VARCHAR);
				stmt.registerOutParameter(13, Types.VARCHAR);

				return stmt;
			}
		}, declaredParameters);

		Object result = resultMap.get("status");
		LOG.info("Message received from package for order number " + membershipInfo.getTransaction().getOrderNumber() + ": Status " + result + ": " + resultMap.get("message") + " : "
				+ resultMap.get("respCode"));

		MembershipResponse response = new MembershipResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(result != null ? result.toString().charAt(0) : 'F');
		responseInfo.setMessage(String.valueOf(resultMap.get("message")));
		responseInfo.setResCode(String.valueOf(resultMap.get("respCode")));
		response.setResponseHeader(responseInfo);

		response.setResponseDetails(new LinkedList<ResponseDetail>());
		ResponseDetail detail = new ResponseDetail();
		detail.setStages(new LinkedList<Stage>());
		response.getResponseDetails().add(detail);

		Stage stage = new Stage();
		stage.setStage("active");

		Object cbAvailObj = resultMap.get("cbAvailable");
		try {
			stage.setTotalCbAvail(cbAvailObj != null ? new BigDecimal(cbAvailObj.toString()) : BigDecimal.ZERO);
		} catch (NumberFormatException e) {
			stage.setTotalCbAvail(BigDecimal.ZERO);
		}
		stage.setTotalCbEarned((BigDecimal) resultMap.get("totalCashBack"));
		stage.setTotalCbRedeemed((BigDecimal) resultMap.get("usedCashBack"));
		stage.setCappingCheckResults(new LinkedList<CappingCheckResult>());
		detail.getStages().add(stage);

		OracleArray stageArr = (OracleArray) resultMap.get("results");
		if (stageArr != null) {
			Object[] arr = (Object[]) stageArr.getArray();
			for (Object obj : arr) {
				OracleStruct struct = (OracleStruct) obj;
				Object[] attrs = struct.getAttributes();

				CappingCheckResult checkResult = new CappingCheckResult();
				checkResult.setStatus(String.valueOf(attrs[0]));
				checkResult.setLevelType(String.valueOf(attrs[1]));
				checkResult.setLevelIdentifier(String.valueOf(attrs[2]));
				checkResult.setLevelDescription(String.valueOf(attrs[2]));
				checkResult.setRedeemptionEligible(String.valueOf(attrs[3]));

				stage.getCappingCheckResults().add(checkResult);
			}
		}

		return response;
	}

	@Transactional
	public MembershipCbResponse getTransactionCbStatus(final MemberShipCbInfo request) throws Exception {

		List<SqlParameter> declaredParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // V_MEMBERSHIP_ID
				new SqlParameter(Types.NUMERIC), // V_MEMBERSHIP_TYPE_ID
				new SqlParameter(Types.VARCHAR), // V_TRANSACTION_NO
				new SqlParameter(Types.VARCHAR), // V_CUST_ORDER_NO
				new SqlParameter(Types.VARCHAR), // V_APPLICATION_ID
				new SqlParameter(Types.TIMESTAMP), // V_TRAN_DATE
				new SqlParameter(Types.NUMERIC), // V_LOCATION
				new SqlParameter(Types.VARCHAR), // V_COUNTRY

				// OUT params
				new SqlOutParameter("O_MEMBERSHIP_ID", Types.NUMERIC), new SqlOutParameter("O_MEMBERSHIP_TYPE_ID", Types.NUMERIC), new SqlOutParameter("O_TOTAL_THRESHOLD", Types.NUMERIC),
				new SqlOutParameter("O_TOTAL_PURCHASES", Types.NUMERIC), new SqlOutParameter("O_REAMINING_LIMIT", Types.NUMERIC), new SqlOutParameter("O_REMAINING_CASHBACK", Types.NUMERIC),
				new SqlOutParameter("O_LAST_TRAN_DATE", Types.TIMESTAMP), new SqlOutParameter("O_TRAN_NO", Types.VARCHAR), new SqlOutParameter("O_ORDER_NO", Types.VARCHAR),
				new SqlOutParameter("O_SOURCE_ID", Types.VARCHAR), new SqlOutParameter("O_TRAN_DATE", Types.TIMESTAMP), new SqlOutParameter("O_LOCATION", Types.NUMERIC),
				new SqlOutParameter("O_COUNTRY", Types.VARCHAR), new SqlOutParameter("O_TOTAL_RETAIL", Types.NUMERIC), new SqlOutParameter("O_TOTAL_DISCOUNT_AMT", Types.NUMERIC),
				new SqlOutParameter("O_TOTAL_JOOD_DISC_AMT", Types.NUMERIC), new SqlOutParameter("O_TOTAL_SELLING_RETAIL", Types.NUMERIC), new SqlOutParameter("O_TOTAL_CASHBACK", Types.NUMERIC),
				new SqlOutParameter("O_REDEEMED_CASHBACK", Types.NUMERIC), new SqlOutParameter("O_REMAINING_CASHBACK", Types.NUMERIC), new SqlOutParameter("O_EXPIRY_DATE", Types.TIMESTAMP),
				new SqlOutParameter("O_CB_RESULT", OracleTypes.ARRAY, "OMSDEV.XX_JOOD_CB_STATUS_DETAIL_TBL"), new SqlOutParameter("O_error_message", Types.VARCHAR),
				new SqlOutParameter("O_JOOD_PROGRAM", Types.NUMERIC));

		Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ call XX_JOOD_CB_STATUS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

				stmt.setLong(1, request.getActiveMembershipID());
				stmt.setLong(2, request.getMembershipTypeID());
				stmt.setString(3, request.getTransaction().getTransactionNumber());
				stmt.setString(4, request.getTransaction().getOrderNumber());
				stmt.setString(5, request.getTransaction().getChannel());
				stmt.setString(6, "16-09-2025"); // new Timestamp(request.getTransaction().getDate().getTime())
				stmt.setLong(7, request.getTransaction().getLocation());
				stmt.setString(8, request.getTransaction().getCountry());

				stmt.registerOutParameter(9, Types.NUMERIC);
				stmt.registerOutParameter(10, Types.NUMERIC);
				stmt.registerOutParameter(11, Types.NUMERIC);
				stmt.registerOutParameter(12, Types.NUMERIC);
				stmt.registerOutParameter(13, Types.NUMERIC);
				stmt.registerOutParameter(14, Types.NUMERIC);
				stmt.registerOutParameter(15, Types.TIMESTAMP);
				stmt.registerOutParameter(16, Types.VARCHAR);
				stmt.registerOutParameter(17, Types.VARCHAR);
				stmt.registerOutParameter(18, Types.VARCHAR);
				stmt.registerOutParameter(19, Types.TIMESTAMP);
				stmt.registerOutParameter(20, Types.NUMERIC);
				stmt.registerOutParameter(21, Types.VARCHAR);
				stmt.registerOutParameter(22, Types.NUMERIC);
				stmt.registerOutParameter(23, Types.NUMERIC);
				stmt.registerOutParameter(24, Types.NUMERIC);
				stmt.registerOutParameter(25, Types.NUMERIC);
				stmt.registerOutParameter(26, Types.NUMERIC);
				stmt.registerOutParameter(27, Types.NUMERIC);
				stmt.registerOutParameter(28, Types.NUMERIC);
				stmt.registerOutParameter(29, Types.TIMESTAMP);
				stmt.registerOutParameter(30, OracleTypes.ARRAY, "OMSDEV.XX_JOOD_CB_STATUS_DETAIL_TBL");
				stmt.registerOutParameter(31, Types.VARCHAR);
				stmt.registerOutParameter(32, Types.NUMERIC);

				return stmt;
			}
		}, declaredParameters);
		LOG.info("Result from XX_JOOD_CB_STATUS: O_MEMBERSHIP_ID :" + String.valueOf(resultMap.get("O_MEMBERSHIP_ID")));

		// Now build response object
		MembershipCbResponse response = new MembershipCbResponse();
		Transaction transaction = new Transaction();

		response.setActiveMembershipID(((BigDecimal) resultMap.get("O_MEMBERSHIP_ID")).longValue());
		response.setMembershipTypeID(((BigDecimal) resultMap.get("O_MEMBERSHIP_TYPE_ID")).intValue());
		response.setMembershipProgram(((BigDecimal) resultMap.get("O_JOOD_PROGRAM")).intValue());
		response.setTotalPurchasesThreshold(String.valueOf(((BigDecimal) resultMap.get("O_TOTAL_THRESHOLD"))));
		response.setTotalPurchases(String.valueOf(((BigDecimal) resultMap.get("O_TOTAL_PURCHASES"))));
		response.setRemainingLimit(String.valueOf(((BigDecimal) resultMap.get("O_REAMINING_LIMIT"))));
		response.setTotalAvailableCashback(String.valueOf(((BigDecimal) resultMap.get("O_REMAINING_CASHBACK"))));
		Timestamp dbDate = (Timestamp) resultMap.get("O_LAST_TRAN_DATE");
		response.setLastTransactionDate(dbDate != null ? new Date(dbDate.getTime()) : null);
		transaction.setTransactionNumber((String) resultMap.get("O_TRAN_NO"));
		transaction.setOrderNumber((String) resultMap.get("O_ORDER_NO"));
		transaction.setChannel((String) resultMap.get("O_SOURCE_ID"));
		transaction.setDate((Timestamp) resultMap.get("O_TRAN_DATE"));
		transaction.setLocation(((BigDecimal) resultMap.get("O_LOCATION")).intValue());
		transaction.setCountry((String) resultMap.get("O_COUNTRY"));
		transaction.setTotalRetailPrice(resultMap.get("O_TOTAL_RETAIL") != null ? ((BigDecimal) resultMap.get("O_TOTAL_RETAIL")).longValue() : 0L);
		transaction.setTotalRetailDiscount(resultMap.get("O_TOTAL_DISCOUNT_AMT") != null ? ((BigDecimal) resultMap.get("O_TOTAL_DISCOUNT_AMT")).longValue() : 0L);
		transaction.setTotalJoodDiscount(resultMap.get("O_TOTAL_JOOD_DISC_AMT") != null ? ((BigDecimal) resultMap.get("O_TOTAL_JOOD_DISC_AMT")).longValue() : 0L);
		transaction.setTotalSellingPrice(resultMap.get("O_TOTAL_SELLING_RETAIL") != null ? ((BigDecimal) resultMap.get("O_TOTAL_SELLING_RETAIL")).longValue() : 0L);
		transaction.setTotalRewardsCB(resultMap.get("O_TOTAL_CASHBACK") != null ? ((BigDecimal) resultMap.get("O_TOTAL_CASHBACK")).longValue() : 0L);
		transaction.setTotalRedeemedCB(resultMap.get("O_REDEEMED_CASHBACK") != null ? ((BigDecimal) resultMap.get("O_REDEEMED_CASHBACK")).longValue() : 0L);
		transaction.setTotalAvailableCB(resultMap.get("O_REMAINING_CASHBACK") != null ? ((BigDecimal) resultMap.get("O_REMAINING_CASHBACK")).longValue() : 0L);
		transaction.setExpiryDate(resultMap.get("O_EXPIRY_DATE") != null ? String.valueOf(resultMap.get("O_EXPIRY_DATE")) : null);

		// Handle ARRAY of line items
		OracleArray txArray = (OracleArray) resultMap.get("O_CB_RESULT");
		LOG.info("O_CB_RESULT is :" + txArray);

		List<Lines> lineList = new ArrayList<>();
		if (txArray != null) {
			Object[] arr = (Object[]) txArray.getArray();
			for (Object obj : arr) {
				OracleStruct struct = (OracleStruct) obj;
				Object[] attrs = struct.getAttributes();

				Lines line = new Lines();
				line.setLineNumber(((BigDecimal) attrs[1]).intValue());
				line.setItem(String.valueOf(attrs[0]));
				line.setQty(((BigDecimal) attrs[2]).intValue());
				line.setUnitRetailPrice(((BigDecimal) attrs[3]).longValue());
				line.setUnitTotalRetailDiscount(((BigDecimal) attrs[4]).longValue());
				line.setUnitTotalJoodDiscount(((BigDecimal) attrs[5]).longValue());
				line.setUnitSellingPrice(((BigDecimal) attrs[6]).longValue());
				line.setUnitRewardsCB(String.valueOf(attrs[7]));
				line.setUnitRedeemedCB(String.valueOf(attrs[8]));
				line.setUnitAvailableCB(String.valueOf(attrs[9]));

				lineList.add(line);
			}
		}
		transaction.setLines(lineList);

		response.setTransaction(transaction);

		String errorMsg = (String) resultMap.get("O_error_message");
		if (errorMsg != null) {
			LOG.error("Error from XX_JOOD_CB_STATUS: " + errorMsg);
		}

		return response;
	}

	@Transactional
	public CbHistoryResponse getCbHistoryTransaction(final CbHistoryRequest request) throws Exception {
		List<SqlParameter> declaredParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), new SqlParameter(Types.NUMERIC), new SqlParameter(Types.NUMERIC), new SqlParameter(Types.NUMERIC),
				new SqlParameter(Types.NUMERIC), new SqlParameter(Types.VARCHAR), new SqlOutParameter("o_joodmemberid", Types.NUMERIC), new SqlOutParameter("o_membership_type_id", Types.NUMERIC),
				new SqlOutParameter("o_mobile_no", Types.NUMERIC), new SqlOutParameter("o_availble_cashback", Types.NUMERIC),new SqlOutParameter("o_total_trans", Types.NUMERIC),
				new SqlOutParameter("o_cb_result", OracleTypes.ARRAY, "OMSDEV.XX_JOOD_CB_TRAN_HIST_DET_TBL"), new SqlOutParameter("o_error_message", Types.VARCHAR));

		Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ call XX_JOOD_CB_TRAN_HISTORY_V1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
				long size = request.getOffSet() + request.getSize() - 1;
				stmt.setLong(1, request.getActiveMembershipID());
				stmt.setLong(2, request.getMembershipTypeID());
				stmt.setLong(3, request.getMobileNo());
				stmt.setLong(4, request.getOffSet());
				stmt.setLong(5, size);
				stmt.setString(6, request.getOrderNo());
				stmt.registerOutParameter(7, Types.NUMERIC);
				stmt.registerOutParameter(8, Types.NUMERIC);
				stmt.registerOutParameter(9, Types.NUMERIC);
				stmt.registerOutParameter(10, Types.NUMERIC);
				stmt.registerOutParameter(11, Types.NUMERIC);
				stmt.registerOutParameter(12, OracleTypes.ARRAY, "XX_JOOD_CB_TRAN_HIST_DET_TBL");
				stmt.registerOutParameter(13, Types.VARCHAR);

				return stmt;
			}
		}, declaredParameters);

		CbHistoryResponse response = new CbHistoryResponse();
		response.setActiveMembershipID(((BigDecimal) resultMap.get("o_joodmemberid")).longValue());
		response.setMembershipTypeID(((BigDecimal) resultMap.get("o_membership_type_id")).intValue());
		response.setMobileNo(((BigDecimal) resultMap.get("o_mobile_no")).longValue());
		response.setTotalAvailableCashback(((BigDecimal) resultMap.get("o_availble_cashback")).longValue());
		response.setTotalTransactions(((BigDecimal) resultMap.get("o_total_trans")).longValue());
		OracleArray txArray = (OracleArray) resultMap.get("o_cb_result");
		int size = txArray.length();
		if (txArray != null) {
			Object[] arr = (Object[]) txArray.getArray();
			List<OrderDetail> orderDetails = new ArrayList<>();

			for (Object obj : arr) {
				OracleStruct struct = (OracleStruct) obj;
				Object[] attrs = struct.getAttributes();

				OrderDetail orderDetail = new OrderDetail();
				String tranType = attrs[0] != null ? attrs[0].toString().trim().toUpperCase() : "";

				TransactionDetails transaction = new TransactionDetails();
				transaction.setTransactionType(tranType);
				transaction.setTransactionDate(String.valueOf(attrs[1]));
				orderDetail.setOrderId(String.valueOf(attrs[3]));
				if ("SALE".equals(tranType) || "RETURN".equals(tranType) || "CANCEL".equals(tranType)) {
					transaction.setAmount(((BigDecimal) attrs[4]).longValue());
				} else {
					transaction.setAmount(((BigDecimal) attrs[5]).longValue());
				}
				transaction.setCurrency("SR");
				transaction.setDescription(null);

				orderDetail.setTransactions(Collections.singletonList(transaction));
				orderDetails.add(orderDetail);
			}
			response.setOrderDetails(orderDetails);
		}

		String errorMsg = (String) resultMap.get("o_error_message");
		if (errorMsg != null) {
			LOG.error("Error from xx_jood_cb_tran_history: " + errorMsg);
		}

		return response;
	}

}