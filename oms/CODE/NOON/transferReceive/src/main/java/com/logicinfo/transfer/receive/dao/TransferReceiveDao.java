package com.logicinfo.transfer.receive.dao;

import java.util.ArrayList;

import com.logicinfo.transfer.receive.model.Items;
import com.logicinfo.transfer.receive.model.Response;
import com.logicinfo.transfer.receive.model.TsfReceiveRequest;
import com.logicinfo.transfer.receive.model.TsfReceiveResponse;

public interface TransferReceiveDao {

	public Response tsfReceive(TsfReceiveRequest request) throws Exception;

	public String checktransferNoExist(String poNumber);

	public ArrayList<Items> checkInvalidItems(TsfReceiveRequest request);

	public ArrayList<Items> checkInvalidQty(TsfReceiveRequest request);

	ArrayList<Items> checkReceivedQty(TsfReceiveRequest request);

	public int saveRequestandResponse(TsfReceiveRequest transferReceiveRequest, TsfReceiveResponse transferReceiveResponse);

	void lockReceiveRequest(TsfReceiveRequest request) throws Exception;

	void removeReceiveRequestLock(TsfReceiveRequest request);
}
