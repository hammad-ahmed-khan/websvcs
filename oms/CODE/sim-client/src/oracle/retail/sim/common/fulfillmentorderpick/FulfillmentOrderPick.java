package oracle.retail.sim.common.fulfillmentorderpick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;

public class FulfillmentOrderPick extends BusinessObject {
  private static final long serialVersionUID = 6828018848586979690L;
  
  private Long id;
  
  private Long storeId;
  
  private Date createDate;
  
  private String createUser;
  
  private Date completeDate;
  
  private String completeUser;
  
  private FulfillmentOrderPickType type;
  
  private FulfillmentOrderPickStatus status = FulfillmentOrderPickStatus.NEW;
  
  private List<FulfillmentOrderPickLineItem> lineItems = new ArrayList<>();
  
  private List<FulfillmentOrderPickLineItem> deletedLineItems = new ArrayList<>();
  
  private boolean dirty;
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id != null) ? this.id.toString() : null;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    checkForNullParameter("Store", paramLong);
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
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
  
  public Date getCompleteDate() {
    return this.completeDate;
  }
  
  public void doSetCompleteDate(Date paramDate) {
    this.completeDate = paramDate;
  }
  
  public String getCompleteUser() {
    return this.completeUser;
  }
  
  public void doSetCompleteUser(String paramString) {
    this.completeUser = paramString;
  }
  
  public FulfillmentOrderPickType getType() {
    return this.type;
  }
  
  public void doSetType(FulfillmentOrderPickType paramFulfillmentOrderPickType) {
    this.type = paramFulfillmentOrderPickType;
  }
  
  public FulfillmentOrderPickStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FulfillmentOrderPickStatus paramFulfillmentOrderPickStatus) {
    this.status = paramFulfillmentOrderPickStatus;
  }
  
  public List<FulfillmentOrderPickLineItem> getLineItems() {
    return Collections.unmodifiableList(this.lineItems);
  }
  
  public List<FulfillmentOrderPickLineItem> getDeletedLineItems() {
    return Collections.unmodifiableList(this.deletedLineItems);
  }
  
  public void doAddLineItem(FulfillmentOrderPickLineItem paramFulfillmentOrderPickLineItem) {
    this.lineItems.add(paramFulfillmentOrderPickLineItem);
  }
  
  public void addLineItem(FulfillmentOrderPickLineItem paramFulfillmentOrderPickLineItem) throws BusinessException {
    checkForNullParameter("Add Line Item", paramFulfillmentOrderPickLineItem);
    executeRule("addLineItem", new Object[] { paramFulfillmentOrderPickLineItem });
    doAddLineItem(paramFulfillmentOrderPickLineItem);
    doSetDirty();
  }
  
  public void doSetLineItems(List<FulfillmentOrderPickLineItem> paramList) {
    if (paramList == null)
      return; 
    this.lineItems = paramList;
  }
  
  public FulfillmentOrderPickLineItem doRemoveLineItem(FulfillmentOrderPickLineItem paramFulfillmentOrderPickLineItem) {
    if (this.lineItems.remove(paramFulfillmentOrderPickLineItem)) {
      if (!paramFulfillmentOrderPickLineItem.isNew())
        this.deletedLineItems.add(paramFulfillmentOrderPickLineItem); 
      return paramFulfillmentOrderPickLineItem;
    } 
    return null;
  }
  
  public FulfillmentOrderPickLineItem removeLineItem(FulfillmentOrderPickLineItem paramFulfillmentOrderPickLineItem) throws BusinessException {
    checkForNullParameter("Remove Line Item", paramFulfillmentOrderPickLineItem);
    executeRule("removeLineItem", new Object[] { paramFulfillmentOrderPickLineItem });
    doSetDirty();
    return doRemoveLineItem(paramFulfillmentOrderPickLineItem);
  }
  
  public boolean isDirty() {
    for (FulfillmentOrderPickLineItem fulfillmentOrderPickLineItem : this.lineItems) {
      if (fulfillmentOrderPickLineItem.isDirty())
        return true; 
    } 
    return this.dirty;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public void doSetClean() {
    this.dirty = false;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.lineItems.isEmpty())
      throw new BusinessException(CommonMessageText.LINE_ITEM_NO_ITEM); 
    for (FulfillmentOrderPickLineItem fulfillmentOrderPickLineItem : this.lineItems)
      fulfillmentOrderPickLineItem.isCoherent(); 
    return super.isCoherent();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPick.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */