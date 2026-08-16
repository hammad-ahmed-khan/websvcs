package oracle.retail.sim.client.application;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************************
 * Application Info Command Panel
 * <p>
 * Executes a native command and displays the results in a non-editable text area.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NativeCommandPanel extends RPanel {
    private static final long serialVersionUID = -8208848714204971179L;

    private RTextAreaEditor commandEditor = new RTextAreaEditor();

    public NativeCommandPanel() {
        commandEditor.getTextArea().setFont(UIManager.getFont(UIThemeName.THEME_BOLD_FONT));
        commandEditor.getTextArea().setInactiveForeground(CustomSwanLookAndFeel.getControlTextColor());

        setLayout(new BorderLayout());
        setEmptyBorder(5);
        add(commandEditor, BorderLayout.CENTER);
    }

    public void loadCommand(String command) {
        BufferedReader reader = null;
        try {
            InputStream inputStream = Runtime.getRuntime().exec(command).getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.length() != 0) {
                    commandEditor.append(line + "\n");
                }
            }
        } catch (Exception exception) {
            commandEditor.setText(Translator.getMessage(UIMessageText.NATIVE_COMMANDS_ERROR.getText()));
            UILog.error(getClass(), exception);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Throwable ex) {
                UILog.error(getClass(), ex);
            }
        }
    }
}
