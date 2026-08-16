package com.extra.notification.model;

/**
 * @author aibrahim
 *
 */
public class SMSInfo {

	private SMS smsData;

	private MetaInfo metadata;

	public SMS getSmsData() {
		return smsData;
	}

	public void setSmsData(SMS smsData) {
		this.smsData = smsData;
	}

	public MetaInfo getMetadata() {
		return metadata;
	}

	public void setMetadata(MetaInfo metadata) {
		this.metadata = metadata;
	}
}
