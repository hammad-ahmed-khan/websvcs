package oracle.retail.sim.common.mdsehierarchy;

import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.locale.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MdseHierarchyNode extends BusinessObject {
  private static final long serialVersionUID = -2923169305452356834L;
  
  public static final String STRUCTURE_SEPARATOR = ":::";
  
  private Long departmentId = null;
  
  private String departmentName = null;
  
  private Long classId = null;
  
  private String className = null;
  
  private Long subclassId = null;
  
  private String subclassName = null;
  
  public Long getDepartmentId() {
    return this.departmentId;
  }
  
  public void doSetDepartmentId(Long paramLong) {
    this.departmentId = paramLong;
  }
  
  public String getDepartmentName() {
    return this.departmentName;
  }
  
  public void doSetDepartmentName(String paramString) {
    this.departmentName = paramString;
  }
  
  public Long getClassId() {
    return this.classId;
  }
  
  public void doSetClassId(Long paramLong) {
    this.classId = paramLong;
  }
  
  public String getClassName() {
    return this.className;
  }
  
  public void doSetClassName(String paramString) {
    this.className = paramString;
  }
  
  public Long getSubclassId() {
    return this.subclassId;
  }
  
  public void doSetSubclassId(Long paramLong) {
    this.subclassId = paramLong;
  }
  
  public String getSubclassName() {
    return this.subclassName;
  }
  
  public void doSetSubclassName(String paramString) {
    this.subclassName = paramString;
  }
  
  public boolean isDepartment() {
    return (this.classId == null);
  }
  
  public boolean isClass() {
    return (this.subclassId == null) ? ((this.classId != null)) : false;
  }
  
  public boolean isSubclass() {
    return (this.subclassId != null);
  }
  
  public String getCompositeId() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.departmentId != null) {
      stringBuilder.append(String.valueOf(this.departmentId));
      if (this.classId != null) {
        stringBuilder.append(":::");
        stringBuilder.append(String.valueOf(this.classId));
        if (this.subclassId != null) {
          stringBuilder.append(":::");
          stringBuilder.append(String.valueOf(this.subclassId));
        } 
      } 
    } 
    return stringBuilder.toString();
  }
  
  public String getCompositeName() {
    StringBuilder stringBuilder = new StringBuilder(this.departmentName);
    if (!StringHelper.isNullOrEmpty(this.className)) {
      stringBuilder.append(":::");
      stringBuilder.append(this.className);
      if (!StringHelper.isNullOrEmpty(this.subclassName)) {
        stringBuilder.append(":::");
        stringBuilder.append(this.subclassName);
      } 
    } 
    return stringBuilder.toString();
  }
  
  public String toString() {
    return getCompositeId();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    MdseHierarchyNode mdseHierarchyNode = (MdseHierarchyNode)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.departmentId, mdseHierarchyNode.departmentId);
    equalsBuilder.append(this.classId, mdseHierarchyNode.classId);
    equalsBuilder.append(this.subclassId, mdseHierarchyNode.subclassId);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.departmentId);
    hashCodeBuilder.append(this.classId);
    hashCodeBuilder.append(this.subclassId);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\mdsehierarchy\MdseHierarchyNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */