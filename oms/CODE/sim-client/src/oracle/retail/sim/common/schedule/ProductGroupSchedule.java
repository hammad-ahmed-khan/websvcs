package oracle.retail.sim.common.schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.rules.core.MaxStringSize255Rule;

public class ProductGroupSchedule extends BusinessObject {
  private static final long serialVersionUID = -760257267222332299L;
  
  public static final Long NO_ID = Long.valueOf(0L);
  
  private Long id = null;
  
  private String description = "";
  
  private ProductGroup productGroup = null;
  
  private List<Long> storeIdList = new ArrayList<>();
  
  private Schedule schedule = null;
  
  private ScheduleStatus status = ScheduleStatus.OPEN;
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id != null) ? this.id.toString() : null;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public ProductGroup getProductGroup() {
    return this.productGroup;
  }
  
  public List<Long> getStoreIds() {
    return Collections.unmodifiableList(this.storeIdList);
  }
  
  public boolean hasStore(Long paramLong) {
    for (Long long_ : this.storeIdList) {
      if (paramLong.equals(long_))
        return true; 
    } 
    return false;
  }
  
  public Schedule getSchedule() {
    return this.schedule;
  }
  
  public ScheduleStatus getStatus() {
    return this.status;
  }
  
  public void setId(Long paramLong) throws BusinessException {
    checkForNullParameter("Id", paramLong);
    executeRule("setId", new Object[] { paramLong });
    doSetId(paramLong);
  }
  
  public void setDescription(String paramString) throws BusinessException {
    checkForNullParameter("Description", paramString);
    MaxStringSize255Rule.execute(paramString);
    executeRule("setDescription", new Object[] { paramString });
    doSetDescription(paramString);
  }
  
  public void setProductGroup(ProductGroup paramProductGroup) throws BusinessException {
    checkForNullParameter("Group", paramProductGroup);
    executeRule("setProductGroup", new Object[] { paramProductGroup });
    doSetProductGroup(paramProductGroup);
  }
  
  public void setStoreIds(List<Long> paramList) throws BusinessException {
    executeRule("Stores", new Object[] { paramList });
    doSetStores(paramList);
  }
  
  public void removeAllStores() throws BusinessException {
    executeRule("removeAllStores", new Object[] { this.storeIdList });
    doRemoveAllStores();
  }
  
  public boolean isEditable(Integer paramInteger) throws BusinessException {
    checkForNullParameter("Lockout Days", paramInteger);
    if (this.schedule != null && this.schedule.getStartDate() != null) {
      int i = paramInteger.intValue() - 1;
      Date date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), SimDateUtil.getCurrentDate(), Integer.valueOf(i));
      if (this.schedule.getStartDate().getTime() < date.getTime())
        throw new BusinessException(ProductGroupScheduleMessageText.START_DATE_EDIT_ERROR); 
    } 
    executeRule("isEditable", new Object[] { paramInteger });
    return true;
  }
  
  public void setSchedule(Schedule paramSchedule) throws BusinessException {
    checkForNullParameter("Schedule", paramSchedule);
    executeRule("setSchedule", new Object[] { paramSchedule });
    doSetSchedule(paramSchedule);
  }
  
  public void setStatus(ScheduleStatus paramScheduleStatus) throws BusinessException {
    checkForNullParameter("Status", paramScheduleStatus);
    if (paramScheduleStatus == ScheduleStatus.CLOSED && this.productGroup.getType().isStockCountUnit())
      throw new BusinessException(ProductGroupScheduleMessageText.STATUS_INVALID); 
    executeRule("setStatus", new Object[] { paramScheduleStatus });
    doSetStatus(paramScheduleStatus);
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public void doSetDescription(String paramString) {
    if (paramString != null)
      this.description = paramString; 
  }
  
  public void doSetProductGroup(ProductGroup paramProductGroup) {
    if (paramProductGroup != null)
      this.productGroup = paramProductGroup; 
  }
  
  public void doAddStore(Long paramLong) {
    if (paramLong != null)
      this.storeIdList.add(paramLong); 
  }
  
  public void doSetStores(List<Long> paramList) {
    this.storeIdList = paramList;
  }
  
  public void doRemoveAllStores() {
    this.storeIdList.clear();
  }
  
  public void doSetSchedule(Schedule paramSchedule) {
    this.schedule = paramSchedule;
  }
  
  public void doSetStatus(ScheduleStatus paramScheduleStatus) {
    if (paramScheduleStatus != null)
      this.status = paramScheduleStatus; 
  }
  
  public boolean isUnitAndAmount() {
    return (this.productGroup != null) ? this.productGroup.getType().isStockCountUnitAmount() : false;
  }
  
  public boolean isPropertyModifiable(String paramString) {
    try {
      executeRule("isPropertyModifiable", new Object[] { paramString });
    } catch (BusinessException businessException) {
      return false;
    } 
    return true;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (StringHelper.isNullOrEmpty(this.description))
      throw new BusinessException(ProductGroupScheduleMessageText.MISSING_DESCRIPTION); 
    if (this.productGroup == null)
      throw new BusinessException(ProductGroupScheduleMessageText.MISSING_GROUP); 
    if (this.storeIdList == null || this.storeIdList.size() <= 0)
      throw new BusinessException(ProductGroupScheduleMessageText.MISSING_STORE); 
    if (this.schedule instanceof WeeklySchedule) {
      Set<Integer> set = ((WeeklySchedule)this.schedule).getDaysOfTheWeek();
      if (set == null || set.isEmpty())
        throw new BusinessException(ProductGroupScheduleMessageText.MISSING_DAY_OF_WEEK); 
    } 
    executeRule("isCoherent", new Object[0]);
    return true;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Product Group Schedule [id=)").append(this.id);
    stringBuilder.append(" Description=").append(this.description);
    if (this.productGroup != null)
      stringBuilder.append(" Product Group Id=").append(this.productGroup.getId()); 
    stringBuilder.append(" Schedule=").append(this.schedule);
    stringBuilder.append(" Status=").append(this.status);
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ProductGroupSchedule productGroupSchedule = (ProductGroupSchedule)paramObject;
    return this.id.equals(productGroupSchedule.id);
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\ProductGroupSchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */