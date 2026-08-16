package oracle.retail.sim.client.tableeditor;

import java.util.Arrays;
import java.util.Collection;
import oracle.retail.sim.client.displayer.SimDisplayerFactory;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.common.util.SimObjectUtils;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class SimEnumTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = 7008904218651789869L;

    public <T extends Enum<T> & SimEnum<?>> SimEnumTableEditor(Class<T> enumClass) {
        this(enumClass, false, null);
    }

    public <T extends Enum<T> & SimEnum<?>> SimEnumTableEditor(Class<T> enumClass, boolean allowEmptySelection, Collection<T> enumValues) {
        setDisplayer(SimDisplayerFactory.createSimEnumDisplayer(enumClass));
        setValueClass(SimObjectUtils.getSimEnumCodeClass(enumClass));
        if (!allowEmptySelection) {
            removeEmptySelection();
        }
        if (enumValues == null) {
            enumValues = Arrays.asList(enumClass.getEnumConstants());
        }
        for (T enumValue : enumValues) {
            addItem(enumValue.getCode());
        }
    }
}
