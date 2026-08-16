package extra.retail.sim.client.common.model;

import java.util.List;

import oracle.retail.sim.common.core.type.Displayable;

/**
 * @author aibrahim
 *
 */
public class BaseLV implements Displayable {

	private Long id;

	private String listCode;

	private String key;

	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toDisplayString() {
		return value;
	}

	public String getListCode() {
		return listCode;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
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
