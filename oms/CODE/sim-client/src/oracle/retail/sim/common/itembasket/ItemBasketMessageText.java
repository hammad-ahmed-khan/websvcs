package oracle.retail.sim.common.itembasket;

import oracle.retail.sim.common.business.MessageText;

public enum ItemBasketMessageText implements MessageText {
  ALTERNATE_ID_EXISTS("Alternate Id already Exists"),
  INVALID_ITEM_SCANNED("Invalid Item"),
  ITEM_NOT_SELLABLE("Item is not sellable"),
  INVALID_BASKET("Item Basket is invalid");
  
  private String message;
  
  ItemBasketMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itembasket\ItemBasketMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */