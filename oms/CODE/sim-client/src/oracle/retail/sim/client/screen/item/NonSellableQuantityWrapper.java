package oracle.retail.sim.client.screen.item;

import java.io.Serializable;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.NonSellableQtyType;

/********************************************************************************************************
 * Non-Sellable Quantity Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NonSellableQuantityWrapper implements Serializable {
    private static final long serialVersionUID = -7248259092970213943L;

    private NonSellableQtyType quantityType;
    private Quantity quantity;

    public NonSellableQuantityWrapper(NonSellableQtyType quantityType, Quantity quantity) {
        this.quantityType = quantityType;
        this.quantity = quantity;
    }

    public NonSellableQtyType getQuantityType() {
        return quantityType;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
