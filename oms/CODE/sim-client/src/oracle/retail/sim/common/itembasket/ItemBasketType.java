package oracle.retail.sim.common.itembasket;

import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.type.Displayable;

public class ItemBasketType extends BusinessObject implements Displayable {
  private static final long serialVersionUID = -8257187277592046598L;
  
  private static final Integer BASKET_TYPE_ID = Integer.valueOf(1);
  
  private static final Integer ORDER_TYPE_ID = Integer.valueOf(2);
  
  private Integer id;
  
  private String description;
  
  public ItemBasketType(Integer paramInteger, String paramString) {
    this.id = paramInteger;
    this.description = paramString;
  }
  
  public Integer getId() {
    return this.id;
  }
  
  public String toDisplayString() {
    return this.description;
  }
  
  public boolean isItemBasketType() {
    return BASKET_TYPE_ID.equals(this.id);
  }
  
  public boolean isCustomerOrderType() {
    return ORDER_TYPE_ID.equals(this.id);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itembasket\ItemBasketType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */