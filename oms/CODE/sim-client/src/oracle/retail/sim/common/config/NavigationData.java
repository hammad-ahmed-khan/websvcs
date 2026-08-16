package oracle.retail.sim.common.config;

import java.io.Serializable;
import oracle.retail.sim.common.core.locale.StringHelper;

public class NavigationData implements Serializable {
  private static final long serialVersionUID = -3462010110070730560L;
  
  private NavigationTabData[] tabArray = new NavigationTabData[0];
  
  public NavigationTabData[] getTabs() {
    return this.tabArray;
  }
  
  public void setTabs(NavigationTabData[] paramArrayOfNavigationTabData) {
    this.tabArray = paramArrayOfNavigationTabData;
  }
  
  public void addTab(String paramString1, String paramString2) {
    addTab(new NavigationTabData(paramString1, paramString2));
  }
  
  public void addTab(NavigationTabData paramNavigationTabData) {
    if (StringHelper.isNullOrEmpty(paramNavigationTabData.getDisplayName()))
      return; 
    for (NavigationTabData navigationTabData : this.tabArray) {
      if (navigationTabData.getDisplayName().equals(paramNavigationTabData.getDisplayName()))
        return; 
    } 
    this.tabArray = increaseTabArray(this.tabArray);
    this.tabArray[this.tabArray.length - 1] = paramNavigationTabData;
  }
  
  public void removeTab(NavigationTabData paramNavigationTabData) {
    if (paramNavigationTabData != null) {
      NavigationTabData[] arrayOfNavigationTabData = new NavigationTabData[this.tabArray.length - 1];
      boolean bool = false;
      byte b = 0;
      for (NavigationTabData navigationTabData : this.tabArray) {
        if (navigationTabData != paramNavigationTabData) {
          arrayOfNavigationTabData[b++] = navigationTabData;
        } else {
          bool = true;
        } 
      } 
      if (bool)
        this.tabArray = arrayOfNavigationTabData; 
    } 
  }
  
  public void addTask(String paramString1, String paramString2, String paramString3, String paramString4) {
    getTabData(paramString1).addTask(paramString2, paramString3, paramString4);
  }
  
  private NavigationTabData getTabData(String paramString) {
    for (NavigationTabData navigationTabData : this.tabArray) {
      if (navigationTabData.getDisplayName().equals(paramString))
        return navigationTabData; 
    } 
    throw new IllegalArgumentException(paramString + " does not exist!");
  }
  
  public void addTaskItem(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
    addTaskItem(paramString1, paramString2, paramString3, paramString4, paramString5, false, null, null);
  }
  
  public void addTaskItem(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean, String paramString6, String paramString7) {
    NavigationTaskData navigationTaskData = getTabData(paramString1).getTask(paramString2);
    navigationTaskData.addTaskItem(new NavigationTaskItemData(paramString3, paramString4, paramString5, paramBoolean, paramString6, paramString7));
  }
  
  public NavigationTaskItemData findTaskItem(String paramString) {
    NavigationTaskData[] arrayOfNavigationTaskData = null;
    NavigationTaskItemData[] arrayOfNavigationTaskItemData = null;
    if (paramString != null)
      for (NavigationTabData navigationTabData : this.tabArray) {
        arrayOfNavigationTaskData = navigationTabData.getTasks();
        for (NavigationTaskData navigationTaskData : arrayOfNavigationTaskData) {
          arrayOfNavigationTaskItemData = navigationTaskData.getTaskItems();
          for (NavigationTaskItemData navigationTaskItemData : arrayOfNavigationTaskItemData) {
            if (paramString.equals(navigationTaskItemData.getActionCommand()))
              return navigationTaskItemData; 
          } 
        } 
      }  
    return null;
  }
  
  private NavigationTabData[] increaseTabArray(NavigationTabData[] paramArrayOfNavigationTabData) {
    NavigationTabData[] arrayOfNavigationTabData = new NavigationTabData[paramArrayOfNavigationTabData.length + 1];
    System.arraycopy(paramArrayOfNavigationTabData, 0, arrayOfNavigationTabData, 0, paramArrayOfNavigationTabData.length);
    return arrayOfNavigationTabData;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\NavigationData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */