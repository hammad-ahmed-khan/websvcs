package oracle.retail.sim.common.itembasket;

import java.util.Comparator;
import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;

public class ItemBasketTypeComparator implements Comparator<ItemBasketType> {
  private StringHelper stringHelper = null;
  
  public ItemBasketTypeComparator() {
    this.stringHelper = StringHelper.getInstance();
    if (this.stringHelper == null)
      this.stringHelper = StringHelper.getInstance(Locale.US); 
  }
  
  public int compare(ItemBasketType paramItemBasketType1, ItemBasketType paramItemBasketType2) {
    if (paramItemBasketType1 == null)
      return -1; 
    if (paramItemBasketType2 == null)
      return 1; 
    String str1 = StringHelper.trimToNull(paramItemBasketType1.toDisplayString());
    String str2 = StringHelper.trimToNull(paramItemBasketType2.toDisplayString());
    return (str1 == null) ? -1 : ((str2 == null) ? 1 : this.stringHelper.compareToIgnoreCase(str1, str2));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itembasket\ItemBasketTypeComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */