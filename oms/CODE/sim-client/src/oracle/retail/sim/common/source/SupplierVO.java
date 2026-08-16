package oracle.retail.sim.common.source;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SupplierVO implements SourceVO, Serializable {
  private static final long serialVersionUID = -1285449976516370995L;
  
  private String id;
  
  private String name;
  
  private SupplierStatus status;
  
  public SupplierVO(String paramString1, String paramString2, SupplierStatus paramSupplierStatus) {
    this.id = paramString1;
    this.name = paramString2;
    this.status = paramSupplierStatus;
  }
  
  public SourceType getSourceType() {
    return SourceType.SUPPLIER;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public SupplierStatus getStatus() {
    return this.status;
  }
  
  public boolean isInactive() {
    return (this.status != SupplierStatus.ACTIVE);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    SupplierVO supplierVO = (SupplierVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, supplierVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Supplier: ");
    stringBuilder.append("id=").append(this.id);
    stringBuilder.append(", name=").append(this.name);
    stringBuilder.append(", status=").append(this.status);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\SupplierVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */