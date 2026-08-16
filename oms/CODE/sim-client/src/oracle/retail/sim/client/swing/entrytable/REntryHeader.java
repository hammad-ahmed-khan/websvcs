package oracle.retail.sim.client.swing.entrytable;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * REntryHeader
 * <p>
 * This class is a single column header displayed within the header row.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

class REntryHeader extends JPanel {
    private static final long serialVersionUID = 8617986130170739703L;

    private static final int LABEL_PAD = 16;

    private Icon upArrow;
    private Icon downArrow;

    private RLabel header1Label = new RLabel(StringConstants.SPACE);
    private RLabel header2Label = new RLabel(StringConstants.SPACE);
    private RLabel arrowLabel = new RLabel(StringConstants.EMPTY);

    private boolean shouldSortAscending = true;
    private boolean isResizable = true;
    private boolean isConfiguration;

    private String label;
    private int index = -1;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param label The label of the header.
     * @param index The index this header has in the header row.
     ***************************************************************************************************/
    public REntryHeader(String label, int index) {
        setBorder(BorderFactory.createRaisedBevelBorder());
        setLayout(new GridBagLayout());
        setOpaque(true);
        this.label = label;
        this.index = index;

        initLabels();
        initLayout();
    }

    /****************************************************************************************************
     * Iniitializes the display of the label. If the label is separator with an "|", then it will be
     * split into two labels and displayed (otherwise single label). Each set of text is translated.
     ***************************************************************************************************/
    private void initLabels() {
        int split = StringUtility.indexOf(label, "|");
        if (split > -1) {
            header1Label.setText(Translator.getText(label.substring(0, split)));
            header2Label.setText(Translator.getText(label.substring(split + 1)));
        } else {
            header1Label.setText(Translator.getText(label));
        }
        upArrow = UIManager.getIcon(UIThemeName.SORT_ASC_1);
        downArrow = UIManager.getIcon(UIThemeName.SORT_DES_1);
    }

    /****************************************************************************************************
     * Initializes the layout of the header.
     ***************************************************************************************************/
    private void initLayout() {
        add(header1Label, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        add(header2Label, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        add(arrowLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Retrieves the index of the header in the header row.
     ***************************************************************************************************/
    protected int getIndex() {
        return index;
    }

    /****************************************************************************************************
     * Assigns the font of the header.
     * <p>
     * @param font The font.
     ***************************************************************************************************/
    public void setFont(Font font) {
        if (font != null) {
            super.setFont(font);

            if (header1Label != null) {
                header1Label.setFont(font);
            }
            if (header2Label != null) {
                header2Label.setFont(font);
            }
        }
    }

    /****************************************************************************************************
     * Assigns the icon to display in the header (this is usually the sort icon).
     ***************************************************************************************************/
    protected void setIcon(Icon icon) {
        header1Label.setText(StringConstants.EMPTY);
        header1Label.setIcon(icon);
    }

    /****************************************************************************************************
     * Assigns whether or not this header is the configuration icon header (it sits above the scrollbar
     * space).
     ***************************************************************************************************/
    protected void setConfiguration(boolean isConfiguration) {
        this.isConfiguration = isConfiguration;
    }

    /****************************************************************************************************
     * Return true if this is the configuration header, false otherwise.
     ***************************************************************************************************/
    protected boolean isConfiguration() {
        return isConfiguration;
    }

    /****************************************************************************************************
     * Sets the mnemonic character to underline in the label.
     ***************************************************************************************************/
    protected void setMnemonic(char character) {
        header1Label.setDisplayedMnemonic(character);
    }

    /****************************************************************************************************
     * Retrieves the mnemonic character to underline in the label.
     ***************************************************************************************************/
    protected int getMnemonic() {
        return header1Label.getDisplayedMnemonic();
    }

    /****************************************************************************************************
     * Sets the label justification. This is EditorConstants.CENTERED by default.
     ***************************************************************************************************/
    protected void setJustification(int justification) {
        header1Label.setHorizontalAlignment(justification);
        header2Label.setHorizontalAlignment(justification);
    }

    /****************************************************************************************************
     * Assigns the width of the header (in pixels). If the width is <0, then the header will expand as
     * the table resizes. If the width is (0), then the header will lock its width to the size of its
     * label. Any positive value will lock the header to that width.
     * <p>
     * @param width The width.
     ***************************************************************************************************/
    protected void setWidth(int width) {
        isResizable = width < 0;

        if (width == 0) {
            FontMetrics metrics = getFontMetrics(getFont());
            width = metrics.stringWidth(header1Label.getText());
            if (header2Label.isVisible()) {
                int width2 = metrics.stringWidth(header2Label.getText());
                if (width2 > width) {
                    width = width2;
                }
            }
            width = width + LABEL_PAD;
        }
        Dimension dimension = new Dimension(width, calculateDefaultHeight());

        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }

    /****************************************************************************************************
     * Returns the calculated default height of the label.
     ***************************************************************************************************/
    private int calculateDefaultHeight() {
        int height = header1Label.getPreferredSize().height;
        if (StringUtility.isNullOrEmpty(header2Label.getText())) {
            return height;
        }
        return header1Label.getPreferredSize().height + header2Label.getPreferredSize().height;
    }

    /****************************************************************************************************
     * Returns true if the header can expand with the table, false otherwise.
     ***************************************************************************************************/
    protected boolean isResizable() {
        return isResizable;
    }

    /****************************************************************************************************
     * Retrieves whether or not this column should be sorted ascending next (or descending).
     * <p>
     * @return True if the next sort should be ascending, false if descending.
     ***************************************************************************************************/
    protected boolean shouldSortAscending() {
        return shouldSortAscending;
    }

    /****************************************************************************************************
     * Removes the arrow from the header.
     ***************************************************************************************************/
    protected void setNoArrow() {
        shouldSortAscending = true;
        arrowLabel.setIcon(null);
    }

    /****************************************************************************************************
     * Assigns an up arrow indicator to the header.
     ***************************************************************************************************/
    protected void setUpArrow() {
        shouldSortAscending = true;
        arrowLabel.setIcon(upArrow);
    }

    /****************************************************************************************************
     * Assigns a down arrow indicator to the header.
     ***************************************************************************************************/
    protected void setDownArrow() {
        shouldSortAscending = false;
        arrowLabel.setIcon(downArrow);
        arrowLabel.invalidate();
        arrowLabel.repaint();
    }

    /****************************************************************************************************
     * Returns a formatted string for debugging purposes.
     ***************************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("RHeader [");
        buffer.append("Label = ").append(label);
        buffer.append("; Index = ").append(getIndex());
        buffer.append("; Resizable = ").append(isResizable());
        buffer.append("; Mnemonic = ").append(getMnemonic());
        buffer.append("]");
        return buffer.toString();
    }
}
