package oracle.retail.sim.client.screen.theme;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.FontUIResource;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * Font Selection Dialog allows the user to preview and select a font.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FontDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 1886301370417804932L;

    private RComboBoxEditor fontNameEditor = new RComboBoxEditor("Font");
    private RComboBoxEditor fontTypeEditor = new RComboBoxEditor("Font Style");
    private RIntegerFieldEditor fontSizeEditor = new RIntegerFieldEditor("Font Size");
    private RTextFieldEditor testTextEditor = new RTextFieldEditor("Test String");
    private RLabel testLabel = new RLabel();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private static final String FONT_ALTERED = "Font.altered";
    private static final String TEXT_ALTERED = "Text.altered";

    private List<FontWrapper> fontWrappers;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public FontDialog(JFrame frame) {
        super(frame);
        setStatusBarVisible(false);
        setTitle("Customize Font");
        setSize(400, 400);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        fontNameEditor.registerAction(this, FONT_ALTERED);
        fontTypeEditor.registerAction(this, FONT_ALTERED);
        fontSizeEditor.registerAction(this, FONT_ALTERED);
        testTextEditor.registerAction(this, TEXT_ALTERED);
        fontNameEditor.setSelectionRequired(true);
        fontTypeEditor.setSelectionRequired(true);
        fontTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        fontSizeEditor.setLength(2);
        fontSizeEditor.setMinimumValue(1);
        fontSizeEditor.setMaximumValue(99);
        testTextEditor.setLength(50);

        testLabel.setHorizontalAlignment(JLabel.CENTER);
        testLabel.setHorizontalTextPosition(JLabel.CENTER);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        REditorPanel fontChoicePanel = new REditorPanel(3);
        fontChoicePanel.setTitleBorder("Font Options");
        fontChoicePanel.add(fontNameEditor);
        fontChoicePanel.add(fontTypeEditor);
        fontChoicePanel.add(fontSizeEditor);

        REditorPanel testTextPanel = new REditorPanel(1);
        testTextPanel.setTitleBorder("Test String");
        testTextPanel.add(testTextEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(fontChoicePanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(testTextPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(testLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public void setFonts(List<FontWrapper> wrappers) throws BusinessException {
        if (wrappers.isEmpty()) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED);
        }
        setActionsEnabled(false);

        fontNameEditor.setItems(getFontNames());
        fontTypeEditor.setItems(getFontTypes());

        fontWrappers = wrappers;

        FontWrapper wrapper = fontWrappers.get(0);

        fontNameEditor.setSelectedItem(wrapper.getFamily());
        fontTypeEditor.setSelectedItem(wrapper.getStyle());
        fontSizeEditor.setInteger(wrapper.getSize());

        String previewText = Translator.getMessage(CommonMessageText.THEME_FONT_PREVIEW_TEXT.getText());
        testTextEditor.setText(previewText);
        testLabel.setText(previewText);

        try {
            doFontAltered();
        } catch (Exception e) {
            // Ignore these...
            LogService.debug(this, getClass().getSimpleName() + "ignoring Excepton");
        }
        setActionsEnabled(true);
    }

    private String[] getFontNames() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    private List<String> getFontTypes() {
        List<String> types = new ArrayList<>();
        types.add(FontWrapper.PLAIN);
        types.add(FontWrapper.ITALIC);
        types.add(FontWrapper.BOLD);
        types.add(FontWrapper.ITBLD);
        return types;
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(FONT_ALTERED)) {
                doFontAltered();
            } else if (command.equals(TEXT_ALTERED)) {
                doStringAltered();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doSaveFont();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancelWindow();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSaveFont() throws UIException {
        FontUIResource font = createFont();
        for (FontWrapper wrapper : fontWrappers) {
            wrapper.setFont(font);
            wrapper.setCustom(true);
        }
        closeWindow();
    }

    private void doFontAltered() throws Exception {
        if (fontNameEditor.isEmptySelection() || fontTypeEditor.isEmptySelection()) {
            return;
        }
        if (StringUtility.isNullOrEmpty(fontSizeEditor.getText())) {
            fontSizeEditor.setInteger(fontWrappers.get(0).getSize());
            throw new BusinessException(CommonMessageText.BLANK_VALUE_INVALID, "Font Size");
        }
        testLabel.setFont(createFont());
    }

    private void doStringAltered() throws UIException {
        testLabel.setText(testTextEditor.getText());
    }

    private FontUIResource createFont() throws UIException {
        String fontName = (String) fontNameEditor.getSelectedItem();
        String fontType = (String) fontTypeEditor.getSelectedItem();
        int fontSize = fontSizeEditor.getIntegerValue();

        return new FontUIResource(fontName, convertFontStyleToInt(fontType), fontSize);
    }

    private int convertFontStyleToInt(String fontType) {
        if (fontType.equals(FontWrapper.ITALIC)) {
            return Font.ITALIC;
        }
        if (fontType.equals(FontWrapper.BOLD)) {
            return Font.BOLD;
        }
        if (fontType.equals(FontWrapper.ITBLD)) {
            return Font.BOLD | Font.ITALIC;
        }
        return Font.PLAIN;
    }

    private void doCancelWindow() {
        closeWindow();
    }
}
