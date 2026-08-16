package extra.retail.sim.server.dataaccess;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import oracle.jdbc.driver.OracleConnection;
import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.server.dataaccess.DatabaseNull;
import oracle.retail.sim.server.dataaccess.DatabaseOutParameter;
import oracle.retail.sim.server.dataaccess.SimBlob;
import oracle.retail.sim.server.dataaccess.SimClob;

import weblogic.jdbc.extensions.WLConnection;

/**
 * ExtraDatabaseUtility.java
 * aibrahim
 * 2023
 */
public class ExtraDatabaseUtility {

	public static void setCallableParameters(CallableStatement paramCallableStatement, List<?> parameters) throws SQLException {
		if (parameters == null || parameters.isEmpty()) {
			return;
		}
		for (byte b = 0; b < parameters.size(); b++) {
			setCallableParameter(paramCallableStatement, b, parameters.get(b));
		}
	}

	public static void setCallableParameter(CallableStatement paramCallableStatement, int paramInt, Object paramObject) throws SQLException {
		if (paramObject instanceof DatabaseOutParameter) {
			paramCallableStatement.registerOutParameter(paramInt + 1, ((DatabaseOutParameter) paramObject).getTypeCode());
		} else {
			setParameter(paramCallableStatement, paramInt, paramObject);
		}
	}

	@SuppressWarnings("unchecked")
	public static void setParameter(PreparedStatement paramPreparedStatement, int paramInt, Object paramObject) throws SQLException {
		if (paramObject == null) {
			paramPreparedStatement.setNull(paramInt + 1, 12);
		} else if (paramObject instanceof DatabaseNull) {
			paramPreparedStatement.setNull(paramInt + 1, ((DatabaseNull) paramObject).getTypeCode());
		} else if (paramObject instanceof String) {
			paramPreparedStatement.setString(paramInt + 1, (String) paramObject);
		} else if (paramObject instanceof Boolean) {
			paramPreparedStatement.setString(paramInt + 1, StringHelper.booleanToYNString(((Boolean) paramObject).booleanValue()));
		} else if (paramObject instanceof Integer) {
			paramPreparedStatement.setInt(paramInt + 1, ((Integer) paramObject).intValue());
		} else if (paramObject instanceof Long) {
			paramPreparedStatement.setLong(paramInt + 1, ((Long) paramObject).longValue());
		} else if (paramObject instanceof Double) {
			paramPreparedStatement.setDouble(paramInt + 1, ((Double) paramObject).doubleValue());
		} else if (paramObject instanceof Date) {
			paramPreparedStatement.setTimestamp(paramInt + 1, new Timestamp(((Date) paramObject).getTime()), SimDateUtil.getGMTCalendar());
		} else if (paramObject instanceof SimEnum) {
			setParameter(paramPreparedStatement, paramInt, ((SimEnum) paramObject).getCode());
		} else if (paramObject instanceof SimClob) {
			Clob clob = paramPreparedStatement.getConnection().createClob();
			clob.setString(1L, ((SimClob) paramObject).getData());
			setParameter(paramPreparedStatement, paramInt, clob);
		} else if (paramObject instanceof SimBlob) {
			Blob blob = paramPreparedStatement.getConnection().createBlob();
			blob.setBytes(1L, ((SimBlob) paramObject).getData());
			setParameter(paramPreparedStatement, paramInt, blob);
		} else if (paramObject instanceof ExtraSimArray) {
			paramPreparedStatement.setArray(paramInt + 1, getArray(paramPreparedStatement, (ExtraSimArray) paramObject));
		} else {
			paramPreparedStatement.setObject(paramInt + 1, paramObject);
		}
	}

	private static Array getArray(PreparedStatement paramPreparedStatement, ExtraSimArray paramObject) throws SQLException {
		
		List<Map<Integer, Object>> list = paramObject.getList();
		Object[][] arrayObj = new Object[list.size()][paramObject.getArgumentLength()];
		for (int i = 0;i < list.size();i++) {
			for (Entry<Integer, Object> entry : list.get(i).entrySet()) {
				arrayObj[i][entry.getKey()] = entry.getValue();
			}
		}
		return ((OracleConnection)((WLConnection)paramPreparedStatement.getConnection()).getVendorConnection()).createOracleArray(paramObject.getTypeName(), arrayObj);
	}
}
