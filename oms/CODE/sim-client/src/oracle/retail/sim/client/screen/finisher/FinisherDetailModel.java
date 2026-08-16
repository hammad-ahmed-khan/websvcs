package oracle.retail.sim.client.screen.finisher;

import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.person.ContactInfo;
import oracle.retail.sim.common.person.FinisherContactInfo;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Finisher Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherDetailModel extends SimScreenModel {

    private Finisher finisher;
    private List<FinisherContactInfo> finisherContactList;

    public void loadFinisher() throws Exception {
        finisher = (Finisher) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FINISHER);
        finisherContactList = ClientServiceFactory.getSourceServices().readFinisherContactInfo(finisher.getId());
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_FINISHER);
    }

    public Finisher getFinisher() {
        return finisher;
    }

    public ContactInfo getContactInfo(AddressType addressType) {
        for (FinisherContactInfo finisherContactInfo : finisherContactList) {
            if (finisherContactInfo.getAddrType() == addressType) {
                return finisherContactInfo.getContactInfo();
            }
        }
        return null;
    }

    public AddressType getDefaultAddressQueryType() {
        return AddressType.RETURNS;
    }

    public Set<AddressType> getAddressQueryTypes() {
        return AddressType.getQuerySet();
    }
}
