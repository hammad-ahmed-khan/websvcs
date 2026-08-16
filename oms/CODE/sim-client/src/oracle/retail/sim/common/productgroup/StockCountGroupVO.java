package oracle.retail.sim.common.productgroup;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockCountGroupVO implements Serializable {
  private static final long serialVersionUID = -4091926924542316702L;
  
  private Long id = null;
  
  private String description = null;
  
  private ProductGroupType type = null;
  
  private BigDecimal varianceCount = null;
  
  private BigDecimal variancePercent = null;
  
  private BigDecimal varianceValue = null;
  
  private boolean isRecountAllowed = false;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public ProductGroupType getType() {
    return this.type;
  }
  
  public void doSetType(ProductGroupType paramProductGroupType) {
    this.type = paramProductGroupType;
  }
  
  public boolean isRecountAllowed() {
    return this.isRecountAllowed;
  }
  
  public void doSetRecountAllowed(boolean paramBoolean) {
    this.isRecountAllowed = paramBoolean;
  }
  
  public BigDecimal getVarianceCount() {
    return this.varianceCount;
  }
  
  public void doSetVarianceCount(BigDecimal paramBigDecimal) {
    this.varianceCount = paramBigDecimal;
  }
  
  public BigDecimal getVariancePercent() {
    return this.variancePercent;
  }
  
  public void doSetVariancePercent(BigDecimal paramBigDecimal) {
    this.variancePercent = paramBigDecimal;
  }
  
  public BigDecimal getVarianceValue() {
    return this.varianceValue;
  }
  
  public void doSetVarianceValue(BigDecimal paramBigDecimal) {
    this.varianceValue = paramBigDecimal;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\StockCountGroupVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */