package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.integration.SimMessageFamily;
import oracle.retail.sim.common.integration.SimMessageType;
import oracle.retail.sim.common.mps.MpsStagedMessage;
import oracle.retail.sim.common.mps.MpsStagedMessageQueryFilter;
import oracle.retail.sim.common.mps.MpsStagedMessageVO;
import oracle.retail.sim.common.mps.MpsWorkStatus;
import oracle.retail.sim.common.mps.MpsWorkerTypeVO;

@Remote
public interface MpsInterface {
  CompressedObject<?> deleteStagedMessages(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<MpsStagedMessageVO>> findStagedMessageVOs(CompressedObject<MpsStagedMessageQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<List<Long>>> generateWorkQueue(CompressedObject<Integer> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> processSimMessage(CompressedObject<String> paramCompressedObject1, CompressedObject<Object> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<Long> paramCompressedObject4, CompressedObject<SimSession> paramCompressedObject5) throws Exception;
  
  CompressedObject<?> processStagedMessage(CompressedObject<MpsStagedMessage> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<MpsWorkStatus> readJobStatus(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<SimMessageFamily>> readSimMessageFamilies(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<SimMessageType>> readSimMessageTypes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<MpsStagedMessage> readStagedMessage(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<MpsWorkerTypeVO>> readWorkerTypeVOs(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> resetStagedMessages(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> stageSimMessage(CompressedObject<String> paramCompressedObject1, CompressedObject<Object> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<Long> paramCompressedObject4, CompressedObject<SimSession> paramCompressedObject5) throws Exception;
  
  CompressedObject<Long> stageSimMessages(CompressedObject<String> paramCompressedObject1, CompressedObject<List<?>> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<Long> paramCompressedObject4, CompressedObject<SimSession> paramCompressedObject5) throws Exception;
  
  CompressedObject<?> startWorkerTypes(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> stopWorkerTypes(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateStagedMessageData(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\MpsInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */