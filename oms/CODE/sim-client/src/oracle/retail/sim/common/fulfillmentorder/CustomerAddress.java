package oracle.retail.sim.common.fulfillmentorder;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.person.PostalAddress;
import oracle.retail.sim.common.telephone.Telephone;

public class CustomerAddress extends BusinessObject {
  private static final long serialVersionUID = 2132548579338425205L;
  
  private Long id;
  
  private Long fulfillmentOrderId;
  
  private CustomerAddressType addressType;
  
  private String firstName;
  
  private String lastName;
  
  private String phoneticFirstName;
  
  private String phoneticLastName;
  
  private String preferredName;
  
  private String companyName;
  
  private PostalAddress postalAddress;
  
  private Telephone phone;
  
  private String email;
  
  public CustomerAddress(CustomerAddressType paramCustomerAddressType) {
    this.addressType = paramCustomerAddressType;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public void doSetFulfillmentOrderId(Long paramLong) {
    this.fulfillmentOrderId = paramLong;
  }
  
  public CustomerAddressType getAddressType() {
    return this.addressType;
  }
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public void setFirstName(String paramString) throws BusinessException {
    executeRule("setFirstName", new Object[] { paramString });
    doSetFirstName(paramString);
  }
  
  public void doSetFirstName(String paramString) {
    this.firstName = paramString;
  }
  
  public String getLastName() {
    return this.lastName;
  }
  
  public void setLastName(String paramString) throws BusinessException {
    executeRule("setLastName", new Object[] { paramString });
    doSetLastName(paramString);
  }
  
  public void doSetLastName(String paramString) {
    this.lastName = paramString;
  }
  
  public String getDisplayName() {
    return this.firstName + " " + this.lastName;
  }
  
  public String getPhoneticFirstName() {
    return this.phoneticFirstName;
  }
  
  public void setPhoneticFirstName(String paramString) throws BusinessException {
    executeRule("setPhoneticFirstName", new Object[] { paramString });
    doSetPhoneticFirstName(paramString);
  }
  
  public void doSetPhoneticFirstName(String paramString) {
    this.phoneticFirstName = paramString;
  }
  
  public String getPhoneticLastName() {
    return this.phoneticLastName;
  }
  
  public void setPhoneticLastName(String paramString) throws BusinessException {
    executeRule("setPhoneticLastName", new Object[] { paramString });
    doSetPhoneticLastName(paramString);
  }
  
  public void doSetPhoneticLastName(String paramString) {
    this.phoneticLastName = paramString;
  }
  
  public String getPhoneticDisplayName() {
    return this.phoneticFirstName + " " + this.phoneticLastName;
  }
  
  public String getPreferredName() {
    return this.preferredName;
  }
  
  public void setPreferredName(String paramString) throws BusinessException {
    executeRule("setPreferredName", new Object[] { paramString });
    doSetPreferredName(paramString);
  }
  
  public void doSetPreferredName(String paramString) {
    this.preferredName = paramString;
  }
  
  public String getCompanyName() {
    return this.companyName;
  }
  
  public void setCompanyName(String paramString) throws BusinessException {
    executeRule("setCompanyName", new Object[] { paramString });
    doSetCompanyName(paramString);
  }
  
  public void doSetCompanyName(String paramString) {
    this.companyName = paramString;
  }
  
  public PostalAddress getPostalAddress() {
    return this.postalAddress;
  }
  
  public void setPostalAddress(PostalAddress paramPostalAddress) throws BusinessException {
    checkForNullParameter("setAddress", paramPostalAddress);
    executeRule("setAddress", new Object[] { paramPostalAddress });
    doSetPostalAddress(paramPostalAddress);
  }
  
  public void doSetPostalAddress(PostalAddress paramPostalAddress) {
    this.postalAddress = paramPostalAddress;
  }
  
  public Telephone getPhone() {
    return this.phone;
  }
  
  public void setPhone(Telephone paramTelephone) throws BusinessException {
    checkForNullParameter("Phone", paramTelephone);
    executeRule("setPhone", new Object[] { paramTelephone });
    doSetPhone(paramTelephone);
  }
  
  public void doSetPhone(Telephone paramTelephone) {
    if (paramTelephone != null)
      this.phone = paramTelephone; 
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String paramString) throws BusinessException {
    checkForNullParameter("Email", paramString);
    executeRule("setEmail", new Object[] { paramString });
    doSetEmail(paramString);
  }
  
  public void doSetEmail(String paramString) {
    this.email = paramString;
  }
  
  public boolean isEmpty() {
    if (!StringHelper.isNullOrEmpty(this.firstName))
      return false; 
    if (!StringHelper.isNullOrEmpty(this.lastName))
      return false; 
    if (!StringHelper.isNullOrEmpty(this.phoneticFirstName))
      return false; 
    if (!StringHelper.isNullOrEmpty(this.phoneticLastName))
      return false; 
    if (!StringHelper.isNullOrEmpty(this.preferredName))
      return false; 
    if (!StringHelper.isNullOrEmpty(this.companyName))
      return false; 
    if (this.postalAddress != null) {
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getAddressLine1()))
        return false; 
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getAddressLine2()))
        return false; 
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getAddressLine3()))
        return false; 
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getCity()))
        return false; 
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getCounty()))
        return false; 
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getCountry()))
        return false; 
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getPostalCode()))
        return false; 
      if (!StringHelper.isNullOrEmpty(this.postalAddress.getState()))
        return false; 
    } 
    return !(this.phone != null && !StringHelper.isNullOrEmpty(this.phone.getTelephoneNumber()));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\CustomerAddress.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */