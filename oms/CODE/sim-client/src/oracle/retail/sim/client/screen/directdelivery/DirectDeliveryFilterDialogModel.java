package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.directdelivery.DirectDeliveryInvoiceEntryType;
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Direct Delivery Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DirectDeliveryFilterDialogModel extends SimScreenModel {
    private DirectDeliveryQueryFilter filter;
    private List<String> userIds;

    public DirectDeliveryQueryFilter getFilter() {
        return filter;
    }

    public void setFilter(DirectDeliveryQueryFilter filter) {
        this.filter = filter;
    }

    public DirectDeliveryQueryFilter resetFilter() {
        filter = BOFactory.createDirectDeliveryQueryFilter();
        filter.doSetStoreId(getStoreId());
        filter.doSetStatus(DirectDeliveryStatus.ACTIVE);
        return filter;
    }

    public boolean isInvoiceEntryEnabled() {
        DirectDeliveryInvoiceEntryType invoiceEntryType = null;
        Integer value = SimConfigManager.getStoreInteger(StoreConfigKeys.DIRECT_DELIVERY_INVOICE_ENTRY, getStoreId());
        if (value != null) {
            invoiceEntryType = DirectDeliveryInvoiceEntryType.toValue(value);
        }
        return invoiceEntryType != DirectDeliveryInvoiceEntryType.DISABLED;
    }

    public List<DirectDeliveryStatus> getDirectDeliveryStatusList() {
        List<DirectDeliveryStatus> statusList = new ArrayList<DirectDeliveryStatus>(5);
        statusList.add(DirectDeliveryStatus.ACTIVE);
        statusList.add(DirectDeliveryStatus.IN_PROGRESS);
        statusList.add(DirectDeliveryStatus.RECEIVED);
        statusList.add(DirectDeliveryStatus.DEXNEX);
        statusList.add(DirectDeliveryStatus.REJECTED);
        return statusList;
    }

    public List<String> findUserIds() throws Exception {
        if (userIds == null) {
            userIds = ClientServiceFactory.getDirectDeliveryServices().findDirectDeliveryUserIds(getStoreId());
        }
        return userIds;
    }
}
