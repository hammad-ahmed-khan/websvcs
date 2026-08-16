package extra.retail.sim.server.dataaccess;

import oracle.retail.sim.server.dataaccess.BaseStoredProcedureBean;
import oracle.retail.sim.server.dataaccess.StoredProcedureStatement;

/**
 * aibrahim
 * 2023
 */
public class ExtraBaseStoredProcedureBean<T> extends BaseStoredProcedureBean<T> {

	protected ExtraBaseStoredProcedureBean(String procedureName, boolean returnParameter, int argumentSize) {
		super(procedureName, returnParameter, argumentSize);
	}

	public StoredProcedureStatement buildStatement() {
		return new ExtraStoredProcedureStatement(getSql(), getParameters());
	}
}
