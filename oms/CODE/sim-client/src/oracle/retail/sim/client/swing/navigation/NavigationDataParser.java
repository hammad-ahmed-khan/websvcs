package oracle.retail.sim.client.swing.navigation;

import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.config.NavigationData;

/******************************************************************************************
 * This handles parsing properly formatted Navigation XML resources into a NavigationData
 * object. The formatting of the XML strings is very specific and is hard-coded in this
 * parser to avoid having to use a full XML engine to accomplish this simple task. It
 * cleanly throwns UIException if an invalid XML format is encountered. A demo example of
 * a Navigation xml file can be found in "Navigation.xml".
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class NavigationDataParser {

    // ERROR CODES

    private static final UIMessageText MISMATCH_TAG = UIMessageText.NAVIGATION_PARSE_MISMATCH_TAG;

    // TAGS

    private static final String OPEN_TAB = "<tab>";
    private static final String CLOSE_TAB = "</tab>";

    private static final String OPEN_TAB_NAME = "<tab_name>";
    private static final String CLOSE_TAB_NAME = "</tab_name>";

    private static final String OPEN_TAB_PERMISSION = "<tab_permission>";
    private static final String CLOSE_TAB_PERMISSION = "</tab_permission>";

    private static final String OPEN_TASK = "<task>";
    private static final String CLOSE_TASK = "</task>";

    private static final String OPEN_TASK_NAME = "<task_name>";
    private static final String CLOSE_TASK_NAME = "</task_name>";

    private static final String OPEN_TASK_PERMISSION = "<task_permission>";
    private static final String CLOSE_TASK_PERMISSION = "</task_permission>";

    private static final String OPEN_DEFAULT_TASK_ITEM = "<default_task_item>";
    private static final String CLOSE_DEFAULT_TASK_ITEM = "</default_task_item>";

    private static final String OPEN_TASK_ITEM = "<task_item>";
    private static final String CLOSE_TASK_ITEM = "</task_item>";

    private static final String OPEN_TASK_ITEM_NAME = "<task_item_name>";
    private static final String CLOSE_TASK_ITEM_NAME = "</task_item_name>";

    private static final String OPEN_TASK_ITEM_PERMISSION = "<task_item_permission>";
    private static final String CLOSE_TASK_ITEM_PERMISSION = "</task_item_permission>";

    private static final String OPEN_TASK_ITEM_COMMAND = "<task_item_command>";
    private static final String CLOSE_TASK_ITEM_COMMAND = "</task_item_command>";

    private static final String OPEN_TASK_ITEM_DUPLICATE = "<task_item_duplicate>";
    private static final String CLOSE_TASK_ITEM_DUPLICATE = "</task_item_duplicate>";

    private static final String OPEN_TASK_ITEM_NEXT = "<task_item_next>";
    private static final String CLOSE_TASK_ITEM_NEXT = "</task_item_next>";

    private static final String OPEN_TASK_ITEM_PREVIOUS = "<task_item_previous>";
    private static final String CLOSE_TASK_ITEM_PREVIOUS = "</task_item_previous>";

    /******************************************************************************************
     * Returns a new NavigationDataParser object.
     ******************************************************************************************/
    public NavigationDataParser() {
    }

    /******************************************************************************************
     * Retrieves a navigation data object for the resource string.
     * <p>
     * @param inputText An xml string containing properly formatted data to parse.
     * @param NavigationData A NavigationData object built from the string.
     ******************************************************************************************/
    public NavigationData getNavigationData(String inputText) throws UIException {
        NavigationData navigationData = new NavigationData();

        if (inputText.length() < 1) {
            throw new UIException(UIMessageText.NAVIGATION_INPUT_EMPTY);
        }
        parseData(navigationData, inputText);

        return navigationData;
    }

    /******************************************************************************************
     * Parses the input text, looping through and peeling off each instance of tab and executing
     * parseTab().
     ******************************************************************************************/
    private void parseData(NavigationData navigationData, String inputText) throws UIException {
        boolean noTabsInData = true;

        int minimumTabSize = OPEN_TAB.length() + CLOSE_TAB.length() + 1;
        int startPosition = -1;
        int stopPosition = -1;

        while (inputText.length() > minimumTabSize) {
            startPosition = inputText.indexOf(OPEN_TAB);
            stopPosition = inputText.indexOf(CLOSE_TAB);

            if (startPosition == -1) {
                break;
            }

            if (stopPosition == -1) {
                throw new UIException(MISMATCH_TAG, OPEN_TAB + "-" + CLOSE_TAB);
            }

            noTabsInData = false;

            parseTab(navigationData, inputText.substring(startPosition + OPEN_TAB.length(), stopPosition));

            inputText = inputText.substring(stopPosition + CLOSE_TAB.length()).trim();
        }

        if (noTabsInData) {
            throw new UIException(UIMessageText.NAVIGATION_PARSE_NO_TAG, OPEN_TAB);
        }
    }

    /******************************************************************************************
     * Parses the tab input text, finding the name and adding the tab to the navigation data
     * object, followed by finding all the task input text and calling parseMenu().
     ******************************************************************************************/
    private void parseTab(NavigationData navigationData, String tabText) throws UIException {
        String tabName = parseString(tabText, OPEN_TAB_NAME, CLOSE_TAB_NAME);
        String permission = parseString(tabText, OPEN_TAB_PERMISSION, CLOSE_TAB_PERMISSION, false);

        navigationData.addTab(tabName, permission);

        boolean noMenusInTab = true;

        int minimumMenuSize = OPEN_TASK.length() + CLOSE_TASK.length() + 1;
        int startPosition = -1;
        int stopPosition = -1;

        while (tabText.length() > minimumMenuSize) {
            startPosition = tabText.indexOf(OPEN_TASK);
            stopPosition = tabText.indexOf(CLOSE_TASK);

            if (startPosition == -1) {
                break;
            }

            if (stopPosition == -1) {
                throw new UIException(MISMATCH_TAG, OPEN_TASK + "-" + CLOSE_TASK);
            }

            noMenusInTab = false;

            parseTask(navigationData, tabName, tabText.substring(startPosition + OPEN_TASK.length(), stopPosition));

            tabText = tabText.substring(stopPosition + CLOSE_TASK.length()).trim();
        }

        if (noMenusInTab) {
            throw new UIException(UIMessageText.NAVIGATION_PARSE_NO_TAG, OPEN_TASK);
        }
    }

    /******************************************************************************************
     * Parses the task input text, finding the name and adding the task to the navigation data
     * object, followed by finding all the task item input text and calling parseMenuItem().
     ******************************************************************************************/
    private void parseTask(NavigationData navigationData, String tabName, String taskText) throws UIException {
        String taskName = parseString(taskText, OPEN_TASK_NAME, CLOSE_TASK_NAME);
        String defaultItem = parseString(taskText, OPEN_DEFAULT_TASK_ITEM, CLOSE_DEFAULT_TASK_ITEM);
        String permission = parseString(taskText, OPEN_TASK_PERMISSION, CLOSE_TASK_PERMISSION, false);

        navigationData.addTask(tabName, taskName, permission, defaultItem);

        boolean noMenuItemsInMenu = true;

        int minimumMenuItemSize = OPEN_TASK_ITEM.length() + CLOSE_TASK_ITEM.length() + 1;
        int startPosition = -1;
        int stopPosition = -1;

        while (taskText.length() > minimumMenuItemSize) {
            startPosition = taskText.indexOf(OPEN_TASK_ITEM);
            stopPosition = taskText.indexOf(CLOSE_TASK_ITEM);

            if (startPosition == -1) {
                break;
            }

            if (stopPosition == -1) {
                throw new UIException(MISMATCH_TAG, OPEN_TASK_ITEM + "-" + CLOSE_TASK_ITEM);
            }

            noMenuItemsInMenu = false;

            String taskItemText = taskText.substring(startPosition + OPEN_TASK_ITEM.length(), stopPosition);

            parseTaskItem(navigationData, tabName, taskName, taskItemText);

            taskText = taskText.substring(stopPosition + CLOSE_TASK_ITEM.length()).trim();
        }

        if (noMenuItemsInMenu) {
            throw new UIException(UIMessageText.NAVIGATION_PARSE_NO_TAG, OPEN_TASK_ITEM);
        }
    }

    /******************************************************************************************
     * Parses the task item input text, finding the name, duplicate and command values and
     * adding the task item to the navigation data object.
     ******************************************************************************************/
    private void parseTaskItem(NavigationData navigationData, String tabName, String taskName, String taskItemText) throws UIException {

        String title = parseString(taskItemText, OPEN_TASK_ITEM_NAME, CLOSE_TASK_ITEM_NAME);
        String perm = parseString(taskItemText, OPEN_TASK_ITEM_PERMISSION, CLOSE_TASK_ITEM_PERMISSION, false);
        String command = parseString(taskItemText, OPEN_TASK_ITEM_COMMAND, CLOSE_TASK_ITEM_COMMAND, false);
        String nextItem = parseString(taskItemText, OPEN_TASK_ITEM_NEXT, CLOSE_TASK_ITEM_NEXT, false);
        String prevItem = parseString(taskItemText, OPEN_TASK_ITEM_PREVIOUS, CLOSE_TASK_ITEM_PREVIOUS, false);
        String dupText = parseString(taskItemText, OPEN_TASK_ITEM_DUPLICATE, CLOSE_TASK_ITEM_DUPLICATE, false);

        boolean duplicate = false;

        if (Boolean.TRUE.toString().equalsIgnoreCase(dupText)) {
            duplicate = true;
        }

        navigationData.addTaskItem(tabName, taskName, title, perm, command, duplicate, nextItem, prevItem);
    }

    /******************************************************************************************
     * Parses the string between the open tag and close tag.
     * <p>
     * @param text The text to parse.
     * @param openTag The open tag in the text.
     * @param closeTag The closed tag in the text.
     * <p>
     * @return The string between the two tags with the text.
     * <p>
     * @throws OldUIException if an error occurs parsing the text.
     ******************************************************************************************/
    private String parseString(String text, String openTag, String closeTag) throws UIException {
        return parseString(text, openTag, closeTag, true);
    }

    /******************************************************************************************
     * Parses the string between the open tag and close tag.
     * <p>
     * @param text The text to parse.
     * @param openTag The open tag in the text.
     * @param closeTag The closed tag in the text.
     * @param size The size of the open tag.
     * @param isRequired True if the tag pair is required in the text, false if not.
     * <p>
     * @return The string between the two tags with the text.
     * <p>
     * @throws OldUIException if an error occurs parsing the text.
     ******************************************************************************************/
    private String parseString(String text, String openTag, String closeTag, boolean isRequired) throws UIException {
        int startPosition = text.indexOf(openTag);
        int stopPosition = text.indexOf(closeTag);

        if (isRequired && startPosition == -1) {
            throw new UIException(UIMessageText.NAVIGATION_PARSE_NO_TAG, openTag);
        }
        if (isRequired && stopPosition == -1) {
            throw new UIException(MISMATCH_TAG, openTag + "-" + closeTag);
        }
        if (startPosition > -1 && stopPosition > -1) {
            return text.substring(startPosition + openTag.length(), stopPosition);
        }
        return null;
    }
}
