package oracle.retail.sim.common.itemrequest;

import java.io.Serializable;
import java.util.Date;

public class ItemRequestVO implements Serializable {
  private static final long serialVersionUID = 2466869566592728930L;
  
  private Long id;
  
  private Long storeId;
  
  private String employeeId;
  
  private String scheduleDescription;
  
  private Date requiredDeliveryDate;
  
  private Date expirationDate;
  
  private ItemRequestStatus status;
  
  private int numberOfLineItems;
  
  private String departmentId;
  
  public ItemRequestVO(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id == null) ? null : this.id.toString();
  }
  
  public String getEmployeeId() {
    return this.employeeId;
  }
  
  public Date getExpirationDate() {
    return this.expirationDate;
  }
  
  public int getNumberOfLineItems() {
    return this.numberOfLineItems;
  }
  
  public Date getRequiredDeliveryDate() {
    return this.requiredDeliveryDate;
  }
  
  public String getScheduleDescription() {
    return this.scheduleDescription;
  }
  
  public ItemRequestStatus getStatus() {
    return this.status;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public String getDepartmentId() {
    return this.departmentId;
  }
  
  public void doSetEmployeeId(String paramString) {
    this.employeeId = paramString;
  }
  
  public void doSetExpirationDate(Date paramDate) {
    this.expirationDate = paramDate;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void doSetNumberOfLineItems(int paramInt) {
    this.numberOfLineItems = paramInt;
  }
  
  public void doSetRequiredDeliveryDate(Date paramDate) {
    this.requiredDeliveryDate = paramDate;
  }
  
  public void doSetScheduleDescription(String paramString) {
    this.scheduleDescription = paramString;
  }
  
  public void doSetStatus(ItemRequestStatus paramItemRequestStatus) {
    this.status = paramItemRequestStatus;
  }
  
  public void doSetDepartmentId(String paramString) {
    this.departmentId = paramString;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append("[ID:" + this.id);
    stringBuilder.append(",Store:" + this.storeId);
    stringBuilder.append(",NumberOfLineItem:" + this.numberOfLineItems);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itemrequest\ItemRequestVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */