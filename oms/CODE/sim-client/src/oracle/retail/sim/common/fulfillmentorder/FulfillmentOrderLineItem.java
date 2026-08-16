package oracle.retail.sim.common.fulfillmentorder;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.item.StockLineItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FulfillmentOrderLineItem extends BusinessObject implements StockLineItem {
  private static final long serialVersionUID = -4716389880879226667L;
  
  private boolean dirty;
  
  private Long id;
  
  private Long substituteLineItemId;
  
  private StockItem stockItem;
  
  private Quantity reservedQuantity = Quantity.ZERO;
  
  private Quantity canceledQuantity = Quantity.ZERO;
  
  private Quantity orderedQuantity = Quantity.ZERO;
  
  private Quantity pickedQuantity = Quantity.ZERO;
  
  private Quantity deliveredQuantity = Quantity.ZERO;
  
  private String preferredUom;
  
  private String comments;
  
  private Date createdDate;
  
  private Date updatedDate;
  
  private SimMoney price;
  
  private boolean substituteAllowed;
  
  public FulfillmentOrderLineItem(StockItem paramStockItem) {
    this.stockItem = paramStockItem;
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty(boolean paramBoolean) {
    this.dirty = paramBoolean;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public StockItem getStockItem() {
    return this.stockItem;
  }
  
  public String getItemId() {
    return this.stockItem.getId();
  }
  
  public String getItemDescription() {
    return SimConfigManager.isItemShortDescription() ? this.stockItem.getShortDescription() : this.stockItem.getLongDescription();
  }
  
  public Quantity getCanceledQuantity() {
    return this.canceledQuantity;
  }
  
  public void doSetCanceledQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.canceledQuantity = paramQuantity;
  }
  
  public void setCanceledQuantity(Quantity paramQuantity) {
    doSetCanceledQuantity(paramQuantity);
    doSetDirty(true);
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void setComments(String paramString) throws BusinessException {
    checkForNullParameter("Comments", paramString);
    executeRule("setComments", new Object[] { paramString });
    doSetComments(paramString);
    doSetDirty(true);
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public Long getSubstituteLineItemId() {
    return this.substituteLineItemId;
  }
  
  public void doSetSubstituteLineItemId(Long paramLong) {
    this.substituteLineItemId = paramLong;
  }
  
  public Date getCreatedDate() {
    return this.createdDate;
  }
  
  public void doSetCreatedDate(Date paramDate) {
    this.createdDate = paramDate;
  }
  
  public Quantity getReservedQuantity() {
    return this.reservedQuantity;
  }
  
  public void doSetReservedQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.reservedQuantity = paramQuantity;
  }
  
  public void setReservedQuantity(Quantity paramQuantity) {
    doSetReservedQuantity(paramQuantity);
    doSetDirty(true);
  }
  
  public Quantity getOrderedQuantity() {
    return this.orderedQuantity;
  }
  
  public void doSetOrderedQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.orderedQuantity = paramQuantity;
  }
  
  public Quantity getPickedQuantity() {
    return this.pickedQuantity;
  }
  
  public void doSetPickedQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.pickedQuantity = paramQuantity;
  }
  
  public void setPickedQuantity(Quantity paramQuantity) {
    doSetPickedQuantity(paramQuantity);
    doSetDirty(true);
  }
  
  public Quantity getDeliveredQuantity() {
    return this.deliveredQuantity;
  }
  
  public void doSetDeliveredQuantity(Quantity paramQuantity) {
    if (paramQuantity == null)
      paramQuantity = Quantity.ZERO; 
    this.deliveredQuantity = paramQuantity;
  }
  
  public void setDeliveredQuantity(Quantity paramQuantity) {
    doSetDeliveredQuantity(paramQuantity);
    doSetDirty(true);
  }
  
  public String getPreferredUom() {
    return this.preferredUom;
  }
  
  public void doSetPreferredUom(String paramString) {
    this.preferredUom = paramString;
  }
  
  public Date getUpdatedDate() {
    return this.updatedDate;
  }
  
  public void doSetUpdatedDate(Date paramDate) {
    this.updatedDate = paramDate;
  }
  
  public SimMoney getPrice() {
    return this.price;
  }
  
  public void doSetPrice(SimMoney paramSimMoney) {
    this.price = paramSimMoney;
  }
  
  public boolean isUINRequired() {
    return this.stockItem.isSerialNumberRequired();
  }
  
  public boolean isSubstituteAllowed() {
    return this.substituteAllowed;
  }
  
  public void doSetSubstituteAllowed(boolean paramBoolean) {
    this.substituteAllowed = paramBoolean;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.stockItem == null)
      throw new BusinessException(CommonMessageText.LINE_ITEM_NO_ITEM); 
    return super.isCoherent();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    FulfillmentOrderLineItem fulfillmentOrderLineItem = (FulfillmentOrderLineItem)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.stockItem.getId(), fulfillmentOrderLineItem.stockItem.getId());
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.stockItem.getId());
    return hashCodeBuilder.toHashCode();
  }
  
  public boolean isSubstitute() {
    return (this.substituteLineItemId != null);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */