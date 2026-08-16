package oracle.retail.sim.common.store;

import java.util.Locale;
import java.util.TimeZone;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.type.Displayable;
import oracle.retail.sim.common.currency.SimMoneyUtility;

public class Store extends BusinessObject implements Displayable {
  private static final long serialVersionUID = 6175704559679703797L;
  
  private Long id;
  
  private String name = "";
  
  private String language = "";
  
  private String country = "";
  
  private String currencyCode = SimMoneyUtility.getDefaultCurrencyCode();
  
  private String transferZone = "";
  
  private TimeZone timezone;
  
  private Boolean simFlag = Boolean.valueOf(false);
  
  public Store(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Locale getLocale() {
    return (this.language == null) ? null : ((this.country == null) ? new Locale(this.language) : new Locale(this.language, this.country));
  }
  
  public String getLanguage() {
    return this.language;
  }
  
  public String getCountry() {
    return this.country;
  }
  
  public Boolean getSimFlag() {
    return this.simFlag;
  }
  
  public String getCurrencyCode() {
    return this.currencyCode;
  }
  
  public String getTransferZone() {
    return this.transferZone;
  }
  
  public void setId(Long paramLong) throws BusinessException {
    checkForNullParameter("Id", paramLong);
    executeRule("setId", new Object[] { paramLong });
    doSetId(paramLong);
  }
  
  public void setName(String paramString) throws BusinessException {
    checkForNullParameter("Name", paramString);
    paramString = paramString.trim();
    executeRule("setName", new Object[] { paramString });
    doSetName(paramString);
  }
  
  public void setSimFlag(Boolean paramBoolean) throws BusinessException {
    checkForNullParameter("SIM Flag", paramBoolean);
    executeRule("setSimFlag", new Object[] { paramBoolean });
    doSetSimFlag(paramBoolean);
  }
  
  public void doSetId(Long paramLong) {
    if (paramLong != null)
      this.id = paramLong; 
  }
  
  public void doSetName(String paramString) {
    if (paramString != null)
      this.name = paramString; 
  }
  
  public void doSetLanguage(String paramString) {
    if (paramString != null)
      this.language = paramString; 
  }
  
  public void doSetCountry(String paramString) {
    if (paramString != null)
      this.country = paramString; 
  }
  
  public void doSetSimFlag(Boolean paramBoolean) {
    if (paramBoolean != null)
      this.simFlag = paramBoolean; 
  }
  
  public void doSetCurrencyCode(String paramString) {
    if (paramString != null)
      this.currencyCode = paramString; 
  }
  
  public void doSetTransferZone(String paramString) {
    this.transferZone = paramString;
  }
  
  public TimeZone getTimeZone() {
    return this.timezone;
  }
  
  public void doSetTimezone(TimeZone paramTimeZone) {
    this.timezone = paramTimeZone;
  }
  
  public void setTimezone(TimeZone paramTimeZone) throws BusinessException {
    checkForNullParameter("setTimezone", paramTimeZone);
    executeRule("setTimezone", new Object[] { paramTimeZone });
    doSetTimezone(paramTimeZone);
  }
  
  public String toDisplayString() {
    return this.id + " - " + this.name;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Store: [");
    stringBuilder.append("Id: ").append(getId());
    stringBuilder.append(";  Name: ").append(getName());
    stringBuilder.append(";  Language: ").append(getLanguage());
    stringBuilder.append(";  Country: ").append(getCountry());
    stringBuilder.append(";  Currency: ").append(getCurrencyCode());
    stringBuilder.append(";  Sim flag: ").append(getSimFlag());
    if (this.timezone != null)
      stringBuilder.append(";  Timezone: ").append(this.timezone.getDisplayName()); 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    Store store = (Store)paramObject;
    return this.id.equals(store.id);
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\store\Store.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */