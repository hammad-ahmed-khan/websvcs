package oracle.retail.sim.common.uda;

import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.type.Displayable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class UDAValue extends BusinessObject implements Displayable {
  private static final long serialVersionUID = -8251165983125318867L;
  
  private Long udaId;
  
  private String valueId;
  
  private String description;
  
  public Long getUdaId() {
    return this.udaId;
  }
  
  public void doSetUdaId(Long paramLong) {
    this.udaId = paramLong;
  }
  
  public String getValueId() {
    return this.valueId;
  }
  
  public void doSetValueId(String paramString) {
    this.valueId = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public String toDisplayString() {
    return this.description;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    UDAValue uDAValue = (UDAValue)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.udaId, uDAValue.udaId);
    equalsBuilder.append(this.valueId, uDAValue.valueId);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.udaId);
    hashCodeBuilder.append(this.valueId);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\commo\\uda\UDAValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */