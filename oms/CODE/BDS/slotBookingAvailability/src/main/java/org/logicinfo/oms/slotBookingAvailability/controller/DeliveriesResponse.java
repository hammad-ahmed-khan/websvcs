package org.logicinfo.oms.slotBookingAvailability.controller;

import java.util.List;

public class DeliveriesResponse {
	
	    int deliveryId;
	    private List<Groups> groups;
		
	    public DeliveriesResponse() {
	        super();
	    }
	    
	    public List<Groups> getGroups() {
			return groups;
		}

		public void setGroups(List<Groups> groups) {
			this.groups = groups;
		}

		

		public void setDeliveryId(int deliveryId) {
	        this.deliveryId = deliveryId;
	    }

	    public int getDeliveryId() {
	        return deliveryId;
	    }
}
