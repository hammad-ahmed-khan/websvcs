package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.item.Item;
import oracle.retail.sim.common.stockcount.StockCountAuthorizeSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.stockcount.StockCountLineItemAreaBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountLineItemCompBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;

@Remote
public interface StockCountLineItemInterface {
  CompressedObject<StockCountLineItem> createAdhocLineItem(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Long> paramCompressedObject3, CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<?> createRejectedLineItem(CompressedObject<Long> paramCompressedObject, CompressedObject<StockCountRejectedLineItem> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<StockCountLineItem> createUnitAndAmountLineItem(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Long> paramCompressedObject3, CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<List<StockCountAuthorizeSerialNumber>> findAuthorizationLineItemSerialNumbers(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<StockCountLineItemCompBreakdownVO>> findLineItemComponentCountBreakdownDetails(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<StockCountLineItemAreaBreakdownVO>> findLineItemSequencedAreaBreakdownDetails(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<StockCountRejectedLineItem>> findRejectedLineItems(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<StockCountSerialNumber> findStockCountSerialNumber(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<StockCountAuthorizeSerialNumber>> generateAuthorizedSerialNumbers(CompressedObject<Long> paramCompressedObject, CompressedObject<StockCountLineItem> paramCompressedObject1, CompressedObject<Integer> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Integer> getNumberOfUncountedLineItems(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<StockCountPhase> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Boolean> isAvailableToCount(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Item> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Boolean> isSerialNumberAlreadyCounted(CompressedObject<Long> paramCompressedObject, CompressedObject<StockCountPhase> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<?> markStockCountLineItemAsDiscrepant(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<StockCountLineItem>> readPrimaryStockCountLineItems(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<StockCountLineItem> readStockCountLineItem(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<StockCountLineItem>> readStockCountLineItems(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<StockCountLineItem>> readStockCountLineItems2(CompressedObject<Long> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateAuthorizedSerialNumbers(CompressedObject<StockCountLineItem> paramCompressedObject, CompressedObject<List<StockCountAuthorizeSerialNumber>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateLineItems(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<List<StockCountLineItem>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<String>> updateRejectedLineItems(CompressedObject<Long> paramCompressedObject, CompressedObject<List<StockCountRejectedLineItem>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\StockCountLineItemInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */