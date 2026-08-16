package oracle.retail.sim.common.fulfillmentorder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.StockItem;

public class ItemFulfillmentOrderVO implements Serializable {
  private static final long serialVersionUID = -6820977657049179144L;
  
  private StockItem stockItem;
  
  private Long fulfillmentOrderId;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private String preferredUom;
  
  private String comments;
  
  private Date createDate;
  
  private Date releaseDate;
  
  private FulfillmentOrderType orderType;
  
  private Quantity orderedQuantity = Quantity.ZERO;
  
  private Quantity deliveredQuantity = Quantity.ZERO;
  
  private Quantity canceledQuantity = Quantity.ZERO;
  
  public Long getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public String getFulfillmentOrderExternalId() {
    return this.fulfillmentOrderExternalId;
  }
  
  public String getItemId() {
    return this.stockItem.getId();
  }
  
  public String getItemDescription() {
    return SimConfigManager.isItemShortDescription() ? this.stockItem.getShortDescription() : this.stockItem.getLongDescription();
  }
  
  public String getStandardUom() {
    return this.stockItem.getUnitOfMeasure();
  }
  
  public String getPreferredUom() {
    return this.preferredUom;
  }
  
  public BigDecimal getEachToUomConversion() {
    return this.stockItem.getEachToUomConversion();
  }
  
  public FulfillmentOrderType getOrderType() {
    return this.orderType;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public Date getReleaseDate() {
    return this.releaseDate;
  }
  
  public Quantity getPendingReservedQuantity() {
    return this.orderedQuantity.subtract(this.canceledQuantity).subtract(this.deliveredQuantity);
  }
  
  public void doSetStockItem(StockItem paramStockItem) {
    this.stockItem = paramStockItem;
  }
  
  public void doSetFulfillmentOrderId(Long paramLong) {
    this.fulfillmentOrderId = paramLong;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public void doSetFulfillmentOrderExternalId(String paramString) {
    this.fulfillmentOrderExternalId = paramString;
  }
  
  public void doSetPreferredUom(String paramString) {
    this.preferredUom = paramString;
  }
  
  public void doSetOrderType(FulfillmentOrderType paramFulfillmentOrderType) {
    this.orderType = paramFulfillmentOrderType;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public void doSetReleaseDate(Date paramDate) {
    this.releaseDate = paramDate;
  }
  
  public void doSetOrderedQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.orderedQuantity = paramQuantity;
  }
  
  public void doSetDeliveredQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.deliveredQuantity = paramQuantity;
  }
  
  public void doSetCanceledQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.canceledQuantity = paramQuantity;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\ItemFulfillmentOrderVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */