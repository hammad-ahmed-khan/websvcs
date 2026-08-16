package oracle.retail.sim.common.tolerance;

import java.math.BigDecimal;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.rules.core.NumberCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.NumberMustBeWholeRule;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ToleranceAdmin extends BusinessObject {
  private static final long serialVersionUID = 8104709230812659680L;
  
  private Long storeId;
  
  private MdseHierarchyNode hierarchyNode;
  
  private BigDecimal varianceCount = BigDecimal.ZERO;
  
  private BigDecimal variancePercent = BigDecimal.ZERO;
  
  private ToleranceTopic topic;
  
  private boolean dirty;
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public MdseHierarchyNode getHierarchyNode() {
    return this.hierarchyNode;
  }
  
  public String getFullDepartmentName() {
    return this.hierarchyNode.getDepartmentId() + " - " + this.hierarchyNode.getDepartmentName();
  }
  
  public String getFullClassName() {
    return this.hierarchyNode.getClassId() + " - " + this.hierarchyNode.getClassName();
  }
  
  public BigDecimal getVarianceCount() {
    return this.varianceCount;
  }
  
  public BigDecimal getVariancePercent() {
    return this.variancePercent;
  }
  
  public void setVarianceCount(BigDecimal paramBigDecimal) throws BusinessException {
    if (paramBigDecimal == null && this.variancePercent == null)
      throw new BusinessException(CommonMessageText.MISSING_VARIANCE_VALUE); 
    NumberCannotBeNegativeRule.execute(paramBigDecimal);
    NumberMustBeWholeRule.execute(paramBigDecimal);
    executeRule("setVarianceCount", new Object[] { paramBigDecimal });
    doSetVarianceCount(paramBigDecimal);
    doSetDirty();
  }
  
  public void setVariancePercent(BigDecimal paramBigDecimal) throws BusinessException {
    if (paramBigDecimal == null && this.varianceCount == null)
      throw new BusinessException(CommonMessageText.MISSING_VARIANCE_VALUE); 
    NumberCannotBeNegativeRule.execute(paramBigDecimal);
    executeRule("setVariancePercent", new Object[] { paramBigDecimal });
    doSetVariancePercent(paramBigDecimal);
    doSetDirty();
  }
  
  public ToleranceTopic getTopic() {
    return this.topic;
  }
  
  public void doSetTopic(ToleranceTopic paramToleranceTopic) {
    this.topic = paramToleranceTopic;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.varianceCount == null && this.variancePercent == null)
      throw new BusinessException(CommonMessageText.MISSING_VARIANCE_VALUE); 
    executeRule("isCoherent", new Object[0]);
    return true;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public void doSetHierarchyNode(MdseHierarchyNode paramMdseHierarchyNode) {
    this.hierarchyNode = paramMdseHierarchyNode;
  }
  
  public void doSetVarianceCount(BigDecimal paramBigDecimal) {
    this.varianceCount = paramBigDecimal;
  }
  
  public void doSetVariancePercent(BigDecimal paramBigDecimal) {
    this.variancePercent = paramBigDecimal;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public void doSetClean() {
    this.dirty = false;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Adhoc Count Group [");
    stringBuilder.append(" Store=").append(this.storeId);
    stringBuilder.append(" Variance Units=").append(this.varianceCount);
    stringBuilder.append(" Variance Percent=").append(this.variancePercent);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ToleranceAdmin toleranceAdmin = (ToleranceAdmin)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.storeId, toleranceAdmin.storeId);
    equalsBuilder.append(this.hierarchyNode, toleranceAdmin.hierarchyNode);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.storeId);
    hashCodeBuilder.append(this.hierarchyNode);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tolerance\ToleranceAdmin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */