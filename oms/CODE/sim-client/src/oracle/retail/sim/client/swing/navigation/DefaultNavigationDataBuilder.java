package oracle.retail.sim.client.swing.navigation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import oracle.retail.sim.client.swing.core.ConfigurationManager;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.configutil.ResourceManager;

/******************************************************************************************
 * This class handles the construction of the NavigationData object used to initialize
 * the navigation area of the application.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class DefaultNavigationDataBuilder implements NavigationDataBuilder {

    private final NavigationDataParser parser = new NavigationDataParser();

    private static final String DEFAULT_RESOURCE_KEY = "Navigation Filename";

    /******************************************************************************************
     * Returns new NavigationDataBuilder object.
     *****************************************************************************************/
    public DefaultNavigationDataBuilder() {
    }

    /******************************************************************************************
     * Creates and returns the navigation data object. It looks for a default file named
     * "Navigation.xml".
     * <p>
     * @return The fully formed navigation data object.
     * <p>
     * @throws OldUIException Thrown if an exception occurs attempting to load the resource.
     *****************************************************************************************/
    public NavigationData getNavigationData() throws UIException {
        String filename = ConfigurationManager.getConfiguration().getString(DEFAULT_RESOURCE_KEY);
        try (InputStream inputStream = ResourceManager.getInputStream("conf/" + filename)) {
            return parser.getNavigationData(getNavigationResourceAsText(inputStream, filename));
        } catch (UIException e) {
            throw e;
        } catch (Throwable t) {
            throw new UIException(UIMessageText.NAVIGATION_RESOURCE_READ_ERROR, t.getLocalizedMessage());
        }
    }

    /******************************************************************************************
     * Creates and returns the navigation data object for the specified file.
     * <p>
     * @param file The file to read the navigation data from.
     * <p>
     * @return The fully formed navigation data object.
     * <p>
     * @throws OldUIException Thrown if an exception occurs attempting to load the resource.
     *****************************************************************************************/
    public NavigationData getNavigationData(File file) throws UIException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return parser.getNavigationData(getNavigationResourceAsText(inputStream, file.getAbsolutePath()));
        } catch (UIException e) {
            throw e;
        } catch (Throwable t) {
            throw new UIException(UIMessageText.NAVIGATION_RESOURCE_READ_ERROR, t.getLocalizedMessage());
        }
    }

    /******************************************************************************************
     * Creates and returns the navigation data xml string. This class reads the input stream
     * and returns the .xml as a string. This method should be overridden by any software that
     * does not want to define their navigation system using the default .xml document style.
     * <p>
     * @param inputStream The input stream to read the information from.
     * @param filename The filename of the resource to load.
     * <p>
     * @return The xml string representation of the navigation data.
     * <p>
     * @throws OldUIException Thrown if an exception occurs attempting to load the resource.
     *****************************************************************************************/
    protected String getNavigationResourceAsText(InputStream inputStream, String filename) throws Exception {
        if (inputStream == null) {
            throw new UIException(UIMessageText.NAVIGATION_RESOURCE_NOT_FOUND, filename);
        }
        if (inputStream.available() < 1) {
            throw new UIException(UIMessageText.NAVIGATION_RESOURCE_EMPTY, filename);
        }
        StringBuilder navigationText = new StringBuilder();
        int value;
        while (true) {
            value = inputStream.read();
            if (value == -1) {
                break;
            }
            navigationText.append((char) value);
        }
        return navigationText.toString();
    }
}
