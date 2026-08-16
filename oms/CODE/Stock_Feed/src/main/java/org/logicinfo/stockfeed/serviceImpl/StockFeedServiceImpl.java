package org.logicinfo.stockfeed.serviceImpl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.logicinfo.stockfeed.dao.StockFeedDao;
import org.logicinfo.stockfeed.model.StockFeedRequestModel;
import org.logicinfo.stockfeed.model.StockFeedResponseModel;
import org.logicinfo.stockfeed.service.StockFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockFeedServiceImpl implements StockFeedService {

	private static final Logger logger = LogManager.getLogger(StockFeedServiceImpl.class.getName());

	@Autowired
	private StockFeedDao stockFeedDao;

	@Override
	public StockFeedResponseModel getDeltaStockFeedService(StockFeedRequestModel request) {
		return stockFeedDao.getPhysicalStockDeltaFeedDaoImpl(request);
	}

	@Override
	public StockFeedResponseModel geFutureInventoryStockFeedService(StockFeedRequestModel request) {
		return stockFeedDao.getFutureInventoryStockFeedDaoImpl(request);
	}

	@Override
	public int persistReqRespObjectService(StockFeedRequestModel request, StockFeedResponseModel response, String requestType) {
		return stockFeedDao.persistReqRespObject(request, response, requestType);
	}

	@Override
	public StockFeedResponseModel getPackItemFeedData(StockFeedRequestModel stockFeedReq) {

		StockFeedResponseModel stockFeedRes = null;
		if (!"Y".equals(stockFeedReq.getPackItemInd())) {
			String message = "Pack Item Indicator should be Y for stockfeed";
			logger.warn(message);
			stockFeedRes = new StockFeedResponseModel();
			stockFeedRes.setStatus("Failure");
			stockFeedRes.setError(message);
		} else {
			stockFeedRes = stockFeedDao.getPackItemFeedData(stockFeedReq);
		}
		stockFeedDao.persistReqRespObject(stockFeedReq, stockFeedRes, "PackInventoryFeed");
		return stockFeedRes;
	}
}
