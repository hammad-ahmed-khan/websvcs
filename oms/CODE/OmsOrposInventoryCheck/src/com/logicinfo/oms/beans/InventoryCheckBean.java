package com.logicinfo.oms.beans;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.model.CustOrdFulDesc;
import com.logicinfo.oms.model.CustOrdFulDescResponse;
import com.logicinfo.oms.model.CustOrdItmDesc;
import com.logicinfo.oms.model.CustOrdItmDescResponse;
import com.logicinfo.oms.model.InventoryCheck;
import com.logicinfo.oms.model.InventoryCheckResponse;
import com.logicinfo.oms.util.OMSUtil;


public class InventoryCheckBean {
    public InventoryCheckBean() {
        super();
    }
    BigDecimal combID;
    int priority;
    long SOH;
    private final static Logger log = Logger.getLogger(InventoryCheckBean.class.getName());


    public InventoryCheckResponse checkInventory(InventoryCheck inventoryCheck) throws SOAPException 
    {
        log.info("inside checkInventory method");
        OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
        OMSUtilSessionEJB session = OMSUtil.doLookup();
		String deliverZone = null;
		String marketPlaceInd = null;
        Map<String, Map<BigDecimal, Long>> itemQtyMap = new HashMap<String, Map<BigDecimal, Long>>();
        Map<String, Long> masterLocMap = new HashMap<String, Long>();
        Map<String, Long> otherLocMap = new HashMap<String, Long>();
        Map<String, Long> futureLocMap = new HashMap<String, Long>();
        FindNextFulfillLoc findNextfulfillLoc = new FindNextFulfillLoc();
        BigDecimal nextLoc;
        String shipClassification = null;
        InventoryCheckResponse inventoryCheckResponse = new InventoryCheckResponse();
        List<CustOrdFulDescResponse> custOrdFulDescResponseList = inventoryCheckResponse.getCustOrdFulDescResponse();
        InterfacePersistence interfacePersistence = new InterfacePersistence();
        String shipingChargeDept =
            session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
       log.info("shipingChargeDept "+shipingChargeDept);
        for (CustOrdFulDesc custOrdFulDesc : inventoryCheck.getCustOrdFulDesc()) {

            inventoryCheckResponse.setInitiateLocId(inventoryCheck.getInitiateLocId());
            CustOrdFulDescResponse custOrdFulDescResponse = new CustOrdFulDescResponse();
            custOrdFulDescResponse.setDeliveryType(custOrdFulDesc.getDeliveryType());
            if (custOrdFulDesc.getDeliveryType().equals("C")) {
                custOrdFulDescResponse.setPickLoc(custOrdFulDesc.getPickLoc());
            }
            custOrdFulDescResponse.setShipCity(custOrdFulDesc.getShipCity());
            List<CustOrdItmDescResponse> custOrdItmDescResponseList =
                custOrdFulDescResponse.getCustOrdItmDescResponse();
            int count = 1;
            long SOH = 0;
            for (CustOrdItmDesc custOrdItmDesc : custOrdFulDesc.getCustOrdItmDesc()) {
                boolean isPOAvailable = false;
                count = 1;
                log.info("Item=" + custOrdItmDesc.getItemId() + "Lineno =" + custOrdItmDesc.getLineNo());
                Date fututreAvailDate = null;
                priority = 1;
                CustOrdItmDescResponse custOrdItmDescResponse = new CustOrdItmDescResponse();

                long L_Cum_Ord_Qty = 0L;
                long L_Pending_Qty = custOrdItmDesc.getRequestedQty().longValue();
                BigDecimal itemDept = null;
                String inventoryIndn = null;
                String applicationId = "ORPOS";
                String shipToStore = "N";


                try {
                    itemDept = session.getItemMasterFindDept(custOrdItmDesc.getItemId());
                    log.info("Item dept fetched from item_master " + itemDept);
                    if (itemDept != null) {

                        inventoryIndn = session.getItemMasterFindInventoryInd(custOrdItmDesc.getItemId(), itemDept);
                        log.info(" inventory Indicator= " + inventoryIndn);
                    }


                    if (shipingChargeDept.equals(itemDept.toString()) == false && inventoryIndn.equals("Y")) {
                        try {
                            if (custOrdFulDesc.getDeliveryType().equals("S")) {
                                log.info("Condition : Ship to customer");
                                //Find the combination id from fulfillmentMatrix

                                String refValue = "";
                                try {
                                    refValue =
                                            session.getOmsReferenceDataFindByISOCode("STATE_CITY_LINK", custOrdFulDesc.getShipCity().toUpperCase());
                                    log.info("City from oms_reference_data is " + refValue);
                                } catch (Exception e) {
                                    log.error("Failed with " + e);
                                }
                                try {
                                    shipClassification =
                                            oMSUtilCommons.findShipmentClassification(custOrdItmDesc.getItemId(),
                                                                                      new BigDecimal(inventoryCheck.getInitiateLocId()),
                                                                                      null);
                                    log.info("shipClassification " + shipClassification);
                                } catch (Exception e) {
                                    custOrdFulDescResponse.setErrorMessage("OMS_ORPOS_ERROR_113");
                                    List<CustOrdFulDescResponse> custOrdFulDescResponseList1 =
                                        inventoryCheckResponse.getCustOrdFulDescResponse();
                                    custOrdFulDescResponseList1.add(custOrdFulDescResponse);
                                    log.info(" returning inventoryCheckResponse ");
                                    return inventoryCheckResponse;
                                }
                                combID =
                                        oMSUtilCommons.processFulfillmentMatrixGetCombID(new BigDecimal(inventoryCheck.getInitiateLocId()),
                                                                                         shipClassification,
                                                                                         refValue.toUpperCase(),
                                                                                         custOrdFulDesc.getDeliveryType(),deliverZone, marketPlaceInd, applicationId, shipToStore);
                            } else {
                                log.info("Condition : Customer pick up");
                                //Find the combination id from fulfillmentMatrix
                                combID =
                                        oMSUtilCommons.processFulfillmentMatrixGetCombIDWoCity(new BigDecimal(custOrdFulDesc.getPickLoc()),
                                                                                               shipClassification,
                                                                                               custOrdFulDesc.getDeliveryType(), deliverZone, marketPlaceInd,applicationId, shipToStore);
                            }
                        } catch (Exception e) {
                            log.info("Combination id is not available");
                            custOrdFulDescResponse.setErrorMessage("OMS_ORPOS_ERROR_102");
                            List<CustOrdFulDescResponse> custOrdFulDescResponseList1 =
                                inventoryCheckResponse.getCustOrdFulDescResponse();
                            custOrdFulDescResponseList1.add(custOrdFulDescResponse);
                            log.info(" returning inventoryCheckResponse ");
                            return inventoryCheckResponse;
                        }

                        int j = 1;
                        List<OmsFulfillMatrixExtDetail> list =
                            session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
                        int recordCount = 0;
                        long otherLocQty = 0;
                        long masterLocQty=0;
                        while (recordCount <= list.size()) {

                            log.info("------------------Iteration=" + j + "with qty fulfilled till now is " +
                                     L_Cum_Ord_Qty + "-----------");
                            j++;
                            recordCount++;
                            OmsFulfillMatrixExtDetail fulfillMatrixExtDetailResult = null;
                            try {
                                fulfillMatrixExtDetailResult =
                                        findNextfulfillLoc.processFulfillmentMatrix(combID, priority);
                            } catch (Exception e) {

                                log.info("Inside catch will find back order back order");
                                //Check for back order
                                //  if (L_Cum_Ord_Qty < custOrdItmDesc.getRequestedQty().longValue())
                                //    {
                                log.info("isPOAvailable=" + isPOAvailable);
                                if (isPOAvailable == false)
                                {
                                    log.info("Finding backorder with qty" + L_Pending_Qty);
                                    List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList =
                                        session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
                                    BackOrderResponse backOrderResponse =
                                        interfacePersistence.backOrder(omsFulfillMatrixExtDetailList, custOrdItmDesc,
                                                                       L_Pending_Qty);

                                    log.info("got response from back order");
                                    if (backOrderResponse != null && backOrderResponse.getFutureAvlQty() > 0) {
                                        log.info("backOrderResponse is not null");

                                        custOrdItmDescResponse.setInTransitQty(BigDecimal.ONE);

                                        log.info("L_Pending_Qty " + L_Pending_Qty);
                                        log.info("backOrderResponse.getFutureAvlQty() " +
                                                 backOrderResponse.getFutureAvlQty());

                                        log.info("Setting fut qty to " + backOrderResponse.getFutureAvlQty());
                                        custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(backOrderResponse.getFutureAvlQty()));
                                        if(futureLocMap.get(custOrdItmDesc.getItemId())!=null)
                                        {
                                           custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(futureLocMap.get(custOrdItmDesc.getItemId())));
                                           if(futureLocMap.get(custOrdItmDesc.getItemId())-L_Pending_Qty<0)
                                           {
                                        	   futureLocMap.put(custOrdItmDesc.getItemId(), 0L);
                                           }else
                                           {
                                           futureLocMap.put(custOrdItmDesc.getItemId(), futureLocMap.get(custOrdItmDesc.getItemId())-L_Pending_Qty);
                                           }
                                            log.info("Adding in futureLocMap for item 1 in if"+custOrdItmDesc.getItemId()+"with qty"+futureLocMap.get(custOrdItmDesc.getItemId()));

                                        }
                                        else
                                        {
                                          custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(backOrderResponse.getFutureAvlQty()));
                                          if(backOrderResponse.getFutureAvlQty()-L_Pending_Qty<0)
                                          {
                                       	   	  futureLocMap.put(custOrdItmDesc.getItemId(), 0L);
                                          }else
                                          {
                                        	  futureLocMap.put(custOrdItmDesc.getItemId(), backOrderResponse.getFutureAvlQty()-L_Pending_Qty);
                                          }
                                            log.info("Adding in futureLocMap for item 1 in else"+custOrdItmDesc.getItemId()+"with qty"+futureLocMap.get(custOrdItmDesc.getItemId()));

                                        }
                                        
                                         /* if(futureLocMap.get(custOrdItmDesc.getItemId())!=null)
                                         {
                                        futureLocMap.put(custOrdItmDesc.getItemId(), futureLocMap.get(custOrdItmDesc.getItemId())-custOrdItmDesc.getRequestedQty().longValue());
                                         }
                                         else
                                         {
                                            */  //futureLocMap.put(custOrdItmDesc.getItemId(), backOrderResponse.getFutureAvlQty()-custOrdItmDesc.getRequestedQty().longValue());
    
                                        // }
                                    }
                                    /* if (L_Pending_Qty > backOrderResponse.getFutureAvlQty()) 
                                    {
                                        custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(L_Pending_Qty));
                                    }
                                    if(futureLocMap.isEmpty()==false)
                                    {
                                    if(L_Pending_Qty>futureLocMap.get(custOrdItmDesc.getItemId())) 
                                    {
                                        
                                        log.info("setting the response as "+custOrdItmDesc.getRequestedQty());
                                        custOrdItmDescResponse.setFutAvlQuantity(custOrdItmDesc.getRequestedQty());
                                        
                                            
                                    }
                                    } */
                                }
                                break;
                            }
                            nextLoc = fulfillMatrixExtDetailResult.getLocation();

                            try {
                                if (itemQtyMap.containsKey(custOrdItmDesc.getItemId())) {
                                    log.info("Item " + custOrdItmDesc.getItemId() + " already exist");
                                    int nextLocExist = 0;
                                    log.info("" + itemQtyMap.get(custOrdItmDesc.getItemId()).size());

                                    Map<BigDecimal, Long> temp = itemQtyMap.get(custOrdItmDesc.getItemId());
                                    log.info("---" + temp.keySet());

                                    if (temp.containsKey(nextLoc)) {
                                        SOH = temp.get(nextLoc);
                                        log.info("location " + nextLoc + " exist with SOH=" + SOH);
                                        nextLocExist = 1;
                                        if (nextLoc.intValue() < 0) {
                                            nextLocExist=0;
                                           // custOrdItmDescResponse.setFutAvlQuantity(custOrdItmDesc.getRequestedQty());
                                        }
                                        // break;
                                    }


                                    if (nextLocExist == 1) {
                                       // SOH = itemQtyMap.get(custOrdItmDesc.getItemId()).get(nextLoc);
                                       

                                    } else {
                                        log.info("Item exist but finding the location witn next location " + nextLoc);
                                        if (fulfillMatrixExtDetailResult.getLocationType().equals("ST")) {
                                            SOH =
interfacePersistence.callSIMStoreInventory(custOrdItmDesc.getItemId(), nextLoc);

                                        } else if (fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {
                                            SOH =
interfacePersistence.findWHInventory(custOrdItmDesc.getItemId(), fulfillMatrixExtDetailResult.getLocation());
                                        } else if (fulfillMatrixExtDetailResult.getLocationType().equals("SU")) {
                                            isPOAvailable = true;
                                            String item_status =
                                                findNextfulfillLoc.findItemStatus(custOrdItmDesc.getItemId(),
                                                                                  fulfillMatrixExtDetailResult.getDeliveryFromLoc());
                                            if (item_status.equals("A") == false) {
                                                throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
                                            }
                                            log.info("Before going for PO checking back order");


                                            log.info("Finding backorder with qty" + L_Pending_Qty);
                                            List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList =
                                                session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
                                            BackOrderResponse backOrderResponse=null;
                                            log.info("^^^^^^^^^^^^^^^^^^"+futureLocMap.get(custOrdItmDesc.getItemId()));
                                            if(futureLocMap.get(custOrdItmDesc.getItemId())==null)
                                            {
                                             backOrderResponse =
                                                interfacePersistence.backOrder(omsFulfillMatrixExtDetailList,
                                                                               custOrdItmDesc, L_Pending_Qty);
                                            log.info("got back order responmse");

                                            log.info("backOrderResponse.getFutureAvlQty()=" +
                                                     backOrderResponse.getFutureAvlQty());
                                            if (backOrderResponse != null && backOrderResponse.getFutureAvlQty() > 0) {
                                                log.info("backOrderResponse is not null");
                                                if (backOrderResponse.getFutureAvlQty() > 0) {

                                                    custOrdItmDescResponse.setInTransitQty(BigDecimal.ONE);

                                                    log.info("L_Pending_Qty " + L_Pending_Qty);
                                                    log.info("backOrderResponse.getFutureAvlQty() " +
                                                             backOrderResponse.getFutureAvlQty());

                                                    log.info("Setting fut qty to " +
                                                             backOrderResponse.getFutureAvlQty());
                                                    custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(backOrderResponse.getFutureAvlQty()));

                                                }
                                                log.info("L_Pending_Qty " + L_Pending_Qty);
                                                log.info("backOrderResponse.getFutureAvlQty() " +
                                                         backOrderResponse.getFutureAvlQty());
                                               
futureLocMap.put(custOrdItmDesc.getItemId(), backOrderResponse.getFutureAvlQty()-L_Pending_Qty);
                                                
         log.info("Adding in futureLocMap for item 2"+custOrdItmDesc.getItemId()+"with qty"+(backOrderResponse.getFutureAvlQty()-L_Pending_Qty));

                                            }
                                            if (L_Pending_Qty >= backOrderResponse.getFutureAvlQty()) {
                                                //PO condition
                                                custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(L_Pending_Qty));
                                            }
                                            
                                        }else {
                                             long avaialbleFutureQty=   futureLocMap.get(custOrdItmDesc.getItemId());
                                                log.info("avaialbleFutureQty="+avaialbleFutureQty+"L_Pending_Qty="+L_Pending_Qty);
                                                if (L_Pending_Qty > avaialbleFutureQty) {
                                                    log.info("PO condition"); 
                                                    custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(L_Pending_Qty));
                                                    futureLocMap.put(custOrdItmDesc.getItemId(), L_Pending_Qty);
                                               log.info("Setting futureLocMap to"+L_Pending_Qty);
                                                }else {
                                                    custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(avaialbleFutureQty));
                                                    futureLocMap.put(custOrdItmDesc.getItemId(), futureLocMap.get(custOrdItmDesc.getItemId())-L_Pending_Qty);
                                                    log.info("Setting futureLocMap to"+(futureLocMap.get(custOrdItmDesc.getItemId())-L_Pending_Qty));
                                                }
                                                
                                            }
                                            
                                         
                                        }
                                    }

                                } else {
                                    log.info("Item does not exists");
                                    if (fulfillMatrixExtDetailResult.getLocationType().equals("ST")) {
                                        SOH =
interfacePersistence.callSIMStoreInventory(custOrdItmDesc.getItemId(), nextLoc);

                                    } else if (fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {
                                        SOH =
interfacePersistence.findWHInventory(custOrdItmDesc.getItemId(), fulfillMatrixExtDetailResult.getLocation());
                                    } else if (fulfillMatrixExtDetailResult.getLocationType().equals("SU")) {
                                        String item_status =
                                            findNextfulfillLoc.findItemStatus(custOrdItmDesc.getItemId(),
                                                                              fulfillMatrixExtDetailResult.getDeliveryFromLoc());
                                        if (item_status.equals("A") == false) {
                                            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
                                        }
                                        log.info("Before going for PO checking back order");

                                        log.info("Finding backorder with qty" + L_Pending_Qty);
                                        List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList =
                                            session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
                                        BackOrderResponse backOrderResponse =
                                            interfacePersistence.backOrder(omsFulfillMatrixExtDetailList,
                                                                           custOrdItmDesc, L_Pending_Qty);
                                        log.info("got response from back order");
                                        //  SOH=L_Pending_Qty;
                                        //log.info("SOH in case of back order"+SOH);(
                                        log.info("backOrderResponse.getFutureAvlQty()=" +
                                                 backOrderResponse.getFutureAvlQty());

                                        if (backOrderResponse != null && backOrderResponse.getFutureAvlQty() > 0) {
                                            log.info("backOrderResponse is not null");
                                            if (backOrderResponse.getFutInvAvlDate() != null) {
                                               
                                                custOrdItmDescResponse.setInTransitQty(BigDecimal.ONE);

                                                log.info("L_Pending_Qty " + L_Pending_Qty);
                                                log.info("backOrderResponse.getFutureAvlQty() " +
                                                         backOrderResponse.getFutureAvlQty());

                                                log.info("Setting fut qty to " + backOrderResponse.getFutureAvlQty());
                                                custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(backOrderResponse.getFutureAvlQty()));
                                                futureLocMap.put(custOrdItmDesc.getItemId(), backOrderResponse.getFutureAvlQty()-custOrdItmDesc.getRequestedQty().longValue());
                                                 log.info("Adding in futureLocMap for item 3"+custOrdItmDesc.getItemId()+"with qty"+(backOrderResponse.getFutureAvlQty()-custOrdItmDesc.getRequestedQty().longValue()));
                                            }

                                            if (L_Pending_Qty > backOrderResponse.getFutureAvlQty()) {
                                                custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(L_Pending_Qty));
                                            }
                                            if(futureLocMap.isEmpty()==false)
                                            {
                                            if(L_Pending_Qty>futureLocMap.get(custOrdItmDesc.getItemId())) {
                                            custOrdItmDescResponse.setFutAvlQuantity(custOrdItmDesc.getRequestedQty());
                                            }
                                            }
                                        }
                                        else{
                                            custOrdItmDescResponse.setFutAvlQuantity(custOrdItmDesc.getRequestedQty());  
                                        }

                                    }

                                }
                                log.info("count="+count);
                                if (count == 1) {
                                log.info("+++++++++++++++++++Setting master loc to "+SOH);
                                masterLocQty=SOH;
                                if(masterLocMap.get(custOrdItmDesc.getItemId())==null)
                                                                                   {
                                    masterLocMap.put(custOrdItmDesc.getItemId(), SOH);
                                                                                   }
                               
                                    count++;
                                }else{
                                    
                                    otherLocQty=otherLocQty+SOH;
                              
                                }
                              

                            } catch (Exception e) {
                                log.error("Error=" + e);
                            }


                            

                            log.info("L_Pending_Qty=" + L_Pending_Qty + "   L_Cum_Ord_Qty=" + L_Cum_Ord_Qty +
                                     "   SOH=" + SOH);
                            
                            Map<BigDecimal, Long> itemLocMap = new HashMap<BigDecimal, Long>();

                            if (itemQtyMap.get(custOrdItmDesc.getItemId()) !=null) {
                                itemLocMap = itemQtyMap.get(custOrdItmDesc.getItemId());
                            }

                            log.info("itemLocMap.keySet=" + itemLocMap.keySet());
                            if (SOH > L_Pending_Qty) {
                                log.info("SOH >= L_Pending_Qty setting SOH to " +
                                         (SOH - custOrdItmDesc.getRequestedQty().longValue()) + "for location+" +
                                         nextLoc);
                                itemLocMap.put(nextLoc, SOH - L_Pending_Qty);
                                //   itemSOH.setSoh(new BigDecimal(SOH-custOrdItmDesc.getRequestedQty().longValue()) );
                            } else
                                
                            {
                                itemLocMap.put(nextLoc, 0L); 
                            }
                            itemQtyMap.put(custOrdItmDesc.getItemId(), itemLocMap);
                            L_Cum_Ord_Qty = L_Cum_Ord_Qty + Math.min(SOH, L_Pending_Qty);
                            L_Pending_Qty = custOrdItmDesc.getRequestedQty().longValue() - L_Cum_Ord_Qty;
                            priority++;
                            log.info("***!");
                            custOrdItmDescResponse.setItemId(custOrdItmDesc.getItemId());
                            if (custOrdFulDesc.getDeliveryType().equals("S")) {
                                custOrdItmDescResponse.setLoc(inventoryCheck.getInitiateLocId());
                            } else {
                                custOrdItmDescResponse.setLoc(custOrdFulDesc.getPickLoc());
                            }
               
                            log.info("setting response");
                            custOrdItmDescResponse.setLineNo(custOrdItmDesc.getLineNo());

                            custOrdItmDescResponse.setRequestedQty(custOrdItmDesc.getRequestedQty());
                            if(nextLoc.intValue()>0){
                            custOrdItmDescResponse.setAvailableQty(new BigDecimal(otherLocQty));
                            }
                            log.info("Setting available qty to " + otherLocQty+
                                     "for item " + custOrdItmDesc.getItemId() + "line no " +
                                     custOrdItmDesc.getLineNo());

                            try {
                                log.info("Stock on hand " + masterLocQty +
                                         "for item " + custOrdItmDesc.getItemId() + "line no " +
                                         custOrdItmDesc.getLineNo());
                                custOrdItmDescResponse.setStockOnHandQty(new BigDecimal(masterLocQty));


                            } catch (Exception e) {
                                log.error("faied in finding soh qty");
                                custOrdItmDescResponse.setStockOnHandQty(BigDecimal.ZERO);
                            }
                         /*    if(masterLocMap.get(custOrdItmDesc.getItemId())>custOrdItmDesc.getRequestedQty().longValue()) {
                            log.info("masterLocMap.get(custOrdItmDesc.getItemId())>custOrdItmDesc.getRequestedQty().longValue()"+(masterLocMap.get(custOrdItmDesc.getItemId())-custOrdItmDesc.getRequestedQty().longValue()));
                                masterLocMap.put(custOrdItmDesc.getItemId(), masterLocMap.get(custOrdItmDesc.getItemId())-custOrdItmDesc.getRequestedQty().longValue());
                            }
                            else {
                                 if(masterLocMap.get(custOrdItmDesc.getItemId())>0) {
                                     log.info("masterLocMap.get(custOrdItmDesc)>0");
                                     long diffQty=custOrdItmDesc.getRequestedQty().longValue()-masterLocMap.get(custOrdItmDesc.getItemId());
                                     masterLocMap.put(custOrdItmDesc.getItemId(),0L);
                                     if(otherLocMap.get(custOrdItmDesc.getItemId())>diffQty) {
                                     log.info("otherLocMap.get(custOrdItmDesc.getItemId())>diffQty "+diffQty);
                                     otherLocMap.put(custOrdItmDesc.getItemId(), otherLocMap.get(custOrdItmDesc.getItemId())-diffQty);
                                     }else {
                                    otherLocMap.put(custOrdItmDesc.getItemId(),0L);
                                     }
                                 }
                                } */

                        } //while loop ending
                     
                        custOrdItmDescResponseList.add(custOrdItmDescResponse);
                    }
                    else 
                    {
                        custOrdItmDescResponse.setItemId(custOrdItmDesc.getItemId());
                        custOrdItmDescResponse.setLineNo(custOrdItmDesc.getLineNo());
                        custOrdItmDescResponse.setRequestedQty(custOrdItmDesc.getRequestedQty());
                        custOrdItmDescResponse.setAvailableQty(BigDecimal.ZERO);
                        custOrdItmDescResponse.setStockOnHandQty(custOrdItmDesc.getRequestedQty());

                        custOrdFulDescResponse.setPickLoc(inventoryCheck.getInitiateLocId());
                        custOrdItmDescResponseList.add(custOrdItmDescResponse);
                    }
                } catch (Exception e) {
                    log.error("Error"+e);
                    custOrdFulDescResponse.setErrorMessage("OMS_ORPOS_ERROR_107");
                    //                     custOrdItmDescResponse.setItemId(custOrdItmDesc.getItemId());
                    //                     custOrdItmDescResponse.setLineNo(custOrdItmDesc.getLineNo());
                    //                     custOrdItmDescResponse.setRequestedQty(custOrdItmDesc.getRequestedQty());
                    //                     custOrdItmDescResponse.setAvailableQty(custOrdItmDesc.getRequestedQty());
                    //                     custOrdItmDescResponse.setStockOnHandQty(BigDecimal.ZERO);
                    //                     custOrdItmDescResponse.setInTransitQty(BigDecimal.ZERO);
                    //                     custOrdFulDescResponse.setPickLoc(inventoryCheck.getInitiateLocId());
                    // custOrdItmDescResponseList.add(custOrdItmDescResponse);

                }

            } //end of item loop


            custOrdFulDescResponseList.add(custOrdFulDescResponse);

        } //end of fulfillmetn detail loop
        return inventoryCheckResponse;
    }


}


