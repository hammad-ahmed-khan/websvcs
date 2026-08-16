package com.logicinfo.oms.beans;


import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSUtil;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class PartialResponse
{
    private final static Logger log = Logger.getLogger(PartialResponse.class.getName());

    public PartialResponse()
    {
        super();
    }

    public ReturnPartialResponseObject STLocation(String item, BigDecimal sourceLocId, long availQty, long pendingQty,
                                                  BigDecimal omsCustOrdNo, long SOH, long orderQty,
                                                  int maxFulfilOrderNo, Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap,
                                                  BigDecimal lineNo, BigDecimal combinationId, String fulFillLocType,
                                                  String srcLocType, BigDecimal fulfillLocId, BigDecimal virtualWH,
                                                  BigDecimal tempOrdQty, String srcLocfulfiLoc, String storeMapValue,
                                                  TreeMap<BigDecimal, String> storeFulFillMap) throws SOAPException {
        
        ReturnPartialResponseObject returnPartialResponse = new ReturnPartialResponseObject();
        int tempkey =maxFulfilOrderNo;
        log.info("<-----------------------------Store====Location ------------------------------------->");
        log.info("<---- Till Now MaxFulfilOrderNo-------->"+tempkey+"<------>");
        try {
            log.info("omsCustOrdNo " + omsCustOrdNo + " Item " + item + "sourceLocId" + sourceLocId + "SOH " + SOH);
            srcLocfulfiLoc = sourceLocId + "," + fulfillLocId;
            log.info("+++++++++++++++srcLocfulfiLoc++++++++++++++++" + srcLocfulfiLoc);
            log.info("---------------inside STLocation----------------- ");
            log.info("availQty----> " + availQty);
            log.info("orderQty----> " + orderQty);
            log.info("Pending Quantity----->"+pendingQty);
            log.info("tempOrdQty---->"+tempOrdQty);
            log.info("SOH from ST " + SOH);
            if (SOH > 0) {
                log.info("<------------------- When Stock On hand is greater than zero-------------->");
                if (SOH >= orderQty) {
                    log.info("SOH is greater than orderQty");
                    availQty = orderQty;
                    pendingQty = tempOrdQty.intValue() - availQty;
                    log.info("availQty " + availQty);
                    log.info("pendingQty " + pendingQty);
                } else if (orderQty >= SOH) {
                    log.info("SOH less than the  orderQty");
                    availQty = SOH;
                    pendingQty = orderQty - availQty;
                    log.info("availQty " + availQty);
                    log.info("pendingQty " + pendingQty);
                }
            } else {
                log.info("<---------- When Stock on hand is less than zero....-------->");
                availQty = 0;
                pendingQty=orderQty;
                log.info("Order Quantity ------->"+orderQty);
                log.info("Pending Quantity"+pendingQty);
            }
        } catch (Exception e) {
            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
        }

        if (availQty > 0) {
            log.info("<-------Availability Quantity is gretaer than zero-------->");
            orderQty = pendingQty;
            log.info("orderQty " + orderQty);
            log.info("maxFulfilOrderNo checking form storeFulFilMap " + maxFulfilOrderNo);
            log.info("storeFulFillMap " + storeFulFillMap.keySet());

            log.info("<-----srcLocfulfiLoc----------->"+srcLocfulfiLoc+"<------------->");
            
            if (storeFulFillMap.get(new BigDecimal(maxFulfilOrderNo)) != null)
            {
                log.info("inside storeFulFillMap.get(maxFulfilOrderNo)!=null");
                maxFulfilOrderNo = maxFulfilOrderNo + 1;
                storeMapValue = storeFulFillMap.get(new BigDecimal(maxFulfilOrderNo));
                log.info("<------------------------ storeMapValue-------->"+storeMapValue+"<------------>");
            } 
            else if (storeMapValue != null  && storeMapValue.equals(srcLocfulfiLoc))
            {
                if (tempkey != maxFulfilOrderNo) {
                    maxFulfilOrderNo = maxFulfilOrderNo - 1;
                }
                log.info("dnt increament the maxfullfillOrdNo " + maxFulfilOrderNo);
            } else {
                log.info("srcLocfulfiLoc " + srcLocfulfiLoc + "is not exist");
                maxFulfilOrderNo = maxFulfilOrderNo + 1;
                log.info("After increamenting the maxFulfilOrderNo " + maxFulfilOrderNo);
            }

            log.info("srcLocfulfiLoc " + srcLocfulfiLoc);
            log.info("putting into storeMap " + maxFulfilOrderNo);
            storeFulFillMap.put(new BigDecimal(maxFulfilOrderNo), srcLocfulfiLoc);
            log.info("maxFulfilOrderNo while calling createOMSTempCOFONewObject for ST " + maxFulfilOrderNo);
            sohMap =
                    createOMSTempCOFONewObject(item, combinationId, omsCustOrdNo, lineNo, sourceLocId, fulFillLocType, availQty,
                                               maxFulfilOrderNo, srcLocType, fulfillLocId, virtualWH, sohMap);
            log.info("returned sohMap from ST " + sohMap.keySet());
            returnPartialResponse.setMaxFulfilOrderNo(maxFulfilOrderNo);
            returnPartialResponse.setOrderQty(orderQty);
            returnPartialResponse.setPendingQty(pendingQty);
            returnPartialResponse.setStoreFulFillMap(storeFulFillMap);
            returnPartialResponse.setSohMap(sohMap);


        } else {
            log.info("<----------------------Availability Quantity is less than Zero------------->");
            log.info("<-----------maxFulfilOrderNo-------->"+maxFulfilOrderNo);
            returnPartialResponse.setMaxFulfilOrderNo(maxFulfilOrderNo);
            log.info("Order Quantity----->"+orderQty+"<----------------");
            returnPartialResponse.setOrderQty(orderQty);
            log.info("<--------Pending Quantity-------->"+pendingQty);
            returnPartialResponse.setPendingQty(pendingQty);
            log.info("storeFulFillMap-------->"+storeFulFillMap);
            returnPartialResponse.setStoreFulFillMap(storeFulFillMap);
            log.info("sohMap---------->"+sohMap);
            returnPartialResponse.setSohMap(sohMap);
        }
        return returnPartialResponse;
    }

    public ReturnPartialResponseObject WHLocation(String item, BigDecimal sourceLocId, long availQty, long pendingQty,
                                                  BigDecimal omsCustOrdNo, long SOH, long orderQty,
                                                  int maxFulfilOrderNo, Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap,
                                                  BigDecimal lineNo, BigDecimal combinationId, String fulFillLocType,
                                                  String srcLocType, BigDecimal fulfillLocId, BigDecimal virtualWH,
                                                  BigDecimal tempOrdQty) throws SOAPException {

        ReturnPartialResponseObject returnPartialResponse = new ReturnPartialResponseObject();
        log.info("---------------inside WHLocation----------------- ");
        log.info("availQty " + availQty);
        log.info("orderQty " + orderQty);
        log.info("tempOrdQty-->" + tempOrdQty);
        log.info("SOH from WH " + SOH);
        if (SOH > 0) {
        log.info("Stock on hand in Ware house is greater than zero-------->");
            if (SOH >= orderQty) {
                log.info("SOH is greater than orderQty");
                availQty = orderQty;
                pendingQty = tempOrdQty.intValue() - availQty;
                log.info("availQty " + availQty);
                log.info("pendingQty " + pendingQty);
            } else if (orderQty >= SOH) {
                log.info("SOH less than the  orderQty");
                availQty = SOH;
                pendingQty = orderQty - availQty;
                log.info("availQty " + availQty);
                log.info("pendingQty " + pendingQty);
            }
        } else {
            log.info("Stock on hand in ware house is less than zero------------->");
            availQty = 0;
           pendingQty=orderQty;
            log.info("availQty------>" + availQty);
            log.info("pendingQty--------->"+pendingQty);
        }
        if (availQty > 0) {
            log.info("<----------------------Availability Quantity is Greater than Zero------------->");
            orderQty = pendingQty;
            log.info("orderQty " + orderQty);
            log.info("pendingQty " + pendingQty);
            log.info("maxFulfilOrderNo before calling createOMSTempCOFONewObject " + maxFulfilOrderNo);
            maxFulfilOrderNo = maxFulfilOrderNo + 1;
            log.info("maxFulfilOrderNo while calling createOMSTempCOFONewObject for WH " + maxFulfilOrderNo);
            sohMap =
                    createOMSTempCOFONewObject(item, combinationId, omsCustOrdNo, lineNo, sourceLocId, fulFillLocType, availQty,
                                               maxFulfilOrderNo, srcLocType, fulfillLocId, virtualWH, sohMap);
            log.info("returned sohMap from WH " + sohMap.keySet());
            returnPartialResponse.setMaxFulfilOrderNo(maxFulfilOrderNo);
            returnPartialResponse.setOrderQty(orderQty);
            returnPartialResponse.setPendingQty(pendingQty);
            returnPartialResponse.setSohMap(sohMap);
        } else {
            log.info("<----------------------Availability Quantity is less than Zero------------->");
            log.info("<---------------------maxFulfilOrderNo------------->"+maxFulfilOrderNo);
            returnPartialResponse.setMaxFulfilOrderNo(maxFulfilOrderNo);
            log.info("<--------------orderQty------------>"+orderQty);
            returnPartialResponse.setOrderQty(orderQty);
            log.info("<-------------pendingQty------------->"+pendingQty);
            returnPartialResponse.setPendingQty(pendingQty);
            log.info("<--------------sohMap------------->"+sohMap);
            returnPartialResponse.setSohMap(sohMap);
        }
        return returnPartialResponse;
    }
    public ReturnPartialResponseObject SULocation(String item,BigDecimal sourceLocId,long availQty,long pendingQty,BigDecimal omsCustOrdNo,long SOH,long orderQty,int maxFulfilOrderNo,
                           Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap,BigDecimal lineNo,BigDecimal combinationId,
                           String fulFillLocType,String srcLocType,BigDecimal fulfillLocId,BigDecimal virtualWH,BigDecimal tempOrdQty,String srcLocfulfiLoc,String storeMapValue,TreeMap<BigDecimal,String> storeFulFillMap) throws SOAPException 
    {
        
        ReturnPartialResponseObject returnPartialResponse=new ReturnPartialResponseObject();
        log.info("omsCustOrdNo "+omsCustOrdNo +" Item "+item +"sourceLocId"+sourceLocId +"SOH "+SOH);
        log.info("maxFulfilOrderNo in supplier scenario "+maxFulfilOrderNo);
        maxFulfilOrderNo=maxFulfilOrderNo+1;
        log.info("maxFulfilOrderNo while calling createOMSTempCOFONewObject for SU "+maxFulfilOrderNo);
        log.info("orderQty "+orderQty);
        availQty=orderQty;
        log.info("availQty "+availQty);
        sohMap=createOMSTempCOFONewObject(item,combinationId,omsCustOrdNo,lineNo,sourceLocId,fulFillLocType,availQty,maxFulfilOrderNo,srcLocType,fulfillLocId,virtualWH,sohMap);
        log.info("returned sohMap from SU "+sohMap.keySet());
        returnPartialResponse.setMaxFulfilOrderNo(maxFulfilOrderNo);
        returnPartialResponse.setOrderQty(0);
        returnPartialResponse.setPendingQty(pendingQty);
        returnPartialResponse.setSohMap(sohMap);
           
        return returnPartialResponse; 
            
    }
    public Map<BigDecimal, ArrayList<OmsTempCoFo>>  createOMSTempCOFONewObject(String item,BigDecimal combinationId,BigDecimal omsCustOrdNo,BigDecimal lineNo,BigDecimal sourceLocId,String fulFillLocType,long availQty,int maxFulfilOrderNo,String srcLocType,BigDecimal fulfillLocId,BigDecimal virtualWH,Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap) 
    {
       
        OmsTempCoFo omsTempCoFonew=new OmsTempCoFo();
        omsTempCoFonew.setSourceLocId(sourceLocId);
        omsTempCoFonew.setSourceLocationType(srcLocType);
        omsTempCoFonew.setFulfillLocId(fulfillLocId);
        omsTempCoFonew.setFulfillLocationType(fulFillLocType);
        omsTempCoFonew.setVirtualWH(virtualWH);
        omsTempCoFonew.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
        omsTempCoFonew.setOrderQty(new BigDecimal(availQty));
        omsTempCoFonew.setFoConfQty(new BigDecimal("W".equals(fulFillLocType) ? 0 : availQty));
        omsTempCoFonew.setItem(item);
        omsTempCoFonew.setLineNo(lineNo);
        omsTempCoFonew.setCombinationId(combinationId);
        omsTempCoFonew.setOmsCustOrdNo(omsCustOrdNo);
        log.info("checking SOHMAP for maxFulfilOrderNo for maxFulfilOrderNo "+maxFulfilOrderNo);
        log.info("existing keyvalues "+sohMap.keySet());
        if (sohMap.get(new BigDecimal(maxFulfilOrderNo)) == null || sohMap.get(new BigDecimal(maxFulfilOrderNo)).size() == 0)
        {
            log.info("inside sohMap.get(new BigDecimal(maxFulfilOrderNo)) == null");
            ArrayList<OmsTempCoFo> omstempList = new ArrayList<OmsTempCoFo>();
            omstempList.add(omsTempCoFonew);
            sohMap.put(new BigDecimal(maxFulfilOrderNo),omstempList);
        }
        else
        {
            log.info("inside else");
            ArrayList<OmsTempCoFo> existingList = sohMap.get(new BigDecimal(maxFulfilOrderNo));
            existingList.add(omsTempCoFonew);
            sohMap.put(new BigDecimal(maxFulfilOrderNo), existingList);
        }
      
        log.info("Returning sohMap.keySet() "+sohMap.keySet());
        return  sohMap;
    }
}
