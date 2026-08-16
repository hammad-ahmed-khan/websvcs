package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;

/********************************************************************************************************
 * Display the purchase order id for a purchase order. Return translated word "New" if no id is found.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PurchaseOrderIdDisplayer extends AbstractDisplayer {
    public String getDisplayText(Object value) {
        if (value instanceof PurchaseOrder) {
            PurchaseOrder purchaseOrder = (PurchaseOrder) value;
            return getDisplayText(purchaseOrder.getExternalId(), purchaseOrder.isNew());
        }
        if (value instanceof PurchaseOrderVO) {
            PurchaseOrderVO purchaseOrderVO = (PurchaseOrderVO) value;
            return getDisplayText(purchaseOrderVO.getExternalId(), purchaseOrderVO.isNew());
        }
        if (value instanceof String) {
            return getDisplayText((String) value, false);
        }
        return getDisplayText(null, false);
    }

    private String getDisplayText(String externalId, boolean isNew) {
        if (!StringHelper.isNullOrEmpty(externalId)) {
            return externalId;
        }
        if (isNew) {
            return Translator.getText("New");
        }
        return StringConstants.EMPTY;
    }
}
