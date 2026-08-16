package com.logicinfo.oms.beans;
//ORPOS


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItm;
import com.logicinfo.oms.ejb.OmsOrposCustOrderHead;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.PreOrderItemLocationSync;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorditmdesc.v1.CustOrdItmDesc;
import com.oracle.retail.integration.base.bo.discntlinedesc.v1.DiscntLineDesc;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException;


public class SourceLocIdentify {
    public SourceLocIdentify() {
        super();
    }
    private final static Logger log = Logger.getLogger(com.logicinfo.oms.beans.SourceLocIdentify.class.getName());
    String error = null;
    TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = null;

    Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap = null;
    boolean isPO = false;
    int nonInvenandShipping = 0;
    int invalidItem = 0;
    boolean bo = false;
    boolean status = false;

    public Map<BigDecimal, ArrayList<OmsTempCoFo>> processSplitOrders(BigDecimal omsCustOrdNo,
                                                                      CustOrderDesc custOrderDesc, long SOH,
                                                                      String fulFillLocType, BigDecimal nextLoc,
                                                                      BigDecimal sourceLocId, String sourceLocType,
                                                                      int priority, BigDecimal combID,
                                                                      BigDecimal fulfillLocId,
                                                                      BigDecimal omsOrposCustOrderId) throws SOAPException,
                                                                                                             EntityAlreadyExistsWSFaultException,
                                                                                                             IllegalArgumentWSFaultException,
                                                                                                             IllegalStateWSFaultException,
                                                                                                             ValidationWSFaultException {
        log.info("omsCustOrdNo " + omsCustOrdNo + "***OMS_SPLIT_ORDER = Y processSplitOrders starts***");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        OmsFulfillMatrixExtDetail fulfillMatrixExtDetailResult = null;
        OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
        TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> treemap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
        int maxFulfillOrderNo = oMSUtilCommons.returnMaxFulFilOrdNo(custOrderDesc.getCustomerOrderId());
        int intialNo = maxFulfillOrderNo;
        log.info("================maxFulfillOrderNo=====================" + maxFulfillOrderNo);
        ;

        sohMap = new HashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
        log.info("omsCustOrdNo " + omsCustOrdNo + "log version for omsCustOrdNo" + omsCustOrdNo);
        //log.info("omsCustOrdNo "+omsCustOrdNo +"sohMap hashmap value"+sohMap.keySet());
        //added code for duplicate items - fulfillment logic
        Map<String, BigDecimal> storeItemMap = new HashMap<String, BigDecimal>();
        Map<String, ItemSOH> itemQtyMap = new HashMap<String, ItemSOH>();
        Map<String, FulfillOrdandSourceLocPOJO> itemCounterMap =
            new ConcurrentHashMap<String, FulfillOrdandSourceLocPOJO>();
        BigDecimal storeSOH = BigDecimal.ZERO;
        BigDecimal virtualWH = BigDecimal.ZERO;
        String deliveryLocType = null;
        boolean isBackOrder;
        //added for fulfillment logic
        //    Map<String,BigDecimal> itemCounterMap=new  HashMap<String,BigDecimal>();  //commented for new PO code
        //SourceLocIdentify sourceLocIdentify=new SourceLocIdentify();
        //Added code for Shipping Classification - fetching data from OmsSystemParameters table based on parameter-id
        //and parameter-name

        //   String parameterValue=session.getOmsSystemParametersFindIndValue("UDA_ID", "OMS_SYSTEM_OPTION");
        Map<String, CustOrdItmDesc> groupedItemMap = groupItem(omsCustOrdNo, custOrderDesc);
        OmsPersistence omsPersistence1 = new OmsPersistence();
        omsPersistence1.persistOmsCustOrdItem(custOrderDesc, groupedItemMap);
        List<OmsCustOrdItem> omsCustOrdItem1 = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
        List<OmsCustOrdItem> omsCustOrdItem2 = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
        log.info("omsCustOrdNo " + omsCustOrdNo + " groupedItemMap.keySet() " + groupedItemMap.keySet());
        log.info("omsCustOrdNo " + omsCustOrdNo + " Before entring For each loop groupedItemMap.size() " +
                 groupedItemMap.size());
        int itemLevel = 0;

        for (String groupItemKey : groupedItemMap.keySet())
        //  for(OmsCustOrdItem omsCustOrdItem:omsCustOrdItem3)
        {
            log.info("omsCustOrdNo " + omsCustOrdNo + "groupItemKey " + groupItemKey);
            log.info("omsCustOrdNo " + omsCustOrdNo + "groupedItemMap.get(groupItemKey) " +
                     groupedItemMap.get(groupItemKey));
            CustOrdItmDesc custOrdItmDesc = groupedItemMap.get(groupItemKey);
            log.info("omsCustOrdNo " + omsCustOrdNo + "groupedItemMap.size() " + groupedItemMap.size());

            boolean isPartialPO = false;
            int partialPoQty = 0;
            isBackOrder = false;
            //Step 1: If particular item is not marked as back order proceed else do nothing
            log.info("omsCustOrdNo " + omsCustOrdNo + " lineNo " + custOrdItmDesc.getLineItemNo());
            log.info("omsCustOrdNo " + omsCustOrdNo + " Item " + custOrdItmDesc.getItemId());
            log.info("omsCustOrdNo " + omsCustOrdNo + " custOrdItmDesc.getShippingChargeFlag().value() " +
                     custOrdItmDesc.getShippingChargeFlag().value());
            itemLevel++;
            log.info("omsCustOrdNo " + omsCustOrdNo + "itemLevel " + itemLevel);
            if (custOrdItmDesc.getShippingChargeFlag().value().equals("Y") == false) {
                priority = 1;
                //code for Shipping Charges
                log.info("omsCustOrdNo " + omsCustOrdNo + "Code for shipping charges starts");
                String shipingChargeDept =
                    session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");

                //Fetching the item department based on the input item sent from the request
                log.info("omsCustOrdNo " + omsCustOrdNo + "Item : " + custOrdItmDesc.getItemId());
                BigDecimal itemDept = null;
                try {
                    log.info("omsCustOrdNo " + omsCustOrdNo + "chrecking for item dept");
                    itemDept = session.getItemMasterFindDept(custOrdItmDesc.getItemId());
                    log.info("omsCustOrdNo " + omsCustOrdNo + "item dept is set " + itemDept);

                } catch (Exception e) {

                    invalidItem++;
                    if (invalidItem == omsCustOrdItem1.size()) {
                        custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_107"); //invalid item
                        log.info("omsCustOrdNo " + omsCustOrdNo + "invalidItem " + invalidItem);
                        log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdItem1.size() " + omsCustOrdItem1.size());
                        fulfillDetailMap = null;
                        sohMap = null;
                        break;
                    }
                }

                String inventoryIndn = session.getItemMasterFindInventoryInd(custOrdItmDesc.getItemId(), itemDept);
                log.info("omsCustOrdNo " + omsCustOrdNo + " inventoryIndn " + inventoryIndn);
                log.info("omsCustOrdNo " + omsCustOrdNo + "shipingChargeDept " + shipingChargeDept);
                if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {

                    nonInvenandShipping++;

                    if (nonInvenandShipping == omsCustOrdItem2.size()) {

                        log.info("omsCustOrdNo " + omsCustOrdNo + "setting error code for OMS_ORPOS_ERROR_112");
                        custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_112"); //shipping charge & non-inventory
                        log.info("omsCustOrdNo " + omsCustOrdNo + "nonInvenandShipping " + nonInvenandShipping);
                        log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdItem2.size() " + omsCustOrdItem2.size());
                        fulfillDetailMap = null;
                        sohMap = null;
                        break;
                    }

                }
                //code for Shipping Charges - Checking for the department based on the item
                if (shipingChargeDept.equals(itemDept.toString()) == false && inventoryIndn.equals("Y")) {

                    log.info("omsCustOrdNo " + omsCustOrdNo + "Shipping loop started");

                    CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
                    // sourceLocId = new BigDecimal(input.getPickLoc());

                    log.info("omsCustOrdNo " + omsCustOrdNo + "item:" + custOrdItmDesc.getItemId());
                    long L_Cum_Ord_Qty = 0L;
                    long L_Pending_Qty = 0L;
                    //Step 2:Find the fulfiment location
                    isPO = false;
                    bo = false;
                    while (L_Cum_Ord_Qty < custOrdItmDesc.getQuantity().longValue() && isPO == false &&
                           isBackOrder == false && bo == false) {
                        FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
                        try {
                            String shipClassifcation = null;

                            //added code for Shipping Classification
                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                     "Executing the code for Shipping Classification");
                            OmsCustOrdItem omsCustOrdItem =
                                session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(),
                                                                    new BigDecimal(custOrdItmDesc.getLineItemNo()));
                            shipClassifcation = omsCustOrdItem.getShipClassification();
                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                     "===============================shipClassifcation =========================" +
                                     shipClassifcation);

                            //Getting the value for Shipping Classification from Custom table
                            // String courier=omsUtilCommons1.getShippingClassificationType();
                            /* BigDecimal udaValue=session.getUdaItemLovFindItemAndUdaValueByUdaId(new BigDecimal(parameterValue),custOrdItmDesc.getItemId());
                            log.info("omsCustOrdNo "+omsCustOrdNo +"udaValue="+udaValue);
                            String udaValueDesc=session.getUdaValueDescByUdaIdAndUdaValue(new BigDecimal(parameterValue), udaValue);
                            log.info("omsCustOrdNo "+omsCustOrdNo +"udaValueDesc="+udaValueDesc);*/
                            //code for translation into ISO code - City Name - ALL feature
                            String refValue = null;
                            //If delivery type is Ship to Customer
                            // custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value();
                            if (custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value().equals("S")) {
                                log.info("omsCustOrdNo " + omsCustOrdNo + "Ship to customer");
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "=======================================================");
                                log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItmDesc.getItemId() " +
                                         custOrdItmDesc.getItemId());
                                log.info("omsCustOrdNo " + omsCustOrdNo + "custOrderDesc.getInitiateLocId() " +
                                         custOrderDesc.getInitiateLocId());
                                log.info("omsCustOrdNo " + omsCustOrdNo + "custOrderDesc.getInitiateCountryCode() " +
                                         custOrderDesc.getInitiateCountryCode());
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value() " +
                                         custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value());
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getStateCode() " +
                                         custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getStateCode());

                                refValue =
                                        session.getOmsReferenceDataFindRefValue("STATE_CITY_LINK", custOrderDesc.getInitiateCountryCode(),
                                                                                custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getStateCode());
                                //changed the code for Shipping Classification - instead of BIG (replaced with Uda value)
                                //the refValue is the state name returned from the OmsReferenceData table for ISO code translation
                                log.info("omsCustOrdNo " + omsCustOrdNo + "refValue " + refValue.toUpperCase());


                                combID =
                                        findNextfulfillLoc.processFulfillmentMatrixGetCombID(new BigDecimal(custOrderDesc.getInitiateLocId()),
                                                                                             shipClassifcation,
                                                                                             refValue.toUpperCase(),
                                                                                             custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value());
                                log.info("omsCustOrdNo " + omsCustOrdNo + "Combination Id " + combID);

                            } else {

                                log.info("omsCustOrdNo " + omsCustOrdNo + "Customer pick up");
                                //changed the code for Shipping Classification - instead of BIG (replaced with Uda value)
                                combID =
                                        findNextfulfillLoc.processFulfillmentMatrixGetCombIDWoCity(new BigDecimal(custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId()),
                                                                                                   shipClassifcation,
                                                                                                   custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value());
                            }
                            fulfillMatrixExtDetailResult =
                                    findNextfulfillLoc.processFulfillmentMatrix(combID, priority);
                            nextLoc = fulfillMatrixExtDetailResult.getLocation();
                            deliveryLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
                            BigDecimal deilveryfromLocation = fulfillMatrixExtDetailResult.getDeliveryFromLoc();
                            if (nextLoc.intValue() < 0) {
                                long L_Pending_Qty_temp;
                                long L_Cum_Ord_Qty_temp;
                                log.info("omsCustOrdNo " + omsCustOrdNo + "calling BackOrder  Functionality");
                                log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty" + L_Pending_Qty);

                                //    BackOrderFulfillDetail backOrderFulfillDetail=processBackOrderItem(custOrdItmDesc,combID ,custOrderDesc,new BigDecimal(L_Pending_Qty),omsCustOrdNo);
                                BackOrderFulfillDetail backOrderFulfillDetail =
                                    processBackOrderItem(custOrdItmDesc, combID, custOrderDesc,
                                                         custOrdItmDesc.getQuantity().subtract(new BigDecimal(L_Cum_Ord_Qty)),
                                                         omsCustOrdNo, omsOrposCustOrderId);
                                SOH = backOrderFulfillDetail.getSOH();
                                log.info("omsCustOrdNo " + omsCustOrdNo + "SOH from BackOrder " + SOH);
                                L_Pending_Qty_temp = custOrdItmDesc.getQuantity().longValue() - L_Cum_Ord_Qty;
                                L_Cum_Ord_Qty_temp = L_Cum_Ord_Qty + Math.min(SOH, L_Pending_Qty_temp);
                                log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty_temp=" + L_Pending_Qty_temp +
                                         "L_Cum_Ord_Qty_temp=" + L_Cum_Ord_Qty_temp + "SOH=" + SOH);

                                if (custOrdItmDesc.getQuantity().subtract(new BigDecimal(L_Cum_Ord_Qty)).intValue() >
                                    SOH)
                                // if(L_Cum_Ord_Qty_temp < custOrdItmDesc.getQuantity().longValue())
                                {
                                    isPartialPO = true;
                                    partialPoQty =
                                            (custOrdItmDesc.getQuantity().subtract(new BigDecimal(L_Cum_Ord_Qty)).subtract(new BigDecimal(SOH)).intValue());
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "isPartialPO=true and partialPoQty=" +
                                             partialPoQty);

                                    log.info("omsCustOrdNo " + omsCustOrdNo + "Its a PO,Find the supplier");
                                    log.info("custOrdItmDesc.getItemId() : " + custOrdItmDesc.getItemId());
                                    log.info("fulfillMatrixExtDetailResult.getDeliveryFromLoc() : " +
                                             fulfillMatrixExtDetailResult.getDeliveryFromLoc());
                                    String item_status =
                                        checkItemLocSOH.findItemStatus(custOrdItmDesc.getItemId(), fulfillMatrixExtDetailResult.getDeliveryFromLoc());
                                    log.info("item_status : " + item_status);
                                    if (item_status.equals("A") == false) {
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 " inside item_status false condition");
                                        log.info("omsCustOrdNo " + omsCustOrdNo + custOrderDesc.getOrderDesc());

                                        custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_108"); //Status of item is not approved,cannot fulfill the order
                                        OmsOrposCustOrderHead omsOrposCustOrderHead =
                                            session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
                                        omsOrposCustOrderHead.setStatus("F");
                                        session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
                                        OmsCustOrdHead omsCustOrdHead =
                                            session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                        omsCustOrdHead.setStatus("F");
                                        session.mergeOmsCustOrdHead(omsCustOrdHead);
                                        status = true;
                                        break;

                                    }
                                    //nextLoc = new BigDecimal(checkItemLocSOH.findSupplier(custOrdItmDesc.getItemId(), "Y"));
                                    /** Eariler we checking whether the primary supplier have direct_ship_ind='Y' or not.
                                     * If Primary supplier of an item having  direct_ship_ind='N' then we are setting "OMS_ORPOS_ERROR_109"
                                     *
                                     * Now ,If the delivery location type is physical store -->'S' then we are using delivery from location.
                                     *  item and delievry location are passed to getPrimarySupplierFromItemLocation method to get primary supplier for the item at that particular
                                     *  location. After getting primary supplier we are passing the item and the primary supplie(supplier) to
                                     *  checkDirectShipIndicatoryofaGivenSupplier method it returns false. we are setting "OMS_ORPOS_ERROR_109"
                                     */
                                    log.info(" Delivery Location type is" + deliveryLocType);
                                    if ("S".equals(deliveryLocType)) {
                                        log.info(" calling getPrimarySupplierFromItemLocation method by passing " +
                                                 custOrdItmDesc.getItemId() + "..... location..." +
                                                 deilveryfromLocation.longValue());
                                        nextLoc =
                                                checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItmDesc.getItemId(),
                                                                                                   deilveryfromLocation.longValue());
                                    } else {
                                        log.info(" calling getPrimarySupplierFromItemLocation method by passing " +
                                                 custOrdItmDesc.getItemId() + "..... location..." +
                                                 custOrderDesc.getInitiateLocId());
                                        nextLoc =
                                                checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItmDesc.getItemId(),
                                                                                                   custOrderDesc.getInitiateLocId());
                                    }
                                    Boolean flag =
                                        checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(custOrdItmDesc.getItemId(),
                                                                                                  nextLoc.longValue());
                                    log.info("Value of the flag is " + flag);
                                    sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
                                    log.info("omsCustOrdNo " + omsCustOrdNo +
                                             "supplier value from findSupplier method " + nextLoc);
                                    if (flag == Boolean.FALSE) {
                                        custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_109"); //supplier doesnot exist
                                        OmsOrposCustOrderHead omsOrposCustOrderHead =
                                            session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
                                        omsOrposCustOrderHead.setStatus("F");
                                        session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
                                        OmsCustOrdHead omsCustOrdHead =
                                            session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                        omsCustOrdHead.setStatus("F");
                                        session.mergeOmsCustOrdHead(omsCustOrdHead);
                                        status = true;
                                        break;
                                    }
                                    fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc();
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId " + fulfillLocId);
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "nextLoc " + nextLoc);
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocId " + sourceLocId);
                                    sourceLocId = nextLoc;
                                    fulFillLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
                                    sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "next log for fing org unit=" + nextLoc);
                                    if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(nextLoc)) !=
                                        0) {
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
                                        custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_104"); //ORG_UNIT_UNMATCHED
                                        OmsOrposCustOrderHead omsOrposCustOrderHead =
                                            session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
                                        omsOrposCustOrderHead.setStatus("F");
                                        session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
                                        OmsCustOrdHead omsCustOrdHead =
                                            session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                        omsCustOrdHead.setStatus("F");
                                        session.mergeOmsCustOrdHead(omsCustOrdHead);
                                        status = true;
                                        break;
                                        //log.info("omsCustOrdNo "+omsCustOrdNo +OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
                                    }
                                    isPO = true;

                                    //  break;  //* commented for new PO code
                                } else {
                                    if (L_Cum_Ord_Qty > 0) {
                                        isPartialPO = true;
                                    }
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "isBackOrder else block");
                                    isBackOrder = true;
                                    fulfillLocId = backOrderFulfillDetail.getFulfillLoc();
                                    sourceLocId = backOrderFulfillDetail.getSourceLoc();
                                    fulFillLocType = backOrderFulfillDetail.getFulfillLocType();
                                    sourceLocType = backOrderFulfillDetail.getSourceLocType();
                                }

                            }
                            if (nextLoc.toString().equals("-1") == false) {
                                sourceLocId = nextLoc;
                                log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocId :" + nextLoc);
                                sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
                                log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocType " + sourceLocType);

                                fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc(); //added
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "fulfillMatrixExtDetailResult.getLocation()=" +
                                         fulfillMatrixExtDetailResult.getLocation());
                            }
                            if (fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {
                                log.info("omsCustOrdNo " + omsCustOrdNo + "inside WH ");
                                //Code change for Virtual Warehouse
                                //   If Source_loc_type =�WH� then query WH table in RMS
                                //   select physical_wh,channel_id from wh where wh='101';
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "Started the code for Virtual Warehouse scenario");
                                List<Object[]> tempWhObject =
                                    session.getWhFindPhysicalWH(fulfillMatrixExtDetailResult.getLocation());
                                BigDecimal physicalWH = BigDecimal.ZERO;
                                BigDecimal channelId = BigDecimal.ZERO;
                                for (Object[] result : tempWhObject) {
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "inside loop");
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0]);
                                    physicalWH = new BigDecimal(result[0].toString());
                                    channelId = new BigDecimal(result[1].toString());
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "channel id=" + result[1]);
                                }
                                sourceLocId = physicalWH;
                                virtualWH = fulfillMatrixExtDetailResult.getLocation();
                                fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc();

                                log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId for WH" + fulfillLocId);
                                fulFillLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
                                log.info("omsCustOrdNo " + omsCustOrdNo + "fulFillLocType " + fulFillLocType);
                                log.info("omsCustOrdNo " + omsCustOrdNo + " physicalWH " + physicalWH);
                                log.info("omsCustOrdNo " + omsCustOrdNo + " channelId " + channelId);
                                List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);
                                int i = 0;
                                while (i < locList.size()) {
                                    log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
                                    i++;
                                }
                                OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
                                String applicationId = "ORPOS";
                                SOH = omsUtilCommons.checkSOHForWH(custOrdItmDesc.getItemId(), locList, applicationId).longValue();
                                log.info("omsCustOrdNo " + omsCustOrdNo + " Item " + custOrdItmDesc.getItemId() +
                                         "locList" + locList.toString() + "SOH " + SOH);

                            }
                        } catch (Exception e) {
                            log.info("omsCustOrdNo " + omsCustOrdNo + " omsOrposCustOrderId " + omsOrposCustOrderId);
                            BackOrderFulfillDetail backOrderFulfillDetail = new BackOrderFulfillDetail();
                            log.info("----------------> The BackOrderFulfillDetail Address ------------------->" +
                                     backOrderFulfillDetail + "<-------------->");
                            int qty = backOrderFulfillDetail.getSOH();
                            qty =
processBackOrderItem(custOrdItmDesc, combID, custOrderDesc, custOrdItmDesc.getQuantity().subtract(new BigDecimal(L_Cum_Ord_Qty)),
                     omsCustOrdNo, omsOrposCustOrderId).getSOH();
                            log.info("omsCustOrdNo " + omsCustOrdNo + "qty " + qty);
                            bo = true;
                            if (qty <
                                custOrdItmDesc.getQuantity().subtract(new BigDecimal(L_Cum_Ord_Qty)).intValue()) {
                                log.error("omsCustOrdNo " + omsCustOrdNo + "failed in source loc in finding comb_id" +
                                          e);
                                log.info("omsCustOrdNo " + omsCustOrdNo + custOrderDesc.getOrderDesc());
                                log.info("omsCustOrdNo " + omsCustOrdNo + "combID " + combID);
                                if (combID.intValue() == 0) {
                                    custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_102"); //failed in comb_id
                                } else {
                                    custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_103"); //inventory unavailble
                                }
                                log.info("omsCustOrdNo " + omsCustOrdNo + custOrderDesc.getOrderDesc());
                                OmsOrposCustOrderHead omsOrposCustOrderHead =
                                    session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "retrived status value from omsOrposCustOrderHead " +
                                         omsOrposCustOrderHead.getStatus());
                                omsOrposCustOrderHead.setStatus("F");
                                session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
                                OmsCustOrdHead omsCustOrdHead =
                                    session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                omsCustOrdHead.setStatus("F");
                                session.mergeOmsCustOrdHead(omsCustOrdHead);
                                OmsCustOrdItem omsCustOrdItem =
                                    session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(),
                                                                        new BigDecimal(custOrdItmDesc.getLineItemNo()));
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "==========================Cancelled Quantity in OMS table=============================== " +
                                         omsCustOrdItem.getQtyOrderedSuom());
                                omsCustOrdItem.setQtyCancelled(omsCustOrdItem.getQtyOrderedSuom());
                                omsCustOrdItem.setStatus("F");
                                session.mergeOmsCustOrdItem(omsCustOrdItem);
                                OmsOrposCustOrdItm omsOrposCustOrdItm =
                                    session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(omsOrposCustOrderId,
                                                                                                custOrdItmDesc.getItemId(),
                                                                                                new BigDecimal(custOrdItmDesc.getLineItemNo()));
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "==========================Cancelled Quantity in ORPOS table=============================== " +
                                         omsCustOrdItem.getQtyOrderedSuom());
                                omsOrposCustOrdItm.setCancelledQuantity(omsCustOrdItem.getQtyOrderedSuom());
                                session.mergeOmsOrposCustOrdItm(omsOrposCustOrdItm);
                                log.info("omsCustOrdNo " + omsCustOrdNo + "----");
                                //break;
                                status = true;
                            }
                            //move to next item

                            //log.info("inside before closing catch block break");
                            break;

                        }
                        priority++;
                        //Step 3 :Check  quantities for particular location in DAS schema
                        log.info("omsCustOrdNo " + omsCustOrdNo + "Checking SOH for item=" +
                                 custOrdItmDesc.getItemId() + "and loc is " + nextLoc);

                        log.info("omsCustOrdNo " + omsCustOrdNo + "******* sourceLocType=" + sourceLocType +
                                 " isBackOrder=" + isBackOrder);
                        if (sourceLocType.equals("ST") && isBackOrder == false) {
                            nextLoc = fulfillMatrixExtDetailResult.getLocation();
                            String itemandLoc = custOrdItmDesc.getItemId() + "," + nextLoc.toString();
                            InterfacePersistence interfacePersistece = new InterfacePersistence();
                            try {
                                log.info("============================================================================ line " +
                                         custOrdItmDesc.getLineItemNo());
                                //added code for duplicate items - fulfillment logic
                                log.info("itemQtyMap.keySet() " + itemQtyMap.keySet());
                                log.info("storeItemMap.keySet()" + storeItemMap.keySet());
                                log.info("itemand Loc" + itemandLoc);
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "Started the code for Duplicate Items - fulfillment logic");
                                log.info("omsCustOrdNo" + omsCustOrdNo + "Procesing for LineNo============= " +
                                         custOrdItmDesc.getLineItemNo());
                                log.info("omsCustOrdNo" + omsCustOrdNo + "Procesing for Item " +
                                         custOrdItmDesc.getItemId());
                                log.info("omsCustOrdNo" + omsCustOrdNo + "Ordered Qty " +
                                         custOrdItmDesc.getQuantity());
                                log.info("omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
                                log.info("omsCustOrdNo" + omsCustOrdNo + "pending qty  " + L_Pending_Qty);
                                int requestQty = 0;
                                //check for needed quantity to fulfill
                                if (L_Pending_Qty == 0 && L_Cum_Ord_Qty == 0) {
                                    requestQty = custOrdItmDesc.getQuantity().intValue();
                                    log.info("requestQty when pending and l_cum_ord_qty " + requestQty);
                                } else if (custOrdItmDesc.getQuantity().intValue() >= L_Cum_Ord_Qty) {
                                    requestQty = custOrdItmDesc.getQuantity().intValue() - (int)L_Cum_Ord_Qty;
                                    log.info("requestQty when custOrdItmDesc.getQuantity().intValue() " + requestQty);
                                } else if (L_Cum_Ord_Qty >= custOrdItmDesc.getQuantity().intValue()) {
                                    requestQty = (int)L_Cum_Ord_Qty - custOrdItmDesc.getQuantity().intValue();
                                    log.info("requestQty when L_Cum_Ord_Qty>=custOrdItmDesc.getQuantity() " +
                                             requestQty);
                                }
                                log.info("============================================================================ line " +
                                         custOrdItmDesc.getLineItemNo());
                                if (itemQtyMap.containsKey(custOrdItmDesc.getItemId())) {
                                    log.info("omsCustOrdNo " + omsCustOrdNo +
                                             "inside containsKey condition for itemQtyMap for line No " +
                                             custOrdItmDesc.getLineItemNo());
                                    if (storeItemMap.containsKey(itemandLoc))
                                    //if(itemQtyMap.get(custOrdItmDesc.getItemId()).getLocation().compareTo(nextLoc)==0)
                                    {
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 "inside compareTo condition for item id");
                                        // SOH=itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh().longValue();
                                        SOH = storeItemMap.get(itemandLoc).longValue();
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 "itemQtyMap already contain item with SOH=" + SOH);
                                        ItemSOH itemSOH = itemQtyMap.get(custOrdItmDesc.getItemId());
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 "=======inside if (storeItemMap.containsKey(itemandLoc))======");
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " +
                                                 custOrdItmDesc.getLineItemNo());
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " +
                                                 custOrdItmDesc.getItemId());
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
                                        log.info("custOrdItems.getOrderQtySuom() " + custOrdItmDesc.getItemId());
                                        if (SOH >= requestQty) {
                                            log.info("omsCustOrdNo " + omsCustOrdNo + "SOH >0 and locatin is " +
                                                     nextLoc);
                                            itemSOH.setLocation(nextLoc);
                                            itemSOH.setSoh(new BigDecimal(SOH - requestQty));
                                            log.info("getting SOH inside inside if (storeItemMap.containsKey(itemandLoc)) " +
                                                     itemSOH.getSoh());
                                        } //end of inner if
                                        else {
                                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                                     "SOH is less than quantity requested,finding in store" + nextLoc);
                                            itemSOH.setSoh(BigDecimal.ZERO);
                                            log.info("omsCustOrdNo" + omsCustOrdNo + "SOH inside else" +
                                                     itemSOH.getSoh());
                                            itemQtyMap.put(custOrdItmDesc.getItemId(), itemSOH);
                                            if (storeItemMap.get(itemandLoc) != null) {
                                                storeSOH = storeItemMap.get(itemandLoc);
                                                log.info("storeSOH" +
                                                         itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh());
                                                storeItemMap.put(itemandLoc,
                                                                 itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh());
                                            }
                                        }
                                        itemQtyMap.put(custOrdItmDesc.getItemId(), itemSOH);
                                        log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
                                        log.info("itemSOH.getSoh() " + itemSOH.getSoh());
                                        long a = itemSOH.getSoh().longValue() - L_Cum_Ord_Qty;
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "updated the soh to " +
                                                 itemSOH.getSoh());
                                        log.info("a " + a);
                                        if (storeItemMap.get(itemandLoc) != null) {
                                            storeSOH = storeItemMap.get(itemandLoc);
                                            log.info("storeSOH" + itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh());
                                            storeItemMap.put(itemandLoc,
                                                             itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh());
                                        }

                                    } //end of 2nd if
                                    else {
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 "Item exist in map ,but find the SOH with next loc");
                                        OmsCustOrdHead omsCustOrdHead =
                                            session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                        SOH =
interfacePersistece.callSIMStoreInventory(custOrdItmDesc.getItemId(), nextLoc, omsCustOrdHead.getApplicationId());
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "Item " +
                                                 custOrdItmDesc.getItemId() + "store " + nextLoc + " SOH" + SOH);
                                        ItemSOH itemSOH = itemQtyMap.get(custOrdItmDesc.getItemId());
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 "=======inside if (storeItemMap.containsKey(itemandLoc))======");
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " +
                                                 custOrdItmDesc.getLineItemNo());
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " +
                                                 custOrdItmDesc.getItemId());
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
                                        log.info("requestQty " + requestQty);
                                        if (SOH >= requestQty) {
                                            log.info("settting when SOH>=custOrdItems.getOrderQtySuom().longValue() ");
                                            itemSOH.setSoh(new BigDecimal(SOH - requestQty));
                                        } else {
                                            log.info("SOH is set to  0");
                                            itemSOH.setSoh(BigDecimal.ZERO);
                                        }
                                        log.info("setted itemSOH " + itemSOH.getSoh());
                                        itemSOH.setLocation(nextLoc);
                                        itemQtyMap.put(custOrdItmDesc.getItemId(), itemSOH);
                                        log.info("putting the SOH in map " +
                                                 itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh());
                                        storeItemMap.put(itemandLoc,
                                                         itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh());
                                        log.info("=====================================================================");

                                    }
                                    //   SOH = interfacePersistece.callSIMStoreInventory(custOrdItmDesc.getItemId(), nextLoc);
                                } //end of major 1st if
                                else {
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "Item does not contain in itemQtyMap");
                                    OmsCustOrdHead omsCustOrdHead =
                                        session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                    SOH =
interfacePersistece.callSIMStoreInventory(custOrdItmDesc.getItemId(), nextLoc, omsCustOrdHead.getApplicationId());
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + custOrdItmDesc.getItemId() +
                                             "nextloc " + nextLoc + " SOH" + SOH);
                                    ItemSOH itemSOH = new ItemSOH();
                                    itemSOH.setLocation(nextLoc);
                                    log.info("custOrdItems.getOrderQtySuom() " + custOrdItmDesc.getQuantity());
                                    log.info("requestQty " + requestQty);
                                    log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
                                    long L_Pending_Qty_temp = requestQty - L_Cum_Ord_Qty;
                                    log.info("L_Pending_Qty_temp " + L_Pending_Qty_temp);
                                    L_Pending_Qty = L_Pending_Qty_temp;
                                    log.info("SOH" + SOH);
                                    log.info("custOrdItmDesc.getQuantity() " + custOrdItmDesc.getQuantity());
                                    if (SOH >= requestQty) {
                                        log.info("inside SOH>=custOrdItmDesc.getQuantity().intValue() " +
                                                 new BigDecimal(SOH - requestQty));
                                        itemSOH.setSoh(new BigDecimal(SOH - requestQty));
                                    } else {
                                        log.info("inside else for SOH>=requestQty condition");
                                        log.info("itemSoh.getSoh()" + itemSOH.getSoh());
                                        itemSOH.setSoh(BigDecimal.ZERO);
                                        log.info("itemSoh.getSoh()" + itemSOH.getSoh());
                                    }
                                    log.info("putting into itemQtyMap map Location" + itemSOH.getLocation());
                                    log.info("putting into itemQtyMap map SOH " + itemSOH.getSoh());
                                    itemQtyMap.put(custOrdItmDesc.getItemId(), itemSOH);
                                    log.info("putting into storeItem map " + itemSOH.getSoh());
                                    storeSOH = itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh();
                                    log.info("storeSOH " + storeSOH);
                                    storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh());
                                    log.info("after putting into the storeMap " + storeItemMap.keySet());
                                    log.info("omsCustOrdNo " + omsCustOrdNo +
                                             "itemQty map does not contain values with SOH=" + itemSOH.getSoh());
                                }
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "Completed the try block for Duplicate items -Fulfillment Logic");
                            } //end of try
                            catch (javax.xml.ws.WebServiceException f) {
                                custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_111"); //RMS/SIM WS is down
                                log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in calling SIM exception is  " + f);
                                OmsCustOrdHead omsCustOrdHead =
                                    session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                omsCustOrdHead.setStatus("F");
                                session.mergeOmsCustOrdHead(omsCustOrdHead);
                                OmsOrposCustOrderHead omsOrposCustOrderHead =
                                    session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
                                omsOrposCustOrderHead.setStatus("F");
                                OmsCustOrdItem omsCustOrdItem =
                                    session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(),
                                                                        new BigDecimal(custOrdItmDesc.getLineItemNo()));
                                omsCustOrdItem.setStatus("F");
                                session.mergeOmsCustOrdItem(omsCustOrdItem);
                                status = true;
                                break;
                                //throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SIM_UNAVL"));
                            } catch (Exception e) {
                                log.info("store level catch block");
                                OmsCustOrdItem omsCustOrdItem =
                                    session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(),
                                                                        new BigDecimal(custOrdItmDesc.getLineItemNo()));
                                omsCustOrdItem.setQtyCancelled(omsCustOrdItem.getQtyOrderedSuom());
                                omsCustOrdItem.setStatus("F");
                                session.mergeOmsCustOrdItem(omsCustOrdItem);
                                OmsOrposCustOrdItm omsOrposCustOrdItm =
                                    session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(omsOrposCustOrderId,
                                                                                                custOrdItmDesc.getItemId(),
                                                                                                new BigDecimal(custOrdItmDesc.getLineItemNo()));
                                omsOrposCustOrdItm.setCancelledQuantity(omsCustOrdItem.getQtyOrderedSuom());
                                session.mergeOmsOrposCustOrdItm(omsOrposCustOrdItm);
                                custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_103"); //UNAVL_INVENTORY
                                OmsCustOrdHead omsCustOrdHead =
                                    session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                omsCustOrdHead.setStatus("F");
                                session.mergeOmsCustOrdHead(omsCustOrdHead);
                                OmsOrposCustOrderHead omsOrposCustOrderHead =
                                    session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
                                omsOrposCustOrderHead.setStatus("F");
                                status = true;
                                break;
                                //       throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
                            }
                        }
                        //added code for duplicate items
                        /* log.info("omsCustOrdNo "+omsCustOrdNo +"Duplicate items code 2 started");
                    if(itemCounterMap.containsKey(custOrdItmDesc.getItemId()))
                    {
                        log.info("omsCustOrdNo "+omsCustOrdNo +"Item already exist in itemCounterMap.");
                        itemCounterMap.put(custOrdItmDesc.getItemId(), itemCounterMap.get(custOrdItmDesc.getItemId()).add(BigDecimal.ONE));
                    }
                    else
                    {
                        log.info("omsCustOrdNo "+omsCustOrdNo +"Item does not exist in itemCounterMap.");
                        itemCounterMap.put(custOrdItmDesc.getItemId(), BigDecimal.ONE);
                    }
                         */ //commented for new PO code

                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                 "________________________________________________________________________________________________");
                        if ((isBackOrder == false && (SOH > 0 || isPO == true)) || isPartialPO == true) {
                            log.info("omsCustOrdNo " + omsCustOrdNo + "Setting into item counter map");
                            if (itemCounterMap.containsKey(custOrdItmDesc.getItemId())) {
                                log.info("omsCustOrdNo " + omsCustOrdNo + "Item already exist in itemCounterMap.");
                                //  itemCounterMap.put(custOrdItems.getItem(), itemCounterMap.get(custOrdItems.getItem()).add(BigDecimal.ONE));///---uncommmented 1dec as duplicate item not working
                                //  itemCounterMap.put(custOrdItems.getItem(), itemCounterMap.get(custOrdItems.getItem()).add(BigDecimal.ONE));
                                FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO =
                                    new FulfillOrdandSourceLocPOJO();
                                fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
                                //fulfillOrdandSourceLocPOJO.setFulfillOrderNo(itemCounterMap.get(custOrdItmDesc.getItemId()).getFulfillOrderNo().add(BigDecimal.ONE));
                                //fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo).add(BigDecimal.ONE));
                                //itemCounterMap.put(custOrdItmDesc.getItemId(),fulfillOrdandSourceLocPOJO);
                                maxFulfillOrderNo =
                                        itemCounterMap.get(custOrdItmDesc.getItemId()).getFulfillOrderNo().add(BigDecimal.ONE).intValue();
                                //maxFulfillOrderNo=new BigDecimal(maxFulfillOrderNo).add(BigDecimal.ONE).intValue();
                                log.info("omsCustOrdNo " + omsCustOrdNo + "maxFulfillOrderNo " + maxFulfillOrderNo);
                                boolean mapFound = false;

                                for (BigDecimal key1 : sohMap.keySet()) {
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "key is " + key1);
                                    ArrayList<OmsTempCoFo> list = sohMap.get(key1);
                                    if (mapFound == true) {
                                        break;
                                    }
                                    log.info("list size=" + list.size());
                                    for (OmsTempCoFo temp : list) {
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "looping over map");
                                        if (temp.getSourceLocId().compareTo(sourceLocId) == 0 &&
                                            temp.getItem().equals(custOrdItmDesc.getItemId()) == false) {
                                            fulfillOrdandSourceLocPOJO.setFulfillOrderNo(key1);
                                            log.info("key1 " + key1);
                                            log.info("omsCustOrdNo " + omsCustOrdNo + "mapFound=true;");
                                            mapFound = true;
                                            break;
                                        }
                                    }

                                }
                                boolean fulOrdNoFound = false;
                                while (fulOrdNoFound == false) {
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "fulOrdNoFound" + fulOrdNoFound +
                                             "maxFulfillOrderNo=" + maxFulfillOrderNo + "key set" + sohMap.keySet());
                                    log.info("sohMap.get(maxFulfillOrderNo)" +
                                             sohMap.containsKey(new BigDecimal(maxFulfillOrderNo)));
                                    log.info("maxFulfillOrderNo " + maxFulfillOrderNo);
                                    if (sohMap.containsKey(new BigDecimal(maxFulfillOrderNo))) {
                                        log.info("Size is" + sohMap.get(new BigDecimal(maxFulfillOrderNo)).size());
                                        log.info("sohMap.get(maxFulfillOrderNo)" +
                                                 sohMap.get(new BigDecimal(maxFulfillOrderNo)).get(0).getSourceLocId() +
                                                 " current loc is " + sourceLocId);
                                        if (sohMap.get(new BigDecimal(maxFulfillOrderNo)).get(0).getSourceLocId().compareTo(sourceLocId) ==
                                            0) {
                                            log.info("sourceLocType=" + sourceLocType);
                                            if (sourceLocType.equals("WH"))
                                                maxFulfillOrderNo++;
                                            fulOrdNoFound = true;
                                        } else {

                                            maxFulfillOrderNo++;
                                            log.info("in else maxFulfillOrderNo=" + maxFulfillOrderNo);
                                        }
                                    } else {
                                        log.info("No record exist in SohMap with ful ord no " + maxFulfillOrderNo);
                                        break;
                                    }
                                }

                                log.info("maxFulfillOrderNo to set in the POJO class " + maxFulfillOrderNo);
                                fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));
                                itemCounterMap.put(custOrdItmDesc.getItemId(), fulfillOrdandSourceLocPOJO);
                            } else {
                                log.info("omsCustOrdNo " + omsCustOrdNo + "Item does not exist in itemCounterMap" +
                                         custOrdItmDesc.getItemId() + "fulfil order no=1");
                                if (itemCounterMap.size() == 0) {
                                    log.info("omsCustOrdNo " + omsCustOrdNo +
                                             "Adding first item with fulfill orde no 1");
                                    FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO =
                                        new FulfillOrdandSourceLocPOJO();
                                    fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
                                    //fulfillOrdandSourceLocPOJO.setFulfillOrderNo(BigDecimal.ONE);
                                    fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));
                                    itemCounterMap.put(custOrdItmDesc.getItemId(), fulfillOrdandSourceLocPOJO);
                                } else {
                                    for (String key : itemCounterMap.keySet()) {
                                        log.info("key value inside for loop");
                                        FulfillOrdandSourceLocPOJO pojo = itemCounterMap.get(key);
                                        //while (it.hasNext())
                                        //{
                                        //FulfillOrdandSourceLocPOJO pojo =it.next().getValue();
                                        if (pojo.getSourceLoc().compareTo(sourceLocId) == 0 &&
                                            sourceLocType.equals("WH") == false) {

                                            FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO =
                                                new FulfillOrdandSourceLocPOJO();
                                            fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
                                            fulfillOrdandSourceLocPOJO.setFulfillOrderNo(pojo.getFulfillOrderNo());
                                            itemCounterMap.put(custOrdItmDesc.getItemId(), fulfillOrdandSourceLocPOJO);
                                            break;
                                        }
                                        //new code
                                        else {
                                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                                     "Source and fulfil loc is not same");
                                            if (sourceLocType.equals("WH") == false && sohMap.size() > 0) {
                                                log.info("omsCustOrdNo " + omsCustOrdNo + "pojo.getFulfillOrderNo()" +
                                                         pojo.getFulfillOrderNo());
                                                if (pojo.getFulfillOrderNo().intValue() >= 1) {
                                                    int i = pojo.getFulfillOrderNo().intValue();
                                                    int initialFulOrdNO = intialNo;
                                                    while (i >= initialFulOrdNO) {
                                                        log.info("omsCustOrdNo " + omsCustOrdNo + "inside while");

                                                        List<OmsTempCoFo> tempSohMapList =
                                                            sohMap.get(new BigDecimal(initialFulOrdNO));
                                                        log.info("omsCustOrdNo " + omsCustOrdNo + "sohMap.keySet() " +
                                                                 sohMap.keySet());
                                                        log.info("omsCustOrdNo " + omsCustOrdNo + "---");
                                                        boolean found = false;
                                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                                 "tempSohMapList size" + tempSohMapList.size());
                                                        for (OmsTempCoFo omsTempCoFo : tempSohMapList) {
                                                            if (custOrdItmDesc.getItemId().equals(omsTempCoFo.getItem())) {
                                                                found = true;
                                                                break;
                                                            }
                                                        }
                                                        log.info("omsCustOrdNo " + omsCustOrdNo + "found+" + found);
                                                        if (found == false) {
                                                            FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO =
                                                                new FulfillOrdandSourceLocPOJO();
                                                            fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
                                                            //   fulfillOrdandSourceLocPOJO.setFulfillOrderNo(pojo.getFulfillOrderNo());
                                                            boolean mapFound = false;
                                                            int highestFulOrdNo = 1;
                                                            //int highestFulOrdNo=0;
                                                            for (BigDecimal key1 : sohMap.keySet()) {
                                                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                                                         "sohMap.keySet()" + sohMap.keySet());

                                                                if (key1.intValue() > highestFulOrdNo) {
                                                                    highestFulOrdNo = key1.intValue();
                                                                }
                                                                ArrayList<OmsTempCoFo> list = sohMap.get(key1);
                                                                if (mapFound == true) {
                                                                    break;
                                                                }
                                                                for (OmsTempCoFo temp : list) {
                                                                    if (temp.getSourceLocId().compareTo(sourceLocId) ==
                                                                        0) {
                                                                        fulfillOrdandSourceLocPOJO.setFulfillOrderNo(key1);
                                                                        mapFound = true;
                                                                        break;
                                                                    }
                                                                }


                                                            }
                                                            if (mapFound == false) {
                                                                fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(highestFulOrdNo +
                                                                                                                            1));
                                                                //fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo+1));
                                                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                                                         "highestFulOrdNo " + maxFulfillOrderNo + 1);
                                                                // highestFulOrdNo=maxFulfillOrderNo;
                                                                maxFulfillOrderNo = highestFulOrdNo + 1;
                                                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                                                         "maxFulfillOrderNo " + maxFulfillOrderNo);
                                                            }
                                                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                                                     "fulfilorder no is " +
                                                                     fulfillOrdandSourceLocPOJO.getFulfillOrderNo());
                                                            itemCounterMap.put(custOrdItmDesc.getItemId(),
                                                                               fulfillOrdandSourceLocPOJO);
                                                            break;
                                                        }
                                                        i--;
                                                    }
                                                }
                                            }

                                            else {
                                                FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO =
                                                    new FulfillOrdandSourceLocPOJO();
                                                fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
                                                log.info("sohMap.keySet() " + sohMap.keySet());
                                                log.info("maxFulfillOrderNo " + maxFulfillOrderNo);
                                                if (sohMap.containsKey(new BigDecimal(maxFulfillOrderNo))) {
                                                    fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo +
                                                                                                                1));
                                                    maxFulfillOrderNo = maxFulfillOrderNo + 1;
                                                    log.info("---------------maxFulfillOrderNo ++++++++++++++" +
                                                             maxFulfillOrderNo);
                                                    itemCounterMap.put(custOrdItmDesc.getItemId(),
                                                                       fulfillOrdandSourceLocPOJO);

                                                } else {
                                                    fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));
                                                    // maxFulfillOrderNo=maxFulfillOrderNo+1;
                                                    log.info("-------in else --------maxFulfillOrderNo ++++++++++++++" +
                                                             maxFulfillOrderNo);
                                                    itemCounterMap.put(custOrdItmDesc.getItemId(),
                                                                       fulfillOrdandSourceLocPOJO);
                                                }
                                                break;

                                            }
                                        }
                                    }
                                    //   itemCounterMap.put(custOrdItems.getItem(), BigDecimal.ONE);

                                }
                            }


                            log.info("omsCustOrdNo " + omsCustOrdNo + "Ended the code for duplicate items");
                            log.info("omsCustOrdNo " + omsCustOrdNo + "SOH" + SOH);
                            L_Pending_Qty = custOrdItmDesc.getQuantity().longValue() - L_Cum_Ord_Qty;
                            log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty" + L_Pending_Qty);
                            log.info("omsCustOrdNo " + omsCustOrdNo + "priority " + priority);
                            log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCoFo values ");
                            OmsTempCoFo omsTempCoFo = new OmsTempCoFo();
                            log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo " + omsCustOrdNo);
                            omsTempCoFo.setOmsCustOrdNo(omsCustOrdNo);
                            omsTempCoFo.setItem(custOrdItmDesc.getItemId());
                            log.info("omsCustOrdNo " + omsCustOrdNo + "item id " + custOrdItmDesc.getItemId());
                            //   omsTempCoFo.setOrderQty(custOrdItems.getOrderQtySuom());
                            omsTempCoFo.setSourceLocId(sourceLocId);
                            log.info("virtualWH " + virtualWH);
                            omsTempCoFo.setVirtualWH(virtualWH);
                            log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocId " + sourceLocId);
                            omsTempCoFo.setSourceLocationType(sourceLocType);
                            log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocType " + sourceLocType);
                            log.info("omsCustOrdNo " + omsCustOrdNo + "combID " + combID);
                            omsTempCoFo.setCombinationId(combID);

                            //added code for fulfillment order no for duplicate items
                            //  omsTempCoFo.setFulfillOrderNo(itemCounterMap.get(custOrdItmDesc.getItemId()));//commented for new PO code
                            if (itemCounterMap.get(custOrdItmDesc.getItemId()) != null && isBackOrder == false) {

                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "itemCounterMap.get(custOrdItmDesc.getItemId())" +
                                         itemCounterMap.get(custOrdItmDesc.getItemId()).getFulfillOrderNo());
                                omsTempCoFo.setFulfillOrderNo(itemCounterMap.get(custOrdItmDesc.getItemId()).getFulfillOrderNo());
                            }

                            log.info("omsCustOrdNo " + omsCustOrdNo + "FulfillOrderNo " +
                                     omsTempCoFo.getFulfillOrderNo());
                            omsTempCoFo.setLineNo(new BigDecimal(custOrdItmDesc.getLineItemNo()));
                            log.info("omsCustOrdNo " + omsCustOrdNo + "lineNo " + custOrdItmDesc.getLineItemNo());
                            //   omsTempCoFo.setFulfillLocId(fulfillLocId);
                            fulFillLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                     "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
                                     fulFillLocType);
                            omsTempCoFo.setFulfillLocationType(fulFillLocType);
                            log.info("omsCustOrdNo " + omsCustOrdNo + "fulFillLocType " + fulFillLocType);
                            omsTempCoFo.setFoConfQty(BigDecimal.ZERO);

                            omsTempCoFo.setRmsResponseCode("");
                            omsTempCoFo.setRmsErrorMsg("");
                            omsTempCoFo.setCreateDatetime(new Timestamp(new java.util.Date().getTime()));
                            omsTempCoFo.setStatus("N");
                            if (isBackOrder == false && (SOH > 0 || isPO == true)) {
                                log.info("omsCustOrdNo " + omsCustOrdNo + "inside SOH > 0 and isPO=" + isPO);
                                //Customer pick up
                                if (custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value().equals("C")) {
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "OrderQty " +
                                             Math.min(SOH, L_Pending_Qty));
                                    omsTempCoFo.setOrderQty(new BigDecimal(Math.min(SOH,
                                                                                    L_Pending_Qty))); //commented for new PO code
                                    if (isPO == true)
                                        omsTempCoFo.setOrderQty(new BigDecimal(L_Pending_Qty));
                                    if (isPartialPO == true)
                                        omsTempCoFo.setOrderQty(new BigDecimal(partialPoQty));
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "FulfillLocId " +
                                             custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId());
                                    omsTempCoFo.setFulfillLocId(new BigDecimal(custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId()));
                                    if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null ||
                                        sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 "in map ******* creating new key with ful ord no=" +
                                                 omsTempCoFo.getFulfillOrderNo());
                                        ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
                                        tempList.add(omsTempCoFo);
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "templist " + tempList.toString());
                                        sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
                                    } else {
                                        ArrayList<OmsTempCoFo> existingList =
                                            sohMap.get(omsTempCoFo.getFulfillOrderNo());
                                        existingList.add(omsTempCoFo);
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "existingList " +
                                                 existingList.toString());
                                        sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
                                    }

                                }
                                //Ship to customer
                                else { //2
                                    //WH
                                    if (fulfillMatrixExtDetailResult.getDeliveryFromLocType().equals("WH") ||
                                        fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "fulfil loc id as virtual loc is=" +
                                                 fulfillLocId);
                                        omsTempCoFo.setOrderQty(new BigDecimal(Math.min(SOH, L_Pending_Qty)));
                                        if (isPO == true)
                                            omsTempCoFo.setOrderQty(new BigDecimal(L_Pending_Qty));
                                        if (isPartialPO == true)
                                            omsTempCoFo.setOrderQty(new BigDecimal(partialPoQty));
                                        omsTempCoFo.setFulfillLocId(fulfillLocId);

                                        if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null ||
                                            sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
                                            log.info("omsCustOrdNo " + omsCustOrdNo + "in map *******");
                                            ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
                                            tempList.add(omsTempCoFo);
                                            sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
                                        } else {
                                            log.info("WHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                                            maxFulfillOrderNo = treemap.lastKey().intValue();
                                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                                     " Before increamenting the maxFulfillOrderNo in WH else block " +
                                                     maxFulfillOrderNo);
                                            maxFulfillOrderNo = maxFulfillOrderNo + 1;
                                            log.info("maxFulfillOrderNo WHHHHHHHHHHHHHHHHHHHHHHHHHHHH" +
                                                     maxFulfillOrderNo);
                                            omsTempCoFo.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));
                                            ArrayList<OmsTempCoFo> existingList = new ArrayList<OmsTempCoFo>();
                                            existingList.add(omsTempCoFo);
                                            sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
                                        }
                                    } else { //1
                                        //Store
                                        omsTempCoFo.setOrderQty(new BigDecimal(Math.min(SOH, L_Pending_Qty)));
                                        //  omsTempCoFo.setFulfillLocId(fulfillLocId);
                                        if (isPO == true)
                                            omsTempCoFo.setOrderQty(new BigDecimal(L_Pending_Qty));
                                        if (isPartialPO == true)
                                            omsTempCoFo.setOrderQty(new BigDecimal(partialPoQty));
                                        omsTempCoFo.setFulfillLocId(fulfillLocId);
                                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                                 "========================fulFillLocType=============================" +
                                                 fulFillLocType);
                                        log.info("sohMap.keySet() " + sohMap.keySet());
                                        int oldFulFilOrdNo = omsTempCoFo.getFulfillOrderNo().intValue();
                                        log.info("oldFulFilOrdNo " + oldFulFilOrdNo);
                                        log.info("omsTempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
                                        log.info("==============MaxfulfilOrderNo ==============" + maxFulfillOrderNo);
                                        omsTempCoFo = STfulFilOrderNo(omsTempCoFo, sohMap);
                                        int newFulFilOrdNo = omsTempCoFo.getFulfillOrderNo().intValue();
                                        log.info("newFulFilOrdNo " + newFulFilOrdNo);
                                        if (oldFulFilOrdNo != newFulFilOrdNo) {
                                            maxFulfillOrderNo = maxFulfillOrderNo - 1;
                                        }
                                        log.info("After checking the new and old fulfilOrdNo " + maxFulfillOrderNo);
                                        if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null ||
                                            sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
                                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                                     "in map for ship to customer*******");
                                            ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
                                            tempList.add(omsTempCoFo);
                                            log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCoFo.getFulfillOrderNo" +
                                                     omsTempCoFo.getFulfillOrderNo());
                                            log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCoFo.getItem()" +
                                                     omsTempCoFo.getItem());
                                            log.info("omsCustOrdNo " + omsCustOrdNo + "tempList.size()" +
                                                     tempList.size());
                                            sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
                                        } else {
                                            ArrayList<OmsTempCoFo> existingList =
                                                sohMap.get(omsTempCoFo.getFulfillOrderNo());
                                            existingList.add(omsTempCoFo);
                                            sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
                                        }
                                    } //else1 end
                                } //else2
                                log.info("omsCustOrdNo " + omsCustOrdNo + "persistOmsTempCoFo success");
                                log.info("omsCustOrdNo " + omsCustOrdNo + "min : SOH " + SOH + "L_Pending_Qty" +
                                         L_Pending_Qty);
                                L_Cum_Ord_Qty = L_Cum_Ord_Qty + Math.min(SOH, L_Pending_Qty);
                                log.info("omsCustOrdNo " + omsCustOrdNo + "L_Cum_Ord_Qty : " + L_Cum_Ord_Qty);
                            } //end of (isBackOrder ==false && (SOH > 0 || isPO == true))

                        } //end of (isBackOrder ==false && (SOH>0 || isPO==true) ) || isPartialPO==true)
                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                 "Before ending  while (L_Cum_Ord_Qty < custOrdItmDesc.getQuantity().longValue() groupedItemMap.size()" +
                                 groupedItemMap.size());
                    } //end of while (L_Cum_Ord_Qty < custOrdItmDesc.getQuantity().longValue() && isPO==false && isBackOrder==false && bo==false)

                    log.info("omsCustOrdNo " + omsCustOrdNo +
                             "Before ending  shipingChargeDept.equals(itemDept.toString()) groupedItemMap.size()" +
                             groupedItemMap.size());

                } // end of shipingChargeDept.equals(itemDept.toString())==false && inventoryIndn.equals("Y")
                log.info("omsCustOrdNo " + omsCustOrdNo + "sohMap size " + sohMap.size());
                if (sohMap.size() == 0) {
                    log.info("omsCustOrdNo " + omsCustOrdNo + " inside sohMap.size()==0 if condition");
                    /* if(custOrderDesc.getOrderDesc().equals("Not Available"))
                {
                    log.info("inside custOrderDesc.getOrderDesc().equals condition");
                    custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_103");
                    log.info("setted OMS_ORPOS_ERROR_103");
                    log.info("inside custOrderDesc.getOrderDesc().equals if condition groupedItemMap.size()"+groupedItemMap.size());
                } */
                } else {
                    log.info("omsCustOrdNo " + omsCustOrdNo + "Before calling createFulfillDetailMap " +
                             sohMap.keySet());
                    fulfillDetailMap = createFulfillDetailMap(omsCustOrdNo, sohMap, 1);
                    log.info("omsCustOrdNo " + omsCustOrdNo + "After creating map");
                }

                log.info("omsCustOrdNo " + omsCustOrdNo +
                         "BEFORE CLOSING  if condition for custOrdItmDesc.getShippingChargeFlag().value() groupedItemMap.size() " +
                         groupedItemMap.size());
            } //end of custOrdItmDesc.getShippingChargeFlag().value().equals("Y")==false if
            log.info("omsCustOrdNo " + omsCustOrdNo +
                     "After CLOSING of  custOrdItmDesc.getShippingChargeFlag().value() groupedItemMap.size() " +
                     groupedItemMap.size());
            treemap.putAll(sohMap);
            log.info("omsCustOrdNo " + omsCustOrdNo + " Added into treeMap " + treemap.keySet());
            // log.info("omsCustOrdNo " + omsCustOrdNo +"maxfulfilordNo "+treemap.lastKey());
            log.info("omsCustOrdNo " + omsCustOrdNo + "status " + status);
            if (status == true) {
                sohMap.clear();
                fulfillDetailMap.clear();
                log.info("size of sohMap " + sohMap.size());
                log.info("size of fulfilDetailMap " + fulfillDetailMap.size());
                break;
            }
        } //end of for item loop
        log.info("omsCustOrdNo " + omsCustOrdNo + "reached end of for loop processing next");
        return fulfillDetailMap;
    }

    public BigDecimal IdentifyVirutalLoc(String locationType, BigDecimal locID) throws SOAPException {
        BigDecimal virtualId = BigDecimal.ZERO;
        //log.info("omsCustOrdNo "+omsCustOrdNo +"***Start-IdentifyVirutalLoc***");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        //  log.info("omsCustOrdNo "+omsCustOrdNo +"locID :" + locID);
        // log.info("omsCustOrdNo "+omsCustOrdNo +"locationType :" + locationType);
        virtualId = session.getOmsVirtualStrMatrixFindVirtualLocID(locID, locationType);
        // log.info("omsCustOrdNo "+omsCustOrdNo +"virtualId:" + virtualId);
        // log.info("omsCustOrdNo "+omsCustOrdNo +"***end-IdentifyVirutalLoc***");
        return virtualId;
    }

    //This method will create the fulfillment detail map with fulfill_order_no as key and value as items fulfied from same store from map having store_id as key

    public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> createFulfillDetailMap(BigDecimal omsCustOrdNo,
                                                                              Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap,
                                                                              int fulfillOrderNo) {
        TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap =
            new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();

        log.info("omsCustOrdNo " + omsCustOrdNo + "inside createFulfillDetailMap");
        for (BigDecimal key : sohMap.keySet()) {
            log.info("omsCustOrdNo " + omsCustOrdNo + sohMap.keySet());
            log.info("omsCustOrdNo " + omsCustOrdNo + "key=" + key);
            ArrayList<OmsTempCoFo> list = sohMap.get(key);
            log.info("omsCustOrdNo " + omsCustOrdNo + "------" + list.size());
            try {
                fulfillDetailMap.put(key, list);
            } catch (Exception e) {

            }
            for (OmsTempCoFo temp : list) {
                log.info("omsCustOrdNo " + omsCustOrdNo + "=================================");
                log.info("omsCustOrdNo " + omsCustOrdNo + " lineNo= " + temp.getLineNo());
                log.info("omsCustOrdNo " + omsCustOrdNo + " item= " + temp.getItem());

            }


        }
        return fulfillDetailMap;
    }

    public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> getMap(BigDecimal omsCustOrdNo) {
        log.info(omsCustOrdNo + "inside getMap " + fulfillDetailMap.keySet() + "-------------->");
        return fulfillDetailMap;
    }

    public BackOrderFulfillDetail processBackOrderItem(CustOrdItmDesc custOrdItmDesc, BigDecimal combID,
                                                       CustOrderDesc custOrderDesc, BigDecimal L_Pending_Qty,
                                                       BigDecimal omsCustOrdNo,
                                                       BigDecimal omsOrposCustOrderId) throws SOAPException,
                                                                                              EntityAlreadyExistsWSFaultException,
                                                                                              IllegalArgumentWSFaultException,
                                                                                              IllegalStateWSFaultException,
                                                                                              ValidationWSFaultException {
        log.info("<----------------------------Begin of  processBackOrderItem method ----------------------------------> ");
        log.info("omsCustOrdNo " + omsCustOrdNo + "inside backorder");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
        BackOrderFulfillDetail backOrderFulfillDetail = new BackOrderFulfillDetail();
        log.info(" Checking the  backOrderFulfillDetail Object address is" + backOrderFulfillDetail +
                 "--------------------------------->");
        log.info("omsCustOrdNo " + omsCustOrdNo + " L_Pending_Qty " + L_Pending_Qty);
        int fulfilledQty = 0;
        BigDecimal channelId = BigDecimal.ZERO;
        backOrderFulfillDetail.setSOH(fulfilledQty);
        try {
            BigDecimal currentPendingQty = BigDecimal.ZERO;
            List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList =
                session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
            int fulfilledqty = 0;
            log.info("omsCustOrdNo " + omsCustOrdNo + "omsFulfillMatrixExtDetailList.size() " +
                     omsFulfillMatrixExtDetailList.size());
            BigDecimal alloctedInventory = null;
            CustFutureInvPosition custFutureInvPosition = null;
            currentPendingQty = L_Pending_Qty;
            int i = 0;
            boolean itemLocked = false;
            //-------------------------------------------------


            Date startDate = new Date();
            while (fulfilledqty != L_Pending_Qty.intValue() && i < omsFulfillMatrixExtDetailList.size()) {

                String boIndicator = "N";
                log.info("omsCustOrdNo " + omsCustOrdNo + "i " + i);
                OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail = omsFulfillMatrixExtDetailList.get(i);
                BigDecimal boCombId = omsFulfillMatrixExtDetail.getCombinationId();
                log.info("omsCustOrdNo " + omsCustOrdNo + " boCombId " + boCombId);
                i++;

                try {
                    CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
                    BigDecimal physicalWH = BigDecimal.ZERO;
                    log.info("omsCustOrdNo " + omsCustOrdNo +
                             "****************************************************************");
                    try {
                        if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
                            List<Object[]> tempWhObject =
                                session.getWhFindPhysicalWH(omsFulfillMatrixExtDetail.getLocation());

                            for (Object[] result : tempWhObject) {
                                physicalWH = new BigDecimal(result[0].toString());
                                // omsFulfillMatrixExtDetail.setLocation(physicalWH);
                                channelId = new BigDecimal(result[1].toString());
                                log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" +
                                         result[1]);
                            }
                            log.info("omsCustOrdNo " + omsCustOrdNo + "physicalWH " + physicalWH);
                        }

                       
                        log.info("<-------------->omsCustOrdNo " + omsCustOrdNo + "<---------Current Thread------>" +
                                 Thread.currentThread() + "<------>");
                        log.info(" <---omsCustOrdNo--->" + omsCustOrdNo + "<-----Item is---->" +
                                 custOrdItmDesc.getItemId() + "<------->");
                        log.info("<----omsCustOrdNo--->" + omsCustOrdNo + "<--Location is----->" +
                                 omsFulfillMatrixExtDetail.getLocation() + "<------->");

                        log.info("Obtaining ItemLoc Singleton Object------------------------------------->");
                    } catch (Exception e) {
                        log.info("Exception occured " + e.getMessage());
                    }

                    itemLocked = PreOrderItemLocationSync.obtainLock(custOrdItmDesc.getItemId(), omsFulfillMatrixExtDetail.getLocation(), omsCustOrdNo.toPlainString());

                    BackOrderAllocatedInventory backOrderAllocatedInventory = new BackOrderAllocatedInventory();


                    //  alloctedInventory =session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(),custOrdItmDesc.getItemId());
                    log.info("omsCustOrdNo " + omsCustOrdNo + "Thread.current thread " +
                             Thread.currentThread().getName());
                    log.info("omsCustOrdNo " + omsCustOrdNo +
                             "Before calling getAllocatedInventoryfromBackOrderDTL method alloctedInventory " +
                             alloctedInventory);
                    // Thread.sleep(2000);
                    alloctedInventory =
                            backOrderAllocatedInventory.getAllocatedInventoryfromBackOrderDTL(omsFulfillMatrixExtDetail.getLocation(),
                                                                                              custOrdItmDesc.getItemId());

                    if (alloctedInventory == null) {
                        alloctedInventory = BigDecimal.ZERO;
                    }
                    log.info("omsCustOrdNo " + omsCustOrdNo +
                             "After calling getAllocatedInventoryfromBackOrderDTL alloctedInventory" +
                             alloctedInventory);
                    
                    log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItmDesc.getItemId());
                    log.info("omsCustOrdNo " + omsCustOrdNo + "omsFulfillMatrixExtDetail.getLocation().longValue() " +
                             omsFulfillMatrixExtDetail.getLocation().longValue());

                    try {
                        log.info("omsCustOrdNo " + omsCustOrdNo + "Finding bo indicator for location" +
                                 omsFulfillMatrixExtDetail.getLocation());
                        boIndicator =
                                oMSUtilCommons.getBOIndicator(omsFulfillMatrixExtDetail.getLocation(), custOrdItmDesc.getItemId());

                        log.info("omsCustOrdNo " + omsCustOrdNo +
                                 "fetched indicator value  from table is +++++++++++===" + boIndicator);
                        if (boIndicator == null || boIndicator.isEmpty()) {
                            log.info("omsCustOrdNo " + omsCustOrdNo + "boIndicator is null ,setting it to N");
                            boIndicator = "N";
                        } else if (boIndicator.equals("Y")) {

                            if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
                                custFutureInvPosition =
                                        checkItemLocSOH.findFutInvDateAndQty(custOrdItmDesc.getItemId(),
                                                                             physicalWH.longValue(), alloctedInventory,
                                                                             L_Pending_Qty,
                                                                             omsFulfillMatrixExtDetail.getLocationType(),
                                                                             channelId.intValue());

                            } else {
                                custFutureInvPosition =
                                        checkItemLocSOH.findFutInvDateAndQty(custOrdItmDesc.getItemId(),
                                                                             omsFulfillMatrixExtDetail.getLocation().longValue(),
                                                                             alloctedInventory, L_Pending_Qty,
                                                                             omsFulfillMatrixExtDetail.getLocationType(),
                                                                             channelId.intValue());
                            }
                        }
                    } catch (Exception e) {
                        boIndicator = "N";
                    }

                    if (boIndicator.equals("Y")) {
                        log.info("omsCustOrdNo " + omsCustOrdNo + "custFutureInvPosition values fetched" +
                                 custFutureInvPosition.getExpectedDate() + "qty=" +
                                 custFutureInvPosition.getExpectedQty());

                        log.info("omsCustOrdNo " + omsCustOrdNo + "custFutureInvPosition date " +
                                 custFutureInvPosition.getExpectedDate());
                        log.info("omsCustOrdNo " + omsCustOrdNo + "custFutureInvPosition qty " +
                                 custFutureInvPosition.getExpectedQty());
                        if (custFutureInvPosition.getExpectedDate() != null) {

                            log.info("omsCustOrdNo " + omsCustOrdNo +
                                     "inside custFutureInvPosition.getExpectedDate()");
                            int qtyToBeFulfilled =
                                custFutureInvPosition.getExpectedQty().intValue() - alloctedInventory.intValue();
                            log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToBeFulfilled " + qtyToBeFulfilled);
                            log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilledQty " + fulfilledQty);
                            if (qtyToBeFulfilled > 0) {

                                fulfilledQty = fulfilledQty + qtyToBeFulfilled;
                                backOrderFulfillDetail.setSOH(fulfilledQty);

                                OmsBackOrderDtl omsBackOrderDtl = new OmsBackOrderDtl();
                                omsBackOrderDtl.setSourceLoc(omsFulfillMatrixExtDetail.getLocation());
                                omsBackOrderDtl.setSourceLocType(omsFulfillMatrixExtDetail.getLocationType());
                                omsBackOrderDtl.setFulfillLocType(omsFulfillMatrixExtDetail.getDeliveryFromLocType());
                                omsBackOrderDtl.setFulfillLoc(omsFulfillMatrixExtDetail.getDeliveryFromLoc());
                                omsBackOrderDtl.setFulInvAvlDate(new Timestamp(custFutureInvPosition.getExpectedDate().getTime()));
                                omsBackOrderDtl.setCreateDatetime(new Timestamp(new Date().getTime()));
                                omsBackOrderDtl.setItem(custOrdItmDesc.getItemId());
                                omsBackOrderDtl.setLineNo(new BigDecimal(custOrdItmDesc.getLineItemNo()));
                                omsBackOrderDtl.setCombinationId(omsFulfillMatrixExtDetail.getCombinationId());
                                omsBackOrderDtl.setOmsCustOrdNo(omsCustOrdNo);
                                BigDecimal qtyToPersist = null;
                                log.info("omsCustOrdNo " + omsCustOrdNo + "currentPendingQty " + currentPendingQty);
                                if (qtyToBeFulfilled <= currentPendingQty.intValue()) {
                                    currentPendingQty =
                                            new BigDecimal(currentPendingQty.intValue() - qtyToBeFulfilled);
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "currentPendingQty " +
                                             currentPendingQty);
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToPersist=" + qtyToPersist);
                                    qtyToPersist = new BigDecimal(qtyToBeFulfilled);
                                } else {
                                    //omsBackOrderDtl.setSourceQty(custOrdItmDesc.getQuantity());
                                    qtyToPersist = currentPendingQty;
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "else qtyToPersist=" + qtyToPersist);
                                    currentPendingQty = BigDecimal.ZERO;
                                }
                                omsBackOrderDtl.setFulfillQty(BigDecimal.ZERO);
                                omsBackOrderDtl.setBackorderStatus("N");
                                omsBackOrderDtl.setCreatedBy("OMSUSER");

                                if (qtyToPersist.intValue() > 0) {
                                    omsBackOrderDtl.setSourceQty(qtyToPersist);

                                    // session.persistOmsBackOrderDtl(omsBackOrderDtl);


                                    backOrderFulfillDetail.setSourceLoc(omsFulfillMatrixExtDetail.getLocation());
                                    backOrderFulfillDetail.setSourceLocType(omsFulfillMatrixExtDetail.getLocationType());
                                    backOrderFulfillDetail.setFulfillLoc(omsFulfillMatrixExtDetail.getDeliveryFromLoc());
                                    backOrderFulfillDetail.setFulfillLocType(omsFulfillMatrixExtDetail.getDeliveryFromLocType());
                                    InterfacePersistence interfacePersistence = new InterfacePersistence();
                                    OmsCustOrdItem omsCustOrdItem =
                                        session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(),
                                                                            new BigDecimal(custOrdItmDesc.getLineItemNo()));
                                    if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" +
                                                 custOrdItmDesc.getItemId() + "qty=" + qtyToPersist + "loc=" +
                                                 physicalWH);
                                        interfacePersistence.callRMSBackorderWS(custOrdItmDesc.getItemId(),
                                                                                qtyToPersist, physicalWH.longValue(),
                                                                                "W", omsCustOrdItem.getStandardUom(),
                                                                                channelId);
                                    } else {
                                        log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" +
                                                 custOrdItmDesc.getItemId() + "qty=" + qtyToPersist + "loc=" +
                                                 omsFulfillMatrixExtDetail.getLocation());
                                        interfacePersistence.callRMSBackorderWS(custOrdItmDesc.getItemId(),
                                                                                qtyToPersist,
                                                                                omsFulfillMatrixExtDetail.getLocation().longValue(),
                                                                                "S", omsCustOrdItem.getStandardUom(),
                                                                                channelId);

                                    }
                                    log.info("<------- omsCustOrdNo----> " + omsCustOrdNo +
                                             "<-------Inserting Record into OmsBackOrderDtl Table ------------------------> ");
                                    log.info("<-----omsCustOrdNo ----->" + omsCustOrdNo +
                                             "<-----Executing Weblogic Thread --------------------->" +
                                             Thread.currentThread().getName() + "<--------->");
                                    session.persistOmsBackOrderDtl(omsBackOrderDtl);
                                   
                                   
                                    Date endDate = new Date();
                                    log.info(" 1.End Date------------------------>" + endDate);
                                    log.info("<----omsCustOrdNo --->" + omsCustOrdNo +
                                             "<-----Time Difference is---->" +
                                             (endDate.getTime() - startDate.getTime()) / 1000 + "Seconds" +
                                             "Thread Name--->" + Thread.currentThread().getName() + "<----->");
                                   
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "Back order successful");
                                    omsCustOrdItem.setBackorderInd("Y");
                                    //omsCustOrdItem.setStatus("S");
                                    log.info("omsCustOrdNo " + omsCustOrdNo + "setted BackOrder Indicator to Y");
                                    session.mergeOmsCustOrdItem(omsCustOrdItem);
                                }
                                OmsCustOrdHead omsCustOrdHead =
                                    session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                                log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdHead.getStatus() " +
                                         omsCustOrdHead.getStatus());
                                omsCustOrdHead.setStatus("S");

                                session.mergeOmsCustOrdHead(omsCustOrdHead);
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "updated the status to S in OmsCustOrdHead ");

                                OmsOrposCustOrderHead omsOrposCustOrderHead =
                                    session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
                                log.info("omsCustOrdNo " + omsCustOrdNo + "omsOrposCustOrderHead.getStatus() " +
                                         omsOrposCustOrderHead.getStatus());
                                omsOrposCustOrderHead.setStatus("S");
                                session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
                                log.info("omsCustOrdNo " + omsCustOrdNo +
                                         "updated the status to S in OmsOrposCustOrderHead ");
                                bo = true;
                            }


                            // break;

                        }
                    }
                    log.info("<----------omsCustOrdNo--->" + omsCustOrdNo +
                             "<----------------------- End of Bo Indicator Part--------------------------------------------------->");
                    log.info("<-----omsCustOrdNo-------->" + omsCustOrdNo +
                             "<-----Need to Remove Item-------------->" + custOrdItmDesc.getItemId() +
                             " and location" + "" + omsFulfillMatrixExtDetail.getLocation() +
                             "from Temp  map matrix--------------->");
                    ItemLoc itemloc = new ItemLoc();
                    itemloc.setItem(custOrdItmDesc.getItemId());
                    itemloc.setLocation(omsFulfillMatrixExtDetail.getLocation());
                } catch (Exception f) {
                    log.info("<--------omsCustOrdNo----> " + omsCustOrdNo +
                             "<------If Some Error Occured then it should allow other request to Procced ---->");
                    
                    log.warn("Exception occured inside while calling backorder ", f);
                } finally {
					try {
						if (itemLocked) {							
							PreOrderItemLocationSync.releaseLock(custOrdItmDesc.getItemId(), omsFulfillMatrixExtDetail.getLocation(), omsCustOrdNo.toPlainString());
						}
					} catch (Exception e) {
						log.warn("Error while relaesing the lock " + custOrdItmDesc.getItemId() + "~" + omsFulfillMatrixExtDetail.getLocation() + " for the oms cust ord no# " + omsCustOrdNo, e);
					}
				}

            }

        } catch (Exception h) {
            log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in back order " + h);
            //OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
            //omsCustOrdHead.setStatus("F");
            //session.mergeOmsCustOrdHead(omsCustOrdHead);
            //custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_105");//UNAVL_INV in BackOrder
            //log.info("omsCustOrdNo "+omsCustOrdNo +OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
        }
        return backOrderFulfillDetail;

    }

    public Map<String, CustOrdItmDesc> groupItem(BigDecimal omsCustOrdNo,
                                                 CustOrderDesc custOrderDesc) throws SOAPException {
        Map<String, CustOrdItmDesc> groupedItemMap = new HashMap<String, CustOrdItmDesc>();
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        String refValue = "";
        try {
            refValue =
                    session.getOmsReferenceDataFindRefValue("OMS_SYSTEM_OPTION", "STORE_GROUPING_PARAM", String.valueOf(custOrderDesc.getInitiateLocId()));
            log.info("omsCustOrdNo " + omsCustOrdNo + "refValue from try block OmsReferenceData " + refValue);
        } catch (Exception e) {
            refValue = session.getOmsSystemParametersFindIndValue("DEFAULT_GROUPING_PARAM", "OMS_SYSTEM_OPTION");
            log.info("omsCustOrdNo " + omsCustOrdNo + "refValue from catch block OmsSystemParameters " + refValue);
        }
        log.info("omsCustOrdNo " + omsCustOrdNo + "Grouping flag is " + refValue);
        log.info("omsCustOrdNo " + omsCustOrdNo + "custOrderDesc.getGiftReceiptAssigned().value() " +
                 custOrderDesc.getGiftReceiptAssigned().value());
        if (custOrderDesc.getGiftReceiptAssigned().value().equals("Y")) {
            refValue = "N";
        }
        for (CustOrdItmDesc custOrdItmDesc : custOrderDesc.getCustOrdItmColDesc().getCustOrdItmDesc()) {
            String discountKey = ",";
            log.info("Discount Mapping");
            log.info("refValue " + refValue);
            if (custOrdItmDesc.getDiscntLineColDesc() != null) {
                log.info(" inside if condition for custOrdItmDesc.getDiscntLineColDesc() ");
                for (DiscntLineDesc discntLineDesc : custOrdItmDesc.getDiscntLineColDesc().getDiscntLineDesc()) {
                    log.info("inside for loop for custOrdItmDesc.getDiscntLineColDesc().getDiscntLineDesc() ");
                    discountKey = discountKey.concat(discntLineDesc.getDiscountReasonCode());
                    log.info("Discount Keys : " + discountKey);
                }
            }
            log.info("outside the if condition for custOrdItmDesc.getDiscntLineColDesc().getDiscntLineDesc()");
            log.info("omsCustOrdNo " + omsCustOrdNo + " and " + "Line No " + custOrdItmDesc.getLineItemNo() +
                     " Item " + custOrdItmDesc.getItemId() + "RMS Promo Type=" + discountKey);
            if (refValue.equals("Y")) {
                String key = custOrdItmDesc.getItemId() + "," + custOrdItmDesc.getUnitSellPrice() + discountKey;
                log.info("KEY : " + key);
                if (groupedItemMap.containsKey(key)) {
                    CustOrdItmDesc custOrdItmDescTemp = new CustOrdItmDesc();
                    custOrdItmDescTemp = groupedItemMap.get(key);
                    if (custOrdItmDesc.getUnitSellPrice().compareTo(custOrdItmDescTemp.getUnitSellPrice()) == 0) {
                        log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItmDescTemp.getQuantity()=" +
                                 custOrdItmDescTemp.getQuantity() + "for lineno" + custOrdItmDesc.getLineItemNo());
                        custOrdItmDescTemp.setQuantity(custOrdItmDescTemp.getQuantity().add(custOrdItmDesc.getQuantity()));
                        log.info("omsCustOrdNo " + omsCustOrdNo + "While grouping qty is now " +
                                 custOrdItmDescTemp.getQuantity());
                        groupedItemMap.put(key, custOrdItmDescTemp);
                    } else {
                        log.info("omsCustOrdNo " + omsCustOrdNo + " in else block for unit sell price " + key);
                        groupedItemMap.put(key, custOrdItmDesc);
                    }
                } else {
                    log.info("omsCustOrdNo " + omsCustOrdNo + " in else block not contain key " + key);
                    groupedItemMap.put(key, custOrdItmDesc);
                }
            } else {
                String key = String.valueOf(custOrdItmDesc.getLineItemNo());
                log.info("omsCustOrdNo " + omsCustOrdNo + " else block for  refValue key is " + key);
                groupedItemMap.put(key, custOrdItmDesc);
            }
        }
        log.info("omsCustOrdNo " + omsCustOrdNo + "groupedItemMap " + groupedItemMap.keySet());
        return groupedItemMap;
    }

    public OmsTempCoFo STfulFilOrderNo(OmsTempCoFo omsTempCoFo, Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap) {
        //Added for 2609 bug
        log.info("inside STfulFilOrderNo");
        BigDecimal stfulfilOrderNO = omsTempCoFo.getFulfillOrderNo();
        log.info("sohMap.keySet() " + sohMap.keySet());
        log.info("omsTempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
        ArrayList<String> itemlist = new ArrayList<String>();
        ArrayList<OmsTempCoFo> list = new ArrayList<OmsTempCoFo>();
        for (BigDecimal k : sohMap.keySet()) {
            log.info("======Key============== " + k);
            list = sohMap.get(k);
            for (OmsTempCoFo temp : list) {
                if (temp.getSourceLocId().intValue() == omsTempCoFo.getSourceLocId().intValue() &&
                    temp.getFulfillLocId().intValue() == omsTempCoFo.getFulfillLocId().intValue()) {
                    log.info("temp.getSourceLocId() " + temp.getSourceLocId());
                    log.info("temp.getFulfillLocId() " + temp.getFulfillLocId());
                    log.info("omsTempCoFo.getSourceLocId() " + omsTempCoFo.getSourceLocId());
                    log.info("omsTempCoFo.getFulfillocId() " + omsTempCoFo.getFulfillLocId());
                    log.info("temp.getItem() " + temp.getItem());
                    log.info("omsTempCoFo.getItem() " + omsTempCoFo.getItem());
                    log.info("temp fulfilorderNo " + temp.getFulfillOrderNo());
                    log.info("omsTempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
                    stfulfilOrderNO = temp.getFulfillOrderNo();
                    log.info("Adding item " + temp.getItem());
                    itemlist.add(temp.getItem().toString());
                    log.info("stfulfilOrderNO " + stfulfilOrderNO);
                }
            }
        }

        log.info("itemlist " + itemlist.toString());
        log.info("omsTempCoFo.getItem() " + omsTempCoFo.getItem());

        if (itemlist.contains(omsTempCoFo.getItem())) {
            log.info("return actual fulfillOrderNo " + omsTempCoFo.getFulfillOrderNo());
        } else {
            omsTempCoFo.setFulfillOrderNo(stfulfilOrderNO);
            log.info("set the stfulfilOrderNO " + stfulfilOrderNO);

        }

        log.info("while returning the fulfilOrderNo is " + omsTempCoFo.getFulfillOrderNo());
        return omsTempCoFo;
    }


}
