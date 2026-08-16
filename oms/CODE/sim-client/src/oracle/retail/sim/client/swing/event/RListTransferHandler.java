package oracle.retail.sim.client.swing.event;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import oracle.retail.sim.client.swing.displayer.DefaultDisplayer;
import oracle.retail.sim.client.swing.widget.RList;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayer;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class RListTransferHandler extends TransferHandler {
    private static final long serialVersionUID = -1497099370741022418L;

    private Displayer displayer = new DefaultDisplayer();

    public int getSourceActions(JComponent c) {
        if (c instanceof RList) {
            return COPY;
        }
        return super.getSourceActions(c);
    }

    protected Transferable createTransferable(JComponent c) {
        if (c instanceof RList) {
            return new StringSelection(getSelection((RList) c));
        }
        return super.createTransferable(c);
    }

    private String getSelection(RList list) {
        if (list.isSelectionEmpty()) {
            return StringConstants.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        Displayer rowDisplayer = list.getRowDisplayer();
        for (Object row : list.getSelectedValues()) {
            sb.append(getValueString(row, rowDisplayer));
            sb.append(StringConstants.EOL);
        }
        return sb.toString();
    }

    private String getValueString(Object value, Displayer rowDisplayer) {
        return getValueDisplayer(rowDisplayer).getDisplayText(value, null);
    }

    private Displayer getValueDisplayer(Displayer rowDisplayer) {
        if (rowDisplayer != null) {
            return rowDisplayer;
        }
        return displayer;
    }
}
