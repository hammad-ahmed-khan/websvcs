package com.logicinfo.oms.model;

import java.math.BigDecimal;

public class BackOrderRepublish {

    public String backOrderXML(String item, Long location, String locType, BigDecimal channelId, String unitOfMeasure,
                               Long backorderqty)

    {

        String a = "\"";
        String soapenvelopetag =
            "<soapenv:Envelope xmlns:soapenv=" + a + "http://schemas.xmlsoap.org/soap/envelope/" + a + "\nxmlns:v1=" +
            a + "http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1" + a +
            "\nxmlns:v11=" + a + "http://www.oracle.com/retail/integration/base/bo/InvBackOrdColDesc/v1" + a +
            "\nxmlns:v12=" + a + "http://www.oracle.com/retail/integration/base/bo/InvBackOrdDesc/v1" + a + ">";
        String headertag =
            "\n<soapenv:Header/>\n<soapenv:Body>\n<v1:createInvBackOrdColDesc>\n<v11:InvBackOrdColDesc>\n<v12:InvBackOrdDesc>";
        String Valuetag1 =
            "\n<v12:item>" + item + "</v12:item>" + "\n<v12:loc_type>" + locType + "</v12:loc_type>" + "\n<v12:location>" +
            location + "</v12:location>";
        String channelIdtagValue = "\n<v12:channel_id>" + channelId + "</v12:channel_id>";
        String Valuetag2 =
            "\n<v12:backorder_qty>" + backorderqty + "</v12:backorder_qty>" + "\n<v12:unit_of_measure>" +
            unitOfMeasure + "</v12:unit_of_measure>";
        String endtag =
            "</v12:InvBackOrdDesc>\n<v11:collection_size>1</v11:collection_size>\n</v11:InvBackOrdColDesc>" +
            "\n</v1:createInvBackOrdColDesc>\n</soapenv:Body>\n</soapenv:Envelope>";
        if (channelId.intValue() > 0)
            return soapenvelopetag + headertag + Valuetag1 + channelIdtagValue + Valuetag2 + endtag;
        else
            return soapenvelopetag + headertag + Valuetag1 + Valuetag2 + endtag;
    }
}
