package oracle.retail.sim.client.swing.format;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.MaskAdaptor;

/********************************************************************************************************
 * This class handles formatting and unformatting decimal data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DecimalMask extends MaskAdaptor {
    // Validation Variables
    private BigDecimal maximumValue = BigDecimal.valueOf(99999999999.99);
    private BigDecimal minimumValue = BigDecimal.valueOf(-9999999999.99);
    private BigDecimal zeroValue = BigDecimal.ZERO;
    private boolean negativeValueAllowed = true;

    /****************************************************************************************************
     * Returns new DecimalMask object.
     ***************************************************************************************************/
    public DecimalMask() {
    }

    /****************************************************************************************************
     * Returns new DecimalMask object.
     * <p>
     * @param allowNegative True if the validator should allow negative values, false otherwise.
     ***************************************************************************************************/
    public DecimalMask(boolean allowNegative) {
        setNegativeValueAllowed(allowNegative);
    }

    /****************************************************************************************************
     * Returns new DecimalMask object.
     * <p>
     * @param maxValue The maximum value allowed by the validator.
     ***************************************************************************************************/
    public DecimalMask(BigDecimal maxValue) {
        setMaximumValue(maxValue);
    }

    /****************************************************************************************************
     * Returns new DecimalMask object.
     * <p>
     * @param maxValue The maximum value allowed by the validator.
     * @param minValue The minimum value allowed by the validator.
     ***************************************************************************************************/
    public DecimalMask(BigDecimal maxValue, BigDecimal minValue) {
        setMaximumValue(maxValue);
        setMinimumValue(minValue);
    }

    /****************************************************************************************************
     * Returns new DecimalMask object.
     * <p>
     * @param maxValue The maximum value allowed by the validator.
     * @param minValue The minimum value allowed by the validator.
     * @param allowNegative True if the validator should allow negative values, false otherwise.
     ***************************************************************************************************/
    public DecimalMask(BigDecimal maxValue, BigDecimal minValue, boolean allowNegative) {
        setMaximumValue(maxValue);
        setMinimumValue(minValue);
        setNegativeValueAllowed(allowNegative);
    }

    /****************************************************************************************************
     * Retrieves the maximum value allowed by the validator.
     * <p>
     * @return The maximum value.
     ***************************************************************************************************/
    public BigDecimal getMaximumValue() {
        return maximumValue;
    }

    /****************************************************************************************************
     * Assigns the maximum value allowed by the validator. The default is 99999999999.99.
     * <p>
     * @param maxValue The maximum value.
     ***************************************************************************************************/
    public void setMaximumValue(BigDecimal maxValue) {
        if (maxValue != null) {
            maximumValue = maxValue;
        }
    }

    /****************************************************************************************************
     * Retrieves the minimum value allowed by the validator.
     * <p>
     * @return The minimum value.
     ***************************************************************************************************/
    public BigDecimal getMinimumValue() {
        return minimumValue;
    }

    /****************************************************************************************************
     * Assigns the minimum value allowed by the validator. The default is -9999999999.99.
     * <p>
     * @param minValue The minimum value.
     ***************************************************************************************************/
    public void setMinimumValue(BigDecimal minValue) {
        if (minValue != null) {
            minimumValue = minValue;
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
                NumberFormat formatter = LocaleManager.getNumberFormatter();
                formatter.setMaximumFractionDigits(2);
                return formatter.format(data);
            } catch (Throwable exception) {
                return data.toString();
            }
        }
        return StringConstants.EMPTY;
    }

    /****************************************************************************************************
     * Formats a text string to a standard decimal format built by the system. If the text is not a valid
     * decimal, the original text is returned.
     * <p>
     * @param text The input text.
     * @return The formatted text.
     ***************************************************************************************************/
    public String format(String text) {
        text = text.trim();

        if (StringUtility.isNullOrEmpty(text)) {
            return StringConstants.EMPTY;
        }
        if (!StringUtility.isValidDecimalInput(text)) {
            return text;
        }
        try {
            NumberFormat formatter = LocaleManager.getNumberFormatter();
            Number number = formatter.parse(text);
            BigDecimal value = BigDecimal.valueOf(number.doubleValue());
            BigDecimal scaledValue = value.setScale(2, BigDecimal.ROUND_HALF_UP);

            formatter.setMaximumFractionDigits(2);
            formatter.setMinimumFractionDigits(2);

            return formatter.format(scaledValue.doubleValue());
        } catch (Exception exception) {
            return text;
        }
    }

    /****************************************************************************************************
     * Returns an unformatted version of the decimal.
     * <p>
     * @param text The input string.
     * @return The unformatted string.
     ***************************************************************************************************/
    public String unformat(String text) {
        return text.trim();
    }

    /****************************************************************************************************
     * Validates a text string to determine if it represents a valid decimal.
     * @param text The text string to validate.
     * @return UIException If the data is not valid.
     ***************************************************************************************************/
    public BusinessException validate(String text) {
        if (text.length() == 0) {
            return null;
        }
        if (!StringUtility.isValidDecimalInput(text)) {
            return buildInvalidNumberException(text);
        }
        try {
            NumberFormat formatter = LocaleManager.getNumberFormatter();
            BigDecimal value = BigDecimal.valueOf(formatter.parse(text).doubleValue());
            BigDecimal amount = value.setScale(2, BigDecimal.ROUND_HALF_UP);

            if (amount.compareTo(maximumValue) > 0) {
                return getInvalidRangeException(amount);
            }
            if (amount.compareTo(minimumValue) < 0) {
                return getInvalidRangeException(amount);
            }
            if (amount.compareTo(zeroValue) < 0 && !negativeValueAllowed) {
                return new BusinessException(CommonMessageText.VALUE_INVALID_NEGATIVE);
            }
        } catch (ParseException parseException) {
            return buildInvalidNumberException(text);
        }
        return null;
    }

    /****************************************************************************************************
     * Helper method to build the invalid number exception.
     ***************************************************************************************************/
    private BusinessException buildInvalidNumberException(String text) {
        return new BusinessException(CommonMessageText.VALUE_NOT_VALID, text);
    }

    /****************************************************************************************************
     * Produces the invalid range exception for the amount.
     ***************************************************************************************************/
    private BusinessException getInvalidRangeException(BigDecimal amount) {
        Object[] params = new String[3];
        params[0] = LocaleManager.getNumberFormatter().format(amount);
        params[1] = LocaleManager.getNumberFormatter().format(minimumValue);
        params[2] = LocaleManager.getNumberFormatter().format(maximumValue);
        return new BusinessException(CommonMessageText.VALUE_NOT_IN_RANGE, params);
    }

    /****************************************************************************************************
     * Under new funcationality, all characters are valid and errors are caught later.
     ***************************************************************************************************/
    public boolean validCharacter(char character) {
        return true;
    }
}
