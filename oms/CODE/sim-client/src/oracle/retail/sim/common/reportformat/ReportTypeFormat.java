package oracle.retail.sim.common.reportformat;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ReportTypeFormat extends BusinessObject {
  private static final long serialVersionUID = -2467147746962327953L;
  
  private String id;
  
  private String reportType;
  
  private String formatName;
  
  private String templateURL;
  
  private String itemTicketTypeId;
  
  private Long defaultPrinterId;
  
  private boolean isDefault;
  
  private Date createdDate;
  
  private Date updateDate;
  
  public String getId() {
    return this.id;
  }
  
  public void setReportType(String paramString) throws BusinessException {
    checkForNullParameter("setReportName", paramString);
    doSetReportType(paramString);
  }
  
  public String getReportType() {
    return this.reportType;
  }
  
  public String getFormatName() {
    return this.formatName;
  }
  
  public void setFormatName(String paramString) throws BusinessException {
    checkForNullParameter("Format ", paramString);
    doSetFormatName(paramString);
  }
  
  public void doSetId(String paramString) {
    this.id = paramString;
  }
  
  public void doSetReportType(String paramString) {
    this.reportType = paramString;
  }
  
  public void doSetFormatName(String paramString) {
    this.formatName = paramString;
  }
  
  public String getTemplateURL() {
    return this.templateURL;
  }
  
  public void setTemplateURL(String paramString) throws BusinessException {
    checkForNullParameter("TemplateURL", paramString);
    doSetTemplateURL(paramString);
  }
  
  public boolean isDefault() {
    return this.isDefault;
  }
  
  public void setIsDefault(boolean paramBoolean) {
    this.isDefault = paramBoolean;
  }
  
  public Long getDefaultPrinterId() {
    return this.defaultPrinterId;
  }
  
  public void setDefaultPrinterId(Long paramLong) {
    this.defaultPrinterId = paramLong;
  }
  
  public void doSetTemplateURL(String paramString) {
    this.templateURL = paramString;
  }
  
  public Date getCreatedDate() {
    return this.createdDate;
  }
  
  public void setCreatedDate(Date paramDate) {
    this.createdDate = paramDate;
  }
  
  public Date getUpdateDate() {
    return this.updateDate;
  }
  
  public void setUpdateDate(Date paramDate) {
    this.updateDate = paramDate;
  }
  
  public String getItemTicketTypeId() {
    return this.itemTicketTypeId;
  }
  
  public void doSetItemTicketTypeId(String paramString) {
    this.itemTicketTypeId = paramString;
  }
  
  public void setItemTicketTypeId(String paramString) {
    doSetItemTicketTypeId(paramString);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ReportTypeFormat reportTypeFormat = (ReportTypeFormat)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, reportTypeFormat.id);
    equalsBuilder.append(this.formatName, reportTypeFormat.formatName);
    equalsBuilder.append(this.reportType, reportTypeFormat.reportType);
    equalsBuilder.append(this.itemTicketTypeId, reportTypeFormat.itemTicketTypeId);
    equalsBuilder.append(this.isDefault, reportTypeFormat.isDefault);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    hashCodeBuilder.append(this.formatName);
    hashCodeBuilder.append(this.reportType);
    hashCodeBuilder.append(this.isDefault);
    hashCodeBuilder.append(this.itemTicketTypeId);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportformat\ReportTypeFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */