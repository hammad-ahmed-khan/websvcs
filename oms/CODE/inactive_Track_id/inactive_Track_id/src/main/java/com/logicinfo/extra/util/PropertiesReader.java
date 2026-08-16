// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.util;

import java.util.Set;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.log4j.LogManager;
import java.util.Properties;
import org.apache.log4j.Logger;

public class PropertiesReader
{
    private static final Logger logger;
    private static final Properties configProp;
    
    static {
        logger = LogManager.getLogger(PropertiesReader.class.getName());
        configProp = new Properties();
        new PropertiesReader();
        PropertiesReader.logger.info((Object)"-----------------All the loaded properties---------------");
        for (final String str : getAllPropertyNames()) {
            PropertiesReader.logger.info((Object)str);
        }
    }
    
    private PropertiesReader() {
        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("db.properties");
        try {
            PropertiesReader.configProp.load(in);
        }
        catch (Exception e) {
            PropertiesReader.logger.error((Object)("Properties path is not correct" + e));
        }
    }
    
    public static String getProperty(final String key) {
        return PropertiesReader.configProp.getProperty(key);
    }
    
    private static Set<String> getAllPropertyNames() {
        return PropertiesReader.configProp.stringPropertyNames();
    }
    
    public boolean containsKey(final String key) {
        return PropertiesReader.configProp.containsKey(key);
    }
}
