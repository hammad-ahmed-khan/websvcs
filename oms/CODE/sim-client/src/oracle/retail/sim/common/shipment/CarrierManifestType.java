package oracle.retail.sim.common.shipment;

import oracle.retail.sim.common.core.SimEnum;

public enum CarrierManifestType implements SimEnum<String> {
  PARCEL("P"),
  HOME_FLEET("H"),
  OTHER("O");
  
  private final String code;
  
  CarrierManifestType(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public static CarrierManifestType toValue(String paramString) {
    if (paramString != null)
      for (CarrierManifestType carrierManifestType : values()) {
        if (carrierManifestType.code.equals(paramString))
          return carrierManifestType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\CarrierManifestType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */