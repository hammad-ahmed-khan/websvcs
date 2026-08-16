package oracle.retail.sim.client.swing.widget;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;

/******************************************************************************************
 * This subclasses RButton to provide a button that paints a triangular arrow facing a
 * specific direction. The minimum size is set to (5,5) by default and the preferred size
 * is set to (14,14) by default.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RArrowButton extends JButton {
    private static final long serialVersionUID = 6916259473859716054L;

    private int direction = RArrowButton.EAST;
    private REventListener eventListener;

    /*****************************************************************************************
     * Constructs an arrow button oriented in the direction specified by the paramenter
     * [RArrowButton.EAST, RArrowButton.WEST, RArrowButton.NORTH, RArrowButton.SOUTH ]
     * <p>
     * @param direction The direction to assign to the arrow.
     *****************************************************************************************/
    public RArrowButton(int direction) {
        setOpaque(false);
        setDirection(direction);
        setRolloverEnabled(true);
        setFocusPainted(false);
        setDefaultCapable(false);
        setMinimumSize(5, 5);
        setPreferredSize(new Dimension(14, 14));
    }

    /******************************************************************************************
     * Returns a string that specifies the name of the L&F class that renders this component.
     * <p>
     * @return The string "ArrowButtonUI"
    /******************************************************************************************/
    public String getUIClassID() {
        return "ArrowButtonUI";
    }

    /*****************************************************************************************
     * Assigns the direction of the arrow [RArrowButton.EAST, RArrowButton.WEST,
     * RArrowButton.NORTH, RArrowButton.SOUTH ]
     * <p>
     * @param direction The direction of the arrow.
     *****************************************************************************************/
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /*****************************************************************************************
     * Retrieves the direction of the arrow [RArrowButton.EAST, RArrowButton.WEST,
     * RArrowButton.NORTH, RArrowButton.SOUTH ]
     * <p>
     * @return The direction of the arrow.
     *****************************************************************************************/
    public int getDirection() {
        return direction;
    }

    /******************************************************************************************
     * Sets the minimum, maximum and preferred width of the button.
     * <p>
     *@param width The width of the button in pixels.
     *****************************************************************************************/
    public void setLockedWidth(int width) {
        setLockedSize(width, getPreferredSize().height);
    }

    /******************************************************************************************
     * Sets the minimum, maximum and preferred width of the button.
     * <p>
     *@param width The width of the button in pixels.
     *@param height The height of the button in pixels.
     ******************************************************************************************/
    public void setLockedSize(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width of the button.
     * <p>
     *@param width The width of the button in pixels.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        setMinimumSize(width, getPreferredSize().height);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width and height of the button.
     * <p>
     *@param width The width of the button in pixels.
     *@param height The height of the button in pixels.
     *****************************************************************************************/
    public void setMinimumSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    /******************************************************************************************
     * Sets the action the button should trigger. This does not create an actual Action object.
     * Using this method, the RButton will deliver its actions as an RActionEvent to the
     * REventListener.
     * <p>
     *@param eventListener The REventListener to receive the action from this button.
     *@param actionCommand The action command to assign to the action.
     *****************************************************************************************/
    public void registerAction(REventListener eventListener, String actionCommand) {
        this.eventListener = eventListener;
        setActionCommand(actionCommand);
    }

    /******************************************************************************************
     * Sets the action the button should trigger. This does not create an actual Action object.
     * Using this method, the RButton will deliver its actions as an RActionEvent to the
     * REventListener.
     * <p>
     *@param eventListener The REventListener to receive the action from this button.
     *@param actionCommand The action command to assign to the action.
     *@param mnemonic The character keystroke the will trigger the button action.
     *****************************************************************************************/
    public void registerAction(REventListener eventListener, String actionCommand, char mnemonic) {
        this.eventListener = eventListener;
        setActionCommand(actionCommand);
        setMnemonic(mnemonic);
    }

    /******************************************************************************************
     * Sets the text to display within the button. The superclass is overridden to supply
     * language translation.
     * <p>
     *@param text The text to display within the button.
     *****************************************************************************************/
    public void setText(String text) {
        super.setText(Translator.getText(text));
        setMnemonic(Translator.getMnemonic(text));
        setPreferredSize(null);
    }

    /******************************************************************************************
     * Implements the action performed method to notify the existing event listener of the
     * action. This is done to conform to the RActionEvent pattern for those who choose to do so.
     /******************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        if (eventListener != null) {
            eventListener.performActionEvent(new RActionEvent(this, getActionCommand()));
        }
    }
}
