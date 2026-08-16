package oracle.retail.sim.common.source;

import java.math.BigInteger;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Warehouse extends BusinessObject implements Source, Comparable<Warehouse> {
  private static final long serialVersionUID = 531274687708574434L;
  
  private String id;
  
  private String name;
  
  private String currencyCode = "";
  
  public Warehouse(String paramString1, String paramString2) {
    this.id = paramString1;
    this.name = paramString2;
  }
  
  public SourceType getSourceType() {
    return SourceType.WAREHOUSE;
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
  
  public int compareTo(Warehouse paramWarehouse) {
    Warehouse warehouse = paramWarehouse;
    int i = 0;
    if (NumberHelper.isIdentifierNumeric(warehouse.getId()) && NumberHelper.isIdentifierNumeric(getId())) {
      BigInteger bigInteger1 = new BigInteger(getId());
      BigInteger bigInteger2 = new BigInteger(warehouse.getId());
      i = bigInteger1.compareTo(bigInteger2);
    } else {
      i = StringHelper.getInstance().compareToIgnoreCase(getId(), warehouse.getId());
    } 
    return (i == 0) ? ((getName() == null) ? -1 : ((warehouse.getName() == null) ? 1 : StringHelper.getInstance().compareToIgnoreCase(getName(), warehouse.getName()))) : i;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    Warehouse warehouse = (Warehouse)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, warehouse.id);
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


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\Warehouse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */