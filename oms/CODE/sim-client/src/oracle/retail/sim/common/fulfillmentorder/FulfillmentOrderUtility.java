package oracle.retail.sim.common.fulfillmentorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;

public class FulfillmentOrderUtility {
  private static final String COMPOSITE_KEY_DELIMITER = ":";
  
  public static Quantity getRemainingQuantityForReceiving(FulfillmentOrderLineItem paramFulfillmentOrderLineItem) {
    return paramFulfillmentOrderLineItem.getOrderedQuantity().subtract(paramFulfillmentOrderLineItem.getDeliveredQuantity()).subtract(paramFulfillmentOrderLineItem.getCanceledQuantity()).subtract(paramFulfillmentOrderLineItem.getReservedQuantity()).max(Quantity.ZERO);
  }
  
  public static Map<String, FulfillmentOrderLineItem> createFulfillmentOrderLineItemMap(FulfillmentOrder paramFulfillmentOrder) {
    List<FulfillmentOrderLineItem> list = paramFulfillmentOrder.getLineItems();
    HashMap<Object, Object> hashMap = new HashMap<>(list.size());
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem : list)
      hashMap.put(fulfillmentOrderLineItem.getItemId(), fulfillmentOrderLineItem); 
    return (Map)hashMap;
  }
  
  public static String buildFulfillmentOrderKey(FulfillmentOrder paramFulfillmentOrder) {
    return buildFulfillmentOrderKey(paramFulfillmentOrder.getCustomerOrderId(), paramFulfillmentOrder.getExternalId());
  }
  
  public static String buildFulfillmentOrderKey(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    if (!StringHelper.isNullOrEmpty(paramString1))
      stringBuilder.append(paramString1); 
    stringBuilder.append(":");
    if (!StringHelper.isNullOrEmpty(paramString2))
      stringBuilder.append(paramString2); 
    return stringBuilder.toString();
  }
  
  public static Map<String, FulfillmentOrder> createFulfillmentOrderMap(Collection<FulfillmentOrder> paramCollection) {
    if (paramCollection.isEmpty())
      return new HashMap<>(); 
    HashMap<Object, Object> hashMap = new HashMap<>(paramCollection.size());
    for (FulfillmentOrder fulfillmentOrder : paramCollection)
      hashMap.put(buildFulfillmentOrderKey(fulfillmentOrder.getCustomerOrderId(), fulfillmentOrder.getExternalId()), fulfillmentOrder); 
    return (Map)hashMap;
  }
  
  public static Map<String, FulfillmentOrderVO> createFulfillmentOrderVOMap(Collection<FulfillmentOrderVO> paramCollection) {
    if (paramCollection.isEmpty())
      return new HashMap<>(); 
    HashMap<Object, Object> hashMap = new HashMap<>(paramCollection.size());
    for (FulfillmentOrderVO fulfillmentOrderVO : paramCollection)
      hashMap.put(buildFulfillmentOrderKey(fulfillmentOrderVO.getCustomerOrderId(), fulfillmentOrderVO.getExternalId()), fulfillmentOrderVO); 
    return (Map)hashMap;
  }
  
  public static List<FulfillmentOrderVO> convertToValueObjects(Collection<FulfillmentOrder> paramCollection) {
    if (paramCollection.isEmpty())
      return new ArrayList<>(); 
    ArrayList<FulfillmentOrderVO> arrayList = new ArrayList(paramCollection.size());
    for (FulfillmentOrder fulfillmentOrder : paramCollection)
      arrayList.add(convertToValueObject(fulfillmentOrder)); 
    return arrayList;
  }
  
  public static FulfillmentOrderVO convertToValueObject(FulfillmentOrder paramFulfillmentOrder) {
    FulfillmentOrderVO fulfillmentOrderVO = BOFactory.createFulfillmentOrderVO();
    fulfillmentOrderVO.doSetId(paramFulfillmentOrder.getId());
    fulfillmentOrderVO.doSetExternalId(paramFulfillmentOrder.getExternalId());
    fulfillmentOrderVO.doSetCustomerOrderId(paramFulfillmentOrder.getCustomerOrderId());
    fulfillmentOrderVO.doSetStoreId(paramFulfillmentOrder.getStoreId());
    fulfillmentOrderVO.doSetOrderType(paramFulfillmentOrder.getOrderType());
    fulfillmentOrderVO.doSetStatus(paramFulfillmentOrder.getStatus());
    fulfillmentOrderVO.doSetComments(paramFulfillmentOrder.getComments());
    fulfillmentOrderVO.doSetCreateDate(paramFulfillmentOrder.getCreateDate());
    fulfillmentOrderVO.doSetUpdateDate(paramFulfillmentOrder.getUpdateDate());
    fulfillmentOrderVO.doSetReleaseDate(paramFulfillmentOrder.getReleaseDate());
    fulfillmentOrderVO.doSetDeliveryDate(paramFulfillmentOrder.getDeliveryDate());
    fulfillmentOrderVO.doSetDeliveryType(paramFulfillmentOrder.getDeliveryType());
    fulfillmentOrderVO.doSetDeliveryCarrier(paramFulfillmentOrder.getDeliveryCarrier());
    fulfillmentOrderVO.doSetDeliveryService(paramFulfillmentOrder.getDeliveryService());
    fulfillmentOrderVO.doSetAllowPartialDelivery(paramFulfillmentOrder.isAllowPartialDelivery());
    fulfillmentOrderVO.doSetLineItemsCount(paramFulfillmentOrder.getLineItems().size());
    return fulfillmentOrderVO;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderUtility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */