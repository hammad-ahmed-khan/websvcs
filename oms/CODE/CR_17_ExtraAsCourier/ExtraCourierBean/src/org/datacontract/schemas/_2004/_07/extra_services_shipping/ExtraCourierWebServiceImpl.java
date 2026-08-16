package org.datacontract.schemas._2004._07.extra_services_shipping;

import java.io.StringWriter;

import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlSeeAlso;

import javax.xml.ws.Action;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.datacontract.schemas._2004._07.extra_services_shipping.repo.CreateLabelCall;
import org.datacontract.schemas._2004._07.extra_services_shipping.repo.ExtraCouierResponse;


@XmlSeeAlso( { ObjectFactory.class })
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT,
                            parameterStyle =
                            javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
@WebService(name = "ExtraCourierWebService",
            serviceName = "ExtraCourierService",
            targetNamespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto",
            portName = "ExtraCourierWebService",
            wsdlLocation = "/WEB-INF/wsdl/ExtraCourierService.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class ExtraCourierWebServiceImpl {
    
    private final static Logger log = Logger.getLogger(ExtraCourierWebServiceImpl.class.getName());
    public ExtraCourierWebServiceImpl() {
    }

    @javax.jws.soap.SOAPBinding(parameterStyle =
                                javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
    @Action(input =
            "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto/CreateShipment",
            output =
            "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto/ExtraCourierWebService/CreateShipmentResponse")
    @WebMethod(operationName = "CreateShipment",
               action = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto/CreateShipment")
    @WebResult(name = "CreateShipmentResponse",
               targetNamespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto",
               partName = "return")
    public ShipmentResponse createShipment(@WebParam(name = "Shipment",
                                                     partName = "input",
                                                     targetNamespace =
                                                     "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto")
        Shipment input) {
        
        log.info("The input is "+input);
        
        JAXBContext jaxbContext;
        StringWriter stringWriter = new StringWriter();
        ExtraCouierResponse executeResponseObj = null;
        CreateLabelCall createLabelObj = new CreateLabelCall();

        try {
            jaxbContext = JAXBContext.newInstance(CreateShipment.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(input, stringWriter);
            String xmlString = stringWriter.toString();
            System.out.println(xmlString);
            executeResponseObj =
                    createLabelObj.callWebService(xmlString, "SIMUSER");
        } catch (JAXBException e) {
            System.out.println("**************************");
        }
        System.out.println("Airway bill no" +
                           executeResponseObj.getAirwaryBillno());

        System.out.println("Label data " + executeResponseObj.getLabelData());

        System.out.println(" error message is " +
                           executeResponseObj.getErrorMessage());

        System.out.println("Package Result is " +
                           executeResponseObj.getPackageResult());

        ShipmentResponse shipmentresponseObj = new ShipmentResponse();

        shipmentresponseObj.setAirwayBillNo(executeResponseObj.getAirwaryBillno());
        shipmentresponseObj.setLabelData(executeResponseObj.getLabelData());
        shipmentresponseObj.setErrorMessage(executeResponseObj.getErrorMessage());
        return shipmentresponseObj;
    }
}
