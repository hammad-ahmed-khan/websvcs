package oracle.retail.sim.common.fulfillmentorderreversepick;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;

public class FulfillmentOrderReversePick extends BusinessObject {
  private static final long serialVersionUID = -4632692113904456761L;
  
  private Long id;
  
  private Long storeId;
  
  private Long fulfillmentOrderId;
  
  private Date createDate;
  
  private String createUser;
  
  private FulfillmentOrderReversePickStatus status = FulfillmentOrderReversePickStatus.NEW;
  
  private List<FulfillmentOrderReversePickLineItem> lineItems = new ArrayList<>();
  
  private boolean dirty;
  
  private boolean setDefaultQuantities = false;
  
  public boolean isSetDefaultQuantities() {
    return this.setDefaultQuantities;
  }
  
  public void setSetDefaultQuantities(boolean paramBoolean) {
    this.setDefaultQuantities = paramBoolean;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id != null) ? this.id.toString() : "New";
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public String getCreateUser() {
    return this.createUser;
  }
  
  public void doSetCreateUser(String paramString) {
    this.createUser = paramString;
  }
  
  public boolean isExternallyInitiated() {
    return Objects.equals(this.createUser, "External System");
  }
  
  public FulfillmentOrderReversePickStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FulfillmentOrderReversePickStatus paramFulfillmentOrderReversePickStatus) {
    this.status = paramFulfillmentOrderReversePickStatus;
  }
  
  public Long getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public void doSetFulfillmentOrderId(Long paramLong) {
    this.fulfillmentOrderId = paramLong;
  }
  
  public List<FulfillmentOrderReversePickLineItem> getLineItems() {
    return this.lineItems;
  }
  
  public void doAddLineItem(FulfillmentOrderReversePickLineItem paramFulfillmentOrderReversePickLineItem) {
    if (paramFulfillmentOrderReversePickLineItem != null)
      this.lineItems.add(paramFulfillmentOrderReversePickLineItem); 
  }
  
  public void doSetLineItems(List<FulfillmentOrderReversePickLineItem> paramList) {
    this.lineItems = paramList;
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    checkForNullParameter("Store", paramLong);
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public boolean isDirty() {
    for (FulfillmentOrderReversePickLineItem fulfillmentOrderReversePickLineItem : this.lineItems) {
      if (fulfillmentOrderReversePickLineItem.isDirty())
        return true; 
    } 
    return this.dirty;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.lineItems.isEmpty())
      throw new BusinessException(CommonMessageText.LINE_ITEM_NO_ITEM); 
    for (FulfillmentOrderReversePickLineItem fulfillmentOrderReversePickLineItem : this.lineItems)
      fulfillmentOrderReversePickLineItem.isCoherent(); 
    return super.isCoherent();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderreversepick\FulfillmentOrderReversePick.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */