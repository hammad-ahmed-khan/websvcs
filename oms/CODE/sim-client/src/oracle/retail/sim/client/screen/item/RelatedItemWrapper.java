package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.item.RelatedItem;

/********************************************************************************************************
 * Related Item Wrapper
 * <p>
 * Copyright 2004, 2014, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RelatedItemWrapper extends Wrapper {

    private RelatedItem relatedItem;

    public RelatedItemWrapper(RelatedItem relatedItem) {
        this.relatedItem = relatedItem;
    }

    public RelatedItem getRelatedItem() {
        return relatedItem;
    }

    public String getItemId() {
        return relatedItem.getId();
    }

    public String getDescription() {
        return relatedItem.getDescription();
    }

    public String getDiff1() {
        return relatedItem.getDiff1();
    }

    public String getDiff2() {
        return relatedItem.getDiff2();
    }

    public String getDiff3() {
        return relatedItem.getDiff3();
    }

    public String getDiff4() {
        return relatedItem.getDiff4();
    }

    public String getType() {
        return relatedItem.getType();
    }

    public Boolean getRequired() {
        return relatedItem.isMandatory();
    }

    public String getUnitOfMeasure() {
        return relatedItem.getUnitOfMeasure();
    }

    public Quantity getAvailableStockOnHand() {
        return relatedItem.getAvailableStockOnHand();
    }
}
