package oracle.retail.sim.client.screen.supplier;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.source.SourceQueryFilter;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.SupplierVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Supplier Lookup Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierLookupModel extends SimScreenModel {
    public List<SupplierVO> findSuppliers(SourceQueryFilter supplierFilter) throws Exception {
        if (supplierFilter.getSearchLimit() < 1) {
            throw new BusinessException(CommonMessageText.SUPPLIER_UNDER_SEARCH_LIMIT);
        }
        if (supplierFilter.getSearchLimit() > SimConfigManager.SEARCH_LIMIT_MAX_VALUE) {
            throw new BusinessException(CommonMessageText.SUPPLIER_OVER_SEARCH_LIMIT);
        }
        return ClientServiceFactory.getSourceServices().findSupplierVOs(supplierFilter, true);
    }

    public void storeSupplier(SupplierVO supplierVO) throws Exception {
        if (supplierVO != null) {
            Supplier supplier = ClientServiceFactory.getSourceServices().readSupplier(supplierVO.getId(), getStoreId(), true);
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_SUPPLIER, supplier);
        }
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_SUPPLIER_LOOKUP);
    }
}
