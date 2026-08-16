package oracle.retail.sim.common.schedule;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProductGroupBatchVO implements Serializable {
  private static final long serialVersionUID = 5325834987726572749L;
  
  private Long storeId = null;
  
  private Long scheduleId = null;
  
  private Date scheduleDate = null;
  
  private ProductGroupType type = null;
  
  public ProductGroupBatchVO(Long paramLong1, Long paramLong2) {
    this.scheduleId = paramLong1;
    this.storeId = paramLong2;
  }
  
  public Date getScheduleDate() {
    return this.scheduleDate;
  }
  
  public void doSetScheduleDate(Date paramDate) {
    this.scheduleDate = paramDate;
  }
  
  public Long getScheduleId() {
    return this.scheduleId;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public ProductGroupType getType() {
    return this.type;
  }
  
  public void doSetProductGroupType(ProductGroupType paramProductGroupType) {
    this.type = paramProductGroupType;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("StoreId=" + this.storeId);
    stringBuilder.append(", scheduleId=" + this.scheduleId);
    stringBuilder.append(", scheduleDate=" + this.scheduleDate);
    stringBuilder.append(", productGroupType=" + this.type);
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ProductGroupBatchVO productGroupBatchVO = (ProductGroupBatchVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.storeId, productGroupBatchVO.storeId);
    equalsBuilder.append(this.scheduleId, productGroupBatchVO.scheduleId);
    equalsBuilder.append(this.scheduleDate, productGroupBatchVO.scheduleDate);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.storeId);
    hashCodeBuilder.append(this.scheduleId);
    hashCodeBuilder.append(this.scheduleDate);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\ProductGroupBatchVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */