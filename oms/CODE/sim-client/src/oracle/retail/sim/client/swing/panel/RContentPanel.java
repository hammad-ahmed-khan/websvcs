package oracle.retail.sim.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.core.RetailEditorManager;
import oracle.retail.sim.client.swing.dialog.RInfoDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventAdaptor;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.task.UITask;
import oracle.retail.sim.client.swing.task.UITaskExecutor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RIconButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * The RContentPanel is intended to be placed inside an RTaskPanel. It contains a header (title area),
 * footer (button area) and central panel. Many of the values and settings of the title and footer are
 * controllable. The panel is not intended to be modified directly, but to have its inteneral content
 * pane modified. getContentPane() returns the RPanel that should contain the contents (including more
 * panels). setContentPane() allows the setting of an RPanel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RContentPanel extends JPanel {
    private static final long serialVersionUID = 8870893189579151415L;

    private RetailEditorManager editorManager = new RetailEditorManager();

    private RContentTitlePanel titlePanel = new RContentTitlePanel();
    private RPanel contentPanel = new RPanel();
    private RPanel bottomPanel = new RPanel();
    private RButtonPanel buttonPanel = new RButtonPanel();

    private RIconButton titleButton = new RIconButton();
    private RLabel titleLabel = new RLabel();

    private Color contentBorderColor = Color.black;
    private int contentBorderThickness = 1;
    private int contentBorderPad;

    private Color titleBorderColor = Color.black;
    private int titleBorderSize = 1;

    private Icon maximizeIcon;
    private Icon minimizeIcon;

    private boolean isStretchable = true;
    private int panelHeight = -1;

    private REventAdaptor eventAdaptor = new REventAdaptor();

    private static final String METHOD_UNAVAILABLE = "Method is unavilable. Please use setContentPane()";

    /****************************************************************************************************
     * Creates a new RContentPanel.
     ***************************************************************************************************/
    public RContentPanel() {
        uniqueInitialize();
    }

    /****************************************************************************************************
     * Creates a new RContentPanel with a title.
     * <p>
     * @param title The title to assign to the panel.
     ***************************************************************************************************/
    public RContentPanel(String title) {
        uniqueInitialize();
        setTitle(title);
    }

    /****************************************************************************************************
     * Creates a new RContentPanel with a title.
     * <p>
     * @param title The title to assign to the panel.
     * @param stretchable True if the panel should expand when space is available, false otherwise.
     ***************************************************************************************************/
    public RContentPanel(String title, boolean stretchable) {
        uniqueInitialize();
        setTitle(title);
        setStretchable(stretchable);
    }

    /****************************************************************************************************
     * Initializes the panel.
     ***************************************************************************************************/
    private void uniqueInitialize() {
        uniqueInitializeContentPanel();
        uniqueInitializeTitlePanel();
        uniqueInitializeBottomPanel();
        uniqueInitializeWidgetManager();
        layoutPanel();
    }

    /****************************************************************************************************
     * Sets the default values of the content area.
     ***************************************************************************************************/
    private void uniqueInitializeContentPanel() {
        setContentBorderColor(UIManager.getColor(UIThemeName.CONTENTPANEL_BORDER_COLOR));
        setContentBorderThickness(UIManager.getInt(UIThemeName.CONTENTPANEL_BORDER_THICKNESS));
        setContentBorderPad(UIManager.getInt(UIThemeName.CONTENTPANEL_BORDER_PAD));
    }

    /****************************************************************************************************
     * Sets the default values of the title area.
     ***************************************************************************************************/
    private void uniqueInitializeTitlePanel() {
        setTitleBorderColor(UIManager.getColor(UIThemeName.CONTENTPANEL_TITLE_BORDER_COLOR));
        setTitleBorderSize(UIManager.getInt(UIThemeName.CONTENTPANEL_TITLE_BORDER_SIZE));
        setTitleForeground(UIManager.getColor(UIThemeName.CONTENTPANEL_TITLE_FOREGROUND));
        setTitleBackground(UIManager.getColor(UIThemeName.CONTENTPANEL_TITLE_BACKGROUND));
        setTitleFont(UIManager.getFont(UIThemeName.CONTENTPANEL_TITLE_FONT));

        maximizeIcon = UIManager.getIcon(UIThemeName.CONTENTPANEL_TITLE_MAX_ICON);
        minimizeIcon = UIManager.getIcon(UIThemeName.CONTENTPANEL_TITLE_MIN_ICON);

        titleButton.addActionListener(getMaximizeListener());

        setTitleIcon(minimizeIcon);

        titlePanel.setMinimumHeight(19);
        titlePanel.setBorder(null);
    }

    /****************************************************************************************************
     * Initializes the RetailEditorManager.
     ***************************************************************************************************/
    private void uniqueInitializeWidgetManager() {
        editorManager.setOwner(this);
    }

    /****************************************************************************************************
     * Sets the default values of the footer area.
     ***************************************************************************************************/
    private void uniqueInitializeBottomPanel() {
        bottomPanel.setVisible(false);
    }

    /****************************************************************************************************
     * Lays out all the components in the content panel.
     ***************************************************************************************************/
    private void layoutPanel() {
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.add(titleButton, GridTool.constraints(0, 0, 1, 1, 0, 0, 2, 0, 0, 3, 0, 0));
        titlePanel.add(titleLabel, GridTool.constraints(1, 0, 1, 1, 1, 0, 2, 1, 0, 5, 0, 0));

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        super.add(titlePanel, BorderLayout.NORTH);
        super.add(bottomPanel, BorderLayout.SOUTH);
        super.add(contentPanel, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Retrieves the content pane. Subclasses should either retrieve the content pane using this method
     * or set a content pane. Using any other means of adding components to this panel is invalid.
     * <p>
     * @return The content pane.
     ***************************************************************************************************/
    public RPanel getContentPane() {
        return contentPanel;
    }

    /****************************************************************************************************
     * Assigns the inteneral content panel to this panel.
     * <p>
     * @param panel The panel to assign to the RContentPanel.
     ***************************************************************************************************/
    public void setContentPane(RPanel panel) {
        if (panel != null) {
            remove(contentPanel);
            contentPanel = panel;
            assignContentBorder();
            super.add(contentPanel, BorderLayout.CENTER);
        }
    }

    /****************************************************************************************************
     * Assigns the content pane border color.
     * <p>
     * @param color The color to assign to the content pane border.
     ***************************************************************************************************/
    public void setContentBorderColor(Color color) {
        if (color == null) {
            color = Color.gray;
        }
        contentBorderColor = color;
        assignContentBorder();
    }

    /****************************************************************************************************
     * Retrieves the content pane border color.
     ***************************************************************************************************/
    public Color getContentBorderColor() {
        return contentBorderColor;
    }

    /****************************************************************************************************
     * Assigns the content pane border thickness.
     * <p>
     * @param thickness The thickness to assign to the content pane border.
     ***************************************************************************************************/
    public void setContentBorderThickness(int thickness) {
        if (thickness < 1) {
            thickness = 1;
        }
        contentBorderThickness = thickness;
        assignContentBorder();
    }

    /****************************************************************************************************
     * Retrieves the content border thickness.
     * <p>
     * @return The content border thickness.
     ***************************************************************************************************/
    public int getContentBorderThickness() {
        return contentBorderThickness;
    }

    /****************************************************************************************************
     * Assigns the content pane border pad. This will produce an empty area inside the line border.
     * <p>
     * @param pad The pad to assign to the content pane border.
     ***************************************************************************************************/
    public void setContentBorderPad(int pad) {
        if (pad < 0) {
            pad = 0;
        }
        contentBorderPad = pad;
        assignContentBorder();
    }

    /****************************************************************************************************
     * Retrieves the content border pad.
     * <p>
     * @return The content border pad.
     ***************************************************************************************************/
    public int getContentBorderPad() {
        return contentBorderPad;
    }

    /****************************************************************************************************
     * Inteneral helper method to create and assign the content border.
     ***************************************************************************************************/
    private void assignContentBorder() {
        contentPanel.setLineBorder(contentBorderColor, contentBorderThickness, contentBorderPad);
    }

    /****************************************************************************************************
     * Assigns the title border color.
     * <p>
     * @param color The title border color to assign.
     ***************************************************************************************************/
    public void setTitleBorderColor(Color color) {
        if (color == null) {
            color = Color.black;
        }
        titleBorderColor = color;
        assignTitleBorder();
    }

    /****************************************************************************************************
     * Retrieves the title border color.
     * <p>
     * @reaturn The title border color.
     ***************************************************************************************************/
    public Color getTitleBorderColor() {
        return titleBorderColor;
    }

    /****************************************************************************************************
     * Assigns the title border size.
     * <p>
     * @param size The title border size to assign.
     ***************************************************************************************************/
    public void setTitleBorderSize(int size) {
        if (size < 1) {
            size = 1;
        }
        titleBorderSize = size;
        assignTitleBorder();
    }

    /****************************************************************************************************
     * Retrieves the title border size.
     * <p>
     * @return The title border size.
     ***************************************************************************************************/
    public int getTitleBorderSize() {
        return titleBorderSize;
    }

    /****************************************************************************************************
     * Assigns the title background color of the content panel.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setTitleBackground(Color color) {
        if (color != null) {
            titlePanel.setBackground(color);
            titleLabel.setBackground(color);
            titleButton.setBackground(color);
        }
    }

    /****************************************************************************************************
     * Retrieves the title background color of the content panel.
     * <p>
     * @return The title background color.
     ***************************************************************************************************/
    public Color getTitleBackground() {
        return titleLabel.getBackground();
    }

    /****************************************************************************************************
     * Assigns the title foreground color of the content panel.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setTitleForeground(Color color) {
        if (color != null) {
            titlePanel.setForeground(color);
            titleLabel.setForeground(color);
            titleButton.setForeground(color);
        }
    }

    /****************************************************************************************************
     * Retrieves the title foreground color of the content panel.
     * <p>
     * @return The title foreground color.
     ***************************************************************************************************/
    public Color getTitleForeground() {
        return titleLabel.getForeground();
    }

    /****************************************************************************************************
     * Assigns the title font of the content panel.
     * <p>
     * @param font The font to assign.
     ***************************************************************************************************/
    public void setTitleFont(Font font) {
        if (font != null) {
            titlePanel.setFont(font);
            titleLabel.setFont(font);
            titleButton.setFont(font);
        }
    }

    /****************************************************************************************************
     * Retrieves the title font of the content panel.
     * <p>
     * @return The title font.
     ***************************************************************************************************/
    public Font getTitleFont() {
        return titleLabel.getFont();
    }

    /****************************************************************************************************
     * Assigns the current title icon to display.
     * <p>
     * @param icon The icon to display.
     ***************************************************************************************************/
    private void setTitleIcon(Icon icon) {
        if (icon != null) {
            titleButton.setIcon(icon);
        }
    }

    /****************************************************************************************************
     * Assigns the title border of the title area.
     ***************************************************************************************************/
    private void assignTitleBorder() {
        titlePanel.setBorder(BorderFactory.createLineBorder(titleBorderColor, titleBorderSize));
    }

    /****************************************************************************************************
     * Assigns the title to the title area. This text will be translated.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /****************************************************************************************************
     * Assigns the title to the title area. It translates the title, but not the additional text.
     * <p>
     * @param title The title to assign.
     * @param additionalText The additional text.
     ***************************************************************************************************/
    public void setTitle(String title, String additionalText) {
        titleLabel.setText(Translator.getText(title) + " " + additionalText, false);
    }

    /****************************************************************************************************
     * Sets whether or not the content panel is stretchable. Stretchable panels will increase their size
     * to consume available space.
     * <p>
     * @param stretchable True if the content panel should stretch, false if not.
     ***************************************************************************************************/
    public void setStretchable(boolean stretchable) {
        boolean originalValue = isStretchable;
        isStretchable = stretchable;
        firePropertyChange(UIPropertyName.CONTENTPANEL_EXPANDABLE, originalValue, stretchable);
    }

    /****************************************************************************************************
     * Retrieves whether or not the content panel is stretchable.
     * <p>
     * @return True if the content panel is stretched, false if not.
     ***************************************************************************************************/
    public boolean isStretchable() {
        return isStretchable;
    }

    /****************************************************************************************************
     * Retrieves whether or not the content panel is currently expandable.
     ***************************************************************************************************/
    public boolean isExpanded() {
        return titleButton.getIcon() == minimizeIcon;
    }

    /****************************************************************************************************
     * Assigns the panel height to the content panel. This is the preferred height of the panel if it is
     * not stretchable and it is expanded. Note that minimum size, preferred size and maximum size (if
     * set on a content panel) will be overriden by task panel, which is responsible for laying out
     * content panels. A negative height will remove this value.
     * <p>
     * @param height The desired height in pixels.
     ***************************************************************************************************/
    public void setPanelHeight(int height) {
        panelHeight = height;
    }

    /****************************************************************************************************
     * Retrieves the panel height assigned to the content panel.
     * <p>
     * @return The panel height in pixels.
     ***************************************************************************************************/
    public int getPanelHeight() {
        return panelHeight;
    }

    /****************************************************************************************************
     * Retrieves the preferred panel dimension assigned to the panel. Null will be returned in no panel
     * height has been assigned.
     * <p>
     * @return The preferred panel size of the content panel.
     ***************************************************************************************************/
    public Dimension getPreferredPanelSize() {
        if (panelHeight < 0 || isStretchable || !isExpanded()) {
            return null;
        }
        return new Dimension(0, panelHeight);
    }

    /****************************************************************************************************
     * Retrieves a component contained within the conent panel by its identifer. This will search through
     * all editors and widgets of the content panel and return the first component with a matching
     * identifier.
     * <p>
     * @param identifier The identifier of the editor or widget.
     * @return The matching editor or widget.
     ***************************************************************************************************/
    public Component findComponent(String identifier) {
        return editorManager.findComponent(identifier);
    }

    /****************************************************************************************************
     * Retrieves all components contained within the content panel with the identifier. This will search
     * through all editors and widgets of the content panel and return an array of all the components
     * with a matching identifier.
     * <p>
     * @param identifier The identifier of the editors or widgets.
     * @return An array of editors or widgets with the specified identifier.
     ***************************************************************************************************/
    public Component[] findComponents(String identifier) {
        return editorManager.findComponents(identifier);
    }

    /****************************************************************************************************
     * Enables or disables the action triggers within the editors belonging to the content panel. Regular
     * widgets do not allow action triggers, so developers must handle this in their specific code. This
     * method loops through all editors and sets their actions enabled (or disabled).
     * <p>
     * @param enabled True if editor actions should be sent, false if they should be ignored.
     ***************************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        editorManager.setActionsEnabled(enabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editors have their actions enabled or not.
     * <p>
     * @return True if editor actions should be sent, false if they should be ignored.
     ***************************************************************************************************/
    public boolean isActionsEnabled() {
        return editorManager.isActionsEnabled();
    }

    /****************************************************************************************************
     * Returns whether or not the content of the content panel has been modified.
     ***************************************************************************************************/
    public boolean isContentModified() {
        return editorManager.isContentModified();
    }

    /****************************************************************************************************
     * Sets whether or not the content of the content panel has been modified.
     * <p>
     * @param modified True if the contents should be considered modified, false if not.
     ***************************************************************************************************/
    public void setContentModified(boolean modified) {
        editorManager.setContentModified(modified);
    }

    /****************************************************************************************************
     * Adds a REventListener to the REventListener list.
     * <p>
     * @param listener The REventListener to add.
     ***************************************************************************************************/
    public void addREventListener(REventListener listener) {
        eventAdaptor.addREventListener(listener);
    }

    /****************************************************************************************************
     * Removes a REventListener from the REventListener list.
     * <p>
     * @param listener The REventListener to remove.
     ***************************************************************************************************/
    public void removeREventListener(REventListener listener) {
        eventAdaptor.removeREventListener(listener);
    }

    /****************************************************************************************************
     * Removes all REventListeners from the REventListener list.
     ***************************************************************************************************/
    public void removeAllREventListeners() {
        eventAdaptor.removeAllREventListeners();
    }

    /****************************************************************************************************
     * Notifies all listeners of an action event.
     * <p>
     * @param event An RActionEvent object containing details about the event.
     ***************************************************************************************************/
    public void notifyREventListeners(RActionEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Notifies all listeners of an error event.
     * <p>
     * @param event An RErrorEvent object containing details about the event.
     ***************************************************************************************************/
    public void notifyREventListeners(RErrorEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Implements the required REventListener method.
     * <p>
     * @param event The RErrorEvent that triggered this listener method.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    /****************************************************************************************************
     * Adds a new button to the footer (button area) of the content panel.
     * <p>
     * @param button The button to add.
     ***************************************************************************************************/
    public void addButton(RButton button) {
        buttonPanel.addButton(button);
        bottomPanel.setVisible(true);
    }

    /****************************************************************************************************
     * Retrieves a new maximize listener.
     ***************************************************************************************************/
    protected ActionListener getMaximizeListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isExpanded()) {
                    minimizePanel();
                } else {
                    maximizePanel();
                }
            }
        };
    }

    /****************************************************************************************************
     * Maximizes the panel.
     ***************************************************************************************************/
    protected void maximizePanel() {
        titleButton.setIcon(minimizeIcon);
        contentPanel.setVisible(true);
        buttonPanel.setVisible(true);
        firePropertyChange(UIPropertyName.CONTENTPANEL_SIZE_CHANGED, false, true);
    }

    /****************************************************************************************************
     * Minimizes the panel.
     ***************************************************************************************************/
    protected void minimizePanel() {
        titleButton.setIcon(maximizeIcon);
        contentPanel.setVisible(false);
        buttonPanel.setVisible(false);
        firePropertyChange(UIPropertyName.CONTENTPANEL_SIZE_CHANGED, true, false);
    }

    /****************************************************************************************************
     * Validates that all editors marked as required contain either selection or input data.
     * <p>
     * @throws OldUIException Thrown if an editor marked as required does not contain data.
     ***************************************************************************************************/
    public void validateRequiredContent() throws UIException {
        editorManager.validateRequiredContent();
    }

    /****************************************************************************************************
     * Validates the required editors. This method uses the identifier of each of the editors within the
     * content panel, along with the class identification to look in a .properties file and determine if
     * an editor should be considered required or not.
     ***************************************************************************************************/
    public void validateRequiredEditors() {
        editorManager.validateRequiredEditors();
    }

    /****************************************************************************************************
     * Validates the permissions of the editors and widgets within the content panel.
     * <p>
     * @throws OldUIException Thrown if an error occurs validating the permissions.
     ***************************************************************************************************/
    public void validatePermissions() throws UIException {
        editorManager.validatePermissions();
    }

    /****************************************************************************************************
     * Executes a task asynchronously.
     * <p>
     * @param task The task to execute.
     ***************************************************************************************************/
    public void execute(UITask task) {
        UITaskExecutor.execute(this, task);
    }

    /****************************************************************************************************
     * Executes a task asynchronously.
     * <p>
     * @param task The task to execute.
     * @param message Message to display while executing.
     ***************************************************************************************************/
    public void execute(UITask task, MessageText message) {
        UITaskExecutor.execute(this, task, message);
    }

    /****************************************************************************************************
     * Retrieves whether or not the content panel currently contains any exceptions.
     * <p>
     * @return True if the content contains any exceptions, false otherwise.
     ***************************************************************************************************/
    public boolean hasExceptions() {
        return editorManager.hasExceptions();
    }

    /****************************************************************************************************
     * Clears all exceptions from the content panel.
     ***************************************************************************************************/
    public void clearExceptions() {
        editorManager.clearExceptions();
    }

    /****************************************************************************************************
     * Displays a status window with an Informational message.
     * <p>
     * @param message A message code to translate and display.
     ***************************************************************************************************/
    public void showMessageWindow(MessageText message) {
        RInfoDialog dialog = new RInfoDialog(ApplicationInternal.getFrame(), "Message");
        dialog.displayMessage(message);
    }

    /****************************************************************************************************
     * Displays a message in the status bar (defaults to RErrorSeverity.INFO);
     * <p>
     * @param message A message code to translate and display.
     ***************************************************************************************************/
    public void displayMessage(MessageText message) {
        UIStatusUtility.displayMessage(this, message);
    }

    /****************************************************************************************************
     * Logs a debug message.
     * <p>
     * @param message The message for the logger to handle.
     ***************************************************************************************************/
    protected void displayDebugMessage(MessageText message) {
        UILog.debug(getClass(), message);
    }

    /****************************************************************************************************
     * Displays a message in the status bar (defaults to RErrorSeverity.INFO) and sends the status bar
     * into searching mode.
     * <p>
     * @param message A message code to translate and display.
     ***************************************************************************************************/
    public void displaySearchMessage(MessageText message) {
        UIStatusUtility.displaySearchMessage(this, message);
    }

    /****************************************************************************************************
     * Displays a warning message in the status bar.
     * <p>
     * @param message A message code to translate and display.
     ***************************************************************************************************/
    public void displayWarning(MessageText message) {
        UIStatusUtility.displayWarning(this, message);
    }

    /****************************************************************************************************
     * Displays a RErrorEvent exception on the status bar.
     * <p>
     * @param exception A RErrorEvent (or sub-class thereof).
     ***************************************************************************************************/
    public void displayException(RErrorEvent event) {
        UIStatusUtility.displayException(this, event);
    }

    /****************************************************************************************************
     * Displays an exception in the appropriate manner. It determines if the exception is a UIException,
     * RuntimeException or other and calls the appropriate method.
     * <p>
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(Throwable throwable) {
        UIStatusUtility.displayException(this, throwable);
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the 1.1 method,
     * add(Component, Object), in place of this method. This method has been overridden and made
     * unavailable.
     * <p>
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container at the given index. This method has been overridden
     * and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ***************************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container with the specified constraints at the specified
     * index. Also notifies the layout manager to add the component to the this container's layout using
     * the specified constraints object. The method has been overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component. -1 means
     *            insert at the end.
     ***************************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     *
     * INNER CLASS TITLE PANEL - Overrides RPanel paint() to supply custom title painting.
     *
     ***************************************************************************************************/

    private class RContentTitlePanel extends RPanel {
        private static final long serialVersionUID = 8955485720202495389L;

        private GeneralPath path = new GeneralPath();

        public final void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(getX(), getY(), getWidth(), getHeight());

            graphics2D.setColor(getBackground());
            int width = getWidth();
            int height = getHeight();
            path.reset();
            path.moveTo(0, height - 1);
            path.lineTo(0, height - 6);
            path.quadTo(1, 1, 15, 0);
            path.lineTo(width - 1, 0);
            path.lineTo(width - 1, height - 1);
            path.lineTo(0, height - 1);
            graphics2D.fill(path);
            graphics2D.setColor(getTitleBorderColor());
            graphics2D.draw(path);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }
}
