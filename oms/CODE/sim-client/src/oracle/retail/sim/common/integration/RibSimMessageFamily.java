package oracle.retail.sim.common.integration;

public enum RibSimMessageFamily implements SimMessageFamily {
  ASNIN("ASNIn"),
  ASNOUT("ASNOut"),
  CLRPRCCHG("ClrPrcChg"),
  DIFFS("Diffs"),
  DLVYSLT("DlvySlt"),
  DSDRECEIPT("DSDReceipt"),
  FULFILORD("FulfilOrd"),
  FULFILORDCFM("FulfilOrdCfm"),
  FULFILORDCFMCNC("FulfilOrdCfmCnc"),
  INVADJUST("InvAdjust"),
  INVREQ("InvReq"),
  ITEMLOC("ItemLoc"),
  ITEMS("Items"),
  MERCHHIER("MerchHier"),
  ORDER("Order"),
  PARTNER("Partner"),
  PRMPRCCHG("PrmPrcChg"),
  RCVUNITADJ("RcvUnitAdj"),
  RECEIVING("Receiving"),
  REGPRCCHG("RegPrcChg"),
  RTV("RTV"),
  RTVREQ("RTVReq"),
  SEEDDATA("SeedData"),
  SHIPINFO("ShipInfo"),
  SOSTATUS("SOStatus"),
  STKCOUNTSCH("StkCountSch"),
  STOCKORDER("StockOrder"),
  STORES("Stores"),
  UDAS("UDAs"),
  VENDOR("Vendor"),
  WH("WH");
  
  private final String code;
  
  RibSimMessageFamily(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\RibSimMessageFamily.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */