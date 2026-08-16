package oracle.retail.sim.service.deals;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.deals.Deal;

public abstract class DealServices {
  public abstract List<Deal> findDeals(Long paramLong, String paramString1, String paramString2, Date paramDate) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\deals\DealServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */