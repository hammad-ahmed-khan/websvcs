package oracle.retail.sim.client.core;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.ClientStatusDialog;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.application.StatusBarInterface;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.quicksearch.ClientQuickJumpDialog;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.util.HelpLauncher;
import oracle.retail.sim.client.util.SimClientStateKey;

/********************************************************************************************************
 * SIM STATUS BAR
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimStatusBar extends RPanel implements StatusBarInterface {
    private static final long serialVersionUID = -3425715221610478885L;

    private RLabel onlineLabel = new RLabel();
    private RLabel userLabel = new RLabel();
    private RLabel storeLabel = new RLabel();
    private RLabel screenLabel = new RLabel();
    private RLabel helpLabel = new RLabel();
    private RLabel searchLabel = new RLabel();

    private JFrame mainFrame;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public SimStatusBar() {
        initStatusBar();
        layoutStatusBar();
    }

    private void initStatusBar() {
        initLabels();

        helpLabel.addMouseListener(createHelpMouseListener());
        onlineLabel.addMouseListener(createOnlineMouseListener());
        searchLabel.addMouseListener(createSearchMouseListener());
    }

    private void initLabels() {
        Font font = UIManager.getFont(UIThemeName.GLOBALBAR_FONT);

        initializeLabel(onlineLabel, font);
        initializeLabel(userLabel, font);
        initializeLabel(storeLabel, font);
        initializeLabel(screenLabel, font);
        initializeLabel(helpLabel, font);
        initializeLabel(searchLabel, font);
    }

    private void initializeLabel(RLabel label, Font font) {
        if (label != null) {
            label.setBorder(BorderFactory.createLoweredBevelBorder());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font(font.getFamily(), font.getStyle(), font.getSize()));
            label.setForeground(UIManager.getColor(UIThemeName.GLOBALBAR_COLOR));
        }
    }

    private void layoutStatusBar() {
        setOpaque(true);
        setLayout(new GridBagLayout());
        add(onlineLabel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(userLabel, GridTool.constraints(1, 0, 1, 1, 3, 1, 0, 3, 0, 0, 0, 0));
        add(storeLabel, GridTool.constraints(2, 0, 1, 1, 3, 1, 0, 3, 0, 0, 0, 0));
        add(screenLabel, GridTool.constraints(3, 0, 1, 1, 3, 1, 0, 3, 0, 0, 0, 0));
        add(helpLabel, GridTool.constraints(4, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(searchLabel, GridTool.constraints(5, 0, 1, 1, 0, 1, 0, 3, 0, 0, 0, 0));
    }

    public void updateUI() {
        super.updateUI();
        initLabels();
    }

    /****************************************************************************************************
     * Set the main background frame that owns the status bar
     ***************************************************************************************************/
    public void setParentFrame(JFrame frame) {
        if (frame != null) {
            mainFrame = frame;
        }
    }

    /****************************************************************************************************
     * Set the current screen name.
     ***************************************************************************************************/
    public void setScreenName(String screenName) {
        screenLabel.setText(Translator.getText(screenName));
    }

    /****************************************************************************************************
     * Set the current store number.
     ***************************************************************************************************/
    public void setStoreInfo(String storeDescription) {
        storeLabel.setText(storeDescription);
    }

    /****************************************************************************************************
     * Set the current user identifier. Change in uses requires refreshing other values for potential
     * change in locale.
     ***************************************************************************************************/
    public void setUser(String userId) {
        userLabel.setText(userId);
        helpLabel.setText(Translator.getText("Help"));
        onlineLabel.setText(Translator.getText("Client Information"));
        searchLabel.setText("  " + Translator.getText("Jump") + "  ");
    }

    /****************************************************************************************************
     * Process the hot key event.
     ***************************************************************************************************/
    public void doHotKeyPressed(int keycode) {
        doDisplayAppSearchDialog();
    }

    /****************************************************************************************************
     * Work in progress event
     ***************************************************************************************************/
    public void workInProgressEvent(final boolean inProgress) {
        if (mainFrame != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (inProgress) {
                        mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    } else {
                        mainFrame.setCursor(Cursor.getDefaultCursor());
                    }
                }
            });
        }
    }

    /****************************************************************************************************
     * Mouse listener that triggers the application info dialog when the on-line label is double-clicked.
     ***************************************************************************************************/
    private MouseListener createHelpMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                HelpLauncher.showHelp();
            }
        };
    }

    /****************************************************************************************************
     * Mouse listener that triggers the application info dialog when the on-line label is double-clicked.
     ***************************************************************************************************/
    private MouseListener createOnlineMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    doDisplayAppInfoDialog();
                }
            }
        };
    }

    /****************************************************************************************************
     * Mouse listener that triggers the application info dialog when the on-line label is double-clicked.
     ***************************************************************************************************/
    private MouseListener createSearchMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    doDisplayAppSearchDialog();
                }
            }
        };
    }

    /****************************************************************************************************
     * Displays the application information dialog.
     ***************************************************************************************************/
    private void doDisplayAppInfoDialog() {
        ClientStatusDialog dialog = new ClientStatusDialog(mainFrame);
        dialog.load();
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Displays the application quick jump dialog.
     ***************************************************************************************************/
    private void doDisplayAppSearchDialog() {
        if (RepositoryManager.getStateObject(SimClientStateKey.QUICK_JUMP_ITEM) == null) {
            ClientQuickJumpDialog dialog = new ClientQuickJumpDialog(mainFrame);
            dialog.loadAvailableSearches();
            dialog.setVisible(true);
        }
    }
}
