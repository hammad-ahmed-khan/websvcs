package oracle.retail.sim.common.storesequence;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.locale.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class StoreSequenceArea extends BusinessObject {
  private static final long serialVersionUID = -6119530087427516459L;
  
  private Long id;
  
  private Long storeId;
  
  private String description;
  
  private StoreSequenceAreaType areaType;
  
  private int order;
  
  private boolean notSequenced;
  
  private Long departmentId;
  
  private Long classId;
  
  private Integer numberOfItems = Integer.valueOf(0);
  
  private boolean dirty;
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id == null) ? null : this.id.toString();
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
  
  public StoreSequenceAreaType getAreaType() {
    return this.areaType;
  }
  
  public void setAreaType(StoreSequenceAreaType paramStoreSequenceAreaType) throws BusinessException {
    if (paramStoreSequenceAreaType == null && this.areaType != null)
      throw new BusinessException(StoreSequenceMessageText.MISSING_AREA); 
    executeRule("setAreaType", new Object[] { paramStoreSequenceAreaType });
    doSetAreaType(paramStoreSequenceAreaType);
    doSetDirty(true);
  }
  
  public void doSetAreaType(StoreSequenceAreaType paramStoreSequenceAreaType) {
    this.areaType = paramStoreSequenceAreaType;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String paramString) throws BusinessException {
    checkForNullParameter("Description", paramString);
    executeRule("setDescription", new Object[] { paramString });
    doSetDescription(paramString);
    doSetDirty(true);
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public int getOrder() {
    return this.order;
  }
  
  public void setOrder(int paramInt) {
    doSetOrder(paramInt);
    doSetDirty(true);
  }
  
  public void doSetOrder(int paramInt) {
    this.order = paramInt;
  }
  
  public boolean isNoLocationType() {
    return (this.areaType == StoreSequenceAreaType.NO_LOCATION);
  }
  
  public boolean isShopFloorType() {
    return (this.areaType == StoreSequenceAreaType.SHOPFLOOR);
  }
  
  public boolean isBackRoomType() {
    return (this.areaType == StoreSequenceAreaType.BACKROOM);
  }
  
  public boolean isSequenced() {
    return !this.notSequenced;
  }
  
  public boolean isNotSequenced() {
    return this.notSequenced;
  }
  
  public void doSetNotSequenced(boolean paramBoolean) {
    this.notSequenced = paramBoolean;
  }
  
  public boolean isCreatedFromHierarchy() {
    return (this.classId != null);
  }
  
  public Long getDepartmentId() {
    return this.departmentId;
  }
  
  public void doSetDepartmentId(Long paramLong) {
    this.departmentId = paramLong;
  }
  
  public Long getClassId() {
    return this.classId;
  }
  
  public void doSetClassId(Long paramLong) {
    this.classId = paramLong;
  }
  
  public Integer getNumberOfItems() {
    return this.numberOfItems;
  }
  
  public void doSetNumberOfItems(Integer paramInteger) {
    if (paramInteger != null)
      this.numberOfItems = paramInteger; 
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty(boolean paramBoolean) {
    this.dirty = paramBoolean;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (StringHelper.isNullOrEmpty(this.description))
      throw new BusinessException(StoreSequenceMessageText.MISSING_DESCRIPTION); 
    if (this.areaType == null)
      throw new BusinessException(StoreSequenceMessageText.MISSING_AREA); 
    return super.isCoherent();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    StoreSequenceArea storeSequenceArea = (StoreSequenceArea)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, storeSequenceArea.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceArea.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */