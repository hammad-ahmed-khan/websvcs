package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.ChromeUtility;
import oracle.retail.sim.client.swing.util.ColorUtility;

/*****************************************************************************************************
 * The footer pane inside the RFrame.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************************/

public class RPlatformFooterPane extends JPanel {
    private static final long serialVersionUID = -1341770363609967888L;

    private RPanel contentPanel = new RPanel();
    private FooterResizePanel resizePanel = new FooterResizePanel();

    /*****************************************************************************************************
     * Creates a new footer pane.
     *****************************************************************************************************/
    public RPlatformFooterPane() {
        initializeFontAndColors();

        contentPanel.setOpaque(false);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        add(resizePanel, BorderLayout.EAST);
    }

    /*****************************************************************************************************
     * Initializes the fonts and colors used by the footer pane.
     *****************************************************************************************************/
    private void initializeFontAndColors() {
        setBackground(UIManager.getColor(UIThemeName.CHROME_PANEL_BACKGROUND));

        MatteBorder outerBorder = new MatteBorder(0, 1, 1, 1, UIManager.getColor(UIThemeName.FRAME_BORDER_COLOR));
        EmptyBorder innerBorder = new EmptyBorder(2, 2, 2, 2);

        setBorder(new CompoundBorder(outerBorder, innerBorder));
    }

    /*****************************************************************************************************
     * Checks if point is in resize corner.
     * <p>
     * @return True if point is in resize corner, false if not.
     *****************************************************************************************************/
    protected boolean isInResizeCorner(Point point) {
        return resizePanel.contains(point);
    }

    /*****************************************************************************************************
     * Checks if point is in resize corner.
     * <p>
     * @return True if point is in resize corner, false if not.
     *****************************************************************************************************/
    protected boolean isInResizeCorner(JFrame frame, int x, int y) {
        return resizePanel.contains(SwingUtilities.convertPoint(frame, x, y, resizePanel));
    }

    /*****************************************************************************************************
     * Paints the background of the ChromePanel.
     * <p>
     * @param graphics The Graphics object to paint.
     *****************************************************************************************************/
    public void paintComponent(Graphics graphics) {
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();

        Color backgroundColor = getBackground();
        Color startColor = ColorUtility.getGradientStart(backgroundColor);

        ChromeUtility.paintCenteredChrome(graphics, 0, 0, width, height, startColor, backgroundColor);
    }

    /*****************************************************************************************************
     *
     * INNER CLASS - RESIZE PANEL FOR THE FOOTER CORNER
     *
     *****************************************************************************************************/

    private class FooterResizePanel extends JComponent {
        private static final long serialVersionUID = -3793942554943649119L;

        private Color dark = new Color(147, 145, 169);
        private Color light = new Color(245, 243, 254);
        private Dimension size = new Dimension(26, 26);

        public FooterResizePanel() {
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        }

        public void paintComponent(Graphics graphics) {
            // 			Commented out so that the background is the same as the footer.
            //			graphics.setColor(new Color(221, 221, 233));
            //			graphics.fillRect(0, 23, getWidth() - 1, getHeight() - 1);
            graphics.setColor(dark);
            graphics.drawLine(10, 20, 23, 7);
            graphics.drawLine(10, 21, 24, 7);
            graphics.drawLine(16, 20, 23, 13);
            graphics.drawLine(16, 21, 24, 13);
            graphics.drawLine(22, 20, 23, 19);
            graphics.drawLine(22, 21, 24, 19);
            graphics.setColor(light);
            graphics.drawLine(10, 22, 25, 7);
            graphics.drawLine(11, 22, 25, 8);
            graphics.drawLine(16, 22, 25, 13);
            graphics.drawLine(17, 22, 25, 14);
            graphics.drawLine(22, 22, 25, 19);
            graphics.drawLine(23, 22, 25, 20);
        }
    }
}
