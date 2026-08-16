package oracle.retail.sim.common.itembasket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.SaleItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ItemBasket extends BusinessObject {
  private static final long serialVersionUID = -401892402416890204L;
  
  private Long id;
  
  private String externalId;
  
  private Long storeId;
  
  private String userId;
  
  private ItemBasketType type;
  
  private ItemBasketStatus status = ItemBasketStatus.ACTIVE;
  
  private Date createDate;
  
  private Date updateDate;
  
  private List<ItemBasketLineItem> lineItems = new ArrayList<>();
  
  private List<ItemBasketLineItem> deletedLineItems = new ArrayList<>();
  
  private boolean dirty;
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id == null) ? null : this.id.toString();
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
    setDirty();
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void setExternalId(String paramString) throws BusinessException {
    executeRule("setExternalId", new Object[] { paramString });
    doSetExternalId(paramString);
    this.dirty = true;
  }
  
  public void doSetExternalId(String paramString) {
    this.externalId = paramString;
    setDirty();
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    checkForNullParameter("Store", paramLong);
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
    setDirty();
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
    setDirty();
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String paramString) throws BusinessException {
    executeRule("setUserId", new Object[] { paramString });
    doSetUserId(paramString);
    setDirty();
  }
  
  public void doSetUserId(String paramString) {
    this.userId = paramString;
  }
  
  public ItemBasketType getType() {
    return this.type;
  }
  
  public void setType(ItemBasketType paramItemBasketType) throws BusinessException {
    checkForNullParameter("Type", paramItemBasketType);
    executeRule("setType", new Object[] { paramItemBasketType });
    doSetType(paramItemBasketType);
    setDirty();
  }
  
  public void doSetType(ItemBasketType paramItemBasketType) {
    this.type = paramItemBasketType;
  }
  
  public ItemBasketStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(ItemBasketStatus paramItemBasketStatus) throws BusinessException {
    checkForNullParameter("Status", paramItemBasketStatus);
    executeRule("setStatus", new Object[] { paramItemBasketStatus });
    doSetStatus(paramItemBasketStatus);
    setDirty();
  }
  
  public void doSetStatus(ItemBasketStatus paramItemBasketStatus) {
    this.status = paramItemBasketStatus;
  }
  
  public List<ItemBasketLineItem> getLineItems() {
    return this.lineItems;
  }
  
  public ItemBasketLineItem getLineItem(String paramString) {
    for (ItemBasketLineItem itemBasketLineItem : this.lineItems) {
      if (itemBasketLineItem.getItemId().equals(paramString))
        return itemBasketLineItem; 
    } 
    return null;
  }
  
  public void doSetLineItems(List<ItemBasketLineItem> paramList) {
    if (paramList != null)
      this.lineItems = paramList; 
  }
  
  public ItemBasketLineItem createLineItem(SaleItem paramSaleItem) throws BusinessException {
    checkForNullParameter("Line Item", paramSaleItem);
    ItemBasketLineItem itemBasketLineItem = getLineItem(paramSaleItem.getId());
    if (itemBasketLineItem != null)
      throw new BusinessException(CommonMessageText.ITEM_ALREADY_EXISTS); 
    executeRule("createLineItem", new Object[] { paramSaleItem });
    setDirty();
    return doCreateLineItem(paramSaleItem);
  }
  
  public ItemBasketLineItem doCreateLineItem(SaleItem paramSaleItem) {
    ItemBasketLineItem itemBasketLineItem = BOFactory.createItemBasketLineItem();
    itemBasketLineItem.doSetSaleItem(paramSaleItem);
    this.lineItems.add(itemBasketLineItem);
    return itemBasketLineItem;
  }
  
  public void addLineItem(ItemBasketLineItem paramItemBasketLineItem) throws BusinessException {
    checkForNullParameter("Line Item", paramItemBasketLineItem);
    executeRule("addLineItem", new Object[] { paramItemBasketLineItem });
    ItemBasketLineItem itemBasketLineItem = getLineItem(paramItemBasketLineItem.getItemId());
    if (itemBasketLineItem != null) {
      itemBasketLineItem.doSetQuantity(paramItemBasketLineItem.getQuantity());
      itemBasketLineItem.doSetCaseSize(paramItemBasketLineItem.getCaseSize());
      itemBasketLineItem.doSetUpdateDate(SimDateUtil.getCurrentDate());
      itemBasketLineItem.doSetDirty();
      return;
    } 
    this.lineItems.add(paramItemBasketLineItem);
  }
  
  public List<ItemBasketLineItem> getDeletedLineItems() {
    return this.deletedLineItems;
  }
  
  public void deleteLineItem(ItemBasketLineItem paramItemBasketLineItem) {
    if (paramItemBasketLineItem != null && this.lineItems.remove(paramItemBasketLineItem)) {
      if (paramItemBasketLineItem.getId() != null)
        this.deletedLineItems.add(paramItemBasketLineItem); 
      setDirty();
    } 
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
    setDirty();
  }
  
  public Date getUpdateDate() {
    return this.updateDate;
  }
  
  public void doSetUpdateDate(Date paramDate) {
    this.updateDate = paramDate;
    setDirty();
  }
  
  public boolean isDirty() {
    if (this.dirty || this.id == null)
      return true; 
    if (this.deletedLineItems.size() > 0)
      return true; 
    for (ItemBasketLineItem itemBasketLineItem : this.lineItems) {
      if (itemBasketLineItem.isDirty() || itemBasketLineItem.getId() == null)
        return true; 
    } 
    return false;
  }
  
  public void setDirty() {
    this.dirty = true;
  }
  
  public void setClean() {
    this.dirty = false;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ItemBasket itemBasket = (ItemBasket)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, itemBasket.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itembasket\ItemBasket.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */