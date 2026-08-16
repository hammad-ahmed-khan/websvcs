package org.logicinfo.oms.bookingCreation.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public interface BookingDAO {
	DeliveriesResponse getResponse(int resvId, String orderNo, List<Deliveries> deliveries, int index, int grpId, int bsize, int lineNo, String itemCode, int orderQty, Date reqDate,String window,int slots,int region,String custCity,String custArea,String custName,String custAddr,String custMob,String custLat, String custLng) throws SQLException;
}
