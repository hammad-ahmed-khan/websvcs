package com.order.notification.mail.bean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

@SerializedName("campaignType")
@Expose
private String campaignType;

public String getCampaignType() {
return campaignType;
}

public void setCampaignType(String campaignType) {
this.campaignType = campaignType;
}

}