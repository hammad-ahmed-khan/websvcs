package oracle.retail.sim.client.swing.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.MouseDoubleClickAdapter;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RArrowButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RList;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/******************************************************************************************
 * This class represents a list with a title label. The title label is a RBoxLabel, though
 * it can be retrieved and modified if desired.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RListEditor extends AbstractEditor implements ListSelectionListener {
    private static final long serialVersionUID = 4817665673548704486L;

    //LIST
    private JPanel innerPanel = new JPanel();
    private RBoxEditorLabel titleLabel = new RBoxEditorLabel();
    private RList valueList = new RList();
    private RScrollPane scrollPane = new RScrollPane(valueList);

    private REventListener singleClickListener;
    private REventListener doubleClickListener;

    private String singleClickCommand;
    private String doubleClickCommand;

    private int titleIndent;
    private Object lastSelectedValue;

    // SWAP
    private RPanel swapPanel = new RPanel();

    private static final String UP_LABEL = "Move Up";
    private static final String DOWN_LABEL = "Move Down";

    private RLabel upLabel = new RLabel(UP_LABEL);
    private RLabel downLabel = new RLabel(DOWN_LABEL);

    private RArrowButton upButton = new RArrowButton(RArrowButton.NORTH);
    private RArrowButton downButton = new RArrowButton(RArrowButton.SOUTH);

    /******************************************************************************************
     * Creates a new RListEditor with no title.
     ******************************************************************************************/
    public RListEditor() {
        initialize();
    }

    /******************************************************************************************
     * Creates a new RListEditor with a title.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public RListEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /******************************************************************************************
     * Creates a new RListEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ******************************************************************************************/
    public RListEditor(String title, boolean required) {
        titleLabel.setText(title);
        setRequired(required);
        initialize();
    }

    /******************************************************************************************
     * Initializes the editor.
     ******************************************************************************************/
    private void initialize() {
        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);

        titleIndent = errorIcon.getIconWidth();

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        validateTitle();
        initializeSwapPanel();

        valueList.addListSelectionListener(this);
        valueList.addMouseListener(createListMouseListener());
        valueList.addFocusListener(createManagerFocusListener());

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.TOP);
    }

    /******************************************************************************************
     * Initializes the swap panel.
     ******************************************************************************************/
    private void initializeSwapPanel() {
        swapPanel.setVisible(false);
        swapPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        swapPanel.add(upLabel);
        swapPanel.add(upButton);
        swapPanel.add(downLabel);
        swapPanel.add(downButton);

        upLabel.addMouseListener(createMoveUpMouseAction());
        downLabel.addMouseListener(createMoveDownMouseAction());

        upButton.addActionListener(createMoveUpAction());
        downButton.addActionListener(createMoveDownAction());

        upButton.addFocusListener(createUpFocusAction());
        downButton.addFocusListener(createDownFocusAction());
    }

    /*********************************************************************************************
     * Creates the list selection mouse listener.
     *********************************************************************************************/
    private MouseListener createListMouseListener() {
        return new MouseDoubleClickAdapter() {
            public void mouseDoubleClicked(MouseEvent event) {
                doDoubleClickAction();
            }
        };
    }

    /*********************************************************************************************
     * Creates the move up mouse action.
     *********************************************************************************************/
    private MouseListener createMoveUpMouseAction() {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                upButton.doClick();
            }
        };
    }

    /*********************************************************************************************
     * Creates the move down mouse action.
     *********************************************************************************************/
    private MouseListener createMoveDownMouseAction() {
        return new MouseListener() {
            public void mouseClicked(MouseEvent event) {
                downButton.doClick();
            }

            public void mouseEntered(MouseEvent event) {
            }

            public void mouseExited(MouseEvent event) {
            }

            public void mousePressed(MouseEvent event) {
            }

            public void mouseReleased(MouseEvent event) {
            }
        };
    }

    /*********************************************************************************************
     * Creates the move up action.
     *********************************************************************************************/
    private ActionListener createMoveUpAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doMoveUpAction();
            }
        };
    }

    /*********************************************************************************************
     * Create the move down action.
     *********************************************************************************************/
    private ActionListener createMoveDownAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doMoveDownAction();
            }
        };
    }

    /*********************************************************************************************
     * Creates the up focus action.
     *********************************************************************************************/
    private FocusListener createUpFocusAction() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                upLabel.setFontStyle(Font.BOLD);
            }

            public void focusLost(FocusEvent event) {
                upLabel.setFontStyle(Font.PLAIN);
            }
        };
    }

    /*********************************************************************************************
     * Creates the down focus action.
     *********************************************************************************************/
    private FocusListener createDownFocusAction() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                downLabel.setFontStyle(Font.BOLD);
            }

            public void focusLost(FocusEvent event) {
                downLabel.setFontStyle(Font.PLAIN);
            }
        };
    }

    /******************************************************************************************
     * Retrieves the label widget associated with this editor.
     * <p>
     * @param The label widget.
     ******************************************************************************************/
    public REditorLabel getLabel() {
        return titleLabel;
    }

    /******************************************************************************************
     * Retrieves the RList object of the editor. Many helpful methods have been added to the
     * editor, but the developer can always retrieve the actual RList widget to get access to
     * methods that are not provided.
     * <p>
     * @return The RList widget.
     ******************************************************************************************/
    public RList getList() {
        return valueList;
    }

    /******************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal
     * component rather than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ******************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueList.addKeyListener(listener);
    }

    /******************************************************************************************
     * This method is called when the identifer is altered in an editor. Subclasses should
     * override this for any desired custom functionality.
     ******************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueList.setIdentifier(identifier);
    }

    /******************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ******************************************************************************************/
    public String getTitle() {
        return titleLabel.getOriginalText();
    }

    /******************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            titleLabel.clear();
        } else {
            titleLabel.setText(title);
        }
        validateTitle();
    }

    /******************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM.
     * The label suffix feature is turned off for all alignments except for LEFT.
     * <p>
     * @param alignment The alignment to assign.
     *****************************************************************************************/
    public void setTitleAlignment(int alignment) {
        titleLabel.setTitleAlignment(alignment);
        validateInnerLayout();
    }

    /******************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title
     * alignment (see EditorConstants).
     * <p>
     * @return The title alignment.
     *****************************************************************************************/
    public int getTitleAlignment() {
        return titleLabel.getTitleAlignment();
    }

    /******************************************************************************************
     * Assigns whether or not the editor represents required information.
     * <p>
     * @param required True if the editor represents required information, false if not.
     *****************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(required);
        markRequiredAssigned();
    }

    /******************************************************************************************
     * Retrieves whether or not the editor represents required information.
     * <p>
     * @return True if the editor represents required information, false if not.
     *****************************************************************************************/
    public boolean isRequired() {
        return titleLabel.isRequired();
    }

    /******************************************************************************************
     * Validates the internal layout of the list editor.
     ******************************************************************************************/
    private void validateInnerLayout() {
        if (sizeType == -1) {
            validateStandardLayout();
        } else {
            validateSizeTypeLayout();
        }
    }

    /******************************************************************************************
     * Validates the standard layout (no size type has been assigned).
     ******************************************************************************************/
    private void validateStandardLayout() {
        removeAll();

        validateTitleLabelLayout();

        int indent = 0;
        if (errorLabel.isVisible()) {
            indent = titleIndent;
        }

        validateStandardInnerPanelLayout();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, indent + 1));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 1, 0));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 1, 0, 0, 0, indent + 1));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 1, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RListEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /******************************************************************************************
     * Performs the standard layout (no size type has been assigned).
     ******************************************************************************************/
    private void validateStandardInnerPanelLayout() {
        innerPanel.removeAll();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(scrollPane, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                innerPanel.add(swapPanel, GridTool.constraints(1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(scrollPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                innerPanel.add(swapPanel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                break;
        }
    }

    /******************************************************************************************
     * Validates the size type layout (a size type has been assigned).
     ******************************************************************************************/
    private void validateSizeTypeLayout() {
        removeAll();

        validateTitleLabelLayout();
        validateSizeTypeInnerPanelLayout();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RListEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /******************************************************************************************
     * Validates the inner panel widget/error label when a size type is assigned.
     ******************************************************************************************/
    private void validateSizeTypeInnerPanelLayout() {
        innerPanel.removeAll();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(scrollPane, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
                innerPanel.add(swapPanel, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                innerPanel.add(scrollPane, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
                innerPanel.add(swapPanel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(scrollPane, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
                innerPanel.add(swapPanel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
        }
    }

    /******************************************************************************************
     * Validates the title label border and layout.
     ******************************************************************************************/
    private void validateTitleLabelLayout() {
        titleLabel.setMinimumSize(null);
        titleLabel.setPreferredSize(null);

        int thick = titleLabel.getBorderThickness();
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                titleLabel.setBorder(new MatteBorder(thick, thick, 0, thick, titleLabel.getBorderColor()));
                break;
            default:
                titleLabel.setBorder(new MatteBorder(thick, thick, thick, thick, titleLabel.getBorderColor()));
                break;
        }

        if (sizeType != -1) {
            Dimension dimension;
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.TOP:
                case EditorConstants.BOTTOM:
                    int width = scrollPane.getPreferredSize().width - 1;
                    dimension = new Dimension(width, titleLabel.getPreferredSize().height);
                    titleLabel.setMinimumSize(dimension);
                    titleLabel.setPreferredSize(dimension);
                    break;
                case EditorConstants.LEFT:
                case EditorConstants.RIGHT:
                    int height = scrollPane.getPreferredSize().height;
                    dimension = new Dimension(titleLabel.getPreferredSize().width, height);
                    titleLabel.setMinimumSize(dimension);
                    titleLabel.setPreferredSize(dimension);
                    break;
                default:
                    break;
            }
        }
    }

    /******************************************************************************************
     * Validates the visibility of the title.
     ******************************************************************************************/
    protected void validateTitle() {
        if (isVisibleDenied()) {
            return;
        }
        titleLabel.setVisible(!StringUtility.isNullOrEmpty(titleLabel.getText()));
    }

    /******************************************************************************************
     * Assigns a minimum and preferred size to the editor. Values sizes include
     * EditorConstants.SMALL, EditorConstants.MEDIUM and EditorConstants.LARGE. This method
     * should only be used if the list editor is placed inside an REditorPanel.
     * Use setSizeType(int, boolean, boolean) for other layouts.
     * <p>
     *@param sizeType The size type (SMALL, MEDIUM, or LARGE).
     *****************************************************************************************/
    public void setSizeType(int sizeType) {
        setSizeType(sizeType, true, true);
    }

    /******************************************************************************************
     * Assigns a minimum and preferred size to the editor. Values sizes include
     * EditorConstants.SMALL, EditorConstants.MEDIUM and EditorConstants.LARGE.
     * <p>
     *@param sizeType The size type (SMALL, MEDIUM, or LARGE).
     *@param width Assign size type to width.
     *@param height Assign size type to height.
     *****************************************************************************************/
    public void setSizeType(int sizeType, boolean width, boolean height) {
        switch (sizeType) {
            case EditorConstants.SMALL:
                setListSize(UIManager.getInt(UIThemeName.LIST_SMALL), width, height);
                break;
            case EditorConstants.MEDIUM:
                setListSize(UIManager.getInt(UIThemeName.LIST_MEDIUM), width, height);
                break;
            case EditorConstants.LARGE:
                setListSize(UIManager.getInt(UIThemeName.LIST_LARGE), width, height);
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        if (width && height) {
            this.sizeType = sizeType;
        }
        validateInnerLayout();
    }

    /******************************************************************************************
     * Assigns a size to the list.
     * <p>
     *@return The size type (SMALL, MEDIUM, or LARGE) or -1 if none is assigned.
     *****************************************************************************************/
    private void setListSize(int size, boolean width, boolean height) {
        if (width && height) {
            scrollPane.setMinimumSize(new Dimension(size, size));
            scrollPane.setPreferredSize(new Dimension(size, size));
        } else if (width) {
            scrollPane.setMinimumSize(new Dimension(size, 0));
            scrollPane.setPreferredSize(new Dimension(size, 0));
        } else if (height) {
            scrollPane.setMinimumSize(new Dimension(0, size));
            scrollPane.setPreferredSize(new Dimension(0, size));
        }
    }

    /******************************************************************************************
     * Retrieves the vertical weight of the editor (used with REditorPanel)
     * <p>
     *@return The vertical weight of the editor.
     *****************************************************************************************/
    public int getVerticalWeight() {
        return 1;
    }

    /******************************************************************************************
     * Retrieves the horiztonal weight of the editor (used with REditorPanel)
     * <p>
     *@return The horiztonal weight of the editor.
     *****************************************************************************************/
    public int getHorizontalWeight() {
        return 1;
    }

    /******************************************************************************************
     * Retrieves the fill of the editor (used with REditorPanel);
     * <p>
     *@return The fill of the editor.
     *****************************************************************************************/
    public int getFill() {
        return 3;
    }

    /******************************************************************************************
     * Sets the minimum width of the editor.
     * <p>
     * @param width The minimum width in pixels.
     ******************************************************************************************/
    public void setMinimumWidth(int width) {
        scrollPane.setMinimumSize(new Dimension(width, 0));
        scrollPane.setPreferredSize(new Dimension(width, 0));
    }

    /******************************************************************************************
     * Assigns single selection mode to the list.
     ******************************************************************************************/
    public void setSingleSelectionMode() {
        valueList.setSingleSelectionMode();
    }

    /******************************************************************************************
     * Assigns multiple selection mode to the list.
     ******************************************************************************************/
    public void setMultipleSelectionMode() {
        valueList.setMultiSelectionMode();
    }

    /******************************************************************************************
     * Allows multiple line display within the list. This means that if display text for the
     * list contains line returns, it will display over multiple lines.
     ******************************************************************************************/
    public void setMultiLineMode() {
        valueList.setMultiLineMode();
    }

    /******************************************************************************************
     * Allows only single line display within the list. This means that if display text for the
     * list contains line returns, it will be truncated.
     ******************************************************************************************/
    public void setSingleLineMode() {
        valueList.setSingleLineMode();
    }

    /******************************************************************************************
     * Assigns the object that displays rows within the list.
     * <p>
     * @param basicDisplayer The BasicDisplayer that will display the list objects.
     ******************************************************************************************/
    public void setRowDisplayer(BasicDisplayer basicDisplayer) {
        valueList.setRowDisplayer(basicDisplayer);
    }

    /******************************************************************************************
     * Retrieves the basic displayer for the list. This property can never be null.
     * <p>
     * @return The BasicDisplayer assigned to this list.
     ******************************************************************************************/
    public BasicDisplayer getRowDisplayer() {
        return valueList.getRowDisplayer();
    }

    /******************************************************************************************
     * Assigns the object that compares rows within the list.
     * <p>
     * @param comparator A comparator
     ******************************************************************************************/
    public void setRowComparator(Comparator comparator) {
        valueList.setRowComparator(comparator);
    }

    /******************************************************************************************
     * Assigns the auto sort flag of the list. If true, the list will attempt to sort all
     * contents alphabetically. By default, auto sort is enabled.
     * <p>
     * @param enabled True if the list should sort, false if not.
     ******************************************************************************************/
    public void setAutoSort(boolean enabled) {
        valueList.setAutoSort(enabled);
        if (enabled) {
            setAllowsSwap(false);
        }
    }

    /******************************************************************************************
     * Retrieves the auto sort flag.
     ******************************************************************************************/
    public boolean isAutoSort() {
        return valueList.isAutoSort();
    }

    /******************************************************************************************
     * Sets whether or not the list should allows its selected item to be swapped up or down.
     * If auto sort is set to true, this value will always be false.
     * <p>
     * @param allowsSwap True if thel ist should allow reorganization, false if not.
     ******************************************************************************************/
    public void setAllowsSwap(boolean allowsSwap) {
        if (isAutoSort()) {
            allowsSwap = false;
        }
        swapPanel.setVisible(allowsSwap);
    }

    /******************************************************************************************
     * Retrieves whether or not the list allows its item to be swapped up or down.
     * <p>
     * @param allowsSwap True if the list should allow reorganization, false if not.
     ******************************************************************************************/
    public boolean allowsSwap() {
        return swapPanel.isVisible();
    }

    /******************************************************************************************
     * Retrieves the height consumed by the swap panel.
     * <p>
     * @return The height consumed by the swap panel.
     ******************************************************************************************/
    public int getSwapPad() {
        if (swapPanel.isVisible()) {
            return swapPanel.getPreferredSize().height;
        }
        return 0;
    }

    /******************************************************************************************
     * Sets an array of objects in the list. This method first removes all previous objects
     * before assigning the new objects.
     * <p>
     * @param collection A collection of objects to display in the list.
     ******************************************************************************************/
    public void setItems(Object[] array) {
        valueList.setItems(array);
    }

    /******************************************************************************************
     * Sets a collection of objects in the list. This method first removes all previous objects
     * before assigning the new objects.
     * <p>
     * @param collection A collection of objects to display in the list.
     ******************************************************************************************/
    public void setItems(Collection collection) {
        valueList.setItems(collection);
    }

    /******************************************************************************************
     * Adds an array of objects to the list. Duplicate objects will not be added.
     * <p>
     * @param array The array of objects to add to the list.
     ******************************************************************************************/
    public void addItems(Object[] array) {
        valueList.addItems(array);
    }

    /******************************************************************************************
     * Adds an single item to the list.
     * <p>
     * @param object The object to add to the list.
     ******************************************************************************************/
    public void addItem(Object object) {
        valueList.addItem(object);
    }

    /******************************************************************************************
     * Adds a collection of objects to the list. Duplicate objects will not be added.
     * <p>
     * @param collection The collection of objects to add to the list.
     ******************************************************************************************/
    public void addItems(Collection collection) {
        valueList.addItems(collection);
    }

    /******************************************************************************************
     * Retrieves all the items in the list box as a array.
     * <p>
     * @return An array of all items in the list.
     ******************************************************************************************/
    public Object[] getItems() {
        return valueList.getItems();
    }

    /******************************************************************************************
     * Retrieves all the items in the list box as a list.
     * <p>
     * @return A list of all items within the list.
     ******************************************************************************************/
    public List getItemsAsList() {
        return valueList.getItemsAsList();
    }

    /******************************************************************************************
     * Returns the first selected value, or <code>null</code> if the selection is empty.
     * <p>
     * @return The first selected value
     ******************************************************************************************/
    public Object getSelectedValue() {
        return valueList.getSelectedValue();
    }

    /******************************************************************************************
     * Sets the parameter value as a selected value in the list.
     * <p>
     * @param value The value to be selected.
     ******************************************************************************************/
    public void setSelectedValue(Object value) {
        valueList.setSelectedValue(value, true);
    }

    /******************************************************************************************
     * Returns an array of the values for the selected cells. The returned values are sorted
     * in increasing index order.
     * <p>
     * @return The selected values or an empty array if nothing is selected.
     ******************************************************************************************/
    public Object[] getSelectedValues() {
        return valueList.getSelectedValues();
    }

    /******************************************************************************************
     * Selects all items in the collection passed in.  If an item is in the collection, but not
     * this list, it is ignored.
     * <p>
     * @param values A collection of values to be selected.
     ******************************************************************************************/
    public void setSelectedValues(Collection values) {
        valueList.setSelectedValues(values);
    }

    /******************************************************************************************
     * Updates an item in the list. This simply takes an object and replaces it with itself,
     * refreshing the object description.
     * <p>
     * @param object The item to update in the list.
     ******************************************************************************************/
    public void updateItem(Object object) {
        valueList.updateItem(object);
    }

    /******************************************************************************************
     * Removes all currently selected values from the list.
     ******************************************************************************************/
    public void removeSelectedValues() {
        valueList.removeSelectedValues();
    }

    /******************************************************************************************
     * Removes all items from the list.
     ******************************************************************************************/
    public void removeItems() {
        valueList.removeItems();
    }

    /******************************************************************************************
     * Removes an array of objects from the list.
     * <p>
     * @param array The array of objects to remove from the list.
     ******************************************************************************************/
    public void removeItems(Object[] array) {
        valueList.removeItems(array);
    }

    /******************************************************************************************
     * Removes a collection of objects from the list.
     * <p>
     * @param collection The collection of objects to remove from the list.
     ******************************************************************************************/
    public void removeItems(Collection collection) {
        valueList.removeItems(collection);
    }

    /******************************************************************************************
     * Removes the specified item from the list.
     * <p>
     * @param object The object to remove from the list.
     ******************************************************************************************/
    public void removeItem(Object object) {
        valueList.removeItem(object);
    }

    /******************************************************************************************
     * Clears the list selection.
     *****************************************************************************************/
    public void clearSelection() {
        valueList.clearSelection();
    }

    /******************************************************************************************
     * Returns true if the list has at least one selected value, otherwise it returns false.
     * <p>
     * @return True if the list has at least one selected value, otherwise false.
     ******************************************************************************************/
    public boolean isEmptySelection() {
        return valueList.isSelectionEmpty();
    }

    /******************************************************************************************
     * Returns true if the list has at least one data value, otherwise it returns false.
     * <p>
     * @return True if the list has at least one data value, otherwise false.
     *****************************************************************************************/
    public boolean isEmpty() {
        return valueList.getItems().length == 0;
    }

    /******************************************************************************************
     * Override the setVisible() to validate against permissions first.
     *****************************************************************************************/
    public void setVisible(boolean visible) {
        if (isVisibleDenied()) {
            visible = false;
        }
        titleLabel.setVisible(visible);
        innerPanel.setVisible(visible);
    }

    /******************************************************************************************
     * Override the setEnabled() of JPanel to call through to the label and to the RList.
     *****************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueList.setEnabled(enabled);
        upButton.setEnabled(enabled);
        downButton.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            labelEnabled = false;
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        valueList.setEnabled(fieldEnabled);
        upButton.setEnabled(fieldEnabled);
        downButton.setEnabled(fieldEnabled);
    }

    /******************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     *****************************************************************************************/
    public boolean isEnabled() {
        return valueList.isEnabled();
    }

    /******************************************************************************************
     * Assigns whether or not the error indicator is available.
     * <p>
     * @param available True if error indicator should be available, false if not.
     *****************************************************************************************/
    public void setErrorIndicatorAvailable(boolean available) {
        errorLabel.setVisible(available);
        setTitleAlignment(getTitleAlignment());
    }

    /******************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     *****************************************************************************************/
    public void setErrorState(boolean errorState) {
        setErrorState(errorState, StringConstants.EMPTY);
    }

    /******************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     * @param errorText The text to display along with the editor.
     *****************************************************************************************/
    public void setErrorState(boolean errorState, String errorText) {
        isErrorState = errorState;

        if (isErrorState) {
            errorLabel.setIcon(errorIcon);
            errorLabel.setToolTipText(errorText);
        } else {
            errorLabel.setIcon(null);
            errorLabel.setToolTipText(StringConstants.EMPTY);
        }
    }

    /******************************************************************************************
     * Retrieves whethor or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     *****************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /******************************************************************************************
     * Assigns an event listener to the list selection events of an RList. The command will
     * be sent to listener whenever the list selections are altered.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     *****************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("listener and command parameters cannot be null!");
        }
        singleClickListener = listener;
        singleClickCommand = command;
    }

    /******************************************************************************************
     * Assigns an event listener to the list selection events of an RList. The command will
     * be sent to listener whenenever the mouse is double clicked on a list item.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     *****************************************************************************************/
    public void registerDoubleClickAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("listener and command parameters cannot be null!");
        }
        doubleClickListener = listener;
        doubleClickCommand = command;
    }

    /******************************************************************************************
     * Implements the list selection listener interface to inform all listeners of list
     * action events.
     *****************************************************************************************/
    public void valueChanged(ListSelectionEvent event) {
        if (isErrorState) {
            setErrorState(false);
        }
        if (isActionEnabled && singleClickListener != null && singleClickCommand != null) {
            Object value = valueList.getSelectedValue();
            if (valueList.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
                if (value == lastSelectedValue) {
                    return;
                }
            }
            lastSelectedValue = value;
            singleClickListener.performActionEvent(new RActionEvent(this, singleClickCommand));
        }
    }

    /******************************************************************************************
     * Notifies a double-click action listener if a row has been double-clicked. It passes
     * along the object selected.
     *****************************************************************************************/
    private void doDoubleClickAction() {
        if (isActionEnabled) {
            Object[] tmpValueList = getSelectedValues();
            if (tmpValueList.length > 0 && doubleClickListener != null && doubleClickCommand != null) {
                doubleClickListener.performActionEvent(new RActionEvent(this, doubleClickCommand, tmpValueList[0]));
            }
        }
    }

    /******************************************************************************************
     * Moves selected item up one in the list.
     *****************************************************************************************/
    private synchronized void doMoveUpAction() {
        if (valueList.getSelectedValues().length == 0) {
            return;
        }
        int index = valueList.getSelectedIndex();
        if (index == 0) {
            return;
        }
        int upIndex = index - 1;

        setActionsEnabled(false);

        DefaultListModel model = (DefaultListModel) valueList.getModel();

        Object value1 = model.getElementAt(index);
        Object value2 = model.getElementAt(upIndex);

        model.setElementAt(value1, upIndex);
        model.setElementAt(value2, index);

        valueList.setSelectedValue(value1, true);

        firePropertyChange(UIPropertyName.LIST_VALUE_POSITION, index, upIndex);

        setActionsEnabled(true);
    }

    /******************************************************************************************
     * Moves selected item down one in the list.
     *****************************************************************************************/
    private synchronized void doMoveDownAction() {
        if (valueList.getSelectedValues().length == 0) {
            return;
        }
        DefaultListModel model = (DefaultListModel) valueList.getModel();

        int index = valueList.getSelectedIndex();
        if (index == model.getSize() - 1) {
            return;
        }
        int downIndex = index + 1;

        setActionsEnabled(false);

        Object value1 = model.getElementAt(index);
        Object value2 = model.getElementAt(downIndex);

        model.setElementAt(value1, downIndex);
        model.setElementAt(value2, index);

        valueList.setSelectedValue(value1, true);

        firePropertyChange(UIPropertyName.LIST_VALUE_POSITION, index, downIndex);

        setActionsEnabled(true);
    }

    /******************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     *****************************************************************************************/
    public boolean isFocusOwner() {
        return valueList.isFocusOwner();
    }

    /******************************************************************************************
     * Requests the focus move the list within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely
     * to succeed.
     *****************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueList.requestFocusInWindow();
    }

    /******************************************************************************************
     * Handles displaying an exception on the application.
     *****************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueList, exception);
    }
}
