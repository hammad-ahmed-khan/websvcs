package oms.logicinfo.com.model;

import com.logicinfo.oms.util.OMSUtil;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Logger;

@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso( { ObjectFactory.class })
@WebService(name = "SIMDeliveryDetailWebService", serviceName = "SIMDeliveryDetailWebService", targetNamespace = "http://com.logicinfo.oms/model/", portName = "SIMDeliveryDetailWebService", wsdlLocation = "/WEB-INF/wsdl/SIMDeliveryDetail.wsdl")
public class SIMDeliveryDetailWebServiceImpl 
{
    
    private final static Logger log = Logger.getLogger(SIMDeliveryDetailWebServiceImpl.class.getName());
    
    public SIMDeliveryDetailWebServiceImpl() 
    {
    }

    @WebResult(name = "processOrderResp", partName = "return", targetNamespace = "http://com.logicinfo.oms/model/")
    @WebMethod
    public SimDeliveryDetailResponse processNewOrder(@WebParam(name = "processOrderReq", partName = "input", targetNamespace = "http://com.logicinfo.oms/model/")
        SIMDeliveryDetail input)  throws SOAPException
    {
        log.info("Inside IMPL class of SIMDeliveryDetail");
        
        SIMDeliveryDetailBean SIMDeliveryDetailBean = new SIMDeliveryDetailBean();
        SIMDeliveryDetailCommon SIMDeliveryDetailCommon = new SIMDeliveryDetailCommon();
        SimDeliveryDetailResponse SIMDeliveryDetailResponse = null;
        SIMDeliveryDetailBean.validateInput(input);
        
        try
        {
            SIMDeliveryDetailResponse = SIMDeliveryDetailBean.processOrdersDetails(input);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(e.toString()));
        }
        
        return SIMDeliveryDetailResponse;
    }
}

