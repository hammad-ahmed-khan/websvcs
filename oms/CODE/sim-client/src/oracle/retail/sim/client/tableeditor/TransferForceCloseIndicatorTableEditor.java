package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.transfer.TransferForceCloseIndicator;

/********************************************************************************************************
 * A table editor that allows only NL, SL or RL indicators
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferForceCloseIndicatorTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 5629868664350183651L;

    public TransferForceCloseIndicatorTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(TransferForceCloseIndicator.NO_LOSS.getCode());
        addItem(TransferForceCloseIndicator.SENDING_LOSS.getCode());
        addItem(TransferForceCloseIndicator.RECEIVING_LOSS.getCode());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
