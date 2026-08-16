package oracle.retail.sim.client.swing.util;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

/******************************************************************************************
 * This class is used to calculate the placement (x and y coordinates) of a window on
 * either the desktop or another window. Note that RFrame and RDialog windows are both
 * children of the Window class and can be used with these methods.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class WindowPlacer {

    /******************************************************************************************
     * Returns new static WindowPlacer object.
     ******************************************************************************************/
    private WindowPlacer() {
    }

    /******************************************************************************************
     * Centers a window on the desktop.
     * <p>
     *@param window The window to center on the desktop.
     ******************************************************************************************/
    public static synchronized void centerWindow(Window window) {
        Dimension screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
        Dimension windowSize = window.getSize();

        int xcord = (screenSize.width - windowSize.width) / 2;
        if (xcord < 0) {
            xcord = 0;
        }

        int ycord = (screenSize.height - windowSize.height) / 2;
        if (ycord < 0) {
            ycord = 0;
        }
        window.setLocation(xcord, ycord);
    }

    /******************************************************************************************
     * Centers a window within another window.
     * <p>
     *@param parent The window to be centered on.
     *@param child The window to be centered.
     ******************************************************************************************/
    public static synchronized void centerOnWindow(Window parent, Window child) {
        centerOnWindow(parent, child, 0, 0);
    }

    /******************************************************************************************
     * Centers a window within another window and includes a slight offset.
     * <p>
     *@param parent The window to be centered on.
     *@param child The window to be centered.
     *@param xoffset The offset form the centered x location.
     *@param yoffset The offset from the centered y location.
     ******************************************************************************************/
    public static synchronized void centerOnWindow(Window parent, Window child, int xoffset, int yoffset) {
        Point point = parent.getLocation();

        Dimension parentSize = parent.getSize();
        Dimension childSize = child.getSize();

        int xcord = (parentSize.width - childSize.width) / 2;
        if (xcord < 0) {
            xcord = 0;
        }

        int ycord = (parentSize.height - childSize.height) / 2;
        if (ycord < 0) {
            ycord = 0;
        }

        child.setLocation(point.x + xcord + xoffset, point.y + ycord + yoffset);
    }

    /******************************************************************************************
     * Centers a window within its owner window.
     * <p>
     *@param window The window to be centered.
     ******************************************************************************************/
    public static synchronized void centerOnOwner(Window window) {
        Window parent = window.getOwner();

        if (parent != null) {
            centerOnWindow(parent, window);
        }
    }

    /******************************************************************************************
     * Centers a window within its owner window otherwise it will center on screen.
     * <p>
     *@param window The window to be centered.
     ******************************************************************************************/
    public static synchronized void centerOnOwnerOrWindow(Window window) {
        Window parent = window.getOwner();
        if (parent != null && parent.isVisible()) {
            centerOnWindow(parent, window);
        } else {
            centerWindow(window);
        }
    }

    /******************************************************************************************
     * Indents a window within another window.
     * <p>
     *@param parent The parent window to start indentation from.
     *@param child The window to indent.
     *@param indentX The number of pixels to indent on the X axis.
     *@param indentY The number of pixels to indent on the Y axis.
     ******************************************************************************************/
    public static synchronized void indentWindow(Window parent, Window child, int indentX, int indentY) {
        Point point = parent.getLocationOnScreen();

        int xcord = point.x + indentX;
        if (xcord < 0) {
            xcord = 0;
        }

        int ycord = point.y + indentY;
        if (ycord < 0) {
            ycord = 0;
        }
        child.setLocation(xcord, ycord);
    }

    /******************************************************************************************
     * Indents a window within its owner.
     * <p>
     *@param window The window to be indented.
     *@param indentX The number of pixels to indent on the X axis.
     *@param indentY The number of pixels to indent on the Y axis.
     ******************************************************************************************/
    public static synchronized void indentOnOwner(Window window, int indexX, int indentY) {
        Window parent = window.getOwner();

        if (parent == null) {
            return;
        }

        Point point = parent.getLocationOnScreen();

        int xcord = point.x + indexX;
        if (xcord < 0) {
            xcord = 0;
        }

        int ycord = point.y + indentY;
        if (ycord < 0) {
            ycord = 0;
        }
        window.setLocation(xcord, ycord);
    }

    /******************************************************************************************
     * Displays a list of windows on a parent window. It begins by centering on the parent
     * and then calculated the original location based on moving backwards with the indent
     * value to discover the location of the first child and then placing them in a stacked
     * pattern over the parent window.
     * <p>
     *@param parent The parent window to start indentation from.
     *@param childList A list of window to indent on the parent.
     *@param indentX The number of pixels to indent on the X axis.
     *@param indentY The number of pixels to indent on the Y axis.
     ******************************************************************************************/
    public static void displayWindows(Window parent, List<Window> childList, int indentX, int indentY) {
        Window lastWindow = null;

        for (int i = 0; i < childList.size(); i++) {
            Window childWindow = childList.get(i);

            if (lastWindow != null) {
                indentWindow(lastWindow, childWindow, indentX, indentY);
            } else {
                int halfWindowCount = childList.size() / 2;
                centerOnWindow(parent, childWindow, 0 - halfWindowCount * indentX, 0 - halfWindowCount * indentY);
            }
            lastWindow = childWindow;

            childWindow.setVisible(true);
        }
    }

    /******************************************************************************************
     * Aligns a popup dialog to the location of a given component on the screen. If right
     * aligned, the right side of the dialog is aligned with the component. If left, the left
     * side is aligned/
     * up.
     * <p>
     * @param component The base component to align against.
     * @param dialog The dialog window.
     * @param rightAligned True if the window should be right aligned, false otherwise.
     * @param preferUp True if the it is preferred that the dialog pop above the component, false
     * if below.
     ******************************************************************************************/
    public static void alignToComponent(JFrame frame, JComponent component, JDialog dialog, boolean rightAligned, boolean preferUp) {
        if (rightAligned) {
            alignToComponentRight(frame, component, dialog, preferUp);
        } else {
            alignToComponentLeft(frame, component, dialog, preferUp);
        }
    }

    /******************************************************************************************
     * Right aligns the popup window. Note that preferUp ALWAYS places the dialog up.
     ******************************************************************************************/
    private static void alignToComponentRight(JFrame frame, JComponent component, JDialog dialog, boolean preferUp) {
        if (component.isShowing()) {
            Point basePoint = component.getLocationOnScreen();
            int width = component.getWidth();
            int height = component.getHeight();
            int dialogWidth = dialog.getWidth();
            int dialogHeight = dialog.getHeight();

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int screenHeight = toolkit.getScreenSize().height;
            Insets screenInsets = toolkit.getScreenInsets(frame.getGraphicsConfiguration());

            int xloc = basePoint.x + width - dialogWidth;
            if (xloc < 0) {
                xloc = 0;
            }

            if (preferUp) {
                dialog.setLocation(new Point(xloc, basePoint.y - dialogHeight));
            } else if (basePoint.y + dialogHeight > screenHeight - screenInsets.bottom) {
                dialog.setLocation(new Point(xloc, basePoint.y - dialogHeight));
            } else {
                dialog.setLocation(new Point(xloc, basePoint.y + height));
            }
        }
    }

    /******************************************************************************************
     * Left aligns the popup window. Note that preferUp ALWAYS places the dialog up.
     ******************************************************************************************/
    private static void alignToComponentLeft(JFrame frame, JComponent component, JDialog dialog, boolean preferUp) {
        if (component.isShowing()) {
            Point basePoint = component.getLocationOnScreen();
            int height = component.getHeight();
            int dialogHeight = dialog.getHeight();

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int screenHeight = toolkit.getScreenSize().height;
            Insets screenInsets = toolkit.getScreenInsets(frame.getGraphicsConfiguration());

            if (preferUp) {
                dialog.setLocation(new Point(basePoint.x, basePoint.y - dialogHeight));
            } else if (basePoint.y + dialogHeight > screenHeight - screenInsets.bottom) {
                dialog.setLocation(new Point(basePoint.x, basePoint.y - dialogHeight));
            } else {
                dialog.setLocation(new Point(basePoint.x, basePoint.y + height));
            }
        }
    }

    /******************************************************************************************
     * Aligns the dialog at the point on the component.
     * <p>
     * @param component The component to calculate the point on.
     * @param dialog The dialog to align.
     * @param point The point to translate.
     ******************************************************************************************/
    public static void alignLocation(JComponent component, JDialog dialog, Point point) {
        if (component.isShowing()) {
            Point basePoint = component.getLocationOnScreen();

            dialog.setLocation(new Point(basePoint.x + point.x, basePoint.y + point.y));
        }
    }
}
