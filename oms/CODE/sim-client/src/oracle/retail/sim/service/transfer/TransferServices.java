package oracle.retail.sim.service.transfer;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferQueryFilter;
import oracle.retail.sim.common.transfer.TransferVO;

public abstract class TransferServices {
  public abstract List<TransferVO> findTransferVOs(TransferQueryFilter paramTransferQueryFilter) throws Exception;
  
  public abstract Transfer readTransfer(Long paramLong) throws Exception;
  
  public abstract Transfer readTransferForViewOnly(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract Long insertTransfer(Transfer paramTransfer) throws Exception;
  
  public abstract void updateTransfer(Transfer paramTransfer) throws Exception;
  
  public abstract void cancelTransfer(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract void requestTransfer(Long paramLong) throws Exception;
  
  public abstract void requestTransfer(Transfer paramTransfer) throws Exception;
  
  public abstract void approveTransfer(Transfer paramTransfer) throws Exception;
  
  public abstract void rejectTransfer(Long paramLong) throws Exception;
  
  public abstract void dispatchTransfer(Long paramLong, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void dispatchTransfer(Long paramLong) throws Exception;
  
  public abstract Long dispatchTransfer(Transfer paramTransfer) throws Exception;
  
  public abstract Long dispatchTransfer(Transfer paramTransfer, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void dispatchTransfers(List<String> paramList, String paramString1, String paramString2, Date paramDate) throws Exception;
  
  public abstract Long submitTransfer(Transfer paramTransfer, List<SessionPrinter> paramList) throws Exception;
  
  public abstract Long submitTransfer(Transfer paramTransfer) throws Exception;
  
  public abstract void receiveTransfer(Transfer paramTransfer) throws Exception;
  
  public abstract void receiveTransfer(Long paramLong) throws Exception;
  
  public abstract List<String> findEmployeeIds(Long paramLong) throws Exception;
  
  public abstract void updateRequestedQuantity(Long paramLong, TransferLineItem paramTransferLineItem) throws Exception;
  
  public abstract void cancelSubmitTransfer(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\transfer\TransferServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */