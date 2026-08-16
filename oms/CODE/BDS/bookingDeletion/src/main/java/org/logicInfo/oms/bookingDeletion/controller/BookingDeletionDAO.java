package org.logicInfo.oms.bookingDeletion.controller;

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
@Repository("BookingDelDAO")
public class BookingDeletionDAO implements BookingDelDAO {
	BookingDeletionDAO() {
		System.out.println(" Default Constructor BookingDeletionDAO is Executed");
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	List<BookingDeletionResponse> bookingDelResp = null;
	BookingDeletionResponse bDelResp = null;
	ArrayList<StatusResponse> sRespList = null;
	StatusResponse sResponse = null;
	String qry = "";
//	String deletionQry = "";
	
	public List<BookingDeletionResponse> getResponse(int reservationId, String orderNo) throws SQLException{
		System.out.println("-----------Inside bookingConfirmationDAO------------------");
		sRespList = new ArrayList<StatusResponse>();
		bookingDelResp = new ArrayList<BookingDeletionResponse>();
		bDelResp = new BookingDeletionResponse();
		sResponse = new StatusResponse();
		Connection conn = null;
	
		
		String packageCallStmt = "{call XXHDB_CORE_PKG.cancel_booking(?,?,?,?)}"; 
		try{
			conn = jdbcTemplate.getDataSource().getConnection();
//			deletionQry = "select * from OMS_ORDER_BOOKING_DETAILS_INFO where RESV_ID ='"+reservationId+"' and ORDER_NO = '"+orderNo+"'";
		//	stmt1 = conn.createStatement();
	//		rs1 = stmt1.executeQuery(deletionQry) ;
	//		System.out.println("Result set for bookingId:"+rs1.isBeforeFirst());
			/*if(!rs1.isBeforeFirst()){
				sResponse.setSuccess(false);
				sResponse.setMessage("Invalid Reservation Id");
				sResponse.setCode("400");
			}else{*/
				OracleCallableStatement oracleCallableStmt = (OracleCallableStatement) conn.prepareCall(packageCallStmt);
				oracleCallableStmt.setInt(1, reservationId);	
				oracleCallableStmt.setString(2, orderNo);
				oracleCallableStmt.registerOutParameter(3, OracleTypes.VARCHAR);
				oracleCallableStmt.registerOutParameter(4, OracleTypes.VARCHAR);
				oracleCallableStmt.executeUpdate();
				System.out.println("Booking Details deleted");
				sResponse.setSuccess(true);
				sResponse.setMessage("");
				sResponse.setCode("");
			//}
			sRespList.add(sResponse);
			bDelResp.setStatus(sRespList);
		}catch(Exception e){
			e.printStackTrace();			
		}finally{
			if(conn!=null)
				conn.close();
		}
		bookingDelResp.add(bDelResp);
		return bookingDelResp;
	}	
}
