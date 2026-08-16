package oracle.retail.sim.common.shipment;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ShipmentCarrierService implements Serializable {
  private static final long serialVersionUID = -1053066910471213886L;
  
  private Long id;
  
  private String code;
  
  private String description;
  
  private Long serviceTime;
  
  private boolean isDefault;
  
  private Long carrierId;
  
  private boolean isWeightRequired;
  
  private boolean isCartonTypeRequired;
  
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
  
  public Long getServiceTime() {
    return this.serviceTime;
  }
  
  public void doSetServiceTime(Long paramLong) {
    this.serviceTime = paramLong;
  }
  
  public boolean isDefault() {
    return this.isDefault;
  }
  
  public void doSetDefault(boolean paramBoolean) {
    this.isDefault = paramBoolean;
  }
  
  public Long getCarrierId() {
    return this.carrierId;
  }
  
  public void doSetCarrierId(Long paramLong) {
    this.carrierId = paramLong;
  }
  
  public boolean isWeightRequired() {
    return this.isWeightRequired;
  }
  
  public void doSetWeightRequired(boolean paramBoolean) {
    this.isWeightRequired = paramBoolean;
  }
  
  public boolean isCartonTypeRequired() {
    return this.isCartonTypeRequired;
  }
  
  public void doSetCartonTypeRequired(boolean paramBoolean) {
    this.isCartonTypeRequired = paramBoolean;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ShipmentCarrierService shipmentCarrierService = (ShipmentCarrierService)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.code, shipmentCarrierService.code);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    return (new HashCodeBuilder()).append(this.code).toHashCode();
  }
  
  public String toString() {
    return this.description;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\ShipmentCarrierService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */