package com.logicinfo.transfer.receive.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logicinfo.transfer.receive.dao.TransferReceiveDao;
import com.logicinfo.transfer.receive.model.Items;
import com.logicinfo.transfer.receive.model.Response;
import com.logicinfo.transfer.receive.model.TsfReceiveRequest;
import com.logicinfo.transfer.receive.model.TsfReceiveResponse;

@Service
@Transactional
public class TransferReceiveServiceImpl implements TransferReceiveService {

	@Autowired
	TransferReceiveDao tsfrcvDao;

	public Response tsfReceive(TsfReceiveRequest request) throws Exception {
		return tsfrcvDao.tsfReceive(request);
	}

	@Override
	public String checktransferNoExist(String poNumber) throws Exception {
		return tsfrcvDao.checktransferNoExist(poNumber);
	}

	@Override
	public ArrayList<Items> checkInvalidItems(TsfReceiveRequest request) throws Exception {
		return tsfrcvDao.checkInvalidItems(request);
	}

	@Override
	public ArrayList<Items> checkInvalidQty(TsfReceiveRequest request) throws Exception {
		return tsfrcvDao.checkInvalidQty(request);
	}

	@Override
	public ArrayList<Items> checkReceivedQty(TsfReceiveRequest request) {
		return tsfrcvDao.checkReceivedQty(request);
	}

	@Override
	public int saveRequestandResponse(TsfReceiveRequest request, TsfReceiveResponse response) {
		return tsfrcvDao.saveRequestandResponse(request, response);
	}

	@Override
	public void lockReceiveRequest(TsfReceiveRequest request) throws Exception {
		tsfrcvDao.lockReceiveRequest(request);
	}

	@Override
	public void removeReceiveRequestLock(TsfReceiveRequest request) {
		tsfrcvDao.removeReceiveRequestLock(request);
	}
}
