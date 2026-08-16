package org.logicInfo.oms.transferCreation.Controller;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleTypes;

@Transactional
@Repository("TsfCreationDAO")
public class TransferCreationDAO implements TsfCreationDAO {

	private final static Logger log = Logger.getLogger(TransferCreationDAO.class.getName());

	TransferCreationDAO() {
		log.info(" Default Constructor Carrera TransferCreationDAO is Executed");
	}

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public TransferCreationResponse getResponse(final int src_id, final int dest_id, final String refNo, String[] item, int[] qty, final String singleitem, final int singleqty, final int src_loc,
			final int ful_loc, final int ful_ord_no, final String cust_ord_no) throws SQLException {
		log.info("Inside Carrera TransferCreationDAO :" + cust_ord_no);
		final Object[][] objType = new Object[item.length][2];
		TransferCreationResponse tcr = new TransferCreationResponse();
		try {
			log.info("Inside TransferCreationDAO - Try " + cust_ord_no);
			if ((item.length == qty.length) && item.length > 0) {
				for (int ii = 0; ii < item.length; ii++) {
					log.info("Inside TransferCreationDAO - For " + cust_ord_no);
					objType[ii][0] = item[ii];
					objType[ii][1] = qty[ii];
					log.info("Inside TransferCreationDAO - For ITEM:" + objType[ii][0] + " :QTY: " + objType[ii][1] + " : " + item[ii] + " : " + qty[ii]);
				}
			} else {
				tcr.setCode(500);
				tcr.setMessage("Item and Qty numbers are not matching");
				tcr.setSuccess(false);
				return tcr;
			}

			String tsf = jdbcTemplate.getJdbcOperations().execute(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement oracleCallableStmt = con.prepareCall("{? = call rms14.xx_tsf_cre_carera_sql.xx_dc_tsf_cre(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
					oracleCallableStmt.registerOutParameter(1, OracleTypes.VARCHAR);
					Array reportsArray = ((oracle.jdbc.OracleConnection) con).createOracleArray("RMS14.XXTSF_TBL_TYPE", objType);
					oracleCallableStmt.setArray(2, reportsArray);
					oracleCallableStmt.setInt(3, src_id);
					oracleCallableStmt.setInt(4, dest_id);
					oracleCallableStmt.setString(5, refNo);
					oracleCallableStmt.setString(6, singleitem);
					oracleCallableStmt.setInt(7, singleqty);
					oracleCallableStmt.setInt(8, src_loc);
					oracleCallableStmt.setInt(9, ful_loc);
					oracleCallableStmt.setString(10, cust_ord_no);
					oracleCallableStmt.setInt(11, ful_ord_no);
					return oracleCallableStmt;
				}
			}, new CallableStatementCallback<String>() {

				@Override
				public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					String tsf = null;
					for (int i = 1; i <= 10; i++) {
						try {
							cs.execute();
							tsf = cs.getString(1);
							if (tsf != null) {
								if (tsf.contains("locked")) {
									if (i == 10) {
										log.info("Carrera tsf creation Failure after attempet no" + i);
										tsf = null;
									}
								} else {
									log.info("Carrera tsf creation Sucessfull attempet no" + i + ": " + cust_ord_no);
									break;
								}
							}
						} catch (Exception e) {
							log.error("Exception occurred while creating Carrera transfer.. " + cust_ord_no, e);
						}
					}

					return tsf;
				}
			});

			log.info("Inside Carrera TransferCreationDAO - After function call " + cust_ord_no);
			// String tsf = ((OracleCallableStatement) oracleCallableStmt).getString(1);
			if (tsf == null || tsf.contains("transfer") || tsf.contains("ORA")) {
				tcr.setTsf_No(null);
				tcr.setCode(500);
				tcr.setSuccess(false);
				tcr.setMessage(tsf);
			} else {
				// tcr.setTsf_No(((OracleCallableStatement) oracleCallableStmt).getString(1));
				tcr.setTsf_No(tsf);
				tcr.setCode(200);
				tcr.setSuccess(true);
				tcr.setMessage(null);
			}
			log.info("Inside Carrera TransferCreationDAO - Tsf Number:CustOrdNo" + tcr.getTsf_No() + ":" + cust_ord_no);
		} catch (Exception e) {
			log.error("Inside Catch : Error in Carrera Transfer Creation", e);
		}
		return tcr;
	}
}
