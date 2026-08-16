package oracle.retail.sim.common.shipment;

import java.io.Serializable;
import oracle.retail.sim.common.business.Quantity;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ShipmentCartonType implements Serializable {
  private static final long serialVersionUID = 6806730231976774013L;
  
  private Long id;
  
  private Long storeId;
  
  private String description;
  
  private Quantity height;
  
  private Quantity width;
  
  private Quantity length;
  
  private String unitOfMeasure;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public Quantity getHeight() {
    return this.height;
  }
  
  public void doSetHeight(Quantity paramQuantity) {
    this.height = paramQuantity;
  }
  
  public Quantity getWidth() {
    return this.width;
  }
  
  public void doSetWidth(Quantity paramQuantity) {
    this.width = paramQuantity;
  }
  
  public Quantity getLength() {
    return this.length;
  }
  
  public void doSetLength(Quantity paramQuantity) {
    this.length = paramQuantity;
  }
  
  public String getUnitOfMeasure() {
    return this.unitOfMeasure;
  }
  
  public void doSetUnitOfMeasure(String paramString) {
    this.unitOfMeasure = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ShipmentCartonType shipmentCartonType = (ShipmentCartonType)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, shipmentCartonType.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    return (new HashCodeBuilder()).append(this.id).toHashCode();
  }
  
  public String toString() {
    return this.description;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\ShipmentCartonType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */