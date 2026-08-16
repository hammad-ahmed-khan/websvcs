package oracle.retail.sim.common.storeorder;

import java.math.BigDecimal;
import java.util.Date;
import oracle.retail.sim.common.business.BusinessObject;

public class ItemSale extends BusinessObject {
  static final long serialVersionUID = 990925079042890126L;
  
  private Date endOfWeekDate;
  
  private BigDecimal salesValue;
  
  private BigDecimal quantity;
  
  private ItemSaleType saleType;
  
  public Date getEndOfWeekDate() {
    return this.endOfWeekDate;
  }
  
  public void setEndOfWeekDate(Date paramDate) {
    this.endOfWeekDate = paramDate;
  }
  
  public BigDecimal getSalesValue() {
    return this.salesValue;
  }
  
  public void setSalesValue(BigDecimal paramBigDecimal) {
    this.salesValue = paramBigDecimal;
  }
  
  public BigDecimal getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(BigDecimal paramBigDecimal) {
    this.quantity = paramBigDecimal;
  }
  
  public ItemSaleType getSaleType() {
    return this.saleType;
  }
  
  public void setSaleType(ItemSaleType paramItemSaleType) {
    this.saleType = paramItemSaleType;
  }
  
  public String getSaleTypeDescription() {
    return (this.saleType != null) ? this.saleType.toString() : "";
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\ItemSale.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */