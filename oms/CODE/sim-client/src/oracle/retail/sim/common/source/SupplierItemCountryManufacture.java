package oracle.retail.sim.common.source;

import oracle.retail.sim.common.business.BusinessObject;

public class SupplierItemCountryManufacture extends BusinessObject {
  private static final long serialVersionUID = 644713651735858534L;
  
  private String itemId;
  
  private String supplierId;
  
  private String countryId;
  
  private boolean primary;
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void setItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public String getSupplierId() {
    return this.supplierId;
  }
  
  public void setSupplierId(String paramString) {
    this.supplierId = paramString;
  }
  
  public String getCountryId() {
    return this.countryId;
  }
  
  public void setCountryId(String paramString) {
    this.countryId = paramString;
  }
  
  public boolean isPrimary() {
    return this.primary;
  }
  
  public void setPrimary(boolean paramBoolean) {
    this.primary = paramBoolean;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\SupplierItemCountryManufacture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */