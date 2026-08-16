package oracle.retail.sim.client.tableeditor;

import java.util.List;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;

/********************************************************************************************************
 * A table editor for delivery time slots that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DeliveryTimeSlotTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = -845222776485037913L;

    public DeliveryTimeSlotTableEditor() {
        setDisplayer(new AttributeDisplayer("description"));
        setSelectionRequired(true);
        setSortEnabled(false);
        removeEmptySelection();
    }

    public void setDeliveryTimeslot(List<DeliveryTimeSlot> deliveryTimeSlots) {
        removeItemListener(this);
        setItems(deliveryTimeSlots);
        removeEmptySelection();
        addItemListener(this);
    }

    public DeliveryTimeSlot getDeliveryTimeSlot() {
        return (DeliveryTimeSlot) getSelectedItem();
    }

    public Class getValueClass() {
        return DeliveryTimeSlot.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }
}
