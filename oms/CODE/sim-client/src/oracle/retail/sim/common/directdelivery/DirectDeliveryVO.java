package oracle.retail.sim.common.directdelivery;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.source.SupplierVO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DirectDeliveryVO implements Serializable {
  private static final long serialVersionUID = -1790416521191769659L;
  
  private Long id;
  
  private Long storeId;
  
  private SupplierVO supplier;
  
  private DirectDeliveryStatus status;
  
  private PurchaseOrderVO purchaseOrder;
  
  private String asnId;
  
  private Date expectedArrivalDate;
  
  private Date createDate;
  
  private Date updateDate;
  
  private Date completeDate;
  
  private String invoiceNumber;
  
  private Date invoiceDate;
  
  private String userId;
  
  private String comments;
  
  private Integer numberOfLineItems;
  
  private Integer numberOfLineItemsReceived;
  
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
  
  public DirectDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(DirectDeliveryStatus paramDirectDeliveryStatus) {
    this.status = paramDirectDeliveryStatus;
  }
  
  public PurchaseOrderVO getPurchaseOrder() {
    return this.purchaseOrder;
  }
  
  public void doSetPurchaseOrder(PurchaseOrderVO paramPurchaseOrderVO) {
    this.purchaseOrder = paramPurchaseOrderVO;
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
  
  public String getInvoiceNumber() {
    return this.invoiceNumber;
  }
  
  public void doSetInvoiceNumber(String paramString) {
    this.invoiceNumber = paramString;
  }
  
  public Date getInvoiceDate() {
    return this.invoiceDate;
  }
  
  public void doSetInvoiceDate(Date paramDate) {
    this.invoiceDate = paramDate;
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
  
  public Integer getNumberOfLineItems() {
    return this.numberOfLineItems;
  }
  
  public void doSetNumberOfLineItems(Integer paramInteger) {
    this.numberOfLineItems = paramInteger;
  }
  
  public Integer getNumberOfLineItemsReceived() {
    return this.numberOfLineItemsReceived;
  }
  
  public void doSetNumberOfLineItemsReceived(Integer paramInteger) {
    this.numberOfLineItemsReceived = paramInteger;
  }
  
  public boolean isFulfillmentOrderRelated() {
    return (this.purchaseOrder != null && this.purchaseOrder.isFulfillmentOrderRelated());
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    DirectDeliveryVO directDeliveryVO = (DirectDeliveryVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, directDeliveryVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */