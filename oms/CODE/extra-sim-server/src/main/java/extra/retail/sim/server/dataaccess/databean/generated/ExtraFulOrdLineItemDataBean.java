package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.retail.sim.server.dataaccess.FullDataBean;

public class ExtraFulOrdLineItemDataBean extends FullDataBean<ExtraFulOrdLineItemDataBean> {

	public static final String TABLE_NAME = "FUL_ORD_LINE_ITEM";

	public static final String COL_ID = "FUL_ORD_LINE_ITEM.ID";

	public static final String COL_FUL_ORD_ID = "FUL_ORD_LINE_ITEM.FUL_ORD_ID";

	public static final String COL_ITEM_ID = "FUL_ORD_LINE_ITEM.ITEM_ID";

	public static final String COL_SUBSTITUTE_LINE_ITEM_ID = "FUL_ORD_LINE_ITEM.SUBSTITUTE_LINE_ITEM_ID";

	public static final String COL_PREFERRED_UOM = "FUL_ORD_LINE_ITEM.PREFERRED_UOM";

	public static final String COL_COMMENTS = "FUL_ORD_LINE_ITEM.COMMENTS";

	public static final String COL_CREATE_DATE = "FUL_ORD_LINE_ITEM.CREATE_DATE";

	public static final String COL_UPDATE_DATE = "FUL_ORD_LINE_ITEM.UPDATE_DATE";

	public static final String COL_QUANTITY_ORDERED = "FUL_ORD_LINE_ITEM.QUANTITY_ORDERED";

	public static final String COL_QUANTITY_PICKED = "FUL_ORD_LINE_ITEM.QUANTITY_PICKED";

	public static final String COL_QUANTITY_DELIVERED = "FUL_ORD_LINE_ITEM.QUANTITY_DELIVERED";

	public static final String COL_QUANTITY_CANCELED = "FUL_ORD_LINE_ITEM.QUANTITY_CANCELED";

	public static final String COL_QUANTITY_RESERVED = "FUL_ORD_LINE_ITEM.QUANTITY_RESERVED";

	public static final String COL_ALLOW_SUBSTITUTION = "FUL_ORD_LINE_ITEM.ALLOW_SUBSTITUTION";

	public static final String COL_UNIT_COST_VALUE = "FUL_ORD_LINE_ITEM.UNIT_COST_VALUE";

	public static final String COL_UNIT_COST_CURRENCY = "FUL_ORD_LINE_ITEM.UNIT_COST_CURRENCY";

	public static final int TYP_ID = 2;

	public static final int TYP_FUL_ORD_ID = 2;

	public static final int TYP_ITEM_ID = 12;

	public static final int TYP_SUBSTITUTE_LINE_ITEM_ID = 2;

	public static final int TYP_PREFERRED_UOM = 12;

	public static final int TYP_COMMENTS = 12;

	public static final int TYP_CREATE_DATE = 91;

	public static final int TYP_UPDATE_DATE = 91;

	public static final int TYP_QUANTITY_ORDERED = 2;

	public static final int TYP_QUANTITY_PICKED = 2;

	public static final int TYP_QUANTITY_DELIVERED = 2;

	public static final int TYP_QUANTITY_CANCELED = 2;

	public static final int TYP_QUANTITY_RESERVED = 2;

	public static final int TYP_ALLOW_SUBSTITUTION = 12;

	public static final int TYP_UNIT_COST_VALUE = 2;

	public static final int TYP_UNIT_COST_CURRENCY = 12;

	public static final String SELECT_SQL = getSelectSqlText();

	public static final String INSERT_SQL = getInsertSqlText();

	public static final String UPDATE_SQL = getUpdateSqlText();

	public static final String DELETE_SQL = getDeleteSqlText();

	private Long id;

	private Long fulOrdId;

	private String itemId;

	private Long substituteLineItemId;

	private String preferredUom;

	private String comments;

	private Date createDate;

	private Date updateDate;

	private Double quantityOrdered;

	private Double quantityPicked;

	private Double quantityDelivered;

	private Double quantityCanceled;

	private Double quantityReserved;

	private String allowSubstitution;

	private Double unitCostValue;

	private String unitCostCurrency;

	private String serviceRequired;

	private static String getSelectSqlText() {
		return "select ID, FUL_ORD_ID, ITEM_ID, SUBSTITUTE_LINE_ITEM_ID, PREFERRED_UOM, COMMENTS, CREATE_DATE, UPDATE_DATE, QUANTITY_ORDERED, QUANTITY_PICKED, QUANTITY_DELIVERED, QUANTITY_CANCELED, QUANTITY_RESERVED, ALLOW_SUBSTITUTION, UNIT_COST_VALUE, UNIT_COST_CURRENCY, APPLY_SERVICE_IND FROM XX_SIM_APP_ORD_DTL_V@rmsdb FUL_ORD_LINE_ITEM ";

	}

	private static String getInsertSqlText() {
		return "insert into FUL_ORD_LINE_ITEM (ID, FUL_ORD_ID, ITEM_ID, SUBSTITUTE_LINE_ITEM_ID, PREFERRED_UOM, COMMENTS, CREATE_DATE, UPDATE_DATE, QUANTITY_ORDERED, QUANTITY_PICKED, QUANTITY_DELIVERED, QUANTITY_CANCELED, QUANTITY_RESERVED, ALLOW_SUBSTITUTION, UNIT_COST_VALUE, UNIT_COST_CURRENCY) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}

	private static String getUpdateSqlText() {
		return "update FUL_ORD_LINE_ITEM set FUL_ORD_ID = ?, ITEM_ID = ?, SUBSTITUTE_LINE_ITEM_ID = ?, PREFERRED_UOM = ?, COMMENTS = ?, CREATE_DATE = ?, UPDATE_DATE = ?, QUANTITY_ORDERED = ?, QUANTITY_PICKED = ?, QUANTITY_DELIVERED = ?, QUANTITY_CANCELED = ?, QUANTITY_RESERVED = ?, ALLOW_SUBSTITUTION = ?, UNIT_COST_VALUE = ?, UNIT_COST_CURRENCY = ? ";
	}

	private static String getDeleteSqlText() {
		return "delete from FUL_ORD_LINE_ITEM ";
	}

	public String getSelectSql() {
		return SELECT_SQL;
	}

	public String getInsertSql() {
		return INSERT_SQL;
	}

	public String getUpdateSql() {
		return UPDATE_SQL;
	}

	public String getDeleteSql() {
		return DELETE_SQL;
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

	public Long getFulOrdId() {
		return this.fulOrdId;
	}

	public void setFulOrdId(Long paramLong) {
		this.fulOrdId = paramLong;
	}

	public void setFulOrdId(long paramLong) {
		this.fulOrdId = Long.valueOf(paramLong);
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String paramString) {
		this.itemId = paramString;
	}

	public Long getSubstituteLineItemId() {
		return this.substituteLineItemId;
	}

	public void setSubstituteLineItemId(Long paramLong) {
		this.substituteLineItemId = paramLong;
	}

	public void setSubstituteLineItemId(long paramLong) {
		this.substituteLineItemId = Long.valueOf(paramLong);
	}

	public String getPreferredUom() {
		return this.preferredUom;
	}

	public void setPreferredUom(String paramString) {
		this.preferredUom = paramString;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String paramString) {
		this.comments = paramString;
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

	public Double getQuantityOrdered() {
		return this.quantityOrdered;
	}

	public void setQuantityOrdered(Double paramDouble) {
		this.quantityOrdered = paramDouble;
	}

	public void setQuantityOrdered(double paramDouble) {
		this.quantityOrdered = Double.valueOf(paramDouble);
	}

	public Double getQuantityPicked() {
		return this.quantityPicked;
	}

	public void setQuantityPicked(Double paramDouble) {
		this.quantityPicked = paramDouble;
	}

	public void setQuantityPicked(double paramDouble) {
		this.quantityPicked = Double.valueOf(paramDouble);
	}

	public Double getQuantityDelivered() {
		return this.quantityDelivered;
	}

	public void setQuantityDelivered(Double paramDouble) {
		this.quantityDelivered = paramDouble;
	}

	public void setQuantityDelivered(double paramDouble) {
		this.quantityDelivered = Double.valueOf(paramDouble);
	}

	public Double getQuantityCanceled() {
		return this.quantityCanceled;
	}

	public void setQuantityCanceled(Double paramDouble) {
		this.quantityCanceled = paramDouble;
	}

	public void setQuantityCanceled(double paramDouble) {
		this.quantityCanceled = Double.valueOf(paramDouble);
	}

	public Double getQuantityReserved() {
		return this.quantityReserved;
	}

	public void setQuantityReserved(Double paramDouble) {
		this.quantityReserved = paramDouble;
	}

	public void setQuantityReserved(double paramDouble) {
		this.quantityReserved = Double.valueOf(paramDouble);
	}

	public String getAllowSubstitution() {
		return this.allowSubstitution;
	}

	public void setAllowSubstitution(String paramString) {
		this.allowSubstitution = paramString;
	}

	public Double getUnitCostValue() {
		return this.unitCostValue;
	}

	public void setUnitCostValue(Double paramDouble) {
		this.unitCostValue = paramDouble;
	}

	public void setUnitCostValue(double paramDouble) {
		this.unitCostValue = Double.valueOf(paramDouble);
	}

	public String getUnitCostCurrency() {
		return this.unitCostCurrency;
	}

	public void setUnitCostCurrency(String paramString) {
		this.unitCostCurrency = paramString;
	}

	public String getServiceRequired() {
		return serviceRequired;
	}

	public void setServiceRequired(String serviceRequired) {
		this.serviceRequired = serviceRequired;
	}

	public ExtraFulOrdLineItemDataBean read(ResultSet paramResultSet) throws SQLException {
		ExtraFulOrdLineItemDataBean fulOrdLineItemDataBean = new ExtraFulOrdLineItemDataBean();
		fulOrdLineItemDataBean.id = getLong(paramResultSet, "ID");
		fulOrdLineItemDataBean.fulOrdId = getLong(paramResultSet, "FUL_ORD_ID");
		fulOrdLineItemDataBean.itemId = getString(paramResultSet, "ITEM_ID");
		fulOrdLineItemDataBean.substituteLineItemId = getLong(paramResultSet, "SUBSTITUTE_LINE_ITEM_ID");
		fulOrdLineItemDataBean.preferredUom = getString(paramResultSet, "PREFERRED_UOM");
		fulOrdLineItemDataBean.comments = getString(paramResultSet, "COMMENTS");
		fulOrdLineItemDataBean.createDate = getDate(paramResultSet, "CREATE_DATE");
		fulOrdLineItemDataBean.updateDate = getDate(paramResultSet, "UPDATE_DATE");
		fulOrdLineItemDataBean.quantityOrdered = getDouble(paramResultSet, "QUANTITY_ORDERED");
		fulOrdLineItemDataBean.quantityPicked = getDouble(paramResultSet, "QUANTITY_PICKED");
		fulOrdLineItemDataBean.quantityDelivered = getDouble(paramResultSet, "QUANTITY_DELIVERED");
		fulOrdLineItemDataBean.quantityCanceled = getDouble(paramResultSet, "QUANTITY_CANCELED");
		fulOrdLineItemDataBean.quantityReserved = getDouble(paramResultSet, "QUANTITY_RESERVED");
		fulOrdLineItemDataBean.allowSubstitution = getString(paramResultSet, "ALLOW_SUBSTITUTION");
		fulOrdLineItemDataBean.unitCostValue = getDouble(paramResultSet, "UNIT_COST_VALUE");
		fulOrdLineItemDataBean.unitCostCurrency = getString(paramResultSet, "UNIT_COST_CURRENCY");
		fulOrdLineItemDataBean.serviceRequired = getString(paramResultSet, "APPLY_SERVICE_IND");
		return fulOrdLineItemDataBean;
	}

	public List<Object> toList(boolean paramBoolean) {
		List<Object> arrayList = new ArrayList<Object>();
		if (paramBoolean)
			addToList(arrayList, this.id, 2);
		addToList(arrayList, this.fulOrdId, 2);
		addToList(arrayList, this.itemId, 12);
		addToList(arrayList, this.substituteLineItemId, 2);
		addToList(arrayList, this.preferredUom, 12);
		addToList(arrayList, this.comments, 12);
		addToList(arrayList, this.createDate, 91);
		addToList(arrayList, this.updateDate, 91);
		addToList(arrayList, this.quantityOrdered, 2);
		addToList(arrayList, this.quantityPicked, 2);
		addToList(arrayList, this.quantityDelivered, 2);
		addToList(arrayList, this.quantityCanceled, 2);
		addToList(arrayList, this.quantityReserved, 2);
		addToList(arrayList, this.allowSubstitution, 12);
		addToList(arrayList, this.unitCostValue, 2);
		addToList(arrayList, this.unitCostCurrency, 12);
		return arrayList;
	}
}
