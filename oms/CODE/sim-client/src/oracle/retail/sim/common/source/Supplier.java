package oracle.retail.sim.common.source;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Supplier extends BusinessObject implements Source {
  private static final long serialVersionUID = -8877173202089221202L;
  
  private static final String CASE = "CA";
  
  private String id;
  
  private String name;
  
  private String currencyCode = "";
  
  private SupplierStatus status = SupplierStatus.ACTIVE;
  
  private boolean returnsAllowed = true;
  
  private boolean authorizationRequired = false;
  
  private boolean purchaseOrderCreationAllowed = true;
  
  private Boolean vendorCheckInd;
  
  private Double vendorCheckPct;
  
  private String supplierQuantityLevel;
  
  private String taxId;
  
  private SupplierDeliveryDiscrepancyType deliveryDiscrepancy = SupplierDeliveryDiscrepancyType.ALLOW;
  
  public Supplier(String paramString1, String paramString2) {
    this.id = paramString1;
    this.name = paramString2;
  }
  
  public SourceType getSourceType() {
    return SourceType.SUPPLIER;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getCurrencyCode() {
    return this.currencyCode;
  }
  
  public void doSetCurrencyCode(String paramString) {
    this.currencyCode = paramString;
  }
  
  public SupplierStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(SupplierStatus paramSupplierStatus) {
    if (paramSupplierStatus != null)
      this.status = paramSupplierStatus; 
  }
  
  public void setStatus(SupplierStatus paramSupplierStatus) throws BusinessException {
    checkForNullParameter("Status", paramSupplierStatus);
    executeRule("setStatus", new Object[] { paramSupplierStatus });
    doSetStatus(paramSupplierStatus);
  }
  
  public SupplierDeliveryDiscrepancyType getDeliveryDiscrepancy() {
    return this.deliveryDiscrepancy;
  }
  
  public void doSetDeliveryDiscrepancy(SupplierDeliveryDiscrepancyType paramSupplierDeliveryDiscrepancyType) {
    if (paramSupplierDeliveryDiscrepancyType != null)
      this.deliveryDiscrepancy = paramSupplierDeliveryDiscrepancyType; 
  }
  
  public void setDeliveryDiscrepancy(SupplierDeliveryDiscrepancyType paramSupplierDeliveryDiscrepancyType) throws BusinessException {
    executeRule("setDeliveryDiscrepancy", new Object[] { paramSupplierDeliveryDiscrepancyType });
    doSetDeliveryDiscrepancy(paramSupplierDeliveryDiscrepancyType);
  }
  
  public boolean isInactive() {
    return (this.status != SupplierStatus.ACTIVE);
  }
  
  public boolean isReturnsAllowed() {
    return this.returnsAllowed;
  }
  
  public void doSetReturnsAllowed(boolean paramBoolean) {
    this.returnsAllowed = paramBoolean;
  }
  
  public boolean isAuthorizationRequired() {
    return this.authorizationRequired;
  }
  
  public void doSetAuthorizationRequired(boolean paramBoolean) {
    this.authorizationRequired = paramBoolean;
  }
  
  public boolean isPurchaseOrderCreationAllowed() {
    return this.purchaseOrderCreationAllowed;
  }
  
  public void doSetPurchaseOrderCreationAllowed(boolean paramBoolean) {
    this.purchaseOrderCreationAllowed = paramBoolean;
  }
  
  public Boolean getVendorCheckInd() {
    return this.vendorCheckInd;
  }
  
  public void doSetVendorCheckInd(Boolean paramBoolean) {
    this.vendorCheckInd = paramBoolean;
  }
  
  public void setVendorCheckInd(Boolean paramBoolean) throws BusinessException {
    checkForNullParameter("Vendor Check Indicator", paramBoolean);
    executeRule("setVendorCheckInd", new Object[] { paramBoolean });
    doSetVendorCheckInd(paramBoolean);
  }
  
  public Double getVendorCheckPct() {
    return this.vendorCheckPct;
  }
  
  public void doSetVendorCheckPct(Double paramDouble) {
    this.vendorCheckPct = paramDouble;
  }
  
  public void setVendorCheckPct(Double paramDouble) throws BusinessException {
    checkForNullParameter("Vendor Check Pct", paramDouble);
    executeRule("setVendorCheckPct", new Object[] { paramDouble });
    doSetVendorCheckPct(paramDouble);
  }
  
  public String getSupplierQuantityLevel() {
    return this.supplierQuantityLevel;
  }
  
  public void doSetSupplierQuantityLevel(String paramString) {
    this.supplierQuantityLevel = paramString;
  }
  
  public boolean isSupplierQuantityLevelCase() {
    return "CA".equals(this.supplierQuantityLevel);
  }
  
  public String getTaxId() {
    return this.taxId;
  }
  
  public void doSetTaxId(String paramString) {
    this.taxId = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    Supplier supplier = (Supplier)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, supplier.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Supplier: ");
    stringBuilder.append("id=").append(this.id);
    stringBuilder.append(", name=").append(this.name);
    stringBuilder.append(", status=").append(this.status);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\Supplier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */