package oracle.retail.sim.common.mps;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.integration.SimMessageDirection;
import oracle.retail.sim.common.integration.SimMessageFamily;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MpsWorkerTypeVO extends BusinessObject {
  private static final long serialVersionUID = 8631163818607508713L;
  
  private static final Long ONE_YEAR = Long.valueOf(31536000000L);
  
  private long id;
  
  private SimMessageFamily messageFamily;
  
  private boolean inbound;
  
  private boolean enabled;
  
  private int pendingCount;
  
  private int retryCount;
  
  private int failedCount;
  
  private Date lastCreateTime;
  
  private Date lastUpdateTime;
  
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
  
  public SimMessageDirection getMessageDirection() {
    return SimMessageDirection.toValue(isInbound());
  }
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.enabled = paramBoolean;
  }
  
  public int getPendingCount() {
    return this.pendingCount;
  }
  
  public void setPendingCount(int paramInt) {
    this.pendingCount = paramInt;
  }
  
  public int getRetryCount() {
    return this.retryCount;
  }
  
  public void setRetryCount(int paramInt) {
    this.retryCount = paramInt;
  }
  
  public int getFailedCount() {
    return this.failedCount;
  }
  
  public void setFailedCount(int paramInt) {
    this.failedCount = paramInt;
  }
  
  public Date getLastCreateTime() {
    return this.lastCreateTime;
  }
  
  public void setLastCreateTime(Date paramDate) {
    this.lastCreateTime = paramDate;
  }
  
  public Date getLastUpdateTime() {
    return this.lastUpdateTime;
  }
  
  public void setLastUpdateTime(Date paramDate) {
    this.lastUpdateTime = paramDate;
  }
  
  public long getDurationSinceLastCreate() {
    if (getLastCreateTime() != null) {
      long l = SimDateUtil.getCurrentDate().getTime() - getLastCreateTime().getTime();
      if (l < ONE_YEAR.longValue())
        return l; 
      if (l < 0L)
        return 0L; 
    } 
    return -1L;
  }
  
  public long getDurationSinceLastUpdate() {
    if (getLastUpdateTime() != null) {
      long l = SimDateUtil.getCurrentDate().getTime() - getLastUpdateTime().getTime();
      if (l < ONE_YEAR.longValue())
        return l; 
      if (l < 0L)
        return 0L; 
    } 
    return -1L;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    MpsWorkerTypeVO mpsWorkerTypeVO = (MpsWorkerTypeVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, mpsWorkerTypeVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("MpsWorkerTypeVO: ");
    stringBuilder.append("id=").append(this.id);
    stringBuilder.append(", messageFamily=").append(this.messageFamily);
    stringBuilder.append(", inbound=").append(this.inbound);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mps\MpsWorkerTypeVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */