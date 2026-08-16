package oracle.retail.sim.common.uda;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.type.Displayable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class UDADetail extends BusinessObject implements Displayable {
  private static final long serialVersionUID = 1541290278669592830L;
  
  private Long id;
  
  private UDAType type;
  
  private String description;
  
  private boolean printTicket;
  
  private boolean printLabel;
  
  private boolean dirty;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public UDAType getType() {
    return this.type;
  }
  
  public void doSetType(UDAType paramUDAType) {
    this.type = paramUDAType;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public boolean isPrintTicket() {
    return this.printTicket;
  }
  
  public void setPrintTicket(boolean paramBoolean) throws BusinessException {
    executeRule("setPrintTicket", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetPrintTicket(paramBoolean);
    this.dirty = true;
  }
  
  public void doSetPrintTicket(boolean paramBoolean) {
    this.printTicket = paramBoolean;
  }
  
  public boolean isPrintLabel() {
    return this.printLabel;
  }
  
  public void setPrintLabel(boolean paramBoolean) throws BusinessException {
    executeRule("setPrintLabel", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetPrintLabel(paramBoolean);
    this.dirty = true;
  }
  
  public void doSetPrintLabel(boolean paramBoolean) {
    this.printLabel = paramBoolean;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty(boolean paramBoolean) {
    this.dirty = paramBoolean;
  }
  
  public boolean isCoherent() throws BusinessException {
    executeRule("isCoherent", new Object[0]);
    return true;
  }
  
  public boolean isPropertyModifiable(String paramString) {
    try {
      executeRule("isPropertyModifiable", new Object[] { paramString, this });
    } catch (Throwable throwable) {
      return false;
    } 
    return true;
  }
  
  public String toDisplayString() {
    return this.description;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    UDADetail uDADetail = (UDADetail)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, uDADetail.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\commo\\uda\UDADetail.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */