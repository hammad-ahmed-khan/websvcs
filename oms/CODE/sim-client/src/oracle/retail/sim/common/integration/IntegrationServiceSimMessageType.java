package oracle.retail.sim.common.integration;

public enum IntegrationServiceSimMessageType implements SimMessageType {
  EXTERNAL_ITEM_IMAGE("ExternalItemImage", RibSimMessageFamily.ITEMS),
  MANIFEST_CLOSE_SHIPMENT("ManifestCloseShipment", IntegrationServiceSimMessageFamily.MANIFEST_CLOSE_SHIPMENT),
  POS_PROCESS_TRANSACTION("PosTransaction", IntegrationServiceSimMessageFamily.POS_TRANSACTION),
  PRINT_UIN_BATCH("PrintUINBatch", IntegrationServiceSimMessageFamily.PRINT_BATCH),
  SERIALIZATION("Serialization", IntegrationServiceSimMessageFamily.SERIALIZATION),
  UIN_DETAIL("UINDetail", IntegrationServiceSimMessageFamily.SERIALIZATION);
  
  private final String code;
  
  private final SimMessageFamily family;
  
  IntegrationServiceSimMessageType(String paramString1, SimMessageFamily paramSimMessageFamily) {
    this.code = paramString1;
    this.family = paramSimMessageFamily;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public SimMessageFamily getFamily() {
    return this.family;
  }
  
  public String toString() {
    return this.code;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\IntegrationServiceSimMessageType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */