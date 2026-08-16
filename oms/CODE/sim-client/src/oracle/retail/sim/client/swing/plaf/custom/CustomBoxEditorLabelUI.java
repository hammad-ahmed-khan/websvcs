package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.metal.MetalLabelUI;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.editor.REditorLabel;

/*********************************************************************************************
 * Chrome Box Editor Label UI
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class CustomBoxEditorLabelUI extends MetalLabelUI {

    protected static CustomBoxEditorLabelUI chromeEditorLabelUI = new CustomBoxEditorLabelUI();

    /**********************************************************************************************
     * Creates the UI component.
     **********************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return chromeEditorLabelUI;
    }

    /**********************************************************************************************
     * Paint clippedText at textX, textY with the labels foreground color.
     **********************************************************************************************/
    protected void paintEnabledText(JLabel label, Graphics graphics, String text, int textX, int textY) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        REditorLabel editorLabel = (REditorLabel) label;

        String requiredSymbol = UIManager.getString(UIThemeName.EDITOR_LABEL_REQUIRED_SYMBOL);
        Color requiredColor = UIManager.getColor(UIThemeName.EDITOR_LABEL_REQUIRED_FOREGROUND);

        int symbolLength = 0;
        int symbolMetric = 0;

        if (requiredSymbol != null) {
            symbolLength = requiredSymbol.length();
            symbolMetric = fontMetrics.stringWidth(requiredSymbol);

            if (editorLabel.isRequired()) {
                graphics.setColor(requiredColor);
                graphics.drawString(requiredSymbol, textX, textY);
            }

            int index = StringUtility.indexOf(text, requiredSymbol);
            if (index != -1) {
                text = StringUtility.substring(text, index + symbolLength);
            }
        }

        int mnemIndex = editorLabel.getDisplayedMnemonicIndex();
        if (mnemIndex != -1) {
            mnemIndex = mnemIndex + symbolLength;
        }

        // Paint Non-Repeating Left-Alignment Suffix
        graphics.setColor(label.getForeground());
        BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, text, mnemIndex, textX + symbolMetric, textY);
    }

    /**********************************************************************************************
     * Just paint the text gray (Label.disabledForeground) rather than in the labels foreground color.
     **********************************************************************************************/
    protected void paintDisabledText(JLabel label, Graphics graphics, String text, int textX, int textY) {
        paintEnabledText(label, graphics, text, textX, textY);
    }
}
