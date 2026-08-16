package oracle.retail.sim.client.core;

import oracle.retail.sim.client.swing.panel.REditorPanel;

/********************************************************************************************************
 * This class is used as the header area of certain SIM screens. It contains a line border and a darker
 * background than the standard panel. It can display one two four components only as it is meant to
 * strictly be a header panel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RHeaderPanel extends REditorPanel {
    private static final long serialVersionUID = -389395031311260886L;

    public RHeaderPanel(int rows) {
        super(rows);
        setLineBorder(1);
    }

    public RHeaderPanel(int rows, int columns) {
        super(rows, columns);
        setLineBorder(1);
    }
}
