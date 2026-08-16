package org.logicInfo.oms.bookingDeletion.controller;

import java.sql.SQLException;
import java.util.List;

public interface BookingDelDAO {
	List<BookingDeletionResponse> getResponse(int reservationId, String orderNo) throws SQLException;
}
