package org.logicInfo.oms.transferCreation.Controller;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public interface TsfCreationDAO {
	TransferCreationResponse getResponse(int src_id, int dest_id, String refNo, String[] itemArr, int[] qtyArr,
			String item, int qty,int src_loc , int ful_loc , int ful_ord_no , String cust_ord_no ) throws SQLException;
}
