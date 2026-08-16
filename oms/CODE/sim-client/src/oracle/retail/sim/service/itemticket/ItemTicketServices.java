package oracle.retail.sim.service.itemticket;

import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.item.RetailItem;
import oracle.retail.sim.common.itemprice.ItemPrice;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.itemticket.ItemTicketQueryFilter;
import oracle.retail.sim.common.itemticket.TicketReason;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.uin.SerialNumberValue;

public abstract class ItemTicketServices {
  public abstract List<ItemTicket> findItemTickets(ItemTicketQueryFilter paramItemTicketQueryFilter) throws Exception;
  
  public abstract ItemTicket createItemTicket(ItemTicket paramItemTicket) throws Exception;
  
  public abstract void updateItemTicket(ItemTicket paramItemTicket) throws Exception;
  
  public abstract void updateItemTickets(List<ItemTicket> paramList) throws Exception;
  
  public abstract List<ItemTicket> createItemTickets(Long paramLong, List<RetailItem> paramList, TicketType paramTicketType, TicketReason paramTicketReason) throws Exception;
  
  public abstract void createPriceChangeTickets(Long paramLong1, List<ItemPrice> paramList, Long paramLong2, TicketReason paramTicketReason) throws Exception;
  
  public abstract List<ItemTicket> createItemTickets(Long paramLong, List<Long> paramList) throws Exception;
  
  public abstract List<TicketTypeFormat> findAllTicketTypeFormats(Long paramLong) throws Exception;
  
  public abstract List<TicketTypeFormat> findTicketTypeFormats(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract TicketTypeFormat findDefaultTicketTypeFormat(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract List<TicketType> findTicketTypes() throws Exception;
  
  public abstract TicketType readTicketType(Long paramLong) throws Exception;
  
  public abstract Map<String, List<SerialNumberValue>> generateItemTicketAGSNs(ItemTicket paramItemTicket) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\itemticket\ItemTicketServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */