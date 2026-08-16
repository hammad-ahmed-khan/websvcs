package org.logicinfo.oms.slotBookingAvailability.controller;

public class Windows {
		String code;
		String fromTime;
		String toTime;
	    boolean available;

	    
	    public void setCode(String code) {
	        this.code = code;
	    }

	    public String getCode() {
	        return code;
	    }
	    
	    public void setFromTime(String fromTime) {
	        this.fromTime = fromTime;
	    }

	    public String getFromTime() {
	        return fromTime;
	    }
	    
	    public void setToTime(String toTime) {
	        this.toTime = toTime;
	    }

	    public String getToTime() {
	        return toTime;
	    }
	    
	    public void setAvailable(boolean available) {
	        this.available = available;
	    }

	    public boolean isAvailable() {
	        return available;
	    }

}	
