package oracle.retail.sim.common.productgroup;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProductGroupHierarchy implements Serializable {
  private static final long serialVersionUID = -6135302073524025053L;
  
  private Long departmentId = null;
  
  private Long classId = null;
  
  private Long subclassId = null;
  
  private Integer numberOfItems = Integer.valueOf(0);
  
  public ProductGroupHierarchy(Long paramLong1, Long paramLong2, Long paramLong3) {
    this.departmentId = paramLong1;
    this.classId = paramLong2;
    this.subclassId = paramLong3;
  }
  
  public Long getDepartmentId() {
    return this.departmentId;
  }
  
  public Long getClassId() {
    return this.classId;
  }
  
  public void setClassId(Long paramLong) {
    this.classId = paramLong;
  }
  
  public Long getSubclassId() {
    return this.subclassId;
  }
  
  public void setSubclassId(Long paramLong) {
    this.subclassId = paramLong;
  }
  
  public Integer getNumberOfItems() {
    return this.numberOfItems;
  }
  
  public void setNumberOfItems(Integer paramInteger) {
    this.numberOfItems = paramInteger;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ProductGroupHierarchy productGroupHierarchy = (ProductGroupHierarchy)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.departmentId, productGroupHierarchy.departmentId);
    equalsBuilder.append(this.classId, productGroupHierarchy.classId);
    equalsBuilder.append(this.subclassId, productGroupHierarchy.subclassId);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.departmentId);
    hashCodeBuilder.append(this.classId);
    hashCodeBuilder.append(this.subclassId);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ProductGroupHierarchy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */