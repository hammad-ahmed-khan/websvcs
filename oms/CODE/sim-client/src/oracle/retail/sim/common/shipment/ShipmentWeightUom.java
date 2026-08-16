package oracle.retail.sim.common.shipment;

import java.io.Serializable;

public class ShipmentWeightUom implements Serializable {
  private static final long serialVersionUID = -8251806305358716050L;
  
  private String uom;
  
  private String description;
  
  public String getUom() {
    return this.uom;
  }
  
  public void doSetUom(String paramString) {
    this.uom = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public String toString() {
    return this.uom;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\ShipmentWeightUom.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */