package com.logicinfo.awb.serviceImpl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.stereotype.Service;
import com.logicinfo.awb.beans.AwbRequest;
import com.logicinfo.awb.beans.AwbResponseStatus;
import com.logicinfo.awb.dao.AwbTrackingDAO;
import com.logicinfo.awb.service.AwbTrackingService;

@Service
public class AwbTrackingServiceImpl implements AwbTrackingService {
	private static final Logger _LOGGER = LogManager.getLogger(AwbTrackingService.class.getName());

	@Autowired
	private AwbTrackingDAO awbtrackingdao;

	@Override
	public AwbResponseStatus addAwb(AwbRequest awbrequest) {

		_LOGGER.info(" The serivce call is " + awbrequest.getTrackingId() + "source is" + awbrequest.getSource()
				+ "courier name is" + awbrequest.getCourier());
		
		
		Boolean result = awbtrackingdao.validateInputRequest(awbrequest);
		
		if (result ==Boolean.TRUE) {
			
			return  new AwbResponseStatus(Boolean.FALSE, 
					"Already Record Exists with same input Request with sucessfull Status "+"Tracking Id "
					+awbrequest.getTrackingId()+ " courier Name"+awbrequest.getCourier()+"  Source \t "+awbrequest.getSource());
		}
		
		
		
		
		AwbResponseStatus serivceLayerCallStatus = awbtrackingdao.addAwb(awbrequest);

		_LOGGER.info(" End of the Service call is  for " + awbrequest.getTrackingId() + "source is"
				+ awbrequest.getSource() + "courier name is" + awbrequest.getCourier());
		return serivceLayerCallStatus;

	}

}
