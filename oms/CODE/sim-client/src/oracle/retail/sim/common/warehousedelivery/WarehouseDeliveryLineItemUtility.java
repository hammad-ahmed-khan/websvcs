package oracle.retail.sim.common.warehousedelivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.core.locale.StringHelper;

public class WarehouseDeliveryLineItemUtility {
  private static final String COMPOSITE_KEY_DELIMITER = ":";
  
  public static String buildCompositeLineItemKey(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    if (!StringHelper.isNullOrEmpty(paramString1))
      stringBuilder.append(paramString1); 
    stringBuilder.append(":");
    if (!StringHelper.isNullOrEmpty(paramString2))
      stringBuilder.append(paramString2); 
    return stringBuilder.toString();
  }
  
  public static String buildSimpleLineItemKey(String paramString1, String paramString2, String paramString3, String paramString4) {
    StringBuilder stringBuilder = new StringBuilder();
    if (!StringHelper.isNullOrEmpty(paramString1))
      stringBuilder.append(paramString1); 
    stringBuilder.append(":");
    if (!StringHelper.isNullOrEmpty(paramString2))
      stringBuilder.append(paramString2); 
    stringBuilder.append(":");
    if (!StringHelper.isNullOrEmpty(paramString3))
      stringBuilder.append(paramString3); 
    stringBuilder.append(":");
    if (!StringHelper.isNullOrEmpty(paramString4))
      stringBuilder.append(paramString4); 
    return stringBuilder.toString();
  }
  
  public static String buildReceiptDocumentKey(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    if (!StringHelper.isNullOrEmpty(paramString1))
      stringBuilder.append(paramString1); 
    stringBuilder.append(":");
    if (!StringHelper.isNullOrEmpty(paramString2))
      stringBuilder.append(paramString2); 
    return stringBuilder.toString();
  }
  
  public static Map<String, WarehouseDeliverySimpleLineItem> createDeliverySimpleLineItemMap(WarehouseDelivery paramWarehouseDelivery) {
    if (paramWarehouseDelivery.isEmpty())
      return new HashMap<>(); 
    List<WarehouseDeliverySimpleLineItem> list = paramWarehouseDelivery.getSimpleLineItems();
    HashMap<Object, Object> hashMap = new HashMap<>(list.size());
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : list)
      hashMap.put(buildSimpleLineItemKey(warehouseDeliverySimpleLineItem.getStockItem().getId(), warehouseDeliverySimpleLineItem.getCarton().getExternalId(), warehouseDeliverySimpleLineItem.getReceiptDocumentId(), warehouseDeliverySimpleLineItem.getReceiptDocumentType()), warehouseDeliverySimpleLineItem); 
    return (Map)hashMap;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryLineItemUtility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */