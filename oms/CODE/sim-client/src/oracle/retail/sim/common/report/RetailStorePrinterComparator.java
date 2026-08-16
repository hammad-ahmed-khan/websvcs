package oracle.retail.sim.common.report;

import java.util.Comparator;
import oracle.retail.sim.common.core.locale.StringHelper;

public class RetailStorePrinterComparator implements Comparator<StorePrinter> {
  private StringHelper stringHelper = null;
  
  public RetailStorePrinterComparator() {
    this.stringHelper = StringHelper.getInstance();
  }
  
  public int compare(StorePrinter paramStorePrinter1, StorePrinter paramStorePrinter2) {
    return this.stringHelper.compareTo(paramStorePrinter1.getDescription(), paramStorePrinter2.getDescription());
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\report\RetailStorePrinterComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */