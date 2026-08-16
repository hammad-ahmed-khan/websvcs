package oracle.retail.sim.common.telephone;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Telephone implements Serializable {
  static final long serialVersionUID = -3033369075146404903L;
  
  private TelephoneType telephoneType = TelephoneType.VOICE;
  
  private String telephoneNumber = "";
  
  public Telephone(TelephoneType paramTelephoneType, String paramString) {
    if (paramTelephoneType != null)
      this.telephoneType = paramTelephoneType; 
    if (paramString != null)
      this.telephoneNumber = extractDigits(paramString); 
  }
  
  public Telephone(Telephone paramTelephone) {
    this.telephoneType = paramTelephone.getTelephoneType();
    this.telephoneNumber = paramTelephone.telephoneNumber;
  }
  
  public String getTelephoneNumber() {
    return this.telephoneNumber;
  }
  
  public TelephoneType getTelephoneType() {
    return this.telephoneType;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    Telephone telephone = (Telephone)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.telephoneType, telephone.telephoneType);
    equalsBuilder.append(this.telephoneNumber, telephone.telephoneNumber);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    StringBuilder stringBuilder = new StringBuilder(this.telephoneType.getCode());
    stringBuilder.append(this.telephoneNumber);
    return stringBuilder.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Telephone[");
    stringBuilder.append(this.telephoneType.getCode());
    stringBuilder.append(" ");
    stringBuilder.append(this.telephoneNumber);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  private String extractDigits(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = paramString.toCharArray();
    for (char c : arrayOfChar) {
      if (Character.isDigit(c))
        stringBuilder.append(c); 
    } 
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\telephone\Telephone.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */