package oracle.retail.sim.service.batch;

import java.util.Date;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import oracle.retail.sim.common.batch.BatchImport;
import oracle.retail.sim.common.batch.ConfigBatchImpExp;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.PriceType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountImportExtract;

public abstract class BatchServices {
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract void itemPriceToHistory(Long paramLong, Date paramDate) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract void purgeItemPrices(Long paramLong, Date paramDate) throws Exception;
  
  public abstract int autoReceiveWarehouseDeliveries(SourceType paramSourceType, Date paramDate1, Date paramDate2) throws Exception;
  
  public abstract int autoReceiveTransfers(Date paramDate1, Date paramDate2) throws Exception;
  
  public abstract ConfigBatchImpExp readConfigBatchImportExport(String paramString) throws Exception;
  
  public abstract long generateStockWastageAdjustment(Long paramLong1, Long paramLong2, Date paramDate) throws Exception;
  
  public abstract int processEmailsForOverdueTransfers() throws Exception;
  
  public abstract int processEmailsForFulfillmentOrderReminders() throws Exception;
  
  public abstract int processEmailsForFulfillmentOrderPickReminders() throws Exception;
  
  public abstract void cleanupShelfReplenishment() throws Exception;
  
  public abstract long closeProductGroupSchedules() throws Exception;
  
  public abstract long deactivateOldUsers(Date paramDate) throws Exception;
  
  public abstract void dexnexFileParser(String paramString) throws Exception;
  
  public abstract int returnNotAfterDateAlerts() throws Exception;
  
  public abstract long purgeDSDReceivings(Date paramDate) throws Exception;
  
  public abstract long purgeWHDReceivings(Date paramDate) throws Exception;
  
  public abstract long purgeReceivedTransfers(Date paramDate) throws Exception;
  
  public abstract long purgeStockCounts(Date paramDate) throws Exception;
  
  public abstract long purgeInventoryAdjustments(Date paramDate) throws Exception;
  
  public abstract long purgeInventoryAdjustTemplate(Date paramDate) throws Exception;
  
  public abstract long purgeStockReturns(Date paramDate) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract long purgePriceChangeWorksheet(Long paramLong, Date paramDate) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract long purgePriceHistories(Long paramLong, Date paramDate) throws Exception;
  
  public abstract long purgeShelfReplenishments(Date paramDate) throws Exception;
  
  public abstract long purgeItemBaskets(Date paramDate) throws Exception;
  
  public abstract long purgeItemRequests(Date paramDate) throws Exception;
  
  public abstract long purgeFulfillmentOrders(Date paramDate) throws Exception;
  
  public abstract long purgeItemTickets(Date paramDate) throws Exception;
  
  public abstract long purgeAudits(Date paramDate) throws Exception;
  
  public abstract long purgeBatchImpExp(Date paramDate) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract long purgeStagedMessage(Date paramDate) throws Exception;
  
  public abstract long purgeLockings(Date paramDate) throws Exception;
  
  public abstract boolean purgeAdhocStockCount(Date paramDate) throws Exception;
  
  public abstract long purgeCompletedUINDetails(Date paramDate) throws Exception;
  
  public abstract long purgeTemporaryUINDetails() throws Exception;
  
  public abstract long purgeUINDetailHistories(Date paramDate) throws Exception;
  
  public abstract long purgeResolvedUINProblems(Date paramDate) throws Exception;
  
  public abstract long purgeDeletedUsers(Date paramDate) throws Exception;
  
  public abstract long purgeInvalidUserRoles(Date paramDate) throws Exception;
  
  public abstract long purgeUserCache(Date paramDate) throws Exception;
  
  public abstract long purgeUserPasswordHistory(Date paramDate) throws Exception;
  
  public abstract long purgeSalesPosting(Date paramDate) throws Exception;
  
  public abstract long purgePurchaseOrders(Date paramDate, int paramInt) throws Exception;
  
  public abstract void generateItemQRCodeTickets(Date paramDate) throws Exception;
  
  public abstract long purgeItem() throws Exception;
  
  public abstract long purgeStoreItemStockHistory(Date paramDate) throws Exception;
  
  public abstract Long parseAndStageSaleAuditImport(byte[] paramArrayOfbyte, BatchImport paramBatchImport) throws Exception;
  
  public abstract Long parseAndStagePosTransactionImport(byte[] paramArrayOfbyte, BatchImport paramBatchImport) throws Exception;
  
  public abstract BatchImport readBatchImpExpExecution(String paramString1, String paramString2) throws Exception;
  
  public abstract Long saveBatchImpExpExecution(BatchImport paramBatchImport) throws Exception;
  
  public abstract void processInRetryPOSTransactions(Long paramLong) throws Exception;
  
  public abstract List<Long> parseAndStagePriceImport(byte[] paramArrayOfbyte, Long paramLong, PriceType paramPriceType) throws Exception;
  
  public abstract boolean extractPriceChange(PriceType paramPriceType, Long paramLong) throws Exception;
  
  public abstract void updatePriceChangeExtractInfo(Long paramLong, ItemPriceStatus paramItemPriceStatus, String paramString) throws Exception;
  
  public abstract List<Long> findAssignedExtractIds(Long paramLong) throws Exception;
  
  public abstract Long getPriceChangeImportCount(Long paramLong) throws Exception;
  
  public abstract long purgeRelatedItems(Date paramDate) throws Exception;
  
  public abstract StockCountImportExtract parseAndStageStockCountImport(byte[] paramArrayOfbyte, Long paramLong) throws Exception;
  
  public abstract StockCount processLoadedThirdPartyStockCount(StockCountImportExtract paramStockCountImportExtract) throws Exception;
  
  public abstract void stageUINAttributeImport(byte[] paramArrayOfbyte) throws Exception;
  
  public abstract void processLoadedUINAttribute() throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\batch\BatchServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */