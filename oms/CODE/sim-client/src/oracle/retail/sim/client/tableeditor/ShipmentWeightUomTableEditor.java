package oracle.retail.sim.client.tableeditor;

import java.util.List;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.shipment.ShipmentWeightUom;

/********************************************************************************************************
 * A table editor that allows a combo box style selection of shipping weight unit of measures.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ShipmentWeightUomTableEditor extends RComboBoxTableEditor {

    private static final long serialVersionUID = -5217994322094579988L;

    public ShipmentWeightUomTableEditor(List<ShipmentWeightUom> uoms) {
        setDisplayer(new TranslatedObjectDisplayer());
        setValueClass(String.class);
        for (ShipmentWeightUom uom : uoms) {
            addItem(uom.toString());
        }
        //setItems(uoms);
        removeEmptySelection();
        addItemListener(this);
    }
}
