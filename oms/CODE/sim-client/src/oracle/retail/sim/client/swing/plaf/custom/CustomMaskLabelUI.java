package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class CustomMaskLabelUI extends BasicLabelUI {

    protected static CustomMaskLabelUI maskLabelUI = new CustomMaskLabelUI();

    public static ComponentUI createUI(JComponent c) {
        return maskLabelUI;
    }

    /**
     * Paint clippedText at textX, textY with background.lighter() and then shifted down and to the right
     * by one pixel with background.darker().
     *
     * @see #paint
     * @see #paintEnabledText
     */
    protected void paintDisabledText(JLabel label, Graphics graphics, String text, int x, int y) {
        int mnemonic = label.getDisplayedMnemonicIndex();
        graphics.setColor(label.getBackground().brighter());
        BasicGraphicsUtils.drawString(graphics, text, mnemonic, x + 1, y + 1);
        graphics.setColor(label.getForeground().brighter());
        BasicGraphicsUtils.drawString(graphics, text, mnemonic, x + 1, y + 1);
    }
}
