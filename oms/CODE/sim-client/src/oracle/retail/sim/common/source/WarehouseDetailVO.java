package oracle.retail.sim.common.source;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WarehouseDetailVO implements Serializable, SourceVO {
  private static final long serialVersionUID = -1603640170601798534L;
  
  private String id;
  
  private String name;
  
  private String organizationUnitId;
  
  private String localeLanguage;
  
  private String localeCountry;
  
  private Date openDate;
  
  private Date closeDate;
  
  private String currencyCode;
  
  private String taxId;
  
  public WarehouseDetailVO(String paramString1, String paramString2) {
    this.id = paramString1;
    this.name = paramString2;
  }
  
  public SourceType getSourceType() {
    return SourceType.WAREHOUSE;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public String getOrganizationUnitId() {
    return this.organizationUnitId;
  }
  
  public void setOrganizationUnitId(String paramString) {
    this.organizationUnitId = paramString;
  }
  
  public String getLocaleLanguage() {
    return this.localeLanguage;
  }
  
  public void setLocaleLanguage(String paramString) {
    this.localeLanguage = paramString;
  }
  
  public String getLocaleCountry() {
    return this.localeCountry;
  }
  
  public void setLocaleCountry(String paramString) {
    this.localeCountry = paramString;
  }
  
  public Date getOpenDate() {
    return this.openDate;
  }
  
  public void setOpenDate(Date paramDate) {
    this.openDate = paramDate;
  }
  
  public Date getCloseDate() {
    return this.closeDate;
  }
  
  public void setCloseDate(Date paramDate) {
    this.closeDate = paramDate;
  }
  
  public String getCurrencyCode() {
    return this.currencyCode;
  }
  
  public void setCurrencyCode(String paramString) {
    this.currencyCode = paramString;
  }
  
  public String getTaxId() {
    return this.taxId;
  }
  
  public void setTaxId(String paramString) {
    this.taxId = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    WarehouseDetailVO warehouseDetailVO = (WarehouseDetailVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, warehouseDetailVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Warehouse: ");
    stringBuilder.append("id=").append(this.id);
    stringBuilder.append(", name=").append(this.name);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\WarehouseDetailVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */