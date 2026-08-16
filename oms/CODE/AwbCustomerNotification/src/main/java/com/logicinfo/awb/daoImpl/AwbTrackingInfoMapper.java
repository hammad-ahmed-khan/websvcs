package com.logicinfo.awb.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.logicinfo.awb.beans.AwbTrackingInfo;

public class AwbTrackingInfoMapper implements RowMapper<AwbTrackingInfo> {

	@Override
	public AwbTrackingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		AwbTrackingInfo awbTrackingInfoObj = new AwbTrackingInfo();
		awbTrackingInfoObj.setSource(rs.getString("SOURCE"));
		awbTrackingInfoObj.setTrackingId(rs.getString("TRACKING_NUMBER"));
		awbTrackingInfoObj.setOmsLinkNo(rs.getString("OMS_LINK"));
		awbTrackingInfoObj.setItemid(rs.getString("ITEM_ID"));
		awbTrackingInfoObj.setCustOrderNo(rs.getString("CUST_ORDER_NO"));
		awbTrackingInfoObj.setCourierName(rs.getString("COURIER_NAME"));
		return awbTrackingInfoObj;
	}

}
