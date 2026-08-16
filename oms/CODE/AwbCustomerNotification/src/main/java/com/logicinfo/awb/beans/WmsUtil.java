package com.logicinfo.awb.beans;

public class WmsUtil {
	
	
public static  String getCarrierName(String source , String couriername){
		String carrierCode=null;
		
		if("WMS".equalsIgnoreCase(source)) {
			if ("UPS".equalsIgnoreCase(couriername))
				 carrierCode ="UPS";
			else if ("SMSA".equalsIgnoreCase(couriername))
				carrierCode ="SMSA";
			else if ("DHL".equalsIgnoreCase(couriername)) 
				carrierCode="DHL";
			else if ("ARAMEX".equalsIgnoreCase(couriername))
				carrierCode="ARMX";
			else if ("FETCHR".equalsIgnoreCase(couriername)){
				carrierCode="FETC";
			}
		}
		
		return carrierCode;
		
	}
	
	
	
	
	}
