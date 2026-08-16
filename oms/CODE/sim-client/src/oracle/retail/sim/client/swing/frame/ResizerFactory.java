package oracle.retail.sim.client.swing.frame;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Insets;

/****************************************************************************************
 * Resizer Factory figures out the appropriate resizer for a x/y location.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ****************************************************************************************/

public class ResizerFactory {

    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private static final int EAST = 6;

    private static final Insets INSETS = new Insets(2, 2, 2, 2);

    private static Resizer[] resizers = new Resizer[] {
            null, new NorthResizer(), new SouthResizer(), new WestResizer(), new NorthWestResizer(), new SouthWestResizer(), new EastResizer(), new NorthEastResizer(), new SouthEastResizer()
    };

    /****************************************************************************************
     * Static class constructor.
     ****************************************************************************************/
    private ResizerFactory() {
    }

    /****************************************************************************************
     * Gets a Resizer for the specified frame at the given X/Y location.
     * <p>
     * @param frame The RFrame to find a resizer for.
     * @param x The x coordinate on the RFrame.
     * @param y The y coordinate on the RFrame.
     * <p>
     * @return The appropriate resizer or null if no resizer is appropriate.
     ****************************************************************************************/
    public static Resizer getResizer(RFrame frame, int x, int y) {
        int width = frame.getWidth();
        int height = frame.getHeight();
        int index = 0;
        if (y <= INSETS.top * 2) {
            index = index + NORTH;
        }
        if (y >= height - INSETS.bottom * 2) {
            index = index + SOUTH;
        }
        if (x <= INSETS.left * 2) {
            index = index + WEST;
        }
        if (x >= width - INSETS.right * 2) {
            index = index + EAST;
        }
        if (index >= resizers.length) {
            index = 0;
        }
        return resizers[index];
    }

    /****************************************************************************************
     * Class for resizing a component in the North direction
     ****************************************************************************************/
    private static class NorthResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX(), comp.getY() + deltaY, comp.getWidth(), comp.getHeight() - deltaY);
        }
    }

    /****************************************************************************************
     * Class for resizing a component in the South direction
     ****************************************************************************************/
    private static class SouthResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight() + deltaY);
        }
    }

    /****************************************************************************************
     * Class for resizing a component in the West direction
     ****************************************************************************************/
    private static class WestResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX() + deltaX, comp.getY(), comp.getWidth() - deltaX, comp.getHeight());
        }
    }

    /****************************************************************************************
     * Class for resizing a component in the Northwest direction
     ****************************************************************************************/
    private static class NorthWestResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX() + deltaX, comp.getY() + deltaY, comp.getWidth() - deltaX, comp.getHeight() - deltaY);
        }
    }

    /****************************************************************************************
     * Class for resizing a component in the Southwest direction
     ****************************************************************************************/
    private static class SouthWestResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX() + deltaX, comp.getY(), comp.getWidth() - deltaX, comp.getHeight() + deltaY);
        }
    }

    /****************************************************************************************
     * Class for resizing a component in the East direction
     ****************************************************************************************/
    private static class EastResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX(), comp.getY(), comp.getWidth() + deltaX, comp.getHeight());
        }
    }

    /****************************************************************************************
     * Class for resizing a component in the Northeast direction
     ****************************************************************************************/
    private static class NorthEastResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX(), comp.getY() + deltaY, comp.getWidth() + deltaX, comp.getHeight() - deltaY);
        }
    }

    /****************************************************************************************
     * Class for resizing a component in the Southeast direction
     ****************************************************************************************/
    private static class SouthEastResizer implements Resizer {
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
        }

        public void resize(Component comp, int deltaX, int deltaY) {
            comp.setBounds(comp.getX(), comp.getY(), comp.getWidth() + deltaX, comp.getHeight() + deltaY);
        }
    }
}
