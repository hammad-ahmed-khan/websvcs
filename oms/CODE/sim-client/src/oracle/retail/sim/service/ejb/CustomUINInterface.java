package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.customuin.CustomSerialNumber;

@Remote
public interface CustomUINInterface {
  CompressedObject<?> moveSerialNumbersToInStock(CompressedObject<Long> paramCompressedObject, CompressedObject<List<CustomSerialNumber>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\CustomUINInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */