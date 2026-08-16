package oracle.retail.sim.client.screen.translation;

import java.awt.GridBagLayout;
import java.util.Locale;
import javax.swing.JFrame;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.LocaleLanguageDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.translation.TranslationServices;

/********************************************************************************************************
 * The dialog that handles editing the translation text for a particular language key/country/variant.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslationDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 3685377233319999841L;

    private RTextFieldEditor languageEditor = new RTextFieldEditor("Language");
    private RTextAreaEditor keyEditor = new RTextAreaEditor("Key");
    private RTextAreaEditor valueEditor = new RTextAreaEditor("Translation");
    private RTextAreaEditor commentEditor = new RTextAreaEditor("Comments");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private Locale locale;
    private TranslationDetailWrapper detailWrapper;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public TranslationDialog(JFrame frame) {
        super(frame);
        setTitle("Translation Detail");
        setSize(500, 500);
        initDialog();
        layoutDialog();
        centerWindow();
    }

    private void initDialog() {
        keyEditor.setIdentifier(SimName.TRANSLATION_KEY);
        valueEditor.setIdentifier(SimName.TRANSLATION_DETAIL);
        commentEditor.setIdentifier(SimName.TRANSLATION_COMMENT);

        keyEditor.setTitleAlignment(EditorConstants.TOP);
        valueEditor.setTitleAlignment(EditorConstants.TOP);
        commentEditor.setTitleAlignment(EditorConstants.TOP);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutDialog() {
        addButton(applyButton);
        addButton(cancelButton);

        RPanel editorPanel = new RPanel(new GridBagLayout());
        editorPanel.add(languageEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 10, 3, 0));
        editorPanel.add(keyEditor, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 10, 3, 0));
        editorPanel.add(valueEditor, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 10, 3, 0));
        editorPanel.add(commentEditor, GridTool.constraints(0, 4, 1, 1, 1, 1, 0, 3, 0, 10, 3, 0));

        LayoutUtility.alignEditorsInGridBag(editorPanel);

        setContentPane(editorPanel);
    }

    /****************************************************************************************************
     * Initialize content
     ***************************************************************************************************/

    public void setTranslationLocale(Locale locale) {
        this.locale = locale;
        LocaleLanguageDisplayer displayer = new LocaleLanguageDisplayer();
        languageEditor.setText(displayer.getDisplayText(locale));
    }

    public void setTranslationDetail(TranslationDetailWrapper wrapper) {
        detailWrapper = wrapper;

        keyEditor.setText(wrapper.getEnglish());
        valueEditor.setText(wrapper.getValue());
        commentEditor.setText(wrapper.getComment());

        keyEditor.setEnabled(false);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Done Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        if (detailWrapper == null) {
            createTranslation();
        } else {
            updateTranslation();
        }
    }

    private void createTranslation() throws Exception {
        if (keyEditor.isEmpty()) {
            displayMessage(CommonMessageText.TRANSLATION_NO_KEY);
            return;
        }
        String key = keyEditor.getText();
        String value = valueEditor.getText();
        TranslationServices translationServices = ClientServiceFactory.getTranslationServices();
        translationServices.addTranslationKey(key, commentEditor.getText());
        translationServices.updateTranslation(locale, key, value);
        detailWrapper = ClientWrapperFactory.createTranslationDetailWrapper(key, key, value, commentEditor.getText());
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.TRANSLATION_LANGUAGE_KEY_ADDED, detailWrapper));
        closeWindow();
    }

    private void updateTranslation() throws Exception {
        TranslationServices translationServices = ClientServiceFactory.getTranslationServices();
        translationServices.updateTranslationKey(detailWrapper.getKey(), commentEditor.getText());
        translationServices.updateTranslation(locale, detailWrapper.getKey(), valueEditor.getText());
        detailWrapper.setValue(valueEditor.getText());
        detailWrapper.setComment(commentEditor.getText());
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.TRANSLATION_LANGUAGE_VALUE_UPDATED, detailWrapper));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
