package oracle.retail.sim.service.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.itemprice.ItemPrice;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketQueryFilter;
import oracle.retail.sim.common.itemticket.TicketReason;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.uin.SerialNumberValue;

@Remote
public interface ItemTicketInterface {
  CompressedObject<ItemTicket> createItemTicket(CompressedObject<ItemTicket> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemTicket>> createItemTickets(CompressedObject<Long> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ItemTicket>> createItemTickets2(CompressedObject<Long> paramCompressedObject, CompressedObject<List<RetailItem>> paramCompressedObject1, CompressedObject<TicketType> paramCompressedObject2, CompressedObject<TicketReason> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<?> createPriceChangeTickets(CompressedObject<Long> paramCompressedObject1, CompressedObject<List<ItemPrice>> paramCompressedObject, CompressedObject<Long> paramCompressedObject2, CompressedObject<TicketReason> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<List<TicketTypeFormat>> findAllTicketTypeFormats(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<TicketTypeFormat> findDefaultTicketTypeFormat(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<ItemTicket>> findItemTickets(CompressedObject<ItemTicketQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<TicketTypeFormat>> findTicketTypeFormats(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<TicketType>> findTicketTypes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Map<String, List<SerialNumberValue>>> generateItemTicketAGSNs(CompressedObject<ItemTicket> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<TicketType> readTicketType(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateItemTicket(CompressedObject<ItemTicket> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateItemTickets(CompressedObject<List<ItemTicket>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ItemTicketInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */