package oracle.retail.sim.client.swing.entrytable;

import java.awt.Font;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.util.UIException;

/********************************************************************************************************
 * REntryTableEditor
 * <p>
 * Interface that table columns use to access the editors within the column.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class REntryTableEditor implements REventListener {

    private ReflectionWrapper wrapper;
    private Object model;
    private String attribute;

    /****************************************************************************************************
     * The table column uses this method to retrieve the actually JComponent that will be displayed in
     * the cell.
     ***************************************************************************************************/
    public abstract JComponent getComponent();

    /****************************************************************************************************
     * The table column uses this method to assign an identifier to the editor.
     ***************************************************************************************************/
    public abstract void setIdentifier(String identifier);

    /****************************************************************************************************
     * The table column uses this method to assign a font to the editor.
     ***************************************************************************************************/
    public abstract void setFont(Font font);

    /****************************************************************************************************
     * The table column uses this method to assign the data to display and update in the editor.
     ***************************************************************************************************/
    public abstract void setData(Object value);

    /****************************************************************************************************
     * The table column uses this method to retrieve the data from the editor and update the actual
     * business object
     ***************************************************************************************************/
    public abstract Object getData() throws UIException;

    /****************************************************************************************************
     * Assigns the ReflectionWrapper that uses the attribute and model to retrieve and assign data to
     * business objects.
     ***************************************************************************************************/
    public void setDataWrapper(ReflectionWrapper wrapper) {
        this.wrapper = wrapper;
    }

    /****************************************************************************************************
     * Assigns the model that will have the data of this editor assigned to it (based on attribute).
     ***************************************************************************************************/
    public void setModel(Object model) {
        this.model = model;
    }

    /****************************************************************************************************
     * The attribute of the model that this editor is modifying.
     ***************************************************************************************************/
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /****************************************************************************************************
     * Empty implemenation of the REventListener interface. Table editors will not need this functionality.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    /****************************************************************************************************
     * Executes when the value within the table editor changes. It retrieves the data from the editor
     * and attempts to assign to the model based on attribute.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        try {
            wrapper.setValue(model, attribute, getData());
        } catch (UIException exception) {
            UIStatusUtility.displayException(model, exception);
        }
    }
}
