package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyCache;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.productgroup.ProductGroupHierarchy;

/********************************************************************************************************
 * Product Group Detail Lien Item Wrapper
 * <p>
 * This is a very simple wrapper class for the line items in the product group detail table. This was
 * created so lookups for items and hierarchies only happen once, rather than many many times. The
 * lookups will happen when the line items are created, and then will be cached so that multiple service
 * calls are not necessary.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupDetailLineItemWrapper {

    private static final int HIERARCHY_TYPE = 0;
    private static final int ITEM_TYPE = 1;

    private ProductGroupHierarchy hierarchy;
    private ProductGroupItem item;
    private int type = -1;

    public ProductGroupDetailLineItemWrapper(ProductGroupHierarchy hierarchy) {
        type = HIERARCHY_TYPE;
        this.hierarchy = hierarchy;
    }

    public ProductGroupDetailLineItemWrapper(ProductGroupItem item) {
        type = ITEM_TYPE;
        this.item = item;
        hierarchy = new ProductGroupHierarchy(item.getDepartmentId(), item.getClassId(), item.getSubclassId());
    }

    public boolean isHierarchyType() {
        return type == HIERARCHY_TYPE;
    }

    public boolean isItemType() {
        return type == ITEM_TYPE;
    }

    public ProductGroupHierarchy getHierarchy() {
        return hierarchy;
    }

    public ProductGroupItem getItem() {
        return item;
    }

    public String getItemId() {
        if (item != null) {
            return item.getId();
        }
        return null;
    }

    public String getDescription() {
        StringBuilder buffer = new StringBuilder();
        if (item != null) {
            buffer.append(item.getId());
            buffer.append(" - ");
            if (SimConfigManager.isItemShortDescription()) {
                buffer.append(item.getShortDescription());
            } else {
                buffer.append(item.getLongDescription());
            }
        }
        return buffer.toString();
    }

    public String getAllLocationText() {
        return null;
    }

    public String getAllDepartmentText() {
        return null;
    }

    public String getDepartment() throws Exception {
        StringBuilder buffer = new StringBuilder();
        MdseHierarchyNode node = getHierarchyNode();
        if (node.getDepartmentId() != null) {
            buffer.append(node.getDepartmentId());
        }
        if (!StringUtility.isNullOrEmpty(node.getDepartmentName())) {
            buffer.append(" - ").append(node.getDepartmentName());
        }
        return buffer.toString();
    }

    public String getClazz() throws Exception {
        StringBuilder buffer = new StringBuilder();
        MdseHierarchyNode node = getHierarchyNode();
        if (node.getClassId() != null) {
            buffer.append(node.getClassId());
        }
        if (!StringUtility.isNullOrEmpty(node.getClassName())) {
            buffer.append(" - ").append(node.getClassName());
        }
        return buffer.toString();
    }

    public String getSubclass() throws Exception {
        StringBuilder buffer = new StringBuilder();
        MdseHierarchyNode node = getHierarchyNode();
        if (node.getSubclassId() != null) {
            buffer.append(node.getSubclassId());
        }
        if (!StringUtility.isNullOrEmpty(node.getSubclassName())) {
            buffer.append(" - ").append(node.getSubclassName());
        }
        return buffer.toString();
    }

    public Integer getItemCount() {
        if (type == HIERARCHY_TYPE) {
            return hierarchy.getNumberOfItems();
        }
        return 1;
    }

    private MdseHierarchyNode getHierarchyNode() throws Exception {
        Long departmentId = hierarchy.getDepartmentId();
        Long classId = hierarchy.getClassId();
        Long subclassId = hierarchy.getSubclassId();
        return MdseHierarchyCache.getMdseHierarchyNode(departmentId, classId, subclassId);
    }
}
