package oracle.retail.sim.common.productgroup;

import oracle.retail.sim.common.lineitem.UOMMode;

public class ShelfReplenishmentGroupVO {
  private Long id = null;
  
  private String description = "";
  
  private UOMMode unitOfMeasureMode = null;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public UOMMode getUnitOfMeasureMode() {
    return this.unitOfMeasureMode;
  }
  
  public void doSetUnitOfMeasureMode(UOMMode paramUOMMode) {
    this.unitOfMeasureMode = paramUOMMode;
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == this) ? true : ((paramObject == null || paramObject.getClass() != getClass()) ? false : this.id.equals(((ShelfReplenishmentGroupVO)paramObject).id));
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ShelfReplenishmentGroupVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */