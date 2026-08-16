package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.shipment.ShipmentCarrierRole;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of handheld picking mode options.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DefaultShipmentCarrierRoleTableEditor extends RComboBoxTableEditor {

    private static final long serialVersionUID = 2231515645271979253L;

    public DefaultShipmentCarrierRoleTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(ShipmentCarrierRole.THIRD_PARTY.toString());
        addItem(ShipmentCarrierRole.SENDER.toString());
        addItem(ShipmentCarrierRole.RECEIVER.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
