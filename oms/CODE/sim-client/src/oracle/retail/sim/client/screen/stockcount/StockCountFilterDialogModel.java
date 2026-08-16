package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.productgroup.StockCountGroupVO;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountQueryFilter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountFilterDialogModel extends SimScreenModel {
    private List<StockCountGroupVO> productGroupVOs = new ArrayList<>();
    private StockCountQueryFilter filter;

    public void setFilter(StockCountQueryFilter filter) {
        this.filter = filter;
    }

    public StockCountQueryFilter getFilter() {
        return filter;
    }

    public StockCountQueryFilter resetFilter() {
        filter = BOFactory.createStockCountQueryFilter();
        filter.doSetStoreId(getStoreId());
        return filter;
    }

    public List<StockCountDisplayStatus> findStockCountStatus() {
        return SimEnumUtility.findAllStockCountStatus();
    }

    public List<StockCountPhase> findAllStockCountPhases() {
        return SimEnumUtility.findAllStockCountPhases();
    }

    public List<StockCountGroupVO> findStockCountGroups() throws Exception {
        if (productGroupVOs.isEmpty()) {
            productGroupVOs = ClientServiceFactory.getProductGroupServices().findStockCountProductGroupVOs(getStoreId());
        }
        return productGroupVOs;
    }

    public StockCountGroupVO findStockCountGroup(Long productGroupId) throws Exception {
        if (productGroupId != null) {
            for (StockCountGroupVO groupVO : findStockCountGroups()) {
                if (productGroupId.equals(groupVO.getId())) {
                    return groupVO;
                }
            }
        }
        return null;
    }
}
