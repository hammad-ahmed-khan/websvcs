package oracle.retail.sim.common.source;

import java.util.Comparator;

public class WarehouseComparator implements Comparator<Warehouse> {
  private static WarehouseComparator singleton;
  
  public static WarehouseComparator getInstance() {
    if (singleton == null)
      singleton = new WarehouseComparator(); 
    return singleton;
  }
  
  public int compare(Warehouse paramWarehouse1, Warehouse paramWarehouse2) {
    return (paramWarehouse1 == paramWarehouse2) ? 0 : ((paramWarehouse1 == null) ? -1 : ((paramWarehouse2 == null) ? 1 : paramWarehouse1.compareTo(paramWarehouse2)));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\WarehouseComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */