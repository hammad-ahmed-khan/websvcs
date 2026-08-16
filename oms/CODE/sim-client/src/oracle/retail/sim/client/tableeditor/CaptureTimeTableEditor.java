package oracle.retail.sim.client.tableeditor;

import java.util.List;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.uin.UINCaptureTime;

/********************************************************************************************************
 * A table editor for UINCaptureTime that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class CaptureTimeTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 5387844164786286355L;

    public CaptureTimeTableEditor() {
        setDisplayer(new TranslatedObjectDisplayer());
    }

    public void setCaptureTimes(List<UINCaptureTime> captureTime) {
        removeItemListener(this);
        setItems(captureTime);
        addItemListener(this);
    }

    public Class<UINCaptureTime> getValueClass() {
        return UINCaptureTime.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignored
    }

    public UINCaptureTime getCaptureTime() {
        return (UINCaptureTime) getValue();
    }
}
