/**
 * Copyright 2012-2019 The Feign Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package feign.soap;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.jaxb.JAXBContextFactory;

/**
 * Encodes requests using SOAPMessage and JAXB for the body part. <br>
 * <p>
 * Basic example with with Feign.Builder:
 * </p>
 * 
 * <pre>
 * 
 * public interface MyApi {
 * 
 *    &#64;RequestLine("POST /getObject")
 *    &#64;Headers({
 *      "SOAPAction: getObject",
 *      "Content-Type: text/xml"
 *    })
 *    MyJaxbObjectResponse getObject(MyJaxbObjectRequest request);
 *    
 * }
 * 
 * ...
 * 
 * JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
 *     .withMarshallerJAXBEncoding("UTF-8")
 *     .withMarshallerSchemaLocation("http://apihost http://apihost/schema.xsd")
 *     .build();
 *
 * api = Feign.builder()
 *     .encoder(new SOAPEncoder(jaxbFactory))
 *     .target(MyApi.class, "http://api");
 *     
 * ...
 *
 * try {
 *    api.getObject(new MyJaxbObjectRequest());
 * } catch (SOAPFaultException faultException) {
 *    log.info(faultException.getFault().getFaultString());
 * }
 * </pre>
 * 
 * <p>
 * The JAXBContextFactory should be reused across requests as it caches the
 * created JAXB contexts.
 * </p>
 */
public class SOAPEncoder implements Encoder {

	private static final String DEFAULT_SOAP_PROTOCOL = SOAPConstants.SOAP_1_1_PROTOCOL;

	static {
		// ROOT_QNAME_MAP.put(DEFAULT_SOAP_PROTOCOL, FulfillOrderPortType.class.getMethod("", null))
	}

	private final boolean writeXmlDeclaration;
	private final boolean formattedOutput;
	private final Charset charsetEncoding;
	private final JAXBContextFactory jaxbContextFactory;
	private final String soapProtocol;
	private final Map<String, QName> rootQueueMap;

	public SOAPEncoder(JAXBContextFactory jaxbContextFactory, Map<String, QName> rootQueueMap) {
		this.jaxbContextFactory = jaxbContextFactory;
		this.writeXmlDeclaration = true;
		this.formattedOutput = false;
		this.charsetEncoding = Charset.defaultCharset();
		this.soapProtocol = DEFAULT_SOAP_PROTOCOL;
		this.rootQueueMap = rootQueueMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) {
		if (!(bodyType instanceof Class)) {
			throw new UnsupportedOperationException("SOAP only supports encoding raw types. Found " + bodyType);
		}
		try {
			Class<?> clazzType = (Class<?>) bodyType;
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Marshaller marshaller = jaxbContextFactory.createMarshaller((Class<?>) bodyType);	
			marshaller.marshal( new JAXBElement(rootQueueMap.get(clazzType.getName()), clazzType, object), document);
			SOAPMessage soapMessage = MessageFactory.newInstance(soapProtocol).createMessage();
			soapMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, Boolean.toString(writeXmlDeclaration));
			soapMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, charsetEncoding.displayName());
			soapMessage.getSOAPBody().addDocument(document);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if (formattedOutput) {
				Transformer t = TransformerFactory.newInstance().newTransformer();
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				t.transform(new DOMSource(soapMessage.getSOAPPart()), new StreamResult(bos));
			} else {
				soapMessage.writeTo(bos);
			}
			template.body(new String(bos.toByteArray()));
		} catch (Exception e) {
			throw new EncodeException(e.toString(), e);
		}
	}
}
