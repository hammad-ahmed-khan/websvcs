package oracle.retail.sim.client.swing.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventAdaptor;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.LayoutUtility;

/********************************************************************************************************
 * This class sublcasses the standard JPanel class in the Swing package to provide custom functionality.
 * This should always be used in place of JPanel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RPanel extends JPanel {
    private static final long serialVersionUID = -2909169991096267081L;

    protected REventAdaptor eventAdaptor = new REventAdaptor();
    protected Component[] focusComponents;
    protected TitledBorder titledBorder;
    protected Color lineColor;
    private Insets titleMargin;

    /****************************************************************************************************
     * Returns new RPanel object.
     ***************************************************************************************************/
    public RPanel() {
        initialize();
    }

    /****************************************************************************************************
     * Returns new RPanel object.
     * <p>
     * @param manager The LayoutManager to use for the panel.
     ***************************************************************************************************/
    public RPanel(LayoutManager manager) {
        super(manager);
        initialize();
    }

    /****************************************************************************************************
     * Initializes the default values of the panel.
     ***************************************************************************************************/
    private void initialize() {
        setDoubleBuffered(true);
        setBackground(UIManager.getColor(UIThemeName.PANEL_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.PANEL_FOREGROUND));
        titleMargin = UIManager.getInsets(UIThemeName.PANEL_TITLE_MARGIN);
    }

    /****************************************************************************************************
     * Sets the minimum height of the panel in pixels.
     * <p>
     * @param height The minimum height of the panel in pixels.
     ***************************************************************************************************/
    public void setMinimumHeight(int height) {
        setMinimumSize(new Dimension(0, height));
        setPreferredSize(new Dimension(0, height));
    }

    /****************************************************************************************************
     * Places an empty border around the panel.
     * <p>
     * @param size The size of the empty border (used on each side of the panel) in pixels.
     ***************************************************************************************************/
    public void setEmptyBorder(int size) {
        setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    /****************************************************************************************************
     * Places an empty border around the panel.
     * <p>
     * @param top The top border in pixels.
     * @param left The left border in pixels.
     * @param bottom The bottom border in pixels.
     * @param right The right border in pixels.
     ***************************************************************************************************/
    public void setEmptyBorder(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    /****************************************************************************************************
     * Builds a panel with a lined border and equal width border pad on each side of the panel.
     * <p>
     * @param size The size of the border pad (between text and border) in pixels.
     ***************************************************************************************************/
    public void setLineBorder(int size) {
        Border border1 = BorderFactory.createLineBorder(getLineColor());
        Border border2 = BorderFactory.createEmptyBorder(size, size, size, size);

        setBorder(new CompoundBorder(border1, border2));
    }

    /****************************************************************************************************
     * Builds a panel with a lined border and border padding specified by parameters.
     * <p>
     * @param top The top border in pixels.
     * @param left The left border in pixels.
     * @param bottom The bottom border in pixels.
     * @param right The right border in pixels.
     ***************************************************************************************************/
    public void setLineBorder(int top, int left, int bottom, int right) {
        Border border1 = BorderFactory.createLineBorder(getLineColor());
        Border border2 = BorderFactory.createEmptyBorder(top, left, bottom, right);

        setBorder(new CompoundBorder(border1, border2));
    }

    /****************************************************************************************************
     * Builds a panel with a lined border with the color, thickness and pad specified.
     * <p>
     * @param lineColor The line color to assign to the line.
     * @param thickness The thickness of the line border in pixels.
     * @param pad Empty space on each side of the panel (in pixels).
     ***************************************************************************************************/
    public void setLineBorder(Color lineColor, int thickness, int pad) {
        setLineColor(lineColor);

        Border border1 = BorderFactory.createLineBorder(getLineColor(), thickness);
        Border border2 = BorderFactory.createEmptyBorder(pad, pad, pad, pad);

        setBorder(new CompoundBorder(border1, border2));
    }

    /****************************************************************************************************
     * Builds a panel with a titled border and default border padding, zero on top where the label is and
     * five pixels on all other sides.
     * <p>
     * @param title The title to assign to the panel.
     ***************************************************************************************************/
    public void setTitleBorder(String title) {
        setTitleBorder(title, null);
    }

    /****************************************************************************************************
     * Builds a panel with a titled border and default border padding, zero on top where the label is and
     * five pixels on all other sides.
     * <p>
     * @param title The title to assign to the panel.
     ***************************************************************************************************/
    public void setTitleBorder(String title, Font font) {
        Border border1 = BorderFactory.createLineBorder(getLineColor());
        Border border2 = createTitledBorder(border1, font, title);
        setBorder(new CompoundBorder(createTitleMarginBorder(), border2));
    }

    /****************************************************************************************************
     * Helper method that creates the empty border inside the title margin.
     * <p>
     * @return An empty border representing the default title margin.
     ***************************************************************************************************/
    protected Border createTitleMarginBorder() {
        Insets insets = titleMargin;
        return BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
    }

    /****************************************************************************************************
     * Builds a panel with a titled border and border padding specified by parameters.
     * <p>
     * @param title The title to assign to the panel.
     * @param size The size of the border pad (between text and border) in pixels.
     ***************************************************************************************************/
    public void setTitleBorder(String title, int size) {
        Border border1 = BorderFactory.createLineBorder(getLineColor());
        Border border2 = createTitledBorder(border1, null, title);
        Border border3 = BorderFactory.createEmptyBorder(size, size, size, size);

        setBorder(new CompoundBorder(border2, border3));
    }

    /****************************************************************************************************
     * Builds a panel with a titled border and border padding specified by parameters.
     * <p>
     * @param title The title to assign to the panel.
     * @param top The top border in pixels.
     * @param left The left border in pixels.
     * @param bottom The bottom border in pixels.
     * @param right The right border in pixels.
     ***************************************************************************************************/
    public void setTitleBorder(String title, int top, int left, int bottom, int right) {
        Border border1 = BorderFactory.createLineBorder(getLineColor());
        Border border2 = createTitledBorder(border1, null, title);
        Border border3 = BorderFactory.createEmptyBorder(top, left, bottom, right);

        setBorder(new CompoundBorder(border2, border3));
    }

    /****************************************************************************************************
     * Sets a raised bevel border on the panel.
     ***************************************************************************************************/
    public void setRaisedBevelBorder() {
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    /****************************************************************************************************
     * Sets a lowered bevel border on the panel.
     ***************************************************************************************************/
    public void setLoweredBevelBorder() {
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    /****************************************************************************************************
     * Creates a titled border.
     ***************************************************************************************************/
    private Border createTitledBorder(Border border, Font font, String title) {
        titledBorder = BorderFactory.createTitledBorder(border, Translator.getText(title));
        if (font == null) {
            titledBorder.setTitleFont(UIManager.getFont(UIThemeName.PANEL_TITLE_FONT));
        } else {
            titledBorder.setTitleFont(font);
        }
        return titledBorder;
    }

    /****************************************************************************************************
     * Retrieves an array of components sorted in the order of focus cycle .This method will ignore any
     * components assigned by setFocusCycleComponents() that do not belong to this panel at the moment t
     * his method is called.
     * <p>
     * @return An array of components sorted in the order of focus cycle.
     ***************************************************************************************************/
    public Component[] getFocusCycleComponents() {
        Component[] cycleComponents = new Component[0];
        if (focusComponents != null) {
            Component[] tempArray;
            int length = -1;

            for (Component focusComponent : focusComponents) {
                if (isAncestorOf(focusComponent)) {
                    length = cycleComponents.length;
                    tempArray = new Component[length + 1];
                    System.arraycopy(cycleComponents, 0, tempArray, 0, length);
                    cycleComponents = tempArray;
                    cycleComponents[length] = focusComponent;
                }
            }
        }
        if (cycleComponents.length == 0) {
            return getComponents();
        }
        return cycleComponents;
    }

    /****************************************************************************************************
     * Assigns the cycle of focus within this component.
     * <p>
     * @param array An array of components sorted in the order of focus cycle.
     ***************************************************************************************************/
    public void setFocusCycleComponents(Component[] array) {
        focusComponents = array;
    }

    /****************************************************************************************************
     * Retrieves the line color used by the panel to paint line borders and titles.
     * <p>
     * @return The line color.
     ***************************************************************************************************/
    public Color getLineColor() {
        if (lineColor == null) {
            lineColor = UIManager.getColor(UIThemeName.PANEL_LINE_COLOR);
        }
        return lineColor;
    }

    /****************************************************************************************************
     * Assigns the line color used by the panel to paint line borders and titles.
     * <p>
     * @param lineColor The color to assign.
     ***************************************************************************************************/
    public void setLineColor(Color lineColor) {
        if (lineColor != null) {
            this.lineColor = lineColor;
        }
    }

    /****************************************************************************************************
     * Enabled and disabled the panel. This first calls the superclass and then, if a titled border
     * exists, it swiches the title from enabled to disabled.
     * <p>
     * @param enabled true if this component should be enabled, false otherwise
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (titledBorder != null) {
            if (enabled) {
                titledBorder.setTitleColor(UIManager.getColor(UIThemeName.PANEL_FOREGROUND));
            } else {
                titledBorder.setTitleColor(getBackground().darker());
            }
        }
    }

    /****************************************************************************************************
     * Adds a REventListener to the REventListener list.
     * <p>
     * @param listener The REventListener to add.
     ***************************************************************************************************/
    public void addREventListener(REventListener listener) {
        eventAdaptor.addREventListener(listener);
    }

    /****************************************************************************************************
     * Removes a REventListener from the REventListener list.
     * <p>
     * @param listener The REventListener to remove.
     ***************************************************************************************************/
    public void removeREventListener(REventListener listener) {
        eventAdaptor.removeREventListener(listener);
    }

    /****************************************************************************************************
     * Removes a REventListener from the REventListener list.
     ***************************************************************************************************/
    public void removeAllREventListeners() {
        eventAdaptor.removeAllREventListeners();
    }

    /****************************************************************************************************
     * Notifies all listeners of an action event.
     * <p>
     * @param event An RActionEvent object containing details about the event.
     ***************************************************************************************************/
    public void notifyREventListeners(RActionEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Notifies all listeners of an error event.
     * <p>
     * @param event An RErrorEvent object containing details about the event.
     ***************************************************************************************************/
    public void notifyREventListeners(RErrorEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Implements the required REventListener method. It sends the event to all current REventListeners
     * of the panel.
     * <p>
     * @param event The RErrorEvent that triggered this listener method.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    /****************************************************************************************************
     * If a grid bag layout, will attempt to format all RetailEditors with aligned labels.
     ***************************************************************************************************/
    public void pack() {
        if (getLayout() instanceof GridBagLayout) {
            LayoutUtility.alignEditorsInGridBag(this);
        }
    }

    /****************************************************************************************************
     * Repaints the panel immediately after validating all widgets and layouts.
     ***************************************************************************************************/
    public void repaintPanel() {
        validate();

        RepaintManager.currentManager(this).paintDirtyRegions();
    }
}
