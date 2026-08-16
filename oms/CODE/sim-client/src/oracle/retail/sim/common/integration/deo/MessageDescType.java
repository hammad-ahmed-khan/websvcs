package oracle.retail.sim.common.integration.deo;

import oracle.retail.sim.common.core.SimEnum;

public enum MessageDescType implements SimEnum<String> {
  ASN("ASN"),
  LOCATION_ID("LOCATION_ID"),
  CLASS_ID("CLASS_ID"),
  CLEARANCE_ID("CLEARANCE_ID"),
  COUNTRY_ID("COUNTRY_ID"),
  CUSTOMER_ORDER_ID("CUSTOMER_ORDER_ID"),
  DELIVERYSLOT_ID("DELIVERYSLOT_ID"),
  DEPT_ID("DEPT_ID"),
  DIFF_ID("DIFF_ID"),
  DIFF_TYPE("DIFF_TYPE"),
  DISTRO_NUM("DISTRO_NUM"),
  IMAGE_ID("IMAGE_ID"),
  ITEM_ID("ITEM_ID"),
  ITEM_PARENT_ID("ITEM_PARENT_ID"),
  ORGUNIT_ID("ORGUNIT_ID"),
  PACK_NUM("PACK_NUM"),
  PARTNER_ID("PARTNER_ID"),
  PO_NUM("PO_NUM"),
  PROMOTION_ID("PROMOTION_ID"),
  PRICE_CHANGE_ID("PRICE_CHANGE_ID"),
  REPLICATION("REPLICATION"),
  RETURN_ASN("RETURN_ASN"),
  RETURN_ID("RETURN_ID"),
  RTV_ORDER_NUM("RTV_ORDER_NUM"),
  SUB_CLASS_ID("SUB_CLASS_ID"),
  SUPPLIER_ID("SUPPLIER_ID"),
  TICKET_TYPE_ID("TICKET_TYPE_ID"),
  TRANSFER_ASN("TRANSFER_ASN"),
  TRANSFER_ID("TRANSFER_ID"),
  UIN("UIN"),
  UDA_ID("UDA_ID"),
  UDA_VALUE_ID("UDA_VALUE_ID");
  
  public static final String TYPE_DELIMITER = "|";
  
  private final String name;
  
  MessageDescType(String paramString1) {
    this.name = paramString1;
  }
  
  public String getCode() {
    return this.name;
  }
  
  public String toString() {
    return this.name;
  }
  
  public static MessageDescType toValue(String paramString) {
    if (paramString != null)
      for (MessageDescType messageDescType : values()) {
        if (messageDescType.name.equals(paramString))
          return messageDescType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\deo\MessageDescType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */