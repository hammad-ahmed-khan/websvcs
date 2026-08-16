package oracle.retail.sim.client.screen.translation;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.LocaleLanguageDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Translation Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslationDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -1352036580283314928L;

    private RComboBoxEditor languageEditor = new RComboBoxEditor("Language");
    private RTextFieldEditor baseEnglishEditor = new RTextFieldEditor("Translation");

    private TranslationDetailModel model = new TranslationDetailModel();

    private SimTable translationTable = new SimTable(new TranslationDetailDefinition());
    private SimTablePane translationPane = new SimTablePane(translationTable);

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public TranslationDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        languageEditor.setDisplayer(new LocaleLanguageDisplayer());
        languageEditor.registerAction(this, SimClientStateKey.TRANSLATION_LANGUAGE_SELECTED);

        baseEnglishEditor.setIdentifier(SimName.TRANSLATION_FILTER);

        translationTable.setSingleRowSelectionMode();
        translationTable.registerDoubleClickAction(this, SimClientStateKey.TRANSLATION_SELECTED);
        translationTable.setTableEditable(false);
    }

    private void layoutScreen() {
        REditorPanel headerPanel = new REditorPanel(1, 2);
        headerPanel.add(languageEditor);
        headerPanel.add(baseEnglishEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(translationPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return translationTable;
    }

    /****************************************************************************************************
     * Start Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        model.refreshLocales();
        languageEditor.setItems(model.findLanguages());
        baseEnglishEditor.clear();
    }

    /****************************************************************************************************
     * Create New Translation
     ***************************************************************************************************/

    public void createTranslation() {
        Locale locale = (Locale) languageEditor.getSelectedItem();
        if (locale == null) {
            displayError(CommonMessageText.TRANSLATION_NO_LANGUAGE);
            return;
        }
        TranslationDialog dialog = new TranslationDialog(Application.getFrame());
        dialog.setTranslationLocale(locale);
        dialog.addREventListener(this);
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Handle The Search
     ***************************************************************************************************/
    public void handleSearch() throws Exception {
        Locale locale = (Locale) languageEditor.getSelectedItem();
        if (locale == null) {
            displayError(CommonMessageText.TRANSLATION_NO_LANGUAGE);
            return;
        }
        translationTable.setRows(model.findLanguageDetails(locale, baseEnglishEditor.getText()));
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.TRANSLATION_LANGUAGE_SELECTED)) {
                doLanguageSelected();
            } else if (command.equals(SimClientStateKey.TRANSLATION_LANGUAGE_KEY_ADDED)) {
                doTranslationKeyAdded(event.getEventData());
            } else if (command.equals(SimClientStateKey.TRANSLATION_LANGUAGE_VALUE_UPDATED)) {
                doTranslationValueUpdated(event.getEventData());
            } else if (command.equals(SimClientStateKey.TRANSLATION_SELECTED)) {
                doEditTranslation();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Language Selected
     ***************************************************************************************************/

    private void doLanguageSelected() {
        translationTable.clearRows();
    }

    /****************************************************************************************************
     * Edit Existing Translation
     ***************************************************************************************************/

    private void doEditTranslation() {
        TranslationDetailWrapper wrapper = (TranslationDetailWrapper) translationTable.getSelectedRowData();
        if (wrapper == null) {
            displayError(CommonMessageText.TRANSLATION_NOT_SELECTED);
            return;
        }
        Locale locale = (Locale) languageEditor.getSelectedItem();
        if (locale == null) {
            displayError(CommonMessageText.TRANSLATION_NO_LANGUAGE);
            return;
        }
        TranslationDialog dialog = new TranslationDialog(Application.getFrame());
        dialog.setTranslationLocale(locale);
        dialog.setTranslationDetail(wrapper);
        dialog.addREventListener(this);
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Translation Key Added
     ***************************************************************************************************/

    private void doTranslationKeyAdded(Object value) throws Exception {
        if (model.addTranslationKey((TranslationDetailWrapper) value)) {
            translationTable.addRow(value);
        }
    }

    private void doTranslationValueUpdated(Object value) {
        if (model.updateTranslationKey((TranslationDetailWrapper) value)) {
            translationTable.updateRow(value);
        }
    }

    /****************************************************************************************************
     * Translation Table Definition
     ***************************************************************************************************/

    private class TranslationDetailDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return TranslationDetailWrapper.class;
        }

        public List getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("english"));
        }

        public List getAttributes() {
            List attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Key", "english"));
            attributes.add(new SimTableAttribute("Translation", "value"));
            attributes.add(new SimTableAttribute("Comments", "comment"));
            return attributes;
        }
    }
}
