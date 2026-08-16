package com.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;

import javax.naming.NamingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;
import com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.DeliveryType;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfilOrdDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.YesNoInd;
import com.oracle.retail.integration.base.bo.fulfilorddtl.v1.FulfilOrdDtl;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderService;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException;

public class AppTest {

	public static void main(String a[])
			throws NamingException, NoSuchAlgorithmException, DatatypeConfigurationException, EncryptedDocumentException, FileNotFoundException, IOException, InvalidFormatException {

		StoreFulfillmentOrderPortType simFulfillOrderPortType = new StoreFulfillmentOrderService().getStoreFulfillmentOrderPort();

		FulfilOrdColDesc fulfilOrdCfmDesc = new FulfilOrdColDesc();
		FulfilOrdDesc fulfilOrdDesc = new FulfilOrdDesc();
		fulfilOrdDesc.setCustomerOrderNo("WEBFULFILLTSTA113");
		fulfilOrdDesc.setFulfillOrderNo("2");

		fulfilOrdDesc.setFulfillLocId(13003);
		fulfilOrdDesc.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType.valueOf("S"));
		fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.N);
		fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf("S"));
		GregorianCalendar greCal = new GregorianCalendar();
		fulfilOrdDesc.setConsumerDeliveryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(greCal));

		FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
		fulfilOrdCustDesc.setCustomerNo("1");
		fulfilOrdCustDesc.setDeliverFirstName("MANI");
		fulfilOrdCustDesc.setDeliverLastName("GANDAN");
		fulfilOrdCustDesc.setDeliverAdd1("KHALID BIN AL WALEED ST.RAKAH AL KHOBAR? Al Jisr, Al Khobar Saudi Arabia");
		fulfilOrdCustDesc.setDeliverCity("TABUK");
		fulfilOrdCustDesc.setDeliverState("56");
		fulfilOrdCustDesc.setDeliverCountryId("SA");

		fulfilOrdCustDesc.setBillFirstName("Nilesh");
		fulfilOrdCustDesc.setBillLastName("Jha");
		fulfilOrdCustDesc.setBillAdd1("KHALID BIN AL WALEED ST.RAKAH AL KHOBAR? Al Jisr, Al Khobar Saudi Arabia");
		fulfilOrdCustDesc.setBillAdd2("jha.n@extra.com");
		fulfilOrdCustDesc.setBillCity("DAMMAM");
		fulfilOrdCustDesc.setBillState("13");
		fulfilOrdCustDesc.setBillCountryId("SA");
		fulfilOrdCustDesc.setBillCounty("SA");

		fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);

		FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
		fulfilOrdDtl.setItem("00102234");
		fulfilOrdDtl.setOrderQtySuom(BigDecimal.ONE);
		fulfilOrdDtl.setStandardUom("EA");
		fulfilOrdDtl.setTransactionUom("EA");
		fulfilOrdDtl.setSubstituteInd("N");
		fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);

		fulfilOrdCfmDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
		fulfilOrdCfmDesc.setCollectionSize(fulfilOrdCfmDesc.getFulfilOrdDesc().size());

		try {
			simFulfillOrderPortType.createFulfillmentOrderDetail(fulfilOrdCfmDesc);
		} catch (IllegalArgumentWSFaultException e) {
			e.printStackTrace();
		} catch (IllegalStateWSFaultException e) {
			e.printStackTrace();
		} catch (ValidationWSFaultException e) {
			e.printStackTrace();
		}

		/*
		 * TransferCreationRequest request = new TransferCreationRequest();
		 * request.setCust_ord_no("WEBCARTST006"); request.setDest_id(1141);
		 * request.setFul_loc(1141); request.setFul_ord_no(2);
		 * request.setRef_no("53538321"); request.setSrc_id(107); request.setSrc_loc(0);
		 * List<TransferRequest> items = new ArrayList<TransferRequest>();
		 * TransferRequest r = new TransferRequest(); r.setItem("00173703");
		 * r.setQty(1); items.add(r); request.setCustomerItems(items);
		 * 
		 * Feign.builder().encoder(new GsonEncoder()) .logger(new
		 * Logger.JavaLogger().appendToFile("http.log")) .logLevel(Logger.Level.FULL)
		 * .decoder(new GsonDecoder()).target(ICarreraTransfer.class,
		 * "http://exvm-qaribapp01.extrastores.com:7701/CarreraTransferCreation").
		 * getOrderItemsResponse(request);
		 */

		/*
		 * DateFormat format = new SimpleDateFormat("EEEE"); Calendar today =
		 * Calendar.getInstance(); int count = 1; for (int i = 1; i <= 7; i++) { for
		 * (int leadTime = 1; leadTime <= 6; leadTime++) {
		 * System.out.println("----------Case " + (count) +
		 * " --------------------------"); Calendar toDate = (Calendar) today.clone();
		 * toDate.add(Calendar.DATE, leadTime); int fromDayOfWeek =
		 * today.get(Calendar.DAY_OF_WEEK); int toDayOfWeek =
		 * toDate.get(Calendar.DAY_OF_WEEK); System.out.println("Start Day -> " +
		 * format.format(today.getTime())); System.out.println("End Day -> " +
		 * format.format(toDate.getTime())); if (leadTime >= 7 || fromDayOfWeek ==
		 * Calendar.FRIDAY || Calendar.FRIDAY == toDayOfWeek || (fromDayOfWeek <
		 * Calendar.FRIDAY && Calendar.FRIDAY < toDayOfWeek) || (fromDayOfWeek <
		 * Calendar.FRIDAY && toDayOfWeek < fromDayOfWeek)) {
		 * System.out.println("friday coming in between toady and lead date..."); } else
		 * { System.out.println("No grace day."); } System.out.println(
		 * "________________________________________________________________________________________________"
		 * ); count++; } today.add(Calendar.DATE, 1); }
		 */

		// Workbook wb = workbookfactory.create(new FileInputStream(""));
	}
}
