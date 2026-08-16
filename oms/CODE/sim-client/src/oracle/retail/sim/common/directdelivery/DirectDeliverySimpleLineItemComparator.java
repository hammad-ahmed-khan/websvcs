package oracle.retail.sim.common.directdelivery;

import java.util.Comparator;
import oracle.retail.sim.common.util.NumericOrderStringComparator;

public class DirectDeliverySimpleLineItemComparator implements Comparator<DirectDeliverySimpleLineItem> {
  private final NumericOrderStringComparator comparator = new NumericOrderStringComparator(true, false);
  
  public int compare(DirectDeliverySimpleLineItem paramDirectDeliverySimpleLineItem1, DirectDeliverySimpleLineItem paramDirectDeliverySimpleLineItem2) {
    if (paramDirectDeliverySimpleLineItem1 == null && paramDirectDeliverySimpleLineItem2 == null)
      return 0; 
    if (paramDirectDeliverySimpleLineItem1 == null)
      return -1; 
    if (paramDirectDeliverySimpleLineItem2 == null)
      return 1; 
    int i = this.comparator.compare(paramDirectDeliverySimpleLineItem1.getStockItem().getId(), paramDirectDeliverySimpleLineItem2.getStockItem().getId());
    if (i != 0)
      return i; 
    DirectDeliveryCarton directDeliveryCarton1 = paramDirectDeliverySimpleLineItem1.getCarton();
    DirectDeliveryCarton directDeliveryCarton2 = paramDirectDeliverySimpleLineItem2.getCarton();
    return (directDeliveryCarton1 == null && directDeliveryCarton2 == null) ? 0 : ((directDeliveryCarton1 == null) ? -1 : ((directDeliveryCarton2 == null) ? 1 : this.comparator.compare(directDeliveryCarton1.getExternalId(), directDeliveryCarton2.getExternalId())));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliverySimpleLineItemComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */