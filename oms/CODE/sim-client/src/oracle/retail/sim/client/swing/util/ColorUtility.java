package oracle.retail.sim.client.swing.util;

import java.awt.Color;

/******************************************************************************************
 * The static color utility class provides a set of convenience methods for working with
 * color.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class ColorUtility {

    /*****************************************************************************************
     * Private constructor forces a static class.
     *****************************************************************************************/
    private ColorUtility() {
    }

    /*****************************************************************************************
     * Converts a color from its HEX string (e.g. #CCFF66 or CCFF66) into a
     * <code>java.awt.Color</code>.
     * <p>
     * @param hexValue The six-character hex string either proceeded by a "#" or not.
     *****************************************************************************************/
    public static Color createColor(String hexValue) {
        hexValue = hexValue.trim();

        if (hexValue == null || hexValue.length() == 0) {
            throw new IllegalArgumentException("Hex Value cannot be less than six characters.");
        }

        if (hexValue.startsWith("#")) {
            hexValue = hexValue.substring(1);
        }

        if (hexValue.length() != 6) {
            throw new IllegalArgumentException("Hex Value cannot be less than six characters.");
        }

        String red = hexValue.substring(0, 2);
        String green = hexValue.substring(2, 4);
        String blue = hexValue.substring(4, 6);

        return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16));
    }

    /*****************************************************************************************
     * Returns a color that's 20% brighter. This is slightly brighter than the
     * <code>java.awt.Color.brighter()</code> method.
     * <p>
     * @param color The color to make brighter.
     *****************************************************************************************/
    public static Color slightlyBrighter(Color color) {
        if (color != null) {
            int red = Math.min(255, color.getRed() + (int) (0.2 * color.getRed()));
            int green = Math.min(255, color.getGreen() + (int) (0.2 * color.getGreen()));
            int blue = Math.min(255, color.getBlue() + (int) (0.2 * color.getBlue()));

            color = new Color(red, green, blue);
        }
        return color;
    }

    /*****************************************************************************************
     * Returns a color that's 20% darker. This is slightly darker than the
     * <code>java.awt.Color.brighter()</code> method.
     * <p>
     * @param color The color to make darker.
     *****************************************************************************************/
    public static Color slightlyDarker(Color color) {
        if (color != null) {
            int red = Math.max(0, color.getRed() - (int) (0.2 * color.getRed()));
            int green = Math.max(0, color.getGreen() - (int) (0.2 * color.getGreen()));
            int blue = Math.max(0, color.getBlue() - (int) (0.2 * color.getBlue()));

            color = new Color(red, green, blue);
        }
        return color;
    }

    /*****************************************************************************************
     * Returns a color with a increased blue tint than the parameter.
     * <p>
     * @param color The color to make more blue.
     *****************************************************************************************/
    public static Color blueTint(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = Math.min(255, color.getBlue() + (int) (0.2 * color.getBlue()));
        return new Color(red, green, blue);
    }

    /*****************************************************************************************
     * Returns a color with a increased red tint than the parameter.
     * <p>
     * @param color The color to make more red.
     *****************************************************************************************/
    public static Color redTint(Color color) {
        int red = Math.min(255, color.getRed() + (int) (0.2 * color.getRed()));
        int green = color.getGreen();
        int blue = color.getBlue();
        return new Color(red, green, blue);
    }

    /*****************************************************************************************
     * Returns a color with a increased green tint than the parameter.
     * <p>
     * @param color The color to make more green.
     *****************************************************************************************/
    public static Color greenTint(Color color) {
        int red = color.getRed();
        int green = Math.min(255, color.getGreen() + (int) (0.2 * color.getGreen()));
        int blue = color.getBlue();
        return new Color(red, green, blue);
    }

    /*****************************************************************************************
     * Returns a color with a increased aqua tint than the parameter.
     * <p>
     * @param color The color to make more aqua.
     *****************************************************************************************/
    public static Color aqauTint(Color color) {
        int red = color.getRed();
        int green = Math.min(255, color.getGreen() + (int) (0.2 * color.getGreen()));
        int blue = Math.min(255, color.getBlue() + (int) (0.2 * color.getBlue()));
        return new Color(red, green, blue);
    }

    /*****************************************************************************************
     * Returns a color with a increased orange tint than the parameter.
     * <p>
     * @param color The color to make more orange.
     *****************************************************************************************/
    public static Color orangeTint(Color color) {
        int red = Math.min(255, color.getRed() + (int) (0.2 * color.getRed()));
        int green = Math.min(255, color.getGreen() + (int) (0.2 * color.getGreen()));
        int blue = color.getBlue();
        return new Color(red, green, blue);
    }

    /*****************************************************************************************
     * Returns a color with a increased purple tint than the parameter.
     * <p>
     * @param color The color to make more purple.
     *****************************************************************************************/
    public static Color purpleTint(Color color) {
        int red = Math.min(255, color.getRed() + (int) (0.2 * color.getRed()));
        int green = color.getGreen();
        int blue = Math.min(255, color.getGreen() + (int) (0.2 * color.getBlue()));
        return new Color(red, green, blue);
    }

    /*****************************************************************************************
     * Returns a color with a duller tint than the parameter (creates disabled text color
     * for any color). It moves the color 50% of the way to gray from its current color.
     * <p>
     * @param color The color to make more purple.
     *****************************************************************************************/
    public static Color disabledTint(Color color) {
        int redOffset = (int) ((color.getRed() - 190) * 0.5);
        int greenOffset = (int) ((color.getGreen() - 190) * 0.5);
        int blueOffset = (int) ((color.getBlue() - 200) * 0.5);

        int red = color.getRed() - redOffset;
        int green = color.getGreen() - greenOffset;
        int blue = color.getBlue() - blueOffset;

        return new Color(red, green, blue);
    }

    /*****************************************************************************************
     * Returns the beginning gradient color for an end gradient color. This is approximately
     * a color that is 60% closer to white.
     * <p>
     * @param gradientEndColor The color that the gradient transition should end on.
     * @return The color that the gradient transition should begin with.
     *****************************************************************************************/
    public static Color getMildGradientStart(Color gradientEndColor) {
        int originalRed = gradientEndColor.getRed();
        int originalGreen = gradientEndColor.getGreen();
        int originalBlue = gradientEndColor.getBlue();

        int deltaRed = (int) (0.5 * (255 - originalRed));
        int deltaGreen = (int) (0.5 * (255 - originalGreen));
        int deltaBlue = (int) (0.5 * (255 - originalBlue));

        return new Color(originalRed + deltaRed, originalGreen + deltaGreen, originalBlue + deltaBlue);
    }

    /*****************************************************************************************
     * Returns the beginning gradient color for an end gradient color. This is approximately
     * a color that is 60% closer to white.
     * <p>
     * @param gradientEndColor The color that the gradient transition should end on.
     * @return The color that the gradient transition should begin with.
     *****************************************************************************************/
    public static Color getGradientStart(Color gradientEndColor) {
        int originalRed = gradientEndColor.getRed();
        int originalGreen = gradientEndColor.getGreen();
        int originalBlue = gradientEndColor.getBlue();

        int deltaRed = (int) (0.85 * (255 - originalRed));
        int deltaGreen = (int) (0.85 * (255 - originalGreen));
        int deltaBlue = (int) (0.85 * (255 - originalBlue));

        return new Color(originalRed + deltaRed, originalGreen + deltaGreen, originalBlue + deltaBlue);
    }

    /*****************************************************************************************
     * Computes a smooth gradient end color for a gradient start color. This is approximately
     * a color that is 60% closer to black.
     * <p>
     * @param color A smooth gradient end color.
     *****************************************************************************************/
    public static Color getGradientEnd(Color gradientStartColor) {
        int originalRed = gradientStartColor.getRed();
        int originalGreen = gradientStartColor.getGreen();
        int originalBlue = gradientStartColor.getBlue();

        int deltaRed = (int) (0.85 * originalRed);
        int deltaGreen = (int) (0.85 * originalGreen);
        int deltaBlue = (int) (0.85 * originalBlue);

        return new Color(originalRed - deltaRed, originalGreen - deltaGreen, originalBlue - deltaBlue);
    }
}
