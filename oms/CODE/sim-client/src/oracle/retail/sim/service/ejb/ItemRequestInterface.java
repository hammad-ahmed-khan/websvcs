package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestQueryFilter;
import oracle.retail.sim.common.itemrequest.ItemRequestVO;

@Remote
public interface ItemRequestInterface {
  CompressedObject<Long> cancelExpiredItemRequests(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> cancelItemRequest(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> cancelItemRequests(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> confirmItemRequest(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> confirmItemRequest2(CompressedObject<ItemRequest> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<DeliveryTimeSlot>> findDeliveryTimeSlots(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<String>> findEmployeeIds(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemRequestVO>> findItemRequestVOs(CompressedObject<ItemRequestQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> generateItemRequests(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Long> insertItemRequest(CompressedObject<ItemRequest> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<DeliveryTimeSlot> readDeliveryTimeslot(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ItemRequest> readItemRequest(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> updateItemRequest(CompressedObject<ItemRequest> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ItemRequestInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */