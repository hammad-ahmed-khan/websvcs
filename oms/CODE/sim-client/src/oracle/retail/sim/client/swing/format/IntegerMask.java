package oracle.retail.sim.client.swing.format;

import java.text.NumberFormat;
import java.text.ParseException;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.MaskAdaptor;

/********************************************************************************************************
 * This class handles formatting and unformatting integer data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IntegerMask extends MaskAdaptor {

    // Validation Variables
    private int maximumValue = Integer.MAX_VALUE;
    private int minimumValue = Integer.MIN_VALUE;
    private int zeroValue;
    private boolean groupingUsed;
    private boolean negativeValueAllowed = true;

    /****************************************************************************************************
     * Returns new IntegerMask object.
     ***************************************************************************************************/
    public IntegerMask() {
    }

    /****************************************************************************************************
     * Returns new IntegerMask object.
     * <p>
     * @param allowNegative True if the validator should allow negative values, false otherwise.
     ***************************************************************************************************/
    public IntegerMask(boolean allowNegative) {
        setNegativeValueAllowed(allowNegative);
    }

    /****************************************************************************************************
     * Returns new IntegerMask object.
     * <p>
     * @param maxValue The maximum value allowed by the validator.
     ***************************************************************************************************/
    public IntegerMask(int maxValue) {
        setMaximumValue(maxValue);
    }

    /****************************************************************************************************
     * Returns new IntegerMask object.
     * <p>
     * @param maxValue The maximum value allowed by the validator.
     * @param minValue The minimum value allowed by the validator.
     ***************************************************************************************************/
    public IntegerMask(int maxValue, int minValue) {
        setMaximumValue(maxValue);
        setMinimumValue(minValue);
    }

    /****************************************************************************************************
     * Returns new IntegerMask object.
     * <p>
     * @param maxValue The maximum value allowed by the validator.
     * @param minValue The minimum value allowed by the validator.
     * @param allowNegative True if the validator should allow negative values, false otherwise.
     ***************************************************************************************************/
    public IntegerMask(int maxValue, int minValue, boolean allowNegative) {
        setMaximumValue(maxValue);
        setMinimumValue(minValue);
        setNegativeValueAllowed(allowNegative);
    }

    /****************************************************************************************************
     * Retrieves the maximum value allowed by the validator.
     * <p>
     * @return The maximum value.
     ***************************************************************************************************/
    public int getMaximumValue() {
        return maximumValue;
    }

    /****************************************************************************************************
     * Assigns the maximum value allowed by the validator. The default is Integer.MAX_INT.
     * <p>
     * @param maxValue The maximum value.
     ***************************************************************************************************/
    public void setMaximumValue(int maxValue) {
        maximumValue = maxValue;
    }

    /****************************************************************************************************
     * Retrieves the minimum value allowed by the validator.
     * <p>
     * @return The minimum value.
     ***************************************************************************************************/
    public int getMinimumValue() {
        return minimumValue;
    }

    /****************************************************************************************************
     * Assigns the minimum value allowed by the validator. The default is Integer.MIN_INT.
     * <p>
     * @param minValue The minimum value.
     ***************************************************************************************************/
    public void setMinimumValue(int minValue) {
        minimumValue = minValue;

        if (minimumValue > -1) {
            negativeValueAllowed = false;
        }
    }

    /****************************************************************************************************
     * Retrieves whether or not a negative value is allowed.
     * <p>
     * @return True if a negative value is allowed, false otherwise.
     ***************************************************************************************************/
    public boolean isNegativeValueAllowed() {
        return negativeValueAllowed;
    }

    /****************************************************************************************************
     * Assigns whether or not a negative value is allowed.
     * <p>
     * @param allowed True if a negative value is allowed, false otherwise.
     ***************************************************************************************************/
    public void setNegativeValueAllowed(boolean allowed) {
        negativeValueAllowed = allowed;
    }

    /****************************************************************************************************
     * Retrieves whether or not grouping should be used in the formatting.
     * <p>
     * @return True if a grouping is allowed, false otherwise.
     ***************************************************************************************************/
    public boolean isGroupingUsed() {
        return groupingUsed;
    }

    /****************************************************************************************************
     * Assigns whether or not grouping should be used in the formatting.
     * <p>
     * @param allowed True if grouping is allowed in formatting, false otherwise.
     ***************************************************************************************************/
    public void setGroupingUsed(boolean allowed) {
        groupingUsed = allowed;
    }

    /****************************************************************************************************
     * This method formats the data as appropriate. This method is called when text is set on a field
     * with a mask. If the format attempt fails, the original text should be returned. This erroneous
     * text should be caught by validate() later.
     * <p>
     * @param text The input text.
     * @return The formatted text.
     ***************************************************************************************************/
    public String formatData(Object data) {
        if (data != null) {
            try {
                return LocaleManager.getIntegerFormatter(groupingUsed).format(data);
            } catch (Throwable exception) {
                return data.toString();
            }
        }
        return StringConstants.EMPTY;
    }

    /****************************************************************************************************
     * This method formats the text as appropriate. This method is called when text is set on a field
     * with a mask. If the format attempt fails, the original text should be returned. This erroneous
     * text should be caught by validate() later.
     * <p>
     * @param text The input text.
     * @return The formatted text.
     ***************************************************************************************************/
    public String format(String text) {
        try {
            if (StringUtility.isValidIntegerInput(text)) {
                NumberFormat formatter = LocaleManager.getIntegerFormatter(groupingUsed);
                return formatter.format(formatter.parse(text));
            }
            return text;
        } catch (ParseException exception) {
            return text;
        }
    }

    /****************************************************************************************************
     * Validates a text string to determine if it represents a valid integer.
     * @param text The text string to validate.
     * @return UIException If the data is not valid.
     ***************************************************************************************************/
    public BusinessException validate(String text) {
        if (StringUtility.isNullOrEmpty(text)) {
            return null;
        }
        if (StringUtility.hasDecimalSeparator(text)) {
            return new BusinessException(CommonMessageText.VALUE_NOT_WHOLE);
        }
        if (!StringUtility.isValidIntegerInput(text)) {
            return buildInvalidNumberException(text);
        }
        try {
            int amount = LocaleManager.getIntegerFormatter().parse(text).intValue();
            if (amount > maximumValue) {
                return getInvalidRangeException(amount);
            }
            if (amount < minimumValue) {
                return getInvalidRangeException(amount);
            }
            if (amount < zeroValue && !negativeValueAllowed) {
                return new BusinessException(CommonMessageText.VALUE_INVALID_NEGATIVE);
            }
        } catch (ParseException parseException) {
            return buildInvalidNumberException(text);
        }
        return null;
    }

    /****************************************************************************************************
     * Produces the invalid range exception for the amount.
     ***************************************************************************************************/
    private BusinessException getInvalidRangeException(int amount) {
        Object[] params = new String[3];
        params[0] = LocaleManager.getNumberFormatter().format(amount);
        params[1] = LocaleManager.getNumberFormatter().format(minimumValue);
        params[2] = LocaleManager.getNumberFormatter().format(maximumValue);
        return new BusinessException(CommonMessageText.VALUE_NOT_IN_RANGE, params);
    }

    /****************************************************************************************************
     * Under new requirements, all characters are valid entry for integer mask and errors are caught
     * later.
     ***************************************************************************************************/
    public boolean validCharacter(char character) {
        return true;
    }

    /****************************************************************************************************
     * Helper method to build the invalid number exception.
     ***************************************************************************************************/
    private BusinessException buildInvalidNumberException(String text) {
        return new BusinessException(CommonMessageText.VALUE_NOT_VALID, text);
    }
}
