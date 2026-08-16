package com.extra.oms.service.client;

import com.extra.oms.bean.AddressInfo;
import com.extra.oms.bean.AddressResponse;
import feign.Headers;
import feign.RequestLine;

public interface IMuleAddressClient {

	@RequestLine("POST /api/getAddressByGeocode")
	@Headers({ "Content-Type: application/json" })
	AddressResponse getAddressByGeocode(AddressInfo paramAddressInfo);

	@RequestLine("POST /api/getAddressByShortAddress")
	@Headers({ "Content-Type: application/json" })
	AddressResponse getAddressByShortAddress(AddressInfo paramAddressInfo);
}
