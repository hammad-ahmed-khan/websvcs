package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.common.core.SimEnum;

/********************************************************************************************************
 * This static factory helps build common displayers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class SimDisplayerFactory {
    private SimDisplayerFactory() {
    }

    public static BooleanDisplayer createBooleanDisplayer() {
        //TODO NEIL: add methods to support boolean displayer variations
        return new BooleanDisplayer();
    }

    public static <T extends Enum<T> & SimEnum<?>> SimEnumDisplayer<T> createSimEnumDisplayer(Class<T> enumClass) {
        return new SimEnumDisplayer<T>(enumClass);
    }
}
