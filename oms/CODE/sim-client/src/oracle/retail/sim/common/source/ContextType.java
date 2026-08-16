package oracle.retail.sim.common.source;

import java.math.BigInteger;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ContextType extends BusinessObject implements Comparable<ContextType> {
  private static final long serialVersionUID = 2593790052205355098L;
  
  public static final String PROMOTION_ID = "PROM";
  
  public static final String REPAIR_ID = "REPAIR";
  
  private String id;
  
  private String name;
  
  public boolean isPromotion() {
    return "PROM".equals(this.id);
  }
  
  public boolean isRepair() {
    return "REPAIR".equals(this.id);
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    doSetId(paramString);
  }
  
  public void doSetId(String paramString) {
    this.id = paramString;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    doSetName(paramString);
  }
  
  public void doSetName(String paramString) {
    this.name = paramString;
  }
  
  public int compareTo(ContextType paramContextType) {
    int i;
    if (StringHelper.isNullOrEmpty(this.id)) {
      if (!StringHelper.isNullOrEmpty(paramContextType.id))
        return -1; 
    } else if (StringHelper.isNullOrEmpty(paramContextType.id)) {
      return 1;
    } 
    if (NumberHelper.isIdentifierNumeric(this.id) && NumberHelper.isIdentifierNumeric(paramContextType.id)) {
      i = (new BigInteger(this.id)).compareTo(new BigInteger(paramContextType.id));
    } else {
      i = StringHelper.getInstance().compareToIgnoreCase(this.id, paramContextType.id);
    } 
    if (i != 0)
      return i; 
    if (StringHelper.isNullOrEmpty(this.name)) {
      if (!StringHelper.isNullOrEmpty(paramContextType.name))
        return -1; 
    } else if (StringHelper.isNullOrEmpty(paramContextType.name)) {
      return 1;
    } 
    return StringHelper.getInstance().compareToIgnoreCase(this.name, paramContextType.name);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ContextType contextType = (ContextType)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, contextType.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
  
  public String toString() {
    return this.name;
  }
  
  public String toDetailedString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ContextType: Rin(").append(this.id);
    stringBuilder.append(") Name(").append(this.name);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\ContextType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */