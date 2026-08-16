package oracle.retail.sim.common.config;

import java.io.Serializable;
import oracle.retail.sim.common.core.locale.StringHelper;

public class NavigationTabData extends NavigationNode implements Serializable {
  private static final long serialVersionUID = -8010642135379599003L;
  
  private NavigationTaskData[] taskArray = new NavigationTaskData[0];
  
  public NavigationTabData(String paramString) {
    super(paramString);
  }
  
  public NavigationTabData(String paramString1, String paramString2) {
    super(paramString1, paramString2);
  }
  
  public NavigationTaskData[] getTasks() {
    return this.taskArray;
  }
  
  public void setTasks(NavigationTaskData[] paramArrayOfNavigationTaskData) {
    this.taskArray = paramArrayOfNavigationTaskData;
  }
  
  public NavigationTaskData getTask(String paramString) {
    for (NavigationTaskData navigationTaskData : this.taskArray) {
      if (navigationTaskData.getDisplayName().equals(paramString))
        return navigationTaskData; 
    } 
    throw new IllegalArgumentException(paramString + " does not exist!");
  }
  
  public void addTask(String paramString1, String paramString2) {
    addTask(paramString1, paramString2, "");
  }
  
  public void addTask(String paramString1, String paramString2, String paramString3) {
    if (StringHelper.isNullOrEmpty(paramString1))
      return; 
    for (NavigationTaskData navigationTaskData : this.taskArray) {
      if (navigationTaskData.getDisplayName().equals(paramString1))
        return; 
    } 
    this.taskArray = increaseTaskArray(this.taskArray);
    this.taskArray[this.taskArray.length - 1] = new NavigationTaskData(paramString1, paramString2, paramString3);
  }
  
  public void removeTask(NavigationTaskData paramNavigationTaskData) {
    if (paramNavigationTaskData != null) {
      NavigationTaskData[] arrayOfNavigationTaskData = new NavigationTaskData[this.taskArray.length - 1];
      boolean bool = false;
      byte b1 = 0;
      for (byte b2 = 0; b2 < this.taskArray.length; b2++) {
        if (!this.taskArray[b2].getDisplayName().equals(paramNavigationTaskData.getDisplayName())) {
          arrayOfNavigationTaskData[b1++] = this.taskArray[b2];
        } else {
          bool = true;
        } 
      } 
      if (bool)
        this.taskArray = arrayOfNavigationTaskData; 
    } 
  }
  
  private NavigationTaskData[] increaseTaskArray(NavigationTaskData[] paramArrayOfNavigationTaskData) {
    NavigationTaskData[] arrayOfNavigationTaskData = new NavigationTaskData[paramArrayOfNavigationTaskData.length + 1];
    System.arraycopy(paramArrayOfNavigationTaskData, 0, arrayOfNavigationTaskData, 0, paramArrayOfNavigationTaskData.length);
    return arrayOfNavigationTaskData;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\NavigationTabData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */