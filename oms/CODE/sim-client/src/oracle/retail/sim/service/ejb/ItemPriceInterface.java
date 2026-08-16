package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.itemprice.FuturePriceVO;
import oracle.retail.sim.common.itemprice.ItemPrice;
import oracle.retail.sim.common.itemprice.ItemPriceQueryFilter;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.ItemPriceVO;
import oracle.retail.sim.common.itemprice.PriceChange;
import oracle.retail.sim.common.itemprice.PriceInfo;
import oracle.retail.sim.common.itemprice.PriceType;
import oracle.retail.sim.common.itemprice.PromotionQueryFilter;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.common.itemticket.ItemPricePerUomVO;

@Remote
public interface ItemPriceInterface {
  CompressedObject<List<PriceInfo>> findItemPriceHistory(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<Integer> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<ItemPricePerUomVO> findItemPricePerUom(CompressedObject<String> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ItemPriceVO>> findItemPriceVOs(CompressedObject<ItemPriceQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<PromotionVO>> findPromotionVOs(CompressedObject<PromotionQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FuturePriceVO> futurePriceInquryRPM(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<Date> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<FuturePriceVO> getPriceOnEffectiveDate(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<Date> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<ItemPrice> readItemPrice(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemPriceVO>> readItemPriceVOs(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<PromotionVO> readPromotion(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<PriceChange> requestNewItemPrice(CompressedObject<ItemPrice> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> savePriceChanges(CompressedObject<PriceType> paramCompressedObject, CompressedObject<List<PriceChange>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateItemPriceStatus(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<ItemPriceStatus> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ItemPriceInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */