package oracle.retail.sim.common.warehousedelivery;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.source.SourceVO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WarehouseDeliveryCartonVO implements Serializable {
  private static final long serialVersionUID = 676026535081984710L;
  
  private Long id;
  
  private Long deliveryId;
  
  private String externalId;
  
  private WarehouseDeliveryStatus status;
  
  private Long storeId;
  
  private SourceVO source;
  
  private String asnId;
  
  private Date expectedArrivalDate;
  
  private Date completeDate;
  
  private boolean fulfillmentOrderRelated;
  
  private Integer numberOfCasesExpected;
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getDeliveryId() {
    return this.deliveryId;
  }
  
  public void doSetDeliveryId(Long paramLong) {
    this.deliveryId = paramLong;
  }
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void doSetExternalId(String paramString) {
    this.externalId = paramString;
  }
  
  public WarehouseDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(WarehouseDeliveryStatus paramWarehouseDeliveryStatus) {
    this.status = paramWarehouseDeliveryStatus;
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
  
  public Date getCompleteDate() {
    return this.completeDate;
  }
  
  public void doSetCompleteDate(Date paramDate) {
    this.completeDate = paramDate;
  }
  
  public boolean isFulfillmentOrderRelated() {
    return this.fulfillmentOrderRelated;
  }
  
  public void doSetFulfillmentOrderRelated(boolean paramBoolean) {
    this.fulfillmentOrderRelated = paramBoolean;
  }
  
  public Integer getNumberOfCasesExpected() {
    return this.numberOfCasesExpected;
  }
  
  public void doSetNumberOfCasesExpected(Integer paramInteger) {
    this.numberOfCasesExpected = paramInteger;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    WarehouseDeliveryCartonVO warehouseDeliveryCartonVO = (WarehouseDeliveryCartonVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, warehouseDeliveryCartonVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryCartonVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */