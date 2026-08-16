package oracle.retail.sim.common.batch;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessObject;

public class BatchImport extends BusinessObject {
  private static final long serialVersionUID = 6032427212689664492L;
  
  private Long id;
  
  private String batchName;
  
  private String dataFileName;
  
  private Date startTime;
  
  private Date endTime;
  
  private Date createdTime;
  
  private String errorMessage;
  
  private BatchImportStatus status;
  
  private Long retryCount;
  
  public BatchImportStatus getStatus() {
    return this.status;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public String getBatchName() {
    return this.batchName;
  }
  
  public String getDataFileName() {
    return this.dataFileName;
  }
  
  public Date getEndTime() {
    return this.endTime;
  }
  
  public Date getCreatedTime() {
    return this.createdTime;
  }
  
  public void setId(Long paramLong) {
    doSetId(paramLong);
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Date getStartDate() {
    return this.startTime;
  }
  
  public String getErrorMessage() {
    return this.errorMessage;
  }
  
  public void doSetErrorMessage(String paramString) {
    this.errorMessage = paramString;
  }
  
  public void setErrorMessage(String paramString) {
    doSetErrorMessage(paramString);
  }
  
  public void doSetBatchName(String paramString) {
    this.batchName = paramString;
  }
  
  public void doSetDataFileName(String paramString) {
    this.dataFileName = paramString;
  }
  
  public void doSetStartTime(Date paramDate) {
    this.startTime = paramDate;
  }
  
  public void doSetEndTime(Date paramDate) {
    this.endTime = paramDate;
  }
  
  public void doSetCreatedTime(Date paramDate) {
    this.createdTime = paramDate;
  }
  
  public void setBatchName(String paramString) {
    doSetBatchName(paramString);
  }
  
  public void setDataFileName(String paramString) {
    doSetDataFileName(paramString);
  }
  
  public void setStartTime(Date paramDate) {
    doSetStartTime(paramDate);
  }
  
  public void setEndTime(Date paramDate) {
    doSetEndTime(paramDate);
  }
  
  public void setCreatedDate(Date paramDate) {
    doSetCreatedTime(paramDate);
  }
  
  public void doSetStatus(BatchImportStatus paramBatchImportStatus) {
    this.status = paramBatchImportStatus;
  }
  
  public void setStatus(BatchImportStatus paramBatchImportStatus) {
    doSetStatus(paramBatchImportStatus);
  }
  
  public Long getRetryCount() {
    return Long.valueOf((this.retryCount == null) ? 0L : this.retryCount.longValue());
  }
  
  public void setRetryCount(Long paramLong) {
    this.retryCount = paramLong;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\batch\BatchImport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */