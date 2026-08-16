package extra.retail.sim.client.swing.table;

import java.util.List;

import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableDefinition;

/**
 * ExtraSimTableDefinition.java aibrahim 2024
 */
public class ExtraSimTable<T> extends SimTable {

	private static final long serialVersionUID = -4706543839249449991L;

	public ExtraSimTable(SimTableDefinition tableDefinition) {
		super(tableDefinition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllSelectedRowData() {
		return super.getAllSelectedRowData();
	}
}
