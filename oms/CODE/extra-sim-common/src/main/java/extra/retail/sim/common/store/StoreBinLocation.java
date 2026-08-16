package extra.retail.sim.common.store;

import java.io.Serializable;

/**
 * StoreBinLocation.java
 * aibrahim
 * 2024
 */
public class StoreBinLocation implements Serializable {

	private static final long serialVersionUID = 4247469024975578936L;

	private Long storeId;

	private Integer bin;

	public Long getStoreId() {
		return storeId;
	}

	public Integer getBin() {
		return bin;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setBin(Integer bin) {
		this.bin = bin;
	}
}
