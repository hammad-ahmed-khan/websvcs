package oracle.retail.sim.service.notes;

import java.util.List;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.notes.Note;

public abstract class NoteServices {
  public abstract List<Note> findNotes(FunctionalArea paramFunctionalArea, Long paramLong) throws Exception;
  
  public abstract void createNotes(List<Note> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\notes\NoteServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */