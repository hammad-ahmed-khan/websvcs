package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.metal.MetalLabelUI;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.REditorLabel;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.common.core.locale.StringConstants;

/*********************************************************************************************
 * Chrome Editor Label UI
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *********************************************************************************************/

public class CustomEditorLabelUI extends MetalLabelUI {

    protected static CustomEditorLabelUI chromeEditorLabelUI = new CustomEditorLabelUI();

    /**********************************************************************************************
     * Creates the UI component.
     **********************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return chromeEditorLabelUI;
    }

    /**********************************************************************************************
     * Paint clippedText at textX, textY with the labels foreground color.
     **********************************************************************************************/
    public Dimension getPreferredSize(JComponent component) {
        Dimension dimension = super.getPreferredSize(component);
        FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
        String suffixSymbol = UIManager.getString(UIThemeName.EDITOR_LABEL_SUFFIX);
        dimension.width = dimension.width + fontMetrics.stringWidth(suffixSymbol);
        return dimension;
    }

    /**********************************************************************************************
     * Paint clippedText at textX, textY with the labels foreground color.
     **********************************************************************************************/
    protected void paintEnabledText(JLabel label, Graphics graphics, String text, int textX, int textY) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        REditorLabel editorLabel = (REditorLabel) label;

        String requiredSymbol = UIManager.getString(UIThemeName.EDITOR_LABEL_REQUIRED_SYMBOL);
        String suffixSymbol = UIManager.getString(UIThemeName.EDITOR_LABEL_SUFFIX);
        Color requiredColor = UIManager.getColor(UIThemeName.EDITOR_LABEL_REQUIRED_FOREGROUND);
        boolean underline = StringUtility.booleanValue(UIManager.getString(UIThemeName.EDITOR_LABEL_UNDERLINE));
        boolean repeating = StringUtility.booleanValue(UIManager.getString(UIThemeName.EDITOR_LABEL_REPEATING));

        int symbolLength = 0;
        int symbolMetric = 0;

        if (editorLabel.getHorizontalAlignment() == EditorConstants.RIGHT) {
            textX = textX - fontMetrics.stringWidth(suffixSymbol);
        }

        if (requiredSymbol != null) {
            symbolLength = requiredSymbol.length();
            symbolMetric = fontMetrics.stringWidth(requiredSymbol);

            if (editorLabel.isRequired()) {
                if (label.isEnabled()) {
                    graphics.setColor(requiredColor);
                } else {
                    graphics.setColor(label.getBackground().darker());
                }
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

        // Paint Underline Option
        if (underline) {
            int index = StringUtility.indexOf(text, suffixSymbol);
            if (index > -1) {
                text = StringUtility.substring(text, 0, index);
            }
            graphics.setColor(editorLabel.getForeground());

            BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, text, mnemIndex, textX + symbolMetric, textY);

            String underlineText = buildFullUnderline(editorLabel, fontMetrics);

            graphics.setColor(ColorUtility.disabledTint(editorLabel.getForeground()));
            BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, underlineText, -1, textX, editorLabel.getHeight());
            return;
        }

        // Paint If No Suffix
        if (StringUtility.isNullOrEmpty(suffixSymbol)) {
            if (label.isEnabled()) {
                graphics.setColor(label.getForeground());
            } else {
                graphics.setColor(label.getBackground().darker());
            }
            BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, text, mnemIndex, textX + symbolMetric, textY);
            return;
        }

        // Paint No Suffix Because Of Alignment
        if (editorLabel.getTitleAlignment() != EditorConstants.LEFT) {
            int index = StringUtility.indexOf(text, suffixSymbol);
            text = StringUtility.substring(text, 0, index);
            if (label.isEnabled()) {
                graphics.setColor(label.getForeground());
            } else {
                graphics.setColor(label.getBackground().darker());
            }
            BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, text, mnemIndex, textX + symbolMetric, textY);
            return;
        }

        // Paint Repeating Suffix
        if (repeating) {
            text = buildFullSuffix(editorLabel, fontMetrics, text, suffixSymbol);

            int index = StringUtility.indexOf(text, suffixSymbol);

            String part1 = StringUtility.substring(text, 0, index);
            String part2 = StringUtility.substring(text, index);

            int labelsize = fontMetrics.stringWidth(part1);

            if (label.isEnabled()) {
                graphics.setColor(label.getForeground());
            } else {
                graphics.setColor(label.getBackground().darker());
            }
            BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, part1, mnemIndex, textX + symbolMetric, textY);

            graphics.setColor(ColorUtility.disabledTint(editorLabel.getForeground()));
            BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, part2, -1, textX + symbolMetric + labelsize, textY);

            return;
        }

        // Paint Non-Repeating Left-Alignment Suffix
        if (label.isEnabled()) {
            graphics.setColor(label.getForeground());
        } else {
            graphics.setColor(label.getBackground().darker());
        }
        BasicGraphicsUtils.drawStringUnderlineCharAt(graphics, text, mnemIndex, textX + symbolMetric, textY);
    }

    /**********************************************************************************************
     * Just paint the text gray (Label.disabledForeground) rather than in the labels foreground color.
     **********************************************************************************************/
    protected void paintDisabledText(JLabel label, Graphics graphics, String text, int textX, int textY) {
        paintEnabledText(label, graphics, text, textX, textY);
    }

    /**********************************************************************************************
     * Builds out the suffix to the full size of the label.
     **********************************************************************************************/
    private String buildFullSuffix(REditorLabel label, FontMetrics fontMetrics, String text, String suffix) {
        Insets insets = label.getInsets();

        int width = label.getWidth() - (insets.left + insets.right) - label.getIconTextGap();

        Icon icon = label.getIcon();
        if (icon != null) {
            width = width - label.getIcon().getIconWidth();
        }
        int finalWidth = width - fontMetrics.stringWidth(suffix) - 1;

        while (fontMetrics.stringWidth(text) < finalWidth) {
            text = text + suffix;
        }
        return text;
    }

    /**********************************************************************************************
     * Builds out the underline to the full size of the label.
     **********************************************************************************************/
    private String buildFullUnderline(REditorLabel label, FontMetrics fontMetrics) {
        Insets insets = label.getInsets();

        int width = label.getWidth() - (insets.left + insets.right);
        int finalWidth = width - fontMetrics.stringWidth(StringConstants.DOT) - 1;

        String text = StringConstants.EMPTY;
        while (fontMetrics.stringWidth(text) < finalWidth) {
            text = text + StringConstants.DOT;
        }
        return text;
    }
}
