package oracle.retail.sim.common.fulfillmentorder;

import java.io.Serializable;
import java.math.BigDecimal;

public class FulfillmentOrderCreateLineItemVO implements Serializable {
  private static final long serialVersionUID = -5092206231549267147L;
  
  private String itemId;
  
  private BigDecimal orderQuantity;
  
  private String preferredUom;
  
  private String comments;
  
  private BigDecimal retailPrice;
  
  private String retailCurrency;
  
  private String standardUom;
  
  private String substituteIndicator;
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public BigDecimal getOrderQuantity() {
    return this.orderQuantity;
  }
  
  public void doSetOrderQuantity(BigDecimal paramBigDecimal) {
    this.orderQuantity = paramBigDecimal;
  }
  
  public String getPreferredUom() {
    return this.preferredUom;
  }
  
  public void doSetPreferredUom(String paramString) {
    this.preferredUom = paramString;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public BigDecimal getRetailPrice() {
    return this.retailPrice;
  }
  
  public void doSetRetailPrice(BigDecimal paramBigDecimal) {
    this.retailPrice = paramBigDecimal;
  }
  
  public String getRetailCurrency() {
    return this.retailCurrency;
  }
  
  public void doSetRetailCurrency(String paramString) {
    this.retailCurrency = paramString;
  }
  
  public String getStandardUom() {
    return this.standardUom;
  }
  
  public void doSetStandardUom(String paramString) {
    this.standardUom = paramString;
  }
  
  public String getSubstituteIndicator() {
    return this.substituteIndicator;
  }
  
  public void doSetSubstituteIndicator(String paramString) {
    this.substituteIndicator = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderCreateLineItemVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */