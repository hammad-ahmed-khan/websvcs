package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.util.SimObjectUtils;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class SimEnumDisplayer<T extends Enum<T> & SimEnum<?>> extends AbstractDisplayer {
    private Class<T> enumClass;

    public SimEnumDisplayer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    public String getDisplayText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        }
        T enumValue = SimObjectUtils.getSimEnumValue(enumClass, value);
        if (enumValue != null) {
            return Translator.getText(enumValue.toString());
        }
        return Translator.getText(value.toString());
    }
}
