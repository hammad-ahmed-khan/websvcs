package org.logicinfo.oms.slotBookingAvailability.controller;

import java.math.BigDecimal;
import java.util.Date;

public class GroupTemp {
	
	@Override
	public String toString() {
		return "GroupTemp [groupId=" + groupId + ", windowsDate=" + windowsDate + "]";
	}
	private BigDecimal groupId;
	public BigDecimal getGroupId() {
		return groupId;
	}
	public void setGroupId(BigDecimal groupId) {
		this.groupId = groupId;
	}
	public Date getWindowsDate() {
		return windowsDate;
	}
	public void setWindowsDate(Date windowsDate) {
		this.windowsDate = windowsDate;
	}
	private Date windowsDate;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((windowsDate == null) ? 0 : windowsDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupTemp other = (GroupTemp) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (windowsDate == null) {
			if (other.windowsDate != null)
				return false;
		} else if (!windowsDate.equals(other.windowsDate))
			return false;
		return true;
	}
}
