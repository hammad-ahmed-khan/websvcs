package com.extra.oms.core.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.extra.oms.common.BaseException;
import com.extra.oms.common.ErrorMessage;
import com.extra.oms.core.bean.ItemFulfilment;
import com.extra.oms.core.bean.UserInfo;
import com.extra.oms.core.service.FulfilmentService;

/**
 * @author aibrahim
 *
 */
@RestController()
@RequestMapping(path = "/bulkfulfilment")
public class BulkFulfilmentController extends BaseController {

	@Autowired
	private FulfilmentService fulfilmentService;

	private static final Logger _LOG = Logger.getLogger(BulkFulfilmentController.class);

	private Map<Long, Map.Entry<BlockingQueue<ItemFulfilment>, AtomicInteger>> bulkProcessQueue = new HashMap<Long, Map.Entry<BlockingQueue<ItemFulfilment>, AtomicInteger>>();

	@PostMapping
	public List<ItemFulfilment> validateFulfilmentRequest(@RequestParam(name = "fulfilmentFile") MultipartFile fulfilReqFile) throws BaseException {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(fulfilReqFile.getInputStream());
		} catch (Exception e) {
			_LOG.error("Error while processing the bulk fulfilments", e);
			throw new BaseException();
		}
		return fulfilmentService.getFulfilments(workbook);
	}

	@PutMapping
	public void processFulfilments(@RequestBody final List<ItemFulfilment> fulfilments, @RequestParam(required = false, defaultValue = "false") final boolean cancel, @RequestAttribute(name = "user") final UserInfo user) throws BaseException {
		_LOG.info("Processing the bulk fulfilment initiated by user " + user.getUserName());
		_LOG.info("Number of records to process " + fulfilments.size());
		if (fulfilments == null || fulfilments.isEmpty()) {
			throw new BaseException("EMPTY_LIST_FULFILMENT");
		}
		if (bulkProcessQueue.containsKey(user.getId())) {
			throw new BaseException("BULK_FULFILMENT_IN_PROCESS");
		}
		bulkProcessQueue.put(user.getId(), new SimpleEntry<BlockingQueue<ItemFulfilment>, AtomicInteger>(new ArrayBlockingQueue<ItemFulfilment>(fulfilments.size()), new AtomicInteger(fulfilments.size())));
		new Thread() {

			@Override
			public void run() {
				for (ItemFulfilment fulfilment : fulfilments) {
					try {
						if (cancel) {
							fulfilment = fulfilmentService.cancelFulfilmentReq(fulfilment, user);
						} else {
							fulfilment = fulfilmentService.createWithCancelFulfilment(fulfilment, true, user);
						}
					} catch (Exception e) {
						_LOG.error(String.format("Error while processing the fulfilment, fulfilment order no %s, item no# %s, line no# %s, oms ord no# %s", fulfilment.getFulfilOrdNo(), fulfilment.getItem(), fulfilment.getLineNo(), fulfilment.getOrderInfo().getOmsOrderNumber()), e);
						fulfilment.setRemarks((e instanceof BaseException && EnumUtils.isValidEnum(ErrorMessage.class, ((BaseException)e).getCode())) ? ErrorMessage.valueOf(((BaseException)e).getCode()).getMessage() : e.getMessage());
						fulfilment.setValid(false);
					} finally {
						bulkProcessQueue.get(user.getId()).getKey().add(fulfilment);
					}
				}
			}
		}.start();
	}

	@GetMapping
	public ItemFulfilment getFulfilmentStatus(@RequestAttribute(name = "user") final UserInfo user) {
		ItemFulfilment fulfilment = null;
		if (bulkProcessQueue.containsKey(user.getId())) {
			Entry<BlockingQueue<ItemFulfilment>, AtomicInteger> entry = bulkProcessQueue.get(user.getId());
			try {
				fulfilment = entry.getKey().take();
				entry.getValue().getAndDecrement();
				if (entry.getValue().intValue() < 1) {
					bulkProcessQueue.remove(user.getId());
				}
			} catch (InterruptedException e) {
				_LOG.error("Error while retriving the fulfilemnt status", e);
				bulkProcessQueue.remove(user.getId());
			}
		}
		return fulfilment;
	}

	@GetMapping("/template")
	public Resource getTemplate() throws BaseException {
		Workbook wb = fulfilmentService.getBulkFulfilTemplate();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			wb.write(stream);
			Resource resouce = new ByteArrayResource(stream.toByteArray());
			return resouce;
		} catch (IOException e) {
			_LOG.error("Error while downloading the template", e);
			throw new BaseException(e);
		} finally {
			try {
				stream.close();
			} catch (Exception e) {
			}
			try {
				wb.close();
			} catch (Exception e) {
			}
		}
	}
}
