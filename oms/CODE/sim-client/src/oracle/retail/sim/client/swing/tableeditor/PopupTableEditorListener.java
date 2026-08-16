package oracle.retail.sim.client.swing.tableeditor;

/********************************************************************************************************
 * This interface must be implemented by objects that wish to receive popup notifications from the
 * CheckIndicatorTableEditor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface PopupTableEditorListener {

    void popupDialog(Object model);
}
