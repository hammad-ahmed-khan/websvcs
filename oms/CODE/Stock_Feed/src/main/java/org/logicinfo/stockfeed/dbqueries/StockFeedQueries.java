package org.logicinfo.stockfeed.dbqueries;

public class StockFeedQueries
{

  public static String physicalStockDeltaQuery_ST = "select ITEM_ID ITEM,location,AVAIL_QTY from TEST_ITEM_STOCK_DTL_V WHERE LAST_UPDATE_DATETIME >= (SELECT MAX(CREATE_DATETIME) FROM STOCK_FEED@rmsdb WHERE STATUS = 'Success' AND REQUEST_TYPE = 'PhysicalDeltaFeed-ST') AND LOCATION NOT IN (19008, 19009)"; 
  
  public static String physicalStockDeltaQuery_WH = " select ITEM,LOC location,AVAIL_QTY from XX_TEST_INVAVAIL_V where GREATEST (LAST_UPDATE_DATETIME , NVL(SOH_UPDATE_DATETIME,LAST_UPDATE_DATETIME)) > (SELECT MAX(CREATE_DATETIME) FROM STOCK_FEED WHERE STATUS = 'Success' AND REQUEST_TYPE = 'PhysicalDeltaFeed-WH') and loc NOT IN (1088, 1098) and loc_type = 'W'";
  
  public static String futureInventoryQuery = " select * from V_CUST_FUTURE_INV_ECOM_FEED_B where location NOT IN (1098, 19008, 19009) ";
  
  public static String omsBackOrderDtltableQuery = "select sum(SOURCE_QTY-FULFILL_QTY) unFullFilledQty from oms_back_order_dtl where BACKORDER_STATUS='N' and SOURCE_QTY>0 and item =':itm' and SOURCE_LOC=:loc";

  public static String stock_feedTableQuery = " Select to_char ((max(create_datetime)),'MM/DD/YYYY HH24:MI:SS') max_dt from stock_feed where STATUS='Success' and REQUEST_TYPE=':phy'";

}
