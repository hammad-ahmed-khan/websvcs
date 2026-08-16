package oracle.retail.sim.client.displayer;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;

/********************************************************************************************************
 * Direct Delivery VO Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryVODisplayer extends AbstractDisplayer {
    public String getDisplayText(Object value) {
        if (value instanceof DirectDeliveryVO) {
            return ((DirectDeliveryVO) value).getId().toString();
        }
        return StringConstants.EMPTY;
    }
}
