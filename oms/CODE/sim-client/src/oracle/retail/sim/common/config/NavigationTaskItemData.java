package oracle.retail.sim.common.config;

import java.io.Serializable;
import oracle.retail.sim.common.core.locale.StringHelper;

public class NavigationTaskItemData extends NavigationNode implements Serializable {
  private static final long serialVersionUID = -6431185244281822505L;
  
  private String actionCommand = null;
  
  private String nextTaskItem = null;
  
  private String previousTaskItem = null;
  
  private boolean allowsMultiple = false;
  
  public NavigationTaskItemData(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4, String paramString5) {
    super(paramString1, paramString2);
    this.actionCommand = paramString3;
    this.allowsMultiple = paramBoolean;
    this.nextTaskItem = paramString4;
    this.previousTaskItem = paramString5;
  }
  
  public String getActionCommand() {
    return this.actionCommand;
  }
  
  public void setActionCommand(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      throw new IllegalArgumentException("Action command cannot be null!"); 
    this.actionCommand = paramString;
  }
  
  public boolean allowsMultiple() {
    return this.allowsMultiple;
  }
  
  public void setAllowsMultiple(boolean paramBoolean) {
    this.allowsMultiple = paramBoolean;
  }
  
  public String getNextTaskItem() {
    return this.nextTaskItem;
  }
  
  public void setNextTaskItem(String paramString) {
    this.nextTaskItem = paramString;
  }
  
  public String getPreviousTaskItem() {
    return this.previousTaskItem;
  }
  
  public void setPreviousTaskItem(String paramString) {
    this.previousTaskItem = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\NavigationTaskItemData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */