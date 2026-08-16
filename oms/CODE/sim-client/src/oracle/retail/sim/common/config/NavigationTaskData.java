package oracle.retail.sim.common.config;

import java.io.Serializable;

public class NavigationTaskData extends NavigationNode implements Serializable {
  private static final long serialVersionUID = -5024138705277754516L;
  
  private String defaultTaskItemTitle = null;
  
  private NavigationTaskItemData[] taskItemArray = new NavigationTaskItemData[0];
  
  public NavigationTaskData(String paramString1, String paramString2, String paramString3) {
    super(paramString1, paramString2);
    this.defaultTaskItemTitle = paramString3;
  }
  
  public String getDefaultTaskItemTitle() {
    return this.defaultTaskItemTitle;
  }
  
  public void setDefaultTaskItemTitle(String paramString) {
    this.defaultTaskItemTitle = paramString;
  }
  
  public NavigationTaskItemData[] getTaskItems() {
    return this.taskItemArray;
  }
  
  public void setTaskItems(NavigationTaskItemData[] paramArrayOfNavigationTaskItemData) {
    this.taskItemArray = paramArrayOfNavigationTaskItemData;
  }
  
  public NavigationTaskItemData getTaskItem(String paramString) {
    for (NavigationTaskItemData navigationTaskItemData : this.taskItemArray) {
      if (navigationTaskItemData.getDisplayName().equals(paramString))
        return navigationTaskItemData; 
    } 
    throw new IllegalArgumentException("Task Item title was not found!");
  }
  
  public void addTaskItem(NavigationTaskItemData paramNavigationTaskItemData) {
    for (NavigationTaskItemData navigationTaskItemData : this.taskItemArray) {
      if (navigationTaskItemData.getDisplayName().equals(paramNavigationTaskItemData.getDisplayName()))
        return; 
    } 
    this.taskItemArray = increaseTaskItemArray(this.taskItemArray);
    this.taskItemArray[this.taskItemArray.length - 1] = paramNavigationTaskItemData;
  }
  
  public void removeTaskItem(NavigationTaskItemData paramNavigationTaskItemData) {
    if (paramNavigationTaskItemData != null) {
      NavigationTaskItemData[] arrayOfNavigationTaskItemData = new NavigationTaskItemData[this.taskItemArray.length - 1];
      boolean bool = false;
      byte b1 = 0;
      for (byte b2 = 0; b2 < this.taskItemArray.length; b2++) {
        if (!this.taskItemArray[b2].getDisplayName().equals(paramNavigationTaskItemData.getDisplayName())) {
          arrayOfNavigationTaskItemData[b1++] = this.taskItemArray[b2];
        } else {
          bool = true;
        } 
      } 
      if (bool)
        this.taskItemArray = arrayOfNavigationTaskItemData; 
    } 
  }
  
  private NavigationTaskItemData[] increaseTaskItemArray(NavigationTaskItemData[] paramArrayOfNavigationTaskItemData) {
    NavigationTaskItemData[] arrayOfNavigationTaskItemData = new NavigationTaskItemData[paramArrayOfNavigationTaskItemData.length + 1];
    System.arraycopy(paramArrayOfNavigationTaskItemData, 0, arrayOfNavigationTaskItemData, 0, paramArrayOfNavigationTaskItemData.length);
    return arrayOfNavigationTaskItemData;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\NavigationTaskData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */