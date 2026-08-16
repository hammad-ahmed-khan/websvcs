package extra.retail.sim.common.spareparts;

import java.io.Serializable;

import oracle.retail.sim.common.business.Quantity;

/**
 * StockRequestRetuenDetailVO.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnDetailVO implements Serializable {

	private static final long serialVersionUID = 3589977273027565438L;

	private String item;

	private Quantity qty;

	private String 	itemDescription;
	
	private String brandName;
	
	private String bin; 

	private Quantity available;

	private Quantity techSubBucket;

	private StockRequestReturnVO requestReturnVO;
	
	public String getItem() {
		return item;
	}

	public Quantity getQty() {
		return qty;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public void setQty(Quantity qty) {
		this.qty = qty;
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

	public Quantity getAvailable() {
		return available;
	}

	public Quantity getTechSubBucket() {
		return techSubBucket;
	}

	public void setAvailable(Quantity available) {
		this.available = available;
	}

	public void setTechSubBucket(Quantity techSubBucket) {
		this.techSubBucket = techSubBucket;
	}

	public StockRequestReturnVO getRequestReturnVO() {
		return requestReturnVO;
	}

	public void setRequestReturnVO(StockRequestReturnVO requestReturnVO) {
		this.requestReturnVO = requestReturnVO;
	}
}
