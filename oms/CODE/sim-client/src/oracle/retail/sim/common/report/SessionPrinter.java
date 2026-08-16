package oracle.retail.sim.common.report;

import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.type.Displayable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SessionPrinter extends BusinessObject implements Displayable {
  private static final long serialVersionUID = -5248007508648866738L;
  
  private String formatType = null;
  
  private String formatName = null;
  
  private Long printerId;
  
  private String printerUri;
  
  private String description;
  
  private Long storeId;
  
  public String getFormatType() {
    return this.formatType;
  }
  
  public void setFormatType(String paramString) {
    this.formatType = paramString;
  }
  
  public String getPrinterUri() {
    return this.printerUri;
  }
  
  public void setPrinterUri(String paramString) {
    this.printerUri = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String paramString) {
    this.description = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String getFormatName() {
    return this.formatName;
  }
  
  public void setFormatName(String paramString) {
    this.formatName = paramString;
  }
  
  public Long getPrinterId() {
    return this.printerId;
  }
  
  public void setPrinterId(Long paramLong) {
    this.printerId = paramLong;
  }
  
  public String toDisplayString() {
    return this.description;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    SessionPrinter sessionPrinter = (SessionPrinter)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.storeId, sessionPrinter.getStoreId());
    equalsBuilder.append(this.formatType, sessionPrinter.getFormatType());
    equalsBuilder.append(this.printerId, sessionPrinter.getPrinterId());
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.storeId);
    hashCodeBuilder.append(this.formatType);
    hashCodeBuilder.append(this.printerId);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\report\SessionPrinter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */