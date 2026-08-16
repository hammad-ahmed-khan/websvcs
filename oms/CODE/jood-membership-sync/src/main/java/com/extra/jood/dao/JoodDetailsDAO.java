package com.extra.jood.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import com.extra.jood.model.JoodSyncRequest;

@Repository
public class JoodDetailsDAO {

	private static final Logger log = LogManager.getLogger(JoodDetailsDAO.class);

	@Autowired
	private NamedParameterJdbcOperations jdbcTemplate;

	public List<JoodSyncRequest> getJoodMemberShipDetails() {
		String query = "SELECT M.*, T.*  FROM XX_JOOD_MEMBERSHIP M JOIN XX_JOOD_MEMBERSHIP_TYPE T ON M.MEMBERSHIP_TYPE_ID = T.MEMBERSHIP_TYPE_ID "
				+ " WHERE M.STATUS = 'A' AND M.PUBLISH_IND = 'N'";
		return jdbcTemplate.query(query, new JoodMembershipRowMapper());
	}

	private static class JoodMembershipRowMapper implements RowMapper<JoodSyncRequest> {
		@Override
		public JoodSyncRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
			JoodSyncRequest membership = new JoodSyncRequest();
			membership.setMembershipId(rs.getLong("MEMBERSHIP_ID"));
			String mobileNo = rs.getString("MOBILE_NO");
			if (mobileNo != null) {
				mobileNo = "00" + mobileNo; // Add "00" prefix
			}
			membership.setCustomerMobile(mobileNo);
			membership.setCustomerEmail(rs.getString("EMAIL"));
			membership.setCustomerFirstName(rs.getString("MEMBERSHIP_NAME"));
			membership.setCustomerLastName(rs.getString("MEMBERSHIP_NAME"));
			membership.setContractNumber(rs.getString("CUST_ORDER_NUMBER"));
			membership.setInvoiceNumber(rs.getString("TRANSACTION_NUMBER"));
			membership.setInvoiceLineNumber(null);
			membership.setServiceProductLine(rs.getString("MEMBERSHIP_TYPE"));
			membership.setPackageName(rs.getString("SKU_DESC"));
			membership.setServiceSku(rs.getString("MEMBERSHIP_SKU"));
			membership.setContractPeriod(rs.getInt("MEMBERSHIP_DAYS"));
			membership.setContractStartingDate(rs.getString("START_DATE"));
			membership.setContractEndingDate(rs.getString("END_DATE"));
			if ("E-COMMERCE".equals(rs.getString("SOURCE")) || "eCom".equals(rs.getString("SOURCE"))) {
				String source = "WEB";
				membership.setSourceSystem(source);
			} else {
				membership.setSourceSystem(rs.getString("SOURCE"));
			}
			membership.setContractStatus("ACTIVE");
			membership.setPaidAmount(rs.getBigDecimal("UNIT_RETAIL"));
			return membership;
		}
	}

	public void updatePublishInd(long membershipId) {
		String query = "update XX_JOOD_MEMBERSHIP set PUBLISH_IND = 'S' where MEMBERSHIP_ID = :membershipId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("membershipId", membershipId);
		jdbcTemplate.update(query, paramMap);
		log.info("record updated.");
	}
}
