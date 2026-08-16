package oracle.retail.sim.client.swing.event;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import oracle.retail.sim.client.locale.StringUtility;

/******************************************************************************************
 * Listens for item changes on the check box and then calls performCheckBoxAction passing
 * along the assigned source, command and value.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class CheckBoxActionAdapter implements ItemListener {

    private Object actionSource;
    private String actionCommand;

    /******************************************************************************************
     * Constructs a new check box action adapter.
     ******************************************************************************************/
    protected CheckBoxActionAdapter() {
    }

    /******************************************************************************************
     * Constructs a new check box action adapter with an associated command.
     * <p>
     * @param command The command to send inside the event.
     ******************************************************************************************/
    protected CheckBoxActionAdapter(String command) {
        setCommand(command);
    }

    /******************************************************************************************
     * Overrides the source of the action. This will replace that actual component that
     * triggered the action inside the event that is sent.
     ******************************************************************************************/
    public void setSource(Object source) {
        actionSource = source;
    }

    /******************************************************************************************
     * Assigns a command to be placed inside the RActionEvent when a check box action is
     * triggered.
     * <p>
     * @param command The command to send inside the event.
     ******************************************************************************************/
    public void setCommand(String command) {
        if (StringUtility.isNullOrEmpty(command)) {
            command = null;
        }
        actionCommand = command;
    }

    /******************************************************************************************
     * When an item state changed action takes place, calls the check box action.
     ******************************************************************************************/
    public void itemStateChanged(ItemEvent event) {
        if (actionCommand != null) {
            boolean value = ((JCheckBox) event.getSource()).isSelected();
            Object source = actionSource;
            if (source == null) {
                source = event.getSource();
            }
            performCheckBoxAction(new RActionEvent(source, actionCommand, value));
        }
    }

    /******************************************************************************************
     * Implemented by all users of the adapter to receive check box actions.
     ******************************************************************************************/
    public abstract void performCheckBoxAction(RActionEvent event);
}
