package com.extra.oms.crossChannel.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.extra.oms.crossChannel.model.CrossChannelRequest;
import com.extra.oms.crossChannel.model.CrossChannelResponse;
import com.extra.oms.crossChannel.model.EligibleServices;
import com.extra.oms.crossChannel.model.LineItem;
import com.extra.oms.crossChannel.model.ServiceOfferedLines;
import com.extra.oms.crossChannel.model.ServiceOfferedRequest;
import com.extra.oms.crossChannel.model.ServiceOfferedResponse;

@Repository
public class CrossChannelDAO extends BaseDAO {

	private static final Logger LOG = LogManager.getLogger(CrossChannelDAO.class);

	public List<CrossChannelResponse> getOfflineOrders(CrossChannelRequest request) {
		StringBuilder query = new StringBuilder("SELECT * FROM OMSDEV.XX_CC_ALL_TRAN_DETAILS WHERE CHANNEL = 'POS' AND TRAN_TYPE = 'SALE' AND QTY > 0 AND MOBILENUMBER = :mobileNumber");

		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("mobileNumber", request.getMobileNo());

		if (request.getOrderNo() != null && !request.getOrderNo().isEmpty()) {
			query.append(" AND ORDERNO = :orderNo");
			paramMap.addValue("orderNo", request.getOrderNo());
		}
		query.append("ORDER BY ORDERDATE DESC");

		LinkedHashMap<String, CrossChannelResponse> responseMap = jdbcTemplate.query(query.toString(), paramMap, new CrossChannelResponseMapper());
		return new ArrayList<>(responseMap.values());
	}

	private class CrossChannelResponseMapper implements ResultSetExtractor<LinkedHashMap<String, CrossChannelResponse>> {

		@Override
		public LinkedHashMap<String, CrossChannelResponse> extractData(ResultSet rs) throws SQLException {

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			LinkedHashMap<String, CrossChannelResponse> rMap = new LinkedHashMap<>();
			while (rs.next()) {
				String transNo = rs.getString("TRANSACTIONNUMBER");
				CrossChannelResponse response = rMap.get(transNo);
				if (response == null) {
					response = new CrossChannelResponse();
					response.setLocation(rs.getString("LOCATION"));
					response.setChannel(rs.getString("CHANNEL"));
					response.setOrderNo(rs.getString("ORDERNO"));
					response.setEmail(rs.getString("EMAIL"));
					response.setMobileNumber(rs.getString("MOBILENUMBER"));
					response.setFirstName(rs.getString("FIRSTNAME"));
					response.setLastName(rs.getString("LASTNAME"));
					response.setTransactionNumber(rs.getString("TRANSACTIONNUMBER"));
					response.setCity(rs.getString("CITY"));
					response.setArea(rs.getString("AREA"));
					response.setAddress(rs.getString("ADDRESS"));
					Timestamp orderDateTimestamp = rs.getTimestamp("ORDERDATE");
					if (orderDateTimestamp != null) {
						response.setOrderDate(dateFormat.format(orderDateTimestamp));
					}
					response.setCountry(rs.getString("COUNTRY"));
					response.setTotalRetailDisount(rs.getBigDecimal("TOTALRETAILDISCOUNT"));
					response.setTotalRetailPrice(rs.getBigDecimal("TOTALRETAILPRICE"));
					response.setTotalSellingPrice(rs.getBigDecimal("TOTALSELLING" + "PRICE"));
					response.setTotalJoodDiscount(rs.getBigDecimal("TOTALJOODDISCOUNT"));
					response.setCouponDiscount(rs.getBigDecimal("COUPONDISCOUNT"));
					response.setCouponCodes(convertStringToList(rs.getString("COUPONCODES")));
					response.setTotalServicePrice(rs.getBigDecimal("TOTALSERVICEPRICE"));
					response.setTotalInstallationPrice(rs.getBigDecimal("TOTALINSTALLATIONPRICE"));
					response.setTotalCashBackPrice(rs.getBigDecimal("TOTALCASHBACKPRICE"));
					response.setDeliveryCost(rs.getBigDecimal("DELIVERYCOST"));
					BigDecimal totalSellingPrice = rs.getBigDecimal("TOTALSELLINGPRICE");
					BigDecimal deliveryCost = rs.getBigDecimal("DELIVERYCOST");
					if (totalSellingPrice == null) {
						totalSellingPrice = BigDecimal.ZERO;
					}
					if (deliveryCost == null) {
						deliveryCost = BigDecimal.ZERO;
					}
					response.setSubCost(totalSellingPrice.subtract(deliveryCost));
					response.setLines(new ArrayList<LineItem>());
					rMap.put(transNo, response);
				}

				LineItem line = new LineItem();
				line.setItem(rs.getString("ITEM"));
				line.setItemDescription(rs.getString("ITEMDESCRIPTION"));
				line.setLineNo(rs.getInt("LINENO"));
				line.setQty(rs.getInt("QTY"));
				line.setRetailDiscount(rs.getDouble("RETAILDISCOUNT"));
				line.setRetailPrice(rs.getDouble("RETAILPRICE"));
				line.setJoodDiscount(rs.getDouble("JOODDISCOUNT"));
				line.setSellingPrice(rs.getDouble("SELLINGPRICE"));
				line.setVat(rs.getInt("VAT"));
				line.setVatAmount(rs.getDouble("VATAMOUNT"));
				line.setDeliverQty(rs.getInt("DELIVERQTY"));
				line.setCancelQty(rs.getInt("CANCELQTY"));
				line.setReturnQty(rs.getInt("RETURNQTY"));
				line.setDeliveryType(rs.getString("DELIVERYTYPE"));
				line.setLinkLineNo(rs.getString("LINKLINENO"));
				Timestamp deliveryLeadTime = rs.getTimestamp("DELIVERYLEADTIME");
				if (deliveryLeadTime != null) {
					line.setDeliveryLeadTime(dateFormat.format(deliveryLeadTime));
				}
				line.setItemType(rs.getString("ITEMTYPE"));
				line.setCashBackAmount(rs.getDouble("CASHBACKAMOUNT"));
				line.setBundleGroupId(rs.getString("BUNDLEGROUPID"));
				line.setBundleRetailPrice(rs.getDouble("BUNDLERETAILPRICE"));
				line.setBundleSellingPrice(rs.getDouble("BUNDLESELLINGPRICE"));
				line.setVasGroup(rs.getString("VAS_GROUP"));
				line.setYears(rs.getString("YEARS"));
				line.setBrand(rs.getString("BRAND"));
				line.setSerialNumber(rs.getString("SERIAL_NUMBER"));

				response.getLines().add(line);
			}
			return rMap;
		}
	}

	private List<String> convertStringToList(String str) {
		if (str == null || str.isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.asList(str.split(","));
	}

	public List<ServiceOfferedResponse> getServicesOffered(ServiceOfferedRequest request) {

		StringBuilder query = new StringBuilder("SELECT * FROM OMSDEV.XX_CC_XCM_SERVICES_V WHERE MOBILENUMBER = :mobileNumber");

		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("mobileNumber", request.getMobileNo());

		if (request.getOrderNo() != null && !request.getOrderNo().isEmpty()) {
			query.append(" AND ORDERNO = :orderNo ");
			paramMap.addValue("orderNo", request.getOrderNo());
		}

		if (request.getEmailId() != null && !request.getEmailId().isEmpty()) {
			query.append(" OR EMAIL = :emailId ");
			paramMap.addValue("emailId", request.getEmailId());
		}
		query.append(" ORDER BY ORDERDATE DESC");

		LinkedHashMap<String, ServiceOfferedResponse> resultMap = jdbcTemplate.query(query.toString(), paramMap, new ServiceOfferedResponseMapper());
		return new ArrayList<>(resultMap.values());
	}

	public class ServiceOfferedResponseMapper implements ResultSetExtractor<LinkedHashMap<String, ServiceOfferedResponse>> {

		@Override
		public LinkedHashMap<String, ServiceOfferedResponse> extractData(ResultSet rs) throws SQLException {

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			LinkedHashMap<String, ServiceOfferedResponse> rMap = new LinkedHashMap<>();
			Map<String, ServiceOfferedLines> lineMap = new HashMap<>();

			while (rs.next()) {
				String transNo = rs.getString("TRANSACTIONNUMBER");

				ServiceOfferedResponse resp = rMap.get(transNo);
				if (resp == null) {

					resp = new ServiceOfferedResponse();

					resp.setLocation(rs.getString("LOCATION"));
					resp.setChannel(rs.getString("CHANNEL"));
					resp.setOrderNo(rs.getString("ORDERNO"));
					resp.setEmail(rs.getString("EMAIL"));
					resp.setMobileNumber(rs.getString("MOBILENUMBER"));
					resp.setFirstName(rs.getString("FIRSTNAME"));
					resp.setLastName(rs.getString("LASTNAME"));
					resp.setTransactionNumber(rs.getString("TRANSACTIONNUMBER"));
					resp.setCity(rs.getString("CITY"));
					resp.setArea(rs.getString("AREA"));
					resp.setAddress(rs.getString("ADDRESS"));

					Timestamp orderDateTimestamp = rs.getTimestamp("ORDERDATE");
					if (orderDateTimestamp != null) {
						resp.setOrderDate(dateFormat.format(orderDateTimestamp));
					}

					resp.setCountry(rs.getString("COUNTRY"));
					resp.setTotalRetailDisount(rs.getBigDecimal("TOTALRETAILDISCOUNT"));
					resp.setTotalRetailPrice(rs.getBigDecimal("TOTALRETAILPRICE"));
					resp.setTotalSellingPrice(rs.getBigDecimal("TOTALSELLINGPRICE"));
					resp.setTotalJoodDiscount(rs.getBigDecimal("TOTALJOODDISCOUNT"));
					resp.setCouponDiscount(rs.getBigDecimal("COUPONDISCOUNT"));
					resp.setCouponCodes(convertStringToList(rs.getString("COUPONCODES")));
					resp.setTotalServicePrice(rs.getBigDecimal("TOTALSERVICEPRICE"));
					resp.setTotalInstallationPrice(rs.getBigDecimal("TOTALINSTALLATIONPRICE"));
					resp.setTotalCashBackPrice(rs.getBigDecimal("TOTALCASHBACKPRICE"));
					resp.setDeliveryCost(rs.getBigDecimal("DELIVERYCOST"));

					BigDecimal totalSellingPrice = rs.getBigDecimal("TOTALSELLINGPRICE");
					BigDecimal deliveryCost = rs.getBigDecimal("DELIVERYCOST");
					if (totalSellingPrice == null) {
						totalSellingPrice = BigDecimal.ZERO;
					}
					if (deliveryCost == null) {
						deliveryCost = BigDecimal.ZERO;
					}
					resp.setSubCost(totalSellingPrice.subtract(deliveryCost));
					resp.setServiceOfferedLines(new ArrayList<ServiceOfferedLines>());
					rMap.put(transNo, resp);
				}

				String lineKey = transNo + "~" + rs.getInt("LINENO");

				ServiceOfferedLines line = lineMap.get(lineKey);

				if (line == null) {
					line = new ServiceOfferedLines();
					line.setItem(rs.getString("ITEM"));
					line.setItemDescription(rs.getString("ITEMDESCRIPTION"));
					line.setLineNo(rs.getInt("LINENO"));
					line.setQty(rs.getInt("QTY"));
					line.setRetailDiscount(rs.getDouble("RETAILDISCOUNT"));
					line.setRetailPrice(rs.getDouble("RETAILPRICE"));
					line.setJoodDiscount(rs.getDouble("JOODDISCOUNT"));
					line.setSellingPrice(rs.getDouble("SELLINGPRICE"));
					line.setVat(rs.getInt("VAT"));
					line.setVatAmount(rs.getDouble("VATAMOUNT"));
					line.setDeliverQty(rs.getInt("DELIVERQTY"));
					line.setCancelQty(rs.getInt("CANCELQTY"));
					line.setReturnQty(rs.getInt("RETURNQTY"));
					line.setDeliveryType(rs.getString("DELIVERYTYPE"));
					line.setLinkLineNo(rs.getString("LINKLINENO"));

					Timestamp deliveryLeadTimeTimestamp = rs.getTimestamp("DELIVERYLEADTIME");
					if (deliveryLeadTimeTimestamp != null) {
						line.setDeliveryLeadTime(dateFormat.format(deliveryLeadTimeTimestamp));
					}

					line.setItemType(rs.getString("ITEMTYPE"));
					line.setCashBackAmount(rs.getDouble("CASHBACKAMOUNT"));
					line.setBundleGroupId(rs.getString("BUNDLEGROUPID"));
					line.setBundleRetailPrice(rs.getDouble("BUNDLERETAILPRICE"));
					line.setBundleSellingPrice(rs.getDouble("BUNDLESELLINGPRICE"));
					line.setVasGroup(rs.getString("VAS_GROUP"));
					line.setYears(rs.getString("YEARS"));
					line.setBrand(rs.getString("BRAND"));
					line.setSerialNumber(rs.getString("SERIAL_NUMBER"));
					line.setEligibleServices(new ArrayList<EligibleServices>());
					lineMap.put(lineKey, line);
					resp.getServiceOfferedLines().add(line);
				}

				EligibleServices eligibleServices = new EligibleServices();
				eligibleServices.setServiceType(rs.getString("SERVICE_GROUP"));
				eligibleServices.setEligibleQTY(rs.getInt("SERVICE_QTY"));
				line.getEligibleServices().add(eligibleServices);
			}

			return rMap;
		}
	}
}
