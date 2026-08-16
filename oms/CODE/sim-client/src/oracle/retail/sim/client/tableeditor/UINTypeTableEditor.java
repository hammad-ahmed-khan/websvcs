package oracle.retail.sim.client.tableeditor;

import java.util.List;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * A table editor for UINType that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class UINTypeTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 5387844164786286355L;

    public UINTypeTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
    }

    public void setTypes(List type) {
        removeItemListener(this);
        setItems(type);
        addItemListener(this);
    }

    public Class<UINType> getValueClass() {
        return UINType.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignored
    }

    public UINType getType() {
        return (UINType) getValue();
    }
}
