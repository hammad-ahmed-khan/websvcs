package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.productgroup.ProductGroupType;

public class ProductGroupScheduleQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -2294408021435443296L;
  
  private Long storeId;
  
  private String description;
  
  private Long groupId;
  
  private ProductGroupType groupType;
  
  private Date nextDate;
  
  private Date lastDate;
  
  private List<Long> authorizedStoreIds;
  
  private ScheduleStatus status = ScheduleStatus.OPEN;
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String paramString) throws BusinessException {
    executeRule("setDescription", new Object[] { paramString });
    doSetDescription(paramString);
  }
  
  public void doSetDescription(String paramString) {
    this.description = StringHelper.trimToNull(paramString);
  }
  
  public Long getGroupId() {
    return this.groupId;
  }
  
  public void setGroupId(Long paramLong) throws BusinessException {
    executeRule("setGroupId", new Object[] { paramLong });
    doSetGroupId(paramLong);
  }
  
  public void doSetGroupId(Long paramLong) {
    this.groupId = paramLong;
  }
  
  public ProductGroupType getGroupType() {
    return this.groupType;
  }
  
  public void setGroupType(ProductGroupType paramProductGroupType) throws BusinessException {
    executeRule("setGroupType", new Object[] { paramProductGroupType });
    doSetGroupType(paramProductGroupType);
  }
  
  public void doSetGroupType(ProductGroupType paramProductGroupType) {
    this.groupType = paramProductGroupType;
  }
  
  public Date getNextDate() {
    return (this.nextDate != null) ? this.nextDate : null;
  }
  
  public void setNextDate(Date paramDate, TimeZone paramTimeZone) throws BusinessException {
    executeRule("setNextDate", new Object[] { paramDate });
    doSetNextDate(paramDate, paramTimeZone);
  }
  
  public void doSetNextDate(Date paramDate, TimeZone paramTimeZone) {
    if (paramDate == null || paramTimeZone == null) {
      this.nextDate = null;
    } else {
      this.nextDate = SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate);
    } 
  }
  
  public Date getLastDate() {
    return (this.lastDate != null) ? this.lastDate : null;
  }
  
  public void setLastDate(Date paramDate, TimeZone paramTimeZone) throws BusinessException {
    executeRule("setLastDate", new Object[] { paramDate });
    doSetLastDate(paramDate, paramTimeZone);
  }
  
  public void doSetLastDate(Date paramDate, TimeZone paramTimeZone) {
    if (paramDate == null || paramTimeZone == null) {
      this.lastDate = null;
    } else {
      this.lastDate = SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate);
    } 
  }
  
  public ScheduleStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(ScheduleStatus paramScheduleStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramScheduleStatus });
    doSetStatus(paramScheduleStatus);
  }
  
  public void doSetStatus(ScheduleStatus paramScheduleStatus) {
    this.status = paramScheduleStatus;
  }
  
  public List<Long> getAuthorizedStoreIds() {
    return this.authorizedStoreIds;
  }
  
  public void setAuthorizedStoreIds(List<Long> paramList) throws BusinessException {
    executeRule("setAuthorizedStoreIds", new Object[] { paramList });
    doSetAuthorizedStoreIds(paramList);
  }
  
  public void doSetAuthorizedStoreIds(List<Long> paramList) {
    this.authorizedStoreIds = paramList;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\ProductGroupScheduleQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */