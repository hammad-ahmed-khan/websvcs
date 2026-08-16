package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.swing.displayer.AttributeComparator;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

/********************************************************************************************************
 * Item Hierarchy Panel containing the department, class, subclass editors.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemHierarchyPanel extends REditorPanel implements REventListener {
    private static final long serialVersionUID = 1761222770559335259L;

    private ItemHierarchyModel model = new ItemHierarchyModel();

    private RComboBoxEditor departmentEditor = new RComboBoxEditor("Dept");
    private RComboBoxEditor classEditor = new RComboBoxEditor("Class");
    private RComboBoxEditor subclassEditor = new RComboBoxEditor("Sub-Class");

    private static final String DEPT_SELECTION = "Dept.selection";
    private static final String CLASS_SELECTION = "Class.selection";
    private static final String SUBCLASS_SELECTION = "Subclass.selection";

    private REventListener eventListener;
    private String eventCommand;

    /****************************************************************************************************
     * INITIALIZE PANEL
     ***************************************************************************************************/

    public ItemHierarchyPanel() {
        super(3);

        initializeEditors();

        add(departmentEditor);
        add(classEditor);
        add(subclassEditor);
    }

    private void initializeEditors() {
        departmentEditor.setDisplayer(new DualAttributeDisplayer("departmentId", "departmentName"));
        departmentEditor.setComparator(new AttributeComparator("departmentId"));
        classEditor.setDisplayer(new DualAttributeDisplayer("classId", "className"));
        classEditor.setComparator(new AttributeComparator("classId"));
        subclassEditor.setDisplayer(new DualAttributeDisplayer("subclassId", "subclassName"));
        subclassEditor.setComparator(new AttributeComparator("subclassId"));

        departmentEditor.registerAction(this, DEPT_SELECTION);
        classEditor.registerAction(this, CLASS_SELECTION);
        subclassEditor.registerAction(this, SUBCLASS_SELECTION);
    }

    public void setSizeType(int sizeType) {
        departmentEditor.setSizeType(sizeType);
        classEditor.setSizeType(sizeType);
        subclassEditor.setSizeType(sizeType);
    }

    public void registerAction(REventListener listener, String command) {
        eventListener = listener;
        eventCommand = command;
    }

    /****************************************************************************************************
     * GET AND SET DATA
     ***************************************************************************************************/

    public void loadDepartments() throws Exception {
        displayDispartments(model.findDepartments());
    }

    public void loadDepartments(List<Long> departmentIds) throws Exception {
        displayDispartments(model.findDepartments(departmentIds));
    }

    private void displayDispartments(List<MdseHierarchyNode> nodes) {
        setActionsEnabled(false);

        departmentEditor.setItems(nodes);
        if (departmentEditor.isEmpty()) {
            UILog.info(getClass(), UIMessageText.DEPARTMENTS_NOT_FOUND);
        }
        classEditor.clear();
        classEditor.setEnabled(false);
        subclassEditor.clear();
        subclassEditor.setEnabled(false);

        setActionsEnabled(true);
    }

    public void clearSelection() {
        departmentEditor.setEmptySelection();
    }

    public MdseHierarchyNode getHierarchyNode() {
        Object selectedValue = subclassEditor.getSelectedItem();
        if (selectedValue == null) {
            selectedValue = classEditor.getSelectedItem();
        }
        if (selectedValue == null) {
            selectedValue = departmentEditor.getSelectedItem();
        }
        return (MdseHierarchyNode) selectedValue;
    }

    public void setHierarchyNode(Long departmentId, Long classId, Long subclassId) throws Exception {
        setActionsEnabled(false);
        model.loadHierarchyNodes(departmentId, classId, subclassId);
        departmentEditor.setSelectedItem(model.getDepartmentNode());
        loadClasses();
        classEditor.setSelectedItem(model.getClassNode());
        loadSubclasses();
        subclassEditor.setSelectedItem(model.getSubclassNode());
        setActionsEnabled(true);
    }

    public void setEnabled(boolean enabled) {
        departmentEditor.setEnabled(enabled);
    }

    public void setActionsEnabled(boolean enabled) {
        departmentEditor.setActionsEnabled(enabled);
        classEditor.setActionsEnabled(enabled);
        subclassEditor.setActionsEnabled(enabled);
    }

    public void setEmptyDescriptionToAll() {
        departmentEditor.setEmptyType(RComboBoxEmptyType.ALL);
        classEditor.setEmptyType(RComboBoxEmptyType.ALL);
        subclassEditor.setEmptyType(RComboBoxEmptyType.ALL);
    }

    /****************************************************************************************************
     * HANDLE PANEL ACTIONS
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(DEPT_SELECTION)) {
                loadClasses();
            } else if (command.equals(CLASS_SELECTION)) {
                loadSubclasses();
            }
            fireHierarchyChanged();
        } catch (Throwable exception) {
            UILog.debug(getClass(), UIMessageText.UNABLE_TO_LOAD_HIERARCHY);
        }
    }

    private void fireHierarchyChanged() {
        if (eventListener != null) {
            if (eventCommand != null) {
                eventListener.performActionEvent(new RActionEvent(this, eventCommand));
            }
        }
    }

    private void loadClasses() throws Exception {
        setActionsEnabled(false);

        MdseHierarchyNode departmentNode = (MdseHierarchyNode) departmentEditor.getSelectedItem();
        classEditor.setItems(model.getClassList(departmentNode));
        classEditor.setEnabled(departmentNode != null);
        subclassEditor.clear();
        subclassEditor.setEnabled(false);

        setActionsEnabled(true);
    }

    private void loadSubclasses() throws Exception {
        setActionsEnabled(false);

        MdseHierarchyNode classNode = (MdseHierarchyNode) classEditor.getSelectedItem();
        subclassEditor.setItems(model.getSubclassList(classNode));
        subclassEditor.setEnabled(classNode != null);

        setActionsEnabled(true);
    }
}
