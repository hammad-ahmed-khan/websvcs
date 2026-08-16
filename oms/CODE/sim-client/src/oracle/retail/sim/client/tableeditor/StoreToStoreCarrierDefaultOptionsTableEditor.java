package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.shipment.ShipmentCarrierRole;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of auto receiving options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StoreToStoreCarrierDefaultOptionsTableEditor extends RComboBoxTableEditor {

	private static final long serialVersionUID = 155226454558394350L;

	public StoreToStoreCarrierDefaultOptionsTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(ShipmentCarrierRole.SENDER.toString());
        addItem(ShipmentCarrierRole.RECEIVER.toString());
        addItem(ShipmentCarrierRole.THIRD_PARTY.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
