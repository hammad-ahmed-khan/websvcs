package oracle.retail.sim.client.swing.format;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.MaskAdaptor;

/********************************************************************************************************
 * This class handles formatting and unformatting numeric ID data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NumericIdMask extends MaskAdaptor {

    private String idType = StringConstants.EMPTY;

    /****************************************************************************************************
     * Returns new NumericIdMask object.
     ***************************************************************************************************/
    public NumericIdMask() {
    }

    /****************************************************************************************************
     * Assigns an ID type to display in front of the ID.
     ***************************************************************************************************/
    public void setIdType(String type) {
        idType = Translator.getText(type);
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
                return LocaleManager.getIntegerFormatter().format(data);
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
        return text;
    }

    /****************************************************************************************************
     * Validates a text string to determine if it represents a valid integer.
     * @param text The text string to validate.
     * @return UIException If the data is not valid.
     ***************************************************************************************************/
    public BusinessException validate(String text) {
        if (StringUtility.isNullOrEmpty(text) || StringUtility.isIdentifierNumeric(text)) {
            return null;
        }
        Object[] params = { text, idType };
        return new BusinessException(CommonMessageText.VALUE_NOT_VALID_ID, params);
    }

    /****************************************************************************************************
     * Under new requirements, all characters are valid entry for integer mask and errors are caught
     * later.
     ***************************************************************************************************/
    public boolean validCharacter(char character) {
        return true;
    }
}
