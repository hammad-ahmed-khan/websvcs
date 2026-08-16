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
 * TransferReturnApprovalDataBean.java aibrahim 2024
 */
public class StockRquestReportDatabean extends FullDataBean<StockRquestReportDatabean> {


	public static final String SELECT_REPORT_SQL = getSelectReportSqlText();

	private String sequenceNo;

	private String serialNo;

	private Long requestLoc;

	private String technicianId;

	private String item;

	private Quantity qty;

	private String status;

	private Long sequenceId;

	private Date requestDate;

	private String comments;

	private Date appRejTime;
	
	private String 	itemDescription;
	
	private String brandName;
	
	private String bin;
	
	private String requestType;

	private static String getSelectReportSqlText() {
		return "SELECT TSF_REQ_SEQ_ID, SRV_REQ_ID, REQ_LOC, REQ_TECH_ID, LINE_ID, ITEM, ITEM_DESC, BRAND_NAME, QTY, TYPE, SP_SEQ_NO, CREATION_TIME, APPR_REJ_TIME, SP_COMMENTS, BIN, STATUS FROM XX_SP_APPR_V ";
	}

	@Override
	public String getSelectSql() {
		return SELECT_REPORT_SQL;
	}

	@Override
	public StockRquestReportDatabean read(ResultSet rs) throws SQLException {
		StockRquestReportDatabean bean = new StockRquestReportDatabean(); 
		bean.serialNo = rs.getString("SRV_REQ_ID");
		bean.sequenceNo = rs.getString("TSF_REQ_SEQ_ID");
		bean.technicianId = rs.getString("REQ_TECH_ID");
		bean.requestLoc = rs.getLong("REQ_LOC");
		bean.item = rs.getString("ITEM");
		bean.itemDescription = rs.getString("ITEM_DESC");
		bean.brandName = rs.getString("BRAND_NAME");	
		bean.qty = new Quantity(rs.getBigDecimal("QTY"));
		bean.sequenceId = rs.getLong("SP_SEQ_NO");
		bean.requestDate = rs.getTimestamp("CREATION_TIME");
		bean.appRejTime = rs.getTimestamp("APPR_REJ_TIME");
		bean.comments = rs.getString("SP_COMMENTS");
		bean.bin = rs.getString("BIN");
		bean.status = rs.getString("STATUS");
		bean.requestType = rs.getString("TYPE");
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	

	public String getRequestType() {
		return requestType;
	}


	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}


	public Date getAppRejTime() {
		return appRejTime;
	}

	public void setLastDate(Date appRejTime) {
		this.appRejTime = appRejTime;
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
	public List<Object> toList(boolean arg0) {
		List<Object> arrayList = new ArrayList<>();
		addToList(arrayList, this.status, Types.NUMERIC);
		addToList(arrayList, this.comments, Types.VARCHAR);
		addToList(arrayList, this.sequenceNo, Types.INTEGER);
		return arrayList;
	}
}
