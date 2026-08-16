package com.order.notification.sms.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmsInfo {

@SerializedName("smsData")
@Expose
private SmsData smsData;

private  Integer OrderNumber;
public SmsData getSmsData() {
return smsData;
}

public void setSmsData(SmsData smsData) {
this.smsData = smsData;
}

public Integer getOrderNumber() {
	return OrderNumber;
}

public void setOrderNumber(Integer orderNumber) {
	OrderNumber = orderNumber;
}


}
