package oracle.retail.sim.service.stockcount;

import java.util.List;
import oracle.retail.sim.common.item.Item;
import oracle.retail.sim.common.stockcount.StockCountAuthorizeSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.stockcount.StockCountLineItemAreaBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountLineItemCompBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;

public abstract class StockCountLineItemServices {
  public abstract List<StockCountLineItem> readStockCountLineItems(Long paramLong) throws Exception;
  
  public abstract List<StockCountLineItem> readStockCountLineItems(Long paramLong, List<Long> paramList) throws Exception;
  
  public abstract List<StockCountLineItem> readPrimaryStockCountLineItems(Long paramLong) throws Exception;
  
  public abstract StockCountLineItem readStockCountLineItem(Long paramLong, String paramString) throws Exception;
  
  public abstract List<StockCountLineItemAreaBreakdownVO> findLineItemSequencedAreaBreakdownDetails(Long paramLong1, Long paramLong2, String paramString) throws Exception;
  
  public abstract List<StockCountLineItemCompBreakdownVO> findLineItemComponentCountBreakdownDetails(Long paramLong1, Long paramLong2, String paramString) throws Exception;
  
  public abstract List<StockCountAuthorizeSerialNumber> findAuthorizationLineItemSerialNumbers(Long paramLong1, Long paramLong2, String paramString) throws Exception;
  
  public abstract StockCountLineItem createAdhocLineItem(Long paramLong1, Long paramLong2, Long paramLong3, String paramString) throws Exception;
  
  public abstract StockCountLineItem createUnitAndAmountLineItem(Long paramLong1, Long paramLong2, Long paramLong3, String paramString) throws Exception;
  
  public abstract void updateLineItems(Long paramLong1, Long paramLong2, List<StockCountLineItem> paramList) throws Exception;
  
  public abstract void updateAuthorizedSerialNumbers(StockCountLineItem paramStockCountLineItem, List<StockCountAuthorizeSerialNumber> paramList) throws Exception;
  
  public abstract List<StockCountAuthorizeSerialNumber> generateAuthorizedSerialNumbers(Long paramLong, StockCountLineItem paramStockCountLineItem, Integer paramInteger) throws Exception;
  
  public abstract List<StockCountRejectedLineItem> findRejectedLineItems(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract List<String> updateRejectedLineItems(Long paramLong, List<StockCountRejectedLineItem> paramList) throws Exception;
  
  public abstract void createRejectedLineItem(Long paramLong, StockCountRejectedLineItem paramStockCountRejectedLineItem) throws Exception;
  
  public abstract int getNumberOfUncountedLineItems(Long paramLong1, Long paramLong2, StockCountPhase paramStockCountPhase) throws Exception;
  
  public abstract boolean isAvailableToCount(Long paramLong1, Long paramLong2, Item paramItem) throws Exception;
  
  public abstract StockCountSerialNumber findStockCountSerialNumber(Long paramLong, String paramString1, String paramString2) throws Exception;
  
  public abstract boolean isSerialNumberAlreadyCounted(Long paramLong, StockCountPhase paramStockCountPhase, String paramString1, String paramString2) throws Exception;
  
  public abstract void markStockCountLineItemAsDiscrepant(Long paramLong1, Long paramLong2, String paramString) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\stockcount\StockCountLineItemServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */