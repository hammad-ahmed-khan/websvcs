package com.extra.restservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extra.restservice.bean.Customer;
import com.extra.restservice.util.ConstantSchema;

@Transactional
@Repository("ExtraReturnStoreDao")
public class ExtraReturnStoreDaoImpl implements ExtraReturnStoreDao {

	ExtraReturnStoreDaoImpl() {
		System.out.println("ExtraReturnStoreDaoImpl");
	}

	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection conn = null;
	java.util.Calendar cal = Calendar.getInstance();
	java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime()); // your
																		// date

	@Override
	public String checkCustomerData(Customer customer) throws SQLException {
		ResultSet rs = null, res = null, res1 = null;
		Statement stmt = null, stmt1 = null, stmt2 = null, stmt3 = null;
		String marketPlaceInd = null;
		try {
			String query = "", query1 = "", query2 = "", qry1 = "";
			int qty = 0, line = 0;
			conn = jdbcTemplate.getDataSource().getConnection();
			// Charan_Prod
			/*
			 * for(i =0; i<customer.getItems().size();i++){ System.out.println(
			 * "Inside checkCustomerDataaa ItemSize:::" +customer.getItems().size());
			 * System.out.println( "Inside checkCustomerData***"
			 * +customer.getCustomerOrderNo()+":"+customer.getItems().get(i).
			 * getItemCode()+":"+customer.getItems().get(i).getOrderQty()); item =
			 * customer.getItems().get(i).getItemCode(); qty =
			 * customer.getItems().get(i).getOrderQty(); line =
			 * customer.getItems().get(i).getLineNo(); System.out.println(
			 * "Inside checkCustomerDatttta ItemSize:::" +customer.getItems().size()); }
			 */
			String item = customer.getItemCode();
			qty = customer.getOrderQty();
			line = customer.getLineNo();
			// if(i==1){

			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();
			System.out.println("Before OMS_CUST_ORD_HEAD**" + customer.getCustomerOrderNo());
			query2 = "select * from OMS_CUST_ORD_HEAD where CUST_ORDER_NO = '" + customer.getCustomerOrderNo() + "' and Status = 'S' and ORD_PAYMENT_STATUS ='S'";
			res = stmt.executeQuery(query2);
			if (res.isBeforeFirst()) {
				System.out.println("Inside IF**");
				while (res.next()) {
					System.out.println("Inside While**");
//					qry1 = "select * from OMS_RMA_RCV_DTL where cust_order_no = '" + customer.getCustomerOrderNo() + "'"
//							+ "and line_item_nbr = '" + line + "'";
//					res1 = stmt1.executeQuery(qry1);
////					return "bef qr : Line -> "+line+"   omsCustOrd ->  "+fetchRmaSeq(customer)+"   Item ->"+item;
//
//					if (!res1.isBeforeFirst()) {
					System.out.println("Inside IF1**");
					// while(res1.next()){
					String omsCustOrd = fetchRmaSeq(customer);
					System.out.println("Inside IF2**");

					query = "select QTY_ORDERED_SUOM, CUM_QTY_DELIVERED, QTY_RETURNED from OMS_CUST_ORD_ITEM " + "WHERE ITEM = '" + item + "' " + " and LINE_NO = '" + line
							+ "' and OMS_CUST_ORD_NO = '" + omsCustOrd + "'";
					System.out.println("query***" + query);
					rs = stmt2.executeQuery(query);
					System.out.println("Inside IF4**");
					if (rs.isBeforeFirst()) {
						while (rs.next()) {
							System.out.println("Inside RS");
							int ordQty = rs.getInt("QTY_ORDERED_SUOM");
							System.out.println("Inside RS O");
							int delQty = rs.getInt("CUM_QTY_DELIVERED");
							System.out.println("Inside RS D");
							int retQty = rs.getInt("QTY_RETURNED");
							System.out.println("Inside RS R:::" + ordQty + ":" + delQty + ":" + retQty);
							if (customer.getMarketPlaceInd() != null) {
								marketPlaceInd = customer.getMarketPlaceInd();
							} else {
								marketPlaceInd = "N";
							}

							if (delQty == 0 || qty > delQty || qty > ordQty || (delQty < (retQty + qty) && ("N".equals(marketPlaceInd)))) {
								return "Return quantity not available";

							} else {
								System.out.println("Inside ELSE:" + qty);
								if (delQty >= (retQty + qty)) {
									retQty = retQty + qty;
									System.out.println("Inside ELSE1:" + retQty);
									query1 = "update OMS_CUST_ORD_ITEM set LAST_UPDATE_DATETIME = sysdate, QTY_RETURNED ='" + retQty + "' WHERE ITEM ='" + item + "' and LINE_NO = '" + line + "' and OMS_CUST_ORD_NO = '" + omsCustOrd
											+ "'";
									if ("N".equals(marketPlaceInd)) {
										int count = stmt3.executeUpdate(query1);
										System.out.println("After update* No of rows updated:" + query1 + ":" + count);
									}
									return "success";
								} else {
									if ("N".equals(marketPlaceInd)) {
										return "Return quantity not available";
									} else {
										return "success";
									}
								}
							}
						}
					} else {
						System.out.println("Inside else");
						return "Check OrderNo/ Item/ LineNo";
					}

//					} else {
//						System.out.println("Inside else1");
//						return "Return Request Processed already";
//					}
				}
			} else {
				System.out.println("Inside else2");
				return "Customer Order number not available";
			}
		} catch (Exception e) {
			System.out.println("Inside Catch" + e.getMessage());
			e.printStackTrace();
		} finally {
			System.out.println("Inside Finally");
			if (rs != null) {
				rs.close();
			}
			if (res != null) {
				res.close();
			}
			if (res1 != null) {
				res.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (stmt1 != null) {
				stmt.close();
			}
			if (stmt2 != null) {
				stmt.close();
			}
			if (stmt3 != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}

		}
		return "Return Request failure";
	}

	@Override
	public String insertCustomerData(Customer customer) {
		// System.out.println(namedjdbcTemplate);
		System.out.println("Inside insertCustomerData***" + customer);
		Date curDate = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		// charan
		map.put("OMS_CUST_ORD_NO", fetchRmaSeq(customer));
		// map.put("OMS_CUST_ORD_NO",omsCustOrd);
		String RMAID = fetchOmsRmaSeq();
		int RMAREQID = fetchRMI_Id(customer);
		map.put("RMA_ID", RMAID);
		map.put("RMA_REQ_ID", RMAREQID);
		map.put("Cust_order_no", customer.getCustomerOrderNo());
		map.put("SUB_CUST_ORDER_NO", 1);
		map.put("STATUS", "S");
		map.put("RETURN_LOC_ID", null);
		map.put("RETURN_DATE_TIME", curDate);
		map.put("Refund_Option", "CLEARING");
		map.put("REFUND_COMPLT_IND", "Y");
		map.put("REFUND_AMOUNT", customer.getRefundAmount());
		map.put("COMMENTS", "NOONRET FOR " + customer.getCustomerOrderNo());
		map.put("RETURN_STATUS", "Y");
		map.put("ORIG_REFUND_AMT", customer.getRefundAmount());
		map.put("RESTOCK_PERCENTAGE", 0);
		map.put("RESTOCK_AMOUNT", 0);
		map.put("CREATE_DATETIME", curDate);
		map.put("LAST_UPDATE_DATETIME", null);
		System.out.println("Inside insertCustomerData1");

		namedjdbcTemplate.execute(ConstantSchema.INSERT_OMS_RMA_REQ, map, new PreparedStatementCallback<Integer>() {
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				System.out.println("Inside insertCustomerData2");
				return ps.executeUpdate();
			}
		});
		return RMAID;
	}

	public String fetchOrderNumberNoFromHead(Customer customer) {
		Map<String, String> m = new HashMap<String, String>();
		m.put("customerOrderNo", customer.getCustomerOrderNo());
		System.out.println("customerOrderNo:" + customer.getCustomerOrderNo());
		System.out.println("RetLocId:" + customer.getReturnLocId());
		String omsOrderNumber = namedjdbcTemplate.queryForObject(ConstantSchema.SELECT_BY_ID_QUERY, m, String.class);
		System.out.println(omsOrderNumber);
		return omsOrderNumber;
	}

	public String fetchRmaSeq(Customer customer) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerOrderNo", customer.getCustomerOrderNo());
		String seq_num = namedjdbcTemplate.queryForObject(ConstantSchema.SELECT_BY_ID_QUERY, map, String.class);

		return seq_num;
	}

	public String fetchOmsRmaSeq() {

		Map<String, Object> map = new HashMap<String, Object>();
		String seq_num = namedjdbcTemplate.queryForObject(ConstantSchema.SELECT_OMS_RMA_ID_SEQ_NEXT, map, String.class);

		return seq_num;
	}

	public int fetchRMI_Id(Customer customer) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerOrderNo", customer.getCustomerOrderNo());
		int rmi_Id = jdbcTemplate.queryForObject(ConstantSchema.SELECT_RMA_SEQ_NEXT, new Object[] {}, Integer.class);
		return rmi_Id;
	}

	public void insertItemData(Customer customer, String RMAID) {
		// Charan_Prod
		// List<Items> items=customer.getItems();

		// for (Items customerItem: items) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("RMA_ID", RMAID);
		map.put("ITEM", customer.getItemCode());
		map.put("LINE_NO", customer.getLineNo());
		map.put("RMA_QTY", customer.getOrderQty());
		map.put("ORIG_RMA_QTY", customer.getOrderQty());
		map.put("ITEM_COMMENTS", "NOONRET FOR " + customer.getCustomerOrderNo());
		map.put("RECEIVED_QTY", customer.getOrderQty());
		map.put("CANCEL_QTY", null);
		map.put("STATUS", "S");
		map.put("CREATE_DATETIME", new Date());
		map.put("LAST_UPDATE_DATETIME", null);
		namedjdbcTemplate.execute(ConstantSchema.INSERT_OMS_RMA_REQ_ITEM, map, new PreparedStatementCallback<Integer>() {

			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				return ps.executeUpdate();
			}
		});
		// }

	}

	public void insertOMSDetails(Customer customer, String RMAID) {
		// Charan_PROD
		// List<Items> items=customer.getItems();

		String customerOrderNumber = fetchOrderNumberNoFromHead(customer);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("RMA_ID", RMAID);
		map.put("PROCESS_SEQ_NO", "1");
		map.put("MESSAGE_ID", RMAID);
		map.put("CUST_ORDER_NO", customer.getCustomerOrderNo());
		map.put("OMS_CUST_ORD_NO", customerOrderNumber);
		map.put("ORDER_REQUESTOR_TYPE", "ST");
		map.put("ORDER_REQUESTOR_ID", customer.getReturnLocId());
		// map.put("ITEM", );
		// map.put("LINE_ITEM_NBR", 1);
		// map.put("RECEIVED_QTY", );
		map.put("DISPOSITION_CODE", null);
		map.put("RTLOG_PUB_IND", "N");
		map.put("CREATE_DATETIME", new Date());
		// for(Items customerItem:items){
		map.put("ITEM", customer.getItemCode());
		map.put("LINE_ITEM_NBR", customer.getLineNo());
		map.put("RECEIVED_QTY", customer.getOrderQty());
		namedjdbcTemplate.execute(ConstantSchema.INSERT_OMS_RMA_RCV_DETAIL, map, new PreparedStatementCallback<Integer>() {

			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				return ps.executeUpdate();
			}

		});
	}

	public String getSystemParameterForInsert() {
		String Param = "N";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PARAMETER_ID", "NOON_RETURN");
			map.put("PARAMETER_NAME", "REQUEST_INSERT");
			Param = namedjdbcTemplate.queryForObject(ConstantSchema.GET_NOON_REQUEST_INSERT_PARAM, map, String.class);

			return Param;
		} catch (Exception Ex) {
			return Param;
		}
	}

	@Override
	public void insertRequestData(Customer customer) {

		String isInsert = getSystemParameterForInsert();

		if (isInsert.equals("Y")) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("CUSTOMER_ORDER_NO", customer.getCustomerOrderNo());
			map.put("RETURN_REQ_ID", customer.getReturnReqId());
			map.put("RETURN_LOC_ID", customer.getReturnLocId());
			map.put("REFUND_AMOUNT", customer.getRefundAmount());
			map.put("ITEM", customer.getItemCode());
			map.put("ORDER_QTY", customer.getOrderQty());
			map.put("LINE_NO", customer.getLineNo());

			namedjdbcTemplate.execute(ConstantSchema.INSERT_NOON_REQUEST, map, new PreparedStatementCallback() {

				public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					// TODO Auto-generated method stub
					return ps.executeUpdate();
				}

			});

		}

	}
}
