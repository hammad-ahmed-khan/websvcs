package oracle.retail.sim.client.swing.event;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import oracle.retail.sim.client.swing.displayer.DefaultDisplayer;
import oracle.retail.sim.client.swing.displaytable.RDisplayTable;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayer;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class RDisplayTableTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 1565520642556302408L;

    private Displayer displayer = new DefaultDisplayer();

    public int getSourceActions(JComponent c) {
        if (c instanceof RDisplayTable) {
            return COPY;
        }
        return super.getSourceActions(c);
    }

    protected Transferable createTransferable(JComponent c) {
        if (c instanceof RDisplayTable) {
            return new StringSelection(getSelection((RDisplayTable) c));
        }
        return super.createTransferable(c);
    }

    private String getSelection(RDisplayTable table) {
        if (!table.getRowSelectionAllowed()) {
            return StringConstants.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        int columnCount = table.getColumnCount();
        for (int row : table.getSelectedRows()) {
            for (int column = 0; column < columnCount; column++) {
                sb.append(getValueString(table.getValueAt(row, column)));
                sb.append(column + 1 < columnCount ? StringConstants.COMMA : StringConstants.EOL);
            }
        }
        return sb.toString();
    }

    private String getValueString(Object value) {
        return displayer.getDisplayText(value, null);
    }
}
