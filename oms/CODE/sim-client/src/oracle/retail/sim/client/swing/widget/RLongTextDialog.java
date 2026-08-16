package oracle.retail.sim.client.swing.widget;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.panel.RPanel;

/********************************************************************************************************
 * This class sublcasses an RPanel and supplies a long field with a popup window for expanded text.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RLongTextDialog extends RDialog {
    private static final long serialVersionUID = 4612011649070588848L;

    private RButton okayButton = new RButton("OK");
    private RButton cancelButton = new RButton("Cancel");

    private RTextArea textArea = new RTextArea();
    private RScrollPane textPane = new RScrollPane(textArea);

    private RLongTextListener textListener = null;

    public RLongTextDialog(JFrame frame, String title) {
        super(frame);
        initialize(title);
    }

    public RLongTextDialog(JDialog dialog, String title) {
        super(dialog);
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setSize(300, 300);
        setStatusBarVisible(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        textPane.setLoweredBorder();

        okayButton.addActionListener(createOkayActionListener());
        cancelButton.addActionListener(createCancelActionListener());

        addButton(okayButton);

        RPanel panel = (RPanel) getContentPane();
        panel.setLayout(new BorderLayout());
        panel.add(textPane, BorderLayout.CENTER);

        centerWindow();
    }

    public void addTextListener(RLongTextListener textListener) {
        this.textListener = textListener;
    }

    public void setIdentifier(String identifier) {
        textArea.setIdentifier(identifier);
    }

    public void setText(String text, boolean isEditable) {
        textArea.setText(text);
        textArea.setEditable(isEditable);
        if (isEditable) {
            addButton(cancelButton);
        }
    }

    public ActionListener createOkayActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (textListener != null) {
                    textListener.updateText(textArea.getText());
                }
                closeWindow();
            }
        };
    }

    public ActionListener createCancelActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                closeWindow();
            }
        };
    }
}
