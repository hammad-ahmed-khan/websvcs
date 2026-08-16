package oracle.retail.sim.common.lineitem;

import java.util.HashSet;
import java.util.Set;

public class UOMConstants {
  public static final String EACHES = "EA";
  
  public static final String PIECES = "PCS";
  
  public static final String UNITS = "Units";
  
  public static final String CASES = "Cases";
  
  public static final String STANDARD_UOM = "Standard UOM";
  
  public static final String PREFERRED = "Transaction";
  
  public static final String KILOGRAM = "KG";
  
  public static final String POUNDS = "LB";
  
  public static Set<String> getUnitUOMs() {
    HashSet<String> hashSet = new HashSet();
    hashSet.add("EA");
    hashSet.add("Units");
    hashSet.add("PCS");
    return hashSet;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\lineitem\UOMConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */