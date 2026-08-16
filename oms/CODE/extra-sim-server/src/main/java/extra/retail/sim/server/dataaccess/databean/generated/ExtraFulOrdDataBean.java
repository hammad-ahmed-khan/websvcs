package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.retail.sim.server.dataaccess.FullDataBean;

public class ExtraFulOrdDataBean extends FullDataBean<ExtraFulOrdDataBean> {

	public static final String TABLE_NAME = "FUL_ORD_CO_V FUL_ORD";

	public static final String SELECT_SQL = getSelectSqlText();

	private Long id;

	private String externalId;

	private String custOrderId;

	private Long storeId;

	private String customerId;

	private Long orderType;

	private Long deliveryType;

	private Long status;

	private String comments;

	private Date custOrderDate;

	private Date createDate;

	private Date updateDate;

	private Date releaseDate;

	private Date deliveryDate;

	private Long shipCarrierId;

	private Long shipCarrierServiceId;

	private String allowPartialDelivery;

	private Double shipCostValue;

	private String shipCostCurrency;

	private String deliveryMode;

	private String deliverySlot;

	private static String getSelectSqlText() {
		return "SELECT ID, EXTERNAL_ID, CUST_ORDER_ID, STORE_ID, CUSTOMER_ID, ORDER_TYPE, DELIVERY_TYPE, STATUS, COMMENTS, CUST_ORDER_DATE, CREATE_DATE, UPDATE_DATE, RELEASE_DATE, DELIVERY_DATE, SHIP_CARRIER_ID, SHIP_CARRIER_SERVICE_ID, ALLOW_PARTIAL_DELIVERY, SHIP_COST_VALUE, SHIP_COST_CURRENCY, DELIVERY_MODE, DELIVERY_SLOT FROM FUL_ORD_CO_V FUL_ORD";
	}

	public String getSelectSql() {
		return SELECT_SQL;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long paramLong) {
		this.id = paramLong;
	}

	public void setId(long paramLong) {
		this.id = Long.valueOf(paramLong);
	}

	public String getExternalId() {
		return this.externalId;
	}

	public void setExternalId(String paramString) {
		this.externalId = paramString;
	}

	public String getCustOrderId() {
		return this.custOrderId;
	}

	public void setCustOrderId(String paramString) {
		this.custOrderId = paramString;
	}

	public Long getStoreId() {
		return this.storeId;
	}

	public void setStoreId(Long paramLong) {
		this.storeId = paramLong;
	}

	public void setStoreId(long paramLong) {
		this.storeId = Long.valueOf(paramLong);
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String paramString) {
		this.customerId = paramString;
	}

	public Long getOrderType() {
		return this.orderType;
	}

	public void setOrderType(Long paramLong) {
		this.orderType = paramLong;
	}

	public void setOrderType(long paramLong) {
		this.orderType = Long.valueOf(paramLong);
	}

	public Long getDeliveryType() {
		return this.deliveryType;
	}

	public void setDeliveryType(Long paramLong) {
		this.deliveryType = paramLong;
	}

	public void setDeliveryType(long paramLong) {
		this.deliveryType = Long.valueOf(paramLong);
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long paramLong) {
		this.status = paramLong;
	}

	public void setStatus(long paramLong) {
		this.status = Long.valueOf(paramLong);
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String paramString) {
		this.comments = paramString;
	}

	public Date getCustOrderDate() {
		return this.custOrderDate;
	}

	public void setCustOrderDate(Date paramDate) {
		this.custOrderDate = paramDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date paramDate) {
		this.createDate = paramDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date paramDate) {
		this.updateDate = paramDate;
	}

	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date paramDate) {
		this.releaseDate = paramDate;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date paramDate) {
		this.deliveryDate = paramDate;
	}

	public Long getShipCarrierId() {
		return this.shipCarrierId;
	}

	public void setShipCarrierId(Long paramLong) {
		this.shipCarrierId = paramLong;
	}

	public void setShipCarrierId(long paramLong) {
		this.shipCarrierId = Long.valueOf(paramLong);
	}

	public Long getShipCarrierServiceId() {
		return this.shipCarrierServiceId;
	}

	public void setShipCarrierServiceId(Long paramLong) {
		this.shipCarrierServiceId = paramLong;
	}

	public void setShipCarrierServiceId(long paramLong) {
		this.shipCarrierServiceId = Long.valueOf(paramLong);
	}

	public String getAllowPartialDelivery() {
		return this.allowPartialDelivery;
	}

	public void setAllowPartialDelivery(String paramString) {
		this.allowPartialDelivery = paramString;
	}

	public Double getShipCostValue() {
		return this.shipCostValue;
	}

	public void setShipCostValue(Double paramDouble) {
		this.shipCostValue = paramDouble;
	}

	public void setShipCostValue(double paramDouble) {
		this.shipCostValue = Double.valueOf(paramDouble);
	}

	public String getShipCostCurrency() {
		return this.shipCostCurrency;
	}

	public void setShipCostCurrency(String paramString) {
		this.shipCostCurrency = paramString;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public String getDeliverySlot() {
		return deliverySlot;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public void setDeliverySlot(String deliverySlot) {
		this.deliverySlot = deliverySlot;
	}

	public ExtraFulOrdDataBean read(ResultSet paramResultSet) throws SQLException {
		ExtraFulOrdDataBean fulOrdDataBean = new ExtraFulOrdDataBean();
		fulOrdDataBean.id = getLong(paramResultSet, "ID");
		fulOrdDataBean.externalId = getString(paramResultSet, "EXTERNAL_ID");
		fulOrdDataBean.custOrderId = getString(paramResultSet, "CUST_ORDER_ID");
		fulOrdDataBean.storeId = getLong(paramResultSet, "STORE_ID");
		fulOrdDataBean.customerId = getString(paramResultSet, "CUSTOMER_ID");
		fulOrdDataBean.orderType = getLong(paramResultSet, "ORDER_TYPE");
		fulOrdDataBean.deliveryType = getLong(paramResultSet, "DELIVERY_TYPE");
		fulOrdDataBean.status = getLong(paramResultSet, "STATUS");
		fulOrdDataBean.comments = getString(paramResultSet, "COMMENTS");
		fulOrdDataBean.custOrderDate = getDate(paramResultSet, "CUST_ORDER_DATE");
		fulOrdDataBean.createDate = getDate(paramResultSet, "CREATE_DATE");
		fulOrdDataBean.updateDate = getDate(paramResultSet, "UPDATE_DATE");
		fulOrdDataBean.releaseDate = getDate(paramResultSet, "RELEASE_DATE");
		fulOrdDataBean.deliveryDate = getDate(paramResultSet, "DELIVERY_DATE");
		fulOrdDataBean.shipCarrierId = getLong(paramResultSet, "SHIP_CARRIER_ID");
		fulOrdDataBean.shipCarrierServiceId = getLong(paramResultSet, "SHIP_CARRIER_SERVICE_ID");
		fulOrdDataBean.allowPartialDelivery = getString(paramResultSet, "ALLOW_PARTIAL_DELIVERY");
		fulOrdDataBean.shipCostValue = getDouble(paramResultSet, "SHIP_COST_VALUE");
		fulOrdDataBean.shipCostCurrency = getString(paramResultSet, "SHIP_COST_CURRENCY");
		fulOrdDataBean.deliveryMode = getString(paramResultSet, "DELIVERY_MODE");
		fulOrdDataBean.deliverySlot = getString(paramResultSet, "DELIVERY_SLOT");
		return fulOrdDataBean;
	}

	@Override
	public String getDeleteSql() {
		throw new UnsupportedOperationException("This operation not supported for view");
	}

	@Override
	public String getInsertSql() {
		throw new UnsupportedOperationException("This operation not supported for view");
	}

	@Override
	public String getUpdateSql() {
		throw new UnsupportedOperationException("This operation not supported for view");
	}

	@Override
	public List<Object> toList(boolean paramBoolean) {
		List<Object> arrayList = new ArrayList<Object>();
		if (paramBoolean) {
			addToList(arrayList, this.id, 2);
		}
		addToList(arrayList, this.externalId, 12);
		addToList(arrayList, this.custOrderId, 12);
		addToList(arrayList, this.storeId, 2);
		addToList(arrayList, this.customerId, 12);
		addToList(arrayList, this.orderType, 2);
		addToList(arrayList, this.deliveryType, 2);
		addToList(arrayList, this.status, 2);
		addToList(arrayList, this.comments, 12);
		addToList(arrayList, this.custOrderDate, 91);
		addToList(arrayList, this.createDate, 91);
		addToList(arrayList, this.updateDate, 91);
		addToList(arrayList, this.releaseDate, 91);
		addToList(arrayList, this.deliveryDate, 91);
		addToList(arrayList, this.shipCarrierId, 2);
		addToList(arrayList, this.shipCarrierServiceId, 2);
		addToList(arrayList, this.allowPartialDelivery, 12);
		addToList(arrayList, this.shipCostValue, 2);
		addToList(arrayList, this.shipCostCurrency, 12);
		addToList(arrayList, this.deliveryMode, 12);
		addToList(arrayList, this.deliverySlot, 12);
		return arrayList;
	}
}
