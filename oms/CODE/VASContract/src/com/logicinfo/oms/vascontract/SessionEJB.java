package com.logicinfo.oms.vascontract;

import com.logicinfo.oms.ejb.VasContractsEcom;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SessionEJB {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    VasContractsEcom persistVasContractsEcom(VasContractsEcom vasContractsEcom);

    VasContractsEcom mergeVasContractsEcom(VasContractsEcom vasContractsEcom);

    void removeVasContractsEcom(VasContractsEcom vasContractsEcom);

    List<VasContractsEcom> getVasContractsEcomFindAll();
}
