package com.extra.test;

import java.io.IOException;
import java.io.Reader;

import javax.xml.ws.soap.SOAPFaultException;

import com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException;

import feign.Feign;
import feign.Response;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import feign.soap.SOAPErrorDecoder;

public class AppTest {

	public static void main(String[] args) throws IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {

		JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
			     .withMarshallerJAXBEncoding("UTF-8")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=1")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=2")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=3")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=4")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=5")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=6")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=7")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=8")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=9")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=10")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=11")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=12")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=13")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=14")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=15")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=16")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=17")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=18")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=19")
			     .withMarshallerSchemaLocation("http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService?xsd=20")
			     .build();

		StoreFulfillmentOrderPortType api = Feign.builder()
		     .encoder(new SOAPEncoder(jaxbFactory))
		     .decoder(new SOAPDecoder(jaxbFactory))
		     .errorDecoder(new SOAPErrorDecoder() {
		    	 @Override
		    	public Exception decode(String methodKey, Response response) {
		    		 Reader initialReader = null;
					try {
						initialReader = response.body().asReader();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		    char[] arr = new char[8 * 1024];
		    		    StringBuilder buffer = new StringBuilder();
		    		    int numCharsRead;
		    		    try {
							while ((numCharsRead = initialReader.read(arr, 0, arr.length)) != -1) {
							    buffer.append(arr, 0, numCharsRead);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		    		    try {
							initialReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		    String targetString = buffer.toString();
		    		 System.out.println(targetString);
		    		return super.decode(methodKey, response);
		    	}
		     })
		     .target(StoreFulfillmentOrderPortType.class, "http://192.168.41.195:7511/StoreFulfillmentOrderBean/StoreFulfillmentOrderService");

		 try {
			 StrFordRef ref = new StrFordRef(); 
			 ref.setIntFulfillmentOrderId(155490L);
			 api.readFulfillmentOrderDetail(ref);
			 System.out.println();
		 } catch (SOAPFaultException e) {
			 e.printStackTrace();
		 }
	}

}
