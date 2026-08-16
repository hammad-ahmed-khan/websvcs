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

public interface ClientCommandFactoryInterface {
  DirectDeliveryValidateUinCommand createDirectDeliveryValidateUINCommand();
  
  FulfillmentOrderDeliveryCreateCommand createFulfillmentOrderDeliveryCreateForFulfillmentOrderCommand();
  
  FulfillmentOrderDeliveryValidateUINCommand createFulfillmentOrderDeliveryValidateUINCommand();
  
  InventoryAdjustmentValidateUINCommand createInventoryAdjustmentValidateUINCommand();
  
  ReturnValidateUINCommand createReturnValidateUINCommand();
  
  TransferValidateUinCommand createTransferValidateUINCommand();
  
  UserPasswordValidationCommand createUserPasswordValidationCommand();
  
  WarehouseDeliveryValidateUINCommand createWarehouseDeliveryValidateUINCommand();
  
  FulfillmentOrderReversePickCreateCommand createFulfillmentOrderReversePickCreateCommand();
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\ClientCommandFactoryInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */