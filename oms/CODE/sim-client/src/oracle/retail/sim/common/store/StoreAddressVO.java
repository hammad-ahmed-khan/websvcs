package oracle.retail.sim.common.store;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class StoreAddressVO implements Serializable {
  private static final long serialVersionUID = -1088894757552346885L;
  
  private Long storeId = null;
  
  private String addressLine1 = "";
  
  private String addressLine2 = "";
  
  private String city = "";
  
  private String state = "";
  
  private String zipCode = "";
  
  private String country = "";
  
  public StoreAddressVO(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public String getAddressLine1() {
    return this.addressLine1;
  }
  
  public String getAddressLine2() {
    return this.addressLine2;
  }
  
  public String getCity() {
    return this.city;
  }
  
  public String getState() {
    return this.state;
  }
  
  public String getZipCode() {
    return this.zipCode;
  }
  
  public String getCountry() {
    return this.country;
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.storeId = paramLong; 
  }
  
  public void doSetAddressLine1(String paramString) {
    if (paramString != null)
      this.addressLine1 = paramString; 
  }
  
  public void doSetAddressLine2(String paramString) {
    if (paramString != null)
      this.addressLine2 = paramString; 
  }
  
  public void doSetCity(String paramString) {
    if (paramString != null)
      this.city = paramString; 
  }
  
  public void doSetState(String paramString) {
    if (paramString != null)
      this.state = paramString; 
  }
  
  public void doSetZipCode(String paramString) {
    if (paramString != null)
      this.zipCode = paramString; 
  }
  
  public void doSetCountry(String paramString) {
    if (paramString != null)
      this.country = paramString; 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(15);
    stringBuilder.append("Store id: ").append(this.storeId).append("[");
    stringBuilder.append(this.addressLine1).append(",");
    stringBuilder.append(this.city).append(",");
    stringBuilder.append(this.state).append(",");
    stringBuilder.append(this.zipCode).append(" ");
    stringBuilder.append(this.country).append("]");
    return stringBuilder.toString();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.storeId);
    return hashCodeBuilder.hashCode();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    StoreAddressVO storeAddressVO = (StoreAddressVO)paramObject;
    return this.storeId.equals(storeAddressVO.storeId);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\store\StoreAddressVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */