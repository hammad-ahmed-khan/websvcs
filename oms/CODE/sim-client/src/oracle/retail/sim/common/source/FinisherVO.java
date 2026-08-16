package oracle.retail.sim.common.source;

import java.io.Serializable;
import java.math.BigInteger;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FinisherVO implements SourceVO, Serializable, Comparable<FinisherVO> {
  private static final long serialVersionUID = 4065675502647091945L;
  
  private String id;
  
  private String name;
  
  private FinisherStatus status;
  
  public FinisherVO(String paramString1, String paramString2, FinisherStatus paramFinisherStatus) {
    this.id = paramString1;
    this.name = paramString2;
    this.status = paramFinisherStatus;
  }
  
  public SourceType getSourceType() {
    return SourceType.FINISHER;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public FinisherStatus getStatus() {
    return this.status;
  }
  
  public int compareTo(FinisherVO paramFinisherVO) {
    int i;
    if (StringHelper.isNullOrEmpty(this.id)) {
      if (!StringHelper.isNullOrEmpty(paramFinisherVO.id))
        return -1; 
    } else if (StringHelper.isNullOrEmpty(paramFinisherVO.id)) {
      return 1;
    } 
    if (NumberHelper.isIdentifierNumeric(this.id) && NumberHelper.isIdentifierNumeric(paramFinisherVO.id)) {
      i = (new BigInteger(this.id)).compareTo(new BigInteger(paramFinisherVO.id));
    } else {
      i = StringHelper.getInstance().compareToIgnoreCase(this.id, paramFinisherVO.id);
    } 
    if (i != 0)
      return i; 
    if (StringHelper.isNullOrEmpty(this.name)) {
      if (!StringHelper.isNullOrEmpty(paramFinisherVO.name))
        return -1; 
    } else if (StringHelper.isNullOrEmpty(paramFinisherVO.name)) {
      return 1;
    } 
    return StringHelper.getInstance().compareToIgnoreCase(this.name, paramFinisherVO.name);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    FinisherVO finisherVO = (FinisherVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, finisherVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Finisher: ");
    stringBuilder.append("id=").append(this.id);
    stringBuilder.append(", name=").append(this.name);
    stringBuilder.append(", status=").append(this.status);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\FinisherVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */