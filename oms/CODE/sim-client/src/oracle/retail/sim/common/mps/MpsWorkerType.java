package oracle.retail.sim.common.mps;

import java.io.Serializable;
import oracle.retail.sim.common.integration.SimMessageFamily;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MpsWorkerType implements Serializable {
  private static final long serialVersionUID = -7379033381547061459L;
  
  private long id;
  
  private SimMessageFamily messageFamily;
  
  private boolean inbound;
  
  private boolean enabled;
  
  private int maxMessageRetries;
  
  public long getId() {
    return this.id;
  }
  
  public void setId(long paramLong) {
    this.id = paramLong;
  }
  
  public SimMessageFamily getMessageFamily() {
    return this.messageFamily;
  }
  
  public void setMessageFamily(SimMessageFamily paramSimMessageFamily) {
    this.messageFamily = paramSimMessageFamily;
  }
  
  public boolean isInbound() {
    return this.inbound;
  }
  
  public void setInbound(boolean paramBoolean) {
    this.inbound = paramBoolean;
  }
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.enabled = paramBoolean;
  }
  
  public int getMaxMessageRetries() {
    return this.maxMessageRetries;
  }
  
  public void setMaxMessageRetries(int paramInt) {
    this.maxMessageRetries = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    MpsWorkerType mpsWorkerType = (MpsWorkerType)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, mpsWorkerType.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("MpsWorkerType: ");
    stringBuilder.append("id=").append(this.id);
    stringBuilder.append(", messageFamily=").append(this.messageFamily);
    stringBuilder.append(", inbound=").append(this.inbound);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mps\MpsWorkerType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */