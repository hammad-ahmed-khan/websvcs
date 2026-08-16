package oracle.retail.sim.common.source;

import java.util.Comparator;

public class ContextTypeComparator implements Comparator<ContextType> {
  private static ContextTypeComparator singleton;
  
  public static ContextTypeComparator getInstance() {
    if (singleton == null)
      singleton = new ContextTypeComparator(); 
    return singleton;
  }
  
  public int compare(ContextType paramContextType1, ContextType paramContextType2) {
    return (paramContextType1 == paramContextType2) ? 0 : ((paramContextType1 == null) ? -1 : ((paramContextType2 == null) ? 1 : paramContextType1.compareTo(paramContextType2)));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\ContextTypeComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */