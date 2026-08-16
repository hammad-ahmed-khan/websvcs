package oracle.retail.sim.client.editor;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.editor.SearchProcessor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for finishers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FinisherSearchProcessor implements SearchProcessor {
    private AttributeDisplayer entryDisplayer = new AttributeDisplayer("id");
    private AttributeDisplayer valueDisplayer = new AttributeDisplayer("name");

    public Object searchById(String finisherId) throws Exception {
        Finisher finisher = ClientServiceFactory.getSourceServices().readFinisher(finisherId, SimRepository.getStoreId());
        if (finisher == null) {
            throw new BusinessException(CommonMessageText.FINISHER_NOT_FOUND, finisherId);
        }
        return finisher;
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
