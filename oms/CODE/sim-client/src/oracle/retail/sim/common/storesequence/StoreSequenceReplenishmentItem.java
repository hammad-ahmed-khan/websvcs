package oracle.retail.sim.common.storesequence;

import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.lineitem.UOMMode;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class StoreSequenceReplenishmentItem {
  private Long id;
  
  private Long storeId;
  
  private String itemId;
  
  private Quantity capacity = Quantity.ZERO;
  
  private UOMMode uomMode;
  
  private StoreSequenceAreaType area;
  
  private int itemOrder;
  
  private int storeOrder;
  
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
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public int getItemOrder() {
    return this.itemOrder;
  }
  
  public UOMMode getUomMode() {
    return this.uomMode;
  }
  
  public void setUomMode(UOMMode paramUOMMode) {
    this.uomMode = paramUOMMode;
  }
  
  public void doSetItemOrder(int paramInt) {
    this.itemOrder = paramInt;
  }
  
  public Quantity getCapacity() {
    return this.capacity;
  }
  
  public void doSetCapacity(Quantity paramQuantity) {
    this.capacity = paramQuantity;
  }
  
  public StoreSequenceAreaType getArea() {
    return this.area;
  }
  
  public void doSetArea(StoreSequenceAreaType paramStoreSequenceAreaType) {
    this.area = paramStoreSequenceAreaType;
  }
  
  public int getStoreOrder() {
    return this.storeOrder;
  }
  
  public void doSetStoreOrder(int paramInt) {
    this.storeOrder = paramInt;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    StoreSequenceReplenishmentItem storeSequenceReplenishmentItem = (StoreSequenceReplenishmentItem)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, storeSequenceReplenishmentItem.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceReplenishmentItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */