package oracle.retail.sim.client.screen.fulfillmentorder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtListVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderTranType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Management List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderMgmtListModel extends SimScreenModel {

    /**
     * @return Returns Customer Order Management List Wrapper objects to be displayed in the panel.
     * @throws Exception Thrown if an exception occurs on the server retrieving the data.
     */
    public List<FulfillmentOrderMgmtListWrapper> findCustomerOrderListVOs() throws Exception {
        List<FulfillmentOrderMgmtListWrapper> wrappers = new ArrayList<>();

        for (FulfillmentOrderMgmtListVO customerOrderListVO : ClientServiceFactory.getFulfillmentOrderServices().findFulfillmentOrderMgmtListVOs(getFilter())) {
            wrappers.add(ClientWrapperFactory.createFulfillmentOrderMgmtListWrapper(customerOrderListVO));
        }
        return wrappers;
    }

    /**
     * @return Returns query object for the filter dialog. It retrieves it from the Repository Manager if an object is alive in the context, else it returns a new object.
     * @throws BusinessException 
     */
    public FulfillmentOrderMgmtQueryFilter getFilter() throws BusinessException {
        FulfillmentOrderMgmtQueryFilter filter = (FulfillmentOrderMgmtQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_MANAGEMENT_FILTER);
        if (filter == null) {
            filter = BOFactory.createFulfillmentOrderMgmtQueryFilter();
            filter.doSetStoreId(SimRepository.getStoreId());
            filter.setSearchLimit(getDefaultSearchLimit());
            filter.setStatus(FulfillmentOrderMgmtQueryStatus.OPEN);
            RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_MANAGEMENT_FILTER, filter);
        }
        return filter;
    }

    /**
     * This method brings the system out of the Fulfillment Order Management Filter state.
     */
    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_MANAGEMENT_FILTER);
    }

    /**
     * @return Returns the default search limit set for Customer Order Management.
     */
    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_CUSTOMER_ORDER_MGMT);
    }

    /**
     * @return Returns map of values used to created a formatted string of the filtered values.
     * @throws BusinessException 
     */
    public Map<String, String> getDescriptionMap() throws BusinessException {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        FulfillmentOrderMgmtQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getCustomerOrderId() != null) {
            descriptionMap.put("Customer Order ID", filter.getCustomerOrderId());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", filter.getStatus().toString());
        }
        if (filter.getTranType() != null) {
            descriptionMap.put("Tran Type", Translator.getText(filter.getTranType().toString()));
        }
        SimEnum<?> status = getTranStatus(filter);
        if (status != null) {
            descriptionMap.put("Tran Status", Translator.getText(status.toString()));
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getUserId() != null) {
            descriptionMap.put("User", filter.getUserId());
        }
        if (filter.getTranId() != null) {
            descriptionMap.put("Tran ID", filter.getTranId());
        }
        if (filter.getShipmentCarrier() != null) {
            descriptionMap.put("Carrier", Translator.getText(filter.getShipmentCarrier().getDescription()));
        }
        if (filter.getShipmentCarrierService() != null) {
            descriptionMap.put("Service", Translator.getText(filter.getShipmentCarrierService().getDescription()));
        }
        return descriptionMap;
    }

    private SimEnum<?> getTranStatus(FulfillmentOrderMgmtQueryFilter filter) {
        Integer tranCode = filter.getTranStatusCode();
        if (tranCode != null) {
            List<? extends SimEnum<?>> statusList = findTranStatusList(filter.getTranType());
            for (SimEnum<?> status : statusList) {
                if (tranCode.equals(status.getCode())) {
                    return status;
                }
            }
        }
        return null;
    }

    private List<? extends SimEnum<?>> findTranStatusList(FulfillmentOrderTranType tranType) {
        switch (tranType) {
            case DIRECT_DELIVERY:
                return SimEnumUtility.findAllDirectDeliveryStatus();
            case WAREHOUSE_DELIVERY:
                return SimEnumUtility.findAllWarehouseDeliveryStatus();
            case CUSTOMER_ORDER:
                return SimEnumUtility.findAllCustomerOrderStatus();
            case PICK:
                return SimEnumUtility.findAllFulfillmentOrderPickStatus();
            case CUSTOMER_ORDER_DELIVERY:
                return SimEnumUtility.findAllFulfillmentOrderDeliveryStatus();
            case TRANSFER:
                return SimEnumUtility.findAllFulfillmentOrderTransferQueryStatus();
            default:
                return Collections.emptyList();
        }
    }
}
