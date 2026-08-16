package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class subclasses the standard JFrame to supply additional custom Oracle functionality. This
 * includes a new title bar, which mandates that the root pane no longer control the menu bar and tool
 * bar. This creates all kinds of problems, much of which we try to manage here.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RPlatformFrame extends RFrame {
    private static final long serialVersionUID = 6813603365725708627L;

    private RPlatformTitleBar titlePane = new RPlatformTitleBar();
    private JPanel contentPane = new JPanel();
    private RPlatformFooterPane footerPane = new RPlatformFooterPane();
    private RFrameListener frameListener;

    private Container holdContentPane = new JPanel();
    private Container userContentPane = holdContentPane;

    private JPanel userFooterPane = new JPanel();

    private String titleText = StringConstants.EMPTY;
    private String versionText = StringConstants.EMPTY;

    /****************************************************************************************************
     * Constructs new RPlatformFrame object.
     ***************************************************************************************************/
    public RPlatformFrame() {
        setColorScheme();
        setUndecorated(true);
        initializePanels();
        initializeFrameListener();
        initializeFrame();
    }

    /****************************************************************************************************
     * Sets the color scheme of the frame.
     ***************************************************************************************************/
    private void setColorScheme() {
        setFont(UIManager.getFont(UIThemeName.FRAME_FONT));
        setBackground(UIManager.getColor(UIThemeName.FRAME_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.FRAME_FOREGROUND));

        setTitleFont(UIManager.getFont(UIThemeName.TITLEBAR_FONT));
        setTitleColor(UIManager.getColor(UIThemeName.TITLEBAR_FOREGROUND));
        setTitleIcon((ImageIcon) UIManager.getIcon(UIThemeName.TITLEBAR_MINI_ICON));

        Color borderColor = UIManager.getColor(UIThemeName.FRAME_BORDER_COLOR);

        getRootPane().setBorder(new MatteBorder(1, 1, 1, 1, borderColor));
    }

    /****************************************************************************************************
     * Initialize Panels
     ***************************************************************************************************/
    protected void initializePanels() {
        holdContentPane.setBackground(Color.WHITE);
        holdContentPane.setLayout(new BorderLayout());

        userFooterPane.setOpaque(false);

        contentPane.setDoubleBuffered(true);
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new RContentBorder());

        titlePane.addMinimizeListener(getMinimizeListener());
        titlePane.addMaximizeListener(getMaximizeListener());
        titlePane.addCloseListener(getCloseListener());
    }

    /****************************************************************************************************
     * Initialize Frame Listener
     ***************************************************************************************************/
    protected void initializeFrameListener() {
        frameListener = new RFrameListener(this);
        addMouseListener(frameListener);
        addMouseMotionListener(frameListener);
        // getRootPane().addMouseListener(frameListener);
        // getRootPane().addMouseMotionListener(frameListener);
    }

    /****************************************************************************************************
     * Layout Frame
     ***************************************************************************************************/
    protected void initializeFrame() {
        contentPane.add(userContentPane, BorderLayout.CENTER);
        footerPane.add(userFooterPane, BorderLayout.CENTER);

        add(titlePane, BorderLayout.NORTH);
        add(footerPane, BorderLayout.SOUTH);

        super.setContentPane(contentPane);
    }

    /****************************************************************************************************
     * Our frame does not validate that the root pane is not being messed with. It is possible for
     * subclasses of RPlatformFrame to totally break the application.
     ***************************************************************************************************/
    protected boolean isRootPaneCheckingEnabled() {
        return false;
    }

    /****************************************************************************************************
     * Retrieves the title bar from the frame.
     ***************************************************************************************************/
    public RPlatformTitleBar getTitleBar() {
        return titlePane;
    }

    /****************************************************************************************************
     * Assigns the content pane of the frame.
     * <p>
     * @param container A container.
     ***************************************************************************************************/
    public void setContentPane(Container container) {
        contentPane.remove(userContentPane);
        if (container == null) {
            userContentPane = holdContentPane;
        } else {
            userContentPane = container;
        }
        contentPane.add(userContentPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Retrieves the content pane from the frame.
     * <p>
     * @return The content pane of the frame.
     ***************************************************************************************************/
    public Container getContentPane() {
        return userContentPane;
    }

    /****************************************************************************************************
     * Retrieves the footer pane from the frame.
     * <p>
     * @return The foot pane.
     ***************************************************************************************************/
    protected RPlatformFooterPane getFooterPane() {
        return footerPane;
    }

    /****************************************************************************************************
     * Assigns a panel to display inside the footer.
     * <p>
     * @param panel A panel to display inside the footer.
     ***************************************************************************************************/
    public void setFooterContent(JPanel panel) {
        if (userFooterPane != null) {
            footerPane.remove(userFooterPane);
        }
        userFooterPane = panel;
        if (userFooterPane != null) {
            footerPane.add(userFooterPane, BorderLayout.CENTER);
        }
    }

    /****************************************************************************************************
     * Sets the title of the frame.
     * <p>
     * @param title The title of the frame.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            title = StringConstants.EMPTY;
        }
        titleText = title;
        displayTitle();
    }

    /****************************************************************************************************
     * Sets the version text to display in the title of the frame.
     * <p>
     * @param version The version to display.
     ***************************************************************************************************/
    public void setVersion(String version) {
        if (version == null) {
            version = StringConstants.EMPTY;
        }
        versionText = version;
        displayTitle();
    }

    /****************************************************************************************************
     * Displays the title in the frame titlebar.
     ***************************************************************************************************/
    private void displayTitle() {
        StringBuilder titleBuffer = new StringBuilder();
        if (!StringUtility.isNullOrEmpty(titleText)) {
            titleBuffer.append(Translator.getText(titleText));
        }
        if (!StringUtility.isNullOrEmpty(versionText)) {
            titleBuffer.append(StringConstants.SPACE);
            titleBuffer.append(versionText);
        }
        String title = titleBuffer.toString();
        super.setTitle(title);
        titlePane.setTitle(title);
    }

    /****************************************************************************************************
     * Assigns the title font to the title bar.
     * <p>
     * @param font The font to assign.
     ***************************************************************************************************/
    public void setTitleFont(Font font) {
        titlePane.setTitleFont(font);
    }

    /****************************************************************************************************
     * Assigns the title color to the title bar.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setTitleColor(Color color) {
        titlePane.setTitleColor(color);
    }

    /****************************************************************************************************
     * Assigns the title icon to the title bar and the minimize frame.
     ***************************************************************************************************/
    private void setTitleIcon(ImageIcon imageIcon) {
        if (imageIcon != null) {
            titlePane.setIcon(imageIcon);
            setIconImage(imageIcon.getImage());
        }
    }

    /****************************************************************************************************
     * Retrieves the minimize title button listener.
     ***************************************************************************************************/
    protected ActionListener getMinimizeListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setMinimized();
            }
        };
    }

    /****************************************************************************************************
     * Retrieves the maximize title button listener.
     ***************************************************************************************************/
    protected ActionListener getMaximizeListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isMaximized()) {
                    setRestored();
                } else {
                    setMaximized();
                }
                titlePane.swapRestoreButtonState();
            }
        };
    }

    /****************************************************************************************************
     * Retrieves the close title button listener.
     ***************************************************************************************************/
    protected ActionListener getCloseListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                closeWindow();
            }
        };
    }
}
