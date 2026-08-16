package oracle.retail.sim.common.directdelivery;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.SerialNumberLineItem;

public interface DirectDeliveryLineItem extends SerialNumberLineItem {
  boolean isNew();
  
  boolean isDirty();
  
  Quantity getCaseSize();
  
  void setCaseSize(Quantity paramQuantity) throws BusinessException;
  
  SimMoney getUnitCost();
  
  void setUnitCost(SimMoney paramSimMoney) throws BusinessException;
  
  boolean isReceivable();
  
  boolean isExpected();
  
  Quantity getQuantityExpected();
  
  Quantity getQuantityExpectedOrZero();
  
  Quantity getQuantityReceived();
  
  Quantity getQuantityReceivedOrZero();
  
  void setQuantityReceived(Quantity paramQuantity) throws BusinessException;
  
  Quantity getQuantityDamaged();
  
  Quantity getQuantityDamagedOrZero();
  
  void setQuantityDamaged(Quantity paramQuantity) throws BusinessException;
  
  Quantity getQuantityReceivedDiscrepant();
  
  Quantity getQuantityReceivedDiscrepantOrZero();
  
  void setQuantityReceivedDiscrepant(Quantity paramQuantity) throws BusinessException;
  
  Quantity getQuantityDamagedDiscrepant();
  
  Quantity getQuantityDamagedDiscrepantOrZero();
  
  void setQuantityDamagedDiscrepant(Quantity paramQuantity) throws BusinessException;
  
  Quantity getQuantityShipped();
  
  Quantity getQuantityShippedOrZero();
  
  void clearReceivedQuantities() throws BusinessException;
  
  DirectDeliveryCarton getCarton();
  
  void doSetCarton(DirectDeliveryCarton paramDirectDeliveryCarton);
  
  void removeSerialNumbers() throws BusinessException;
  
  void resetSerialNumberQuantities() throws BusinessException;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */