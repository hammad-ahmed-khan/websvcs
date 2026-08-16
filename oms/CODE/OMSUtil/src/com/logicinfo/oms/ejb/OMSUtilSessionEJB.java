package com.logicinfo.oms.ejb;


import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.List;

import javax.ejb.Remote;

import javax.xml.soap.SOAPException;

@Remote
public interface OMSUtilSessionEJB {


    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);
    //Used in CustomerOrder

    Ordcust persistOrdcust(Ordcust ordcust);
    //Used in CustomerOrder

    Ordcust mergeOrdcust(Ordcust ordcust);
    //Used in CustomerOrder

    void removeOrdcust(Ordcust ordcust);
    //Used in CustomerOrder

    List<Ordcust> getOrdcustFindAll();
    //Used in CustomerOrder

    List<Ordcust> getOrdcustFindByFulfilOrdNo(String customerOrderNo, String fulfillOrderNo,BigDecimal sourceLocId ,BigDecimal fulfillLocId);
    //Used in CustomerOrder

    OmsFulfillMatrixExtHead persistOmsFulfillMatrixExtHead(OmsFulfillMatrixExtHead omsFulfillMatrixExtHead);
    //Used in CustomerOrder

    OmsFulfillMatrixExtHead mergeOmsFulfillMatrixExtHead(OmsFulfillMatrixExtHead omsFulfillMatrixExtHead);
    //Used in CustomerOrder

    void removeOmsFulfillMatrixExtHead(OmsFulfillMatrixExtHead omsFulfillMatrixExtHead);
    //Used in CustomerOrder

    List<OmsFulfillMatrixExtHead> getOmsFulfillMatrixExtHeadFindAll();
    //Used in CustomerOrder

    BigDecimal getOmsFulfillMatrixExtHeadFindCombination(BigDecimal reqID, String itemType, String custCity,
                                                         String modeOfDelv, String DeliveryZone, String marketPlaceInd, String applicationId, String shipToStore);
    //Used in CustomerOrder

    BigDecimal getOmsFulfillMatrixExtHeadFindCombinationWoCity(BigDecimal reqID, String itemType, String modeOfDelv, String DeliveryZone, String marketPlaceInd, String applicationId, String shipToStore);
    //Used in CustomerOrder

    OmsCoFulfillDetail persistOmsCoFulfillDetail(OmsCoFulfillDetail omsCoFulfillDetail);
    //Used in CustomerOrder

    OmsCoFulfillDetail mergeOmsCoFulfillDetail(OmsCoFulfillDetail omsCoFulfillDetail);
    //Used in CustomerOrder

    void removeOmsCoFulfillDetail(OmsCoFulfillDetail omsCoFulfillDetail);
    //Used in CustomerOrder

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindAll();
    //Used in CustomerOrder

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindFulFillDetailByItem(BigDecimal custNumb, String item);

    List<BigDecimal> getOmsCoFulfillDetailFindByOmsCustOrderNo(BigDecimal omsCustOrdNo);

    BigDecimal getOmsCoFulfillDetailFindFulfillSeqForResv(BigDecimal lineNo, BigDecimal omsCustOrdNo);

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindColumns(BigDecimal omsCustOrdNo);

    //U sed in cancellation

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindFulFillDetails(BigDecimal custNumb, String item,
                                                                     BigDecimal lineNo);

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindOpenFulFillOrders(BigDecimal custNumb);

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindBySourceLocAndOmsCustNo(BigDecimal omsCustOrdNo, String item,
                                                                              BigDecimal sourceLoc, BigDecimal lineNo);

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindFulfillByLineNo(BigDecimal lineNo, BigDecimal omsCustOrdNo);

    BigDecimal getOmsCoFulfillDetailFindMaxFulOrdNo(BigDecimal omsCustOrdNo);
    //Used in Home Delivery Status Update

    boolean isCoFulfilled(BigDecimal omsCustOrdNo, Long fulfillOrdNo) throws SOAPException;

    List<BigDecimal> getOmsCoFulfillDetailFindBySourceLoc(BigDecimal sourceLoc);

    //Used in CustomerOrder

    OmsRtlogPublishLog persistOmsRtlogPublishLog(OmsRtlogPublishLog omsRtlogPublishLog);
    //Used in CustomerOrder

    OmsRtlogPublishLog mergeOmsRtlogPublishLog(OmsRtlogPublishLog omsRtlogPublishLog);
    //Used in CustomerOrder

    void removeOmsRtlogPublishLog(OmsRtlogPublishLog omsRtlogPublishLog);
    //Used in CustomerOrder

    List<OmsRtlogPublishLog> getOmsRtlogPublishLogFindAll();
    //Used in CustomerOrder

    OmsVirtualStrMatrix persistOmsVirtualStrMatrix(OmsVirtualStrMatrix omsVirtualStrMatrix);
    //Used in CustomerOrder

    OmsVirtualStrMatrix mergeOmsVirtualStrMatrix(OmsVirtualStrMatrix omsVirtualStrMatrix);
    //Used in CustomerOrder

    void removeOmsVirtualStrMatrix(OmsVirtualStrMatrix omsVirtualStrMatrix);
    //Used in CustomerOrder

    List<OmsVirtualStrMatrix> getOmsVirtualStrMatrixFindAll();
    //Used in CustomerOrder

    BigDecimal getOmsVirtualStrMatrixFindVirtualLocID(BigDecimal locID, String locationType);
    //Used in CustomerOrder

    OmsCustOrdHead persistOmsCustOrdHead(OmsCustOrdHead omsCustOrdHead);
    //Used in CustomerOrder

    OmsCustOrdHead mergeOmsCustOrdHead(OmsCustOrdHead omsCustOrdHead);
    //Used in CustomerOrder

    void removeOmsCustOrdHead(OmsCustOrdHead omsCustOrdHead);
    //Used in CustomerOrder

    List<OmsCustOrdHead> getOmsCustOrdHeadFindAll();
    //Used in CustomerOrder

    List<BigDecimal> getOmsCustOrdHeadFindDuplicate(String appId, String custOrdNumb, String status);
    //Used in CustomerOrder

    public List<BigDecimal> getOmsCustOrdHeadFindByExternalCustOrdNo(String custOrderNo, String subCustOrderNo,
                                                                     String applicationId);
    
    public List<BigDecimal> getOmsCustOrdHeadFindByExternalCustOrdNoByFailedStatus(String custOrderNo, String subCustOrderNo,
                                                                     String applicationId);

    public BigDecimal getOmsCustOrdHeadFindOmsCustOrderNo(String custOrderNo, String subCustOrderNo,
                                                          String applicationId);

    public BigDecimal getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(String custOrderNo, String subCustOrderNo);

    List<OmsCustOrdHead> getOmsCustOrdHeadFindByCustOrdNo(BigDecimal omsCustOrdNo);

    public String getOmsCustOrdHeadFindLanguage(BigDecimal omsCustOrdNo);
    //used in customerorder

    List<String> getOmsCustOrdHeadFindPaymentStatus(BigDecimal omsCustOrdNo);
    //in customer order

    public List<BigDecimal> getOmsCustOrdHeadFindByStatus(String appId, String custOrdNumb, String subCustOrderNo);
    //Used in CustomerOrder

    OmsCustOrdTender persistOmsCustOrdTender(OmsCustOrdTender omsCustOrdTender);
    //Used in CustomerOrder

    OmsCustOrdTender mergeOmsCustOrdTender(OmsCustOrdTender omsCustOrdTender);
    //Used in CustomerOrder

    void removeOmsCustOrdTender(OmsCustOrdTender omsCustOrdTender);
    //Used in CustomerOrder

    List<OmsCustOrdTender> getOmsCustOrdTenderFindAll();
    //Used in CustomerOrder

    OmsCustOrdTender getOmsCustOrdTenderFindByTenderType(BigDecimal omsCustOrdNo, String tenderTypeGroup);
    //Used in CustomerOrder

    List<BigDecimal> getOmsCustOrdTenderFindByTenderRefId(String tenderRefId);

    OmsTempCoFo persistOmsTempCoFo(OmsTempCoFo omsTempCoFo);
    //Used in CustomerOrder

    OmsTempCoFo mergeOmsTempCoFo(OmsTempCoFo omsTempCoFo);
    //Used in CustomerOrder

    void removeOmsTempCoFo(OmsTempCoFo omsTempCoFo);
    //Used in CustomerOrder

    List<OmsTempCoFo> getOmsTempCoFoFindAll();
    //Used in CustomerOrder

    List<Object[]> getOmsTempCoFoFindSADAD(BigDecimal custNo);
    //Used in CustomerOrder

    List<OmsTempCoFo> getOmsTempCoFoFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    //Used in CustomerOrder

    List<Object[]> getOmsTempCoFoFindDistinctLocs(BigDecimal custNo);
    //Used in CustomerOrder

    List<OmsTempCoFo> getOmsTempCoFoFindByLocation(BigDecimal custNo, BigDecimal sourceLocId, BigDecimal fulfillLocId);
    //Used in CustomerOrder

    List<OmsTempCoFo> getOmsTempCoFoFindByResponseCode(BigDecimal custNo, String rmsResponseCode);
    //Used in CustomerOrder

    OmsSystemParameters persistOmsSystemParameters(OmsSystemParameters omsSystemParameters);
    //Used in CustomerOrder

    OmsSystemParameters mergeOmsSystemParameters(OmsSystemParameters omsSystemParameters);
    //Used in CustomerOrder

    void removeOmsSystemParameters(OmsSystemParameters omsSystemParameters);
    //Used in CustomerOrder

    List<OmsSystemParameters> getOmsSystemParametersFindAll();
    //Used in CustomerOrder

    public List<OmsSystemParameters> getOmsSystemParametersFindByParameterId(String paraId);

    String getOmsSystemParametersFindIndValue(String paraName, String paraId);
    //Used in CustomerOrder

    OmsCustOrdAddress persistOmsCustOrdAddress(OmsCustOrdAddress omsCustOrdAddress);
    //Used in CustomerOrder

    OmsCustOrdAddress mergeOmsCustOrdAddress(OmsCustOrdAddress omsCustOrdAddress);
    //Used in CustomerOrder

    void removeOmsCustOrdAddress(OmsCustOrdAddress omsCustOrdAddress);
    //Used in CustomerOrder

    List<OmsCustOrdAddress> getOmsCustOrdAddressFindAll();
    //Used in CustomerOrder

    List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstName(String deliverFirstName);

    List<BigDecimal> getOmsCustOrdAddressFindByDeliverLastName(String deliverLastName);

    List<BigDecimal> getOmsCustOrdAddressFindByDeliverPhoneNo(String deliverPhoneNo);

    List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstNameAndLastName(String deliverFirstName,
                                                                           String deliverLastName);

    List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstNameAndPhoneNo(String deliverFirstName,
                                                                          String deliverPhoneNo);

    List<BigDecimal> getOmsCustOrdAddressFindByDeliverLastNameAndPhoneNo(String deliverLastName,
                                                                         String deliverPhoneNo);

    List<BigDecimal> getOmsCustOrdAddressFindByDeliverFirstNameLastNameAndPhoneNo(String deliverFirstName,
                                                                                  String deliverLastName,
                                                                                  String deliverPhoneNo);

    OmsFulfillMatrixExtDetail persistOmsFulfillMatrixExtDetail(OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail);
    //Used in CustomerOrder

    OmsFulfillMatrixExtDetail mergeOmsFulfillMatrixExtDetail(OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail);
    //Used in CustomerOrder

    void removeOmsFulfillMatrixExtDetail(OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail);
    //Used in CustomerOrder

    List<OmsFulfillMatrixExtDetail> getOmsFulfillMatrixExtDetailFindAll();
    //Used in CustomerOrder

    OmsFulfillMatrixExtDetail getOmsFulfillMatrixExtDetailFindDetail(BigDecimal combID, BigDecimal priority);
    //Used in CustomerOrder

    OmsFulfillMatrixExtDetail getOmsFulfillMatrixExtDetailFindPriority(BigDecimal combID, BigDecimal location);

    List<OmsFulfillMatrixExtDetail> getOmsFulfillMatrixExtDetailFindByCombId(BigDecimal combID);
    //Used in CustomerOrder

    OmsCustOrdItem persistOmsCustOrdItem(OmsCustOrdItem omsCustOrdItem);
    //Used in CustomerOrder

    OmsCustOrdItem mergeOmsCustOrdItem(OmsCustOrdItem omsCustOrdItem);
    //Used in CustomerOrder

    void removeOmsCustOrdItem(OmsCustOrdItem omsCustOrdItem);
    //Used in CustomerOrder

    List<OmsCustOrdItem> getOmsCustOrdItemFindAll();
    //Used in CustomerOrder

    List<OmsCustOrdItem> getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(BigDecimal omsCustOrdNo, BigDecimal lineNo);

    //  List<OmsCustOrdItem> getOmsCustOrdItemFindItemsByDept(BigDecimal dept);

    BigDecimal getOmsCustOrdItemFindSumOfQtysForCloseDateTime(BigDecimal omsCustOrdNo);

    OmsCustOrdItem getOmsCustOrdItemFindByItem(BigDecimal omsCustOrdNo, String item, BigDecimal lineNo);
    //Used in CustomerOrder

    BigDecimal getOmsCustOrdItemSumUnitRetail(BigDecimal omsCustOrdNo);

    List<OmsCustOrdItem> getOmsCustOrdItemFindByBOIndAndOmsCustNo(BigDecimal omsCustOrdNo, String backorderInd);
    //used in cancellation

    List<OmsCustOrdItem> getOmsCustOrdItemFindByCustOrdNo(BigDecimal omsCustNumber);
    //Used in CustomerOrder

    public List<BigDecimal> getOmsCustOrdItemFindBackOrders(String backorderInd, Timestamp backorderDlyDate);

    List<BigDecimal> getOmsCustOrdItemFindByItemAndLastUpdatedDateTime(String item, Timestamp startTime,
                                                                       Timestamp endTime, BigDecimal omsCustOrdNo);

    //used in rma

    BigDecimal getOmsCustOrdItemFindDeilverdQuantity(BigDecimal omsCustOrdNo, String item, BigDecimal lineNo);

    public List<OmsCustOrdItem> getOmsCustOrdItemFindByLinkLineNo(BigDecimal omsCustOrdNo, BigDecimal lineLinkNo);

    OmsCustOrdReserve persistOmsCustOrdReserve(OmsCustOrdReserve omsCustOrdReserve);
    //Used in CustomerOrder

    List<BigDecimal> getOmsCustOrdItemFindOmsCustOrdNoByBOInd(String backorderInd);
    //Used in CustomerOrder

    List<OmsCustOrdItem> getOmsCustOrdItemFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    //Used in CustomerOrder

    OmsCustOrdReserve mergeOmsCustOrdReserve(OmsCustOrdReserve omsCustOrdReserve);
    //Used in CustomerOrder

    void removeOmsCustOrdReserve(OmsCustOrdReserve omsCustOrdReserve);
    //Used in CustomerOrder

    List<OmsCustOrdReserve> getOmsCustOrdReserveFindAll();
    //Used in CustomerOrder

    List<OmsCustOrdReserve> getOmsCustOrdReserveFindByOmsCustOrdNoAndItem(BigDecimal omsCustOrdNo, String item,
                                                                          BigDecimal lineNo);
    //Used in CustomerOrder

    List<OmsCustOrdReserve> getOmsCustOrdReserveFindByFulOrdNo(BigDecimal omsCustOrdNo, BigDecimal fulfillOrderNo);

    List<OmsCustOrdReserve> getOmsCustOrdReserveFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    //Used in CustomerOrder

    OmsCustOrdReserve getOmsCustOrdReserveFindByOmsCustOrdNoAndItemAndResvLoc(BigDecimal omsCustOrdNo, String item,
                                                                              BigDecimal lineNo,
                                                                              BigDecimal rmsResvLoc);

    OmsCustOrdHead getOmsCustOrdHeadFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    //Used in CustomerOrder

    List<BigDecimal> getOmsCustOrdHeadFindByCustId(String custId);

    List<OmsCustOrdTender> getOmsCustOrdTenderFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    //Used in CustomerOrder

    BigDecimal getOmsCustOrdTenderFindMaxTenderSeqNo(BigDecimal omsCustOrdNo);


    List<BigDecimal> getOmsCustOrdTenderFindByccNo(String ccNo);

    BigDecimal getOmsCustOrdTenderSumOfTenderAmt(BigDecimal omsCustOrdNo);

    OmsCustOrdAddress getOmsCustOrdAddressFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    //Used in CustomerOrder

    List<OmsTempCoFo> getOmsTempCoFoFindByStatus(BigDecimal custNo, String status);
    //Used in CustomerOrder

    OmsOrdItemTenderSplit persistOmsOrdItemTenderSplit(OmsOrdItemTenderSplit omsOrdItemTenderSplit);
    //Used in CustomerOrder

    OmsOrdItemTenderSplit mergeOmsOrdItemTenderSplit(OmsOrdItemTenderSplit omsOrdItemTenderSplit);
    //Used in CustomerOrder

    void removeOmsOrdItemTenderSplit(OmsOrdItemTenderSplit omsOrdItemTenderSplit);
    //Used in CustomerOrder

    List<OmsOrdItemTenderSplit> getOmsOrdItemTenderSplitFindAll();

    //used in OMSInvAdj

    BigDecimal getOmsInvAdjFindByServiceReqType(String serviceReqTyp);
    //used in OMSInvAdj

    OmsInvAdjItem persistOmsInvAdjItem(OmsInvAdjItem omsInvAdjItem);
    //used in OMSInvAdj

    OmsInvAdjItem mergeOmsInvAdjItem(OmsInvAdjItem omsInvAdjItem);
    //used in OMSInvAdj

    void removeOmsInvAdjItem(OmsInvAdjItem omsInvAdjItem);
    //used in OMSInvAdj

    List<OmsInvAdjItem> getOmsInvAdjItemFindAll();
    //used in OMSInvAdj

    OmsInvAdj persistOmsInvAdj(OmsInvAdj omsInvAdj);
    //used in OMSInvAdj

    OmsInvAdj mergeOmsInvAdj(OmsInvAdj omsInvAdj);
    //used in OMSInvAdj

    void removeOmsInvAdj(OmsInvAdj omsInvAdj);
    //used in OMSInvAdj

    List<OmsInvAdj> getOmsInvAdjFindAll();
    //used in OMSInvAdj

    List<BigDecimal> getOmsInvAdjFindOmsAdjReqId(String reqId, String appId);
    //used in OMSInvAdj

    OmsInvAdj getOmsInvAdjFindByOmsAdjReqId(BigDecimal reqId);
    //used in OMSInvAdj

    OmsInvAdjItem getOmsInvAdjItemFindByOmsAdjReqId(BigDecimal reqId);
    //Used in StoreToStoreTransfer

    OmsStsTrf persistOmsStsTrf(OmsStsTrf omsStsTrf);
    //Used in StoreToStoreTransfer

    OmsStsTrf mergeOmsStsTrf(OmsStsTrf omsStsTrf);
    //Used in StoreToStoreTransfer

    void removeOmsStsTrf(OmsStsTrf omsStsTrf);
    //Used in StoreToStoreTransfer

    List<OmsStsTrf> getOmsStsTrfFindAll();
    //Used in StoreToStoreTransfer

    List<BigDecimal> getOmsStsTrfFindByTransReqId(String appId, String transID);
    //Used in StoreToStoreTransfer

    OmsStsTrf getOmsStsTrfFindByOmsTrfReqId(BigDecimal reqId);
    //Used in StoreToStoreTransfer

    OmsStsTrfItem persistOmsStsTrfItem(OmsStsTrfItem omsStsTrfItem);
    //Used in StoreToStoreTransfer

    OmsStsTrfItem mergeOmsStsTrfItem(OmsStsTrfItem omsStsTrfItem);
    //Used in StoreToStoreTransfer

    void removeOmsStsTrfItem(OmsStsTrfItem omsStsTrfItem);
    //Used in StoreToStoreTransfer

    List<OmsStsTrfItem> getOmsStsTrfItemFindAll();
    //Used in StoreToStoreTransfer

    OmsStsTrfItem getOmsStsTrfItemFindByOmsTrfReqId(BigDecimal reqId);
    //Used in co cancellation

    OmsCoFoCancel persistOmsCoFoCancel(OmsCoFoCancel omsCoFoCancel);
    //Used in co cancellation

    OmsCoFoCancel mergeOmsCoFoCancel(OmsCoFoCancel omsCoFoCancel);
    //Used in co cancellation

    void removeOmsCoFoCancel(OmsCoFoCancel omsCoFoCancel);
    //Used in co cancellation

    List<OmsCoFoCancel> getOmsCoFoCancelFindAll();

    List<OmsCoFoCancel> getOmsCoFoCancelFindByOmsCancelId(BigDecimal omsCancelId);
    //Used in co cancellation

    OmsCustOrdLog persistOmsCustOrdLog(OmsCustOrdLog omsCustOrdLog);
    //Used in co cancellation

    OmsCustOrdLog mergeOmsCustOrdLog(OmsCustOrdLog omsCustOrdLog);
    //Used in co cancellation

    void removeOmsCustOrdLog(OmsCustOrdLog omsCustOrdLog);
    //Used in co cancellation

    List<OmsCustOrdLog> getOmsCustOrdLogFindAll();
    //Used in co cancellation

    List<BigDecimal> getOmsCustOrdLogFindOmsCancelId(BigDecimal omsCancelId);

    BigDecimal getOmsCustOrdLogFindMaxLogSeqNo(BigDecimal omsCustOrdNo);
    //Used in co cancellation

    BigDecimal getOmsCustOrdLogFindlogSeqNo(BigDecimal omsCancelId);
    //Used in co cancellation

    OmsCoCancelItem persistOmsCoCancelItem(OmsCoCancelItem omsCoCancelItem);
    //Used in co cancellation

    OmsCoCancelItem mergeOmsCoCancelItem(OmsCoCancelItem omsCoCancelItem);
    //Used in co cancellation

    void removeOmsCoCancelItem(OmsCoCancelItem omsCoCancelItem);
    //Used in co cancellation

    List<OmsCoCancelItem> getOmsCoCancelItemFindAll();
    //Used in co cancellation

    List<OmsCoCancelItem> getOmsCoCancelItemFindByOmsCancelId(BigDecimal omsCancelId);
    //Used in co cancellation

    List<OmsCoCancelItem> getOmsCoCancelItemGetCustItemList(BigDecimal omsCancelId, String inputItem);
    //Used in co cancellation

    OmsCoCancelHead persistOmsCoCancelHead(OmsCoCancelHead omsCoCancelHead);
    //Used in co cancellation

    OmsCoCancelHead mergeOmsCoCancelHead(OmsCoCancelHead omsCoCancelHead);
    //Used in co cancellation

    void removeOmsCoCancelHead(OmsCoCancelHead omsCoCancelHead);
    //Used in co cancellation

    List<OmsCoCancelHead> getOmsCoCancelHeadFindAll();
    //Used in co cancellation

    List<OmsCoCancelHead> getOmsCoCancelHeadFindByCustOrdNo(String custOrdNo);

    public List<BigDecimal> getOmsCoCancelHeadFindOmsCancelId(String custOrdNo, String subCustOrdNo,
                                                              BigDecimal cancelReqId);
    //Used in co cancellation

    OmsCustOrdLogItem persistOmsCustOrdLogItem(OmsCustOrdLogItem omsCustOrdLogItem);
    //Used in co cancellation

    OmsCustOrdLogItem mergeOmsCustOrdLogItem(OmsCustOrdLogItem omsCustOrdLogItem);
    //Used in co cancellation

    void removeOmsCustOrdLogItem(OmsCustOrdLogItem omsCustOrdLogItem);

    public BigDecimal getOmsCustOrdLogLineItemandLogSeqNo(BigDecimal lineNo, String item, BigDecimal logSeqNo);
    //Used in co cancellation

    List<OmsCustOrdLogItem> getOmsCustOrdLogItemFindAll();

    OmsCoFoCancelTemp persistOmsCoFoCancelTemp(OmsCoFoCancelTemp omsCoFoCancelTemp);

    OmsCoFoCancelTemp mergeOmsCoFoCancelTemp(OmsCoFoCancelTemp omsCoFoCancelTemp);

    void removeOmsCoFoCancelTemp(OmsCoFoCancelTemp omsCoFoCancelTemp);

    List<OmsCoFoCancelTemp> getOmsCoFoCancelTempFindAll();

    List<OmsCoFoCancelTemp> getOmsCoFoCancelTempFindByCancelId(BigDecimal omsCancelId, BigDecimal fulfillOrderNo,
                                                               String wsResponse);

    OmsCustCancelTender persistOmsCustCancelTender(OmsCustCancelTender omsCustCancelTender);

    OmsCustCancelTender mergeOmsCustCancelTender(OmsCustCancelTender omsCustCancelTender);

    void removeOmsCustCancelTender(OmsCustCancelTender omsCustCancelTender);

    List<OmsCustCancelTender> getOmsCustCancelTenderFindAll();
    //used in customer order

    OmsRevsPick persistOmsRevsPick(OmsRevsPick omsRevsPick);
    //used in customer order

    OmsRevsPick mergeOmsRevsPick(OmsRevsPick omsRevsPick);
    //used in customer order

    void removeOmsRevsPick(OmsRevsPick omsRevsPick);
    //used in customer order

    List<OmsRevsPick> getOmsRevsPickFindAll();
    //used in customer order

    OmsCoSasInvAdj persistOmsCoSasInvAdj(OmsCoSasInvAdj omsCoSasInvAdj);
    //used in customer order

    OmsCoSasInvAdj mergeOmsCoSasInvAdj(OmsCoSasInvAdj omsCoSasInvAdj);
    //used in customer order

    void removeOmsCoSasInvAdj(OmsCoSasInvAdj omsCoSasInvAdj);
    //used in customer order

    List<OmsCoSasInvAdj> getOmsCoSasInvAdjFindAll();

    List<OmsCoSasInvAdj> getOmsCoSasInvAdjFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);

    String getItemMasterFindStandardUom(String item);

    String getItemMasterFindItemStatus(String item);

    public BigDecimal getItemMasterFindDept(String item);

    String getItemMasterFindItemDesc(String item);

    OmsWebserviceUriDetail persistOmsWebserviceUriDetail(OmsWebserviceUriDetail omsWebserviceUriDetail);

    OmsWebserviceUriDetail mergeOmsWebserviceUriDetail(OmsWebserviceUriDetail omsWebserviceUriDetail);

    void removeOmsWebserviceUriDetail(OmsWebserviceUriDetail omsWebserviceUriDetail);

    List<OmsWebserviceUriDetail> getOmsWebserviceUriDetailFindAll();

    String getOmsWebserviceUriDetailFindByWebserviceName(String webServiceName);

    BigDecimal getOmsWebserviceUriDetailFindWebServiceId(String webServiceName);

    public List<BigDecimal> getWhFindByPhysicalWH(BigDecimal physicalWH);

    BigDecimal getWhFindPhyWhForVirtualWh(BigDecimal wh);

    List<Object[]> getWhFindPhysicalWH(BigDecimal wh);

    List<BigDecimal> getWhFindVirtualWh(BigDecimal physicalWH, BigDecimal channelId);
    //Used in rma

    OmsRmaReqItem persistOmsRmaReqItem(OmsRmaReqItem omsRmaReqItem);

    OmsRmaReqItem mergeOmsRmaReqItem(OmsRmaReqItem omsRmaReqItem);

    void removeOmsRmaReqItem(OmsRmaReqItem omsRmaReqItem);

    List<OmsRmaReqItem> getOmsRmaReqItemFindAll();

    OmsRmaReqItem getOmsRmaReqItemFindByRmaIdAndItem(BigDecimal rmaId, String item, BigDecimal lineNo);

    List<OmsRmaReqItem> getOmsRmaReqItemFindByRmaId(BigDecimal rmaId);

    OmsRmaReq persistOmsRmaReq(OmsRmaReq omsRmaReq);

    OmsRmaReq mergeOmsRmaReq(OmsRmaReq omsRmaReq);

    void removeOmsRmaReq(OmsRmaReq omsRmaReq);

    List<OmsRmaReq> getOmsRmaReqFindAll();

    List<BigDecimal> getOmsRmaReqFindRmaId(BigDecimal omsCustOrdNo, String rmaReqId);
    //used in rma

    OmsRmaReq getOmsRmaReqFindByOmsCustOrdNoAndRmaId(BigDecimal omsCustOrdNo, String rmaReqId);
    //used in rma

    OmsRmaReq getOmsRmaReqFindByRmaId(BigDecimal rmaId);

    List<OmsRmaReq> getOmsRmaReqFindByOmscustOrdNo(BigDecimal omsCustOrdNo);

    List<VWh> getVWhFindAll();
    //used in rma

    List<BigDecimal> getVWhFindPhyWH(Object physicalWh);

    OmsErrorCodes persistOmsErrorCodes(OmsErrorCodes omsErrorCodes);

    OmsErrorCodes mergeOmsErrorCodes(OmsErrorCodes omsErrorCodes);

    void removeOmsErrorCodes(OmsErrorCodes omsErrorCodes);

    List<OmsErrorCodes> getOmsErrorCodesFindAll();

    OmsErrorCodes getOmsErrorCodesFindByErrorCode(String omsErrorCode, String langCode);


    OmsCustUpdDelivInfo persistOmsCustUpdDelivInfo(OmsCustUpdDelivInfo omsCustUpdDelivInfo);

    OmsCustUpdDelivInfo mergeOmsCustUpdDelivInfo(OmsCustUpdDelivInfo omsCustUpdDelivInfo);

    void removeOmsCustUpdDelivInfo(OmsCustUpdDelivInfo omsCustUpdDelivInfo);

    List<OmsCustUpdDelivInfo> getOmsCustUpdDelivInfoFindAll();
    //Spare Parts Request Methods

    public OmsSparePartFulfill persistOmsSparePartFulfill(OmsSparePartFulfill omsSparePartFulfill);

    public OmsSparePartFulfill mergeOmsSparePartFulfill(OmsSparePartFulfill omsSparePartFulfill);

    public void removeOmsSparePartFulfill(OmsSparePartFulfill omsSparePartFulfill);

    List<OmsSparePartFulfill> getOmsSparePartFulfillFindByOmsServiceReqId(String omsServiceReqSeqId);

    public List<OmsSparePartFulfill> getOmsSparePartFulfillFindAll();

    public OmsSparePartHeader persistOmsSparePartHeader(OmsSparePartHeader omsSparePartHeader);

    public OmsSparePartHeader mergeOmsSparePartHeader(OmsSparePartHeader omsSparePartHeader);

    public void removeOmsSparePartHeader(OmsSparePartHeader omsSparePartHeader);

    public List<OmsSparePartHeader> getOmsSparePartHeaderFindAll();

    public OmsSparePartHeader getOmsSparePartHeaderByExtKeys(String serviceRequestId,
                                                             String sequenceId) throws SOAPException;

    public OmsSparePartHeader getOmsSparePartHeaderByServiceIdItem(String serviceRequestId,
                                                                   String Item) throws SOAPException;

    public OmsSparePartHeader getOmsSparePartHeaderByIntKey(String omsServiceRequestSeqId) throws SOAPException;

    OmsSparePartHeader getOmsSparePartHeaderByKeyComb(String omsServiceRequestSeqId,
                                                      String serviceRequestId) throws SOAPException;

    public OmsSparePartAudit persistOmsSparePartAudit(OmsSparePartAudit omsSparePartAudit);

    public OmsSparePartAudit mergeOmsSparePartAudit(OmsSparePartAudit omsSparePartAudit);

    public void removeOmsSparePartAudit(OmsSparePartAudit omsSparePartAudit);

    public List<OmsSparePartAudit> getOmsSparePartAuditFindAll();
    //cancellation

    OmsSparePartConfirmHdr persistOmsSparePartConfirmHdr(OmsSparePartConfirmHdr omsSparePartConfirmHdr);

    OmsSparePartConfirmHdr mergeOmsSparePartConfirmHdr(OmsSparePartConfirmHdr omsSparePartConfirmHdr);

    void removeOmsSparePartConfirmHdr(OmsSparePartConfirmHdr omsSparePartConfirmHdr);

    public BigDecimal getTsfdetailFindSelectedQty(BigDecimal tsfNo, String item);

    public BigDecimal getTsfdetailFindDistroQty(BigDecimal tsfNo, String item);

    List<OmsSparePartConfirmHdr> getOmsSparePartConfirmHdrFindAll();

    OmsSparePartConfirmDtl persistOmsSparePartConfirmDtl(OmsSparePartConfirmDtl omsSparePartConfirmDtl);

    OmsSparePartConfirmDtl mergeOmsSparePartConfirmDtl(OmsSparePartConfirmDtl omsSparePartConfirmDtl);

    void removeOmsSparePartConfirmDtl(OmsSparePartConfirmDtl omsSparePartConfirmDtl);

    List<OmsSparePartConfirmDtl> getOmsSparePartConfirmDtlFindAll();

    OmsSparePartCancelHdr persistOmsSparePartCancelHdr(OmsSparePartCancelHdr omsSparePartCancelHdr);

    OmsSparePartCancelHdr mergeOmsSparePartCancelHdr(OmsSparePartCancelHdr omsSparePartCancelHdr);

    void removeOmsSparePartCancelHdr(OmsSparePartCancelHdr omsSparePartCancelHdr);

    List<OmsSparePartCancelHdr> getOmsSparePartCancelHdrFindAll();

    List<OmsSparePartFulfill> getOmsSparePartFulfillFindByRequestAndItem(String serviceRequestId, String itemId);

    OmsCoAwbDetail persistOmsCoAwbDetail(OmsCoAwbDetail omsCoAwbDetail);

    OmsCoAwbDetail mergeOmsCoAwbDetail(OmsCoAwbDetail omsCoAwbDetail);

    void removeOmsCoAwbDetail(OmsCoAwbDetail omsCoAwbDetail);

    List<OmsCoAwbDetail> getOmsCoAwbDetailFindAll();

    OmsCoAwbHead persistOmsCoAwbHead(OmsCoAwbHead omsCoAwbHead);

    OmsCoAwbHead mergeOmsCoAwbHead(OmsCoAwbHead omsCoAwbHead);

    void removeOmsCoAwbHead(OmsCoAwbHead omsCoAwbHead);

    List<OmsCoAwbHead> getOmsCoAwbHeadFindAll();

    OmsCoAwbHead getOmsCoAwbHeadFindByKey(String awbUpdReqId) throws SOAPException;


    OmsSparePartConfirmHdr getOmsSparePartConfirmHdrFindByKey(String serviceReqId,
                                                              String sparePartConfimId) throws SOAPException;

    OmsSparePartCancelHdr getOmsSparePartCancelHdrByKey(String cancellationId, String sequenceId,
                                                        String serviceReqSeqId);

    OmsRmaDelDetail persistOmsRmaDelDetail(OmsRmaDelDetail omsRmaDelDetail);

    OmsRmaDelDetail mergeOmsRmaDelDetail(OmsRmaDelDetail omsRmaDelDetail);

    void removeOmsRmaDelDetail(OmsRmaDelDetail omsRmaDelDetail);

    OmsRmaDelHead getOmsRmaDelHeadFindByRmaId(BigDecimal rmaId, String status);

    List<OmsRmaDelDetail> getOmsRmaDelDetailFindByRmaDelReqId(String rmaDelReqId);

    List<OmsRmaDelDetail> getOmsRmaDelDetailFindAll();

    OmsRmaDelHead persistOmsRmaDelHead(OmsRmaDelHead omsRmaDelHead);

    OmsRmaDelHead mergeOmsRmaDelHead(OmsRmaDelHead omsRmaDelHead);

    void removeOmsRmaDelHead(OmsRmaDelHead omsRmaDelHead);

    List<OmsRmaDelHead> getOmsRmaDelHeadFindAll();

    OmsRmaDelHead getOmsRmaDelHeadFindByRmaDelReqId(String rmaDelReqId, BigDecimal rmaId);

    OmsRmaModDetail persistOmsRmaModDetail(OmsRmaModDetail omsRmaModDetail);

    OmsRmaModDetail mergeOmsRmaModDetail(OmsRmaModDetail omsRmaModDetail);

    void removeOmsRmaModDetail(OmsRmaModDetail omsRmaModDetail);

    List<OmsRmaModDetail> getOmsRmaModDetailFindAll();

    List<OmsRmaModDetail> getOmsRmaModDetailFindByRmaModReqId(String rmaModReqId);

    OmsRmaModHead persistOmsRmaModHead(OmsRmaModHead omsRmaModHead);

    OmsRmaModHead mergeOmsRmaModHead(OmsRmaModHead omsRmaModHead);

    void removeOmsRmaModHead(OmsRmaModHead omsRmaModHead);

    List<OmsRmaModHead> getOmsRmaModHeadFindAll();

    OmsRmaModHead getOmsRmaModHeadFindByRmaModReqId(String rmaModReqId) throws SOAPException;

    OmsOrposPrcOvdLine persistOmsOrposPrcOvdLine(OmsOrposPrcOvdLine omsOrposPrcOvdLine);

    OmsOrposPrcOvdLine mergeOmsOrposPrcOvdLine(OmsOrposPrcOvdLine omsOrposPrcOvdLine);

    void removeOmsOrposPrcOvdLine(OmsOrposPrcOvdLine omsOrposPrcOvdLine);

    List<OmsOrposPrcOvdLine> getOmsOrposPrcOvdLineFindAll();

    OmsOrposTravelCheckTender persistOmsOrposTravelCheckTender(OmsOrposTravelCheckTender omsOrposTravelCheckTender);

    OmsOrposTravelCheckTender mergeOmsOrposTravelCheckTender(OmsOrposTravelCheckTender omsOrposTravelCheckTender);

    void removeOmsOrposTravelCheckTender(OmsOrposTravelCheckTender omsOrposTravelCheckTender);

    List<OmsOrposTravelCheckTender> getOmsOrposTravelCheckTenderFindAll();

    OmsOrposGiftcertTender persistOmsOrposGiftcertTender(OmsOrposGiftcertTender omsOrposGiftcertTender);

    OmsOrposGiftcertTender mergeOmsOrposGiftcertTender(OmsOrposGiftcertTender omsOrposGiftcertTender);

    void removeOmsOrposGiftcertTender(OmsOrposGiftcertTender omsOrposGiftcertTender);

    List<OmsOrposGiftcertTender> getOmsOrposGiftcertTenderFindAll();

    OmsOrposPayment persistOmsOrposPayment(OmsOrposPayment omsOrposPayment);

    OmsOrposPayment mergeOmsOrposPayment(OmsOrposPayment omsOrposPayment);

    void removeOmsOrposPayment(OmsOrposPayment omsOrposPayment);

    List<OmsOrposPayment> getOmsOrposPaymentFindAll();


    OmsOrposCustOrdDel persistOmsOrposCustOrdDel(OmsOrposCustOrdDel omsOrposCustOrdDel);

    OmsOrposCustOrdDel mergeOmsOrposCustOrdDel(OmsOrposCustOrdDel omsOrposCustOrdDel);

    void removeOmsOrposCustOrdDel(OmsOrposCustOrdDel omsOrposCustOrdDel);

    List<OmsOrposCustOrdDel> getOmsOrposCustOrdDelFindAll();

    OmsOrposGeoaddr persistOmsOrposGeoaddr(OmsOrposGeoaddr omsOrposGeoaddr);

    OmsOrposGeoaddr mergeOmsOrposGeoaddr(OmsOrposGeoaddr omsOrposGeoaddr);

    void removeOmsOrposGeoaddr(OmsOrposGeoaddr omsOrposGeoaddr);

    List<OmsOrposGeoaddr> getOmsOrposGeoaddrFindAll();

    public OmsOrposGeoaddr getOmsOrposGeoaddrFindByCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposGeoaddr getOmsOrposGeoaddrFindByPaymentSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo);

    OmsOrposGeoaddr getOmsOrposGeoaddrFindByFulSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal custOrdFulSeqNo);

    OmsOrposDiscntLine persistOmsOrposDiscntLine(OmsOrposDiscntLine omsOrposDiscntLine);

    OmsOrposDiscntLine mergeOmsOrposDiscntLine(OmsOrposDiscntLine omsOrposDiscntLine);

    void removeOmsOrposDiscntLine(OmsOrposDiscntLine omsOrposDiscntLine);

    List<OmsOrposDiscntLine> getOmsOrposDiscntLineFindAll();

    OmsOrposPromoLine persistOmsOrposPromoLine(OmsOrposPromoLine omsOrposPromoLine);

    OmsOrposPromoLine mergeOmsOrposPromoLine(OmsOrposPromoLine omsOrposPromoLine);

    void removeOmsOrposPromoLine(OmsOrposPromoLine omsOrposPromoLine);

    List<OmsOrposPromoLine> getOmsOrposPromoLineFindAll();

    OmsOrposCustOrdItm persistOmsOrposCustOrdItm(OmsOrposCustOrdItm omsOrposCustOrdItm);

    OmsOrposCustOrdItm mergeOmsOrposCustOrdItm(OmsOrposCustOrdItm omsOrposCustOrdItm);

    /**
     * @param omsOrposCustOrderId
     * @param itemId
     * @param lineItemNo
     * @return
     */
    OmsOrposCustOrdItm getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(BigDecimal omsOrposCustOrderId,
                                                                           String itemId, BigDecimal lineItemNo);

    OmsOrposCustomer getOmsOrposCustomerFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposCustomer getOmsOrposCustomerFindByOmsOrposCustOrdIdAndCustomerId(BigDecimal omsOrposCustOrderId,
                                                                             String customerId);

    OmsOrposCustOrdItm getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndLineItemNo(BigDecimal omsOrposCustOrderId,
                                                                                 BigDecimal lineItemNo);

    OmsOrposLocale getOmsOrposLocaleFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposLocale getOmsOrposLocaleFindByOmsOrposCustOrdIdAndLocaleSeq(BigDecimal omsOrposCustOrderId,
                                                                        BigDecimal localeSeq);

    OmsOrposContact getOmsOrposContactFindByOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId);

    OmsOrposContact getOmsOrposContactFindByOmsOrposCustOrderIdandCustomerId(BigDecimal omsOrposCustOrderId,
                                                                             BigDecimal customerId);

    List<OmsOrposPhone> getOmsOrposPhoneFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    List<OmsOrposPhone> getOmsOrposPhoneFindByOmsOrposContactSeq(BigDecimal omsOrposCustOrderId,
                                                                 BigDecimal contactSeq);

    List<OmsOrposEmail> getOmsOrposEmailFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    List<OmsOrposEmail> getOmsOrposEmailFindByOmsOrposContactSeq(BigDecimal omsOrposCustOrderId,
                                                                 BigDecimal contactSeq);


    List<OmsOrposAddrbookEntry> getOmsOrposAddrbookEntryFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);


    OmsOrposPrcOvdLine getOmsOrposPrcOvdLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposPromoLine getOmsOrposPromoLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposDiscntLine getOmsOrposDiscntLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposTaxLine getOmsOrposTaxLineFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposAlterationItem getOmsOrposAlterationItemFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposGiftcardItem getOmsOrposGiftcardItemFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    List<OmsOrposCustOrdFul> getOmsOrposCustOrdFulFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposCustOrdDel getOmsOrposCustOrdDelFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    List<OmsOrposPayment> getOmsOrposPaymentFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    void removeOmsOrposCustOrdItm(OmsOrposCustOrdItm omsOrposCustOrdItm);

    List<OmsOrposCustOrdItm> getOmsOrposCustOrdItmFindAll();

    List<OmsOrposCustOrdItm> getOmsOrposCustOrdItmFindByOmsOrposCustOrdId(BigDecimal omsOrposCustOrderId);

    OmsOrposEmail persistOmsOrposEmail(OmsOrposEmail omsOrposEmail);

    OmsOrposEmail mergeOmsOrposEmail(OmsOrposEmail omsOrposEmail);

    void removeOmsOrposEmail(OmsOrposEmail omsOrposEmail);

    List<OmsOrposEmail> getOmsOrposEmailFindAll();

    OmsOrposCouponTender persistOmsOrposCouponTender(OmsOrposCouponTender omsOrposCouponTender);

    OmsOrposCouponTender mergeOmsOrposCouponTender(OmsOrposCouponTender omsOrposCouponTender);

    void removeOmsOrposCouponTender(OmsOrposCouponTender omsOrposCouponTender);

    List<OmsOrposCouponTender> getOmsOrposCouponTenderFindAll();

    OmsOrposTaxLine persistOmsOrposTaxLine(OmsOrposTaxLine omsOrposTaxLine);

    OmsOrposTaxLine mergeOmsOrposTaxLine(OmsOrposTaxLine omsOrposTaxLine);

    void removeOmsOrposTaxLine(OmsOrposTaxLine omsOrposTaxLine);

    List<OmsOrposTaxLine> getOmsOrposTaxLineFindAll();

    OmsOrposGiftcardTender persistOmsOrposGiftcardTender(OmsOrposGiftcardTender omsOrposGiftcardTender);

    OmsOrposGiftcardTender mergeOmsOrposGiftcardTender(OmsOrposGiftcardTender omsOrposGiftcardTender);

    void removeOmsOrposGiftcardTender(OmsOrposGiftcardTender omsOrposGiftcardTender);

    List<OmsOrposGiftcardTender> getOmsOrposGiftcardTenderFindAll();

    OmsOrposContact persistOmsOrposContact(OmsOrposContact omsOrposContact);

    OmsOrposContact mergeOmsOrposContact(OmsOrposContact omsOrposContact);

    void removeOmsOrposContact(OmsOrposContact omsOrposContact);

    List<OmsOrposContact> getOmsOrposContactFindAll();

    OmsOrposContact getOmsOrposContactFindByCustomerId(BigDecimal omsOrposCustOrderId, String customerId);

    OmsOrposContact getOmsOrposContactFindByFulSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal custOrdFulSeqNo);

    OmsOrposContact getOmsOrposContactFindByPaymentSeqNo(BigDecimal omsOrposCustOrderId, BigDecimal PaymentSeqNo);

    OmsOrposCustomerGrpIdLst persistOmsOrposCustomerGrpIdLst(OmsOrposCustomerGrpIdLst omsOrposCustomerGrpIdLst);

    OmsOrposCustomerGrpIdLst mergeOmsOrposCustomerGrpIdLst(OmsOrposCustomerGrpIdLst omsOrposCustomerGrpIdLst);

    void removeOmsOrposCustomerGrpIdLst(OmsOrposCustomerGrpIdLst omsOrposCustomerGrpIdLst);

    List<OmsOrposCustomerGrpIdLst> getOmsOrposCustomerGrpIdLstFindAll();

    OmsOrposGiftcardItem persistOmsOrposGiftcardItem(OmsOrposGiftcardItem omsOrposGiftcardItem);

    OmsOrposGiftcardItem mergeOmsOrposGiftcardItem(OmsOrposGiftcardItem omsOrposGiftcardItem);

    void removeOmsOrposGiftcardItem(OmsOrposGiftcardItem omsOrposGiftcardItem);

    List<OmsOrposGiftcardItem> getOmsOrposGiftcardItemFindAll();

    OmsOrposCustomer persistOmsOrposCustomer(OmsOrposCustomer omsOrposCustomer);

    OmsOrposCustomer mergeOmsOrposCustomer(OmsOrposCustomer omsOrposCustomer);

    void removeOmsOrposCustomer(OmsOrposCustomer omsOrposCustomer);

    List<OmsOrposCustomer> getOmsOrposCustomerFindAll();

    OmsOrposCustOrdFul persistOmsOrposCustOrdFul(OmsOrposCustOrdFul omsOrposCustOrdFul);

    OmsOrposCustOrdFul mergeOmsOrposCustOrdFul(OmsOrposCustOrdFul omsOrposCustOrdFul);

    void removeOmsOrposCustOrdFul(OmsOrposCustOrdFul omsOrposCustOrdFul);

    List<OmsOrposCustOrdFul> getOmsOrposCustOrdFulFindAll();

    OmsOrposCustOrderHead persistOmsOrposCustOrderHead(OmsOrposCustOrderHead omsOrposCustOrderHead);

    OmsOrposCustOrderHead mergeOmsOrposCustOrderHead(OmsOrposCustOrderHead omsOrposCustOrderHead);

    void removeOmsOrposCustOrderHead(OmsOrposCustOrderHead omsOrposCustOrderHead);

    List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadFindAll();

    List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadFindByOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId);

    OmsOrposCustOrderHead getOmsOrposCustOrderHeadDetailsByOrderId(String customerOrderId);

    BigDecimal getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(String customerOrderId);

    OmsOrposPhone persistOmsOrposPhone(OmsOrposPhone omsOrposPhone);

    OmsOrposPhone mergeOmsOrposPhone(OmsOrposPhone omsOrposPhone);

    void removeOmsOrposPhone(OmsOrposPhone omsOrposPhone);

    List<OmsOrposPhone> getOmsOrposPhoneFindAll();

    OmsOrposAlterationItem persistOmsOrposAlterationItem(OmsOrposAlterationItem omsOrposAlterationItem);

    OmsOrposAlterationItem mergeOmsOrposAlterationItem(OmsOrposAlterationItem omsOrposAlterationItem);

    void removeOmsOrposAlterationItem(OmsOrposAlterationItem omsOrposAlterationItem);

    List<OmsOrposAlterationItem> getOmsOrposAlterationItemFindAll();

    OmsOrposCheckTender persistOmsOrposCheckTender(OmsOrposCheckTender omsOrposCheckTender);

    OmsOrposCheckTender mergeOmsOrposCheckTender(OmsOrposCheckTender omsOrposCheckTender);

    void removeOmsOrposCheckTender(OmsOrposCheckTender omsOrposCheckTender);

    List<OmsOrposCheckTender> getOmsOrposCheckTenderFindAll();

    OmsOrposCreditDebitTender persistOmsOrposCreditDebitTender(OmsOrposCreditDebitTender omsOrposCreditDebitTender);

    OmsOrposCreditDebitTender mergeOmsOrposCreditDebitTender(OmsOrposCreditDebitTender omsOrposCreditDebitTender);

    void removeOmsOrposCreditDebitTender(OmsOrposCreditDebitTender omsOrposCreditDebitTender);

    List<OmsOrposCreditDebitTender> getOmsOrposCreditDebitTenderFindAll();

    OmsOrposLocale persistOmsOrposLocale(OmsOrposLocale omsOrposLocale);

    OmsOrposLocale mergeOmsOrposLocale(OmsOrposLocale omsOrposLocale);

    void removeOmsOrposLocale(OmsOrposLocale omsOrposLocale);

    List<OmsOrposLocale> getOmsOrposLocaleFindAll();

    OmsOrposAddrbookEntry persistOmsOrposAddrbookEntry(OmsOrposAddrbookEntry omsOrposAddrbookEntry);

    OmsOrposAddrbookEntry mergeOmsOrposAddrbookEntry(OmsOrposAddrbookEntry omsOrposAddrbookEntry);

    void removeOmsOrposAddrbookEntry(OmsOrposAddrbookEntry omsOrposAddrbookEntry);

    List<OmsOrposAddrbookEntry> getOmsOrposAddrbookEntryFindAll();


    BigDecimal getDepsFindGroupNo(BigDecimal dept);


    List<BigDecimal> getOmsOrposCustOrderPickupFindCustOrderPicVoSeq(String customerOrderId);

    BigDecimal getMaxOmsOrposCustOrderPickUp(String customerOrderId);

    List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadfindColumns(String customerOrderId);

    List<OmsOrposCustOrderPickup> getOmsOrposCustOrderPickupFindAllColumns(BigDecimal custOrderPicVoSeq);

    List<OmsOrposCustOrdItm> getOmsOrposCustOrdItmFindColumns(BigDecimal omsOrposCustOrderId);

    List<OmsOrposCustOrdItmPickup> getOmsOrposCustOrdItmPickupFindAllColumns(BigDecimal omsOrposCustOrderId,
                                                                             BigDecimal custOrderPicVoSeq);

    List<OmsOrposDiscntLine> getOmsOrposDiscntLineFindColumns(BigDecimal omsOrposCustOrderId,
                                                              BigDecimal capturedLineItemNo, String itemId);

    List<OmsOrposDiscntLinePickup> getOmsOrposDiscntLinePickupFindColumns(BigDecimal omsOrposCustOrderId,
                                                                          BigDecimal custOrderPicVoSeq,
                                                                          BigDecimal lineItemNo);

    List<OmsOrposTaxLine> getOmsOrposTaxLineFindColumns(BigDecimal omsOrposCustOrderId, BigDecimal capturedLineItemNo,
                                                        String itemId);

    List<OmsOrposTaxLinePickup> getOmsOrposTaxLinePickupFindColumns(BigDecimal omsOrposCustOrderId,
                                                                    BigDecimal custOrderPicVoSeq,
                                                                    BigDecimal lineItemNo);

    List<OmsCustOrdHead> getOmsCustOrdHeadFindColumns(String custOrderNo);

    BigDecimal getOmsCustOrdHeadFindOmsCustOrdNo(String custOrderNo);

    List<OmsOrposCustOrdItmPickup> getOmsOrposCustOrdItmPickupFindColumns(BigDecimal omsOrposCustOrderId,
                                                                          BigDecimal custOrderPicVoSeq,
                                                                          String fulfillOrderId);

    List<Addr> getAddrFindByAddrKeyValue1(String keyValue1);

    List<BigDecimal> getOmsOrposCustOrderRtnFindCustOrderRtnSeq(String customerOrderId);

    BigDecimal getMaxOmsOrposCustOrderRtn(BigDecimal omsOrposCustOrderId);


    List<OmsOrposCustOrderRtn> getOmsOrposCustOrderRtnFindAllColumns(String customerOrderId);


    List<OmsOrposCustOrdItmRtn> getOmsOrposCustOrdItmRtnfindColumns(BigDecimal omsOrposCustOrderId,
                                                                    BigDecimal custOrderRtnSeqNo);

    List<OmsOrposDiscntLine> getOmsOrposDiscntLineFindAllColumns(BigDecimal omsOrposCustOrderId,
                                                                 BigDecimal capturedLineItemNo);

    List<OmsOrposDiscntLineRtn> getOmsOrposDiscntLineRtnFindAllColumns(BigDecimal omsOrposCustOrderId,
                                                                       BigDecimal custOrderRtnSeqNo,
                                                                       BigDecimal lineItemNo);

    List<OmsOrposTaxLine> getOmsOrposTaxLineFindAllColumns(BigDecimal omsOrposCustOrderId,
                                                           BigDecimal capturedLineItemNo);

    List<OmsOrposTaxLineRtn> getOmsOrposTaxLineRtnFindAllColumns(BigDecimal omsOrposCustOrderId,
                                                                 BigDecimal custOrderRtnSeqNo, BigDecimal lineItemNo);

    OmsOrposCustOrderRtn persistOmsOrposCustOrderRtn(OmsOrposCustOrderRtn omsOrposCustOrderRtn);

    OmsOrposCustOrderRtn mergeOmsOrposCustOrderRtn(OmsOrposCustOrderRtn omsOrposCustOrderRtn);

    void removeOmsOrposCustOrderRtn(OmsOrposCustOrderRtn omsOrposCustOrderRtn);

    List<OmsOrposCustOrderRtn> getOmsOrposCustOrderRtnFindAll();

    OmsOrposCustOrdItmRtn persistOmsOrposCustOrdItmRtn(OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn);

    OmsOrposCustOrdItmRtn mergeOmsOrposCustOrdItmRtn(OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn);

    void removeOmsOrposCustOrdItmRtn(OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn);

    List<OmsOrposCustOrdItmRtn> getOmsOrposCustOrdItmRtnFindAll();

    OmsOrposDiscntLineRtn persistOmsOrposDiscntLineRtn(OmsOrposDiscntLineRtn omsOrposDiscntLineRtn);

    OmsOrposDiscntLineRtn mergeOmsOrposDiscntLineRtn(OmsOrposDiscntLineRtn omsOrposDiscntLineRtn);

    void removeOmsOrposDiscntLineRtn(OmsOrposDiscntLineRtn omsOrposDiscntLineRtn);

    List<OmsOrposDiscntLineRtn> getOmsOrposDiscntLineRtnFindAll();


    OmsOrposTaxLineRtn persistOmsOrposTaxLineRtn(OmsOrposTaxLineRtn omsOrposTaxLineRtn);

    OmsOrposTaxLineRtn mergeOmsOrposTaxLineRtn(OmsOrposTaxLineRtn omsOrposTaxLineRtn);

    void removeOmsOrposTaxLineRtn(OmsOrposTaxLineRtn omsOrposTaxLineRtn);

    List<OmsOrposTaxLineRtn> getOmsOrposTaxLineRtnFindAll();

    OmsOrposTaxLinePickup persistOmsOrposTaxLinePickup(OmsOrposTaxLinePickup omsOrposTaxLinePickup);

    OmsOrposCustOrderPickup persistOmsOrposCustOrderPickup(OmsOrposCustOrderPickup omsOrposCustOrderPickup);

    OmsOrposCustOrdItmPickup persistOmsOrposCustOrdItmPickup(OmsOrposCustOrdItmPickup omsOrposCustOrdItmPickup);

    OmsOrposDiscntLinePickup persistOmsOrposDiscntLinePickup(OmsOrposDiscntLinePickup omsOrposDiscntLinePickup);

    OmsRepublishData persistOmsRepublishData(OmsRepublishData omsRepublishData);

    //Used in Shipping Classification WS

    UdaItemLov persistUdaItemLov(UdaItemLov udaItemLov);

    UdaItemLov mergeUdaItemLov(UdaItemLov udaItemLov);

    void removeUdaItemLov(UdaItemLov udaItemLov);

    List<UdaItemLov> getUdaItemLovFindAll();

    /**
     * @param udaId
     * @return
     */
    BigDecimal getUdaItemLovFindItemAndUdaValueByUdaId(BigDecimal udaId, String item);

    UdaValues persistUdaValues(UdaValues udaValues);

    UdaValues mergeUdaValues(UdaValues udaValues);

    void removeUdaValues(UdaValues udaValues);

    List<UdaValues> getUdaValuesFindAll();

    /**
     * @param udaId
     * @param udaValue
     * @return
     */
    String getUdaValueDescByUdaIdAndUdaValue(BigDecimal udaId, BigDecimal udaValue);

    OmsReferenceData persistOmsReferenceData(OmsReferenceData omsReferenceData);

    OmsReferenceData mergeOmsReferenceData(OmsReferenceData omsReferenceData);

    void removeOmsReferenceData(OmsReferenceData omsReferenceData);

    List<OmsReferenceData> getOmsReferenceDataFindAll();

    /**
     * @param refKey1
     * @param refKey2
     * @param refKey3
     * @return
     */
    String getOmsReferenceDataFindRefValue(String refKey1, String refKey2, String refKey3);

    String getOmsReferenceDataFindByISOCode(String refKey1, String refKey3);
    //XXItemCustomInfo

    XxItemCustomInfo persistXxItemCustomInfo(XxItemCustomInfo xxItemCustomInfo);

    XxItemCustomInfo mergeXxItemCustomInfo(XxItemCustomInfo xxItemCustomInfo);

    void removeXxItemCustomInfo(XxItemCustomInfo xxItemCustomInfo);

    List<XxItemCustomInfo> getXxItemCustomInfoFindAll();

    String getXxItemCustomInfoFindCourierValue(String item);


    BigDecimal getStoreFindOrgUnit(BigDecimal store);

    BigDecimal getStoreFindChannelId(BigDecimal store);

    BigDecimal getPartnerOrgUnitFindOrgUnitId(BigDecimal partner);

    List<String> getCodeDetailFindCode(String codeType);


    BigDecimal getOmsCustOrdTenderCancelFindByOmsCancelIdandomsCustOrdNumberByMaxSeqNo(BigDecimal omsCancelId,
                                                                                       BigDecimal omsCustOrdNo);

    OmsBackOrderDtl persistOmsBackOrderDtl(OmsBackOrderDtl omsBackOrderDtl);

    OmsBackOrderDtl mergeOmsBackOrderDtl(OmsBackOrderDtl omsBackOrderDtl);

    List<OmsBackOrderDtl> getOmsBackOrderDtlFindBackOrders(Timestamp fulInvAvlDate);


    BigDecimal getOmsBackOrderDtlFindAssignedInvOrders(BigDecimal sourceLoc, String item);

    List<BigDecimal> getOmsBackOrderDtlFindOmsCustOrdNoForBackOrder(Timestamp fulInvAvlDate);

    List<OmsBackOrderDtl> getOmsBackOrderDtlFindByOmsCustOrdNo(BigDecimal omsCustOrdNo);

    List<OmsBackOrderDtl> getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNo(BigDecimal omsCustOrdNo, BigDecimal lineNo);

    OmsBackOrderDtl getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNoAndLoc(BigDecimal omsCustOrdNo, BigDecimal lineNo,
                                                                       BigDecimal sourceLoc);

    List<OmsBackOrderDtl> getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(BigDecimal omsCustOrdNo, String item,
                                                                        BigDecimal lineNo);

    String getOmsErrorCodesFindByonlyErrorCode(String omsErrorCode);

    String getItemMasterFindInventoryInd(String item, BigDecimal dept);

    OmsOrposRefundTender persistOmsOrposRefundTender(OmsOrposRefundTender omsOrposRefundTender);

    OmsOrposRefundTender mergeOmsOrposRefundTender(OmsOrposRefundTender omsOrposRefundTender);

    void removeOmsOrposRefundTender(OmsOrposRefundTender omsOrposRefundTender);

    List<OmsOrposRefundTender> getOmsOrposRefundTenderFindAll();

    List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindByItem(BigDecimal omsCustOrdNo, BigDecimal lineNo, String item);

    public BigDecimal getOmsOrpospaymentFindMaxSeqNoByomsOrposCustOrderId(BigDecimal omsOrposCustOrderId);

    public BigDecimal getOmsOrposRefundCustOrdTenderFindMaxTenderSeqNo(BigDecimal omsCustOrdNo);

    public BigDecimal getOmsOrposCustOrderHeadFindByCustomerOrderId(String customerOrderId);

    public List<OmsOrposCustOrdItmRtn> getOmsOrposCustOrdItmRtnFindByOmsOrposCustOrdIdAndLineItemNo(BigDecimal omsOrposCustOrderId,
                                                                                                    BigDecimal lineItemNo);

    public OmsOrposCustOrderHead getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(String customerOrderId,
                                                                                        String status);

    public List<OmsOrposCustOrderHead> getOmsOrposCustOrderHeadfindBycustometOrderIdandstatusList(String customerOrderId,
                                                                                                  String status);

    public OmsCustOrdHead getOmsCustOrdHeadfindByCustOrdNoAndStatus(String custOrderNo, String status);

    public List<OmsCoFulfillDetail> getOmsCoFulfillDetailFindByOmsCustOrdNoandLineNoandItemId(BigDecimal omsCustOrdNo,
                                                                                              BigDecimal lineNo,
                                                                                              String item);

    public List<Tsfdetail> getTsfdetailFindQty(BigDecimal tsfNo, String item);

    public OmsCoCancelHead getOmsCoCancelHeadFindCustOrderNoByomsCancelId(BigDecimal omsCancelId);

    public PosTenderTypeHead persistPosTenderTypeHead(PosTenderTypeHead posTenderTypeHead);

    public PosTenderTypeHead mergePosTenderTypeHead(PosTenderTypeHead posTenderTypeHead);

    public void removePosTenderTypeHead(PosTenderTypeHead posTenderTypeHead);

    public List<PosTenderTypeHead> getPosTenderTypeHeadFindAll();

    public String getPosTenderTypeHeadFindByTenderTypeId(BigDecimal tenderTypeId);

    public String getOmsSystemParameterFindByParameterValue(String parameterValue);

    public OmsOrposCustOrderHead getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(BigDecimal omsOrposCustOrderId);

    public OmsCoCancelItem getOmsCoCancelItemFindByOmsCancelIdandLineNo(BigDecimal omsCancelId, BigDecimal lineNo);

    public List<OmsCoFulfillDetail> getOmsCoFulfillDetailfindByOmsCustOrdNoLineNoandItemsrcandFul(BigDecimal omsCustOrdNo,
                                                                                                  BigDecimal lineNo);

    public String getItemMasterFindItemDescSecondary(String item);

    /**
     * @param customerOrderNo
     * @return
     */
    public BigDecimal getOrdCustCountofCustomerOrderNo(String customerOrderNo);

    public OmsOrposMasterAudit persistOmsOrposMasterAudit(OmsOrposMasterAudit omsOrposMasterAudit);

    public OmsOrposMasterAudit mergeOmsOrposMasterAudit(OmsOrposMasterAudit omsOrposMasterAudit);

    public void removeOmsOrposMasterAudit(OmsOrposMasterAudit omsOrposMasterAudit);

    public List<OmsOrposMasterAudit> getOmsOrposMasterAuditFindAll();

    public OmsCustOrdItemDisc persistOmsCustOrdItemDisc(OmsCustOrdItemDisc omsCustOrdItemDisc);

    public OmsCustOrdItemDisc mergeOmsCustOrdItemDisc(OmsCustOrdItemDisc omsCustOrdItemDisc);

    public void removeOmsCustOrdItemDisc(OmsCustOrdItemDisc omsCustOrdItemDisc);

    public List<OmsCustOrdItemDisc> getOmsCustOrdItemDiscFindAll();
    
    public List<OmsCustOrdItemDisc> getOmsCustOrdItemDiscFindByOmsCustordNoLineNo(BigDecimal omsCustOrdNo, BigDecimal lineNo);
    
    public OmsSimCancelTsfDetail persistOmsSimCancelTsfDetail(OmsSimCancelTsfDetail omsSimCancelTsfDetail);

    public OmsSimCancelTsfDetail mergeOmsSimCancelTsfDetail(OmsSimCancelTsfDetail omsSimCancelTsfDetail);

    public void removeOmsSimCancelTsfDetail(OmsSimCancelTsfDetail omsSimCancelTsfDetail);

    public List<OmsSimCancelTsfDetail> getOmsSimCancelTsfDetailFindAll();

    public List<OmsOrposMasterAudit> getOmsOrposMasterAuditfindByOrderIdandOrposTransactionNumber(String orderId,
                                                                                            String orposTransactionNumber);
    
    public OmsOrposDiscntLine getOmsOrposDiscntLineCustOrdIdLineNoDiscntLineNo(BigDecimal omsOrposCustOrderId, BigDecimal lineNo, BigDecimal capturedLineItemNo);
    
    //Added for finding sum of discounts
    public BigDecimal getOmsCustOrdItemDiscfindByOmsCustordNoLineNoUnitDiscnt(BigDecimal omsCustOrdNo,BigDecimal lineNo);
    
    public OmsTsfCancelledQtySumm persistOmsTsfCancelledQtySumm(OmsTsfCancelledQtySumm omsTsfCancelledQtySumm);

    public OmsTsfCancelledQtySumm mergeOmsTsfCancelledQtySumm(OmsTsfCancelledQtySumm omsTsfCancelledQtySumm);

    public void removeOmsTsfCancelledQtySumm(OmsTsfCancelledQtySumm omsTsfCancelledQtySumm);

    public List<OmsTsfCancelledQtySumm> getOmsTsfCancelledQtySummFindAll();
    
    public OmsTsfCancelledQtySumm getOmsTsfCancelledQtySummfindByCustOrdNoTsfnoandOmsCustOrdNo(String custOrderNo,BigDecimal tsfNo,BigDecimal omsCustOrdNo); 

    public List<Tsfdetail> getTsfDetailTransferQty(BigDecimal tsfNo); 
    
    public int updateOmsCustOrdHeadforOrdPaymentStatus(BigDecimal omscustordNo);
    
    OmsItemLocSync persistOmsItemLocSync(OmsItemLocSync omsItemLocSync);

    OmsItemLocSync mergeOmsItemLocSync(OmsItemLocSync omsItemLocSync);

    int removeOmsItemLocSync(OmsItemLocSync omsItemLocSync);

    List<OmsItemLocSync> getOmsItemLocSyncFindAll();
        
    public String getItemFromItemLocSync(String Item , BigDecimal location);
   
    public String getMaxFulFilOrdNofromOrdCust(String customerOrderNo);
    
    OmsPaymentSync persistOmsPaymentSync(OmsPaymentSync omsPaymentSync);

    OmsPaymentSync mergeOmsPaymentSync(OmsPaymentSync omsPaymentSync);

    void removeOmsPaymentSync(OmsPaymentSync omsPaymentSync);

    List<OmsPaymentSync> getOmsPaymentSyncFindAll();
    
    public BigDecimal  getQuantityOmsPaymentSyncBasedOnItemAndLoc(String item, BigDecimal loc);
    
    List<OmsRtlogPublishLog> getOmsRtlogPublishLogFindByOmsCustOrderNo(BigDecimal omsCustOrdNo);
    
    List<OmsPaymentSync> getOmsPaymentSychfindByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    
    public List<Ordcust> getOrdcustFindByCustomerOrderNoandStatus(String customerOrderNo);

    /**
     * @param ordcustDetail
     * @return
     */
    OrdcustDetail persistOrdcustDetail(OrdcustDetail ordcustDetail);

    OrdcustDetail mergeOrdcustDetail(OrdcustDetail ordcustDetail);

    void removeOrdcustDetail(OrdcustDetail ordcustDetail);

    List<OrdcustDetail> getOrdcustDetailFindAll();
    
    public List<OrdcustDetail> getOrdcustDetailFindByOrdCustNo(BigDecimal ordcustNo); 

    OmsUnapprovedTransfers persistOmsUnapprovedTransfers(OmsUnapprovedTransfers omsUnapprovedTransfers);

    OmsUnapprovedTransfers mergeOmsUnapprovedTransfers(OmsUnapprovedTransfers omsUnapprovedTransfers);

    void removeOmsUnapprovedTransfers(OmsUnapprovedTransfers omsUnapprovedTransfers);

    List<OmsUnapprovedTransfers> getOmsUnapprovedTransfersFindAll();
    
    OmsUnapprovedTransfers getOmsUnapprovedTransfersFindByOmsCustOrderNo(BigDecimal tsfNo, String item,BigDecimal location);
    List<OmsUnapprovedTransfers> getOmsUnapprovedTransfersFindByQtyByLoc( String item,BigDecimal location);
    
    List<OmsUnapprovedTransfers> getOmsUnapprovedTransfersfindByTsfNoandOmsCustOrdNo(BigDecimal tsfNo, BigDecimal omsCustOrdNo);
    
    List<OmsCoCancelHead> getOmsCoCancelHeadFindByCustOrdNoforCancelledOrder(String custOrdNo,String refundCompltInd);
    
    List<OmsCoCancelHead> getOmsCoCancelHeadFindByCustOrdNoforORPOSRefundOrder(String custOrdNo,String refundCompltInd,String refundOption);
    
    List<OmsRmaReq> getOmsRmaReqFindByOmscustOrdNoforRMAReturn(BigDecimal omsCustOrdNo);
    
    List<OmsRmaReq> getOmsRmaReqFindByOmscustOrdNoforRMAORPOS(BigDecimal omsCustOrdNo);
    
    OmsResUnresvCustOrderLog persistOmsResUnresvCustOrderLog(OmsResUnresvCustOrderLog omsResUnresvCustOrderLog);

    OmsResUnresvCustOrderLog mergeOmsResUnresvCustOrderLog(OmsResUnresvCustOrderLog omsResUnresvCustOrderLog);

    void removeOmsResUnresvCustOrderLog(OmsResUnresvCustOrderLog omsResUnresvCustOrderLog);

    List<OmsResUnresvCustOrderLog> getOmsResUnresvCustOrderLogFindAll();
    public List<ItemSuppCountryDim> getItemSuppCountryDimFindByItemId(String item);
    public BigDecimal getOmsCustOrdTenderFindTenderTypeIdByOmsCustOrdNo(BigDecimal omsCustOrdNo);
    OmsCustOrdTracking persistOmsCustOrdTracking(OmsCustOrdTracking omsCustOrdTracking);
    OmsCustOrdTracking mergeOmsCustOrdTracking(OmsCustOrdTracking omsCustOrdTracking);
    void removeOmsCustOrdTracking(OmsCustOrdTracking omsCustOrdTracking);
    public String getOrdcustFindBillPhoneByCustomerOrderNo(String customerOrderNo);
    
    CfsSmsEmailStatusInfo persistCfsSmsEmailStatusInfo(CfsSmsEmailStatusInfo cfsSmsEmailStatusInfo);

      CfsSmsEmailStatusInfo mergeCfsSmsEmailStatusInfo(CfsSmsEmailStatusInfo cfsSmsEmailStatusInfo);

      void removeCfsSmsEmailStatusInfo(CfsSmsEmailStatusInfo cfsSmsEmailStatusInfo);

      List<CfsSmsEmailStatusInfo> getCfsSmsEmailStatusInfoFindAll();

    void persistOmsWSPublishData(OmsWSPublishData omsWSPublishData);
}
