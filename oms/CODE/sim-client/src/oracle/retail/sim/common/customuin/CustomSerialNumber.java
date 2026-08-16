package oracle.retail.sim.common.customuin;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.uin.UINStatus;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class CustomSerialNumber extends BusinessObject {
  private static final long serialVersionUID = 7328521596108816814L;
  
  private Long id;
  
  private UINStatus status;
  
  public void setId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public Long getId() {
    return this.id;
  }
  
  public UINStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(UINStatus paramUINStatus) throws BusinessException {
    checkForNullParameter("Status", paramUINStatus);
    executeRule("setStatus", new Object[] { paramUINStatus });
    this.status = paramUINStatus;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    CustomSerialNumber customSerialNumber = (CustomSerialNumber)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, customSerialNumber.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\customuin\CustomSerialNumber.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */