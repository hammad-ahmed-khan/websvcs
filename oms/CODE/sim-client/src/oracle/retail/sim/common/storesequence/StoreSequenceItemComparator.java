package oracle.retail.sim.common.storesequence;

import java.util.Comparator;

public class StoreSequenceItemComparator implements Comparator {
  public int compare(Object paramObject1, Object paramObject2) {
    Integer integer1 = Integer.valueOf(((StoreSequenceItem)paramObject1).getOrder());
    Integer integer2 = Integer.valueOf(((StoreSequenceItem)paramObject2).getOrder());
    return integer1.compareTo(integer2);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceItemComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */