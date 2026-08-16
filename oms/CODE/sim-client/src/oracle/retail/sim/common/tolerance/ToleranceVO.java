package oracle.retail.sim.common.tolerance;

import java.io.Serializable;

public class ToleranceVO implements Serializable {
  private static final long serialVersionUID = 4483623774010941959L;
  
  private Long classId;
  
  private Long departmentId;
  
  private Long storeId;
  
  private ToleranceTopic topic;
  
  public Long getClassId() {
    return this.classId;
  }
  
  public void doSetClassId(Long paramLong) {
    this.classId = paramLong;
  }
  
  public Long getDepartmentId() {
    return this.departmentId;
  }
  
  public void doSetDepartmentId(Long paramLong) {
    this.departmentId = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public ToleranceTopic getTopic() {
    return this.topic;
  }
  
  public void doSetTopic(ToleranceTopic paramToleranceTopic) {
    this.topic = paramToleranceTopic;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tolerance\ToleranceVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */