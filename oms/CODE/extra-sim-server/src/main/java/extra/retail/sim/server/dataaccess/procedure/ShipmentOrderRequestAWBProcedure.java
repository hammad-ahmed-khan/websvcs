package extra.retail.sim.server.dataaccess.procedure;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.common.shipment.AWBRequestLineItem;
import extra.retail.sim.server.dataaccess.ExtraBaseStoredProcedureBean;
import extra.retail.sim.server.dataaccess.ExtraSimArray;

/**
 * aibrahim
 * 2023
 */
public class ShipmentOrderRequestAWBProcedure extends ExtraBaseStoredProcedureBean<ShipmentOrderRequestAWBProcedure> {

	public static final String PROCEDURE_NAME = "XX_SIM_AWB_SQL.CREATE_AWB";

	private static final String TYPE_NAME = "TYP_AWB_TBL";

	public static final boolean RETURN_PARAMETER = false;

	public static final int ARGUMENT_SIZE = 4;

	private AWBRequest awbRequest;

	private String status;

	private String errorMessage;

	private String trackingNumber;

	public ShipmentOrderRequestAWBProcedure() {
		super(PROCEDURE_NAME, RETURN_PARAMETER, ARGUMENT_SIZE);
	}

	@Override
	public ShipmentOrderRequestAWBProcedure read(CallableStatement paramCallableStatement) throws SQLException {
		this.trackingNumber = getString(paramCallableStatement, 2);
		this.status = getString(paramCallableStatement, 3);
		this.errorMessage = getString(paramCallableStatement, 4);
		return this;
	}

	@Override
	public List<Object> getParameters() {
		List<Object> arrayList = new ArrayList<>();
		addParameter(arrayList, wraptoArray(awbRequest), Types.ARRAY);
	    addOutParameter(arrayList, Types.VARCHAR);
	    addOutParameter(arrayList, Types.VARCHAR);
	    addOutParameter(arrayList, Types.VARCHAR);
	    return arrayList;
	}

	private ExtraSimArray wraptoArray(AWBRequest awbRequest) {
		List<Map<Integer, Object>> list = new ArrayList<>(awbRequest.getLineItems().size());
		Map<Integer, Object> map = null;
		for (AWBRequestLineItem lineItem  : awbRequest.getLineItems()) {
			map = new  HashMap<>();
			map.put(0, awbRequest.getStoreId());
			map.put(1, awbRequest.getPickId());
			map.put(2, awbRequest.getFulOrdId());
			map.put(3, lineItem.getDeliveryId());
			map.put(4, lineItem.getPickLineItemId());
			map.put(5, lineItem.getItem());
			map.put(6, lineItem.getQty());
			map.put(7, lineItem.getCartonNumber());
			list.add(map);
		}
		return new ExtraSimArray(TYPE_NAME, 8, list);
	}

	public AWBRequest getAwbRequest() {
		return awbRequest;
	}

	public void setAwbRequest(AWBRequest awbRequest) {
		this.awbRequest = awbRequest;
	}

	public String getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}
}
