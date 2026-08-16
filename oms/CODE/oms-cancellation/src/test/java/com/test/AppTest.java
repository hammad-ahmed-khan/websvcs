package com.test;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class AppTest {
	
	public static void main(String a[]) throws NamingException {
		NamedParameterJdbcTemplate jdbcTemplate = null;
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@exvm-qarmsdb01.extrastores.com:1521/RMSQA.extrastores.com", "OMSDEV", "Logic123");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>(4);
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlParameter(Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("status", Types.NUMERIC));
		declaredParameters.add(new SqlOutParameter("message", Types.VARCHAR));
		Map<String, Object> result = jdbcTemplate.getJdbcOperations().call(new CallableStatementCreator() {
			
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement stmt = con.prepareCall("{ CALL XX_IS_CANCELLABLE(?, ?, ?, ?) }" );
				stmt.setBigDecimal(1, BigDecimal.TEN);
				stmt.setBigDecimal(2, BigDecimal.TEN);
				stmt.registerOutParameter(3, Types.NUMERIC);
				stmt.registerOutParameter(4, Types.VARCHAR);
		        return stmt;
			}
		}, declaredParameters);
		System.out.println(result.get("status"));
		System.out.println(result.get("message"));
	}
}
