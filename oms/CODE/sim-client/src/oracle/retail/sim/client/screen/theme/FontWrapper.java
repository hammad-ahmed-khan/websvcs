package oracle.retail.sim.client.screen.theme;

import java.awt.Font;
import javax.swing.plaf.FontUIResource;
import oracle.retail.sim.common.theme.CustomFont;

/********************************************************************************************************
 * Wraps a font ui resource for ease of usage in the table of the font screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FontWrapper {

    public static final String PLAIN = "Plain";
    public static final String BOLD = "Bold";
    public static final String ITALIC = "Italic";
    public static final String ITBLD = "Italic-Bold";

    private String name;
    private FontUIResource font;
    private boolean custom;

    public FontWrapper(String name, FontUIResource font) {
        this.name = name;
        this.font = font;
        custom = false;
    }

    public FontWrapper(String name, CustomFont font) {
        this.name = name;
        this.font = new FontUIResource(font.getFamily(), font.getStyle(), font.getSize());
        custom = true;
    }

    public void setFont(FontUIResource font) {
        if (font != null) {
            this.font = font;
        }
    }

    public void setCustom(boolean isCustom) {
        custom = isCustom;
    }

    public FontUIResource getFont() {
        return font;
    }

    public CustomFont getCustomFont() {
        CustomFont customFont = new CustomFont(name);
        customFont.setFamily(font.getFamily());
        customFont.setStyle(font.getStyle());
        customFont.setSize(font.getSize());
        return customFont;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return font.getFamily();
    }

    public String getStyle() {
        switch (font.getStyle()) {
            case Font.PLAIN:
                return PLAIN;
            case Font.ITALIC:
                return ITALIC;
            case Font.BOLD:
                return BOLD;
            default:
                return ITBLD;
        }
    }

    public int getStyleInt() {
        return font.getStyle();
    }

    public int getSize() {
        return font.getSize();
    }

    public Boolean isCustom() {
        return custom;
    }
}
