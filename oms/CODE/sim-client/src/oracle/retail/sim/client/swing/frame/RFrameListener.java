package oracle.retail.sim.client.swing.frame;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

/******************************************************************************************
 * The frame listener listens to the mouse on the frame and takes the appropriate action.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RFrameListener implements MouseListener, MouseMotionListener {

    private Resizer resizer;

    private int x;
    private int y;

    private RPlatformFrame frame;
    private RPlatformTitleBar titleBar;
    private RPlatformFooterPane footerPane;

    private boolean isMoveable;
    private boolean cornerResize;

    /******************************************************************************************
     * Creates a new frame listener around the associated frame.
     * <p>
     * @param frame The frame to listener to.
     *****************************************************************************************/
    public RFrameListener(RPlatformFrame frame) {
        this.frame = frame;
        titleBar = frame.getTitleBar();
        footerPane = frame.getFooterPane();
    }

    /******************************************************************************************
     * Implements the mouse moved method to determine if the frame should be moved.
     *****************************************************************************************/
    public void mouseMoved(MouseEvent event) {
        if (!frame.isMaximized()) {
            Resizer sizer = ResizerFactory.getResizer(frame, event.getX(), event.getY());
            if (sizer != null) {
                frame.setCursor(sizer.getCursor());
            } else {
                frame.setCursor(Cursor.getDefaultCursor());
                isMoveable = titleBar.contains(event.getPoint());
                cornerResize = footerPane.isInResizeCorner(frame, event.getX(), event.getY());
            }
        }
    }

    /******************************************************************************************
     * Implements the mouse pressed method to see if a resizer needs to be set.
     *****************************************************************************************/
    public void mousePressed(MouseEvent event) {
        if (!frame.isMaximized()) {
            x = event.getX();
            y = event.getY();
            if (cornerResize) {
                x = frame.getWidth();
                y = frame.getHeight() - 1;
            }
            resizer = ResizerFactory.getResizer(frame, x, y);
        }
    }

    /******************************************************************************************
     * Implements the mouse dragged method to drag the frame.
     *****************************************************************************************/
    public void mouseDragged(MouseEvent event) {
        if (!frame.isMaximized()) {
            int mouseX = event.getX();
            int mouseY = event.getY();
            if (resizer != null) {
                int deltaX = mouseX - x;
                int deltaY = mouseY - y;
                int oldX = frame.getX();
                int oldY = frame.getY();
                resizer.resize(frame, deltaX, deltaY);
                x = mouseX - (frame.getX() - oldX);
                y = mouseY - (frame.getY() - oldY);
                frame.validate();
                frame.repaint();
            } else if (isMoveable) {
                int deltaX = mouseX - x;
                int deltaY = mouseY - y;
                int oldX = frame.getX();
                int oldY = frame.getY();
                frame.setLocation(frame.getLocation().x + deltaX, frame.getLocation().y + deltaY);
                x = mouseX - (frame.getX() - oldX);
                y = mouseY - (frame.getY() - oldY);
            }
        }
    }

    /******************************************************************************************
     * When the mouse is released, the resize is released and the frame redrawn.
     *****************************************************************************************/
    public void mouseReleased(MouseEvent event) {
        if (!frame.isMaximized()) {
            if (resizer != null) {
                frame.invalidate();
                frame.validate();
                resizer = null;
            }
            isMoveable = false;
            cornerResize = false;
        }
    }

    /******************************************************************************************
     * Checks if the mouse has entered the frame and sets the cursor appropriately.
     *****************************************************************************************/
    public void mouseEntered(MouseEvent event) {
        if (!frame.isMaximized()) {
            Resizer sizer = ResizerFactory.getResizer(frame, event.getX(), event.getY());
            if (sizer != null) {
                frame.setCursor(sizer.getCursor());
            } else {
                frame.setCursor(Cursor.getDefaultCursor());
                isMoveable = titleBar.contains(event.getPoint());
                cornerResize = footerPane.isInResizeCorner(event.getPoint());
            }
        }
    }

    /******************************************************************************************
     * Checks if the mouse has existed the frame and sets the cursor appropriately.
     *****************************************************************************************/
    public void mouseExited(MouseEvent event) {
        if (!frame.isMaximized()) {
            if (resizer == null) {
                frame.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    /******************************************************************************************
     * Implements the mouse clicked method to maximize the screen when the titlebar is double
     * clicked.
     *****************************************************************************************/
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(event)) {
            if (titleBar.contains(event.getPoint())) {
                frame.setMaximized();
            }
        }
    }
}
