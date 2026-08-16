package oracle.retail.sim.service.itemprice;

import java.util.Date;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import oracle.retail.sim.common.business.BusinessException;
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

public abstract class ItemPriceServices {
  public abstract ItemPrice readItemPrice(Long paramLong) throws Exception;
  
  public abstract List<ItemPriceVO> readItemPriceVOs(List<Long> paramList) throws Exception;
  
  public abstract PromotionVO readPromotion(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract List<ItemPriceVO> findItemPriceVOs(ItemPriceQueryFilter paramItemPriceQueryFilter) throws Exception;
  
  public abstract ItemPricePerUomVO findItemPricePerUom(String paramString, boolean paramBoolean) throws Exception;
  
  public abstract List<PromotionVO> findPromotionVOs(PromotionQueryFilter paramPromotionQueryFilter) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract FuturePriceVO getPriceOnEffectiveDate(Long paramLong, String paramString, Date paramDate) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract FuturePriceVO futurePriceInquryRPM(Long paramLong, String paramString, Date paramDate) throws BusinessException, Exception;
  
  public abstract List<PriceInfo> findItemPriceHistory(Long paramLong, String paramString, int paramInt) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract PriceChange requestNewItemPrice(ItemPrice paramItemPrice) throws BusinessException, Exception;
  
  public abstract void savePriceChanges(PriceType paramPriceType, List<PriceChange> paramList) throws Exception;
  
  public abstract void updateItemPriceStatus(List<Long> paramList, ItemPriceStatus paramItemPriceStatus) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\itemprice\ItemPriceServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */