package org.logicinfo.stockfeed.service;

import org.logicinfo.stockfeed.model.StockFeedRequestModel;
import org.logicinfo.stockfeed.model.StockFeedResponseModel;

public interface StockFeedService {

	StockFeedResponseModel getDeltaStockFeedService(StockFeedRequestModel request);

	StockFeedResponseModel geFutureInventoryStockFeedService(StockFeedRequestModel request);

	int persistReqRespObjectService(StockFeedRequestModel request, StockFeedResponseModel response, String requestType);

	StockFeedResponseModel getPackItemFeedData(StockFeedRequestModel stockFeedReq);
}
