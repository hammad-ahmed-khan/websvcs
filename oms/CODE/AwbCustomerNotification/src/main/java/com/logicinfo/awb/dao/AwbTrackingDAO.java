package com.logicinfo.awb.dao;

import com.logicinfo.awb.beans.AwbRequest;
import com.logicinfo.awb.beans.AwbResponseStatus;

public interface AwbTrackingDAO {
	
	AwbResponseStatus addAwb(AwbRequest  awbrequest);
	
	Boolean validateInputRequest(AwbRequest awbrequestObj);
}
