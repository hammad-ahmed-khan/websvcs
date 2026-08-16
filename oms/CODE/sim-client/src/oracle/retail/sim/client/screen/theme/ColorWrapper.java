package oracle.retail.sim.client.screen.theme;

import javax.swing.plaf.ColorUIResource;
import oracle.retail.sim.common.theme.CustomColor;

/********************************************************************************************************
 * Wrapper for a ColorUIResource to assist display in the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ColorWrapper {

    private String name;
    private ColorUIResource color;
    private boolean custom;

    public ColorWrapper(String name, ColorUIResource color) {
        this.name = name;
        this.color = color;
        custom = false;
    }

    public ColorWrapper(String name, CustomColor color) {
        this.name = name;
        this.color = new ColorUIResource(color.getRed(), color.getGreen(), color.getBlue());
        custom = true;
    }

    public void setColor(ColorUIResource color) {
        if (color != null) {
            this.color = color;
        }
    }

    public void setCustom(boolean isCustom) {
        custom = isCustom;
    }

    public ColorUIResource getColor() {
        return color;
    }

    public CustomColor getCustomColor() {
        return new CustomColor(name, color.getRed(), color.getGreen(), color.getBlue());
    }

    public String getName() {
        return name;
    }

    public int getRed() {
        return color.getRed();
    }

    public int getGreen() {
        return color.getGreen();
    }

    public int getBlue() {
        return color.getBlue();
    }

    public Boolean isCustom() {
        return custom;
    }
}
