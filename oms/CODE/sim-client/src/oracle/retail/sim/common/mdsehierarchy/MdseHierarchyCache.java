package oracle.retail.sim.common.mdsehierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.service.core.ClientServiceFactory;

public class MdseHierarchyCache {
  private static List<MdseHierarchyNode> departments = new ArrayList<>();
  
  private static Map<Long, List> classMap = new HashMap<>();
  
  private static Map<String, List> subclassMap = new HashMap<>();
  
  public static List<MdseHierarchyNode> getAllDepartments() throws Exception {
    if (departments.isEmpty())
      departments = ClientServiceFactory.getMdseHierarchyServices().findDepartments(); 
    return departments;
  }
  
  public static List<MdseHierarchyNode> getClasses(MdseHierarchyNode paramMdseHierarchyNode) throws Exception {
    if (paramMdseHierarchyNode == null)
      return Collections.emptyList(); 
    List<MdseHierarchyNode> list = classMap.get(paramMdseHierarchyNode.getDepartmentId());
    if (list == null) {
      list = ClientServiceFactory.getMdseHierarchyServices().findClasses(paramMdseHierarchyNode.getDepartmentId());
      classMap.put(paramMdseHierarchyNode.getDepartmentId(), list);
    } 
    return list;
  }
  
  public static List<MdseHierarchyNode> getSubclasses(MdseHierarchyNode paramMdseHierarchyNode) throws Exception {
    if (paramMdseHierarchyNode == null)
      return Collections.emptyList(); 
    List<MdseHierarchyNode> list = subclassMap.get(paramMdseHierarchyNode.getCompositeId());
    if (list == null) {
      list = ClientServiceFactory.getMdseHierarchyServices().findSubclasses(paramMdseHierarchyNode.getDepartmentId(), paramMdseHierarchyNode.getClassId());
      subclassMap.put(paramMdseHierarchyNode.getCompositeId(), list);
    } 
    return list;
  }
  
  public static MdseHierarchyNode getMdseHierarchyNode(Long paramLong) throws Exception {
    return (paramLong != null) ? getMdseHierarchyNode(paramLong, null, null) : null;
  }
  
  public static MdseHierarchyNode getMdseHierarchyNode(Long paramLong1, Long paramLong2, Long paramLong3) throws Exception {
    if (paramLong1 == null)
      return null; 
    MdseHierarchyNode mdseHierarchyNode = getDepartment(paramLong1);
    if (paramLong2 == null)
      return mdseHierarchyNode; 
    List<MdseHierarchyNode> list = getClasses(mdseHierarchyNode);
    for (MdseHierarchyNode mdseHierarchyNode1 : list) {
      if (mdseHierarchyNode1.getClassId().equals(paramLong2)) {
        if (paramLong3 == null)
          return mdseHierarchyNode1; 
        List<MdseHierarchyNode> list1 = getSubclasses(mdseHierarchyNode1);
        for (MdseHierarchyNode mdseHierarchyNode2 : list1) {
          if (mdseHierarchyNode2.getSubclassId().equals(paramLong3))
            return mdseHierarchyNode2; 
        } 
        return mdseHierarchyNode1;
      } 
    } 
    return mdseHierarchyNode;
  }
  
  public static Long[] breakCompositeKey(String paramString) {
    Long[] arrayOfLong = new Long[3];
    if (StringHelper.isNullOrEmpty(paramString))
      return arrayOfLong; 
    int i = paramString.indexOf(":::");
    if (i < 0) {
      arrayOfLong[0] = Long.valueOf(paramString);
    } else {
      arrayOfLong[0] = Long.valueOf(paramString.substring(0, i));
      paramString = paramString.substring(i + 3);
      i = paramString.indexOf(":::");
      if (i < 0) {
        arrayOfLong[1] = Long.valueOf(paramString);
      } else {
        arrayOfLong[1] = Long.valueOf(paramString.substring(0, i));
        arrayOfLong[2] = Long.valueOf(paramString.substring(i + 3));
      } 
    } 
    return arrayOfLong;
  }
  
  public static String getDepartmentName(Long paramLong) throws Exception {
    MdseHierarchyNode mdseHierarchyNode = getDepartment(paramLong);
    return (mdseHierarchyNode != null) ? mdseHierarchyNode.getDepartmentName() : "";
  }
  
  private static MdseHierarchyNode getDepartment(Long paramLong) throws Exception {
    List<MdseHierarchyNode> list = getAllDepartments();
    for (MdseHierarchyNode mdseHierarchyNode : list) {
      if (mdseHierarchyNode.getDepartmentId().equals(paramLong))
        return mdseHierarchyNode; 
    } 
    return null;
  }
  
  public static void clear() {
    departments.clear();
    classMap.clear();
    subclassMap.clear();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mdsehierarchy\MdseHierarchyCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */