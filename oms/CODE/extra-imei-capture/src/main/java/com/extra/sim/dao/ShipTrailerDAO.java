package com.extra.sim.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.extra.sim.model.DMShipTrailer;

/**
 * @author aibrahim
 *
 */
@Repository
public class ShipTrailerDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public DMShipTrailer getShipTrailer(Long transferReturnId) {
		return DataAccessUtils.singleResult(jdbcTemplate.query("SELECT ID, SHIPMENT_BOL_ID, TRANSPORTER, TRUCK_NO, WAY_BILL_NO, TRUCK_LOAD, TRAILER_TYPE, NO_OF_QTY, PICK_TYPE FROM XX_SHIP_TRAILER_INFO WHERE ID = ?", new RowMapper<DMShipTrailer>() {

			@Override
			public DMShipTrailer mapRow(ResultSet rs, int rowNum) throws SQLException {
				DMShipTrailer shipTrailer = new DMShipTrailer();
				shipTrailer.setTransferReturnId(rs.getLong(1));
				shipTrailer.setShipmentBOLID(rs.getLong(2));
				shipTrailer.setTransporter(rs.getString(3));
				shipTrailer.setTruckNo(rs.getString(4));
				shipTrailer.setWayBillNo(rs.getString(5));
				shipTrailer.setTruckLoad(rs.getString(6));
				shipTrailer.setTrailerType(rs.getString(7));
				shipTrailer.setNoOfQty(rs.getInt(8));
				shipTrailer.setPickType(rs.getString(9));
				return shipTrailer;
			}

		}, transferReturnId));
	}

	public Long getShipmentBOLID(Long transferReturnId, Boolean isTransfer) {
		if (isTransfer) {
			return jdbcTemplate.queryForObject("SELECT SHIPMENT_BOL_ID FROM TRANSFER WHERE ID = ? ", Long.class, transferReturnId);
		} else {
			return jdbcTemplate.queryForObject("SELECT SHIPMENT_BOL_ID FROM RETURN WHERE ID = ? ", Long.class, transferReturnId);
		}
	}

	public void saveShipTrailer(DMShipTrailer shipTrailer) {
		jdbcTemplate.update("INSERT INTO XX_SHIP_TRAILER_INFO(ID, SHIPMENT_BOL_ID, TRANSPORTER, TRUCK_NO, WAY_BILL_NO, TRUCK_LOAD, TRAILER_TYPE, NO_OF_QTY, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER, PICK_TYPE) "
				+ "VALUES(?, (SELECT SHIPMENT_BOL_ID FROM " + (shipTrailer.isTransfer() ? "TRANSFER" : "RETURN") + " WHERE ID = ?), ?, ?, ?, ?, ?, ?, SYSDATE, ?, NULL, NULL, ?)", shipTrailer.getTransferReturnId(), shipTrailer.getTransferReturnId(), shipTrailer.getTransporter(),
				shipTrailer.getTruckNo(), shipTrailer.getWayBillNo(), shipTrailer.getTruckLoad(), shipTrailer.getTrailerType(), shipTrailer.getNoOfQty(), shipTrailer.getCreatedUser(), shipTrailer.getPickType());
	}

	public void updateShipTrailer(DMShipTrailer shipTrailer) {
		jdbcTemplate.update("UPDATE XX_SHIP_TRAILER_INFO SET TRANSPORTER = ?, TRUCK_NO = ?, WAY_BILL_NO = ?, TRUCK_LOAD = ?, TRAILER_TYPE = ?, NO_OF_QTY = ?, UPDATED_DATE = SYSDATE, UPDATED_USER = ?, PICK_TYPE = ? WHERE ID = ?", shipTrailer.getTransporter(),
				shipTrailer.getTruckNo(), shipTrailer.getWayBillNo(), shipTrailer.getTruckLoad(), shipTrailer.getTrailerType(), shipTrailer.getNoOfQty(), shipTrailer.getUpdatedUser(), shipTrailer.getPickType(), shipTrailer.getTransferReturnId());
	}

	public boolean hasShipTrailer(Long transferReturnId) {
		try {
			Long[] param = { transferReturnId };
			return jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM XX_SHIP_TRAILER_INFO WHERE ID = ?", param, Boolean.class);
		} catch (Exception e) {
			// EAT Exception
		}
		return false;
	}

	public boolean hasShipTrailerConfigured(Long storeId) {
		try {
			Long[] param = { storeId };
			return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM XX_SHIP_TRAILER_CONFIG WHERE STORE_ID = ?", param, Integer.class) > 0;
		} catch (Exception e) {
			// EAT Exception
		}
		return false;
	}

	public boolean hasShipTrailerRequired(Long storeId) {
		try {
			Long[] param = { storeId };
			return jdbcTemplate.queryForObject("SELECT STATUS_IND FROM XX_SHIP_TRAILER_CONFIG WHERE STORE_ID = ?", param, String.class).equals("Y");
		} catch (Exception e) {
			// EAT Exception
		}
		return false;
	}
}
