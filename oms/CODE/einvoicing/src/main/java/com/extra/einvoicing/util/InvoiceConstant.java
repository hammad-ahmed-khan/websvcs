package com.extra.einvoicing.util;

import java.util.Objects;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ProfileIDType;

/**
 * @author aibrahim
 *
 */
public class InvoiceConstant {

	private static ProfileIDType PROFILE_ID_TYPE;

	public static ProfileIDType getProfileType() {
		if (Objects.isNull(PROFILE_ID_TYPE)) {
			PROFILE_ID_TYPE =  new ProfileIDType();
			PROFILE_ID_TYPE.setValue("reporting:1.0");
		}
		return PROFILE_ID_TYPE;
	}
}
