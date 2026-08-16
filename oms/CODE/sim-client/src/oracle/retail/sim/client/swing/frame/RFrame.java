package oracle.retail.sim.client.swing.frame;

import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import oracle.retail.sim.client.core.SimApplicationConfig;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.core.OracleFocusPolicy;
import oracle.retail.sim.client.swing.event.HotKeyDispatcher;
import oracle.retail.sim.client.swing.event.HotKeyEventDispatcher;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventAdaptor;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.plaf.custom.CustomThemeManager;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class subclasses the standard JFrame to supply additional custom Oracle functionality, basically
 * some language translation, focus policy, event support and hot key support. This class should be used
 * in place of JFrame at all times.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RFrame extends JFrame implements WindowListener, HotKeyDispatcher {
    private static final long serialVersionUID = -8624065818356018833L;

    private static final String KEY_STATE = "gui.state";
    private static final String KEY_SIZE = "gui.size";
    private static final String KEY_LOCATION = "gui.location";

    private static final String STATE_MAXIMIZED = "MAXIMIZED";
    private static final String STATE_NORMAL = "NORMAL";

    private REventAdaptor eventAdaptor = new REventAdaptor();
    private boolean isMaximized;

    /****************************************************************************************************
     * Constructs new RFrame object.
     ***************************************************************************************************/
    public RFrame() {
        addWindowListener(this);
        setFocusTraversalPolicy(new OracleFocusPolicy());
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new HotKeyEventDispatcher(this));
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
        super.setTitle(Translator.getText(title));
    }

    /****************************************************************************************************
     * Minimizes the frame.
     ***************************************************************************************************/
    public void setMinimized() {
        setExtendedState(ICONIFIED);
    }

    /****************************************************************************************************
     * Maximizes the frame.
     ***************************************************************************************************/
    public void setMaximized() {
        setMaximizedBounds(getMaxBounds());
        setExtendedState(MAXIMIZED_BOTH);
        isMaximized = true;
    }

    /****************************************************************************************************
     * Restores the frame.
     ***************************************************************************************************/
    public void setRestored() {
        setExtendedState(NORMAL);
        isMaximized = false;
    }

    /****************************************************************************************************
     * Returns true if the frame is maximized, otherwise false.
     ***************************************************************************************************/
    public boolean isMaximized() {
        return (getExtendedState() & JFrame.MAXIMIZED_BOTH) == 6;
    }

    /****************************************************************************************************
     * Returns true if the frame is iconfied, otherwise false.
     ***************************************************************************************************/
    public boolean isIconified() {
        return (getExtendedState() & JFrame.ICONIFIED) == 1;
    }

    /****************************************************************************************************
     * Retrieves the maximum size for the window. This uses the bounds of the graphics configuration,
     * minus any screen insets returned by the toolkit.
     * <p>
     * @return The Rectangle object that represents the maximum bounds of the frame.
     ***************************************************************************************************/
    private Rectangle getMaxBounds() {
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());

        bounds.x = bounds.x + insets.left;
        bounds.y = bounds.y + insets.top;
        bounds.width = bounds.width - Math.abs(insets.left - insets.right);
        bounds.height = bounds.height - Math.abs(insets.top - insets.bottom);

        return bounds;
    }

    /****************************************************************************************************
     * Returns whether or not the RFrame is closeable. This method should be overwritten by subclasses if
     * logic is required.
     * <p>
     * @return True if the RFrame is closeable, false otherwise.
     ***************************************************************************************************/
    public boolean isCloseable() {
        return true;
    }

    /****************************************************************************************************
     * Closes the window, but only if isCloseable() return true.
     ***************************************************************************************************/
    public void closeWindow() {
        if (isCloseable()) {
            ApplicationExit.exit(this);
        }
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
     * Removes a REventListener from the REventListener list.
     ***************************************************************************************************/
    public void removeAllREventListeners() {
        eventAdaptor.removeAllREventListeners();
    }

    /****************************************************************************************************
     * Notifies all listeners of an RActionEvent event.
     * <p>
     * @param event An RActionEvent.
     ***************************************************************************************************/
    public void notifyREventListeners(RActionEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Notifies all listeners of an RErrorEvent event.
     * <p>
     * @param event An RErrorEvent.
     ***************************************************************************************************/
    public void notifyREventListeners(RErrorEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Implements the required REventListener method. As default functinoality, it sends the event to all
     * current REventListeners of the frame.
     * <p>
     * @param event The RErrorEvent that triggered this listener method.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
        eventAdaptor.notifyREventListeners(event);
    }

    /****************************************************************************************************
     * Implements the hot key dispatcher to pop the Navigator Window when the hot key is pressed.
     ***************************************************************************************************/
    public boolean hotKeyPressed(int keyCode) {
        return doHotKeyPressed(keyCode);
    }

    /****************************************************************************************************
     * Empty implementation of method than can be overridden by subclasses to process hot keys.
     ***************************************************************************************************/
    protected boolean doHotKeyPressed(int keyCode) {
        return false;
    }

    /****************************************************************************************************
     * Empty implementation to satisfy the WindowListener interface.
     ***************************************************************************************************/
    public void windowActivated(WindowEvent event) {
    }

    /****************************************************************************************************
     * Empty implementation to satisfy the WindowListener interface.
     ***************************************************************************************************/
    public void windowDeactivated(WindowEvent event) {
    }

    /****************************************************************************************************
     * Empty implementation to satisfy the WindowListener interface.
     ***************************************************************************************************/
    public void windowClosed(WindowEvent event) {
    }

    /****************************************************************************************************
     * Closes the window if the window is closeable.
     ***************************************************************************************************/
    public void windowClosing(WindowEvent event) {
        closeWindow();
    }

    /****************************************************************************************************
     * Empty implementation to satisfy the WindowListener interface.
     ***************************************************************************************************/
    public void windowIconified(WindowEvent event) {
        isMaximized = isMaximized();
    }

    /****************************************************************************************************
     * Maximizes the window if it has been previously maximized.
     ***************************************************************************************************/
    public void windowDeiconified(WindowEvent event) {
        if (isMaximized) {
            setMaximized();
        } else {
            setRestored();
        }
    }

    /****************************************************************************************************
     * Empty implementation to satisfy the WindowListener interface.
     ***************************************************************************************************/
    public void windowOpened(WindowEvent event) {
    }

    /****************************************************************************************************
     * Method will parse and use string properties and set window to desired size and location. The
     * default size will be Window#getPreferredSize. The default location will be 0,0.
     * <p>
     * @param window The window
     * @param properties The properties containing the size and location.
     ***************************************************************************************************/
    protected void resetWindowProperties(Properties properties) {
        CustomThemeManager.applyTheme(SimApplicationConfig.getLastThemeNameFromCache(), LocaleManager.getLanguageLocale());
        if (STATE_MAXIMIZED.equals(properties.getProperty(KEY_STATE, STATE_NORMAL))) {
            setMaximized();
            return;
        }
        resetSize(properties);
        resetLocation(properties);
    }

    /****************************************************************************************************
     * Resets the frame's display size based on the values in the configuration properties.
     ***************************************************************************************************/
    private void resetSize(Properties properties) {
        String sizeValue = properties.getProperty(KEY_SIZE, StringConstants.EMPTY);

        StringTokenizer tokenizer = new StringTokenizer(sizeValue, "java.awt.Dimension[width=,height]");
        if (tokenizer.countTokens() == 2) {
            try {
                int width = Integer.parseInt(tokenizer.nextToken());
                int height = Integer.parseInt(tokenizer.nextToken());
                if (width < 1024) {
                    width = 1024;
                }
                if (height < 768) {
                    height = 768;
                }
                setSize(width, height);
            } catch (Throwable exception) {
                UILog.error(getClass(), UIMessageText.WINDOW_PROPERTIES_ERROR, exception);
            }
        } else {
            setSize(1024, 768);
        }
    }

    /****************************************************************************************************
     * Resets the frame's display location based on the values in the configuration properties.
     ***************************************************************************************************/
    private void resetLocation(Properties properties) {
        Point location = new Point();
        String pointValue = properties.getProperty(KEY_LOCATION, location.toString());
        StringTokenizer tokenizer = new StringTokenizer(pointValue, "java.awt.Point[x=,y]");

        if (tokenizer.countTokens() == 2) {
            try {
                location.x = Integer.parseInt(tokenizer.nextToken());
                location.y = Integer.parseInt(tokenizer.nextToken());
            } catch (Throwable exception) {
                UILog.error(getClass(), UIMessageText.WINDOW_LOCATION_ERROR, exception);
            }
        } else {
            location.x = 0;
            location.y = 0;
        }
        setLocation(location);
    }

    /****************************************************************************************************
     * Method will get the state, size and location of an RFrame window.
     ***************************************************************************************************/
    public Properties getWindowProperties() {
        Properties properties = new Properties();
        properties.setProperty(KEY_STATE, isMaximized() ? STATE_MAXIMIZED : STATE_NORMAL);
        properties.setProperty(KEY_SIZE, getSize().toString());
        properties.setProperty(KEY_LOCATION, getLocation().toString());
        return properties;
    }
}
