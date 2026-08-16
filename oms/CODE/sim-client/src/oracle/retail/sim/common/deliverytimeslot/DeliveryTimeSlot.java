package oracle.retail.sim.common.deliverytimeslot;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DeliveryTimeSlot implements Serializable {
  private static final long serialVersionUID = -832777306647832826L;
  
  private String id;
  
  private String description;
  
  private Integer sequence;
  
  public String getId() {
    return this.id;
  }
  
  public void doSetId(String paramString) {
    this.id = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public Integer getSequence() {
    return this.sequence;
  }
  
  public void doSetSequence(Integer paramInteger) {
    this.sequence = paramInteger;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    DeliveryTimeSlot deliveryTimeSlot = (DeliveryTimeSlot)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, deliveryTimeSlot.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\deliverytimeslot\DeliveryTimeSlot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */