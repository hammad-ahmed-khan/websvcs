package oracle.retail.sim.client.screen.notes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.notes.Note;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Model for the notes dialog window.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NotesDialogModel extends SimScreenModel {

    private FunctionalArea functionalArea;
    private Long functionalId;

    public void setFunctionalArea(FunctionalArea functionalArea) {
        this.functionalArea = functionalArea;
    }

    public void setFunctionalId(Long functionalId) {
        this.functionalId = functionalId;
    }

    public Collection<Note> findNotes() throws Exception {
        if (functionalArea == null || functionalId == null) {
            return Collections.emptyList();
        }
        return ClientServiceFactory.getNoteServices().findNotes(functionalArea, functionalId);
    }

    public Note createNote(String text) throws BusinessException {
        Note note = BOFactory.createNote();
        note.setDate(SimDateUtil.getCurrentDate());
        note.setFunctionalArea(functionalArea);
        note.setFunctionalId(functionalId);
        note.setText(text);
        note.setUser(getUserName());
        return note;
    }

    public void saveNotes(List<Note> notes) throws Exception {
        List<Note> newNotes = new ArrayList<>();
        for (Note note : notes) {
            if (note.getId() == null) {
                newNotes.add(note);
            }
        }
        ClientServiceFactory.getNoteServices().createNotes(newNotes);
    }
}
