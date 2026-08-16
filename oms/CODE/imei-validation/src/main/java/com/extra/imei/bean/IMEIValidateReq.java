/**
 * 
 */
package com.extra.imei.bean;

/**
 * @author aibrahim
 *
 */
public class IMEIValidateReq {

	private String item;

	private String imei;

	private String source;

	private Long location;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getLocation() {
		return location;
	}

	public void setLocation(Long location) {
		this.location = location;
	}
}
