package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferQueryFilter;
import oracle.retail.sim.common.transfer.TransferVO;

@Remote
public interface TransferInterface {
  CompressedObject<?> approveTransfer(CompressedObject<Transfer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> cancelSubmitTransfer(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> cancelTransfer(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> dispatchTransfer(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> dispatchTransfer2(CompressedObject<Transfer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> dispatchTransfer3(CompressedObject<Long> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> dispatchTransfer4(CompressedObject<Transfer> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> dispatchTransfers(CompressedObject<List<String>> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<Date> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<List<String>> findEmployeeIds(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<TransferVO>> findTransferVOs(CompressedObject<TransferQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> insertTransfer(CompressedObject<Transfer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Transfer> readTransfer(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Transfer> readTransferForViewOnly(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> receiveTransfer(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> receiveTransfer2(CompressedObject<Transfer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> rejectTransfer(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> requestTransfer(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> requestTransfer2(CompressedObject<Transfer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> submitTransfer(CompressedObject<Transfer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> submitTransfer2(CompressedObject<Transfer> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateRequestedQuantity(CompressedObject<Long> paramCompressedObject, CompressedObject<TransferLineItem> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateTransfer(CompressedObject<Transfer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\TransferInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */