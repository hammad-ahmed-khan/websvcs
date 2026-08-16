package oracle.retail.sim.client.screen.uda;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * UDA Print Setup Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UDAPrintSetupModel extends SimScreenModel {

    public List<UDADetail> findUDADetails(UDAType udaType) throws Exception {
        List<UDADetail> filteredUdaDetails = new ArrayList<>();
        for (UDADetail udaDetail : ClientDataCacheUtility.getUDADetails()) {
            if ((udaType == null) || (udaType == udaDetail.getType())) {
                filteredUdaDetails.add(udaDetail);
            }
        }
        return filteredUdaDetails;
    }

    public void updateUDADetails(List<UDADetail> udaDetails) throws Exception {
        List<UDADetail> udaDetailsToUpdate = new ArrayList<>();
        for (UDADetail udaDetail : udaDetails) {
            if (udaDetail.isDirty()) {
                udaDetailsToUpdate.add(udaDetail);
            }
        }
        if (udaDetailsToUpdate.isEmpty()) {
            return;
        }
        ClientServiceFactory.getUDAServices().updateUDADetails(udaDetailsToUpdate);
    }
}
