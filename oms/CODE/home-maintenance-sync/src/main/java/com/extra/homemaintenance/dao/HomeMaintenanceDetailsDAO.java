package com.extra.homemaintenance.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import com.extra.homemaintenance.model.HomeMaintenanceSyncRequest;

@Repository
public class HomeMaintenanceDetailsDAO {

	private static final Logger log = LogManager.getLogger(HomeMaintenanceDetailsDAO.class);

	@Autowired
	private NamedParameterJdbcOperations jdbcTemplate;

	public List<HomeMaintenanceSyncRequest> getHomeMaintenanceMemberShipDetails() {
		String query = "SELECT  M.*, T.* " + "FROM XX_SUBSCRIPTION_DETAILS M  JOIN XX_SUBSCRIPTION_TYPE T "
				+ "  ON M.SUBSCRIPTION_TYPE_ID = T.SUBSCRIPTION_TYPE_ID"
				+ " WHERE M.STATUS = 'A' AND M.PUBLISH_IND = 'N' AND M.RETRY_COUNT < 6";
		return jdbcTemplate.query(query, new HomeMaintenanceMembershipRowMapper());
	}

	private static class HomeMaintenanceMembershipRowMapper implements RowMapper<HomeMaintenanceSyncRequest> {
		@Override
		public HomeMaintenanceSyncRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
			HomeMaintenanceSyncRequest membership = new HomeMaintenanceSyncRequest();

			membership.setSubscriptionId(rs.getLong("SUBSCRIPTION_ID"));
			membership.setCountry(rs.getString("COUNTRY"));

			// ================= CUSTOMER =================
			String mobileNo = rs.getString("MOBILE_NO");
			if (mobileNo != null && !mobileNo.startsWith("00")) {
				mobileNo = "00" + mobileNo;
			}
			membership.setCustomerMobile(mobileNo);

			membership.setCustomerEmail(rs.getString("EMAIL"));

			// Split name properly instead of duplicating
			String fullName = rs.getString("SUBSCRIBER_NAME");
			membership.setCustomerFirstName(fullName);
			membership.setCustomerLastName(fullName);

			// ================= CONTRACT =================
			membership.setContractNumber(rs.getString("TRANSACTION_NUMBER"));
			membership.setInvoiceNumber(rs.getString("TRANSACTION_NUMBER"));
			membership.setInvoiceLineNumber("1"); // Not available in DB → default

			membership.setPackageName(rs.getString("SUBSCRIPTION_TYPE_DESC"));
			membership.setServiceSku(rs.getString("SUBSCRIPTION_ITEM"));

			membership.setContractPeriod(rs.getInt("SUBSCRIPTION_DAYS"));

			membership.setSourceSystem(rs.getString("SOURCE"));
			membership.setContractStatus(rs.getString("STATUS"));

			// ================= DATES =================
			Timestamp startTimestamp = rs.getTimestamp("START_DATE");
			Timestamp endTimestamp = rs.getTimestamp("END_DATE");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			if (startTimestamp != null) {
				membership.setContractStartingDate(formatter.format(startTimestamp));
			}

			if (endTimestamp != null) {
				membership.setContractEndingDate(formatter.format(endTimestamp));
			}

			if ("E-COMMERCE".equals(rs.getString("SOURCE")) || "eCom".equals(rs.getString("SOURCE"))) {
				String source = "WEB";
				membership.setSourceSystem(source);
			} else {
				membership.setSourceSystem(rs.getString("SOURCE"));
			}

			// ================= AMOUNT =================
			membership.setPaidAmount(rs.getBigDecimal("UNIT_PRICE"));
			return membership;
		}
	}

	public void updatePublishInd(long subscriptionId) {
		String query = "update XX_SUBSCRIPTION_DETAILS set PUBLISH_IND = 'S',RETRY_COUNT = RETRY_COUNT + 1, LAST_UPDATE_TIME = sysdate where SUBSCRIPTION_ID = :subscriptionId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("subscriptionId", subscriptionId);
		jdbcTemplate.update(query, paramMap);
		log.info("record success updated.");
	}

	public void updateFailure(long subscriptionId) {
		String query = "update XX_SUBSCRIPTION_DETAILS set RETRY_COUNT = RETRY_COUNT + 1, LAST_UPDATE_TIME = sysdate where SUBSCRIPTION_ID = :subscriptionId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("subscriptionId", subscriptionId);
		jdbcTemplate.update(query, paramMap);
		log.info("record failure updated.");
	}
}
