package com.extra.oms.bean;

import java.util.List;

public class AddressResponse {

	private ResponseHeader responseHeader;

	private List<Address> addresses;

	private Address nationalAddress;

	public ResponseHeader getResponseHeader() {
		return this.responseHeader;
	}

	public List<Address> getAddresses() {
		return this.addresses;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
		if (addresses != null && !addresses.isEmpty()) {
			this.nationalAddress = addresses.get(0);
		}
	}

	public Address getNationalAddress() {
		return this.nationalAddress;
	}

	public void setNationalAddress(Address nationalAddress) {
		this.nationalAddress = nationalAddress;
	}
}
