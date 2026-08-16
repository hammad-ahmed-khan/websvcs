package org.logicInfo.oms.bookingConfirmation.controller;

import java.sql.SQLException;
import java.util.List;

public interface BookingConfDAO {
	List<BookingConfirmationResponse> getResponse(int resvId, String orderNo, String omsOrderNo) throws SQLException;
}
