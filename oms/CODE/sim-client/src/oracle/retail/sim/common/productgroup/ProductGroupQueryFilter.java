package oracle.retail.sim.common.productgroup;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;

public class ProductGroupQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 1819326388399789163L;
  
  private ProductGroupType productGroupType = null;
  
  private Long storeId = null;
  
  private Long groupId = null;
  
  private String description = null;
  
  private String itemId = null;
  
  private Long departmentId = null;
  
  private Long classId = null;
  
  private Long subclassId = null;
  
  public ProductGroupType getProductGroupType() {
    return this.productGroupType;
  }
  
  public void setProductGroupType(ProductGroupType paramProductGroupType) throws BusinessException {
    executeRule("setProductGroupType", new Object[] { paramProductGroupType });
    doSetProductGroupType(paramProductGroupType);
  }
  
  public void doSetProductGroupType(ProductGroupType paramProductGroupType) {
    this.productGroupType = paramProductGroupType;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public Long getGroupId() {
    return this.groupId;
  }
  
  public void setGroupId(Long paramLong) throws BusinessException {
    executeRule("setGroupId", new Object[] { paramLong });
    doSetGroupId(paramLong);
  }
  
  public void doSetGroupId(Long paramLong) {
    this.groupId = paramLong;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String paramString) throws BusinessException {
    executeRule("setDescription", new Object[] { paramString });
    doSetDescription(paramString);
  }
  
  public void doSetDescription(String paramString) {
    this.description = StringHelper.trimToNull(paramString);
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void setItemId(String paramString) throws BusinessException {
    executeRule("setItemId", new Object[] { paramString });
    doSetItemId(paramString);
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public Long getDepartmentId() {
    return this.departmentId;
  }
  
  public void setDepartmentId(Long paramLong) throws BusinessException {
    executeRule("setDepartmentId", new Object[] { paramLong });
    doSetDepartmentId(paramLong);
  }
  
  public void doSetDepartmentId(Long paramLong) {
    this.departmentId = paramLong;
  }
  
  public Long getClassId() {
    return this.classId;
  }
  
  public void setClassId(Long paramLong) throws BusinessException {
    executeRule("setClassId", new Object[] { paramLong });
    doSetClassId(paramLong);
  }
  
  public void doSetClassId(Long paramLong) {
    this.classId = paramLong;
  }
  
  public Long getSubclassId() {
    return this.subclassId;
  }
  
  public void setSubclassId(Long paramLong) throws BusinessException {
    executeRule("setSubclassId", new Object[] { paramLong });
    doSetSubclassId(paramLong);
  }
  
  public void doSetSubclassId(Long paramLong) {
    this.subclassId = paramLong;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getName());
    stringBuilder.append("[");
    stringBuilder.append(paramString());
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public String paramString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("description=");
    stringBuilder.append(this.description);
    stringBuilder.append(", productGroupType=");
    stringBuilder.append(this.productGroupType);
    stringBuilder.append(", storeId=");
    stringBuilder.append(this.storeId);
    stringBuilder.append(", groupId=");
    stringBuilder.append(this.groupId);
    stringBuilder.append(", departmentId=");
    stringBuilder.append(this.departmentId);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ProductGroupQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */