package org.logicInfo.oms.transferCreation.Controller;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TsfCreationDAO {
	TransferCreationResponse getResponse(int src_id, int dest_id, String refNo, String[] itemArr, int[] qtyArr) throws SQLException;

	ArrayList<Items> findInvalidItem(int sourceId, int destId, ArrayList<TransferRequest> customerItems) throws Exception;

	ArrayList<Items> findStockPerLoc(int loc, ArrayList<TransferRequest> customerItems) throws Exception;

	ArrayList<Items> findIncorrectItem(int loc, int destId, ArrayList<TransferRequest> customerItems) throws Exception;

	String transferExists(String refNo) throws Exception;

	void saveRequestandResponse(TransferCreationRequest sub, TransferCreationResponse resp);

}
