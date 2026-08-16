package oracle.retail.sim.client.screen.supplier;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.source.SourceQueryFilter;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.SupplierVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Supplier Lookup Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierLookupTabModel extends SimScreenModel {
    private Supplier supplier;

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_SUPPLIER_LOOKUP);
    }

    public List<SupplierVO> findSuppliers(SourceQueryFilter filter) throws Exception {
        if (filter.getSearchLimit() < 1) {
            throw new BusinessException(CommonMessageText.SUPPLIER_UNDER_SEARCH_LIMIT);
        }
        if (filter.getSearchLimit() > SimConfigManager.SEARCH_LIMIT_MAX_VALUE) {
            throw new BusinessException(CommonMessageText.SUPPLIER_OVER_SEARCH_LIMIT);
        }
        return ClientServiceFactory.getSourceServices().findSupplierVOs(filter, true);
    }

    public Supplier getSupplier(SupplierVO supplierVO) throws Exception {
        if (supplier == null || !supplier.getId().equals(supplierVO.getId())) {
            supplier = ClientServiceFactory.getSourceServices().readSupplier(supplierVO.getId(), getStoreId(), true);
        }
        if (supplier.isInactive()) {
            throw new BusinessException(CommonMessageText.SUPPLIER_SITE_INACTIVE);
        }
        return supplier;
    }
}
