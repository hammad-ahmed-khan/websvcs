package com.extra.einvoice.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author abubakkarSiddique
 *
 */
@Component
public class ApplicationProperty {

	@Value("${zatca.data.seller.street-name}")
	private String supStreetName;

	@Value("${zatca.data.seller.building-num}")
	private String supBuildingNum;

	@Value("${zatca.data.seller.additional-num}")
	private String supAdditionalNum;

	@Value("${zatca.data.seller.city-div}")
	private String supCityDivision;

	@Value("${zatca.data.seller.city}")
	private String supCity;

	@Value("${zatca.data.seller.postal}")
	private String supPostal;

	@Value("${zatca.data.seller.country}")
	private String supCountry;

	@Value("${zatca.data.seller.vat}")
	private String supVat;

	@Value("${zatca.data.seller.reg-name}")
	private String supRegName;

	@Value("${zatca.xml.validate}")
	private boolean validateXML;

	@Value("${zatca.data.vat-rate}")
	private BigDecimal vatRate;

	public String getSupStreetName() {
		return supStreetName;
	}

	public void setSupStreetName(String supStreetName) {
		this.supStreetName = supStreetName;
	}

	public String getSupBuildingNum() {
		return supBuildingNum;
	}

	public void setSupBuildingNum(String supBuildingNum) {
		this.supBuildingNum = supBuildingNum;
	}

	public String getSupAdditionalNum() {
		return supAdditionalNum;
	}

	public void setSupAdditionalNum(String supAdditionalNum) {
		this.supAdditionalNum = supAdditionalNum;
	}

	public String getSupCityDivision() {
		return supCityDivision;
	}

	public void setSupCityDivision(String supCityDivision) {
		this.supCityDivision = supCityDivision;
	}

	public String getSupPostal() {
		return supPostal;
	}

	public void setSupPostal(String supPostal) {
		this.supPostal = supPostal;
	}

	public String getSupCountry() {
		return supCountry;
	}

	public void setSupCountry(String supCountry) {
		this.supCountry = supCountry;
	}

	public String getSupVat() {
		return supVat;
	}

	public void setSupVat(String supVat) {
		this.supVat = supVat;
	}

	public String getSupRegName() {
		return supRegName;
	}

	public void setSupRegName(String supRegName) {
		this.supRegName = supRegName;
	}

	public boolean isValidateXML() {
		return validateXML;
	}

	public void setValidateXML(boolean validateXML) {
		this.validateXML = validateXML;
	}

	public String getSupCity() {
		return supCity;
	}

	public void setSupCity(String supCity) {
		this.supCity = supCity;
	}

	public BigDecimal getVATRate() {
		return this.vatRate;
	}

	public BigDecimal getVatRate() {
		return vatRate;
	}

	public void setVatRate(BigDecimal vatRate) {
		this.vatRate = vatRate;
	}
}
