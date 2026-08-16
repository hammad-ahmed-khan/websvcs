package oracle.retail.sim.common.warehousedelivery;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.source.SourceVO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WarehouseDeliveryVO implements Serializable {
  private static final long serialVersionUID = -6875115971785817166L;
  
  private Long id;
  
  private Long storeId;
  
  private SourceVO source;
  
  private WarehouseDeliveryStatus status;
  
  private String asnId;
  
  private Date expectedArrivalDate;
  
  private Date createDate;
  
  private Date updateDate;
  
  private Date completeDate;
  
  private String userId;
  
  private String comments;
  
  private boolean fulfillmentOrderRelated;
  
  private Integer numberOfCartons;
  
  private boolean missingCartons;
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public SourceVO getSource() {
    return this.source;
  }
  
  public void doSetSource(SourceVO paramSourceVO) {
    this.source = paramSourceVO;
  }
  
  public WarehouseDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(WarehouseDeliveryStatus paramWarehouseDeliveryStatus) {
    this.status = paramWarehouseDeliveryStatus;
  }
  
  public String getAsnId() {
    return this.asnId;
  }
  
  public void doSetAsnId(String paramString) {
    this.asnId = paramString;
  }
  
  public Date getExpectedArrivalDate() {
    return this.expectedArrivalDate;
  }
  
  public void doSetExpectedArrivalDate(Date paramDate) {
    this.expectedArrivalDate = paramDate;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public Date getUpdateDate() {
    return this.updateDate;
  }
  
  public void doSetUpdateDate(Date paramDate) {
    this.updateDate = paramDate;
  }
  
  public Date getCompleteDate() {
    return this.completeDate;
  }
  
  public void doSetCompleteDate(Date paramDate) {
    this.completeDate = paramDate;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void doSetUserId(String paramString) {
    this.userId = paramString;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public boolean isFulfillmentOrderRelated() {
    return this.fulfillmentOrderRelated;
  }
  
  public void doSetFulfillmentOrderRelated(boolean paramBoolean) {
    this.fulfillmentOrderRelated = paramBoolean;
  }
  
  public Integer getNumberOfCartons() {
    return this.numberOfCartons;
  }
  
  public void doSetNumberOfCartons(Integer paramInteger) {
    this.numberOfCartons = paramInteger;
  }
  
  public boolean isMissingCartons() {
    return this.missingCartons;
  }
  
  public void doSetMissingCartons(boolean paramBoolean) {
    this.missingCartons = paramBoolean;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    WarehouseDeliveryVO warehouseDeliveryVO = (WarehouseDeliveryVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, warehouseDeliveryVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */