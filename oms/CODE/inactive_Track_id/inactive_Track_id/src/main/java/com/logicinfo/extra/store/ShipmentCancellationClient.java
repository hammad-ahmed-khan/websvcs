// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.store;

import com.google.gson.Gson;
import java.sql.SQLException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;
import com.logicinfo.extra.util.PropertiesReader;
import com.sun.jersey.api.client.Client;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

public class ShipmentCancellationClient
{
    static Logger LOGGER;
    
    static {
        ShipmentCancellationClient.LOGGER = Logger.getLogger((Class)ShipmentCancellationClient.class);
    }
    
    public static void callInactiveOrdrdelete(final AwbCancellationRequest awbcancellationRequestObj, final String source) {
        ShipmentCancellationClient.LOGGER.info((Object)" *************Begin of  callInactiveOrdrdelete method*********");
        final Gson gson = new GsonBuilder().create();
        System.setProperty("https.protocols", "TLSv1.1");
        ShipmentCancellationClient.LOGGER.info((Object)("Order No\t" + awbcancellationRequestObj.getOrderNo() + "Tracking No\t" + awbcancellationRequestObj.getCourierTrackingNo() + "Carrier Name\t" + awbcancellationRequestObj.getCarrier()));
        final String mailInfoString = gson.toJson((Object)awbcancellationRequestObj);
        ShipmentCancellationClient.LOGGER.info((Object)("Json Object is " + mailInfoString));
        final Client client = Client.create();
        final WebResource webResource = client.resource(PropertiesReader.getProperty("sim.invalide.tracking.id.update.api.url"));
        final ClientResponse response = (ClientResponse)((WebResource.Builder)webResource.type("application/json").header("Authorization", (Object)("Basic " + PropertiesReader.getProperty("client.and.secret.id.base64.encoded")))).delete((Class)ClientResponse.class, (Object)mailInfoString);
        ShipmentCancellationClient.LOGGER.info((Object)("Order No\t" + awbcancellationRequestObj.getOrderNo() + "Tracking No\t" + awbcancellationRequestObj.getCourierTrackingNo() + "Carrier Name\t" + awbcancellationRequestObj.getCarrier() + "Response status" + response.getStatus()));
        if (response.getStatus() == 200) {
            try {
                System.out.println("inside trackingOrderListObj ");
                final TrackingOrderList trackingOrderListObj = new TrackingOrderList();
                trackingOrderListObj.updateProcessIndFlagAndDate(awbcancellationRequestObj, source);
            }
            catch (SQLException e) {
                ShipmentCancellationClient.LOGGER.error((Object)("ShipmentCancellationClient::callInactiveOrddelete method SQLException" + e));
            }
            catch (Exception e2) {
                ShipmentCancellationClient.LOGGER.error((Object)("ShipmentCancellationClient::callInactiveOrddelete method Exception" + e2));
            }
        }
        else {
            try {
                System.out.println("Inside trackingOrderListObj Count method ");
                final TrackingOrderList trackingOrderListObj = new TrackingOrderList();
                trackingOrderListObj.updateProcessIndCountAndDate(awbcancellationRequestObj, source);
            }
            catch (SQLException e) {
                ShipmentCancellationClient.LOGGER.error((Object)("ShipmentCancellationClient::callInactiveOrderdelete method SQLException" + e));
            }
            catch (Exception e2) {
                ShipmentCancellationClient.LOGGER.error((Object)("ShipmentCancellationClient::callInactiveOrderdelete method Exception" + e2));
            }
        }
        ShipmentCancellationClient.LOGGER.info((Object)"************End of  callInactiveOrdrdelete method***********");
    }
}
