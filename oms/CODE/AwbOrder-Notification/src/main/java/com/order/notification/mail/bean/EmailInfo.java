package com.order.notification.mail.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmailInfo {
	
	@SerializedName("email")
	@Expose
	private Email email;
	
	private Integer OrderNumber;
	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Integer getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		OrderNumber = orderNumber;
	}



	
	
	
	

}
