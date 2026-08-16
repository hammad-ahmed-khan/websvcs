package oracle.retail.sim.common.core.locale;

import java.text.CollationKey;
import java.util.Comparator;

public class StringCollatorComparator implements Comparator<CollationKey> {
  public int compare(CollationKey paramCollationKey1, CollationKey paramCollationKey2) {
    return paramCollationKey1.compareTo(paramCollationKey2);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\locale\StringCollatorComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */