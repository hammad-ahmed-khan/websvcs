package oracle.retail.sim.common.itembasket;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.SaleItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ItemBasketLineItem extends BusinessObject {
  private static final long serialVersionUID = 4955918805053349707L;
  
  private Long id;
  
  private SaleItem saleItem;
  
  private String uin;
  
  private Quantity quantity = Quantity.ZERO;
  
  private Quantity caseSize = Quantity.ONE;
  
  private Date createDate;
  
  private Date updateDate;
  
  private boolean dirty;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getItemId() {
    return this.saleItem.getId();
  }
  
  public SaleItem getSaleItem() {
    return this.saleItem;
  }
  
  public void setSaleItem(SaleItem paramSaleItem) throws BusinessException {
    checkForNullParameter("Item", paramSaleItem);
    executeRule("setItem", new Object[] { paramSaleItem });
    doSetSaleItem(paramSaleItem);
    doSetDirty();
  }
  
  public void doSetSaleItem(SaleItem paramSaleItem) {
    this.saleItem = paramSaleItem;
  }
  
  public String getSellingUnitOfMeasure() {
    return this.saleItem.getSellingUnitOfMeasure();
  }
  
  public String getUnitOfMeasure() {
    return this.saleItem.getUnitOfMeasure();
  }
  
  public Quantity getCaseSize() {
    return this.caseSize;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Case Size", paramQuantity);
    executeRule("setCaseSize", new Object[] { paramQuantity });
    doSetCaseSize(paramQuantity);
    doSetDirty();
  }
  
  public void doSetCaseSize(Quantity paramQuantity) {
    this.caseSize = paramQuantity;
  }
  
  public String getUin() {
    return this.uin;
  }
  
  public void setUin(String paramString) throws BusinessException {
    executeRule("setUin", new Object[] { this.quantity });
    doSetUin(paramString);
    doSetDirty();
  }
  
  public void doSetUin(String paramString) {
    this.uin = paramString;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity", paramQuantity);
    executeRule("setQuantity", new Object[] { paramQuantity });
    doSetQuantity(paramQuantity);
    doSetDirty();
  }
  
  public void doSetQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public Date getUpdateDate() {
    return this.updateDate;
  }
  
  public void doSetUpdateDate(Date paramDate) {
    this.updateDate = paramDate;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public void doSetClean() {
    this.dirty = false;
  }
  
  public boolean isDirty() {
    return (this.dirty || this.id == null);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    ItemBasketLineItem itemBasketLineItem = (ItemBasketLineItem)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.saleItem, itemBasketLineItem.saleItem);
    equalsBuilder.append(this.uin, itemBasketLineItem.uin);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.saleItem);
    hashCodeBuilder.append(this.uin);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itembasket\ItemBasketLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */