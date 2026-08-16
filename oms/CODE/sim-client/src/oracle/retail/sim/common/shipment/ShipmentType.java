package oracle.retail.sim.common.shipment;

import oracle.retail.sim.common.core.SimEnum;

public enum ShipmentType implements SimEnum<String> {
  FULFILLMENT_ORDER_DELIVERY("BOLF"),
  TRANSFER("BOLT"),
  RETURN("BOLR");
  
  private final String code;
  
  ShipmentType(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
  
  public static ShipmentType toValue(String paramString) {
    if (paramString != null)
      for (ShipmentType shipmentType : values()) {
        if (shipmentType.code.equals(paramString))
          return shipmentType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\ShipmentType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */