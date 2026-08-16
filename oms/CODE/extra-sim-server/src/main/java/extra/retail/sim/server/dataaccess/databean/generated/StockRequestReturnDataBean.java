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
 * TransferReturnApprovalDataBean.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnDataBean extends FullDataBean<StockRequestReturnDataBean> {

	public static final String TABLE_NAME = "XX_SP_STOCK_REQ_RET";

	public static final String SELECT_SQL = getSelectSqlText();

	public static final String UPDATE_SQL = getUpdateSqlText();

	private String sequenceNo;

	private String serialNo;

	private Long requestLoc;

	private String technicianId;

	private String item;

	private Quantity qty;

	private String status;

	private Quantity available;

	private Quantity techSubBucket;

	private Long sequenceId;

	private Date requestDate;

	private String comments;

	private Date lastDate;
	
	private String 	itemDescription;
	
	private String brandName;
	
	private String bin;

	private String updatedBy;
	
	private static String getSelectSqlText() {
		return "SELECT TSF_REQ_SEQ_ID, SRV_REQ_ID, REQ_LOC, REQ_TECH_ID, LINE_ID, ITEM, QTY, ITEM_DESC, BRAND_NAME, SP_SEQ_NO, TYPE, AVAIL_TECH_SUB, AVAIL_QTY, CREATION_TIME, LAST_UPDATED_TIME, SP_COMMENTS, BIN FROM XX_SP_STK_RET_V";
	}

	private static String getUpdateSqlText() {
		return "UPDATE XX_SP_STOCK_REQ_RET@rmsdb SET STATUS = ?, APPR_REJ_TIME = SYSTIMESTAMP, SP_COMMENTS = ?, LAST_UPDATED_TIME = SYSTIMESTAMP, USER_ID = ? WHERE TSF_REQ_SEQ_ID = ? ";
	}

	@Override
	public String getSelectSql() {
		return SELECT_SQL;
	}

	@Override
	public StockRequestReturnDataBean read(ResultSet rs) throws SQLException {
		StockRequestReturnDataBean bean = new StockRequestReturnDataBean();
		bean.serialNo = rs.getString("SRV_REQ_ID");
		bean.sequenceNo = rs.getString("TSF_REQ_SEQ_ID");
		bean.technicianId = rs.getString("REQ_TECH_ID");
		bean.requestLoc = rs.getLong("REQ_LOC");
		bean.item = rs.getString("ITEM");
		bean.qty = new Quantity(rs.getBigDecimal("QTY"));
		bean.itemDescription = rs.getString("ITEM_DESC");
		bean.brandName = rs.getString("BRAND_NAME");
		bean.available = new Quantity(rs.getBigDecimal("AVAIL_QTY"));
		bean.techSubBucket = new Quantity(rs.getBigDecimal("AVAIL_TECH_SUB"));
		bean.sequenceId = rs.getLong("SP_SEQ_NO");
		bean.requestDate = rs.getTimestamp("CREATION_TIME");
		bean.lastDate = rs.getTimestamp("LAST_UPDATED_TIME");
		bean.comments = rs.getString("SP_COMMENTS");
		bean.bin = rs.getString("BIN");
		return bean;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public Long getRequestLoc() {
		return requestLoc;
	}

	public String getTechnicianId() {
		return technicianId;
	}

	public String getItem() {
		return item;
	}

	public Quantity getQty() {
		return qty;
	}

	@Override
	public String getDeleteSql() {
		throw new UnsupportedOperationException("Can't delete entry in XX_SP_STOCK_REQ_RET");
	}

	@Override
	public String getInsertSql() {
		throw new UnsupportedOperationException("Can't add entry in XX_SP_STOCK_REQ_RET");
	}

	@Override
	public String getUpdateSql() {
		return UPDATE_SQL;
	}

	@Override
	public List<Object> toList(boolean arg0) {
		List<Object> arrayList = new ArrayList<>();
		addToList(arrayList, this.status, Types.NUMERIC);
		addToList(arrayList, this.comments, Types.VARCHAR);
		addToList(arrayList, this.updatedBy, Types.VARCHAR);
		addToList(arrayList, this.sequenceNo, Types.INTEGER);
		return arrayList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Quantity getAvailable() {
		return available;
	}

	public Quantity getTechSubBucket() {
		return techSubBucket;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Long getSequenceId() {
		return sequenceId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public String getComments() {
    	return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public void setUpdatedBy(String userName) {
		this.updatedBy = userName;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}
}
