package oracle.retail.sim.client.swing.widget;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Currency;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.format.MoneyMaskFactory;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.core.locale.NumberParser;
import oracle.retail.sim.common.format.MoneyMask;

/********************************************************************************************************
 * This class sub-classes the RTextField to create a currency entry field. The default settings are right
 * justified with standard currency mask. This is not a hard-coded field, so it is possible to set an
 * alternate mask and any other property of the text field (making it no longer a currency field). This
 * is intentional as the users of this widget may want different kinds of currency masks.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RMoneyField extends RTextField {
    private static final long serialVersionUID = 9142004642149545556L;

    private MoneyMask mask = MoneyMaskFactory.createMoneyMask(LocaleManager.getNumericLocale());

    /****************************************************************************************************
     * Returns new RCurrencyField.
     ***************************************************************************************************/
    public RMoneyField() {
        setHorizontalAlignment(RIGHT);
        setMask(mask);
    }

    /****************************************************************************************************
     * Assigns currency to the mask.
     * <p>
     * @param currency The currency to assign.
     ***************************************************************************************************/
    public void setCurrency(Currency currency) {
        mask = MoneyMaskFactory.createMoneyMask(LocaleManager.getNumericLocale(), currency);
        setMask(mask);
    }

    /****************************************************************************************************
     * Returns the currency assigned to the mask.
     * <p>
     * @return The currency
     ***************************************************************************************************/
    public Currency getCurrency() {
        return mask.getCurrency();
    }

    /****************************************************************************************************
     * Assigns the amount to the money field. Along with currency, this will correctly format the text in
     * the field.
     * <p>
     * @param amount The amount to assign.
     ***************************************************************************************************/
    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            setText(LocaleManager.getNumberFormatter().format(amount));
        } else {
            clear();
        }
    }

    /****************************************************************************************************
     * Retrieves the amount of the money field.
     * <p>
     * @return The amount.
     * @throws OldUIException Thrown if the number in the field is not in a valid format.
     ***************************************************************************************************/
    public BigDecimal getAmount() throws UIException {
        String amount = getText();
        if (StringUtility.isNullOrEmpty(amount)) {
            return null;
        }
        try {
            return NumberParser.getInstance(LocaleManager.getNumericLocale()).getBigDecimal(amount);
        } catch (Throwable exception) {
            throw new UIException(UIMessageText.MONEY_FORMAT_ERROR);
        }
    }

    /****************************************************************************************************
     * Overrides the text field key listener methods to provide alternate functionality. The quick entry
     * functionality has been removed.
     * <p>
     * @param event Details about the key event that occurred.
     ***************************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    /****************************************************************************************************
     * Implements the key listener interface "key pressed" method. The method tracks whether or not the
     * key pressed was a valid processable keystroke.
     * <p>
     * @param event Details about the key event that occurred.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_LEFT:
                processKey = false;
                break;
            default:
                processKey = true;
        }
    }

    /****************************************************************************************************
     * Implements the key listener interface "key typed" method. It captures the key typed action, checks
     * to see if the allowable length is reached and if the key is not a backspace/delete/left arrow key,
     * then the key is ignored.
     * <p>
     * If the text in the field is selected, a new keystroke would replace the selected text and by
     * default that means we cannot allow the event to continue.
     * <p>
     * @param event Details about the key event that occurred.
     ***************************************************************************************************/
    public void keyTyped(KeyEvent event) {
        if (getSelectedText() != null) {
            return;
        }
        if (processKey) {
            String text = getUnformattedText();
            if (text.length() >= allowedLength) {
                event.consume();
            }
            if (!mask.validCharacter(event.getKeyChar())) {
                event.consume();
                return;
            }
            StringBuilder separator = new StringBuilder(1);
            separator.append(LocaleManager.getCurrencyDecimalSymbols().getDecimalSeparator());
            int decimalLength = mask.getCurrency().getDefaultFractionDigits();
            int index = StringUtility.lastIndexOf(text, separator.toString());
            if (index > -1 && StringUtility.substring(text, index).length() > decimalLength) {
                event.consume();
            }
        }
    }
}
