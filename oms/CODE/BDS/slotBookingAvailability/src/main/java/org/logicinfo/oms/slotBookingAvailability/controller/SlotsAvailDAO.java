package org.logicinfo.oms.slotBookingAvailability.controller;

import java.sql.SQLException;
import java.util.List;

public interface SlotsAvailDAO {
	List<SlotsAvailabilityResponse> getResponse(String itemQuery, List<Deliveries> deliveries, int index, int size, String expressItemSKU) throws SQLException;

	public Long saveRequest(SlotsAvailCheckRequest sub)throws Exception;

	public void saveResponse(String OrderNumber,List<SlotsAvailabilityResponse> sAResposne, Long seqId)throws Exception;

	public String getExpressItemSKU();
}
