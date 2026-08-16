package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.common.configutil.ResourceManager;

/********************************************************************************************************
 * This class subclasses the metal look and feel and then adds our own UI objects to the defaults.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomMetalLookAndFeel extends MetalLookAndFeel implements CustomLookAndFeel, Serializable {
    private static final long serialVersionUID = 5753726172674851184L;

    private final String packageName = CustomThemeManager.PACKAGE;
    private Map customMap;

    /****************************************************************************************************
     * Assigns a map of custom default UI settings. These settings will override the superclass. This
     * method has no effect if executed after the look and feel is installed in the UIManager.
     * <p>
     * @param defaults A map of UI default settings.
     ***************************************************************************************************/
    public void setCustomDefaults(Map defaultMap) {
        customMap = defaultMap;
    }

    /****************************************************************************************************
     * Initialize the uiClassID to BasicComponentUI mapping. The JComponent classes define their own
     * uiClassID constants (see AbstractComponent.getUIClassID). This table must map those constants to a
     * BasicComponentUI class of the appropriate type.
     * <p>
     * @param defaults A UIDefaults object to place the new UI features in.
     ***************************************************************************************************/
    protected void initClassDefaults(UIDefaults defaults) {
        super.initClassDefaults(defaults);
        Map<String, String> map = new HashMap<>(5);
        map.put("ArrowButtonUI", packageName + "CustomArrowButtonUI");
        map.put("BoxEditorLabelUI", packageName + "CustomBoxEditorLabelUI");
        map.put("EditorLabelUI", packageName + "CustomEditorLabelUI");
        map.put("MaskLabelUI", packageName + "CustomMaskLabelUI");
        map.put("ExpandButtonUI", packageName + "CustomExpandButtonUI");
        map.put("HyperlinkUI", packageName + "CustomHyperlinkUI");
        map.put("SimToolBarUI", packageName + "CustomToolBarUI");
        defaults.putDefaults(CustomThemeManager.convertToArray(map));
    }

    /****************************************************************************************************
     * Load the SystemColors into the defaults table. The keys for SystemColor defaults are the same as
     * the names of the public fields in SystemColor. If the table is being created on a native Windows
     * platform we use the SystemColor values, otherwise we create color objects whose values match the
     * defaults Windows95 colors.
     * <p>
     * @param defaults A UIDefaults object to place the default system colors in.
     ***************************************************************************************************/
    protected void initSystemColorDefaults(UIDefaults defaults) {
        super.initSystemColorDefaults(defaults);
    }

    /****************************************************************************************************
     * Load the component default values for the system. This calls several helper methods to init
     * various parts of the UI default settings. The very last method calls inits any custom settings
     * from a custom theme.
     * <p>
     * @param defaults A UIDefaults object containing all the component default values.
     ***************************************************************************************************/
    protected void initComponentDefaults(UIDefaults defaults) {
        super.initComponentDefaults(defaults);
        initComponentsFonts(defaults);
        initComponentsIcons(defaults);
        initComponentsSizes(defaults);
        initComponentsMiscs(defaults);
        initComponentsColor(defaults);
        initComponentsCustom(defaults);
    }

    /****************************************************************************************************
     * Init Fonts
     ***************************************************************************************************/
    private void initComponentsFonts(UIDefaults defaults) {
        Map<String, Font> map = new HashMap<>();
        map.put("Button.font", getDefaultBoldFont());
        map.put("CheckBox.font", getDefaultFont());
        map.put("CheckBoxMenuItem.font", getDefaultFont());
        map.put("CheckBoxMenuItem.acceleratorFont", getDefaultFont());
        map.put("ColorChooser.font", getDefaultFont());
        map.put("ComboBox.font", getDefaultFont());
        map.put("DesktopIcon.font", getDefaultFont());
        map.put("EditorPane.font", getDefaultFont());
        map.put("FormattedTextField.font", getDefaultBoldFont());
        map.put("Frame.font", getDefaultBoldFont());
        map.put("GlobalBar.font", getDefaultBoldFont());
        map.put("Hyperlink.font", getDefaultFont());
        map.put("InternalFrame.font", getDefaultFont());
        map.put("InternalFrame.titleFont", getDefaultBoldFont());
        map.put("Label.font", getDefaultFont());
        map.put("List.font", getDefaultFont());
        map.put("Menu.acceleratorFont", getDefaultFont());
        map.put("Menu.font", getDefaultFont());
        map.put("MenuBar.acceleratorFont", getDefaultFont());
        map.put("MenuBar.font", getDefaultFont());
        map.put("MenuItem.acceleratorFont", getDefaultFont());
        map.put("MenuItem.font", getDefaultFont());
        map.put("Navigation.menuFont", getDefaultBoldFont());
        map.put("Navigation.menuItemFont", getDefaultBoldFont());
        map.put("Navigator.titleFont", getDefaultBoldFont());
        map.put("OptionPane.font", getDefaultBoldFont());
        map.put("Panel.font", getDefaultFont());
        map.put("Panel.titleBorderFont", getDefaultBoldFont());
        map.put("Panel.disabledTitleBorderFont", getDefaultFont());
        map.put("PasswordField.font", getDefaultFont());
        map.put("PopupMenu.font", getDefaultFont());
        map.put("ProgressBar.font", getDefaultFont());
        map.put("RadioButton.font", getDefaultFont());
        map.put("RadioButtonMenuItem.acceleratorFont", getDefaultFont());
        map.put("RadioButtonMenuItem.font", getDefaultFont());
        map.put("RCalendar.titleFont", getDefaultBoldFont());
        map.put("RContentPanel.titleFont", getDefaultFont());
        map.put("RDisplayTable.headerFont", getDefaultBoldFont());
        map.put("RDisplayTablePane.font", getDefaultLargeBoldFont());
        map.put("REntryHeader.font", getDefaultBoldFont());
        map.put("REntryTable.font", getDefaultFont());
        map.put("RErrorDialog.font", getDefaultLargeBoldFont());
        map.put("RowTitleTable.headerFont", getDefaultBoldFont());
        map.put("RStatusBar.font", getDefaultFont());
        map.put("ScrollPane.font", getDefaultFont());
        map.put("Spinner.font", getDefaultFont());
        map.put("TabbedPane.font", getDefaultBoldFont());
        map.put("Table.font", getDefaultFont());
        map.put("TableHeader.font", getDefaultFont());
        map.put("TaskPanel.font", getDefaultLargeFont());
        map.put("TaskPanel.titleFont", getDefaultLargeBoldFont());
        map.put("TextArea.font", getDefaultFont());
        map.put("TextField.font", getDefaultFont());
        map.put("TextPane.font", getDefaultFont());
        map.put("ThemeDefault.font", getDefaultFont());
        map.put("ThemeDefaultBold.font", getDefaultBoldFont());
        map.put("ThemeDefaultLarge.font", getDefaultLargeFont());
        map.put("ThemeDefaultLargeBold.font", getDefaultLargeBoldFont());
        map.put("Titlebar.font", getDefaultLargeBoldFont());
        map.put("TitledBorder.font", getDefaultFont());
        map.put("ToggleButton.font", getDefaultFont());
        map.put("ToolBar.font", getDefaultFont());
        map.put("ToolBar.acceleratorFont", getDefaultFont());
        map.put("ToolTip.font", getDefaultFont());
        map.put("Tree.font", getDefaultFont());
        map.put("Viewport.font", getDefaultFont());
        defaults.putDefaults(CustomThemeManager.convertToArray(map));
    }

    /****************************************************************************************************
     * Static methods to retrieve all the correct fonts...
     ***************************************************************************************************/

    public static FontUIResource getDefaultFont() {
        return new FontUIResource("Tahoma", Font.PLAIN, 10);
    }

    public static FontUIResource getDefaultBoldFont() {
        return new FontUIResource("Tahoma", Font.BOLD, 10);
    }

    public static FontUIResource getDefaultLargeFont() {
        return new FontUIResource("Tahoma", Font.PLAIN, 12);
    }

    public static FontUIResource getDefaultLargeBoldFont() {
        return new FontUIResource("Tahoma", Font.BOLD, 12);
    }

    /****************************************************************************************************
     * Init Icons
     * @throws IOException
     ***************************************************************************************************/

    private void initComponentsIcons(UIDefaults defaults) {
        Map<String, Icon> map = new HashMap<>();
        map.put("RCalendarField.icon", ResourceManager.getImageIcon("calendar.gif"));
        map.put("RCalendarField.disabledIcon", ResourceManager.getImageIcon("calendar_disabled.gif"));
        map.put("CheckBox.icon", CustomIcons.createCheckBoxIcon());
        map.put("Error.alertIcon", ResourceManager.getImageIcon("error_alert.gif"));
        map.put("LargeOracle.icon", ResourceManager.getImageIcon("oraclelarge.gif"));
        map.put("ListOfValues.icon", ResourceManager.getImageIcon("listofvalues.gif"));
        map.put("ListOfValues.filterIcon", ResourceManager.getImageIcon("listofvalues_filter.gif"));
        map.put("LoginBackground.icon", ResourceManager.getImageIcon("login_background.gif"));
        map.put("LongField.icon", ResourceManager.getImageIcon("comments.gif"));
        map.put("Navigation.menuItemIcon", ResourceManager.getImageIcon("navigationMenuItem.gif"));
        map.put("Navigation.menuOpenIcon", ResourceManager.getImageIcon("navigationMenuOpen.gif"));
        map.put("Navigation.menuClosedIcon", ResourceManager.getImageIcon("navigationMenuClosed.gif"));
        map.put("Navigator.leafIcon", ResourceManager.getImageIcon("navigationMenuItem.gif"));
        map.put("OptionPane.errorIcon", ResourceManager.getImageIcon("option_error.gif"));
        map.put("OptionPane.informationIcon", ResourceManager.getImageIcon("option_info.gif"));
        map.put("OptionPane.questionIcon", ResourceManager.getImageIcon("option_question.gif"));
        map.put("OptionPane.warningIcon", ResourceManager.getImageIcon("option_warning.gif"));
        map.put("RadioButton.selectedIcon", ResourceManager.getImageIcon("radio_selected.gif"));
        map.put("RadioButton.unselectedIcon", ResourceManager.getImageIcon("radio_unselected.gif"));
        map.put("RContentPanel.maximizeIcon", ResourceManager.getImageIcon("content_maximize.gif"));
        map.put("RContentPanel.minimizeIcon", ResourceManager.getImageIcon("content_minimize.gif"));
        map.put("RDisplayTable.configurationIcon", ResourceManager.getImageIcon("listofvalues_filter.gif"));
        map.put("RStatusBar.progressDisabledIcon", ResourceManager.getImageIcon("progress_disabled.gif"));
        map.put("RStatusBar.progressEnabledIcon", ResourceManager.getImageIcon("progress_enabled.gif"));
        map.put("Sort.ascendingIcon1", ResourceManager.getImageIcon("sort_1_up.gif"));
        map.put("Sort.ascendingIcon2", ResourceManager.getImageIcon("sort_2_up.gif"));
        map.put("Sort.ascendingIcon3", ResourceManager.getImageIcon("sort_3_up.gif"));
        map.put("Sort.ascendingIcon4", ResourceManager.getImageIcon("sort_4_up.gif"));
        map.put("Sort.ascendingIcon5", ResourceManager.getImageIcon("sort_5_up.gif"));
        map.put("Sort.ascendingIcon6", ResourceManager.getImageIcon("sort_6_up.gif"));
        map.put("Sort.ascendingIcon7", ResourceManager.getImageIcon("sort_7_up.gif"));
        map.put("Sort.ascendingIcon8", ResourceManager.getImageIcon("sort_8_up.gif"));
        map.put("Sort.ascendingIcon9", ResourceManager.getImageIcon("sort_9_up.gif"));
        map.put("Sort.ascendingIconX", ResourceManager.getImageIcon("sort_x_up.gif"));
        map.put("Sort.descendingIcon1", ResourceManager.getImageIcon("sort_1_down.gif"));
        map.put("Sort.descendingIcon2", ResourceManager.getImageIcon("sort_2_down.gif"));
        map.put("Sort.descendingIcon3", ResourceManager.getImageIcon("sort_3_down.gif"));
        map.put("Sort.descendingIcon4", ResourceManager.getImageIcon("sort_4_down.gif"));
        map.put("Sort.descendingIcon5", ResourceManager.getImageIcon("sort_5_down.gif"));
        map.put("Sort.descendingIcon6", ResourceManager.getImageIcon("sort_6_down.gif"));
        map.put("Sort.descendingIcon7", ResourceManager.getImageIcon("sort_7_down.gif"));
        map.put("Sort.descendingIcon8", ResourceManager.getImageIcon("sort_8_down.gif"));
        map.put("Sort.descendingIcon9", ResourceManager.getImageIcon("sort_9_down.gif"));
        map.put("Sort.descendingIconX", ResourceManager.getImageIcon("sort_x_down.gif"));
        map.put("TableOfValues.icon", ResourceManager.getImageIcon("tableofvalues.gif"));
        map.put("TaskPanel.retailIcon", ResourceManager.getImageIcon("tasklogo.gif"));
        map.put("TaskPanel.maxIcon", ResourceManager.getImageIcon("taskmaximize.gif"));
        map.put("TaskPanel.minIcon", ResourceManager.getImageIcon("taskminimize.gif"));
        map.put("Titlebar.icon", ResourceManager.getImageIcon("titlebar.gif"));
        map.put("Titlebar.minimizeIcon", ResourceManager.getImageIcon("icon.gif"));
        map.put("Toolbar.backgroundIcon", ResourceManager.getImageIcon("toolbarIcon.gif"));
        defaults.putDefaults(CustomThemeManager.convertToArray(map));
    }

    /****************************************************************************************************
     * Init Sizes
     ***************************************************************************************************/
    private void initComponentsSizes(UIDefaults defaults) {
        Map<String, Integer> map = new HashMap<>();
        map.put("CheckBox.smallSize", 30);
        map.put("CheckBox.mediumSize", 60);
        map.put("CheckBox.largeSize", 90);
        map.put("ComboBox.smallSize", 100);
        map.put("ComboBox.mediumSize", 175);
        map.put("ComboBox.largeSize", 250);
        map.put("RCalendar.smallSize", 80);
        map.put("RCalendar.mediumSize", 80);
        map.put("RCalendar.largeSize", 120);
        map.put("List.smallSize", 75);
        map.put("List.mediumSize", 175);
        map.put("List.largeSize", 275);
        map.put("ListOfValues.smallSize", 75);
        map.put("ListOfValues.mediumSize", 175);
        map.put("ListOfValues.largeSize", 275);
        map.put("LongField.smallSize", 75);
        map.put("LongField.mediumSize", 175);
        map.put("LongField.largeSize", 275);
        map.put("PasswordField.smallSize", 50);
        map.put("PasswordField.mediumSize", 100);
        map.put("PasswordField.largeSize", 200);
        map.put("RCurrencyField.tinySize", 40);
        map.put("RCurrencyField.smallSize", 80);
        map.put("RCurrencyField.mediumSize", 175);
        map.put("RCurrencyField.largeSize", 275);
        map.put("TableOfValues.smallSize", 75);
        map.put("TableOfValues.mediumSize", 175);
        map.put("TableOfValues.largeSize", 275);
        map.put("TextArea.smallSize", 75);
        map.put("TextArea.mediumSize", 175);
        map.put("TextArea.largeSize", 275);
        map.put("TextField.tinySize", 40);
        map.put("TextField.smallSize", 80);
        map.put("TextField.mediumSize", 175);
        map.put("TextField.largeSize", 275);
        defaults.putDefaults(CustomThemeManager.convertToArray(map));
    }

    /****************************************************************************************************
     * Init Miscellaneous
     ***************************************************************************************************/

    private void initComponentsMiscs(UIDefaults defaults) {
        Map<String, Object> map = new HashMap<>();
        map.put("CheckBox.margin", new Insets(2, 0, 2, 0));
        map.put("EditorLabel.repeating", "false");
        map.put("EditorLabel.underline", "false");
        map.put("EditorLabel.suffix", ":");
        map.put("EditorLabel.requiredSymbol", "*");
        map.put("Label.requiredSymbol", "*");
        map.put("ListOfValues.delimeter", CustomThemeManager.getDefaultText("ListOfValues.delimeter"));
        map.put("OptionPane.yesButtonText", CustomThemeManager.getDefaultText("Yes"));
        map.put("OptionPane.noButtonText", CustomThemeManager.getDefaultText("No"));
        map.put("OptionPane.cancelButtonText", CustomThemeManager.getDefaultText("Cancel"));
        map.put("OptionPane.okButtonText", CustomThemeManager.getDefaultText("OK"));
        map.put("Panel.titleBorderMargin", new Insets(0, 5, 5, 5));
        map.put("PasswordField.margin", new Insets(0, 3, 0, 3));
        map.put("RContentPanel.borderThickness", 1);
        map.put("RContentPanel.borderPad", 5);
        map.put("RContentPanel.titleBorderSize", 1);
        map.put("RDisplayLabelEditor.margin", new Insets(1, 2, 1, 2));
        map.put("RDisplayTable.sortLimit", 300);
        map.put("RDisplayTextArea.margin", new Insets(0, 3, 0, 3));
        map.put("RSearchField.buttonWidth", 20);
        map.put("RSearchField.buttonLabel", "...");
        map.put("RSearchField.entryWidth", 100);
        map.put("ScrollBar.width", 20);
        map.put("System.disabledLabelColorsActive", false);
        map.put("System.disabledValueColorsActive", true);
        map.put("TabbedPane.tabInsets", new Insets(2, 10, 2, 10));
        map.put("TabbedPane.contentBorderInsets", new Insets(2, 2, 2, 2));
        map.put("TabbedPane.tabAreaInsets", new Insets(0, 2, 0, 0));
        map.put("TableOfValues.delimeter", CustomThemeManager.getDefaultText("TableOfValues.delimeter"));
        map.put("TextArea.margin", new Insets(0, 3, 0, 3));
        map.put("TextField.margin", new Insets(0, 3, 0, 3));

        defaults.putDefaults(CustomThemeManager.convertToArray(map));
    }

    /****************************************************************************************************
     * Init Colors
     ***************************************************************************************************/

    private void initComponentsColor(UIDefaults defaults) {
        Map<String, Color> map = new HashMap<>();
        map.putAll(getColors1());
        map.putAll(getColors2());
        defaults.putDefaults(CustomThemeManager.convertToArray(map));
    }

    private Map<String, Color> getColors1() {
        Map<String, Color> map = new HashMap<>();
        map.put("BoxLabel.background", getSwanBaseHighlightColor());
        map.put("BoxLabel.foreground", getSwanBaseTextColor());
        map.put("BoxLabel.lineColor", getSwanBaseBorderColor());
        map.put("Button.background", getSwanBaseHighlightColor());
        map.put("Button.foreground", getSwanButtonTextColor());
        map.put("ButtonPanel.background", getSwanBaseHighlightColor());
        map.put("ButtonPanel.foreground", getSwanBaseTextColor());
        map.put("ButtonPanel.lineColor", getSwanBaseBorderColor());
        map.put("CheckBox.disabledForeground", getDefaultInactiveSystemTextColor());
        map.put("CheckBox.background", getSwanBaseBackgroundColor());
        map.put("CheckBox.foreground", getSwanBaseTextColor());
        map.put("CheckBox.defaultBoxBackground", getWhite());
        map.put("CheckBox.defaultBoxForeground", getSwanBaseTextColor());
        map.put("CheckBox.focusBoxBackground", getDefaultTextHighlightColor());
        map.put("CheckBox.focusBoxForeground", getWhite());
        map.put("ComboBox.background", getWhite());
        map.put("ComboBox.foreground", getSwanBaseTextColor());
        map.put("ComboBox.disabledBackground", getSwanBaseBackgroundColor());
        map.put("ComboBox.disabledForeground", getDefaultInactiveSystemTextColor());
        map.put("ComboBox.selectionBackground", getDefaultTextHighlightColor());
        map.put("ComboBox.selectionForeground", getDefaultHighlightedTextColor());
        map.put("ComboBox.buttonBackground", ColorUtility.slightlyDarker(getSwanBaseBackgroundColor()));
        map.put("ComboBox.buttonShadow", getSwanBaseBackgroundColor().darker());
        map.put("ComboBox.buttonDarkShadow", getSwanBaseBackgroundColor().darker().darker());
        map.put("ComboBox.buttonHighlight", getSwanBaseBackgroundColor().brighter());
        map.put("EditorLabel.requiredForeground", getSwanRedStatusColor());
        map.put("Frame.background", getDefaultWindowBackground());
        map.put("Frame.foreground", getBlack());
        map.put("Frame.borderColor", getBlack());
        map.put("GlobalBar.color", Color.BLUE.darker());
        map.put("Hyperlink.foreground", getSwanBaseTextColor());
        map.put("Hyperlink.disabledForeground", ColorUtility.disabledTint(getSwanBaseTextColor()));
        map.put("Hyperlink.selectedForeground", getDefaultHighlightedTextColor());
        map.put("Hyperlink.borderColor", getBlack());
        map.put("Label.requiredForeground", getSwanRedStatusColor());
        map.put("Label.disabledForeground", getDefaultLabelDisabledColor());
        map.put("List.background", getDefaultWindowBackground());
        map.put("List.foreground", getSwanBaseTextColor());
        map.put("List.selectionBackground", getDefaultTextHighlightColor());
        map.put("List.selectionForeground", getDefaultHighlightedTextColor());
        map.put("List.disabledBackground", getSwanBaseBackgroundColor());
        map.put("ListOfValues.linkForeground", Color.BLUE);
        map.put("Navigation.menuBackground", getSwanBaseAlternateBackgroundColor());
        map.put("Navigation.menuForeground", getSwanHeaderTextColor());
        map.put("Navigation.menuFocusForeground", getWhite());
        map.put("Navigation.menuItemBackground", getSwanBaseBackgroundColor());
        map.put("Navigation.menuItemForeground", getSwanHeaderTextColor());
        map.put("Navigation.menuItemRolloverBackground", getSwanBaseHighlightColor());
        map.put("Navigation.menuItemRolloverForeground", getSwanHeaderTextColor());
        map.put("Navigation.menuItemActiveBackground", getDefaultTextHighlightColor());
        map.put("Navigation.menuItemActiveForeground", getWhite());
        map.put("Navigation.tabPaneBackground", getSwanBaseAlternateBackgroundColor());
        map.put("Navigation.tabPaneForeground", getSwanHeaderTextColor());
        map.put("Navigation.tabAreaBackground", ColorUtility.slightlyBrighter(getSwanBaseBackgroundColor()));
        map.put("Navigator.background", getSwanBaseAlternateBackgroundColor());
        return map;
    }

    private Map<String, Color> getColors2() {
        Map<String, Color> map = new HashMap<>();
        map.put("OptionPane.background", getSwanBaseBackgroundColor());
        map.put("Panel.background", getSwanBaseBackgroundColor());
        map.put("Panel.foreground", getSwanBaseTextColor());
        map.put("Panel.disabledForeground", getDefaultInactiveSystemTextColor());
        map.put("Panel.lineColor", getSwanBaseBorderColor());
        map.put("PasswordField.background", getWhite());
        map.put("PasswordField.foreground", getSwanBaseTextColor());
        map.put("PasswordField.inactiveBackground", getSwanBaseBackgroundColor());
        map.put("PasswordField.inactiveForeground", getDefaultInactiveSystemTextColor());
        map.put("PasswordField.focusBackground", getDefaultTextHighlightColor());
        map.put("PasswordField.focusForeground", getWhite());
        map.put("PasswordField.selectionBackground", ColorUtility.slightlyBrighter(getDefaultTextHighlightColor()));
        map.put("PasswordField.selectionForeground", getWhite());
        map.put("PasswordField.caretFocusForeground", getWhite());
        map.put("RadioButton.foreground", getSwanBaseTextColor());
        map.put("RadioButton.background", getSwanBaseBackgroundColor());
        map.put("RCalendar.background", getSwanBaseBackgroundColor());
        map.put("RCalendar.borderColor", getSwanHeaderTextColor());
        map.put("RCalendar.titleBackground", getSwanBaseHighlightColor());
        map.put("RCalendar.titleForeground", getSwanBaseTextColor());
        map.put("RCalendar.dayForeground", getSwanHeaderTextColor());
        map.put("RCalendar.dayBackground", getWhite());
        map.put("RCalendar.dayFocusForeground", getWhite());
        map.put("RCalendar.dayFocusBackground", getSwanBaseBackgroundColor());
        map.put("RCalendar.dayCurrentForeground", getSwanHeaderTextColor());
        map.put("RCalendar.dayCurrentBackground", getSwanBaseAlternateBackgroundColor());
        map.put("RChromePanel.background", getSwanBaseAlternateBackgroundColor());
        map.put("RContainerPanel.foreground", getSwanHeaderTextColor());
        map.put("RContentPanel.borderColor", getSwanBaseBorderColor());
        map.put("RContentPanel.titleBorderColor", getSwanBaseBorderColor());
        map.put("RContentPanel.titleForeground", getSwanBaseTextColor());
        map.put("RContentPanel.titleBackground", getSwanBaseHighlightColor());
        map.put("RDisplayLabelEditor.borderColor", getSwanBaseHighlightColor());
        map.put("RDisplayTable.headerBackground", getSwanTableHeaderBackgroundColor());
        map.put("RDisplayTable.headerForeground", getSwanHeaderTextColor());
        map.put("RDisplayTable.defaultBackground", getSwanTableRowBackgroundColor());
        map.put("RDisplayTable.defaultForeground", getSwanBaseTextColor());
        map.put("RDisplayTable.errorBackground", getSwanRedStatusColor());
        map.put("RDisplayTable.errorForeground", getWhite());
        map.put("RDisplayTable.alternateBackground", getWhite());
        map.put("RDisplayTablePane.background", getSwanTableHeaderBackgroundColor());
        map.put("RDisplayTextArea.background", getSwanBaseBackgroundColor());
        map.put("RDisplayTextArea.foreground", getSwanBaseTextColor());
        map.put("RDivider.background", getSwanBaseBackgroundColor());
        map.put("RDivider.foreground", getSwanHeaderTextColor());
        map.put("REntryHeader.background", getSwanTableHeaderBackgroundColor());
        map.put("REntryHeader.foreground", getSwanHeaderTextColor());
        map.put("REntryTable.background", getSwanTableRowBackgroundColor());
        map.put("REntryTable.foreground", getSwanBaseTextColor());
        map.put("REntryTable.alternateBackground", getWhite());
        map.put("REntryTable.borderColor", getSwanBaseTextColor());
        map.put("RExpandablePanel.foreground", getSwanBaseTextColor());
        map.put("RMessageButton.background", getSwanBaseAlternateBackgroundColor());
        map.put("RMessageButton.foreground", getSwanHeaderTextColor());
        map.put("RowTitleTable.headerBackground", getSwanTableHeaderBackgroundColor());
        map.put("RowTitleTable.headerForeground", getSwanHeaderTextColor());
        map.put("RowTitleTable.cellBackground", getWhite());
        map.put("RowTitleTable.cellForeground", getSwanBaseTextColor());
        map.put("RowTitleTable.cellFocusBackground", getWhite());
        map.put("RowTitleTable.cellFocusForeground", getSwanBaseTextColor());
        map.put("RStatusBar.background", getSwanBaseAlternateBackgroundColor());
        map.put("RStatusBar.foreground", getSwanHeaderTextColor());
        map.put("RStatusBar.messageBackground", getDefaultBlueStatusColor());
        map.put("RStatusBar.messageForeground", getSwanHeaderTextColor());
        map.put("RStatusBar.warningBackground", getDefaultYellowStatusColor());
        map.put("RStatusBar.warningForeground", getSwanHeaderTextColor());
        map.put("RStatusBar.errorBackground", getSwanRedStatusColor());
        map.put("RStatusBar.errorForeground", getWhite());
        map.put("RStatusIndicator.activeBackground", getDefaultGreenStatusColor());
        map.put("RStatusIndicator.activeForeground", getWhite());
        map.put("RStatusIndicator.inactiveBackground", getSwanRedStatusColor());
        map.put("RStatusIndicator.inactiveForeground", getWhite());
        map.put("RStatusWindow.background", getSwanBaseHighlightColor());
        map.put("RStatusWindow.foreground", getSwanBaseTextColor());
        map.put("ScrollBar.track", getDefaultScrollbarTrack());
        map.put("ScrollBar.thumb", getDefaultScrollbarThumb());
        map.put("ScrollBar.shadow", getDefaultScrollbarShadow());
        map.put("ScrollBar.thumbHighlight", getDefaultScrollbarThumbLightShadow());
        map.put("ScrollBar.thumbLightShadow", getDefaultScrollbarThumbLightShadow());
        map.put("ScrollBar.thumbDarkShadow", getDefaultScrollbarThumbDarkShadow());
        map.put("ScrollPane.background", getWhite());
        map.put("ScrollPane.foreground", getSwanBaseTextColor());
        map.put("ScrollPane.lineColor", getSwanBaseBorderColor());
        map.put("SplitPaneDivider.background", getSwanBaseTextColor());
        map.put("SplitPaneDivider.dark", getDefaultSplitPaneDividerDark());
        map.put("SplitPaneDivider.mediumDark", getDefaultSplitPaneDividerMedium());
        map.put("SplitPaneDivider.mediumLight", getDefaultSplitPaneDividerLight());
        map.put("SplitPaneDivider.light", getDefaultSplitPaneDividerBright());
        map.put("TabbedPane.background", getSwanBaseBackgroundColor());
        map.put("TabbedPane.foreground", getSwanBaseTextColor());
        map.put("TabbedPane.tabAreaBackground", ColorUtility.slightlyDarker(getSwanBaseBackgroundColor()));
        map.put("Table.background", getWhite());
        map.put("Table.foreground", getSwanBaseTextColor());
        map.put("Table.selectionBackground", getDefaultTextHighlightColor());
        map.put("Table.selectionForeground", getDefaultHighlightedTextColor());
        map.put("TableHeader.background", getSwanBaseHighlightColor());
        map.put("TableHeader.foreground", getBlack());
        map.put("Table.editorBorderColor", getBlack());
        map.put("TaskPanel.background", getDefaultWindowBackground());
        map.put("TaskPanel.foreground", getSwanHeaderTextColor());
        map.put("TaskPanel.borderBackground", getSwanBaseAlternateBackgroundColor());
        map.put("TextArea.background", getWhite());
        map.put("TextArea.foreground", getSwanBaseTextColor());
        map.put("TextArea.inactiveBackground", getSwanBaseBackgroundColor());
        map.put("TextArea.inactiveForeground", getDefaultInactiveSystemTextColor());
        map.put("TextField.background", getWhite());
        map.put("TextField.foreground", getSwanBaseTextColor());
        map.put("TextField.inactiveBackground", getSwanBaseBackgroundColor());
        map.put("TextField.inactiveForeground", getSwanBaseTextColor());
        map.put("TextField.focusBackground", getDefaultTextHighlightColor());
        map.put("TextField.focusForeground", getWhite());
        map.put("TextField.selectionBackground", getDefaultTextHighlightColor().brighter());
        map.put("TextField.selectionForeground", getWhite());
        map.put("TextField.caretFocusForeground", getWhite());
        map.put("Titlebar.foreground", getBlack());
        map.put("Titlebar.background", new Color(146, 144, 166));
        map.put("TitlebarButton.foreground", new ColorUIResource(74, 73, 89));
        map.put("TitlebarButton.startBackground", getWhite());
        map.put("TitlebarButton.finishBackground", new ColorUIResource(127, 126, 160));
        map.put("TitlebarButton.closeStartBackground", new ColorUIResource(255, 230, 225));
        map.put("TitlebarButton.closeFinishBackground", new ColorUIResource(194, 90, 61));
        map.put("TitlebarButton.borderColor", new ColorUIResource(106, 104, 128));
        map.put("TitlebarButton.borderHighlight", new ColorUIResource(201, 201, 211));
        map.put("TitlebarButton.borderShadow", new ColorUIResource(150, 149, 167));
        return map;
    }

    /****************************************************************************************************
     * Static methods to retrieve all SWAN colors
     ***************************************************************************************************/

    public static ColorUIResource getSwanBaseBackgroundColor() {
        return new ColorUIResource(217, 229, 239); // Light Blue - Bit gray [ Container, Message Box ]
    }

    public static ColorUIResource getSwanBaseBorderColor() {
        return new ColorUIResource(163, 190, 216); // Cream Blue [ Container Border ]
    }

    public static ColorUIResource getSwanBaseHighlightColor() {
        return new ColorUIResource(163, 194, 223); // Pale Blue [ Background ]
    }

    public static ColorUIResource getSwanBaseAlternateBackgroundColor() {
        return new ColorUIResource(234, 239, 245); // Gray with tiny blue [ Region With Header ]
    }

    public static ColorUIResource getSwanAlternateTitleForegroundColor() {
        return new ColorUIResource(58, 90, 135); // Dark Blue [ Page Title, Tips ]
    }

    public static ColorUIResource getSwanTableHeaderBackgroundColor() {
        return new ColorUIResource(207, 224, 241); // Light Blue [ Table Header ]
    }

    public static ColorUIResource getSwanTableRowBackgroundColor() {
        return new ColorUIResource(242, 242, 245); // Gray [ Table Cell ]
    }

    public static ColorUIResource getSwanTableBorderColor() {
        return new ColorUIResource(201, 203, 211); // Gray [ Table Cell ]
    }

    public static ColorUIResource getSwanNavigationBackgroundColor() {
        return new ColorUIResource(204, 215, 224); // BlueGray [ Navigation Area ]
    }

    public static ColorUIResource getSwanBaseTextColor() {
        return new ColorUIResource(60, 60, 60); // Black [ Text ]
    }

    public static ColorUIResource getSwanHeaderTextColor() {
        return new ColorUIResource(52, 52, 52); // Blacker [ Header ]
    }

    public static ColorUIResource getSwanButtonTextColor() {
        return new ColorUIResource(88, 96, 115); // Dark Gray [ Button Text ]
    }

    public static ColorUIResource getSwanRedStatusColor() {
        return new ColorUIResource(237, 28, 36); // Oracle Red [ Status/ Errors]
    }

    public static ColorUIResource getSwanBaseLinksTextColor() {
        return new ColorUIResource(43, 124, 146); // Blue Green
    }

    public static ColorUIResource getSwanContainerLinksTextColor() {
        return new ColorUIResource(20, 117, 114); // Dark Blue Green
    }

    public static ColorUIResource getSwanTableHeaderLinksTextColor() {
        return new ColorUIResource(46, 85, 140); // Dark Blue
    }

    /****************************************************************************************************
     * Static methods to retrieve all the correct colors...
     ***************************************************************************************************/

    private static ColorUIResource getDefaultWindowBackground() {
        return new ColorUIResource(255, 255, 255); // White
    }

    private static ColorUIResource getDefaultScrollbarTrack() {
        return getSwanBaseBackgroundColor();
    }

    private static ColorUIResource getDefaultScrollbarShadow() {
        return new ColorUIResource(getDefaultScrollbarTrack().darker().darker());
    }

    private static ColorUIResource getDefaultScrollbarThumb() {
        return new ColorUIResource(ColorUtility.slightlyDarker(getDefaultScrollbarTrack()));
    }

    private static ColorUIResource getDefaultScrollbarThumbLightShadow() {
        return new ColorUIResource(getDefaultScrollbarThumb().brighter());
    }

    private static ColorUIResource getDefaultScrollbarThumbDarkShadow() {
        return new ColorUIResource(getDefaultScrollbarThumb().darker().darker());
    }

    private static ColorUIResource getDefaultYellowStatusColor() {
        return new ColorUIResource(255, 242, 155);
    }

    public static ColorUIResource getDefaultGreenStatusColor() {
        return new ColorUIResource(70, 150, 70);
    }

    public static ColorUIResource getDefaultBlueStatusColor() {
        return getSwanBaseHighlightColor();
    }

    private static ColorUIResource getDefaultTextHighlightColor() {
        return new ColorUIResource(92, 114, 152);
    }

    private static ColorUIResource getDefaultHighlightedTextColor() {
        return new ColorUIResource(255, 255, 255);
    }

    private static ColorUIResource getDefaultInactiveSystemTextColor() {
        return new ColorUIResource(110, 110, 110);
    }

    private static ColorUIResource getDefaultLabelDisabledColor() {
        return new ColorUIResource(70, 70, 70);
    }

    private static ColorUIResource getDefaultSplitPaneDividerDark() {
        return new ColorUIResource(121, 119, 133);
    }

    private static ColorUIResource getDefaultSplitPaneDividerMedium() {
        return new ColorUIResource(154, 153, 167);
    }

    private static ColorUIResource getDefaultSplitPaneDividerLight() {
        return new ColorUIResource(180, 178, 189);
    }

    private static ColorUIResource getDefaultSplitPaneDividerBright() {
        return new ColorUIResource(241, 241, 243);
    }

    /****************************************************************************************************
     * Init Custom Settings
     ***************************************************************************************************/

    private void initComponentsCustom(UIDefaults defaults) {
        defaults.putDefaults(CustomThemeManager.convertToArray(customMap));
    }

    /****************************************************************************************************
     * Equals and HashCode
     ***************************************************************************************************/

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        CustomMetalLookAndFeel that = (CustomMetalLookAndFeel) object;
        return getID().equals(that.getID());
    }

    public int hashCode() {
        return getID().hashCode();
    }
}
