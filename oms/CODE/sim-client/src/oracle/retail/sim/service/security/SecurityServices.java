package oracle.retail.sim.service.security;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.security.Permission;
import oracle.retail.sim.common.security.PermissionGroup;
import oracle.retail.sim.common.security.PermissionGroupQueryFilter;
import oracle.retail.sim.common.security.PermissionQueryFilter;
import oracle.retail.sim.common.security.PermissionSet;
import oracle.retail.sim.common.security.Role;
import oracle.retail.sim.common.security.RoleQueryFilter;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserLoginVO;
import oracle.retail.sim.common.security.UserQueryFilter;
import oracle.retail.sim.common.security.UserRole;
import oracle.retail.sim.common.security.UserRolesSaveVO;
import oracle.retail.sim.common.security.UserSaveVO;
import oracle.retail.sim.common.security.UserStore;
import oracle.retail.sim.common.security.UserStoresSaveVO;
import oracle.retail.sim.common.store.Store;

public abstract class SecurityServices {
  public abstract String authenticateUser(String paramString, char[] paramArrayOfchar, boolean paramBoolean) throws Exception;
  
  public abstract List<Store> readUserAuthorizedStores(String paramString) throws Exception;
  
  public abstract PermissionSet readUserAuthorizedPermissions(String paramString, Long paramLong, DeviceType paramDeviceType) throws Exception;
  
  public abstract User readUser(String paramString) throws Exception;
  
  public abstract List<User> findUsers(UserQueryFilter paramUserQueryFilter) throws Exception;
  
  public abstract List<Role> findRoles(RoleQueryFilter paramRoleQueryFilter) throws Exception;
  
  public abstract List<RoleType> findRoleTypes() throws Exception;
  
  public abstract List<Permission> findPermissions(PermissionQueryFilter paramPermissionQueryFilter) throws Exception;
  
  public abstract List<PermissionGroup> findPermissionGroups(PermissionGroupQueryFilter paramPermissionGroupQueryFilter) throws Exception;
  
  public abstract PermissionSet readRolePermissions(String paramString) throws Exception;
  
  public abstract List<UserStore> readUserStores(String paramString) throws Exception;
  
  public abstract List<UserRole> readUserRoles(String paramString) throws Exception;
  
  public abstract void deleteUser(String paramString, Long paramLong) throws Exception;
  
  public abstract void deleteUsers(List<String> paramList, Long paramLong) throws Exception;
  
  public abstract Role saveRole(Role paramRole, PermissionSet paramPermissionSet, Long paramLong) throws Exception;
  
  public abstract void deleteRole(String paramString, Long paramLong) throws Exception;
  
  public abstract void deleteRoles(List<String> paramList, Long paramLong) throws Exception;
  
  public abstract void saveUserPassword(String paramString, char[] paramArrayOfchar1, char[] paramArrayOfchar2, Long paramLong) throws Exception;
  
  public abstract Date readUserPasswordDate(String paramString) throws Exception;
  
  public abstract Boolean isRoleAssigned(String paramString) throws Exception;
  
  public abstract void saveUser(UserSaveVO paramUserSaveVO, Long paramLong) throws Exception;
  
  public abstract void saveUserStores(UserStoresSaveVO paramUserStoresSaveVO, Long paramLong) throws Exception;
  
  public abstract void saveUserRoles(UserRolesSaveVO paramUserRolesSaveVO, Long paramLong) throws Exception;
  
  public abstract UserLoginVO loginUser(String paramString, char[] paramArrayOfchar, boolean paramBoolean, DeviceType paramDeviceType) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\security\SecurityServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */