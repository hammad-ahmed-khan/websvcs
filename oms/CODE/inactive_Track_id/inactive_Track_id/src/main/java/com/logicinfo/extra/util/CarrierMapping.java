// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.logicinfo.extra.store.ExtraDBConnection;

public class CarrierMapping {
	PreparedStatement getCarrierDescription;
	ExtraDBConnection extraDBConnection= new  ExtraDBConnection();

	public CarrierMapping() throws SQLException, Exception {
		this.extraDBConnection.loadProperties("wms");
		final Connection wmsConnection = this.extraDBConnection.getDBConnection();
		this.getCarrierDescription = wmsConnection
				.prepareCall("select EXTERNAL_CARRIER_CODE from xx_carrier_Setup where CARRIER_CODE = ? and rownum=1");
	}

	public  String getCarrierDescription(final String carrier) {
		 ResultSet rs = null;
	 String carrierDesc = null;
		try {
			this.getCarrierDescription.setString(1, carrier);
			rs=this.getCarrierDescription.executeQuery();
			if(rs.next()){
				carrierDesc=rs.getString(1);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		
//		 if ("ARMX".equalsIgnoreCase(carrier) || "ARM".equalsIgnoreCase(carrier)) {
//		 	carrierDesc = "Aramex";
//		 } else if ("SMSA".equalsIgnoreCase(carrier) || "SMS".equalsIgnoreCase(carrier)) {
//		 	carrierDesc = "Smsa";
//		 } else if ("DHL".equalsIgnoreCase(carrier)) {
//		 	carrierDesc = "Dhl";
//		 } else if ("UPS".equalsIgnoreCase(carrier)) {
//		 	carrierDesc = "Ups";
//		 } else if ("FETC".equalsIgnoreCase(carrier) || "FTC".equalsIgnoreCase(carrier)) {
//		 	carrierDesc = "Fetcher";
//		 } else if ("TMCO".equalsIgnoreCase(carrier) || "TAM".equalsIgnoreCase(carrier)) {
//		 	carrierDesc = "Tamco";
//		 }
		return carrierDesc;
	}

	public static String getcamelCaseCarrierName(final String carrierName) {
		return String.valueOf(carrierName.charAt(0)).concat(carrierName.substring(1).toLowerCase());
	}
}
