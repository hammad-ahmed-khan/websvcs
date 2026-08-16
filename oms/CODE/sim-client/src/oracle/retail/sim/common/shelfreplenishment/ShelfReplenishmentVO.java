package oracle.retail.sim.common.shelfreplenishment;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.lineitem.UOMMode;

public class ShelfReplenishmentVO implements Serializable {
  private static final long serialVersionUID = 3365475303834150419L;
  
  private Long id = null;
  
  private String productGroupDescription = null;
  
  private String userId = null;
  
  private Date createDate = null;
  
  private ShelfReplenishmentType type = null;
  
  private ShelfReplenishmentStatus status = null;
  
  private Quantity quantityToReplenish = null;
  
  private UOMMode uomType = null;
  
  public ShelfReplenishmentVO(Long paramLong) {
    this.id = paramLong;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getProductGroupDescription() {
    return this.productGroupDescription;
  }
  
  public Quantity getQuantityToReplenish() {
    return this.quantityToReplenish;
  }
  
  public ShelfReplenishmentStatus getStatus() {
    return this.status;
  }
  
  public ShelfReplenishmentType getType() {
    return this.type;
  }
  
  public UOMMode getUnitOfMeasureMode() {
    return this.uomType;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public void doSetProductGroupDescription(String paramString) {
    this.productGroupDescription = paramString;
  }
  
  public void doSetQuantityToReplenish(Quantity paramQuantity) {
    this.quantityToReplenish = paramQuantity;
  }
  
  public void doSetStatus(ShelfReplenishmentStatus paramShelfReplenishmentStatus) {
    this.status = paramShelfReplenishmentStatus;
  }
  
  public void doSetType(ShelfReplenishmentType paramShelfReplenishmentType) {
    this.type = paramShelfReplenishmentType;
  }
  
  public void doSetUnitOfMeasureMode(UOMMode paramUOMMode) {
    this.uomType = paramUOMMode;
  }
  
  public void doSetUserId(String paramString) {
    this.userId = paramString;
  }
  
  public String getIdAsString() {
    return (this.id == null) ? null : this.id.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishmentVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */