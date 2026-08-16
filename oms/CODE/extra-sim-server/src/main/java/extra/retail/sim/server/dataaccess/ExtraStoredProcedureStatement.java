package extra.retail.sim.server.dataaccess;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.server.dataaccess.DatabaseUtility;
import oracle.retail.sim.server.dataaccess.StoredProcedureStatement;

/**
 * aibrahim
 * 2023
 */
public class ExtraStoredProcedureStatement extends StoredProcedureStatement {

	private List<?> parameters;

	public ExtraStoredProcedureStatement(String procedureName) {
		this(procedureName, null);
	}

	public ExtraStoredProcedureStatement(String procedureName, List<?> paramList) {
		super(procedureName);
		this.parameters = (paramList != null) ? paramList : new ArrayList<>();
	}

	public ExtraStoredProcedureStatement(String procedureName, boolean returnParameter, List<?> paramList) {
		this(DatabaseUtility.buildCallStoredProcedure(procedureName, returnParameter, (paramList != null) ? (returnParameter ? (paramList.size() - 1) : paramList.size()) : 0), paramList);
	}

	public void setupStatement(CallableStatement paramCallableStatement) throws SQLException {
		ExtraDatabaseUtility.setCallableParameters(paramCallableStatement, this.parameters);
	}

	public String toString() {
		if (this.sql == null) {
			return "[NULL SQL stored procedure String]";
		}
		StringBuilder stringBuilder = new StringBuilder(this.sql);
		for (Object object : this.parameters) {
			if (object instanceof SimEnum) {
				object = ((SimEnum) object).getCode();
			}
			stringBuilder.append("[").append(object).append("]");
		}
		return stringBuilder.toString();
	}
}
