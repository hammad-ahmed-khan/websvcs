package oracle.retail.sim.common.source;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.person.ContactInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Finisher extends BusinessObject implements Source {
  private static final long serialVersionUID = -9120229420577239024L;
  
  private String id;
  
  private String name;
  
  private String currencyCode = "";
  
  private String principalCountry;
  
  private String language;
  
  private String paymentTerms;
  
  private String vatRegion;
  
  private String transferEntityId;
  
  private String orgUnitId;
  
  private FinisherStatus status = FinisherStatus.ACTIVE;
  
  private ContactInfo contactInfo;
  
  public Finisher(String paramString1, String paramString2) {
    this.id = paramString1;
    this.name = paramString2;
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
  
  public String getCurrencyCode() {
    return this.currencyCode;
  }
  
  public void doSetCurrencyCode(String paramString) {
    this.currencyCode = paramString;
  }
  
  public FinisherStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FinisherStatus paramFinisherStatus) {
    if (paramFinisherStatus != null)
      this.status = paramFinisherStatus; 
  }
  
  public void setStatus(FinisherStatus paramFinisherStatus) throws BusinessException {
    checkForNullParameter("setStatus", paramFinisherStatus);
    executeRule("setStatus", new Object[] { paramFinisherStatus });
    doSetStatus(paramFinisherStatus);
  }
  
  public ContactInfo getDefaultContact() {
    return this.contactInfo;
  }
  
  public void doSetDefaultContact(ContactInfo paramContactInfo) {
    if (paramContactInfo != null)
      this.contactInfo = paramContactInfo; 
  }
  
  public String getLanguage() {
    return this.language;
  }
  
  public void doSetLanguage(String paramString) {
    this.language = paramString;
  }
  
  public String getPrincipalCountry() {
    return this.principalCountry;
  }
  
  public void doSetPrincipalCountry(String paramString) {
    this.principalCountry = paramString;
  }
  
  public String getVatRegion() {
    return this.vatRegion;
  }
  
  public void doSetVatRegion(String paramString) {
    this.vatRegion = paramString;
  }
  
  public String getPaymentTerms() {
    return this.paymentTerms;
  }
  
  public void doSetPaymentTerms(String paramString) {
    this.paymentTerms = paramString;
  }
  
  public String getOrgUnitId() {
    return this.orgUnitId;
  }
  
  public void doSetOrgUnitId(String paramString) {
    this.orgUnitId = paramString;
  }
  
  public String getTransferEntityId() {
    return this.transferEntityId;
  }
  
  public void doSetTransferEntityId(String paramString) {
    this.transferEntityId = paramString;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    Finisher finisher = (Finisher)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, finisher.id);
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


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\Finisher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */