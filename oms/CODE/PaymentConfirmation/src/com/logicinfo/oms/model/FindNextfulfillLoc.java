package com.logicinfo.oms.model;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;

import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.util.OMSUtil;

//import com.logicinfo.oms.utils.ProjectUtils;

import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

import java.math.BigDecimal;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

public class FindNextfulfillLoc {
	private final static Logger log = Logger.getLogger(FindNextfulfillLoc.class.getName());

	public FindNextfulfillLoc() {
		super();
	}

	public BigDecimal processFulfillmentMatrixGetCombID(BigDecimal reqId, String itemType, String custCity,
			String modeOfDelv) throws SOAPException {
		log.info("***Start processFulfillmentMatrixGetCombID***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal combinationID = null;
		String deliveryZone = null;
		String marketPlaceInd = null;
        String applicationId = "E-COMMERCE";
        String shipToStore = "N";
		try {
			log.info("reqId" + reqId + "itemType" + itemType + "custCity" + custCity + "modeOfDelv" + modeOfDelv);
			combinationID = session.getOmsFulfillMatrixExtHeadFindCombination(reqId, itemType, custCity, modeOfDelv,
					deliveryZone, marketPlaceInd, applicationId, shipToStore);
			log.info(combinationID);
		} catch (Exception e) {

			// e.printStackTrace();
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Combination ID unavailable for the given record"));
		}
		// ProjectUtils.setCombinationID(combinationID);

		return combinationID;
	}

	public BigDecimal processFulfillmentMatrixGetCombIDWoCity(BigDecimal reqId, String itemType, String modeOfDelv) throws EntityAlreadyExistsWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			SOAPException {
		log.info("***Start processFulfillmentMatrixGetCombIDWoCity***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal combinationID = null;
		String deliveryZone = null;
		String marketPlaceInd = null;
        String applicationId = "E-COMMERCE";
        String shipToStore = "N";
		try {
			log.info("reqId" + reqId + "itemType" + itemType + "modeOfDelv" + modeOfDelv);
			combinationID = session.getOmsFulfillMatrixExtHeadFindCombinationWoCity(reqId, itemType, modeOfDelv,
					deliveryZone, marketPlaceInd, applicationId, shipToStore);
			log.info(combinationID);
		} catch (Exception e) {
			// OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
			// omsCustomerOrderBean.rollback();
			e.printStackTrace();
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Combination ID unavailable for the given record"));
		}

		return combinationID;
	}

	public OmsFulfillMatrixExtDetail processFulfillmentMatrix(BigDecimal combinationID, int priority) throws EntityAlreadyExistsWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,

			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			SOAPException {
		log.info("***Start processFulfillmentMatrix***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsFulfillMatrixExtDetail detailFindpriority = null;

		try {

			while (detailFindpriority == null) {
				log.info("combinationID:" + combinationID);
				log.info(priority);
				detailFindpriority = session.getOmsFulfillMatrixExtDetailFindDetail(combinationID,
						new BigDecimal(priority));
				log.info(detailFindpriority.getLocation());
			}
		} catch (Exception e) {
			log.info("processFulfillmentMatrix exception");
			// OMSCustomerOrderBean omsCustomerOrderBean=new OMSCustomerOrderBean();
			// omsCustomerOrderBean.rollback();
			e.printStackTrace();
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Next location unavailable for the given record"));
		}

		return detailFindpriority;
	}

}
