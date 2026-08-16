package oracle.retail.sim.client.configutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.SimObjectUtils;

/********************************************************************************************************
 * Class to help read/write to client cache for SIM GUI
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ClientCacheManager {
    private static final String CACHE_DIR = System.getProperty("java.io.tmpdir");

    private static File getCacheDir(boolean mkdirs, boolean userSpecific) {
        String userName = !userSpecific || StringHelper.isNullOrEmpty(SimRepository.getUserName()) ? "everyone" : SimRepository.getUserName();
        String path = CACHE_DIR + File.separator + "sim" + File.separator + "cache" + File.separator + userName;
        File cacheDir = new File(path);
        return cacheDir.exists() || mkdirs && cacheDir.mkdirs() ? cacheDir : null;
    }

    /**
     * This method caches an object to disk into CACHE_DIR
     */
    public static void writeToClientCache(String key, Object value, boolean userSpecific) {
        FileOutputStream fos = null;
        try {
            File cacheDir = getCacheDir(true, userSpecific);
            if (cacheDir != null && key != null && value != null) {
                File cacheFile = new File(cacheDir + File.separator + key);
                LogService.debug(ClientCacheManager.class, "Writing to client cache: " + cacheFile);
                fos = new FileOutputStream(cacheFile);
                fos.write(SimObjectUtils.toByteArray(value));
            }
        } catch (FileNotFoundException e) {
            LogService.error(ClientCacheManager.class, "Cannot find client cache file! " + "key=" + key);
        } catch (IOException e) {
            LogService.error(ClientCacheManager.class, "Cannot write to client cache! " + "key=" + key);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogService.warn(ClientCacheManager.class, "Cannot close client cache file! " + "key=" + key);
                }
            }
        }
    }

    /**
     * This method reads information from CACHE_DIR that was previously cached using the above method
     */
    public static Object readFromClientCache(String key, boolean userSpecific) {
        FileInputStream fis = null;
        try {
            File cacheDir = getCacheDir(false, userSpecific);
            if (cacheDir != null && key != null) {
                File cacheFile = new File(cacheDir + File.separator + key);
                LogService.debug(ClientCacheManager.class, "Reading from client cache: " + cacheFile);
                byte[] data = new byte[(int) cacheFile.length()];
                fis = new FileInputStream(cacheFile);
                fis.read(data);
                return SimObjectUtils.fromByteArray(data);
            }
        } catch (FileNotFoundException e) {
            LogService.error(ClientCacheManager.class, "Cannot find client cache! " + " key=" + key);
        } catch (IOException e) {
            LogService.error(ClientCacheManager.class, "Cannot read from client cache! " + " key=" + key);
        } catch (ClassNotFoundException e) {
            LogService.error(ClientCacheManager.class, "Corrupt client cache! " + " key=" + key);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LogService.warn(ClientCacheManager.class, "Cannot close client cache file!");
                }
            }
        }
        return null;
    }
}
