package org.logicinfo.hybriscancellation.controller;

import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.logicinfo.hybriscancellation.model.HybrisCancellationMainModel;
import org.logicinfo.hybriscancellation.model.HybrisCancellationResModel;
import org.logicinfo.hybriscancellation.serviceImpl.HybrisCancellationServImpl;
import org.logicinfo.hybriscancellation.utill.UtillConstantsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HybrisCancellationController {
	private static final Logger _LOGGER = LogManager.getLogger(HybrisCancellationController.class.getName());
	@Autowired
	private HybrisCancellationServImpl hybrisService;

	@GetMapping("/ping")
	public ResponseEntity<String> getServiceResponse() {
		_LOGGER.info(" Hybris Cancellation Web service is up and running ");
		String res = "Hybris Cancellation Web service is up and running";

		return new ResponseEntity<String>(res, HttpStatus.OK);
	}

	@PostMapping(value = "/createCancellation")
	public ResponseEntity<HybrisCancellationResModel> getLabelCreationResponse(@RequestBody HybrisCancellationMainModel request) throws SQLException {
		HybrisCancellationResModel res = null, CheckRefundValueRes = null, mainRes = null;

		try {
			if ("Y".equals(request.getMarketPlaceOrder()) || request.getTransactionType().equals("RefundOnly")) {
				hybrisService.insertRequest(request);
				hybrisService.getSeqIdDetails(request);
				res = hybrisService.insertDetailInMuleTableProcedure(request);
				if (UtillConstantsCode.succesCode.equals(res.getCode())) {
					res.setCode(UtillConstantsCode.succesCode);
					res.setStatus(UtillConstantsCode.success);
					res.setMessage(UtillConstantsCode.successMpMessage);
				}
				hybrisService.insertResponse(res, request.getOrderCode());
			} else {
				_LOGGER.info("Start: Inside Cancellation request block");
				// step 1: check the result existing or not in the hybris head table
				mainRes = hybrisService.checkCustomerOrder(request);
				CheckRefundValueRes = hybrisService.checkRefundValue(request);
				res = hybrisService.checkvalidation(request, mainRes);
				if (res.getCode().equals(UtillConstantsCode.noDataCode)) {

					// step 2: If data not there insert details in custom table
					if (request.getTransactionType().equals("CancelAndRefund")) {
						long cancelId = hybrisService.generateOmsCancelId();

						// if oms cancellation id generated successfully
						if (cancelId <= 0) {
							// error while generate oms cancel id
							res.setCode(UtillConstantsCode.OmsCustomTablePresistErrorCode);
							res.setStatus(UtillConstantsCode.failedMsg);
							res.setMessage(UtillConstantsCode.oms_Cancel_Id_Error);
							return new ResponseEntity<HybrisCancellationResModel>(res, HttpStatus.OK);
						}

						request.setOmsCancelId(cancelId);

					}

					res = hybrisService.insertRequestInHybrisTables(request);

					// if custom table insertion done successfully proceed next
					if (res.getCode().equals(UtillConstantsCode.succesCode)) {
						if (CheckRefundValueRes != null && CheckRefundValueRes.getCode().equals(UtillConstantsCode.tenderfailedCode)) {
							hybrisService.updateHybrisHeadTableErrorMessage(request, CheckRefundValueRes);
							_LOGGER.info("End:Cancellation block");
							return new ResponseEntity<HybrisCancellationResModel>(CheckRefundValueRes, HttpStatus.OK);
						}

						// step 4 : proceed next step and refund process
						if (request.getTransactionType().equals("RefundOnly") && (request.getRefunds().get(0).getRefundable().equals("Y") || request.getRefunds().get(0).getRefundable().equals("N"))) {

							if (CheckRefundValueRes != null && CheckRefundValueRes.getCode().equals(UtillConstantsCode.tenderSuccessCode)) {
								hybrisService.updateHybrisHeadTableErrorMessage(request, mainRes);
								_LOGGER.info("End:Cancellation block");
								mainRes.setCode(UtillConstantsCode.succesCode);
								mainRes.setStatus(UtillConstantsCode.successMsg);
								mainRes.setMessage(UtillConstantsCode.refundsuccessMsg);
								return new ResponseEntity<HybrisCancellationResModel>(mainRes, HttpStatus.OK);
							} 
						}

						// step 5: proceed cancellation process

						else if (request.getTransactionType().equals("CancelAndRefund")
								&& (request.getRefunds().get(0).getRefundable().equals("N") || request.getRefunds().get(0).getRefundable().equals("Y"))) {

							// step 6 : check the refund amount value

							if (CheckRefundValueRes != null && CheckRefundValueRes.getCode().equals(UtillConstantsCode.tenderSuccessCode)) {

								// step 7: calling coCancellation base web service
								res = hybrisService.processingCencellation(request, mainRes);

								hybrisService.updateHybrisHeadTableErrorMessage(request, res);
								_LOGGER.info("End:Cancellation block");
								return new ResponseEntity<HybrisCancellationResModel>(res, HttpStatus.OK);

							}
							// tender amt incorrect
							else {
								hybrisService.updateHybrisHeadTableErrorMessage(request, res);
								_LOGGER.info("End:Cancellation block");
								return new ResponseEntity<HybrisCancellationResModel>(res, HttpStatus.OK);
							}
						}

						// wrong TransactionType and Refund indicator values
						else {

							res.setMessage(UtillConstantsCode.transactionNoError);
							res.setCode(UtillConstantsCode.invaildInput);
							res.setStatus(UtillConstantsCode.failedMsg);
						}
					} else {
						// if custom table data not inserted correctly
						hybrisService.updateHybrisHeadTableErrorMessage(request, res);
						_LOGGER.info("End:Cancellation block");
						return new ResponseEntity<HybrisCancellationResModel>(res, HttpStatus.OK);
					}

				} else {

					// if data already there send the existing hybris head table
					// result to mule
					hybrisService.updateHybrisHeadTableErrorMessage(request, res);
					_LOGGER.info("End:Cancellation block");
					return new ResponseEntity<HybrisCancellationResModel>(res, HttpStatus.OK);
				}

			}
		} catch (Exception e) {
			_LOGGER.info("Unexpected error" + e);
			_LOGGER.error("Error:", e);
		}
		// if any exception occur
		hybrisService.updateHybrisHeadTableErrorMessage(request, res);
		_LOGGER.info("End:Cancellation block");
		return new ResponseEntity<HybrisCancellationResModel>(res, HttpStatus.OK);
	}

}
