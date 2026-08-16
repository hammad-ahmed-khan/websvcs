package oracle.retail.sim.client.tableeditor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.editor.SearchReceiver;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * A table editor for entering a value that identifies an object or for pressing a search button and
 * "finding" the object.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SimSearchTableEditor extends JPanel implements FocusListener, MouseListener, REventListener, SearchReceiver {
    private static final long serialVersionUID = -1174906331741656954L;

    private AbstractDisplayer displayer;
    private RTextField valueField = new RTextField();
    private RButton searchButton = new RButton(SimNavigation.LOOKUP);
    private Object value;
    private boolean isErrorState;

    private int row = -1;
    private int column = -1;

    private SearchListener searchListener;
    private static final String SEARCH_TRIGGER = "RSearchFieldEditor.search";

    protected SimSearchTableEditor() {
        int searchWidth = UIManager.getInt(UIThemeName.RSEARCHFIELD_BUTTON_WIDTH);
        searchButton.setLockedSize(searchWidth, valueField.getPreferredSize().height);
        searchButton.registerAction(this, SEARCH_TRIGGER);
        searchButton.addMouseListener(this);

        valueField.setMargin(null);
        valueField.setBorder(null);
        valueField.addFocusListener(this);

        setBorder(null);

        setLayout(new BorderLayout());
        add(searchButton, BorderLayout.EAST);
        add(valueField, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Assigns the search listener interested in when the search button is processed.
     * <p>
     * @param listener The search listener.
     * @param command The command to send back to the listener.
     ***************************************************************************************************/
    public void setSearchListener(SearchListener listener) {
        searchListener = listener;
    }

    /****************************************************************************************************
     * Assigns a displayer responsible for displaying the information in the field.
     ***************************************************************************************************/
    protected void setDisplayer(AbstractDisplayer displayer) {
        this.displayer = displayer;
    }

    /****************************************************************************************************
     * Sets an identifier for the object. This will trigger a search for the object.
     ***************************************************************************************************/
    protected void setIdentifier(String identifier) {
        valueField.setIdentifier(identifier);
    }

    /****************************************************************************************************
     * Assign the table coordinates.
     ***************************************************************************************************/
    public void setCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /****************************************************************************************************
     * Reactivate editing within the table cell.
     ***************************************************************************************************/
    private void reactivateEditing(Object object) {
        if (object instanceof SimTable) {
            SimTable table = (SimTable) object;
            try {
                if (table.getSelectedRow() != row) {
                    table.setRowSelectionInterval(row, row);
                }
                table.editCellAt(row, column);
            } catch (Throwable ex) {
                UILog.debug(getClass(), ex);
            }
        }
    }

    protected void setErrorState(boolean errorState) {
        isErrorState = errorState;
    }

    protected boolean isErrorState() {
        return isErrorState;
    }

    protected RTextField getTextField() {
        return valueField;
    }

    protected String getText() {
        return valueField.getText();
    }

    protected void resetTextField() {
        if (displayer != null) {
            valueField.setText(displayer.getDisplayText(value));
        }
    }

    protected Object getSearchData() {
        return value;
    }

    protected void setSearchData(Object value) {
        this.value = value;

        if (displayer != null) {
            valueField.setText(displayer.getDisplayText(value));
        }
    }

    public boolean requestFocusInWindow() {
        boolean focusMoved = valueField.requestFocusInWindow();
        valueField.setCaretPosition(0);
        return focusMoved;
    }

    public void setFont(Font font) {
        super.setFont(font);
        if (valueField != null) {
            valueField.setFont(font);
        }
    }

    public void setEnabled(boolean enabled) {
        valueField.setEnabled(enabled);
        searchButton.setEnabled(enabled);
    }

    protected void displayErrorWithClear(MessageText message) {
        setSearchData(null);
        displayError(message);
    }

    protected void displayErrorWithReset(MessageText message) {
        resetTextField();
        setErrorState(true);
        displayError(message);
    }

    protected void displayError(UIException exception) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Table Editor Error");
        dialog.setMessage(exception);
        dialog.activate();
    }

    protected void displayError(BusinessException exception) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Table Editor Error");
        dialog.setMessage(exception);
        dialog.activate();
    }

    protected void displayError(MessageText message) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Table Editor Error");
        dialog.setMessage(message);
        dialog.activate();
    }

    /****************************************************************************************************
     * Defined by subclasses to handle the logic when the value changes.
     ***************************************************************************************************/
    protected abstract boolean doValueModified();

    /****************************************************************************************************
     * Implement the focus listener methods to call do value modified when focus islost.
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
    }

    public void focusLost(FocusEvent event) {
        if (event.isTemporary()) {
            return;
        }
        if (doValueModified()) {
            return;
        }
        reactivateEditing(event.getOppositeComponent());
    }

    /****************************************************************************************************
     * Listen for search trigger and call appropriate method.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(SEARCH_TRIGGER)) {
            triggerSearch();
        }
    }

    /****************************************************************************************************
     * Helper method to trigger the search when the search button is pressed.
     ***************************************************************************************************/
    private void triggerSearch() {
        if (searchButton.hasFocus()) {
            if (searchListener != null) {
                searchListener.search();
            }
        }
    }

    /****************************************************************************************************
     * Implements all the mouse listener methods.
     ***************************************************************************************************/
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        searchButton.requestFocusInWindow();
    }

    public void mouseExited(MouseEvent e) {
        valueField.requestFocusInWindow();
    }
}
