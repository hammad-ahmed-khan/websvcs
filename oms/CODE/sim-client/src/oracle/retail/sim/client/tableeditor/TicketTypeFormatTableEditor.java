package oracle.retail.sim.client.tableeditor;

import java.util.List;
import oracle.retail.sim.client.displayer.TicketTypeFormatDisplayer;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;

/********************************************************************************************************
 * A table editor for selecting ticket type formats from a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TicketTypeFormatTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 3116129640416251161L;

    private TicketTypeFormatDisplayer displayer = new TicketTypeFormatDisplayer();

    public TicketTypeFormatTableEditor() {
        setDisplayer(displayer);
        setEmptyType(RComboBoxEmptyType.BLANK);
    }

    public void setTicketTypeFormats(List<TicketTypeFormat> ticketTypeFormats) {
        removeItemListener(this);
        displayer.setTicketTypeFormats(ticketTypeFormats);
        setItems(ticketTypeFormats);
        addItemListener(this);
    }

    public Class getValueClass() {
        return TicketTypeFormat.class;
    }

    public TicketTypeFormat getTicketTypeFormat() {
        return (TicketTypeFormat) getSelectedItem();
    }
}
