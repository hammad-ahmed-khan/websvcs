package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.store.ItemBasketIndicator;

public class ItemBasketTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = -3895359521438690284L;

    public ItemBasketTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
        addItem(ItemBasketIndicator.AUTOMATIC.toString());
        addItem(ItemBasketIndicator.MANUAL.toString());
        removeEmptySelection();
        setValueClass(String.class);
    }
}
