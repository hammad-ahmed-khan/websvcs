package oracle.retail.sim.service.ejb;

import java.math.BigDecimal;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

@Remote
public interface UOMInterface {
  CompressedObject<BigDecimal> findUOMConversionFactor(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\UOMInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */