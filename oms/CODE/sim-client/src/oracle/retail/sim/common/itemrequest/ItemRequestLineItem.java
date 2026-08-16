package oracle.retail.sim.common.itemrequest;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.rules.core.CaseSizeMustBePositiveRule;
import oracle.retail.sim.common.rules.core.CaseSizeValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.itemrequest.ItemRequestLineItemPropertyModifiableRule;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ItemRequestLineItem extends BusinessObject {
  private static final long serialVersionUID = -594099779574939521L;
  
  private Long id;
  
  private OrderItem orderItem;
  
  private Quantity quantity;
  
  private DeliveryTimeSlot deliveryTimeSlot;
  
  private Quantity caseSize;
  
  private int lineSequence;
  
  private boolean dirty;
  
  public ItemRequestLineItem(OrderItem paramOrderItem) {
    this.orderItem = paramOrderItem;
    this.caseSize = paramOrderItem.getDefaultCaseSize();
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public OrderItem getOrderItem() {
    return this.orderItem;
  }
  
  public final int getLineSequence() {
    return this.lineSequence;
  }
  
  public final void doSetLineSequence(int paramInt) {
    this.lineSequence = paramInt;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
  
  public Quantity getQuantityOrZero() {
    return (this.quantity != null) ? this.quantity : Quantity.ZERO;
  }
  
  public void setQuantity(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity", paramQuantity);
    QuantityCannotBeNegativeRule.execute(paramQuantity);
    executeRule("setQuantity", new Object[] { paramQuantity });
    if (!paramQuantity.equals(this.quantity)) {
      doSetDirty();
      doSetQuantity(paramQuantity);
    } 
  }
  
  public void doSetQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
  }
  
  public boolean isOrderDateValidationRequired() {
    return this.orderItem.isOrderDateValidationRequired();
  }
  
  public boolean isStoreOrderReplenishmentType() {
    return this.orderItem.isStoreOrderReplenishmentType();
  }
  
  public boolean isMultipleDeliveryAllowed() {
    return this.orderItem.isMultipleDeliveryAllowed();
  }
  
  public Date getNextDeliveryDate() {
    return this.orderItem.getNextDeliveryDate();
  }
  
  public void setDeliveryTimeSlot(DeliveryTimeSlot paramDeliveryTimeSlot) throws BusinessException {
    executeRule("setDeliveryTimeSlot", new Object[] { paramDeliveryTimeSlot });
    doSetDeliveryTimeSlot(paramDeliveryTimeSlot);
    doSetDirty();
  }
  
  public DeliveryTimeSlot getDeliveryTimeSlot() {
    return this.deliveryTimeSlot;
  }
  
  public void doSetDeliveryTimeSlot(DeliveryTimeSlot paramDeliveryTimeSlot) {
    this.deliveryTimeSlot = paramDeliveryTimeSlot;
  }
  
  public Quantity getCaseSize() {
    return this.caseSize;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Case Size", paramQuantity);
    CaseSizeMustBePositiveRule.execute(paramQuantity);
    CaseSizeValidForUomRule.execute(this, paramQuantity);
    executeRule("setCaseSize", new Object[] { paramQuantity });
    doSetCaseSize(paramQuantity);
    doSetDirty();
  }
  
  public void doSetCaseSize(Quantity paramQuantity) {
    this.caseSize = paramQuantity;
  }
  
  public Long getDepartmentId() {
    return (this.orderItem != null) ? this.orderItem.getDepartmentId() : null;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public void doSetClean() {
    this.dirty = false;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public boolean isPropertyModifiable(String paramString, ItemRequest paramItemRequest) {
    if (ItemRequestLineItemPropertyModifiableRule.execute(paramItemRequest, this, paramString)) {
      try {
        executeRule("isPropertyModifiable", new Object[] { paramString, paramItemRequest });
      } catch (BusinessException businessException) {
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ItemRequestLineItem itemRequestLineItem = (ItemRequestLineItem)paramObject;
    return (this.orderItem == null && itemRequestLineItem.orderItem == null) ? true : ((this.orderItem == null) ? false : this.orderItem.getId().equals(itemRequestLineItem.orderItem.getId()));
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.orderItem);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itemrequest\ItemRequestLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */