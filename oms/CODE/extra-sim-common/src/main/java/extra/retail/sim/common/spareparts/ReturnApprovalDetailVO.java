package extra.retail.sim.common.spareparts;
 
import java.io.Serializable;

import oracle.retail.sim.common.business.Quantity;
 
public class ReturnApprovalDetailVO implements Serializable {
 
	private static final long serialVersionUID = -2781137671891645722L;

	private long returnId;
 
	private long storeId;
 
	private String sourceId;
 
	private String itemId;
 
	private Quantity qty;
 
	private String itemDescription;
 
	private String brandName;
 
	public long getReturnId() {
		return returnId;
	}
 
	public void setReturnId(long returnId) {
		this.returnId = returnId;
	}
 
	public long getStoreId() {
		return storeId;
	}
 
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
 
	public String getSourceId() {
		return sourceId;
	}
 
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
 
	public String getItemId() {
		return itemId;
	}
 
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
 
	public Quantity getQty() {
		return qty;
	}
 
	public void setQty(Quantity qty) {
		this.qty = qty;
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
}