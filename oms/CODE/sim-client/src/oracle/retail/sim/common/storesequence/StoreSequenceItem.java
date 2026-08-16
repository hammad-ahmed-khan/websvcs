package oracle.retail.sim.common.storesequence;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.NumberCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.storesequence.CapacityMustConformToUOMRule;
import oracle.retail.sim.common.rules.storesequence.NumberMustBePositiveAndLessThan1000000000Rule;
import oracle.retail.sim.common.rules.storesequence.UOMMustConformToCapacityRule;
import oracle.retail.sim.common.rules.storesequence.WidthMustConformToUOMRule;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class StoreSequenceItem extends BusinessObject {
  private static final long serialVersionUID = -3475707648367714197L;
  
  private Long id;
  
  private Long sequenceAreaId;
  
  private Long storeId;
  
  private String itemId;
  
  private String shortDescription;
  
  private String longDescription;
  
  private Quantity capacity = Quantity.ZERO;
  
  private Quantity width = Quantity.ZERO;
  
  private UOMMode uomMode = UOMMode.STANDARD;
  
  private String unitOfMeasure;
  
  private String ticketTypeFormatId;
  
  private Integer ticketQuantity = Integer.valueOf(1);
  
  private int order;
  
  private boolean primary;
  
  private boolean multipleAreaSequenced;
  
  private boolean dirty;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public Long getSequenceAreaId() {
    return this.sequenceAreaId;
  }
  
  public void setSequenceAreaId(Long paramLong) throws BusinessException {
    checkForNullParameter("Location", paramLong);
    executeRule("setSequenceAreaId", new Object[] { paramLong });
    doSetSequenceAreaId(paramLong);
    doSetDirty(true);
  }
  
  public void doSetSequenceAreaId(Long paramLong) {
    this.sequenceAreaId = paramLong;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public String getShortDescription() {
    return this.shortDescription;
  }
  
  public void doSetShortDescription(String paramString) {
    this.shortDescription = paramString;
  }
  
  public String getLongDescription() {
    return this.longDescription;
  }
  
  public void doSetLongDescription(String paramString) {
    this.longDescription = paramString;
  }
  
  public int getOrder() {
    return this.order;
  }
  
  public void setOrder(int paramInt) throws BusinessException {
    if (paramInt != this.order) {
      executeRule("setOrder", new Object[] { Integer.valueOf(paramInt) });
      doSetOrder(paramInt);
      doSetDirty(true);
    } 
  }
  
  public void doSetOrder(int paramInt) {
    this.order = paramInt;
  }
  
  public Quantity getCapacity() {
    return this.capacity;
  }
  
  public void setCapacity(Quantity paramQuantity) throws BusinessException {
    CapacityMustConformToUOMRule.execute(this, paramQuantity);
    checkForNullParameter("Capacity", paramQuantity);
    QuantityCannotBeNegativeRule.execute(paramQuantity);
    executeRule("setCapacity", new Object[] { paramQuantity });
    doSetCapacity(paramQuantity);
    doSetDirty(true);
  }
  
  public void doSetCapacity(Quantity paramQuantity) {
    this.capacity = paramQuantity;
  }
  
  public Quantity getWidth() {
    return this.width;
  }
  
  public void setWidth(Quantity paramQuantity) throws BusinessException {
    WidthMustConformToUOMRule.execute(this, paramQuantity);
    checkForNullParameter("Width", paramQuantity);
    executeRule("setWidth", new Object[] { paramQuantity });
    doSetWidth(paramQuantity);
    doSetDirty(true);
  }
  
  public void doSetWidth(Quantity paramQuantity) {
    this.width = paramQuantity;
  }
  
  public UOMMode getUnitOfMeasureMode() {
    return this.uomMode;
  }
  
  public void setUnitOfMeasureMode(UOMMode paramUOMMode) throws BusinessException {
    checkForNullParameter("UOM Mode", paramUOMMode);
    UOMMustConformToCapacityRule.execute(this, paramUOMMode);
    executeRule("setUnitOfMeasureMode", new Object[] { paramUOMMode });
    doSetUnitOfMeasureMode(paramUOMMode);
    doSetDirty(true);
  }
  
  public void doSetUnitOfMeasureMode(UOMMode paramUOMMode) {
    this.uomMode = paramUOMMode;
  }
  
  public String getUnitOfMeasure() {
    return this.unitOfMeasure;
  }
  
  public void doSetUnitOfMeasure(String paramString) {
    this.unitOfMeasure = paramString;
  }
  
  public String getTicketTypeFormatId() {
    return this.ticketTypeFormatId;
  }
  
  public void setTicketTypeFormatId(String paramString) throws BusinessException {
    executeRule("setTicketTypeFormatId", new Object[] { paramString });
    doSetTicketTypeFormatId(paramString);
    doSetDirty(true);
  }
  
  public void doSetTicketTypeFormatId(String paramString) {
    this.ticketTypeFormatId = paramString;
  }
  
  public Integer getTicketQuantity() {
    return this.ticketQuantity;
  }
  
  public void setTicketQuantity(Integer paramInteger) throws BusinessException {
    if (paramInteger == null)
      throw new BusinessException(CommonMessageText.QUANTITY_NOT_POSITIVE); 
    NumberMustBePositiveAndLessThan1000000000Rule.execute(paramInteger);
    NumberCannotBeNegativeRule.execute(paramInteger);
    executeRule("setTicketQuantity", new Object[] { paramInteger });
    doSetTicketQuantity(paramInteger);
    doSetDirty(true);
  }
  
  public void doSetTicketQuantity(Integer paramInteger) {
    this.ticketQuantity = paramInteger;
  }
  
  public boolean isMultipleAreaSequenced() {
    return this.multipleAreaSequenced;
  }
  
  public void doSetMultipleAreaSequenced(boolean paramBoolean) {
    this.multipleAreaSequenced = paramBoolean;
  }
  
  public boolean isPrimary() {
    return this.primary;
  }
  
  public void setPrimary(boolean paramBoolean) throws BusinessException {
    executeRule("setPrimary", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetPrimary(paramBoolean);
    doSetDirty(true);
  }
  
  public void doSetPrimary(boolean paramBoolean) {
    this.primary = paramBoolean;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty(boolean paramBoolean) {
    this.dirty = paramBoolean;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (StringHelper.isNullOrEmpty(this.itemId))
      throw new BusinessException(StoreSequenceMessageText.LOCATION_EMPTY_ERROR); 
    return true;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    StoreSequenceItem storeSequenceItem = (StoreSequenceItem)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, storeSequenceItem.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */