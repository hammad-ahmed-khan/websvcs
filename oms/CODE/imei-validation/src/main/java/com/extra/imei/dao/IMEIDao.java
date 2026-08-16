package com.extra.imei.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.extra.imei.bean.IMEIValidateReq;

/**
 * @author aibrahim
 *
 */
@Repository
public class IMEIDao {

	private static final Logger LOG = Logger.getLogger(IMEIDao.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public boolean validateIMEI(final IMEIValidateReq validateReq) {
		
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);

		LOG.info("Calling procedure to validate the imei number " + validateReq.getImei());
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlParameter(Types.VARCHAR));
		declaredParameters.add(new SqlOutParameter("valid", Types.VARCHAR));
		Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
			
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ CALL XX_IMEI_SN_UPLOAD_VALIDATE.IMEI_SN_UPLOAD_VALIDATE(?, ?, ?, ?, ?, ?) }" );
				stmt.setString(1, validateReq.getItem().trim());
				stmt.setString(2, validateReq.getImei().trim());
				stmt.setLong(3, validateReq.getLocation());
				stmt.setString(4, validateReq.getSource().trim());
				stmt.setString(5, "Y");
				stmt.registerOutParameter(6, Types.VARCHAR);
		        return stmt;
			}
		}, declaredParameters);
		Object result = resultMap.get("valid");
		LOG.info("Response received from the procedure for imei number " + validateReq.getImei() + " is " + result);
		return !StringUtils.isEmpty(result) && ((String) result).equalsIgnoreCase("Y");
	}
}
