package oracle.retail.sim.client.screen.itemprice;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.itemprice.ItemPriceQueryFilter;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.PriceType;

/********************************************************************************************************
 * Price Change Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceFilterDialogModel extends SimScreenModel {

    private ItemPriceQueryFilter filter;

    public void setFilter(ItemPriceQueryFilter filter) {
        this.filter = filter;
    }

    public ItemPriceQueryFilter getFilter() {
        return filter;
    }

    public ItemPriceQueryFilter resetFilter() throws BusinessException {
        filter = BOFactory.createItemPriceQueryFilter();
        filter.doSetStoreId(getStoreId());
        //   filter.doSetStatus(ItemPriceStatus.DEFAULT_DISPLAY);
        filter.doSetSearchLimit(getDefaultSearchLimit());
        Date startDate = SimDateUtil.getCurrentDateAtStartOfDay(getTimeZone());
        Date twoWeeks = SimDateUtil.addDays(getTimeZone(), startDate, 7);
        Date endDate = SimDateUtil.getDateAtEndOfDay(getTimeZone(), twoWeeks);
        filter.setEffectiveDateRange(startDate, endDate);
        return filter;
    }

    private Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_PRICE_CHANGE);
    }

    public List<ItemPriceStatus> findItemPriceStatus() {
        return SimEnumUtility.findAllItemPriceStatus();
    }

    public List<PriceType> findPriceDescriptions() {
        return SimEnumUtility.findAllPriceTypes();
    }
}
