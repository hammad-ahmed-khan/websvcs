package oracle.retail.sim.client.swing.displayer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Key Value Pair Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class KeyValuePairDisplayer extends AbstractDisplayer {
    private AbstractDisplayer defaultDisplayer;
    private Map<Class<?>, AbstractDisplayer> classDisplayerMap = new HashMap<>();
    private Map<Object, AbstractDisplayer> keyDisplayerMap = new HashMap<>();

    /****************************************************************************************************
     * Creates a new KeyValuePairDisplayer with the ObjectDisplayer specified as the default displayer
     * type.
     ***************************************************************************************************/
    public KeyValuePairDisplayer() {
        this(new ObjectDisplayer());
    }

    /****************************************************************************************************
     * Creates a new KeyValuePairDisplayer with the given default renderer.
     ***************************************************************************************************/
    public KeyValuePairDisplayer(AbstractDisplayer displayer) {
        defaultDisplayer = displayer;
        addClassDisplayer(Boolean.class, new BooleanDisplayer());
        addClassDisplayer(Date.class, new DateDisplayer());
        addClassDisplayer(Number.class, new NumberDisplayer());
        addClassDisplayer(String.class, new TranslatedObjectDisplayer());
    }

    /****************************************************************************************************
     * Adds a displayer for the given key.
     ***************************************************************************************************/
    public void addKeyDisplayer(Object key, AbstractDisplayer displayer) {
        keyDisplayerMap.put(key, displayer);
    }

    /****************************************************************************************************
     * Adds a displayer for the given class type.
     ***************************************************************************************************/
    public void addClassDisplayer(Class<?> classType, AbstractDisplayer displayer) {
        classDisplayerMap.put(classType, displayer);
    }

    /****************************************************************************************************
     * Finds a displayer for the given key, or null if it does not exist.
     ***************************************************************************************************/
    public AbstractDisplayer getKeyDisplayer(Object key) {
        return keyDisplayerMap.get(key);
    }

    /****************************************************************************************************
     * Finds a displayer for the given class. Look for exact match. If no exact, try superclasses and
     * interfaces, else return the default displayer.
     ***************************************************************************************************/
    public AbstractDisplayer getClassDisplayer(Class<?> classType) {
        if (classDisplayerMap.containsKey(classType)) {
            return classDisplayerMap.get(classType);
        }
        for (Map.Entry<Class<?>, AbstractDisplayer> entry : classDisplayerMap.entrySet()) {
            if (entry.getKey().isAssignableFrom(classType)) {
                AbstractDisplayer displayer = entry.getValue();
                addClassDisplayer(classType, displayer);
                return displayer;
            }
        }
        addClassDisplayer(classType, defaultDisplayer);
        return defaultDisplayer;
    }

    /****************************************************************************************************
     * Gets the display text for the value.
     ***************************************************************************************************/
    public String getDisplayText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        }
        if (value instanceof ConfigurationOption) {
            ConfigurationOption pair = (ConfigurationOption) value;
            AbstractDisplayer displayer = getKeyDisplayer(pair.getConfigKey());
            if (displayer == null) {
                displayer = getClassDisplayer(pair.getConfigValue().getClass());
            }
            return displayer.getDisplayText(pair.getConfigValue(), pair);
        }
        return value.toString();
    }
}
