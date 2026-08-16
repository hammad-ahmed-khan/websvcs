package oracle.retail.sim.common.shipment;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BillOfLadingMotive implements Serializable {
  private static final long serialVersionUID = 7110333382230512681L;
  
  public static final String RETURN_MOTIVE_ID = "R";
  
  public static final String TRANSFER_MOTIVE_ID = "T";
  
  public static final String FO_DELIVERY_MOTIVE_ID = "F";
  
  private String id;
  
  private String description;
  
  public BillOfLadingMotive(String paramString1, String paramString2) {
    this.id = paramString1;
    this.description = paramString2;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public String toString() {
    return this.description;
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    BillOfLadingMotive billOfLadingMotive = (BillOfLadingMotive)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, billOfLadingMotive.id);
    return equalsBuilder.isEquals();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shipment\BillOfLadingMotive.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */