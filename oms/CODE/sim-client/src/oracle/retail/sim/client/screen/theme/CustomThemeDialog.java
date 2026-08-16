package oracle.retail.sim.client.screen.theme;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import oracle.retail.sim.client.application.ThemeUtility;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.plaf.custom.CustomLookAndFeel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.theme.CustomTheme;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Dialog for creating or editing a custom theme.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomThemeDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 458714050028634540L;

    private RTextFieldEditor nameEditor = new RTextFieldEditor("Name", true);
    private RTextFieldEditor descEditor = new RTextFieldEditor("Description", true);
    private RComboBoxEditor lookFeelEditor = new RComboBoxEditor("Look & Feel", true);
    private RCheckBoxEditor activeEditor = new RCheckBoxEditor("Active");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private List<CustomThemeWrapper> themeWrappers;
    private CustomThemeWrapper themeWrapper;
    private boolean isEditMode;

    /****************************************************************************************************
     * Constructor
     * @param frame The parent frame.
     ***************************************************************************************************/
    public CustomThemeDialog(JFrame frame) {
        super(frame);
        setTitle("Custom Theme Selection");
        setSize(420, 220);
        setStatusBarVisible(false);
        initDialog();
        layoutDialog();
        centerWindow();
    }

    /****************************************************************************************************
     * Initializes dialog properties and editors.
     ***************************************************************************************************/
    private void initDialog() {
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        nameEditor.setIdentifier(SimName.THEME_NAME);
        descEditor.setIdentifier(SimName.THEME_DESCRIPTION);

        lookFeelEditor.setDisplayer(new ThemeDisplayer());
        lookFeelEditor.setItems(ThemeUtility.getLookAndFeelList());
        lookFeelEditor.removeEmptySelection();
        lookFeelEditor.setSelectedItem(ThemeUtility.getDefaultLookAndFeel());

        nameEditor.setSizeType(EditorConstants.MEDIUM);
        descEditor.setSizeType(EditorConstants.LARGE);
        lookFeelEditor.setSizeType(EditorConstants.LARGE);
    }

    /****************************************************************************************************
     * Lays out the dialog
     ***************************************************************************************************/
    private void layoutDialog() {
        addButton(applyButton);
        addButton(cancelButton);

        REditorPanel editorPanel = new REditorPanel(4);
        editorPanel.setTitleBorder("Theme Details");
        editorPanel.add(nameEditor);
        editorPanel.add(descEditor);
        editorPanel.add(lookFeelEditor);
        editorPanel.add(activeEditor);

        setContentPane(editorPanel);
    }

    /****************************************************************************************************
     * Sets all themes to use for validation
     ***************************************************************************************************/
    public void setThemes(List<CustomThemeWrapper> wrappers) {
        themeWrappers = wrappers;
    }

    /****************************************************************************************************
     * Assigns the custom theme to edit.
     ***************************************************************************************************/
    public void setCustomTheme(CustomThemeWrapper theme) {
        nameEditor.setText(theme.getName());
        descEditor.setText(theme.getDescription());
        activeEditor.setSelected(theme.isActive());
        lookFeelEditor.setSelectedItem(theme.getLookAndFeel());
        themeWrapper = theme;
        isEditMode = true;
    }

    /****************************************************************************************************
     * Handles UI events delegating to the appropriate method
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle cancel action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }

    /****************************************************************************************************
     * Handle the final selection and saving of theme when button is pressed.
     ***************************************************************************************************/
    private void doApply() throws Exception {
        if (nameEditor.isEmpty() || descEditor.isEmpty()) {
            throw new BusinessException(CommonMessageText.THEME_NO_NAME);
        }
        CustomLookAndFeel lookAndFeel = (CustomLookAndFeel) lookFeelEditor.getSelectedItem();

        CustomTheme theme = BOFactory.createCustomTheme();
        theme.setName(nameEditor.getText());
        theme.setDescription(descEditor.getText());
        theme.setLookAndFeel(lookAndFeel.getClass().getName());
        theme.setActive(activeEditor.isSelected());

        validateNameAndDescription(theme);

        if (isEditMode) {
            CustomTheme currentTheme = themeWrapper.getCustomTheme();
            currentTheme.setName(theme.getName());
            currentTheme.setDescription(theme.getDescription());
            currentTheme.setLookAndFeel(theme.getLookAndFeel());
            currentTheme.setActive(theme.isActive());

            ClientServiceFactory.getCustomThemeServices().update(themeWrapper.getCustomTheme());
        } else {
            ClientServiceFactory.getCustomThemeServices().insert(theme);
        }
        closeWindow();
    }

    /****************************************************************************************************
     * Validate name and description
     ***************************************************************************************************/
    private void validateNameAndDescription(CustomTheme theme) throws BusinessException {
        boolean isNameDuplicate = false;
        boolean isDescriptionDuplicate = false;

        if (isEditMode) {
            for (CustomThemeWrapper wrapper : themeWrappers) {
                if (wrapper.getCustomTheme() != themeWrapper.getCustomTheme()) {
                    if (wrapper.getName().equals(theme.getName())) {
                        isNameDuplicate = true;
                    }
                    if (wrapper.getDescription().equals(theme.getDescription())) {
                        isDescriptionDuplicate = true;
                    }
                }
            }
        } else {
            for (CustomThemeWrapper wrapper : themeWrappers) {
                if (wrapper.getName().equals(theme.getName())) {
                    isNameDuplicate = true;
                }
                if (wrapper.getDescription().equals(theme.getDescription())) {
                    isDescriptionDuplicate = true;
                }
            }
        }
        if (isNameDuplicate && isDescriptionDuplicate) {
            throw new BusinessException(CommonMessageText.THEME_NAME_DESC_EXISTS);
        }
        if (isNameDuplicate) {
            throw new BusinessException(CommonMessageText.THEME_NAME_EXISTS);
        }
        if (isDescriptionDuplicate) {
            throw new BusinessException(CommonMessageText.THEME_DESC_EXISTS);
        }
    }

    /****************************************************************************************************
     *
     * THEME DISPLAYER - Handles the display string for the string selection box
     *
     ***************************************************************************************************/
    private class ThemeDisplayer extends AbstractDisplayer {

        public String getDisplayText(Object object) {
            if (object instanceof LookAndFeel) {
                return ((LookAndFeel) object).getDescription();
            }
            return StringConstants.EMPTY;
        }
    }
}
