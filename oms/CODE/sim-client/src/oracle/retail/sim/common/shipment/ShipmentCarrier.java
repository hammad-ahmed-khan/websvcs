package oracle.retail.sim.common.shipment;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ShipmentCarrier implements Serializable {
  private static final long serialVersionUID = 2848867226777513041L;
  
  private Long id;
  
  private String code;
  
  private String description;
  
  private CarrierManifestType manifestType;
  
  public static final String OTHER_CARRIER_CODE = "O";
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public void doSetCode(String paramString) {
    this.code = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public CarrierManifestType getManifestType() {
    return this.manifestType;
  }
  
  public void doSetManifestType(CarrierManifestType paramCarrierManifestType) {
    this.manifestType = paramCarrierManifestType;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ShipmentCarrier shipmentCarrier = (ShipmentCarrier)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.code, shipmentCarrier.code);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    return (new HashCodeBuilder()).append(this.code).toHashCode();
  }
  
  public String toString() {
    return this.description;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\ShipmentCarrier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */