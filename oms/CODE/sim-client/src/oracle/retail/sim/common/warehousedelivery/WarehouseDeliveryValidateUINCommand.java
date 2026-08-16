package oracle.retail.sim.common.warehousedelivery;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.Command;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;

public class WarehouseDeliveryValidateUINCommand extends Command {
  private SerialNumberValue serialNumber;
  
  private boolean isNewOnTransaction;
  
  private String uinLabel;
  
  private Long storeId;
  
  public void setSerialNumber(SerialNumberValue paramSerialNumberValue) {
    this.serialNumber = paramSerialNumberValue;
  }
  
  public void setNewOnTransaction(boolean paramBoolean) {
    this.isNewOnTransaction = paramBoolean;
  }
  
  public void setUINLabel(String paramString) {
    this.uinLabel = paramString;
  }
  
  public void setStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  protected void doExecute() throws Exception {
    if (this.serialNumber.getStatus() == null)
      throw new IllegalArgumentException("UINStatus cannot be null!"); 
    UINStatus uINStatus = this.serialNumber.getStatus();
    if (!UINStatus.getValidSetForDeliveryReceipt().contains(uINStatus)) {
      String[] arrayOfString = new String[3];
      arrayOfString[0] = this.uinLabel;
      arrayOfString[1] = this.serialNumber.getUin();
      arrayOfString[2] = this.serialNumber.getStatus().toString();
      throw new BusinessException(UINMessageText.UIN_CANNOT_BE_RECEIVED, arrayOfString);
    } 
    if (this.isNewOnTransaction) {
      if (uINStatus == UINStatus.UNCONFIRMED)
        return; 
      if (uINStatus == UINStatus.IN_RECEIVING) {
        String[] arrayOfString = new String[3];
        arrayOfString[0] = this.uinLabel;
        arrayOfString[1] = this.serialNumber.getUin();
        arrayOfString[2] = this.serialNumber.getStatus().toString();
        throw new BusinessException(UINMessageText.UIN_CANNOT_BE_RECEIVED, arrayOfString);
      } 
      if (uINStatus == UINStatus.SHIPPED_TO_STORE) {
        if (this.storeId.equals(this.serialNumber.getStoreId())) {
          String[] arrayOfString1 = new String[3];
          arrayOfString1[0] = this.uinLabel;
          arrayOfString1[1] = this.serialNumber.getUin();
          throw new BusinessException(UINMessageText.UIN_NOT_RECEIVABLE, arrayOfString1);
        } 
        String[] arrayOfString = new String[3];
        arrayOfString[0] = this.uinLabel;
        arrayOfString[1] = this.serialNumber.getUin();
        arrayOfString[2] = this.serialNumber.getStatus().toString();
        throw new BusinessException(UINMessageText.UIN_AT_ANOTHER_STORE, arrayOfString);
      } 
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryValidateUINCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */