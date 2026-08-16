package oracle.retail.sim.common.directdelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.core.locale.StringHelper;

public class DirectDeliveryLineItemUtility {
  private static final String COMPOSITE_KEY_DELIMITER = ":";
  
  public static String buildSimpleLineItemKey(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    if (!StringHelper.isNullOrEmpty(paramString1))
      stringBuilder.append(paramString1); 
    stringBuilder.append(":");
    if (!StringHelper.isNullOrEmpty(paramString2))
      stringBuilder.append(paramString2); 
    return stringBuilder.toString();
  }
  
  public static Map<String, DirectDeliverySimpleLineItem> createDeliverySimpleLineItemMap(DirectDelivery paramDirectDelivery) {
    if (paramDirectDelivery.isEmpty())
      return new HashMap<>(); 
    List<DirectDeliverySimpleLineItem> list = paramDirectDelivery.getSimpleLineItems();
    HashMap<Object, Object> hashMap = new HashMap<>(list.size());
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : list)
      hashMap.put(buildSimpleLineItemKey(directDeliverySimpleLineItem.getStockItem().getId(), directDeliverySimpleLineItem.getCarton().getExternalId()), directDeliverySimpleLineItem); 
    return (Map)hashMap;
  }
  
  public static Map<String, DirectDeliveryLineItem> createFlatDeliveryLineItemMap(DirectDelivery paramDirectDelivery) {
    if (paramDirectDelivery.isEmpty())
      return new HashMap<>(); 
    if (paramDirectDelivery.getCartons().size() == 1)
      return createFlatCartonLineItemMap(paramDirectDelivery.getCartons().get(0)); 
    List<DirectDeliverySimpleLineItem> list = paramDirectDelivery.getSimpleLineItems();
    Collections.sort(list, new DirectDeliverySimpleLineItemComparator());
    HashMap<Object, Object> hashMap = new HashMap<>();
    ArrayList<DirectDeliverySimpleLineItem> arrayList = new ArrayList();
    DirectDeliverySimpleLineItem directDeliverySimpleLineItem = null;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem1 : list) {
      if (directDeliverySimpleLineItem != null)
        if (directDeliverySimpleLineItem.getStockItem().getId().equals(directDeliverySimpleLineItem1.getStockItem().getId())) {
          arrayList.add(directDeliverySimpleLineItem);
        } else if (!arrayList.isEmpty()) {
          arrayList.add(directDeliverySimpleLineItem);
          hashMap.put(directDeliverySimpleLineItem.getStockItem().getId(), BOFactory.createDirectDeliveryCompositeLineItem(arrayList.<DirectDeliverySimpleLineItem>toArray(new DirectDeliverySimpleLineItem[arrayList.size()])));
          arrayList.clear();
        } else {
          hashMap.put(directDeliverySimpleLineItem.getStockItem().getId(), directDeliverySimpleLineItem);
        }  
      directDeliverySimpleLineItem = directDeliverySimpleLineItem1;
    } 
    if (directDeliverySimpleLineItem != null)
      if (!arrayList.isEmpty()) {
        arrayList.add(directDeliverySimpleLineItem);
        hashMap.put(directDeliverySimpleLineItem.getStockItem().getId(), BOFactory.createDirectDeliveryCompositeLineItem(arrayList.<DirectDeliverySimpleLineItem>toArray(new DirectDeliverySimpleLineItem[arrayList.size()])));
      } else {
        hashMap.put(directDeliverySimpleLineItem.getStockItem().getId(), directDeliverySimpleLineItem);
      }  
    return (Map)hashMap;
  }
  
  public static Map<String, DirectDeliveryLineItem> createFlatCartonLineItemMap(DirectDeliveryCarton paramDirectDeliveryCarton) {
    if (paramDirectDeliveryCarton.isEmpty())
      return new HashMap<>(); 
    List<DirectDeliveryLineItem> list = paramDirectDeliveryCarton.getLineItems();
    HashMap<Object, Object> hashMap = new HashMap<>(list.size());
    for (DirectDeliveryLineItem directDeliveryLineItem : list)
      hashMap.put(directDeliveryLineItem.getStockItem().getId(), directDeliveryLineItem); 
    return (Map)hashMap;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryLineItemUtility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */