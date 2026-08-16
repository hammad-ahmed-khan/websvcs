package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.util.OMSUtil;

public class RMSPackage {
	public RMSPackage() {
		super();
	}

	private final static Logger log = Logger.getLogger(com.logicinfo.oms.model.RMSPackage.class.getName());

	void rollbackRmsPackageCall(CustomerOrderCancellation input, CustomerOrderCancellationItems item, BigDecimal omsCustrdNo, OmsCustOrdReserve cancelItem, BigDecimal cancelQty) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("***Rollback -rmsPackageCall-started***");
		Connection con = null;
		CallableStatement pstmt = null;
		int result = 0;
		// List<OmsCustOrdReserve>
		// omsCustOrdReserveList=session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustrdNo);
		// for(CustomerOrderCancellationItems item: input.getCancellationItems())
		// {
		List<OmsCustOrdReserve> omsCustOrdReserveList1 = null;
		try {
			omsCustOrdReserveList1 = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(omsCustrdNo, item.getItem(), new BigDecimal(item.getLineNo()));
		} catch (Exception e) {
			log.error("Error in getOmsCustOrdReserveFindByOmsCustOrdNoAndItem=" + e);
		}
		log.info("omsCustOrdReserveList1" + omsCustOrdReserveList1.size());
		// for (OmsCustOrdReserve cancelItem : omsCustOrdReserveList1) {
		if (cancelItem.getRmsResvLocType().equals("SU") == false && cancelItem.getRmsResvLocType().equals("ST") == false) {
			try {
				con = OMSUtil.createDBConnection("jdbc/oms");
				pstmt = con.prepareCall("{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}");
				pstmt.setString(1, cancelItem.getItem());
				int i_inv_staus = Integer.parseInt(session.getOmsSystemParametersFindIndValue("RESV_INV_STATUS", "OMS_SYSTEM_OPTION"));
				pstmt.setInt(2, i_inv_staus); // read from system parameter table I_inv_status
				if (cancelItem.getRmsResvLocType().equals("ST")) {
					pstmt.setString(3, "S"); // loc type S,W ,source loc type
				} else {
					pstmt.setString(3, "W");
				}
				// pstmt.setInt(4, new BigDecimal(input.getPickLoc()).intValueExact()); //loc
				pstmt.setInt(4, (cancelItem.getRmsResvLoc().intValueExact())); // loc make it source loc id
				int qty = -(cancelQty.intValueExact());
				// pstmt.setInt(5, custOrdItems.getRmsResvQty().intValueExact()); //loc qty
				pstmt.setInt(5, qty);
				// System.out.println("5:locQty" + custOrdItems.getOrderQty().intValueExact());
				// pstmt.setInt(5, custOrdItems.getOrderQty().intValueExact()); //loc qty
				int reason_code = Integer.parseInt(session.getOmsSystemParametersFindIndValue("REASON_CODE", "OMS_SYSTEM_OPTION"));
				pstmt.setInt(6, reason_code); // reason_id
				OmsCustOrdHead OmsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustrdNo);
				pstmt.setString(7, OmsCustOrdHead.getCustOrderNo() + "_CL");
				pstmt.registerOutParameter(8, Types.INTEGER);
				pstmt.registerOutParameter(9, Types.VARCHAR);
				// pstmt.registerOutParameter(1, Types.INTEGER);
				pstmt.executeUpdate();
				result = pstmt.getInt(8);
				log.info("plsql call ::" + result);
				String err_msg = pstmt.getString(9);
				log.info(err_msg);
				if (result != 1) {
					log.info("unable to process the record in RMS package");
					log.info(err_msg);
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
				}
				log.info("***rollback rmsPackageCall success***");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// }
		// }
	}
}
