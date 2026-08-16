package oracle.retail.sim.client.screen.stockcount;

import java.math.BigDecimal;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

public class AuthorizeQueryFilter {
    private MdseHierarchyNode hierarchyNode;
    private Integer varianceUOM;
    private BigDecimal variancePercent;

    public void setHierarchyNode(MdseHierarchyNode hierarchyNode) {
        this.hierarchyNode = hierarchyNode;
    }

    public MdseHierarchyNode getHierarchyNode() {
        return hierarchyNode;
    }

    public void setVarianceUom(Integer value) {
        varianceUOM = value;
    }

    public Integer getVarianceUom() {
        return varianceUOM;
    }

    public void setVariancePercent(BigDecimal value) {
        variancePercent = value;
    }

    public BigDecimal getVariancePercent() {
        return variancePercent;
    }
}
