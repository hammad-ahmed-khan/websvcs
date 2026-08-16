package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceConstants;

/********************************************************************************************************
 * Store Sequence Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceAreaDisplayer extends AbstractDisplayer {

    private StoreSequenceAreaDescDisplayer areaDescDisplayer = new StoreSequenceAreaDescDisplayer();
    private TranslatedObjectDisplayer areaTypeDisplayer = new TranslatedObjectDisplayer();

    public String getDisplayText(Object object) {
        if (object instanceof StoreSequenceArea) {
            StoreSequenceArea sequenceArea = (StoreSequenceArea) object;

            if (sequenceArea.isNotSequenced() || sequenceArea.getAreaType() == null || sequenceArea.getDescription() == null) {
                return Translator.getText(StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION);
            }
            StringBuilder buffer = new StringBuilder();
            buffer.append(areaDescDisplayer.getDisplayText(sequenceArea.getDescription()));
            buffer.append(" - ");
            buffer.append(areaTypeDisplayer.getDisplayText(sequenceArea.getAreaType()));

            return buffer.toString();
        }
        return Translator.getText(StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION);
    }
}
