package oracle.retail.sim.common.itemrequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.rules.core.MaxCommentSizeRule;
import oracle.retail.sim.common.rules.itemrequest.ItemRequestAddLineItemRule;
import oracle.retail.sim.common.rules.itemrequest.ItemRequestIsCoherentRule;
import oracle.retail.sim.common.rules.itemrequest.ItemRequestValidateDateRule;

public class ItemRequest extends BusinessObject {
  private static final long serialVersionUID = -5428648930895487571L;
  
  private Long id = null;
  
  private Long storeId = null;
  
  private String username = null;
  
  private String comments = "";
  
  private DeliveryTimeSlot deliveryTimeSlot = null;
  
  private ItemRequestStatus status = null;
  
  private Date reqDeliveryDate;
  
  private Date processDate = null;
  
  private Date createDate = null;
  
  private Date expirationDate;
  
  private List<ItemRequestLineItem> lineItems = new ArrayList<>();
  
  private List<ItemRequestLineItem> deletedLineItems = new ArrayList<>();
  
  private int numberOfLineItems = 0;
  
  private transient boolean readyToSend = false;
  
  private String productGroupScheduleDescription;
  
  public static final String BATCH_USER = "Auto Create";
  
  public ItemRequest(Long paramLong) {
    this.storeId = paramLong;
    doSetCreateDate(SimDateUtil.getCurrentDate());
    doSetStatus(ItemRequestStatus.PENDING);
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id == null) ? null : this.id.toString();
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public void setUsername(String paramString) throws BusinessException {
    checkForNullParameter("Username", paramString);
    executeRule("setUsername", new Object[] { paramString });
    doSetUsername(paramString);
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void doSetUsername(String paramString) {
    this.username = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStatus(ItemRequestStatus paramItemRequestStatus) throws BusinessException {
    checkForNullParameter("Status", paramItemRequestStatus);
    if (paramItemRequestStatus == ItemRequestStatus.CANCELED && !isPendingStatus())
      throw new BusinessException(ItemRequestMessageText.INVALID_CANCEL_STATE); 
    executeRule("setStatus", new Object[] { paramItemRequestStatus });
    doSetStatus(paramItemRequestStatus);
  }
  
  public void doSetStatus(ItemRequestStatus paramItemRequestStatus) {
    this.status = paramItemRequestStatus;
  }
  
  public ItemRequestStatus getStatus() {
    return this.status;
  }
  
  public boolean isPendingStatus() {
    return (this.status == ItemRequestStatus.PENDING);
  }
  
  public void setComments(String paramString) throws BusinessException {
    MaxCommentSizeRule.execute(paramString);
    executeRule("setComments", new Object[] { paramString });
    doSetComments(paramString);
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public void setDeliveryTimeSlot(DeliveryTimeSlot paramDeliveryTimeSlot) throws BusinessException {
    executeRule("setDeliveryTimeslot", new Object[] { paramDeliveryTimeSlot });
    doSetDeliveryTimeSlot(paramDeliveryTimeSlot);
  }
  
  public DeliveryTimeSlot getDeliveryTimeSlot() {
    return this.deliveryTimeSlot;
  }
  
  public void doSetDeliveryTimeSlot(DeliveryTimeSlot paramDeliveryTimeSlot) {
    this.deliveryTimeSlot = paramDeliveryTimeSlot;
  }
  
  public void setProductGroupScheduleDescription(String paramString) throws BusinessException {
    executeRule("setScheduleDesc", new Object[] { paramString });
    doSetProductScheduleDescription(paramString);
  }
  
  public String getProductGroupScheduleDescription() {
    return this.productGroupScheduleDescription;
  }
  
  public void doSetProductScheduleDescription(String paramString) {
    this.productGroupScheduleDescription = paramString;
  }
  
  public boolean isCreatedFromProductGroup() {
    return (isBatchCreated() || this.productGroupScheduleDescription != null);
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public void setProcessDate(Date paramDate) throws BusinessException {
    checkForNullParameter("Process Date", paramDate);
    executeRule("setProcessDate", new Object[] { paramDate });
    doSetProcessDate(paramDate);
  }
  
  public Date getProcessDate() {
    return this.processDate;
  }
  
  public void doSetProcessDate(Date paramDate) {
    this.processDate = paramDate;
  }
  
  public void setReqDeliveryDate(Date paramDate, TimeZone paramTimeZone) throws BusinessException {
    ItemRequestValidateDateRule.executeIt(paramDate, paramTimeZone);
    executeRule("setReqDeliveryDate", new Object[] { paramDate, paramTimeZone });
    doSetReqDeliveryDate(paramDate);
  }
  
  public Date getReqDeliveryDate() {
    return this.reqDeliveryDate;
  }
  
  public void doSetReqDeliveryDate(Date paramDate) {
    this.reqDeliveryDate = paramDate;
  }
  
  public void setExpirationDate(Date paramDate) throws BusinessException {
    executeRule("setExpirationDate", new Object[] { paramDate });
    doSetExpirationDate(paramDate);
  }
  
  public Date getExpirationDate() {
    return this.expirationDate;
  }
  
  public void doSetExpirationDate(Date paramDate) {
    this.expirationDate = paramDate;
  }
  
  public boolean isBatchCreated() {
    return "Auto Create".equals(this.username);
  }
  
  public boolean isReadyToSend() {
    return this.readyToSend;
  }
  
  public void setReadyToSendFlag(boolean paramBoolean) {
    this.readyToSend = paramBoolean;
  }
  
  public List<ItemRequestLineItem> getLineItems() {
    return Collections.unmodifiableList(this.lineItems);
  }
  
  public List<ItemRequestLineItem> getDeletedLineItems() {
    return Collections.unmodifiableList(this.deletedLineItems);
  }
  
  public ItemRequestLineItem createLineItem(OrderItem paramOrderItem) throws BusinessException {
    checkForNullParameter("Order Item", paramOrderItem);
    ItemRequestLineItem itemRequestLineItem = findDeletedLineItem(paramOrderItem);
    if (itemRequestLineItem != null) {
      itemRequestLineItem.doSetQuantity(null);
      doAddLineItem(itemRequestLineItem);
      return itemRequestLineItem;
    } 
    ItemRequestAddLineItemRule.execute(this, paramOrderItem);
    executeRule("createLineItem", new Object[] { paramOrderItem });
    return doCreateLineItem(paramOrderItem);
  }
  
  public ItemRequestLineItem doCreateLineItem(OrderItem paramOrderItem) {
    ItemRequestLineItem itemRequestLineItem = BOFactory.createItemRequestLineItem(paramOrderItem);
    itemRequestLineItem.doSetLineSequence(getMaximumSequenceId() + 1);
    this.lineItems.add(itemRequestLineItem);
    return itemRequestLineItem;
  }
  
  public void addLineItem(ItemRequestLineItem paramItemRequestLineItem) throws BusinessException {
    if (paramItemRequestLineItem != null) {
      ItemRequestLineItem itemRequestLineItem = findDeletedLineItem(paramItemRequestLineItem.getOrderItem());
      if (itemRequestLineItem != null) {
        doAddLineItem(itemRequestLineItem);
        return;
      } 
      ItemRequestAddLineItemRule.execute(this, paramItemRequestLineItem.getOrderItem());
      doAddLineItem(paramItemRequestLineItem);
    } 
  }
  
  private void doAddLineItem(ItemRequestLineItem paramItemRequestLineItem) {
    this.deletedLineItems.remove(paramItemRequestLineItem);
    paramItemRequestLineItem.doSetLineSequence(getMaximumSequenceId() + 1);
    this.lineItems.add(paramItemRequestLineItem);
  }
  
  private ItemRequestLineItem findDeletedLineItem(OrderItem paramOrderItem) {
    for (ItemRequestLineItem itemRequestLineItem : this.deletedLineItems) {
      if (itemRequestLineItem.getOrderItem().equals(paramOrderItem))
        return itemRequestLineItem; 
    } 
    return null;
  }
  
  public void removeLineItem(ItemRequestLineItem paramItemRequestLineItem) throws BusinessException {
    checkForNullParameter("Line Item", paramItemRequestLineItem);
    executeRule("removeLineItem", new Object[] { paramItemRequestLineItem });
    doRemoveItemRequestLineItem(paramItemRequestLineItem);
  }
  
  public void doRemoveItemRequestLineItem(ItemRequestLineItem paramItemRequestLineItem) {
    if (paramItemRequestLineItem != null) {
      this.lineItems.remove(paramItemRequestLineItem);
      if (paramItemRequestLineItem.getId() != null)
        this.deletedLineItems.add(paramItemRequestLineItem); 
    } 
  }
  
  public int getNumberOfLineItems() {
    return (this.lineItems == null || (this.lineItems.isEmpty() && this.numberOfLineItems != 0)) ? this.numberOfLineItems : this.lineItems.size();
  }
  
  public int getDepartmentCount() {
    HashSet<Long> hashSet = new HashSet();
    for (ItemRequestLineItem itemRequestLineItem : this.lineItems)
      hashSet.add(itemRequestLineItem.getOrderItem().getDepartmentId()); 
    return hashSet.size();
  }
  
  public String getDepartmentId() {
    if (this.lineItems.isEmpty())
      return null; 
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (ItemRequestLineItem itemRequestLineItem : this.lineItems) {
      Long long_ = itemRequestLineItem.getOrderItem().getDepartmentId();
      Integer integer = (Integer)hashMap.get(long_);
      if (integer == null)
        integer = Integer.valueOf(0); 
      hashMap.put(long_, Integer.valueOf(integer.intValue() + 1));
    } 
    String str = null;
    int i = 0;
    for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
      Long long_ = (Long)entry.getKey();
      Integer integer = (Integer)entry.getValue();
      if (integer.intValue() > i) {
        i = integer.intValue();
        str = String.valueOf(long_);
      } 
    } 
    return str;
  }
  
  public boolean isAllLineItemsStoreOrderable() {
    for (ItemRequestLineItem itemRequestLineItem : getLineItems()) {
      if (!itemRequestLineItem.isStoreOrderReplenishmentType())
        return false; 
    } 
    return true;
  }
  
  private int getMaximumSequenceId() {
    int i = 0;
    for (ItemRequestLineItem itemRequestLineItem : getLineItems()) {
      if (itemRequestLineItem.getLineSequence() > i)
        i = itemRequestLineItem.getLineSequence(); 
    } 
    return i;
  }
  
  public boolean isCoherent(TimeZone paramTimeZone) throws BusinessException {
    ItemRequestIsCoherentRule.execute(this, paramTimeZone);
    executeRule("isCoherent", new Object[] { paramTimeZone });
    return true;
  }
  
  public boolean isPropertyModifiable(String paramString) {
    try {
      executeRule("isPropertyModifiable", new Object[] { paramString, this });
    } catch (BusinessException businessException) {
      return false;
    } 
    return true;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itemrequest\ItemRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */