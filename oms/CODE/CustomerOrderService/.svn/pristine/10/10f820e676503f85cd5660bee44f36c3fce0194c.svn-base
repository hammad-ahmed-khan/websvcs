package com.logicinfo.oms.beans;


import com.logicinfo.oms.ejb.Addr;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoCancelHead;
import com.logicinfo.oms.ejb.OmsCoCancelItem;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdItemDisc;
import com.logicinfo.oms.ejb.OmsCustOrdTender;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItm;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItmRtn;
import com.logicinfo.oms.ejb.OmsOrposCustOrderHead;
import com.logicinfo.oms.ejb.OmsOrposDiscntLine;
import com.logicinfo.oms.ejb.OmsOrposPayment;
import com.logicinfo.oms.ejb.OmsOrposTaxLine;
import com.logicinfo.oms.ejb.OmsRmaReq;
import com.logicinfo.oms.ejb.OmsRmaReqItem;
import com.logicinfo.oms.ejb.OmsTaxDesc;
import com.logicinfo.oms.ejb.Tsfdetail;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.contactdesc.v1.ContactDesc;
import com.oracle.retail.integration.base.bo.contactdesc.v1.Emails;
import com.oracle.retail.integration.base.bo.contactdesc.v1.Phones;
import com.oracle.retail.integration.base.bo.customerdesc.v1.AddrBook;
import com.oracle.retail.integration.base.bo.customerdesc.v1.AddrBookEntry;
import com.oracle.retail.integration.base.bo.customerdesc.v1.CustomerDesc;
import com.oracle.retail.integration.base.bo.customerdesc.v1.CustomerType;
import com.oracle.retail.integration.base.bo.custordercoldesc.v1.CustOrderColDesc;
import com.oracle.retail.integration.base.bo.custordercrivo.v1.CustOrderCriVo;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.GiftReceiptAssigned;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.InitiateLocType;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.OrderStatus;
import com.oracle.retail.integration.base.bo.custordfulcoldesc.v1.CustOrdFulColDesc;
import com.oracle.retail.integration.base.bo.custordfuldesc.v1.BillingDestDtl;
import com.oracle.retail.integration.base.bo.custordfuldesc.v1.CustOrdFulDesc;
import com.oracle.retail.integration.base.bo.custordfuldesc.v1.DeliveryDestDtl;
import com.oracle.retail.integration.base.bo.custordfuldesc.v1.DeliveryType;
import com.oracle.retail.integration.base.bo.custordfuldesc.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.custorditmcoldesc.v1.CustOrdItmColDesc;
import com.oracle.retail.integration.base.bo.custorditmdesc.v1.CustOrdItmDesc;
import com.oracle.retail.integration.base.bo.custorditmdesc.v1.ItemType;
import com.oracle.retail.integration.base.bo.custorditmdesc.v1.Flag;
import com.oracle.retail.integration.base.bo.discntlinecoldesc.v1.DiscntLineColDesc;
import com.oracle.retail.integration.base.bo.discntlinedesc.v1.DiscntLineDesc;
import com.oracle.retail.integration.base.bo.emaildesc.v1.EmailDesc;
import com.oracle.retail.integration.base.bo.geoaddrdesc.v1.GeoAddrDesc;
import com.oracle.retail.integration.base.bo.localedesc.v1.LocaleDesc;
import com.oracle.retail.integration.base.bo.paymentcoldesc.v1.PaymentColDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.AuthorizationMethod;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CertificateType;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CheckTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CouponTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CouponType;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CreditDebitTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CreditFlag;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.EntryMethod;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.GiftCardTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentType;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PersonalIdSwipedFlag;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PurchaseOrdTender;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.State;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.StoreCreditTender;
import com.oracle.retail.integration.base.bo.phonedesc.v1.PhoneDesc;
import com.oracle.retail.integration.base.bo.phonedesc.v1.PhoneType;
import com.oracle.retail.integration.base.bo.taxlinecoldesc.v1.TaxLineColDesc;
import com.oracle.retail.integration.base.bo.taxlinedesc.v1.EnumTaxModScope;
import com.oracle.retail.integration.base.bo.taxlinedesc.v1.EnumTaxMode;
import com.oracle.retail.integration.base.bo.taxlinedesc.v1.TaxLineDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException;

import java.math.BigDecimal;

import java.math.RoundingMode;

import java.sql.SQLException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;


public class ResponseProcessing {
    public ResponseProcessing() {
        super();
    }

    private final static Logger log = Logger.getLogger(ResponseProcessing.class.getName());
    BigDecimal omsCustOrdNo;
    BigDecimal omsOrposCustOrderId;
    String currencyCode = "";
    Boolean entryMethod = false;
    BigDecimal grandTotal = BigDecimal.ZERO;
    
    BigDecimal inclusiveTaxTotal = BigDecimal.ZERO;
    
    //BigDecimal cancelQty=BigDecimal.ZERO;md
    BigDecimal completedQty = BigDecimal.ZERO;

    public CustOrderColDesc createResponse(CustOrderCriVo input) throws SOAPException, IllegalArgumentWSFaultException,
                                                                        IllegalArgumentWSFaultException,
                                                                        IllegalStateWSFaultException,
                                                                        ValidationWSFaultException,
                                                                        EntityAlreadyExistsWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                        com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                        com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException

    {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        BigDecimal grandTotal = new BigDecimal(0);
        String custOrderId = input.getOrderCriteria().getCustomerOrderId();
        String subOrderId = null;
        String delimiter = "-";
        //Parsing OrderId
        try {
            if (custOrderId.contains("-")) {
                String[] orderId = custOrderId.split(delimiter);
                //for(int i=0;i< orderId.length;i++){
                custOrderId = orderId[0];
                subOrderId = orderId[1];
                log.info("custOrderId-->" + custOrderId);
                log.info("subOrderId-->" + subOrderId);
                while ((subOrderId.length() > 1) && (subOrderId.charAt(0) == '0')) {
                    subOrderId = subOrderId.substring(1);
                }
                log.info("After deleting the leading zeros " + subOrderId);
                //   }
                try {
                    omsCustOrdNo = session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(custOrderId, subOrderId);
                    OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                    log.info("omsCustOrdHead.getStatus() " + omsCustOrdHead.getStatus());
                    log.info("omsCustOrdHead.getOrdPaymentStatus() " + omsCustOrdHead.getOrdPaymentStatus());
                    if (omsCustOrdHead.getStatus().equals("S") && omsCustOrdHead.getOrdPaymentStatus().equals("S")) {
                        omsCustOrdNo = omsCustOrdHead.getOmsCustOrdNo();
                    } else {
                        CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
                        custOrderColDesc.setCollectionSize(0);
                        return custOrderColDesc;
                    }
                } catch (Exception e) {
                    log.info("No record exist in table for orderNo " + input.getOrderCriteria().getCustomerOrderId());
                    CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
                    custOrderColDesc.setCollectionSize(0);
                    return custOrderColDesc;
                }
            } else {
                // try
                //{
                log.info("input.getOrderCriteria().getCustomerOrderId() " +
                         input.getOrderCriteria().getCustomerOrderId());
                omsCustOrdNo =
                        session.getOmsCustOrdHeadFindByCustOrdNoAndSubCustOrdNo(input.getOrderCriteria().getCustomerOrderId(),
                                                                                "1");
                OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
                log.info("omsCustOrdHead.getStatus() " + omsCustOrdHead.getStatus());
                log.info("omsCustOrdHead.getOrdPaymentStatus() " + omsCustOrdHead.getOrdPaymentStatus());
                if (omsCustOrdHead.getStatus().equals("S") && omsCustOrdHead.getOrdPaymentStatus().equals("S")) {
                    omsCustOrdNo = omsCustOrdHead.getOmsCustOrdNo();
                } else {
                    CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
                    custOrderColDesc.setCollectionSize(0);
                    return custOrderColDesc;
                }
                //}
                /* catch(Exception e)
                {

                    //throw new  SOAPException(e);

                    log.info("No record exist in table for orderNo "+input.getOrderCriteria().getCustomerOrderId());
                    CustOrderColDesc custOrderColDesc=new CustOrderColDesc();
                     custOrderColDesc.setCollectionSize(0);
                    return custOrderColDesc;
                } */
            }
        } catch (Exception e) {

            //throw new  SOAPException(e);
            log.info("Exception : " + e.getMessage());
            log.info("No record exist in table for orderNo " + input.getOrderCriteria().getCustomerOrderId());
            CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
            custOrderColDesc.setCollectionSize(0);
            return custOrderColDesc;
        }


        log.info("OMS Cust Ord No:" + omsCustOrdNo);
        OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        log.info("Creating response");
        CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
        custOrderColDesc.setCollectionSize(1);
        CustOrderDesc custOrderDesc = new CustOrderDesc();
        if (input.getOrderCriteria().getCustomerOrderId().contains("-")) {
            log.info("custOrderId: " + custOrderId);
            if (omsCustOrdHead.getSubCustOrderNo().equals("1") && omsCustOrdHead.getSubCustOrderNo() != null) {
                log.info("CustOrderNo: " + omsCustOrdHead.getCustOrderNo() + "SubCustOrderNo: " +
                         omsCustOrdHead.getSubCustOrderNo());
                custOrderDesc.setCustomerOrderId(omsCustOrdHead.getCustOrderNo());
            } else if (omsCustOrdHead.getCustOrderNo() != null && omsCustOrdHead.getSubCustOrderNo().length() == 1) {
                log.info("CustOrderNo: " + omsCustOrdHead.getCustOrderNo() + "SubCustOrderNo: " +
                         omsCustOrdHead.getSubCustOrderNo());
                custOrderDesc.setCustomerOrderId(omsCustOrdHead.getCustOrderNo() + "-00" +
                                                 omsCustOrdHead.getSubCustOrderNo());
            } else if (omsCustOrdHead.getCustOrderNo() != null && omsCustOrdHead.getSubCustOrderNo().length() == 2) {
                log.info("CustOrderNo: " + omsCustOrdHead.getCustOrderNo() + "SubCustOrderNo: " +
                         omsCustOrdHead.getSubCustOrderNo());
                custOrderDesc.setCustomerOrderId(omsCustOrdHead.getCustOrderNo() + "-0" +
                                                 omsCustOrdHead.getSubCustOrderNo());
            } else if (omsCustOrdHead.getCustOrderNo() != null && omsCustOrdHead.getSubCustOrderNo().length() == 3) {
                log.info("CustOrderNo: " + omsCustOrdHead.getCustOrderNo() + "SubCustOrderNo: " +
                         omsCustOrdHead.getSubCustOrderNo());
                custOrderDesc.setCustomerOrderId(omsCustOrdHead.getCustOrderNo() + "-" +
                                                 omsCustOrdHead.getSubCustOrderNo());
            }

        } else if (!(input.getOrderCriteria().getCustomerOrderId().contains("-"))) {
            log.info("CustOrderNo: " + omsCustOrdHead.getCustOrderNo() + " SubCustOrderNo: 001");
            custOrderDesc.setCustomerOrderId(omsCustOrdHead.getCustOrderNo());
        }
        log.info("omsCustOrdHead.getCustId()" + omsCustOrdHead.getCustId());
        List<OmsCustOrdItem> omsCustOrdItemList1 = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
        currencyCode = omsCustOrdItemList1.get(0).getRetailCurr();
        /*   if (omsCustOrdItemList1.get(0).getRetailCurr() != null) {

            custOrderDesc.setCurrencyCode(omsCustOrdItemList1.get(0).getRetailCurr());
        }
        if (omsCustOrdItemList1.get(0).getComments() != null) {
            custOrderDesc.setOrderDesc(omsCustOrdItemList1.get(0).getComments());
        }
         log.info("Printing the values upto OrderDesc");
       List<OmsCoFulfillDetail> omsCoFulfillDetailList= session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);
       String ordStatus="NEW";
       for(OmsCoFulfillDetail omsCoFulfillDetail:omsCoFulfillDetailList) {
           if(omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc())) {
               ordStatus="FILLED";
               break;
           }

       }


       int i=0;
        for(OmsCoFulfillDetail omsCoFulfillDetail:omsCoFulfillDetailList) {
            if(omsCoFulfillDetail.getFulfillReqQty().equals(omsCoFulfillDetail.getFulfillCancelQty())==false) {
                i=1;

            }
        }
        if(i==0) ordStatus="CANCELED";
        BigDecimal orderedQty=BigDecimal.ZERO;
        BigDecimal cancellledQty=BigDecimal.ZERO;
        BigDecimal deliveredQty=BigDecimal.ZERO;
        for(OmsCoFulfillDetail omsCoFulfillDetail:omsCoFulfillDetailList) {
          orderedQty=orderedQty.add(omsCoFulfillDetail.getFulfillReqQty());
            cancellledQty=cancellledQty.add(omsCoFulfillDetail.getFulfillCancelQty());
            deliveredQty=deliveredQty.add(omsCoFulfillDetail.getFulfillDeliverQty());
        }
        if(ordStatus.equals(cancellledQty.add(deliveredQty))) ordStatus="COMPLETED";

            OrderStatus orderStatus = custOrderDesc.getOrderStatus();
            custOrderDesc.setOrderStatus(orderStatus.fromValue(ordStatus)); */


        List<BigDecimal> sourceLocList =
            session.getOmsCoFulfillDetailFindByOmsCustOrderNo(omsCustOrdHead.getOmsCustOrdNo());
        log.info("get SourceLoc: " + sourceLocList.size());
        List<OmsCustOrdItem> omsCustOrdItemList =
            session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
        if (sourceLocList.size() != 0) {
            BigDecimal sourceLoc = sourceLocList.get(0);
            List<BigDecimal> fulfillLoc = session.getOmsCoFulfillDetailFindBySourceLoc(sourceLoc);
            for (BigDecimal fulloc : fulfillLoc) {
                if (fulloc.equals(sourceLoc)) {
                    log.info("after checking fulfill location");
                    try {
                        for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
                            log.info("inside omsCustOrdItemList");
                            log.info("omsCustOrdItem omsCustOrdNo" + omsCustOrdItem.getOmsCustOrdNo());
                            if (omsCustOrdItem.getRetailCurr() != null) {
                                custOrderDesc.setCurrencyCode(omsCustOrdItem.getRetailCurr());
                                log.info("after omsCustOrdItemList" + omsCustOrdItem.getRetailCurr());
                            }
                            //OrderStatus
                            custOrderDesc.setOrderStatus(OrderStatus.PARTIAL);
                            log.info("preveous OrderStatus value" + OrderStatus.PARTIAL);
                            BigDecimal deliveredQty = omsCustOrdItem.getCumQtyDelivered();
                            log.info("deliveredQty=" + deliveredQty);
                            BigDecimal cancelledQty = omsCustOrdItem.getQtyCancelled();
                            log.info("cancelledQty=" + cancelledQty);
                            BigDecimal orderedQuantity = omsCustOrdItem.getQtyOrderedSuom();
                            log.info("orderedQuantity=" + orderedQuantity);
                            BigDecimal sumOfDeliveredAndCanceled = deliveredQty.add(cancelledQty);
                            log.info("sumOfDeliveredAndCanceled=" + sumOfDeliveredAndCanceled);
                            int result = sumOfDeliveredAndCanceled.compareTo(orderedQuantity);
                            if (result == 0) {
                                log.info("checking wheather deliveredQty is zero or not");
                                if (deliveredQty.intValue() == 0) {
                                    log.info("before OrderStatus should be CANCELED");
                                    custOrderDesc.setOrderStatus(OrderStatus.CANCELED);
                                } else {
                                    log.info("before OrderStatus should be COMPLETED");
                                    custOrderDesc.setOrderStatus(OrderStatus.COMPLETED);
                                }
                            } else {
                                log.info("checking wheather sumOfDeliveredAndCanceled is less than orderedQuantity");
                                if (sumOfDeliveredAndCanceled.intValue() < orderedQuantity.longValue()) {
                                    log.info("sumOfDeliveredAndCanceled is less than orderedQuantity");
                                    if (sumOfDeliveredAndCanceled.intValue() > 0) {
                                        log.info("before OrderStatus should be PARTIAL");
                                        custOrderDesc.setOrderStatus(OrderStatus.PARTIAL);
                                    } else {
                                        log.info("before OrderStatus should be FILLED");
                                        custOrderDesc.setOrderStatus(OrderStatus.FILLED);
                                        log.info("after OrderStatus is Filled" + OrderStatus.FILLED);
                                    }
                                }
                            }
                        } //end of for loop for omsCustOrdItems
                        log.info("before adding custOrderDesc into custOrderDescList");
                        //  custOrderDescList.add(custOrderDesc);
                    } catch (NumberFormatException e) {
                        log.info(e.getMessage());
                    }
                } else {
                    for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
                        if (omsCustOrdItem.getRetailCurr() != null) {
                            custOrderDesc.setCurrencyCode(omsCustOrdItem.getRetailCurr());
                        }
                        log.info("In else for OrderStatus NEW");
                        custOrderDesc.setOrderStatus(OrderStatus.NEW);
                        log.info("OrderStatus.NEW" + OrderStatus.NEW);
                        //   custOrderDescList.add(custOrderDesc);
                    }
                }
            }
        } else {
            for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
                if (omsCustOrdItem.getRetailCurr() != null) {
                    custOrderDesc.setCurrencyCode(omsCustOrdItem.getRetailCurr());
                }
                log.info("In else for OrderStatus NEW");
                custOrderDesc.setOrderStatus(OrderStatus.NEW);
                log.info("OrderStatus.NEW" + OrderStatus.NEW);
                //    custOrderDescList.add(custOrderDesc);
            }
        }
        InitiateLocType initiateLocType = custOrderDesc.getInitiateLocType();
        custOrderDesc.setInitiateLocType(initiateLocType.S);
        custOrderDesc.setInitiateLocId(omsCustOrdHead.getOrderRequestorId().longValue());
        List<Addr> addrList = session.getAddrFindByAddrKeyValue1(omsCustOrdHead.getOrderRequestorId().toString());
        //fetch from RMS table
        if (addrList.size() != 0) {
            custOrderDesc.setInitiateCountryCode(addrList.get(0).getCountryId());
        } else {
            custOrderDesc.setInitiateCountryCode("SA");
        }

        GiftReceiptAssigned giftReceiptAssigned = custOrderDesc.getGiftReceiptAssigned();
        custOrderDesc.setGiftReceiptAssigned(giftReceiptAssigned.N);

        if (omsCustOrdHead.getCreateDatetime() != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = null;
            try {
                datatypeFactory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException f) {
                throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(custOrderDesc.toString()));
            }
            XMLGregorianCalendar createTimestamp = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            Calendar createTimestampResp = Calendar.getInstance();
            createTimestampResp.setTimeInMillis(omsCustOrdHead.getCreateDatetime().getTime());
            createTimestamp.setMonth(createTimestampResp.get(Calendar.MONTH) + 1);
            createTimestamp.setYear(createTimestampResp.get(Calendar.YEAR));
            createTimestamp.setDay(createTimestampResp.get(Calendar.DAY_OF_MONTH));
            custOrderDesc.setCreateTimestamp(createTimestamp);
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = null;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            log.warn(e.toString());
            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
        }
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        custOrderDesc.setUpdateTimestamp(now);

        //custOrderDescList.add(custOrderDesc);

        custOrderDesc.setLocaleDesc(createResponseForLocale(omsCustOrdHead));
        custOrderDesc.setCustomerDesc(createResponseForCustomer(omsCustOrdHead));
        custOrderDesc.setCustOrdItmColDesc(createResponseForItem(custOrderDesc,omsCustOrdHead, custOrderId));
        custOrderDesc.setGrandTotal(grandTotal);
        log.info("Grand total is :" + grandTotal);
        /* Start Vat Changes @tsultana */
        List<OmsOrposCustOrderHead> omsOrposHead=session.getOmsOrposCustOrderHeadfindColumns(omsCustOrdHead.getCustOrderNo());
        if(omsCustOrdHead.getApplicationId().equals("ORPOS")){
            custOrderDesc.setTaxTotal(omsOrposHead.get(0).getTaxTotal());
            custOrderDesc.setInclusiveTaxTotal(omsOrposHead.get(0).getInclusiveTaxTotal());
        }
        else if(omsCustOrdHead.getApplicationId().equals("E-COMMERCE")){
            custOrderDesc.setTaxTotal(inclusiveTaxTotal);
            custOrderDesc.setInclusiveTaxTotal(inclusiveTaxTotal);
        }
        
        
        
        /* End Vat Changes @tsultana */
        custOrderDesc.setCustOrdFulColDesc(createResponseForCustOrdFul(custOrderId));
        //custOrderDesc.setCustOrdDelColDesc(createResponseForCustOrdDel());
        custOrderDesc.setPaymentColDesc(createResponseForPayment(input.getOrderCriteria().getCustomerOrderId()));
        custOrderColDesc.getCustOrderDesc().add(custOrderDesc);

        //     }
        return custOrderColDesc;
    }


    public LocaleDesc createResponseForLocale(OmsCustOrdHead omsCustOrdHead) throws SOAPException {
        log.info("inside locale");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        LocaleDesc localeDesc = new LocaleDesc();
        List<Addr> addrList = session.getAddrFindByAddrKeyValue1(omsCustOrdHead.getOrderRequestorId().toString());
        //fetch from RMS table

        //Fetch from RMS.STORE.COUNTRY_CODE for ORDER_REQUESTOR_ID
        if (addrList.size() != 0) {
            localeDesc.setCountry(checkNullValueForString(addrList.get(0).getCountryId()));
        } else {
            localeDesc.setCountry("SA");
        }
        localeDesc.setLang(omsCustOrdHead.getCustomerLang());
        return localeDesc;
    }

    public CustomerDesc createResponseForCustomer(OmsCustOrdHead omsCustOrdHead) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        try {
            CustomerDesc customerDesc = new CustomerDesc();

            CustomerType customerType = customerDesc.getCustomerType();
            log.info("before cust tye");
            /* if (omsCustOrdHead.getCustId() != null)
            {
               customerDesc.setCustomerId(omsCustOrdHead.getCustId());
            } */


            /*   if(omsCustOrdHead.getApplicationId().equals("ORPOS"))
            {
                try
                {
                  OmsOrposCustOrderHead omsOrposCustOrderHead=session.getOmsOrposCustOrderHeadfindBycustometOrderIdandstatus(omsCustOrdHead.getCustOrderNo(), "S");
                  OmsOrposCustomer omsOrposCustomer=session.getOmsOrposCustomerFindByOmsOrposCustOrdId(omsOrposCustOrderHead.getOmsOrposCustOrderId());
                    if(omsOrposCustomer!=null)
                    {
                    customerDesc.setCustomerId(omsCustOrdHead.getCustId());
                    customerDesc.setCustomerType(customerDesc.getCustomerType().fromValue(omsOrposCustomer.getCustomerType()));
                    customerDesc.setContactByMail(customerDesc.getContactByMail().fromValue(omsOrposCustomer.getContactByMail()));
                    customerDesc.setContactByEmail(customerDesc.getContactByEmail().fromValue(omsOrposCustomer.getContactByEmail()));
                    customerDesc.setContactByPhone(customerDesc.getContactByPhone().fromValue(omsOrposCustomer.getContactByPhone()));
                    }

                }

                catch(Exception e)
                {

                }
            }  */

            // customerDesc.setCustomerId(omsCustOrdHead.getCustId());
            customerDesc.setCustomerType(customerDesc.getCustomerType().REGULAR);
            customerDesc.setContactByMail(customerDesc.getContactByMail().fromValue("N"));
            customerDesc.setContactByEmail(customerDesc.getContactByEmail().fromValue("N"));
            customerDesc.setContactByPhone(customerDesc.getContactByPhone().fromValue("N"));

            customerDesc.setContactDesc(createResponseForContact(omsCustOrdHead));
            customerDesc.setAddrBook(createResponseForAddrBookEntry(omsCustOrdHead));
            customerDesc.setLocaleDesc(createResponseForLocale(omsCustOrdHead));
            return customerDesc;
        } catch (Exception e) {
            return null;
        }
    }

    public ContactDesc createResponseForContact(OmsCustOrdHead omsCustOrdHead) throws SOAPException {
        log.info("inside contact");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        //OmsCustOrdAddress omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
        ContactDesc contactDesc = new ContactDesc();
        try {
            if (omsCustOrdHead.getCustFirstName() != null) {
                log.info("---First Name--------" + omsCustOrdHead.getCustFirstName());
                contactDesc.setFirstName(checkNullValueForString(omsCustOrdHead.getCustFirstName()));
            }
            if (omsCustOrdHead.getCustLastName() != null) {
                log.info("---Last Name--------" + omsCustOrdHead.getCustLastName());
                contactDesc.setLastName(omsCustOrdHead.getCustLastName());
            }
            contactDesc.setPhones(createResponseForPhone(omsCustOrdHead));
            contactDesc.setEmails(createResponseForEmail());
        } catch (Exception e) {
            return null;
        }
        return contactDesc;
    }


    public Phones createResponseForPhone(OmsCustOrdHead omsCustOrdHead) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        Phones phones = new Phones();
        List<PhoneDesc> phoneDescList = phones.getPhoneDesc();
        PhoneDesc phoneDesc = new PhoneDesc();
        if (omsCustOrdHead.getCustPhoneNo() != null) {
            log.info("======Phone number=====" + omsCustOrdHead.getCustPhoneNo());
            phoneDesc.setPhoneNumber(omsCustOrdHead.getCustPhoneNo());
        }
        PhoneType phoneType = phoneDesc.getPhoneType();
        phoneDesc.setPhoneType(phoneType.fromValue("HOME"));
        phoneDesc.setPrimaryPhoneInd(checkNullValueForString("Y"));
        phoneDescList.add(phoneDesc);
        return phones;
    }


    public AddrBook createResponseForAddrBookEntry(OmsCustOrdHead omsCustOrdHead) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        AddrBook addrBook = new AddrBook();
        List<AddrBookEntry> addrBookEntryList = addrBook.getAddrBookEntry();
        AddrBookEntry addrBookEntry = new AddrBookEntry();
        addrBookEntry.setPrimaryAddrInd(addrBookEntry.getPrimaryAddrInd().fromValue("Y"));
        //     addrBookEntry.setGeoAddrDesc(createResponseForGeoAddr());
        addrBookEntry.setContactDesc(createResponseForContact(omsCustOrdHead));
        addrBookEntryList.add(addrBookEntry);
        //createResponseForGeoAddr();
        return addrBook;
    }

    public Emails createResponseForEmail() throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        Emails emails = new Emails();
        List<EmailDesc> emailDesclist = emails.getEmailDesc();
        EmailDesc emailDesc = new EmailDesc();
        emailDesc.setPrimaryEmailInd("N");
        emailDesc.setEmailAddress("");
        emailDesclist.add(emailDesc);
        return emails;
    }


    /*  public GeoAddrDesc createResponseForGeoAddr() throws SOAPException{
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        GeoAddrDesc geoAddrDesc=new GeoAddrDesc();
        OmsOrposGeoaddr omsOrposGeoaddr=session.getOmsOrposGeoaddrFindByCustOrdId(omsOrposCustOrderId);
        if(omsOrposGeoaddr.getAddressAlias()!=null){
        geoAddrDesc.setAddressAlias(checkNullValueForString(omsOrposGeoaddr.getAddressAlias()));
        }
        geoAddrDesc.setAddress1(checkNullValueForString(omsOrposGeoaddr.getAddress1()));
        if(omsOrposGeoaddr.getAddress2()!=null){
        geoAddrDesc.setAddress2(checkNullValueForString(omsOrposGeoaddr.getAddress2()));
        }
        if(omsOrposGeoaddr.getAddress3()!=null){
        geoAddrDesc.setAddress3(checkNullValueForString(omsOrposGeoaddr.getAddress3()));
        }
        if(omsOrposGeoaddr.getAddress4()!=null){
        geoAddrDesc.setAddress4(checkNullValueForString(omsOrposGeoaddr.getAddress4()));
        }
        if(omsOrposGeoaddr.getAddress5()!=null){
        geoAddrDesc.setAddress5(checkNullValueForString(omsOrposGeoaddr.getAddress5()));
        }
        if(omsOrposGeoaddr.getCity()!=null){
        geoAddrDesc.setCity(checkNullValueForString(omsOrposGeoaddr.getCity()));
        }
        if(omsOrposGeoaddr.getCounty()!=null){
        geoAddrDesc.setCounty(checkNullValueForString(omsOrposGeoaddr.getCounty()));
        }
        if(omsOrposGeoaddr.getCountryCode()!=null){
        geoAddrDesc.setCountryCode(checkNullValueForString(omsOrposGeoaddr.getCountryCode()));
        }
        if(omsOrposGeoaddr.getStateName()!=null){
        geoAddrDesc.setStateName(checkNullValueForString(omsOrposGeoaddr.getStateName()));
        }
        if(omsOrposGeoaddr.getStateCode()!=null){
        geoAddrDesc.setStateCode(checkNullValueForString(omsOrposGeoaddr.getStateCode()));
        }
        if(omsOrposGeoaddr.getCountryName()!=null){
        geoAddrDesc.setCountryName(checkNullValueForString(omsOrposGeoaddr.getCountryName()));
        }
        if(omsOrposGeoaddr.getPostalCode()!=null){
        geoAddrDesc.setPostalCode(checkNullValueForString(omsOrposGeoaddr.getPostalCode()));
        }
        if(omsOrposGeoaddr.getJurisdictionCode()!=null){
        geoAddrDesc.setJurisdictionCode(checkNullValueForString(omsOrposGeoaddr.getJurisdictionCode()));
        }
        return geoAddrDesc;
    } */


    public CustOrdItmColDesc createResponseForItem(CustOrderDesc custOrderDesc,OmsCustOrdHead omsCustOrdHead,
                                                   String custOrderId) throws SOAPException,
                                                                              IllegalArgumentWSFaultException,
                                                                              IllegalArgumentWSFaultException,
                                                                              IllegalStateWSFaultException,
                                                                              ValidationWSFaultException,
                                                                              EntityAlreadyExistsWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException

    {
        OmsRmaReqItem omsRmaReqItem = null;
        BigDecimal rmaQty1 = BigDecimal.ZERO;
        BigDecimal selectedQuantity = BigDecimal.ZERO;
        Integer CouponBalance = 0;
        boolean flag = false;
        boolean flag1 = false;
        //boolean flag2=false;
        int count = 0;
        String indictor = null;
        String returnStatus = null;
        log.info("inside item");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        CustOrdItmColDesc custOrdItmColDesc = new CustOrdItmColDesc();
        List<CustOrdItmDesc> custOrdItmDescList = custOrdItmColDesc.getCustOrdItmDesc();
        List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
        //WHCheck(custOrderId, omsCustOrdNo);


        for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
            boolean flag2 = false;

            //           log.info("Checking whether the item is a shipping charge item");
            //           String shipingChargeDept= session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
            //           log.info("shipingChargeDept "+shipingChargeDept);
            //           log.info("items.getItem() "+omsCustOrdItem.getItem());
            //           BigDecimal itemDept= session.getItemMasterFindDept(omsCustOrdItem.getItem());
            // log.info("itemDept "+itemDept);
            //           if(shipingChargeDept.equals(itemDept.toString())==true)
            //           {
            //                log.info("contains a shipping charge Item");
            //                continue;
            //           }

            CustOrdItmDesc custOrdItmDesc = new CustOrdItmDesc();
            custOrdItmDesc.setItemId(omsCustOrdItem.getItem());
            custOrdItmDesc.setLineItemNo(omsCustOrdItem.getLineNo().intValue());
            custOrdItmDesc.setCapturedLineItemNo(omsCustOrdItem.getLineNo().intValue());
            custOrdItmDesc.setItemUpc(omsCustOrdItem.getItem());
            String itemDesc = session.getItemMasterFindItemDesc(omsCustOrdItem.getItem());
            log.info("itemDesc " + itemDesc);
            String delimiter = "##";
            if (itemDesc.length() >= 120) {
                itemDesc = itemDesc.substring(0, 120);
                log.info("substring of itemDesc " + itemDesc);
            }
            String item_desc_secondary = session.getItemMasterFindItemDescSecondary(omsCustOrdItem.getItem());
            log.info("item_desc_secondary " + item_desc_secondary);
            if (item_desc_secondary != null && !item_desc_secondary.isEmpty() && item_desc_secondary.length() > 0) {
                StringBuffer s1 = new StringBuffer(item_desc_secondary);
                log.info("Length " + s1.length());
                StringBuffer revString = null;
                if (s1.length() >= 60) {
                    revString = new StringBuffer(s1.reverse().substring(0, 60));
                    log.info("revString " + revString.reverse());
                    log.info("revString Length " + revString.length());
                    item_desc_secondary = revString.reverse().toString();
                }
                //String delimiter=session.getOmsSystemParametersFindIndValue("OMS_ITEM_DESC_SECONDARY","Delimiter");
                // log.info("delimiter "+delimiter);

                //custOrdItmDesc.setItemDescription(session.getItemMasterFindItemDesc(omsCustOrdItem.getItem()));
                log.info("Concat " + itemDesc.concat(delimiter.concat(item_desc_secondary)));
                custOrdItmDesc.setItemDescription(itemDesc.concat(delimiter.concat(item_desc_secondary)));
            } else {
                if (itemDesc.length() >= 60) {
                    custOrdItmDesc.setItemDescription(itemDesc.concat(delimiter.concat(itemDesc.substring(0, 60))));
                } else {
                    custOrdItmDesc.setItemDescription(itemDesc.concat(delimiter.concat(itemDesc)));
                }
                
            }
            custOrdItmDesc.setEntryMethod(custOrdItmDesc.getEntryMethod().MANUAL);
            /* Start get the total of inclusive tax amount at each item level to set at head level @tsultana */
            inclusiveTaxTotal = inclusiveTaxTotal.add(omsCustOrdItem.getUnitVatAmount().multiply(omsCustOrdItem.getQtyOrderedSuom()));
            /* End get the total of inclusive tax amount at each item level to set at head level @tsultana */
            grandTotal = grandTotal.add(omsCustOrdItem.getUnitRetail().multiply(omsCustOrdItem.getQtyOrderedSuom()));
            custOrdItmDesc.setQuantity(omsCustOrdItem.getQtyOrderedSuom());
            //  OMS_CO_FULFILL_DETAIL.FULFILL_CONF_QTY-(OMS_CUST_ORD_ITM.CUM_QTY_DELIVERED+OMS_CUST_ORD_ITM.QTY_CANCELLED)
            List<OmsCoFulfillDetail> omsCoFulfillDetailList1 =
                session.getOmsCoFulfillDetailFindFulFillDetailByItem(omsCustOrdNo, omsCustOrdItem.getItem());
            log.info("---" + omsCustOrdNo + "omsCustOrdItem.getItem()" + omsCustOrdItem.getItem() + "----");
            // log.info("omsCoFulfillDetailList1.get(0).getFulfillLoc()"+omsCoFulfillDetailList1.get(0).getFulfillLoc());
            OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
            if (omsCoFulfillDetailList1.size() != 0) {
                List<OmsCoFulfillDetail> omsCoFulfillDetailList2 =
                    session.getOmsCoFulfillDetailFindBySourceLocAndOmsCustNo(omsCustOrdNo, omsCustOrdItem.getItem(),
                                                                             omsCoFulfillDetailList1.get(0).getFulfillLoc(),
                                                                             omsCustOrdItem.getLineNo());
                log.info("after");
                BigDecimal availableQty = BigDecimal.ZERO;

                for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList2) {
                    //changed the code for Transfers between Store to Store
                    log.info("calling checkOpenDeliveryForQuantity_PickedandDevelired ");
                    int quantity_picked =
                        omsUtilCommmons.checkOpenDeliveryForQuantity_PickedandDevelired(custOrderId, omsCustOrdNo,
                                                                                        omsCustOrdItem);
                    log.info("quantity_picked " + quantity_picked);
                    OmsCoFulfillDetail omsCoFulfillDetail1 = null;
                    try
                    {
                        //omsCoFulfillDetail1=session.getOmsCoFulfillDetailfindByOmsCustOrdNoLineNoandItemsrcandFul(omsCustOrdNo, omsCustOrdItem.getLineNo());
                        quantity_picked = quantity_picked - omsCustOrdItem.getCumQtyDelivered().intValue();
                    } catch (Exception e) {
                        quantity_picked = 0;
                    }


                    if (quantity_picked > 0) 
                    {
                        log.info("inside if for quantity_picked");
                        availableQty = new BigDecimal(quantity_picked);
                    }
                    //                    else if(omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc()))
                    //                    {
                    //                        availableQty=availableQty.add(omsCoFulfillDetail.getFulfillConfQty().subtract(omsCoFulfillDetail.getFulfillDeliverQty()).subtract(omsCoFulfillDetail.getFulfillCancelQty()));
                    //                    }
                    else {
                        availableQty = availableQty.add(new BigDecimal(0));
                        log.info("************availableQty : " +new BigDecimal(0)+"****************");
                    }
                }
                custOrdItmDesc.setAvailableQuantity(availableQty);
                log.info("***************availableQty : " +availableQty+"******************");
            } else {
                custOrdItmDesc.setAvailableQuantity(new BigDecimal(0));
                log.info("************availableQty : " +new BigDecimal(0)+"****************");
            }

            if (omsCustOrdItem.getCumQtyDelivered() != null) {
                log.info("inside CumQtyDelivered is not equal to null");
                //selectedQuantity=WHCheck(custOrderId,omsCustOrdNo,omsCustOrdItem);
                log.info("selectedQuantity " + selectedQuantity);
                int courierQty = 0;
                //courierQty=checkOpenDelivery(custOrderId,omsCustOrdNo,omsCustOrdItem);
                log.info("courierQty " + courierQty);
                log.info("selectedQuantity with courierQty" + selectedQuantity.intValue() + courierQty);
                int bol = courierQty - omsCustOrdItem.getCumQtyDelivered().intValue();
                log.info("bol " + bol);
                selectedQuantity = selectedQuantity.add(new BigDecimal(bol));

                if (selectedQuantity.intValue() > 0) {
                    BigDecimal deliveredQty = omsCustOrdItem.getCumQtyDelivered().add(selectedQuantity);
                    log.info("deliveredQty " + deliveredQty);
                    log.info("**********************custOrdItmDesc.getAvailableQuantity() " + custOrdItmDesc.getAvailableQuantity()+"*******************************");
                    //if(deliveredQty.intValue()<=custOrdItmDesc.getAvailableQuantity().intValue())
                    //{
                    log.info("selected qty is greater than 0");
                    log.info("omsCustOrdItem.getCumQtyDelivered().add(selectedQuantity) " +
                             omsCustOrdItem.getCumQtyDelivered().add(selectedQuantity));
                    custOrdItmDesc.setCompletedQuantity(omsCustOrdItem.getCumQtyDelivered().add(selectedQuantity));
                    custOrdItmDesc.setAvailableQuantity(custOrdItmDesc.getAvailableQuantity().subtract(new BigDecimal(bol)));
                    
                    log.info("****************custOrdItmDesc.setAvailableQuantity(custOrdItmDesc.getAvailableQuantity().subtract(new BigDecimal(bol))) :"+custOrdItmDesc.getAvailableQuantity().subtract(new BigDecimal(bol))+"**********************");
                    /* }
                  else {
                      log.info("quantity is less than zero");
                      custOrdItmDesc.setCompletedQuantity(omsCustOrdItem.getCumQtyDelivered());

                  }*/
                } else {
                    log.info("quantity is less than zero " + omsCustOrdItem.getCumQtyDelivered() + "for line No " +
                             omsCustOrdItem.getLineNo());
                    custOrdItmDesc.setCompletedQuantity(omsCustOrdItem.getCumQtyDelivered());

                }

            } else {
                log.info("omsCustOrdItem.getCumQtyDelivered() inside else " + omsCustOrdItem.getCumQtyDelivered());
                custOrdItmDesc.setCompletedQuantity(omsCustOrdItem.getCumQtyDelivered());
            }
            if (omsCustOrdItem.getQtyCancelled() != null) {
                custOrdItmDesc.setCancelledQuantity(omsCustOrdItem.getQtyCancelled());
            }


            log.info("Getting RMA Values");
            List<OmsRmaReq> rmaIdList = session.getOmsRmaReqFindByOmscustOrdNo(omsCustOrdNo);
            log.info("rmaIdList size " + rmaIdList.size());
            BigDecimal rmaQty = BigDecimal.ZERO;
            BigDecimal cancelQty = BigDecimal.ZERO;
            BigDecimal previousRmaQty = BigDecimal.ZERO;
            for (OmsRmaReq rmaId : rmaIdList) {
                log.info("inside rmaId loop");

                try {
                    omsRmaReqItem =
                            session.getOmsRmaReqItemFindByRmaIdAndItem(rmaId.getRmaId(), omsCustOrdItem.getItem(),
                                                                       omsCustOrdItem.getLineNo());
                    log.info("=RMA Table values===OMSustOrdItem Table values====");
                    log.info("RMA ID " + rmaId.getRmaId());
                    log.info("Line No " + omsRmaReqItem.getLineNo() + " " + "omsCustOrdItem.getLineNo() " +
                             omsCustOrdItem.getLineNo());
                    log.info("RMA Item " + omsRmaReqItem.getItem() + " " + "omsCustOrdItem.getItem() " +
                             omsCustOrdItem.getItem());
                    rmaQty1 = omsRmaReqItem.getRmaQty();
                    log.info("rmaQty1 " + rmaQty1);
                    indictor = rmaId.getRefundCompltInd();
                    returnStatus = rmaId.getReturnStatus();
                    log.info("Indictor in RMAReq " + indictor);
                    log.info("Return Status " + returnStatus);
                    log.info("checking condition");

                    if (omsRmaReqItem.getItem().toString().equals(omsCustOrdItem.getItem().toString()) &&
                        (omsRmaReqItem.getLineNo().toString().equals(omsCustOrdItem.getLineNo().toString()))) {
                        log.info("inside RMA loop condition true for line No and Item");
                        if (rmaId.getStatus().equals("S")) {
                            if (indictor.equals("N") && returnStatus.equals("Y")) {
                                log.info("Indictor in RMAReq is N and Return Status is Y");
                                //add with cusOrdItem cancel qty
                                if (rmaQty1.intValue() == 0) {

                                    log.info("inside rmaQty1 ==0");
                                    cancelQty = cancelQty.add(rmaQty1.add(omsCustOrdItem.getQtyCancelled()));
                                    log.info("Cancelled Qty " + cancelQty);
                                } else {
                                    flag2 = true;
                                    custOrdItmDesc.setEntryMethod(custOrdItmDesc.getEntryMethod().ICCFLBK);
                                    log.info("inside else rmaQty1!=0");
                                    cancelQty = cancelQty.add(rmaQty1);
                                    log.info("Cancelled Qty " + cancelQty);
                                    //log.info("Available quantity "+custOrdItmDesc.getAvailableQuantity().add(omsCustOrdItem.getQtyCancelled()));
                                    //custOrdItmDesc.setAvailableQuantity(custOrdItmDesc.getAvailableQuantity().add(omsCustOrdItem.getQtyCancelled()));
                                }

                                flag1 = true;
                                //log.info("setting cancelledQqty "+rmaQty1.add(omsCustOrdItem.getQtyCancelled()));
                                completedQty =
                                        (omsCustOrdItem.getCumQtyDelivered().subtract(rmaQty1)).subtract(previousRmaQty);
                                log.info("completedQty " + completedQty);
                                log.info("previousRmaQty " + previousRmaQty);
                                previousRmaQty = previousRmaQty.add(rmaQty1);
                                //Getting the custOrdItemDesc cancelled qty and adding with cancelQty.
                                log.info("custOrdItmDesc.getCancelledQuantity() " +
                                         custOrdItmDesc.getCancelledQuantity());
                                log.info("cancelQty " + cancelQty);
                                //log.info("Adding custOrdItmDesc.getCancelledQuantity().add(cancelQty) "+custOrdItmDesc.getCancelledQuantity().add(cancelQty));
                                custOrdItmDesc.setCancelledQuantity(cancelQty);
                                custOrdItmDesc.setCompletedQuantity(completedQty);

                                log.info("completed Amount " +
                                         (omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail())).subtract(cancelQty.multiply(omsCustOrdItem.getUnitRetail())));
                                custOrdItmDesc.setCompletedAmount((omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail())).subtract(cancelQty.multiply(omsCustOrdItem.getUnitRetail())));

                                //log.info("CompletedQuantity "+omsCustOrdItem.getCumQtyDelivered().subtract(rmaQty1));
                                log.info("setted qty value in response");
                                rmaQty1 = BigDecimal.ZERO;
                            }
                        }
                    }
                    // rmaQty=rmaQty1;
                    rmaQty = rmaQty.add(rmaQty1);
                    log.info("===============rmaQty====================" + rmaQty);
                } catch (Exception e) {


                    log.info("No records in RMA Item table for " + rmaId.getRmaId() + "," + omsCustOrdItem.getItem() +
                             "," + omsCustOrdItem.getLineNo());
                    log.info("completed Amount " +
                             (omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail())).subtract(cancelQty.multiply(omsCustOrdItem.getUnitRetail())));
                    //custOrdItmDesc.setCompletedAmount(new BigDecimal(0));
                    custOrdItmDesc.setCompletedAmount((omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail())).subtract(cancelQty.multiply(omsCustOrdItem.getUnitRetail())));

                }
            }


            log.info("test point 1");
            OmsCustOrdItem omsCustOrdItem1 =
                session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsCustOrdItem.getItem(),
                                                    omsCustOrdItem.getLineNo());
            log.info("setting Returned Qty for lineNo " + omsCustOrdItem.getLineNo() + "Returned Qty " +
                     omsCustOrdItem1.getQtyReturned());
            custOrdItmDesc.setReturnedQuantity(omsCustOrdItem1.getQtyReturned());

            log.info("before calling coupon balance function::::"+omsCustOrdNo);            
            flag = omsUtilCommmons.checkTenderType(omsCustOrdNo);
            log.info("after calling coupon balance function::::"+flag);
                try {
                    if(flag){
                            CouponBalance = omsUtilCommmons.getCouponBalance(omsCustOrdNo, custOrderId);
                            log.info("after calling coupon balance function");
                            log.info("coupon balance: "+CouponBalance);
                            if(CouponBalance<0){
                                CouponBalance = 0;
                            }
                            log.info("before setting coupon balance to setPaidAmount");
                            custOrderDesc.setPaidAmount(new BigDecimal(CouponBalance));
                            log.info("after setting coupon balance to setPaidAmount");
                            log.info("custOrderDesc.setPaidAmount: "+custOrderDesc.getPaidAmount());
                    }else{
                        custOrderDesc.setPaidAmount(new BigDecimal(CouponBalance));
                    }
                }catch (SQLException e) {
                    log.info("getCouponBalance Exception block:"+e.getMessage());
            }
            
            OmsOrposCustOrdItm omsOrposCustOrdItm = null;
            try {
                omsOrposCustOrdItm =
                        session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndLineItemNo(omsOrposCustOrderId,
                                                                                          omsCustOrdItem1.getLineNo());

                BigDecimal retuntedQtyFromOrpos = omsOrposCustOrdItm.getReturnedQuantity();
                BigDecimal returnedQty = rmaQty.add(retuntedQtyFromOrpos);
                if (omsOrposCustOrdItm.getReturnedQuantity() != null) {
                    //  custOrdItmDesc.setReturnedQuantity(returnedQty);
                }
            } catch (Exception e) {
                log.info("inside catch block");
                //read from Audit item table instead of zero
                try {
                    log.info("inside try block fetching the record from audit item table");
                    BigDecimal retuntedQtyFromOrpos = BigDecimal.ZERO;
                    log.info("custOrderId " + custOrderId);
                    List<OmsOrposCustOrderHead> omsOrposCustOrderHeadList =
                        session.getOmsOrposCustOrderHeadfindColumns(custOrderId);
                    log.info("omsOrposCustOrderHeadList.size() " + omsOrposCustOrderHeadList.size());
                    if (omsOrposCustOrderHeadList.size() == 0) {
                        log.info("no records present in the ORPOS table setting returned qty to 0");
                        // custOrdItmDesc.setReturnedQuantity(rmaQty);
                    }
                    for (OmsOrposCustOrderHead omsOrposCustOrderHead : omsOrposCustOrderHeadList) {
                        log.info("omsOrposCustOrderHead.getOmsOrposCustOrderId() " +
                                 omsOrposCustOrderHead.getOmsOrposCustOrderId());
                        log.info("omsCustOrdItem1.getLineNo() " + omsCustOrdItem1.getLineNo());
                        List<OmsOrposCustOrdItmRtn> omsOrposCustOrdItmRtnList =
                            session.getOmsOrposCustOrdItmRtnFindByOmsOrposCustOrdIdAndLineItemNo(omsOrposCustOrderHead.getOmsOrposCustOrderId(),
                                                                                                 omsCustOrdItem1.getLineNo());
                        log.info("omsOrposCustOrdItmRtnList.size() " + omsOrposCustOrdItmRtnList.size());
                        if (omsOrposCustOrdItmRtnList.size() > 0) {
                            for (OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn : omsOrposCustOrdItmRtnList) {
                                retuntedQtyFromOrpos =
                                        retuntedQtyFromOrpos.add(omsOrposCustOrdItmRtn.getReturnedQuantity());
                                log.info("=========retuntedQtyFromOrpos========== " + retuntedQtyFromOrpos);
                                BigDecimal returnedQty = rmaQty.add(retuntedQtyFromOrpos);
                                log.info("==========returnedQty==============" + returnedQty);

                                // custOrdItmDesc.setReturnedQuantity(returnedQty);
                            }
                        } else {
                            log.info("setting return qty to zero");
                            // custOrdItmDesc.setReturnedQuantity(rmaQty);

                        }
                    }
                } catch (Exception e1) {
                    log.info("no records found in audit return Itm setting return quantity to zero");
                    // custOrdItmDesc.setReturnedQuantity(BigDecimal.ZERO);

                }


            }
            if (omsCustOrdItem.getStandardUom() != null) {
                custOrdItmDesc.setUnitOfMeasure(omsCustOrdItem.getStandardUom());
            }
            if (omsCustOrdItem.getRetailCurr() != null) {
                custOrdItmDesc.setCurrencyCode(omsCustOrdItem.getRetailCurr());
            }

            custOrdItmDesc.setItemTotal(omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItem.getUnitRetail()));

            if (omsCustOrdItem.getUnitRetail() != null) {
                custOrdItmDesc.setUnitSellPrice(omsCustOrdItem.getUnitRetail());
            }
            if (omsCustOrdItem.getOrigUnitRetail() != null) {
                custOrdItmDesc.setUnitRegularPrice(omsCustOrdItem.getOrigUnitRetail());
                log.info("UnitRegularPrice-->" + omsCustOrdItem.getOrigUnitRetail());
            }
            
            if (flag2 == true)
            {
                log.info("****** flag2 is set to true ******");
                custOrdItmDesc.setAvailableQuantity(custOrdItmDesc.getAvailableQuantity().add(omsCustOrdItem.getQtyCancelled()));
                log.info("**********custOrdItmDesc.getAvailableQuantity().add(omsCustOrdItem.getQtyCancelled()) " +custOrdItmDesc.getAvailableQuantity().add(omsCustOrdItem.getQtyCancelled())+"********************");
            }
            if (flag1 == false)
            {
                log.info("completed amount when flag is false " +
                         omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail()));
                custOrdItmDesc.setCompletedAmount(omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail()));
            }
            //do with table indicator validation
            //get the indicator value from table
            log.info("===================================code for setting Amount in item Table====================================");
            BigDecimal omsCancelId = new BigDecimal(0);
            String indicator = null;
            BigDecimal dummyCancelQty = BigDecimal.ZERO;
            BigDecimal remainingQty = BigDecimal.ZERO;
            List<OmsCoCancelHead> omsCoCancelHeadList = session.getOmsCoCancelHeadFindByCustOrdNo(custOrderId);
            log.info("Checking for List size " + omsCoCancelHeadList.size());
            if (omsCoCancelHeadList.size() != 0 && omsCustOrdItem.getQtyCancelled().intValue() != 0)
            {
                log.info("inside if condition list size is gretaer than zero");

                for (OmsCoCancelHead omsCoCancelHead : omsCoCancelHeadList) 
                {
                    log.info("inside OmsCoCancelHead loop");
                    omsCancelId = omsCoCancelHead.getOmsCancelId();
                    log.info("omsCancelId " + omsCancelId);
                    indicator = omsCoCancelHead.getRefundCompltInd();
                    log.info("indicator " + indicator);
                    List<OmsCoCancelItem> omsCoCancelItemList =
                        session.getOmsCoCancelItemFindByOmsCancelId(omsCancelId);
                    for (OmsCoCancelItem omsCoCancelItem : omsCoCancelItemList) {

                        log.info("inside omsCoCancelItem loop");
                        log.info("==============Items====================");
                        log.info("omsCustOrdItem.getItem() " + omsCustOrdItem.getItem());
                        log.info("omsCoCancelItem.getItem() " + omsCoCancelItem.getItem());
                        log.info("================Line No===================");
                        log.info("omsCustOrdItem.getLineNo() " + omsCustOrdItem.getLineNo());
                        log.info("omsCoCancelItem.getLineNo() " + omsCoCancelItem.getLineNo());
                        if ((omsCustOrdItem.getItem().equals(omsCoCancelItem.getItem()) &&
                             (omsCustOrdItem.getLineNo().equals(omsCoCancelItem.getLineNo())))) {
                            log.info("checking whether the indicator is N or Y");
                            if (indicator.equals("N") && flag2 == false) {
                                log.info("Indicator is N");
                                log.info("Setting Amount 0");
                                //BigDecimal remainingQty=omsCustOrdItem.getQtyCancelled().subtract(omsCoCancelItem.getCancelConfQty());
                                //custOrdItmDesc.setAvailableQuantity(custOrdItmDesc.getAvailableQuantity().add(remainingQty));
                                log.info("omsCoCancelItem.getCancelConfQty() " + omsCoCancelItem.getCancelConfQty());
                                dummyCancelQty = dummyCancelQty.add(omsCoCancelItem.getCancelConfQty());
                                log.info("dummyCancelQty " + dummyCancelQty);
                                if (dummyCancelQty.intValue() > 0) 
                                {
                                    custOrdItmDesc.setCancelledQuantity(dummyCancelQty);
                                    remainingQty = omsCustOrdItem.getQtyCancelled().subtract(dummyCancelQty);
                                    log.info("remainingQty " + remainingQty);
                                    custOrdItmDesc.setEntryMethod(custOrdItmDesc.getEntryMethod().ICC);
                                }

                                if (custOrdItmDesc.getCancelledAmount() == null)
                                {
                                    custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                                }
                                log.info("custOrdItmDesc.getCancelledAmount() " + custOrdItmDesc.getCancelledAmount());

                                custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                                //custOrdItmDesc.setCancelledAmount(remainingQty.multiply(omsCustOrdItem.getUnitRetail()));
                                //custOrdItmDesc.setCompletedAmount(omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail()));
                            } 
                            else 
                            {
                                log.info("else block indicator is Y");
                                if (flag2 == false) {
                                    log.info("inside flag2 == false ");
                                    if (custOrdItmDesc.getCancelledAmount() == null) {
                                        custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                                    }
                                    log.info("custOrdItmDesc.getCancelledAmount() " +
                                             custOrdItmDesc.getCancelledAmount());
                                    //added omsCoCancelItem.getCancelConfQty() from omscustordItem.getCancelledqty
                                    log.info("setting Cancelled Amount " +
                                             custOrdItmDesc.getCancelledAmount().add(omsCoCancelItem.getCancelConfQty().multiply(omsCustOrdItem.getUnitRetail())));
                                    custOrdItmDesc.setCancelledAmount(custOrdItmDesc.getCancelledAmount().add(omsCoCancelItem.getCancelConfQty().multiply(omsCustOrdItem.getUnitRetail())));
                                } else {
                                    if (custOrdItmDesc.getCancelledAmount() == null) {
                                        custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                                    }
                                    log.info("custOrdItmDesc.getCancelledAmount() " +
                                             custOrdItmDesc.getCancelledAmount());
                                    log.info("inside flag2 !=false ie fla2= " + flag2);
                                    log.info("cancelled amount " + 0);
                                    custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                                }
                                //log.info("setting completed amount "+(omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail()).subtract(omsCustOrdItem.getQtyCancelled().multiply(omsCustOrdItem.getUnitRetail()))));


                                // custOrdItmDesc.setCompletedAmount(omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitRetail()));
                            }

                        } else {
                            // custOrdItmDesc.setCancelledAmount(new BigDecimal(0));
                            continue;
                        }

                    }
                    log.info(" omsCoCancelItem loop ends");
                    //break;
                }
                log.info(" omsCoCancelHead loop ends");
                if (remainingQty.intValue() > 0)
                {
                    if (custOrdItmDesc.getCancelledAmount() == null)
                    {
                        custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                    }
                    log.info("****************** custOrdItmDesc.getAvailableQuantity()" + custOrdItmDesc.getAvailableQuantity()+"*******************");
                    log.info("******************** custOrdItmDesc.getAvailableQuantity().add(remainingQty) " +custOrdItmDesc.getAvailableQuantity().add(remainingQty)+"***************************");
                    custOrdItmDesc.setAvailableQuantity(custOrdItmDesc.getAvailableQuantity().add(remainingQty));
                    log.info("custOrdItmDesc.getCancelledAmount() " + custOrdItmDesc.getCancelledAmount());
                    custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                }

            } 
            else 
            {
                if (custOrdItmDesc.getCancelledAmount() == null) 
                {
                    custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
                }
                log.info("custOrdItmDesc.getCancelledAmount() " + custOrdItmDesc.getCancelledAmount());

                custOrdItmDesc.setCancelledAmount(BigDecimal.ZERO);
            }
            try 
            {
                /*  if(omsOrposCustOrdItm.getReturnedAmount()!=null)
               {
                    custOrdItmDesc.setReturnedAmount((checkNullValueForNumber(omsOrposCustOrdItm.getReturnedAmount())));
               } */
            } 
            catch (Exception e) 
            {
                custOrdItmDesc.setReturnedAmount(BigDecimal.ZERO);
            }
            log.info("test point2");
            // custOrdItmDesc.setPaidAmount(omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItem.getUnitRetail()));
            //          List<OmsCustOrdTender> omsCustOrdTender=session.getOmsCustOrdTenderFindByOmsCustOrdNo(omsCustOrdNo);
            //           log.info("============="+omsCustOrdTender.size());
            //           log.info("============="+omsCustOrdTender);
            log.info("omsCustOrdHead.getPayInStore() " + omsCustOrdHead.getPayInStore());
            if (omsCustOrdHead.getPayInStore().equals("Y")) {
                log.info("Pay In Store is y setting paidamount to 0");

                custOrdItmDesc.setPaidAmount(BigDecimal.ZERO);
            } else {
                log.info("pay in store indicator is N setting it " +
                         omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItem.getUnitRetail()));
                custOrdItmDesc.setPaidAmount(omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItem.getUnitRetail()));
            }
            BigDecimal discountTotal = BigDecimal.ZERO;
            //Setting the discount to 0 as because of this calculation ORPOS
            //is doing wrong calculation
            if (omsCustOrdItem.getOrigUnitRetail() != null) {
                discountTotal = omsCustOrdItem.getOrigUnitRetail().subtract(omsCustOrdItem.getUnitRetail());
            }
            custOrdItmDesc.setDiscountTotal(discountTotal);
            custOrdItmDesc.setCompletedDiscountAmount(discountTotal.multiply(omsCustOrdItem.getCumQtyDelivered()));
            custOrdItmDesc.setCancelledDiscountAmount(discountTotal.multiply(omsCustOrdItem.getQtyCancelled()));
            try
            {
                /* if(omsOrposCustOrdItm.getReturnedDiscountAmount()!=null)
               {
                    custOrdItmDesc.setReturnedDiscountAmount((checkNullValueForNumber(omsOrposCustOrdItm.getReturnedDiscountAmount())));
               } */
            } 
            catch (Exception e) 
            {
                custOrdItmDesc.setReturnedAmount(BigDecimal.ZERO);
            }
//            custOrdItmDesc.setTaxTotal(BigDecimal.ZERO);
//            custOrdItmDesc.setCompletedTaxAmount(BigDecimal.ZERO);
//            custOrdItmDesc.setCancelledTaxAmount(BigDecimal.ZERO);
//            custOrdItmDesc.setReturnedTaxAmount(BigDecimal.ZERO);
            /* Start Vat Changes @tsultana */
            custOrdItmDesc.setInclusiveTaxTotal(omsCustOrdItem.getUnitVatAmount().multiply(omsCustOrdItem.getQtyOrderedSuom()));
            custOrdItmDesc.setTaxTotal(omsCustOrdItem.getUnitVatAmount().multiply(omsCustOrdItem.getQtyOrderedSuom()));
            /* End Vat Changes @tsultana */
//            custOrdItmDesc.setCompletedInclusiveTaxAmount(BigDecimal.ZERO);
//            custOrdItmDesc.setCancelledInclusiveTaxAmount(BigDecimal.ZERO);
//            custOrdItmDesc.setReturnedInclusiveTaxAmount(BigDecimal.ZERO);
            custOrdItmDesc.setTaxGroupId(BigDecimal.ZERO);
            /* Start Vat Changes @tsultana */
            if(omsCustOrdHead.getApplicationId().equals("ORPOS")) 
            {
                log.info("Its an ORPOS order");
                List<OmsOrposCustOrderHead> omsOrposHead=session.getOmsOrposCustOrderHeadfindColumns(omsCustOrdHead.getCustOrderNo());
                OmsOrposCustOrdItm omsorposcustItm=session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(omsOrposHead.get(0).getOmsOrposCustOrderId(), omsCustOrdItem.getItem(), omsCustOrdItem.getLineNo());
                
                log.info("ORPOS Objects Line No "+omsorposcustItm.getLineItemNo()+"Item "+omsorposcustItm.getItemId());
                log.info("OmsCustOrdItem object LineNo "+omsCustOrdItem.getLineNo()+"Item "+omsCustOrdItem.getItem());
                log.info("Group Id "+omsorposcustItm.getTaxGroupId());
                log.info("Taxable flag value is "+omsorposcustItm.getTaxableFlag());
                custOrdItmDesc.setTaxGroupId(omsorposcustItm.getTaxGroupId());
                custOrdItmDesc.setTaxableFlag(Flag.valueOf(omsorposcustItm.getTaxableFlag()));
            }
                else if(omsCustOrdHead.getApplicationId().equals("E-COMMERCE"))
                {
                   String taxableInd="N";
                    try
                    {
                        if("S".equalsIgnoreCase(omsCustOrdHead.getDeliveryType()))
                        {
                            taxableInd = omsUtilCommmons.getTaxableIndicator(omsCustOrdItem.getItem(), omsCustOrdHead.getOrderRequestorId());
                        }
                        else
                        {
                            taxableInd = omsUtilCommmons.getTaxableIndicator(omsCustOrdItem.getItem(), omsCustOrdHead.getPickLoc());
                        }
                        log.info("Taxable Indicator for E-Commerece Order "+taxableInd);

                    }
                    catch(Exception e)
                    {
                            log.info("Cannot retrieve Taxable Indicator for the given item");
                    }
                    custOrdItmDesc.setTaxableFlag(Flag.valueOf(taxableInd));
                        
                }
            /* End Vat Changes @tsultana */
            BigDecimal departmentId = session.getItemMasterFindDept(omsCustOrdItem.getItem());
            custOrdItmDesc.setDepartmentId(String.valueOf(session.getItemMasterFindDept(omsCustOrdItem.getItem())));
            custOrdItmDesc.setRestrictiveAge(BigDecimal.ZERO);

            //DiscountableFlag discountableFlag=custOrdItmDesc.getDiscountableFlag();
            //custOrdItmDesc.setDiscountableFlag(discountableFlag.fromValue(omsOrposCustOrdItm.getDiscountableFlag()));
            log.info("before discountable flag");

            custOrdItmDesc.setDiscountableFlag(custOrdItmDesc.getDiscountableFlag().fromValue("Y"));

            log.info("after discountable flag");
            //DamageDiscountableFlag damageDiscountableFlag = custOrdItmDesc.getDamageDiscountableFlag(omsOrposCustOrdItm.getDamageDiscountableFlag());
            //custOrdItmDesc.setDamageDiscountableFlag(damageDiscountableFlag.fromValue());

            custOrdItmDesc.setDamageDiscountableFlag(custOrdItmDesc.getDamageDiscountableFlag().fromValue("Y"));

            custOrdItmDesc.setEmployeeDiscountableFlag(custOrdItmDesc.getEmployeeDiscountableFlag().fromValue("Y"));
            log.info("------------------");
            ItemType itemType = custOrdItmDesc.getItemType();
            custOrdItmDesc.setItemType(custOrdItmDesc.getItemType().fromValue("STCK"));

            log.info("after item type1");

            log.info("brfore setReturnEligibleFlag");

            custOrdItmDesc.setMerchandiseHierarchyGroupId(String.valueOf(session.getDepsFindGroupNo(departmentId)));
            //PRODUCT_GROUP***********


            custOrdItmDesc.setReturnEligibleFlag(custOrdItmDesc.getReturnEligibleFlag().Y);
            log.info("before serialized item");
            OMSUtilCommons utiCommon = new OMSUtilCommons();
            List<OmsCoFulfillDetail> omsCoFulfillDetailList =
                session.getOmsCoFulfillDetailFindFulFillDetailByItem(omsCustOrdNo, omsCustOrdItem.getItem());
            if (omsCoFulfillDetailList.size() > 0) {
                log.info("item_loc " +
                         utiCommon.findUINType(omsCustOrdItem.getItem(), omsCoFulfillDetailList.get(0).getFulfillLoc()));
                custOrdItmDesc.setSerializedItemFlag(custOrdItmDesc.getSerializedItemFlag().fromValue(utiCommon.findUINType(omsCustOrdItem.getItem(),
                                                                                                                            omsCoFulfillDetailList.get(0).getFulfillLoc())));
            } else {
                custOrdItmDesc.setSerializedItemFlag(custOrdItmDesc.getSerializedItemFlag().fromValue("N"));
            }
            custOrdItmDesc.setValidateSerialNumberFlag(custOrdItmDesc.getValidateSerialNumberFlag().fromValue("N"));

            custOrdItmDesc.setSizeRequiredFlag(custOrdItmDesc.getSizeRequiredFlag().N);
            try {
                //custOrdItmDesc.setAllowNewSerialNumberFlag(custOrdItmDesc.getAllowNewSerialNumberFlag().fromValue(omsOrposCustOrdItm.getAllowNewSerialNumberFlag()));
                //custOrdItmDesc.setTaxGroupId(checkNullValueForNumber(omsOrposCustOrdItm.getTaxGroupId()));
                //custOrdItmDesc.setTaxableFlag(custOrdItmDesc.getTaxableFlag().fromValue(omsOrposCustOrdItm.getTaxableFlag()));
            } catch (Exception e) {
                custOrdItmDesc.setTaxGroupId(BigDecimal.ZERO);
                custOrdItmDesc.setTaxableFlag(custOrdItmDesc.getTaxableFlag().fromValue("N"));
            }

            log.info("before weight");
            //  custOrdItmDesc.setWeight(checkNullValueForNumber(omsOrposCustOrdItm.getWeight()));

            log.info("inside gift recipt item");
            custOrdItmDesc.setGiftReceiptedItemFlag(custOrdItmDesc.getGiftReceiptedItemFlag().fromValue("N"));
            custOrdItmDesc.setShippingChargeFlag(custOrdItmDesc.getShippingChargeFlag().fromValue("N"));

            //custOrdItmDesc.setEntryMethod(custOrdItmDesc.getEntryMethod().fromValue("MANUAL"));
            log.info("after shipping charge");


            //custOrdItmDesc.setEntryMethod(entryMethod.fromValue(omsOrposCustOrdItm.getEntryMethod()));
            log.info("after entry method");
            try {

                log.info("Checking whether the item is a shipping charge item");
                String shipingChargeDept =
                    session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
                log.info("shipingChargeDept " + shipingChargeDept);
                log.info("items.getItem() " + omsCustOrdItem.getItem());
                BigDecimal itemDept = session.getItemMasterFindDept(omsCustOrdItem.getItem());
                log.info("itemDept " + itemDept);
                String inventoryIndn = session.getItemMasterFindInventoryInd(custOrdItmDesc.getItemId(), itemDept);
                log.info("inventoryIndn " + inventoryIndn);
                //               if(inventoryIndn.equals("N"))
                //               {
                //                   log.info("inside FulfillmentSeqNo for non-inventoryIndn");
                //                   custOrdItmDesc.setFulfillmentSeqNo(0);
                //               }
                if (shipingChargeDept.equals(itemDept.toString()) == true &&
                    omsCustOrdHead.getApplicationId().equals("ORPOS") || inventoryIndn.equals("N")) {
                    log.info("contains a shipping charge Item");
                    custOrdItmDesc.setFulfillmentSeqNo(0);

                } else if (shipingChargeDept.equals(itemDept.toString()) == true &&
                           !omsCustOrdHead.getApplicationId().equals("ORPOS") || inventoryIndn.equals("N")) {
                    custOrdItmDesc.setFulfillmentSeqNo(1);
                }

                else {
                    custOrdItmDesc.setFulfillmentSeqNo(count);
                    count++;
                }

            } catch (Exception e) {
                custOrdItmDesc.setFulfillmentSeqNo(0);
            }
            //     custOrdItmDesc.setPrcOvdLineDesc(createResponseForPrcOvdLine());
            ///      custOrdItmDesc.setPromoLineDesc(createResponseForPromoLine());
            DiscntLineColDesc discntLineColDesc = createResponseForDiscntLine(omsCustOrdHead, custOrderId, omsCustOrdNo, omsCustOrdItem, custOrdItmDesc);
            log.info("discntLineColDesc " + discntLineColDesc.getDiscntLineDesc().size());
           
            if (discntLineColDesc.getDiscntLineDesc().size() > 0)
            {
                custOrdItmDesc.setDiscntLineColDesc(discntLineColDesc);
            } 
            else
            {
                custOrdItmDesc.setDiscntLineColDesc(null);
            }
            
            //Adding VAT changes for response by Tabu
            /* Start Vat Changes @tsultana */
            TaxLineColDesc taxLineColDesc = createResponseForTaxLine(omsCustOrdHead, custOrderId, omsCustOrdNo, omsCustOrdItem, custOrdItmDesc);
            log.info("taxLineColDesc " + taxLineColDesc.getTaxLineDesc().size());
            
            if (taxLineColDesc.getTaxLineDesc().size() > 0)
            {
                custOrdItmDesc.setTaxLineColDesc(taxLineColDesc);
                List<TaxLineDesc> taxLineDesc=taxLineColDesc.getTaxLineDesc();
                log.info("taxLineDesc.get(0).getCancelledTaxAmount() "+taxLineDesc.get(0).getCancelledTaxAmount());
                log.info("taxLineDesc.get(0).getReturnedTaxAmount() "+taxLineDesc.get(0).getReturnedTaxAmount());
                log.info("taxLineDesc.get(0).getCompletedTaxAmount()"+taxLineDesc.get(0).getCompletedTaxAmount());
                if(taxLineDesc.get(0).getCancelledTaxAmount()==null) 
                {
                    log.info("setting Item Cancelled tax Amt");
                    custOrdItmDesc.setCancelledTaxAmount(BigDecimal.ZERO);
                    custOrdItmDesc.setCancelledInclusiveTaxAmount(BigDecimal.ZERO);
                    log.info("setted Item Cancelled tax Amt");
                }
                if(taxLineDesc.get(0).getReturnedTaxAmount()==null) 
                {
                    custOrdItmDesc.setReturnedTaxAmount(BigDecimal.ZERO);  
                    custOrdItmDesc.setReturnedInclusiveTaxAmount(BigDecimal.ZERO);
                }
                if(taxLineDesc.get(0).getCompletedTaxAmount()==null) 
                {
                    custOrdItmDesc.setCompletedTaxAmount(BigDecimal.ZERO); 
                    custOrdItmDesc.setCompletedInclusiveTaxAmount(BigDecimal.ZERO);
                }
               
                custOrdItmDesc.setCompletedTaxAmount(taxLineDesc.get(0).getCompletedTaxAmount());
                custOrdItmDesc.setCancelledTaxAmount(taxLineDesc.get(0).getCancelledTaxAmount());
                custOrdItmDesc.setReturnedTaxAmount(taxLineDesc.get(0).getReturnedTaxAmount());
                custOrdItmDesc.setCompletedInclusiveTaxAmount(taxLineDesc.get(0).getCompletedTaxAmount());
                custOrdItmDesc.setCancelledInclusiveTaxAmount(taxLineDesc.get(0).getCancelledTaxAmount());
                custOrdItmDesc.setReturnedInclusiveTaxAmount(taxLineDesc.get(0).getReturnedTaxAmount());
            }  
           
            else
            {
                custOrdItmDesc.setCompletedTaxAmount(BigDecimal.ZERO);
                custOrdItmDesc.setCancelledTaxAmount(BigDecimal.ZERO);
                custOrdItmDesc.setReturnedTaxAmount(BigDecimal.ZERO);
                custOrdItmDesc.setCompletedInclusiveTaxAmount(BigDecimal.ZERO);
                custOrdItmDesc.setCancelledInclusiveTaxAmount(BigDecimal.ZERO);
                custOrdItmDesc.setReturnedInclusiveTaxAmount(BigDecimal.ZERO);
                custOrdItmDesc.setTaxLineColDesc(null);
            }
            //set for Item level
            
            if(custOrdItmDesc.getCompletedTaxAmount()==null) 
            {
                 custOrdItmDesc.setCompletedTaxAmount(BigDecimal.ZERO);
            }
            if(custOrdItmDesc.getCompletedInclusiveTaxAmount()==null)
            {     
                 custOrdItmDesc.setCompletedInclusiveTaxAmount(BigDecimal.ZERO);
            }
            
            if(custOrdItmDesc.getCancelledTaxAmount()==null) 
            {
                custOrdItmDesc.setCancelledTaxAmount(BigDecimal.ZERO);
            }
            if(custOrdItmDesc.getCancelledInclusiveTaxAmount()==null)
            { 
                custOrdItmDesc.setCancelledInclusiveTaxAmount(BigDecimal.ZERO);
            }
            if(custOrdItmDesc.getReturnedTaxAmount()==null) 
            {
                custOrdItmDesc.setReturnedTaxAmount(BigDecimal.ZERO);
            }
            if(custOrdItmDesc.getReturnedInclusiveTaxAmount()==null)
            {
                custOrdItmDesc.setReturnedInclusiveTaxAmount(BigDecimal.ZERO);
            }
            
            //set for header level
                if(custOrderDesc.getCancelledTaxAmount()==null)
                {
                    custOrderDesc.setCancelledTaxAmount(BigDecimal.ZERO);   
                }
                if(custOrderDesc.getReturnedTaxAmount()==null)
                {
                    custOrderDesc.setReturnedTaxAmount(BigDecimal.ZERO);
                }
                if(custOrderDesc.getCompletedTaxAmount()==null)
                {
                    custOrderDesc.setCompletedTaxAmount(BigDecimal.ZERO); 
                }
                if(custOrderDesc.getCompletedInclusiveTaxAmount()==null)
                {
                    custOrderDesc.setCompletedInclusiveTaxAmount(BigDecimal.ZERO);
                }
                if(custOrderDesc.getCancelledInclusiveTaxAmount()==null)
                {
                    custOrderDesc.setCancelledInclusiveTaxAmount(BigDecimal.ZERO); 
                }
                if(custOrderDesc.getReturnedInclusiveTaxAmount()==null)
                {
                    custOrderDesc.setReturnedInclusiveTaxAmount(BigDecimal.ZERO);  
                }
                log.info("custOrdItmDesc.getCompletedTaxAmount() "+custOrdItmDesc.getCompletedTaxAmount());
                log.info("custOrdItmDesc.getCancelledTaxAmount() "+custOrdItmDesc.getCancelledTaxAmount());
                log.info("custOrdItmDesc.getReturnedTaxAmount() "+custOrdItmDesc.getReturnedTaxAmount());
            
                log.info("custOrdItmDesc.getCompletedInclusiveTaxAmount() "+custOrdItmDesc.getCompletedInclusiveTaxAmount());
                log.info("custOrdItmDesc.getCancelledInclusiveTaxAmount() "+custOrdItmDesc.getCancelledInclusiveTaxAmount());
                log.info("custOrdItmDesc.getReturnedInclusiveTaxAmount() "+custOrdItmDesc.getReturnedInclusiveTaxAmount());

                
                log.info("custOrderDesc.getCompletedTaxAmount() "+custOrderDesc.getCompletedTaxAmount());
                log.info("custOrderDesc.getCancelledTaxAmount() "+custOrderDesc.getCancelledTaxAmount());
                log.info("custOrderDesc.getReturnedTaxAmount() "+custOrderDesc.getReturnedTaxAmount());
            
                log.info("custOrderDesc.getCompletedInclusiveTaxAmount() "+custOrderDesc.getCompletedInclusiveTaxAmount());
                log.info("custOrderDesc.getCancelledInclusiveTaxAmount() "+custOrderDesc.getCancelledInclusiveTaxAmount());
                log.info("custOrderDesc.getReturnedInclusiveTaxAmount() "+custOrderDesc.getReturnedInclusiveTaxAmount());
            
                custOrderDesc.setCancelledTaxAmount(custOrderDesc.getCancelledTaxAmount().add(custOrdItmDesc.getCancelledTaxAmount()));
                custOrderDesc.setCompletedTaxAmount(custOrderDesc.getCompletedTaxAmount().add(custOrdItmDesc.getCompletedTaxAmount()));
                custOrderDesc.setReturnedTaxAmount(custOrderDesc.getReturnedTaxAmount().add(custOrdItmDesc.getReturnedTaxAmount()));

                custOrderDesc.setCancelledInclusiveTaxAmount(custOrderDesc.getCancelledInclusiveTaxAmount().add(custOrdItmDesc.getCancelledInclusiveTaxAmount()));
                custOrderDesc.setCompletedInclusiveTaxAmount(custOrderDesc.getCompletedInclusiveTaxAmount().add(custOrdItmDesc.getCompletedInclusiveTaxAmount()));
                custOrderDesc.setReturnedInclusiveTaxAmount(custOrderDesc.getReturnedInclusiveTaxAmount().add(custOrdItmDesc.getReturnedInclusiveTaxAmount()));
            
            
               // custOrderDesc.setCancelledInclusiveTaxAmount(custOrderDesc.getCancelledInclusiveTaxAmount().setScale(2, BigDecimal.ROUND_UP));
            /* End Vat Changes @tsultana */
            //    custOrdItmDesc.setTaxLineColDesc(createResponseForTaxLine());
            //  custOrdItmDesc.setAlterationItem(createResponseForAlteration());
            // custOrdItmDesc.setGiftCardItem(createResponseForGiftCardItem());
            custOrdItmDescList.add(custOrdItmDesc);
            flag1 = false;
        }

        return custOrdItmColDesc;
    }

    public DiscntLineColDesc createResponseForDiscntLine(OmsCustOrdHead omsCustOrdHead, String custOrderId,
                                                         BigDecimal omsCustOrdNo, OmsCustOrdItem omsCustOrdItem,
                                                         CustOrdItmDesc custOrdItmDesc) throws SOAPException {
        // DiscntLineColDesc discntLineDescList;
        log.info("inside createResponseForDiscntLine");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        DiscntLineColDesc discntLineColDesc = new DiscntLineColDesc();
        List<DiscntLineDesc> discntLineDescList = discntLineColDesc.getDiscntLineDesc();
        try {
            BigDecimal discountAmt = BigDecimal.ZERO;
            log.info("calling RMARestocking_Fee");
            discountAmt = RMARestocking_Fee(custOrderId, omsCustOrdNo, omsCustOrdItem);
            log.info("discountAmt " + discountAmt);
            if (discountAmt.intValue() == 0) {
                List<OmsCustOrdItemDisc> omsCustOrdItemDiscList = null;
                try {
                    log.info("Inside try of Discounts for discountAmt=0 value");
                    omsCustOrdItemDiscList =
                            session.getOmsCustOrdItemDiscFindByOmsCustordNoLineNo(omsCustOrdNo, omsCustOrdItem.getLineNo());
                } catch (Exception e) {
                    log.info("no records exist in item disc table when discount is equal to 0" + e.getMessage());
                }

                log.info("********** after catch *********");
                if (omsCustOrdItemDiscList.size() != 0 && omsCustOrdItemDiscList != null) {
                    log.info("omsCustOrdItemDiscList:" + omsCustOrdItemDiscList.size());
                    for (OmsCustOrdItemDisc omsCustOrdItemDisc : omsCustOrdItemDiscList) {
                        DiscntLineDesc discntLineDesc = new DiscntLineDesc();
                        log.info("omsCustOrdItemDisc.getDiscLineNo() : " +
                                 omsCustOrdItemDisc.getDiscLineNo().intValue());
                        discntLineDesc.setLineNo(omsCustOrdItemDisc.getDiscLineNo().intValue());
                        if (omsCustOrdItemDisc.getRmsPromoType() != null) {
                            log.info("omsCustOrdItemDisc.getRmsPromoType() : " + omsCustOrdItemDisc.getRmsPromoType());
                            discntLineDesc.setDiscountReasonCode(omsCustOrdItemDisc.getRmsPromoType());
                        }
                        if (omsCustOrdItemDisc.getDiscRefNo() != null) {
                            log.info("omsCustOrdItemDisc.getDiscRefNo() : " + omsCustOrdItemDisc.getDiscRefNo());
                            discntLineDesc.setPromotionId(omsCustOrdItemDisc.getDiscRefNo());
                        }
                        if (omsCustOrdItemDisc.getEmployeeId() != null) {
                            log.info("omsCustOrdItemDisc.getEmployeeId() : " + omsCustOrdItemDisc.getEmployeeId());
                            discntLineDesc.setDiscountEmployeeId(omsCustOrdItemDisc.getEmployeeId());
                        }
                        if (omsCustOrdItemDisc.getDiscountType() != null) {
                            log.info("omsCustOrdItemDisc.getDiscountType() : " + omsCustOrdItemDisc.getDiscountType());
                            discntLineDesc.setStoreCouponId(omsCustOrdItemDisc.getDiscountType());
                        }
                        if (omsCustOrdItemDisc.getUnitDiscountAmount() != null) {
                            log.info("omsCustOrdItemDisc.getUnitDiscountAmount() : " +
                                     omsCustOrdItemDisc.getUnitDiscountAmount());
                            discntLineDesc.setUnitDiscountAmount(omsCustOrdItemDisc.getUnitDiscountAmount());
                        }

                        if (omsCustOrdItemDisc.getPromoCompId() != null) {
                            log.info("omsCustOrdItemDisc.getPromoCompId() : " + omsCustOrdItemDisc.getPromoCompId());
                            discntLineDesc.setPromotionComponentId(omsCustOrdItemDisc.getPromoCompId());
                        }

                        //Added for Simple Promo
                        if (omsCustOrdItemDisc.getSimplePromoInd() != null) {
                            log.info("Discount Rule Id : " + omsCustOrdItemDisc.getSimplePromoInd());
                            discntLineDesc.setDiscountRuleId(omsCustOrdItemDisc.getSimplePromoInd());
                        }

                        log.info("***** discntLineDescList ***** " + discntLineDescList.size());


                        //accountingMethod
                        discntLineDesc.setAccountingMethod(discntLineDesc.getAccountingMethod().DISCOUNT);
                        //AdvancedPricingRuleFlag
                        log.info("discntLineDesc.getAdvancedPricingRuleFlag().N : " +
                                 discntLineDesc.getAdvancedPricingRuleFlag().N);
                        discntLineDesc.setAdvancedPricingRuleFlag(discntLineDesc.getAdvancedPricingRuleFlag().N);

                        //log.info("discntLineDesc.getAssignmentBasis().MANU : " +discntLineDesc.getAssignmentBasis().OTHR);
                        //discntLineDesc.setAssignmentBasis(discntLineDesc.getAssignmentBasis().OTHR);

                        log.info("omsCustOrdHead.getApplicationId() : " + omsCustOrdHead.getApplicationId());

                        //Assignment Basis
                        if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) {
                            log.info("Its E-Commerce order so Assignment Basis is set to OTHR");
                            discntLineDesc.setAssignmentBasis(discntLineDesc.getAssignmentBasis().OTHR);
                            log.info("discntLineDesc.getAssignmentBasis() : " + discntLineDesc.getAssignmentBasis());
                        } else if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                            log.info("Its ORPOS order so Assignment Basis is set set based on request");
                            List<OmsOrposCustOrderHead> omsOrposCustOrderHeadList =
                                session.getOmsOrposCustOrderHeadfindColumns(custOrderId);
                            log.info("omsOrposCustOrderHeadList.size() " + omsOrposCustOrderHeadList.size());
                            OmsOrposDiscntLine omsOrposDiscntLine = null;
                            if (omsOrposCustOrderHeadList.size() > 0) {
                                log.info("Orpos seq no " + omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId());
                                log.info(" %%%%%% omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId() : " +
                                         omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId());
                                log.info(" %%%%%% omsCustOrdItemDisc.getDiscLineNo() : " +
                                         omsCustOrdItemDisc.getDiscLineNo());
                                log.info(" %%%%%% omsCustOrdItemDisc.getLineNo() : " + omsCustOrdItemDisc.getLineNo());
                                try {
                                    omsOrposDiscntLine =
                                            session.getOmsOrposDiscntLineCustOrdIdLineNoDiscntLineNo(omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId(),
                                                                                                     omsCustOrdItemDisc.getDiscLineNo(),
                                                                                                     omsCustOrdItemDisc.getLineNo());
                                } catch (Exception e) {
                                    log.info(" Error Occured While Fetching record" + e.getMessage());
                                }
                                discntLineDesc.setAssignmentBasis(discntLineDesc.getAssignmentBasis().fromValue(omsOrposDiscntLine.getAssignmentBasis()));
                            }
                        }

                        //DAMAGE_DISCOUNT_FLAG
                        log.info("discntLineDesc.getDamageDiscountFlag().N : " +
                                 discntLineDesc.getDamageDiscountFlag().N);
                        discntLineDesc.setDamageDiscountFlag(discntLineDesc.getDamageDiscountFlag().N);


                        if (omsCustOrdItem.getRetailCurr() != null) {
                            log.info("omsCustOrdItem.getRetailCurr() : " + omsCustOrdItem.getRetailCurr());
                            discntLineDesc.setCurrencyCode(omsCustOrdItem.getRetailCurr());
                        }

                        //                    BigDecimal discountTotal=BigDecimal.ZERO;
                        //                    if(omsCustOrdItem.getOrigUnitRetail()!=null)
                        //                    {
                        //                        discountTotal= omsCustOrdItem.getOrigUnitRetail().subtract(omsCustOrdItem.getUnitRetail()).multiply(omsCustOrdItem.getQtyOrderedSuom());
                        //                    }
                        discntLineDesc.setDiscountAmount(omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItemDisc.getUnitDiscountAmount()));
                        //log.info("Discount Total : " +omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItemDisc.getUnitDiscountAmount()));
                        log.info("Discount Total : " +
                                 omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItemDisc.getUnitDiscountAmount()));
                        //discntLineDesc.setCompletedDiscountAmount(BigDecimal.ZERO);

                        discntLineDesc.setUnitDiscountAmount(omsCustOrdItemDisc.getUnitDiscountAmount());

                        discntLineDesc.setCompletedDiscountAmount(omsCustOrdItem.getCumQtyDelivered().multiply(discntLineDesc.getUnitDiscountAmount()));
                        discntLineDesc.setCancelledDiscountAmount(omsCustOrdItem.getQtyCancelled().multiply(discntLineDesc.getUnitDiscountAmount()));
                        discntLineDesc.setReturnedDiscountAmount(omsCustOrdItem.getQtyReturned().multiply(discntLineDesc.getUnitDiscountAmount()));
                        discntLineDesc.setDiscountMethod(discntLineDesc.getDiscountMethod().AMT);
                        discntLineDesc.setDiscountScope(discntLineDesc.getDiscountScope().ITM);
                        //INCLUDED_IN_BESTDEAL_FLAG
                        discntLineDesc.setIncludedInBestdealFlag(discntLineDesc.getIncludedInBestdealFlag().N);
                        log.info("discntLineDesc adding for " + discntLineDesc.getLineNo());
                        discntLineDescList.add(discntLineDesc);
                        log.info("Inside for loop : " + discntLineDescList);
                    }
                    log.info("Outside for loop : " + discntLineDescList);
                }


            } else if (discountAmt.intValue() > 0) {

                List<OmsCustOrdItemDisc> omsCustOrdItemDiscList = null;
                try { //Adding discounts
                    log.info("Inside try of Discounts for discountAmt>0 value");
                    omsCustOrdItemDiscList =
                            session.getOmsCustOrdItemDiscFindByOmsCustordNoLineNo(omsCustOrdNo, omsCustOrdItem.getLineNo());
                } catch (Exception e) {
                    log.info("no records exist in item disc table when discount>0 ");
                }
                if (omsCustOrdItemDiscList.size() != 0 && omsCustOrdItemDiscList != null) {
                    log.info("omsCustOrdItemDiscList:" + omsCustOrdItemDiscList.size());
                    for (OmsCustOrdItemDisc omsCustOrdItemDisc : omsCustOrdItemDiscList) {

                        DiscntLineDesc discntLineDesc = new DiscntLineDesc();
                        log.info("omsCustOrdItemDisc.getDiscLineNo() : " +
                                 omsCustOrdItemDisc.getDiscLineNo().intValue());
                        discntLineDesc.setLineNo(omsCustOrdItemDisc.getDiscLineNo().intValue());
                        if (omsCustOrdItemDisc.getRmsPromoType() != null) {
                            log.info("omsCustOrdItemDisc.getRmsPromoType() : " + omsCustOrdItemDisc.getRmsPromoType());
                            discntLineDesc.setDiscountReasonCode(omsCustOrdItemDisc.getRmsPromoType());
                        }
                        if (omsCustOrdItemDisc.getDiscRefNo() != null) {
                            log.info("omsCustOrdItemDisc.getDiscRefNo() : " + omsCustOrdItemDisc.getDiscRefNo());
                            discntLineDesc.setPromotionId(omsCustOrdItemDisc.getDiscRefNo());
                        }
                        if (omsCustOrdItemDisc.getEmployeeId() != null) {
                            log.info("omsCustOrdItemDisc.getEmployeeId() : " + omsCustOrdItemDisc.getEmployeeId());
                            discntLineDesc.setDiscountEmployeeId(omsCustOrdItemDisc.getEmployeeId());
                        }
                        if (omsCustOrdItemDisc.getDiscountType() != null) {
                            log.info("omsCustOrdItemDisc.getDiscountType() : " + omsCustOrdItemDisc.getDiscountType());
                            discntLineDesc.setStoreCouponId(omsCustOrdItemDisc.getDiscountType());
                        }
                        if (omsCustOrdItemDisc.getUnitDiscountAmount() != null) {
                            log.info("omsCustOrdItemDisc.getUnitDiscountAmount() : " +
                                     omsCustOrdItemDisc.getUnitDiscountAmount());
                            discntLineDesc.setUnitDiscountAmount(omsCustOrdItemDisc.getUnitDiscountAmount());
                        }

                        if (omsCustOrdItemDisc.getPromoCompId() != null) {
                            log.info("omsCustOrdItemDisc.getPromoCompId() : " + omsCustOrdItemDisc.getPromoCompId());
                            discntLineDesc.setPromotionComponentId(omsCustOrdItemDisc.getPromoCompId());
                        }

                        //Added for Simple Promo
                        if (omsCustOrdItemDisc.getSimplePromoInd() != null) {
                            log.info("Discount Rule Id : " + omsCustOrdItemDisc.getSimplePromoInd());
                            discntLineDesc.setDiscountRuleId(omsCustOrdItemDisc.getSimplePromoInd());
                        }

                        log.info("***** discntLineDescList ***** " + discntLineDescList.size());


                        //accountingMethod
                        discntLineDesc.setAccountingMethod(discntLineDesc.getAccountingMethod().DISCOUNT);
                        //AdvancedPricingRuleFlag
                        log.info("discntLineDesc.getAdvancedPricingRuleFlag().N : " +
                                 discntLineDesc.getAdvancedPricingRuleFlag().N);
                        discntLineDesc.setAdvancedPricingRuleFlag(discntLineDesc.getAdvancedPricingRuleFlag().N);


                        //log.info("discntLineDesc.getAssignmentBasis().MANU : " +discntLineDesc.getAssignmentBasis().OTHR);
                        //discntLineDesc.setAssignmentBasis(discntLineDesc.getAssignmentBasis().OTHR);

                        // AssignmentBasis
                        if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) {
                            log.info("Its E-Commerce order so Assignment Basis is set to OTHR");
                            discntLineDesc.setAssignmentBasis(discntLineDesc.getAssignmentBasis().OTHR);
                            log.info("discntLineDesc.getAssignmentBasis() : " + discntLineDesc.getAssignmentBasis());
                        } else if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                            log.info("Its ORPOS order so Assignment Basis is set set based on request");
                            List<OmsOrposCustOrderHead> omsOrposCustOrderHeadList =
                                session.getOmsOrposCustOrderHeadfindColumns(custOrderId);
                            log.info("omsOrposCustOrderHeadList.size() " + omsOrposCustOrderHeadList.size());

                            if (omsOrposCustOrderHeadList.size() > 0) {
                                log.info("Orpos seq no " + omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId());
                                log.info(" %%%%%% omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId() : " +
                                         omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId());
                                log.info(" %%%%%% omsCustOrdItemDisc.getDiscLineNo() : " +
                                         omsCustOrdItemDisc.getDiscLineNo());
                                log.info(" %%%%%% omsCustOrdItemDisc.getLineNo() : " + omsCustOrdItemDisc.getLineNo());
                                OmsOrposDiscntLine omsOrposDiscntLine =
                                    session.getOmsOrposDiscntLineCustOrdIdLineNoDiscntLineNo(omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId(),
                                                                                             omsCustOrdItemDisc.getDiscLineNo(),
                                                                                             omsCustOrdItemDisc.getLineNo());
                                discntLineDesc.setAssignmentBasis(discntLineDesc.getAssignmentBasis().fromValue(omsOrposDiscntLine.getAssignmentBasis()));
                            }
                        }


                        //DAMAGE_DISCOUNT_FLAG
                        log.info("discntLineDesc.getDamageDiscountFlag().N : " +
                                 discntLineDesc.getDamageDiscountFlag().N);
                        discntLineDesc.setDamageDiscountFlag(discntLineDesc.getDamageDiscountFlag().N);


                        if (omsCustOrdItem.getRetailCurr() != null) {
                            log.info("omsCustOrdItem.getRetailCurr() : " + omsCustOrdItem.getRetailCurr());
                            discntLineDesc.setCurrencyCode(omsCustOrdItem.getRetailCurr());
                        }


                        discntLineDesc.setDiscountAmount(omsCustOrdItem.getQtyOrderedSuom().multiply(omsCustOrdItemDisc.getUnitDiscountAmount()));


                        discntLineDesc.setUnitDiscountAmount(omsCustOrdItemDisc.getUnitDiscountAmount());


                        discntLineDesc.setCompletedDiscountAmount(omsCustOrdItem.getCumQtyDelivered().multiply(discntLineDesc.getUnitDiscountAmount()));
                        discntLineDesc.setCancelledDiscountAmount(omsCustOrdItem.getQtyCancelled().multiply(discntLineDesc.getUnitDiscountAmount()));
                        discntLineDesc.setReturnedDiscountAmount(omsCustOrdItem.getQtyReturned().multiply(discntLineDesc.getUnitDiscountAmount()));
                        discntLineDesc.setDiscountMethod(discntLineDesc.getDiscountMethod().AMT);
                        discntLineDesc.setDiscountScope(discntLineDesc.getDiscountScope().ITM);
                        //INCLUDED_IN_BESTDEAL_FLAG
                        discntLineDesc.setIncludedInBestdealFlag(discntLineDesc.getIncludedInBestdealFlag().N);
                        log.info("discntLineDesc adding for " + discntLineDesc.getLineNo());
                        discntLineDescList.add(discntLineDesc);
                        log.info("Inside for loop : " + discntLineDescList);
                    }
                }
                DiscntLineDesc discntLineDesc1 = new DiscntLineDesc();
                log.info("omsCustOrdItemDiscList.size() " + omsCustOrdItemDiscList.size());
                discntLineDesc1.setLineNo(omsCustOrdItemDiscList.size() +
                                          1); //added this change for discounts line number for rma_restocking fee should not be separate
                // discntLineDesc1.setLineNo(omsCustOrdItem.getLineNo().intValue());
                discntLineDesc1.setDiscountReasonCode("RMA_RESTOCKING_FEE");
                // discntLineDesc1.setPromotionId(BigDecimal.ONE);
                // discntLineDesc1.setDiscountEmployeeId("aaa");
                discntLineDesc1.setStoreCouponId("bbb");
                // discntLineDesc1.setPromotionComponentId(BigDecimal.ZERO);
                discntLineDesc1.setAccountingMethod(discntLineDesc1.getAccountingMethod().DISCOUNT);
                discntLineDesc1.setAdvancedPricingRuleFlag(discntLineDesc1.getAdvancedPricingRuleFlag().N);
                discntLineDesc1.setAssignmentBasis(discntLineDesc1.getAssignmentBasis().MANU);
                discntLineDesc1.setDamageDiscountFlag(discntLineDesc1.getDamageDiscountFlag().N);
                discntLineDesc1.setCurrencyCode(omsCustOrdItem.getRetailCurr());

                BigDecimal discountTotal = BigDecimal.ZERO;
                if (omsCustOrdItem.getOrigUnitRetail() != null) {
                    discountTotal =
                            omsCustOrdItem.getOrigUnitRetail().subtract(omsCustOrdItem.getUnitRetail()).multiply(omsCustOrdItem.getQtyOrderedSuom());
                }
                discntLineDesc1.setDiscountAmount(discountAmt);
                log.info("discountTotal.multiply(omsCustOrdItem.getCumQtyDelivered()) : " +
                         discountTotal.multiply(omsCustOrdItem.getCumQtyDelivered()));


                /* discntLineDesc1.setCompletedDiscountAmount(omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getOrigUnitRetail()));
                    discntLineDesc1.setCancelledDiscountAmount(omsCustOrdItem.getQtyCancelled().multiply(omsCustOrdItem.getOrigUnitRetail()));
                    discntLineDesc1.setReturnedDiscountAmount(omsCustOrdItem.getQtyReturned().multiply(omsCustOrdItem.getOrigUnitRetail())); */

                /*  discntLineDesc1.setCompletedDiscountAmount(BigDecimal.ZERO);
                    discntLineDesc1.setCancelledDiscountAmount(BigDecimal.ZERO);
                    discntLineDesc1.setReturnedDiscountAmount(BigDecimal.ZERO);  */

                //  discntLineDesc1.setCompletedDiscountAmount(omsCustOrdItem.getCumQtyDelivered().multiply();
                // log.info("discountTotal.multiply(omsCustOrdItem.getQtyCancelled() : " +discountTotal.multiply(omsCustOrdItem.getQtyCancelled()));
                //discntLineDesc1.setCancelledDiscountAmount(discountTotal.multiply(omsCustOrdItem.getQtyCancelled()));
                //discntLineDesc1.setReturnedDiscountAmount(BigDecimal.ZERO);

                if (omsCustOrdItem.getOrigUnitRetail() != null) {
                    log.info("inside if CancelledQuantity > 0");
                    BigDecimal unitDisAmt = BigDecimal.ZERO;
                    if (omsCustOrdItem.getRetailCurr().equals("SAR") || omsCustOrdItem.getRetailCurr().equals("USD") ||
                        omsCustOrdItem.equals("GPP")) {
                        unitDisAmt =
                                (discountAmt.divide(custOrdItmDesc.getCancelledQuantity(), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        unitDisAmt =
                                (discountAmt.divide(custOrdItmDesc.getCancelledQuantity(), 3, BigDecimal.ROUND_HALF_UP));
                    }
                    log.info("discountAmt.divide(custOrdItmDesc.getQtyCancelled()) " + unitDisAmt);
                    discntLineDesc1.setUnitDiscountAmount(unitDisAmt);


                } else {
                    discntLineDesc1.setUnitDiscountAmount(BigDecimal.ZERO);
                }

                ////Adding the code for defect 2449 - RMA_RESTOCKING_FEE
                //discntLineDesc1.setCompletedDiscountAmount(omsCustOrdItem.getCumQtyDelivered().multiply(discntLineDesc1.getUnitDiscountAmount()));
                discntLineDesc1.setCompletedDiscountAmount(BigDecimal.ZERO);
                discntLineDesc1.setCancelledDiscountAmount(omsCustOrdItem.getQtyCancelled().multiply(discntLineDesc1.getUnitDiscountAmount()));
                discntLineDesc1.setReturnedDiscountAmount(omsCustOrdItem.getQtyReturned().multiply(discntLineDesc1.getUnitDiscountAmount()));
                discntLineDesc1.setDiscountMethod(discntLineDesc1.getDiscountMethod().AMT);
                discntLineDesc1.setDiscountScope(discntLineDesc1.getDiscountScope().ITM);
                //INCLUDED_IN_BESTDEAL_FLAG
                discntLineDesc1.setIncludedInBestdealFlag(discntLineDesc1.getIncludedInBestdealFlag().Y);
                log.info("discntLineDesc adding for " + discntLineDesc1.getLineNo());
                discntLineDescList.add(discntLineDesc1);


            }
            log.info("Outside for loop : " + discntLineDescList);
            //}


        } catch (Exception e) {
            return null;
        }
        return discntLineColDesc;
    }


    public CustOrdFulColDesc createResponseForCustOrdFul(String custOrderId) throws SOAPException {
        log.info("inside createResponseForCustOrdFul");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        CustOrdFulColDesc custOrdFulColDesc = new CustOrdFulColDesc();
        List<CustOrdFulDesc> custOrdFulDescList = custOrdFulColDesc.getCustOrdFulDesc();

        log.info("custOrderId " + custOrderId);
        BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(custOrderId);
        log.info("omsCustOrdNo " + omsCustOrdNo);
        OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        List<OmsCustOrdItem> omsCustOrdItemList = null;
        List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
        CustOrdFulDesc custOrdFulDesc = new CustOrdFulDesc();
        int i = 0;
        try {
            omsCustOrdItemList = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
            omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);
            for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
                custOrdFulDesc = new CustOrdFulDesc();
                custOrdFulColDesc.setCollectionSize(i);

                log.info("before FulfillOrderSeqNo");
                custOrdFulDesc.setSeqNo(i);

                log.info("before getFulfillOrderId()");
                custOrdFulDesc.setFulfillOrderId(omsCoFulfillDetail.getFulfillOrderNo().toString());
                if (omsCoFulfillDetail.getFulfillLocType() != null) {
                    FulfillLocType fulfillLocType = custOrdFulDesc.getFulfillLocType();
                    if (omsCoFulfillDetail.getFulfillLocType().equals("V")) {
                        custOrdFulDesc.setFulfillLocType(fulfillLocType.fromValue("W"));
                    } else {
                        custOrdFulDesc.setFulfillLocType(fulfillLocType.fromValue(omsCoFulfillDetail.getFulfillLocType()));
                    }
                }
                //custOrdFulDesc.getFulfillLocType().fromValue(omsOrposCustOrdFul.getFulfillLocType());
                log.info("omsOrposCustOrdFul.getFulfillLocType()" + omsCoFulfillDetail.getFulfillLocType());
                custOrdFulDesc.setFulfillLocId(omsCoFulfillDetail.getFulfillLoc().longValue());
                if (omsCustOrdHead.getDeliveryType() != null) {
                    DeliveryType deliveryType = custOrdFulDesc.getDeliveryType();
                    custOrdFulDesc.setDeliveryType(deliveryType.fromValue(omsCustOrdHead.getDeliveryType()));
                }
                //PARTIAL_DELIVERY_IND
                custOrdFulDesc.setPartialDeliveryInd(custOrdFulDesc.getPartialDeliveryInd().fromValue("Y"));
                //SHIP_TO_FULFILL_LOC_FLAG
                //check the value
                custOrdFulDesc.setShipToFulfillLocFlag(custOrdFulDesc.getShipToFulfillLocFlag().fromValue("Y"));
                //CONSUMER_DELIVERY_DATE
                if (omsCustOrdHead.getConsumerDlyTime() != null) {
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    DatatypeFactory datatypeFactory = null;
                    try {
                        datatypeFactory = DatatypeFactory.newInstance();
                    } catch (DatatypeConfigurationException f) {
                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(custOrdFulDesc.toString()));
                    }
                    XMLGregorianCalendar consumerDeliveryDate =
                        datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                    Calendar consumerDeliveryDateResp = Calendar.getInstance();
                    consumerDeliveryDateResp.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
                    consumerDeliveryDate.setMonth(consumerDeliveryDateResp.get(Calendar.MONTH) + 1);
                    consumerDeliveryDate.setYear(consumerDeliveryDateResp.get(Calendar.YEAR));
                    consumerDeliveryDate.setDay(consumerDeliveryDateResp.get(Calendar.DAY_OF_MONTH));
                    custOrdFulDesc.setConsumerDeliveryDate(consumerDeliveryDate);
                }
                //CONSUMER_DELIVERY_TIME
                if (omsCustOrdHead.getConsumerDlyTime() != null) {
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    DatatypeFactory datatypeFactory = null;
                    try {
                        datatypeFactory = DatatypeFactory.newInstance();
                    } catch (DatatypeConfigurationException f) {
                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(custOrdFulDesc.toString()));
                    }
                    XMLGregorianCalendar consumerDeliveryTime =
                        datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                    Calendar consumerDeliveryTimeResp = Calendar.getInstance();
                    consumerDeliveryTimeResp.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
                    consumerDeliveryTime.setMonth(consumerDeliveryTimeResp.get(Calendar.MONTH) + 1);
                    consumerDeliveryTime.setYear(consumerDeliveryTimeResp.get(Calendar.YEAR));
                    consumerDeliveryTime.setDay(consumerDeliveryTimeResp.get(Calendar.DAY_OF_MONTH));
                    custOrdFulDesc.setConsumerDeliveryTime(consumerDeliveryTime);
                }

                DeliveryDestDtl deliveryDestDtl = new DeliveryDestDtl();

                log.info("after DeliveryDestDtl");

                OmsCustOrdAddress omsCustOrdAddress = null;
                try {
                    omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
                } catch (Exception e) {

                }
                ContactDesc contactDesc = new ContactDesc();
                log.info("contactDesc------------");
                if (omsCustOrdHead.getCustFirstName() != null) {
                    contactDesc.setFirstName(omsCustOrdHead.getCustFirstName());
                    log.info("omsOrposContact.getFirstName()");
                }
                if (omsCustOrdHead.getCustLastName() != null) {
                    contactDesc.setLastName(omsCustOrdHead.getCustLastName());
                }
                deliveryDestDtl.setContactDesc(contactDesc);
                //    phones

                Phones phones = new Phones();
                List<PhoneDesc> phoneDescList = phones.getPhoneDesc();
                PhoneDesc phoneDesc = new PhoneDesc();
                phoneDesc.setPhoneId(BigDecimal.ONE);
                if (omsCustOrdHead.getCustPhoneNo() != null) {
                    phoneDesc.setPhoneNumber(omsCustOrdHead.getCustPhoneNo());
                }
                phoneDesc.setPhoneType(phoneDesc.getPhoneType().HOME);
                phoneDesc.setPrimaryPhoneInd("Y");
                phoneDescList.add(phoneDesc);
                contactDesc.setPhones(phones);

                // EMAILS
                BillingDestDtl billingDestDtl = new BillingDestDtl();
                log.info("after BillingDestDtl");
                billingDestDtl.setContactDesc(contactDesc);
                //GeoAddress
                try {
                    GeoAddrDesc geoAddrDesc = new GeoAddrDesc();
                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverAdd1() != null) {
                        geoAddrDesc.setAddress1(omsCustOrdAddress.getDeliverAdd1());
                    }
                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverAdd2() != null) {
                        geoAddrDesc.setAddress2(omsCustOrdAddress.getDeliverAdd2());
                    }
                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverAdd3() != null) {
                        geoAddrDesc.setAddress3(omsCustOrdAddress.getDeliverAdd3());
                    }

                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverCity() != null) {
                        geoAddrDesc.setCity(omsCustOrdAddress.getDeliverCity());
                    }

                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverCountry() != null) {
                        geoAddrDesc.setCountryCode(omsCustOrdAddress.getDeliverCountry());
                    }
                    deliveryDestDtl.setGeoAddrDesc(geoAddrDesc);
                    billingDestDtl.setGeoAddrDesc(geoAddrDesc);
                } catch (Exception e) {

                }
                custOrdFulDesc.setDeliveryDestDtl(deliveryDestDtl);
                contactDesc.setPhones(phones);
                custOrdFulDesc.setBillingDestDtl(billingDestDtl);
                custOrdFulDescList.add(custOrdFulDesc);
                i++;
            }

        } catch (Exception e) {
            log.info("no record found in OmsCoFulfillDetail table now checking for, BackorderDtl ");

        }


        if (omsCustOrdItemList.size() > omsCoFulfillDetailList.size()) {
            List<OmsCustOrdItem> omsCustOrdItemRow =
                session.getOmsCustOrdItemFindByBOIndAndOmsCustNo(omsCustOrdNo, "Y");

            log.info("omsCustOrdItemList.size()" + omsCustOrdItemList.size());
            for (OmsCustOrdItem omsCustOrdItemRecord : omsCustOrdItemRow) {
                try {
                    List<OmsCoFulfillDetail> omsCoFulfillDetail =
                        session.getOmsCoFulfillDetailFindByItem(omsCustOrdNo, omsCustOrdItemRecord.getLineNo(),
                                                                omsCustOrdItemRecord.getItem());
                    if (omsCoFulfillDetail.size() == 0) {

                        // CustOrdFulDesc custOrdFulDesc=new CustOrdFulDesc();
                        log.info("i " + i);
                        custOrdFulColDesc =
                                backOrder_Detail(omsCustOrdHead, i, omsCustOrdItemRecord, custOrdFulDesc, custOrdFulColDesc,
                                                 custOrdFulDescList);
                        //custOrdFulDescList.add(custOrdFulDesc);
                        i++;
                    }
                } catch (Exception e) {

                }

            }
        }

        return custOrdFulColDesc;

    }


    public PaymentColDesc createResponseForPayment(String custOrderId) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        String tenderTypeId = null;
        String tenderTypeGroup = null;
        List<OmsOrposPayment> omsOrposPayment = null;
        try {
            PaymentColDesc paymentColDesc = new PaymentColDesc();
            paymentColDesc.setCollectionSize(4);
            log.info("inside payment");
            List<PaymentDesc> paymentDescList = paymentColDesc.getPaymentDesc();
            log.info("omsCustOrdNo " + omsCustOrdNo);
            List<OmsCustOrdTender> omsCustOrdTenderList = session.getOmsCustOrdTenderFindByOmsCustOrdNo(omsCustOrdNo);
            log.info("omsCustOrdTenderList.size() " + omsCustOrdTenderList.size());
            try {
                omsOrposCustOrderId = session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderId);
                omsOrposPayment = session.getOmsOrposPaymentFindByOmsOrposCustOrdId(omsOrposCustOrderId);
            } catch (Exception e) {

            }
            OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
            for (OmsCustOrdTender omsCustOrdTender : omsCustOrdTenderList) {
                log.info("inside omsCustOrdTender loop");
                PaymentDesc paymentDesc = new PaymentDesc();
                log.info("omsCustOrdTender.getTenderSeqNo().intValue() " +
                         omsCustOrdTender.getTenderSeqNo().intValue());
                paymentDesc.setSeqNo(omsCustOrdTender.getTenderSeqNo().intValue());
                PaymentType paymentType = paymentDesc.getPaymentType();
                tenderTypeId = omsCustOrdTender.getTenderTypeId().toString();
                log.info("tenderTypeId " + tenderTypeId);
                tenderTypeGroup = session.getOmsSystemParameterFindByParameterValue(tenderTypeId);
                log.info("tenderTypeGroup " + tenderTypeGroup);
                paymentDesc.setPaymentType(paymentType.fromValue(tenderTypeGroup));
                paymentDesc.setCurrencyCode(currencyCode);
                if (omsCustOrdTender.getTenderAmt() != null) {
                    paymentDesc.setAmount(omsCustOrdTender.getTenderAmt());
                }


                log.info("paymentDesc.getPaymentType() " + paymentDesc.getPaymentType());
                //================================================CHECK==============================================
                if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("CHECK"))) {

                    CheckTender checkTender = new CheckTender();
                    log.info("inside the CHECK if condition");
                       
                    if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                        log.info("inside if condition when application id is ORPOS");
                        for (OmsOrposPayment omsOrposPaymentLoop : omsOrposPayment) {
                            log.info("inside OmsOrposPayment loop for CheckTender - if application id is - ORPOS");
                            if (omsOrposPaymentLoop.getPaymentType().equals("CHECK")) {


                                log.info("inside checkTender");
                                if (omsOrposPaymentLoop.getCertificateType() != null) {
                                    log.info("omsOrposPaymentLoop.getBankId()() " + omsOrposPaymentLoop.getBankId());
                                    checkTender.setBankId(omsOrposPaymentLoop.getBankId());
                                }
                                if (omsOrposPaymentLoop.getAccountNumber() != null) {
                                    log.info("omsOrposPaymentLoop.getAccountNumber() " +
                                             omsOrposPaymentLoop.getAccountNumber());
                                    checkTender.setAccountNumber(checkNullValueForString(omsOrposPaymentLoop.getAccountNumber()));
                                }
                                if (omsOrposPaymentLoop.getMicrNumber() != null) {
                                    log.info("omsOrposPaymentLoop.getMicrNumber() " +
                                             omsOrposPaymentLoop.getMicrNumber());
                                    checkTender.setMicrNumber(omsOrposPaymentLoop.getMicrNumber());
                                }
                                if (omsOrposPaymentLoop.getCheckNumber() != null) {
                                    log.info("omsOrposPaymentLoop.getCheckNumber() " +
                                             omsOrposPaymentLoop.getCheckNumber());
                                    checkTender.setCheckNumber(omsOrposPaymentLoop.getCheckNumber());
                                }
                                if (omsOrposPaymentLoop.getAuthorizationCode() != null) {
                                    log.info("omsOrposPaymentLoop.getAuthorizationCode() " +
                                             omsOrposPaymentLoop.getAuthorizationCode());
                                    checkTender.setAuthorizationCode(omsOrposPaymentLoop.getAuthorizationCode());
                                }

                                log.info("omsOrposPaymentLoop.getAuthorizationMethod() " + AuthorizationMethod.AUTO);
                                checkTender.setAuthorizationMethod(AuthorizationMethod.AUTO);

                                log.info("omsOrposPaymentLoop.getPersonalIdSwipedFlag() " + "N");
                                checkTender.setPersonalIdSwipedFlag(PersonalIdSwipedFlag.N);
                                paymentDesc.setCheckTender(checkTender);

                            } //end of if
                        } //end of for
                    } //end if
                    else if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) 
                    {
                        log.info("");
                        if(omsCustOrdTender.getTenderRefId()!=null)
                        {
                            checkTender.setBankId("BANK");
                            checkTender.setAccountNumber("ACCOUNT");
                            checkTender.setMicrNumber("MICR");
                            checkTender.setCheckNumber(omsCustOrdTender.getTenderRefId());
                            checkTender.setAuthorizationCode("AAA");
                            paymentDesc.setCheckTender(checkTender);
                        }
                    }
                }
                //================================================================================================================================
                if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("PURCHASEORDER"))) {
                    PurchaseOrdTender purchaseOrdTender = new PurchaseOrdTender();
                    log.info("inside the PURCHASEORDER if condition");
                   
                    if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                        log.info("inside if condition when application id is ORPOS");
                        for (OmsOrposPayment omsOrposPaymentLoop : omsOrposPayment) {
                            log.info("inside OmsOrposPayment loop for PURCHASEORDER - if application id is - ORPOS");
                            if (omsOrposPaymentLoop.getPaymentType().equals("PURCHASEORDER")) {


                                log.info("inside PURCHASEORDER");
                                if (omsOrposPaymentLoop.getAgentName() != null) {
                                    log.info("omsOrposPaymentLoop.getBankId()() " +
                                             omsOrposPaymentLoop.getAgentName());
                                    purchaseOrdTender.setAgentName(omsOrposPaymentLoop.getAgentName());
                                }
                                if (omsOrposPaymentLoop.getPurchaseOrderNumber() != null) {
                                    log.info("omsOrposPaymentLoop.getPurchaseOrderNumber() " +
                                             omsOrposPaymentLoop.getPurchaseOrderNumber());
                                    purchaseOrdTender.setPurchaseOrderNumber(checkNullValueForString(omsOrposPaymentLoop.getPurchaseOrderNumber()));
                                }

                                paymentDesc.setPurchaseOrdTender(purchaseOrdTender);

                            } //end of if
                        } //end of for
                    } //end if
                    else if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE"))
                    {
                        log.info("************* Purchase Order for Ecommerce *****************");
                        purchaseOrdTender.setPurchaseOrderNumber(omsCustOrdTender.getTenderRefId());
                        purchaseOrdTender.setAgentName("CUSTOMER");
                        log.info("*****omsCustOrdTender.getTenderRefId() : "+omsCustOrdTender.getTenderRefId()+"***************");
                        paymentDesc.setPurchaseOrdTender(purchaseOrdTender);
                    }   
                 
                }
                
                //=================================================STORECREDIT=====================================================================
                if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("STORECREDIT"))) {
                    StoreCreditTender storeCreditTender = new StoreCreditTender();
                    log.info("inside the STORECREDIT if condition");
                  
                    if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                       log.info("inside if condition when application id is ORPOS");
                           for (OmsOrposPayment omsOrposPaymentLoop : omsOrposPayment) {
                            log.info("inside OmsOrposPayment loop for STORECREDIT - if application id is - ORPOS");
                            if (omsOrposPaymentLoop.getPaymentType().equals("STORECREDIT")) {
                                log.info("inside store credit tender");
                                if (omsOrposPaymentLoop.getCertificateType() != null) {
                                    log.info("omsOrposPaymentLoop.getCertificateType() " +
                                             omsOrposPaymentLoop.getCertificateType());
                                    storeCreditTender.setCertificateType(CertificateType.valueOf(omsOrposPaymentLoop.getCertificateType()));
                                }
                                if (omsOrposPaymentLoop.getFirstName() != null) {
                                    log.info("omsOrposPaymentLoop.getFirstName() " +
                                             omsOrposPaymentLoop.getFirstName());
                                    storeCreditTender.setFirstName(checkNullValueForString(omsOrposPaymentLoop.getFirstName()));
                                }
                                if (omsOrposPaymentLoop.getLastName() != null) {
                                    log.info("omsOrposPaymentLoop.getLastName() " + omsOrposPaymentLoop.getLastName());
                                    storeCreditTender.setLastName(omsOrposPaymentLoop.getLastName());
                                }
                                if (omsOrposPaymentLoop.getPersonalIdType() != null) {
                                    log.info("omsOrposPaymentLoop.getPersonalIdType() " +
                                             omsOrposPaymentLoop.getPersonalIdType());
                                    storeCreditTender.setPersonalIdType(omsOrposPaymentLoop.getPersonalIdType());
                                }
                                if (omsOrposPaymentLoop.getState() != null) {
                                    log.info("omsOrposPaymentLoop.getState() " + omsOrposPaymentLoop.getState());
                                    storeCreditTender.setState(State.valueOf(omsOrposPaymentLoop.getState()));
                                }
                                if (omsOrposPaymentLoop.getStoreCreditId() != null) {
                                    log.info("omsOrposPaymentLoop.getStoreCreditId() " +
                                             omsOrposPaymentLoop.getStoreCreditId());
                                    storeCreditTender.setStoreCreditId(omsOrposPaymentLoop.getStoreCreditId());
                                }
                                paymentDesc.setStoreCreditTender(storeCreditTender);

                            } //end of if
                       } //end of for
                   } //end if
                    
                   // E-commerce 
                   else if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) 
                    {
                        if(omsCustOrdTender.getTenderRefId()!=null)
                        {
                            storeCreditTender.setStoreCreditId(omsCustOrdTender.getTenderRefId());
                            storeCreditTender.setCertificateType(CertificateType.STORE);
                            storeCreditTender.setState(State.REDEEM);
                            paymentDesc.setStoreCreditTender(storeCreditTender);
                        }
                    }
                 
                }
                //==================================================================================================================================

                else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("CREDIT")) ||
                         paymentDesc.getPaymentType().equals(PaymentType.valueOf("DEBIT"))) {
                    CreditDebitTender creditDebitTender = new CreditDebitTender();
                    log.info("Credit debit");
                    if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                        for (OmsOrposPayment omsOrposPaymentLoop : omsOrposPayment) {
                            if (omsOrposPaymentLoop.getPaymentType().equals("CREDIT") ||
                                omsOrposPaymentLoop.getPaymentType().equals("DEBIT")) {
                                log.info("Inside if loop of credit/debit");
                                if (omsOrposPaymentLoop.getMaskedAccountNumber() != null) {
                                    log.info("omsOrposPaymentLoop.getMaskedAccountNumber()");
                                    creditDebitTender.setMaskedAccountNumber(omsOrposPaymentLoop.getMaskedAccountNumber());
                                }

                                if (omsOrposPaymentLoop.getCardToken() != null) {
                                    log.info("omsOrposPaymentLoop.getCardToken() : " +
                                             omsOrposPaymentLoop.getCardToken());
                                    creditDebitTender.setCardToken(omsOrposPaymentLoop.getCardToken());
                                }


                                if (omsOrposPaymentLoop.getCardType() != null) {
                                    log.info("omsOrposPaymentLoop.getCardType() : " +
                                             omsOrposPaymentLoop.getCardType());
                                    creditDebitTender.setCardType(creditDebitTender.getCardType().fromValue(omsOrposPaymentLoop.getCardType()));
                                }
                                if (omsOrposPaymentLoop.getEntryMethod() != null) {
                                    creditDebitTender.setEntryMethod(creditDebitTender.getEntryMethod().fromValue(omsOrposPaymentLoop.getEntryMethod()));
                                }
                                if (omsOrposPaymentLoop.getAuthorizationCode() != null) {
                                    creditDebitTender.setAuthorizationCode(omsOrposPaymentLoop.getAuthorizationCode());
                                }
                                if (omsOrposPaymentLoop.getAuthorizationDatetime() != null) {
                                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                                    DatatypeFactory datatypeFactory = null;
                                    try {
                                        datatypeFactory = DatatypeFactory.newInstance();
                                    } catch (DatatypeConfigurationException f) {
                                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(creditDebitTender.toString()));
                                    }
                                    XMLGregorianCalendar authorizationDateTime =
                                        datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                                    Calendar authorizationDateTimeResp = Calendar.getInstance();
                                    authorizationDateTimeResp.setTimeInMillis(omsCustOrdTender.getCreateDatetime().getTime());
                                    authorizationDateTime.setMonth(authorizationDateTimeResp.get(Calendar.MONTH) + 1);
                                    authorizationDateTime.setYear(authorizationDateTimeResp.get(Calendar.YEAR));
                                    authorizationDateTime.setDay(authorizationDateTimeResp.get(Calendar.DAY_OF_MONTH));
                                    creditDebitTender.setAuthorizationDatetime(authorizationDateTime);
                                    //creditDebitTender.setAuthorizationDatetime(authorizationDateTime);
                                }
                                if (omsOrposPaymentLoop.getAuthorizationMethod() != null) {
                                    log.info("omsOrposPaymentLoop.getAuthorizationMethod(): " +
                                             omsOrposPaymentLoop.getAuthorizationMethod());
                                    creditDebitTender.setAuthorizationMethod(creditDebitTender.getAuthorizationMethod().fromValue(omsOrposPaymentLoop.getAuthorizationMethod()));
                                }
                                if (omsOrposPaymentLoop.getPersonalIdCountry() != null) {
                                    log.info("omsOrposPaymentLoop.getPersonalIdCountry() : " +
                                             omsOrposPaymentLoop.getPersonalIdCountry());
                                    creditDebitTender.setPersonalIdCountry(omsOrposPaymentLoop.getPersonalIdCountry());
                                }
                                if (omsOrposPaymentLoop.getPersonalIdState() != null) {
                                    log.info("omsOrposPaymentLoop.getPersonalIdState() : " +
                                             omsOrposPaymentLoop.getPersonalIdState());
                                    creditDebitTender.setPersonalIdState(omsOrposPaymentLoop.getPersonalIdState());
                                }

                                if (omsOrposPaymentLoop.getPersonalIdExpirationDate() != null) {
                                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                                    DatatypeFactory datatypeFactory = null;
                                    try {
                                        datatypeFactory = DatatypeFactory.newInstance();
                                    } catch (DatatypeConfigurationException f) {
                                        throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(creditDebitTender.toString()));
                                    }
                                    XMLGregorianCalendar authorizationDateTime =
                                        datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                                    Calendar authorizationDateTimeResp = Calendar.getInstance();
                                    authorizationDateTimeResp.setTimeInMillis(omsCustOrdTender.getCreateDatetime().getTime());
                                    authorizationDateTime.setMonth(authorizationDateTimeResp.get(Calendar.MONTH) + 1);
                                    authorizationDateTime.setYear(authorizationDateTimeResp.get(Calendar.YEAR));
                                    authorizationDateTime.setDay(authorizationDateTimeResp.get(Calendar.DAY_OF_MONTH));
                                    creditDebitTender.setAuthorizationDatetime(authorizationDateTime);
                                    //creditDebitTender.setAuthorizationDatetime(authorizationDateTime);
                                }
                                if (omsOrposPaymentLoop.getPrepaidBalance() != null) {
                                    log.info("omsOrposPaymentLoop.getPrepaidBalance() : " +
                                             omsOrposPaymentLoop.getPrepaidBalance());
                                    creditDebitTender.setPrepaidBalance(omsOrposPaymentLoop.getPrepaidBalance());
                                }
                                if (omsOrposPaymentLoop.getAccountApr() != null) {
                                    log.info("omsOrposPaymentLoop.getAccountApr() : " +
                                             omsOrposPaymentLoop.getAccountApr());
                                    creditDebitTender.setAccountApr(omsOrposPaymentLoop.getAccountApr());
                                }
                                if (omsOrposPaymentLoop.getAccountAprType() != null) {
                                    log.info("omsOrposPaymentLoop.getAccountAprType() : " +
                                             omsOrposPaymentLoop.getAccountAprType());
                                    creditDebitTender.setAccountAprType(omsOrposPaymentLoop.getAccountAprType());
                                }
                                if (omsOrposPaymentLoop.getPromotionApr() != null) {
                                    log.info("omsOrposPaymentLoop.getPromotionApr() : " +
                                             omsOrposPaymentLoop.getPromotionApr());
                                    creditDebitTender.setPromotionApr(omsOrposPaymentLoop.getPromotionApr());
                                }
                                if (omsOrposPaymentLoop.getPromotionAprType() != null) {
                                    log.info("omsOrposPaymentLoop.getPromotionAprType() : " +
                                             omsOrposPaymentLoop.getPromotionAprType());
                                    creditDebitTender.setPromotionAprType(omsOrposPaymentLoop.getPromotionAprType());
                                }
                                if (omsOrposPaymentLoop.getPromotionDescription() != null) {
                                    log.info("omsOrposPaymentLoop.getPromotionDescription() : " +
                                             omsOrposPaymentLoop.getPromotionDescription());
                                    creditDebitTender.setPromotionDescription(omsOrposPaymentLoop.getPromotionDescription());
                                }
                                if (omsOrposPaymentLoop.getPromotionDuration() != null) {
                                    log.info("omsOrposPaymentLoop.getPromotionDuration() :" +
                                             omsOrposPaymentLoop.getPromotionDuration());
                                    creditDebitTender.setPromotionDuration(omsOrposPaymentLoop.getPromotionDuration());
                                }

                                if (omsOrposPaymentLoop.getSettlementData() != null) {
                                    log.info("omsOrposPaymentLoop.getSettlementData() :" +
                                             omsOrposPaymentLoop.getSettlementData());
                                    creditDebitTender.setSettlementData(omsOrposPaymentLoop.getSettlementData());
                                }
                                if (omsOrposPaymentLoop.getAdditionalSecurityInfo() != null) {
                                    log.info("omsOrposPaymentLoop.getAdditionalSecurityInfo() : " +
                                             omsOrposPaymentLoop.getAdditionalSecurityInfo());
                                    creditDebitTender.setAdditionalSecurityInfo(omsOrposPaymentLoop.getAdditionalSecurityInfo());
                                }


                                log.info("$$$$$$$$$$$$$$$$$$");
                            }
                            log.info("******************");
                            paymentDesc.setCreditDebitTender(creditDebitTender);

                        }
                    } else {
                        if (omsCustOrdTender.getCcNo() != null)

                        {
                            try {
                                creditDebitTender.setMaskedAccountNumber(omsCustOrdTender.getCcNo());
                            } catch (Exception e) {
                                log.error("msk act no" + e);
                            }
                        }
                        log.info("Inside CreditDebitTender");

                        if (omsCustOrdTender.getCcNo() != null) {
                            log.info(":inside card token");
                            creditDebitTender.setCardToken(omsCustOrdTender.getCcNo());
                            //creditDebitTender.setCardToken(omsOrposPayment.getCardToken());
                        }

                        com.oracle.retail.integration.base.bo.paymentdesc.v1.EntryMethod entryMethod =
                            creditDebitTender.getEntryMethod();
                        creditDebitTender.setEntryMethod(entryMethod.MANUAL);
                        log.info("aftet entry method");
                        //creditDebitTender.setEntryMethod(creditDebitTender.getEntryMethod().fromValue(omsOrposPayment.getEntryMethod()));

                        if (omsCustOrdTender.getCcAuthNo() != null) {
                            creditDebitTender.setAuthorizationCode(omsCustOrdTender.getCcAuthNo());
                            //creditDebitTender.setAuthorizationCode(omsOrposPayment.getAuthorizationCode());
                        }
                        //authorization_datetime
                        if (omsCustOrdTender.getCreateDatetime() != null) {
                            GregorianCalendar gregorianCalendar = new GregorianCalendar();
                            DatatypeFactory datatypeFactory = null;
                            try {
                                datatypeFactory = DatatypeFactory.newInstance();
                            } catch (DatatypeConfigurationException f) {
                                throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(creditDebitTender.toString()));
                            }
                            XMLGregorianCalendar authorizationDateTime =
                                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                            Calendar authorizationDateTimeResp = Calendar.getInstance();
                            authorizationDateTimeResp.setTimeInMillis(omsCustOrdTender.getCreateDatetime().getTime());
                            authorizationDateTime.setMonth(authorizationDateTimeResp.get(Calendar.MONTH) + 1);
                            authorizationDateTime.setYear(authorizationDateTimeResp.get(Calendar.YEAR));
                            authorizationDateTime.setDay(authorizationDateTimeResp.get(Calendar.DAY_OF_MONTH));
                            creditDebitTender.setAuthorizationDatetime(authorizationDateTime);
                            //creditDebitTender.setAuthorizationDatetime(authorizationDateTime);
                            paymentDesc.setCreditDebitTender(creditDebitTender);

                        }
                        log.info("before authorization");
                        // AuthorizationMethod authorizationMethod=creditDebitTender.getAuthorizationMethod();
                        /*  if(omsCustOrdTender.getCcAuthSrc()!=null)
                                        {
                                    creditDebitTender.setAuthorizationMethod(paymentDesc.getCreditDebitTender().getAuthorizationMethod().MANU);
                                     //creditDebitTender.setAuthorizationMethod(creditDebitTender.getAuthorizationMethod().fromValue(omsOrposPayment.getAuthorizationMethod()));
                                         log.info(" creditDebitTender.setAuthorizationMethod"+ creditDebitTender.getAuthorizationMethod());
                                     }
                                     */
                        /* SET SIGNATURE DATA */
                        /*  if(omsOrposPayment.getSignatureData()!=null){
                                    creditDebitTender.setSignatureData(omsOrposPayment.getSignatureData());
                                    }          */
                        paymentDesc.setCreditDebitTender(creditDebitTender);
                    }

                    log.info("Credit debit set");
                    CheckTender checkTender = new CheckTender();

                    if (omsCustOrdTender.getTenderRefId() != null) {
                        checkTender.setCheckNumber(omsCustOrdTender.getTenderRefId().toString());
                        //checkTender.setCheckNumber(omsOrposPayment.getCheckNumber());
                    }
                    log.info("check tender");
                    log.info("before coupon tender");
                    //  paymentDescList.add(paymentDesc);
                } //end of if - payment type is credit or debit

                else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("GIFTCARD"))) //for gift card tender
                {
                    GiftCardTender giftCardTender = new GiftCardTender();
                    log.info("inside the gift card if condition");
                    if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                        for (OmsOrposPayment omsOrposPaymentLoop : omsOrposPayment) {
                            log.info("inside OmsOrposPayment loop for gift card - if application id is - ORPOS");
                            if (omsOrposPaymentLoop.getPaymentType().equals("GIFTCARD")) {
                                log.info("inside the omsOrposPayment loop from database");
                                log.info("" + omsOrposPaymentLoop.getCardNumber());
                                if (omsOrposPaymentLoop.getCardNumber() != null) {
                                    giftCardTender.setCardNumber(omsOrposPaymentLoop.getCardNumber());
                                    log.info("*********card number is set : " +omsOrposPaymentLoop.getCardNumber()+"************");
                                }
                                if (omsOrposPaymentLoop.getGifcardAuthorizationCode() != null) {
                                    giftCardTender.setAuthorizationCode(omsOrposPaymentLoop.getGifcardAuthorizationCode());
                                    log.info("authorization code is set");
                                }
                                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                                DatatypeFactory datatypeFactory = null;
                                try {
                                    datatypeFactory = DatatypeFactory.newInstance();
                                } catch (DatatypeConfigurationException f) {
                                    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(giftCardTender.toString()));
                                }
                                XMLGregorianCalendar authorizationDateTime =
                                    datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                                Calendar authorizationDateTimeResp = Calendar.getInstance();
                                authorizationDateTimeResp.setTimeInMillis(omsCustOrdTender.getCreateDatetime().getTime());
                                authorizationDateTime.setMonth(authorizationDateTimeResp.get(Calendar.MONTH) + 1);
                                authorizationDateTime.setYear(authorizationDateTimeResp.get(Calendar.YEAR));
                                authorizationDateTime.setDay(authorizationDateTimeResp.get(Calendar.DAY_OF_MONTH));
                                giftCardTender.setAuthorizationDatetime(authorizationDateTime);
                                log.info("authorization datetime set");
                                log.info("omsOrposPaymentLoop.getGifcardAuthorizationMethod() " +
                                         omsOrposPaymentLoop.getGifcardAuthorizationMethod());
                                if (omsOrposPaymentLoop.getGifcardAuthorizationMethod() != null) {
                                    giftCardTender.setAuthorizationMethod(AuthorizationMethod.valueOf(omsOrposPaymentLoop.getGifcardAuthorizationMethod()));
                                }
                                log.info("omsOrposPaymentLoop.getCreditFlag() " + omsOrposPaymentLoop.getCreditFlag());
                                if (omsOrposPaymentLoop.getCreditFlag() != null) {
                                    giftCardTender.setCreditFlag(CreditFlag.valueOf(omsOrposPaymentLoop.getCreditFlag()));
                                }
                                log.info("omsOrposPaymentLoop.getGifcardEntryMethod() " +
                                         omsOrposPaymentLoop.getGifcardEntryMethod());
                                if (omsOrposPaymentLoop.getGifcardEntryMethod() != null) {
                                    giftCardTender.setEntryMethod(EntryMethod.valueOf(omsOrposPaymentLoop.getGifcardEntryMethod()));
                                }
                                log.info("omsOrposPaymentLoop.getOriginalBalance() " +
                                         omsOrposPaymentLoop.getOriginalBalance());
                                if (omsOrposPaymentLoop.getOriginalBalance() != null) {
                                    giftCardTender.setOriginalBalance(omsOrposPaymentLoop.getOriginalBalance());
                                }
                                log.info("omsOrposPaymentLoop.getRemainingBalance() " +
                                         omsOrposPaymentLoop.getRemainingBalance());
                                if (omsOrposPaymentLoop.getRemainingBalance() != null) {
                                    giftCardTender.setRemainingBalance(omsOrposPaymentLoop.getRemainingBalance());
                                }
                                log.info("omsOrposPaymentLoop.getGifcardsettlementData() " +
                                         omsOrposPaymentLoop.getGifcardsettlementData());
                                if (omsOrposPaymentLoop.getGifcardsettlementData() != null) {
                                    giftCardTender.setSettlementData(omsOrposPaymentLoop.getGifcardsettlementData());
                                }
                                paymentDesc.setGiftCardTender(giftCardTender);
                            }
                        }
                    } // end of if - ORPOS as application_id
                    else if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) {
                        giftCardTender.setCardNumber(omsCustOrdTender.getTenderRefId());
                        log.info("*****omsCustOrdTender.getTenderRefId() : "+omsCustOrdTender.getTenderRefId()+"***************");
                        giftCardTender.setAuthorizationCode("1234");
                        GregorianCalendar gregorianCalendar = new GregorianCalendar();
                        DatatypeFactory datatypeFactory = null;
                        try {
                            datatypeFactory = DatatypeFactory.newInstance();
                        } catch (DatatypeConfigurationException f) {
                            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(giftCardTender.toString()));
                        }
                        XMLGregorianCalendar authorizationDateTime =
                            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                        Calendar authorizationDateTimeResp = Calendar.getInstance();
                        authorizationDateTimeResp.setTimeInMillis(omsCustOrdTender.getCreateDatetime().getTime());
                        authorizationDateTime.setMonth(authorizationDateTimeResp.get(Calendar.MONTH) + 1);
                        authorizationDateTime.setYear(authorizationDateTimeResp.get(Calendar.YEAR));
                        authorizationDateTime.setDay(authorizationDateTimeResp.get(Calendar.DAY_OF_MONTH));
                        giftCardTender.setAuthorizationDatetime(authorizationDateTime);
                        giftCardTender.setAuthorizationMethod(AuthorizationMethod.AUTO);
                        giftCardTender.setCreditFlag(CreditFlag.N);
                        giftCardTender.setEntryMethod(EntryMethod.MANUAL);
                        giftCardTender.setOriginalBalance(new BigDecimal(0));
                        giftCardTender.setRemainingBalance(new BigDecimal(0));
                        giftCardTender.setSettlementData("12345");
                        paymentDesc.setGiftCardTender(giftCardTender);
                        log.info("ended the gift card tender for E-COMMERCE");


                    } //end of else if - E-COMMERCE
                } //end of else if - payment type - GIFTCARD

                //==================================================================================================================================
                else if (paymentDesc.getPaymentType().equals(PaymentType.valueOf("COUPON"))) // for coupon tender
                {
                    CouponTender couponTender = new CouponTender();
                    log.info("inside the gift card if condition");
                    if (omsCustOrdHead.getApplicationId().equals("ORPOS")) {
                        log.info("inside if condition when application id is ORPOS");
                        for (OmsOrposPayment omsOrposPaymentLoop : omsOrposPayment) {
                            log.info("inside OmsOrposPayment loop for gift card - if application id is - ORPOS");
                            if (omsOrposPaymentLoop.getPaymentType().equals("COUPON")) {
                                log.info("Inside the if condition for payment type- COUPON");
                                log.info("Coupon number is-----" + omsOrposPaymentLoop.getCouponNumber());

                                String couponNumber = omsOrposPaymentLoop.getCouponNumber();
                                log.info("coupon number++++" + couponNumber);
                                if (omsOrposPaymentLoop.getCouponType() != null) {
                                    couponTender.setCouponType(CouponType.valueOf(omsOrposPaymentLoop.getCouponType()));
                                    log.info("Coupon type is set");
                                }
                                if (omsOrposPaymentLoop.getCoupontenderEntryMethod() != null) {
                                    couponTender.setEntryMethod(EntryMethod.valueOf(omsOrposPaymentLoop.getCoupontenderEntryMethod()));
                                    log.info("Entry method is set");
                                }
                                if (couponNumber != null) {
                                    couponTender.setCouponNumber(couponNumber);
                                    log.info("Coupon Number is set");
                                }

                                paymentDesc.setCouponTender(couponTender);
                                log.info("ended the coupon tender for ORPOS");
                            } //end of if - Payment - COUPON


                        } //end of for
                    } //end of if - application_id - ORPOS
                    else if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) {
                        log.info("inside if condition when application id is E-COMMERCE");
                        log.info("inside OmsOrposPayment loop for gift card - if application id is - " +
                                 omsCustOrdHead.getApplicationId());
                        log.info("Inside the if condition for payment type - COUPON");
                        couponTender.setCouponNumber(omsCustOrdTender.getTenderRefId());
                        couponTender.setCouponType(CouponType.STORE);
                        couponTender.setEntryMethod(EntryMethod.MANUAL);
                        paymentDesc.setCouponTender(couponTender);
                        log.info("ended the coupon tender for E-COMMERCE");
                    }
                }


                paymentColDesc.getPaymentDesc().add(paymentDesc);

            }
            log.info("payment list size is : " + paymentColDesc.getPaymentDesc().size());
            paymentColDesc.setCollectionSize(paymentColDesc.getPaymentDesc().size());

            return paymentColDesc;
        } catch (Exception e) {
            return null;
        }

    }

    public String checkNullValueForString(String value) {
        if (value == null) {
            return null;
        } else {
            return value;
        }
    }

    public BigDecimal checkNullValueForNumber(BigDecimal value) {
        if (value == null) {
            return null;
        } else {
            return value;
        }
    }

    public BigDecimal WHCheck(String custOrderId, BigDecimal omsCustOrdNo,
                              OmsCustOrdItem omsCustOrdItem) throws SOAPException {

        log.info("inside WHCheck");
        log.info("custOrderId " + custOrderId);
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        log.info("omsCustOrdNo " + omsCustOrdNo);
        //List<OmsCustOrdItem> omsCustOrdItem=null;
        BigDecimal selectedQty = BigDecimal.ZERO;
        BigDecimal shippedQty = BigDecimal.ZERO;
        List<Tsfdetail> tsfdetail = null;
        BigDecimal quantity = BigDecimal.ZERO;
        List<OmsCoFulfillDetail> omsCoFulfillDetail = null;
        try {
            //omsCustOrdItem=session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
            // log.info("omsCustOrdItem.size() "+omsCustOrdItem.size());
            //for(OmsCustOrdItem omsCustOrdItemList:omsCustOrdItem)
            //{
            log.info("omsCustOrdItem.getOmsCustOrdNo() " + omsCustOrdItem.getOmsCustOrdNo());
            log.info("omsCustOrdItem.getLineNo() " + omsCustOrdItem.getLineNo());
            log.info("omsCustOrdItem.getItem()" + omsCustOrdItem.getItem());
            omsCoFulfillDetail =
                    session.getOmsCoFulfillDetailFindFulFillDetails(omsCustOrdItem.getOmsCustOrdNo(), omsCustOrdItem.getItem().toString(),
                                                                    omsCustOrdItem.getLineNo());
            log.info("omsCoFulfillDetail.size()" + omsCoFulfillDetail.size());

            if (omsCoFulfillDetail != null && omsCoFulfillDetail.size() > 0) {
                log.info("inside if condition for omsCoFulfillDetail");
                for (OmsCoFulfillDetail omsCoFulfillDetailList : omsCoFulfillDetail) {

                    log.info("inside for loop omsCoFulfillDetail");
                    log.info("omsCoFulfillDetailList.getOmsCustOrdNo() " + omsCoFulfillDetailList.getOmsCustOrdNo());
                    log.info("omsCoFulfillDetailList.getLineNo() " + omsCoFulfillDetailList.getLineNo());
                    log.info("omsCoFulfillDetailList.getItem()" + omsCoFulfillDetailList.getItem());
                    log.info("omsCoFulfillDetailList.getTsfNo() :" + omsCoFulfillDetailList.getTsfNo());
                    log.info("Shipped Quantity(Delivered Qty)  :" + omsCoFulfillDetailList.getFulfillDeliverQty());
                    log.info("omsCoFulfillDetailList.getTsfApprovalStatus() " +
                             omsCoFulfillDetailList.getTsfApprovalStatus());
                    try {
                        tsfdetail =
                                session.getTsfdetailFindQty(omsCoFulfillDetailList.getTsfNo(), omsCoFulfillDetailList.getItem());
                        log.info("tsfdetail.size() " + tsfdetail.size());
                        for (Tsfdetail tsfdetailList : tsfdetail) {
                            log.info("inside tsfdetail loop");
                            if (tsfdetailList.getSelectedQty() == null) {
                                selectedQty = BigDecimal.ZERO;
                            } else {
                                selectedQty = tsfdetailList.getSelectedQty();
                            }
                            if (tsfdetailList.getShipQty() == null) {
                                shippedQty = BigDecimal.ZERO;
                            } else {
                                shippedQty = tsfdetailList.getShipQty();
                            }
                            log.info("selectedQty " + selectedQty);
                            log.info("shippedQty " + shippedQty);
                            log.info("selectedQty.subtract(shippedQty) " + selectedQty.subtract(shippedQty));
                            if ((selectedQty.subtract(shippedQty)).intValue() > 0) {
                                log.info("inside if condition");
                                log.info("intial quantity value " + quantity);
                                quantity = quantity.add(selectedQty.subtract(shippedQty));
                                log.info("assiging quantity value quantity.add(selectedQty.subtract(shippedQty))");
                                log.info("quantity " + quantity);

                            } //end of if

                        } //end of for-loop
                    } catch (Exception ex) {
                        log.info("exception in transfer table " + ex);
                    }
                } //end of for-loop

            } //end of if

            //}//end of for-loop

        } //end of try

        catch (Exception e) {
            log.info("exception  " + e);
        }
        log.info("quantity " + quantity);
        return quantity;
    }

    public BigDecimal RMARestocking_Fee(String custOrderId, BigDecimal omsCustOrdNo,
                                        OmsCustOrdItem omsCustOrdItem) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        log.info("inside RMARestocking_Fee");
        log.info("omsCustOrdNo " + omsCustOrdNo);
        log.info("custOrderId " + custOrderId);
        log.info("==================================OmsCustOrdItem============================");
        log.info("omsCustOrdItem.getOmsCustOrdNo() " + omsCustOrdItem.getOmsCustOrdNo());
        log.info("omsCustOrdItem.getLineNo() " + omsCustOrdItem.getLineNo());
        log.info("omsCustOrdItem.getItem()" + omsCustOrdItem.getItem());

        List<OmsRmaReq> omsRmaReqList = null;
        OmsRmaReqItem omsRmaReqItem = null;
        BigDecimal restockAmt = BigDecimal.ZERO;
        BigDecimal quanity = BigDecimal.ZERO;
        BigDecimal return_Amt = BigDecimal.ZERO;
        try {
            omsRmaReqList = session.getOmsRmaReqFindByOmscustOrdNo(omsCustOrdNo);
            log.info("omsRmaReqList.size() " + omsRmaReqList.size());
            for (OmsRmaReq omsRmaReq : omsRmaReqList) {
                log.info("OmsRmaReq table values");
                log.info("omsRmaReq.getRmaId() " + omsRmaReq.getRmaId());
                log.info("omsRmaReq.getRefundCompltInd() " + omsRmaReq.getRefundCompltInd());
                log.info("omsRmaReq.getReturnStatus() " + omsRmaReq.getReturnStatus());
                log.info("omsRmaReq.getRestockAmount() " + omsRmaReq.getRestockAmount());
                try {
                    omsRmaReqItem =
                            session.getOmsRmaReqItemFindByRmaIdAndItem(omsRmaReq.getRmaId(), omsCustOrdItem.getItem(),
                                                                       omsCustOrdItem.getLineNo());
                    log.info("omsRmaReqItem table values");
                    log.info("omsRmaReqItem.getRmaQty() " + omsRmaReqItem.getRmaQty());
                    if (omsRmaReq.getStatus().equals("S")) {
                        if (omsRmaReq.getRefundCompltInd().equals("N") && omsRmaReq.getReturnStatus().equals("Y")) {
                            log.info("inside if condition");
                            quanity = quanity.add(omsRmaReqItem.getRmaQty());
                            restockAmt = restockAmt.add(omsRmaReq.getRestockAmount());
                            log.info("restockAmt " + restockAmt);
                        }
                    }
                } catch (Exception ex) {
                    log.info("No Record found in omsRmaReqItem for RMA_ID " + omsRmaReq.getRmaId());
                }
            }
        } catch (Exception e) {
            log.info("No records found in OmsRmaReq table for omsCustOrdNo " + omsCustOrdNo);
        }
        log.info("quanity " + quanity);
        log.info("restockAmt " + restockAmt);
        if (quanity.intValue() > 0 && restockAmt.intValue() > 0) {
            // return_Amt= restockAmt.divide(quanity);
            log.info("return_Amt " + return_Amt);
        }
        return restockAmt;

    }

    public int checkOpenDelivery(String custOrderId, BigDecimal omsCustOrdNo,
                                 OmsCustOrdItem input) throws SOAPException,
                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
                                                              com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                              com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
                                                              com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
                                                              com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
                                                              com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                              com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
                                                              EntityAlreadyExistsWSFaultException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        log.info("--> inside checkOpenDelivery for oms_cust_ord_no=" + omsCustOrdNo);
        OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
        List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
        int handeOverToCourierQty = 0;
        log.info("inside for of item, omsCustOrderNo=" + omsCustOrdNo + "inputItem.getLineNo()" + input.getLineNo() +
                 "inputItem.getItem()" + input.getItem());
        //omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByItem(omsCustOrdNo,input.getLineNo(), input.getItem());
        omsCoFulfillDetailList =
                session.getOmsCoFulfillDetailFindByItem(omsCustOrdNo, input.getLineNo(), input.getItem());

        log.info("omsCoFulfillDetailList size" + omsCoFulfillDetailList.size());
        int openQty = 0;
        for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
            log.info("checking open delivery,inside fulfill order loop---" + omsCustOrdHead.getDeliveryType() +
                     "----" + omsCoFulfillDetail.getSourceLocType());
            openQty =
                    openQty + omsCoFulfillDetail.getFulfillReqQty().intValue() - (omsCoFulfillDetail.getFulfillDeliverQty().intValue() +
                                                                                  omsCoFulfillDetail.getFulfillCancelQty().intValue());
            if (omsCustOrdHead.getDeliveryType().equals("S") &&
                omsCoFulfillDetail.getSourceLocType().equals("WH") == false &&
                omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc())) {
                OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
                OpenDeliveryBean openDeliveryBean =
                    omsUtilCommmons.findAvailableQtyForCancellation(omsCoFulfillDetail, custOrderId);
                log.info("openDeliveryBean.getQuantity()" + openDeliveryBean.getQuantity());
                //OpenDeliveryBean openDeliveryBean1=omsUtilCommmons.findQuantityPickedFromBoL(omsCoFulfillDetail, custOrderId);
                //log.info("Quantity picked "+openDeliveryBean1.getQuantity());
                if (openDeliveryBean.getItem() != null) {

                    handeOverToCourierQty = handeOverToCourierQty + openDeliveryBean.getQuantity();
                    log.info("handeOverToCourierQty=" + handeOverToCourierQty);
                } //end of if

            }

        } //end of OmsCoFulfillDetail loop


        return handeOverToCourierQty;
    }


    public CustOrdFulColDesc backOrder_Detail(OmsCustOrdHead omsCustOrdHead, int count, OmsCustOrdItem omsCustOrdItem,
                                              CustOrdFulDesc custOrdFulDesc, CustOrdFulColDesc custOrdFulColDesc,
                                              List<CustOrdFulDesc> custOrdFulDescList) throws SOAPException {
        log.info("inside backOrder_Detail method ");
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        custOrdFulColDesc.setCollectionSize(count);


        try {
            List<OmsBackOrderDtl> omsBackOrderDtl =
                session.getOmsBackOrderDtlFindByOmsCustOrdNoItemLinNo(omsCustOrdNo, omsCustOrdItem.getItem(),
                                                                      omsCustOrdItem.getLineNo());
            log.info("omsBackOrderDtl.size()" + omsBackOrderDtl.size());
            custOrdFulDesc = new CustOrdFulDesc();
            log.info("before FulfillOrderSeqNo");
            // if(omsCoFulfillDetail.getSourceLoc().equals(omsCoFulfillDetail.getFulfillLoc()))
            //{
            custOrdFulDesc.setSeqNo(count);

            log.info("before getFulfillOrderId()");

            custOrdFulDesc.setFulfillOrderId("0");

            if (omsBackOrderDtl.get(0).getFulfillLocType() != null) {
                FulfillLocType fulfillLocType = custOrdFulDesc.getFulfillLocType();
                if (omsBackOrderDtl.get(0).getFulfillLocType().equals("V")) {
                    custOrdFulDesc.setFulfillLocType(fulfillLocType.fromValue("W"));
                } else {
                    custOrdFulDesc.setFulfillLocType(fulfillLocType.fromValue(omsBackOrderDtl.get(0).getFulfillLocType()));
                }
            }
            //custOrdFulDesc.getFulfillLocType().fromValue(omsOrposCustOrdFul.getFulfillLocType());
            log.info("omsOrposCustOrdFul.getFulfillLocType()" + omsBackOrderDtl.get(0).getFulfillLocType());

            custOrdFulDesc.setFulfillLocId(omsBackOrderDtl.get(0).getFulfillLoc().longValue());


            if (omsCustOrdHead.getDeliveryType() != null) {
                DeliveryType deliveryType = custOrdFulDesc.getDeliveryType();

                custOrdFulDesc.setDeliveryType(deliveryType.fromValue(omsCustOrdHead.getDeliveryType()));
            }
            //PARTIAL_DELIVERY_IND

            custOrdFulDesc.setPartialDeliveryInd(custOrdFulDesc.getPartialDeliveryInd().fromValue("Y"));

            //SHIP_TO_FULFILL_LOC_FLAG
            //check the value
            custOrdFulDesc.setShipToFulfillLocFlag(custOrdFulDesc.getShipToFulfillLocFlag().fromValue("Y"));


            //CONSUMER_DELIVERY_DATE
            if (omsCustOrdHead.getConsumerDlyTime() != null) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                DatatypeFactory datatypeFactory = null;
                try {
                    datatypeFactory = DatatypeFactory.newInstance();
                } catch (DatatypeConfigurationException f) {
                    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(custOrdFulDesc.toString()));
                }
                XMLGregorianCalendar consumerDeliveryDate = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                Calendar consumerDeliveryDateResp = Calendar.getInstance();
                consumerDeliveryDateResp.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
                consumerDeliveryDate.setMonth(consumerDeliveryDateResp.get(Calendar.MONTH) + 1);
                consumerDeliveryDate.setYear(consumerDeliveryDateResp.get(Calendar.YEAR));
                consumerDeliveryDate.setDay(consumerDeliveryDateResp.get(Calendar.DAY_OF_MONTH));
                custOrdFulDesc.setConsumerDeliveryDate(consumerDeliveryDate);
            }
            //CONSUMER_DELIVERY_TIME
            if (omsCustOrdHead.getConsumerDlyTime() != null) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                DatatypeFactory datatypeFactory = null;
                try {
                    datatypeFactory = DatatypeFactory.newInstance();
                } catch (DatatypeConfigurationException f) {
                    throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(custOrdFulDesc.toString()));
                }
                XMLGregorianCalendar consumerDeliveryTime = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                Calendar consumerDeliveryTimeResp = Calendar.getInstance();
                consumerDeliveryTimeResp.setTimeInMillis(omsCustOrdHead.getConsumerDlyTime().getTime());
                consumerDeliveryTime.setMonth(consumerDeliveryTimeResp.get(Calendar.MONTH) + 1);
                consumerDeliveryTime.setYear(consumerDeliveryTimeResp.get(Calendar.YEAR));
                consumerDeliveryTime.setDay(consumerDeliveryTimeResp.get(Calendar.DAY_OF_MONTH));
                custOrdFulDesc.setConsumerDeliveryTime(consumerDeliveryTime);
            }

            DeliveryDestDtl deliveryDestDtl = new DeliveryDestDtl();

            log.info("after DeliveryDestDtl");
            try {
                OmsCustOrdAddress omsCustOrdAddress = null;
                try {
                    omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
                } catch (Exception e) {

                }
                ContactDesc contactDesc = new ContactDesc();
                log.info("contactDesc------------");
                if (omsCustOrdHead.getCustFirstName() != null) {
                    contactDesc.setFirstName(omsCustOrdHead.getCustFirstName());
                    log.info("omsOrposContact.getFirstName()");
                }

                if (omsCustOrdHead.getCustLastName() != null) {
                    contactDesc.setLastName(omsCustOrdHead.getCustLastName());
                }

                deliveryDestDtl.setContactDesc(contactDesc);
                //    phones

                Phones phones = new Phones();
                List<PhoneDesc> phoneDescList = phones.getPhoneDesc();


                PhoneDesc phoneDesc = new PhoneDesc();

                phoneDesc.setPhoneId(BigDecimal.ONE);

                if (omsCustOrdHead.getCustPhoneNo() != null) {
                    phoneDesc.setPhoneNumber(omsCustOrdHead.getCustPhoneNo());
                }

                phoneDesc.setPhoneType(phoneDesc.getPhoneType().HOME);
                phoneDesc.setPrimaryPhoneInd("Y");
                phoneDescList.add(phoneDesc);

                contactDesc.setPhones(phones);

                // EMAILS

                BillingDestDtl billingDestDtl = new BillingDestDtl();
                log.info("after BillingDestDtl");
                billingDestDtl.setContactDesc(contactDesc);
                //GeoAddress
                try {

                    GeoAddrDesc geoAddrDesc = new GeoAddrDesc();

                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverAdd1() != null) {
                        geoAddrDesc.setAddress1(omsCustOrdAddress.getDeliverAdd1());
                    }
                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverAdd2() != null) {
                        geoAddrDesc.setAddress2(omsCustOrdAddress.getDeliverAdd2());
                    }
                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverAdd3() != null) {
                        geoAddrDesc.setAddress3(omsCustOrdAddress.getDeliverAdd3());
                    }

                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverCity() != null) {
                        geoAddrDesc.setCity(omsCustOrdAddress.getDeliverCity());
                    }

                    if (omsCustOrdAddress != null && omsCustOrdAddress.getDeliverCountry() != null) {
                        geoAddrDesc.setCountryCode(omsCustOrdAddress.getDeliverCountry());
                    }


                    deliveryDestDtl.setGeoAddrDesc(geoAddrDesc);
                    billingDestDtl.setGeoAddrDesc(geoAddrDesc);
                } catch (Exception e) 
                {

                }
                custOrdFulDesc.setDeliveryDestDtl(deliveryDestDtl);
                contactDesc.setPhones(phones);
                custOrdFulDesc.setBillingDestDtl(billingDestDtl);

            } catch (Exception e) {
                log.error("Address not found");
            }
            custOrdFulDescList.add(custOrdFulDesc);

        } catch (Exception e) {
            log.info("No records found in OmsBackOrderDtl for omsCustOrdNo " + omsCustOrdNo + "item " +
                     omsCustOrdItem.getItem() + "lineNo " + omsCustOrdItem.getLineNo());
        }
        return custOrdFulColDesc;
    }
    
    /* Start Vat Changes @tsultana */
    /**
     * To get the Tax details for customer query
     * @param omsCustOrdHead
     * @param custOrderId
     * @param omsCustOrdNo
     * @param omsCustOrdItem
     * @param custOrdItmDesc
     * @return TaxLineColDesc
     * @throws SOAPException
     */
    public TaxLineColDesc createResponseForTaxLine(OmsCustOrdHead omsCustOrdHead, String custOrderId,
                                                   BigDecimal omsCustOrdNo, OmsCustOrdItem omsCustOrdItem,
                                                   CustOrdItmDesc custOrdItmDesc) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();
        TaxLineColDesc taxLineColDesc = new TaxLineColDesc();

        if (omsCustOrdHead.getApplicationId().equals("ORPOS"))
        {
            log.info("Order is ORPOS");
            List<OmsOrposCustOrderHead> omsOrposCustOrderHeadList =session.getOmsOrposCustOrderHeadfindColumns(custOrderId);
            log.info("omsorposCustorderId " + omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId());
            log.info("LineNo " + omsCustOrdItem.getLineNo());
            log.info("Item " + omsCustOrdItem.getItem());
            List<OmsOrposTaxLine> omsorpostaxLineList =session.getOmsOrposTaxLineFindColumns(omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId(),omsCustOrdItem.getLineNo(), omsCustOrdItem.getItem());
            if (omsorpostaxLineList != null && omsorpostaxLineList.size() > 0) 
            {
                log.info("omsorpostaxLineList size " + omsorpostaxLineList.size());
                for (OmsOrposTaxLine omsOrposTaxLine : omsorpostaxLineList)
                {

                    TaxLineDesc taxLineDesc = new TaxLineDesc();
                    log.info("Tax details exists");
                    log.info("Tax Details: line number" + omsOrposTaxLine.getLineNo().intValue());
                    taxLineDesc.setLineNo(omsOrposTaxLine.getLineNo().intValue());
                    log.info("Tax Details: Authority Id" + omsOrposTaxLine.getTaxAuthorityId());
                    taxLineDesc.setTaxAuthorityId(omsOrposTaxLine.getTaxAuthorityId());
                    log.info("Tax Details: Tax Group Id" + omsOrposTaxLine.getTaxGroupId());
                    taxLineDesc.setTaxGroupId(omsOrposTaxLine.getTaxGroupId());
                    log.info("Tax Details: Tax Type Code" + omsOrposTaxLine.getTaxTypeCode());
                    taxLineDesc.setTaxTypeCode(omsOrposTaxLine.getTaxTypeCode());

                    com.oracle.retail.integration.base.bo.taxlinedesc.v1.Flag taxHolidayFlag =taxLineDesc.getTaxHolidayFlag();

                    log.info("Tax Details: Tax Holiday Flag" +taxHolidayFlag.fromValue(omsOrposTaxLine.getTaxHolidayFlag()));
                    taxLineDesc.setTaxHolidayFlag(taxHolidayFlag.fromValue(omsOrposTaxLine.getTaxHolidayFlag()));
                    log.info("Tax Details: Currency Code" + omsOrposTaxLine.getCurrencyCode());
                    taxLineDesc.setCurrencyCode(omsOrposTaxLine.getCurrencyCode());
                    log.info("Tax Details: Taxable Amount" + omsOrposTaxLine.getTaxableAmount());
                    taxLineDesc.setTaxableAmount(omsOrposTaxLine.getTaxableAmount());
                    log.info("Tax Details: Tax Amount" + omsOrposTaxLine.getTaxAmount());
                    taxLineDesc.setTaxAmount(omsOrposTaxLine.getTaxAmount());
                    //set the Complted,cancelled and Return tax
                    taxLineDesc=setTaxAmountTransactionValues(taxLineDesc, omsCustOrdItem, omsCustOrdHead);

                    com.oracle.retail.integration.base.bo.taxlinedesc.v1.Flag inclusiveTaxFlag =taxLineDesc.getInclusiveTaxFlag();
                    log.info("Tax Details: Inclusive Tax Flag" +inclusiveTaxFlag.fromValue(omsOrposTaxLine.getInclusiveTaxFlag()));
                    taxLineDesc.setInclusiveTaxFlag(inclusiveTaxFlag.fromValue(omsOrposTaxLine.getInclusiveTaxFlag()));
                    EnumTaxMode taxMode = taxLineDesc.getTaxMode();
                    log.info("Tax Details: Tax Mode" + omsOrposTaxLine.getTaxMode());
                    taxLineDesc.setTaxMode(taxMode.fromValue(omsOrposTaxLine.getTaxMode()));

                    if (omsOrposTaxLine.getTaxModReasonCode() != null)
                    {
                        log.info("Tax Details: Tax Mode Reason code" + omsOrposTaxLine.getTaxModReasonCode());
                        taxLineDesc.setTaxModReasonCode(omsOrposTaxLine.getTaxModReasonCode());
                    }

                    EnumTaxModScope taxModeScope = taxLineDesc.getTaxModScope();
                    if (omsOrposTaxLine.getTaxModScope() != null)
                    {
                        log.info("Tax Details: Tax Mode Scope" +
                                 taxModeScope.fromValue(omsOrposTaxLine.getTaxModScope()));
                        taxLineDesc.setTaxModScope(taxModeScope.fromValue(omsOrposTaxLine.getTaxModScope()));
                    }
                    if (omsOrposTaxLine.getTaxRate() != null) {
                        log.info("Tax Details: Tax Rate" + omsOrposTaxLine.getTaxRate());
                        taxLineDesc.setTaxRate(omsOrposTaxLine.getTaxRate());
                    }
                    if (omsOrposTaxLine.getTaxRuleName() != null) {
                        log.info("Tax Details:Tax Rule Name" + omsOrposTaxLine.getTaxRuleName());
                        taxLineDesc.setTaxRuleName(omsOrposTaxLine.getTaxRuleName());
                    }

                    if (omsOrposTaxLine.getTaxAuthorityName() != null) {
                        log.info("Tax Details:Tax Authority Name" + omsOrposTaxLine.getTaxAuthorityName());
                        taxLineDesc.setTaxAuthorityName(omsOrposTaxLine.getTaxAuthorityName());
                    }
                    taxLineColDesc.getTaxLineDesc().add(taxLineDesc);
                }
            }
        }
        else if (omsCustOrdHead.getApplicationId().equals("E-COMMERCE")) 
        {
            OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
                String taxableInd = null;
                String vatCode = null;
                BigDecimal vatRegion = null;
                OmsTaxDesc omsTaxDesc = null;
            try
            {
                if("S".equalsIgnoreCase(omsCustOrdHead.getDeliveryType()))
                {
                taxableInd = omsUtilCommmons.getTaxableIndicator(omsCustOrdItem.getItem(), omsCustOrdHead.getOrderRequestorId());
                    vatRegion = omsUtilCommmons.getVatRegion(omsCustOrdHead.getOrderRequestorId());    
                }
                else
                {
                        taxableInd = omsUtilCommmons.getTaxableIndicator(omsCustOrdItem.getItem(), omsCustOrdHead.getPickLoc());
                        vatRegion = omsUtilCommmons.getVatRegion(omsCustOrdHead.getPickLoc());    
                 }
                if("Y".equalsIgnoreCase(taxableInd)){
                
                vatCode = omsUtilCommmons.getVatCode(omsCustOrdItem.getItem(), vatRegion, omsCustOrdHead.getCreateDatetime());
                omsTaxDesc = omsUtilCommmons.getOmsTaxDetails(vatCode, vatRegion, omsCustOrdHead.getCreateDatetime());
                
                TaxLineDesc taxLineDesc = new TaxLineDesc();
                log.info("Tax details exists");
                log.info("Tax Details: line number" + omsCustOrdItem.getLineNo().intValue());
                taxLineDesc.setLineNo(omsCustOrdItem.getLineNo().intValue());
                log.info("Tax Details: Authority Id" + omsTaxDesc.getTaxAuthorityId());
                taxLineDesc.setTaxAuthorityId(omsTaxDesc.getTaxAuthorityId());
                log.info("Tax Details: Tax Group Id" + omsTaxDesc.getTaxGroupId());
                taxLineDesc.setTaxGroupId(omsTaxDesc.getTaxGroupId());
                log.info("Tax Details: Tax Type Code" + omsTaxDesc.getTaxTypeCode());
                taxLineDesc.setTaxTypeCode(omsTaxDesc.getTaxTypeCode());

                com.oracle.retail.integration.base.bo.taxlinedesc.v1.Flag taxHolidayFlag =taxLineDesc.getTaxHolidayFlag();

                log.info("Tax Details: Tax Holiday Flag" +taxHolidayFlag.fromValue(omsTaxDesc.getTaxHolidayFlag()));
                taxLineDesc.setTaxHolidayFlag(taxHolidayFlag.fromValue(omsTaxDesc.getTaxHolidayFlag()));
                com.oracle.retail.integration.base.bo.taxlinedesc.v1.Flag inclusiveTaxFlag =taxLineDesc.getInclusiveTaxFlag();
                log.info("Tax Details: Inclusive Tax Flag" +inclusiveTaxFlag.fromValue(omsTaxDesc.getInclusiveTaxFlag()));
                taxLineDesc.setInclusiveTaxFlag(inclusiveTaxFlag.fromValue(omsTaxDesc.getInclusiveTaxFlag()));
                
                log.info("Tax Details: Currency Code" + omsCustOrdItem.getRetailCurr());
                taxLineDesc.setCurrencyCode(omsCustOrdItem.getRetailCurr());
                log.info("Tax Details: Taxable Amount" + omsCustOrdItem.getUnitRetail());
                taxLineDesc.setTaxableAmount(omsCustOrdItem.getUnitRetail().multiply(omsCustOrdItem.getQtyOrderedSuom()));
                log.info("Tax Details: Tax Amount" + omsCustOrdItem.getUnitVatAmount());
                taxLineDesc.setTaxAmount(omsCustOrdItem.getUnitVatAmount().multiply(omsCustOrdItem.getQtyOrderedSuom()));
                
                //set the Complted,cancelled and Return tax
                taxLineDesc=setTaxAmountTransactionValues(taxLineDesc, omsCustOrdItem, omsCustOrdHead);
             
                EnumTaxMode taxMode = taxLineDesc.getTaxMode();
                log.info("Tax Details: Tax Mode" + omsTaxDesc.getTaxMode());
                taxLineDesc.setTaxMode(taxMode.fromValue(omsTaxDesc.getTaxMode()));

                if ( omsTaxDesc.getTaxModReasonCode() != null)
                {
                    log.info("Tax Details: Tax Mode Reason code" +  omsTaxDesc.getTaxModReasonCode());
                    taxLineDesc.setTaxModReasonCode(omsTaxDesc.getTaxModReasonCode());
                }

                EnumTaxModScope taxModeScope = taxLineDesc.getTaxModScope();
                if (omsTaxDesc.getTaxModScope() != null) 
                {
                    log.info("Tax Details: Tax Mode Scope" +taxModeScope.fromValue(omsTaxDesc.getTaxModScope()));
                    taxLineDesc.setTaxModScope(taxModeScope.fromValue(omsTaxDesc.getTaxModScope()));
                }
                if (omsTaxDesc.getTaxRate() != null) {
                    log.info("Tax Details: Tax Rate" + omsTaxDesc.getTaxRate());
                    taxLineDesc.setTaxRate(omsTaxDesc.getTaxRate());
                }
                if (omsTaxDesc.getTaxRuleName() != null) {
                    log.info("Tax Details:Tax Rule Name" + omsTaxDesc.getTaxRuleName());
                    taxLineDesc.setTaxRuleName(omsTaxDesc.getTaxRuleName());
                }

                if (omsTaxDesc.getTaxAuthorityName() != null) {
                    log.info("Tax Details:Tax Authority Name" + omsTaxDesc.getTaxAuthorityName());
                    taxLineDesc.setTaxAuthorityName(omsTaxDesc.getTaxAuthorityName());
                }
                taxLineColDesc.getTaxLineDesc().add(taxLineDesc);
                
            }
                }
                    catch(Exception e)
            {
                            log.info("exception in retrieval of tax details from oms_tax_desc table " + e);
                        }
                    
            }
        return taxLineColDesc;
    }
    
    public TaxLineDesc setTaxAmountTransactionValues(TaxLineDesc taxLineDesc,OmsCustOrdItem omsCustOrdItem,OmsCustOrdHead omsCustOrdHead) throws SOAPException {
        OMSUtilSessionEJB session = OMSUtil.doLookup();

        int i=0;
        int k=0;
        List<OmsCoCancelHead> cancelledList=null;
        List<OmsCoCancelHead> cancelListORPOS=null;
        List<OmsRmaReq> rmaReturned=null;
        List<OmsRmaReq> rmaRefundORPOS=null;
        if(omsCustOrdItem.getCumQtyDelivered()==null || omsCustOrdItem.getCumQtyDelivered().intValue()==0)
        {
         log.info("Complted tax amount is zero since no qty has been delivered");
         taxLineDesc.setCompletedTaxAmount(BigDecimal.ZERO);
        }
        else
        {
            BigDecimal completedTaxAmt=BigDecimal.ZERO; 
            completedTaxAmt=omsCustOrdItem.getCumQtyDelivered().multiply(omsCustOrdItem.getUnitVatAmount());
            completedTaxAmt=completedTaxAmt.setScale(2, RoundingMode.HALF_EVEN);
            log.info("Completed Tax amount for line No"+omsCustOrdItem.getLineNo()+"is " +completedTaxAmt); 
            
            taxLineDesc.setCompletedTaxAmount(completedTaxAmt);
        }
        if(omsCustOrdItem.getQtyCancelled()==null || omsCustOrdItem.getQtyCancelled().intValue()==0)
        {
            log.info("Cancelled tax amount is zero since no qty has been cancelled");
            taxLineDesc.setCancelledTaxAmount(BigDecimal.ZERO);
        }
        else
        {
            BigDecimal cancelledTaxAmt=BigDecimal.ZERO;
            
            try
            {
             cancelledList=session.getOmsCoCancelHeadFindByCustOrdNoforCancelledOrder(omsCustOrdHead.getCustOrderNo(),"Y");
            }
            catch(Exception e) 
            {
                   log.info("No records in cancelledList");     
            }
            try
            {
             cancelListORPOS=session.getOmsCoCancelHeadFindByCustOrdNoforORPOSRefundOrder(omsCustOrdHead.getCustOrderNo(),"N","ORPOS");
            }
            catch(Exception e) 
            {
                   log.info("No records in cancelledList cancelListORPOS");     
            }
            if(cancelListORPOS!=null && cancelListORPOS.size()>0)
            {
                for(OmsCoCancelHead omsCoCancelHead:cancelListORPOS)
                {
                    if(omsCoCancelHead.getRefundOption().equalsIgnoreCase("ORPOS") && omsCoCancelHead.getRefundCompltInd().equalsIgnoreCase("N"))
                    {    
                            try
                            {
                                
                            OmsCoCancelItem omsCoCancelItem=session.getOmsCoCancelItemFindByOmsCancelIdandLineNo(omsCoCancelHead.getOmsCancelId(), omsCustOrdItem.getLineNo());
                            cancelledTaxAmt=cancelledTaxAmt.add(omsCoCancelItem.getCancelConfQty().multiply(omsCustOrdItem.getUnitVatAmount()));
                            log.info("CancelledTaxAmt in ORPOS "+cancelledTaxAmt); 
                           // taxLineDesc.setCompletedTaxAmount(BigDecimal.ZERO);
                            }
                            catch(Exception e) 
                            {
                               log.info("No records in omsCoCancelItem");     
                            }
                        
                    }
                } 
            } 
            else if(cancelledList!=null && cancelledList.size()>0)
            {
                    cancelledTaxAmt=cancelledTaxAmt.add(omsCustOrdItem.getQtyCancelled().multiply(omsCustOrdItem.getUnitVatAmount()));
                    log.info("CancelledTaxAmt in clearing "+cancelledTaxAmt);
                
            }
            else
            {
               cancelledTaxAmt=BigDecimal.ZERO;    
            }
            log.info("cancelled tax amount for line No "+omsCustOrdItem.getLineNo()+" is "+cancelledTaxAmt);
            cancelledTaxAmt=cancelledTaxAmt.setScale(2,RoundingMode.HALF_EVEN);
            taxLineDesc.setCancelledTaxAmount(cancelledTaxAmt);

        }
        if(omsCustOrdItem.getQtyReturned()==null || omsCustOrdItem.getQtyReturned().intValue()==0)
        {
            log.info("Returned tax amount is zero since no qty has been delivered");
            taxLineDesc.setReturnedTaxAmount(BigDecimal.ZERO);
          
        }
        else
        {
            BigDecimal returnedTaxAmt=BigDecimal.ZERO; 
            BigDecimal refundCancelledTax=BigDecimal.ZERO;
            //check whether the item has been already cancelled from RMS with refund option Clearing or ORPOS
            
            try
            {
              rmaReturned=session.getOmsRmaReqFindByOmscustOrdNoforRMAReturn(omsCustOrdHead.getOmsCustOrdNo());
            }
            catch(Exception e)
            {
                log.info("No record found in rmaReturned");
            }
            try
            {
              rmaRefundORPOS=session.getOmsRmaReqFindByOmscustOrdNoforRMAORPOS(omsCustOrdHead.getOmsCustOrdNo());
            }
            catch(Exception e) 
            {
               log.info("No records found for rmaRefundORPOS");     
            }

            log.info("rmaRefundORPOS.size()"+rmaRefundORPOS.size());
            log.info("rmaReturned.size()"+rmaReturned.size());
            
            if(rmaRefundORPOS!=null && rmaRefundORPOS.size()>0)
            {
                for(OmsRmaReq omsRmaReq:rmaRefundORPOS)
                {
                    log.info("inside for loop for rmaRefundORPOS");
                    if(omsRmaReq.getRefundOption().equalsIgnoreCase("ORPOS") && omsRmaReq.getRefundCompltInd().equalsIgnoreCase("N"))
                    { 
                             try
                             {
                             OmsRmaReqItem omsRmaReqItem=session.getOmsRmaReqItemFindByRmaIdAndItem(omsRmaReq.getRmaId(), omsCustOrdItem.getItem(), omsCustOrdItem.getLineNo());
                             refundCancelledTax=refundCancelledTax.add(omsRmaReqItem.getOrigRmaQty().multiply(omsCustOrdItem.getUnitVatAmount()));
                             refundCancelledTax=refundCancelledTax.setScale(2, RoundingMode.HALF_EVEN);
                             taxLineDesc.setCancelledTaxAmount(refundCancelledTax);
                             //taxLineDesc.setCompletedTaxAmount(BigDecimal.ZERO);
                             //taxLineDesc.setReturnedTaxAmount(BigDecimal.ZERO);
                             }
                             catch(Exception e) 
                             {
                               log.info("No records found");    
                             }
                         
                    }
                } 
            }   
             else if(rmaReturned!=null && rmaReturned.size()>0)
                    {
                        returnedTaxAmt=returnedTaxAmt.add(omsCustOrdItem.getQtyReturned().multiply(omsCustOrdItem.getUnitVatAmount()));
                        taxLineDesc.setReturnedTaxAmount(returnedTaxAmt.setScale(2, RoundingMode.HALF_EVEN));
                        for(OmsRmaReq omsRmaReq:rmaReturned)
                        {
                            try
                            {
                            OmsRmaReqItem omsRmaReqItem=session.getOmsRmaReqItemFindByRmaIdAndItem(omsRmaReq.getRmaId(), omsCustOrdItem.getItem(), omsCustOrdItem.getLineNo());
                            if(omsRmaReq.getRefundOption().equalsIgnoreCase("ORPOS") && omsRmaReqItem.getLineNo().intValue()==omsCustOrdItem.getLineNo().intValue())
                            {    
                              taxLineDesc.setCancelledTaxAmount(returnedTaxAmt.setScale(2, RoundingMode.HALF_EVEN));
                            }
                            }
                            catch(Exception e) 
                            {
                              log.info("No records in RMA");    
                            }
                        }    
                    }
            else
            {
                //Actual ORPOS Return
                
                  returnedTaxAmt=returnedTaxAmt.add(omsCustOrdItem.getQtyReturned().multiply(omsCustOrdItem.getUnitVatAmount())); 
                  returnedTaxAmt=returnedTaxAmt.setScale(2, RoundingMode.HALF_EVEN);
                 taxLineDesc.setReturnedTaxAmount(returnedTaxAmt);
            }
            log.info("Returned Tax amount for line No"+omsCustOrdItem.getLineNo()+"is " +returnedTaxAmt); 
            
           /* try
            {
            if(cancelListORPOS.size()>0 && rmaRefundORPOS.size()==0)
            {
               taxLineDesc.setCompletedTaxAmount(BigDecimal.ZERO);
               taxLineDesc.setReturnedTaxAmount(BigDecimal.ZERO);
            }
            }
            catch(Exception e) 
            {
               log.info("No records");     
             } */
            
        }
        
        return taxLineDesc;
    }/* End Vat Changes @tsultana */
}
