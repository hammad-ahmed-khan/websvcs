package com.logicinfo.oms.util;


import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsRepublishData;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import org.datacontract.schemas._2004._07.extra_services.ArrayOfOrderDetailStatus;
import org.datacontract.schemas._2004._07.extra_services.OrderDetailStatus;
import org.datacontract.schemas._2004._07.extra_services.OrderStatus;

import retail.siebel.com.soaintegration.IStatus;
import retail.siebel.com.soaintegration.Status;


public class OmsStatusUpdateForReturnPickupCancellation {

    private final static Logger log = Logger.getLogger(OmsStatusUpdateForReturnPickupCancellation.class.getName());

    public OmsStatusUpdateForReturnPickupCancellation() {
        super();
    }

    public Boolean callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup(OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderObj) {
        log.info("------------------callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup------------");
        String valueOfResponse = null;
        Boolean statusFlag = Boolean.TRUE;
        log.info("Order Id "+omsOrderStatusUpdateHeaderObj.getOrderId()+" Application Id "+omsOrderStatusUpdateHeaderObj.getApplicationId());
        if (!"ORPOS".equalsIgnoreCase(omsOrderStatusUpdateHeaderObj.getApplicationId())) {
            OrderStatus orderHeaderStatusObj = new OrderStatus();
            OrderDetailStatus omsOrderDetailStatusObj = null;
            ArrayOfOrderDetailStatus arrayOrderDetailStatusObj = new ArrayOfOrderDetailStatus();
            log.info("omsOrderStatusUpdateHeaderObj.getEntityId()" + omsOrderStatusUpdateHeaderObj.getEntityId());
            orderHeaderStatusObj.setEnitityId(omsOrderStatusUpdateHeaderObj.getEntityId());
            log.info("omsOrderStatusUpdateHeaderObj.getApplicationId()" +
                     omsOrderStatusUpdateHeaderObj.getApplicationId());
            orderHeaderStatusObj.setApplicationId(omsOrderStatusUpdateHeaderObj.getApplicationId());
            log.info("omsOrderStatusUpdateHeaderObj.getOmsOrderId()" + omsOrderStatusUpdateHeaderObj.getOmsOrderId());
            orderHeaderStatusObj.setOmsOrderId(omsOrderStatusUpdateHeaderObj.getOmsOrderId());
            log.info("omsOrderStatusUpdateHeaderObj.getOrderId()" + omsOrderStatusUpdateHeaderObj.getOrderId());
            orderHeaderStatusObj.setOrderId(omsOrderStatusUpdateHeaderObj.getOrderId());
            log.info("omsOrderStatusUpdateHeaderObj.getDeliveryDate()" +
                     omsOrderStatusUpdateHeaderObj.getDeliveryDate());
            orderHeaderStatusObj.setDeliveryDate(omsOrderStatusUpdateHeaderObj.getDeliveryDate());
            log.info("omsOrderStatusUpdateHeaderObj.getUpdateDate()" + omsOrderStatusUpdateHeaderObj.getUpdateDate());
            orderHeaderStatusObj.setUpdateDate(omsOrderStatusUpdateHeaderObj.getUpdateDate());
            List<OmsOrderDetailStatus> omsOrderDetailStatusReturnList =
                omsOrderStatusUpdateHeaderObj.getOrderDetailStatuses().getOrderDetailStatus();
            log.info("Size is " + omsOrderDetailStatusReturnList.size());
            for (OmsOrderDetailStatus omsOrderDetailStatus : omsOrderDetailStatusReturnList) {
                omsOrderDetailStatusObj = new OrderDetailStatus();
                omsOrderDetailStatusObj.setOrderDetailId(omsOrderDetailStatus.getOrderDetailId());
                omsOrderDetailStatusObj.setProductSku(omsOrderDetailStatus.getProductSku());
                omsOrderDetailStatusObj.setQuantity(omsOrderDetailStatus.getQuantity());
                omsOrderDetailStatusObj.setSourceId(omsOrderDetailStatus.getSourceId());
                omsOrderDetailStatusObj.setSourceType(omsOrderDetailStatus.getSourceType());
                omsOrderDetailStatusObj.setFulfillId(omsOrderDetailStatus.getFulfillId());
                omsOrderDetailStatusObj.setFulfillType(omsOrderDetailStatus.getFulfillType());
                omsOrderDetailStatusObj.setEventId(omsOrderDetailStatus.getEventId());
                omsOrderDetailStatusObj.setEventComment(omsOrderDetailStatus.getEventComment());
                omsOrderDetailStatusObj.setEventReferenceId(omsOrderDetailStatus.getEventReferenceId());
                omsOrderDetailStatusObj.setUpdateDate(omsOrderDetailStatus.getUpdateDate());
                arrayOrderDetailStatusObj.getOrderDetailStatus().add(omsOrderDetailStatusObj);
            }
            orderHeaderStatusObj.setOrderDetailStatuses(arrayOrderDetailStatusObj);
            try {
                log.info(" --Creating the Order status Client Object------");
                Status omsOrderStatusUpdate = new Status();
                IStatus orderStatusBindingHttpStatusObj = omsOrderStatusUpdate.getBasicHttpBindingIStatus();
                log.info("omsOrderStatusUpdateHeaderObj.getOrderId()" + omsOrderStatusUpdateHeaderObj.getOrderId());
                valueOfResponse = orderStatusBindingHttpStatusObj.updateOrderStatus(orderHeaderStatusObj);

                if (null != valueOfResponse) {
                    log.info("---------------Response of Ws Call-----------" +
                             omsOrderStatusUpdateHeaderObj.getOmsOrderId() + "---" + valueOfResponse);
                    if ("FALSE".contains(valueOfResponse) &&
                        !"ORPOS".equals(omsOrderStatusUpdateHeaderObj.getApplicationId())) {
                        log.info("...........Response of the E-commerce order response is fales while calling Order status update .......");
                        insertRecordIntoRepublishData(omsOrderStatusUpdateHeaderObj);
                    }
                }
            } catch (Exception e) {
                log.info("...........Error occured while calling Sibel web services.......");
                try {
                    insertRecordIntoRepublishData(omsOrderStatusUpdateHeaderObj);
                } catch (Exception e1) {
                    log.info(".............Error occured while inserting into republish data table.............");
                }
                statusFlag = Boolean.FALSE;
            }
            log.info("-----------------------Response from ws call-------------------" + valueOfResponse);
        }
        return statusFlag;
    }


    public void insertRecordIntoRepublishData(OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderObj) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        OmsRepublishData omsRepublishData = new OmsRepublishData();
        omsRepublishData.setApplicationId(omsOrderStatusUpdateHeaderObj.getApplicationId());
        omsRepublishData.setErrorMsg("UNABLE TO CALL SEIBEL WEB SERVICE");
        omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
        omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
        omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
        omsRepublishData.setRepublishStatus("F");
        omsRepublishData.setTransactionKey(omsOrderStatusUpdateHeaderObj.getOrderId());
//        String webserviceName = session.getOmsWebserviceUriDetailFindByWebserviceName("SOA_CO_STATUS_UPDATE");
        String webserviceName = session.getOmsWebserviceUriDetailFindWebServiceId("SOA_CO_STATUS_UPDATE").toString();
        omsRepublishData.setWebServiceId(webserviceName);
        String xmlMessage = getomsOrderStatusMesage(omsOrderStatusUpdateHeaderObj);
        omsRepublishData.setXmlMsg(xmlMessage);
        try {
            session.persistOmsRepublishData(omsRepublishData);
            log.info("persisting in omsRepublish data for Seibel issue");
        } catch (Exception f) {
            log.error("persisting in omsRepublish data failed" + f.getMessage());
        }
    }

    private String getomsOrderStatusMesage(OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderObj) {
        String headerXMLPart = "<oms:UpdateOrderStatus>\n" +
            "<!--Optional:-->\n" +
            "<oms:orderStatus>\n" +
            "<ext:EnitityId>" + omsOrderStatusUpdateHeaderObj.getEntityId() + "</ext:EnitityId>\n" +
            "<ext:ApplicationId>" + omsOrderStatusUpdateHeaderObj.getApplicationId() + "</ext:ApplicationId>\n" +
            "<ext:OrderId>" + omsOrderStatusUpdateHeaderObj.getOrderId() + "</ext:OrderId>\n" +
            "<ext:OmsOrderId>" + omsOrderStatusUpdateHeaderObj.getOmsOrderId() + "</ext:OmsOrderId>\n" +
            "<ext:DeliveryDate>" + omsOrderStatusUpdateHeaderObj.getDeliveryDate() + "</ext:DeliveryDate>\n" +
            "<ext:UpdateDate>" + omsOrderStatusUpdateHeaderObj.getUpdateDate() + "</ext:UpdateDate>\n";
        List<OmsOrderDetailStatus> omsOrderDetailStatuslist =
            omsOrderStatusUpdateHeaderObj.getOrderDetailStatuses().getOrderDetailStatus();

        StringBuffer xmlDetailPart = new StringBuffer();
        for (OmsOrderDetailStatus omsOrderDetailStatus : omsOrderDetailStatuslist) {
            String tempXmlDetailPart = "<ext:OrderDetailStatuses>" + "<ext:OrderDetailStatus>\n" +
                "<ext:OrderDetailId>" + omsOrderDetailStatus.getOrderDetailId() + "</ext:OrderDetailId>\n" +
                "<ext:ProductSku>" + omsOrderDetailStatus.getProductSku() + "</ext:ProductSku>\n" +
                "<ext:Quantity>" + omsOrderDetailStatus.getQuantity() + "</ext:Quantity>\n" +
                "<ext:SourceType>" + omsOrderDetailStatus.getSourceType() + "</ext:SourceType>\n" +
                "<ext:SourceId>" + omsOrderDetailStatus.getSourceId() + "</ext:SourceId>\n" +
                "<ext:FulfillType>" + omsOrderDetailStatus.getFulfillType() + "</ext:FulfillType>\n" +
                "<ext:FulfillId>" + omsOrderDetailStatus.getFulfillId() + "</ext:FulfillId>\n" +
                "<ext:EventId>" + omsOrderDetailStatus.getEventId() + "</ext:EventId>\n" +
                "<ext:EventComment>" + omsOrderDetailStatus.getEventComment() + "</ext:EventComment>\n" +
                "<ext:EventReferenceId>" + omsOrderDetailStatus.getEventReferenceId() + "</ext:EventReferenceId>\n" +
                "<ext:UpdateDate>" + omsOrderDetailStatus.getUpdateDate() + "</ext:UpdateDate>\n" +
                "</ext:OrderDetailStatus>\n" +
                "</ext:OrderDetailStatuses>\n";
            xmlDetailPart.append(tempXmlDetailPart);
        }
        
      headerXMLPart = headerXMLPart.concat(xmlDetailPart.toString()).concat("</oms:orderStatus>").concat("</oms:UpdateOrderStatus>");
       String  updateOmsOrderStatus =    OMSConstants.OMS_ORDER_STATUS_HEADER.concat(headerXMLPart).concat("</soapenv:Body>").concat("</soapenv:Envelope>");
         log.info("XML Message----------"+updateOmsOrderStatus);
        return updateOmsOrderStatus;
    }
}
