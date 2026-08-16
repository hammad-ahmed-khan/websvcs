package extra.retail.sim.common.item;

import java.io.Serializable;
import java.util.Date;

/**
 * ItemBinLocation.java
 * aibrahim
 * 2024
 */
public class ItemBinLocation implements Serializable {

	private static final long serialVersionUID = 1642088856898629895L;

	private Long storeId;

	private String item;

	private Integer bin;

	private Date createdDate;

	private Date updatedDate;

	public Long getStoreId() {
		return storeId;
	}

	public String getItem() {
		return item;
	}

	public Integer getBin() {
		return bin;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setBin(Integer bin) {
		this.bin = bin;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
