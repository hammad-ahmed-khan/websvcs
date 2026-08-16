package oracle.retail.sim.common.shelfreplenishment;

import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.item.StockLineItem;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;

public class ShelfReplenishmentLineItem extends BusinessObject implements StockLineItem {
  private static final long serialVersionUID = -6645896609227141520L;
  
  private Long id;
  
  private Long shelfReplenishmentId;
  
  private StockItem stockItem;
  
  private Quantity requestedQuantity;
  
  private Quantity actualQuantity;
  
  private ShelfReplenishmentFromArea fromArea = ShelfReplenishmentFromArea.BACKROOM;
  
  private String shipmentId;
  
  private Quantity caseSize;
  
  private String comment;
  
  public ShelfReplenishmentLineItem(StockItem paramStockItem) {
    this.stockItem = paramStockItem;
    this.caseSize = paramStockItem.getDefaultCaseSize();
  }
  
  public ShelfReplenishmentLineItem(Long paramLong, StockItem paramStockItem) {
    this.shelfReplenishmentId = paramLong;
    this.stockItem = paramStockItem;
    this.caseSize = paramStockItem.getDefaultCaseSize();
  }
  
  public ShelfReplenishmentLineItem() {}
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getShelfReplenishmentId() {
    return this.shelfReplenishmentId;
  }
  
  public void setShelfReplenishmentId(Long paramLong) {
    this.shelfReplenishmentId = paramLong;
  }
  
  public StockItem getStockItem() {
    return this.stockItem;
  }
  
  public Quantity getRequestedQuantity() {
    return this.requestedQuantity;
  }
  
  public void setRequestedQuantity(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Requested Quantity", paramQuantity);
    QuantityCannotBeNegativeRule.execute(paramQuantity);
    executeRule("setRequestedQuantity", new Object[] { paramQuantity });
    doSetRequestedQuantity(paramQuantity);
  }
  
  public void doSetRequestedQuantity(Quantity paramQuantity) {
    this.requestedQuantity = paramQuantity;
  }
  
  public Quantity getActualQuantity() {
    return this.actualQuantity;
  }
  
  public void setActualQuantity(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Actual Quantity", paramQuantity);
    if (paramQuantity.compareTo(Quantity.ZERO) < 0)
      throw new BusinessException(ShelfReplenishmentMessageText.QUANTITY_ERROR); 
    if (this.requestedQuantity.compareTo(paramQuantity) < 0)
      throw new BusinessException(ShelfReplenishmentMessageText.QUANTITY_ERROR); 
    QuantityMustConformToUOMRule.execute(this, paramQuantity);
    executeRule("setActualQuantity", new Object[] { paramQuantity });
    doSetActualQuantity(paramQuantity);
  }
  
  public void doSetActualQuantity(Quantity paramQuantity) {
    this.actualQuantity = paramQuantity;
  }
  
  public Quantity getCaseSize() {
    return this.caseSize;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Case Size", paramQuantity);
    executeRule("setCaseSize", new Object[] { paramQuantity });
    doSetCaseSize(paramQuantity);
  }
  
  public void doSetCaseSize(Quantity paramQuantity) {
    this.caseSize = paramQuantity;
  }
  
  public String getComment() {
    return this.comment;
  }
  
  public void doSetComment(String paramString) {
    this.comment = paramString;
  }
  
  public String getShipmentId() {
    return this.shipmentId;
  }
  
  public void setShipmentId(String paramString) throws BusinessException {
    executeRule("setShipmentId", new Object[] { paramString });
    doSetShipmentId(paramString);
  }
  
  public void doSetShipmentId(String paramString) {
    this.shipmentId = paramString;
  }
  
  public ShelfReplenishmentFromArea getFromArea() {
    return this.fromArea;
  }
  
  public void setFromArea(ShelfReplenishmentFromArea paramShelfReplenishmentFromArea) throws BusinessException {
    checkForNullParameter("From Location", paramShelfReplenishmentFromArea);
    executeRule("setFromArea", new Object[] { paramShelfReplenishmentFromArea });
    doSetFromArea(paramShelfReplenishmentFromArea);
  }
  
  public void doSetFromArea(ShelfReplenishmentFromArea paramShelfReplenishmentFromArea) {
    this.fromArea = paramShelfReplenishmentFromArea;
  }
  
  public boolean isDeletable() {
    return false;
  }
  
  public boolean isPropertyModifiable(String paramString, ShelfReplenishmentStatus paramShelfReplenishmentStatus, Date paramDate) {
    HashSet<ShelfReplenishmentStatus> hashSet = new HashSet(5);
    hashSet.add(ShelfReplenishmentStatus.NEW);
    hashSet.add(ShelfReplenishmentStatus.PENDING_ALTERED);
    hashSet.add(ShelfReplenishmentStatus.CLOSED);
    hashSet.add(ShelfReplenishmentStatus.COMPLETE);
    hashSet.add(ShelfReplenishmentStatus.CANCELED);
    if (hashSet.contains(paramShelfReplenishmentStatus))
      return false; 
    HashSet<String> hashSet1 = new HashSet(3);
    hashSet1.add("actualShelfReplenishmentAmount");
    hashSet1.add("actualQuantityBasedOnUom");
    hashSet1.add("unitOfMeasureMode");
    if (!hashSet1.contains(paramString))
      return false; 
    TimeZone timeZone = SimDateUtil.getGMTTimeZone();
    Date date = SimDateUtil.getCurrentDateAtStartOfDay(timeZone);
    if (!SimDateUtil.isSameDay(timeZone, date, paramDate))
      return false; 
    try {
      executeRule("isPropertyModifiable", new Object[] { paramString });
    } catch (BusinessException businessException) {
      return false;
    } 
    return true;
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == this) ? true : ((paramObject == null || paramObject.getClass() != getClass()) ? false : super.equals(paramObject));
  }
  
  public int hashCode() {
    return super.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishmentLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */