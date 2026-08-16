package oracle.retail.sim.client.screen.supplier;

import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.person.AddressType;
import oracle.retail.sim.common.person.ContactInfo;
import oracle.retail.sim.common.person.SupplierContactInfo;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Supplier Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierDetailModel extends SimScreenModel {

    private Supplier supplier;
    private List<SupplierContactInfo> contactList;

    public void setSupplier(Supplier supplier) throws Exception {
        this.supplier = supplier;
        this.contactList = ClientServiceFactory.getSourceServices().readSupplierContactInfo(supplier.getId());
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public ContactInfo getContactInfo(AddressType addressType) {
        for (SupplierContactInfo contactInfo : contactList) {
            if (contactInfo.getAddrType() == addressType) {
                return contactInfo.getContactInfo();
            }
        }
        return null;
    }

    public AddressType getDefaultAddressQueryType() {
        return AddressType.RETURNS;
    }

    public Set<AddressType> findAddressQueryTypes() {
        return AddressType.getQuerySet();
    }
}
