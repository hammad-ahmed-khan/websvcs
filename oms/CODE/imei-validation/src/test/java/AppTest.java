import java.sql.CallableStatement;
import java.sql.Connection;
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

import oracle.jdbc.OracleTypes;

public class AppTest {

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
			DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@exvm-qarmsdb01.extrastores.com:1521/RMSQA.extrastores.com", "OMSDEV", "Logic123");
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
			
			List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlParameter(Types.NUMERIC));
			declaredParameters.add(new SqlOutParameter("status", Types.NUMERIC));
			declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
			Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
				
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmt = con.prepareCall("{ CALL XX_IS_CANCELLABLE(?, ?, ?, ?) }" );
					stmt.setBigDecimal(1, null);
					stmt.setBigDecimal(2, null);
					stmt.registerOutParameter(3, Types.NUMERIC);
					stmt.registerOutParameter(4, Types.VARCHAR);
			        return stmt;
				}
			}, declaredParameters);
			boolean status = (Boolean) result.get("status");
		} finally {
			try {
				// jdbcTemplate.getDataSource().getConnection().close();
			} catch (Exception e) {
			}
		}
	}
}
