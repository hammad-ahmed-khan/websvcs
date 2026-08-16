package extra.retail.sim.common.spareparts;

import java.util.Date;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class TransferReturnRequestQueryFilter extends BusinessObject implements QueryFilter {

	private static final long serialVersionUID = 5945806043189117102L;

	private Date fromDate;

	private Date toDate;

	private String srNumber;

	private String itemId;

	private String brand;

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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
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

	public String getSrNumber() {
		return srNumber;
	}

	public String getBrand() {
		return brand;
	}

	public void setSrNumber(String srNumber) {
		this.srNumber = srNumber;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}
