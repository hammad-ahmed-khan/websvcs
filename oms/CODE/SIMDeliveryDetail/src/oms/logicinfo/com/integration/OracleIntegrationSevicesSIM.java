package oms.logicinfo.com.integration;


import com.oracle.retail.integration.base.bo.invavailcoldesc.v1.InvAvailColDesc;
import com.oracle.retail.integration.base.bo.invavailcrivo.v1.InvAvailCriVo;
import com.oracle.retail.integration.base.bo.invavailcrivo.v1.InvLocation;
import com.oracle.retail.integration.base.bo.invavaildesc.v1.InvAvailDesc;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.stradjref.v1.StrAdjRef;
import com.oracle.retail.integration.base.bo.strinvcoldesc.v1.StrInvColDesc;
import com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvCriVo;
import com.oracle.retail.integration.base.bo.strinvdesc.v1.StrInvDesc;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvItmMod;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvModVo;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfItm;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;
import com.oracle.retail.integration.base.bo.ststsfreqmodvo.v1.StsTsfReqItmMod;
import com.oracle.retail.integration.base.bo.ststsfreqmodvo.v1.StsTsfReqModVo;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentService;
import com.oracle.retail.sim.integration.services.storeinventoryservice.v1.StoreInventoryPortType;
import com.oracle.retail.sim.integration.services.storeinventoryservice.v1.StoreInventoryService;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferPortType;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferService;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;


public class OracleIntegrationSevicesSIM {
    private final static Logger log = Logger.getLogger(OracleIntegrationSevicesSIM.class.getName());
    String errorReason = null;
    
    public OracleIntegrationSevicesSIM() {
        super();
    }

    /**
     *
     * @param item
     * @param pLocationID
     * @return
     * @throws SOAPException
     */
    public BigDecimal getSOHByItemLocation(String item, BigDecimal pLocationID) throws SOAPException {
        try {
            StoreInventoryService storeInventoryService = new StoreInventoryService();
            StoreInventoryPortType storeInventoryPortType = storeInventoryService.getStoreInventoryPort();
            StrInvCriVo strInvCriVo = new StrInvCriVo();
            strInvCriVo.getItemIdCol().add(item);
            strInvCriVo.getStoreIdCol().add(pLocationID.longValue());

            strInvCriVo.setUomType(strInvCriVo.getUomType().fromValue("STANDARD"));
            StrInvColDesc strInvColDesc = storeInventoryPortType.lookupInventoryInStore(strInvCriVo);
            return strInvColDesc.getStrInvDesc().get(0).getAvailableQty();
        } 
        catch (com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException iawsfe)
        {
            errorReason =  iawsfe.getMessage() + " " + " Error Reason :" + iawsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException iswsfe) {
            errorReason = iswsfe.getMessage() + " " + " Error Reason :" + iswsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException vwsfe) {
            errorReason = vwsfe.getMessage() + " " + " Error Reason :" +vwsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } 
        catch(IndexOutOfBoundsException in) 
        {
            return BigDecimal.ZERO;
        }
        catch (Exception e) {
            log.error(" --> Exception ST: " + e);
          
            throw new SOAPException(e.getMessage());
        }
    }

    /**
     *
     * @param item
     * @param pLocationID
     * @param pReasonCode
     * @param pQuantity
     * @return adjustmentId (from SIM)
     * @throws SOAPException
     */
    public long adjustInventoryByItemLocation(String item, BigDecimal pLocationID, BigDecimal pReasonCode,
                                              BigDecimal pQuantity, String sparePartsReqId) throws SOAPException {

        try {
            InventoryAdjustmentService inventoryAdjustmentService = new InventoryAdjustmentService();
            InventoryAdjustmentPortType inventoryAdjustmentPortType =
                inventoryAdjustmentService.getInventoryAdjustmentPort();

            StrAdjModVo strAdjModVo = new StrAdjModVo();
            StrAdjItmMod strAdjItemMod = new StrAdjItmMod();

            strAdjItemMod.setItemId(item);
            strAdjItemMod.setReasonId(pReasonCode.longValue());
            strAdjItemMod.setQuantity(pQuantity);
            strAdjItemMod.setCaseSize(new BigDecimal(1));

            strAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
            strAdjModVo.setStoreId(pLocationID.longValue());
            strAdjModVo.setComments("Spare Parts Request Id "+ sparePartsReqId);

            StrAdjRef strAdjRef = inventoryAdjustmentPortType.saveAndConfirmInventoryAdjustment(strAdjModVo);
            return strAdjRef.getAdjustmentId();
        } catch (com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException iawsfe) {   
            errorReason = iawsfe.getMessage() + " " + " Error Reason :" + iawsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException iswsfe) {
            errorReason = iswsfe.getMessage() + " " + " Error Reason :" + iswsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException vwsfe) {
            errorReason = vwsfe.getMessage() + " " + " Error Reason :" +vwsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (Exception e) {
            log.error(" --> Exception ST: " + e);
            throw new SOAPException(e.getMessage());
        }
    }


    /**
     *
     * @param item
     * @param pFromLocationID
     * @param pToLocationID
     * @param pQuantity
     * @return transferId (generated from SIM)
     * @throws SOAPException
     */
    public long saveTransferRequestStore2Store(String item, BigDecimal pFromLocationID, BigDecimal pToLocationID,
                                               BigDecimal pQuantity, String sparePartsReqId) throws SOAPException {
        try {
            StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
            StoreToStoreTransferPortType store2storeTransferPortType =
                store2storeTransferService.getStoreToStoreTransferPort();
            StsTsfReqModVo stsTsfReqModVo = new StsTsfReqModVo();
            StsTsfReqItmMod stsTsfReqItmMod = new StsTsfReqItmMod();

            stsTsfReqItmMod.setItemId(item);
            stsTsfReqItmMod.setRequestedQuantity(pQuantity);
            stsTsfReqItmMod.setCaseSize(new BigDecimal(1));
            
            stsTsfReqModVo.getStsTsfReqItmMod().add(stsTsfReqItmMod);
            stsTsfReqModVo.setSendingStoreId(pFromLocationID.longValue());
            stsTsfReqModVo.setReceivingStoreId(pToLocationID.longValue());
            stsTsfReqModVo.setComments("Spare Parts Req Id "+ sparePartsReqId);

            StsTsfRef stsTsfRef = store2storeTransferPortType.saveTransferRequest(stsTsfReqModVo);

            return stsTsfRef.getTransferId();

        } catch (com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException iawsfe) {
            errorReason =  iawsfe.getMessage() + " " + " Error Reason :" + iawsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException iswsfe) {
            errorReason = iswsfe.getMessage() + " " + " Error Reason :" + iswsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException vwsfe) {
            errorReason = vwsfe.getMessage() + " " + " Error Reason :" +vwsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (Exception e) {
            log.error(" --> Exception ST: " + e);
            throw new SOAPException(e.getMessage());
        }
    }


    /**
     *
     * @param storeId
     * @param transferId
     * @return
     * @throws SOAPException
     */
    public long cancelTransferRequestStore2Store(BigDecimal storeId, BigDecimal transferId) throws SOAPException {
        try {
            StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
            StoreToStoreTransferPortType store2storeTransferPortType =
                store2storeTransferService.getStoreToStoreTransferPort();

            StsTsfRef stsTsfRef = new StsTsfRef();
            stsTsfRef.setStoreId(storeId.longValue());
            stsTsfRef.setTransferId(transferId.longValue());

            InvocationSuccess invocationSuccess = store2storeTransferPortType.cancelTransfer(stsTsfRef);
            log.info("invocationSuccess "+invocationSuccess.getSuccessMessage());
            //InvocationSuccess invocationSuccess = store2storeTransferPortType.cancelTransferSubmission(stsTsfRef);

            return stsTsfRef.getTransferId();

        } catch (com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException iawsfe) {
            errorReason = iawsfe.getMessage() + " " + " Error Reason :" + iawsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException iswsfe) {
            errorReason = iswsfe.getMessage() + " " + " Error Reason :" + iswsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException vwsfe) {
            errorReason = vwsfe.getMessage() + " " + " Error Reason :" +vwsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (Exception e) {
            log.error(" --> Exception ST: " + e);
            throw new SOAPException(e.getMessage());
        }
    }


    /**
     *
     * @param storeIds
     * @param itemId
     * @return
     * @throws SOAPException
     */
    public BigDecimal getStoreSOHForItem(List<BigDecimal> storeIds, String itemId) throws SOAPException {
        BigDecimal sohAtStore = new BigDecimal(0);
        try {

            StoreInventoryService storeInventoryService = new StoreInventoryService();
            StoreInventoryPortType storeInventoryPortType = storeInventoryService.getStoreInventoryPort();

            StrInvCriVo strInvCriVo = new StrInvCriVo();
            for (BigDecimal storeId : storeIds) {
                strInvCriVo.getStoreIdCol().add(storeId.longValue());
                strInvCriVo.getItemIdCol().add(itemId);
            }

            log.info("   ***&&& Store Object Created ");

            InvAvailCriVo invAvailCriVo = new InvAvailCriVo();
            InvLocation theLocation = new InvLocation();

            for (BigDecimal storeId : storeIds) {
                theLocation.setLocation(storeId.longValue());
                log.info("Location Created " + storeId.longValue());
                invAvailCriVo.getInvLocation().add(theLocation);
                invAvailCriVo.getItems().add(itemId);
            }

            log.info("Inv Available Obj Created");

            StrInvColDesc strInvColDesc = storeInventoryPortType.lookupInventoryInStore(strInvCriVo);
            log.info("Store SIM Call Executed. Data Coll Size : " + strInvColDesc.getCollectionSize());

            InvAvailColDesc invAvailColDesc = storeInventoryPortType.lookupAvailableInventory(invAvailCriVo);
            log.info("Inv Available SIM Call Executed. Data Coll Size : " + invAvailColDesc.getCollectionSize());

            List<StrInvDesc> theList = strInvColDesc.getStrInvDesc();
            for (StrInvDesc strInvDesc : theList) {
                /* if(strInvDesc.getStoreId()==storeId.longValue()){ */
                sohAtStore = sohAtStore.add(strInvDesc.getAvailableQty());

                /*                     break;
                } */
            }

            log.info(" SOH from Store Service :" + sohAtStore.toString());

            List<InvAvailDesc> theAvailList = invAvailColDesc.getInvAvailDesc();
            for (InvAvailDesc invAvailDesc : theAvailList) {
                //                if(invAvailDesc.getLocation()==storeId.longValue()){
                sohAtStore = sohAtStore.add(invAvailDesc.getAvailableQty());
                //                    break;
                //              }
            }
            log.info(" SOH from Inv Available Service :" + sohAtStore.toString());
            return sohAtStore;
        } catch (com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException iawsfe) {
            errorReason = iawsfe.getMessage() + " " + " Error Reason :" + iawsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException iswsfe) {
            errorReason = iswsfe.getFaultInfo().getErrorDescription();
            errorReason = iswsfe.getMessage() + " " + " Error Reason :" + iswsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException vwsfe) {
            errorReason = vwsfe.getFaultInfo().getErrorDescription();
            errorReason = vwsfe.getMessage() + " " + " Error Reason :" +vwsfe.getFaultInfo().getErrorDescription();
            log.error(" --> " + errorReason);
            throw new SOAPException(errorReason);
        } catch (Exception e) {
            log.error(" --> Exception ST: " + e);
            throw new SOAPException(e.getMessage());
        }
    }
    public void requestTransferGetLineIdReqQty(BigDecimal transferId,BigDecimal storeId,String confirmation,String acceptance) throws SOAPException
        {
            try 
            {
                List<StsTsfDesc> stsTsfRefList=new ArrayList();
                StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
                StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
              
                StsTsfRef stsTsfRef = new StsTsfRef();
                stsTsfRef.setTransferId(transferId.longValue());
                stsTsfRef.setStoreId(storeId.longValue());
                
               
                //get line id and requested qty from response of requestTransfer method by passing transfer Id and reuestedStoreId
                log.info("calling requestTransfer method");
                log.info("Transfer Id "+transferId.longValue());
                log.info("Store Id "+storeId.longValue());
                InvocationSuccess invocationSuccess = store2storeTransferPortType.requestTransfer(stsTsfRef);
                String msg=invocationSuccess.getSuccessMessage();
                log.info("invocationSuccess.getSuccessMessage() "+msg);
                if(msg.equals("Service Operation Complete"))
                {
                    log.info("calling readTransferDetail");
                    StsTsfDesc stsTsfDesc=store2storeTransferPortType.readTransferDetail(stsTsfRef);
                    StsTsfItm stsTsfItm=new StsTsfItm();
                    StsTsfApvModVo stsTsfApvModVo=new StsTsfApvModVo();
                    stsTsfApvModVo.setTransferId(stsTsfDesc.getTransferId());
                    List<StsTsfApvItmMod> stsTsfApvItmLsit= stsTsfApvModVo.getStsTsfApvItmMod();
                    StsTsfApvItmMod stsTsfApvItmMod=new StsTsfApvItmMod();
                    
                    stsTsfApvItmMod.setLineId(stsTsfDesc.getStsTsfItm().get(0).getLineId());
                    stsTsfApvItmMod.setApprovedQuantity(stsTsfDesc.getStsTsfItm().get(0).getRequestedQuantity());
                    stsTsfApvItmLsit.add(stsTsfApvItmMod);
                    stsTsfApvModVo.getStsTsfApvItmMod().add(stsTsfApvItmMod);
                   
                    //======================================================================================================================
                    log.info("Transfer Id "+stsTsfDesc.getTransferId());
                    log.info("Line Id "+stsTsfDesc.getStsTsfItm().get(0).getLineId());
                    log.info("Requested Qty "+stsTsfDesc.getStsTsfItm().get(0).getRequestedQuantity());
                    if(confirmation.equals("Y") && acceptance.equals("Y"))
                    {    
                        log.info("confirmation and acceptance are equal");
                        log.info("calling savePendingTransferRequest");
                        InvocationSuccess invocationSuccess1=store2storeTransferPortType.savePendingTransferRequest(stsTsfApvModVo);
                        String msg1=invocationSuccess1.getSuccessMessage();
                        log.info("invocationSuccess1.getSuccessMessage() in savePendingTransferRequest method"+msg1);
                        if(msg1.equals("Service Operation Complete"))
                        {
                            
                            log.info("calling approveTransfer");
                            log.info("stsTsfRef.getTransferId()" +stsTsfRef.getTransferId());
                            log.info("stsTsfRef.getStoreId() "+stsTsfRef.getStoreId());
                            InvocationSuccess invocationSuccess2=store2storeTransferPortType.approveTransfer(stsTsfRef);
                            String msg3=invocationSuccess2.getSuccessMessage();
                            log.info("invocationSuccess2.getSuccessMessage() in approveTransfer method "+msg3);
                            if(msg3.equals("Service Operation Complete"))
                            {
                                log.info("Message Success Transfer approved");
                            }
                            else
                            {
                               log.info("transfer not approved");
                            }
                       }
                        else
                        {
                           log.info("savePendingTransferRequest not unsuccess");
                        }
                    }        
                       
                    }
                    else
                    {
                      log.info("requestTransfer not success");    
                    }
                }   
               // return stsTsfRefList; 

            
            catch(Exception e) 
            {
                log.error(" --> Exception ST: " + e);
                throw new SOAPException(e.getMessage());
            }
        }

}
