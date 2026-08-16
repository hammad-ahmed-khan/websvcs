package oracle.retail.sim.common.shipment;

import oracle.retail.sim.common.business.MessageText;

public enum ShipmentMessageText implements MessageText {
  ADDRESS_DOES_NOT_EXIST("To / From Address does not exist. Contact System Administrator."),
  CARTON_TYPE_REQUIRED("Package Type is required for this carrier service."),
  WEIGHT_REQUIRED("Weight is required for this carrier service");
  
  private final String message;
  
  ShipmentMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\ShipmentMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */