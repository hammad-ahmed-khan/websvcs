package com.logicinfo.oms.vascontract;

import com.logicinfo.oms.ejb.VasContractsEcom;

import com.logicinfo.oms.ejb.VasContractsEcomPK;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "SessionEJB", mappedName = "VASContract-VASContract-SessionEJB")
public class VASContractEJBSessionBean implements SessionEJB, SessionEJBLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "VASContract")
    private EntityManager em;

    public VASContractEJBSessionBean() {
    }

    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    public VasContractsEcom persistVasContractsEcom(VasContractsEcom vasContractsEcom) {
        em.persist(vasContractsEcom);
        return vasContractsEcom;
    }

    public VasContractsEcom mergeVasContractsEcom(VasContractsEcom vasContractsEcom) {
        return em.merge(vasContractsEcom);
    }

    public void removeVasContractsEcom(VasContractsEcom vasContractsEcom) {
        vasContractsEcom = em.find(VasContractsEcom.class, new VasContractsEcomPK(vasContractsEcom.getSrvId()));
        em.remove(vasContractsEcom);
    }

    /** <code>select o from VasContractsEcom o</code> */
    public List<VasContractsEcom> getVasContractsEcomFindAll() {
        return em.createNamedQuery("VasContractsEcom.findAll").getResultList();
    }
}
