package oracle.retail.sim.client.screen.security;

import java.awt.BorderLayout;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.security.PasswordConfiguration;
import oracle.retail.sim.common.security.SecurityMessageText;

/********************************************************************************************************
 * Password Configuration Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PasswordConfigPanel extends ScreenPanel {
    private static final long serialVersionUID = 7421246640223163224L;

    private static final int MINIMUM_LENGTH_MIN = 1;
    private static final int MINIMUM_LENGTH_MAX = 100;
    private static final int MAXIMUM_LENGTH_MIN = 2;
    private static final int MAXIMUM_LENGTH_MAX = 256;
    private static final int DAYS_UNTIL_EXPIRES_MIN = 0;
    private static final int DAYS_UNTIL_EXPIRES_MAX = 999;
    private static final int DAYS_BEFORE_EXPIRES_TO_NOTIFY_MIN = 0;
    private static final int DAYS_BEFORE_EXPIRES_TO_NOTIFY_MAX = 999;
    private static final int NUMBER_OF_PREVIOUS_TO_DISALLOW_MIN = 0;
    private static final int NUMBER_OF_PREVIOUS_TO_DISALLOW_MAX = 999;

    private PasswordConfigModel model = new PasswordConfigModel();

    private RCheckBoxEditor numericRequiredEditor = new RCheckBoxEditor("Numeric");
    private RCheckBoxEditor specialRequiredEditor = new RCheckBoxEditor("Special Character");
    private RCheckBoxEditor alphaRequiredEditor = new RCheckBoxEditor("Alpha-Numeric Character");
    private RCheckBoxEditor capitalRequiredEditor = new RCheckBoxEditor("Capital Letter");
    private RIntegerFieldEditor minimumLengthEditor = new RIntegerFieldEditor("Minimum Password Length");
    private RIntegerFieldEditor maximumLengthEditor = new RIntegerFieldEditor("Maximum Password Length");
    private RIntegerFieldEditor daysToExpireEditor = new RIntegerFieldEditor("Days Until Password Expires");
    private RIntegerFieldEditor daysBeforeExpireNotifyEditor = new RIntegerFieldEditor("Days Before Password Expires To Notify");
    private RIntegerFieldEditor previousDisallowEditor = new RIntegerFieldEditor("Number Of Previous Passwords To Disallow");
    private RTextFieldEditor specialCharacterEditor = new RTextFieldEditor("Special Characters");
    private RCheckBoxEditor initialPasswordEditor = new RCheckBoxEditor("Change Initial Password");
    private RCheckBoxEditor emailPasswordAssignmentEditor = new RCheckBoxEditor("Email User New Password Assignments");

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public PasswordConfigPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        minimumLengthEditor.setIdentifier(SimName.PASSWORD_MIN_LENGTH);
        maximumLengthEditor.setIdentifier(SimName.PASSWORD_MAX_LENGTH);
        daysToExpireEditor.setIdentifier(SimName.PASSWORD_DAYS_TO_EXPIRE);
        daysBeforeExpireNotifyEditor.setIdentifier(SimName.PASSWORD_DAYS_BEFORE_EXPIRE_NOTIFY);
        previousDisallowEditor.setIdentifier(SimName.PASSWORD_PREV_DISALLOWED);
        specialCharacterEditor.setIdentifier(SimName.PASSWORD_SPECIAL_CHARACTER);

        minimumLengthEditor.setSizeType(EditorConstants.SMALL);
        maximumLengthEditor.setSizeType(EditorConstants.SMALL);
        daysToExpireEditor.setSizeType(EditorConstants.SMALL);
        daysBeforeExpireNotifyEditor.setSizeType(EditorConstants.SMALL);
        previousDisallowEditor.setSizeType(EditorConstants.SMALL);
        specialCharacterEditor.setSizeType(EditorConstants.MEDIUM);

        minimumLengthEditor.setMinimumValue(MINIMUM_LENGTH_MIN);
        minimumLengthEditor.setMaximumValue(MINIMUM_LENGTH_MAX);
        maximumLengthEditor.setMinimumValue(MAXIMUM_LENGTH_MIN);
        maximumLengthEditor.setMaximumValue(MAXIMUM_LENGTH_MAX);
        daysToExpireEditor.setMinimumValue(DAYS_UNTIL_EXPIRES_MIN);
        daysToExpireEditor.setMaximumValue(DAYS_UNTIL_EXPIRES_MAX);
        daysBeforeExpireNotifyEditor.setMinimumValue(DAYS_BEFORE_EXPIRES_TO_NOTIFY_MIN);
        daysBeforeExpireNotifyEditor.setMaximumValue(DAYS_BEFORE_EXPIRES_TO_NOTIFY_MAX);
        previousDisallowEditor.setMinimumValue(NUMBER_OF_PREVIOUS_TO_DISALLOW_MIN);
        previousDisallowEditor.setMaximumValue(NUMBER_OF_PREVIOUS_TO_DISALLOW_MAX);
    }

    private void layoutPanel() {
        REditorPanel upperPanel = new REditorPanel(4);
        upperPanel.setTitleBorder("Passwords Must Contain");
        upperPanel.add(numericRequiredEditor);
        upperPanel.add(specialRequiredEditor);
        upperPanel.add(alphaRequiredEditor);
        upperPanel.add(capitalRequiredEditor);

        REditorPanel lowerPanel = new REditorPanel(8);
        lowerPanel.setEmptyBorder(10);
        lowerPanel.add(minimumLengthEditor);
        lowerPanel.add(maximumLengthEditor);
        lowerPanel.add(daysToExpireEditor);
        lowerPanel.add(daysBeforeExpireNotifyEditor);
        lowerPanel.add(previousDisallowEditor);
        lowerPanel.add(specialCharacterEditor);
        lowerPanel.add(initialPasswordEditor);
        lowerPanel.add(emailPasswordAssignmentEditor);

        RPanel mainPanel = new RPanel(new BorderLayout());
        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(lowerPanel, BorderLayout.CENTER);

        LayoutUtility.alignPanels(upperPanel, lowerPanel);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() {
        try {
            PasswordConfiguration config = model.getPasswordConfiguration();
            minimumLengthEditor.setInteger(config.getMinimumPasswordLength());
            maximumLengthEditor.setInteger(config.getMaximumPasswordLength());
            daysToExpireEditor.setInteger(config.getDaysUntilPasswordExpires());
            daysBeforeExpireNotifyEditor.setInteger(config.getDaysBeforePasswordExpiresToNotify());
            previousDisallowEditor.setInteger(config.getPreviousPasswordsDisallowCount());
            numericRequiredEditor.setSelected(config.isPasswordMustContainNumeric());
            specialRequiredEditor.setSelected(config.isPasswordMustContainSpecial());
            alphaRequiredEditor.setSelected(config.isPasswordMustContainLetter());
            capitalRequiredEditor.setSelected(config.isPasswordMustContainCapital());
            initialPasswordEditor.setSelected(config.isChangeInitialPassword());
            emailPasswordAssignmentEditor.setSelected(config.isEmailPasswordAssignment());
            specialCharacterEditor.setText(config.getSpecialCharacters());
        } catch (Exception e) {
            displayException(e);
        }
    }

    /****************************************************************************************************
     * Handle Done Action
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        if (specialRequiredEditor.isSelected() && StringHelper.isNullOrEmpty(specialCharacterEditor.getText())) {
            throw new BusinessException(SecurityMessageText.SPECIAL_CHARS_MISSING);
        }
        int minimumLength = minimumLengthEditor.isEmpty() ? -1 : minimumLengthEditor.getIntegerValue();
        if (minimumLength < MINIMUM_LENGTH_MIN || minimumLength > MINIMUM_LENGTH_MAX) {
            Object[] values = new Object[] { MINIMUM_LENGTH_MIN, MINIMUM_LENGTH_MAX };
            throw new BusinessException(SecurityMessageText.INVALID_PASSWORD_MINIMUM_LENGTH, values);
        }
        int maximumLength = maximumLengthEditor.isEmpty() ? -1 : maximumLengthEditor.getIntegerValue();
        if (maximumLength < MAXIMUM_LENGTH_MIN || maximumLength > MAXIMUM_LENGTH_MAX) {
            throw new BusinessException(SecurityMessageText.INVALID_PASSWORD_MAXIMUM_LENGTH, new String[] { Integer.toString(MAXIMUM_LENGTH_MIN), Integer.toString(MAXIMUM_LENGTH_MAX) });
        }
        if (minimumLength > maximumLength) {
            throw new BusinessException(SecurityMessageText.PASSWORD_LENGTH_MISMATCH);
        }
        int daysUntilPasswordExpires = daysToExpireEditor.isEmpty() ? -1 : daysToExpireEditor.getIntegerValue();
        if (daysUntilPasswordExpires < DAYS_UNTIL_EXPIRES_MIN || daysUntilPasswordExpires > DAYS_UNTIL_EXPIRES_MAX) {
            throw new BusinessException(SecurityMessageText.INVALID_PASSWORD_DAYS_UNTIL_EXPIRES, new String[] { Integer.toString(DAYS_UNTIL_EXPIRES_MIN), Integer.toString(DAYS_UNTIL_EXPIRES_MAX) });
        }
        int daysBeforePasswordExpiresToNotify = daysBeforeExpireNotifyEditor.isEmpty() ? -1 : daysBeforeExpireNotifyEditor.getIntegerValue();
        if (daysBeforePasswordExpiresToNotify < DAYS_BEFORE_EXPIRES_TO_NOTIFY_MIN || daysBeforePasswordExpiresToNotify > DAYS_BEFORE_EXPIRES_TO_NOTIFY_MAX) {
            throw new BusinessException(SecurityMessageText.INVALID_PASSWORD_DAYS_BEFORE_EXPIRES_TO_NOTIFY, new String[] { Integer.toString(DAYS_BEFORE_EXPIRES_TO_NOTIFY_MIN),
                    Integer.toString(DAYS_BEFORE_EXPIRES_TO_NOTIFY_MAX) });
        }
        int previousPasswordsDisallowCount = previousDisallowEditor.isEmpty() ? -1 : previousDisallowEditor.getIntegerValue();
        if (previousPasswordsDisallowCount < NUMBER_OF_PREVIOUS_TO_DISALLOW_MIN || previousPasswordsDisallowCount > NUMBER_OF_PREVIOUS_TO_DISALLOW_MAX) {
            throw new BusinessException(SecurityMessageText.INVALID_PASSWORD_NUMBER_OF_PREVIOUS_TO_DISALLOW, new String[] { Integer.toString(NUMBER_OF_PREVIOUS_TO_DISALLOW_MIN),
                    Integer.toString(NUMBER_OF_PREVIOUS_TO_DISALLOW_MAX) });
        }

        PasswordConfiguration config = model.getPasswordConfiguration();
        config.setChangeInitialPassword(initialPasswordEditor.isSelected());
        config.setEmailPasswordAssignment(emailPasswordAssignmentEditor.isSelected());
        config.setPasswordMustContainCapital(capitalRequiredEditor.isSelected());
        config.setPasswordMustContainLetter(alphaRequiredEditor.isSelected());
        config.setPasswordMustContainNumeric(numericRequiredEditor.isSelected());
        config.setPasswordMustContainSpecial(specialRequiredEditor.isSelected());
        config.setMinimumPasswordLength(minimumLength);
        config.setMaximumPasswordLength(maximumLength);
        config.setDaysUntilPasswordExpires(daysUntilPasswordExpires);
        config.setDaysBeforePasswordExpiresToNotify(daysBeforePasswordExpiresToNotify);
        config.setPreviousPasswordsDisallowCount(previousPasswordsDisallowCount);
        config.setSpecialCharacters(specialCharacterEditor.getText());

        model.savePasswordConfiguration();
    }
}
