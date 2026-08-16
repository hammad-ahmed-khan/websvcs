package org.logicinfo.oms.slotBookingAvailability.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Dates {
	public Dates() {
        super();
    }
//	@JsonFormat(pattern="yyyy-MM-dd")
	private String date;
	private List<Windows> windows;

   
	public void setDate(Date date) {
	    if (date == null) {
	        this.date = null;
	        return;
	    }
	 
	    // Extract date parts using Calendar (no timezone conversion)
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	 
	    int year  = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH) + 1; // 0-based
	    int day   = c.get(Calendar.DAY_OF_MONTH);
	 
	    // Format manually to yyyy-MM-dd
	    this.date = String.format("%04d-%02d-%02d", year, month, day);
	}

	public String getDate() {
		return date;
	}

	public List<Windows> getWindows() {
		return windows;
	}

	public void setWindows(List<Windows> windows) {
		this.windows = windows;
	}

	
    
    
}
