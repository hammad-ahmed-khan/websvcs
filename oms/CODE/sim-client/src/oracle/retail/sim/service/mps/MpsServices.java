package oracle.retail.sim.service.mps;

import java.util.List;
import oracle.retail.sim.common.integration.SimMessageFamily;
import oracle.retail.sim.common.integration.SimMessageType;
import oracle.retail.sim.common.mps.MpsStagedMessage;
import oracle.retail.sim.common.mps.MpsStagedMessageQueryFilter;
import oracle.retail.sim.common.mps.MpsStagedMessageVO;
import oracle.retail.sim.common.mps.MpsWorkStatus;
import oracle.retail.sim.common.mps.MpsWorkerTypeVO;

public abstract class MpsServices {
  public abstract List<MpsWorkerTypeVO> readWorkerTypeVOs() throws Exception;
  
  public abstract void startWorkerTypes(List<Long> paramList) throws Exception;
  
  public abstract void stopWorkerTypes(List<Long> paramList) throws Exception;
  
  public abstract List<MpsStagedMessageVO> findStagedMessageVOs(MpsStagedMessageQueryFilter paramMpsStagedMessageQueryFilter) throws Exception;
  
  public abstract MpsStagedMessage readStagedMessage(Long paramLong) throws Exception;
  
  public abstract void resetStagedMessages(List<Long> paramList) throws Exception;
  
  public abstract void deleteStagedMessages(List<Long> paramList) throws Exception;
  
  public abstract void updateStagedMessageData(Long paramLong, String paramString) throws Exception;
  
  public abstract List<List<Long>> generateWorkQueue(int paramInt) throws Exception;
  
  public abstract List<SimMessageFamily> readSimMessageFamilies() throws Exception;
  
  public abstract List<SimMessageType> readSimMessageTypes() throws Exception;
  
  public abstract void processStagedMessage(MpsStagedMessage paramMpsStagedMessage, boolean paramBoolean) throws Exception;
  
  public abstract void processSimMessage(String paramString1, Object paramObject, boolean paramBoolean, String paramString2, Long paramLong) throws Exception;
  
  public abstract Long stageSimMessage(String paramString1, Object paramObject, boolean paramBoolean, String paramString2, Long paramLong) throws Exception;
  
  public abstract Long stageSimMessages(String paramString1, List<?> paramList, boolean paramBoolean, String paramString2, Long paramLong) throws Exception;
  
  public abstract MpsWorkStatus readJobStatus(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\mps\MpsServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */