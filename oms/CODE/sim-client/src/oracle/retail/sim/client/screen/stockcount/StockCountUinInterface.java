package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * Interface for stock count line items that contain UINs.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public interface StockCountUinInterface {

    UINType getType();

    FunctionalArea getFunctionalArea();

    String getItemId();

    String getLabel();
}
