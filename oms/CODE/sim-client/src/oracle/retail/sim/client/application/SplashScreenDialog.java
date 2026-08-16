package oracle.retail.sim.client.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.WindowPlacer;

/********************************************************************************************************
 * SPLASH SCREEN DIALOG
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SplashScreenDialog extends JWindow {
    private static final long serialVersionUID = 2813357988277920664L;

    private JLabel imageLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JLabel statusLabel = new JLabel();
    private JProgressBar progressBar = new JProgressBar();

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public SplashScreenDialog(Window window) {
        super(window);

        initComponents();
        layoutDialog();

        pack();
        setSize(600, 300);

        WindowPlacer.centerWindow(this);
    }

    /****************************************************************************************************
     * Initialize the window components.
     ***************************************************************************************************/
    private void initComponents() {
        Font baseFont = UIManager.getFont(UIThemeName.THEME_LARGE_FONT);

        statusLabel.setBackground(Color.WHITE);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setFont(baseFont.deriveFont(Font.BOLD, 20));

        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(baseFont.deriveFont(Font.BOLD, 25));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setText(Translator.getMessage("Store Inventory Management"));

        imageLabel.setIcon(UIManager.getIcon(UIThemeName.LOGIN_BACKGROUND));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(Color.BLUE);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setBorderPainted(false);
        progressBar.setMinimumSize(new Dimension(0, 40));
        progressBar.setPreferredSize(new Dimension(0, 40));
    }

    /****************************************************************************************************
     * Layout the components on the dialog.
     ***************************************************************************************************/
    private void layoutDialog() {
        Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.GRAY, Color.DARK_GRAY);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createLoweredBevelBorder()));
        mainPanel.add(titleLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 10, 0, 0, 0));
        mainPanel.add(imageLabel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        mainPanel.add(statusLabel, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(progressBar, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Set the splash screen in online/offline mode. Text is black in online mode, red in offline mode.
     * <p>
     * @param online True if the system is online, false if not.
     ***************************************************************************************************/
    public void setOnline(boolean online) {
        if (online) {
            statusLabel.setForeground(CustomSwanLookAndFeel.getBlack());
            progressBar.setForeground(CustomSwanLookAndFeel.getDefaultBlueStatusColor());
        } else {
            statusLabel.setForeground(CustomSwanLookAndFeel.getSwanRedStatusColor());
            progressBar.setForeground(CustomSwanLookAndFeel.getSwanRedStatusColor());
        }
    }

    /****************************************************************************************************
     * Sets a new status message to display in the status area about the progress bar. The values are
     * displays as "message...label".
     * <p>
     * @param message The message to display (translated).
     ***************************************************************************************************/
    public void setStatus(String message) {
        statusLabel.setText(Translator.getMessage(message));
        statusLabel.repaint();
    }

    /****************************************************************************************************
     * Sets the progress value in the progress bar.
     ***************************************************************************************************/
    public void setProgressTarget(int value) {
        progressBar.setMaximum(value);
    }

    /****************************************************************************************************
     * Sets the progress value in the progress bar.
     ***************************************************************************************************/
    public void setProgress(int value) {
        progressBar.setValue(value);
        progressBar.repaint();
    }
}
