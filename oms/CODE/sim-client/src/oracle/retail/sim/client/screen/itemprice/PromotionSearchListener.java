package oracle.retail.sim.client.screen.itemprice;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.itemprice.PromotionVO;

/********************************************************************************************************
 * Generic Search Listener For Promotion Search Field Editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class PromotionSearchListener implements SearchListener {

    public void search() {
        PromotionLookupDialog dialog = new PromotionLookupDialog();
        dialog.setSearchListener(this);
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignPromotion((PromotionVO) value);
    }

    public abstract void assignPromotion(PromotionVO promotion);
}
