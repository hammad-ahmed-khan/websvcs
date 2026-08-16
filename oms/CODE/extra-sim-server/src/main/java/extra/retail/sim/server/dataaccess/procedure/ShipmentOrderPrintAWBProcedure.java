package extra.retail.sim.server.dataaccess.procedure;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.retail.sim.server.dataaccess.BaseStoredProcedureBean;

/**
 * ShipmentOrderPrintAWBProcedure.java
 * aibrahim
 * 2023
 */
public class ShipmentOrderPrintAWBProcedure extends BaseStoredProcedureBean<ShipmentOrderPrintAWBProcedure> {

	public static final String PROCEDURE_NAME = "XX_SIM_AWB_SQL.PRINT_AWB";

	public static final boolean RETURN_PARAMETER = false;

	public static final int ARGUMENT_SIZE = 3;

	private String trackingNumber;

	private String status;

	private String errorMessage;

	public ShipmentOrderPrintAWBProcedure() {
		super(PROCEDURE_NAME, RETURN_PARAMETER, ARGUMENT_SIZE);
	}

	@Override
	public ShipmentOrderPrintAWBProcedure read(CallableStatement paramCallableStatement) throws SQLException {
		this.status = getString(paramCallableStatement, 2);
		this.errorMessage = getString(paramCallableStatement, 3);
		return this;
	}

	@Override
	public List<Object> getParameters() {
		List<Object> arrayList = new ArrayList<>();
		addParameter(arrayList, trackingNumber, Types.VARCHAR);
	    addOutParameter(arrayList, Types.VARCHAR);
	    addOutParameter(arrayList, Types.VARCHAR);
	    return arrayList;
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

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
}
