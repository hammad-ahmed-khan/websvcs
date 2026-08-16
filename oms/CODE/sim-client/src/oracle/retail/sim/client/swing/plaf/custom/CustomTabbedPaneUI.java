package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import oracle.retail.sim.client.swing.util.FontUtility;
import oracle.retail.sim.client.swing.widget.RTabbedPane;

/******************************************************************************************
 * This class subclasses the standard MetalButtonUI in order to paint the tabbed pane
 * using the chrome look and feel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class CustomTabbedPaneUI extends MetalTabbedPaneUI {

    private GeneralPath path = new GeneralPath();
    private Color selectForeground;
    private Color selectBackground;
    private Font displayFont;

    /******************************************************************************************
     * Creates a new ChromeTabbedPaneUI for a specific component.
     * <p>
     * @param component The component to create the ChromeTabbedPaneUI for.
     * <p>
     * @return The ChromeTabbedPaneUI object.
     *****************************************************************************************/
    public static ComponentUI createUI(JComponent tabbedPane) {
        return new CustomTabbedPaneUI();
    }

    /****************************************************************************************
     * Installs the UI
     ****************************************************************************************/
    public void installUI(JComponent component) {
        super.installUI(component);
    }

    /****************************************************************************************
     * Installs fonts and colors for an RTabbedPane.
     ****************************************************************************************/
    protected void installFontsAndColors(JComponent component) {
        if (component instanceof RTabbedPane) {
            RTabbedPane tabbedPane = (RTabbedPane) component;

            displayFont = tabbedPane.getFont();
            tabAreaBackground = tabbedPane.getTabAreaBackground();
            selectForeground = tabbedPane.getSelectedTabForeground();
            selectBackground = tabbedPane.getSelectedTabBackground();

            if (selectBackground == null) {
                selectBackground = tabAreaBackground;
            }
        } else {
            displayFont = UIManager.getFont(UIThemeName.TABBEDPANE_FONT);
            selectForeground = UIManager.getColor(UIThemeName.TABBEDPANE_FOREGROUND);
            selectBackground = UIManager.getColor(UIThemeName.TABBEDPANE_BACKGROUND);
        }
    }

    /****************************************************************************************
     * Paints the entired tabbed pane component.
     ****************************************************************************************/
    public void paint(Graphics graphics, JComponent component) {
        installFontsAndColors(component);

        int selectedIndex = tabPane.getSelectedIndex();
        int tabPlacement = tabPane.getTabPlacement();
        int tabCount = tabPane.getTabCount();

        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();
        Rectangle clipRect = graphics.getClipBounds();

        paintContentBorder(graphics, tabPlacement, selectedIndex);

        int start = -1;
        int next = -1;
        int end = -1;

        // Paint TabRuns From Back To Front
        for (int runIndex = runCount - 1; runIndex >= 0; runIndex--) {
            start = tabRuns[runIndex];
            next = tabRuns[runIndex + 1];
            end = tabCount - 1;

            if (runIndex == runCount - 1) {
                next = tabRuns[0];
            }
            if (next != 0) {
                end = next - 1;
            }

            for (int tabIndex = start; tabIndex <= end; tabIndex++) {
                if (rects[tabIndex].intersects(clipRect)) {
                    paintTab(graphics, tabPlacement, rects, tabIndex, iconRect, textRect);
                }
            }
        }

        // Paint selected tab if its in the front run since it may overlap other tabs
        if (selectedIndex > 0 && getRunForTab(tabCount, selectedIndex) == 0) {
            if (rects[selectedIndex].intersects(clipRect)) {
                paintTab(graphics, tabPlacement, rects, selectedIndex, iconRect, textRect);
            }
        }
    }

    /****************************************************************************************
     * Paints a tab.
     ****************************************************************************************/
    protected void paintTab(Graphics graphics, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {

        Rectangle tabRect = rects[tabIndex];
        int selectedIndex = tabPane.getSelectedIndex();
        boolean isSelected = selectedIndex == tabIndex;

        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintTabBackground(graphics, tabPlacement, tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height, isSelected);

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        paintTabBorder(graphics, tabPlacement, tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height, isSelected);

        String title = tabPane.getTitleAt(tabIndex);
        Font font = displayFont;

        if (isSelected) {
            font = FontUtility.getBoldFont(font);
        }

        FontMetrics metrics = graphics.getFontMetrics(font);
        Icon icon = getIconForTab(tabIndex);

        layoutLabel(tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected);

        paintText(graphics, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
        paintIcon(graphics, tabPlacement, tabIndex, icon, iconRect, isSelected);
    }

    /****************************************************************************************
     * Paints the tab background using Graphics 2D.
     ****************************************************************************************/
    protected void paintTabBackground(Graphics graphics, int tabPlacement, int tabIndex, int x, int y, int width, int height, boolean isSelected) {

        if (isSelected) {
            graphics.setColor(selectBackground);
        } else {
            graphics.setColor(tabPane.getBackgroundAt(tabIndex));
        }

        int yshift = 0;
        if (isSelected) {
            yshift = 1;
        }

        path.reset();
        path.moveTo(x, y + height + 1 + yshift);
        path.lineTo(x, y + height - 3);
        path.quadTo(x + 2, y + 1, x + 11, y);
        path.lineTo(x + width - 7, y);
        path.quadTo(x + width - 3, y + 2, x + width - 3, y + 6);
        path.lineTo(x + width - 3, y + height + 1 + yshift);

        ((Graphics2D) graphics).fill(path);
    }

    /****************************************************************************************
     * Paints a tab border using graphics 2D.
     ****************************************************************************************/
    protected void paintTabBorder(Graphics graphics, int tabPlacement, int tabIndex, int x, int y, int width, int height, boolean isSelected) {

        Graphics2D graphics2D = (Graphics2D) graphics;

        Color borderColor;

        if (isSelected) {
            borderColor = selectBackground.darker();
        } else {
            borderColor = tabPane.getBackgroundAt(tabIndex).darker();
        }
        graphics.setColor(borderColor);

        if (tabIndex != 0) {
            graphics.drawLine(x, y + height, x, y + height - 6);
        }
        path.reset();
        path.moveTo(x, y + height + 1 + (isSelected ? 1 : 0));
        path.lineTo(x, y + height - 8);
        path.quadTo(x + 2, y + 1, x + 12, y);
        path.lineTo(x + width - 7, y);
        path.quadTo(x + width - 5, y + 1, x + width - 4, y + 2);

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.draw(path);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        graphics.setColor(borderColor.darker());

        path.reset();
        path.moveTo(x + width - 4, y + 2);
        path.lineTo(x + width - 3, y + 3);
        path.lineTo(x + width - 3, y + height);

        graphics2D.draw(path);
    }

    /****************************************************************************************
     * Paints the text on a tab.
     ****************************************************************************************/
    protected void paintText(Graphics graphics, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {

        graphics.setFont(font);

        if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
            Color foreground = selectForeground;
            if (tabPane.getSelectedIndex() != tabIndex) {
                foreground = tabPane.getForegroundAt(tabIndex);
            }
            graphics.setColor(foreground);
            graphics.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        } else {
            Color backgroundColor = tabPane.getBackgroundAt(tabIndex);

            graphics.setColor(backgroundColor.brighter());
            graphics.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            graphics.setColor(backgroundColor.darker());
            graphics.drawString(title, textRect.x - 1, textRect.y + metrics.getAscent() - 1);
        }
    }

    /****************************************************************************************
     * Gets the indent for the specific tab run.
     ****************************************************************************************/
    protected int getTabRunIndent(int tabPlacement, int run) {
        return run * 3;
    }

    /****************************************************************************************
     * Lays out the tab pane as a compound label.
     ****************************************************************************************/
    protected void layoutLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon, Rectangle tabRect, Rectangle iconRect, Rectangle textRect, boolean isSelected) {

        textRect.x = 0;
        textRect.y = 0;
        iconRect.x = 0;
        iconRect.y = 0;

        SwingUtilities.layoutCompoundLabel(tabPane, metrics, title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER, SwingUtilities.CENTER, SwingUtilities.TRAILING, tabRect, iconRect, textRect,
                textIconGap);

        textRect.y = textRect.y + 1;
    }
}
