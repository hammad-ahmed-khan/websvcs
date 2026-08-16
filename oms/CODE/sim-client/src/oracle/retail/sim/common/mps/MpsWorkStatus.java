package oracle.retail.sim.common.mps;

import oracle.retail.sim.common.core.SimEnum;

public enum MpsWorkStatus implements SimEnum<String> {
  COMPLETE, ERROR, IN_PROGRESS;
  
  public String getCode() {
    return name();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mps\MpsWorkStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */