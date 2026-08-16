package oracle.retail.sim.client.swing.util;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;

/*******************************************************************************************************
 * This class provides static graphics utility methods, similar to BasicGraphicsUtil.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class GraphicsUtility {

    /****************************************************************************************************
     * Draw an underlined string with the graphics <code>graphics</code> at location
     * (<code>x</code>, <code>y</code>) just like <code>graphics.drawString</code> would.
     * The character at index <code>mnemonicIndex</code> in text will be underlined below the full text
     * underline. If <code>index</code> is beyond the bounds of <code>text</code> (including < 0), nothing
     * will be underlined.
     * <p>
     * @param graphics The Graphics instance to draw on.
     * @param text The text to draw.
     * @param mnemonicIndex The index of any mnemonic that is to be underlined. If -1, no mnemonic will be
     * underlined.
     * @param x The 'x' coordinate where the string will be drawn.
     * @param y The 'y' coordinate where the string will be drawn.
     *****************************************************************************************************/
    public static void drawUnderlinedString(Graphics graphics, String text, int mnemonicIndex, int x, int y) {
        int endUnderlineOffset = 0;
        if (text != null) {
            endUnderlineOffset = text.length() - 1;
        }
        drawUnderlinedString(graphics, text, mnemonicIndex, x, y, 0, endUnderlineOffset);
    }

    /****************************************************************************************************
     * Draw an underlined string with the graphics <code>graphics</code> at location
     * (<code>x</code>, <code>y</code>) just like <code>graphics.drawString</code> would.
     * The character at index <code>mnemonicIndex</code> in text will be underlined below the full text
     * underline. If <code>index</code> is beyond the bounds of <code>text</code> (including < 0), nothing
     * will be underlined.
     * <p>
     * @param graphics The Graphics instance to draw on.
     * @param text The text to draw.
     * @param mnemonicIndex The index of any mnemonic that is to be underlined. If -1, no mnemonic will be
     * underlined.
     * @param x The 'x' coordinate where the string will be drawn.
     * @param y The 'y' coordinate where the string will be drawn.
     * @param startUnderlineOffset The offset from the beginning of the string where underlining is to begin.
     * @param endUnderlineOffset The offset from the beginning of the string where underlining is to end.
     *****************************************************************************************************/
    public static void drawUnderlinedString(Graphics graphics, String text, int mnemonicIndex, int x, int y, int startUnderlineOffset, int endUnderlineOffset) {

        // Draw the text and the hyperlink underline. This takes into account complex characters --
        // This is based on code in BasicGraphicsUtils.drawStringUnderlineCharAt.
        int underlineX = x;
        int underlineWidth;
        int mnemonicOffset = -1;
        int mnemonicWidth = 1;
        boolean complex = false;
        int descent;

        // Check for complex characters
        int count = text.length();
        for (int i = 0; i < count; i++) {
            char ch = text.charAt(i);
            if (ch >= 0x0590 && ch < 0x109f) {
                complex = true;
                break;
            }
        }

        if (!complex || !(graphics instanceof Graphics2D)) {
            graphics.drawString(text, x, y);
            FontMetrics metrics = graphics.getFontMetrics();
            descent = metrics.getDescent();
            if (mnemonicIndex > -1) {
                mnemonicOffset = x + metrics.stringWidth(text.substring(0, mnemonicIndex));
                mnemonicWidth = metrics.charWidth(text.charAt(mnemonicIndex));
            }
            if (startUnderlineOffset > 0) {
                underlineX = x + metrics.stringWidth(text.substring(0, startUnderlineOffset));
            } else {
                underlineX = x;
            }
            String textToUnderline = text.substring(startUnderlineOffset);
            underlineWidth = metrics.stringWidth(textToUnderline);
        } else {
            // Complex characters in the text...
            Graphics2D graphics2D = (Graphics2D) graphics;
            TextLayout textLayout = new TextLayout(text, graphics.getFont(), graphics2D.getFontRenderContext());
            textLayout.draw(graphics2D, x, y);
            Shape shape = textLayout.getLogicalHighlightShape(0, text.length());
            Rectangle rect = shape.getBounds();
            underlineWidth = rect.width;
            descent = (int) textLayout.getDescent();
            if (mnemonicIndex > -1) {
                shape = textLayout.getLogicalHighlightShape(mnemonicIndex, mnemonicIndex + 1);
                rect = shape.getBounds();
                mnemonicOffset = x + rect.x;
                mnemonicWidth = rect.width;
            }
            if (startUnderlineOffset > 0) {
                shape = textLayout.getLogicalHighlightShape(0, startUnderlineOffset);
                rect = shape.getBounds();
                underlineX = x + rect.width;
                underlineWidth -= rect.width;
            }
        }

        // Draw the underline
        int underlineY = y + descent - 1;
        int underlineHeight = 1;
        graphics.fillRect(underlineX, underlineY, underlineWidth, underlineHeight);

        if (mnemonicIndex > -1 && mnemonicIndex < text.length()) {
            underlineY = underlineY + 3;
            graphics.fillRect(mnemonicOffset, underlineY, mnemonicWidth, underlineHeight);
        }
    }

    /*****************************************************************************************************
     * Draws a focus rectange within the graphics defined by the bound (x, y, width, height).
     *****************************************************************************************************/
    public static void drawFocusRect(Graphics graphics, int x, int y, int width, int height) {
        int vx;
        int vy;

        // Draw upper and lower horizontal dashes
        for (vx = x; vx < x + width; vx = vx + 3) {
            int dashWidth = x + width - vx < 2 ? 1 : 2;
            graphics.fillRect(vx, y, dashWidth, 1);
            graphics.fillRect(vx, y + height - 1, dashWidth, 1);
        }

        // Draw left and right vertical dashes
        for (vy = y; vy < y + height; vy = vy + 3) {
            int dashHeight = y + height - vy < 2 ? 1 : 2;
            graphics.fillRect(x, vy, 1, dashHeight);
            graphics.fillRect(x + width - 1, vy, 1, dashHeight);
        }
    }
}
