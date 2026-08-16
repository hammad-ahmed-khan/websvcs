package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.batch.BatchImport;
import oracle.retail.sim.common.batch.ConfigBatchImpExp;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.PriceType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountImportExtract;

@Remote
public interface BatchInterface {
  CompressedObject<Integer> autoReceiveTransfers(CompressedObject<Date> paramCompressedObject1, CompressedObject<Date> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Integer> autoReceiveWarehouseDeliveries(CompressedObject<SourceType> paramCompressedObject, CompressedObject<Date> paramCompressedObject1, CompressedObject<Date> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> cleanupShelfReplenishment(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Long> closeProductGroupSchedules(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Long> deactivateOldUsers(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> dexnexFileParser(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> extractPriceChange(CompressedObject<PriceType> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<Long>> findAssignedExtractIds(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> generateItemQRCodeTickets(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> generateStockWastageAdjustment(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Long> getPriceChangeImportCount(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> itemPriceToHistory(CompressedObject<Long> paramCompressedObject, CompressedObject<Date> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> parseAndStagePosTransactionImport(CompressedObject<byte[]> paramCompressedObject, CompressedObject<BatchImport> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<Long>> parseAndStagePriceImport(CompressedObject<byte[]> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<PriceType> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Long> parseAndStageSaleAuditImport(CompressedObject<byte[]> paramCompressedObject, CompressedObject<BatchImport> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<StockCountImportExtract> parseAndStageStockCountImport(CompressedObject<byte[]> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Integer> processEmailsForFulfillmentOrderPickReminders(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Integer> processEmailsForFulfillmentOrderReminders(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Integer> processEmailsForOverdueTransfers(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> processInRetryPOSTransactions(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<StockCount> processLoadedThirdPartyStockCount(CompressedObject<StockCountImportExtract> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> processLoadedUINAttribute(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Boolean> purgeAdhocStockCount(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeAudits(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeBatchImpExp(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeCompletedUINDetails(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeDSDReceivings(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeDeletedUsers(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeFulfillmentOrders(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeInvalidUserRoles(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeInventoryAdjustTemplate(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeInventoryAdjustments(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeItem(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Long> purgeItemBaskets(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> purgeItemPrices(CompressedObject<Long> paramCompressedObject, CompressedObject<Date> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> purgeItemRequests(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeItemTickets(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeLockings(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgePriceChangeWorksheet(CompressedObject<Long> paramCompressedObject, CompressedObject<Date> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> purgePriceHistories(CompressedObject<Long> paramCompressedObject, CompressedObject<Date> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> purgePurchaseOrders(CompressedObject<Date> paramCompressedObject, CompressedObject<Integer> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> purgeReceivedTransfers(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeRelatedItems(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeResolvedUINProblems(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeSalesPosting(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeShelfReplenishments(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeStagedMessage(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeStockCounts(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeStockReturns(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeStoreItemStockHistory(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeTemporaryUINDetails(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Long> purgeUINDetailHistories(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeUserCache(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeUserPasswordHistory(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> purgeWHDReceivings(CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<BatchImport> readBatchImpExpExecution(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<ConfigBatchImpExp> readConfigBatchImportExport(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Integer> returnNotAfterDateAlerts(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Long> saveBatchImpExpExecution(CompressedObject<BatchImport> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> stageUINAttributeImport(CompressedObject<byte[]> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updatePriceChangeExtractInfo(CompressedObject<Long> paramCompressedObject, CompressedObject<ItemPriceStatus> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\BatchInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */