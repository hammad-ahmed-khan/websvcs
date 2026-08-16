package oracle.retail.sim.client.swing.displaytable;

import java.io.Serializable;
import oracle.retail.sim.client.swing.util.Repository;

/******************************************************************************************
 * In memory location to store all table configuration information. New code needs to be
 * added in the future to persist the TableConfigurationRepository.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class TableConfigurationRepository implements Serializable {
    private static final long serialVersionUID = -1490335033127753886L;

    private static Repository repository = new Repository();

    /******************************************************************************************
     * Adds a new configuration data object to the table configuration repository.
     * <p>
     * @param configurationData The table configuration to add.
     ******************************************************************************************/
    private static void addTableConfigurationData(TableConfigurationData configurationData) {
        repository.put(configurationData.getIdentifier(), configurationData);
    }

    /******************************************************************************************
     * Retrieves the table configuration data from the repository based on an identifier. If the
     * identifier is not found, this will create a new TableConfigurationData object and place
     * it in the Repository.
     * <p>
     * @param identifier The identifier to retrieve table configuration data for.
     * <p>
     * @return The table configuration data.
     ******************************************************************************************/
    public static TableConfigurationData getTableConfigurationData(String identifier) {
        TableConfigurationData configurationData = (TableConfigurationData) repository.get(identifier);
        if (configurationData == null) {
            configurationData = new TableConfigurationData();
            configurationData.setIdentifier(identifier);
            addTableConfigurationData(configurationData);
        }
        return configurationData;
    }
}
