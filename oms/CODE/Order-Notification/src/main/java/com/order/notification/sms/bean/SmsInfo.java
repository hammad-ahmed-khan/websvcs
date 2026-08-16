package com.order.notification.sms.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmsInfo {

@SerializedName("smsData")
@Expose
private SmsData smsData;

private transient String OrderNumber;

@SerializedName("metadata")
@Expose
private MetaData metadata;

public SmsData getSmsData() {
return smsData;
}

public void setSmsData(SmsData smsData) {
this.smsData = smsData;
}

public String getOrderNumber() {
	return OrderNumber;
}

public void setOrderNumber(String orderNumber) {
	OrderNumber = orderNumber;
}

public MetaData getMetadata() {
	return metadata;
}

public void setMetadata(MetaData metadata) {
	this.metadata = metadata;
}


}
