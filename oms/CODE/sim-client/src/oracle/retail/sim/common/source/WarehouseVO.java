package oracle.retail.sim.common.source;

import java.io.Serializable;
import java.math.BigInteger;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WarehouseVO implements SourceVO, Serializable, Comparable<WarehouseVO> {
  private static final long serialVersionUID = 4065675502647091945L;
  
  private String id;
  
  private String name;
  
  public WarehouseVO(String paramString1, String paramString2) {
    this.id = paramString1;
    this.name = paramString2;
  }
  
  public SourceType getSourceType() {
    return SourceType.WAREHOUSE;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int compareTo(WarehouseVO paramWarehouseVO) {
    int i;
    if (StringHelper.isNullOrEmpty(this.id)) {
      if (!StringHelper.isNullOrEmpty(paramWarehouseVO.id))
        return -1; 
    } else if (StringHelper.isNullOrEmpty(paramWarehouseVO.id)) {
      return 1;
    } 
    if (NumberHelper.isIdentifierNumeric(this.id) && NumberHelper.isIdentifierNumeric(paramWarehouseVO.id)) {
      i = (new BigInteger(this.id)).compareTo(new BigInteger(paramWarehouseVO.id));
    } else {
      i = StringHelper.getInstance().compareToIgnoreCase(this.id, paramWarehouseVO.id);
    } 
    if (i != 0)
      return i; 
    if (StringHelper.isNullOrEmpty(this.name)) {
      if (!StringHelper.isNullOrEmpty(paramWarehouseVO.name))
        return -1; 
    } else if (StringHelper.isNullOrEmpty(paramWarehouseVO.name)) {
      return 1;
    } 
    return StringHelper.getInstance().compareToIgnoreCase(this.name, paramWarehouseVO.name);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    WarehouseVO warehouseVO = (WarehouseVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, warehouseVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Warehouse: ");
    stringBuilder.append("id=").append(this.id);
    stringBuilder.append(", name=").append(this.name);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\WarehouseVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */