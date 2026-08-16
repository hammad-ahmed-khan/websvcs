package com.logicinfo.oms.beans;


import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.postrndesc.v1.PosTrnDesc;
import com.oracle.retail.integration.base.bo.postrndesc.v1.PosTrnItm;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;


public class PosTransactionMsg {

    private Boolean pos_transacation_call;
    
    private int collectionSize;
    private PosTrnDesc posTransactionDesc;

    public void setPos_transacation_call(Boolean pos_transacation_call) {
        this.pos_transacation_call = pos_transacation_call;
    }

    public Boolean getPos_transacation_call() {
        return pos_transacation_call;
    }
    
   public String getPosTranscationXMLMessage(PosTrnDesc posTransactionDesc) {
        String headertag =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1\" xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/PosTrnColDesc/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1\">\n" +
            "<soapenv:Header/>\n" +
            "<soapenv:Body>" +
            "<v1:processPOSTransactions>" + "<v11:PosTrnColDesc>\n" +
            "<v12:PosTrnDesc>\n" +
            "<v12:store_id>" + posTransactionDesc.getStoreId() + "</v12:store_id>" +
            "<v12:transaction_id>" + posTransactionDesc.getTransactionId() +"</v12:transaction_id>" +
            "<v12:transaction_timestamp>" +posTransactionDesc.getTransactionTimestamp() + "</v12:transaction_timestamp>" +
            "<v12:cust_order_id>" + posTransactionDesc.getCustOrderId()+ "</v12:cust_order_id>" +
            "<v12:cust_order_comment>" + posTransactionDesc.getCustOrderComment()+"</v12:cust_order_comment>";

        List<PosTrnItm> posTrnItmsList = posTransactionDesc.getPosTrnItm();
        StringBuffer pos_transaction_item = new StringBuffer();
        String Itemtag =null;
        for (PosTrnItm PosTrnItm : posTrnItmsList) {
            Itemtag =
                "<v12:PosTrnItm>" + "<v12:item_id>" + PosTrnItm.getItemId() + "</v12:item_id>" + "<v12:quantity>" +
                PosTrnItm.getQuantity() + "</v12:quantity>" + "<v12:unit_of_measure>" + PosTrnItm.getUnitOfMeasure() +
                "</v12:unit_of_measure>" + "<v12:drop_ship>false</v12:drop_ship>" + "<v12:comments>" +
                PosTrnItm.getComments() + "</v12:comments>" + "<v12:reservation_type>"+PosTrnItm.getReservationType()+"</v12:reservation_type>" +
                "<v12:transaction_code>"+PosTrnItm.getTransactionCode()+"</v12:transaction_code>"+"</v12:PosTrnItm>";
            pos_transaction_item.append(Itemtag);
        }
      String endtag="</v12:PosTrnDesc>"+"<v11:collection_size>1</v11:collection_size>"+
                    "</v11:PosTrnColDesc>"+"</v1:processPOSTransactions>"+"</soapenv:Body>" +
                    "</soapenv:Envelope>";

        return headertag+Itemtag+endtag;
    }


    public void insertRecordIntoRepublishDataForPos_Transaction(String Pos_Transaction_Msg,
                                                                String customeOrderNo) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customeOrderNo, "S");
        OmsRepublishData omsRepublishData = new OmsRepublishData();
        omsRepublishData.setApplicationId(omsCustOrdHead.getApplicationId());
        omsRepublishData.setErrorMsg("UNABLE TO CALL SEIBEL WEB SERVICE");
        omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
        omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
        omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
        omsRepublishData.setRepublishStatus("F");
        omsRepublishData.setTransactionKey(customeOrderNo);
        BigDecimal webserviceName = session.getOmsWebserviceUriDetailFindWebServiceId("POS_TRANSACTION");
        omsRepublishData.setWebServiceId(webserviceName.intValue()+"");
        omsRepublishData.setXmlMsg(Pos_Transaction_Msg);
        try {
            session.persistOmsRepublishData(omsRepublishData);
        } catch (Exception f) {

        }
    }

    public void setPosTransactionDesc(PosTrnDesc posTransactionDesc) {
        this.posTransactionDesc = posTransactionDesc;
    }

    public PosTrnDesc getPosTransactionDesc() {
        return posTransactionDesc;
    }

    public void setCollectionSize(int collectionSize) {
        this.collectionSize = collectionSize;
    }

    public int getCollectionSize() {
        return collectionSize;
    }
}
