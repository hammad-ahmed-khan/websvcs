package oracle.retail.sim.client.swing.util;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/********************************************************************************************
 * This tool creates a complex GridBagConstraints object with only numbers. It is easy to
 * learn and use and much more flexible than any other route to creating well-layed out JAVA screens.
 * <p>
 * It is important to note that GridBagConstraints does not support component orientation
 * and thus this should be handle inside each screen being coded.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ********************************************************************************************/

public class GridTool {
    private static GridBagConstraints constraint = new GridBagConstraints();

    /******************************************************************************************
     * Returns new static GridTool object.
     ******************************************************************************************/
    private GridTool() {
    }

    /******************************************************************************************
     * Creates and returns a GridBagConstraints objects based on a series of integers. This is
     * a shorthand for GUI design simplicity. Unfortunately, it requires understanding the
     * constraints.
     * <p>
     *@param xCoordinate The X Grid Location (column to start the widget in).
     *@param yCoordinate The Y Grid Location (row to start the widget in).
     *@param gridWidth Grid Width To Consume (number of columns).
     *@param gridHeight Grid Height To Consume (number of rows).
     *@param horizontalWeight Weight of Horizontal Resizing
     *@param verticalWeight Weight of Vertical Resizing
     *@param anchorValue Anchor. Valid values include:
     *          0 = Center,
     *          1 = West,
     *          2 = East,
     *          3 = North,
     *          4 = South,
     *          5 = Northwest,
     *          6 = Northeast,
     *          7 = Southwest,
     *          8 = Southeast.
     *@param fillValue Fill. Valid values include:
     *          0 = None,
     *          1 = Horizontal,
     *          2 = Vertical,
     *          3 = Both.
     *@param topPad Top Pad (in pixels).
     *@param leftPad Left Pad (in pixels).
     *@param bottomPad Bottom Pad (in pixels).
     *@param rightPad Right Pad (in pixels);
     *
     *@return A GrigBagConstraints object constructed from the input parameters.
     ******************************************************************************************/
    public static GridBagConstraints constraints(int xCoordinate, int yCoordinate, int gridWidth, int gridHeight, int horizontalWeight, int verticalWeight, int anchorValue, int fillValue, int topPad, int leftPad, int bottomPad, int rightPad) {
        constraint.gridx = xCoordinate;
        constraint.gridy = yCoordinate;
        constraint.gridwidth = gridWidth;
        constraint.gridheight = gridHeight;
        constraint.weightx = horizontalWeight;
        constraint.weighty = verticalWeight;
        constraint.insets = new Insets(topPad, leftPad, bottomPad, rightPad);
        constraint.ipadx = 0;
        constraint.ipady = 0;

        switch (anchorValue) {
            case 1:
                constraint.anchor = GridBagConstraints.WEST;
                break;
            case 2:
                constraint.anchor = GridBagConstraints.EAST;
                break;
            case 3:
                constraint.anchor = GridBagConstraints.NORTH;
                break;
            case 4:
                constraint.anchor = GridBagConstraints.SOUTH;
                break;
            case 5:
                constraint.anchor = GridBagConstraints.NORTHWEST;
                break;
            case 6:
                constraint.anchor = GridBagConstraints.NORTHEAST;
                break;
            case 7:
                constraint.anchor = GridBagConstraints.SOUTHWEST;
                break;
            case 8:
                constraint.anchor = GridBagConstraints.SOUTHEAST;
                break;
            default:
                constraint.anchor = GridBagConstraints.CENTER;
        }

        switch (fillValue) {
            case 1:
                constraint.fill = GridBagConstraints.HORIZONTAL;
                break;
            case 2:
                constraint.fill = GridBagConstraints.VERTICAL;
                break;
            case 3:
                constraint.fill = GridBagConstraints.BOTH;
                break;
            default:
                constraint.fill = GridBagConstraints.NONE;
        }

        return constraint;
    }
}
