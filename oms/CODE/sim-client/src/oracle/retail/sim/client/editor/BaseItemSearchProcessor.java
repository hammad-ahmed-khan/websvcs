package oracle.retail.sim.client.editor;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.displayer.ItemIdDescriptionDisplayer;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.editor.SearchProcessor;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/********************************************************************************************************
 * Base Item Search Process that handles the generic methods.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class BaseItemSearchProcessor implements SearchProcessor {

    private AttributeDisplayer entryDisplayer = new AttributeDisplayer("id");
    private ItemIdDescriptionDisplayer valueDisplayer = new ItemIdDescriptionDisplayer();

    public BasicDisplayer getEntryDisplayer() {
        return entryDisplayer;
    }

    public BasicDisplayer getValueDisplayer() {
        return valueDisplayer;
    }

    protected boolean isNonRangedAllowed() {
        return SimConfigManager.getBoolean(SimConfigManager.ALLOW_NON_RANGE_ITEM);
    }

    protected void displayError(Throwable exception) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Error");
        dialog.setMessage(exception);
        dialog.activate();
    }

    protected void displayError(MessageText message) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Error");
        dialog.setMessage(message);
        dialog.activate();
    }
}
