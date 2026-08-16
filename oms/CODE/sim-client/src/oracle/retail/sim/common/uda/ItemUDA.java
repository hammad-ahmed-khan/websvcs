package oracle.retail.sim.common.uda;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ItemUDA extends BusinessObject {
  private static final long serialVersionUID = 9011687895827133868L;
  
  private Long id;
  
  private String itemId;
  
  private Long udaId;
  
  private Date udaDate;
  
  private String udaText;
  
  private String udaValue;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public Long getUdaId() {
    return this.udaId;
  }
  
  public void doSetUdaId(Long paramLong) {
    this.udaId = paramLong;
  }
  
  public Date getUdaDate() {
    return this.udaDate;
  }
  
  public void doSetUdaDate(Date paramDate) {
    this.udaDate = paramDate;
  }
  
  public String getUdaText() {
    return this.udaText;
  }
  
  public void doSetUdaText(String paramString) {
    this.udaText = paramString;
  }
  
  public String getUdaValue() {
    return this.udaValue;
  }
  
  public void doSetUdaValue(String paramString) {
    this.udaValue = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ItemUDA itemUDA = (ItemUDA)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, itemUDA.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\commo\\uda\ItemUDA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */