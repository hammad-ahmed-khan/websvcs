package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorderpicvo.v1.CustOrderPicVo;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.CardType;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentDesc;
import com.oracle.retail.integration.base.bo.paymentdesc.v1.PaymentType;

import java.math.BigDecimal;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

public class TenderTypeClassifier {

	public TenderTypeClassifier() {
		super();
	}

	private final static Logger log = Logger.getLogger(com.logicinfo.oms.beans.TenderTypeClassifier.class.getName());
	BigDecimal tenderValue;

	public BigDecimal getTenderTypeIdBasedOnParameterValueForCreateOrder(PaymentDesc input, CustOrderDesc custOrderDesc) throws SOAPException {
		log.info("**********Inside Tender Type Mapping for Create order**********");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("Session object intialized !");
		if (input.getPaymentType().equals(PaymentType.CASH)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			if (custOrderDesc.getRoundingAdjustment() != null && custOrderDesc.getRoundingAdjustment().intValue() != 0) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("ROUNDING_EXISTS", "RTLOG_TENDERS"));
			} else if (input.getAlternateCurrencyCode() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("ALT_CURR_CASH", "RTLOG_TENDERS"));
			} else {
				log.info("inside cash else block");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("CASH", "RTLOG_TENDERS"));
				log.info("tenderValue for CASH " + tenderValue);
			}

		} else if (input.getPaymentType().equals(PaymentType.MONEYORDER)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			log.info("==========================================================");
			tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("MONEYORDER", "RTLOG_TENDERS"));

		} else if (input.getPaymentType().equals(PaymentType.CREDIT)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			log.info("inside payment type credit checking whether CreditDebitTender.getCardType() is null or not");
			if (input.getCreditDebitTender().getCardType() != null) {
				log.info("CreditDebitTender.getCardType() is not equal to null");
				if (input.getCreditDebitTender().getCardType().equals(CardType.VISA)) {
					log.info("inside CardType.VISA");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("VISA", "RTLOG_TENDERS"));
				} else if (input.getCreditDebitTender().getCardType().equals(CardType.MASTER)) {
					log.info("inside CardType.MASTER");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("MASTERCARD", "RTLOG_TENDERS"));
				} else if (input.getCreditDebitTender().getCardType().equals(CardType.AMEX)) {
					log.info("inside CardType.AMEX");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("AMEX", "RTLOG_TENDERS"));
				} else if (input.getCreditDebitTender().getCardType().equals(CardType.DISCOVER)) {
					log.info("inside CardType.DISCOVER");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("DISCOVER", "RTLOG_TENDERS"));
				} else if (input.getCreditDebitTender().getCardType().equals(CardType.DINERS)) {
					log.info("inside CardType.DINERS");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("DINER", "RTLOG_TENDERS"));
				} else if (input.getCreditDebitTender().getCardType().equals(CardType.HOUSECARD)) {
					log.info("inside CardType.HOUSECARD");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("HOUSECARD", "RTLOG_TENDERS"));
				} else if (input.getCreditDebitTender().getCardType().equals(CardType.JCB)) {
					log.info("inside CardType.JCB");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("JCB", "RTLOG_TENDERS"));
				} else if (input.getCreditDebitTender().getCardType().equals(CardType.SPAN)) {
					log.info("inside CardType.SPAN");
					tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("SPAN", "RTLOG_TENDERS"));
				}
			}
		} else if (input.getPaymentType().equals(PaymentType.CHECK)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			if (input.getCheckTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("CHCK", "RTLOG_TENDERS"));
			} else if (input.getTravelCheckTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("TRVLCHCK", "RTLOG_TENDERS"));
			} else if (input.getMailCheckTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("MAILCHCK", "RTLOG_TENDERS"));
			}
		} else if (input.getPaymentType().equals(PaymentType.GIFTCARD)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			if (input.getGiftCardTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("GIFT_CARD", "RTLOG_TENDERS"));
				log.info("tenderValue for gift card " + tenderValue);
			}
		} else if (input.getPaymentType().equals(PaymentType.GIFTCERT)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			if (input.getGiftCertTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("GIFT_CERTIFICATE", "RTLOG_TENDERS"));
			}
		} else if (input.getPaymentType().equals(PaymentType.STORECREDIT)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			if (input.getStoreCreditTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("STORE_CREDIT", "RTLOG_TENDERS"));
			}
		} else if (input.getPaymentType().equals(PaymentType.PURCHASEORDER)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			log.info("input.getPurchaseOrdTender() " + input.getPurchaseOrdTender());
			if (input.getPurchaseOrdTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("PO_TENDER", "RTLOG_TENDERS"));
			}
		} else if (input.getPaymentType().equals(PaymentType.COUPON)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			if (input.getCouponTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("COUPON_TENDER", "RTLOG_TENDERS"));
			}
		} else if (input.getPaymentType().equals(PaymentType.DEBIT)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			if (input.getCreditDebitTender() != null) {
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("DEBIT_CARD", "RTLOG_TENDERS"));
			}
		} else if (input.getPaymentType().equals(PaymentType.MALLCERT)) {
			log.info("Entered the if loop when payment type is----" + input.getPaymentType());
			tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("MALL_CERTIFICATE", "RTLOG_TENDERS"));
		} else if (PaymentType.TRAVCHECK.equals(input.getPaymentType())) {
			tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("TRAVCHECK", "RTLOG_TENDERS"));
		}
		return tenderValue;
	}

	// Tender mapping for Pick-up WS input mapping
	public BigDecimal getTenderTypeIdBasedOnParameterValueForPickup(PaymentDesc paymentDesc, CustOrderPicVo input) throws SOAPException {
		log.info("**********Inside Tender Type Mapping for Pickup Webservice**********");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("Session object intialized !");
		if (paymentDesc.getPaymentType().equals(PaymentType.CASH)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			if (input.getRoundingAdjustment() != null && input.getRoundingAdjustment().intValue() != 0) {
				log.info("inside the condition if rounding adjustment is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("ROUNDING_EXISTS", "RTLOG_TENDERS"));
			} else if (paymentDesc.getAlternateCurrencyCode() != null) {
				log.info("inside the condition if alternate currency code is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("ALT_CURR_CASH", "RTLOG_TENDERS"));
			} else {
				log.info("inside the condition if CASH ");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("CASH", "RTLOG_TENDERS"));
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.MONEYORDER)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());

			log.info("inside the condition if MONEYORDER ");
			tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("MONEYORDER", "RTLOG_TENDERS"));

		} else if (paymentDesc.getPaymentType().equals(PaymentType.CREDIT)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.VISA)) {
				log.info("when card type is VISA");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("VISA", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			} else if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.MASTER)) {
				log.info("when card type is MASTER");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("MASTERCARD", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			} else if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.AMEX)) {
				log.info("when card type is AMEX");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("AMEX", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			} else if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.DISCOVER)) {
				log.info("when card type is DISCOVER");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("DISCOVER", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			} else if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.DINERS)) {
				log.info("when card type is DINERS");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("DINER", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			} else if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.HOUSECARD)) {
				log.info("when card type is HOUSECARD");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("HOUSECARD", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			} else if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.JCB)) {
				log.info("when card type is JCB");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("JCB", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}

			else if (paymentDesc.getCreditDebitTender().getCardType().equals(CardType.SPAN)) {
				log.info("when card type is SPAN");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("SPAN", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.CHECK)) {
			log.info("*************Inside the Payment type of CHECK****");
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			log.info("$$$$$$$$$$$$ Before Null Check $$$$$$$$$$$$$$$$$$$");

			if (paymentDesc.getCheckTender() != null) {
				log.info("inside the condition if check is not null for CHECK ");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("CHCK", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}

			else if (paymentDesc.getTravelCheckTender() != null) {
				log.info("inside the condition if travel check is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("TRVLCHCK", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			} else if (paymentDesc.getMailCheckTender() != null) {
				log.info("inside the condition if Mail check tender is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("MAILCHCK", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.GIFTCARD)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			if (paymentDesc.getGiftCardTender() != null) {
				log.info("inside the condition if giftcardtender is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("GIFT_CARD", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.GIFTCERT)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			if (paymentDesc.getGiftCertTender() != null) {
				log.info("inside the condition if giftcerttender is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("GIFT_CERTIFICATE", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.STORECREDIT)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			log.info("**********************paymentDesc.getStoreCreditTender() " + paymentDesc.getStoreCreditTender() + "**************************");
			if (paymentDesc.getStoreCreditTender() != null) {
				log.info("inside the condition if storecredit is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("STORE_CREDIT", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.PURCHASEORDER)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			log.info("paymentDesc.getPurchaseOrdTender() " + paymentDesc.getPurchaseOrdTender());
			if (paymentDesc.getPurchaseOrdTender() != null) {
				log.info("inside the condition if purchaseorder is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("PO_TENDER", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.COUPON)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			if (paymentDesc.getCouponTender() != null) {
				log.info("inside the condition if coupontender is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("COUPON_TENDER", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (paymentDesc.getPaymentType().equals(PaymentType.DEBIT)) {
			log.info("Entered the if loop when payment type is----" + paymentDesc.getPaymentType());
			if (paymentDesc.getCreditDebitTender() != null) {
				log.info("inside the condition if Debit is not null");
				tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("DEBIT_CARD", "RTLOG_TENDERS"));
				log.info("tender value is====" + tenderValue);
			}
		} else if (PaymentType.TRAVCHECK.equals(paymentDesc.getPaymentType())) {
			tenderValue = new BigDecimal(session.getOmsSystemParametersFindIndValue("TRAVCHECK", "RTLOG_TENDERS"));
		}
		return tenderValue;
	}
}