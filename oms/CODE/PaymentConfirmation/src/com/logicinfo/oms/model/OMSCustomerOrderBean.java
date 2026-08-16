package com.logicinfo.oms.model;


import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.OmsErrorCodesConstant;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsPaymentSync;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderService;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.SOAPException;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;


public class OMSCustomerOrderBean {
    public final static Logger log = Logger.getLogger(com.logicinfo.oms.model.OMSCustomerOrderBean.class.getName());

    public OMSCustomerOrderBean() {
        super();
    }

    BigDecimal omsCustOrdNo = null;
    ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> completeMap =
        new ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
    String extCustOrdNo = "";

    public BigDecimal getOmsCustOrdNo(CoPaymentConf input) throws SOAPException {
        log.info("inside getOmsCustOrdNo");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        String subCustomerOrdNo = "1";
        if (input.getCustomerSubOrderNo() != null) {
            subCustomerOrdNo = input.getCustomerSubOrderNo();
        }
        omsCustOrdNo = BigDecimal.ZERO;
        List<BigDecimal> list = null;
        OmsCustOrdHead omsCustOrdHead = null;


        try {
            log.info("input.getCustomerOrderNo() " + input.getCustomerOrderNo());
            log.info("subCustomerOrdNo " + subCustomerOrdNo);
            log.info("input.getApplicationId() " + input.getApplicationId());
            list =
session.getOmsCustOrdHeadFindByExternalCustOrdNo(input.getCustomerOrderNo(), subCustomerOrdNo, input.getApplicationId());
            log.info("list size " + list.size());
            // When omscustomer Order No is not found then it was throwing payment cannot confirm for failed orders.
            // below lines of code are added to to show customer order Doesn't exist...
            // when the list size is zero i.e given customer order No exist in oms cust Ord head table... or
            // all record are exist in oms cust Ordhead exist failed in status 'F'
            log.info("<------------------------------------------------->");
            if (list.size() == 0) {
                throw new SOAPException(list.size() + "");
            }
        } catch (Exception e) {
            log.info("inside catch block");
            if (list.size() == 0) {
                log.info("<-----Checking Record Exist For failed status in omscustOrdhead table------>");
                List<BigDecimal> failedOmsCustdOrdNoList =
                    session.getOmsCustOrdHeadFindByExternalCustOrdNoByFailedStatus(input.getCustomerOrderNo(),
                                                                                   subCustomerOrdNo,
                                                                                   input.getApplicationId());
                if (failedOmsCustdOrdNoList.size() > 0) {
                    String languageCode =
                        OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(failedOmsCustdOrdNoList.get(0));
                    Boolean flag =
                        OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.paymentCannotConfirm, languageCode);
                    if (Boolean.FALSE == flag) {
                        languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
                    }
                    throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.paymentCannotConfirm,
                                                                                languageCode,
                                                                                new String[] { input.getCustomerOrderNo() }));
                } else {
                    String languageCode = OMSUtilCommons.getLanguageCodeByCustomerOrderNo(input.getCustomerOrderNo());
                    log.info("<-----Langauge returned by the function call --->" + languageCode);
                    Boolean flag =
                        OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.customerOrderNotExist, languageCode);
                    String errString = null;
                    if (Boolean.FALSE == flag) {
                        languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
                    }
                    errString =
                            OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.customerOrderNotExist, languageCode,
                                                                new String[] { input.getCustomerOrderNo(), "1",
                                                                               input.getApplicationId() });
                    log.error(errString);
                    throw new SOAPException(errString);
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            log.info("inside for loop I " + i);
            log.info("omsCustOrdNo " + omsCustOrdNo);
            omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(list.get(i));
            if (omsCustOrdHead.getStatus().equals("S")) {
                omsCustOrdNo = omsCustOrdHead.getOmsCustOrdNo();
                break;
            }
        }
        if (omsCustOrdNo.intValue() == 0) {
            String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
            Boolean flag =
                OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.paymentCannotConfirm, languageCode);
            if (Boolean.FALSE == flag) {
                languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
            }
            throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.paymentCannotConfirm,
                                                                        languageCode,
                                                                        new String[] { input.getCustomerOrderNo() }));
        }
        omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        if (omsCustOrdHead.getOrdPaymentStatus().equals("S") && omsCustOrdHead.getStatus().equals("S")) {
            log.info("omsCustOrdNo " + omsCustOrdNo + "inside if condition to throw exception");
            String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
            Boolean flag =
                OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.paymentAlreadyConfirmed, languageCode);
            if (Boolean.FALSE == flag) {
                languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
            }
            throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.paymentAlreadyConfirmed,
                                                                        languageCode,
                                                                        new String[] { input.getCustomerOrderNo() }));
        }
        log.info("omsCustOrdNo " + omsCustOrdNo + "checking whether payment already confirmed");

        if (omsCustOrdNo.intValue() != 0) {
            log.info("omsCustOrdNo is not equal 0");
            omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
            log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdHead.getOrdPaymentStatus() " +
                     omsCustOrdHead.getOrdPaymentStatus());
            List<OmsCustOrdReserve> omsCustOrdReserveList =
                session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
            //order in failed status
            if (omsCustOrdHead.getStatus().equals("S") == false) {
                //order failed at the time of creation
                if (omsCustOrdReserveList.size() > 0 && omsCustOrdReserveList.get(0).getResvStatus().equals("RES")) {
                    String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
                    Boolean flag =
                        OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.paymentCannotConfirm, languageCode);
                    if (Boolean.FALSE == flag) {
                        languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
                    }
                    throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.paymentCannotConfirm,
                                                                                languageCode,
                                                                                new String[] { input.getCustomerOrderNo() }));
                } else if (omsCustOrdReserveList.size() == 0) {
                    //failed order at the time of creation
                    String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
                    Boolean flag =
                        OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.paymentCannotConfirm, languageCode);
                    if (Boolean.FALSE == flag) {
                        languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
                    }
                    throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.paymentCannotConfirm,
                                                                                languageCode,
                                                                                new String[] { input.getCustomerOrderNo() }));
                }
            }
            if (omsCustOrdHead.getOrdPaymentStatus().equals("S")) {
                log.info("omsCustOrdNo " + omsCustOrdNo + "inside if condition to throw exception");
                String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
                Boolean flag =
                    OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.paymentAlreadyConfirmed, languageCode);
                if (Boolean.FALSE == flag) {
                    languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
                }
                throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.paymentAlreadyConfirmed,
                                                                            languageCode,
                                                                            new String[] { input.getCustomerOrderNo() }));
            }
            if (omsCustOrdHead.getOrdPaymentStatus().equals("R")) {
                log.info("omsCustOrdNo " + omsCustOrdNo + "inside R");
                String languageCode = OMSUtilCommons.getLanguageCodeOfOmsCustomerOrderNo(omsCustOrdNo);
                Boolean flag =
                    OMSUtilCommons.checkErrorCodeExistOrNot(OmsErrorCodesConstant.paymentRejected, languageCode);
                if (Boolean.FALSE == flag) {
                    languageCode = OmsErrorCodesConstant.baseLanguageCodeValue;
                }
                throw new SOAPException(OMSUtilCommons.formErrorDescription(OmsErrorCodesConstant.paymentRejected,
                                                                            languageCode, new String[] { }));
            }
        }
        //}
        return omsCustOrdNo;
    }

    public void validate(CoPaymentConf input) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        String subCustomerOrdNo = "1";
        if (input.getCustomerSubOrderNo() != null) {
            subCustomerOrdNo = input.getCustomerSubOrderNo();
        }
        try {
            session.getOmsCustOrdHeadFindByExternalCustOrdNo(input.getCustomerOrderNo(), subCustomerOrdNo,
                                                             input.getApplicationId());
        } catch (Exception e) {
            // If Given customer doesn't in oms_cust_ord_head table then we need to show the error message in English
            // Only . i.e other than English is not possible.
            log.info("--- Error Occured while fetching the External Customer Order No---->");
            String errString =
                OMSUtilCommons.formErrorDescription("CO_NOT_EXISTS", "1", new String[] { input.getCustomerOrderNo(),
                                                                                         subCustomerOrdNo,
                                                                                         input.getApplicationId() });
            log.error("-->" + errString);
            throw new SOAPException(errString);
        }
    }


    public ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> processPaymentConfirmation(CoPaymentConf input) throws SOAPException,
                                                                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                                                                        EntityAlreadyExistsWSFaultException, BusinessException {
        ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap =
            new ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
        ExtSystemUpdate extsys = new ExtSystemUpdate();
        Date date1 = new Date();
        log.info("omsCustOrdNo " + omsCustOrdNo + "Start Time in processPaymentConfirmation " + date1);
        try {

            OMSUtilSessionEJB session = OMSUtil.doLookup();
            log.info("omsCustOrdNo " + omsCustOrdNo + "Getting milliseconds value from OmsSystemParameters");
            String millseconds =
                session.getOmsSystemParametersFindIndValue("PAYMENT_FULFILLMENT", "TIME_IN_MILLISECS");
            log.info("millseconds " + millseconds);
            log.info("omsCustOrdNo " + omsCustOrdNo + "millseconds " + Long.parseLong(millseconds));
            log.info("omsCustOrdNo " + omsCustOrdNo + "proceeding fulfillment with 2 milliseconds");
            Thread.sleep(Long.parseLong(millseconds));
            fulfillDetailMap = extsys.processPaymentConfirmation(input, omsCustOrdNo);

            Date date2 = new Date();
            log.info("omsCustOrdNo " + omsCustOrdNo + "End Time in processPaymentConfirmation " + date2);
            log.info("omsCustOrdNo " + omsCustOrdNo +
                     "Difference in Start and End Time in processPaymentConfirmation " +
                     (date2.getTime() - date1.getTime()) + " milli seconds");
        } catch (Exception e) {
            log.info("inside catch block " + e.getMessage());
            log.info("omsCustOrdNo " + omsCustOrdNo +
                     " Exception occured while merging the PaymentStatus in omsCustOrdHead " + e.getMessage());
            //calling rollback for timeout exception
            log.info("<-------------completeMap : " + completeMap.keySet() +
                     " size in processPaymentConfirmation method------------->");
            log.info("Before calling rollback for Timeout Exception : " + input.getCustomerOrderNo());
            rollbackForTimeout(omsCustOrdNo, input.getCustomerOrderNo());
            log.info("After calling rollback for Timeout Exception");
        }
        return fulfillDetailMap;
    }

    public void persistOmsCustOrdReserve(CoPaymentConf input) throws SOAPException {
        OMSPersistence omsPersistence = new OMSPersistence();
        Date date1 = new Date();
        log.info("omsCustOrdNo " + omsCustOrdNo + "Start Time to persist in to OmsCustOrdReserve " + date1);
        omsPersistence.persistOmsCustOrdReserve(input, omsCustOrdNo);
        Date date2 = new Date();
        log.info("omsCustOrdNo " + omsCustOrdNo + "End Time to persist in to OmsCustOrdReserve " + date2);
        log.info("omsCustOrdNo " + omsCustOrdNo +
                 "Difference in Start and End Time in persist in to OmsCustOrdReserve " +
                 (date2.getTime() - date1.getTime()) + " milli seconds");
    }

    //added code for bug 2603

    public void persistOmsPaymentSync(BigDecimal omsCustOrdNo) throws SOAPException {
        log.info("<----------------Persist in OmsPaymentSync Table--------------->");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);

        for (OmsCustOrdReserve omsCustOrdReserveTemp : omsCustOrdReserveList) {
            if (omsCustOrdReserveTemp.getQty().intValue() > 0) {
                OmsPaymentSync omsPaymentSync = new OmsPaymentSync();
                log.info("*******************************************");
                log.info("OmsCustOrdNumber :" + omsCustOrdNo);
                omsPaymentSync.setOmsCustOrdNo(omsCustOrdNo);
                log.info("Item : " + omsCustOrdReserveTemp.getItem());
                omsPaymentSync.setItem(omsCustOrdReserveTemp.getItem());
                log.info("Line Number : " + omsCustOrdReserveTemp.getLineNo());
                omsPaymentSync.setLineNo(omsCustOrdReserveTemp.getLineNo());
                log.info("Location Type : " + omsCustOrdReserveTemp.getRmsResvLocType());
                omsPaymentSync.setLocationType(omsCustOrdReserveTemp.getRmsResvLocType());
                log.info("Location : " + omsCustOrdReserveTemp.getRmsResvLoc());
                omsPaymentSync.setLocation(omsCustOrdReserveTemp.getRmsResvLoc());
                log.info("Quantity : " + omsCustOrdReserveTemp.getQty());
                omsPaymentSync.setCreateId("OMSDEV");
                omsPaymentSync.setQuantity(omsCustOrdReserveTemp.getQty());
                omsPaymentSync.setCreateDatetime(new Timestamp(new Date().getTime()));
                session.persistOmsPaymentSync(omsPaymentSync);
                log.info("Successfully Peristed in OmsPaymentSync Table");
            }
        }
    }

    public void unreserveQuantities(CoPaymentConf input) throws SOAPException {
        RMSPackage rmsPackage = new RMSPackage();
        Date date1 = new Date();
        log.info("omsCustOrdNo " + omsCustOrdNo + "Start Time  in rmsPackageCall " + date1);
        rmsPackage.rmsPackageCall(input, omsCustOrdNo);
        Date date2 = new Date();
        log.info("omsCustOrdNo " + omsCustOrdNo + "End Time in rmsPackageCall " + date2);
        log.info("omsCustOrdNo " + omsCustOrdNo + "Difference in Start and End Time in rmsPackageCall " +
                 (date2.getTime() - date1.getTime()) + "milli seconds");
        InterfacePersistence interfacePersistence = new InterfacePersistence();
        Date date3 = new Date();
        log.info("omsCustOrdNo " + omsCustOrdNo + "Start Time  in adjustInventoryByItemLocation " + date3);
        interfacePersistence.adjustInventoryByItemLocation(input, omsCustOrdNo);
        Date date4 = new Date();
        log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo " + omsCustOrdNo +
                 "End Time in adjustInventoryByItemLocation " + date4);
        log.info("omsCustOrdNo " + omsCustOrdNo +
                 "Difference in Start and End Time in adjustInventoryByItemLocation " +
                 (date4.getTime() - date3.getTime()) + "milli seconds");

    }

    public void rollback(ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfullDetailMap) throws EntityAlreadyExistsWSFaultException,
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
        log.info("omsCustOrdNo " + omsCustOrdNo + "Starting rollback");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        log.info("omsCustOrdNo " + omsCustOrdNo + "fulfullDetailMap map size=" + fulfullDetailMap.size());
        InterfacePersistence interfacePersistence = new InterfacePersistence();
        if (fulfullDetailMap.size() >= 1) {
            for (BigDecimal key : fulfullDetailMap.keySet()) {
                ArrayList<OmsTempCoFo> list = fulfullDetailMap.get(key);
                log.info("omsCustOrdNo " + omsCustOrdNo + "List size" + list.size());
                log.info("omsCustOrdNo " + omsCustOrdNo + "Processing app=" + list.get(0).getProcessingApp());
                if (list.get(0).getProcessingApp().equals("RMS") && list.size() >= 1) {
                    if (list.get(0).getSourceLocId().compareTo(list.get(0).getFulfillLocId()) == 0) {
                        interfacePersistence.callSIMCancelllationWS(list.get(0).getOmsCustOrdNo(), list);
                        log.info("omsCustOrdNo " + omsCustOrdNo + "callSIMCancelllationWS successful");
                    }
                    interfacePersistence.callRMSCancellationWebservice(list.get(0).getOmsCustOrdNo(), list);
                    log.info("omsCustOrdNo " + omsCustOrdNo + "callRMSCancellationWebservice successful");
                } else {
                    interfacePersistence.callSIMCancelllationWS(list.get(0).getOmsCustOrdNo(), list);
                    log.info("omsCustOrdNo " + omsCustOrdNo + "callSIMCancelllationWS successful");
                }
                log.info(">>>");

            }
        }
    }


    public CoPaymentConfResponse createResponse(CoPaymentConf input,
                                                ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> completeMap,
                                                String errMessage) throws SOAPException {
        CoPaymentConfResponse coPaymentConfResponse = null;
        ResponseProcessing responseProcessing = new ResponseProcessing();
        try {
            log.info("omsCustOrdNo : " + omsCustOrdNo + " calling create response method");
            log.info("<--------completeMap size in CoPaymentConfResponse createResponse :" + completeMap);
            coPaymentConfResponse = responseProcessing.createResponse(input, omsCustOrdNo, errMessage, completeMap);
        } catch (Exception e) {
            log.info("omsCustOrdNo" + omsCustOrdNo + "Exception inside create response : " + e.getMessage());
        }
        return coPaymentConfResponse;
    }

    public void callSiebelOrderFeed(CoPaymentConf input) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        if (!input.getApplicationId().equals("SIEBEL_CRM")) {
            InterfacePersistence interfacePersistance = new InterfacePersistence();
            log.info("Calling siebel");
            try {
                if (session.getOmsSystemParametersFindIndValue("CALL_SIEBEL", "OMS_SYSTEM_OPTION").equals("Y")) {
                    Date date1 = new Date();
                    log.info("Start Time  in callSeibelWebservice " + date1);
                    interfacePersistance.callSeibelWebservice(input, omsCustOrdNo);
                    Date date2 = new Date();
                    log.info("End Time in callSeibelWebservice " + date2);
                    log.info("Difference in Start and End Time in callSeibelWebservice " +
                             (date2.getTime() - date1.getTime()) + "milli seconds");
                }
            } catch (Exception e) {
                log.error("callSeibelWebservice failed" + e);
            }
        }
    }

    public void callSIMCancelllationWS(BigDecimal omsCustOrdNo,
                                       List<OmsTempCoFo> temp) throws com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,

            SOAPException, EntityAlreadyExistsWSFaultException,
            com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,

            com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,

            com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,

            com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException {
        log.info("callSIMCancelllationWS started as part of rollback");

        OMSUtilSessionEJB session = OMSUtil.doLookup();
        OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        if (omsCustOrdHead.getSubCustOrderNo().equals("1")) {
            extCustOrdNo = omsCustOrdHead.getCustOrderNo();
        } else {
            extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + omsCustOrdHead.getSubCustOrderNo();
            if (omsCustOrdHead.getSubCustOrderNo().trim().length() < 3) {
                if (omsCustOrdHead.getSubCustOrderNo().trim().length() == 2) {
                    extCustOrdNo =
                            omsCustOrdHead.getCustOrderNo().trim() + "-0" + omsCustOrdHead.getSubCustOrderNo().trim();
                } else {
                    extCustOrdNo =
                            omsCustOrdHead.getCustOrderNo().trim() + "-00" + omsCustOrdHead.getSubCustOrderNo().trim();
                }
            }
        }
        StoreFulfillmentOrderService storeFulfillmentOrderService = new StoreFulfillmentOrderService();
        StoreFulfillmentOrderPortType storeFulfillmentOrderPortType =
            storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
        Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>();
        FulfilOrdColRef fulfilOrdColRef1 = new FulfilOrdColRef();
        fulfilOrdColRef1.setCollectionSize(1);

        fulfilOrdColRef.value = fulfilOrdColRef1;
        FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
        fulfilOrdRef.setCustomerOrderNo(extCustOrdNo);
        fulfilOrdRef.setFulfillLocId(temp.get(0).getFulfillLocId().longValue());
        fulfilOrdRef.setFulfillLocType(fulfilOrdRef.getFulfillLocType().fromValue(temp.get(0).getFulfillLocationType()));

        fulfilOrdRef.setSourceLocId(temp.get(0).getSourceLocId().longValue()); //**
        fulfilOrdRef.setSourceLocType(fulfilOrdRef.getSourceLocType().fromValue(temp.get(0).getSourceLocationType()));

        fulfilOrdRef.setFulfillOrderNo(String.valueOf(temp.get(0).getFulfillOrderNo()));
        log.info("fulfil ord_no=" + temp.get(0).getFulfillOrderNo() + "Sourc_loc=" + temp.get(0).getSourceLocId() +
                 fulfilOrdRef.getSourceLocType() + "fulfill_loc=" + temp.get(0).getFulfillLocId() +
                 fulfilOrdRef.getFulfillLocType());
        for (OmsTempCoFo tempCoFo : temp) {
            log.info("extCustOrdNo=" + extCustOrdNo + "Item sent for cancelllatin is " + tempCoFo.getItem());
            FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
            // fulfilOrdDtlRef.setCancelQtySuom(item.getCancelQtySuom());
            log.info("cancel qy=" + tempCoFo.getOrderQty());
            fulfilOrdDtlRef.setCancelQtySuom(tempCoFo.getOrderQty()); //check
            fulfilOrdDtlRef.setItem(tempCoFo.getItem());

            OmsCustOrdItem omsCustOrdItem =
                session.getOmsCustOrdItemFindByItem(omsCustOrdNo, tempCoFo.getItem(), tempCoFo.getLineNo());
            fulfilOrdDtlRef.setTransactionUom(omsCustOrdItem.getTransactionUom());
            fulfilOrdDtlRef.setStandardUom(omsCustOrdItem.getStandardUom());
            fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
        }
        fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
        fulfilOrdColRef.value = fulfilOrdColRef1;
        try {
            log.info("fulfilOrdColRef size=" + fulfilOrdColRef.value.getFulfilOrdRef().size());
            log.info("---" + fulfilOrdColRef.value.getFulfilOrdRef().get(0).getCustomerOrderNo());
            storeFulfillmentOrderPortType.cancelFulfillmentOrderDetail(fulfilOrdColRef);

        } catch (Exception e) {
            log.error("Error in sim cancellation");
        }
    }

    public void rollbackUnreservation(CoPaymentConf input) throws SOAPException, EntityAlreadyExistsWSFaultException,
                                                                  IllegalStateWSFaultException,
                                                                  IllegalStateWSFaultException,
                                                                  IllegalStateWSFaultException,
                                                                  IllegalArgumentWSFaultException,
                                                                  IllegalArgumentWSFaultException,
                                                                  ValidationWSFaultException,
                                                                  ValidationWSFaultException,
                                                                  ValidationWSFaultException,
                                                                  ValidationWSFaultException {
        RMSPackage rMSPackage = new RMSPackage();
        log.info("Calling Rollback RMSPackage Call : " + input.getCustomerOrderNo());
        rMSPackage.rollbackRmsPackageCall(input, omsCustOrdNo);
        InterfacePersistence interfacePersistence = new InterfacePersistence();
        interfacePersistence.rollbackAdjustInventoryByItemLocation(input, omsCustOrdNo);
        log.info("calling RMS to rollback");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        List<OmsCustOrdReserve> omsCustOrdReserveList = session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
        ExtSystemUpdate extsys = new ExtSystemUpdate();
        ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap =
            new ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
        BigDecimal physicalWH;
        for (OmsCustOrdReserve omsCustOrdReserveTemp : omsCustOrdReserveList) {
            OmsTempCoFo omsTempCoFo = extsys.convertOmsCustOrdResvToOmsCustOrdTemp(omsCustOrdReserveTemp);
            log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdReserveTemp value item=" + omsTempCoFo.getItem() +
                     "Lineno=" + omsTempCoFo.getLineNo() + "fulfillment no=" + omsTempCoFo.getFulfillOrderNo());
            if (fulfillDetailMap.get(omsTempCoFo.getFulfillOrderNo()) == null ||
                fulfillDetailMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
                log.info("omsCustOrdNo " + omsCustOrdNo + "in map ******* ");
                if (omsTempCoFo.getSourceLocationType().equals("WH")) {
                    physicalWH = session.getWhFindPhyWhForVirtualWh(omsTempCoFo.getSourceLocId());
                    omsTempCoFo.setSourceLocId(physicalWH);
                }
                ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
                tempList.add(omsTempCoFo);
                fulfillDetailMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
            } else {
                log.info("omsCustOrdNo " + omsCustOrdNo +
                         "Ship to customer scenario setting in to soh map with fulfill loc=" +
                         omsCustOrdReserveTemp.getLoc());
                ArrayList<OmsTempCoFo> existingList = fulfillDetailMap.get(omsCustOrdReserveTemp.getFulfillOrderNo());
                if (omsTempCoFo.getSourceLocationType().equals("WH")) {
                    physicalWH = session.getWhFindPhyWhForVirtualWh(omsTempCoFo.getSourceLocId());
                    omsTempCoFo.setSourceLocId(physicalWH);
                }
                existingList.add(omsTempCoFo);
                fulfillDetailMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
            }


        }
        //if(0)
        //  {
        log.info("calling rofulfillDetailMap!= null && fulfillDetailMap.size()>llback to cancel");
        for (BigDecimal key : fulfillDetailMap.keySet()) {
            ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
            log.info("omsCustOrdNo " + omsCustOrdNo + "List size" + list.size());
            List<OmsCustOrdReserve> omsCustOrdReserveList1 =
                session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
            log.info(" The list size is" + omsCustOrdReserveList1.size());
            for (OmsCustOrdReserve omsCustOrdReserve : omsCustOrdReserveList1) {
                omsCustOrdReserve.setResvStatus("RES");
                log.info("Updating  omsCustOrderReserve Table");
                OmsCustOrdReserve custOrdReserve = session.mergeOmsCustOrdReserve(omsCustOrdReserve);

            }

            log.info("omsCustOrdNo " + omsCustOrdNo + "Processing app=" + list.get(0).getProcessingApp());
            log.info("calling RMS Cancellation");
            try {
                interfacePersistence.callRMSCancellationWebservice(list.get(0).getOmsCustOrdNo(), list);
                log.info("omsCustOrdNo " + omsCustOrdNo + "callRMSCancellationWebservice successful");
            } catch (Exception e) {
                log.info("No record exist");
            }


        }

        //}
    }

    //Added code for Rollback Timeout exception

    public String rollbackForTimeout(BigDecimal omsCustOrdNo, String customerOrderNo) throws SOAPException,
                                                                                             IllegalStateWSFaultException,
                                                                                             EntityAlreadyExistsWSFaultException,
                                                                                             IllegalStateWSFaultException,
                                                                                             IllegalArgumentWSFaultException,
                                                                                             ValidationWSFaultException,
                                                                                             ValidationWSFaultException,
                                                                                             com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                                             com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                                             com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                                             com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                             com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                                             com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException, BusinessException {

        OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
        InterfacePersistence interfacePersistence = new InterfacePersistence();
        log.info("***** Inside rollback for Timeout Exception *****");
        log.info("ManiException");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        String status = "F";
        List<OmsCustOrdReserve> omsCustOrdReserveList1 = session.getOmsCustOrdReserveFindByOmsCustOrdNo(omsCustOrdNo);
        log.info(" The list size is" + omsCustOrdReserveList1.size());
        for (OmsCustOrdReserve omsCustOrdReserve : omsCustOrdReserveList1) {
            omsCustOrdReserve.setPaymentStatus("P");
            omsCustOrdReserve.setResvStatus("RES");
            log.info("Updating  omsCustOrderReserve Table");
            session.mergeOmsCustOrdReserve(omsCustOrdReserve);
            log.info("After updating omsCustOrderReserve Table");
        }
        log.info("returning status Failed");

        //update omsRTlogPublish Table
        //Code added for bug 2631
        List<OmsRtlogPublishLog> omsRtlogPublishLogList =
            session.getOmsRtlogPublishLogFindByOmsCustOrderNo(omsCustOrdNo);
        log.info("<-------------Size of the omsRtlogPublishLogList : " + omsRtlogPublishLogList.size() +
                 "------------->");
        if (omsRtlogPublishLogList.size() > 0) {
            log.info("omsCustOrdNo " + omsCustOrdNo + "Setting Publish Indicator to F in OmsRtlogPublishLog ");
            for (OmsRtlogPublishLog omsRtlogPublishLog : omsRtlogPublishLogList) {
                omsRtlogPublishLog.setPublishedInd("F");
                log.info("<--------------Indicator : " + omsRtlogPublishLog.getPublishedInd() + "-------------->");
                session.mergeOmsRtlogPublishLog(omsRtlogPublishLog);
            }
        }
        log.info("Calling Rollback for : " + customerOrderNo);

        oMSUtilCommons.rollback(customerOrderNo);
        
        


        return status;
    }
    //Code added for checking the payment status

    public Boolean checkOmsCustOrdHeadPaymentStatus() throws SOAPException {
        log.info("---> Begin Of checkOmsCustOrdHeadPaymentStatus method");
        log.info("Establishing connection to DataBase");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        Boolean result = false;

        if (omsCustOrdNo.intValue() == 0) {
            return result;
        }
        log.info("Fetching OmsCustOrdHead Record table by passing omsCustOrdNo" + omsCustOrdNo);
        OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        if (omsCustOrdHead.getOrdPaymentStatus().equals("S")) {
            result = true;
        }
        log.info("----->Payment status------------>" + result);
        return result;
    }


}
