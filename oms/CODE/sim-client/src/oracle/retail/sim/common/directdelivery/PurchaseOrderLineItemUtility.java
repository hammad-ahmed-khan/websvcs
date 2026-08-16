package oracle.retail.sim.common.directdelivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderLineItemUtility {
  public static Map<String, PurchaseOrderLineItem> createPurchaseOrderLineItemMap(PurchaseOrder paramPurchaseOrder) {
    if (paramPurchaseOrder.isEmpty())
      return new HashMap<>(); 
    List<PurchaseOrderLineItem> list = paramPurchaseOrder.getLineItems();
    HashMap<Object, Object> hashMap = new HashMap<>(list.size());
    for (PurchaseOrderLineItem purchaseOrderLineItem : list)
      hashMap.put(purchaseOrderLineItem.getSupplierItem().getItemId(), purchaseOrderLineItem); 
    return (Map)hashMap;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\PurchaseOrderLineItemUtility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */