package org.logicinfo.stockfeed.dao;

import org.logicinfo.stockfeed.model.StockFeedRequestModel;
import org.logicinfo.stockfeed.model.StockFeedResponseModel;

public interface StockFeedDao {

	public StockFeedResponseModel getPhysicalStockDeltaFeedDaoImpl(StockFeedRequestModel request);

	public StockFeedResponseModel getFutureInventoryStockFeedDaoImpl(StockFeedRequestModel request);

	public int persistReqRespObject(StockFeedRequestModel request, StockFeedResponseModel response, String requestType);

	StockFeedResponseModel getPackItemFeedData(StockFeedRequestModel stockFeedReq);
}
