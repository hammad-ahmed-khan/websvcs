package oracle.retail.sim.common.fulfillmentorderpick;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;

public class FulfillmentOrderBin extends BusinessObject {
  private static final long serialVersionUID = -1922351267929270673L;
  
  private Long id;
  
  private Long fulfillmentOrderId;
  
  private Long fulfillmentOrderPickId;
  
  private String binId;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public void doSetFulfillmentOrderId(Long paramLong) {
    this.fulfillmentOrderId = paramLong;
  }
  
  public Long getFulfillmentOrderPickId() {
    return this.fulfillmentOrderPickId;
  }
  
  public void doSetFulfillmentOrderPickId(Long paramLong) {
    this.fulfillmentOrderPickId = paramLong;
  }
  
  public String getBinId() {
    return this.binId;
  }
  
  public void setBinId(String paramString) throws BusinessException {
    executeRule("setBinId", new Object[0]);
    doSetBinId(paramString);
  }
  
  public void doSetBinId(String paramString) {
    this.binId = paramString;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FulfillmentOrderBin: Id(");
    stringBuilder.append(getId());
    stringBuilder.append(") FulfillmentOrderId(");
    stringBuilder.append(getFulfillmentOrderId());
    stringBuilder.append(") FulfillmentOrderPickId(");
    stringBuilder.append(getFulfillmentOrderPickId());
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderBin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */