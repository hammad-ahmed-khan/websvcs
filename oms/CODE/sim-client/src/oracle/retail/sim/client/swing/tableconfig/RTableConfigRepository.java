package oracle.retail.sim.client.swing.tableconfig;

import java.awt.Font;
import java.io.Serializable;
import javax.swing.UIManager;
import oracle.retail.sim.client.configutil.ClientCacheManager;
import oracle.retail.sim.client.swing.util.Repository;

/********************************************************************************************************
 * In-memory location to store all table configuration information.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RTableConfigRepository implements Serializable {

    private static final long serialVersionUID = 8734660967831068887L;

    private static Repository repository = new Repository();

    /****************************************************************************************************
     * Retrieves the table configuration data from the repository based on an identifier. If the
     * identifier is not found, this will create a new RTableConfigData object and place it in the
     * Repository.
     * <p>
     * @param identifier The identifier to retrieve table configuration data for.
     * @return The table configuration data.
     ***************************************************************************************************/
    public static RTableConfigData getTableConfigurationData(String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("Identifier cannot be null!");
        }
        RTableConfigData configData = (RTableConfigData) repository.get(identifier);
        if (configData == null) {
            configData = new RTableConfigData();
            configData.setIdentifier(identifier);

            // Default To Theme Font
            Font font = UIManager.getFont("Table.font");
            if (font != null) {
                int fontSize = font.getSize();
                if (fontSize >= RTableConfigConstants.SMALLEST && fontSize <= RTableConfigConstants.LARGEST) {
                    configData.setFontSizeSetting(fontSize);
                }
            }

            // Put In Repository
            repository.put(configData.getIdentifier(), configData);
        }
        return configData;
    }

    /****************************************************************************************************
     * Writes the repository to the specified file.
     * <p>
     * @param filename The file name of the file to write the repository to.
     ***************************************************************************************************/
    public static void cacheRepository(String key) {
        ClientCacheManager.writeToClientCache(key, repository, true);
    }

    /****************************************************************************************************
     * Reads the repository from the specified file.
     * <p>
     * @param filename The file name of the file to read the repository from.
     ***************************************************************************************************/
    public static void loadRepositoryFromCache(String key) {
        repository = (Repository) ClientCacheManager.readFromClientCache(key, true);
        repository = repository == null ? new Repository() : repository;
    }
}
