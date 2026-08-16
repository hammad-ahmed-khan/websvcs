package oracle.retail.sim.common.schedule;

import java.io.Serializable;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import org.apache.commons.lang.builder.EqualsBuilder;

public class ProductGroupScheduleVO implements Serializable {
  private static final long serialVersionUID = 6637555610145400654L;
  
  private Long id = null;
  
  private String description = null;
  
  private ScheduleStatus status = ScheduleStatus.OPEN;
  
  private Schedule schedule = null;
  
  private Long groupId = null;
  
  private String groupDescription = null;
  
  private ProductGroupType groupType = null;
  
  private String storeId = null;
  
  private int storeCount = 0;
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (getId() != null) ? getId().toString() : null;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public ScheduleStatus getStatus() {
    return this.status;
  }
  
  public Schedule getSchedule() {
    return this.schedule;
  }
  
  public Long getGroupId() {
    return this.groupId;
  }
  
  public String getGroupDescription() {
    return this.groupDescription;
  }
  
  public String getFullGroupDescription() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.groupId != null)
      stringBuilder.append(String.valueOf(this.groupId)); 
    if (this.groupDescription != null) {
      stringBuilder.append(" - ");
      stringBuilder.append(this.groupDescription);
    } 
    return stringBuilder.toString();
  }
  
  public ProductGroupType getGroupType() {
    return this.groupType;
  }
  
  public String getStoreId() {
    return (this.storeCount > 1) ? "Multiple" : this.storeId;
  }
  
  public String getVOStoreId() {
    return this.storeId;
  }
  
  public int getStoreCount() {
    return this.storeCount;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public void doSetStatus(ScheduleStatus paramScheduleStatus) {
    this.status = paramScheduleStatus;
  }
  
  public void doSetSchedule(Schedule paramSchedule) {
    this.schedule = paramSchedule;
  }
  
  public void doSetGroupId(Long paramLong) {
    this.groupId = paramLong;
  }
  
  public void doSetGroupDescription(String paramString) {
    this.groupDescription = paramString;
  }
  
  public void doSetGroupType(ProductGroupType paramProductGroupType) {
    this.groupType = paramProductGroupType;
  }
  
  public void doSetStoreId(String paramString) {
    this.storeId = paramString;
  }
  
  public void doSetStoreCount(int paramInt) {
    this.storeCount = paramInt;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Product Group Schedule VO[ Id=");
    stringBuilder.append(this.id);
    stringBuilder.append(" Description=");
    stringBuilder.append(this.description);
    stringBuilder.append(" Status=");
    stringBuilder.append(this.status);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ProductGroupScheduleVO productGroupScheduleVO = (ProductGroupScheduleVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, productGroupScheduleVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    return super.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\ProductGroupScheduleVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */