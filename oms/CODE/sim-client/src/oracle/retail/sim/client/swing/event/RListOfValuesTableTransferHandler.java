package oracle.retail.sim.client.swing.event;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import oracle.retail.sim.client.swing.displayer.DefaultDisplayer;
import oracle.retail.sim.client.swing.lov.RListOfValuesTable;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayer;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class RListOfValuesTableTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 5091965553423319389L;

    private Displayer displayer = new DefaultDisplayer();

    public int getSourceActions(JComponent c) {
        if (c instanceof RListOfValuesTable) {
            return COPY;
        }
        return super.getSourceActions(c);
    }

    protected Transferable createTransferable(JComponent c) {
        if (c instanceof RListOfValuesTable) {
            return new StringSelection(getSelection((RListOfValuesTable) c));
        }
        return super.createTransferable(c);
    }

    private String getSelection(RListOfValuesTable table) {
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
