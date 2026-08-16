package oracle.retail.sim.client.screen.billoflading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderContactInfo;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.person.FinisherContactInfo;
import oracle.retail.sim.common.person.PostalAddress;
import oracle.retail.sim.common.person.SupplierContactInfo;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.shipment.BillOfLading;
import oracle.retail.sim.common.shipment.BillOfLadingMotive;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierRole;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import oracle.retail.sim.common.shipment.ShipmentCartonType;
import oracle.retail.sim.common.shipment.ShipmentType;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnStatus;
import oracle.retail.sim.common.store.StoreAddressVO;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * BOL Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BillOfLadingDetailModel extends SimScreenModel {
    private FulfillmentOrder fulfillmentOrder;
    private FulfillmentOrderDelivery orderDelivery;
    private Return stockReturn;
    private Transfer stockTransfer;
    private BillOfLading billOfLading;
    private List<ShipmentCarrier> shipmentCarriers = new ArrayList<>();
    private List<FinisherContactInfo> finisherContactList = new ArrayList<>();
    private List<SupplierContactInfo> supplierContactList = new ArrayList<>();
    private FulfillmentOrderContactInfo orderContactInfo;

    public void loadShipment() throws Exception {
        stockReturn = (Return) RepositoryManager.getStateObject(SimClientStateKey.BILL_OF_LADING_RETURN);
        stockTransfer = (Transfer) RepositoryManager.getStateObject(SimClientStateKey.BILL_OF_LADING_TRANSFER);
        orderDelivery = (FulfillmentOrderDelivery) RepositoryManager.getStateObject(SimClientStateKey.BILL_OF_LADING_FULFILLMENT_ORDER_DELIVERY);
        fulfillmentOrder = (FulfillmentOrder) RepositoryManager.getStateObject(SimClientStateKey.BILL_OF_LADING_FULFILLMENT_ORDER);
        RepositoryManager.removeStateObject(SimClientStateKey.BILL_OF_LADING_RETURN);
        RepositoryManager.removeStateObject(SimClientStateKey.BILL_OF_LADING_TRANSFER);
        RepositoryManager.removeStateObject(SimClientStateKey.BILL_OF_LADING_FULFILLMENT_ORDER_DELIVERY);
        loadShipmentCarriers();
        loadBillOfLading();
        loadFinisherContactList();
        loadSupplierContactList();
        loadCustomerAddress();
    }

    private void loadShipmentCarriers() throws Exception {
        shipmentCarriers = ClientServiceFactory.getShipmentServices().findAllCarriers();
    }

    private void loadBillOfLading() throws Exception {
        if (isReturn()) {
            billOfLading = stockReturn.getBillOfLading();
        }
        if (isTransfer()) {
            billOfLading = stockTransfer.getBillOfLading();
        }
        if (isOrderDelivery()) {
            billOfLading = orderDelivery.getBillOfLading();
        }
        if (billOfLading == null) {
            billOfLading = BOFactory.createBillOfLading();
        }
    }

    private void loadSupplierContactList() throws Exception {
        if (isVendorReturn()) {
            Supplier supplier = (Supplier) stockReturn.getDestination();
            supplierContactList = ClientServiceFactory.getSourceServices().readSupplierContactInfo(supplier.getId());
        }
    }

    private void loadFinisherContactList() throws Exception {
        if (isFinisherReturn()) {
            Finisher finisher = (Finisher) stockReturn.getDestination();
            finisherContactList = ClientServiceFactory.getSourceServices().readFinisherContactInfo(finisher.getId());
        }
    }

    private void loadCustomerAddress() throws Exception {
        if (isOrderDelivery()) {
            orderContactInfo = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrderContactInfo(fulfillmentOrder.getId());
        }
    }

    public FulfillmentOrderDelivery getFulfillmentOrderDelivery() {
        return orderDelivery;
    }

    public Return getStockReturn() {
        return stockReturn;
    }

    public Transfer getTransfer() {
        return stockTransfer;
    }

    public boolean isReturn() {
        return stockReturn != null;
    }

    public boolean isTransfer() {
        return stockTransfer != null;
    }

    public boolean isOrderDelivery() {
        return orderDelivery != null;
    }

    public String getShipmentIdTitle() {
        if (isReturn()) {
            return "Return ID";
        } else if (isTransfer()) {
            return "Transfer ID";
        }
        return "Delivery ID";
    }

    public Long getShipmentId() {
        if (isReturn()) {
            return stockReturn.getId();
        } else if (isTransfer()) {
            return stockTransfer.getId();
        }
        return orderDelivery.getId();
    }

    public Long getBillOfLadingId() {
        return billOfLading.getId();
    }

    public Date getCreateDate() {
        if (isReturn()) {
            return stockReturn.getCreateDate();
        } else if (isTransfer()) {
            return stockTransfer.getCreateDate();
        }
        return orderDelivery.getCreateDate();
    }

    public Date getDispatchDate() {
        if (isReturn()) {
            return stockReturn.getDispatchDate();
        } else if (isTransfer()) {
            return stockTransfer.getShippedDate();
        }
        return orderDelivery.getDispatchDate();
    }

    public AddressType getShipToAddressType() {
        if (billOfLading != null) {
            return billOfLading.getShipToAddressType();
        }
        if (isReturn()) {
            return getReturnShipToAddressType();
        }
        return AddressType.DELIVERY;
    }

    private AddressType getReturnShipToAddressType() {
        AddressType addressType = billOfLading.getShipToAddressType();
        if (addressType == AddressType.RETURNS) {
            if (isFinisherReturn() && getFinisherShipToAddress(addressType) == null) {
                addressType = null;
            }
            if (isVendorReturn() && getVendorShipToAddress(addressType) == null) {
                addressType = null;
            }
        }
        if (addressType == null) {
            return AddressType.BUSINESS;
        }
        return addressType;
    }

    public String getShipFromAddressName() {
        if (isReturn()) {
            return getStore().toDisplayString();
        } else if (isTransfer()) {
            return stockTransfer.getSendingStore().toDisplayString();
        }
        return getStore().toDisplayString();
    }

    public PostalAddress getShipFromAddress() throws Exception {
        if (isReturn()) {
            return getStoreAddress(stockReturn.getStoreId());
        } else if (isTransfer()) {
            return getStoreAddress(stockTransfer.getSendingStore().getId());
        }
        return getStoreAddress(orderDelivery.getStoreId());
    }

    public String getShipToAddressName() {
        if (isReturn()) {
            Source source = stockReturn.getDestination();
            return source.getId() + " - " + source.getName();
        } else if (isTransfer()) {
            return stockTransfer.getReceivingStore().toDisplayString();
        } else if (isOrderDelivery()) {
            if (orderContactInfo.getDeliveryAddress() != null) {
                return orderContactInfo.getDeliveryAddress().getDisplayName();
            }
        }
        return null;
    }

    public String getShipToAddressPhoneticName() {
        if (isOrderDelivery()) {
            if (orderContactInfo.getDeliveryAddress() != null) {
                return orderContactInfo.getDeliveryAddress().getPhoneticDisplayName();
            }
        }
        return null;
    }

    public PostalAddress getShipToAddress(AddressType addressType) throws Exception {
        if (isVendorReturn()) {
            return getVendorShipToAddress(addressType);
        }
        if (isFinisherReturn()) {
            return getFinisherShipToAddress(addressType);
        }
        if (isWarehouseReturn()) {
            return null;
        }
        if (isTransfer()) {
            return getStoreAddress(stockTransfer.getReceivingStore().getId());
        }
        if (isOrderDelivery()) {
            if (orderContactInfo.getDeliveryAddress() != null) {
                return orderContactInfo.getDeliveryAddress().getPostalAddress();
            }
        }
        return null;
    }

    private PostalAddress getVendorShipToAddress(AddressType addressType) {
        Supplier supplier = (Supplier) stockReturn.getDestination();
        if (supplier != null) {
            for (SupplierContactInfo contactInfo : supplierContactList) {
                if (addressType == contactInfo.getAddrType()) {
                    return contactInfo.getContactInfo().getAddress();
                }
            }
        }
        return null;
    }

    private PostalAddress getFinisherShipToAddress(AddressType addressType) {
        Finisher finisher = (Finisher) stockReturn.getDestination();
        if (finisher != null) {
            for (FinisherContactInfo contactInfo : finisherContactList) {
                if (addressType == contactInfo.getAddrType()) {
                    return contactInfo.getContactInfo().getAddress();
                }
            }
        }
        return null;
    }

    public BillOfLadingMotive getBillOfLadingMotive() {
        return BOFactory.createBillOfLadingMotive(billOfLading.getMotiveId(), "");
    }

    public ShipmentCarrierRole getCarrierType() {
        return billOfLading.getCarrierRole();
    }

    public String getAlternameCarrierName() {
        return billOfLading.getAlternateCarrierName();
    }

    public String getAlternateCarrierAddress() {
        return billOfLading.getAlternateCarrierAddress();
    }

    public String getBillOfLadingTaxId() {
        return billOfLading.getTaxId();
    }

    public String getAlternateShipToAddress() {
        return billOfLading.getAlternateShipToAddress();
    }

    public Date getRequestedPickupDate() {
        return billOfLading.getRequestedPickupDate();
    }

    public boolean isVendorReturn() {
        return stockReturn != null && stockReturn.isVendorReturn();
    }

    public boolean isFinisherReturn() {
        return stockReturn != null && stockReturn.isFinisherReturn();
    }

    public boolean isWarehouseReturn() {
        return stockReturn != null && stockReturn.isWarehouseReturn();
    }

    public boolean isReturnEditable() {
        if (hasPermission(PermissionKey.PC_EDIT_RETURN_BOL) && stockReturn.getStatus() == ReturnStatus.PENDING) {
            return true;
        }
        return false;
    }

    // Only allow modify BOL if transfer is in edit mode
    public boolean isTransferEditable() {
        Boolean viewOnly = (Boolean) RepositoryManager.getStateObject(SimClientStateKey.TRANSFER_VIEW_ONLY_MODE);
        if (Boolean.TRUE.equals(viewOnly)) {
            return false;
        }
        if (!hasPermission(PermissionKey.PC_EDIT_TRANSFER_BOL)) {
            return false;
        }
        return stockTransfer.getStatus() == TransferStatus.IN_PROGRESS;
    }

    public boolean isDeliveryEditable() {
        return hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_BOL) && orderDelivery.getStatus() == FulfillmentOrderDeliveryStatus.IN_PROGRESS;
    }

    public List<AddressType> getShipToAddressTypes() {
        List<AddressType> addresses = new ArrayList<>();
        if (isVendorReturn()) {
            for (SupplierContactInfo contactInfo : supplierContactList) {
                addresses.add(contactInfo.getAddrType());
            }
        }
        if (isFinisherReturn()) {
            for (FinisherContactInfo contactInfo : finisherContactList) {
                addresses.add(contactInfo.getAddrType());
            }
        }
        return addresses;
    }

    public boolean isEditableTaxId() {
        if (isVendorReturn() && isReturnEditable()) {
            Supplier supplier = (Supplier) stockReturn.getDestination();
            return supplier.getTaxId() == null;
        }
        return false;
    }

    public List<BillOfLadingMotive> getAvailableMotives() throws Exception {
        if (isReturn()) {
            return ClientServiceFactory.getShipmentServices().findMotives(ShipmentType.RETURN);
        }
        if (isTransfer()) {
            return ClientServiceFactory.getShipmentServices().findMotives(ShipmentType.TRANSFER);
        }
        return ClientServiceFactory.getShipmentServices().findMotives(ShipmentType.FULFILLMENT_ORDER_DELIVERY);
    }

    public List<ShipmentCarrier> getAvailableCarriers() throws Exception {
        return shipmentCarriers;
    }

    public List<ShipmentCarrierService> getCarrierServices(ShipmentCarrier carrier) throws Exception {
        if (carrier != null) {
            return ClientServiceFactory.getShipmentServices().findCarrierServices(carrier.getId());
        }
        return Collections.emptyList();
    }

    public ShipmentCarrier getCarrier() {
        return billOfLading.getCarrier();
    }

    public boolean isOtherCarrier(ShipmentCarrier carrier) {
        if (carrier == null) {
            return false;
        }
        return ShipmentCarrier.OTHER_CARRIER_CODE.equals(carrier.getCode());
    }

    public ShipmentCarrierService getCarrierService() {
        return billOfLading.getCarrierService();
    }

    public String getTrackingNumber() {
        return billOfLading.getTrackingNumber();
    }

    public Quantity getWeight() {
        return billOfLading.getWeight();
    }

    public String getWeightUom() {
        return billOfLading.getWeightUom();
    }

    public String getWeightUomLabel() {
        if (StringHelper.isNullOrEmpty(billOfLading.getWeightUom())) {
            billOfLading.doSetWeightUom(SimConfigManager.getStoreString(StoreConfigKeys.MANIFEST_WEIGHT_UOM, getStoreId()));
        }
        return billOfLading.getWeightUom();
    }

    public ShipmentCartonType getCartonType() {
        return billOfLading.getCartonType();
    }

    public List<ShipmentCartonType> getCartonTypes() throws Exception {
        return ClientDataCacheUtility.getCartonTypes(getStoreId());
    }

    public void setTrackingNumber(String trackingNumber) throws BusinessException {
        billOfLading.setTrackingNumber(trackingNumber);
    }

    public void setWeight(Quantity weight) throws BusinessException {
        billOfLading.setWeight(weight);
    }

    public void setShipToAddressType(AddressType addressType) throws BusinessException {
        billOfLading.setShipToAddressType(addressType);
    }

    public void setBillOfLadingMotive(BillOfLadingMotive motive) throws BusinessException {
        if (motive != null) {
            billOfLading.setMotiveId(motive.getId());
        }
    }

    public void setCarrierType(ShipmentCarrierRole carrierType) throws BusinessException {
        billOfLading.setCarrierRole(carrierType);
    }

    public void setCarrier(ShipmentCarrier carrier) throws BusinessException {
        billOfLading.setCarrier(carrier);
    }

    public void setCarrierService(ShipmentCarrierService carrierService) throws BusinessException {
        billOfLading.setCarrierService(carrierService);
    }

    public void setCartonType(ShipmentCartonType cartonType) throws BusinessException {
        billOfLading.setCartonType(cartonType);
    }

    public void setAlternateCarrierName(String carrierName) throws BusinessException {
        billOfLading.setAlternateCarrierName(carrierName);
    }

    public void setAlternateCarrierAddress(String carrierAddress) throws BusinessException {
        billOfLading.setAlternateCarrierAddress(carrierAddress);
    }

    public void setSupplierTaxId(String supplierTaxId) throws BusinessException {
        if (isReturn()) {
            billOfLading.setTaxId(supplierTaxId);
        }
    }

    public void setAlternateShipToAddress(String shipToAddress) throws BusinessException {
        billOfLading.setAlternateShipToAddress(shipToAddress);
    }

    public void setRequestedPickupDate(Date pickupDate) throws BusinessException {
        if (pickupDate != null) {
            Date createDate = getCreateDate();
            if (!SimDateUtil.isSameDay(getTimeZone(), createDate, pickupDate) && !SimDateUtil.isValidDateRange(createDate, pickupDate)) {
                throw new BusinessException(CommonMessageText.REQUESTED_PICKUP_DATE_IN_PAST_ERROR);
            }
            billOfLading.setRequestedPickupDate(pickupDate);
        }
    }

    public void storeShipment() {
        if (isReturn()) {
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_RETURN, stockReturn);
        } else if (isTransfer()) {
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_TRANSFER, stockTransfer);
        } else {
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_DELIVERY, orderDelivery);
        }
    }

    private PostalAddress getStoreAddress(Long storeId) throws Exception {
        StoreAddressVO storeAddressVo = ClientServiceFactory.getStoreServices().selectStoreAddress(storeId);

        PostalAddress postalAddress = BOFactory.createPostalAddress();
        postalAddress.setAddressLine1(storeAddressVo.getAddressLine1());
        if (!storeAddressVo.getAddressLine2().isEmpty()) {
            postalAddress.setAddressLine2(storeAddressVo.getAddressLine2());
        }
        postalAddress.setCity(storeAddressVo.getCity());
        //PostalCode can be empty for Non-US Address and its a Nullable Column
        if (!storeAddressVo.getZipCode().isEmpty()) {
            postalAddress.setPostalCode(storeAddressVo.getZipCode());
        }
        //State can be empty for Non-US Address and its a Nullable Column
        if (!storeAddressVo.getState().isEmpty()) {
            postalAddress.setState(storeAddressVo.getState());
        }
        return postalAddress;
    }

    public ShipmentCarrier getOtherCarrier() throws Exception {
        return ClientServiceFactory.getShipmentServices().findCarrier(ShipmentCarrier.OTHER_CARRIER_CODE);
    }

}
