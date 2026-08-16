package oracle.retail.sim.common.uom;

import oracle.retail.sim.common.core.SimEnum;

public enum UOMInventoryType implements SimEnum<String> {
  SELLING, STANDARD;
  
  public String getCode() {
    return name();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\commo\\uom\UOMInventoryType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */