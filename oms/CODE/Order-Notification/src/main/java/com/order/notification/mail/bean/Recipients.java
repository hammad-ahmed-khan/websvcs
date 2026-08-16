package com.order.notification.mail.bean;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Recipients {

@SerializedName("to")
@Expose
private List<To> to = null;
@SerializedName("bcc")
@Expose
private List<String> bcc = null;

public List<To> getTo() {
return to;
}

public void setTo(List<To> to) {
this.to = to;
}

public List<String> getBcc() {
return bcc;
}

public void setBcc(List<String> bcc) {
this.bcc = bcc;
}

}