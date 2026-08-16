package oracle.retail.sim.client.screen.customuin;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.customuin.CustomSerialNumber;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.customuin.CustomUINServices;

/********************************************************************************************************
 * Model for the Create UIN entry screen.
 * <p>
 * This is future work-in-progress code for future SIM 14.0 or later release.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomUINCreateModel extends SimScreenModel {
    private static final String CUSTOM_UIN_SERVICES_KEY = "CUSTOM_UIN_SERVICES_KEY";

    public void saveSerialNumbers(List<CustomUINCreateWrapper> wrappers) throws Exception {
        List<CustomSerialNumber> serialNumbers = new ArrayList<>();
        for (CustomUINCreateWrapper wrapper : wrappers) {
            for (SerialNumberValue serialNumberValue : wrapper.getSerialNumbers()) {
                CustomSerialNumber serialNumber = new CustomSerialNumber();
                serialNumber.setId(serialNumberValue.getUinId());
                serialNumber.setStatus(serialNumberValue.getStatus());
                serialNumbers.add(serialNumber);
            }
        }
        getCustomUINService().moveSerialNumbersToInStock(getStoreId(), serialNumbers);
    }

    private CustomUINServices getCustomUINService() {
        CustomUINServices services = (CustomUINServices) RepositoryManager.getStateObject(CUSTOM_UIN_SERVICES_KEY);
        if (services == null) {
            services = ClientServiceFactory.getCustomUINServices();
            RepositoryManager.addStateObject(CUSTOM_UIN_SERVICES_KEY, services);
        }
        return services;
    }
}
