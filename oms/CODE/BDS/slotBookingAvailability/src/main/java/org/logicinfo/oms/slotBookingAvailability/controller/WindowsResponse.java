package org.logicinfo.oms.slotBookingAvailability.controller;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WindowsResponse extends Windows{
	public WindowsResponse(){
		super();
	}
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date dates;

	public Date getDates() {
		return dates;
	}

	public void setDates(Date dates) {
		this.dates = dates;
	}
	
	
}
