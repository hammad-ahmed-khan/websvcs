package oracle.retail.sim.common.manifest;

import oracle.retail.sim.common.core.SimEnum;

public enum ManifestTranType implements SimEnum<String> {
  TRANSFER, RETURN, FULFILLMENT_ORDER_DELIVERY;
  
  public String getCode() {
    return name();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\manifest\ManifestTranType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */