package oracle.retail.sim.common.shipment;

import oracle.retail.sim.common.core.SimEnum;

public enum ShipmentCarrierRole implements SimEnum<Integer> {
  SENDER(Integer.valueOf(1), "Sender"),
  RECEIVER(Integer.valueOf(2), "Receiver"),
  THIRD_PARTY(Integer.valueOf(3), "Third Party");
  
  private final int code;
  
  private final String description;
  
  ShipmentCarrierRole(Integer paramInteger, String paramString1) {
    this.code = paramInteger.intValue();
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ShipmentCarrierRole toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ShipmentCarrierRole shipmentCarrierRole : values()) {
        if (shipmentCarrierRole.code == paramInteger.intValue())
          return shipmentCarrierRole; 
      }  
    return null;
  }
  
  public static ShipmentCarrierRole toValue(String paramString) {
    if (paramString != null)
      for (ShipmentCarrierRole shipmentCarrierRole : values()) {
        if (shipmentCarrierRole.description.equals(paramString))
          return shipmentCarrierRole; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\ShipmentCarrierRole.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */