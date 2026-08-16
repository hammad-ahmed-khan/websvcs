package oracle.retail.sim.common.directdelivery;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.source.SupplierVO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DirectDeliveryCartonVO implements Serializable {
  private static final long serialVersionUID = -4705105948803827072L;
  
  private Long id;
  
  private Long deliveryId;
  
  private String externalId;
  
  private DirectDeliveryStatus status;
  
  private Long storeId;
  
  private SupplierVO supplier;
  
  private String asnId;
  
  private Date expectedArrivalDate;
  
  private Date completeDate;
  
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
  
  public DirectDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(DirectDeliveryStatus paramDirectDeliveryStatus) {
    this.status = paramDirectDeliveryStatus;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public SupplierVO getSupplier() {
    return this.supplier;
  }
  
  public void doSetSupplier(SupplierVO paramSupplierVO) {
    this.supplier = paramSupplierVO;
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
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    DirectDeliveryCartonVO directDeliveryCartonVO = (DirectDeliveryCartonVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, directDeliveryCartonVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryCartonVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */