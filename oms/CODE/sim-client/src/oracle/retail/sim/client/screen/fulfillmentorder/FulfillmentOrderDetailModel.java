package oracle.retail.sim.client.screen.fulfillmentorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderReportRequest;
import oracle.retail.sim.service.core.ClientServiceFactory;

public class FulfillmentOrderDetailModel extends SimScreenModel {
  private FulfillmentOrder fulfillmentOrder;
  
  public void loadFulfillmentOrder() {
    this.fulfillmentOrder = (FulfillmentOrder)RepositoryManager.getStateObject("SELECTED_FULFILLMENT_ORDER");
  }
  
  public FulfillmentOrder getFulfillmentOrder() {
    return this.fulfillmentOrder;
  }
  
  public boolean isOrderEditable() {
    if (this.fulfillmentOrder == null)
      loadFulfillmentOrder(); 
    return (this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.NEW || this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.IN_PROGRESS);
  }
  
  public boolean isNotesEditable() {
    FulfillmentOrderStatus fulfillmentOrderStatus = this.fulfillmentOrder.getStatus();
    return (fulfillmentOrderStatus == FulfillmentOrderStatus.NEW || fulfillmentOrderStatus == FulfillmentOrderStatus.IN_PROGRESS);
  }
  
  public boolean isWebOrder() {
    return (this.fulfillmentOrder.getOrderType() == FulfillmentOrderType.WEB_ORDER);
  }
  
  public boolean isItemDetailOrigin() {
    String str = (String)RepositoryManager.getStateObject("CUSTOMER_ORDER_DETAIL_ORIGIN");
    return (str != null && str.equals(SimScreenName.ITEM_CUSTOMER_ORDER_SCREEN));
  }
  
  public List<FulfillmentOrderLineItemWrapper> getCustomerOrderItems() throws Exception {
    List<FulfillmentOrderLineItem> list = this.fulfillmentOrder.getLineItems();
    ArrayList<FulfillmentOrderLineItemWrapper> arrayList = new ArrayList<FulfillmentOrderLineItemWrapper>();
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem : list)
      arrayList.add(ClientWrapperFactory.createFulfillmentOrderLineItemWrapper(this.fulfillmentOrder, fulfillmentOrderLineItem, findConversionFactor(fulfillmentOrderLineItem))); 
    return arrayList;
  }
  
  private BigDecimal findConversionFactor(FulfillmentOrderLineItem paramFulfillmentOrderLineItem) throws Exception {
    return UomUtility.getStandardUomToTargetUom(paramFulfillmentOrderLineItem.getStockItem(), paramFulfillmentOrderLineItem.getPreferredUom());
  }
  
  public void markFulfillmentOrderAsInProgress() throws Exception {
    if (this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.NEW) {
      ClientServiceFactory.getFulfillmentOrderServices().markFulfillmentOrderInProgress(this.fulfillmentOrder.getId());
      RepositoryManager.addStateObject("FULFILLMENT_ORDER_MODIFIED", Boolean.TRUE);
    } 
  }
  
  public void printCustomerOrder() throws Exception {
    Long long_ = getStoreId();
    List<RetailStoreFormatPrinter> list = SimClientPrintUtility.selectFormatPrinter(long_, ReportFormat.CUSTOMER_ORDER);
    if (list == null || list.isEmpty())
      return; 
    FulfillmentOrderReportRequest fulfillmentOrderReportRequest = BOFactory.createFulfillmentOrderReportRequest(this.fulfillmentOrder.getId());
    SimClientPrintUtility.printReportRequest((ReportRequest)fulfillmentOrderReportRequest, list, (MessageText)FulfillmentOrderMessageText.REPORT_PRINTED);
  }
}
