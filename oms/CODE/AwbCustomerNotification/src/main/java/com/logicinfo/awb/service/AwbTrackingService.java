package com.logicinfo.awb.service;

import com.logicinfo.awb.beans.AwbRequest;
import com.logicinfo.awb.beans.AwbResponseStatus;

public interface AwbTrackingService {
	AwbResponseStatus addAwb(AwbRequest  awbrequest);

}
