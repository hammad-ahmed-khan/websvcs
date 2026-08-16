package oracle.retail.sim.common.business;

import oracle.retail.sim.common.configutil.CommonConfigManager;
import oracle.retail.sim.common.directdelivery.DirectDeliveryValidateUinCommand;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryCreateCommand;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryValidateUINCommand;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickCreateCommand;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentValidateUINCommand;
import oracle.retail.sim.common.security.UserPasswordValidationCommand;
import oracle.retail.sim.common.stockreturn.ReturnValidateUINCommand;
import oracle.retail.sim.common.transfer.TransferValidateUinCommand;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryValidateUINCommand;

public class ClientCommandFactory {
  private static ClientCommandFactoryInterface factory = getDefaultFactory();
  
  private static ClientCommandFactoryInterface getDefaultFactory() {
    return CommonConfigManager.getClientCommandFactoryImpl();
  }
  
  public static DirectDeliveryValidateUinCommand createDirectDeliveryValidateUINCommand() {
    return factory.createDirectDeliveryValidateUINCommand();
  }
  
  public static FulfillmentOrderDeliveryCreateCommand createFulfillmentOrderDeliveryCreateForFulfillmentOrderCommand() {
    return factory.createFulfillmentOrderDeliveryCreateForFulfillmentOrderCommand();
  }
  
  public static FulfillmentOrderDeliveryValidateUINCommand createFulfillmentOrderDeliveryValidateUINCommand() {
    return factory.createFulfillmentOrderDeliveryValidateUINCommand();
  }
  
  public static InventoryAdjustmentValidateUINCommand createInventoryAdjustmentValidateUINCommand() {
    return factory.createInventoryAdjustmentValidateUINCommand();
  }
  
  public static ReturnValidateUINCommand createReturnValidateUINCommand() {
    return factory.createReturnValidateUINCommand();
  }
  
  public static TransferValidateUinCommand createTransferValidateUINCommand() {
    return factory.createTransferValidateUINCommand();
  }
  
  public static UserPasswordValidationCommand createUserPasswordValidationCommand() {
    return factory.createUserPasswordValidationCommand();
  }
  
  public static WarehouseDeliveryValidateUINCommand createWarehouseDeliveryValidateUINCommand() {
    return factory.createWarehouseDeliveryValidateUINCommand();
  }
  
  public static FulfillmentOrderReversePickCreateCommand createFulfillmentOrderReversePickCreateCommand() {
    return factory.createFulfillmentOrderReversePickCreateCommand();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\ClientCommandFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */