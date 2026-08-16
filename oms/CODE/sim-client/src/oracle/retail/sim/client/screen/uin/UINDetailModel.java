package oracle.retail.sim.client.screen.uin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.uin.UINAvailability;
import oracle.retail.sim.common.uin.UINDetail;
import oracle.retail.sim.common.uin.UINDetailLookupQueryFilter;
import oracle.retail.sim.common.uin.UINDetailVO;
import oracle.retail.sim.common.uin.UINStoreItem;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * UIN Detail Display Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINDetailModel extends SimScreenModel {
    private ItemDetailVO itemDetailVO;
    private UINStoreItem uinStoreItem;

    public void loadItem() {
        itemDetailVO = (ItemDetailVO) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
    }

    public StockItem getStockItem() {
        return itemDetailVO.getStockItem();
    }

    public boolean isSerialNumberType() {
        return itemDetailVO.getStockItem().getUINType() == UINType.SERIAL;
    }

    public String getCaptureTimeDescription() throws Exception {
        if (uinStoreItem == null) {
            uinStoreItem = ClientServiceFactory.getUINServices().readUINStoreItem(itemDetailVO.getId(), itemDetailVO.getStoreId());
        }
        if (uinStoreItem != null) {
            if (uinStoreItem.getCaptureTime() != null) {
                return uinStoreItem.getCaptureTime().toString();
            }
        }
        return StringConstants.EMPTY;
    }

    public UINDetailLookupQueryFilter getFilter() {
        UINDetailLookupQueryFilter filter = (UINDetailLookupQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.ITEM_UIN_FILTER);
        if (filter == null) {
            filter = BOFactory.createUINDetailLookupQueryFilter();
            filter.doSetItemId(itemDetailVO.getId());
            filter.doSetStoreId(getStoreId());
            filter.doSetAvailability(UINAvailability.OPEN);
            filter.doSetSearchLimit(getDefaultSearchLimit());
            RepositoryManager.addStateObject(SimClientStateKey.ITEM_UIN_FILTER, filter);
        }
        return filter;
    }

    public List<UINDetailVO> findUINDetailLookupVOs() throws Exception {
        return ClientServiceFactory.getUINServices().findUINDetailVOs(getFilter());
    }

    public void storeUINDetail(UINDetailVO containerVO) throws Exception {
        UINDetail detail = ClientServiceFactory.getUINServices().readUINDetail(containerVO.getUINDetailId());
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_UIN, detail);
    }

    public ReportResponse printTickets(List<UINDetailVO> detailContainerVOs) throws Exception {
        Map<String, List<String>> itemIdsAndUins = new ConcurrentHashMap<>();
        List<String> uins = new ArrayList<>();
        for (UINDetailVO detailContainerVO : detailContainerVOs) {
            uins.add(detailContainerVO.getUin());
        }
        itemIdsAndUins.put(itemDetailVO.getId(), uins);
        return ClientServiceFactory.getUINServices().printUINAgsnDetails(itemIdsAndUins, getStoreId(), null, null);
    }

    private Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_CONTAINER_LOOKUP);
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<>();
        UINDetailLookupQueryFilter filter = getFilter();
        if (filter.getSerialNumber() != null) {
            descriptionMap.put("UIN", filter.getSerialNumber());
        }
        if (filter.getAvailibility() != null) {
            descriptionMap.put("Availability", Translator.getText(filter.getAvailibility().toString()));
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
        }
        if (filter.getFunctionalArea() != null) {
            descriptionMap.put("Functional Area", Translator.getText(filter.getFunctionalArea().toString()));
        }
        if (filter.getFunctionalAreaId() != null) {
            descriptionMap.put("Identifier", filter.getFunctionalAreaId());
        }
        if (filter.isDamagedOnly()) {
            descriptionMap.put("Damaged Only", new BooleanDisplayer().getDisplayText(Boolean.TRUE));
        }
        descriptionMap.put("Search Limit", LocaleManager.getIntegerFormatter().format(filter.getSearchLimit()));
        return descriptionMap;
    }
}
