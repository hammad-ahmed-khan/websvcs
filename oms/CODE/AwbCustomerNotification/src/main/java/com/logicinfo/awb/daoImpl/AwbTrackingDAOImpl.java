package com.logicinfo.awb.daoImpl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.logicinfo.awb.beans.AwbRequest;
import com.logicinfo.awb.beans.AwbResponseStatus;
import com.logicinfo.awb.beans.AwbTrackingInfo;
import com.logicinfo.awb.beans.AwbUtil;
import com.logicinfo.awb.beans.WmsUtil;
import com.logicinfo.awb.dao.AwbTrackingDAO;
import com.logicinfo.awb.entites.OmsAwbDetailInfo;
import com.logicinfo.awb.entites.OmsAwbHeaderInfo;
import com.logicinfo.awb.entites.OmsCoFulfillDetail;
import com.logicinfo.awb.entites.OmsCustOrdAddress;
import com.logicinfo.awb.entites.OmsCustOrdHead;

@Repository
public class AwbTrackingDAOImpl implements AwbTrackingDAO {

	private static final Logger _Logger = Logger.getLogger(AwbTrackingDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbctemplate;

	private BigDecimal awbheaderId;

	private BigDecimal omsCustOrdNo;

	@Override
	public AwbResponseStatus addAwb(AwbRequest awbrequestObj) {
		_Logger.info("Begin of Add airway Bill method for "+awbrequestObj.getTrackingId()+"source "+awbrequestObj.getSource()+"courier name is"+awbrequestObj.getCourier());

		String sqlQuery = null;
		if ("SIM".equalsIgnoreCase(awbrequestObj.getSource()))
			sqlQuery = AwbUtil.SIM_QUERY;
		else
			sqlQuery = AwbUtil.WMS_QUERY;
		
		String courierName=null;
		 if("WMS".equalsIgnoreCase(awbrequestObj.getSource()))
				 courierName=WmsUtil.getCarrierName(awbrequestObj.getSource(), awbrequestObj.getCourier());
		 
		 else  courierName = awbrequestObj.getCourier().toUpperCase();
		 _Logger.info("Courier Name is"+courierName);

		 
		List<AwbTrackingInfo> AwbTrackingInfoList = jdbctemplate.query(sqlQuery,
				new Object[] { awbrequestObj.getSource(), courierName, awbrequestObj.getTrackingId() },
				new AwbTrackingInfoMapper());

		System.out.println("***********************************2");
		String custOrderNo = null;

		if (AwbTrackingInfoList.size() > 0) {
			custOrderNo = AwbTrackingInfoList.get(0).getCustOrderNo();
		} else {
			_Logger.info("Unable to get any record " + awbrequestObj.getSource() + "Tracking Id"
					+ awbrequestObj.getTrackingId() + "courier Name " + awbrequestObj.getCourier());
			
			return new AwbResponseStatus(Boolean.FALSE, new String("Unable to Find Records in"+awbrequestObj.getSource()+"Tracking Id"+awbrequestObj.getTrackingId()));
			
		}

		/**
		 * Get the customer order No and save the records into
		 * oms_awb_header_info table
		 */

		_Logger.info(" Getting the Header information Data" +awbrequestObj.getTrackingId()+"source is"+awbrequestObj.getSource()+"couier name is"+awbrequestObj.getCourier());
		OmsAwbHeaderInfo omsAwbHeaderInfoObj = getOmsAwbHeaderInfoObj(custOrderNo, awbrequestObj);

		_Logger.info("Saving thee Header information table"+awbrequestObj.getTrackingId()+"source is"+awbrequestObj.getSource()+"couier name is"+awbrequestObj.getCourier());
		Boolean saveHeaderInfoStatus = saveOmsAwbHeaderInfo(omsAwbHeaderInfoObj);

		if (Boolean.FALSE == saveHeaderInfoStatus) {
			_Logger.info("Error in saving the header records into a table"+awbrequestObj.getTrackingId()+"source is"+awbrequestObj.getSource()+"couier name is"+awbrequestObj.getCourier());
			
			return  new AwbResponseStatus(Boolean.FALSE, new String("Unable to Save Records in Oms_awb_header_info table"+awbrequestObj.getSource()+"Tracking Id"+awbrequestObj.getTrackingId()));
			
		}

		/**
		 * Logic to prepare the details record in order to save into the
		 * database
		 */

		_Logger.info(" Awb id" + awbheaderId + "Tracking id" + awbrequestObj.getTrackingId() + "Couier Name is"
				+ awbrequestObj.getCourier());
		List<OmsAwbDetailInfo> OmsAwbDetailInfoListObj = null;
		try {
			OmsAwbDetailInfoListObj = prepareOmsDetailInfoList(AwbTrackingInfoList, awbrequestObj.getSource(),
					awbheaderId, awbrequestObj, custOrderNo);
		} catch (Exception e) {
			_Logger.error(" Awb id" + awbheaderId + "Tracking id" + awbrequestObj.getTrackingId() + "Couier Name is"
					+ awbrequestObj.getCourier());
			_Logger.info("update the header to status to failed" + awbheaderId);
			updateHeaderStaustoFail(awbheaderId);
			return	new AwbResponseStatus(Boolean.FALSE, new String("Unable to Get the items and qty infromation for source"+awbrequestObj.getSource()+"Tracking Id"+awbrequestObj.getTrackingId()));
			
		}

		_Logger.info(" Awb id" + awbheaderId + "Tracking id" + awbrequestObj.getTrackingId() + "Courier Name is"
				+ awbrequestObj.getCourier() + "calling insert the data into detail");
		Boolean detailInfoResultFlag = insertIntoOmsAwbDetailInfo(OmsAwbDetailInfoListObj);

		if (Boolean.FALSE == detailInfoResultFlag) {
			_Logger.error(" Awb id" + awbheaderId + "Tracking id" + awbrequestObj.getTrackingId() + "Couier Name is"
					+ awbrequestObj.getCourier()+"Failed to insert into detail table");
			_Logger.info("update the header to status to failed" + awbheaderId);
			updateHeaderStaustoFail(awbheaderId);
			return	new AwbResponseStatus(Boolean.FALSE, new String("Unable to Save Records in Oms_awb_detail_info table for source"+awbrequestObj.getSource()+"Tracking Id"+awbrequestObj.getTrackingId()));
			 
		}
		/*
		 * update the header table status to 'S'.
		 */

		_Logger.info(" Awb id" + awbheaderId + "Tracking id" + awbrequestObj.getTrackingId() + "Courier Name is"
				+ awbrequestObj.getCourier() + "update  the status to success");
		return updateHeaderStaustoSuccess();

	}

	private void updateHeaderStaustoFail(BigDecimal awbheaderId2) {

		try {
			jdbctemplate.update(AwbUtil.updateOmsHeaderInfoSqlFailStatusSql, new Object[] { awbheaderId });
		} catch (Exception e) {

		}

	}

	private AwbResponseStatus updateHeaderStaustoSuccess() {
		try {
			jdbctemplate.update(AwbUtil.updateOmsHeaderInfoSuccessStatusSql, new Object[] { awbheaderId });
		} catch (Exception e) {
			return  new AwbResponseStatus(Boolean.FALSE, new String("Failed to update the head status to S"));
		}
		return  new AwbResponseStatus(Boolean.TRUE, new String("Sucess"));
	}

	public OmsAwbHeaderInfo getOmsAwbHeaderInfoObj(String custOrderNo, AwbRequest awbRequestObj) {
		_Logger.info(" Awb id Begin of getOmsAwbHeaderInfoObj " + awbheaderId + "Tracking id" + awbRequestObj.getTrackingId() + "Courier Name is"
				+ awbRequestObj.getCourier());
		OmsAwbHeaderInfo headerinfo = new OmsAwbHeaderInfo();

		awbheaderId = getawbId();
		headerinfo.setAwbId(awbheaderId.intValue());
		headerinfo.setCourierName(awbRequestObj.getCourier());
		headerinfo.setCustOrderNo(custOrderNo);
		headerinfo.setSmsStausCode("0");
		headerinfo.setEmailStatusCode("0");
		headerinfo.setTrackingId(awbRequestObj.getTrackingId());
		headerinfo.setStatus("A");
		_Logger.info("Getting the oms customer Order No from oms_cust_ord_head for the "+awbRequestObj.getTrackingId()+"Source"+awbRequestObj.getSource()+"Courier Name"+awbRequestObj.getCourier());
		omsCustOrdNo = getOmsCustOrdHeadFindOmsCustOrdNo(custOrderNo);
		_Logger.info("Getting the oms customer Order No from oms_cust_ord_address for the "+awbRequestObj.getTrackingId()+"Source"+awbRequestObj.getSource()+"Courier Name"+awbRequestObj.getCourier());
		OmsCustOrdAddress omsAddress = getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
		headerinfo.setCustPhoneNo(omsAddress.getDeliverPhoneNo());
		headerinfo.setCreateDatetime(new Timestamp(new Date().getTime()));
		headerinfo.setEmailId(getEmailAddressInfo());
		headerinfo.setSource(awbRequestObj.getSource());
		_Logger.info("Awb id End of getOmsAwbHeaderInfoObj" + awbheaderId + "Tracking id" + awbRequestObj.getTrackingId() + "Courier Name is"
				+ awbRequestObj.getCourier());
		return headerinfo;
	}

	private OmsCustOrdAddress getOmsCustOrdAddressFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
	_Logger.info(" Awb id Begin of getOmsCustOrdAddressFindByOmsCustOrdNo " + awbheaderId + "getOmsCustOrdAddressFindByOmsCustOrdNo");
		OmsCustOrdAddress omscustOrdAddressRecObj = jdbctemplate.queryForObject(AwbUtil.omscustOrdAddressSql,
				new Object[] { omsCustOrdNo }, new BeanPropertyRowMapper<OmsCustOrdAddress>(OmsCustOrdAddress.class));
		_Logger.info(" Awb id End of getOmsCustOrdAddressFindByOmsCustOrdNo " + awbheaderId + "getOmsCustOrdAddressFindByOmsCustOrdNo");
		return omscustOrdAddressRecObj;
	}

	private BigDecimal getOmsCustOrdHeadFindOmsCustOrdNo(String custOrderNo) {
	_Logger.info(" Awb id Begin of getOmsCustOrdHeadFindOmsCustOrdNo " + awbheaderId + "getOmsCustOrdHeadFindOmsCustOrdNo");
		OmsCustOrdHead omscustOrdHeadRecObj = jdbctemplate.queryForObject(AwbUtil.omsCustOrdHeadsql,
				new Object[] { custOrderNo }, new BeanPropertyRowMapper<OmsCustOrdHead>(OmsCustOrdHead.class));
		_Logger.info(" Awb id End of getOmsCustOrdHeadFindOmsCustOrdNo " + awbheaderId + "getOmsCustOrdHeadFindOmsCustOrdNo");
		return omscustOrdHeadRecObj.getOmsCustOrdNo();
	}

	private BigDecimal getawbId() {
		_Logger.info(" Awb id Begin of getawbId " + awbheaderId + "getawbId");
		BigDecimal awbId = BigDecimal.ZERO;
		awbId = jdbctemplate.queryForObject(AwbUtil.headerSeqNo, BigDecimal.class);
		_Logger.info(" Awb id End of getawbId " + awbheaderId + "getawbId");
		return awbId;
	}

	private Boolean saveOmsAwbHeaderInfo(OmsAwbHeaderInfo OmsawbHeaderInfoObj) {
	_Logger.info(" Begin of saveOmsAwbHeaderInfo awbheaderid"+OmsawbHeaderInfoObj.getAwbId()+"tracking id is"+OmsawbHeaderInfoObj.getTrackingId()+"Source is"+OmsawbHeaderInfoObj.getCourierName());

		try {
			jdbctemplate.update(AwbUtil.insertHeaderSql,
					new Object[] { OmsawbHeaderInfoObj.getAwbId(), OmsawbHeaderInfoObj.getCustOrderNo(),
							OmsawbHeaderInfoObj.getStatus(), OmsawbHeaderInfoObj.getCourierName(),
							OmsawbHeaderInfoObj.getSmsStausCode(), OmsawbHeaderInfoObj.getEmailStatusCode(),
							OmsawbHeaderInfoObj.getTrackingId(), OmsawbHeaderInfoObj.getCreateDatetime(),
							OmsawbHeaderInfoObj.getCustPhoneNo(), OmsawbHeaderInfoObj.getEmailId(),OmsawbHeaderInfoObj.getSource() });
		} catch (Exception e) {
			_Logger.info(" Exception occurered while saving of saveOmsAwbHeaderInfo awbheaderid"+OmsawbHeaderInfoObj.getAwbId()+"tracking id is"+OmsawbHeaderInfoObj.getTrackingId()+"Source is"+OmsawbHeaderInfoObj.getCourierName());
			_Logger.error("Error occured of saveOmsAwbHeaderInfo awbheaderid"+OmsawbHeaderInfoObj.getAwbId()+"tracking id is"+OmsawbHeaderInfoObj.getTrackingId()+"Source is"+OmsawbHeaderInfoObj.getCourierName());
			return Boolean.FALSE;
		}
		_Logger.info("End of saveOmsAwbHeaderInfo awbheaderid"+OmsawbHeaderInfoObj.getAwbId()+"tracking id is"+OmsawbHeaderInfoObj.getTrackingId()+"Source is"+OmsawbHeaderInfoObj.getCourierName());
		return Boolean.TRUE;
	}

	private List<OmsAwbDetailInfo> prepareOmsDetailInfoList(List<AwbTrackingInfo> awbTrackingInfoList, String source,
			BigDecimal awbId, AwbRequest awbrequestObj, String custOrderNo) {
		_Logger.info("Begin of prepareOmsDetailInfoList  Tracking id"+awbrequestObj.getTrackingId()+"Source is "+awbrequestObj.getSource()+"Source is"+source);
		List<OmsAwbDetailInfo> omsAwbDetailInfoList = new ArrayList<OmsAwbDetailInfo>();
		
		for (AwbTrackingInfo awbTrackingInfoObj : awbTrackingInfoList) {
			String item = awbTrackingInfoObj.getItemid();
			OmsCoFulfillDetail omsCoFulfillDetailRecObj = getOmsCoFulfillDetailFindFulFillDetailByItemAndTsfno(item,
					new BigDecimal(awbTrackingInfoObj.getOmsLinkNo()), awbrequestObj);
			System.out.println("line Number"+omsCoFulfillDetailRecObj.getLineNo());
			_Logger.info("calling  the getExtactUnitretailprice method awb header id"+ awbId+" Tracking id is"+awbrequestObj.getTrackingId()+"source is"+awbrequestObj.getSource());
			BigDecimal unitRetail = getExtactUnitretailprice(item, omsCoFulfillDetailRecObj.getLineNo());
			_Logger.info("Unit Retail Price"+unitRetail );
			OmsAwbDetailInfo OmsAwbDetailInfoObj = new OmsAwbDetailInfo();
			OmsAwbDetailInfoObj.setAwbId(new BigDecimal(awbId.intValue()));
			OmsAwbDetailInfoObj.setId(getOmsAwbDetailInfoSeqValue());
			OmsAwbDetailInfoObj.setItemId(item);

			Integer distrubtedQty = Integer.MIN_VALUE;
			_Logger.info(" getting the disrubted qty  awbheader id"+awbheaderId+" tracking id "+awbrequestObj.getTrackingId()+"awb source is "+awbrequestObj.getSource()+"couier name is"+awbrequestObj.getCourier());
			if ("WMS".equalsIgnoreCase(source))
				distrubtedQty = getDistrubtedQtyFromWMS(awbTrackingInfoObj.getTrackingId(),
						awbTrackingInfoObj.getOmsLinkNo(), item);
		
			else
				distrubtedQty = getDistrubtedQtyFromSim(awbTrackingInfoObj.getTrackingId(),
						awbTrackingInfoObj.getOmsLinkNo(), item, custOrderNo);
			_Logger.info(" getting the disrubted qty  awbheader id"+awbheaderId+" tracking id "+awbrequestObj.getTrackingId()+"awb source is "+awbrequestObj.getSource()+"couier name is"+awbrequestObj.getCourier()+"disrubted qty"+distrubtedQty);
			
			_Logger.info("Quantity is"+distrubtedQty);
			
			OmsAwbDetailInfoObj.setQty(new BigDecimal(distrubtedQty));
            OmsAwbDetailInfoObj.setUnitRetail(unitRetail);
			omsAwbDetailInfoList.add(OmsAwbDetailInfoObj);

		}
		_Logger.info("Begin of prepareOmsDetailInfoList  Tracking id"+awbrequestObj.getTrackingId()+"Source is "+awbrequestObj.getSource()+"Source is"+source);
		_Logger.info("AwbHeader Id The list size is of"+awbheaderId+"size is"+omsAwbDetailInfoList.size());
		return omsAwbDetailInfoList;
	}

	private OmsCoFulfillDetail getOmsCoFulfillDetailFindFulFillDetailByItemAndTsfno(String item, BigDecimal omsLinkno,
			AwbRequest awbrequestObj) {
		String sql = null;
		if ("SIM".equalsIgnoreCase(awbrequestObj.getSource())) {
			sql = AwbUtil.omscoFulfilldetailSIMSql;
		} else {
			sql = AwbUtil.omscoFulfilldetailWMSSql;
		}

		OmsCoFulfillDetail omsCoFulfillDetailObj = jdbctemplate.queryForObject(sql,
				new Object[] { omsCustOrdNo, item, omsLinkno },
				new BeanPropertyRowMapper<OmsCoFulfillDetail>(OmsCoFulfillDetail.class));
		return omsCoFulfillDetailObj;
	}

	private BigDecimal getExtactUnitretailprice(String item, BigDecimal lineNo) {

		BigDecimal unitRetailPrice = jdbctemplate.queryForObject(AwbUtil.omscustOrdItemUnitRetailsql,
				new Object[] { omsCustOrdNo, lineNo, item }, BigDecimal.class);

		BigDecimal unitDiscountAmount = BigDecimal.ZERO;

		try {
			unitDiscountAmount = jdbctemplate.queryForObject(AwbUtil.omscustOrdItemDiscPriceSql,
					new Object[] { omsCustOrdNo, lineNo }, BigDecimal.class);

		} catch (EmptyResultDataAccessException e) {
			unitDiscountAmount = BigDecimal.ZERO;
		}

		_Logger.info("Discount Prices"+unitRetailPrice.subtract(unitDiscountAmount));
		
		return unitRetailPrice.subtract(unitDiscountAmount);

	}

	public BigDecimal getOmsAwbDetailInfoSeqValue() {

		return jdbctemplate.queryForObject(AwbUtil.omsdetailInfoSeq, BigDecimal.class);
	}

	public Boolean insertIntoOmsAwbDetailInfo(final List<OmsAwbDetailInfo> omsAwbDetailInfoListObj) {
		_Logger.info(" Begin of insertIntoOmsAwbDetailInfo awb header id"+awbheaderId+ "end");
		try {
			jdbctemplate.batchUpdate(AwbUtil.omsdetailInsertSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					OmsAwbDetailInfo omsAwbDetailInfoObj = omsAwbDetailInfoListObj.get(index);
					ps.setBigDecimal(1, omsAwbDetailInfoObj.getAwbId());
					ps.setBigDecimal(2, omsAwbDetailInfoObj.getId());
					ps.setString(3, omsAwbDetailInfoObj.getItemId());
					ps.setBigDecimal(4, omsAwbDetailInfoObj.getQty());
					ps.setBigDecimal(5, omsAwbDetailInfoObj.getUnitRetail());
				}

				@Override
				public int getBatchSize() {
					return omsAwbDetailInfoListObj.size();

				}
			});
		} catch (Exception e) {
			_Logger.error(" Error of insertIntoOmsAwbDetailInfo awb header id"+awbheaderId+ "end");
			return Boolean.FALSE;
		}

		_Logger.info(" End of insertIntoOmsAwbDetailInfo awb header id"+awbheaderId+ "end");
		return Boolean.TRUE;
	}

	private String getEmailAddressInfo() {

		String emailInfo = null;
		try {

			emailInfo = jdbctemplate.queryForObject(AwbUtil.omscustOrdAddressEmailInfoSql,
					new Object[] { omsCustOrdNo }, String.class);
		} catch (Exception e) {

		}
		return emailInfo;
	}

	private Integer getDistrubtedQtyFromWMS(String trackingId, String linkNo, String itemId) {
		_Logger.info("Awb Header Id getDistrubtedQtyFromWMS "+awbheaderId+"Tracking id is "+trackingId+" item is "+itemId+"Oms link is "+linkNo);
		return jdbctemplate.queryForObject(AwbUtil.disrubuteQtyfromWMS, new Object[] { trackingId, linkNo, itemId },
				Integer.class);
	}

	private Integer getDistrubtedQtyFromSim(String trackingId, String linkNo, String itemId, String custOrderNo) {
		_Logger.info("Awb Header Id getDistrubtedQtyFromSim "+awbheaderId+"Tracking id is "+trackingId+" item is "+itemId+"Oms link is "+linkNo);
		return jdbctemplate.queryForObject(AwbUtil.distrubteQtyFromSim,
				new Object[] { trackingId, custOrderNo, linkNo, itemId }, Integer.class);
	}

	@Override
	public Boolean validateInputRequest(AwbRequest awbrequestObj) {
		
		Integer rowcountValue = null;
		rowcountValue = jdbctemplate.queryForObject(AwbUtil.checkOmsHeaderInfoRecordExist, new Object[] { awbrequestObj.getCourier(),awbrequestObj.getTrackingId(),awbrequestObj.getSource()}, Integer.class);
		if(rowcountValue.intValue()==0)
			return Boolean.FALSE ;
		else return Boolean.TRUE;
	}
	
	
	
	
}
