package com.logicinfo.transfer.receive.service;

import java.util.ArrayList;

import com.logicinfo.transfer.receive.model.Items;
import com.logicinfo.transfer.receive.model.Response;
import com.logicinfo.transfer.receive.model.TsfReceiveRequest;
import com.logicinfo.transfer.receive.model.TsfReceiveResponse;

public interface TransferReceiveService {

	public Response tsfReceive(TsfReceiveRequest request) throws Exception;

	public String checktransferNoExist(String poNumber) throws Exception;

	public ArrayList<Items> checkInvalidItems(TsfReceiveRequest request) throws Exception;

	public ArrayList<Items> checkInvalidQty(TsfReceiveRequest request) throws Exception;

	public ArrayList<Items> checkReceivedQty(TsfReceiveRequest request);

	public int saveRequestandResponse(TsfReceiveRequest request, TsfReceiveResponse response);

	public void lockReceiveRequest(TsfReceiveRequest request) throws Exception;

	void removeReceiveRequestLock(TsfReceiveRequest request);
}
