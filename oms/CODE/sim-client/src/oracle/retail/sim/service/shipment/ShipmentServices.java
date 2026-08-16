package oracle.retail.sim.service.shipment;

import java.util.List;
import oracle.retail.sim.common.shipment.BillOfLadingMotive;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import oracle.retail.sim.common.shipment.ShipmentCartonType;
import oracle.retail.sim.common.shipment.ShipmentType;
import oracle.retail.sim.common.shipment.ShipmentWeightUom;

public abstract class ShipmentServices {
  public abstract List<ShipmentCarrier> findAllCarriers() throws Exception;
  
  public abstract ShipmentCarrier findCarrier(String paramString) throws Exception;
  
  public abstract List<ShipmentCarrierService> findAllCarrierServices() throws Exception;
  
  public abstract List<ShipmentCarrierService> findCarrierServices(Long paramLong) throws Exception;
  
  public abstract List<ShipmentCartonType> findCartonTypes(Long paramLong) throws Exception;
  
  public abstract ShipmentCartonType findCartonType(Long paramLong) throws Exception;
  
  public abstract List<ShipmentWeightUom> findAllWeightUoms() throws Exception;
  
  public abstract List<BillOfLadingMotive> findMotives(ShipmentType paramShipmentType) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\shipment\ShipmentServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */