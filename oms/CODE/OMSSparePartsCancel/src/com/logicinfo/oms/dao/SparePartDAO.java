package com.logicinfo.oms.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.logicinfo.oms.model.SparePartsCancelRequest;
import com.logicinfo.oms.util.OMSUtil;

/**
 * @author aibrahim
 *
 */
public class SparePartDAO {

	private final static Logger _LOG = Logger.getLogger(SparePartDAO.class);

	public void saveTransferReversalRequest(SparePartsCancelRequest input, String itemId, BigDecimal sourceLoc, BigDecimal qty) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {			
			connection = OMSUtil.createDBConnection("jdbc/rms");
			statement = connection.prepareStatement("INSERT INTO OMS_SIEBEL_CANCELLATION(SERVICE_REQUEST_ID, OMS_SERVICE_REQ_SEQ_ID, STORE_ID, ITEM_ID, QUANTITY_REQUESTED, SOURCE_LOC, DESTINATION_LOC, CREATE_TIMESTAMP, STATUS) "
					+ "VALUES(?, ?, ?, ?, ?, ?, (SELECT SOURCE_LOCATION from OMS_SPARE_PART_FULFILL WHERE OMS_SERVICE_REQ_SEQ_ID = ? AND TRAN_TYPE = 'TSF' AND QUANTITY_SHIPPED > 0), SYSDATE, 'P')");
			statement.setString(1, input.getServiceRequestId());
			statement.setLong(2, Long.parseLong(input.getOMSServiceId()));
			statement.setBigDecimal(3, sourceLoc);
			statement.setString(4, itemId);
			statement.setBigDecimal(5, qty);
			statement.setBigDecimal(6, sourceLoc);
			statement.setLong(7, Long.parseLong(input.getOMSServiceId()));
			statement.executeUpdate();
			_LOG.info("Transfer reversal request saved successfully.");
		} catch (Exception e) {
			_LOG.warn("Error while saving the transfer reversal request", e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, null);
		}
	}
}
