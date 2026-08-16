package oracle.retail.sim.common.productgroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.item.ProductGroupItem;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.MaxStringSize25Rule;
import oracle.retail.sim.common.rules.core.NumberBetween1And99Rule;
import oracle.retail.sim.common.rules.core.NumberCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.NumberMustBeWholeRule;
import oracle.retail.sim.common.rules.group.ProductGroupAddSingleItemRule;
import oracle.retail.sim.common.rules.group.ProductGroupIsCoherentRule;
import oracle.retail.sim.common.rules.group.ProductGroupNoDuplicateHierarchiesRule;
import oracle.retail.sim.common.rules.group.ProductGroupPropertiesModifiableRule;
import oracle.retail.sim.common.stockcount.StockCountBreakdownType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.store.Store;
import org.apache.commons.lang.builder.EqualsBuilder;

public class ProductGroup extends BusinessObject {
  private static final long serialVersionUID = -6559061415679638925L;
  
  private Long id;
  
  private ProductGroupType groupType;
  
  private String description = "";
  
  private Store store;
  
  private ProductGroupStatus status = ProductGroupStatus.ACTIVE;
  
  private UOMMode uomMode = UOMMode.STANDARD;
  
  private Integer varianceCount;
  
  private BigDecimal variancePercent;
  
  private BigDecimal varianceValue;
  
  private StockCountingMethod countingMethod = StockCountingMethod.UNGUIDED;
  
  private StockCountBreakdownType breakdownType = StockCountBreakdownType.NONE;
  
  private boolean allItems;
  
  private boolean recountActive;
  
  private boolean autoAuthorize;
  
  private boolean problemLinePickLessSuggested;
  
  private boolean problemLineReplLessSuggested;
  
  private boolean problemLineNegativeAvailable;
  
  private boolean problemLineUINDiscrepancy;
  
  private boolean includeActiveItems = true;
  
  private boolean includeInactiveItems;
  
  private boolean includeDiscontinuedItems;
  
  private boolean includeDeletedItems;
  
  private boolean includeSOHZero;
  
  private boolean includeSOHLessThanZero;
  
  private boolean includeSOHGreaterThanZero;
  
  private boolean dirty;
  
  private Integer daysToExpire;
  
  private Integer daysToRequestDelivery;
  
  private List<ProductGroupHierarchy> hierarchies = new ArrayList<>();
  
  private List<String> singleItemIds = new ArrayList<>();
  
  private List<ProductGroupItem> singleItems = new ArrayList<>();
  
  public ProductGroup(ProductGroupType paramProductGroupType) {
    this.groupType = paramProductGroupType;
    validateDefaultUom();
  }
  
  public Long getId() {
    return this.id;
  }
  
  public ProductGroupType getType() {
    return this.groupType;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public Store getStore() {
    return this.store;
  }
  
  public boolean isForAllStores() {
    return (this.store == null);
  }
  
  public ProductGroupStatus getStatus() {
    return this.status;
  }
  
  public Integer getDaysToExpire() {
    return this.daysToExpire;
  }
  
  public Integer getDaysToRequestDelivery() {
    return this.daysToRequestDelivery;
  }
  
  public UOMMode getUnitOfMeasureMode() {
    return this.uomMode;
  }
  
  public Integer getVarianceCount() {
    return this.varianceCount;
  }
  
  public BigDecimal getVariancePercent() {
    return this.variancePercent;
  }
  
  public BigDecimal getVarianceValue() {
    return this.varianceValue;
  }
  
  public boolean isRecountRequired() {
    return this.recountActive;
  }
  
  public boolean isAutoAuthorize() {
    return this.autoAuthorize;
  }
  
  public boolean isAllItems() {
    return this.allItems;
  }
  
  public boolean isProblemLinePickLessSuggested() {
    return this.problemLinePickLessSuggested;
  }
  
  public boolean isProblemLineReplLessSuggested() {
    return this.problemLineReplLessSuggested;
  }
  
  public boolean isProblemLineNegativeAvailable() {
    return this.problemLineNegativeAvailable;
  }
  
  public boolean isProblemLineUINDiscrepancy() {
    return this.problemLineUINDiscrepancy;
  }
  
  public boolean includeActiveItems() {
    return this.includeActiveItems;
  }
  
  public boolean includeInactiveItems() {
    return this.includeInactiveItems;
  }
  
  public boolean includeDiscontinuedItems() {
    return this.includeDiscontinuedItems;
  }
  
  public boolean includeDeletedItems() {
    return this.includeDeletedItems;
  }
  
  public boolean includeSOHZero() {
    return this.includeSOHZero;
  }
  
  public boolean includeSOHLessThanZero() {
    return this.includeSOHLessThanZero;
  }
  
  public boolean includeSOHGreaterThanZero() {
    return this.includeSOHGreaterThanZero;
  }
  
  public StockCountingMethod getCountingMethod() {
    return this.countingMethod;
  }
  
  public boolean isGuidedCount() {
    return (this.countingMethod == StockCountingMethod.GUIDED);
  }
  
  public boolean isThirdPartyCount() {
    return (this.countingMethod == StockCountingMethod.THIRD_PARTY);
  }
  
  public StockCountBreakdownType getBreakdownType() {
    return this.breakdownType;
  }
  
  public List<ProductGroupHierarchy> getHierarchies() {
    return this.hierarchies;
  }
  
  public List<String> getSingleItemIds() {
    return this.singleItemIds;
  }
  
  public List<ProductGroupItem> getSingleItems() {
    return this.singleItems;
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public void setDescription(String paramString) throws BusinessException {
    checkForNullParameter("Description", paramString);
    MaxStringSize25Rule.execute(paramString);
    executeRule("isPropertyModifiable", new Object[] { "description" });
    executeRule("setDescription", new Object[] { paramString });
    doSetDescription(paramString);
    doSetDirty(true);
  }
  
  public void doSetDescription(String paramString) {
    if (!StringHelper.isNullOrEmpty(paramString))
      this.description = paramString; 
  }
  
  public void setStore(Store paramStore) throws BusinessException {
    checkForNullParameter("Store", paramStore);
    executeRule("isPropertyModifiable", new Object[] { "store" });
    executeRule("setStore", new Object[] { paramStore });
    doSetStore(paramStore);
    doSetDirty(true);
  }
  
  public void doSetStore(Store paramStore) {
    this.store = paramStore;
  }
  
  public void setForAllStores() throws BusinessException {
    executeRule("setForAllStores", new Object[0]);
    doSetForAllStores();
    doSetDirty(true);
  }
  
  public void doSetForAllStores() {
    doSetStore((Store)null);
  }
  
  public void setStatus(ProductGroupStatus paramProductGroupStatus) throws BusinessException {
    checkForNullParameter("Status", paramProductGroupStatus);
    executeRule("isPropertyModifiable", new Object[] { "status" });
    executeRule("setStatus", new Object[] { paramProductGroupStatus });
    doSetStatus(paramProductGroupStatus);
    doSetDirty(true);
  }
  
  public void doSetStatus(ProductGroupStatus paramProductGroupStatus) {
    this.status = paramProductGroupStatus;
  }
  
  public void setDaysToExpire(Integer paramInteger) throws BusinessException {
    if (paramInteger != null) {
      executeRule("isPropertyModifiable", new Object[] { "daysToExpire" });
      NumberBetween1And99Rule.execute(paramInteger);
      executeRule("setDaysToExpire", new Object[] { paramInteger });
    } 
    doSetDaysToExpire(paramInteger);
    doSetDirty(true);
  }
  
  public void doSetDaysToExpire(Integer paramInteger) {
    this.daysToExpire = paramInteger;
  }
  
  public void setDaysToRequestDelivery(Integer paramInteger) throws BusinessException {
    if (paramInteger != null) {
      executeRule("isPropertyModifiable", new Object[] { "daysToDelivery" });
      NumberBetween1And99Rule.execute(paramInteger);
      executeRule("setDaysToRequestDelivery", new Object[] { paramInteger });
    } 
    doSetDaysToRequestDelivery(paramInteger);
    doSetDirty(true);
  }
  
  public void doSetDaysToRequestDelivery(Integer paramInteger) {
    this.daysToRequestDelivery = paramInteger;
  }
  
  public void setUnitOfMeasureMode(UOMMode paramUOMMode) throws BusinessException {
    checkForNullParameter("UOMMode", paramUOMMode);
    executeRule("isPropertyModifiable", new Object[] { "uomMode" });
    executeRule("setUnitOfMeasureMode", new Object[] { paramUOMMode });
    doSetUnitOfMeasureMode(paramUOMMode);
    doSetDirty(true);
  }
  
  public void doSetUnitOfMeasureMode(UOMMode paramUOMMode) {
    this.uomMode = paramUOMMode;
  }
  
  public void setRecountRequired(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "recountRequired" });
    doSetRecountRequired(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetRecountRequired(boolean paramBoolean) {
    this.recountActive = paramBoolean;
  }
  
  public void setAutoAuthorize(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "autoAuthorize" });
    doSetAutoAuthorize(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetAutoAuthorize(boolean paramBoolean) {
    this.autoAuthorize = paramBoolean;
  }
  
  public void setAllItems(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "allItems" });
    doSetAllItems(paramBoolean);
    if (paramBoolean) {
      clearHierarchy();
      clearItems();
    } 
    doSetDirty(true);
  }
  
  public void doSetAllItems(boolean paramBoolean) {
    this.allItems = paramBoolean;
  }
  
  public void setProblemLineReplLessSuggested(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "problemLineReplLessSuggested" });
    doSetProblemLineReplLessSuggested(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetProblemLineReplLessSuggested(boolean paramBoolean) {
    this.problemLineReplLessSuggested = paramBoolean;
  }
  
  public void setProblemLinePickLessSuggested(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "problemLinePickLessSuggested" });
    doSetProblemLinePickLessSuggested(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetProblemLinePickLessSuggested(boolean paramBoolean) {
    this.problemLinePickLessSuggested = paramBoolean;
  }
  
  public void setProblemLineNegativeAvailable(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "problemLineNegativeAvailable" });
    doSetProblemLineNegativeAvailable(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetProblemLineNegativeAvailable(boolean paramBoolean) {
    this.problemLineNegativeAvailable = paramBoolean;
  }
  
  public void setProblemLineUINDiscrepancy(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "problemLineUINDiscrepancy" });
    doSetProblemLineUINDiscrepancy(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetProblemLineUINDiscrepancy(boolean paramBoolean) {
    this.problemLineUINDiscrepancy = paramBoolean;
  }
  
  public void setIncludeActiveItem(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "includeActiveItem" });
    doSetIncludeActiveItem(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetIncludeActiveItem(boolean paramBoolean) {
    this.includeActiveItems = paramBoolean;
  }
  
  public void setIncludeInactiveItems(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "includeInactiveItem" });
    doSetIncludeInactiveItems(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetIncludeInactiveItems(boolean paramBoolean) {
    this.includeInactiveItems = paramBoolean;
  }
  
  public void setIncludeDiscontinuedItems(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "includeDiscountinuedItem" });
    doSetIncludeDiscontinuedItems(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetIncludeDiscontinuedItems(boolean paramBoolean) {
    this.includeDiscontinuedItems = paramBoolean;
  }
  
  public void setIncludeDeletedItems(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "includeDeletedItem" });
    doSetIncludeDeletedItems(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetIncludeDeletedItems(boolean paramBoolean) {
    this.includeDeletedItems = paramBoolean;
  }
  
  public void setIncludeSOHZero(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "includeSOHZero" });
    doSetIncludeSOHZero(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetIncludeSOHZero(boolean paramBoolean) {
    this.includeSOHZero = paramBoolean;
  }
  
  public void setIncludeSOHLessThanZero(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "includeSOHLessThan" });
    doSetIncludeSOHLessThanZero(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetIncludeSOHLessThanZero(boolean paramBoolean) {
    this.includeSOHLessThanZero = paramBoolean;
  }
  
  public void setIncludeSOHGreaterThanZero(boolean paramBoolean) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "includeSOHGreaterThan" });
    doSetIncludeSOHGreaterThanZero(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetIncludeSOHGreaterThanZero(boolean paramBoolean) {
    this.includeSOHGreaterThanZero = paramBoolean;
  }
  
  public void setVarianceCount(Integer paramInteger) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "varianceCount" });
    NumberCannotBeNegativeRule.execute(paramInteger);
    NumberMustBeWholeRule.execute(paramInteger);
    executeRule("setVarianceCount", new Object[] { paramInteger });
    doSetVarianceCount(paramInteger);
    doSetDirty(true);
  }
  
  public void doSetVarianceCount(Integer paramInteger) {
    this.varianceCount = paramInteger;
  }
  
  public void setVariancePercent(BigDecimal paramBigDecimal) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "variancePercent" });
    NumberCannotBeNegativeRule.execute(paramBigDecimal);
    executeRule("setVariancePercent", new Object[] { paramBigDecimal });
    doSetVariancePercent(paramBigDecimal);
    doSetDirty(true);
  }
  
  public void doSetVariancePercent(BigDecimal paramBigDecimal) {
    this.variancePercent = paramBigDecimal;
  }
  
  public void setVarianceValue(BigDecimal paramBigDecimal) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "varianceValue" });
    NumberCannotBeNegativeRule.execute(paramBigDecimal);
    executeRule("setVarianceValue", new Object[] { paramBigDecimal });
    doSetVarianceValue(paramBigDecimal);
    doSetDirty(true);
  }
  
  public void doSetVarianceValue(BigDecimal paramBigDecimal) {
    this.varianceValue = paramBigDecimal;
  }
  
  public void setCountingMethod(StockCountingMethod paramStockCountingMethod) throws BusinessException {
    executeRule("isPropertyModifiable", new Object[] { "countingMethod" });
    executeRule("setCountingMethod", new Object[] { paramStockCountingMethod });
    doSetCountingMethod(paramStockCountingMethod);
    doSetDirty(true);
  }
  
  public void doSetCountingMethod(StockCountingMethod paramStockCountingMethod) {
    if (paramStockCountingMethod != null)
      this.countingMethod = paramStockCountingMethod; 
  }
  
  public void setBreakdownType(StockCountBreakdownType paramStockCountBreakdownType) throws BusinessException {
    checkForNullParameter("Breakdown Type", paramStockCountBreakdownType);
    executeRule("isPropertyModifiable", new Object[] { "breakdownType" });
    executeRule("setBreakdownType", new Object[] { paramStockCountBreakdownType });
    doSetBreakdownType(paramStockCountBreakdownType);
    doSetDirty(true);
  }
  
  public void doSetBreakdownType(StockCountBreakdownType paramStockCountBreakdownType) {
    this.breakdownType = paramStockCountBreakdownType;
  }
  
  public void addHierarchy(ProductGroupHierarchy paramProductGroupHierarchy) throws BusinessException {
    ProductGroupNoDuplicateHierarchiesRule.execute(this, paramProductGroupHierarchy);
    executeRule("addHierarchy", new Object[] { paramProductGroupHierarchy });
    doAddHierarchy(paramProductGroupHierarchy);
    doSetDirty(true);
  }
  
  public void doAddHierarchy(ProductGroupHierarchy paramProductGroupHierarchy) {
    if (paramProductGroupHierarchy != null)
      this.hierarchies.add(paramProductGroupHierarchy); 
  }
  
  public void removeHierarchy(ProductGroupHierarchy paramProductGroupHierarchy) throws BusinessException {
    executeRule("removeHierarchy", new Object[] { paramProductGroupHierarchy });
    doRemoveHierarchy(paramProductGroupHierarchy);
    doSetDirty(true);
  }
  
  public void doRemoveHierarchy(ProductGroupHierarchy paramProductGroupHierarchy) {
    if (paramProductGroupHierarchy != null)
      this.hierarchies.remove(paramProductGroupHierarchy); 
  }
  
  public void clearHierarchy() throws BusinessException {
    executeRule("clearHierarchy", new Object[0]);
    doClearHierarchy();
    doSetDirty(true);
  }
  
  public void doClearHierarchy() {
    this.hierarchies.clear();
  }
  
  public void addSingleItem(ProductGroupItem paramProductGroupItem) throws BusinessException {
    ProductGroupAddSingleItemRule.execute(this, paramProductGroupItem);
    executeRule("addSingleItem", new Object[] { paramProductGroupItem });
    doAddSingleItem(paramProductGroupItem.getId());
    doAddSingleItem(paramProductGroupItem);
    doSetDirty(true);
  }
  
  public void doAddSingleItem(String paramString) {
    if (paramString != null)
      this.singleItemIds.add(paramString); 
  }
  
  public void doAddSingleItem(ProductGroupItem paramProductGroupItem) {
    this.singleItems.add(paramProductGroupItem);
  }
  
  public void removeSingleItem(String paramString) throws BusinessException {
    executeRule("removeSingleItem", new Object[] { paramString });
    doRemoveSingleItem(paramString);
    doSetDirty(true);
  }
  
  public void removeSingleItem(ProductGroupItem paramProductGroupItem) throws BusinessException {
    executeRule("removeSingleItem", new Object[] { paramProductGroupItem });
    doRemoveSingleItem(paramProductGroupItem.getId());
    doRemoveSingleItem(paramProductGroupItem);
    doSetDirty(true);
  }
  
  public void doRemoveSingleItem(String paramString) {
    if (paramString != null)
      this.singleItemIds.remove(paramString); 
  }
  
  public void doRemoveSingleItem(ProductGroupItem paramProductGroupItem) {
    if (paramProductGroupItem != null)
      this.singleItems.remove(paramProductGroupItem); 
  }
  
  public void clearItems() throws BusinessException {
    executeRule("clearItems", new Object[0]);
    doClearItems();
    doSetDirty(true);
  }
  
  public void doClearItems() {
    this.singleItemIds.clear();
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty(boolean paramBoolean) {
    this.dirty = paramBoolean;
  }
  
  private void validateDefaultUom() {
    Integer integer = SimConfigManager.getDefaultUom();
    if (integer != null) {
      this.uomMode = UOMMode.toValue(integer);
    } else {
      this.uomMode = UOMMode.STANDARD;
    } 
  }
  
  public boolean isPropertyModifiable(String paramString) {
    if (ProductGroupPropertiesModifiableRule.execute(this, paramString)) {
      try {
        executeRule("isPropertyModifiable", new Object[] { paramString });
      } catch (BusinessException businessException) {
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  public boolean isCoherent() throws BusinessException {
    ProductGroupIsCoherentRule.execute(this);
    executeRule("isCoherent", new Object[0]);
    return true;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Product Group:\n");
    stringBuilder.append(" Id=").append(this.id).append(";");
    stringBuilder.append(" Type=").append(this.groupType).append(";");
    stringBuilder.append(" Description=").append(this.description).append(";");
    stringBuilder.append(" Status=").append(this.status);
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ProductGroup productGroup = (ProductGroup)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, productGroup.id);
    equalsBuilder.append(this.groupType, productGroup.groupType);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ProductGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */