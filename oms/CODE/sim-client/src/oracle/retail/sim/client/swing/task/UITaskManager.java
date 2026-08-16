package oracle.retail.sim.client.swing.task;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;

/**************************************************************************************************
 * This class handles placing a glass pane in front of the application while the application is
 * considered "busy".
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *************************************************************************************************/

public class UITaskManager {

    private static final BusyGlassPane GLASSPANE = new BusyGlassPane();

    /**************************************************************************************************
     * Shows the app as disabled - use this for potentially long-winded operations. Make sure to reverse
     * this when the operation has completed! This method shows the appropriate hourglass icon, and will
     * disable ALL user-initiated events (if busy is true.) If busy is false, these operations will be
     * reversed.
     * <p>
     * If the parameter is false, we remove the glass pane in a SwingUtilities runnable so that events
     * are consumed by the glass pane up until the moment it is removed.
     * <p>
     * @param busy True if the application should show busy, false otherwise.
     *************************************************************************************************/
    public static void showBusy(boolean busy) {
        if (busy) {
            ApplicationInternal.getFrame().setGlassPane(GLASSPANE);
            GLASSPANE.setVisible(true);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GLASSPANE.setVisible(false);
                }
            });
        }
    }

    /**************************************************************************************************
     * BUSY GLASS PANE - Use when the app is busy.
     *************************************************************************************************/

    private static class BusyGlassPane extends JPanel {
        private static final long serialVersionUID = 7014147289301294085L;

        BusyGlassPane() {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setName("BusyGlassPane");
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
            });
            addMouseMotionListener(new MouseMotionAdapter() {
            });
            addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                }

                public void keyReleased(KeyEvent e) {
                }
            });
        }
    }
}
