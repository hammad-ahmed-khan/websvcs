package extra.retail.sim.common.baselv;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author aibrahim
 * 
 */
public class BaseLV implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6518142844492226173L;

	private Long id;

	private String listCode;

	private String key;

	private String value;

	public Long getId() {
		return id;
	}

	public String getListCode() {
		return listCode;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseLV other = (BaseLV) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return value;
	}

	public static BaseLV findItem(List<BaseLV> collections, String key) {
		BaseLV baseLV = null;
		if (collections != null) {
			for (BaseLV base : collections) {
				if (base.getKey().equals(key)) {
					return base;
				}
			}
		}
		return baseLV;
	}
}
