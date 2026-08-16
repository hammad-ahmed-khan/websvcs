package oracle.retail.sim.common.directdelivery;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.source.SupplierVO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PurchaseOrderVO implements Serializable {
  private static final long serialVersionUID = -6124946107514390911L;
  
  private Long id;
  
  private Long storeId;
  
  private SupplierVO supplier;
  
  private PurchaseOrderStatus status;
  
  private String externalId;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private Date createDate;
  
  private Date updateDate;
  
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
  
  public PurchaseOrderStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(PurchaseOrderStatus paramPurchaseOrderStatus) {
    this.status = paramPurchaseOrderStatus;
  }
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void doSetExternalId(String paramString) {
    this.externalId = paramString;
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public String getFulfillmentOrderExternalId() {
    return this.fulfillmentOrderExternalId;
  }
  
  public void doSetFulfillmentOrderExternalId(String paramString) {
    this.fulfillmentOrderExternalId = paramString;
  }
  
  public boolean isFulfillmentOrderRelated() {
    return !StringHelper.isNullOrEmpty(this.customerOrderId);
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
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    PurchaseOrderVO purchaseOrderVO = (PurchaseOrderVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, purchaseOrderVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\PurchaseOrderVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */