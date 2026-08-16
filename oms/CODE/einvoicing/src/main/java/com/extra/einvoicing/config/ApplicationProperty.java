package com.extra.einvoicing.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperty {

	@Value("${zatca.api.seller.street-name}")
	private String streetName;

	@Value("${zatca.api.seller.building-num}")
	private String buildingName;

	@Value("${zatca.api.seller.city-div}")
	private String cityDivision;

	@Value("${zatca.api.seller.city}")
	private String city;

	@Value("${zatca.api.seller.postal}")
	private String postalCode;

	@Value("${zatca.api.seller.country}")
	private String country;

	@Value("${zatca.api.seller.vat}")
	private String vatNo;

	@Value("${zatca.api.seller.reg-name}")
	private String registerName;

	@Value("#{${report.bip.path}}")
	private Map<String, String> reportPath;

	@Value("${report.bip.username}")
	private String reportUserName;

	@Value("${report.bip.password}")
	private String reportPassword;

	@Value("${report.bip.email-body}")
	private String reportEmailBody;

	@Value("${report.bip.email-from}")
	private String reportEmailFrom;

	@Value("${report.bip.email-server}")
	private String reportEmailServerName;

	@Value("${report.bip.email-subject}")
	private String reportEmailSubject;

	@Value("${zatca.api.seller.additional-num}")
	private String addtionalStreetNumber;

	@Value("#{${report.bip.email-cc}}")
	private Map<String, String> reportEmailCC;

	@Value("#{${report.bip.email-to}}")
	private Map<String, String> reportEmailTo;

	@Value("${report.bip.pos.email-to}")
	private String b2bPOSEmailTo;

	@Value("#{${b2b.pos.stores}}")
	private List<String> b2bStoreIds;

	@Value("#{${zatca.api.seller.ebs-crn}}")
	private Map<String, String> commercialRegMap;

	public String getStreetName() {
		return streetName;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public String getCityDivision() {
		return cityDivision;
	}

	public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	public String getVatNo() {
		return vatNo;
	}

	public String getRegisterName() {
		return registerName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public void setCityDivision(String cityDivision) {
		this.cityDivision = cityDivision;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setVatNo(String vatNo) {
		this.vatNo = vatNo;
	}

	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	public Map<String, String> getReportPath() {
		return reportPath;
	}

	public void setReportPath(Map<String, String> reportPath) {
		this.reportPath = reportPath;
	}

	public String getReportUserName() {
		return reportUserName;
	}

	public String getReportPassword() {
		return reportPassword;
	}

	public void setReportUserName(String reportUserName) {
		this.reportUserName = reportUserName;
	}

	public void setReportPassword(String reportPassword) {
		this.reportPassword = reportPassword;
	}

	public String getReportEmailBody() {
		return reportEmailBody;
	}

	public String getReportEmailFrom() {
		return reportEmailFrom;
	}

	public String getReportEmailServerName() {
		return reportEmailServerName;
	}

	public String getReportEmailSubject() {
		return reportEmailSubject;
	}

	public void setReportEmailBody(String reportEmailBody) {
		this.reportEmailBody = reportEmailBody;
	}

	public void setReportEmailFrom(String reportEmailFrom) {
		this.reportEmailFrom = reportEmailFrom;
	}

	public void setReportEmailServerName(String reportEmailServerName) {
		this.reportEmailServerName = reportEmailServerName;
	}

	public void setReportEmailSubject(String reportEmailSubject) {
		this.reportEmailSubject = reportEmailSubject;
	}

	public String getAddtionalStreetNumber() {
		return addtionalStreetNumber;
	}

	public void setAddtionalStreetNumber(String addtionalStreetNumber) {
		this.addtionalStreetNumber = addtionalStreetNumber;
	}

	public Map<String, String> getReportEmailCC() {
		return reportEmailCC;
	}

	public void setReportEmailCC(Map<String, String> reportEmailCC) {
		this.reportEmailCC = reportEmailCC;
	}

	public String getB2bPOSEmailTo() {
		return b2bPOSEmailTo;
	}

	public void setB2bPOSEmailTo(String b2bPOSEmailTo) {
		this.b2bPOSEmailTo = b2bPOSEmailTo;
	}

	public Map<String, String> getCommercialRegMap() {
		return commercialRegMap;
	}

	public void setCommercialRegMap(Map<String, String> commercialRegMap) {
		this.commercialRegMap = commercialRegMap;
	}

	public List<String> getB2bStoreIds() {
		return b2bStoreIds;
	}

	public void setB2bStoreIds(List<String> b2bStoreIds) {
		this.b2bStoreIds = b2bStoreIds;
	}

	public Map<String, String> getReportEmailTo() {
		return reportEmailTo;
	}

	public void setReportEmailTo(Map<String, String> reportEmailTo) {
		this.reportEmailTo = reportEmailTo;
	}
}
