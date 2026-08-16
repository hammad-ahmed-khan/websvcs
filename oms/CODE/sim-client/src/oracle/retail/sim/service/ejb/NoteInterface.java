package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.notes.Note;

@Remote
public interface NoteInterface {
  CompressedObject<?> createNotes(CompressedObject<List<Note>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<Note>> findNotes(CompressedObject<FunctionalArea> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\NoteInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */