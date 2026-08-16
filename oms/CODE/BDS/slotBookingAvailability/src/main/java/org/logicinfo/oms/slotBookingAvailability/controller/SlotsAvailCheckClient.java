package org.logicinfo.oms.slotBookingAvailability.controller;



import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class SlotsAvailCheckClient {

	@Autowired
	SlotsAvailCheckDAO slotsDAO; 
	
	private static final Logger log = Logger.getLogger(SlotsAvailCheckClient.class);
	  public void run() throws SQLException {
	     List<SlotsAvailabilityResponse> response = slotsDAO.getResponse(null, null, 0, 0, "");
	     log.info("Response: "+ response);
	  }
}
