package oracle.retail.sim.common.mps;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.integration.SimMessageDirection;
import oracle.retail.sim.common.integration.SimMessageFamily;
import oracle.retail.sim.common.integration.SimMessageType;

public class MpsStagedMessageVO implements Serializable {
  private static final long serialVersionUID = 2693091684432756603L;
  
  private Long id;
  
  private Long jobId;
  
  private boolean inbound;
  
  private SimMessageType messageType;
  
  private Date createTime;
  
  private Date updateTime;
  
  private Long retryCount;
  
  private String businessId;
  
  private String messageDescription;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getJobId() {
    return this.jobId;
  }
  
  public void setJobId(Long paramLong) {
    this.jobId = paramLong;
  }
  
  public boolean isInbound() {
    return this.inbound;
  }
  
  public void setInbound(boolean paramBoolean) {
    this.inbound = paramBoolean;
  }
  
  public SimMessageDirection getMessageDirection() {
    return SimMessageDirection.toValue(this.inbound);
  }
  
  public SimMessageType getMessageType() {
    return this.messageType;
  }
  
  public void setMessageType(SimMessageType paramSimMessageType) {
    this.messageType = paramSimMessageType;
  }
  
  public SimMessageFamily getMessageFamily() {
    return (this.messageType == null) ? null : this.messageType.getFamily();
  }
  
  public Date getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(Date paramDate) {
    this.createTime = paramDate;
  }
  
  public Date getUpdateTime() {
    return this.updateTime;
  }
  
  public void setUpdateTime(Date paramDate) {
    this.updateTime = paramDate;
  }
  
  public Long getRetryCount() {
    return this.retryCount;
  }
  
  public void setRetryCount(Long paramLong) {
    this.retryCount = paramLong;
  }
  
  public String getBusinessId() {
    return this.businessId;
  }
  
  public void setBusinessId(String paramString) {
    this.businessId = paramString;
  }
  
  public String getMessageDescription() {
    return this.messageDescription;
  }
  
  public void setMessageDescription(String paramString) {
    this.messageDescription = paramString;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getMessageDirection());
    stringBuilder.append(":").append(getMessageFamily());
    stringBuilder.append(":").append(this.messageType);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mps\MpsStagedMessageVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */