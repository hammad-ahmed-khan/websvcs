package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.SimSession;
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

@Remote
public interface SecurityInterface {
  CompressedObject<String> authenticateUser(CompressedObject<String> paramCompressedObject, CompressedObject<char[]> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> deleteRole(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> deleteRoles(CompressedObject<List<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> deleteUser(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> deleteUsers(CompressedObject<List<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<PermissionGroup>> findPermissionGroups(CompressedObject<PermissionGroupQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<Permission>> findPermissions(CompressedObject<PermissionQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<RoleType>> findRoleTypes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<Role>> findRoles(CompressedObject<RoleQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<User>> findUsers(CompressedObject<UserQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> isRoleAssigned(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<UserLoginVO> loginUser(CompressedObject<String> paramCompressedObject, CompressedObject<char[]> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<DeviceType> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<PermissionSet> readRolePermissions(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<User> readUser(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<PermissionSet> readUserAuthorizedPermissions(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<DeviceType> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<Store>> readUserAuthorizedStores(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Date> readUserPasswordDate(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<UserRole>> readUserRoles(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<UserStore>> readUserStores(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Role> saveRole(CompressedObject<Role> paramCompressedObject, CompressedObject<PermissionSet> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> saveUser(CompressedObject<UserSaveVO> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> saveUserPassword(CompressedObject<String> paramCompressedObject, CompressedObject<char[]> paramCompressedObject1, CompressedObject<char[]> paramCompressedObject2, CompressedObject<Long> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<?> saveUserRoles(CompressedObject<UserRolesSaveVO> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> saveUserStores(CompressedObject<UserStoresSaveVO> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\SecurityInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */