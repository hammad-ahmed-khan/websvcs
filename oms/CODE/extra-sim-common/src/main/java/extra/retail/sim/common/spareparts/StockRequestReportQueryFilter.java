package extra.retail.sim.common.spareparts;

import java.util.Date;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;
import oracle.retail.sim.common.rules.fulfillmentorder.FulfillmentOrderMgmtSearchLimitRule;

public class StockRequestReportQueryFilter extends BusinessObject implements QueryFilter {
	private static final long serialVersionUID = 796445764306773407L;

	private Date fromDate;

	private Date toDate;

	private String serialNo;

	private String requestType;

	private String itemId;

	private String technicianId;

	private String status;

	private Integer searchLimit = Integer.valueOf(999);

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(String technicianId) {
		this.technicianId = technicianId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSearchLimit() {
		return this.searchLimit.intValue();
	}

	public void setSearchLimit(int paramInt) throws BusinessException {
		FulfillmentOrderMgmtSearchLimitRule.execute(Integer.valueOf(paramInt));
		executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt) });
		doSetSearchLimit(paramInt);
	}

	public void doSetSearchLimit(int paramInt) {
		this.searchLimit = Integer.valueOf(paramInt);
	}

	public void setDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
		DateRangeValidRule.execute(paramDate1, paramDate2);
		executeRule("setDateRange", new Object[] { paramDate1, paramDate2 });
		doSetDateRange(paramDate1, paramDate2);
	}

	public void doSetDateRange(Date paramDate1, Date paramDate2) {
		this.fromDate = paramDate1;
		this.toDate = paramDate2;
	}

}
