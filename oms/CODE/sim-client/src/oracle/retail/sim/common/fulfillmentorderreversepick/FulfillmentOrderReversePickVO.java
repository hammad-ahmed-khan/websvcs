package oracle.retail.sim.common.fulfillmentorderreversepick;

import java.io.Serializable;
import java.util.Date;

public class FulfillmentOrderReversePickVO implements Serializable {
  private static final long serialVersionUID = -691720652575788494L;
  
  private Long id;
  
  private Date createDate;
  
  private String createUser;
  
  private FulfillmentOrderReversePickStatus status;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public String getCreateUser() {
    return this.createUser;
  }
  
  public void doSetCreateUser(String paramString) {
    this.createUser = paramString;
  }
  
  public FulfillmentOrderReversePickStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FulfillmentOrderReversePickStatus paramFulfillmentOrderReversePickStatus) {
    this.status = paramFulfillmentOrderReversePickStatus;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderreversepick\FulfillmentOrderReversePickVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */