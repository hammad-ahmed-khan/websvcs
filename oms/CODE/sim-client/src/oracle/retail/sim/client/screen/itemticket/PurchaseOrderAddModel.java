package oracle.retail.sim.client.screen.itemticket;

import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.item.ItemSuppCtryMfrVO;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Purchase Order Add Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderAddModel extends SimScreenModel {
    public List<DirectDeliveryVO> findDeliveries(String purchaseOrderExternalId) throws Exception {
        if (purchaseOrderExternalId == null) {
            return Collections.emptyList();
        }
        DirectDeliveryQueryFilter filter = BOFactory.createDirectDeliveryQueryFilter();
        filter.doSetStoreId(getStoreId());
        filter.doSetPurchaseOrderExternalId(purchaseOrderExternalId);
        return ClientServiceFactory.getDirectDeliveryServices().findDirectDeliveryVOs(filter, false);
    }

    public void applyDelivery(String purchaseOrderExternalId, DirectDeliveryVO deliveryVO) throws Exception {
        DirectDelivery delivery = ClientServiceFactory.getDirectDeliveryServices().readDirectDelivery(deliveryVO.getId());
        for (DirectDeliveryLineItem lineItem : delivery.getLineItems()) {
            createItemTicket(purchaseOrderExternalId, lineItem);
        }
    }

    private void createItemTicket(String purchaseOrderExternalId, DirectDeliveryLineItem lineItem) throws Exception {
        Quantity receivedQty = lineItem.getQuantityReceived();
        if (receivedQty == null || receivedQty.isZero()) {
            return;
        }

        StockItem stockItem = lineItem.getStockItem();
        RetailItem retailItem = ClientServiceFactory.getItemServices().readRetailItem(stockItem.getId(), stockItem.getStoreId());

        ItemTicket itemTicket = BOFactory.createItemTicket(retailItem);
        itemTicket.setExternalPoId(purchaseOrderExternalId);
        itemTicket.setQuantity(receivedQty.getBigDecimal().intValue());
        itemTicket.setUserId(getUserName());
        itemTicket.doSetTicketTypeFormat(null);

        String suggestedFormat = retailItem.getSuggestedTicketTypeCode();
        for (TicketTypeFormat typeFormat : ClientDataCacheUtility.getItemTicketFormats()) {
            if (typeFormat.getFormatName().equalsIgnoreCase(suggestedFormat)) {
                itemTicket.setTicketTypeFormat(typeFormat);
                break;
            }
        }
        ItemSuppCtryMfrVO countryOfManufacture = ClientServiceFactory.getItemServices().findDefaultCountryOfManufacture(retailItem.getId(), retailItem.getStoreId());
        if (countryOfManufacture != null) {
            itemTicket.setCountryManufacture(countryOfManufacture.getCountryId());
        }
        ClientServiceFactory.getItemTicketServices().createItemTicket(itemTicket);
    }
}
