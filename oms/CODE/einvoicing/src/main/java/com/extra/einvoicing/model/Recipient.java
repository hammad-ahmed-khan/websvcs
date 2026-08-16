package com.extra.einvoicing.model;

import java.util.List;

public class Recipient {

	private List<To> to;

	private List<To> cc;

	public List<To> getTo() {
		return to;
	}

	public void setTo(List<To> to) {
		this.to = to;
	}

	public List<To> getCc() {
		return cc;
	}

	public void setCc(List<To> cc) {
		this.cc = cc;
	}
}
