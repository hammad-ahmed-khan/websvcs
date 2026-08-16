package org.logicinfo.stockfeed.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.logicinfo.stockfeed.model.StockFeedRequestModel;
import org.logicinfo.stockfeed.model.StockFeedResponseModel;
import org.logicinfo.stockfeed.service.StockFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockFeedController {

	private static final Logger logger = LogManager.getLogger(StockFeedController.class.getName());

	@Autowired
	private StockFeedService stockFeedService;

	@PostMapping(value = "/physicaldeltafeed")
	public ResponseEntity<StockFeedResponseModel> getPhysicalStockDeltaFeedResponse(@RequestBody StockFeedRequestModel request) throws Exception {
		logger.info(" Inside getPhysicalStockDeltaFeedResponse method " + request.getId());
		StockFeedResponseModel response = null;
		response = validatePhysicalStockDeltaFeedRequest(request);
		if (response.getStatus() == null) {
			response = stockFeedService.getDeltaStockFeedService(request);
			stockFeedService.persistReqRespObjectService(request, response, "PhysicalDeltaFeed-".concat(request.getLocType()));

		} else {
			stockFeedService.persistReqRespObjectService(request, response, "PhysicalDeltaFeed-".concat(request.getLocType()));
			return new ResponseEntity<StockFeedResponseModel>(response, HttpStatus.OK);

		}
		return new ResponseEntity<StockFeedResponseModel>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/futureinventoryfeed")
	public ResponseEntity<StockFeedResponseModel> getFutureInventoryFeedResponse(@RequestBody StockFeedRequestModel request) throws Exception {
		logger.info(" Inside getFutureInventoryFeedResponse method for Id " + request.getId());
		StockFeedResponseModel response = null;
		response = validateFutureInventoryFeedRequest(request);

		if (response.getStatus() == null) {
			response = stockFeedService.geFutureInventoryStockFeedService(request);
			stockFeedService.persistReqRespObjectService(request, response, "FutureInventoryFeed");

		} else {
			stockFeedService.persistReqRespObjectService(request, response, "FutureInventoryFeed");
			return new ResponseEntity<StockFeedResponseModel>(response, HttpStatus.OK);

		}
		return new ResponseEntity<StockFeedResponseModel>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/packitemfeed")
	public StockFeedResponseModel getPackItemFeedData(@RequestBody StockFeedRequestModel stockFeedReq) {
		return stockFeedService.getPackItemFeedData(stockFeedReq);
	}

	public StockFeedResponseModel validatePhysicalStockDeltaFeedRequest(StockFeedRequestModel request) {
		logger.info(" Inside validatePhysicalStockDeltaFeedRequest method ");
		StockFeedResponseModel response = new StockFeedResponseModel();
		if (request == null) {
			response.setStatus("Failure");
			response.setError("Received request is null/empty");

		}
		if (request.getPhysicalStockInd() == null || request.getPhysicalStockInd().equalsIgnoreCase("N")) {
			response.setStatus("Failure");
			response.setError("Received request physical stock indicator is null/empty or should be Y for physical delta stockfeed");

		}
		if (request.getLocType() == null) {
			response.setStatus("Failure");
			response.setError("Request does not contain locType  for physical delta stockfeed");

		}
		if (request.getLocType() != null) {
			if (!("WH").equals(request.getLocType()) && !("ST").equals(request.getLocType())) {
				logger.info("locType " + request.getLocType());
				response.setStatus("Failure");
				response.setError("Received request loctype should be either WH or ST for physical delta stockfeed");

			}
		}
		return response;
	}

	public StockFeedResponseModel validateFutureInventoryFeedRequest(StockFeedRequestModel request) {
		logger.info(" Inside validateFutureInventoryFeedRequest method ");
		StockFeedResponseModel response = new StockFeedResponseModel();
		if (request == null) {
			response.setStatus("Failure");
			response.setError("Received request is null");

		}
		if (request.getPhysicalStockInd() == null || request.getPhysicalStockInd().equalsIgnoreCase("Y")) {
			response.setStatus("Failure");
			response.setError("Received request physical stock indicator is null or should be N for FutureInventory stockfeed");

		}

		return response;
	}
}
