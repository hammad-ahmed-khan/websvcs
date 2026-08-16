package oracle.retail.sim.common.business;

import oracle.retail.sim.common.directdelivery.DirectDeliveryValidateUinCommand;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryCreateCommand;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryValidateUINCommand;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickCreateCommand;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentValidateUINCommand;
import oracle.retail.sim.common.security.UserPasswordValidationCommand;
import oracle.retail.sim.common.stockreturn.ReturnValidateUINCommand;
import oracle.retail.sim.common.transfer.TransferValidateUinCommand;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryValidateUINCommand;

public class ClientCommandFactoryImpl implements ClientCommandFactoryInterface {
  public DirectDeliveryValidateUinCommand createDirectDeliveryValidateUINCommand() {
    return new DirectDeliveryValidateUinCommand();
  }
  
  public FulfillmentOrderDeliveryCreateCommand createFulfillmentOrderDeliveryCreateForFulfillmentOrderCommand() {
    return new FulfillmentOrderDeliveryCreateCommand();
  }
  
  public FulfillmentOrderDeliveryValidateUINCommand createFulfillmentOrderDeliveryValidateUINCommand() {
    return new FulfillmentOrderDeliveryValidateUINCommand();
  }
  
  public InventoryAdjustmentValidateUINCommand createInventoryAdjustmentValidateUINCommand() {
    return new InventoryAdjustmentValidateUINCommand();
  }
  
  public ReturnValidateUINCommand createReturnValidateUINCommand() {
    return new ReturnValidateUINCommand();
  }
  
  public TransferValidateUinCommand createTransferValidateUINCommand() {
    return new TransferValidateUinCommand();
  }
  
  public UserPasswordValidationCommand createUserPasswordValidationCommand() {
    return new UserPasswordValidationCommand();
  }
  
  public WarehouseDeliveryValidateUINCommand createWarehouseDeliveryValidateUINCommand() {
    return new WarehouseDeliveryValidateUINCommand();
  }
  
  public FulfillmentOrderReversePickCreateCommand createFulfillmentOrderReversePickCreateCommand() {
    return new FulfillmentOrderReversePickCreateCommand();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\ClientCommandFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */