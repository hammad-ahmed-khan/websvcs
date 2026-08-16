package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.shipment.BillOfLadingMotive;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import oracle.retail.sim.common.shipment.ShipmentCartonType;
import oracle.retail.sim.common.shipment.ShipmentType;
import oracle.retail.sim.common.shipment.ShipmentWeightUom;

@Remote
public interface ShipmentInterface {
  CompressedObject<List<ShipmentCarrierService>> findAllCarrierServices(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<ShipmentCarrier>> findAllCarriers(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<ShipmentWeightUom>> findAllWeightUoms(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<ShipmentCarrier> findCarrier(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ShipmentCarrierService>> findCarrierServices(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ShipmentCartonType> findCartonType(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ShipmentCartonType>> findCartonTypes(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<BillOfLadingMotive>> findMotives(CompressedObject<ShipmentType> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ShipmentInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */