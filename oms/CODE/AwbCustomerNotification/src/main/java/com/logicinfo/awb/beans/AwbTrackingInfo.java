package com.logicinfo.awb.beans;

import java.io.Serializable;

public class AwbTrackingInfo  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String source;
	private String courierName;
	private String trackingId;
	private String itemid;
	private String custOrderNo;
	private String omsLinkNo;

	public AwbTrackingInfo() {

	}
	
	public AwbTrackingInfo(String source, String courierName, String trackingId, String itemid, String custOrderNo,
			String omsLinkNo) {
		this.source = source;
		this.courierName = courierName;
		this.trackingId = trackingId;
		this.itemid = itemid;
		this.custOrderNo = custOrderNo;
		this.omsLinkNo = omsLinkNo;
	}


	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getCustOrderNo() {
		return custOrderNo;
	}

	public void setCustOrderNo(String custOrderNo) {
		this.custOrderNo = custOrderNo;
	}

	public String getOmsLinkNo() {
		return omsLinkNo;
	}

	public void setOmsLinkNo(String omsLinkNo) {
		this.omsLinkNo = omsLinkNo;
	}

	@Override
	public String toString() {
		return "AwbTrackingInfo [source=" + source + ", courierName=" + courierName + ", trackingId=" + trackingId
				+ ", itemid=" + itemid + ", custOrderNo=" + custOrderNo + ", omsLinkNo=" + omsLinkNo + "]";
	}
	
	

}
