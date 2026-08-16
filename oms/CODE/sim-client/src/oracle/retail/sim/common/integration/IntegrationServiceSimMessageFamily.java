package oracle.retail.sim.common.integration;

public enum IntegrationServiceSimMessageFamily implements SimMessageFamily {
  CUSTOMER_ORDER("CustomerOrder"),
  EXTERNAL_INV_ADJ("ExternalInvAdj"),
  MANIFEST_CLOSE_SHIPMENT("ManifestCloseShipment"),
  POS_TRANSACTION("PosTransaction"),
  PRINT_BATCH("PrintBatch"),
  SALE_RET_TXN("SaleRetTxn"),
  SERIALIZATION("Serialization");
  
  private final String code;
  
  IntegrationServiceSimMessageFamily(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\IntegrationServiceSimMessageFamily.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */