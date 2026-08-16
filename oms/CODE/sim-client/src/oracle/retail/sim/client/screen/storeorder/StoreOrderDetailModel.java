package oracle.retail.sim.client.screen.storeorder;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.reportrequest.StoreOrderReportRequest;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.common.storeorder.StoreOrderMessageText;
import oracle.retail.sim.common.storeorder.StoreOrderState;
import oracle.retail.sim.common.storeorder.StoreOrderStatus;
import oracle.retail.sim.common.storeorder.StoreOrderType;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.item.ItemServices;

public class StoreOrderDetailModel extends SimScreenModel {
  private StoreOrder storeOrder;
  
  private boolean newStoreOrder;
  
  private List<StoreOrderLineItem> rangedItemList = new ArrayList<>();
  
  private List<StoreOrderLineItem> nonRangedItemList = new ArrayList<>();
  
  public boolean hasSelectedStoreOrder() {
    return (RepositoryManager.getStateObject("STORE_ORDER_DETAIL") != null);
  }
  
  public boolean hasStoreOrder() {
    return (this.storeOrder != null);
  }
  
  public void loadNewStoreOrder() throws BusinessException {
    this.newStoreOrder = true;
    try {
      this.storeOrder = BOFactory.createStoreOrder();
      this.storeOrder.setCreationUser(getUserName());
      this.storeOrder.setStatus(StoreOrderStatus.PENDING);
      this.storeOrder.setToLocation(getStore());
      if (this.storeOrder.getOrderState() == StoreOrderState.CREATE) {
        this.storeOrder.doSetNotBeforeDate(SimDateUtil.getTomorrowAtStartOfDay(getTimeZone()));
        if (this.storeOrder.getType() == StoreOrderType.TRANSFER)
          this.storeOrder.doSetNotAfterDate(SimDateUtil.getTomorrowAtStartOfDay(getTimeZone())); 
      } 
      this.storeOrder.setCreationDate(SimDateUtil.getCurrentDate());
      this.storeOrder.removeAllStoreOrderLineItem();
    } catch (BusinessException businessException) {
      this.storeOrder = null;
      throw businessException;
    } 
  }
  
  public void loadExistingStoreOrder() throws BusinessException {
    this.storeOrder = (StoreOrder)RepositoryManager.getStateObject("STORE_ORDER_DETAIL");
    RepositoryManager.removeStateObject("STORE_ORDER_DETAIL");
    this.storeOrder.doSetToLocation(getStore());
    this.newStoreOrder = StringUtility.isNullOrEmpty(this.storeOrder.getStoreOrderNumber());
    if (this.storeOrder.getLineItems().size() == 0)
      try {
        this.storeOrder = ClientServiceFactory.getStoreOrderServices().updateStoreOrderLineItems(this.storeOrder);
      } catch (Exception exception) {
        throw new BusinessException(StoreOrderMessageText.LINE_ITEM_RETRIEVE_FAILED);
      }  
  }
  
  public StoreOrder getStoreOrder() {
    return this.storeOrder;
  }
  
  public boolean isNewStoreOrder() {
    return this.newStoreOrder;
  }
  
  public boolean isCoherent(StoreOrder paramStoreOrder) throws BusinessException {
    return paramStoreOrder.isCoherent(getTimeZone());
  }
  
  public boolean isStoreOrderUnmodifiable() {
    return !hasPermission("PC_EDIT_STORE_ORDER") ? (!isNewStoreOrder()) : false;
  }
  
  public boolean isAddItemUnavailable() {
    return (!isNewStoreOrder() && !hasPermission("PC_ADD_ITEM_STORE_ORDER"));
  }
  
  public boolean isApproveUnavailable() {
    return !hasPermission("PC_APPROVE_STORE_ORDER");
  }
  
  public boolean isTSFStoreOrder() {
    return (this.storeOrder.getType() == StoreOrderType.TRANSFER);
  }
  
  public boolean isStoreOrderClosed() {
    return (this.storeOrder.getStatus() == StoreOrderStatus.CLOSED);
  }
  
  public boolean isStoreOrderApproved() {
    return (this.storeOrder.getStatus() == StoreOrderStatus.APPROVED);
  }
  
  public boolean isPOStoreOrder() {
    return (this.storeOrder == null || this.storeOrder.getType() == StoreOrderType.PURCHASE_ORDER);
  }
  
  public boolean isUnitCostEnterable() {
    return (isPOStoreOrder() && SimConfigManager.getBoolean("DISPLAY_UNIT_COST_FOR_DIRECT_DELIVERIES"));
  }
  
  public boolean isPackSizeEnabled() {
    return !SimConfigManager.getBoolean("DISABLE_PACK_SIZE");
  }
  
  public boolean isScannerAvailable() {
    return !isStoreOrderUnmodifiable();
  }
  
  public boolean isAvailableFromSupplier(OrderItem paramOrderItem) throws Exception {
    Source source = this.storeOrder.getFromLocation();
    List<SupplierItem> findSupplierItems = ClientServiceFactory.getItemServices().findSupplierItems(paramOrderItem.getId(), source.getId());
    for (SupplierItem supplierItem : findSupplierItems) {
      if (supplierItem.getSupplierId().equals(source.getId()))
        return true; 
    } 
    return false;
  }
  
  public boolean isSuppliedByWarehouse(OrderItem paramOrderItem) throws Exception {
    return ClientServiceFactory.getItemServices().isItemShippedByWarehouse(paramOrderItem.getId(), this.storeOrder.getFromLocation().getId());
  }
  
  public String getToLocationInfo() {
    Store store = this.storeOrder.getToLocation();
    return (store == null) ? "" : (store.getId() + " - " + store.getName());
  }
  
  public List<StoreOrderLineItemWrapper> findLineItems() throws Exception {
    this.rangedItemList.clear();
    this.nonRangedItemList.clear();
    Long long_ = this.storeOrder.getToLocation().getId();
    ArrayList<StoreOrderLineItem> arrayList = new ArrayList<>();
    ItemServices itemServices = ClientServiceFactory.getItemServices();
    for (StoreOrderLineItem storeOrderLineItem : this.storeOrder.getLineItems()) {
      try {
        if (itemServices.isRanged(storeOrderLineItem.getOrderItem().getId(), long_)) {
          this.rangedItemList.add(storeOrderLineItem);
          continue;
        } 
        this.nonRangedItemList.add(storeOrderLineItem);
      } catch (NullPointerException nullPointerException) {
        arrayList.add(storeOrderLineItem);
      } 
    } 
    for (StoreOrderLineItem storeOrderLineItem : arrayList)
      this.storeOrder.removeStoreOrderLineItem(storeOrderLineItem); 
    ArrayList<StoreOrderLineItemWrapper> arrayList1 = new ArrayList();
    for (StoreOrderLineItem storeOrderLineItem : this.rangedItemList)
      arrayList1.add(ClientWrapperFactory.createStoreOrderLineItemWrapper(storeOrderLineItem, this.storeOrder)); 
    return arrayList1;
  }
  
  public List<StoreOrderLineItem> getNonRangedItems() {
    return this.nonRangedItemList;
  }
  
  public void storeStoreOrderForItemOrders(StoreOrderLineItemWrapper paramStoreOrderLineItemWrapper) {
    RepositoryManager.addStateObject("STORE_ORDER_DETAIL", this.storeOrder);
    RepositoryManager.addStateObject("SELECTED_ITEM", paramStoreOrderLineItemWrapper.getLineItem());
  }
  
  public StoreOrderLineItemWrapper buildNewStoreOrderLineItem() throws BusinessException {
    StoreOrderLineItem storeOrderLineItem = BOFactory.createStoreOrderLineItem();
    StoreOrderLineItemWrapper storeOrderLineItemWrapper = ClientWrapperFactory.createStoreOrderLineItemWrapper(storeOrderLineItem, this.storeOrder);
    this.storeOrder.addLineItem(storeOrderLineItem);
    return storeOrderLineItemWrapper;
  }
  
  public List<Warehouse> findAllWarehouses() throws Exception {
    return new ArrayList<>(ClientDataCacheUtility.getAllWarehouses().values());
  }
  
  public void storeFromLocation() {
    Source source = this.storeOrder.getFromLocation();
    if (source instanceof oracle.retail.sim.common.source.Supplier)
      RepositoryManager.addStateObject("SELECTED_SUPPLIER", source); 
    if (source instanceof Warehouse)
      RepositoryManager.addStateObject("SELECTED_WAREHOUSE", source); 
  }
  
  public void clearStoreOrder() {
    this.storeOrder = null;
    this.newStoreOrder = false;
    this.rangedItemList.clear();
    this.nonRangedItemList.clear();
  }
  
  public void printStoreOrder() throws Exception {
    ClientServiceFactory.getStoreOrderServices().createTempRecordsForPrint(this.storeOrder, getStoreId());
    if (this.storeOrder != null) {
      List list = SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.STORE_ORDER);
      if (list != null && list.size() > 0) {
        StoreOrderReportRequest storeOrderReportRequest = BOFactory.createStoreOrderReportRequest(this.storeOrder.getStoreOrderNumber());
        SimClientPrintUtility.printReportRequest((ReportRequest)storeOrderReportRequest, list, (MessageText)StoreOrderMessageText.STORE_ORDER_PRINTED);
      } 
    } 
  }
  
  public void cancelStoreOrder() throws Exception {
    if (this.storeOrder.getOrderState() == StoreOrderState.EDIT) {
      ClientServiceFactory.getStoreOrderServices().delete(this.storeOrder);
      RepositoryManager.addStateObject("STORE_ORDER_DETAIL_MODIFIED", Boolean.TRUE);
    } 
  }
  
  public void createStoreOrder() throws Exception {
    ClientServiceFactory.getStoreOrderServices().create(this.storeOrder);
    RepositoryManager.addStateObject("STORE_ORDER_DETAIL_MODIFIED", Boolean.TRUE);
  }
  
  public void updateStoreOrder() throws Exception {
    ClientServiceFactory.getStoreOrderServices().update(this.storeOrder);
    RepositoryManager.addStateObject("STORE_ORDER_DETAIL_MODIFIED", Boolean.TRUE);
  }
  
  public void removeLineItem(StoreOrderLineItemWrapper paramStoreOrderLineItemWrapper) {
    if (isNewStoreOrder())
      this.storeOrder.removeStoreOrderLineItem(paramStoreOrderLineItemWrapper.getLineItem()); 
  }
  
  public StoreOrderLineItemWrapper createNewLineItemWrapper() throws Exception {
    if (this.storeOrder.getFromLocation() == null) {
      if (this.storeOrder.getType() == StoreOrderType.PURCHASE_ORDER)
        throw new BusinessException(StoreOrderMessageText.STORE_ORDER_NO_SUPPLIER); 
      if (this.storeOrder.getType() == StoreOrderType.TRANSFER)
        throw new BusinessException(StoreOrderMessageText.STORE_ORDER_NO_WAREHOUSE); 
      throw new BusinessException(StoreOrderMessageText.MISSING_SOURCE);
    } 
    return buildNewStoreOrderLineItem();
  }
  
  public void updateExistingLineItem(StoreOrderLineItemWrapper paramStoreOrderLineItemWrapper, BarcodeItem paramBarcodeItem) throws Exception {
    if (paramBarcodeItem.getQuantity().isPositive()) {
      if (paramStoreOrderLineItemWrapper.isCasesMode() && paramStoreOrderLineItemWrapper.isEachesStandardUnitOfMeasure()) {
        paramStoreOrderLineItemWrapper.setQuantity(paramStoreOrderLineItemWrapper.getQuantityOrZero().add(paramBarcodeItem.getQuantity().multiply(paramStoreOrderLineItemWrapper.getCaseSize())));
      } else {
        paramStoreOrderLineItemWrapper.setQuantity(paramStoreOrderLineItemWrapper.getQuantityOrZero().add(paramBarcodeItem.getQuantity()));
      } 
      return;
    } 
    throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
  }
}
