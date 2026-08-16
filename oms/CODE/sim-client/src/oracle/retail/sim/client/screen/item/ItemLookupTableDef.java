package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;

/********************************************************************************************************
 * ITEM LOOKUP TABLE DEF
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemLookupTableDef extends SimTableDefinition {

    public Class getDataClass() {
        return ItemVO.class;
    }

    public List<SimTableSortAttribute> getSortAttributes() {
        if (SimConfigManager.isItemShortDescription()) {
            return Collections.singletonList(new SimTableSortAttribute("shortDescription"));
        }
        return Collections.singletonList(new SimTableSortAttribute("longDescription"));
    }

    public List<SimTableAttribute> getAttributes() {
        List<SimTableAttribute> attributes = new ArrayList<>(7);
        attributes.add(new SimTableAttribute("Item", "id"));
        if (SimConfigManager.isItemShortDescription()) {
            attributes.add(new SimTableAttribute("Item Description", "shortDescription"));
        } else {
            attributes.add(new SimTableAttribute("Item Description", "longDescription"));
        }
        attributes.add(new SimTableAttribute("Primary Supplier", "supplierVO.id"));
        attributes.add(new SimTableAttribute("Primary Supplier Name", "supplierVO.name"));
        attributes.add(new SimTableAttribute("Dept.", "departmentName"));
        attributes.add(new SimTableAttribute("Class", "className"));
        attributes.add(new SimTableAttribute("Sub-Class", "subclassName"));
        return attributes;
    }
}
