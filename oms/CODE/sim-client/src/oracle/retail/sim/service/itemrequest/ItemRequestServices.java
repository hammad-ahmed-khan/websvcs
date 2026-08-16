package oracle.retail.sim.service.itemrequest;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestQueryFilter;
import oracle.retail.sim.common.itemrequest.ItemRequestVO;

public abstract class ItemRequestServices {
  public abstract List<ItemRequestVO> findItemRequestVOs(ItemRequestQueryFilter paramItemRequestQueryFilter) throws Exception;
  
  public abstract ItemRequest readItemRequest(Long paramLong) throws Exception;
  
  public abstract Long insertItemRequest(ItemRequest paramItemRequest) throws Exception;
  
  public abstract Long updateItemRequest(ItemRequest paramItemRequest) throws Exception;
  
  public abstract Long confirmItemRequest(Long paramLong) throws Exception;
  
  public abstract Long confirmItemRequest(ItemRequest paramItemRequest) throws Exception;
  
  public abstract void cancelItemRequest(Long paramLong) throws Exception;
  
  public abstract void cancelItemRequests(List<Long> paramList) throws Exception;
  
  public abstract long cancelExpiredItemRequests() throws Exception;
  
  public abstract long generateItemRequests(Long paramLong1, Long paramLong2, Date paramDate) throws Exception;
  
  public abstract List<String> findEmployeeIds(Long paramLong) throws Exception;
  
  public abstract List<DeliveryTimeSlot> findDeliveryTimeSlots() throws Exception;
  
  public abstract DeliveryTimeSlot readDeliveryTimeslot(String paramString) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\itemrequest\ItemRequestServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */