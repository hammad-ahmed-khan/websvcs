package oracle.retail.sim.common.config;

import java.io.Serializable;
import oracle.retail.sim.common.core.locale.StringHelper;

public abstract class NavigationNode implements Serializable {
  private static final long serialVersionUID = -7763183886646850240L;
  
  private String displayName = null;
  
  private String permissionName = null;
  
  private NavigationPermission permission = NavigationPermission.FULL;
  
  public NavigationNode(String paramString) {
    this.displayName = paramString;
  }
  
  public NavigationNode(String paramString1, String paramString2) {
    this.displayName = paramString1;
    this.permissionName = paramString2;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
  
  public void setDisplayName(String paramString) {
    if (!StringHelper.isNullOrEmpty(paramString))
      this.displayName = paramString; 
  }
  
  public String getPermissionName() {
    return this.permissionName;
  }
  
  public void setPermissionName(String paramString) {
    this.permissionName = paramString;
  }
  
  public NavigationPermission getNavigationPermission() {
    return this.permission;
  }
  
  public void setNavigationPermission(NavigationPermission paramNavigationPermission) {
    if (paramNavigationPermission == null)
      paramNavigationPermission = NavigationPermission.FULL; 
    this.permission = paramNavigationPermission;
  }
  
  public String toString() {
    return this.displayName;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\NavigationNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */