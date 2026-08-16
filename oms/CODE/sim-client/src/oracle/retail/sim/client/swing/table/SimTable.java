package oracle.retail.sim.client.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.DateDisplayer;
import oracle.retail.sim.client.swing.displayer.IntegerDisplayer;
import oracle.retail.sim.client.swing.displayer.NumberDisplayer;
import oracle.retail.sim.client.swing.displayer.ObjectDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.event.SimTableTransferHandler;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigColumnData;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigConstants;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigData;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigDialog;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigListener;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigPopupMenu;
import oracle.retail.sim.client.swing.tableconfig.RTableConfigRepository;
import oracle.retail.sim.client.swing.tableeditor.BigDecimalTableEditor;
import oracle.retail.sim.client.swing.tableeditor.BooleanTableEditor;
import oracle.retail.sim.client.swing.tableeditor.DoubleTableEditor;
import oracle.retail.sim.client.swing.tableeditor.IntegerTableEditor;
import oracle.retail.sim.client.swing.tableeditor.NumberTableEditor;
import oracle.retail.sim.client.swing.tableeditor.QuantityTableEditor;
import oracle.retail.sim.client.swing.tableeditor.StringTableEditor;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.currency.SimMoney;

/********************************************************************************************************
 * An extension of JTable that provides extensive additional functionality (see methods).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTable extends JTable implements SimTableHeaderInterface, KeyListener, MouseListener, RTableConfigListener, PropertyChangeListener {
    private static final long serialVersionUID = 5125502209026131008L;

    public static final int LABEL_WIDTH = 0;
    public static final int STRETCHABLE = -1;

    private SimTableModel tableModel = new SimTableModel(Object.class);
    private List<SimTableAttribute> originalAttributes = new ArrayList<>();
    private List<SimTableSortAttribute> originalSortAttributes = new ArrayList<>();

    private RTableConfigPopupMenu popupMenu;
    private RTableConfigDialog configDialog;

    private Color tableHeaderBackground = Color.WHITE;
    private Color tableHeaderForeground = Color.BLACK;
    private Color defaultRowBackground = Color.WHITE;
    private Color defaultRowForeground = Color.BLACK;
    private Color alternateRowBackground = Color.WHITE;

    private Font tableHeaderFont = new Font("Verdana", Font.BOLD, 10);

    private REventListener singleActionListener;
    private REventListener doubleActionListener;
    private String singleActionCommand;
    private String doubleActionCommand;

    private String identifier = "TableDefaultID";
    private MouseEvent lastMouseEvent;
    private boolean isConfigurationEnabled = true;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param definition The definition of the table.
     ***************************************************************************************************/
    public SimTable(SimTableDefinition definition) {
        super(new SimTableModel(definition.getDataClass()));
        tableModel = (SimTableModel) getModel();
        tableModel.setOverrideEditableAttributes(definition.getOverrideEditableAttributes());
        setColumnSelectionAllowed(false);
        addKeyListener(this);
        addMouseListener(this);
        setTransferHandler(new SimTableTransferHandler());
        initializeUIDefaults();
        initializeDefaultRenderers();
        initializeDefaultEditors();
        setRowHeight(getRowHeight() + 2);
        identifier = definition.getIdentifier() + ".table";
        originalAttributes = definition.getAttributes();
        originalSortAttributes = definition.getSortAttributes();
        resetTableConfiguration();
        resetSortConfiguration();
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param definition The definition of the table.
     ***************************************************************************************************/
    public SimTable(SimTableDef definition) {
        super(new SimTableModel(definition.getDataClass()));
        tableModel = (SimTableModel) getModel();
        tableModel.setOverrideEditableAttributes(definition.getNotEditableAttributes());
        setColumnSelectionAllowed(false);
        addKeyListener(this);
        addMouseListener(this);
        setTransferHandler(new SimTableTransferHandler());
        initializeUIDefaults();
        initializeDefaultRenderers();
        initializeDefaultEditors();
        setRowHeight(getRowHeight() + 2);
        identifier = definition.getIdentifier() + ".table";
        originalAttributes = definition.getTableAttributes();
        originalSortAttributes = definition.getSortAttributes();
        resetTableConfiguration();
        resetSortConfiguration();
    }

    /****************************************************************************************************
     * Initialize UI defaults setting the default values for foregrounds and backgrounds.
     ***************************************************************************************************/
    private void initializeUIDefaults() {
        setTableHeaderBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_HEADER_BACKGROUND));
        setTableHeaderForeground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_HEADER_FOREGROUND));
        setTableHeaderFont(UIManager.getFont(UIThemeName.RDISPLAYTABLE_HEADER_FONT));
        setRowBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_DEFAULT_BACKGROUND));
        setRowForeground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_DEFAULT_FOREGROUND));
        setAlternateRowBackground(UIManager.getColor(UIThemeName.RDISPLAYTABLE_ALTERNATE_BACKGROUND));
        setShowHorizontalLines(StringUtility.booleanValue(UIManager.getString(UIThemeName.TABLE_SHOW_HORIZONTAL)));
    }

    /****************************************************************************************************
     * Assign default table renderers for all the basic data types.
     ***************************************************************************************************/
    private void initializeDefaultRenderers() {
        setDefaultRenderer(Object.class, new DisplayerTableCellRenderer(new ObjectDisplayer()));
        setDefaultRenderer(Date.class, new DisplayerTableCellRenderer(new DateDisplayer()));
        setDefaultRenderer(Quantity.class, new DisplayerTableCellRenderer(new QuantityDisplayer()));
        setDefaultRenderer(BigDecimal.class, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Number.class, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Integer.class, new DisplayerTableCellRenderer(new IntegerDisplayer()));
        setDefaultRenderer(Double.class, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Float.class, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Long.class, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Short.class, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Byte.class, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Boolean.class, new DisplayerTableCellRenderer(new BooleanDisplayer()));
        setDefaultRenderer(SimMoney.class, new DisplayerTableCellRenderer(new SimMoneyDisplayer(), JLabel.RIGHT));
        setDefaultRenderer(Integer.TYPE, new DisplayerTableCellRenderer(new IntegerDisplayer()));
        setDefaultRenderer(Double.TYPE, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Float.TYPE, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Long.TYPE, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Short.TYPE, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Byte.TYPE, new DisplayerTableCellRenderer(new NumberDisplayer()));
        setDefaultRenderer(Boolean.TYPE, new DisplayerTableCellRenderer(new BooleanDisplayer()));
        setDefaultRenderer(String.class, new DisplayerTableCellRenderer(new ObjectDisplayer()));
    }

    /****************************************************************************************************
     * Assign default table editors for all the basic data types.
     ***************************************************************************************************/
    private void initializeDefaultEditors() {
        setDefaultEditor(Quantity.class, new DisplayerTableCellEditor(new QuantityTableEditor()));
        setDefaultEditor(Number.class, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(BigDecimal.class, new DisplayerTableCellEditor(new BigDecimalTableEditor()));
        setDefaultEditor(Integer.class, new DisplayerTableCellEditor(new IntegerTableEditor()));
        setDefaultEditor(Double.class, new DisplayerTableCellEditor(new DoubleTableEditor()));
        setDefaultEditor(Float.class, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Long.class, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Short.class, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Byte.class, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Boolean.class, new DisplayerTableCellEditor(new BooleanTableEditor()));
        setDefaultEditor(Integer.TYPE, new DisplayerTableCellEditor(new IntegerTableEditor()));
        setDefaultEditor(Double.TYPE, new DisplayerTableCellEditor(new DoubleTableEditor()));
        setDefaultEditor(Float.TYPE, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Long.TYPE, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Short.TYPE, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Byte.TYPE, new DisplayerTableCellEditor(new NumberTableEditor()));
        setDefaultEditor(Boolean.TYPE, new DisplayerTableCellEditor(new BooleanTableEditor()));
        setDefaultEditor(String.class, new DisplayerTableCellEditor(new StringTableEditor()));
    }

    /****************************************************************************************************
     * Updates the configuration settings, column attributes, etc.
     ***************************************************************************************************/
    private void resetTableConfiguration() {
        RTableConfigData data = RTableConfigRepository.getTableConfigurationData(identifier);

        validateFontSize(data.getFontSizeSetting());
        validateGridLines(data.getGridlinesSetting());

        if (data.getColumnConfigData().isEmpty()) {
            initializeColumns(originalAttributes);
            initializeColumnSettings(originalAttributes);
            return;
        }

        // Ensure that all original attributes have column information
        List<SimTableAttribute> tempAttributeList = new ArrayList<>();
        for (SimTableAttribute attribute : originalAttributes) {
            RTableConfigColumnData foundColumnData = null;

            for (RTableConfigColumnData loopColumnData : data.getColumnConfigData()) {
                if (loopColumnData.getTitle().equals(attribute.getTitle())) {
                    foundColumnData = loopColumnData;
                    break;
                }
            }
            if (foundColumnData == null) {
                foundColumnData = new RTableConfigColumnData(attribute.getTitle());
                foundColumnData.setVisible(true);

                List<RTableConfigColumnData> updatedData = new ArrayList<>();
                updatedData.addAll(data.getColumnConfigData());
                updatedData.add(foundColumnData);

                data.setColumnConfigData(updatedData);
            }
            if (foundColumnData.isVisible()) {
                tempAttributeList.add(attribute);
            }
        }
        // Ensure the information is in the correct order by resorting based on original
        List<SimTableAttribute> finalAttributeList = new ArrayList<>();
        for (RTableConfigColumnData loopColumnData : data.getColumnConfigData()) {
            for (SimTableAttribute attribute : tempAttributeList) {
                if (loopColumnData.getTitle().equals(attribute.getTitle())) {
                    finalAttributeList.add(attribute);
                    break;
                }
            }
        }
        initializeColumns(finalAttributeList);
        initializeColumnSettings(finalAttributeList);
    }

    /****************************************************************************************************
     * Programmatically sets the columnTitle to visible or not
     ***************************************************************************************************/
    public void setColumnVisible(String columnTitle, boolean isVisible) {
        RTableConfigData tableConfigData = RTableConfigRepository.getTableConfigurationData(identifier);
        List<RTableConfigColumnData> columnConfigData = tableConfigData.getColumnConfigData();
        if (columnConfigData.isEmpty()) {
            columnConfigData.add(new RTableConfigColumnData(columnTitle));
        }
        for (RTableConfigColumnData loopColumnData : columnConfigData) {
            if (loopColumnData.getTitle().equals(columnTitle)) {
                if (loopColumnData.isVisible() != isVisible) {
                    loopColumnData.setVisible(isVisible);
                    resetTableConfiguration();
                }
            }
        }
    }

    /****************************************************************************************************
     * Initializes the table from sim attributes. This will build SimTableColumns, custom renderers and
     * custom editors for each attribute.
     ***************************************************************************************************/
    private void initializeColumns(List<SimTableAttribute> attributes) {
        List<SimTableColumn> columns = new ArrayList<>(attributes.size());
        for (SimTableAttribute attribute : attributes) {
            SimTableColumn column = new SimTableColumn(attribute.getTitle(), attribute.getAttribute());
            column.setEditable(attribute.isEditable());
            column.setDisplayer(attribute.getDisplayer());

            columns.add(column);
        }
        setColumns(columns);
    }

    /****************************************************************************************************
     * Helper method to loop through the table attributes and set the appropriate settings on each column
     * that is matched with the attribute. Settings include displayers, editors and width.
     * @param attributes All the table attributes.
     ***************************************************************************************************/
    private void initializeColumnSettings(List<SimTableAttribute> attributes) {
        for (SimTableAttribute attribute : attributes) {
            String title = attribute.getAttribute();
            if (attribute.getRenderer() != null) {
                setColumnRenderer(title, attribute.getRenderer());
            } else if (attribute.getDisplayer() != null) {
                setColumnRenderer(title, new DisplayerTableCellRenderer(attribute.getDisplayer()));
            }
            if (attribute.getEditor() != null) {
                setColumnEditor(title, new DisplayerTableCellEditor(attribute.getEditor()));
            }
            setColumnSize(title, attribute.getMaxWidth(), attribute.getPreferredWidth(), attribute.getMaxWidth());
        }
    }

    /****************************************************************************************************
     * Resets the sorting of the table after sort configuration takes place.
     ***************************************************************************************************/
    private void resetSortConfiguration() {
        tableModel.clearSortCriteria();

        List<SimTableSortCriteria> defaultList = new ArrayList<>();
        for (SimTableSortAttribute sortAttribute : originalSortAttributes) {
            int column = tableModel.findColumn(sortAttribute.getAttribute());
            if (column < 0) {
                UILog.error(getClass(), UIMessageText.INVALID_COLUMN_SORT, sortAttribute.getAttribute());
                continue;
            }
            defaultList.add(new SimTableSortCriteria(column, sortAttribute.isAscending()));
        }
        tableModel.setDefaultSortCriteria(defaultList.toArray(new SimTableSortCriteria[defaultList.size()]));

        RTableConfigData data = RTableConfigRepository.getTableConfigurationData(identifier);
        List<SimTableSortCriteria> configList = new ArrayList<>();
        for (RTableConfigColumnData columnData : data.getSortColumnList()) {
            int column = convertColumnIndexToView(tableModel.findColumnByTitle(columnData.getTitle()));
            if (column < 0) {
                UILog.error(getClass(), UIMessageText.INVALID_COLUMN_SORT, columnData.getTitle());
                continue;
            }
            configList.add(new SimTableSortCriteria(column, columnData.isAscending()));
        }
        if (configList.isEmpty()) {
            tableModel.setConfigSortCriteria(null);
        } else {
            tableModel.setConfigSortCriteria(configList.toArray(new SimTableSortCriteria[configList.size()]));
        }
        refreshHeader();
        if (isEmpty()) {
            return;
        }
        tableModel.sort();
    }

    /****************************************************************************************************
     * Assigns a list of table columns to the table. These table columns MUST be SimTableColumns.
     * <p>
     * @param columns A list of SimTableColumn objects.
     ***************************************************************************************************/
    public void setColumns(List<SimTableColumn> columns) {
        if (columns != null) {
            tableModel.setColumns(columns);
        }
    }

    /****************************************************************************************************
     * Assigns a column table cell renderer to a particular column based on attribute.
     * <p>
     * @param attribute The column attribute.
     * @param renderer The column renderer.
     ***************************************************************************************************/
    public void setColumnRenderer(String attribute, TableCellRenderer renderer) {
        getColumn(tableModel.findColumn(attribute)).setCellRenderer(renderer);
    }

    /****************************************************************************************************
     * Assigns a column table cell editor to a particular column based on attribute.
     * <p>
     * @param attribute The column attribute.
     * @param editor The column editor.
     ***************************************************************************************************/
    public void setColumnEditor(String attribute, TableCellEditor editor) {
        getColumn(tableModel.findColumn(attribute)).setCellEditor(editor);
        validateRowHeight(editor);
    }

    /****************************************************************************************************
     * Assigns an error task to be performed when an error occurs within a table cell editor action. This
     * task will be executed using SwingUtility when an exception is caught in the table cell editor. The
     * default error task just returns the focus to the editor.
     * <p>
     * @param attribute The column attribute.
     * @param task A SimTableErrorTask to execute.
     ***************************************************************************************************/
    public void setColumnEditorErrorTask(String attribute, SimTableErrorTask task) {
        int column = tableModel.findColumn(attribute);
        if (column == -1) {
            UILog.info(getClass(), UIMessageText.UNABLE_TO_FIND_COLUMN, attribute);
            return;
        }
        TableCellEditor editor = getColumn(column).getCellEditor();

        if (editor instanceof DisplayerTableCellEditor) {
            ((DisplayerTableCellEditor) editor).setErrorTask(task);
        }
    }

    /****************************************************************************************************
     * Retrieves the table editor for a given class type.
     * <p>
     * @param classType The Class that the editor edits.
     * @param return The table editor.
     ***************************************************************************************************/
    public SimTableEditor getTableEditor(Class<?> classType) {
        TableCellEditor editor = getDefaultEditor(classType);
        if (editor instanceof DisplayerTableCellEditor) {
            return ((DisplayerTableCellEditor) editor).getTableEditor();
        }
        return null;
    }

    /****************************************************************************************************
     * Allows single table row selection only.
     ***************************************************************************************************/
    public void setSingleRowSelectionMode() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /****************************************************************************************************
     * Allows multiple table row selection. This is the default setting.
     ***************************************************************************************************/
    public void setMultipleRowSelectionMode() {
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    /****************************************************************************************************
     * Returns true is multiple row selection mode is active.
     ***************************************************************************************************/
    public boolean isMultipleRowSelectionMode() {
        return getSelectionModel().getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    }

    /****************************************************************************************************
     * Sets the entire table sortable or not. If this value is true, clicking on column headers will sort
     * the table by the data within that column.
     * @param sortable True if the table should be sortable, false otherwise.
     ***************************************************************************************************/
    public void setSortingEnabled(boolean sortable) {
        for (int index = 0; index < tableModel.getColumnCount(); index++) {
            tableModel.getColumn(index).setSortable(sortable);
        }
    }

    /****************************************************************************************************
     * Sets the column at index sortable or not sortable (true/false).
     * @param index The index of the column to assign the value to.
     * @param sortable True if the column should allow sorting, false otherwise.
     ***************************************************************************************************/
    public void setColumnSortable(int index, boolean sortable) {
        tableModel.getColumn(index).setSortable(sortable);
    }

    /****************************************************************************************************
     * Assigns a column size (in pixels) to the column associated with the attribute.
     * <p>
     * @param attribute The attribute that identifies the column.
     * @param lockedWidth The minimum, preferred and maximum width will be set to this value.
     ***************************************************************************************************/
    public void setColumnSize(String attribute, int lockedWidth) {
        setColumnSize(attribute, lockedWidth, lockedWidth, lockedWidth);
    }

    /****************************************************************************************************
     * Assigns a column size (in pixels) to the column associated with the attribute.
     * <p>
     * @param attribute The attribute that identifiers the column.
     * @param minWidth The allowed minimum width of the column (in pixels).
     * @param preferredWidth The preferred width of the column (in pixels).
     * @param maxWidth The maximum width of the column (in pixels).
     ***************************************************************************************************/
    public void setColumnSize(String attribute, int minWidth, int prefWidth, int maxWidth) {
        int columnIndex = tableModel.findColumn(attribute);
        if (columnIndex < 0) {
            return;
        }
        TableColumn column = getColumn(tableModel.findColumn(attribute));

        SimTableHeaderRenderer headerRenderer = (SimTableHeaderRenderer) getTableHeader().getDefaultRenderer();
        if (minWidth == 0) {
            FontMetrics metrics = headerRenderer.getFontMetrics(headerRenderer.getFont());
            String headerText = Translator.getText(column.getHeaderValue().toString());
            minWidth = StringUtility.longestSize(metrics, headerText, "\\|") + 30;
        }
        if (minWidth != -1) {
            column.setMinWidth(minWidth);
        }
        if (maxWidth == 0 && minWidth > -1) {
            column.setMaxWidth(minWidth);
        } else if (maxWidth > 0) {
            column.setMaxWidth(maxWidth);
        }
        if (prefWidth > 0) {
            column.setPreferredWidth(prefWidth);
        }
    }

    /****************************************************************************************************
     * Helper method to directly retrieve a table column for an index.
     ***************************************************************************************************/
    private TableColumn getColumn(int index) {
        return getColumnModel().getColumn(index);
    }

    /****************************************************************************************************
     * Assigns the header area background color.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setTableHeaderBackground(Color color) {
        if (color != null) {
            tableHeaderBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the header area background color.
     * <p>
     * @return The header area background color.
     ***************************************************************************************************/
    public Color getTableHeaderBackground() {
        return tableHeaderBackground;
    }

    /****************************************************************************************************
     * Assigns the header area foreground color.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    public void setTableHeaderForeground(Color color) {
        if (color != null) {
            tableHeaderForeground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the header area foreground color.
     * <p>
     * @return The header area foreground color.
     ***************************************************************************************************/
    public Color getTableHeaderForeground() {
        return tableHeaderForeground;
    }

    /****************************************************************************************************
     * Assigns the header area font.
     * <p>
     * @param font The font to assign
     ***************************************************************************************************/
    public void setTableHeaderFont(Font font) {
        if (font != null) {
            tableHeaderFont = font;
        }
    }

    /****************************************************************************************************
     * Retrieves the header area font.
     * <p>
     * @return The header area font.
     ***************************************************************************************************/
    public Font getTableHeaderFont() {
        return tableHeaderFont;
    }

    /****************************************************************************************************
     * Assigns the row background color.
     * <p>
     * @param color The row background color.
     ***************************************************************************************************/
    public void setRowBackground(Color color) {
        if (color != null) {
            defaultRowBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the row background color.
     * <p>
     * @return The row background color.
     ***************************************************************************************************/
    public Color getRowBackground() {
        return defaultRowBackground;
    }

    /****************************************************************************************************
     * Assigns the row foreground color.
     * <p>
     * @param color The row foreground color.
     ***************************************************************************************************/
    public void setRowForeground(Color color) {
        if (color != null) {
            defaultRowForeground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the row foreground color.
     * <p>
     * @return The row foreground color.
     ***************************************************************************************************/
    public Color getRowForeground() {
        return defaultRowForeground;
    }

    /****************************************************************************************************
     * Assigns the row alternate background color. This color is alternated with the main color every
     * other row within the table.
     * <p>
     * @param color The row alternate background color.
     ***************************************************************************************************/
    public void setAlternateRowBackground(Color color) {
        if (color != null) {
            alternateRowBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the row alternate background color. This color is alternated with the main color every
     * other row within the table.
     * <p>
     * @return The row alternate background color.
     ***************************************************************************************************/
    public Color getAlternateRowBackground() {
        return alternateRowBackground;
    }

    /****************************************************************************************************
     * Add a data object to the table, creating a display row from it.
     * @param value The data object.
     ***************************************************************************************************/
    public void addRow(Object value) {
        if (value != null) {
            tableModel.addRow(value);
        }
    }

    /****************************************************************************************************
     * Add a collection of data objects to the table, creating a display row for each object.
     * @param values The data objects to add to the table.
     ***************************************************************************************************/
    public void addRows(Collection values) {
        if (values != null) {
            tableModel.addRows(values);
        }
    }

    /****************************************************************************************************
     * Sets the table to the collection of data objects. Any old rows will be removed as a result of this
     * call. The table is re-sorted at the end of this method call.
     * @param values The data objects to display in the table.
     ***************************************************************************************************/
    public void setRows(Collection values) {
        if (values == null) {
            values = Collections.emptyList();
        }
        tableModel.setRows(values);
    }

    /****************************************************************************************************
     * Inserts a data object into the table, creating a new row and inserting it at the index (or row
     * number).
     * @param index The row index to insert the data object at.
     * @param value The data object to insert.
     * @return True if the row is inserted, false if not.
     ***************************************************************************************************/
    public boolean insertRow(int index, Object value) {
        if (value == null) {
            return false;
        }
        return tableModel.insertRow(index, value);
    }

    /****************************************************************************************************
     * Updates the row containing the value with the new information in the value. The table cycles
     * through the rows to find the row containing the object (based on how equals() is implemented) and
     * then replaced the object in the table with the one passed in. This triggered the table to
     * redisplay the row in question.
     * @param value The data object to update.
     * @return True if the row is updated, false if not.
     ***************************************************************************************************/
    public boolean updateRow(Object value) {
        if (value == null) {
            return false;
        }
        return tableModel.updateRow(value);
    }

    /****************************************************************************************************
     * Removes the specified data object (based on how equals() is implemented) and corresponding row
     * from the model.
     * @param value The data object to remove.
     * @return True if the row is updated, false if not.
     ***************************************************************************************************/
    public boolean removeRow(Object value) {
        if (value == null) {
            return false;
        }
        return tableModel.removeRow(value);
    }

    /****************************************************************************************************
     * De-selects the last row that has been selected.
     ***************************************************************************************************/
    public void removeLastRowSelection() {
        ListSelectionModel model = getSelectionModel();
        if (model != null) {
            int row = model.getLeadSelectionIndex();
            removeRowSelectionInterval(row, row);
        }
    }

    /****************************************************************************************************
     * Returns the number of rows in the table.
     * <p>
     * @return The number of rows in the table.
     ***************************************************************************************************/
    public int getRowCount() {
        return tableModel.getRowCount();
    }

    /****************************************************************************************************
     * Returns all the data within the table in the sequence that the rows are displayed. Modifying this
     * list will not have an affect on the table unless the data is set back on the table (usually by
     * calling setRows()).
     * @return A List containing all the data objects in the table.
     ***************************************************************************************************/
    public List getAllRowData() {
        return tableModel.getAllRows();
    }

    /****************************************************************************************************
     * Returns all selected row data objects as a list.
     * @return A List containing all the selected data objects in the table.
     ***************************************************************************************************/
    public List getAllSelectedRowData() {
        return tableModel.getRows(getSelectedRows());
    }

    /****************************************************************************************************
     * Returns the row data object for the given index.
     * @param rowIndex The row index.
     * @return The data object.
     ***************************************************************************************************/
    public Object getRowData(int rowIndex) {
        return tableModel.getRow(rowIndex);
    }

    /****************************************************************************************************
     * Returns all row data objects for the row numbers.
     * @param rowNumbers An array of row numbers to find data for.
     * @return A List containing data objects.
     ***************************************************************************************************/
    public List getRowData(int[] rowNumbers) {
        return tableModel.getRows(rowNumbers);
    }

    /****************************************************************************************************
     * Retrieves the selected data object. If more than one row is selected, this is the data object of
     * the first selected row.
     * @return The selected data objects.
     ***************************************************************************************************/
    public Object getSelectedRowData() {
        return tableModel.getRow(getSelectedRow());
    }

    /****************************************************************************************************
     * Returns true if the table contains the data object, false otherwise.
     * @param value The data object.
     * @return True if the table contains the object.
     ***************************************************************************************************/
    public boolean containsRow(Object value) {
        if (value == null) {
            return false;
        }
        return tableModel.containsRow(value);
    }

    /****************************************************************************************************
     * Returns the index of the row containing the data object if it exists in the table, -1 otherwise.
     * @param value The data object.
     * @return The row index if the data exists in the table, -1 otherwise.
     ***************************************************************************************************/
    public int getRowIndex(Object value) {
        if (value == null) {
            return -1;
        }
        return tableModel.getRowIndex(value);
    }

    /****************************************************************************************************
     * Retrieves the index of the last row of the table.
     * <p>
     * @return The index of the last row of the table.
     ***************************************************************************************************/
    public int getLastRowIndex() {
        return getRowCount() - 1;
    }

    /****************************************************************************************************
     * Clears all the data objects and rows from the table.
     ***************************************************************************************************/
    public void clearRows() {
        tableModel.clearRows();
    }

    /****************************************************************************************************
     * Return true if the table is empty, false otherwise.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    /****************************************************************************************************
     * Determines if the entire table is editable. If this returns false, then no cells in the table will
     * be editable. If true, the column and cell must still be editable for editing to take place.
     * @return True if the table is editable at the table level, false otherwise.
     ***************************************************************************************************/
    public boolean isTableEditable() {
        return tableModel.isTableEditable();
    }

    /****************************************************************************************************
     * Sets the table editable attribute.
     * @param isEditable True if the table should be editable at the table level, false otherwise.
     ***************************************************************************************************/
    public void setTableEditable(boolean isEditable) {
        tableModel.setTableEditable(isEditable);
    }

    /****************************************************************************************************
     * Sets the table configuration enabled or disabled. If enabled, the table may be configured with a
     * popup dialog.
     * <p>
     * @param enabled True if table configuration should be enabled, false otherwise.
     ***************************************************************************************************/
    public void setTableConfigurationEnabled(boolean enabled) {
        isConfigurationEnabled = enabled;
    }

    /****************************************************************************************************
     * Prepares a renderer to display data. See method on JTable(). This only adds the logic to deal with
     * alternate row coloring.
     ***************************************************************************************************/
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
        Component component = super.prepareRenderer(renderer, rowIndex, columnIndex);

        if (isCellSelected(rowIndex, columnIndex)) {
            component.setBackground(UIManager.getColor(UIThemeName.TABLE_SELECTION_BACKGROUND));
            component.setForeground(UIManager.getColor(UIThemeName.TABLE_SELECTION_FOREGROUND));
        } else if (rowIndex % 2 == 0) {
            component.setBackground(defaultRowBackground);
            component.setForeground(defaultRowForeground);
        } else {
            component.setBackground(alternateRowBackground);
            component.setForeground(defaultRowForeground);
        }
        return component;
    }

    /****************************************************************************************************
     * Validates the height of the row versus the height of the editor. Height is always 2 extra for some
     * additional space.
     * @param editor The editor to validate height for.
     ***************************************************************************************************/
    private void validateRowHeight(TableCellEditor editor) {
        int height = 16;
        if (editor instanceof JComponent) {
            height = (int) ((JComponent) editor).getPreferredSize().getHeight();
        }
        if (editor instanceof DisplayerTableCellEditor) {
            height = ((DisplayerTableCellEditor) editor).getEditorHeight();
        }
        if (height > getRowHeight()) {
            setRowHeight(height + 2);
        }
    }

    /****************************************************************************************************
     * Retrieves the selected row number.
     * @return The selected row number, or -1 if no row is selected.
     ***************************************************************************************************/
    public int getSelectedRow() {
        if (getRowCount() == 0) {
            return -1;
        }
        return super.getSelectedRow();
    }

    /****************************************************************************************************
     * Sets a single row as the row selection by row number (which begins at zero).
     * <p>
     * @param row The row number to select.
     ***************************************************************************************************/
    public void setRowSelection(int row) {
        if (row > -1 && row < getRowCount()) {
            setRowSelectionInterval(row, row);
        }
    }

    /****************************************************************************************************
     * Selects a row based on an object. It searches each row and checks the object against
     * the data stored in the row. If a match is found, the row is selected and the method stops.
     * <p>
     * @param value The object to seek in data.
     ***************************************************************************************************/
    public void setRowSelection(Object value) {
        if (value == null) {
            return;
        }
        setRowSelection(tableModel.getRowIndex(value));
    }

    /****************************************************************************************************
     * Find column index for attribute.
     * <p>
     * @param attribute The attribute that defines the column.
     * @return The column index for that attribute.
     ***************************************************************************************************/
    public int findColumnIndex(String attribute) {
        return tableModel.findColumn(attribute);
    }

    /****************************************************************************************************
     * Retrieves the first row where the cell defined by row and column contains a NULL value.
     * @param attribute The attribute that identifies the column.
     * @return The row number or -1 if all rows contain data.
     ***************************************************************************************************/
    public int getFirstNullValueRow(String attribute) {
        return getFirstNullValueRow(findColumnIndex(attribute));
    }

    /****************************************************************************************************
     * Retrieves the first row where the cell defined by row and column contains a NULL value.
     * @param column The column index to search through.
     * @return The row number or -1 if all rows contain data.
     ***************************************************************************************************/
    public int getFirstNullValueRow(int column) {
        if (column > -1 && column < tableModel.getColumnCount()) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (tableModel.getValueAt(row, column) == null) {
                    if (tableModel.isCellEditable(row, column)) {
                        return row;
                    }
                }
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Stops editing if a table cell is currently being edited.
     ***************************************************************************************************/
    public void stopEditing() {
        if (cellEditor != null) {
            cellEditor.cancelCellEditing();
        }
    }

    /****************************************************************************************************
     * Invoked when editing is finished. In the superclass, the changes are saved and the editor is
     * discarded. In SimTable, we have taken over the moment when changes to the cell are assigned to the
     * underlying data model, so the superclass method is overridden to do the same thing as editing
     * cancelled.
     * <p>
     * Application code will not use these methods explicitly, they are used internally by JTable.
     ***************************************************************************************************/
    public void editingStopped(ChangeEvent changeEvent) {
        editingCanceled(changeEvent);
    }

    /****************************************************************************************************
     * Programmatically starts editing the cell at row and column, if the cell is editable. To prevent
     * the JTable from editing a particular table, column or cell value, return false from the
     * isCellEditable method in the TableModel interface.
     * <p>
     * It seems that the JTable API advertises that the following call happens automatically, but,
     * looking through the source, this doesn't seem to be the case, so we will add it here.
     * <p>
     * @param row The row to be edited.
     * @param column The column to be edited.
     * @param eventObject The event object to pass into shouldSelectCell.
     * @return False if the cell cannot be edited.
     * @throws IllegalArgumentException If row or column is not in the valid range.
     ***************************************************************************************************/
    public boolean editCellAt(int row, int column, EventObject eventObject) {
        boolean returnValue = super.editCellAt(row, column, eventObject);
        if (returnValue && cellEditor != null) {
            cellEditor.shouldSelectCell(eventObject);
        }
        return returnValue;
    }

    /****************************************************************************************************
     * Starts the table editing the cell at the given column in the last row of the table.
     * <p>
     * @param attribute The attribute of the column to edit.
     ***************************************************************************************************/
    public void editCellInLastRow(String attribute) {
        editCellInRow(attribute, getRowCount() - 1);
    }

    /****************************************************************************************************
     * Starts the table editing the cell at the given column in the selected row of the table.
     * <p>
     * @param attribute The column "attribute".
     ***************************************************************************************************/
    public void editCellInSelectedRow(String attribute) {
        editCellInRow(attribute, getSelectedRow());
    }

    /****************************************************************************************************
     * Starts the table editing the cell at the given column and row of the table. This will be triggered
     * through SwingUtilities so it will actually execute on a separate thread.
     * <p>
     * @param attribute The column "attribute".
     * @param row The row number.
     ***************************************************************************************************/
    public void editCellInRow(final String attribute, final int row) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int column = convertColumnIndexToView(tableModel.findColumn(attribute));

                if (row >= 0 && column >= 0 && isCellEditable(row, column)) {
                    changeSelection(row, column, false, false);
                    editCellAt(row, column);

                    Component component = getEditorComponent();

                    if (component != null) {
                        component.requestFocusInWindow();
                    }
                }
            }
        });
    }

    /****************************************************************************************************
     * Helper method to create the default table header object.
     * @return A table header object.
     ***************************************************************************************************/
    protected JTableHeader createDefaultTableHeader() {
        JTableHeader header = super.createDefaultTableHeader();
        header.setDefaultRenderer(new SimTableHeaderRenderer());
        header.addMouseListener(createHeaderMouseListener());
        return header;
    }

    /****************************************************************************************************
     * Helper method to create header mouse listener. This listens to table clicks in the table header to
     * determine if a sort should take place.
     ***************************************************************************************************/
    private MouseListener createHeaderMouseListener() {
        return new MouseAdapter() {
            public void mouseEntered(MouseEvent event) {
            	stopEditing();
            }
            public void mouseClicked(MouseEvent e) {
                if (isConfigurationEnabled && SwingUtilities.isRightMouseButton(e)) {
                    displayPopupMenu(e.getComponent(), e.getPoint());
                    return;
                }
                int index = convertColumnIndexToModel(getColumnModel().getColumnIndexAtX(e.getX()));
                if (tableModel.isSortable(index)) {
                    tableModel.sort(index);
                }
            }
        };
    }

    /****************************************************************************************************
     * Assigns an action to be sent when a row is selected through key navigation or a single click. The
     * command will be sent to the listener.
     * <p>
     * @param listener The listener to assign to the table to receive single click actions.
     * @param command The command to send in the action event.
     ***************************************************************************************************/
    public void registerSingleClickAction(REventListener listener, String command) {
        singleActionListener = listener;
        singleActionCommand = command;
    }

    /****************************************************************************************************
     * Removes the single click action event from the table. No action will be sent for row selections
     * after this method is called.
     ***************************************************************************************************/
    public void removeSingleClickAction() {
        singleActionListener = null;
        singleActionCommand = null;
    }

    /****************************************************************************************************
     * Assigns an action to be sent when a row is double-clicked or a row is selected and the enter key
     * is pressed. The command will be sent to the listener inside an RActionEvent.
     * <p>
     * @param listener The listener to assign to the table to receive double-click actions.
     * @param command The command to send in the action event.
     ***************************************************************************************************/
    public void registerDoubleClickAction(REventListener listener, String command) {
        doubleActionListener = listener;
        doubleActionCommand = command;
    }

    /****************************************************************************************************
     * Removes the double-click action event from the table. No action will be sent when double-clicking
     * on a row.
     ***************************************************************************************************/
    public void removeDoubleClickAction() {
        doubleActionListener = null;
        doubleActionCommand = null;
    }

    /****************************************************************************************************
     * Sorts the table with the current sort settings.
     ***************************************************************************************************/
    public void sort() {
        tableModel.sort();
        refreshHeader();
    }

    /****************************************************************************************************
     * Sorts the table based on a an array of sort criteria (column and direction).
     * @param sortCriteria An array of sort criteria.
     ***************************************************************************************************/
    public void sort(List<SimTableSortAttribute> sortAttributeList) {
        List<SimTableSortCriteria> criteriaList = new ArrayList<>();
        for (SimTableSortAttribute sortAttribute : sortAttributeList) {
            int column = tableModel.findColumn(sortAttribute.getAttribute());
            if (column < 0) {
                UILog.error(getClass(), UIMessageText.INVALID_COLUMN_SORT, sortAttribute.getAttribute());
                continue;
            }
            criteriaList.add(new SimTableSortCriteria(column, sortAttribute.isAscending()));
        }
        tableModel.sort(criteriaList.toArray(new SimTableSortCriteria[criteriaList.size()]));
    }

    /****************************************************************************************************
     * Refreshes the table rows from the current data in the table.
     ***************************************************************************************************/
    public void refreshTable() {
        tableModel.fireTableDataChanged();
    }

    /****************************************************************************************************
     * Helper method to refresh the table header.
     ***************************************************************************************************/
    private void refreshHeader() {
        getTableHeader().resizeAndRepaint();
    }

    /****************************************************************************************************
     * Overrides the JTable list selection event and notifies single action listener of the event. This
     * method strips out adjusting values.
     ***************************************************************************************************/
    public void valueChanged(ListSelectionEvent event) {
        super.valueChanged(event);

        if (!event.getValueIsAdjusting()) {
            doSingleClickAction();
        }
    }

    /****************************************************************************************************
     * Implements the key listener interface 'key pressed' method. If the table owns focus and is
     * editable, then make sure the keystroke is valid for the table cell editor before allowing the
     * keystroke to activate the table cell.
     ***************************************************************************************************/
    public void keyTyped(KeyEvent keyEvent) {
    }

    /****************************************************************************************************
     * Empty implementation of the key listener interface method.
     ***************************************************************************************************/
    public void keyReleased(KeyEvent keyEvent) {
    }

    /****************************************************************************************************
     * Implements the key listener interface 'key pressed' method. This method checks to see if the tab
     * key was pressed, and if so, it transfers the focus to the next focusable component. If ALT-HotKey
     * is pressed, the appropriate column is sorted. If ENTER is pressed, then the table row selected
     * action is triggered.
     * <p>
     * @param event Details about the key event that occurred.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent keyEvent) {
        if (isFocusOwner()) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_TAB:
                    if (tableModel.isTableEditable()) {
                        break;
                    }
                    keyEvent.consume();
                    if (keyEvent.isShiftDown()) {
                        doShiftTabPressed();
                    } else {
                        doTabPressed();
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    keyEvent.consume();
                    doDoubleClickAction();
                    break;
                default:
                    break;
            }
        }
    }

    /****************************************************************************************************
     * Method is executed when the shift-tab key is pressed within a RDisplayTable. It transfers the
     * focus to the previous component (if one is assigned)..
     ***************************************************************************************************/
    private void doShiftTabPressed() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(this);
    }

    /****************************************************************************************************
     * Method is executed when the tab key is pressed within a RDisplayTable. It transfers the focus to
     * the next component.
     ***************************************************************************************************/
    private void doTabPressed() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(this);
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mouseExited(MouseEvent mouseEvent) {
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mousePressed(MouseEvent mouseEvent) {
    }

    /****************************************************************************************************
     * Empty implementation of the mouse listener interface method.
     ***************************************************************************************************/
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    /****************************************************************************************************
     * Implementation of the mouse listener interface method. It listens for double-clicking on rows or
     * right-clicking on a column and determines if an action should be fired or if the table config
     * popup menu should be displayed.
     ***************************************************************************************************/
    public void mouseClicked(MouseEvent mouseEvent) {
        if (lastMouseEvent != null) {
            long oldEvent = lastMouseEvent.getWhen();
            long newEvent = mouseEvent.getWhen();
            if (oldEvent == newEvent) {
                return;
            }
        }
        Object object = mouseEvent.getComponent();

        if (isConfigurationEnabled && SwingUtilities.isRightMouseButton(mouseEvent)) {
            displayPopupMenu(object, mouseEvent.getPoint());
        } else if (object instanceof JTable && mouseEvent.getClickCount() == 2) {
            doDoubleClickAction();
        }
        lastMouseEvent = mouseEvent;
    }

    /****************************************************************************************************
     * Notifies the single-click action listener if a row has been selected. It includes the row clicked
     * on as the event data of the RActionEvent.
     ***************************************************************************************************/
    private void doSingleClickAction() {
        if (singleActionListener != null && singleActionCommand != null) {
            ListSelectionModel model = getSelectionModel();
            if (model != null) {
                Integer row = model.getLeadSelectionIndex();
                singleActionListener.performActionEvent(new RActionEvent(this, singleActionCommand, row));
            } else {
                singleActionListener.performActionEvent(new RActionEvent(this, singleActionCommand, null));
            }
        }
    }

    /****************************************************************************************************
     * Notifies a double-click action listener if a row has been double-clicked.
     ***************************************************************************************************/
    private void doDoubleClickAction() {
        if (doubleActionListener == null || doubleActionCommand == null) {
            return;
        }
        if (getSelectedRowCount() > 0) {
            doubleActionListener.performActionEvent(new RActionEvent(this, doubleActionCommand));
        }
    }

    /****************************************************************************************************
     * Pops up the configuration popup menu.
     * @param component The component that was clicked on.
     * @param point The point that the click took place.
     ***************************************************************************************************/
    protected void displayPopupMenu(Object component, Point point) {
        if (popupMenu == null) {
            popupMenu = new RTableConfigPopupMenu(this);
        }
        popupMenu.show(this, point.x, point.y);
    }

    /****************************************************************************************************
     * Implements the table configuration listener method to receive table configuration events.
     * @param command The configuration command.
     ***************************************************************************************************/
    public void configurationPerformed(String command) {
        if (command.equals(RTableConfigConstants.SMALLEST_LABEL)) {
            validateFontSize(RTableConfigConstants.SMALLEST);
        } else if (command.equals(RTableConfigConstants.SMALLER_LABEL)) {
            validateFontSize(RTableConfigConstants.SMALLER);
        } else if (command.equals(RTableConfigConstants.STANDARD_LABEL)) {
            validateFontSize(RTableConfigConstants.STANDARD);
        } else if (command.equals(RTableConfigConstants.LARGE_LABEL)) {
            validateFontSize(RTableConfigConstants.LARGE);
        } else if (command.equals(RTableConfigConstants.LARGER_LABEL)) {
            validateFontSize(RTableConfigConstants.LARGER);
        } else if (command.equals(RTableConfigConstants.LARGEST_LABEL)) {
            validateFontSize(RTableConfigConstants.LARGEST);
        } else if (command.equals(RTableConfigConstants.ALL_LINES_LABEL)) {
            validateGridLines(RTableConfigConstants.ALL_GRIDLINES);
        } else if (command.equals(RTableConfigConstants.NONE_LINES_LABEL)) {
            validateGridLines(RTableConfigConstants.NO_GRIDLINES);
        } else if (command.equals(RTableConfigConstants.COLUMN_LINES_LABEL)) {
            validateGridLines(RTableConfigConstants.COL_GRIDLINES);
        } else if (command.equals(RTableConfigConstants.ROW_LINES_LABEL)) {
            validateGridLines(RTableConfigConstants.ROW_GRIDLINES);
        } else if (command.equals(RTableConfigConstants.TABLE_CONFIG_LABEL)) {
            validateTableConfiguration();
        }
    }

    /****************************************************************************************************
     * Helper method to assign the size content information in the table.
     ***************************************************************************************************/
    private void validateFontSize(int sizeContent) {
        Font currentFont = getFont();
        switch (sizeContent) {
            case RTableConfigConstants.SMALLEST:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 8f));
                break;
            case RTableConfigConstants.SMALLER:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 9f));
                break;
            case RTableConfigConstants.LARGE:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 11f));
                break;
            case RTableConfigConstants.LARGER:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 12f));
                break;
            case RTableConfigConstants.LARGEST:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 14f));
                break;
            default:
                setFont(currentFont.deriveFont(currentFont.getStyle(), 10f));
                break;
        }
    }

    /****************************************************************************************************
     * Helper method to assign the grid line setting in the table.
     ***************************************************************************************************/
    private void validateGridLines(int setting) {
        switch (setting) {
            case RTableConfigConstants.NO_GRIDLINES:
                setShowVerticalLines(false);
                setShowHorizontalLines(false);
                setIntercellSpacing(new Dimension(0, 0));
                break;
            case RTableConfigConstants.COL_GRIDLINES:
                setShowVerticalLines(true);
                setShowHorizontalLines(false);
                setIntercellSpacing(new Dimension(2, 0));
                break;
            case RTableConfigConstants.ROW_GRIDLINES:
                setShowVerticalLines(false);
                setShowHorizontalLines(true);
                setIntercellSpacing(new Dimension(0, 2));
                break;
            default:
                setShowVerticalLines(true);
                setShowHorizontalLines(true);
                setIntercellSpacing(new Dimension(2, 2));
                break;
        }
    }

    /****************************************************************************************************
     * Displays the configuration dialog and initializes it with the correct identifier.
     ***************************************************************************************************/
    private void validateTableConfiguration() {
        if (configDialog == null) {
            Container container = getTopLevelAncestor();

            if (container instanceof JFrame) {
                configDialog = new RTableConfigDialog((JFrame) container);
            } else if (container instanceof JDialog) {
                configDialog = new RTableConfigDialog((JDialog) container);
            } else {
                configDialog = new RTableConfigDialog(new JFrame());
            }
            configDialog.addPropertyChangeListener(this);
        }
        configDialog.initialize(identifier, tableModel.getColumnTitles());
        configDialog.setVisible(true);
    }

    /****************************************************************************************************
     * Listens for property change from dialog and delegates to appropriate method.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String command = event.getPropertyName();
        if (command.equals(UIPropertyName.TABLE_CONFIGURATION_ALTERED)) {
            resetTableConfiguration();
            resetSortConfiguration();
        }
    }
}
