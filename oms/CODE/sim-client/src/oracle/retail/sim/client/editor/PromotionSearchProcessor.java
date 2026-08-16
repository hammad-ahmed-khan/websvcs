package oracle.retail.sim.client.editor;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.editor.SearchProcessor;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for promotions.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PromotionSearchProcessor implements SearchProcessor {
    private AttributeDisplayer entryDisplayer = new AttributeDisplayer("id");
    private AttributeDisplayer valueDisplayer = new AttributeDisplayer("name");

    public Object searchById(String promotionId) throws Exception {
        PromotionVO promotionVO = null;
        if (NumberHelper.isIdentifierNumeric(promotionId)) {
            promotionVO = ClientServiceFactory.getItemPriceServices().readPromotion(Long.valueOf(promotionId), SimRepository.getStoreId());
        }
        if (promotionVO == null) {
            promotionVO = BOFactory.createPromotionVO(promotionId, PromotionVO.UNKNOWN);
        }
        return promotionVO;
    }

    public BasicDisplayer getEntryDisplayer() {
        return entryDisplayer;
    }

    public BasicDisplayer getValueDisplayer() {
        return valueDisplayer;
    }

    public Object validateData(Object data) {
        return data;
    }
}
