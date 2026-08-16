package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyCache;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

/********************************************************************************************************
 * Item Hierarchy Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemHierarchyModel extends SimScreenModel {

    private MdseHierarchyNode departmentNode;
    private MdseHierarchyNode classNode;
    private MdseHierarchyNode subclassNode;

    public List<MdseHierarchyNode> findDepartments() throws Exception {
        return MdseHierarchyCache.getAllDepartments();
    }

    public List<MdseHierarchyNode> findDepartments(List<Long> departmentIds) throws Exception {
        List<MdseHierarchyNode> testNodes = MdseHierarchyCache.getAllDepartments();
        List<MdseHierarchyNode> finalNodes = new ArrayList<>();
        for (MdseHierarchyNode node : testNodes) {
            if (departmentIds.contains(node.getDepartmentId())) {
                finalNodes.add(node);
            }
        }
        return finalNodes;
    }

    public List<MdseHierarchyNode> getClassList(MdseHierarchyNode departmentNode) throws Exception {
        if (departmentNode != null) {
            return MdseHierarchyCache.getClasses(departmentNode);
        }
        return Collections.emptyList();
    }

    public List<MdseHierarchyNode> getSubclassList(MdseHierarchyNode classNode) throws Exception {
        if (classNode != null) {
            return MdseHierarchyCache.getSubclasses(classNode);
        }
        return Collections.emptyList();
    }

    public void loadHierarchyNodes(Long departmentId, Long classId, Long subclassId) throws Exception {
        MdseHierarchyNode node = MdseHierarchyCache.getMdseHierarchyNode(departmentId, classId, subclassId);
        if (node == null) {
            departmentNode = null;
            classNode = null;
            subclassNode = null;
            return;
        }
        if (node.isDepartment()) {
            departmentNode = node;
            classNode = null;
            subclassNode = null;
            return;
        }
        if (node.isClass()) {
            departmentNode = MdseHierarchyCache.getMdseHierarchyNode(node.getDepartmentId());
            classNode = node;
            subclassNode = null;
            return;
        }
        if (node.isSubclass()) {
            departmentNode = MdseHierarchyCache.getMdseHierarchyNode(node.getDepartmentId());
            classNode = MdseHierarchyCache.getMdseHierarchyNode(node.getDepartmentId(), node.getClassId(), null);
            subclassNode = node;
        }
    }

    public MdseHierarchyNode getDepartmentNode() {
        return departmentNode;
    }

    public MdseHierarchyNode getClassNode() {
        return classNode;
    }

    public MdseHierarchyNode getSubclassNode() {
        return subclassNode;
    }
}
