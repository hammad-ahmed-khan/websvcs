package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingType;

import org.apache.log4j.Logger;

import com.logicinfo.oms.util.OMSUtil;

@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ ObjectFactory.class })
@WebService(name = "VASContractWebService", serviceName = "VASContractWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "VASContractWebService", wsdlLocation = "/WEB-INF/wsdl/VASContractWebService.wsdl")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class VASContractWebServiceImpl {
	private static final Logger _LOG = Logger.getLogger(VASContractWebServiceImpl.class.getName());

	public VASContractWebServiceImpl() {
	}

	@WebResult(name = "VASContractResp", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
	@WebMethod
	public VASContractResponse processVASContract(@WebParam(name = "processVASContractReq", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/") VASContract input)
			throws SOAPException, DatatypeConfigurationException {
		_LOG.info("Inside processVasContract");

		VASContractResponse vasContractResponse = new VASContractResponse();
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = OMSUtil.getDBConnection("jdbc/extradev");

			String query = "INSERT INTO VAS_CONTRACTS_ECOM(SRV_ID,SERVICE_PACKAGE_NAME,OPER_UNIT_ID,CONTRACT_NO,ORG_ID,ITEM_SKU,ITEM_SERIAL_1,ITEM_SERIAL_2,\n  "
					+ "SRV_SKU,SRV_PRICE,SRV_START_DATE,SRV_END_DATE,NO_OF_YEARS,STATUS_DESC,\n                                  "
					+ "SRV_INV_NO,SRV_INV_SOURCE,ITEM_INV_SOURCE,EXTRA_ITEM,MOBILE,DP_YEAR,\n                                  "
					+ "SALESMAN_ID,SRV_INV_COM,ITEM_INV_COM,ITEM_INV_DATE,ON_SITE,SRV_INVOICE_LINE_NO,\n                                 "
					+ "ITEM_INVOICE_LINE_NO,TOTAL_VISITS,FREE_LABOR,ALLOW_LOANER,NUM_OF_RE_INSTALLATION_AVAIL,\n                                  "
					+ "NUM_OF_PREV_MAINTENANCE_AVAIL,ON_SITE_VISIT_FLAG,REPLACEMENT_GUARANTEE,NUMBER_OF_CLEANING_VISITS,\n                                  "
					+ "NUMBER_OF_OTHER_VISITS,LAST_UPDATE_DATE,VAS_GROUP,VAS_TYPE,FIRST_NAME,LAST_NAME,COVERED_PRODUCT,\n                                 "
					+ "DIVISION_STORE_NAME,COUNTRY,VAS_CREATED_DATE,SERVICE_STATUS,RETURNED_BSN_DATE)\n"
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			Timestamp srvEndDate = new Timestamp(input.getSrvEndDate().toGregorianCalendar().getTimeInMillis());

			Timestamp srvStartDate = new Timestamp(input.getSrvStartDate().toGregorianCalendar().getTimeInMillis());
			Timestamp itemInvDate = new Timestamp(input.getItemInvDate().toGregorianCalendar().getTimeInMillis());
			Timestamp vascreatedate = new Timestamp(input.getVasCreatedDate().toGregorianCalendar().getTimeInMillis());
			stmt = con.prepareStatement(query);
			StringBuilder log = new StringBuilder();
			log.append("setting srv id").append(input.getSrvId()).append(System.lineSeparator());
			stmt.setLong(1, input.getSrvId().longValue());

			log.append("setting service package" + input.getServicePackageName()).append(System.lineSeparator());
			stmt.setString(2, input.getServicePackageName());

			log.append("setting oper unit id" + input.getOperUnitId()).append(System.lineSeparator());
			stmt.setString(3, input.getOperUnitId());

			log.append("setting contract no" + input.getContractNo()).append(System.lineSeparator());
			stmt.setString(4, input.getContractNo());

			log.append("setting org id" + input.getOrgId()).append(System.lineSeparator());
			stmt.setString(5, input.getOrgId());

			log.append("setting item sku" + input.getItemSku()).append(System.lineSeparator());
			stmt.setString(6, input.getItemSku());

			log.append("setting item serail1" + input.getItemSerial1()).append(System.lineSeparator());
			stmt.setString(7, input.getItemSerial1());

			log.append("setting item serial2" + input.getItemSerial2()).append(System.lineSeparator());
			stmt.setString(8, input.getItemSerial2());

			log.append("setting srv sku" + input.getSrvSku()).append(System.lineSeparator());
			stmt.setString(9, input.getSrvSku());
			log.append("setting srv price" + input.getSrvPrice()).append(System.lineSeparator());
			stmt.setBigDecimal(10, input.getSrvPrice());
			log.append("setting srv start date" + srvStartDate).append(System.lineSeparator());
			stmt.setTimestamp(11, srvStartDate);
			log.append("setting srv end date" + srvEndDate).append(System.lineSeparator());
			stmt.setTimestamp(12, srvEndDate);
			log.append("setting no of years" + input.getNoOfYears()).append(System.lineSeparator());
			stmt.setBigDecimal(13, input.getNoOfYears());
			log.append("setting status desc" + input.getStatusDesc()).append(System.lineSeparator());
			stmt.setString(14, input.getStatusDesc());
			log.append("setting srv inv no" + input.getSrvInvNo()).append(System.lineSeparator());
			stmt.setString(15, input.getSrvInvNo());
			log.append("setting srv inv source" + input.getSrvInvSource()).append(System.lineSeparator());
			stmt.setString(16, input.getSrvInvSource());
			log.append("setting item inv source" + input.getItemInvSource()).append(System.lineSeparator());
			stmt.setString(17, input.getItemInvSource());
			log.append("setting extra item" + input.getExtraItem()).append(System.lineSeparator());
			stmt.setString(18, input.getExtraItem());
			log.append("setting mobile" + input.getMobile()).append(System.lineSeparator());
			stmt.setString(19, input.getMobile());
			log.append("setting dp year").append(System.lineSeparator());
			stmt.setString(20, input.getDpYear());
			log.append("setting salesman id" + input.getSalesmanId()).append(System.lineSeparator());
			stmt.setString(21, input.getSalesmanId());
			log.append("setting srv inv com" + input.getSrvInvCom()).append(System.lineSeparator());
			stmt.setString(22, input.getSrvInvCom());
			log.append("seeting item inv com" + input.getItemInvCom()).append(System.lineSeparator());
			stmt.setString(23, input.getItemInvCom());
			log.append("setting item inv date").append(System.lineSeparator());
			stmt.setTimestamp(24, itemInvDate);
			log.append("setting on site" + input.getOnSite()).append(System.lineSeparator());
			stmt.setString(25, input.getOnSite());
			log.append("setting srv invoice line no" + input.getSrvInvoiceLineNo()).append(System.lineSeparator());
			stmt.setBigDecimal(26, input.getSrvInvoiceLineNo());
			log.append("setting item invoice line no" + input.getItemInvoiceLineNo()).append(System.lineSeparator());
			stmt.setBigDecimal(27, input.getItemInvoiceLineNo());
			log.append("setting total visists" + input.getTotalVisits()).append(System.lineSeparator());
			stmt.setString(28, input.getTotalVisits());
			log.append("setting free labour" + input.getFreeLabor()).append(System.lineSeparator());
			stmt.setString(29, input.getFreeLabor());
			log.append("setting allow loaner" + input.getAllowLoaner()).append(System.lineSeparator());
			stmt.setString(30, input.getAllowLoaner());
			log.append("setting no of re installation avail" + input.getNumOfReInstallationAvail()).append(System.lineSeparator());
			stmt.setString(31, input.getNumOfReInstallationAvail());
			log.append("setting no of prev installation avail" + input.getNumOfPrevInstallationAvail()).append(System.lineSeparator());
			stmt.setString(32, input.getNumOfPrevInstallationAvail());
			log.append("setting on site visit flag" + input.getOnSiteVisitFlag()).append(System.lineSeparator());
			stmt.setString(33, input.getOnSiteVisitFlag());
			log.append("setting replacement guarantee" + input.getReplacementGuarantee()).append(System.lineSeparator());
			stmt.setString(34, input.getReplacementGuarantee());
			log.append("seeting number of cleaning visits" + input.getNumberOfCleaningVisits()).append(System.lineSeparator());
			stmt.setString(35, input.getNumberOfCleaningVisits());
			log.append("setting number of other visits" + input.getNumberOfOtherVisits()).append(System.lineSeparator());
			stmt.setString(36, input.getNumberOfOtherVisits());
			stmt.setTimestamp(37, new Timestamp(new Date().getTime()));
			log.append("setting vas group" + input.getVasGroup()).append(System.lineSeparator());
			stmt.setString(38, input.getVasGroup());
			stmt.setString(39, input.getVasType());
			log.append("setting first name" + input.getFirstName()).append(System.lineSeparator());
			stmt.setString(40, input.getFirstName());
			log.append("setting last name" + input.getLastName()).append(System.lineSeparator());
			stmt.setString(41, input.getLastName());
			log.append("setting covered product" + input.getCoveredProduct()).append(System.lineSeparator());
			stmt.setString(42, input.getCoveredProduct());
			log.append("setting division store name" + input.getDivisionStoreName()).append(System.lineSeparator());
			stmt.setString(43, input.getDivisionStoreName());
			log.append("setting country" + input.getCountry());
			stmt.setString(44, input.getCountry());
			stmt.setTimestamp(45, vascreatedate);
			stmt.setString(46, input.getServiceType());
			stmt.setString(47, "null");
			_LOG.info(log.toString());
			stmt.executeUpdate();
			vasContractResponse.setXRequestorId(input.getOrgId());
			vasContractResponse.setXApplicationId("E-COMMERCE");
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Timestamp(new Date().getTime()));
			XMLGregorianCalendar response_date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			vasContractResponse.setXResponseDatetime(response_date);
			vasContractResponse.setStatusCode(new BigDecimal(200));
			vasContractResponse.setStatus("Success");
			vasContractResponse.setMessage("Success");
			_LOG.info("after setting the response message");
		} catch (Exception e) {
			_LOG.error("Exception while data persist", e);
			vasContractResponse.setXRequestorId(input.getOrgId());
			vasContractResponse.setXApplicationId("E-COMMERCE");
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Timestamp(new Date().getTime()));
			XMLGregorianCalendar response_date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			vasContractResponse.setXResponseDatetime(response_date);
			vasContractResponse.setStatusCode(new BigDecimal(400));
			vasContractResponse.setStatus("Failed");
			vasContractResponse.setMessage("Client specific issue (invalid input, invalid resource, unauthorized etc.)");
		} finally {
			OMSUtil.closeDBConnection(con, stmt, null);
		}
		return vasContractResponse;
	}
}