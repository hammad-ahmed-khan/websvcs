package extra.retail.sim.server.dataaccess.databean.generated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.server.dataaccess.FullDataBean;

/**
 * ReturnRequestDataBean.java
 * aibrahim
 * 2024
 */
public class ReturnRequestItemDataBean extends FullDataBean<ReturnRequestItemDataBean> {

	public static final String TABLE_NAME = "XX_SP_RTV_REQ";

	public static final String SELECT_SQL = getSelectSqlText();

	public static final String INSERT_SQL = getInsertSqlText();

	public static final String UPDATE_SQL = getUpdateSqlText();

	private Long returnId;

	private Long storeId;

	private String sourceId;

	private String itemId;

	private Quantity qty;

	private String createdBy;

	private Date createdDate;

	private String status;

	private String updatedBy;

	private Date updatedDate;

	private String supplierName;

	private String itemDescription;

	private String brandName;

	private static String getSelectSqlText() {
		return "SELECT A.ID AS RETURN_ID, A.STORE_ID AS LOCATION, A.SOURCE_ID AS SUPPLIER_ID, S.NAME AS SUPPLIER_NAME, A.ITEM_ID, I.LONG_DESCRIPTION AS ITEM_DESC, A.QUANTITY, I.BRAND, A.CREATE_USER, A.CREATE_DATE, A.APPROVED_BY FROM XX_SP_RTV_REQ A, SUPPLIER S, ITEM I WHERE A.SOURCE_ID = S.ID AND A.ITEM_ID = I.ITEM_ID AND A.STATUS='NEW'";
	}

	private static String getUpdateSqlText() {
		return "UPDATE XX_SP_RTV_REQ SET APPRV_REJ_DATETIME = SYSDATE, APPROVED_BY = ?, STATUS = ?, UPDATED_DATE = SYSTIMESTAMP WHERE ID = ? ";
	}

	private static String getInsertSqlText() {
		return "INSERT INTO XX_SP_RTV_REQ(ID, STORE_ID, SOURCE_ID, ITEM_ID, QUANTITY, CREATE_USER, CREATE_DATE, STATUS) VALUES(?, ?, ?, ?, ?, ?, SYSDATE, ?) ";
	}

	@Override
	public String getDeleteSql() {
		throw new UnsupportedOperationException("Can't delete entry in XX_SP_RTV_REQ");
	}

	@Override
	public String getInsertSql() {
		return INSERT_SQL;
	}

	@Override
	public String getUpdateSql() {
		return UPDATE_SQL;
	}

	@Override
	public List<Object> toList(boolean isNew) {
		List<Object> arrayList = new ArrayList<>();
		if (isNew) {
			addToList(arrayList, this.returnId, Types.NUMERIC);
			addToList(arrayList, this.storeId, Types.NUMERIC);
			addToList(arrayList, this.sourceId, Types.VARCHAR);
			addToList(arrayList, this.itemId, Types.VARCHAR);
			addToList(arrayList, this.qty.getBigDecimal(), Types.NUMERIC);
			addToList(arrayList, this.createdBy, Types.VARCHAR);
			addToList(arrayList, this.status, Types.VARCHAR);
		} else {
			addToList(arrayList, this.updatedBy, Types.VARCHAR);
			addToList(arrayList, this.status, Types.VARCHAR);
			addToList(arrayList, this.returnId, Types.NUMERIC);
		}
		return arrayList;
	}

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	@Override
	public ReturnRequestItemDataBean read(ResultSet rs) throws SQLException {
		ReturnRequestItemDataBean bean = new ReturnRequestItemDataBean();
		bean.returnId = rs.getLong("RETURN_ID");
		bean.storeId = rs.getLong("LOCATION");
		bean.sourceId = rs.getString("SUPPLIER_ID");
		bean.supplierName = rs.getString("SUPPLIER_NAME");
		bean.createdBy = rs.getString("CREATE_USER");
		bean.createdDate = rs.getTimestamp("CREATE_DATE");
		bean.itemId = rs.getString("ITEM_ID");
		bean.itemDescription = rs.getString("ITEM_DESC");
		bean.brandName = rs.getString("BRAND");
		bean.qty = new Quantity(rs.getBigDecimal("QUANTITY"));
		return bean;
	}

	public Long getReturnId() {
		return returnId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public String getItemId() {
		return itemId;
	}

	public Quantity getQty() {
		return qty;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getStatus() {
		return status;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setQty(Quantity qty) {
		this.qty = qty;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
}
