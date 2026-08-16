package oracle.retail.sim.common.theme;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;

public class CustomTheme extends BusinessObject {
  private static final long serialVersionUID = -6796355702744821482L;
  
  private Long id = null;
  
  private String name = null;
  
  private String description = null;
  
  private String lookAndFeelClassName = null;
  
  private boolean isActive = true;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) throws BusinessException {
    checkForNullParameter("Name", paramString);
    doSetName(paramString);
  }
  
  public void doSetName(String paramString) {
    this.name = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String paramString) throws BusinessException {
    checkForNullParameter("Description", paramString);
    doSetDescription(paramString);
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public String getLookAndFeel() {
    return this.lookAndFeelClassName;
  }
  
  public void setLookAndFeel(String paramString) throws BusinessException {
    checkForNullParameter("Look And Feel", paramString);
    doSetLookAndFeel(paramString);
  }
  
  public void doSetLookAndFeel(String paramString) {
    this.lookAndFeelClassName = paramString;
  }
  
  public boolean isActive() {
    return this.isActive;
  }
  
  public void setActive(boolean paramBoolean) {
    doSetActive(paramBoolean);
  }
  
  public void doSetActive(boolean paramBoolean) {
    this.isActive = paramBoolean;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    CustomTheme customTheme = (CustomTheme)paramObject;
    return this.id.equals(customTheme.id);
  }
  
  public int hashCode() {
    return this.id.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\theme\CustomTheme.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */