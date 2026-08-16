package oracle.retail.sim.client.swing.event;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.table.TableCellRenderer;
import oracle.retail.sim.client.swing.displayer.DefaultDisplayer;
import oracle.retail.sim.client.swing.table.DisplayerTableCellRenderer;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayer;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class SimTableTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 8030064888030847840L;

    private Displayer displayer = new DefaultDisplayer();

    public int getSourceActions(JComponent c) {
        if (c instanceof SimTable) {
            return COPY;
        }
        return super.getSourceActions(c);
    }

    protected Transferable createTransferable(JComponent c) {
        if (c instanceof SimTable) {
            return new StringSelection(getSelection((SimTable) c));
        }
        return super.createTransferable(c);
    }

    private String getSelection(SimTable table) {
        if (!table.getRowSelectionAllowed()) {
            return StringConstants.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        int columnCount = table.getColumnCount();
        for (int row : table.getSelectedRows()) {
            for (int column = 0; column < columnCount; column++) {
                sb.append(getValueString(table.getValueAt(row, column), table.getCellRenderer(row, column)));
                sb.append(column + 1 < columnCount ? StringConstants.COMMA : StringConstants.EOL);
            }
        }
        return sb.toString();
    }

    private String getValueString(Object value, TableCellRenderer renderer) {
        return getValueDisplayer(renderer).getDisplayText(value, null);
    }

    private Displayer getValueDisplayer(TableCellRenderer renderer) {
        if (renderer instanceof DisplayerTableCellRenderer) {
            return ((DisplayerTableCellRenderer) renderer).getDisplayer();
        }
        return displayer;
    }
}
