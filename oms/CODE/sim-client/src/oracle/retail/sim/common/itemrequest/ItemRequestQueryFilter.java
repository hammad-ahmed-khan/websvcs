package oracle.retail.sim.common.itemrequest;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class ItemRequestQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -8411172173243598152L;
  
  private String userId;
  
  private Long storeId;
  
  private ItemRequestStatus status;
  
  private String itemId;
  
  private String departmentId;
  
  private Long itemRequestId;
  
  private Date fromExpirationDate;
  
  private Date toExpirationDate;
  
  private Date fromRequestDate;
  
  private Date toRequestDate;
  
  private DeliveryTimeSlot deliveryTimeSlot;
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public String getDepartmentId() {
    return this.departmentId;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public Long getItemRequestId() {
    return this.itemRequestId;
  }
  
  public ItemRequestStatus getStatus() {
    return this.status;
  }
  
  public Date getFromExpirationDate() {
    return this.fromExpirationDate;
  }
  
  public Date getToExpirationDate() {
    return this.toExpirationDate;
  }
  
  public Date getFromRequestDate() {
    return this.fromRequestDate;
  }
  
  public Date getToRequestDate() {
    return this.toRequestDate;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public DeliveryTimeSlot getDeliveryTimeSlot() {
    return this.deliveryTimeSlot;
  }
  
  public void setItemId(String paramString) throws BusinessException {
    executeRule("setItemId", new Object[] { paramString });
    doSetItemId(paramString);
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public void setItemRequestId(Long paramLong) throws BusinessException {
    executeRule("setItemRequestId", new Object[] { paramLong });
    doSetItemRequestId(paramLong);
  }
  
  public void doSetItemRequestId(Long paramLong) {
    this.itemRequestId = paramLong;
  }
  
  public void setStatus(ItemRequestStatus paramItemRequestStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramItemRequestStatus });
    doSetStatus(paramItemRequestStatus);
  }
  
  public void doSetStatus(ItemRequestStatus paramItemRequestStatus) {
    this.status = paramItemRequestStatus;
  }
  
  public void setDepartmentId(String paramString) throws BusinessException {
    executeRule("setDepartmentId", new Object[] { paramString });
    doSetDepartmentId(paramString);
  }
  
  public void doSetDepartmentId(String paramString) {
    this.departmentId = paramString;
  }
  
  public void setUserId(String paramString) throws BusinessException {
    executeRule("setUserId", new Object[] { paramString });
    doSetUserId(paramString);
  }
  
  public void doSetUserId(String paramString) {
    this.userId = paramString;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public void setFromExpirationDate(Date paramDate) throws BusinessException {
    DateRangeValidRule.execute(paramDate, this.toExpirationDate);
    executeRule("setFromExpirationDate", new Object[] { paramDate });
    doSetFromExpirationDate(paramDate);
  }
  
  public void doSetFromExpirationDate(Date paramDate) throws BusinessException {
    this.fromExpirationDate = paramDate;
  }
  
  public void setFromRequestDate(Date paramDate) throws BusinessException {
    DateRangeValidRule.execute(paramDate, this.toRequestDate);
    executeRule("setFromRequestDate", new Object[] { paramDate });
    doSetFromRequestDate(paramDate);
  }
  
  public void doSetFromRequestDate(Date paramDate) throws BusinessException {
    this.fromRequestDate = paramDate;
  }
  
  public void setToExpirationDate(Date paramDate) throws BusinessException {
    DateRangeValidRule.execute(this.fromExpirationDate, paramDate);
    executeRule("setToExpirationDate", new Object[] { paramDate });
    doSetToExpirationDate(paramDate);
  }
  
  public void doSetToExpirationDate(Date paramDate) throws BusinessException {
    this.toExpirationDate = paramDate;
  }
  
  public void setToRequestDate(Date paramDate) throws BusinessException {
    DateRangeValidRule.execute(this.fromRequestDate, paramDate);
    executeRule("setToRequestDate", new Object[] { paramDate });
    doSetToRequestDate(paramDate);
  }
  
  public void doSetToRequestDate(Date paramDate) throws BusinessException {
    this.toRequestDate = paramDate;
  }
  
  public void setDeliveryTimeSlot(DeliveryTimeSlot paramDeliveryTimeSlot) throws BusinessException {
    executeRule("setDeliveryTimeSlot", new Object[] { paramDeliveryTimeSlot });
    doSetDeliveryTimeSlot(paramDeliveryTimeSlot);
  }
  
  public void doSetDeliveryTimeSlot(DeliveryTimeSlot paramDeliveryTimeSlot) throws BusinessException {
    this.deliveryTimeSlot = paramDeliveryTimeSlot;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itemrequest\ItemRequestQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */