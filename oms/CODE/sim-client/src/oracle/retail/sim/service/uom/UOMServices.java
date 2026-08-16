package oracle.retail.sim.service.uom;

import java.math.BigDecimal;

public abstract class UOMServices {
  public abstract BigDecimal findUOMConversionFactor(String paramString1, String paramString2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\servic\\uom\UOMServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */