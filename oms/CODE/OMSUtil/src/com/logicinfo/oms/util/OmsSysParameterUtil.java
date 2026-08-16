package com.logicinfo.oms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OmsSysParameterUtil.java
 * aibrahim
 * 2024
 */
public class OmsSysParameterUtil {

	private static OmsSysParamBean _INSTANCE;

	public static OmsSysParamBean initialize(List<String> paramKeys) {
		_INSTANCE = new OmsSysParamBean();
		_INSTANCE.init(paramKeys);
		return _INSTANCE;
	}

	public static OmsSysParamBean reload(List<String> paramKeys) {
		_INSTANCE.init(paramKeys);
		return _INSTANCE;
	}

	public static String getValue(String paramId, String paramKey) {
		return _INSTANCE.getValue(paramId + "~" + paramKey);
	}

	public static class OmsSysParamBean {

		private Map<String, String> sysParams;
	
		private String getValue(String key) {
			return sysParams.get(key);
		}
	
		private void init(List<String> paramKeys) {
			sysParams = new HashMap<String, String>();
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			try {
				connection = OMSUtil.getDBConnection(OMSConstants.DS_OMS_STRING);
				StringBuilder query = new StringBuilder("SELECT PARAMETER_ID, PARAMETER_NAME, PARAMETER_VALUE FROM OMS_SYSTEM_PARAMETERS WHERE PARAMETER_ID IN (");
				for (int i = 1; i <= paramKeys.size(); i++) {
					query.append("?");
					if (i != paramKeys.size()) {
						query.append(", ");
					}
				}
				query.append(")");
				statement = connection.prepareStatement(query.toString());
				int lpCnt = 1;
				for (String paramKey : paramKeys) {				
					statement.setString(lpCnt++, paramKey);
				}
				rs = statement.executeQuery();
				while (rs.next()) {
					sysParams.put(rs.getString(1) + "~" + rs.getString(2), rs.getString(3));
				}
			} catch (Exception e) {
				
			} finally {
				OMSUtil.closeDBConnection(connection, statement, rs);
			}
		}
	}
}
