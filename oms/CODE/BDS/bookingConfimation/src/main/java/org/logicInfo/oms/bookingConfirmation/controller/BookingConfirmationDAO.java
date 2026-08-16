package org.logicInfo.oms.bookingConfirmation.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

@Transactional
@Repository("BookingConfDAO")
public class BookingConfirmationDAO implements BookingConfDAO {
	BookingConfirmationDAO() {
		System.out.println(" Default Constructor bookingCreationDAO is Executed");
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	List<BookingConfirmationResponse> bookingConfResp = null;
	BookingConfirmationResponse bConfResp = null;
	ArrayList<StatusResponse> sRespList = null;
	StatusResponse sResponse = null;
	//String qry = "";
	String bookingQry = "";
	Boolean resFlag = false;
	int resultsetBookingId = 0;
	
	public List<BookingConfirmationResponse> getResponse(int resvId, String orderNo, String omsOrderNo) throws SQLException{
		System.out.println("-----------Inside bookingConfirmationDAO------------------");
		bookingConfResp = new ArrayList<BookingConfirmationResponse>();
		sRespList = new ArrayList<StatusResponse>();
		bConfResp = new BookingConfirmationResponse();
		sResponse = new StatusResponse();
		Connection conn = null;
		Statement stmt =null;
		Statement stmt1 =null;
		
		String packageCallStmt = "{call XXHDB_CORE_PKG.confirm_booking(?,?,?,?,?,?)}";
		try{
			conn = jdbcTemplate.getDataSource().getConnection();
			//Charan - Booking Details Info
			//bookingQry = "select * from OMS_ORDER_BOOKING_DETAILS_INFO where ORDER_NO ='"+orderNo+"' and RESV_ID ='"+resvId+"'";
			//stmt1 = conn.createStatement();
			//rs1 = stmt1.executeQuery(bookingQry) ;
			//System.out.println("Result set for orderNo/ResvId:"+rs1.isBeforeFirst());
			/*if(!rs1.isBeforeFirst()){
				sResponse.setSuccess(false);
				sResponse.setMessage("Invalid Order No/Reservation Id");
				sResponse.setCode("400");
			}else{*/
			//Charan - Booking Details Info
				OracleCallableStatement oracleCallableStmt = (OracleCallableStatement) conn.prepareCall(packageCallStmt);
				stmt = conn.createStatement();
				/*qry = "select RESERVE_ONLY from OMS_ORDER_BOOKING_HEAD_INFO where ORDERNO='"+orderNo+"'";
				rs = stmt.executeQuery(qry);
				if(rs.next())
					res = rs.getString("RESERVE_ONLY");
				System.out.println("Reserve Flag from DB:"+rs.getString("RESERVE_ONLY")+":"+res);*/
				oracleCallableStmt.setInt(1, resvId);
				oracleCallableStmt.setString(2, orderNo);
			//	oracleCallableStmt.setString(3, omsOrderNo);
				oracleCallableStmt.setString(3, orderNo);
				oracleCallableStmt.registerOutParameter(4, OracleTypes.NUMBER);
				oracleCallableStmt.registerOutParameter(5, OracleTypes.VARCHAR);
				oracleCallableStmt.registerOutParameter(6, OracleTypes.VARCHAR);
				oracleCallableStmt.executeUpdate();
				resultsetBookingId = ((OracleCallableStatement) oracleCallableStmt).getInt(4);
				String stat = ((OracleCallableStatement) oracleCallableStmt).getString(5);
				System.out.println("*****BookingId*****"+resultsetBookingId+":"+stat);
				//qry = "update OMS_ORDER_BOOKING_DETAILS_INFO set BOOKING_ID = '"+resultsetBookingId+"' where RESV_ID ='"+resvId+"' and ORDER_NO ='"+orderNo+"'";
				//stmt.executeUpdate(qry);
				sResponse.setSuccess(true);
				if(stat.equals("F")){
					sResponse.setSuccess(false);
				}
				sResponse.setMessage(((OracleCallableStatement) oracleCallableStmt).getString(6));
				sResponse.setCode("");
			//	Charan - Booking Details Info	
			//}
			sRespList.add(sResponse);
			bConfResp.setStatus(sRespList);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				stmt.close();
			}
			if(stmt1!=null){
				stmt1.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		
		bookingConfResp.add(bConfResp);
		return bookingConfResp;
		
	}
}
