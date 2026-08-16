package org.extra.deliveryUpdate;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController
{

  private static final Logger logger = LogManager.getLogger(DeliveryController.class.getName());

  @Autowired
  private DeliveryService deliveryService;

  @PostMapping(value = "/update")
  public ResponseEntity<DeliveryResponse> updateDeliveryDetails(@RequestBody DeliveryRequest request)
  {
    logger.info("Inside updateDeliveryDetails for omsCustOrdNo " + request.getOmsCustOrdNo());
    DeliveryResponse response = new DeliveryResponse();
    String message = validateDeliveryRequest(request);
    logger.info("Validated messsage for omsCustOrdNo " + request.getOmsCustOrdNo() + "is " + message);

    if (message.equals("proceed"))
    {
      response = deliveryService.updateDeliveryService(request);
    }
    else
    {
      response.setStatus("Failure");
      response.setMessage(message);
    }

    return new ResponseEntity<DeliveryResponse>(response, HttpStatus.OK);
  }

  public String validateDeliveryRequest(DeliveryRequest request)
  {
    String message = "proceed";
    if (request != null)
    {
      if (request.getOrderNo() == null || request.getOrderNo().isEmpty() || request.getOrderNo().equals(""))
      {
        return "OrderNo is null";
      }
      
      if (request.getOmsCustOrdNo() == 0 )
      {
        return "OmsCustOrdNo is null";
      }
     
      if (request.getClassification() == null || request.getClassification().isEmpty() || request.getClassification().equals(""))
      {
        return "Classification is null";
      }
      
     /* if ((request.getBookingId() != null || request.getBookingId().intValue() <= 0)
          ||(request.getAddress() != null || request.getAddress().isEmpty() || request.getAddress().equals(""))
          || (request.getFirstName() != null || request.getFirstName().isEmpty() || request.getFirstName().equals(""))
          || (request.getLastName() != null || request.getLastName().isEmpty() || request.getLastName().equals(""))
          || (request.getMobileNo() != null || request.getMobileNo().isEmpty() || request.getMobileNo().equals("")))
      {
        return "request must contain any one of the attributes(BookingId,Address,FirstName,Lastname,MobileNo) with valid values";
      }*/
    }
    return message;
  }

}
