package oracle.retail.sim.common.fulfillmentorderpick;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FulfillmentOrderPossiblePickVO implements Serializable {
  private static final long serialVersionUID = 235900353826391014L;
  
  private Long id;
  
  private Date releaseDate;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Date getReleaseDate() {
    return this.releaseDate;
  }
  
  public void doSetReleaseDate(Date paramDate) {
    this.releaseDate = paramDate;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FulfillmentOrderPossiblePickVO: Id(");
    stringBuilder.append(getId());
    stringBuilder.append(") ReleaseDate(");
    stringBuilder.append(getReleaseDate());
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    FulfillmentOrderPossiblePickVO fulfillmentOrderPossiblePickVO = (FulfillmentOrderPossiblePickVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, fulfillmentOrderPossiblePickVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPossiblePickVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */