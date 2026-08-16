package oracle.retail.sim.common.shelfreplenishment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.QuantityMustBePositiveRule;

public class ShelfReplenishment extends BusinessObject {
  private static final long serialVersionUID = 4476111574093170606L;
  
  private boolean allowAdjustment;
  
  private Long id = null;
  
  private String employeeId = null;
  
  private Long storeId = null;
  
  private Long productGroupId = null;
  
  private String productGroupDescription = null;
  
  private UOMMode uomMode = UOMMode.CASES;
  
  private ShelfReplenishmentType type = ShelfReplenishmentType.WITHIN_DAY;
  
  private ShelfReplenishmentStatus status = ShelfReplenishmentStatus.NEW;
  
  private Quantity quantityToReplenish = null;
  
  private int numberOfLineItems = 0;
  
  private List<ShelfReplenishmentLineItem> lineItems = new ArrayList<>();
  
  private Date createDate = null;
  
  private Date statusDate = null;
  
  public ShelfReplenishment(Long paramLong) {
    doSetStoreId(paramLong);
    doSetStatus(ShelfReplenishmentStatus.NEW);
    doSetId((Long)null);
    doSetCreateDate(SimDateUtil.getCurrentDate());
  }
  
  public ShelfReplenishment() {
    doSetStatus(ShelfReplenishmentStatus.NEW);
  }
  
  public boolean isAllowAdjustment() {
    return this.allowAdjustment;
  }
  
  public void setAllowAdjustment(boolean paramBoolean) {
    this.allowAdjustment = paramBoolean;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id == null) ? null : this.id.toString();
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public String getEmployeeId() {
    return this.employeeId;
  }
  
  public void setEmployeeId(String paramString) throws BusinessException {
    checkForNullParameter("Employee Id", paramString);
    executeRule("setTheOperator", new Object[] { paramString });
    doSetEmployeeId(paramString);
  }
  
  public void doSetEmployeeId(String paramString) {
    this.employeeId = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public ShelfReplenishmentType getType() {
    return this.type;
  }
  
  public void doSetType(ShelfReplenishmentType paramShelfReplenishmentType) {
    this.type = paramShelfReplenishmentType;
  }
  
  public ShelfReplenishmentStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(ShelfReplenishmentStatus paramShelfReplenishmentStatus) throws BusinessException {
    checkForNullParameter("Status", paramShelfReplenishmentStatus);
    executeRule("setStatus", new Object[] { paramShelfReplenishmentStatus });
    doSetStatus(paramShelfReplenishmentStatus);
  }
  
  public void doSetStatus(ShelfReplenishmentStatus paramShelfReplenishmentStatus) {
    this.status = paramShelfReplenishmentStatus;
  }
  
  public UOMMode getUnitOfMeasureMode() {
    return this.uomMode;
  }
  
  public void doSetUnitOfMeasureMode(UOMMode paramUOMMode) {
    this.uomMode = paramUOMMode;
  }
  
  public Long getProductGroupId() {
    return this.productGroupId;
  }
  
  public void setProductGroupId(Long paramLong) throws BusinessException {
    checkForNullParameter("Product Group", paramLong);
    doSetProductGroupId(paramLong);
  }
  
  public void doSetProductGroupId(Long paramLong) {
    this.productGroupId = paramLong;
  }
  
  public String getProductGroupDescription() {
    return this.productGroupDescription;
  }
  
  public void doSetProductGroupDescription(String paramString) {
    this.productGroupDescription = paramString;
  }
  
  public void setInProgress() throws BusinessException {
    executeRule("setInProgress", new Object[0]);
    doSetInProgress();
  }
  
  public void doSetInProgress() {
    this.status = ShelfReplenishmentStatus.IN_PROGRESS;
  }
  
  public void setClosed() throws BusinessException {
    executeRule("setClosed", new Object[0]);
    doSetClosed();
  }
  
  public void doSetClosed() {
    this.status = ShelfReplenishmentStatus.CLOSED;
  }
  
  public void setCanceled() throws BusinessException {
    if (this.status != ShelfReplenishmentStatus.NEW && this.status != ShelfReplenishmentStatus.PENDING_ALTERED)
      throw new BusinessException(ShelfReplenishmentMessageText.ONLY_PENDED_CAN_CANCEL); 
    executeRule("setCanceled", new Object[0]);
    doSetStatus(ShelfReplenishmentStatus.CANCELED);
    doSetStatusDate(SimDateUtil.getCurrentDate());
  }
  
  public void setComplete() throws BusinessException {
    executeRule("setComplete", new Object[0]);
    doSetStatus(ShelfReplenishmentStatus.COMPLETE);
    doSetStatusDate(SimDateUtil.getCurrentDate());
  }
  
  public Quantity getQuantityToReplenish() {
    return this.quantityToReplenish;
  }
  
  public void setQuantityToReplenish(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity To Replenish", paramQuantity);
    QuantityMustBePositiveRule.execute(paramQuantity);
    executeRule("setQuantityToReplenish", new Object[] { paramQuantity });
    doSetQuantityToReplenish(paramQuantity);
  }
  
  public void doSetQuantityToReplenish(Quantity paramQuantity) {
    this.quantityToReplenish = paramQuantity;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
    if (this.statusDate == null)
      doSetStatusDate(paramDate); 
  }
  
  public Date getStatusDate() {
    return this.statusDate;
  }
  
  public void setStatusDate(Date paramDate) throws BusinessException {
    checkForNullParameter("Status Date", paramDate);
    executeRule("setStatusDate", new Object[] { paramDate });
    doSetStatusDate(paramDate);
  }
  
  public void doSetStatusDate(Date paramDate) {
    this.statusDate = paramDate;
  }
  
  public BigDecimal getMaxFillPercentage() {
    if (this.type == ShelfReplenishmentType.END_OF_DAY) {
      Double double_ = SimConfigManager.getStoreDouble("REPLENISHMENT_END_OF_DAY_MAX_FILL_PCT", this.storeId);
      return BigDecimal.valueOf(double_.doubleValue() / 100.0D);
    } 
    if (this.type == ShelfReplenishmentType.WITHIN_DAY) {
      Double double_ = SimConfigManager.getStoreDouble("REPLENISHMENT_WITHIN_DAY_MAX_FILL_PCT", this.storeId);
      return BigDecimal.valueOf(double_.doubleValue() / 100.0D);
    } 
    return null;
  }
  
  public List<ShelfReplenishmentLineItem> getLineItems() {
    return Collections.unmodifiableList(this.lineItems);
  }
  
  public Set<String> getAssociatedStockItemIds() {
    HashSet<String> hashSet = new HashSet();
    for (ShelfReplenishmentLineItem shelfReplenishmentLineItem : this.lineItems)
      hashSet.add(shelfReplenishmentLineItem.getStockItem().getId()); 
    return hashSet;
  }
  
  public int getNumberOfLineItems() {
    List<ShelfReplenishmentLineItem> list = getLineItems();
    return (list == null || (list.isEmpty() && this.numberOfLineItems != 0)) ? this.numberOfLineItems : getLineItems().size();
  }
  
  public void doSetNumberOfLineItems(int paramInt) {
    this.numberOfLineItems = paramInt;
  }
  
  public void removeLineItem(ShelfReplenishmentLineItem paramShelfReplenishmentLineItem) throws BusinessException {
    checkForNullParameter("removeLineItem", paramShelfReplenishmentLineItem);
    executeRule("removeLineItem", new Object[] { paramShelfReplenishmentLineItem });
    doRemoveLineItem(paramShelfReplenishmentLineItem);
  }
  
  public void doAddLineItem(ShelfReplenishmentLineItem paramShelfReplenishmentLineItem) {
    if (paramShelfReplenishmentLineItem == null)
      return; 
    this.lineItems.add(paramShelfReplenishmentLineItem);
  }
  
  public void doRemoveLineItem(ShelfReplenishmentLineItem paramShelfReplenishmentLineItem) {
    if (paramShelfReplenishmentLineItem == null)
      return; 
    this.lineItems.remove(paramShelfReplenishmentLineItem);
  }
  
  public ShelfReplenishmentLineItem createLineItem(StockItem paramStockItem) throws BusinessException {
    checkForNullParameter("createLineItem", paramStockItem);
    executeRule("createLineItem", new Object[] { paramStockItem });
    return doCreateLineItem(this.id, paramStockItem);
  }
  
  public ShelfReplenishmentLineItem createLineItem(Long paramLong, StockItem paramStockItem) throws BusinessException {
    checkForNullParameter("createLineItem", paramStockItem);
    executeRule("createLineItem", new Object[] { paramStockItem });
    return doCreateLineItem(paramLong, paramStockItem);
  }
  
  public ShelfReplenishmentLineItem doCreateLineItem(Long paramLong, StockItem paramStockItem) {
    ShelfReplenishmentLineItem shelfReplenishmentLineItem = BOFactory.createShelfReplenishmentLineItem(paramLong, paramStockItem);
    doAddLineItem(shelfReplenishmentLineItem);
    return shelfReplenishmentLineItem;
  }
  
  public void markInProgress() throws BusinessException {
    if (this.status == ShelfReplenishmentStatus.CANCELED)
      throw new BusinessException(ShelfReplenishmentMessageText.CANCELLED_ERROR); 
    doSetStatus(ShelfReplenishmentStatus.IN_PROGRESS);
    doSetCreateDate(SimDateUtil.getCurrentDate());
  }
  
  public boolean isPropertyModifiable(String paramString) {
    if ("quantityToReplenish".equals(paramString) && this.type == ShelfReplenishmentType.END_OF_DAY)
      return false; 
    try {
      executeRule("isPropertyModifiable", new Object[] { paramString });
    } catch (BusinessException businessException) {
      return false;
    } 
    return true;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.productGroupId == null)
      throw new BusinessException(ShelfReplenishmentMessageText.NO_GROUP_ASSIGNED); 
    if (this.type == ShelfReplenishmentType.WITHIN_DAY && this.quantityToReplenish == null)
      throw new BusinessException(ShelfReplenishmentMessageText.NO_QTY_TO_REPLENISH); 
    executeRule("isCoherent", new Object[0]);
    return true;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ShelfReplenishment shelfReplenishment = (ShelfReplenishment)paramObject;
    return getId().equals(shelfReplenishment.getId());
  }
  
  public int hashCode() {
    return getId().hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */