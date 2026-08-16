package com.logicinfo.oms.beans;


import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.postrncoldesc.v1.PosTrnColDesc;
import com.oracle.retail.integration.base.bo.postrndesc.v1.PosTrnDesc;
import com.oracle.retail.integration.base.bo.postrndesc.v1.PosTrnItm;
import com.oracle.retail.integration.base.bo.postrndesc.v1.PosTrnItmTranCode;
import com.oracle.retail.integration.base.bo.postrndesc.v1.PosTrnOrdResvType;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.POSTransactionPortType;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.POSTransactionService;
import com.oracle.retail.sim.integration.services.postransactionservice.v1.ValidationWSFaultException;

import java.math.BigDecimal;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class POSTransactionBean {
    public POSTransactionBean() {
        super();
    }

    private final static Logger log = Logger.getLogger(com.logicinfo.oms.beans.CustOrdCreateBean.class.getName());
    /*changed for bug Id 2532

    */

    public PosTransactionMsg pickFromPos(String customerOrderNo, String transactionNo,
                                         Map<String, BigDecimal> pospickUpItm) throws SOAPException {
        PosTransactionMsg posTransactionMsg = new PosTransactionMsg();
        PosTrnColDesc posTrnColDesc = new PosTrnColDesc();
        Boolean retryFlag = Boolean.TRUE;
        try {
            OMSUtilSessionEJB session = OMSUtil.doLookup();

            log.info("inside pickFromPos method" + transactionNo);
            log.info("customerOrderNo " + customerOrderNo);
            log.info("transactionNo " + transactionNo);
            OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderNo, "S");
            posTrnColDesc.setCollectionSize(pospickUpItm.size());
            PosTrnDesc posTrnDesc = new PosTrnDesc();
            log.info("transactionNo " + transactionNo + "setting the posTrnDesc objects ");
            posTrnDesc.setCustOrderId(customerOrderNo);
            log.info("transactionNo " + transactionNo + "setted customerOrderNo " + customerOrderNo);
            posTrnDesc.setTransactionId(transactionNo);
            log.info("transactionNo " + transactionNo + "setted transactionNo " + transactionNo);
            String storeId = transactionNo.substring(0, 5);
            log.info("transactionNo " + transactionNo + "store Id " + storeId);
            posTrnDesc.setStoreId(Long.parseLong(storeId));
            log.info("transactionNo " + transactionNo + "setted storeId " + storeId);
            posTrnDesc.setCustOrderComment("POS PickUp");
            log.info("transactionNo " + transactionNo + "setted customerOrderComment");
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = null;
            try {
                datatypeFactory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                log.warn(e.toString());
                throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
            }
            XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            posTrnDesc.setTransactionTimestamp(now);
            log.info("transactionNo " + transactionNo + "setted timeStamp " + now);

            for (String key : pospickUpItm.keySet()) {
                PosTrnItm posTrnItm = new PosTrnItm();
                String keyData[] = key.split(",");
                BigDecimal deliveredQty = pospickUpItm.get(key);
                BigDecimal fulFillOrderNo = new BigDecimal(keyData[0]);
                BigDecimal lineNo = new BigDecimal(keyData[1]);
                BigDecimal sourceLoc = new BigDecimal(keyData[2]);
                String sourcelocType = keyData[3];
                BigDecimal fulFillLoc = new BigDecimal(keyData[4]);
                String fulFillLocType = keyData[5];
                String item = keyData[6];
                log.info("transactionNo " + transactionNo + "lineNo " + lineNo);
                log.info("transactionNo " + transactionNo + "sourceLoc" + sourceLoc);
                log.info("transactionNo " + transactionNo + "sourceClocType " + sourcelocType);
                log.info("transactionNo " + transactionNo + "fulFillLoc " + fulFillLoc);
                log.info("transactionNo " + transactionNo + "fulFillLocType" + fulFillLocType);
                posTrnItm.setItemId(item);
                String itemDesc = session.getItemMasterFindItemDesc(item);
                log.info("transactionNo " + transactionNo + "itemDesc " + itemDesc);
                //posTrnItm.setComments(itemDesc);
                log.info("transactionNo " + transactionNo + "Setted Item " + item);
                posTrnItm.setQuantity(deliveredQty);
                log.info("transactionNo " + transactionNo + "setted delivered Qty " + deliveredQty);
                posTrnItm.setUnitOfMeasure("EA");
                log.info("setted unit of measure");
                posTrnItm.setDropShip(false);
                log.info("transactionNo " + transactionNo + "setted dropShip");
                posTrnItm.setFulfillOrderId(fulFillOrderNo.toString());
               
                log.info("transactionNo " + transactionNo + "Setted fulFillOrderNo " + fulFillOrderNo);
                posTrnItm.setReservationType(PosTrnOrdResvType.WEB_ORDER);
                log.info("transactionNo " + transactionNo + "setted reservationType " + PosTrnOrdResvType.WEB_ORDER);
                posTrnItm.setTransactionCode(PosTrnItmTranCode.ORDER_FULFILL);
                log.info("transactionNo " + transactionNo + "setted TransactionCode " +
                         PosTrnItmTranCode.ORDER_FULFILL);
                log.info("transactionNo " + transactionNo + "Adding posTrnItm to posTrnDesc");
                posTrnDesc.getPosTrnItm().add(posTrnItm);
                log.info("transactionNo " + transactionNo + "Added posTrnItm to posTrnDesc");
            }

            log.info("transactionNo " + transactionNo + "Adding posTrnDesc to posTrnColDesc");
            posTrnColDesc.getPosTrnDesc().add(posTrnDesc);
            log.info("transactionNo " + transactionNo + "Added posTrnDesc to posTrnColDesc");


            for ( int ws_call=0 ; ws_call <5 ;ws_call++){
            log.info(" ws_call"+ws_call+" Attempt Web service call started----");
            try {
                POSTransactionService pOSTransactionService = new POSTransactionService();
                POSTransactionPortType pOSTransactionPortType = pOSTransactionService.getPOSTransactionPort();
                log.info("transactionNo " + transactionNo +
                         "calling the processPOSTransactions for POS pickUp the collection size " +
                         posTrnColDesc.getCollectionSize());
                
                log.info(" Pos transaction web service call started.");
                InvocationSuccess invocationSuccess = pOSTransactionPortType.processPOSTransactions(posTrnColDesc);
                log.info("transactionNo " + transactionNo +
                         "After calling the processPOSTransactions method from POS pick " +
                         invocationSuccess.getSuccessMessage());
                log.info(" ws_call"+ws_call+"Web service call Attempet successfull----");
                    break;
            } catch (IllegalArgumentWSFaultException e) {
                log.info("transactionNo " + transactionNo + "inside IllegalArgumentWSFaultException for pos pick" +
                         e.getMessage());
                retryFlag=Boolean.FALSE;
            } catch (IllegalStateWSFaultException e) {
                log.info("transactionNo " + transactionNo + "inside IllegalStateWSFaultException for pos pick" +
                         e.getMessage());
                retryFlag=Boolean.FALSE;
            } catch (ValidationWSFaultException e) {
                log.info("transactionNo " + transactionNo + "inside ValidationWSFaultException for pos pick" +
                         e.getMessage());
                retryFlag=Boolean.FALSE;
            } catch (Exception e) {
                log.info("transactionNo " + transactionNo + "Exception occured in POS pickUp " + e.getMessage());
                retryFlag=Boolean.FALSE;
            
            }
            
        } // for loop checking the issues.
            if( Boolean.FALSE ==retryFlag){
              posTransactionMsg=  setpostransactionXMLRepublsihForPickup(posTransactionMsg, posTrnColDesc);
                posTransactionMsg.setPos_transacation_call(retryFlag);
            }else{
                posTransactionMsg.setPos_transacation_call(retryFlag);
                posTransactionMsg.setPosTransactionDesc(null);
            }
            
        } catch (Exception exceptione) {
           log.info("transactionNo " + transactionNo + "Exception occured in POS pickUp " + exceptione.getMessage());
        }

        return posTransactionMsg;
    } // end of the function.

    public PosTransactionMsg retunFromPOS(String customerOrderNo, String transactionNo,
                                          Map<String, BigDecimal> posReturnItm) {
        PosTransactionMsg posTransactionMsg = new PosTransactionMsg();
        PosTrnColDesc posTrnColDesc = new PosTrnColDesc(); 
        Boolean returnretryCount= Boolean.FALSE;
        try {
            OMSUtilSessionEJB session = OMSUtil.doLookup();
            log.info("transactionNo " + transactionNo + "inside retunFromPOS method for transactionNo" +
                     transactionNo);
            log.info("customerOrderNo " + customerOrderNo);
            log.info("transactionNo " + transactionNo);
            posTrnColDesc.setCollectionSize(posReturnItm.size());
            PosTrnDesc posTrnDesc = new PosTrnDesc();
            log.info("transactionNo " + transactionNo + "setting the posTrnDesc objects ");
            posTrnDesc.setCustOrderId(customerOrderNo);
            log.info("transactionNo " + transactionNo + "setted customerOrderNo " + customerOrderNo);
            posTrnDesc.setTransactionId(transactionNo);
            log.info("setted transactionNo " + transactionNo);
            String storeId = transactionNo.substring(0, 5);
            log.info("transactionNo " + transactionNo + "store Id " + storeId);
            posTrnDesc.setStoreId(Long.parseLong(storeId));
            log.info("transactionNo " + transactionNo + "setted storeId " + storeId);
            posTrnDesc.setCustOrderComment("POS Return");
            log.info("transactionNo " + transactionNo + "setted customerOrderComment");
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = null;
            try {
                datatypeFactory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                log.warn(e.toString());
                throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
            }
            XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            posTrnDesc.setTransactionTimestamp(now);
            log.info("transactionNo " + transactionNo + "setted timeStamp " + now);

            log.info("posReturnItm.keySet() " + posReturnItm.keySet());
            for (String key : posReturnItm.keySet()) {
                log.info("transactionNo " + transactionNo + "inside for for posReturnItm " + key);
                PosTrnItm posTrnItm = new PosTrnItm();
                BigDecimal returnQty = posReturnItm.get(key);
                String keyData[] = key.split(",");
                log.info("transactionNo " + transactionNo + "keyData.length " + keyData.length);
                BigDecimal lineNo = new BigDecimal(keyData[0]);
                log.info("transactionNo " + transactionNo + "lineNo " + lineNo);
                String item = keyData[1];
                posTrnItm.setItemId(item);
                log.info("transactionNo " + transactionNo + "Setted Item " + item);
                String itemDesc = session.getItemMasterFindItemDesc(item);
                log.info("transactionNo " + transactionNo + "itemDesc " + itemDesc);
              // posTrnItm.setComments(itemDesc);
                log.info("transactionNo " + transactionNo + "setted comments " + itemDesc);
                posTrnItm.setQuantity(returnQty);
                log.info("transactionNo " + transactionNo + "setted Return Qty " + returnQty);
                posTrnItm.setUnitOfMeasure("EA");
                log.info("transactionNo " + transactionNo + "setted unit of measure");
                posTrnItm.setDropShip(false);
                log.info("transactionNo " + transactionNo + "setted dropShip");
                posTrnItm.setReservationType(PosTrnOrdResvType.NO_VALUE);
                log.info("transactionNo " + transactionNo + "setted reservationType " + PosTrnOrdResvType.NO_VALUE);
                posTrnItm.setTransactionCode(PosTrnItmTranCode.RETURN);
                log.info("transactionNo " + transactionNo + "setted TransactionCode " + PosTrnItmTranCode.RETURN);
                log.info("transactionNo " + transactionNo + "Adding posTrnItm to posTrnDesc");
                posTrnDesc.getPosTrnItm().add(posTrnItm);
                log.info("transactionNo " + transactionNo + "Added posTrnItm to posTrnDesc");
            }

            log.info("transactionNo " + transactionNo + "Adding posTrnDesc to posTrnColDesc");
            posTrnColDesc.getPosTrnDesc().add(posTrnDesc);
            log.info("transactionNo " + transactionNo + "Added posTrnDesc to posTrnColDesc");
            log.info("transactionNo " + transactionNo +
                     "calling the processPOSTransactions for POS return the collection size " +
                     posTrnColDesc.getCollectionSize());


            for (int ws_call = 0; ws_call < 5; ws_call++) {
                try {
                    POSTransactionService pOSTransactionService = new POSTransactionService();
                    POSTransactionPortType pOSTransactionPortType = pOSTransactionService.getPOSTransactionPort();
                    InvocationSuccess invocationSuccess = pOSTransactionPortType.processPOSTransactions(posTrnColDesc);
                    log.info("transactionNo " + transactionNo +
                             "After calling the processPOSTransactions method from POS return " +
                             invocationSuccess.getSuccessMessage());
                    break;
                } catch (IllegalArgumentWSFaultException e) {
                    log.info("transactionNo " + transactionNo +
                             "inside IllegalArgumentWSFaultException for pos return" + e.getMessage());
                    returnretryCount = Boolean.FALSE;
                } catch (IllegalStateWSFaultException e) {
                    log.info("transactionNo " + transactionNo + "inside IllegalStateWSFaultException for pos return" +
                             e.getMessage());
                } catch (ValidationWSFaultException e) {
                    log.info("transactionNo " + transactionNo + "inside ValidationWSFaultException for pos return" +
                             e.getMessage());
                    returnretryCount = Boolean.FALSE;
                } catch (Exception e) {
                    log.info("transactionNo " + transactionNo + "Exception occured in POS return " + e.getMessage());
                    returnretryCount = Boolean.FALSE;
                }
            }
            
            if(returnretryCount==Boolean.FALSE){
              posTransactionMsg=  setpostransactionXMLRepublsihForPickup(posTransactionMsg,posTrnColDesc);
              posTransactionMsg.setPos_transacation_call(returnretryCount);
            }else {
                posTransactionMsg.setPos_transacation_call(returnretryCount);
                posTransactionMsg.setPosTransactionDesc(null);
            }
        }catch(Exception exceptionMsg){
            log.info("transactionNo " + transactionNo + "Exception occured in POS return " + exceptionMsg.getMessage());
        }
        
        
        return posTransactionMsg;
    } //end of retunFromPOS

    private PosTransactionMsg setpostransactionXMLRepublsihForPickup(PosTransactionMsg PosTransactionMsgForRepublish,
                                                                     PosTrnColDesc posTrnColDesc) {
        PosTrnDesc posTrnDescForRepublishHeader = new PosTrnDesc();
        PosTrnColDesc posTrnColDescRepublish =  new PosTrnColDesc();
        PosTrnItm posTrnItmForRepublishItemDetails = null;
        
        // Getting the header details and setting It
        PosTrnDesc posTransactionDescription = posTrnColDesc.getPosTrnDesc().get(0);
        posTrnColDescRepublish.setCollectionSize(posTrnColDesc.getCollectionSize());
        posTrnDescForRepublishHeader.setStoreId(posTransactionDescription.getStoreId());
        posTrnDescForRepublishHeader.setCustOrderId(posTransactionDescription.getCustOrderId());
        posTrnDescForRepublishHeader.setTransactionId(posTransactionDescription.getTransactionId());
        posTrnDescForRepublishHeader.setTransactionTimestamp(posTransactionDescription.getTransactionTimestamp());
        posTrnDescForRepublishHeader.setCustOrderComment(posTransactionDescription.getCustOrderComment());
        // Getting the item details and setting the 
        List<PosTrnItm> posTransactionItemList= posTransactionDescription.getPosTrnItm();
        for (PosTrnItm posTransactionItem : posTransactionItemList) {
            posTrnItmForRepublishItemDetails = new PosTrnItm();
            posTrnItmForRepublishItemDetails.setItemId(posTransactionItem.getItemId());
            posTrnItmForRepublishItemDetails.setQuantity(posTransactionItem.getQuantity());
            posTrnItmForRepublishItemDetails.setUnitOfMeasure(posTransactionItem.getUnitOfMeasure());
            posTrnItmForRepublishItemDetails.setDropShip(Boolean.FALSE);
            posTrnItmForRepublishItemDetails.setFulfillOrderId(posTransactionItem.getFulfillOrderId());
            posTrnItmForRepublishItemDetails.setComments(posTransactionItem.getComments());
            posTrnItmForRepublishItemDetails.setReservationType(posTransactionItem.getReservationType());
            posTrnItmForRepublishItemDetails.setTransactionCode(posTransactionItem.getTransactionCode());
            posTrnDescForRepublishHeader.getPosTrnItm().add(posTrnItmForRepublishItemDetails);
        }
    
        PosTransactionMsgForRepublish.setPosTransactionDesc(posTrnDescForRepublishHeader);
        PosTransactionMsgForRepublish.setCollectionSize(posTrnColDesc.getCollectionSize());
        return PosTransactionMsgForRepublish;
    }
    
} //end of class
