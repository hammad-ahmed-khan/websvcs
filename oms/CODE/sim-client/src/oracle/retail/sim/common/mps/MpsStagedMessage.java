package oracle.retail.sim.common.mps;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.integration.SimMessageDirection;
import oracle.retail.sim.common.integration.SimMessageFamily;
import oracle.retail.sim.common.integration.SimMessageType;
import oracle.retail.sim.common.logging.LogService;

public class MpsStagedMessage implements Serializable {
  private static final long serialVersionUID = 2693091684432756603L;
  
  private Long id;
  
  private Long jobId;
  
  private Long storeId;
  
  private SimMessageType messageType;
  
  private boolean inbound;
  
  private Date createTime;
  
  private Date updateTime;
  
  private long retryCount;
  
  private String messageError;
  
  private String businessId;
  
  private String messageDescription;
  
  private String messageData;
  
  private boolean processed;
  
  private boolean deleted;
  
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
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public long getRetryCount() {
    return this.retryCount;
  }
  
  public void setRetryCount(long paramLong) {
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
  
  public String getMessageError() {
    return this.messageError;
  }
  
  public void setMessageError(Throwable paramThrowable) {
    setMessageError(LogService.getStackTraceAsString(paramThrowable));
  }
  
  public void setMessageError(String paramString) {
    this.messageError = paramString;
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
  
  public String getMessageData() {
    return this.messageData;
  }
  
  public void setMessageData(String paramString) {
    this.messageData = paramString;
  }
  
  public SimMessageType getMessageType() {
    return this.messageType;
  }
  
  public void setMessageType(SimMessageType paramSimMessageType) {
    this.messageType = paramSimMessageType;
  }
  
  public SimMessageFamily getMessageFamily() {
    return (this.messageType != null) ? this.messageType.getFamily() : null;
  }
  
  public boolean isInbound() {
    return this.inbound;
  }
  
  public void setInbound(boolean paramBoolean) {
    this.inbound = paramBoolean;
  }
  
  public boolean isProcessed() {
    return this.processed;
  }
  
  public void setProcessed(boolean paramBoolean) {
    this.processed = paramBoolean;
  }
  
  public boolean isDeleted() {
    return this.deleted;
  }
  
  public void setDeleted(boolean paramBoolean) {
    this.deleted = paramBoolean;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(SimMessageDirection.toValue(this.inbound));
    stringBuilder.append(":").append(getMessageFamily());
    stringBuilder.append(":").append(this.messageType);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mps\MpsStagedMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */