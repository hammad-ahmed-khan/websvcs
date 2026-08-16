package oracle.retail.sim.client.displayer;

import java.util.Comparator;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.stockcount.StockCountChild;

public class StockCountChildComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        try {
            StockCountChild value1 = (StockCountChild) o1;
            StockCountChild value2 = (StockCountChild) o2;
            if (value1 == null && value2 == null) {
                return 0;
            } else if (value1 == null) {
                return -1;
            } else if (value2 == null) {
                return 1;
            }
            Long number1 = value1.getId();
            Long number2 = value2.getId();
            if (number1 == null && number2 == null) {
                return 0;
            } else if (number1 == null) {
                return -1;
            } else if (number2 == null) {
                return 1;
            }

            return number1.compareTo(number2);
        } catch (Throwable exception) {
            UILog.debug(getClass(), UIMessageText.COMPARATOR_FAILURE, exception);
            return 0;
        }
    }
}
