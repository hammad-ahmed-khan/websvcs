package com.extra;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SQLTest {

	public static void main(String[] a) {
		NamedParameterJdbcTemplate jdbcTemplate = null;
		/*
		 * try { DriverManagerDataSource dataSource = new DriverManagerDataSource(
		 * "jdbc:oracle:thin:@exvm-qarmsdb02.extrastores.com:1521/RMSQA.extrastores.com",
		 * "EXTRADEV", "wFeJdt6c");
		 * dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		 * 
		 * jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		 * 
		 * List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(3);
		 * 
		 * declaredParameters.add(new SqlParameter(Types.VARCHAR));
		 * declaredParameters.add(new SqlParameter(Types.VARCHAR));
		 * declaredParameters.add(new SqlOutParameter("valid", Types.VARCHAR));
		 * Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new
		 * CallableStatementCreator() {
		 * 
		 * @Override public CallableStatement createCallableStatement(Connection con)
		 * throws SQLException { CallableStatement stmt = con.
		 * prepareCall("{ CALL XX_IMEI_UPLOAD_API_VALIDATE.IMEI_UPLOAD_VALIDATE(?, ?, ?) }"
		 * ); stmt.setString(1, "324"); stmt.setString(2, "324324232");
		 * stmt.registerOutParameter(3, Types.VARCHAR); return stmt; } },
		 * declaredParameters); System.out.println(resultMap.get("valid") instanceof
		 * String); } finally { try { //
		 * jdbcTemplate.getDataSource().getConnection().close(); } catch (Exception e) {
		 * } }
		 */
		
		try {
			DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@192.168.50.111:1521/rmsprd.extrastores.com", "EXTRADEV", "wFeJdt6c");
			dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			
			jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			
			/*
			 * String message = jdbcTemplate.getJdbcOperations().execute(new
			 * CallableStatementCreator() {
			 * 
			 * @Override public CallableStatement createCallableStatement(Connection con)
			 * throws SQLException { CallableStatement stmt = con.
			 * prepareCall("{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?) }"
			 * ); stmt.registerOutParameter(1, Types.VARCHAR); stmt.registerOutParameter(2,
			 * Types.VARCHAR); stmt.setBigDecimal(3, new BigDecimal("2342342342"));
			 * stmt.setString(4, "23432423"); stmt.setLong(5, 1L); return stmt; } }, new
			 * CallableStatementCallback<String>() {
			 * 
			 * @Override public String doInCallableStatement(CallableStatement cs) throws
			 * SQLException, DataAccessException { cs.execute(); String message =
			 * cs.getString(1); return message; } } );
			 */
			
			List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(6);

			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.DATE));
			declaredParameters.add(new SqlParameter(Types.DATE));
			declaredParameters.add(new SqlOutParameter("status", Types.VARCHAR));
			declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
			final Long publishId = 3534543L;
			Map<String, Object> resultMap = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
				
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{ CALL XX_APPLE_ON_PRICING.POST_PUBLISH_UPDATE(?, ?, ?, ?, ?, ?, ?) }" );
					/*
					 * stmt.setBigDecimal(1, BigDecimal.valueOf(2343242l)); try {
					 * stmt.setBigDecimal(2, BigDecimal.valueOf(publishId)); } catch (Exception e) {
					 * e.printStackTrace(); }
					 */
					
					stmt.setLong(1, (2343242l));
					stmt.setLong(2, (2343242l));
					stmt.setLong(3, (2343242l));
					
					stmt.setDate(4, new Date(System.currentTimeMillis()));
					stmt.setDate(5, new Date(System.currentTimeMillis()));
					stmt.registerOutParameter(6, Types.VARCHAR);
					stmt.registerOutParameter(7, Types.VARCHAR);
			        return stmt;
				}
			}, declaredParameters);
			
			String status = (String) resultMap.get("status");
			System.out.println(status);
			System.out.println(resultMap.get("message"));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// jdbcTemplate.getDataSource().getConnection().close();
			} catch (Exception e) {
			}
		}
	}
}
