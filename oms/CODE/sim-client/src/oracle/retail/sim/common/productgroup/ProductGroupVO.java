package oracle.retail.sim.common.productgroup;

import java.io.Serializable;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.store.Store;

public class ProductGroupVO implements Serializable {
  private static final long serialVersionUID = -1471291323555424791L;
  
  private Long id = null;
  
  private ProductGroupType type = null;
  
  private String description = "";
  
  private Store store = null;
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id != null) ? this.id.toString() : null;
  }
  
  public ProductGroupType getType() {
    return this.type;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public Store getStore() {
    return this.store;
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public void doSetType(ProductGroupType paramProductGroupType) {
    if (paramProductGroupType != null)
      this.type = paramProductGroupType; 
  }
  
  public void doSetDescription(String paramString) {
    if (!StringHelper.isNullOrEmpty(paramString))
      this.description = paramString; 
  }
  
  public void doSetStore(Store paramStore) {
    this.store = paramStore;
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == this) ? true : ((paramObject == null || paramObject.getClass() != getClass()) ? false : this.id.equals(((ProductGroupVO)paramObject).id));
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ProductGroupVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */