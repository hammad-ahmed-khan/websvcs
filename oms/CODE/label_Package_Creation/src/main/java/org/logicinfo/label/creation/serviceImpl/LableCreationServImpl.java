package org.logicinfo.label.creation.serviceImpl;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;

import java.sql.SQLException;

import org.logicinfo.label.creation.model.LableCreationModel;
import org.logicinfo.label.creation.service.LableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleCallableStatement;

import oracle.jdbc.OracleTypes;

@Transactional
@Repository("lableService")
public class LableCreationServImpl implements LableService {

	LableCreationServImpl() {
		System.out.println("default constructor for LableCreateService ");
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Clob label_data;
	private String result;
	private String errormsg;

	public String getlablePackageResponse(String s) throws SQLException {
		Connection con = null;
		try {

			con = jdbcTemplate.getDataSource().getConnection();
			String packageCallStmt = "{call XX_CREATE_LABEL(?,?,?)}";
			OracleCallableStatement oracleCallableStmt = (OracleCallableStatement) con.prepareCall(packageCallStmt);
			oracleCallableStmt.setStringForClob(1, s);
			oracleCallableStmt.registerOutParameter(2, OracleTypes.CLOB);
			oracleCallableStmt.registerOutParameter(3, OracleTypes.VARCHAR);
			oracleCallableStmt.executeUpdate();
			label_data = ((OracleCallableStatement) oracleCallableStmt).getClob(2);
			if (null != label_data)
				result = label_data.getSubString(1, (int) label_data.length());
			else
				result = "Error in label package web service";
			errormsg = ((OracleCallableStatement) oracleCallableStmt).getString(3);
		} catch (Exception e) {
			e.printStackTrace();
			if (errormsg != null || errormsg != "")
				return errormsg;
			else
				return "Error in label package web service";

		} finally {
			if (con != null) {
				con.close();
			}

		}
		if (errormsg.equalsIgnoreCase("SUCCESS"))
			return result;
		else if (result != null || result != "")
			return result;
		else
			return errormsg;
	}

}
