package extra.retail.sim.client.swing.tableeditor;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;

public class IMEITableEditor extends RTextField implements SimTableEditor {
	private static final long serialVersionUID = 7639639868321826798L;

	private Class classType;
	private Object model;
	private SimTableEditorEventAdaptor eventAdaptor;
	private int row = -1;
	private int column = -1;
	private boolean isNullable;
	private String identifier = "";

	public IMEITableEditor() {
		this(false);
	}

	public IMEITableEditor(boolean isNullable) {
		eventAdaptor = new SimTableEditorEventAdaptor(this);
		setIdentifier("IMEINumber", true);
		setIsNullable(isNullable);
		addFocusListener(createFocusListener());
	}

	/****************************************************************************************************
	 * Basic Property Methods of a Table Editor
	 ***************************************************************************************************/

	public Class getValueClass() {
		return classType;
	}

	public void setValueClass(Class valueClass) {
		// System.out.println("inside QuantityTableEditor setValueClass");
		classType = valueClass;
	}

	public void setModel(Object model) {
		// System.out.println("inside QuantityTableEditor setModel :
		// "+model.toString());
		this.model = model;
	}

	protected Object getModel() {
		// System.out.println("inside QuantityTableEditor getModel");
		return model;
	}

	public JComponent getComponent() {
		return this;
	}

	public void setIsNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	/****************************************************************************************************
	 * Assign coordinates to the editor.
	 ***************************************************************************************************/
	public void setCoordinates(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void setIdentifier(String identifier, boolean validateLength) {
		super.setIdentifier(identifier, validateLength);
		if (identifier == null) {
			identifier = StringConstants.EMPTY;
		}
		if (validateLength) {
			setLength(25);
		}
		this.identifier = identifier;
	}

	/****************************************************************************************************
	 * Reactivate editing within the table cell.
	 ***************************************************************************************************/
	private void reactivateEditing(Object object) {
		if (object instanceof SimTable) {
			SimTable table = (SimTable) object;
			try {
				if (table.getSelectedRow() != row) {
					table.setRowSelectionInterval(row, row);
				}
				table.editCellAt(row, column);
			} catch (Throwable ex) {
				// UILog.debug(getClass(), ex.getMessage());
			}
		}
	}

	/****************************************************************************************************
	 * Table Editor Listener
	 ***************************************************************************************************/

	public void addTableEditorListener(SimTableEditorListener listener) {
		eventAdaptor.addTableEditorListener(listener);
	}

	public void removeTableEditorListener(SimTableEditorListener listener) {
		eventAdaptor.removeTableEditorListener(listener);
	}

	private FocusListener createFocusListener() {
		return new FocusAdapter() {
			public void focusLost(FocusEvent event) {
				if (event.isTemporary()) {
					return;
				}
				if (checkValue()) {
					eventAdaptor.fireTypeEditorEvent();
					return;
				}
				reactivateEditing(event.getOppositeComponent());
			}
		};
	}

	public boolean isInvalidKeystroke(KeyEvent event) {
		return false;
	}

	protected void displayError(String message) {
		RErrorDialog dialog = new RErrorDialog(Application.getFrame());
		dialog.setTitle("Table Editor Error");
		dialog.setMessage(CommonMessageText.ACTION_INVALID);
		dialog.activate();
	}

	/****************************************************************************************************
	 * Get, Set and Check the data value of the editor
	 ***************************************************************************************************/

	/****************************************************************************************************
	 * Get, Set and Check the data value of the editor
	 ***************************************************************************************************/

	public void setValue(Object value) {
		if (value == null) {
			clear();
			return;
		}
		setText(value.toString());
		eventAdaptor.fireTypeEditorEvent();
	}

	public Object getValue() {
		return getText();
	}

	public boolean checkValue() {
		Object value = getValue();
		if (value == null) {
			if (!isNullable) {
				displayError("IMEI number can not be blank");
			}
			return isNullable;
		}
		return true;
	}

}
