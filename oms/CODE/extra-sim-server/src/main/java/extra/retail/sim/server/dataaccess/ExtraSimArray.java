package extra.retail.sim.server.dataaccess;

import java.util.List;
import java.util.Map;

/**
 * ExtraSimArray.java
 * aibrahim
 * 2023
 */
public class ExtraSimArray {

	private String typeName;

	private List<Map<Integer, Object>> list;

	private int argumentLength;

	public ExtraSimArray(String typeName, int argumentLength, List<Map<Integer, Object>> list) {
		this.typeName = typeName;
		this.argumentLength = argumentLength;
		this.list = list;
	}

	public List<Map<Integer, Object>> getList() {
		return list;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getArgumentLength() {
		return argumentLength;
	}
}
