package oracle.retail.sim.client.swing.editor;

import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.UIException;

/******************************************************************************************
 * This class is the interface implements by all editors.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public interface RetailEditor {
    String getIdentifier();

    void setIdentifier(String identifier);

    String getTitle();

    void setTitle(String title);

    REditorLabel getLabel();

    void setRequired(boolean required);

    boolean isRequired();

    boolean isEmpty();

    void setActionsEnabled(boolean enabled);

    boolean isActionsEnabled();

    void setErrorState(boolean errorState, String message);

    void setErrorState(boolean errorState);

    boolean isErrorState();

    String getErrorMessage();

    void registerAction(REventListener listener, String command);

    void setTitleAlignment(int alignment);

    int getTitleAlignment();

    void setSizeType(int sizeType);

    int getSizeType();

    void validateRequiredState(String ownerPrefix);

    void validatePermission(String ownerPrefix) throws UIException;

    int getVerticalWeight();

    int getHorizontalWeight();

    int getFill();
}
