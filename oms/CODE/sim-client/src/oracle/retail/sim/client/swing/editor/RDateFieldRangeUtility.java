package oracle.retail.sim.client.swing.editor;

import java.util.Date;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.UIException;

/********************************************************************************************************
 * This editor handles range functionality across two date field editors.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDateFieldRangeUtility implements REventListener {

    private RDateFieldEditor startEditor;
    private RDateFieldEditor finalEditor;

    private static final String START_DATE_MODIFIED = "StartDate.modified";
    private static final String END_DATE_MODIFIED = "EndDate.modified";

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public RDateFieldRangeUtility(RDateFieldEditor startEditor, RDateFieldEditor finalEditor) {
        this.startEditor = startEditor;
        this.finalEditor = finalEditor;
        this.startEditor.registerAction(this, START_DATE_MODIFIED);
        this.finalEditor.registerAction(this, END_DATE_MODIFIED);
        doStartDateModified();
        doEndDateModified();
    }

    /****************************************************************************************************
     * Static Initializer
     ***************************************************************************************************/
    public static void setDateRangeEditors(RDateFieldEditor startEditor, RDateFieldEditor finalEditor) {
        new RDateFieldRangeUtility(startEditor, finalEditor);
    }

    /****************************************************************************************************
     * Implements event handlers
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(START_DATE_MODIFIED)) {
            doStartDateModified();
        } else if (command.equals(END_DATE_MODIFIED)) {
            doEndDateModified();
        }
    }

    /****************************************************************************************************
     * If start date modified, alter valid start date on the end date widget.
     ***************************************************************************************************/
    private void doStartDateModified() {
        Date startDate = null;
        try {
            startDate = startEditor.getDate();
        } catch (UIException e) {
            startDate = null;
        }
        finalEditor.setValidStartDate(startDate);
    }

    /****************************************************************************************************
     * If end date modified, alter valid end date on the start date widget.
     ***************************************************************************************************/
    private void doEndDateModified() {
        Date endDate = null;
        try {
            endDate = finalEditor.getDate();
        } catch (UIException e) {
            endDate = null;
        }
        startEditor.setValidEndDate(endDate);
    }
}
