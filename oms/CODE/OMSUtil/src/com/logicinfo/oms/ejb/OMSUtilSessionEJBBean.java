package com.logicinfo.oms.ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.soap.SOAPException;

@Stateless(name = "OMSUtilSessionEJB", mappedName = "OMSUtil-OMSUtil-OMSUtilSessionEJB")

public class OMSUtilSessionEJBBean implements OMSUtilSessionEJB {

	@Resource
	SessionContext sessionContext;
	@PersistenceContext(unitName = "omsUtilPersistence")
	private EntityManager em;

	public OMSUtilSessionEJBBean() {
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

	public OmsCustOrdHead persistOmsCustOrdHead(OmsCustOrdHead omsCustOrdHead) {
		em.persist(omsCustOrdHead);
		return omsCustOrdHead;
	}

	public OmsCustOrdHead mergeOmsCustOrdHead(OmsCustOrdHead omsCustOrdHead) {
		return em.merge(omsCustOrdHead);
	}

	public void removeOmsCustOrdHead(OmsCustOrdHead omsCustOrdHead) {
		omsCustOrdHead = em.find(OmsCustOrdHead.class, omsCustOrdHead.getOmsCustOrdNo());
		em.remove(omsCustOrdHead);
	}

	/** <code>select o from OmsCustOrdHead o</code> */
	public List<OmsCustOrdHead> getOmsCustOrdHeadFindAll() {
		return em.createNamedQuery("OmsCustOrdHead.findAll").getResultList();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdHead o where o.applicationId=:appId and o.custOrderNo=:custOrdNumb</code>
	 */
	public List<BigDecimal> getOmsCustOrdHeadFindDuplicate(String appId, String custOrdNumb, String status) {
		return em.createNamedQuery("OmsCustOrdHead.findDuplicate").setParameter("appId", appId).setParameter("custOrdNumb", custOrdNumb).setParameter("status", status).getResultList();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdHead o where  o.custOrderNo=:custOrderNo and o.subCustOrderNo=:subCustOrderNo and o.applicationId=:appId</code>
	 */
	public List<BigDecimal> getOmsCustOrdHeadFindByExternalCustOrdNo(String custOrderNo, String subCustOrderNo, String applicationId) {
		return em.createNamedQuery("OmsCustOrdHead.findByExternalCustOrdNo").setParameter("custOrderNo", custOrderNo).setParameter("subCustOrderNo", subCustOrderNo)
				.setParameter("applicationId", applicationId).getResultList();
	}

	public List<BigDecimal> getOmsCustOrdHeadFindByExternalCustOrdNoByFailedStatus(String custOrderNo, String subCustOrderNo, String applicationId) {
		return em.createNamedQuery("OmsCustOrdHead.findByExternalCustOrdNoByFailedStatus").setParameter("custOrderNo", custOrderNo).setParameter("subCustOrderNo", subCustOrderNo)
				.setParameter("applicationId", applicationId).getResultList();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdHead o where  o.custOrderNo=:custOrderNo and o.subCustOrderNo=:subCustOrderNo</code>
	 */
	public BigDecimal getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(String custOrderNo, String subCustOrderNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdHead.findByCustOrdNoAndSubCustOrdNo").setParameter("custOrderNo", custOrderNo).setParameter("subCustOrderNo", subCustOrderNo)
				.getSingleResult();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdHead o where o.custOrderNo=:custOrderNo</code>
	 */
	public BigDecimal getOmsCustOrdHeadFindOmsCustOrdNo(String custOrderNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdHead.findOmsCustOrdNo").setParameter("custOrderNo", custOrderNo).getSingleResult();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdHead o where  o.custOrderNo=:custOrderNo and o.subCustOrderNo=:subCustOrderNo and o.applicationId=:appId</code>
	 */
	public BigDecimal getOmsCustOrdHeadFindOmsCustOrderNo(String custOrderNo, String subCustOrderNo, String applicationId) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdHead.findByExternalCustOrdNo").setParameter("custOrderNo", custOrderNo).setParameter("subCustOrderNo", subCustOrderNo)
				.setParameter("applicationId", applicationId).getSingleResult();
	}

	public List<OmsCustOrdHead> getOmsCustOrdHeadFindByCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCustOrdHead.findByCustNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select o.paymentStatus from OmsCustOrdHead o where  o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public List<String> getOmsCustOrdHeadFindPaymentStatus(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCustOrdHead.findPaymentStatus").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select o.customerLang from OmsCustOrdHead o where  o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public String getOmsCustOrdHeadFindLanguage(BigDecimal omsCustOrdNo) {
		return (String) em.createNamedQuery("OmsCustOrdHead.findLanguage").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdHead o where o.applicationId=:appId and o.custOrderNo=:custOrdNumb and o.subCustOrderNo=:subCustOrderNo and o.status IN(:status)</code>
	 */
	public List<BigDecimal> getOmsCustOrdHeadFindByStatus(String appId, String custOrdNumb, String subCustOrderNo) {
		return em.createNamedQuery("OmsCustOrdHead.findByStatus").setParameter("appId", appId).setParameter("custOrdNumb", custOrdNumb).setParameter("subCustOrderNo", subCustOrderNo).getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdHead o where o.custOrderNo=:custOrderNo</code>
	 * 
	 * @param custOrderNo
	 * @return
	 */
	public List<OmsCustOrdHead> getOmsCustOrdHeadFindColumns(String custOrderNo) {
		return em.createNamedQuery("OmsCustOrdHead.findColumns").setParameter("custOrderNo", custOrderNo).getResultList();
	}

	public List<BigDecimal> getOmsCustOrdHeadFindByCustId(String custId) {
		return em.createNamedQuery("OmsCustOrdHead.findByCustId").setParameter("custId", custId).getResultList();
	}

	public Ordcust persistOrdcust(Ordcust ordcust) {
		em.persist(ordcust);
		return ordcust;
	}

	public Ordcust mergeOrdcust(Ordcust ordcust) {
		return em.merge(ordcust);
	}

	public void removeOrdcust(Ordcust ordcust) {
		ordcust = em.find(Ordcust.class, ordcust.getOrdcustNo());
		em.remove(ordcust);
	}

	/** <code>select o from Ordcust o</code> */
	public List<Ordcust> getOrdcustFindAll() {
		return em.createNamedQuery("Ordcust.findAll").getResultList();
	}

	/**
	 * <code>select o from Ordcust o where o.customerOrderNo=:customerOrderNo and fulfillOrderNo=:fulfillOrderNo </code>
	 */
	public List<Ordcust> getOrdcustFindByFulfilOrdNo(String customerOrderNo, String fulfillOrderNo, BigDecimal sourceLocId, BigDecimal fulfillLocId) {
		return em.createNamedQuery("Ordcust.findByFulfilOrdNo").setParameter("customerOrderNo", customerOrderNo).setParameter("fulfillOrderNo", fulfillOrderNo).setParameter("sourceLocId", sourceLocId)
				.setParameter("fulfillLocId", fulfillLocId).getResultList();
	}

	public OmsFulfillMatrixExtHead persistOmsFulfillMatrixExtHead(OmsFulfillMatrixExtHead omsFulfillMatrixExtHead) {
		em.persist(omsFulfillMatrixExtHead);
		return omsFulfillMatrixExtHead;
	}

	public OmsFulfillMatrixExtHead mergeOmsFulfillMatrixExtHead(OmsFulfillMatrixExtHead omsFulfillMatrixExtHead) {
		return em.merge(omsFulfillMatrixExtHead);
	}

	public void removeOmsFulfillMatrixExtHead(OmsFulfillMatrixExtHead omsFulfillMatrixExtHead) {
		omsFulfillMatrixExtHead = em.find(OmsFulfillMatrixExtHead.class, omsFulfillMatrixExtHead.getCombinationId());
		em.remove(omsFulfillMatrixExtHead);
	}

	/** <code>select o from OmsFulfillMatrixExtHead o</code> */
	public List<OmsFulfillMatrixExtHead> getOmsFulfillMatrixExtHeadFindAll() {
		return em.createNamedQuery("OmsFulfillMatrixExtHead.findAll").getResultList();
	}

	/**
	 * <code>select o.combinationId from OmsFulfillMatrixExtHead o where o.requestorId=:reqID and o.shipClassification=:shipClassification and o.customerCity=:custCity and o.modeOfDelivery=:modeOfDelv</code>
	 */
	public BigDecimal getOmsFulfillMatrixExtHeadFindCombination(BigDecimal reqID, String shipClassification, String custCity, String modeOfDelv, String deliverZone, String marketPlaceInd, String applicationId, String shipToStore) {
		return (BigDecimal) em.createNamedQuery("OmsFulfillMatrixExtHead.findCombination").setParameter("reqID", reqID).setParameter("shipClassification", shipClassification)
				.setParameter("custCity", custCity).setParameter("modeOfDelv", modeOfDelv).setParameter("deliverZone", deliverZone).setParameter("marketPlaceInd", marketPlaceInd).setParameter("applicationId", applicationId).setParameter("shipToStore", shipToStore).getSingleResult();
	}

	/**
	 * <code>select o.combinationId from OmsFulfillMatrixExtHead o where o.requestorId=:reqID and o.shipClassification=:shipClassification and o.modeOfDelivery=:modeOfDelv</code>
	 */
	public BigDecimal getOmsFulfillMatrixExtHeadFindCombinationWoCity(BigDecimal reqID, String shipClassification, String modeOfDelv, String deliverZone, String marketPlaceInd, String applicationId, String shipToStore) {
		return (BigDecimal) em.createNamedQuery("OmsFulfillMatrixExtHead.findCombinationWoCity").setParameter("reqID", reqID).setParameter("shipClassification", shipClassification)
				.setParameter("modeOfDelv", modeOfDelv).setParameter("deliverZone", deliverZone).setParameter("marketPlaceInd", marketPlaceInd).setParameter("applicationId", applicationId).setParameter("shipToStore", shipToStore).getSingleResult();
	}

	public OmsCoFulfillDetail persistOmsCoFulfillDetail(OmsCoFulfillDetail omsCoFulfillDetail) {
		em.persist(omsCoFulfillDetail);
		return omsCoFulfillDetail;
	}

	public OmsCoFulfillDetail mergeOmsCoFulfillDetail(OmsCoFulfillDetail omsCoFulfillDetail) {
		return em.merge(omsCoFulfillDetail);
	}

	public void removeOmsCoFulfillDetail(OmsCoFulfillDetail omsCoFulfillDetail) {
		omsCoFulfillDetail = em.find(OmsCoFulfillDetail.class, new OmsCoFulfillDetailPK(omsCoFulfillDetail.getFulfillOrderNo(), omsCoFulfillDetail.getItem(), omsCoFulfillDetail.getOmsCustOrdNo()));
		em.remove(omsCoFulfillDetail);
	}

	/** <code>select o from OmsCoFulfillDetail o</code> */
	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindAll() {
		return em.createNamedQuery("OmsCoFulfillDetail.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCoFulfillDetail.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:custNumb and o.item=:item and lineNo=:lineNo and o.fulfillConfQty - o.fulfillDeliverQty - o.fulfillCancelQty > 0 ORDER BY o.sourceLocType</code>
	 */
	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindFulFillDetails(BigDecimal custNumb, String item, BigDecimal lineNo) {
		return em.createNamedQuery("OmsCoFulfillDetail.findFulFillDetails").setParameter("custNumb", custNumb).setParameter("item", item).setParameter("lineNo", lineNo).getResultList();
	}

	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindOpenFulFillOrders(BigDecimal custNumb) {
		return em.createNamedQuery("OmsCoFulfillDetail.findOpenFulFillOrders").setParameter("custNumb", custNumb).getResultList();
	}

	/**
	 * <code>select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:custNumb and o.item=:item  and o.fulfillConfQty - o.fulfillDeliverQty - o.fulfillCancelQty > 0 ORDER BY o.sourceLocType</code>
	 */
	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindFulFillDetailByItem(BigDecimal custNumb, String item) {
		return em.createNamedQuery("OmsCoFulfillDetail.findFulFillDetailByItem").setParameter("custNumb", custNumb).setParameter("item", item).getResultList();
	}

	/**
	 * <code>select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:custNumb and o.item=:item  and o.fulfillConfQty - o.fulfillDeliverQty - o.fulfillCancelQty > 0 ORDER BY o.sourceLocType</code>
	 */
	public BigDecimal getOmsCoFulfillDetailFindMaxFulOrdNo(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCoFulfillDetail.findMaxFulOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item</code>
	 * 
	 * @param omsCustOrdNo
	 * @param item
	 * @return
	 */
	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindColumns(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCoFulfillDetail.findColumns").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select o.fulfillOrderNo from OmsCoFulfillDetail o where o.lineNo=:lineNo and o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public BigDecimal getOmsCoFulfillDetailFindFulfillSeqForResv(BigDecimal lineNo, BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCoFulfillDetail.findFulfillSeqForResv").setParameter("lineNo", lineNo).setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o.fulfillOrderNo from OmsCoFulfillDetail o where o.lineNo=:lineNo and o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindFulfillByLineNo(BigDecimal lineNo, BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCoFulfillDetail.findFulfillByLineNo").setParameter("lineNo", lineNo).setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * select o.sourceLoc from OmsCoFulfillDetail o where
	 * o.omsCustOrdNo=:omsCustOrdNo
	 **/
	public List<BigDecimal> getOmsCoFulfillDetailFindByOmsCustOrderNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCoFulfillDetail.findByOmsCustOrderNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindBySourceLocAndOmsCustNo(BigDecimal omsCustOrdNo, String item, BigDecimal sourceLoc, BigDecimal lineNo) {
		return em.createNamedQuery("OmsCoFulfillDetail.findBySourceLocAndOmsCustNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("item", item).setParameter("sourceLoc", sourceLoc)
				.setParameter("lineNo", lineNo).getResultList();
	}

	/**
	 * <code>SELECT o.omsCustOrdNo, o.fulfillOrderNo, SUM(o.fulfillConfQty - o.fulfillDeliverQty - o.fulfillCancelQty) FROM OmsCoFulfillDetail o WHERE o.omsCustOrdNo=:omsCustOrdNumb AND o.fulfillOrderNo=:fulfillOrdNo  GROUP BY o.omsCustOrdNo, o.fulfillOrderNo</code>
	 */
	public boolean isCoFulfilled(BigDecimal omsCustOrdNo, Long fulfillOrdNo) throws SOAPException {
		List<Object[]> results = em.createNamedQuery("OmsCoFulfillDetail.findFulfillmentTotals").setParameter("omsCustOrdNumb", omsCustOrdNo).setParameter("fulfillOrdNo", fulfillOrdNo)
				.getResultList();
		if (results.size() > 0) { // If the fulfillment record exists
			Object[] theResultObject = results.get(0);
			BigDecimal pendingQty = (BigDecimal) theResultObject[2];
			if (pendingQty.floatValue() > 0) { // If the order is still not completely fulfilled
				return false;
			} else { // If the order is completely fulfilled.
				return true;
			}
		} else { // If there is no fulfillment record.
			throw new SOAPException("No Data");
		}
	}

	public List<BigDecimal> getOmsCoFulfillDetailFindBySourceLoc(BigDecimal sourceLoc) {
		return em.createNamedQuery("OmsCoFulfillDetail.findBySourceLoc").setParameter("sourceLoc", sourceLoc).getResultList();
	}

	public OmsRtlogPublishLog persistOmsRtlogPublishLog(OmsRtlogPublishLog omsRtlogPublishLog) {
		em.persist(omsRtlogPublishLog);
		return omsRtlogPublishLog;
	}

	public OmsRtlogPublishLog mergeOmsRtlogPublishLog(OmsRtlogPublishLog omsRtlogPublishLog) {
		return em.merge(omsRtlogPublishLog);
	}

	public void removeOmsRtlogPublishLog(OmsRtlogPublishLog omsRtlogPublishLog) {
		omsRtlogPublishLog = em.find(OmsRtlogPublishLog.class, omsRtlogPublishLog.getOmsRtlogPubSeqNo());
		em.remove(omsRtlogPublishLog);
	}

	/** <code>select o from OmsRtlogPublishLog o</code> */
	public List<OmsRtlogPublishLog> getOmsRtlogPublishLogFindAll() {
		return em.createNamedQuery("OmsRtlogPublishLog.findAll").getResultList();
	}

	public OmsVirtualStrMatrix persistOmsVirtualStrMatrix(OmsVirtualStrMatrix omsVirtualStrMatrix) {
		em.persist(omsVirtualStrMatrix);
		return omsVirtualStrMatrix;
	}

	public OmsVirtualStrMatrix mergeOmsVirtualStrMatrix(OmsVirtualStrMatrix omsVirtualStrMatrix) {
		return em.merge(omsVirtualStrMatrix);
	}

	public void removeOmsVirtualStrMatrix(OmsVirtualStrMatrix omsVirtualStrMatrix) {
		omsVirtualStrMatrix = em.find(OmsVirtualStrMatrix.class, omsVirtualStrMatrix.getLocId());
		em.remove(omsVirtualStrMatrix);
	}

	/** <code>select o from OmsVirtualStrMatrix o</code> */
	public List<OmsVirtualStrMatrix> getOmsVirtualStrMatrixFindAll() {
		return em.createNamedQuery("OmsVirtualStrMatrix.findAll").getResultList();
	}

	/**
	 * <code>select o.virtualLocId from OmsVirtualStrMatrix o where o.locId=:locID and o.locType=:locationType</code>
	 */
	public BigDecimal getOmsVirtualStrMatrixFindVirtualLocID(BigDecimal locID, String locationType) {
		return (BigDecimal) em.createNamedQuery("OmsVirtualStrMatrix.findVirtualLocID").setParameter("locID", locID).setParameter("locationType", locationType).getSingleResult();
	}

	public OmsCustOrdTender persistOmsCustOrdTender(OmsCustOrdTender omsCustOrdTender) {
		em.persist(omsCustOrdTender);
		return omsCustOrdTender;
	}

	public OmsCustOrdTender mergeOmsCustOrdTender(OmsCustOrdTender omsCustOrdTender) {
		return em.merge(omsCustOrdTender);
	}

	public void removeOmsCustOrdTender(OmsCustOrdTender omsCustOrdTender) {
		omsCustOrdTender = em.find(OmsCustOrdTender.class, new OmsCustOrdTenderPK(omsCustOrdTender.getOmsCustOrdNo(), omsCustOrdTender.getTenderSeqNo()));
		em.remove(omsCustOrdTender);
	}

	/** <code>select o from OmsCustOrdTender o</code> */
	public List<OmsCustOrdTender> getOmsCustOrdTenderFindAll() {
		return em.createNamedQuery("OmsCustOrdTender.findAll").getResultList();
	}

	public OmsTempCoFo persistOmsTempCoFo(OmsTempCoFo omsTempCoFo) {
		em.persist(omsTempCoFo);
		return omsTempCoFo;
	}

	public OmsTempCoFo mergeOmsTempCoFo(OmsTempCoFo omsTempCoFo) {
		return em.merge(omsTempCoFo);
	}

	public void removeOmsTempCoFo(OmsTempCoFo omsTempCoFo) {
		omsTempCoFo = em.find(OmsTempCoFo.class, new OmsTempCoFoPK(omsTempCoFo.getItem(), omsTempCoFo.getOmsCustOrdNo(), omsTempCoFo.getSourceLocId()));
		em.remove(omsTempCoFo);
	}

	/** <code>select o from OmsTempCoFo o</code> */
	public List<OmsTempCoFo> getOmsTempCoFoFindAll() {
		return em.createNamedQuery("OmsTempCoFo.findAll").getResultList();
	}

	/**
	 * <code>select o.item,o.sourceLocId,o.orderQty from OmsTempCoFo o where o.omsCustOrdNo=:custNo</code>
	 */
	public List<Object[]> getOmsTempCoFoFindSADAD(BigDecimal custNo) {
		return em.createNamedQuery("OmsTempCoFo.findSADAD").setParameter("custNo", custNo).getResultList();
	}

	/**
	 * <code>select o from OmsTempCoFo o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public List<OmsTempCoFo> getOmsTempCoFoFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsTempCoFo.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select DISTINCT o.sourceLocId,o.fulfillLocId from OmsTempCoFo o where o.omsCustOrdNo=:custNo</code>
	 */
	public List<Object[]> getOmsTempCoFoFindDistinctLocs(BigDecimal custNo) {
		return em.createNamedQuery("OmsTempCoFo.findDistinctLocs").setParameter("custNo", custNo).getResultList();
	}

	/**
	 * <code>select o from OmsTempCoFo o where o.omsCustOrdNo=:custNo and o.sourceLocId=:sourceLocId and o.fulfillLocId=:fulfillLocId</code>
	 */
	public List<OmsTempCoFo> getOmsTempCoFoFindByLocation(BigDecimal custNo, BigDecimal sourceLocId, BigDecimal fulfillLocId) {
		return em.createNamedQuery("OmsTempCoFo.findByLocation").setParameter("custNo", custNo).setParameter("sourceLocId", sourceLocId).setParameter("fulfillLocId", fulfillLocId).getResultList();
	}

	/**
	 * <code>select o from OmsTempCoFo o where o.omsCustOrdNo=:custNo  and o.rmsResponseCode=:rmsResponseCode  </code>
	 */
	public List<OmsTempCoFo> getOmsTempCoFoFindByResponseCode(BigDecimal custNo, String rmsResponseCode) {
		return em.createNamedQuery("OmsTempCoFo.findByResponseCode").setParameter("custNo", custNo).setParameter("rmsResponseCode", rmsResponseCode).getResultList();
	}

	public OmsSystemParameters persistOmsSystemParameters(OmsSystemParameters omsSystemParameters) {
		em.persist(omsSystemParameters);
		return omsSystemParameters;
	}

	public OmsSystemParameters mergeOmsSystemParameters(OmsSystemParameters omsSystemParameters) {
		return em.merge(omsSystemParameters);
	}

	public void removeOmsSystemParameters(OmsSystemParameters omsSystemParameters) {
		omsSystemParameters = em.find(OmsSystemParameters.class, new OmsSystemParametersPK(omsSystemParameters.getParameterId(), omsSystemParameters.getParameterName()));
		em.remove(omsSystemParameters);
	}

	/** <code>select o from OmsSystemParameters o</code> */
	public List<OmsSystemParameters> getOmsSystemParametersFindAll() {
		return em.createNamedQuery("OmsSystemParameters.findAll").getResultList();
	}

	/**
	 * <code>select o.parameterValue from OmsSystemParameters o where o.parameterName=:paraName and o.parameterId=:paraId</code>
	 */
	public String getOmsSystemParametersFindIndValue(String paraName, String paraId) {
		return (String) em.createNamedQuery("OmsSystemParameters.findIndValue").setParameter("paraName", paraName).setParameter("paraId", paraId).getSingleResult();
	}

	/**
	 * <code>select o from OmsSystemParameters o where  o.parameterId=:paraId</code>
	 */
	public List<OmsSystemParameters> getOmsSystemParametersFindByParameterId(String paraId) {
		return em.createNamedQuery("OmsSystemParameters.findByParameterId").setParameter("paraId", paraId).getResultList();
	}

	public OmsCustOrdAddress persistOmsCustOrdAddress(OmsCustOrdAddress omsCustOrdAddress) {
		em.persist(omsCustOrdAddress);
		return omsCustOrdAddress;
	}

	public OmsCustOrdAddress mergeOmsCustOrdAddress(OmsCustOrdAddress omsCustOrdAddress) {
		return em.merge(omsCustOrdAddress);
	}

	public void removeOmsCustOrdAddress(OmsCustOrdAddress omsCustOrdAddress) {
		omsCustOrdAddress = em.find(OmsCustOrdAddress.class, omsCustOrdAddress.getOmsCustOrdNo());
		em.remove(omsCustOrdAddress);
	}

	/** <code>select o from OmsCustOrdAddress o</code> */
	public List<OmsCustOrdAddress> getOmsCustOrdAddressFindAll() {
		return em.createNamedQuery("OmsCustOrdAddress.findAll").getResultList();
	}

	/**
	 * select o.omsCustOrdNo from OmsCustOrdAddress o where
	 * o.deliverFirstName=:deliverFirstName
	 **/
	public List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstName(String deliverFirstName) {
		return em.createNamedQuery("OmsCustOrdAddress.findByDeliverFirstName").setParameter("deliverFirstName", deliverFirstName).getResultList();
	}

	/**
	 * select o.omsCustOrdNo from OmsCustOrdAddress o where
	 * o.deliverLastName=:deliverLastName
	 **/
	public List<BigDecimal> getOmsCustOrdAddressFindByDeliverLastName(String deliverLastName) {
		return em.createNamedQuery("OmsCustOrdAddress.findByDeliverLastName").setParameter("deliverLastName", deliverLastName).getResultList();
	}

	/**
	 * select o.omsCustOrdNo from OmsCustOrdAddress o where
	 * o.deliverPhoneNo=:deliverPhoneNo
	 **/
	public List<BigDecimal> getOmsCustOrdAddressFindByDeliverPhoneNo(String deliverPhoneNo) {
		return em.createNamedQuery("OmsCustOrdAddress.findByDeliverPhoneNo").setParameter("deliverPhoneNo", deliverPhoneNo).getResultList();
	}

	/**
	 * select o.omsCustOrdNo from OmsCustOrdAddress o where
	 * o.deliverFirstName=:deliverFirstName and o.deliverLastName=:deliverLastName
	 **/
	public List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstNameAndLastName(String deliverFirstName, String deliverLastName) {
		return em.createNamedQuery("OmsCustOrdAddress.findByDeliverFirstNameAndLastName").setParameter("deliverFirstName", deliverFirstName).setParameter("deliverLastName", deliverLastName)
				.getResultList();
	}

	/**
	 * select o.omsCustOrdNo from OmsCustOrdAddress o where
	 * o.deliverFirstName=:deliverFirstName and o.deliverPhoneNo=:deliverPhoneNo
	 **/
	public List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstNameAndPhoneNo(String deliverFirstName, String deliverPhoneNo) {
		return em.createNamedQuery("OmsCustOrdAddress.findByDeliverFirstNameAndPhoneNo").setParameter("deliverFirstName", deliverFirstName).setParameter("deliverPhoneNo", deliverPhoneNo)
				.getResultList();
	}

	/**
	 * select o.omsCustOrdNo from OmsCustOrdAddress o where
	 * o.deliverLastName=:deliverLastName and o.deliverPhoneNo=:deliverPhoneNo
	 **/
	public List<BigDecimal> getOmsCustOrdAddressFindByDeliverLastNameAndPhoneNo(String deliverLastName, String deliverPhoneNo) {
		return em.createNamedQuery("OmsCustOrdAddress.findByDeliverLastNameAndPhoneNo").setParameter("deliverLastName", deliverLastName).setParameter("deliverPhoneNo", deliverPhoneNo).getResultList();
	}

	/**
	 * select o.omsCustOrdNo from OmsCustOrdAddress o where
	 * o.deliverFirstName=:deliverFirstName and o.deliverLastName=:deliverLastName
	 * and o.deliverPhoneNo=:deliverPhoneNo
	 **/
	public List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstNameLastNameAndPhoneNo(String deliverFirstName, String deliverLastName, String deliverPhoneNo) {
		return em.createNamedQuery("OmsCustOrdAddress.findByDeliverFirstNameLastNameAndPhoneNo").setParameter("deliverFirstName", deliverFirstName).setParameter("deliverLastName", deliverLastName)
				.setParameter("deliverPhoneNo", deliverPhoneNo).getResultList();
	}

	public OmsFulfillMatrixExtDetail persistOmsFulfillMatrixExtDetail(OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail) {
		em.persist(omsFulfillMatrixExtDetail);
		return omsFulfillMatrixExtDetail;
	}

	public OmsFulfillMatrixExtDetail mergeOmsFulfillMatrixExtDetail(OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail) {
		return em.merge(omsFulfillMatrixExtDetail);
	}

	public void removeOmsFulfillMatrixExtDetail(OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail) {
		omsFulfillMatrixExtDetail = em.find(OmsFulfillMatrixExtDetail.class, new OmsFulfillMatrixExtDetailPK(omsFulfillMatrixExtDetail.getCombinationId(), omsFulfillMatrixExtDetail.getPriority()));
		em.remove(omsFulfillMatrixExtDetail);
	}

	/** <code>select o from OmsFulfillMatrixExtDetail o</code> */
	public List<OmsFulfillMatrixExtDetail> getOmsFulfillMatrixExtDetailFindAll() {
		return em.createNamedQuery("OmsFulfillMatrixExtDetail.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsFulfillMatrixExtDetail o where o.combinationId=:combID and o.priority =:priority</code>
	 */
	public OmsFulfillMatrixExtDetail getOmsFulfillMatrixExtDetailFindDetail(BigDecimal combID, BigDecimal priority) {
		return (OmsFulfillMatrixExtDetail) em.createNamedQuery("OmsFulfillMatrixExtDetail.findDetail").setParameter("combID", combID).setParameter("priority", priority).getSingleResult();
	}

	/**
	 * <code>select o from OmsFulfillMatrixExtDetail o where o.combinationId=:combID and o.location =:location</code>
	 */
	public OmsFulfillMatrixExtDetail getOmsFulfillMatrixExtDetailFindPriority(BigDecimal combID, BigDecimal location) {
		return (OmsFulfillMatrixExtDetail) em.createNamedQuery("OmsFulfillMatrixExtDetail.findPriority").setParameter("combID", combID).setParameter("location", location).getSingleResult();
	}

	/**
	 * <code>select o from OmsFulfillMatrixExtDetail o where o.combinationId=:combID and o.location =:location</code>
	 */
	public List<OmsFulfillMatrixExtDetail> getOmsFulfillMatrixExtDetailFindByCombId(BigDecimal combID) {
		return em.createNamedQuery("OmsFulfillMatrixExtDetail.findByCombId").setParameter("combID", combID).getResultList();
	}

	public OmsCustOrdItem persistOmsCustOrdItem(OmsCustOrdItem omsCustOrdItem) {
		em.persist(omsCustOrdItem);
		return omsCustOrdItem;
	}

	public OmsCustOrdItem mergeOmsCustOrdItem(OmsCustOrdItem omsCustOrdItem) {
		return em.merge(omsCustOrdItem);
	}

	public void removeOmsCustOrdItem(OmsCustOrdItem omsCustOrdItem) {
		omsCustOrdItem = em.find(OmsCustOrdItem.class, new OmsCustOrdItemPK(omsCustOrdItem.getItem(), omsCustOrdItem.getLineNo(), omsCustOrdItem.getOmsCustOrdNo()));
		em.remove(omsCustOrdItem);
	}

	/** <code>select o from OmsCustOrdItem o</code> */
	public List<OmsCustOrdItem> getOmsCustOrdItemFindAll() {
		return em.createNamedQuery("OmsCustOrdItem.findAll").getResultList();
	}

	/**
	 * <code>select sum(o.unitRetail) from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 * 
	 * @param omsCustOrdNo
	 * @return
	 */
	public BigDecimal getOmsCustOrdItemSumUnitRetail(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdItem.findSumOfUnitRetailPrice").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item</code>
	 */
	public OmsCustOrdItem getOmsCustOrdItemFindByItem(BigDecimal omsCustOrdNo, String item, BigDecimal lineNo) {
		return (OmsCustOrdItem) em.createNamedQuery("OmsCustOrdItem.findByItem ").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("item", item).setParameter("lineNo", lineNo)
				.getSingleResult();
	}

	public List<OmsCustOrdItem> getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(BigDecimal omsCustOrdNo, BigDecimal lineNo) {
		return em.createNamedQuery("OmsCustOrdItem.findByOmsCustOrdNoAndLineNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo).getResultList();
	}

	public BigDecimal getOmsCustOrdItemFindSumOfQtysForCloseDateTime(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdItem.calculateSumOfQtysForCloseDateTime").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustNumber</code>
	 */
	public List<OmsCustOrdItem> getOmsCustOrdItemFindByCustOrdNo(BigDecimal omsCustNumber) {
		return em.createNamedQuery("OmsCustOrdItem.getCustOrdNo").setParameter("omsCustNumber", omsCustNumber).getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustNumber</code>
	 */
	/*
	 * public List<OmsCustOrdItem> getOmsCustOrdItemFindItemsByDept(BigDecimal dept)
	 * { return
	 * em.createNamedQuery("OmsCustOrdItem.findItemsByDept").setParameter("dept",
	 * dept).getResultList(); }
	 */

	/**
	 * <code>select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.lineLinkNo=:lineLinkNo</code>
	 */
	public List<OmsCustOrdItem> getOmsCustOrdItemFindByLinkLineNo(BigDecimal omsCustOrdNo, BigDecimal lineLinkNo) {
		return em.createNamedQuery("OmsCustOrdItem.getOmsCustOrdItemFindByLinkLineNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineLinkNo", lineLinkNo).getResultList();
	}

	public OmsCustOrdReserve persistOmsCustOrdReserve(OmsCustOrdReserve omsCustOrdReserve) {
		em.persist(omsCustOrdReserve);
		return omsCustOrdReserve;
	}

	public OmsCustOrdReserve mergeOmsCustOrdReserve(OmsCustOrdReserve omsCustOrdReserve) {
		return em.merge(omsCustOrdReserve);
	}

	public void removeOmsCustOrdReserve(OmsCustOrdReserve omsCustOrdReserve) {
		omsCustOrdReserve = em.find(OmsCustOrdReserve.class,
				new OmsCustOrdReservePK(omsCustOrdReserve.getItem(), omsCustOrdReserve.getLoc(), omsCustOrdReserve.getOmsCustOrdNo(), omsCustOrdReserve.getLineNo()));
		em.remove(omsCustOrdReserve);
	}

	/** <code>select o from OmsCustOrdReserve o</code> */
	public List<OmsCustOrdReserve> getOmsCustOrdReserveFindAll() {
		return em.createNamedQuery("OmsCustOrdReserve.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdReserve o where o.omsCustOrdNo=:omsCustOrdNo </code>
	 */
	public List<OmsCustOrdReserve> getOmsCustOrdReserveFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCustOrdReserve.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdReserve o where o.omsCustOrdNo=:omsCustOrdNo </code>
	 */
	public List<OmsCustOrdReserve> getOmsCustOrdReserveFindByFulOrdNo(BigDecimal omsCustOrdNo, BigDecimal fulfillOrderNo) {
		return em.createNamedQuery("OmsCustOrdReserve.findByFulOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("fulfillOrderNo", fulfillOrderNo).getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdSadad o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item</code>
	 */
	public List<OmsCustOrdReserve> getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(BigDecimal omsCustOrdNo, String item, BigDecimal lineNo) {
		return em.createNamedQuery("OmsCustOrdReserve.findByOmsCustOrdNoAndItem").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("item", item).setParameter("lineNo", lineNo).getResultList();
	}

	public OmsCustOrdReserve getOmsCustOrdReserveFindByOmsCustOrdNoAndItemAndResvLoc(BigDecimal omsCustOrdNo, String item, BigDecimal lineNo, BigDecimal rmsResvLoc) {
		return (OmsCustOrdReserve) em.createNamedQuery("OmsCustOrdReserve.findByOmsCustOrdNoAndItemAndResvLoc").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("item", item)
				.setParameter("lineNo", lineNo).setParameter("rmsResvLoc", rmsResvLoc).getSingleResult();
	}

	/**
	 * <code>select o from OmsCustOrdHead o where  o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public OmsCustOrdHead getOmsCustOrdHeadFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return (OmsCustOrdHead) em.createNamedQuery("OmsCustOrdHead.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o from OmsCustOrdTender o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public List<OmsCustOrdTender> getOmsCustOrdTenderFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCustOrdTender.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select max(o.tenderAmt) from OmsCustOrdTender o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 * 
	 * @param omsCustOrdNo
	 * @return
	 */
	public BigDecimal getOmsCustOrdTenderSumOfTenderAmt(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdTender.findSumOfTenderAmount").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	public BigDecimal getOmsCustOrdTenderFindMaxTenderSeqNo(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdTender.findMaxTenderSeqNoByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/** select o.omsCustOrdNo from OmsCustOrdTender o where o.ccNo=:ccNo **/
	public List<BigDecimal> getOmsCustOrdTenderFindByccNo(String ccNo) {
		return em.createNamedQuery("OmsCustOrdTender.findByccNo").setParameter("ccNo", ccNo).getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdAddress o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public OmsCustOrdAddress getOmsCustOrdAddressFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return (OmsCustOrdAddress) em.createNamedQuery("OmsCustOrdAddress.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o from OmsTempCoFo o where o.omsCustOrdNo=:custNo  and o.status=:status </code>
	 */
	public List<OmsTempCoFo> getOmsTempCoFoFindByStatus(BigDecimal custNo, String status) {
		return em.createNamedQuery("OmsTempCoFo.findByStatus").setParameter("custNo", custNo).setParameter("status", status).getResultList();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdItem o where o.backorderInd=:backorderInd </code>
	 */
	public List<BigDecimal> getOmsCustOrdItemFindOmsCustOrdNoByBOInd(String backorderInd) {
		return em.createNamedQuery("OmsCustOrdItem.findOmsCustOrdNoByBOInd ").setParameter("backorderInd", backorderInd).getResultList();
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdItem o where o.backorderInd=:backorderInd and o.status<>'SUCCESS'  </code>
	 */
	public List<BigDecimal> getOmsCustOrdItemFindBackOrders(String backorderInd, Timestamp backorderDlyDate) {
		return em.createNamedQuery("OmsCustOrdItem.findBackOrders ").setParameter("backorderInd", backorderInd).setParameter("backorderDlyDate", backorderDlyDate).getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo </code>
	 */
	public List<OmsCustOrdItem> getOmsCustOrdItemFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCustOrdItem.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.backorderInd=:backorderInd  </code>
	 */
	public List<OmsCustOrdItem> getOmsCustOrdItemFindByBOIndAndOmsCustNo(BigDecimal omsCustOrdNo, String backorderInd) {
		return em.createNamedQuery("OmsCustOrdItem.findByBOIndAndOmsCustNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("backorderInd", backorderInd).getResultList();
	}

	public List<BigDecimal> getOmsCustOrdItemFindByItemAndLastUpdatedDateTime(String item, Timestamp startTime, Timestamp endTime, BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCustOrdItem.findByItemAndLastUpdatedDateTime").setParameter("item", item).setParameter("startTime", startTime).setParameter("endTime", endTime)
				.setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * select o from OmsCustOrdTender o where o.omsCustOrdNo=:omsCustOrdNo and
	 * o.tenderTypeGroup=:tenderTypeGroup
	 */
	public OmsCustOrdTender getOmsCustOrdTenderFindByTenderType(BigDecimal omsCustOrdNo, String tenderTypeGroup) {
		Query query = em.createNamedQuery("OmsCustOrdTender.findByTenderType");
		query.setMaxResults(1);
		List<OmsCustOrdTender> results = query.setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("tenderTypeGroup", tenderTypeGroup).getResultList();
		if (!(results == null || results.isEmpty())) {
			return results.get(0);
		}
		return null;
	}

	public List<BigDecimal> getOmsCustOrdTenderFindByTenderRefId(String tenderRefId) {
		return em.createNamedQuery("OmsCustOrdTender.findByTenderRefId").setParameter("tenderRefId", tenderRefId).getResultList();
	}

	public OmsOrdItemTenderSplit persistOmsOrdItemTenderSplit(OmsOrdItemTenderSplit omsOrdItemTenderSplit) {
		em.persist(omsOrdItemTenderSplit);
		return omsOrdItemTenderSplit;
	}

	public OmsOrdItemTenderSplit mergeOmsOrdItemTenderSplit(OmsOrdItemTenderSplit omsOrdItemTenderSplit) {
		return em.merge(omsOrdItemTenderSplit);
	}

	public void removeOmsOrdItemTenderSplit(OmsOrdItemTenderSplit omsOrdItemTenderSplit) {
		omsOrdItemTenderSplit = em.find(OmsOrdItemTenderSplit.class,
				new OmsOrdItemTenderSplitPK(omsOrdItemTenderSplit.getItem(), omsOrdItemTenderSplit.getOmsCustOrdNo(), omsOrdItemTenderSplit.getTenderSeqNo(), omsOrdItemTenderSplit.getLineNo()));
		em.remove(omsOrdItemTenderSplit);
	}

	/** <code>select o from OmsOrdItemTenderSplit o</code> */
	public List<OmsOrdItemTenderSplit> getOmsOrdItemTenderSplitFindAll() {
		return em.createNamedQuery("OmsOrdItemTenderSplit.findAll").getResultList();
	}

	/**
	 * <code>select o.reasonId from OmsInvAdjReasonCode o where o.serviceRequestType=:serviceReqTyp </code>
	 */
	public BigDecimal getOmsInvAdjFindByServiceReqType(String serviceReqTyp) {
		return (BigDecimal) em.createNamedQuery("OmsInvAdj.findByServiceReqType").setParameter("serviceReqTyp", serviceReqTyp).getSingleResult();
	}

	public OmsInvAdjItem persistOmsInvAdjItem(OmsInvAdjItem omsInvAdjItem) {
		em.persist(omsInvAdjItem);
		return omsInvAdjItem;
	}

	public OmsInvAdjItem mergeOmsInvAdjItem(OmsInvAdjItem omsInvAdjItem) {
		return em.merge(omsInvAdjItem);
	}

	public void removeOmsInvAdjItem(OmsInvAdjItem omsInvAdjItem) {
		omsInvAdjItem = em.find(OmsInvAdjItem.class, new OmsInvAdjItemPK(omsInvAdjItem.getItem(), omsInvAdjItem.getOmsAdjReqId()));
		em.remove(omsInvAdjItem);
	}

	/** <code>select o from OmsInvAdjItem o</code> */
	public List<OmsInvAdjItem> getOmsInvAdjItemFindAll() {
		return em.createNamedQuery("OmsInvAdjItem.findAll").getResultList();
	}

	public OmsInvAdj persistOmsInvAdj(OmsInvAdj omsInvAdj) {
		em.persist(omsInvAdj);
		return omsInvAdj;
	}

	public OmsInvAdj mergeOmsInvAdj(OmsInvAdj omsInvAdj) {
		return em.merge(omsInvAdj);
	}

	public void removeOmsInvAdj(OmsInvAdj omsInvAdj) {
		omsInvAdj = em.find(OmsInvAdj.class, omsInvAdj.getOmsAdjReqId());
		em.remove(omsInvAdj);
	}

	/** <code>select o from OmsInvAdj o</code> */
	public List<OmsInvAdj> getOmsInvAdjFindAll() {
		return em.createNamedQuery("OmsInvAdj.findAll").getResultList();
	}

	/**
	 * <code>select o.omsAdjReqId from OmsInvAdj o where o.adjRequestId=:reqId and o.applicationId=:appId</code>
	 */
	public List<BigDecimal> getOmsInvAdjFindOmsAdjReqId(String reqId, String appId) {
		return em.createNamedQuery("OmsInvAdj.findOmsAdjReqId").setParameter("reqId", reqId).setParameter("appId", appId).getResultList();
	}

	/** <code>select o from OmsInvAdj o where o.omsAdjReqId=:reqId </code> */
	public OmsInvAdj getOmsInvAdjFindByOmsAdjReqId(BigDecimal reqId) {
		return (OmsInvAdj) em.createNamedQuery("OmsInvAdj.findByOmsAdjReqId").setParameter("reqId", reqId).getSingleResult();
	}

	/** <code>select o from OmsInvAdjItem o where o.omsAdjReqId=:reqId </code> */
	public OmsInvAdjItem getOmsInvAdjItemFindByOmsAdjReqId(BigDecimal reqId) {
		return (OmsInvAdjItem) em.createNamedQuery("OmsInvAdjItem.findByOmsAdjReqId").setParameter("reqId", reqId).getSingleResult();
	}

	public OmsStsTrf persistOmsStsTrf(OmsStsTrf omsStsTrf) {
		em.persist(omsStsTrf);
		return omsStsTrf;
	}

	public OmsStsTrf mergeOmsStsTrf(OmsStsTrf omsStsTrf) {
		return em.merge(omsStsTrf);
	}

	public void removeOmsStsTrf(OmsStsTrf omsStsTrf) {
		omsStsTrf = em.find(OmsStsTrf.class, omsStsTrf.getOmsTrfReqId());
		em.remove(omsStsTrf);
	}

	/** <code>select o from OmsStsTrf o</code> */
	public List<OmsStsTrf> getOmsStsTrfFindAll() {
		return em.createNamedQuery("OmsStsTrf.findAll").getResultList();
	}

	/**
	 * <code>select o.omsTrfReqId from OmsStsTrf o where o.applicationId=:appId and o.transferRequestId=:transID</code>
	 */
	public List<BigDecimal> getOmsStsTrfFindByTransReqId(String appId, String transID) {
		return em.createNamedQuery("OmsStsTrf.findByTransReqId").setParameter("appId", appId).setParameter("transID", transID).getResultList();
	}

	/** <code>select o from OmsStsTrf o where o.omsTrfReqId=:reqId </code> */
	public OmsStsTrf getOmsStsTrfFindByOmsTrfReqId(BigDecimal reqId) {
		return (OmsStsTrf) em.createNamedQuery("OmsStsTrf.findByOmsTrfReqId").setParameter("reqId", reqId).getSingleResult();
	}

	public OmsStsTrfItem persistOmsStsTrfItem(OmsStsTrfItem omsStsTrfItem) {
		em.persist(omsStsTrfItem);
		return omsStsTrfItem;
	}

	public OmsStsTrfItem mergeOmsStsTrfItem(OmsStsTrfItem omsStsTrfItem) {
		return em.merge(omsStsTrfItem);
	}

	public void removeOmsStsTrfItem(OmsStsTrfItem omsStsTrfItem) {
		omsStsTrfItem = em.find(OmsStsTrfItem.class, new OmsStsTrfItemPK(omsStsTrfItem.getItem(), omsStsTrfItem.getOmsTrfReqId()));
		em.remove(omsStsTrfItem);
	}

	/** <code>select o from OmsStsTrfItem o</code> */
	public List<OmsStsTrfItem> getOmsStsTrfItemFindAll() {
		return em.createNamedQuery("OmsStsTrfItem.findAll").getResultList();
	}

	/** <code>select o from OmsStsTrfItem o where o.omsTrfReqId=:reqId </code> */
	public OmsStsTrfItem getOmsStsTrfItemFindByOmsTrfReqId(BigDecimal reqId) {
		return (OmsStsTrfItem) em.createNamedQuery("OmsStsTrfItem.findByOmsTrfReqId").setParameter("reqId", reqId).getSingleResult();
	}

	public OmsCoFoCancel persistOmsCoFoCancel(OmsCoFoCancel omsCoFoCancel) {
		em.persist(omsCoFoCancel);
		return omsCoFoCancel;
	}

	public OmsCoFoCancel mergeOmsCoFoCancel(OmsCoFoCancel omsCoFoCancel) {
		return em.merge(omsCoFoCancel);
	}

	public void removeOmsCoFoCancel(OmsCoFoCancel omsCoFoCancel) {
		omsCoFoCancel = em.find(OmsCoFoCancel.class, new OmsCoFoCancelPK(omsCoFoCancel.getFulfillOrderNo(), omsCoFoCancel.getItem(), omsCoFoCancel.getLineNo(), omsCoFoCancel.getOmsCancelId()));
		em.remove(omsCoFoCancel);
	}

	/** <code>select o from OmsCoFoCancel o</code> */
	public List<OmsCoFoCancel> getOmsCoFoCancelFindAll() {
		return em.createNamedQuery("OmsCoFoCancel.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCoFoCancel o  o where o.omsCancelId=:omsCancelId</code>
	 */
	public List<OmsCoFoCancel> getOmsCoFoCancelFindByOmsCancelId(BigDecimal omsCancelId) {
		return em.createNamedQuery("OmsCoFoCancel.findByOmsCancelId").setParameter("omsCancelId", omsCancelId).getResultList();

	}

	public OmsCustOrdLog persistOmsCustOrdLog(OmsCustOrdLog omsCustOrdLog) {
		em.persist(omsCustOrdLog);
		return omsCustOrdLog;
	}

	public OmsCustOrdLog mergeOmsCustOrdLog(OmsCustOrdLog omsCustOrdLog) {
		return em.merge(omsCustOrdLog);
	}

	public void removeOmsCustOrdLog(OmsCustOrdLog omsCustOrdLog) {
		omsCustOrdLog = em.find(OmsCustOrdLog.class, omsCustOrdLog.getLogSeqNo());
		em.remove(omsCustOrdLog);
	}

	/** <code>select o from OmsCustOrdLog o</code> */
	public List<OmsCustOrdLog> getOmsCustOrdLogFindAll() {
		return em.createNamedQuery("OmsCustOrdLog.findAll").getResultList();
	}

	/**
	 * <code>select o.omsCancelId from OmsCustOrdLog o where o.omsCancelId=:omsCancelId</code>
	 */
	public List<BigDecimal> getOmsCustOrdLogFindOmsCancelId(BigDecimal omsCancelId) {
		return em.createNamedQuery("OmsCustOrdLog.findOmsCancelId").setParameter("omsCancelId", omsCancelId).getResultList();
	}

	/**
	 * <code>select max(o.logSeqNo) from OmsCustOrdLog o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 * 
	 * @return
	 */
	public BigDecimal getOmsCustOrdLogFindMaxLogSeqNo(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdLog.findMaxLogSeqNo").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	/**
	 * <code>select o.logSeqNo from OmsCustOrdLog o where o.omsCancelId=:omsCancelId</code>
	 */
	public BigDecimal getOmsCustOrdLogFindlogSeqNo(BigDecimal omsCancelId) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdLog.findlogSeqNo").setParameter("omsCancelId", omsCancelId).getSingleResult();
	}

	public OmsCoCancelItem persistOmsCoCancelItem(OmsCoCancelItem omsCoCancelItem) {
		em.persist(omsCoCancelItem);
		return omsCoCancelItem;
	}

	public OmsCoCancelItem mergeOmsCoCancelItem(OmsCoCancelItem omsCoCancelItem) {
		return em.merge(omsCoCancelItem);
	}

	public void removeOmsCoCancelItem(OmsCoCancelItem omsCoCancelItem) {

	}

	/** <code>select o from OmsCoCancelItem o</code> */
	public List<OmsCoCancelItem> getOmsCoCancelItemFindAll() {
		return em.createNamedQuery("OmsCoCancelItem.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCoCancelItem o where o.omsCancelId=:omsCancelId  </code>
	 */
	public List<OmsCoCancelItem> getOmsCoCancelItemFindByOmsCancelId(BigDecimal omsCancelId) {
		return em.createNamedQuery("OmsCoCancelItem.findByOmsCancelId").setParameter("omsCancelId", omsCancelId).getResultList();
	}

	/**
	 * <code>select o from OmsCoCancelItem o where o.omsCancelId=:omsCancelId and o.item=:inputItem</code>
	 */
	public List<OmsCoCancelItem> getOmsCoCancelItemGetCustItemList(BigDecimal omsCancelId, String inputItem) {
		return em.createNamedQuery("OmsCoCancelItem.getCustItemList").setParameter("omsCancelId", omsCancelId).setParameter("inputItem", inputItem).getResultList();
	}

	public OmsCoCancelHead persistOmsCoCancelHead(OmsCoCancelHead omsCoCancelHead) {
		em.persist(omsCoCancelHead);
		return omsCoCancelHead;
	}

	public OmsCoCancelHead mergeOmsCoCancelHead(OmsCoCancelHead omsCoCancelHead) {
		return em.merge(omsCoCancelHead);
	}

	public void removeOmsCoCancelHead(OmsCoCancelHead omsCoCancelHead) {

	}

	/** <code>select o from OmsCoCancelHead o</code> */
	public List<OmsCoCancelHead> getOmsCoCancelHeadFindAll() {
		return em.createNamedQuery("OmsCoCancelHead.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCoCancelHead o where o.custOrdNo=:custOrdNo</code>
	 * 
	 * @param custOrdNo
	 * @return
	 */
	public List<OmsCoCancelHead> getOmsCoCancelHeadFindByCustOrdNo(String custOrdNo) {
		return em.createNamedQuery("OmsCoCancelHead.findByCustOrdNo").setParameter("custOrdNo", custOrdNo).getResultList();
	}

	/**
	 * <code>select o.omsCancelId from OmsCoCancelHead o where o.omsCustOrdNo=:OmscustOrdNumb and o.cancelReqId=:cancelReqId and o.applicationId=:applicationId </code>
	 */
	public List<BigDecimal> getOmsCoCancelHeadFindOmsCancelId(String custOrdNo, String subCustOrdNo, BigDecimal cancelReqId) {
		return em.createNamedQuery("OmsCoCancelHead.findOmsCancelId").setParameter("custOrdNo", custOrdNo).setParameter("subCustOrdNo", subCustOrdNo).setParameter("cancelReqId", cancelReqId)
				.getResultList();
	}

	public OmsCustOrdLogItem persistOmsCustOrdLogItem(OmsCustOrdLogItem omsCustOrdLogItem) {
		em.persist(omsCustOrdLogItem);
		return omsCustOrdLogItem;
	}

	public OmsCustOrdLogItem mergeOmsCustOrdLogItem(OmsCustOrdLogItem omsCustOrdLogItem) {
		return em.merge(omsCustOrdLogItem);
	}

	public void removeOmsCustOrdLogItem(OmsCustOrdLogItem omsCustOrdLogItem) {
		omsCustOrdLogItem = em.find(OmsCustOrdLogItem.class, new OmsCustOrdLogItemPK(omsCustOrdLogItem.getItem(), omsCustOrdLogItem.getLogSeqNo()));
		em.remove(omsCustOrdLogItem);
	}

	/** <code>select o from OmsCustOrdLogItem o</code> */
	public List<OmsCustOrdLogItem> getOmsCustOrdLogItemFindAll() {
		return em.createNamedQuery("OmsCustOrdLogItem.findAll").getResultList();
	}

	public BigDecimal getOmsCustOrdLogLineItemandLogSeqNo(BigDecimal lineNo, String item, BigDecimal logSeqNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdLogLineItem.findItemandLogSeqNo").setParameter("lineNo", lineNo).setParameter("item", item).setParameter("logSeqNo", logSeqNo)
				.getSingleResult();
	}

	public OmsCoFoCancelTemp persistOmsCoFoCancelTemp(OmsCoFoCancelTemp omsCoFoCancelTemp) {
		em.persist(omsCoFoCancelTemp);
		return omsCoFoCancelTemp;
	}

	public OmsCoFoCancelTemp mergeOmsCoFoCancelTemp(OmsCoFoCancelTemp omsCoFoCancelTemp) {
		return em.merge(omsCoFoCancelTemp);
	}

	public void removeOmsCoFoCancelTemp(OmsCoFoCancelTemp omsCoFoCancelTemp) {
		omsCoFoCancelTemp = em.find(OmsCoFoCancelTemp.class, new OmsCoFoCancelTempPK(omsCoFoCancelTemp.getFulfillOrderNo(), omsCoFoCancelTemp.getItem(), omsCoFoCancelTemp.getOmsCancelId()));
		em.remove(omsCoFoCancelTemp);
	}

	/** <code>select o from OmsCoFoCancelTemp o</code> */
	public List<OmsCoFoCancelTemp> getOmsCoFoCancelTempFindAll() {
		return em.createNamedQuery("OmsCoFoCancelTemp.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCoFoCancelTemp o where o.omsCancelId=:omsCancelId and o.fulfillOrderNo=:fulfillOrderNo and o.wsResponse=:wsResponse</code>
	 */
	public List<OmsCoFoCancelTemp> getOmsCoFoCancelTempFindByCancelId(BigDecimal omsCancelId, BigDecimal fulfillOrderNo, String wsResponse) {
		return em.createNamedQuery("OmsCoFoCancelTemp.findByCancelId").setParameter("omsCancelId", omsCancelId).setParameter("fulfillOrderNo", fulfillOrderNo).setParameter("wsResponse", wsResponse)
				.getResultList();
	}

	public OmsCustCancelTender persistOmsCustCancelTender(OmsCustCancelTender omsCustCancelTender) {
		em.persist(omsCustCancelTender);
		return omsCustCancelTender;
	}

	public OmsCustCancelTender mergeOmsCustCancelTender(OmsCustCancelTender omsCustCancelTender) {
		return em.merge(omsCustCancelTender);
	}

	public void removeOmsCustCancelTender(OmsCustCancelTender omsCustCancelTender) {
		omsCustCancelTender = em.find(OmsCustCancelTender.class, new OmsCustCancelTenderPK(omsCustCancelTender.getOmsCancelId(), omsCustCancelTender.getTenderSeqNo()));
		em.remove(omsCustCancelTender);
	}

	/** <code>select o from OmsCustCancelTender o</code> */
	public List<OmsCustCancelTender> getOmsCustCancelTenderFindAll() {
		return em.createNamedQuery("OmsCustCancelTender.findAll").getResultList();
	}

	public OmsRevsPick persistOmsRevsPick(OmsRevsPick omsRevsPick) {
		em.persist(omsRevsPick);
		return omsRevsPick;
	}

	public OmsRevsPick mergeOmsRevsPick(OmsRevsPick omsRevsPick) {
		return em.merge(omsRevsPick);
	}

	public void removeOmsRevsPick(OmsRevsPick omsRevsPick) {
		omsRevsPick = em.find(OmsRevsPick.class, omsRevsPick.getOmsCustOrdNo());
		em.remove(omsRevsPick);
	}

	/** <code>select o from OmsRevsPick o</code> */
	public List<OmsRevsPick> getOmsRevsPickFindAll() {
		return em.createNamedQuery("OmsRevsPick.findAll").getResultList();
	}

	public OmsCoSasInvAdj persistOmsCoSasInvAdj(OmsCoSasInvAdj omsCoSasInvAdj) {
		em.persist(omsCoSasInvAdj);
		return omsCoSasInvAdj;
	}

	public OmsCoSasInvAdj mergeOmsCoSasInvAdj(OmsCoSasInvAdj omsCoSasInvAdj) {
		return em.merge(omsCoSasInvAdj);
	}

	public void removeOmsCoSasInvAdj(OmsCoSasInvAdj omsCoSasInvAdj) {
		omsCoSasInvAdj = em.find(OmsCoSasInvAdj.class, new OmsCoSasInvAdjPK(omsCoSasInvAdj.getCustOrderNo(), omsCoSasInvAdj.getItem(), omsCoSasInvAdj.getLineItemNo(), omsCoSasInvAdj.getLocationId(),
				omsCoSasInvAdj.getOmsCustOrdNo(), omsCoSasInvAdj.getSubCustOrderNo()));
		em.remove(omsCoSasInvAdj);
	}

	/** <code>select o from OmsCoSasInvAdj o</code> */
	public List<OmsCoSasInvAdj> getOmsCoSasInvAdjFindAll() {
		return em.createNamedQuery("OmsCoSasInvAdj.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo</code>
	 */
	public List<OmsCoSasInvAdj> getOmsCoSasInvAdjFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsCoSasInvAdj.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	@Override
	public String getItemMasterFindStandardUom(String item) {

		return (String) em.createNamedQuery("ItemMaster.findStandardUom").setParameter("item", item).getSingleResult();

	}

	@Override
	public String getItemMasterFindItemStatus(String item) {

		return (String) em.createNamedQuery("ItemMaster.findItemStatus").setParameter("item", item).getSingleResult();

	}

	@Override
	public BigDecimal getItemMasterFindDept(String item) {

		return (BigDecimal) em.createNamedQuery("ItemMaster.findDept").setParameter("item", item).getSingleResult();

	}

	public String getItemMasterFindItemDesc(String item) {

		return (String) em.createNamedQuery("ItemMaster.findItemDesc").setParameter("item", item).getSingleResult();

	}

	public OmsWebserviceUriDetail persistOmsWebserviceUriDetail(OmsWebserviceUriDetail omsWebserviceUriDetail) {
		em.persist(omsWebserviceUriDetail);
		return omsWebserviceUriDetail;
	}

	public OmsWebserviceUriDetail mergeOmsWebserviceUriDetail(OmsWebserviceUriDetail omsWebserviceUriDetail) {
		return em.merge(omsWebserviceUriDetail);
	}

	public void removeOmsWebserviceUriDetail(OmsWebserviceUriDetail omsWebserviceUriDetail) {
		omsWebserviceUriDetail = em.find(OmsWebserviceUriDetail.class, omsWebserviceUriDetail.getWebServiceId());
		em.remove(omsWebserviceUriDetail);
	}

	/** <code>select o from OmsWebserviceUriDetail o</code> */
	public List<OmsWebserviceUriDetail> getOmsWebserviceUriDetailFindAll() {
		return em.createNamedQuery("OmsWebserviceUriDetail.findAll").getResultList();
	}

	@Override
	public String getOmsWebserviceUriDetailFindByWebserviceName(String webServiceName) {
		return (String) em.createNamedQuery("OmsWebserviceUriDetail.findByWebserviceName").setParameter("webServiceName", webServiceName).getSingleResult();
	}

	@Override
	public BigDecimal getOmsWebserviceUriDetailFindWebServiceId(String webServiceName) {
		return (BigDecimal) em.createNamedQuery("OmsWebserviceUriDetail.findWebServiceId").setParameter("webServiceName", webServiceName).getSingleResult();
	}

	public List<BigDecimal> getWhFindByPhysicalWH(BigDecimal physicalWH) {
		return em.createNamedQuery("Wh.findByPhysicalWH").setParameter("physicalWH", physicalWH).getResultList();
	}

	public List<Object[]> getWhFindPhysicalWH(BigDecimal wh) {
		return em.createNamedQuery("Wh.findPhysicalWH").setParameter("wh", wh).getResultList();
	}

	public BigDecimal getWhFindPhyWhForVirtualWh(BigDecimal wh) {
		return (BigDecimal) em.createNamedQuery("Wh.findPhyWhForVirtualWh").setParameter("wh", wh).getSingleResult();
	}

	public List<BigDecimal> getWhFindVirtualWh(BigDecimal physicalWH, BigDecimal channelId) {
		return em.createNamedQuery("Wh.findVirtualWh").setParameter("physicalWH", physicalWH).setParameter("channelId", channelId).getResultList();
	}

	public OmsRmaReqItem persistOmsRmaReqItem(OmsRmaReqItem omsRmaReqItem) {
		em.persist(omsRmaReqItem);
		return omsRmaReqItem;
	}

	public OmsRmaReqItem mergeOmsRmaReqItem(OmsRmaReqItem omsRmaReqItem) {
		return em.merge(omsRmaReqItem);
	}

	public void removeOmsRmaReqItem(OmsRmaReqItem omsRmaReqItem) {
		omsRmaReqItem = em.find(OmsRmaReqItem.class, new OmsRmaReqItemPK(omsRmaReqItem.getItem(), omsRmaReqItem.getRmaId()));
		em.remove(omsRmaReqItem);
	}

	/** <code>select o from OmsRmaReqItem o</code> */
	public List<OmsRmaReqItem> getOmsRmaReqItemFindAll() {
		return em.createNamedQuery("OmsRmaReqItem.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsRmaReqItem o where o.rmaId=:rmaId and o.item=:item and o.lineNo=:lineNo</code>
	 */
	public OmsRmaReqItem getOmsRmaReqItemFindByRmaIdAndItem(BigDecimal rmaId, String item, BigDecimal lineNo) {
		return (OmsRmaReqItem) em.createNamedQuery("OmsRmaReqItem.findByRmaIdAndItem").setParameter("rmaId", rmaId).setParameter("item", item).setParameter("lineNo", lineNo).getSingleResult();
	}

	public List<OmsRmaReqItem> getOmsRmaReqItemFindByRmaId(BigDecimal rmaId) {
		return em.createNamedQuery("OmsRmaReqItem.findByRmaId").setParameter("rmaId", rmaId).getResultList();
	}

	public OmsRmaReq persistOmsRmaReq(OmsRmaReq omsRmaReq) {
		em.persist(omsRmaReq);
		return omsRmaReq;
	}

	public OmsRmaReq mergeOmsRmaReq(OmsRmaReq omsRmaReq) {
		return em.merge(omsRmaReq);
	}

	public void removeOmsRmaReq(OmsRmaReq omsRmaReq) {
		omsRmaReq = em.find(OmsRmaReq.class, new OmsRmaReqPK(omsRmaReq.getOmsCustOrdNo(), omsRmaReq.getRmaReqId()));
		em.remove(omsRmaReq);
	}

	/** <code>select o from OmsRmaReq o</code> */
	public List<OmsRmaReq> getOmsRmaReqFindAll() {
		return em.createNamedQuery("OmsRmaReq.findAll").getResultList();
	}

	/**
	 * <code>select o.rmaId from OmsRmaReq o where o.omsCustOrdNo=:omsCustOrdNo and o.rmaReqId=:rmaReqId</code>
	 */
	public List<BigDecimal> getOmsRmaReqFindRmaId(BigDecimal omsCustOrdNo, String rmaReqId) {
		return em.createNamedQuery("OmsRmaReq.findRmaId").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("rmaReqId", rmaReqId).getResultList();
	}

	/**
	 * <code>select o from OmsRmaReq o where o.omsCustOrdNo=:omsCustOrdNo and o.rmaReqId=:rmaReqId</code>
	 */
	public OmsRmaReq getOmsRmaReqFindByOmsCustOrdNoAndRmaId(BigDecimal omsCustOrdNo, String rmaReqId) {
		return (OmsRmaReq) em.createNamedQuery("OmsRmaReq.findByOmsCustOrdNoAndRmaId").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("rmaReqId", rmaReqId).getSingleResult();
	}

	/**
	 * <code>select o from OmsRmaReq o where o.rmaId=:rmaId and o.rmaId=:rmaId</code>
	 */
	public OmsRmaReq getOmsRmaReqFindByRmaId(BigDecimal rmaId) {
		return (OmsRmaReq) em.createNamedQuery("OmsRmaReq.findByOmsRmaId").setParameter("rmaId", rmaId).getSingleResult();
	}

	/**
	 * <code>select o from OmsRmaReq o where o.rmaId=:rmaId and o.rmaId=:rmaId</code>
	 */
	public List<OmsRmaReq> getOmsRmaReqFindByOmscustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsRmaReq.findByOmscustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	/**
	 * <code>select o.cumQtyDelivered from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item and o.lineNo=:lineNo</code>
	 */
	public BigDecimal getOmsCustOrdItemFindDeilverdQuantity(BigDecimal omsCustOrdNo, String item, BigDecimal lineNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdItem.findDeilverdQuantity").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("item", item).setParameter("lineNo", lineNo)
				.getSingleResult();
	}

	/** <code>select o from VWh o</code> */
	public List<VWh> getVWhFindAll() {
		return em.createNamedQuery("VWh.findAll").getResultList();
	}

	/**
	 * <code>select o.physicalWh from VWh o where o.physicalWh=:physicalWh </code>
	 */
	public List<BigDecimal> getVWhFindPhyWH(Object physicalWh) {
		return em.createNamedQuery("VWh.findPhyWH").setParameter("physicalWh", physicalWh).getResultList();
	}

	public OmsErrorCodes persistOmsErrorCodes(OmsErrorCodes omsErrorCodes) {
		em.persist(omsErrorCodes);
		return omsErrorCodes;
	}

	public OmsCustUpdDelivInfo persistOmsCustUpdDelivInfo(OmsCustUpdDelivInfo omsCustUpdDelivInfo) {
		em.persist(omsCustUpdDelivInfo);
		return omsCustUpdDelivInfo;
	}

	public OmsErrorCodes mergeOmsErrorCodes(OmsErrorCodes omsErrorCodes) {
		return em.merge(omsErrorCodes);
	}

	public OmsCustUpdDelivInfo mergeOmsCustUpdDelivInfo(OmsCustUpdDelivInfo omsCustUpdDelivInfo) {
		return em.merge(omsCustUpdDelivInfo);
	}

	public void removeOmsErrorCodes(OmsErrorCodes omsErrorCodes) {
		omsErrorCodes = em.find(OmsErrorCodes.class, omsErrorCodes.getOmsErrorCode());
		em.remove(omsErrorCodes);
	}

	public void removeOmsCustUpdDelivInfo(OmsCustUpdDelivInfo omsCustUpdDelivInfo) {
		omsCustUpdDelivInfo = em.find(OmsCustUpdDelivInfo.class, omsCustUpdDelivInfo.getOmsDeliveryId());
		em.remove(omsCustUpdDelivInfo);
	}

	/** <code>select o from OmsErrorCodes o</code> */
	public List<OmsErrorCodes> getOmsErrorCodesFindAll() {
		return em.createNamedQuery("OmsErrorCodes.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsErrorCodes o where o.omsErrorCode=:omsErrorCode</code>
	 */
	public OmsErrorCodes getOmsErrorCodesFindByErrorCode(String omsErrorCode, String langCode) {
		return (OmsErrorCodes) em.createNamedQuery("OmsErrorCodes.findByErrorCode").setParameter("omsErrorCode", omsErrorCode).setParameter("langCode", langCode).getSingleResult();
	}

	/** <code>select o from OmsCustUpdDelivInfo o</code> */
	public List<OmsCustUpdDelivInfo> getOmsCustUpdDelivInfoFindAll() {
		return em.createNamedQuery("OmsCustUpdDelivInfo.findAll").getResultList();
	}

	public OmsSparePartFulfill persistOmsSparePartFulfill(OmsSparePartFulfill omsSparePartFulfill) {
		em.persist(omsSparePartFulfill);
		return omsSparePartFulfill;
	}

	public OmsSparePartFulfill mergeOmsSparePartFulfill(OmsSparePartFulfill omsSparePartFulfill) {
		return em.merge(omsSparePartFulfill);
	}

	public void removeOmsSparePartFulfill(OmsSparePartFulfill omsSparePartFulfill) {
		omsSparePartFulfill = em.find(OmsSparePartFulfill.class,
				new OmsSparePartFulfillPK(omsSparePartFulfill.getOmsServiceReqSeqId(), omsSparePartFulfill.getSourceLocation(), omsSparePartFulfill.getTranId()));
		em.remove(omsSparePartFulfill);
	}

	/** <code>select o from OmsSparePartFulfill o</code> */
	public List<OmsSparePartFulfill> getOmsSparePartFulfillFindAll() {
		return em.createNamedQuery("OmsSparePartFulfill.findAll").getResultList();
	}

	/**
	 *
	 * @param serviceRequestId
	 * @param itemId
	 * @return
	 */
	public List<OmsSparePartFulfill> getOmsSparePartFulfillFindByRequestAndItem(String serviceRequestId, String itemId) {
		return em.createNamedQuery("OmsSparePartFulfill.findByRequestAndItem").setParameter("omsServiceReqSeqId", serviceRequestId).setParameter("itemId", itemId).getResultList();
	}

	public List<OmsSparePartFulfill> getOmsSparePartFulfillFindByOmsServiceReqId(String omsServiceReqSeqId) {
		return em.createNamedQuery("OmsSparePartFulfill.findByOmsServiceReqId").setParameter("omsServiceReqSeqId", omsServiceReqSeqId).getResultList();
	}

	public OmsSparePartHeader persistOmsSparePartHeader(OmsSparePartHeader omsSparePartHeader) {
		em.persist(omsSparePartHeader);
		return omsSparePartHeader;
	}

	public OmsSparePartHeader mergeOmsSparePartHeader(OmsSparePartHeader omsSparePartHeader) {
		return em.merge(omsSparePartHeader);
	}

	public void removeOmsSparePartHeader(OmsSparePartHeader omsSparePartHeader) {
		omsSparePartHeader = em.find(OmsSparePartHeader.class, new OmsSparePartHeaderPK(omsSparePartHeader.getSequenceId(), omsSparePartHeader.getServiceRequestId()));
		em.remove(omsSparePartHeader);
	}

	/**
	 * <code>select o from OmsSparePartHeader o where o.service_request_id= :serviceRequestId and o.sequence_id = :sequenceId </code>
	 */
	public OmsSparePartHeader getOmsSparePartHeaderByExtKeys(String serviceRequestId, String sequenceId) throws SOAPException {
		try {
			return (OmsSparePartHeader) em.createNamedQuery("OmsSparePartHeader.findSparePartHeaderByExtKeys").setParameter("serviceRequestId", serviceRequestId).setParameter("sequenceId", sequenceId)
					.getSingleResult();
		} catch (NoResultException nre) {
			throw new SOAPException("No Record");
		}
	}

	public OmsSparePartHeader getOmsSparePartHeaderByServiceIdItem(String serviceRequestId, String Item) throws SOAPException {
		try {
			return (OmsSparePartHeader) em.createNamedQuery("OmsSparePartHeader.findSparePartHeaderByServiceIdItem").setParameter("serviceRequestId", serviceRequestId).setParameter("itemId", Item)
					.getSingleResult();
		} catch (NoResultException nre) {
			throw new SOAPException("No Record");
		}
	}

	/**
	 * <code>select o from OmsSparePartHeader o where o.omsServiceReqSeqId= :omsServiceReqSeqId  </code>
	 */
	public OmsSparePartHeader getOmsSparePartHeaderByIntKey(String omsServiceRequestSeqId) throws SOAPException {
		try {
			return (OmsSparePartHeader) em.createNamedQuery("OmsSparePartHeader.findSparePartHeaderByIntKey").setParameter("omsServiceReqSeqId", omsServiceRequestSeqId).getSingleResult();
		} catch (NoResultException nre) {
			throw new SOAPException("No Record");
		}
	}

	public OmsSparePartHeader getOmsSparePartHeaderByKeyComb(String omsServiceRequestSeqId, String serviceRequestId) throws SOAPException {
		try {
			return (OmsSparePartHeader) em.createNamedQuery("OmsSparePartHeader.findSparePartHeaderByKeyComb").setParameter("omsServiceReqSeqId", omsServiceRequestSeqId)
					.setParameter("serviceRequestId", serviceRequestId).getSingleResult();
		} catch (NoResultException nre) {
			throw new SOAPException("No Record");
		}
	}

	/** <code>select o from OmsSparePartHeader o</code> */
	public List<OmsSparePartHeader> getOmsSparePartHeaderFindAll() {
		return em.createNamedQuery("OmsSparePartHeader.findAll").getResultList();
	}

	public OmsSparePartAudit persistOmsSparePartAudit(OmsSparePartAudit omsSparePartAudit) {
		em.persist(omsSparePartAudit);
		return omsSparePartAudit;
	}

	public OmsSparePartAudit mergeOmsSparePartAudit(OmsSparePartAudit omsSparePartAudit) {
		return em.merge(omsSparePartAudit);
	}

	public void removeOmsSparePartAudit(OmsSparePartAudit omsSparePartAudit) {
		omsSparePartAudit = em.find(OmsSparePartAudit.class, omsSparePartAudit.getOmsAuditSeqId());
		em.remove(omsSparePartAudit);
	}

	/** <code>select o from OmsSparePartAudit o</code> */
	public List<OmsSparePartAudit> getOmsSparePartAuditFindAll() {
		return em.createNamedQuery("OmsSparePartAudit.findAll").getResultList();
	}

	/** <code>select o.selectedQty from Tsfdetail o where o.tsfNo=:tsfNo</code> */
	public BigDecimal getTsfdetailFindSelectedQty(BigDecimal tsfNo, String item) {
		return (BigDecimal) em.createNamedQuery("Tsfdetail.findSelectedQty").setParameter("tsfNo", tsfNo).setParameter("item", item).getSingleResult();

	}

	/**
	 * @param tsfNo
	 * @param item
	 * @return
	 */
	public BigDecimal getTsfdetailFindDistroQty(BigDecimal tsfNo, String item) {
		return (BigDecimal) em.createNamedQuery("Tsfdetail.findDistroQty").setParameter("tsfNo", tsfNo).setParameter("item", item).getSingleResult();

	}

	public OmsSparePartConfirmHdr persistOmsSparePartConfirmHdr(OmsSparePartConfirmHdr omsSparePartConfirmHdr) {
		em.persist(omsSparePartConfirmHdr);
		return omsSparePartConfirmHdr;
	}

	public OmsSparePartConfirmHdr mergeOmsSparePartConfirmHdr(OmsSparePartConfirmHdr omsSparePartConfirmHdr) {
		return em.merge(omsSparePartConfirmHdr);
	}

	public void removeOmsSparePartConfirmHdr(OmsSparePartConfirmHdr omsSparePartConfirmHdr) {
		omsSparePartConfirmHdr = em.find(OmsSparePartConfirmHdr.class, new OmsSparePartConfirmHdrPK(omsSparePartConfirmHdr.getServiceConfirmId(), omsSparePartConfirmHdr.getServiceRequestId()));
		em.remove(omsSparePartConfirmHdr);
	}

	public OmsSparePartConfirmHdr getOmsSparePartConfirmHdrFindByKey(String serviceReqId, String sparePartsConfirmId) throws SOAPException {
		return (OmsSparePartConfirmHdr) em.createNamedQuery("OmsSparePartConfirmHdr.findByKey").setParameter("serviceConfirmId", sparePartsConfirmId).setParameter("serviceRequestId", serviceReqId)
				.getSingleResult();
	}

	/** <code>select o from OmsSparePartConfirmHdr o</code> */
	public List<OmsSparePartConfirmHdr> getOmsSparePartConfirmHdrFindAll() {
		return em.createNamedQuery("OmsSparePartConfirmHdr.findAll").getResultList();
	}

	public OmsSparePartConfirmDtl persistOmsSparePartConfirmDtl(OmsSparePartConfirmDtl omsSparePartConfirmDtl) {
		em.persist(omsSparePartConfirmDtl);
		return omsSparePartConfirmDtl;
	}

	public OmsSparePartConfirmDtl mergeOmsSparePartConfirmDtl(OmsSparePartConfirmDtl omsSparePartConfirmDtl) {
		return em.merge(omsSparePartConfirmDtl);
	}

	public void removeOmsSparePartConfirmDtl(OmsSparePartConfirmDtl omsSparePartConfirmDtl) {
		omsSparePartConfirmDtl = em.find(OmsSparePartConfirmDtl.class, new OmsSparePartConfirmDtlPK(omsSparePartConfirmDtl.getItem(), omsSparePartConfirmDtl.getServiceConfirmId()));
		em.remove(omsSparePartConfirmDtl);
	}

	/** <code>select o from OmsSparePartConfirmDtl o</code> */
	public List<OmsSparePartConfirmDtl> getOmsSparePartConfirmDtlFindAll() {
		return em.createNamedQuery("OmsSparePartConfirmDtl.findAll").getResultList();
	}

	public OmsSparePartCancelHdr persistOmsSparePartCancelHdr(OmsSparePartCancelHdr omsSparePartCancelHdr) {
		em.persist(omsSparePartCancelHdr);
		return omsSparePartCancelHdr;
	}

	public OmsSparePartCancelHdr mergeOmsSparePartCancelHdr(OmsSparePartCancelHdr omsSparePartCancelHdr) {
		return em.merge(omsSparePartCancelHdr);
	}

	public void removeOmsSparePartCancelHdr(OmsSparePartCancelHdr omsSparePartCancelHdr) {
		omsSparePartCancelHdr = em.find(OmsSparePartCancelHdr.class,
				new OmsSparePartCancelHdrPK(omsSparePartCancelHdr.getCancellationId(), omsSparePartCancelHdr.getSequenceId(), omsSparePartCancelHdr.getServiceReqSeqId()));
		em.remove(omsSparePartCancelHdr);
	}

	/**
	 * @param cancellationId
	 * @param sequenceId
	 * @param serviceReqSeqId
	 * @return
	 * @throws SOAPException
	 */
	public OmsSparePartCancelHdr getOmsSparePartCancelHdrByKey(String cancellationId, String sequenceId, String serviceReqSeqId) {
		return em.find(OmsSparePartCancelHdr.class, new OmsSparePartCancelHdrPK(cancellationId, sequenceId, serviceReqSeqId));
	}

	/** <code>select o from OmsSparePartCancelHdr o</code> */
	public List<OmsSparePartCancelHdr> getOmsSparePartCancelHdrFindAll() {
		return em.createNamedQuery("OmsSparePartCancelHdr.findAll").getResultList();
	}

	public OmsCoAwbDetail persistOmsCoAwbDetail(OmsCoAwbDetail omsCoAwbDetail) {
		em.persist(omsCoAwbDetail);
		return omsCoAwbDetail;
	}

	public OmsCoAwbDetail mergeOmsCoAwbDetail(OmsCoAwbDetail omsCoAwbDetail) {
		return em.merge(omsCoAwbDetail);
	}

	public void removeOmsCoAwbDetail(OmsCoAwbDetail omsCoAwbDetail) {
		omsCoAwbDetail = em.find(OmsCoAwbDetail.class, new OmsCoAwbDetailPK(omsCoAwbDetail.getAwbUpdReqId(), omsCoAwbDetail.getDeliveryId()));
		em.remove(omsCoAwbDetail);
	}

	/** <code>select o from OmsCoAwbDetail o</code> */
	public List<OmsCoAwbDetail> getOmsCoAwbDetailFindAll() {
		return em.createNamedQuery("OmsCoAwbDetail.findAll").getResultList();
	}

	public OmsCoAwbHead persistOmsCoAwbHead(OmsCoAwbHead omsCoAwbHead) {
		em.persist(omsCoAwbHead);
		return omsCoAwbHead;
	}

	public OmsCoAwbHead mergeOmsCoAwbHead(OmsCoAwbHead omsCoAwbHead) {
		return em.merge(omsCoAwbHead);
	}

	public void removeOmsCoAwbHead(OmsCoAwbHead omsCoAwbHead) {
		omsCoAwbHead = em.find(OmsCoAwbHead.class, omsCoAwbHead.getAwbUpdReqId());
		em.remove(omsCoAwbHead);
	}

	/** <code>select o from OmsCoAwbHead o</code> */
	public List<OmsCoAwbHead> getOmsCoAwbHeadFindAll() {
		return em.createNamedQuery("OmsCoAwbHead.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsCoAwbHead o where o.awbUpdReqId=:awbUpdReqId </code>
	 */
	public OmsCoAwbHead getOmsCoAwbHeadFindByKey(String awbUpdReqId) throws SOAPException {
		try {
			return (OmsCoAwbHead) em.createNamedQuery("OmsCoAwbHead.findByKey").setParameter("awbUpdReqId", awbUpdReqId).getSingleResult();
		} catch (NoResultException nre) {
			throw new SOAPException("No Record");
		}
	}

	public OmsRmaDelDetail persistOmsRmaDelDetail(OmsRmaDelDetail omsRmaDelDetail) {
		em.persist(omsRmaDelDetail);
		return omsRmaDelDetail;
	}

	public OmsRmaDelDetail mergeOmsRmaDelDetail(OmsRmaDelDetail omsRmaDelDetail) {
		return em.merge(omsRmaDelDetail);
	}

	public void removeOmsRmaDelDetail(OmsRmaDelDetail omsRmaDelDetail) {
		omsRmaDelDetail = em.find(OmsRmaDelDetail.class, new OmsRmaDelDetailPK(omsRmaDelDetail.getItem(), omsRmaDelDetail.getLineNo(), omsRmaDelDetail.getRmaDelReqId()));
		em.remove(omsRmaDelDetail);
	}

	/** <code>select o from OmsRmaDelDetail o</code> */
	public List<OmsRmaDelDetail> getOmsRmaDelDetailFindAll() {
		return em.createNamedQuery("OmsRmaDelDetail.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsRmaDelDetail o  where o.rmaDelReqId=:rmaDelReqId</code>
	 */
	public List<OmsRmaDelDetail> getOmsRmaDelDetailFindByRmaDelReqId(String rmaDelReqId) {
		return em.createNamedQuery("OmsRmaDelDetail.findByRmaDelReqId").setParameter("rmaDelReqId", rmaDelReqId).getResultList();
	}

	public OmsRmaDelHead persistOmsRmaDelHead(OmsRmaDelHead omsRmaDelHead) {
		em.persist(omsRmaDelHead);
		return omsRmaDelHead;
	}

	public OmsRmaDelHead mergeOmsRmaDelHead(OmsRmaDelHead omsRmaDelHead) {
		return em.merge(omsRmaDelHead);
	}

	public void removeOmsRmaDelHead(OmsRmaDelHead omsRmaDelHead) {
		omsRmaDelHead = em.find(OmsRmaDelHead.class, omsRmaDelHead.getRmaId());
		em.remove(omsRmaDelHead);
	}

	/** <code>select o from OmsRmaDelHead o</code> */
	public List<OmsRmaDelHead> getOmsRmaDelHeadFindAll() {
		return em.createNamedQuery("OmsRmaDelHead.findAll").getResultList();
	}

	/** <code>select o from OmsRmaDelHead o</code> */
	public OmsRmaDelHead getOmsRmaDelHeadFindByRmaDelReqId(String rmaDelReqId, BigDecimal rmaId) {
		return (OmsRmaDelHead) em.createNamedQuery("OmsRmaDelHead.findByRmaDelReqId").setParameter("rmaDelReqId", rmaDelReqId).setParameter("rmaId", rmaId).getSingleResult();
	}

	public OmsRmaDelHead getOmsRmaDelHeadFindByRmaId(BigDecimal rmaId, String status) {
		return (OmsRmaDelHead) em.createNamedQuery("OmsRmaDelHead.findByRmaId").setParameter("rmaId", rmaId).setParameter("status", status).getSingleResult();
	}

	public OmsRmaModDetail persistOmsRmaModDetail(OmsRmaModDetail omsRmaModDetail) {
		em.persist(omsRmaModDetail);
		return omsRmaModDetail;
	}

	public OmsRmaModDetail mergeOmsRmaModDetail(OmsRmaModDetail omsRmaModDetail) {
		return em.merge(omsRmaModDetail);
	}

	public void removeOmsRmaModDetail(OmsRmaModDetail omsRmaModDetail) {
		omsRmaModDetail = em.find(OmsRmaModDetail.class, new OmsRmaModDetailPK(omsRmaModDetail.getItem(), omsRmaModDetail.getLineNo(), omsRmaModDetail.getRmaModReqId()));
		em.remove(omsRmaModDetail);
	}

	/** <code>select o from OmsRmaModDetail o</code> */
	public List<OmsRmaModDetail> getOmsRmaModDetailFindAll() {
		return em.createNamedQuery("OmsRmaModDetail.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsRmaModDetail o where o.rmaModReqId=:rmaModReqId</code>
	 */
	public List<OmsRmaModDetail> getOmsRmaModDetailFindByRmaModReqId(String rmaModReqId) {
		return em.createNamedQuery("OmsRmaModDetail.findByRmaModReqId").setParameter("rmaModReqId", rmaModReqId).getResultList();
	}

	public OmsRmaModHead persistOmsRmaModHead(OmsRmaModHead omsRmaModHead) {
		em.persist(omsRmaModHead);
		return omsRmaModHead;
	}

	public OmsRmaModHead mergeOmsRmaModHead(OmsRmaModHead omsRmaModHead) {
		return em.merge(omsRmaModHead);
	}

	public void removeOmsRmaModHead(OmsRmaModHead omsRmaModHead) {
		omsRmaModHead = em.find(OmsRmaModHead.class, omsRmaModHead.getRmaModReqId());
		em.remove(omsRmaModHead);
	}

	/** <code>select o from OmsRmaModHead o</code> */
	public List<OmsRmaModHead> getOmsRmaModHeadFindAll() {
		return em.createNamedQuery("OmsRmaModHead.findAll").getResultList();
	}

	/** <code>select o from OmsRmaModHead o</code> */
	public OmsRmaModHead getOmsRmaModHeadFindByRmaModReqId(String rmaModReqId) throws SOAPException {
		try {
			return (OmsRmaModHead) em.createNamedQuery("OmsRmaModHead.findByRmaModReqId").setParameter("rmaModReqId", rmaModReqId).getSingleResult();
		} catch (NoResultException nre) {
			throw new SOAPException("No Record");
		}
	}

	public OmsOrposPrcOvdLine persistOmsOrposPrcOvdLine(OmsOrposPrcOvdLine omsOrposPrcOvdLine) {
		em.persist(omsOrposPrcOvdLine);
		return omsOrposPrcOvdLine;
	}

	public OmsOrposPrcOvdLine mergeOmsOrposPrcOvdLine(OmsOrposPrcOvdLine omsOrposPrcOvdLine) {
		return em.merge(omsOrposPrcOvdLine);
	}

	public void removeOmsOrposPrcOvdLine(OmsOrposPrcOvdLine omsOrposPrcOvdLine) {

	}

	/** <code>select o from OmsOrposPrcOvdLine o</code> */
	public List<OmsOrposPrcOvdLine> getOmsOrposPrcOvdLineFindAll() {
		return em.createNamedQuery("OmsOrposPrcOvdLine.findAll").getResultList();
	}

	public OmsOrposTravelCheckTender persistOmsOrposTravelCheckTender(OmsOrposTravelCheckTender omsOrposTravelCheckTender) {
		em.persist(omsOrposTravelCheckTender);
		return omsOrposTravelCheckTender;
	}

	public OmsOrposTravelCheckTender mergeOmsOrposTravelCheckTender(OmsOrposTravelCheckTender omsOrposTravelCheckTender) {
		return em.merge(omsOrposTravelCheckTender);
	}

	public void removeOmsOrposTravelCheckTender(OmsOrposTravelCheckTender omsOrposTravelCheckTender) {
		omsOrposTravelCheckTender = em.find(OmsOrposTravelCheckTender.class, omsOrposTravelCheckTender.getTravelCheckTenderId());
		em.remove(omsOrposTravelCheckTender);
	}

	/** <code>select o from OmsOrposTravelCheckTender o</code> */
	public List<OmsOrposTravelCheckTender> getOmsOrposTravelCheckTenderFindAll() {
		return em.createNamedQuery("OmsOrposTravelCheckTender.findAll").getResultList();
	}

	public OmsOrposPayment persistOmsOrposPayment(OmsOrposPayment omsOrposPayment) {
		em.persist(omsOrposPayment);
		return omsOrposPayment;
	}

	public OmsOrposPayment mergeOmsOrposPayment(OmsOrposPayment omsOrposPayment) {
		return em.merge(omsOrposPayment);
	}

	public void removeOmsOrposPayment(OmsOrposPayment omsOrposPayment) {
		omsOrposPayment = em.find(OmsOrposPayment.class, omsOrposPayment.getPaymentSeqNo());
		em.remove(omsOrposPayment);
	}

	/** <code>select o from OmsOrposPayment o</code> */
	public List<OmsOrposPayment> getOmsOrposPaymentFindAll() {
		return em.createNamedQuery("OmsOrposPayment.findAll").getResultList();
	}

	public OmsOrposGiftcertTender persistOmsOrposGiftcertTender(OmsOrposGiftcertTender omsOrposGiftcertTender) {
		em.persist(omsOrposGiftcertTender);
		return omsOrposGiftcertTender;
	}

	public OmsOrposGiftcertTender mergeOmsOrposGiftcertTender(OmsOrposGiftcertTender omsOrposGiftcertTender) {
		return em.merge(omsOrposGiftcertTender);
	}

	public void removeOmsOrposGiftcertTender(OmsOrposGiftcertTender omsOrposGiftcertTender) {
		omsOrposGiftcertTender = em.find(OmsOrposGiftcertTender.class, omsOrposGiftcertTender.getGiftcertSerialNumber());
		em.remove(omsOrposGiftcertTender);
	}

	/** <code>select o from OmsOrposGiftcertTender o</code> */
	public List<OmsOrposGiftcertTender> getOmsOrposGiftcertTenderFindAll() {
		return em.createNamedQuery("OmsOrposGiftcertTender.findAll").getResultList();
	}

	public OmsOrposCustOrdDel persistOmsOrposCustOrdDel(OmsOrposCustOrdDel omsOrposCustOrdDel) {
		em.persist(omsOrposCustOrdDel);
		return omsOrposCustOrdDel;
	}

	public OmsOrposCustOrdDel mergeOmsOrposCustOrdDel(OmsOrposCustOrdDel omsOrposCustOrdDel) {
		return em.merge(omsOrposCustOrdDel);
	}

	public void removeOmsOrposCustOrdDel(OmsOrposCustOrdDel omsOrposCustOrdDel) {
		omsOrposCustOrdDel = em.find(OmsOrposCustOrdDel.class, new OmsOrposCustOrdDelPK(omsOrposCustOrdDel.getCustOrdDelId(), omsOrposCustOrdDel.getCustOrdDelSeqNo()));
		em.remove(omsOrposCustOrdDel);
	}

	/** <code>select o from OmsOrposCustOrdDel o</code> */
	public List<OmsOrposCustOrdDel> getOmsOrposCustOrdDelFindAll() {
		return em.createNamedQuery("OmsOrposCustOrdDel.findAll").getResultList();
	}

	public OmsOrposGeoaddr persistOmsOrposGeoaddr(OmsOrposGeoaddr omsOrposGeoaddr) {
		em.persist(omsOrposGeoaddr);
		return omsOrposGeoaddr;
	}

	public OmsOrposGeoaddr mergeOmsOrposGeoaddr(OmsOrposGeoaddr omsOrposGeoaddr) {
		return em.merge(omsOrposGeoaddr);
	}

	public void removeOmsOrposGeoaddr(OmsOrposGeoaddr omsOrposGeoaddr) {

	}
	/* <code>select o from OmsOrposGeoaddr o</code> */

	public OmsOrposGeoaddr getOmsOrposGeoaddrFindByCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposGeoaddr) em.createNamedQuery("OmsOrposGeoaddr.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposGeoaddr o</code> */
	public List<OmsOrposGeoaddr> getOmsOrposGeoaddrFindAll() {
		return em.createNamedQuery("OmsOrposGeoaddr.findAll").getResultList();
	}

	/** <code>select o from OmsOrposPayment o</code> */
	public OmsOrposGeoaddr getOmsOrposGeoaddrFindByPaymentSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo) {
		return (OmsOrposGeoaddr) em.createNamedQuery("OmsOrposGeoaddr.findByPaymentSeqNo").setParameter("paymentSeqNo", paymentSeqNo).setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.getSingleResult();
	}

	/** <code>select o from OmsOrposGeoaddr o</code> */
	public OmsOrposGeoaddr getOmsOrposGeoaddrFindByFulSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal custOrdFulSeqNo) {
		return (OmsOrposGeoaddr) em.createNamedQuery("OmsOrposGeoaddr.findByFulSeqNo").setParameter("custOrdFulSeqNo", custOrdFulSeqNo).setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.getSingleResult();
	}

	public OmsOrposDiscntLine persistOmsOrposDiscntLine(OmsOrposDiscntLine omsOrposDiscntLine) {
		em.persist(omsOrposDiscntLine);
		return omsOrposDiscntLine;
	}

	public OmsOrposDiscntLine mergeOmsOrposDiscntLine(OmsOrposDiscntLine omsOrposDiscntLine) {
		return em.merge(omsOrposDiscntLine);
	}

	public void removeOmsOrposDiscntLine(OmsOrposDiscntLine omsOrposDiscntLine) {
		omsOrposDiscntLine = em.find(OmsOrposDiscntLine.class, new OmsOrposDiscntLinePK(omsOrposDiscntLine.getCapturedLineItemNo(), omsOrposDiscntLine.getItemId(), omsOrposDiscntLine.getLineNo(),
				omsOrposDiscntLine.getOmsCustOrdNo(), omsOrposDiscntLine.getOmsOrposCustOrderId()));
		em.remove(omsOrposDiscntLine);
	}

	/** <code>select o from OmsOrposDiscntLine o</code> */
	public List<OmsOrposDiscntLine> getOmsOrposDiscntLineFindAll() {
		return em.createNamedQuery("OmsOrposDiscntLine.findAll").getResultList();
	}

	public OmsOrposPromoLine persistOmsOrposPromoLine(OmsOrposPromoLine omsOrposPromoLine) {
		em.persist(omsOrposPromoLine);
		return omsOrposPromoLine;
	}

	public OmsOrposPromoLine mergeOmsOrposPromoLine(OmsOrposPromoLine omsOrposPromoLine) {
		return em.merge(omsOrposPromoLine);
	}

	public void removeOmsOrposPromoLine(OmsOrposPromoLine omsOrposPromoLine) {

	}

	/** <code>select o from OmsOrposPromoLine o</code> */
	public List<OmsOrposPromoLine> getOmsOrposPromoLineFindAll() {
		return em.createNamedQuery("OmsOrposPromoLine.findAll").getResultList();
	}

	public OmsOrposCustOrdItm persistOmsOrposCustOrdItm(OmsOrposCustOrdItm omsOrposCustOrdItm) {
		em.persist(omsOrposCustOrdItm);
		return omsOrposCustOrdItm;
	}

	public OmsOrposCustOrdItm mergeOmsOrposCustOrdItm(OmsOrposCustOrdItm omsOrposCustOrdItm) {
		return em.merge(omsOrposCustOrdItm);
	}

	public void removeOmsOrposCustOrdItm(OmsOrposCustOrdItm omsOrposCustOrdItm) {
		omsOrposCustOrdItm = em.find(OmsOrposCustOrdItm.class, omsOrposCustOrdItm.getLineItemNo());
		em.remove(omsOrposCustOrdItm);
	}

	/** <code>select o from OmsOrposCus o</code> */
	public List<OmsOrposCustOrdItm> getOmsOrposCustOrdItmFindAll() {
		return em.createNamedQuery("OmsOrposCustOrdItm.findAll").getResultList();
	}

//   /** <code>select o from OmsOrposCustOrdItm o</code> */
//    public List<OmsOrposCustOrdItm> getOmsOrposCustOrdItmFindAll() {
//    return em.createNamedQuery("OmsOrposCustOrdItm.findAll").getResultList();
//     }

	/** <code>select o from OmsOrposCustOrdItm o</code> */
	public List<OmsOrposCustOrdItm> getOmsOrposCustOrdItmFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposCustOrdItm.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	/**
	 * <code>select o from OmsOrposCustOrdItm o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.itemId=:itemId and o.lineItemNo=:lineItemNo</code>
	 */
	public OmsOrposCustOrdItm getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(BigDecimal omsOrposCustOrderId, String itemId, BigDecimal lineItemNo) {
		return (OmsOrposCustOrdItm) em.createNamedQuery("OmsOrposCustOrdItm.findByOmsOrposCustOrdIdAndItem").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("itemId", itemId)
				.setParameter("lineItemNo", lineItemNo).getSingleResult();
	}

	/** <code>select o from OmsOrposCustOrdItm o</code> */
	public OmsOrposCustOrdItm getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndLineItemNo(BigDecimal omsOrposCustOrderId, BigDecimal lineItemNo) {
		return (OmsOrposCustOrdItm) em.createNamedQuery("OmsOrposCustOrdItm.findByOmsOrposCustOrdIdAndLineItemNo").setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.setParameter("lineItemNo", lineItemNo).getSingleResult();
	}

	/**
	 * <code>select o from OmsOrposCustOrdItm o where o.omsOrposCustOrderId=:omsOrposCustOrderId</code>
	 * 
	 * @param omsOrposCustOrderId
	 * @return
	 */
	public List<OmsOrposCustOrdItm> getOmsOrposCustOrdItmFindColumns(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposCustOrdItm.findColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	/** <code>select o from OmsOrposContact o</code> */
	public OmsOrposContact getOmsOrposContactFindByOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposContact) em.createNamedQuery("OmsOrposContact.findByOmsOrposCustOrderId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposContact o</code> */
	public OmsOrposContact getOmsOrposContactFindByCustomerId(BigDecimal omsOrposCustOrderId, String customerId) {
		return (OmsOrposContact) em.createNamedQuery("OmsOrposContact.findByOmsOrposCustOrderId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("customerId", customerId)
				.getSingleResult();
	}

	public OmsOrposContact getOmsOrposContactFindByOmsOrposCustOrderIdandCustomerId(BigDecimal omsOrposCustOrderId, BigDecimal customerId) {
		return (OmsOrposContact) em.createNamedQuery("OmsOrposContact.findByOmsOrposCustOrderIdandCustomerId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.setParameter("customerId", customerId).getSingleResult();
	}

	/** <code>select o from OmsOrposContact o</code> */
	public OmsOrposContact getOmsOrposContactFindByFulSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal custOrdFulSeqNo) {
		return (OmsOrposContact) em.createNamedQuery("OmsOrposContact.findByFulSeqNo").setParameter("custOrdFulSeqNo", custOrdFulSeqNo).setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.getSingleResult();
	}

	/** <code>select o from OmsOrposContact o</code> */
	public OmsOrposContact getOmsOrposContactFindByPaymentSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo) {
		return (OmsOrposContact) em.createNamedQuery("OmsOrposContact.findByPaymentSeqNo").setParameter("paymentSeqNo", paymentSeqNo).setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.getSingleResult();
	}

	/** <code>select o from OmsOrposPhone o</code> */
	public List<OmsOrposPhone> getOmsOrposPhoneFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposPhone.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	/** <code>select o from OmsOrposPhone o</code> */
	public List<OmsOrposPhone> getOmsOrposPhoneFindByOmsOrposContactSeq(BigDecimal omsOrposCustOrderId, BigDecimal contactSeq) {
		return em.createNamedQuery("OmsOrposPhone.findByOmsOrposContactSeq").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("contactSeq", contactSeq).getResultList();
	}

	/** <code>select o from OmsOrposEmail o</code> */
	public List<OmsOrposEmail> getOmsOrposEmailFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposEmail.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	/** <code>select o from OmsOrposAddrbookEntry o</code> */
	public List<OmsOrposAddrbookEntry> getOmsOrposAddrbookEntryFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposAddrbookEntry.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	/** <code>select o from OmsOrposEmail o</code> */
	public List<OmsOrposEmail> getOmsOrposEmailFindByOmsOrposContactSeq(BigDecimal omsOrposCustOrderId, BigDecimal contactSeq) {
		return em.createNamedQuery("OmsOrposEmail.findByOmsOrposContactSeq").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("contactSeq", contactSeq).getResultList();
	}

	public OmsOrposEmail persistOmsOrposEmail(OmsOrposEmail omsOrposEmail) {
		em.persist(omsOrposEmail);
		return omsOrposEmail;
	}

	public OmsOrposEmail mergeOmsOrposEmail(OmsOrposEmail omsOrposEmail) {
		return em.merge(omsOrposEmail);
	}

	public void removeOmsOrposEmail(OmsOrposEmail omsOrposEmail) {

	}

	/** <code>select o from OmsOrposEmail o</code> */
	public List<OmsOrposEmail> getOmsOrposEmailFindAll() {
		return em.createNamedQuery("OmsOrposEmail.findAll").getResultList();
	}

	/** <code>select o from OmsOrposPrcOvdLine o</code> */
	public OmsOrposPrcOvdLine getOmsOrposPrcOvdLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposPrcOvdLine) em.createNamedQuery("OmsOrposPrcOvdLine.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposPromoLine o</code> */
	public OmsOrposPromoLine getOmsOrposPromoLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposPromoLine) em.createNamedQuery("OmsOrposPromoLine.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposDiscntLine o</code> */
	public OmsOrposDiscntLine getOmsOrposDiscntLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposDiscntLine) em.createNamedQuery("OmsOrposDiscntLine.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposTaxLine o</code> */
	public OmsOrposTaxLine getOmsOrposTaxLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposTaxLine) em.createNamedQuery("OmsOrposTaxLine.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposAlterationItem o</code> */
	public OmsOrposAlterationItem getOmsOrposAlterationItemFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposAlterationItem) em.createNamedQuery("OmsOrposAlterationItem.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposGiftcardItem o</code> */
	public OmsOrposGiftcardItem getOmsOrposGiftcardItemFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposGiftcardItem) em.createNamedQuery("OmsOrposGiftcardItem.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposCustOrdFul o</code> */
	public List<OmsOrposCustOrdFul> getOmsOrposCustOrdFulFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposCustOrdFul.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	/** <code>select o from OmsOrposCustOrdDel o</code> */
	public OmsOrposCustOrdDel getOmsOrposCustOrdDelFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposCustOrdDel) em.createNamedQuery("OmsOrposCustOrdDel.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposPayment o</code> */
	public List<OmsOrposPayment> getOmsOrposPaymentFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposPayment.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	public OmsOrposCouponTender persistOmsOrposCouponTender(OmsOrposCouponTender omsOrposCouponTender) {
		em.persist(omsOrposCouponTender);
		return omsOrposCouponTender;
	}

	public OmsOrposCouponTender mergeOmsOrposCouponTender(OmsOrposCouponTender omsOrposCouponTender) {
		return em.merge(omsOrposCouponTender);
	}

	public void removeOmsOrposCouponTender(OmsOrposCouponTender omsOrposCouponTender) {
		omsOrposCouponTender = em.find(OmsOrposCouponTender.class, omsOrposCouponTender.getCouponNumber());
		em.remove(omsOrposCouponTender);
	}

	/** <code>select o from OmsOrposCouponTender o</code> */
	public List<OmsOrposCouponTender> getOmsOrposCouponTenderFindAll() {
		return em.createNamedQuery("OmsOrposCouponTender.findAll").getResultList();
	}

	public OmsOrposGiftcardTender persistOmsOrposGiftcardTender(OmsOrposGiftcardTender omsOrposGiftcardTender) {
		em.persist(omsOrposGiftcardTender);
		return omsOrposGiftcardTender;
	}

	public OmsOrposGiftcardTender mergeOmsOrposGiftcardTender(OmsOrposGiftcardTender omsOrposGiftcardTender) {
		return em.merge(omsOrposGiftcardTender);
	}

	public void removeOmsOrposGiftcardTender(OmsOrposGiftcardTender omsOrposGiftcardTender) {
		omsOrposGiftcardTender = em.find(OmsOrposGiftcardTender.class, omsOrposGiftcardTender.getCardNumber());
		em.remove(omsOrposGiftcardTender);
	}

	/** <code>select o from OmsOrposGiftcardTender o</code> */
	public List<OmsOrposGiftcardTender> getOmsOrposGiftcardTenderFindAll() {
		return em.createNamedQuery("OmsOrposGiftcardTender.findAll").getResultList();
	}

	public OmsOrposTaxLine persistOmsOrposTaxLine(OmsOrposTaxLine omsOrposTaxLine) {
		em.persist(omsOrposTaxLine);
		return omsOrposTaxLine;
	}

	public OmsOrposTaxLine mergeOmsOrposTaxLine(OmsOrposTaxLine omsOrposTaxLine) {
		return em.merge(omsOrposTaxLine);
	}

	public void removeOmsOrposTaxLine(OmsOrposTaxLine omsOrposTaxLine) {

	}

	/** <code>select o from OmsOrposTaxLine o</code> */
	public List<OmsOrposTaxLine> getOmsOrposTaxLineFindAll() {
		return em.createNamedQuery("OmsOrposTaxLine.findAll").getResultList();
	}

	public OmsOrposContact persistOmsOrposContact(OmsOrposContact omsOrposContact) {
		em.persist(omsOrposContact);
		return omsOrposContact;
	}

	public OmsOrposContact mergeOmsOrposContact(OmsOrposContact omsOrposContact) {
		return em.merge(omsOrposContact);
	}

	public void removeOmsOrposContact(OmsOrposContact omsOrposContact) {

	}

	/** <code>select o from OmsOrposContact o</code> */
	public List<OmsOrposContact> getOmsOrposContactFindAll() {
		return em.createNamedQuery("OmsOrposContact.findAll").getResultList();
	}

	public OmsOrposCustomerGrpIdLst persistOmsOrposCustomerGrpIdLst(OmsOrposCustomerGrpIdLst omsOrposCustomerGrpIdLst) {
		em.persist(omsOrposCustomerGrpIdLst);
		return omsOrposCustomerGrpIdLst;
	}

	public OmsOrposCustomerGrpIdLst mergeOmsOrposCustomerGrpIdLst(OmsOrposCustomerGrpIdLst omsOrposCustomerGrpIdLst) {
		return em.merge(omsOrposCustomerGrpIdLst);
	}

	public void removeOmsOrposCustomerGrpIdLst(OmsOrposCustomerGrpIdLst omsOrposCustomerGrpIdLst) {
		omsOrposCustomerGrpIdLst = em.find(OmsOrposCustomerGrpIdLst.class, omsOrposCustomerGrpIdLst.getCustomerGroupId());
		em.remove(omsOrposCustomerGrpIdLst);
	}

	/** <code>select o from OmsOrposCustomerGrpIdLst o</code> */
	public List<OmsOrposCustomerGrpIdLst> getOmsOrposCustomerGrpIdLstFindAll() {
		return em.createNamedQuery("OmsOrposCustomerGrpIdLst.findAll").getResultList();
	}

	public OmsOrposGiftcardItem persistOmsOrposGiftcardItem(OmsOrposGiftcardItem omsOrposGiftcardItem) {
		em.persist(omsOrposGiftcardItem);
		return omsOrposGiftcardItem;
	}

	public OmsOrposGiftcardItem mergeOmsOrposGiftcardItem(OmsOrposGiftcardItem omsOrposGiftcardItem) {
		return em.merge(omsOrposGiftcardItem);
	}

	public void removeOmsOrposGiftcardItem(OmsOrposGiftcardItem omsOrposGiftcardItem) {
		omsOrposGiftcardItem = em.find(OmsOrposGiftcardItem.class, omsOrposGiftcardItem.getGiftCardNumber());
		em.remove(omsOrposGiftcardItem);
	}

	/** <code>select o from OmsOrposGiftcardItem o</code> */
	public List<OmsOrposGiftcardItem> getOmsOrposGiftcardItemFindAll() {
		return em.createNamedQuery("OmsOrposGiftcardItem.findAll").getResultList();
	}

	public OmsOrposCustomer persistOmsOrposCustomer(OmsOrposCustomer omsOrposCustomer) {
		em.persist(omsOrposCustomer);
		return omsOrposCustomer;
	}

	public OmsOrposCustomer mergeOmsOrposCustomer(OmsOrposCustomer omsOrposCustomer) {
		return em.merge(omsOrposCustomer);
	}

	public void removeOmsOrposCustomer(OmsOrposCustomer omsOrposCustomer) {
		omsOrposCustomer = em.find(OmsOrposCustomer.class, omsOrposCustomer.getCustomerId());
		em.remove(omsOrposCustomer);
	}

	/** <code>select o from OmsOrposCustomer o</code> */
	public List<OmsOrposCustomer> getOmsOrposCustomerFindAll() {
		return em.createNamedQuery("OmsOrposCustomer.findAll").getResultList();
	}

	/** <code>select o from OmsOrposCustomer o</code> */
	public OmsOrposCustomer getOmsOrposCustomerFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposCustomer) em.createNamedQuery("OmsOrposCustomer.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposCustomer o</code> */
	public OmsOrposCustomer getOmsOrposCustomerFindByOmsOrposCustOrdIdAndCustomerId(BigDecimal omsOrposCustOrderId, String customerId) {
		return (OmsOrposCustomer) em.createNamedQuery("OmsOrposCustomer.findByOmsOrposCustOrdIdAndCustomerId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.setParameter("customerId", customerId).getSingleResult();
	}

	/** <code>select o from OmsOrposLocale o</code> */
	public OmsOrposLocale getOmsOrposLocaleFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposLocale) em.createNamedQuery("OmsOrposLocale.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposLocale o</code> */
	public OmsOrposLocale getOmsOrposLocaleFindByOmsOrposCustOrdIdAndLocaleSeq(BigDecimal omsOrposCustOrderId, BigDecimal localeSeq) {
		return (OmsOrposLocale) em.createNamedQuery("OmsOrposLocale.findByOmsOrposCustOrdId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("localeSeq", localeSeq)
				.getSingleResult();
	}

	public OmsOrposCustOrdFul persistOmsOrposCustOrdFul(OmsOrposCustOrdFul omsOrposCustOrdFul) {
		em.persist(omsOrposCustOrdFul);
		return omsOrposCustOrdFul;
	}

	public OmsOrposCustOrdFul mergeOmsOrposCustOrdFul(OmsOrposCustOrdFul omsOrposCustOrdFul) {
		return em.merge(omsOrposCustOrdFul);
	}

	public void removeOmsOrposCustOrdFul(OmsOrposCustOrdFul omsOrposCustOrdFul) {
		omsOrposCustOrdFul = em.find(OmsOrposCustOrdFul.class, new OmsOrposCustOrdFulPK(omsOrposCustOrdFul.getCustOrdFulSeqNo(), omsOrposCustOrdFul.getFulfillOrderId()));
		em.remove(omsOrposCustOrdFul);
	}

	/** <code>select o from OmsOrposCustOrdFul o</code> */
	public List<OmsOrposCustOrdFul> getOmsOrposCustOrdFulFindAll() {
		return em.createNamedQuery("OmsOrposCustOrdFul.findAll").getResultList();
	}

	public OmsOrposCustOrderHead persistOmsOrposCustOrderHead(OmsOrposCustOrderHead omsOrposCustOrderHead) {
		em.persist(omsOrposCustOrderHead);
		return omsOrposCustOrderHead;
	}

	public OmsOrposCustOrderHead mergeOmsOrposCustOrderHead(OmsOrposCustOrderHead omsOrposCustOrderHead) {
		return em.merge(omsOrposCustOrderHead);
	}

	public void removeOmsOrposCustOrderHead(OmsOrposCustOrderHead omsOrposCustOrderHead) {
		omsOrposCustOrderHead = em.find(OmsOrposCustOrderHead.class, omsOrposCustOrderHead.getOmsOrposCustOrderId());
		em.remove(omsOrposCustOrderHead);
	}

	/** <code>select o from OmsOrposCustOrderHead o</code> */
	public List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadFindAll() {
		return em.createNamedQuery("OmsOrposCustOrderHead.findAll").getResultList();
	}

	public OmsOrposCustOrderHead getOmsOrposCustOrderHeadDetailsByOrderId(String customerOrderId) {
		return (OmsOrposCustOrderHead) em.createNamedQuery("OmsOrposCustOrderHead.findOrderHeadDetailsByOrderId").setParameter("customerOrderId", customerOrderId).getSingleResult();
	}

	/**
	 * <code>select o.omsOrposCustOrderId from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId</code>
	 */
	public BigDecimal getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(String customerOrderId) {
		return (BigDecimal) em.createNamedQuery("OmsOrposCustOrderHead.findOmsOrposCustOrdId").setParameter("customerOrderId", customerOrderId).getSingleResult();
	}

	/** <code>select o from OmsOrposCustOrderHead o</code> */
	public List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadFindByOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
		return em.createNamedQuery("OmsOrposCustOrderHead.findOrderHeadDetailsByCustomerId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getResultList();
	}

	/**
	 * <code>select o from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId </code>
	 * 
	 * @param customerOrderId
	 * @return
	 */
	public List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadfindColumns(String customerOrderId) {
		return em.createNamedQuery("OmsOrposCustOrderHead.findColumns").setParameter("customerOrderId", customerOrderId).getResultList();
	}

	public OmsOrposPhone persistOmsOrposPhone(OmsOrposPhone omsOrposPhone) {
		em.persist(omsOrposPhone);
		return omsOrposPhone;
	}

	public OmsOrposPhone mergeOmsOrposPhone(OmsOrposPhone omsOrposPhone) {
		return em.merge(omsOrposPhone);
	}

	public void removeOmsOrposPhone(OmsOrposPhone omsOrposPhone) {

	}

	/** <code>select o from OmsOrposPhone o</code> */
	public List<OmsOrposPhone> getOmsOrposPhoneFindAll() {
		return em.createNamedQuery("OmsOrposPhone.findAll").getResultList();
	}

	public OmsOrposAlterationItem persistOmsOrposAlterationItem(OmsOrposAlterationItem omsOrposAlterationItem) {
		em.persist(omsOrposAlterationItem);
		return omsOrposAlterationItem;
	}

	public OmsOrposAlterationItem mergeOmsOrposAlterationItem(OmsOrposAlterationItem omsOrposAlterationItem) {
		return em.merge(omsOrposAlterationItem);
	}

	public void removeOmsOrposAlterationItem(OmsOrposAlterationItem omsOrposAlterationItem) {

	}

	/** <code>select o from OmsOrposAlterationItem o</code> */
	public List<OmsOrposAlterationItem> getOmsOrposAlterationItemFindAll() {
		return em.createNamedQuery("OmsOrposAlterationItem.findAll").getResultList();
	}

	public OmsOrposCheckTender persistOmsOrposCheckTender(OmsOrposCheckTender omsOrposCheckTender) {
		em.persist(omsOrposCheckTender);
		return omsOrposCheckTender;
	}

	public OmsOrposCheckTender mergeOmsOrposCheckTender(OmsOrposCheckTender omsOrposCheckTender) {
		return em.merge(omsOrposCheckTender);
	}

	public void removeOmsOrposCheckTender(OmsOrposCheckTender omsOrposCheckTender) {
		omsOrposCheckTender = em.find(OmsOrposCheckTender.class,
				new OmsOrposCheckTenderPK(omsOrposCheckTender.getAccountNumber(), omsOrposCheckTender.getBankId(), omsOrposCheckTender.getMicrNumber()));
		em.remove(omsOrposCheckTender);
	}

	/** <code>select o from OmsOrposCheckTender o</code> */
	public List<OmsOrposCheckTender> getOmsOrposCheckTenderFindAll() {
		return em.createNamedQuery("OmsOrposCheckTender.findAll").getResultList();
	}

	public OmsOrposCreditDebitTender persistOmsOrposCreditDebitTender(OmsOrposCreditDebitTender omsOrposCreditDebitTender) {
		em.persist(omsOrposCreditDebitTender);
		return omsOrposCreditDebitTender;
	}

	public OmsOrposCreditDebitTender mergeOmsOrposCreditDebitTender(OmsOrposCreditDebitTender omsOrposCreditDebitTender) {
		return em.merge(omsOrposCreditDebitTender);
	}

	public void removeOmsOrposCreditDebitTender(OmsOrposCreditDebitTender omsOrposCreditDebitTender) {
		omsOrposCreditDebitTender = em.find(OmsOrposCreditDebitTender.class, omsOrposCreditDebitTender.getMaskedAccountNumber());
		em.remove(omsOrposCreditDebitTender);
	}

	/** <code>select o from OmsOrposCreditDebitTender o</code> */
	public List<OmsOrposCreditDebitTender> getOmsOrposCreditDebitTenderFindAll() {
		return em.createNamedQuery("OmsOrposCreditDebitTender.findAll").getResultList();
	}

	public OmsOrposLocale persistOmsOrposLocale(OmsOrposLocale omsOrposLocale) {
		em.persist(omsOrposLocale);
		return omsOrposLocale;
	}

	public OmsOrposLocale mergeOmsOrposLocale(OmsOrposLocale omsOrposLocale) {
		return em.merge(omsOrposLocale);
	}

	public void removeOmsOrposLocale(OmsOrposLocale omsOrposLocale) {

	}

	/** <code>select o from OmsOrposLocale o</code> */
	public List<OmsOrposLocale> getOmsOrposLocaleFindAll() {
		return em.createNamedQuery("OmsOrposLocale.findAll").getResultList();
	}

	public OmsOrposAddrbookEntry persistOmsOrposAddrbookEntry(OmsOrposAddrbookEntry omsOrposAddrbookEntry) {
		em.persist(omsOrposAddrbookEntry);
		return omsOrposAddrbookEntry;
	}

	public OmsOrposAddrbookEntry mergeOmsOrposAddrbookEntry(OmsOrposAddrbookEntry omsOrposAddrbookEntry) {
		return em.merge(omsOrposAddrbookEntry);
	}

	public void removeOmsOrposAddrbookEntry(OmsOrposAddrbookEntry omsOrposAddrbookEntry) {
		omsOrposAddrbookEntry = em.find(OmsOrposAddrbookEntry.class, omsOrposAddrbookEntry.getAddrId());
		em.remove(omsOrposAddrbookEntry);
	}

	/** <code>select o from OmsOrposAddrbookEntry o</code> */
	public List<OmsOrposAddrbookEntry> getOmsOrposAddrbookEntryFindAll() {
		return em.createNamedQuery("OmsOrposAddrbookEntry.findAll").getResultList();
	}

	/** <code>select o from OmsOrposAddrbookEntry o</code> */
	public BigDecimal getDepsFindGroupNo(BigDecimal dept) {
		return (BigDecimal) em.createNamedQuery("Deps.findGroupNo").setParameter("dept", dept).getSingleResult();
	}

	// OmsOrposDiscntLinePickup
	public OmsOrposDiscntLinePickup persistOmsOrposDiscntLinePickup(OmsOrposDiscntLinePickup omsOrposDiscntLinePickup) {
		em.persist(omsOrposDiscntLinePickup);
		return omsOrposDiscntLinePickup;
	}

	public OmsOrposDiscntLinePickup mergeOmsOrposDiscntLinePickup(OmsOrposDiscntLinePickup omsOrposDiscntLinePickup) {
		return em.merge(omsOrposDiscntLinePickup);
	}

	public void removeOmsOrposDiscntLinePickup(OmsOrposDiscntLinePickup omsOrposDiscntLinePickup) {
		omsOrposDiscntLinePickup = em.find(OmsOrposDiscntLinePickup.class,
				new OmsOrposDiscntLinePickupPK(omsOrposDiscntLinePickup.getCustOrderPicVoSeq(), omsOrposDiscntLinePickup.getLineItemNo(), omsOrposDiscntLinePickup.getLineNo()));
		em.remove(omsOrposDiscntLinePickup);
	}

	/** <code>select o from OmsOrposDiscntLinePickup o</code> */
	public List<OmsOrposDiscntLinePickup> getOmsOrposDiscntLinePickupFindAll() {
		return em.createNamedQuery("OmsOrposDiscntLinePickup.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsOrposDiscntLinePickup o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.custOrderPicVoSeq=:custOrderPicVoSeq and o.lineItemNo=:lineItemNo</code>
	 * 
	 * @param omsOrposCustOrderId
	 * @param custOrderPicVoSeq
	 * @return
	 */
	public List<OmsOrposDiscntLinePickup> getOmsOrposDiscntLinePickupFindColumns(BigDecimal omsOrposCustOrderId, BigDecimal custOrderPicVoSeq, BigDecimal lineItemNo) {
		return em.createNamedQuery("OmsOrposDiscntLinePickup.findColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("custOrderPicVoSeq", custOrderPicVoSeq)
				.setParameter("lineItemNo", lineItemNo).getResultList();

	}

	/**
	 * <code>select o from OmsOrposDiscntLine o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.capturedLineItemNo=:capturedLineItemNo and o.itemId=:itemId</code>
	 * 
	 * @param omsOrposCustOrderId
	 * @param capturedLineItemNo
	 * @param itemId
	 * @return
	 */
	public List<OmsOrposDiscntLine> getOmsOrposDiscntLineFindColumns(BigDecimal omsOrposCustOrderId, BigDecimal capturedLineItemNo, String itemId) {
		return em.createNamedQuery("OmsOrposDiscntLine.findColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("capturedLineItemNo", capturedLineItemNo)
				.setParameter("itemId", itemId).getResultList();
	}

	// OmsOrposCustOrderPickup
	public OmsOrposCustOrderPickup persistOmsOrposCustOrderPickup(OmsOrposCustOrderPickup omsOrposCustOrderPickup) {
		em.persist(omsOrposCustOrderPickup);
		return omsOrposCustOrderPickup;
	}

	public OmsOrposCustOrderPickup mergeOmsOrposCustOrderPickup(OmsOrposCustOrderPickup omsOrposCustOrderPickup) {
		return em.merge(omsOrposCustOrderPickup);
	}

	public void removeOmsOrposCustOrderPickup(OmsOrposCustOrderPickup omsOrposCustOrderPickup) {
		omsOrposCustOrderPickup = em.find(OmsOrposCustOrderPickup.class, new OmsOrposCustOrderPickupPK(omsOrposCustOrderPickup.getCustOrderPicVoSeq(), omsOrposCustOrderPickup.getCustomerOrderId()));
		em.remove(omsOrposCustOrderPickup);
	}

	/** <code>select o from OmsOrposCustOrderPickup o</code> */
	public List<OmsOrposCustOrderPickup> getOmsOrposCustOrderPickupFindAll() {
		return em.createNamedQuery("OmsOrposCustOrderPickup.findAll").getResultList();
	}

	/**
	 * <code>select o.custOrderPicVoSeq from OmsOrposCustOrderPickup o where o.customerOrderId=:customerOrderId</code>
	 */
	public List<BigDecimal> getOmsOrposCustOrderPickupFindCustOrderPicVoSeq(String customerOrderId) {
		return em.createNamedQuery("OmsOrposCustOrderPickup.findCustOrderPicVoSeq").setParameter("customerOrderId", customerOrderId).getResultList();
	}

	/**
	 * <code>select o from OmsOrposCustOrderPickup o where o.omsOrposCustOrderId=:omsOrposCustOrderId</code>
	 * 
	 * @param omsOrposCustOrderId
	 * @return
	 */
	public List<OmsOrposCustOrderPickup> getOmsOrposCustOrderPickupFindAllColumns(BigDecimal custOrderPicVoSeq) {
		return em.createNamedQuery("OmsOrposCustOrderPickup.findAllColumns").setParameter("custOrderPicVoSeq", custOrderPicVoSeq).getResultList();
	}

	// OmsOrposTaxLinePickup
	public OmsOrposTaxLinePickup persistOmsOrposTaxLinePickup(OmsOrposTaxLinePickup omsOrposTaxLinePickup) {
		em.persist(omsOrposTaxLinePickup);
		return omsOrposTaxLinePickup;
	}

	public OmsOrposTaxLinePickup mergeOmsOrposTaxLinePickup(OmsOrposTaxLinePickup omsOrposTaxLinePickup) {
		return em.merge(omsOrposTaxLinePickup);
	}

	public void removeOmsOrposTaxLinePickup(OmsOrposTaxLinePickup omsOrposTaxLinePickup) {
		/*
		 * omsOrposTaxLinePickup = em.find(OmsOrposTaxLinePickup.class, new
		 * OmsOrposTaxLinePickupPK(omsOrposTaxLinePickup.getCustOrderPicVoSeq(),
		 * omsOrposTaxLinePickup.getLineItemNo(), omsOrposTaxLinePickup.getLineNo() ));
		 * em.remove(omsOrposTaxLinePickup);
		 */
	}

	/** <code>select o from OmsOrposTaxLinePickup o</code> */
	public List<OmsOrposTaxLinePickup> getOmsOrposTaxLinePickupFindAll() {
		return em.createNamedQuery("OmsOrposTaxLinePickup.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsOrposTaxLinePickup o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.custOrderPicVoSeq=:custOrderPicVoSeq and o.lineItemNo=:lineItemNo</code>
	 */
	public List<OmsOrposTaxLinePickup> getOmsOrposTaxLinePickupFindColumns(BigDecimal omsOrposCustOrderId, BigDecimal custOrderPicVoSeq, BigDecimal lineItemNo) {
		return em.createNamedQuery("OmsOrposTaxLinePickup.findColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("custOrderPicVoSeq", custOrderPicVoSeq)
				.setParameter("lineItemNo", lineItemNo).getResultList();
	}

	/**
	 * <code>select o from OmsOrposTaxLine o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.capturedLineItemNo=:capturedLineItemNo and o.itemId=:itemId</code>
	 * 
	 * @param omsOrposCustOrderId
	 * @param capturedLineItemNo
	 * @param itemId
	 * @return
	 */
	public List<OmsOrposTaxLine> getOmsOrposTaxLineFindColumns(BigDecimal omsOrposCustOrderId, BigDecimal capturedLineItemNo, String itemId) {
		return em.createNamedQuery("OmsOrposTaxLine.findColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("capturedLineItemNo", capturedLineItemNo)
				.setParameter("itemId", itemId).getResultList();
	}

	// OmsOrposCustOrdItmPickup
	public OmsOrposCustOrdItmPickup persistOmsOrposCustOrdItmPickup(OmsOrposCustOrdItmPickup omsOrposCustOrdItmPickup) {
		em.persist(omsOrposCustOrdItmPickup);
		return omsOrposCustOrdItmPickup;
	}

	public OmsOrposCustOrdItmPickup mergeOmsOrposCustOrdItmPickup(OmsOrposCustOrdItmPickup omsOrposCustOrdItmPickup) {
		return em.merge(omsOrposCustOrdItmPickup);
	}

	public void removeOmsOrposCustOrdItmPickup(OmsOrposCustOrdItmPickup omsOrposCustOrdItmPickup) {
		omsOrposCustOrdItmPickup = em.find(OmsOrposCustOrdItmPickup.class, new OmsOrposCustOrdItmPickupPK(omsOrposCustOrdItmPickup.getCustOrderPicVoSeq(), omsOrposCustOrdItmPickup.getLineItemNo()));
		em.remove(omsOrposCustOrdItmPickup);
	}

	/**
	 * <code>select o from OmsOrposCustOrdItmPickup o</code>
	 * 
	 * @return
	 */
	public List<OmsOrposCustOrdItmPickup> getOmsOrposCustOrdItmPickupFindAll() {
		return em.createNamedQuery("OmsOrposCustOrdItmPickup.findAll").getResultList();
	}

	/**
	 * <code>select o from OmsOrposCustOrdItmPickup o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.custOrderPicVoSeq=:custOrderPicVoSeq</code>
	 */
	public List<OmsOrposCustOrdItmPickup> getOmsOrposCustOrdItmPickupFindAllColumns(BigDecimal omsOrposCustOrderId, BigDecimal custOrderPicVoSeq) {
		return em.createNamedQuery("OmsOrposCustOrdItmPickup.findAllColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("custOrderPicVoSeq", custOrderPicVoSeq)
				.getResultList();
	}

	/**
	 * <code>select o from OmsOrposCustOrdItmPickup o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.custOrderPicVoSeq=:custOrderPicVoSeq and o.fulfillOrderId=:fulfillOrderId</code>
	 * 
	 * @param omsOrposCustOrderId
	 * @param custOrderPicVoSeq
	 * @param fulfillOrderId
	 * @return
	 */
	public List<OmsOrposCustOrdItmPickup> getOmsOrposCustOrdItmPickupFindColumns(BigDecimal omsOrposCustOrderId, BigDecimal custOrderPicVoSeq, String fulfillOrderId) {
		return em.createNamedQuery("OmsOrposCustOrdItmPickup.findColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("custOrderPicVoSeq", custOrderPicVoSeq)
				.setParameter("fulfillOrderId", fulfillOrderId).getResultList();
	}

	/**
	 * <code>select max(o.custOrderPicVoSeq) from OmsOrposCustOrderPickup o where o.omsOrposCustOrderId=:omsOrposCustOrderId</code>
	 */
	public BigDecimal getMaxOmsOrposCustOrderPickUp(String customerOrderId) {
		return (BigDecimal) em.createNamedQuery("OmsOrposCustOrderPickup.max").setParameter("customerOrderId", customerOrderId).getSingleResult();
	}

	public List<Addr> getAddrFindByAddrKeyValue1(String keyValue1) {
		return em.createNamedQuery("Addr.findByAddrKeyValue1").setParameter("keyValue1", keyValue1).getResultList();
	}

	public List<BigDecimal> getOmsOrposCustOrderRtnFindCustOrderRtnSeq(String customerOrderId) {
		return em.createNamedQuery("OmsOrposCustOrderRtn.findCustOrdRtnSeq").setParameter("customerOrderId", customerOrderId).getResultList();
	}

	public BigDecimal getMaxOmsOrposCustOrderRtn(BigDecimal omsOrposCustOrderId) {
		return (BigDecimal) em.createNamedQuery("OmsOrposCustOrderRtn.max").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	public List<OmsOrposCustOrderRtn> getOmsOrposCustOrderRtnFindAllColumns(String customerOrderId) {
		return em.createNamedQuery("OmsOrposCustOrderRtn.findAllColumns").setParameter("customerOrderId", customerOrderId).getResultList();
	}

	public List<OmsOrposCustOrdItmRtn> getOmsOrposCustOrdItmRtnfindColumns(BigDecimal omsOrposCustOrderId, BigDecimal custOrderRtnSeqNo) {
		return em.createNamedQuery("OmsOrposCustOrdItmRtn.findColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("custOrderRtnSeqNo", custOrderRtnSeqNo).getResultList();
	}

	public List<OmsOrposDiscntLine> getOmsOrposDiscntLineFindAllColumns(BigDecimal omsOrposCustOrderId, BigDecimal capturedLineItemNo) {
		return em.createNamedQuery("OmsOrposDiscntLine.findAllColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("capturedLineItemNo", capturedLineItemNo).getResultList();
	}

	public List<OmsOrposDiscntLineRtn> getOmsOrposDiscntLineRtnFindAllColumns(BigDecimal omsOrposCustOrderId, BigDecimal custOrderRtnSeqNo, BigDecimal lineItemNo) {
		return em.createNamedQuery("OmsOrposDiscntLineRt.findAllColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("custOrderRtnSeqNo", custOrderRtnSeqNo)
				.setParameter("lineItemNo", lineItemNo).getResultList();
	}

	public List<OmsOrposTaxLine> getOmsOrposTaxLineFindAllColumns(BigDecimal omsOrposCustOrderId, BigDecimal capturedLineItemNo) {
		return em.createNamedQuery("OmsOrposTaxLine.findAllColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("capturedLineItemNo", capturedLineItemNo).getResultList();
	}

	public List<OmsOrposTaxLineRtn> getOmsOrposTaxLineRtnFindAllColumns(BigDecimal omsOrposCustOrderId, BigDecimal custOrderRtnSeqNo, BigDecimal lineItemNo) {
		return em.createNamedQuery("OmsOrposTaxLineRt.findAllColumns").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("custOrderRtnSeqNo", custOrderRtnSeqNo)
				.setParameter("lineItemNo", lineItemNo).getResultList();
	}

	public OmsOrposCustOrderRtn persistOmsOrposCustOrderRtn(OmsOrposCustOrderRtn omsOrposCustOrderRtn) {
		em.persist(omsOrposCustOrderRtn);
		return omsOrposCustOrderRtn;
	}

	public OmsOrposCustOrderRtn mergeOmsOrposCustOrderRtn(OmsOrposCustOrderRtn omsOrposCustOrderRtn) {
		return em.merge(omsOrposCustOrderRtn);
	}

	public void removeOmsOrposCustOrderRtn(OmsOrposCustOrderRtn omsOrposCustOrderRtn) {
		omsOrposCustOrderRtn = em.find(OmsOrposCustOrderRtn.class, new OmsOrposCustOrderRtnPK(omsOrposCustOrderRtn.getCustOrderRtnSeqNo(), omsOrposCustOrderRtn.getOmsOrposCustOrderId()));
		em.remove(omsOrposCustOrderRtn);
	}

	/** <code>select o from OmsOrposCustOrderRtn o</code> */
	public List<OmsOrposCustOrderRtn> getOmsOrposCustOrderRtnFindAll() {
		return em.createNamedQuery("OmsOrposCustOrderRtn.findAll").getResultList();
	}

	public OmsOrposCustOrdItmRtn persistOmsOrposCustOrdItmRtn(OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn) {
		em.persist(omsOrposCustOrdItmRtn);
		return omsOrposCustOrdItmRtn;
	}

	public OmsOrposCustOrdItmRtn mergeOmsOrposCustOrdItmRtn(OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn) {
		return em.merge(omsOrposCustOrdItmRtn);
	}

	public void removeOmsOrposCustOrdItmRtn(OmsOrposCustOrdItmRtn omsOrposCustOrdItmRt) {
		/*
		 * omsOrposCustOrdItmRt = em.find(OmsOrposCustOrdItmRt.class, new
		 * OmsOrposCustOrdItmRtPK(omsOrposCustOrdItmRt.getCustOrderRtnSeqNo(),
		 * omsOrposCustOrdItmRt.getLineItemNo(),
		 * omsOrposCustOrdItmRt.getOmsOrposCustOrderId()));
		 * em.remove(omsOrposCustOrdItmRt);
		 */
	}

	/** <code>select o from OmsOrposCustOrdItmRt o</code> */
	public List<OmsOrposCustOrdItmRtn> getOmsOrposCustOrdItmRtnFindAll() {
		return em.createNamedQuery("OmsOrposCustOrdItmRtn.findAll").getResultList();
	}

	public OmsOrposDiscntLineRtn persistOmsOrposDiscntLineRtn(OmsOrposDiscntLineRtn omsOrposDiscntLineRtn) {
		em.persist(omsOrposDiscntLineRtn);
		return omsOrposDiscntLineRtn;
	}

	public OmsOrposDiscntLineRtn mergeOmsOrposDiscntLineRtn(OmsOrposDiscntLineRtn omsOrposDiscntLineRtn) {
		return em.merge(omsOrposDiscntLineRtn);
	}

	public void removeOmsOrposDiscntLineRtn(OmsOrposDiscntLineRtn omsOrposDiscntLineRtn) {
		/*
		 * omsOrposDiscntLineRt = em.find(OmsOrposDiscntLineRt.class, new
		 * OmsOrposDiscntLineRtPK(omsOrposDiscntLineRt.getLineItemNo(),
		 * omsOrposDiscntLineRt.getCustOrderRtnSeqNo(),
		 * omsOrposDiscntLineRt.getLineNo(),
		 * omsOrposDiscntLineRt.getOmsOrposCustOrderId()));
		 * em.remove(omsOrposDiscntLineRt);
		 */
	}

	public List<OmsOrposDiscntLineRtn> getOmsOrposDiscntLineRtnFindAll() {
		return em.createNamedQuery("OmsOrposDiscntLineRtn.findAll").getResultList();
	}

	public OmsOrposTaxLineRtn persistOmsOrposTaxLineRtn(OmsOrposTaxLineRtn omsOrposTaxLineRtn) {
		em.persist(omsOrposTaxLineRtn);
		return omsOrposTaxLineRtn;
	}

	public OmsOrposTaxLineRtn mergeOmsOrposTaxLineRtn(OmsOrposTaxLineRtn omsOrposTaxLineRtn) {
		return em.merge(omsOrposTaxLineRtn);
	}

	public void removeOmsOrposTaxLineRtn(OmsOrposTaxLineRtn omsOrposTaxLineRtn) {
		omsOrposTaxLineRtn = em.find(OmsOrposTaxLineRtn.class,
				new OmsOrposTaxLineRtnPK(omsOrposTaxLineRtn.getLineItemNo(), omsOrposTaxLineRtn.getCustOrderRtnSeqNo(), omsOrposTaxLineRtn.getLineNo(), omsOrposTaxLineRtn.getOmsOrposCustOrderId()));
		em.remove(omsOrposTaxLineRtn);
	}

	/** <code>select o from OmsOrposTaxLineRt o</code> */
	public List<OmsOrposTaxLineRtn> getOmsOrposTaxLineRtnFindAll() {
		return em.createNamedQuery("OmsOrposTaxLineRtn.findAll").getResultList();
	}

	public OmsRepublishData persistOmsRepublishData(OmsRepublishData omsRepublishData) {
		em.persist(omsRepublishData);
		return omsRepublishData;
	}

	// For Shipping Classification Web Service
	public UdaItemLov persistUdaItemLov(UdaItemLov udaItemLov) {
		em.persist(udaItemLov);
		return udaItemLov;
	}

	public UdaItemLov mergeUdaItemLov(UdaItemLov udaItemLov) {
		return em.merge(udaItemLov);
	}

	public void removeUdaItemLov(UdaItemLov udaItemLov) {
		udaItemLov = em.find(UdaItemLov.class, new UdaItemLovPK(udaItemLov.getItem(), udaItemLov.getUdaId(), udaItemLov.getUdaValue()));
		em.remove(udaItemLov);
	}

	/** <code>select o from UdaItemLov o</code> */
	public List<UdaItemLov> getUdaItemLovFindAll() {
		return em.createNamedQuery("UdaItemLov.findAll").getResultList();
	}

	/**
	 * <code>select o.udaValue from UdaItemLov o where o.udaId=:udaId and o.item=:item</code>
	 * 
	 * @param udaId
	 * @return
	 */
	public BigDecimal getUdaItemLovFindItemAndUdaValueByUdaId(BigDecimal udaId, String item) {
		return (BigDecimal) em.createNamedQuery("UdaItemLov.findItemAndUdaValue").setParameter("udaId", udaId).setParameter("item", item).getSingleResult();
	}

	public UdaValues persistUdaValues(UdaValues udaValues) {
		em.persist(udaValues);
		return udaValues;
	}

	public UdaValues mergeUdaValues(UdaValues udaValues) {
		return em.merge(udaValues);
	}

	public void removeUdaValues(UdaValues udaValues) {
		udaValues = em.find(UdaValues.class, new UdaValuesPK(udaValues.getUdaId(), udaValues.getUdaValue()));
		em.remove(udaValues);
	}

	/** <code>select o from UdaValues o</code> */
	public List<UdaValues> getUdaValuesFindAll() {
		return em.createNamedQuery("UdaValues.findAll").getResultList();
	}

	/**
	 * @param udaId
	 * @param udaValue
	 * @return
	 */
	/**
	 * <code>select o.udaValueDesc from UdaValues o where o.udaId=:udaId and o.udaValue=:udaValue</code>
	 */
	public String getUdaValueDescByUdaIdAndUdaValue(BigDecimal udaId, BigDecimal udaValue) {
		return (String) em.createNamedQuery("UdaValues.findUdaValueDesc").setParameter("udaId", udaId).setParameter("udaValue", udaValue).getSingleResult();
	}

	// OmsReferenceData for City name feature
	public OmsReferenceData persistOmsReferenceData(OmsReferenceData omsReferenceData) {
		em.persist(omsReferenceData);
		return omsReferenceData;
	}

	public OmsReferenceData mergeOmsReferenceData(OmsReferenceData omsReferenceData) {
		return em.merge(omsReferenceData);
	}

	public void removeOmsReferenceData(OmsReferenceData omsReferenceData) {
		omsReferenceData = em.find(OmsReferenceData.class, omsReferenceData.getRefId());
		em.remove(omsReferenceData);
	}

	/** <code>select o from OmsReferenceData o</code> */
	public List<OmsReferenceData> getOmsReferenceDataFindAll() {
		return em.createNamedQuery("OmsReferenceData.findAll").getResultList();
	}

	/**
	 * <code>select o.refValue from OmsReferenceData o where o.refKey1=:refKey1 and o.refKey2=:refKey2 and o.refKey3=:refKey3</code>
	 */
	public String getOmsReferenceDataFindRefValue(String refKey1, String refKey2, String refKey3) {
		return (String) em.createNamedQuery("OmsReferenceData.findRefValue").setParameter("refKey1", refKey1).setParameter("refKey2", refKey2).setParameter("refKey3", refKey3).getSingleResult();
	}

	/**
	 * <code>select o.refValue from OmsReferenceData o where o.refKey1=:refKey1 and o.refKey2=:refKey2 and o.refKey3=:refKey3</code>
	 */
	public String getOmsReferenceDataFindByISOCode(String refKey1, String refKey3) {
		return (String) em.createNamedQuery("OmsReferenceData.findByISOCode").setParameter("refKey1", refKey1).setParameter("refKey3", refKey3).getSingleResult();
	}

	// XXItemCustomInfo
	public XxItemCustomInfo persistXxItemCustomInfo(XxItemCustomInfo xxItemCustomInfo) {
		em.persist(xxItemCustomInfo);
		return xxItemCustomInfo;
	}

	public XxItemCustomInfo mergeXxItemCustomInfo(XxItemCustomInfo xxItemCustomInfo) {
		return em.merge(xxItemCustomInfo);
	}

	public void removeXxItemCustomInfo(XxItemCustomInfo xxItemCustomInfo) {
		xxItemCustomInfo = em.find(XxItemCustomInfo.class, xxItemCustomInfo.getItem());
		em.remove(xxItemCustomInfo);
	}

	/** <code>select o from XxItemCustomInfo o</code> */
	public List<XxItemCustomInfo> getXxItemCustomInfoFindAll() {
		return em.createNamedQuery("XxItemCustomInfo.findAll").getResultList();
	}

	/** <code>select o.courier from XxItemCustomInfo o where o.item=:item</code> */
	public String getXxItemCustomInfoFindCourierValue(String item) {
		return (String) em.createNamedQuery("XxItemCustomInfo.findCourierValue").setParameter("item", item).getSingleResult();
	}

	public BigDecimal getStoreFindOrgUnit(BigDecimal store) {
		return (BigDecimal) em.createNamedQuery("Store.findOrgUnit").setParameter("store", store).getSingleResult();
	}

	public BigDecimal getStoreFindChannelId(BigDecimal store) {
		return (BigDecimal) em.createNamedQuery("Store.findChannelId").setParameter("store", store).getSingleResult();
	}

	public BigDecimal getPartnerOrgUnitFindOrgUnitId(BigDecimal partner) {
		return (BigDecimal) em.createNamedQuery("PartnerOrgUnit.findOrgUnitId").setParameter("partner", partner).getSingleResult();
	}

	public List<String> getCodeDetailFindCode(String codeType) {
		return em.createNamedQuery("CodeDetail.findCode").setParameter("codeType", codeType).getResultList();
	}

	public BigDecimal getOmsCustOrdTenderCancelFindByOmsCancelIdandomsCustOrdNumberByMaxSeqNo(BigDecimal omsCancelId, BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustCancelTender.findbyOmscancelIdOmsCustOrdNumberMaxOfTenderSeqNo").setParameter("omsCancelId", omsCancelId)
				.setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	public OmsBackOrderDtl persistOmsBackOrderDtl(OmsBackOrderDtl omsBackOrderDtl) {
		em.persist(omsBackOrderDtl);
		return omsBackOrderDtl;
	}

	public OmsBackOrderDtl mergeOmsBackOrderDtl(OmsBackOrderDtl omsBackOrderDtl) {
		return em.merge(omsBackOrderDtl);
	}

	/**
	 * <code>select o.omsCustOrdNo from OmsCustOrdItem o where o.backorderInd=:backorderInd </code>
	 */
	public List<OmsBackOrderDtl> getOmsBackOrderDtlFindBackOrders(Timestamp fulInvAvlDate) {
		return em.createNamedQuery("OmsBackOrderDtl.findBackOrders").setParameter("fulInvAvlDate", fulInvAvlDate).getResultList();
	}

	public BigDecimal getOmsBackOrderDtlFindAssignedInvOrders(BigDecimal sourceLoc, String item) {
		return (BigDecimal) em.createNamedQuery("OmsBackOrderDtl.findAssignedInvOrders").setParameter("sourceLoc", sourceLoc).setParameter("item", item).getSingleResult();
	}

	public List<BigDecimal> getOmsBackOrderDtlFindOmsCustOrdNoForBackOrder(Timestamp fulInvAvlDate) {
		return em.createNamedQuery("OmsBackOrderDtl.findOmsCustOrdNoForBackOrder").getResultList();
	}

	public List<OmsBackOrderDtl> getOmsBackOrderDtlFindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsBackOrderDtl.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	public List<OmsBackOrderDtl> getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNo(BigDecimal omsCustOrdNo, BigDecimal lineNo) {
		return em.createNamedQuery("OmsBackOrderDtl.findByOmsCustOrdNoAndLinNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo).getResultList();
	}

	public OmsBackOrderDtl getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNoAndLoc(BigDecimal omsCustOrdNo, BigDecimal lineNo, BigDecimal sourceLoc) {
		return (OmsBackOrderDtl) em.createNamedQuery("OmsBackOrderDtl.findByOmsCustOrdNoAndLinNoAndLoc").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo)
				.setParameter("sourceLoc", sourceLoc).getSingleResult();
	}

	public List<OmsBackOrderDtl> getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(BigDecimal omsCustOrdNo, String item, BigDecimal lineNo) {
		return em.createNamedQuery("OmsBackOrderDtl.findbyomsCustOrderNoItemLineNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("item", item).setParameter("lineNo", lineNo)
				.getResultList();
	}

	public String getOmsErrorCodesFindByonlyErrorCode(String omsErrorCode) {
		return (String) em.createNamedQuery("OmsErrorCodes.findByindByonlyErrorCode").setParameter("omsErrorCode", omsErrorCode).getSingleResult();
	}

	// itemmaster Inventory Indicator
	public String getItemMasterFindInventoryInd(String item, BigDecimal dept) {
		return (String) em.createNamedQuery("ItemMaster.findItemInventoryInd").setParameter("item", item).setParameter("dept", dept).getSingleResult();

	}

	public OmsOrposRefundTender persistOmsOrposRefundTender(OmsOrposRefundTender omsOrposRefundTender) {
		em.persist(omsOrposRefundTender);
		return omsOrposRefundTender;
	}

	public OmsOrposRefundTender mergeOmsOrposRefundTender(OmsOrposRefundTender omsOrposRefundTender) {
		return em.merge(omsOrposRefundTender);
	}

	public void removeOmsOrposRefundTender(OmsOrposRefundTender omsOrposRefundTender) {
		omsOrposRefundTender = em.find(OmsOrposRefundTender.class, new OmsOrposRefundTenderPK(omsOrposRefundTender.getOmsCustOrdNo(), omsOrposRefundTender.getTenderSeqNo()));
		em.remove(omsOrposRefundTender);
	}

	/** <code>select o from OmsOrposRefundTender o</code> */
	public List<OmsOrposRefundTender> getOmsOrposRefundTenderFindAll() {
		return em.createNamedQuery("OmsOrposRefundTender.findAll").getResultList();
	}

	public BigDecimal getOmsOrpospaymentFindMaxSeqNoByomsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
		return (BigDecimal) em.createNamedQuery("OmsOrposPayment.findMaxPaymentSeqNo").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	public BigDecimal getOmsOrposRefundCustOrdTenderFindMaxTenderSeqNo(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsOrposRefundTender.findMaxTenderSeqNoByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();

	}

	public BigDecimal getOmsOrposCustOrderHeadFindByCustomerOrderId(String customerOrderId) {
		return (BigDecimal) em.createNamedQuery("OmsOrposCustOrderHead.findBycustomerOrderId").setParameter("customerOrderId", customerOrderId).getSingleResult();
	}

	public OmsCoCancelHead getOmsCoCancelHeadFindCustOrderNoByomsCancelId(BigDecimal omsCancelId) {
		return (OmsCoCancelHead) em.createNamedQuery("OmsCoCancelHead.findByomscancelId").setParameter("omsCancelId", omsCancelId).getSingleResult();
	}

	public List<OmsOrposCustOrdItmRtn> getOmsOrposCustOrdItmRtnFindByOmsOrposCustOrdIdAndLineItemNo(BigDecimal omsOrposCustOrderId, BigDecimal lineItemNo) {
		return em.createNamedQuery("OmsOrposCustOrdItmRtn.findByOmsOrposCustOrdIdAndLineItemNo").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).setParameter("lineItemNo", lineItemNo)
				.getResultList();
	}

	public OmsOrposCustOrderHead getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(String customerOrderId, String status) {
		return (OmsOrposCustOrderHead) em.createNamedQuery("OmsOrposCustOrderHead.findBycustomerOrderIdandstatus").setParameter("customerOrderId", customerOrderId).setParameter("status", status)
				.getSingleResult();
	}

	public List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadfindBycustometOrderIdandstatusList(String customerOrderId, String status) {
		return em.createNamedQuery("OmsOrposCustOrderHead.findBycustomerOrderIdandstatusList").setParameter("customerOrderId", customerOrderId).setParameter("status", status).getResultList();

	}

	public OmsCustOrdHead getOmsCustOrdHeadfindByCustOrdNoAndStatus(String custOrderNo, String status) {
		return (OmsCustOrdHead) em.createNamedQuery("OmsCustOrdHead.findByCustOrdNoAndStatus").setParameter("custOrderNo", custOrderNo).setParameter("status", status).getSingleResult();
	}

	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindByOmsCustOrdNoandLineNoandItemId(BigDecimal omsCustOrdNo, BigDecimal lineNo, String item) {
		return em.createNamedQuery("OmsCoFulfillDetail.findByOmsCustOrdNoandLineNoandItemId").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo).setParameter("item", item)
				.getResultList();
	}

	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindByItem(BigDecimal omsCustOrdNo, BigDecimal lineNo, String item) {
		return em.createNamedQuery("OmsCoFulfillDetail.findByItem").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo).setParameter("item", item).getResultList();
	}

	public List<Tsfdetail> getTsfdetailFindQty(BigDecimal tsfNo, String item) {
		return em.createNamedQuery("Tsfdetail.findQty").setParameter("tsfNo", tsfNo).setParameter("item", item).getResultList();
	}

	public PosTenderTypeHead persistPosTenderTypeHead(PosTenderTypeHead posTenderTypeHead) {
		em.persist(posTenderTypeHead);
		return posTenderTypeHead;
	}

	public PosTenderTypeHead mergePosTenderTypeHead(PosTenderTypeHead posTenderTypeHead) {
		return em.merge(posTenderTypeHead);
	}

	public void removePosTenderTypeHead(PosTenderTypeHead posTenderTypeHead) {
		posTenderTypeHead = em.find(PosTenderTypeHead.class, posTenderTypeHead.getTenderTypeId());
		em.remove(posTenderTypeHead);
	}

	/** <code>select o from PosTenderTypeHead o</code> */
	public List<PosTenderTypeHead> getPosTenderTypeHeadFindAll() {
		return em.createNamedQuery("PosTenderTypeHead.findAll").getResultList();
	}

	public String getPosTenderTypeHeadFindByTenderTypeId(BigDecimal tenderTypeId) {
		return (String) em.createNamedQuery("PosTenderTypeHead.findTenderTypeGroup").setParameter("tenderTypeId", tenderTypeId).getSingleResult();
	}

	public String getOmsSystemParameterFindByParameterValue(String parameterValue) {
		return (String) em.createNamedQuery("OmsSystemParameters.findByParameterValue").setParameter("parameterValue", parameterValue).getSingleResult();
	}

	public OmsOrposCustOrderHead getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
		return (OmsOrposCustOrderHead) em.createNamedQuery("OmsOrposCustOrderHead.findByomsOrposCustOrderId").setParameter("omsOrposCustOrderId", omsOrposCustOrderId).getSingleResult();
	}

	public OmsCoCancelItem getOmsCoCancelItemFindByOmsCancelIdandLineNo(BigDecimal omsCancelId, BigDecimal lineNo) {
		return (OmsCoCancelItem) em.createNamedQuery("OmsCoCancelItem.findByomsCancelIdandLineNo").setParameter("omsCancelId", omsCancelId).setParameter("lineNo", lineNo).getSingleResult();

	}

	public List<OmsCoFulfillDetail> getOmsCoFulfillDetailfindByOmsCustOrdNoLineNoandItemsrcandFul(BigDecimal omsCustOrdNo, BigDecimal lineNo) {
		return em.createNamedQuery("OmsCoFulfillDetail.findBuOmsCustOrdNoandLineNoandsrcandFullfill").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo).getResultList();
	}

	public String getItemMasterFindItemDescSecondary(String item) {
		return (String) em.createNamedQuery("ItemMaster.findItemDescSecondary").setParameter("item", item).getSingleResult();
	}

	/**
	 * @param customerOrderNo
	 * @return
	 */
	public BigDecimal getOrdCustCountofCustomerOrderNo(String customerOrderNo) {
		return (BigDecimal) em.createNamedQuery("Ordcust.count").setParameter("customerOrderNo", customerOrderNo).getSingleResult();
	}

	public OmsOrposMasterAudit persistOmsOrposMasterAudit(OmsOrposMasterAudit omsOrposMasterAudit) {
		em.persist(omsOrposMasterAudit);
		return omsOrposMasterAudit;
	}

	public OmsOrposMasterAudit mergeOmsOrposMasterAudit(OmsOrposMasterAudit omsOrposMasterAudit) {
		return em.merge(omsOrposMasterAudit);
	}

	public void removeOmsOrposMasterAudit(OmsOrposMasterAudit omsOrposMasterAudit) {
		omsOrposMasterAudit = em.find(OmsOrposMasterAudit.class, new OmsOrposMasterAuditPK(omsOrposMasterAudit.getLineItemNo(), omsOrposMasterAudit.getOmsCustOrderNo(),
				omsOrposMasterAudit.getOrderId(), omsOrposMasterAudit.getOrposTransactionNumber()));
		em.remove(omsOrposMasterAudit);
	}

	/** <code>select o from OmsOrposMasterAudit o</code> */
	public List<OmsOrposMasterAudit> getOmsOrposMasterAuditFindAll() {
		return em.createNamedQuery("OmsOrposMasterAudit.findAll").getResultList();
	}

	public OmsCustOrdItemDisc persistOmsCustOrdItemDisc(OmsCustOrdItemDisc omsCustOrdItemDisc) {
		em.persist(omsCustOrdItemDisc);
		return omsCustOrdItemDisc;
	}

	public OmsCustOrdItemDisc mergeOmsCustOrdItemDisc(OmsCustOrdItemDisc omsCustOrdItemDisc) {
		return em.merge(omsCustOrdItemDisc);
	}

	public void removeOmsCustOrdItemDisc(OmsCustOrdItemDisc omsCustOrdItemDisc) {
		omsCustOrdItemDisc = em.find(OmsCustOrdItemDisc.class, new OmsCustOrdItemDiscPK(omsCustOrdItemDisc.getDiscLineNo(), omsCustOrdItemDisc.getLineNo(), omsCustOrdItemDisc.getOmsCustOrdNo()));
		em.remove(omsCustOrdItemDisc);
	}

	/** <code>select o from OmsCustOrdItemDisc o</code> */
	public List<OmsCustOrdItemDisc> getOmsCustOrdItemDiscFindAll() {
		return em.createNamedQuery("OmsCustOrdItemDisc.findAll").getResultList();
	}

	public List<OmsCustOrdItemDisc> getOmsCustOrdItemDiscFindByOmsCustordNoLineNo(BigDecimal omsCustOrdNo, BigDecimal lineNo) {
		return em.createNamedQuery("OmsCustOrdItemDisc.FindByOmsCustordNoLineNo").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo).getResultList();
	}

	public OmsSimCancelTsfDetail persistOmsSimCancelTsfDetail(OmsSimCancelTsfDetail omsSimCancelTsfDetail) {
		em.persist(omsSimCancelTsfDetail);
		return omsSimCancelTsfDetail;
	}

	public OmsSimCancelTsfDetail mergeOmsSimCancelTsfDetail(OmsSimCancelTsfDetail omsSimCancelTsfDetail) {
		return em.merge(omsSimCancelTsfDetail);
	}

	public void removeOmsSimCancelTsfDetail(OmsSimCancelTsfDetail omsSimCancelTsfDetail) {
		omsSimCancelTsfDetail = em.find(OmsSimCancelTsfDetail.class, omsSimCancelTsfDetail.getOmsSimTsfCancelSeq());
		em.remove(omsSimCancelTsfDetail);
	}

	/** <code>select o from OmsSimCancelTsfDetail o</code> */
	public List<OmsSimCancelTsfDetail> getOmsSimCancelTsfDetailFindAll() {
		return em.createNamedQuery("OmsSimCancelTsfDetail.findAll").getResultList();
	}

	public List<OmsOrposMasterAudit> getOmsOrposMasterAuditfindByOrderIdandOrposTransactionNumber(String orderId, String orposTransactionNumber) {
		return em.createNamedQuery("OmsOrposMasterAudit.findByOrderIdandOrposTransactionNumber").setParameter("orderId", orderId).setParameter("orposTransactionNumber", orposTransactionNumber)
				.getResultList();
	}

	public OmsOrposDiscntLine getOmsOrposDiscntLineCustOrdIdLineNoDiscntLineNo(BigDecimal omsOrposCustOrderId, BigDecimal lineNo, BigDecimal capturedLineItemNo) {
		return (OmsOrposDiscntLine) em.createNamedQuery("OmsOrposDiscntLine.findLineNoandDiscLineNo").setParameter("omsOrposCustOrderId", omsOrposCustOrderId)
				.setParameter("capturedLineItemNo", capturedLineItemNo).setParameter("lineNo", lineNo).getSingleResult();
	}

	// Added for finding sum of discounts
	public BigDecimal getOmsCustOrdItemDiscfindByOmsCustordNoLineNoUnitDiscnt(BigDecimal omsCustOrdNo, BigDecimal lineNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdItemDisc.FindByOmsCustordNoLineNoUnitDiscnt").setParameter("omsCustOrdNo", omsCustOrdNo).setParameter("lineNo", lineNo).getSingleResult();
	}

	public OmsTsfCancelledQtySumm persistOmsTsfCancelledQtySumm(OmsTsfCancelledQtySumm omsTsfCancelledQtySumm) {
		em.persist(omsTsfCancelledQtySumm);
		return omsTsfCancelledQtySumm;
	}

	public OmsTsfCancelledQtySumm mergeOmsTsfCancelledQtySumm(OmsTsfCancelledQtySumm omsTsfCancelledQtySumm) {
		return em.merge(omsTsfCancelledQtySumm);
	}

	public void removeOmsTsfCancelledQtySumm(OmsTsfCancelledQtySumm omsTsfCancelledQtySumm) {
		omsTsfCancelledQtySumm = em.find(OmsTsfCancelledQtySumm.class,
				new OmsTsfCancelledQtySummPK(omsTsfCancelledQtySumm.getCustOrderNo(), omsTsfCancelledQtySumm.getOmsCustOrdNo(), omsTsfCancelledQtySumm.getTsfNo()));
		em.remove(omsTsfCancelledQtySumm);
	}

	/** <code>select o from OmsTsfCancelledQtySumm o</code> */
	public List<OmsTsfCancelledQtySumm> getOmsTsfCancelledQtySummFindAll() {
		return em.createNamedQuery("OmsTsfCancelledQtySumm.findAll").getResultList();
	}

	public OmsTsfCancelledQtySumm getOmsTsfCancelledQtySummfindByCustOrdNoTsfnoandOmsCustOrdNo(String custOrderNo, BigDecimal tsfNo, BigDecimal omsCustOrdNo) {
		return (OmsTsfCancelledQtySumm) em.createNamedQuery("OmsTsfCancelledQtySumm.findByCustOrdNoTsfnoandOmsCustOrdNo").setParameter("custOrderNo", custOrderNo).setParameter("tsfNo", tsfNo)
				.setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();

	}

	public List<Tsfdetail> getTsfDetailTransferQty(BigDecimal tsfNo) {
		return em.createNamedQuery("Tsfdetail.findTransferQty").setParameter("tsfNo", tsfNo).getResultList();

	}

	public int updateOmsCustOrdHeadforOrdPaymentStatus(BigDecimal omsCustOrdNo) {
		Query q = em.createQuery("update OmsCustOrdHead o set o.ordPaymentStatus='S',o.status='S' where  o.omsCustOrdNo=:omsCustOrdNo");
		q.setParameter("omsCustOrdNo", omsCustOrdNo);
		return q.executeUpdate();
	}

	public OmsItemLocSync persistOmsItemLocSync(OmsItemLocSync omsItemLocSync) {
		em.persist(omsItemLocSync);
		return omsItemLocSync;
	}

	public OmsItemLocSync mergeOmsItemLocSync(OmsItemLocSync omsItemLocSync) {
		return em.merge(omsItemLocSync);
	}

	public int removeOmsItemLocSync(OmsItemLocSync omsItemLocSync) {
		return em.createNamedQuery("OmsItemLocSync.removeById").setParameter("item", omsItemLocSync.getItem()).setParameter("loc", omsItemLocSync.getLoc()).executeUpdate();
	}

	/** <code>select o from OmsItemLocSync o</code> */
	public List<OmsItemLocSync> getOmsItemLocSyncFindAll() {
		return em.createNamedQuery("OmsItemLocSync.findAll").getResultList();
	}

	public String getItemFromItemLocSync(String item, BigDecimal dept) {
		try {
			return (String) em.createNamedQuery("OmsItemLocSync.findItemLoc").setParameter("item", item).setParameter("loc", dept).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public String getMaxFulFilOrdNofromOrdCust(String customerOrderNo) {
		return (String) em.createNamedQuery("Ordcust.findmaxfulfilordNo").setParameter("customerOrderNo", customerOrderNo).getSingleResult();

	}

	public OmsPaymentSync persistOmsPaymentSync(OmsPaymentSync omsPaymentSync) {
		em.persist(omsPaymentSync);
		return omsPaymentSync;
	}

	public OmsPaymentSync mergeOmsPaymentSync(OmsPaymentSync omsPaymentSync) {
		return em.merge(omsPaymentSync);
	}

	public void removeOmsPaymentSync(OmsPaymentSync omsPaymentSync) {
		omsPaymentSync = em.find(OmsPaymentSync.class, new OmsPaymentSyncPK(omsPaymentSync.getLineNo(), omsPaymentSync.getLocation(), omsPaymentSync.getOmsCustOrdNo()));
		em.remove(omsPaymentSync);
	}

	/** <code>select o from OmsPaymentSync o</code> */
	public List<OmsPaymentSync> getOmsPaymentSyncFindAll() {
		return em.createNamedQuery("OmsPaymentSync.findAll").getResultList();
	}

	public BigDecimal getQuantityOmsPaymentSyncBasedOnItemAndLoc(String item, BigDecimal loc) {
		return (BigDecimal) em.createNamedQuery("OmsPaymentSync.findQuantityBasedItemAndLoc").setParameter("item", item).setParameter("location", loc).getSingleResult();
	}

	public List<OmsPaymentSync> getOmsPaymentSychfindByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsPaymentSync.findByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	public List<OmsRtlogPublishLog> getOmsRtlogPublishLogFindByOmsCustOrderNo(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsRtlogPublishLog.findByOmsCustOrderNo").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	// Added for Rollback Logic
	public List<Ordcust> getOrdcustFindByCustomerOrderNoandStatus(String customerOrderNo) {
		return em.createNamedQuery("Ordcust.findByCustomerOrderNoandStatus").setParameter("customerOrderNo", customerOrderNo).getResultList();
	}

	public OrdcustDetail persistOrdcustDetail(OrdcustDetail ordcustDetail) {
		em.persist(ordcustDetail);
		return ordcustDetail;
	}

	public OrdcustDetail mergeOrdcustDetail(OrdcustDetail ordcustDetail) {
		return em.merge(ordcustDetail);
	}

	/**
	 * @param ordcustDetail
	 */
	public void removeOrdcustDetail(OrdcustDetail ordcustDetail) {
		ordcustDetail = em.find(OrdcustDetail.class, ordcustDetail.getOrdcustNo());
		em.remove(ordcustDetail);
	}

	/** <code>select o from OrdcustDetail o</code> */
	public List<OrdcustDetail> getOrdcustDetailFindAll() {
		return em.createNamedQuery("OrdcustDetail.findAll").getResultList();
	}

	// Added for rollback Logic
	public List<OrdcustDetail> getOrdcustDetailFindByOrdCustNo(BigDecimal ordcustNo) {
		return em.createNamedQuery("OrdcustDetail.findByOrdCustNo").setParameter("ordcustNo", ordcustNo).getResultList();
	}

	public OmsUnapprovedTransfers persistOmsUnapprovedTransfers(OmsUnapprovedTransfers omsUnapprovedTransfers) {
		em.persist(omsUnapprovedTransfers);
		return omsUnapprovedTransfers;
	}

	public OmsUnapprovedTransfers mergeOmsUnapprovedTransfers(OmsUnapprovedTransfers omsUnapprovedTransfers) {
		return em.merge(omsUnapprovedTransfers);
	}

	public void removeOmsUnapprovedTransfers(OmsUnapprovedTransfers omsUnapprovedTransfers) {
		omsUnapprovedTransfers = em.find(OmsUnapprovedTransfers.class,
				new OmsUnapprovedTransfersPK(omsUnapprovedTransfers.getItem(), omsUnapprovedTransfers.getLocation(), omsUnapprovedTransfers.getOmsCustOrdNo(), omsUnapprovedTransfers.getTsfNo()));
		em.remove(omsUnapprovedTransfers);
	}

	/** <code>select o from OmsUnapprovedTransfers o</code> */
	public List<OmsUnapprovedTransfers> getOmsUnapprovedTransfersFindAll() {
		return em.createNamedQuery("OmsUnapprovedTransfers.findAll").getResultList();
	}

	public OmsUnapprovedTransfers getOmsUnapprovedTransfersFindByOmsCustOrderNo(BigDecimal tsfNo, String item, BigDecimal location) {
		return (OmsUnapprovedTransfers) em.createNamedQuery("OmsUnapprovedTransfers.findQtyByItemAndLoc").setParameter("tsfNo", tsfNo).setParameter("item", item).setParameter("location", location)
				.getSingleResult();
	}

	public List<OmsUnapprovedTransfers> getOmsUnapprovedTransfersFindByQtyByLoc(String item, BigDecimal location) {
		return em.createNamedQuery("OmsUnapprovedTransfers.findQtyByLoc").setParameter("item", item).setParameter("location", location).getResultList();
	}

	public List<OmsUnapprovedTransfers> getOmsUnapprovedTransfersfindByTsfNoandOmsCustOrdNo(BigDecimal tsfNo, BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsUnapprovedTransfers.findBytsfNoandOmsCustOrdNo").setParameter("tsfNo", tsfNo).setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();

	}

	public List<OmsCoCancelHead> getOmsCoCancelHeadFindByCustOrdNoforCancelledOrder(String custOrdNo, String refundCompltInd) {
		return em.createNamedQuery("OmsCoCancelHead.findByCustOrdNoforCancelledOrder").setParameter("custOrdNo", custOrdNo).setParameter("refundCompltInd", refundCompltInd).getResultList();

	}

	public List<OmsCoCancelHead> getOmsCoCancelHeadFindByCustOrdNoforORPOSRefundOrder(String custOrdNo, String refundCompltInd, String refundOption) {
		return em.createNamedQuery("OmsCoCancelHead.findByCustOrdNoforORPOSRefundOrder").setParameter("custOrdNo", custOrdNo).setParameter("refundCompltInd", refundCompltInd)
				.setParameter("refundOption", refundOption).getResultList();

	}

	public List<OmsRmaReq> getOmsRmaReqFindByOmscustOrdNoforRMAReturn(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsRmaReq.findByOmscustOrdNoforRmaReturn").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	public List<OmsRmaReq> getOmsRmaReqFindByOmscustOrdNoforRMAORPOS(BigDecimal omsCustOrdNo) {
		return em.createNamedQuery("OmsRmaReq.findByOmscustOrdNoforORPOSRefund").setParameter("omsCustOrdNo", omsCustOrdNo).getResultList();
	}

	public OmsResUnresvCustOrderLog persistOmsResUnresvCustOrderLog(OmsResUnresvCustOrderLog omsResUnresvCustOrderLog) {
		em.persist(omsResUnresvCustOrderLog);
		return omsResUnresvCustOrderLog;
	}

	public OmsResUnresvCustOrderLog mergeOmsResUnresvCustOrderLog(OmsResUnresvCustOrderLog omsResUnresvCustOrderLog) {
		return em.merge(omsResUnresvCustOrderLog);
	}

	public void removeOmsResUnresvCustOrderLog(OmsResUnresvCustOrderLog omsResUnresvCustOrderLog) {
		omsResUnresvCustOrderLog = em.find(OmsResUnresvCustOrderLog.class, new OmsResUnresvCustOrderLogPK(omsResUnresvCustOrderLog.getLogId(), omsResUnresvCustOrderLog.getOmsCustOrdNo()));
		em.remove(omsResUnresvCustOrderLog);
	}

	/** <code>select o from OmsResUnresvCustOrderLog o</code> */
	public List<OmsResUnresvCustOrderLog> getOmsResUnresvCustOrderLogFindAll() {
		return em.createNamedQuery("OmsResUnresvCustOrderLog.findAll").getResultList();
	}

	public List<ItemSuppCountryDim> getItemSuppCountryDimFindByItemId(String item) {
		return em.createNamedQuery("ItemSuppCountryDim.findByItemId").setParameter("item", item).setMaxResults(1).getResultList();
	}

	public BigDecimal getOmsCustOrdTenderFindTenderTypeIdByOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		return (BigDecimal) em.createNamedQuery("OmsCustOrdTender.findTenderTypeIdByOmsCustOrdNo").setParameter("omsCustOrdNo", omsCustOrdNo).getSingleResult();
	}

	public OmsCustOrdTracking persistOmsCustOrdTracking(OmsCustOrdTracking omsCustOrdTracking) {
		em.persist(omsCustOrdTracking);
		return omsCustOrdTracking;
	}

	public OmsCustOrdTracking mergeOmsCustOrdTracking(OmsCustOrdTracking omsCustOrdTracking) {
		return em.merge(omsCustOrdTracking);
	}

	public void removeOmsCustOrdTracking(OmsCustOrdTracking omsCustOrdTracking) {
		omsCustOrdTracking = em.find(OmsCustOrdTracking.class, omsCustOrdTracking.getTrackId());
		em.remove(omsCustOrdTracking);
	}

	public String getOrdcustFindBillPhoneByCustomerOrderNo(String customerOrderNo) {

		String s = (String) em.createNamedQuery("Ordcust.findBillPhoneNo").setParameter("customerOrderNo", customerOrderNo).getSingleResult();
		return (String) em.createNamedQuery("Ordcust.findBillPhoneNo").setParameter("customerOrderNo", customerOrderNo).getSingleResult();
	}

	public CfsSmsEmailStatusInfo persistCfsSmsEmailStatusInfo(CfsSmsEmailStatusInfo cfsSmsEmailStatusInfo) {
		em.persist(cfsSmsEmailStatusInfo);
		return cfsSmsEmailStatusInfo;
	}

	public CfsSmsEmailStatusInfo mergeCfsSmsEmailStatusInfo(CfsSmsEmailStatusInfo cfsSmsEmailStatusInfo) {
		return em.merge(cfsSmsEmailStatusInfo);
	}

	public void removeCfsSmsEmailStatusInfo(CfsSmsEmailStatusInfo cfsSmsEmailStatusInfo) {
		cfsSmsEmailStatusInfo = em.find(CfsSmsEmailStatusInfo.class, cfsSmsEmailStatusInfo.getId());
		em.remove(cfsSmsEmailStatusInfo);
	}

	/* <code>select o from CfsSmsEmailStatusInfo o</code> */
	public List<CfsSmsEmailStatusInfo> getCfsSmsEmailStatusInfoFindAll() {
		return em.createNamedQuery("CfsSmsEmailStatusInfo.findAll").getResultList();
	}

	public void persistOmsWSPublishData(OmsWSPublishData omsWSPublishData) {
		em.persist(omsWSPublishData);
	}
}
