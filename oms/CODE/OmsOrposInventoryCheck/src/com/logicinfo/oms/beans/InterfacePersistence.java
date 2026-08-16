package com.logicinfo.oms.beans;


import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.model.CustOrdItmDesc;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.strinvcoldesc.v1.StrInvColDesc;
import com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvCriVo;
import com.oracle.retail.sim.integration.services.storeinventoryservice.v1.StoreInventoryPortType;
import com.oracle.retail.sim.integration.services.storeinventoryservice.v1.StoreInventoryService;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;


public class InterfacePersistence {
    public InterfacePersistence() {
        super();
    }
    private final static Logger log = Logger.getLogger(InterfacePersistence.class.getName());
    public long callSIMStoreInventory(String item,BigDecimal nextLoc) throws   com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
                                                               com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
                                                               com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
                                                               com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException
    {
        long SOH=0;
        try
        {    
        StoreInventoryService storeInventoryService = new StoreInventoryService();
        StoreInventoryPortType storeInventoryPortType = storeInventoryService.getStoreInventoryPort();
        StrInvCriVo strInvCriVo = new StrInvCriVo();
        strInvCriVo.getItemIdCol().add(item);
        strInvCriVo.getStoreIdCol().add(nextLoc.longValue());
        strInvCriVo.setUomType(strInvCriVo.getUomType().fromValue("STANDARD"));
        StrInvColDesc strInvColDesc = storeInventoryPortType.lookupInventoryInStore(strInvCriVo);
        SOH = strInvColDesc.getStrInvDesc().get(0).getAvailableQty().longValue();
        if(SOH<0) SOH=0;
        }
        catch(IndexOutOfBoundsException e) 
        {
             SOH=0;                         
        }
        
      return SOH;
    }
    
    
    public long findWHInventory(String item,BigDecimal location) throws SOAPException {
        long SOH=0;
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        List<Object[]> tempWhObject =
            session.getWhFindPhysicalWH(location);
        BigDecimal physicalWH = BigDecimal.ZERO;
        BigDecimal channelId = BigDecimal.ZERO;
        for (Object[] result : tempWhObject) {

            physicalWH = new BigDecimal(result[0].toString());
            channelId = new BigDecimal(result[1].toString());
            log.info("WH=" + result[0] + "channel id=" + result[1]);
        }

        List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);

        int i = 0;
        while (i < locList.size()) {
            log.info(locList.get(i));
            i++;
        }
        OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
        String applicationId ="ORPOS";
        SOH = omsUtilCommons.checkSOHForWH(item, locList, applicationId).longValue();
        return SOH;
    }
    
   
  
  
    public BackOrderResponse backOrder(List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList,
                                       CustOrdItmDesc custOrdItmDesc, long pendingQty) throws SOAPException {
       log.info("inside backOrder");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        OMSUtilCommons oMSUtilCommons=new OMSUtilCommons();
        int i = 0;
        Date fut_inv_av_date = null;
        BackOrderResponse backOrderResponse = new BackOrderResponse();
        int expectedQty=0;
        BigDecimal alloctedInventory =null;
        log.info("looping");
        for (OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail : omsFulfillMatrixExtDetailList)
        {
            if(omsFulfillMatrixExtDetail.getLocation().intValue()>0)
            {
            log.info("omsFulfillMatrixExtDetail.getLocation() is greater than O");
            String boIndicator="N";
            log.info("omsFulfillMatrixExtDetail.getLocation() "+omsFulfillMatrixExtDetail.getLocation());
            log.info("custOrdItmDesc.getItemId() "+custOrdItmDesc.getItemId());
            /* BigDecimal alloctedInventory =session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(),
                                                                custOrdItmDesc.getItemId());
            if (alloctedInventory == null) 
            {
                alloctedInventory = BigDecimal.ZERO;
            }
            log.info("alloctedInventory" + alloctedInventory); */

            FindNextFulfillLoc checkItemLocSOH = new FindNextFulfillLoc();
           
            BigDecimal  physicalWH=BigDecimal.ZERO;
            BigDecimal  channelId=BigDecimal.ZERO;
            if (omsFulfillMatrixExtDetail.getLocationType().equals("WH") )
            {
                
                List<Object[]> tempWhObject=   session.getWhFindPhysicalWH(omsFulfillMatrixExtDetail.getLocation());
                
               
                for(Object[] result:tempWhObject) 
                {
                    physicalWH=  new BigDecimal(result[0].toString());
                    //omsFulfillMatrixExtDetail.setLocation(physicalWH);
                    channelId=new BigDecimal(result[1].toString());
                     log.info("WH="+result[0]+"channel id="+result[1]);
                     
                }
               /*  if(physicalWH.intValue()>0 || physicalWH!=null)
                {
                alloctedInventory =session.getOmsBackOrderDtlFindAssignedInvOrders(physicalWH,custOrdItmDesc.getItemId());
                } */
                
            }
           
                alloctedInventory =session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(),custOrdItmDesc.getItemId());    
            
            if (alloctedInventory == null)
            {
               alloctedInventory = BigDecimal.ZERO;
            }
            log.info("alloctedInventory" + alloctedInventory);
            CustFutureInvPosition custFutureInvPosition = null;
            try
            {    
              log.info("Finding bo indicator for location"+omsFulfillMatrixExtDetail.getLocation());
                boIndicator=oMSUtilCommons.getBOIndicator(omsFulfillMatrixExtDetail.getLocation(), custOrdItmDesc.getItemId());
               
                log.info("fetched indicator value  from table is +++++++++++==="+boIndicator);
                if(boIndicator==null || boIndicator.isEmpty())
                {
                    log.info("boIndicator is null ,setting it to N");
                     boIndicator="N";    
                }
                 if(boIndicator.equals("Y"))
                {
                   log.info("inside indicator Y");
                   if(omsFulfillMatrixExtDetail.getLocationType().equals("WH")) 
                   {
                   log.info("physicalWH "+physicalWH);
              custFutureInvPosition=  checkItemLocSOH.findFutInvDateAndQty(custOrdItmDesc.getItemId(), physicalWH.longValue(),alloctedInventory,new BigDecimal(pendingQty),omsFulfillMatrixExtDetail.getLocationType(),channelId.intValue());

                   }else{
                    custFutureInvPosition=  checkItemLocSOH.findFutInvDateAndQty(custOrdItmDesc.getItemId(),omsFulfillMatrixExtDetail.getLocation().longValue(),alloctedInventory,new BigDecimal(pendingQty),omsFulfillMatrixExtDetail.getLocationType(),channelId.intValue());
                   }
                   log.info("custFutureInvPosition values fetched"+custFutureInvPosition.getExpectedDate()+"qty="+custFutureInvPosition.getExpectedQty());
                } 
                
            }
            catch(Exception e) 
            {
                boIndicator="N";     
            }
            
            try {

                
                if (custFutureInvPosition != null) {

                   
                   
                    log.info("Expected qty at store  " + omsFulfillMatrixExtDetail.getLocation() + " is " +
                             custFutureInvPosition.getExpectedQty() );
                    expectedQty=expectedQty+custFutureInvPosition.getExpectedQty().intValue() ;
                        log.info("expectedQty=" + expectedQty );
                       
                          

                        //    backOrderResponse.setFutInvAvlDate(expectedDate);
                            backOrderResponse.setFutureAvlQty(expectedQty);


                }else{
                    log.info("inside else");
                    backOrderResponse.setFutureAvlQty(expectedQty);

                }

            } catch (Exception e) {
                backOrderResponse.setFutureAvlQty(expectedQty);
                log.error("Error in backorder"+e);
                
            }
        }
        }
        log.info("returning backorder"+backOrderResponse.getFutureAvlQty());
        return backOrderResponse;
    }

}
