package oracle.retail.sim.common.fulfillmentorderpick;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.business.Quantity;

public class FulfillmentOrderPickVO implements Serializable {
  private static final long serialVersionUID = -5344751423600716008L;
  
  private Long id;
  
  private Date createDate;
  
  private String createUser;
  
  private Date completeDate;
  
  private String completeUser;
  
  private FulfillmentOrderPickType type;
  
  private FulfillmentOrderPickStatus status;
  
  private Quantity totalQuantity;
  
  private Quantity actualQuantity;
  
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
  
  public Date getCompleteDate() {
    return this.completeDate;
  }
  
  public void doSetCompleteDate(Date paramDate) {
    this.completeDate = paramDate;
  }
  
  public String getCompleteUser() {
    return this.completeUser;
  }
  
  public void doSetCompleteUser(String paramString) {
    this.completeUser = paramString;
  }
  
  public FulfillmentOrderPickType getType() {
    return this.type;
  }
  
  public void doSetType(FulfillmentOrderPickType paramFulfillmentOrderPickType) {
    this.type = paramFulfillmentOrderPickType;
  }
  
  public FulfillmentOrderPickStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FulfillmentOrderPickStatus paramFulfillmentOrderPickStatus) {
    this.status = paramFulfillmentOrderPickStatus;
  }
  
  public Quantity getTotalQuantity() {
    return this.totalQuantity;
  }
  
  public void doSetTotalQuantity(Quantity paramQuantity) {
    this.totalQuantity = paramQuantity;
  }
  
  public Quantity getActualQuantity() {
    return this.actualQuantity;
  }
  
  public void doSetActualQuantity(Quantity paramQuantity) {
    this.actualQuantity = paramQuantity;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */