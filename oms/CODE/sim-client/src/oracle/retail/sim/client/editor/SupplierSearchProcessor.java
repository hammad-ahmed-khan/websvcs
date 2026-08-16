package oracle.retail.sim.client.editor;

import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.editor.SearchProcessor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Search process implementation for suppliers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class SupplierSearchProcessor implements SearchProcessor {
    private AttributeDisplayer entryDisplayer = new AttributeDisplayer("id");
    private AttributeDisplayer valueDisplayer = new AttributeDisplayer("name");

    private boolean allowInactiveSupplier = true;

    public SupplierSearchProcessor() {
    }

    public SupplierSearchProcessor(boolean allowInactiveSupplier) {
        this.allowInactiveSupplier = allowInactiveSupplier;
    }

    public Object searchById(String supplierId) throws Exception {
        Supplier supplier = ClientServiceFactory.getSourceServices().readSupplier(supplierId, SimRepository.getStoreId(), true);
        if (supplier == null) {
            throw new BusinessException(CommonMessageText.SUPPLIER_NOT_FOUND, supplierId);
        }
        if (!allowInactiveSupplier && supplier.isInactive()) {
            throw new BusinessException(CommonMessageText.SUPPLIER_SITE_INACTIVE);
        }
        return supplier;
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
