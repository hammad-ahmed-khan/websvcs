package com.order.notification.sms.bean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmsData {

@SerializedName("toNumber")
@Expose
private String toNumber;
@SerializedName("fromNumber")
@Expose
private String fromNumber;
@SerializedName("body")
@Expose
private String body;

public String getToNumber() {
return toNumber;
}

public void setToNumber(String toNumber) {
this.toNumber = toNumber;
}

public String getFromNumber() {
return fromNumber;
}

public void setFromNumber(String fromNumber) {
this.fromNumber = fromNumber;
}

public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}

}