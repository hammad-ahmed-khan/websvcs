package oracle.retail.sim.client.tableeditor;

import java.util.List;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.uin.UINLabelVO;

/********************************************************************************************************
 * A table editor for UINLabel that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class UINLabelTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 5387844164786286355L;

    public UINLabelTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
    }

    public void setLabels(List<UINLabelVO> labels) {
        removeItemListener(this);
        setItems(labels);
        addItemListener(this);
    }

    public Class<UINLabelVO> getValueClass() {
        return UINLabelVO.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignored
    }

    public UINLabelVO getType() {
        return (UINLabelVO) getValue();
    }
}
