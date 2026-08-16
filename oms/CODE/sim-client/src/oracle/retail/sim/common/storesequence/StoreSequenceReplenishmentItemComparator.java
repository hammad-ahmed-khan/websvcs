package oracle.retail.sim.common.storesequence;

import java.util.Comparator;

public class StoreSequenceReplenishmentItemComparator implements Comparator<StoreSequenceReplenishmentItem> {
  public int compare(StoreSequenceReplenishmentItem paramStoreSequenceReplenishmentItem1, StoreSequenceReplenishmentItem paramStoreSequenceReplenishmentItem2) {
    Integer integer1 = Integer.valueOf(paramStoreSequenceReplenishmentItem1.getStoreOrder());
    Integer integer2 = Integer.valueOf(paramStoreSequenceReplenishmentItem2.getStoreOrder());
    int i = integer1.compareTo(integer2);
    if (i == 0) {
      integer1 = Integer.valueOf(paramStoreSequenceReplenishmentItem1.getItemOrder());
      integer2 = Integer.valueOf(paramStoreSequenceReplenishmentItem2.getItemOrder());
      i = integer1.compareTo(integer2);
    } 
    return i;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceReplenishmentItemComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */